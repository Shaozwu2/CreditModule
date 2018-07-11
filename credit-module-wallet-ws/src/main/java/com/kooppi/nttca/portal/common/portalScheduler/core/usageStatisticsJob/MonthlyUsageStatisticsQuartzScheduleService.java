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
public class MonthlyUsageStatisticsQuartzScheduleService {	

	@Inject
	private TransactionRepository transactionRepository;
	
	@Inject
	private WalletRepository walletRepository;

	@Inject
	private UsageStatisticsService usageStatisticsService;
	
	private static final Logger logger = LoggerFactory.getLogger(MonthlyUsageStatisticsQuartzScheduleService.class);

	public void collectMonthlyUsageStatistics() throws JobExecutionException {
		logger.info("Monthly usage statistics job starts at {}", LocalDateTime.now());
		
		List<Wallet> wallets = walletRepository.findAllWallets();
		
		wallets.forEach((wallet) -> {
			try {
					logger.info("Monthly usage statistics of wallet {}", wallet.getWalletId());
					//sum up amount
					Optional<Number> optTotalAmount = transactionRepository.sumUpTransactionsOfLastMonthByWalletId(wallet.getWalletId());
					//get transaction start "year", "month", "day".
					LocalDateTime startDateTime = LocalDateTime.now().minusMonths(1);
					
					if(optTotalAmount.isPresent()){
						usageStatisticsService.createMonthlyUsageStatistics(wallet.getWalletId(), optTotalAmount.get().intValue(), startDateTime.getYear(), startDateTime.getMonthValue(),startDateTime.getDayOfMonth());
					}
					
			} catch (Exception e) {
				logger.error(String.format("Error occurred when collecting monthly usage statistics of wallet = {%s}, skipping this process", wallet.getWalletId(), e));
			}
		}); 
		
		logger.info("Monthly usage statistics job ends at {}", LocalDateTime.now());
	}
}