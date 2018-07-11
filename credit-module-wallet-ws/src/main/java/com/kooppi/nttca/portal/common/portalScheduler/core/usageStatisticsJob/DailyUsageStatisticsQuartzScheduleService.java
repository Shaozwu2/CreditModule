package com.kooppi.nttca.portal.common.portalScheduler.core.usageStatisticsJob;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kooppi.nttca.wallet.common.persistence.domain.Wallet;
import com.kooppi.nttca.wallet.rest.UsageStatistics.service.UsageStatisticsService;
import com.kooppi.nttca.wallet.rest.transaction.repository.TransactionRepository;
import com.kooppi.nttca.wallet.rest.wallet.repository.WalletRepository;

@Stateless
public class DailyUsageStatisticsQuartzScheduleService {	

	@Inject
	private TransactionRepository transactionRepository;
	
	@Inject
	private WalletRepository walletRepository;

	@Inject
	private UsageStatisticsService usageStatisticsService;
	
	private static final Logger logger = LoggerFactory.getLogger(DailyUsageStatisticsQuartzScheduleService.class);

	public void collectDailyUsageStatistics() throws JobExecutionException {
		logger.info("Daily usage statistics job starts at {}", LocalDateTime.now());
		
		List<Wallet> wallets = walletRepository.findAllWallets();
		
		wallets.forEach((wallet) -> {
			try {
					logger.info("Daily usage statistics of wallet {}", wallet.getWalletId());
					//sum up amount
					Optional<Number> optTotalAmount = transactionRepository.sumUpTransactionsOfPreviousDayByWalletId(wallet.getWalletId());
					//get transaction start "month" and "day".
					LocalDateTime startDateTime = LocalDateTime.now().minusDays(1);
					
					if(optTotalAmount.isPresent()){
						usageStatisticsService.createDailyUsageStatistics(wallet.getWalletId(), optTotalAmount.get().intValue(), startDateTime.getMonthValue(), startDateTime.getDayOfMonth());
					}
			} catch (Exception e) {
				logger.error(String.format("Error occurred when collecting daily usage statistics of wallet = {%s}, skipping this process", wallet.getWalletId(), e));
			}
		}); 
		
		logger.info("Daily usage statistics job ends at {}", LocalDateTime.now());
	}
}