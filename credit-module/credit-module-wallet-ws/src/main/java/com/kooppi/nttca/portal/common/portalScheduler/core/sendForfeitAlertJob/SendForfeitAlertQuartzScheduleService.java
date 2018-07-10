package com.kooppi.nttca.portal.common.portalScheduler.core.sendForfeitAlertJob;

import java.time.LocalDateTime;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kooppi.nttca.portal.common.email.service.ForfeitAlertService;
import com.kooppi.nttca.wallet.common.persistence.domain.Wallet;
import com.kooppi.nttca.wallet.rest.wallet.repository.WalletRepository;

@Stateless
public class SendForfeitAlertQuartzScheduleService {

	@Inject
	private WalletRepository walletRepository;
	
	@Inject
	private ForfeitAlertService forfeitAlertServie;
	
	private static final Logger logger = LoggerFactory.getLogger(SendForfeitAlertQuartzScheduleService.class);
	
	public void scanAndSendForfeitAlett() throws JobExecutionException {
		logger.info("Scan and send forfeit alert emails if needed, starts at {}", LocalDateTime.now());
		
		List<Wallet> wallets = walletRepository.findAllWallets();
		
		wallets.forEach((wallet) -> {
			forfeitAlertServie.checkAndSendAlert(wallet);
		});
		
		logger.info("Scan and send forfeit alert emails if needed, ends at {}", LocalDateTime.now());
	}
}
