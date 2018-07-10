package com.kooppi.nttca.portal.common.mq.service;

import java.io.IOException;

import com.kooppi.nttca.wallet.rest.priceBook.service.PriceBookService;
import com.kooppi.nttca.wallet.rest.report.service.ReportService;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueueConsumer extends DefaultConsumer {

	private static final Logger logger = LoggerFactory.getLogger(QueueConsumer.class);

	private Channel channel;

	private PriceBookService priceBookService;

	public QueueConsumer(Channel channel, PriceBookService priceBookService) {
		super(channel);
		logger.info("Creating QueueConsumer.....");
		this.channel = channel;
		this.priceBookService = priceBookService;
	}

	@Override
	public void handleDelivery(String consumeTag, Envelope envelope, AMQP.BasicProperties msgProperties, byte[] body)
			throws IOException {
		logger.debug("handleDelivery");
//		priceBookService.getAllPriceBooks();
		long deliveryTag = envelope.getDeliveryTag();
		String message = new String(body);
		logger.debug("Channel :" + channel + " Channel No: " + channel.getChannelNumber() + " Thread:"
				+ Thread.currentThread() + " message:" + message);
		logger.debug("Thread Name: " + Thread.currentThread().getName());
		logger.debug("[x] <================================================== Received");
		logger.debug("Body:");
		logger.debug("  " + message);
		priceBookService.createOrUpdatePricebookFromEms(message);

		this.channel.basicAck(deliveryTag, false);
	}

}
