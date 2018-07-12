package com.kooppi.nttca.portal.common.mq.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kooppi.nttca.portal.common.config.file.ConfigurationValue;
import com.kooppi.nttca.wallet.rest.priceBook.service.PriceBookService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@Stateless
public class MqMessageConsumer {

	private static final Logger logger = LoggerFactory.getLogger(MqMessageConsumer.class);

	@Inject
	@ConfigurationValue(property = "ems.mq.status.on", defaultValue = "false")
	private Boolean isStatusOn;

	@Inject
	@ConfigurationValue(property = "ems.mq.queue.credit.name", defaultValue = "portal-credit")
	private String queueName;

	@Inject
	@ConfigurationValue(property = "ems.mq.exchange.credit.name", defaultValue = "portal-credit-bcast")
	private String exchangeName;

	@Inject
	@ConfigurationValue(property = "ems.mq.uri", defaultValue = "amqp://admin:admin@192.168.12.248:5672")
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

//	public static void main(String[] args) {
//		ConnectionFactory factory = new ConnectionFactory();
//		System.err.println("estart");
//		try {
//			// factory.setUri("amqp://admin:admin@192.168.12.248:5672"+"/"+"credit-module");
//			factory.setAutomaticRecoveryEnabled(true);
//			factory.setUri("amqp://admin:admin@192.168.12.248:5672/credit_module");
//			factory.setNetworkRecoveryInterval(60);
//			factory.setConnectionTimeout(5000);
//			ExecutorService mqEService = Executors.newFixedThreadPool(20);
//			Connection connection = factory.newConnection(mqEService);
//			Channel channel = connection.createChannel();
//			// logger.info("prefetchCount : " + prefetchCount);
//			channel.basicQos(0);
//			channel.basicConsume("portal-credit", SERVER_AUTO_ACK, new QueueConsumer(channel, null, null));
//			System.err.println("finish");
//
//		} catch (KeyManagementException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (TimeoutException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}

	// @PostConstruct
	public void initConstant() {
		try {
//			this.priceBookService = priceBookService;
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
		// for (int i = 0; i < maxNumberOfThreads; i++) {
		// logger.info("Creating channels and consumer : " + i);
		Channel channel = createChannel();
		// logger.info("prefetchCount : " + prefetchCount);
		channel.basicQos(prefetchCount);
		
		
//		List<String> msgList = Lists.newArrayList();
//		logger.debug("msgList szie:" + msgList.size());

//		Consumer consumer = new DefaultConsumer(channel) {
//		      @Override
//		      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
//		          throws IOException {
//				logger.debug("handleDelivery");
//				long deliveryTag = envelope.getDeliveryTag();
//		        String message = new String(body, "UTF-8");
//		        System.out.println(" [x] Received '" + message + "'");
//
//				logger.debug("Channel :" + channel + " Channel No: " + channel.getChannelNumber() + " Thread:"
//						+ Thread.currentThread() + " message:" + message);
//				logger.debug("Thread Name: " + Thread.currentThread().getName());
//				logger.debug("[x] <================================================== Received");
//				logger.debug("Body:");
//				logger.debug("  " + message);
//				msgList.add(message);
//				logger.debug("msgList szie:" + msgList.size());
//				channel.basicAck(deliveryTag, false);
//		      }
//		    };
//		    channel.basicConsume(queueName, SERVER_AUTO_ACK, consumer);
//			logger.debug("msgList szie:" + msgList.size());
//		for (String msg : msgList) {
//			logger.debug("msg:");
//
//			priceBookService.createOrUpdatePricebookFromEms(msg);
//		}
		channel.basicConsume(queueName, SERVER_AUTO_ACK, new QueueConsumer(channel, priceBookService));
		// }
	}

	@PreDestroy
	public void cleanUp() {
		try {
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
