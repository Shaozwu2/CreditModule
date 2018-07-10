package com.kooppi.nttca.portal.common.portalScheduler.core.releaseExpiriedReservationJob;

import java.time.LocalDateTime;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kooppi.nttca.portal.common.config.file.ConfigurationValue;
import com.kooppi.nttca.wallet.rest.reservation.repository.TransactionReservationRepository;
import com.kooppi.nttca.wallet.rest.reservation.service.TransactionReservationService;
import com.kooppi.nttca.wallet.rest.wallet.service.WalletService;

@Stateless
public class ReleaseExpiriedReservationQuartzScheduleService {
	
	private static final Logger logger = LoggerFactory.getLogger(ReleaseExpiriedReservationQuartzScheduleService.class);
	
	@Inject
	TransactionReservationRepository transactionReservationRepository;
	
	@Inject 
	TransactionReservationService transactionReservationService;
	
	@Inject
	WalletService walletService;
	
    @ConfigurationValue(property = "portal.app.user.id.default", defaultValue = "system")
    private String jobUserId;
	
	public void scanAndReleaseExpiriedReservations() throws JobExecutionException {
		logger.info("Scan and release expiried reservation job starts at {}", LocalDateTime.now());
		//TBC
//		List<TransactionReservation> transactionReservations = transactionReservationRepository.findAllReservedTransactionReservations();
//		transactionReservations.forEach((transactionReservation) -> {
//					try {
//						if (transactionReservation.getExpiredDate() != null) {
//							LocalDateTime expiredDate = transactionReservation.getExpiredDate();
//							if (expiredDate.isBefore(LocalDateTime.now())) {
//								Optional<Wallet> optWallet = walletService.findWalletById(transactionReservation.getWalletId());
//								Wallet wallet = PortalExceptionUtils.throwIfEmpty(optWallet,PortalErrorCode.INVALID_WALLET_ID);
//
//								transactionReservationService.releaseExpiredCredits(transactionReservation, wallet);
//							}
//						}
//					} catch (Exception e) {
//				logger.error(String.format("Error occurred when scan and release expiried reservation transactionId = {%s}, exception = {}, skipping this scan", transactionReservation.getTransactionId(), e));
//			}
//		}); 
		
		logger.info("Scan and release expiried reservation job  ends at {}", LocalDateTime.now());
	}
}
