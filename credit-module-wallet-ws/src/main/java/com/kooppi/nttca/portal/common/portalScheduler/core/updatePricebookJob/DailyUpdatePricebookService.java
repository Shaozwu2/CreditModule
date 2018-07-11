package com.kooppi.nttca.portal.common.portalScheduler.core.updatePricebookJob;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.kooppi.nttca.portal.common.config.file.ConfigurationValue;
import com.kooppi.nttca.wallet.rest.priceBook.service.PriceBookService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

@Stateless
public class DailyUpdatePricebookService {

	private static final Logger logger = LoggerFactory.getLogger(DailyUpdatePricebookService.class);

	@Inject
	@ConfigurationValue(property = "portal.mq.status.on", defaultValue = "false")
	private Boolean isStatusOn;

	@Inject
	@ConfigurationValue(property = "portal.mq.queue.credit.name", defaultValue = "portal-credit")
	private String queueName;

	@Inject
	@ConfigurationValue(property = "portal.mq.exchange.credit.name", defaultValue = "portal-credit-bcast")
	private String exchangeName;

	@Inject
	@ConfigurationValue(property = "portal.mq.uri", defaultValue = "amqp://admin:admin@192.168.12.248:5672")
	private String uri;

	@Inject
	private PriceBookService priceBookService;

	private static final boolean SERVER_AUTO_ACK = false;
	private boolean autoRecovery = true;
	private int maxNumberOfThreads = 20;
	private int prefetchCount = 0;
	private int recoveryInterval = 60;
	private int connectionTimeout = 5000;
	private ExecutorService eService = null;
	private ExecutorService mqEService = null;
	private Thread thread = null;
	private volatile boolean initConnectionFailed = false;
	private Connection connection = null;
	private String virtualHost = "credit_module";
	
	List<String> consumedMsg = Lists.newArrayList();

	public void initConstant() {
		try {
			// this.priceBookService = priceBookService;
			logger.info("read application.properties");
			logger.info("queueName: " + queueName + " maxNumberOfThreads: " + maxNumberOfThreads + " prefetchCount: "
					+ prefetchCount);
			eService = Executors.newSingleThreadExecutor();
			createMqConnection();
			invoke();
		} catch (IOException e) {
			logger.error("Cannot connect to the host: " + e);
			initConnectionFailed = true;
			restartMqConnection();
		} catch (Exception e) {
			logger.error("Cannot read application.properties or missing properties! " + e);
		}
	}

	private void createMqConnection()
			throws KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException, TimeoutException {
		logger.info("Creating Mq Connection");
		mqEService = Executors.newFixedThreadPool(maxNumberOfThreads);
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUri(uri + "/" + virtualHost);
		factory.setAutomaticRecoveryEnabled(autoRecovery);
		factory.setNetworkRecoveryInterval(recoveryInterval);
		factory.setConnectionTimeout(connectionTimeout);
		connection = factory.newConnection(mqEService);
		logger.info("Created Mq Connection");
	}

	private void restartMqConnection() {
		thread = new Thread() {
			@Override
			public void run() {
				try {
					while (initConnectionFailed) {
						logger.info(Thread.currentThread().getName() + " Rabbit MQ start process will be triggered in "
								+ recoveryInterval + " seconds...");
						Thread.sleep(recoveryInterval * 1000);
						try {
							createMqConnection();
							initConnectionFailed = false;
							invoke();
							logger.info("Restart RabbitMQ process finished.");
						} catch (KeyManagementException | NoSuchAlgorithmException | URISyntaxException | IOException
								| TimeoutException e) {
							logger.error("Restart RabbitMQ process failed." + e);
						}
					}
				} catch (InterruptedException e) {
					logger.error("mq connection interrupted: " + e);
				}
			}
		};
		eService.execute(thread);
	}

	public void invoke() throws IOException {
		logger.info("invoke channels and consumer");
		Channel channel = createChannel();
//		channel.basicQos(prefetchCount);

		while (true) {
			logger.info("getting message");

			GetResponse response = channel.basicGet(queueName, true);
			if (response != null) {
			    String message = new String(response.getBody(), "UTF-8");
			    logger.info(" [x] Received '" + message + "'");
			    priceBookService.createOrUpdatePricebookFromEms(message);
			  
			} else {
				logger.info("no more message");
				cleanUp();
				break;
			}
		}

//		Consumer consumer = new DefaultConsumer(channel) {
//			@Override
//			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
//					byte[] body) throws IOException {
//				long deliveryTag = envelope.getDeliveryTag();
//				String message = new String(body, "UTF-8");
//				logger.info(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
//				channel.basicAck(deliveryTag, false);
//				consumedMsg.add(message);
//				logger.info("Size :" + consumedMsg.size());
//
//			}
//		};
//		logger.info("############# Size :" + consumedMsg.size());

//		channel.basicConsume(queueName, SERVER_AUTO_ACK, new QueueConsumer(channel, priceBookService));

	}

	@PreDestroy
	public void cleanUp() {
		try {
			logger.info("############# Size :" + consumedMsg.size());
			for (String msg : consumedMsg) {
				logger.info("###########123123123123## msg  :" + msg);

				priceBookService.createOrUpdatePricebookFromEms(msg);
			}
			logger.info("Clean up rabbitMQ connection.");
			if (eService != null) {
				logger.info("Closing rabbitMQ restart thread pool.");
				eService.shutdownNow();
				if (!eService.awaitTermination(10, TimeUnit.MICROSECONDS)) {
					logger.info("Still waiting....");
				}
				logger.info("Exited rabbitMQ restart thread pool.");
			}
			close();
			
			
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException("Error occur during pre-destroy rabbitMQ conncetion.", e);
		}
	}

	private Channel createChannel() throws IOException {
		logger.info("Create a new rabbitMQ channel.");
		return connection.createChannel();
	}

	private void close() throws IOException {
		logger.info("Close rabbitMQ connection.");
		connection.close();
	}

}
