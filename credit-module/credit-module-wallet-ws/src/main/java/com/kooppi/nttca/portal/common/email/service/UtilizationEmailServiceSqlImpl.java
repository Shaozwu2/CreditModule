package com.kooppi.nttca.portal.common.email.service;

import java.time.Duration;
import java.time.LocalDateTime;

import javax.inject.Inject;

import com.kooppi.nttca.cm.common.mail.repository.EmailRepository;
import com.kooppi.nttca.portal.common.config.file.ConfigurationValue;
import com.kooppi.nttca.wallet.common.persistence.domain.Wallet;
import com.kooppi.nttca.wallet.rest.wallet.service.WalletService;

public class UtilizationEmailServiceSqlImpl implements UtilizationAlertService {

	@Inject
	WalletService walletService;

	@Inject
	EmailRepository emailRepository;

	@Inject
	@ConfigurationValue(property = "portal.wallet.alert.default.sender_email", defaultValue = "ntt-admin@ntt.com.hk")
	private String configSender;

	@Inject
	@ConfigurationValue(property = "portal.wallet.utilization.alert.default.subject", defaultValue = "Wallet Utilization Notification")
	private String configSubject;

	@Inject
	@ConfigurationValue(property = "portal.wallet.utilization.alert.default.body", defaultValue = "")
	private String configMessage;

	@Override
	public void checkAndSendAlert(Wallet wallet) {
		if (wallet.getUtilizationAlert1Threshold() != null && wallet.getUtilizationAlert1ReceiverList().size() > 0) {

			if (wallet.getBalance() <= wallet.getUtilizationAlert1Threshold() && wallet.getBalance() >= 0) {
				if (wallet.getUtilizationThreshold1LastSentDate() == null) {
					String message = String.format(configMessage, wallet.getWalletId(), wallet.getBalance(), wallet.getUtilizationAlert1Threshold());
					if (!(wallet.getUtilizationAlert1ReceiverList().size() == 0 && wallet.getUtilizationAlert1BccList().size() != 0)) {
						emailRepository.sendEmail(configSender, wallet.getUtilizationAlert1ReceiverList(), wallet.getUtilizationAlert1BccList(), configSubject, message);
						wallet.setUtilizationThreshold1LastSentDate(LocalDateTime.now());
					}
				} else {
					long diffInHours = Duration.between(wallet.getUtilizationThreshold1LastSentDate(), LocalDateTime.now()).toHours();
					if (diffInHours >= 24) {
						String message = String.format(configMessage, wallet.getWalletId(), wallet.getBalance(), wallet.getUtilizationAlert1Threshold());
						if (!(wallet.getUtilizationAlert1ReceiverList().size() == 0 && wallet.getUtilizationAlert1BccList().size() != 0)) {
							emailRepository.sendEmail(configSender, wallet.getUtilizationAlert1ReceiverList(), wallet.getUtilizationAlert1BccList(), configSubject, message);
							wallet.setUtilizationThreshold1LastSentDate(LocalDateTime.now());
						}
					}
				}
			} else if (wallet.getBalance() <= wallet.getUtilizationAlert1Threshold() && wallet.getBalance() < 0) {
				if (wallet.getUtilizationNegative1LastSentDate() == null) {
					String message = String.format(configMessage, wallet.getWalletId(), wallet.getBalance(), wallet.getUtilizationAlert1Threshold());
					if (!(wallet.getUtilizationAlert1ReceiverList().size() == 0 && wallet.getUtilizationAlert1BccList().size() != 0)) {
						emailRepository.sendEmail(configSender, wallet.getUtilizationAlert1ReceiverList(), wallet.getUtilizationAlert1BccList(), configSubject, message);
						wallet.setUtilizationNegative1LastSentDate(LocalDateTime.now());
					}
				} else {
					long diffInHours = Duration.between(wallet.getUtilizationNegative1LastSentDate(), LocalDateTime.now()).toHours();
					if (diffInHours >= 24) {
						String message = String.format(configMessage, wallet.getWalletId(), wallet.getBalance(), wallet.getUtilizationAlert1Threshold());
						if (!(wallet.getUtilizationAlert1ReceiverList().size() == 0 && wallet.getUtilizationAlert1BccList().size() != 0)) {
							emailRepository.sendEmail(configSender, wallet.getUtilizationAlert1ReceiverList(), wallet.getUtilizationAlert1BccList(), configSubject, message);
							wallet.setUtilizationNegative1LastSentDate(LocalDateTime.now());
						}
					}
				}
			}
		}

		if (wallet.getUtilizationAlert2Threshold() != null && wallet.getUtilizationAlert2ReceiverList().size() > 0) {

			if (wallet.getBalance() <= wallet.getUtilizationAlert2Threshold() && wallet.getBalance() >= 0) {
				if (wallet.getUtilizationThreshold2LastSentDate() == null) {
					String message = String.format(configMessage, wallet.getWalletId(), wallet.getBalance(), wallet.getUtilizationAlert2Threshold());
					if (!(wallet.getUtilizationAlert2ReceiverList().size() == 0 && wallet.getUtilizationAlert2BccList().size() != 0)) {
						emailRepository.sendEmail(configSender, wallet.getUtilizationAlert2ReceiverList(), wallet.getUtilizationAlert2BccList(), configSubject, message);
						wallet.setUtilizationThreshold2LastSentDate(LocalDateTime.now());
					}
				} else {
					long diffInHours = Duration.between(wallet.getUtilizationThreshold2LastSentDate(), LocalDateTime.now()).toHours();
					if (diffInHours >= 24) {
						String message = String.format(configMessage, wallet.getWalletId(), wallet.getBalance(), wallet.getUtilizationAlert2Threshold());
						if (!(wallet.getUtilizationAlert2ReceiverList().size() == 0 && wallet.getUtilizationAlert2BccList().size() != 0)) {
							emailRepository.sendEmail(configSender, wallet.getUtilizationAlert2ReceiverList(), wallet.getUtilizationAlert2BccList(), configSubject, message);
							wallet.setUtilizationThreshold2LastSentDate(LocalDateTime.now());
						}
					}
				}
			} else if (wallet.getBalance() <= wallet.getUtilizationAlert2Threshold() && wallet.getBalance() < 0) {
				if (wallet.getUtilizationNegative2LastSentDate() == null) {
					String message = String.format(configMessage, wallet.getWalletId(), wallet.getBalance(), wallet.getUtilizationAlert2Threshold());
					if (!(wallet.getUtilizationAlert2ReceiverList().size() == 0 && wallet.getUtilizationAlert2BccList().size() != 0)) {
						emailRepository.sendEmail(configSender, wallet.getUtilizationAlert2ReceiverList(), wallet.getUtilizationAlert2BccList(), configSubject, message);
						wallet.setUtilizationNegative2LastSentDate(LocalDateTime.now());
					}
				} else {
					long diffInHours = Duration.between(wallet.getUtilizationNegative2LastSentDate(), LocalDateTime.now()).toHours();
					if (diffInHours >= 24) {
						String message = String.format(configMessage, wallet.getWalletId(), wallet.getBalance(), wallet.getUtilizationAlert2Threshold());
						if (!(wallet.getUtilizationAlert2ReceiverList().size() == 0 && wallet.getUtilizationAlert2BccList().size() != 0)) {
							emailRepository.sendEmail(configSender, wallet.getUtilizationAlert2ReceiverList(), wallet.getUtilizationAlert2BccList(), configSubject, message);
							wallet.setUtilizationNegative2LastSentDate(LocalDateTime.now());
						}
					}
				}
			}
		}
	}
}
