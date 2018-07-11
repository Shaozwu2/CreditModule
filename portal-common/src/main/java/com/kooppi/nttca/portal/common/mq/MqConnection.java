package com.kooppi.nttca.portal.common.mq;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MqConnection {
	
	private volatile Connection connection;
	
	private ExecutorService eService = null;
		
	private final static Logger logger = LoggerFactory.getLogger(MqConnection.class);
	
	MqConnection() {}
	
	public MqConnection(String uri, boolean recoveryChannel, int maxThread, int recoveryInterval) throws IOException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException, TimeoutException {
		logger.info("Create rabbitMQ conncetion uri: " + uri);
		eService = Executors.newFixedThreadPool(maxThread);
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUri(uri);
		factory.setAutomaticRecoveryEnabled(recoveryChannel);
		factory.setNetworkRecoveryInterval(recoveryInterval);
		connection = factory.newConnection(eService);
	}

	
	public Channel createChannel() throws IOException {
		logger.info("Create rabbitMQ new channel.");
		return connection.createChannel();
	}
	
	public void close() throws IOException {
		logger.info("Close rabbitMQ connection.");
		if (connection != null){
			connection.close();
		}
		if (eService != null) {
			eService.shutdown();
		}
	}

}