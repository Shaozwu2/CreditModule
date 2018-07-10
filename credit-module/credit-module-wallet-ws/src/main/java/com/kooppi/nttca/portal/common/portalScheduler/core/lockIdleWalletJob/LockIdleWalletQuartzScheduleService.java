package com.kooppi.nttca.portal.common.portalScheduler.core.lockIdleWalletJob;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kooppi.nttca.portal.common.config.file.ConfigurationValue;
import com.kooppi.nttca.wallet.common.persistence.domain.Transaction;
import com.kooppi.nttca.wallet.common.persistence.domain.Wallet;
import com.kooppi.nttca.wallet.rest.transaction.service.TransactionService;
import com.kooppi.nttca.wallet.rest.wallet.repository.WalletRepository;
import com.kooppi.nttca.wallet.rest.wallet.service.WalletService;

@Stateless
public class LockIdleWalletQuartzScheduleService {
	
	@Inject
	private TransactionService transactionService;
	
	@Inject
	private WalletRepository walletRepository;
	
	@Inject
	private WalletService walletService;
	
	private static final Logger logger = LoggerFactory.getLogger(LockIdleWalletQuartzScheduleService.class);
	
	@ConfigurationValue(property = "portal.app.user.id.default", defaultValue = "system")
    private String jobUserId;

	@ConfigurationValue(property = "portal.wallet.scheduler.lockIdleWallet.remark", defaultValue = "")
	private String remark;
	
	public void scanAndLockIdleWallets() throws JobExecutionException {
		logger.info("Scan and lock idle wallets job starts at {}", LocalDateTime.now());
		
		List<Wallet> wallets = walletRepository.findAllWallets();
		
		wallets.forEach((wallet) -> {
			try {
				Integer maxIdlePeriod = wallet.getMaxIdlePeriod();
				//if maxIdlePeriod = 0, this wallet will expire
				if (maxIdlePeriod != null && maxIdlePeriod != 0) {
					
					LocalDateTime modDateTime = wallet.getModifiedDate();
					LocalDateTime currentDate = LocalDateTime.now();
						
					if (wallet.isActive()){
						if (currentDate.compareTo(modDateTime.plusDays(maxIdlePeriod)) >= 0) {
							LocalDateTime recentChargeTime = currentDate.minusDays(maxIdlePeriod);
							Optional<Transaction> optTrasaction = transactionService.findTransactionByWalletIdAndRecentChargeTime(wallet.getWalletId(), recentChargeTime);
							if (!optTrasaction.isPresent()) {
								logger.info("lock idle wallet {}", wallet.getWalletId());
								walletService.lockIdleWallet(wallet, jobUserId);
							}
						}
					}
				}
				
			} catch (Exception e) {
				logger.error(String.format("Error occurred when scan and lock idle wallets  walletId = {%s}, exception = {}, skipping this scan", wallet.getWalletId(), e));
			}
		}); 
		
		logger.info("Scan and lock idle wallets ends at {}", LocalDateTime.now());
	}
}
