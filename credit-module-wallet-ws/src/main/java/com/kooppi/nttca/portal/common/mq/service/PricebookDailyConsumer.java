package com.kooppi.nttca.portal.common.mq.service;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.kooppi.nttca.portal.common.config.file.ConfigurationValue;
import com.kooppi.nttca.portal.common.rabbitmq.RabbitConnection;
import com.kooppi.nttca.wallet.rest.priceBook.service.PriceBookService;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

//@Singleton
//@Startup
public class PricebookDailyConsumer implements Consumer{
	private static final Logger logger = LoggerFactory.getLogger(MqMessageConsumer.class);
	
	private RabbitConnection rabbitConnection;

	private String serviceRequestQueue;
	private Integer requestPrefetchCount = 0;
	private Integer requestThreadCount = 20;
	private boolean requestAutoAck = true;
	private Map<String, Channel> channelMap = Maps.newHashMap();

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
	@ConfigurationValue(property = "portal.mq.hostname", defaultValue = "192.168.12.248")
	private String uri;

	@Inject
	@ConfigurationValue(property = "portal.mq.port", defaultValue = "5672")
	private String port;
	
	@Inject
	@ConfigurationValue(property = "portal.mq.username", defaultValue = "admin")
	private String rabbitMqUsername;
	
	@Inject
	@ConfigurationValue(property = "portal.mq.password", defaultValue = "admin")
	private String rabbitMqPassword;
	
	private static String virtualHost = "credit_module";
	

	private PriceBookService priceBookService;
	
	@Inject
	public PricebookDailyConsumer(PriceBookService priceBookService) throws IOException {
		this.priceBookService = priceBookService;
		this.rabbitConnection = new RabbitConnection(uri, virtualHost, Integer.valueOf(port), rabbitMqUsername, rabbitMqPassword, true);
	}

    @PostConstruct
    public void start() {
    	logger.info("Start consuming the message from queue ({}) ", this.serviceRequestQueue);
    	try {
			// Create the channel and then consume it.
			for (int i = 0; i < this.requestThreadCount; i++) {
				Channel channel = this.rabbitConnection.createChannel();
				if (this.requestPrefetchCount != null) {
					channel.basicQos(requestPrefetchCount, false);
				}
				String consumerTag = channel.basicConsume(serviceRequestQueue, this.requestAutoAck, this);
				this.channelMap.put(consumerTag, channel);
			}
		} catch (Exception e) {
			logger.warn("The BhecRequestConsumer fail to start .It may cause the request queue message cannot be processed" , e);
		}
    }
    
	@Override
	public void handleConsumeOk(String consumerTag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleCancelOk(String consumerTag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleCancel(String consumerTag) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleDelivery(String arg0, Envelope arg1, BasicProperties arg2, byte[] arg3) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleRecoverOk(String consumerTag) {
		// TODO Auto-generated method stub
		
	}

}
