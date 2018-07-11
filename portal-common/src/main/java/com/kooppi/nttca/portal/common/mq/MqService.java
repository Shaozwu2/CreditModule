package com.kooppi.nttca.portal.common.mq;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.kooppi.nttca.cm.common.mail.representation.CreateEmailRequest;
import com.kooppi.nttca.cm.common.mail.representation.WS_Email;
import com.kooppi.nttca.portal.common.config.file.ConfigurationValue;
import com.kooppi.nttca.portal.common.constant.PortalConstant;
import com.kooppi.nttca.portal.common.utils.JAXBUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;

@Stateless
public class MqService {

    private Logger logger = LoggerFactory.getLogger(MqService.class);

    @Inject
    @ConfigurationValue(property = "portal.mq.status.on", defaultValue = "false")
    private Boolean isStatusOn;
    
    @Inject
    @ConfigurationValue(property = "portal.mq.exchange.email.name", defaultValue = "portal-email-pub-bcast")
    private String emailExchangeName;

    @Inject
    @ConfigurationValue(property = "portal.mq.exchange.profile.name", defaultValue = "portal-email-pub-bcast")
    private String profileExchangeName;
    
    @Inject
    @ConfigurationValue(property = "portal.mq.exchange.audit.log.name", defaultValue = "portal-audit-log-pub-bcast")
    private String auditLogExchangeName;
    
    @Inject
    @ConfigurationValue(property = "portal.wallet.app.id", defaultValue = "portal-wallet")
    private String appId;
    
    @Inject
    @ConfigurationValue(property = "portal.mq.uri", defaultValue = "amqp://guest:guest@192.168.12.248:5672")
    private String uri;
    
    @Inject
    @MqComponent
    private MqConnection rabbitConnection;

    public MqService() {
    }

    @PostConstruct
    public void init() {
        logger.debug(" uri: " + uri + " is MQ On: " + isStatusOn + " profileExchangeName: " + profileExchangeName + " emailExchangeName: " + emailExchangeName);
    }

    public void sentEmail(Object message) throws Exception {
        logger.info("sentEmail");
        String json = JAXBUtil.toJson(message);
        this.sent(json, "", "/emails/add", "/v1", emailExchangeName);
    }

    public void sentAuditLog(Object message) throws Exception {
        logger.info("sentAuditLog");
        Gson gson = new Gson();
        String json = gson.toJson(message);
        this.sent(json, "", "/auditLogs/add", "/v1", auditLogExchangeName);
    }

    public void sent(Object message, String routingKey) throws Exception {
        String json = JAXBUtil.toJson(message);
        this.sent(json, routingKey, null, null, profileExchangeName);
    }

    public void sent(String message, String routingKey) throws Exception {
        this.sent(message, routingKey, null, null, profileExchangeName);
    }

    private void sent(String message, String routingKey, String api, String version, String exchangeName) throws Exception {
        logger.debug("exchangename: " + exchangeName + " uri: " + uri + " routingKey: " + routingKey + " is MQ On: " + isStatusOn);
        if (isStatusOn) {
            Channel channel = null;
            try {
                channel = rabbitConnection.createChannel();

                logger.info("Created a new rabbitMQ channel: " + channel.getChannelNumber());

                if (Strings.isNullOrEmpty(routingKey)) {
                    channel.exchangeDeclare(exchangeName, "fanout", true);
                    logger.debug("fanout");
                } else {
                    channel.exchangeDeclare(exchangeName, "topic", true);
                }
                Map<String, Object> headers = new HashMap<String, Object>();
                headers.put(PortalConstant.PORTAL_MQ_APP_ID, appId);
                headers.put(PortalConstant.PORTAL_MQ_REQUEST_ID, UUID.randomUUID().toString());
                if (!Strings.isNullOrEmpty(api)) {
                    headers.put(PortalConstant.PORTAL_MQ_API, api);
                }
                if (!Strings.isNullOrEmpty(version)) {
                    headers.put(PortalConstant.PORTAL_MQ_VERSION, version);
                }

                headers.put(PortalConstant.PORTAL_MQ_SERVICE, appId);
                BasicProperties msgProperties = new AMQP.BasicProperties.Builder().contentType(MediaType.APPLICATION_JSON).headers(headers)
                        .build();

                channel.basicPublish(exchangeName, routingKey, msgProperties, message.getBytes("UTF-8"));

                logger.debug(" [x] Sent '" + routingKey + "':'" + message + "'");

            } catch (Exception e) {
                logger.error("Cannot create a channel" + e);
                throw e;
            }finally {
            		channel.close();
			}
        }
    }
    
    public static void main(String args[]){
    	CreateEmailRequest test = new CreateEmailRequest();
    	test.setEmail(new WS_Email());
    	test.setQueuePriority(1);
    	Object ob = test;
    	String str = JAXBUtil.toJson(ob);
    	System.out.println(str);
    }
}
