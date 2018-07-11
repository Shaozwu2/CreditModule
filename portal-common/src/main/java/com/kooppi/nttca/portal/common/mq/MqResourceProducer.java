package com.kooppi.nttca.portal.common.mq;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kooppi.nttca.portal.common.config.file.ConfigurationValue;
import com.rabbitmq.client.Channel;

@ApplicationScoped
public class MqResourceProducer {

	private static final Logger logger = LoggerFactory.getLogger(MqResourceProducer.class);
	
    @Inject
    @ConfigurationValue(property = "portal.mq.uri", defaultValue = "amqp://guest:guest@192.168.12.248:5672")
    private String uri;
    
    @Inject
    @ConfigurationValue(property = "portal.mq.max.thread", defaultValue = "20")
    private Integer maxThread;
    
    @Inject
    @ConfigurationValue(property = "portal.mq.recovey.interval", defaultValue = "5000")
    private Integer recoveryInterval;

    public MqResourceProducer() {
        logger.info("MqResourceProvider init");
    }

    @Produces
    @MqComponent
    public MqConnection createRabbitMqConnection() throws KeyManagementException, NoSuchAlgorithmException, IOException, URISyntaxException, TimeoutException {
    	logger.info("Creating rabbitMQ conncetion.");
        return new MqConnection(uri, true, maxThread, recoveryInterval);
    }

    public void cleanUpRabbitMqConnection(@Disposes @MqComponent MqConnection rabbitConnection) throws IOException {
    	logger.info("Clean up rabbitMQ conncetion.");
        rabbitConnection.close();
        logger.info("Clean up rabbitMQ conncetion finished.");
    }
    
	public static void main(String[] args) throws IOException {
		MqConnection mqConnection = null;
		try {
			mqConnection = new MqConnection("amqp://credit_user:!QAZ2wsx@127.0.0.1:56005/credit_module", true, 20, 5000);
			Channel channel = mqConnection.createChannel();
			
			
			String message = "Hello World!";
			channel.queueDeclare("portal_credit", false, false, false, null);
			channel.basicPublish("portal-credit-bcast", "", null, message.getBytes());
			System.out.println(" [x] Sent '" + message + "'");
		} catch (KeyManagementException | NoSuchAlgorithmException | IOException | URISyntaxException | TimeoutException e) {
			e.printStackTrace();
		} finally {
			if(mqConnection != null) {
				mqConnection.close();
			}
		}
	}
}
