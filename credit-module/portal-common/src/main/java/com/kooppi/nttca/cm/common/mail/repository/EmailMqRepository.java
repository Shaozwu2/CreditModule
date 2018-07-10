package com.kooppi.nttca.cm.common.mail.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kooppi.nttca.cm.common.mail.representation.CreateEmailRequest;
import com.kooppi.nttca.cm.common.mail.representation.WS_Email;
import com.kooppi.nttca.portal.common.config.file.ConfigurationValue;
import com.kooppi.nttca.portal.common.constant.PortalConstant;
import com.kooppi.nttca.portal.common.mq.MqService;
import com.kooppi.nttca.portal.common.utils.DateUtils;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;

@ApplicationScoped
public class EmailMqRepository implements EmailRepository{

	private static final Logger logger = LoggerFactory.getLogger(EmailMqRepository.class);
	
	@Inject
	private MqService mqService;
	
	@Inject
	@ConfigurationValue(property = "portal.email.appid.default",defaultValue = PortalConstant.WALLET_APP_ID)
	private String currentAppId;
	
	@Override
	public void sendEmail(String sender, List<String> recipients, List<String> bccRecipients, String subject, String message) {
		new Thread(new Runnable() {
			public void run() {
				WS_Email email = new WS_Email();
				email.setMailBody(message);
				email.setMailSubject(subject);
				email.setPriority(3);
				email.setToAddressList(recipients);
				email.setBccAddressList(bccRecipients);
				email.setValidMinutes(1440);
				email.setFromAddress(sender);
				email.setApplication(currentAppId);
				email.setHtmlContent(true);
				email.setSubmitTime(DateUtils.formatClient(LocalDateTime.now()));
				CreateEmailRequest cer = new CreateEmailRequest();
				cer.setEmail(email);
				cer.setQueuePriority(1);
				try {
					mqService.sentEmail(cer);
				} catch (Exception e) {
					logger.error("mq sent mail fail", e);
					PortalExceptionUtils.throwNow(PortalErrorCode.MQ_ERROR_ERROR_ON_CONNECTION);
				}
			}
		}).start();
	}

}
