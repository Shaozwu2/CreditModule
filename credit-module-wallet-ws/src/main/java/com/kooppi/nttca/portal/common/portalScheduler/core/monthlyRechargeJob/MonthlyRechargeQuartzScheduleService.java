package com.kooppi.nttca.portal.common.portalScheduler.core.monthlyRechargeJob;

import java.time.LocalDateTime;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kooppi.nttca.portal.common.config.file.ConfigurationValue;
import com.kooppi.nttca.wallet.common.persistence.domain.Adjustment;
import com.kooppi.nttca.wallet.common.persistence.domain.Transaction;
import com.kooppi.nttca.wallet.common.persistence.domain.Wallet;
import com.kooppi.nttca.wallet.rest.adjustment.service.AdjustmentService;
import com.kooppi.nttca.wallet.rest.transaction.service.TransactionService;

@Stateless
public class MonthlyRechargeQuartzScheduleService {
	
	@Inject TransactionService transactionService;
	
	@Inject
	private AdjustmentService adjustmentService;
	
    @Inject
	@ConfigurationValue(property = "portal.app.user.id.default", defaultValue = "system")
    private String jobUserId;
    
    
	private static final Logger logger = LoggerFactory.getLogger(MonthlyRechargeQuartzScheduleService.class);
	
	public void monthlyRechargeWallets() throws JobExecutionException {
		
		logger.info("do daily scheduled adjustment job starts at {}", LocalDateTime.now());
		List<Adjustment> scheduledAdjustments = adjustmentService.findAllScheduledAdjustmentOnToday();
		
		scheduledAdjustments.forEach(adj -> {
			Wallet wallet = adj.getWallet();
			Transaction transaction = null;
			logger.info("do adjustment on transaction id = {}",adj.getContractId());
			if (adj.isOneoff()) {
				transaction = wallet.addOneOffAdjustment(adj, jobUserId);
			} else {
				if (adj.isTimeToMonthlyRecharge()) {
					transaction = wallet.addRecurrsiveAdjustment(adj, jobUserId);
				}
			}
			
			if (transaction != null) {
				transactionService.createTransaction(transaction);
			}
		});
		logger.info("do daily scheduled adjustment job ends at {}", LocalDateTime.now());
	
	}
	
}
