package com.kooppi.nttca.portal.common.email.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.inject.Inject;

import com.kooppi.nttca.cm.common.mail.repository.EmailRepository;
import com.kooppi.nttca.portal.common.config.file.ConfigurationValue;
import com.kooppi.nttca.wallet.common.persistence.domain.Wallet;

public class ForfeitEmailServiceSqlImpl implements ForfeitAlertService {

	@Inject
	EmailRepository emailRepository;
	
	@Inject
    @ConfigurationValue(property = "portal.wallet.alert.default.sender_email", defaultValue = "ntt-admin@ntt.com.hk")
    private String configSender;
	
	@Inject
    @ConfigurationValue(property = "portal.wallet.forfeit.alert.default.subject", defaultValue = "Wallet Utilization Notification")
	private String configSubject;
	
	@Inject
    @ConfigurationValue(property = "portal.wallet.forfeit.alert.default.body", defaultValue = "")
	private String configMessage;
	
	@Inject
	@ConfigurationValue(property = "portal.wallet.forfeit.negative.alert.default.body", defaultValue = "")
	private String configNegativeMessage;
	
	@Override
	public void checkAndSendAlert(Wallet wallet) {
		if (wallet.getForfeitAlert1Threshold() != null && wallet.getForfeitAlert1ReceiverList().size() > 0) {
			LocalDate expireOn = wallet.getExpiredOn();
			LocalDate now = LocalDate.now();
			long intervalDays = ChronoUnit.DAYS.between(now, expireOn);
			
			if ((int) intervalDays <= wallet.getForfeitAlert1Threshold() && intervalDays >= 0) {
				if (wallet.getForfeitThreshold1LastSentDate() == null) {
					String message = String.format(configMessage, wallet.getWalletId(), wallet.getExpiredOn(), intervalDays);
					if (!(wallet.getForfeitAlert1ReceiverList().size() == 0 && wallet.getForfeitAlert1BccList().size() != 0)) {
						emailRepository.sendEmail(configSender, wallet.getForfeitAlert1ReceiverList(), wallet.getForfeitAlert1BccList(), configSubject, message);
						wallet.setForfeitThreshold1LastSentDate(LocalDateTime.now());
					}
				} else {
					long diffeInHours = Duration.between(wallet.getForfeitThreshold1LastSentDate(), LocalDateTime.now()).toHours();
					if (diffeInHours >= 24) {
						String message = String.format(configMessage, wallet.getWalletId(), wallet.getExpiredOn(), intervalDays);
						if (!(wallet.getForfeitAlert1ReceiverList().size() == 0 && wallet.getForfeitAlert1BccList().size() != 0)) {
							emailRepository.sendEmail(configSender, wallet.getForfeitAlert1ReceiverList(), wallet.getForfeitAlert1BccList(), configSubject, message);
							wallet.setForfeitThreshold1LastSentDate(LocalDateTime.now());
						}
					}
				}
			} else if ((int) intervalDays <= wallet.getForfeitAlert1Threshold() && intervalDays < 0) {
				if (wallet.getForfeitNegative1LastSentDate() == null) {
					String message = String.format(configNegativeMessage, wallet.getWalletId(), wallet.getExpiredOn(), 0-intervalDays);
					if (!(wallet.getForfeitAlert1ReceiverList().size() == 0 && wallet.getForfeitAlert1BccList().size() != 0)) {
						emailRepository.sendEmail(configSender, wallet.getForfeitAlert1ReceiverList(), wallet.getForfeitAlert1BccList(), configSubject, message);
						wallet.setForfeitNegative1LastSentDate(LocalDateTime.now());
					}
				} else {
					long diffeInHours = Duration.between(wallet.getForfeitNegative1LastSentDate(), LocalDateTime.now()).toHours();
					if (diffeInHours >= 24) {
						String message = String.format(configNegativeMessage, wallet.getWalletId(), wallet.getExpiredOn(), 0-intervalDays);
						if (!(wallet.getForfeitAlert1ReceiverList().size() == 0 && wallet.getForfeitAlert1BccList().size() != 0)) {
							emailRepository.sendEmail(configSender, wallet.getForfeitAlert1ReceiverList(), wallet.getForfeitAlert1BccList(), configSubject, message);
							wallet.setForfeitNegative1LastSentDate(LocalDateTime.now());
						}
					}
				}
			}
		}
		
		if (wallet.getForfeitAlert2Threshold() != null && wallet.getForfeitAlert2ReceiverList().size() > 0) {
			LocalDate expireOn = wallet.getExpiredOn();
			LocalDate now = LocalDate.now();
			long intervalDays = ChronoUnit.DAYS.between(now, expireOn);
			
			if ((int) intervalDays <= wallet.getForfeitAlert2Threshold() && intervalDays >= 0) {
				if (wallet.getForfeitThreshold2LastSentDate() == null) {
					String message = String.format(configMessage, wallet.getWalletId(), wallet.getExpiredOn(), intervalDays);
					if (!(wallet.getForfeitAlert2ReceiverList().size() == 0 && wallet.getForfeitAlert2BccList().size() != 0)) {
						emailRepository.sendEmail(configSender, wallet.getForfeitAlert2ReceiverList(), wallet.getForfeitAlert2BccList(), configSubject, message);
						wallet.setForfeitThreshold2LastSentDate(LocalDateTime.now());
					}
				} else {
					long diffeInHours = Duration.between(wallet.getForfeitThreshold2LastSentDate(), LocalDateTime.now()).toHours();
					if (diffeInHours >= 24) {
						String message = String.format(configMessage, wallet.getWalletId(), wallet.getExpiredOn(), intervalDays);
						if (!(wallet.getForfeitAlert2ReceiverList().size() == 0 && wallet.getForfeitAlert2BccList().size() != 0)) {
							emailRepository.sendEmail(configSender, wallet.getForfeitAlert2ReceiverList(), wallet.getForfeitAlert2BccList(), configSubject, message);
							wallet.setForfeitThreshold2LastSentDate(LocalDateTime.now());
						}
					}
				}
			} else if ((int) intervalDays <= wallet.getForfeitAlert2Threshold() && intervalDays < 0) {
				if (wallet.getForfeitNegative2LastSentDate() == null) {
					String message = String.format(configNegativeMessage, wallet.getWalletId(), wallet.getExpiredOn(), 0-intervalDays);
					if (!(wallet.getForfeitAlert2ReceiverList().size() == 0 && wallet.getForfeitAlert2BccList().size() != 0)) {
						emailRepository.sendEmail(configSender, wallet.getForfeitAlert2ReceiverList(), wallet.getForfeitAlert2BccList(), configSubject, message);
						wallet.setForfeitNegative2LastSentDate(LocalDateTime.now());
					}
				} else {
					long diffeInHours = Duration.between(wallet.getForfeitNegative2LastSentDate(), LocalDateTime.now()).toHours();
					if (diffeInHours >= 24) {
						String message = String.format(configNegativeMessage, wallet.getWalletId(), wallet.getExpiredOn(), 0-intervalDays);
						if (!(wallet.getForfeitAlert2ReceiverList().size() == 0 && wallet.getForfeitAlert2BccList().size() != 0)) {
							emailRepository.sendEmail(configSender, wallet.getForfeitAlert2ReceiverList(), wallet.getForfeitAlert2BccList(), configSubject, message);
							wallet.setForfeitNegative2LastSentDate(LocalDateTime.now());
						}
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		LocalDate from = LocalDate.of(2020, 1, 1);
		LocalDate to = LocalDate.of(2019, 1, 1);
		long interval = ChronoUnit.DAYS.between(from, to);
		System.out.println(interval);
	}

}
