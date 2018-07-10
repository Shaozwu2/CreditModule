package com.kooppi.nttca.portal.common.portalScheduler.core.monthlyRechargeJob;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kooppi.nttca.portal.common.config.file.ConfigurationValue;
import com.kooppi.nttca.wallet.common.persistence.domain.Adjustment;
import com.kooppi.nttca.wallet.rest.adjustment.service.AdjustmentService;

@Stateless
public class CloseAdjustmentQuartzScheduleService {

	@Inject
	private AdjustmentService adjustmentService;
	
	@Inject
	@ConfigurationValue(property = "portal.app.user.id.default", defaultValue = "system")
    private String jobUserId;
	
	private static final Logger logger = LoggerFactory.getLogger(CloseAdjustmentQuartzScheduleService.class);
	
	public void closeAdjustment() throws JobExecutionException {
		logger.info("do daily schedled adjustment job to check if adjustment status need to be changed to CLOSED , job starts at {}", LocalDateTime.now());
		
		List<Adjustment> adjustments = adjustmentService.findAllPendingAndInUseAdjustment();
		adjustments.forEach(adj -> {
			LocalDate creditExpiryDate = adj.getCreditExpiryDate();
			Integer balance = adj.getBalance();
			
			if (creditExpiryDate != null) {
				LocalDate today = LocalDate.now();
				if (today.isAfter(creditExpiryDate))
					adj.closeAdjustment(jobUserId);
			}
			
			if (balance != null && balance <= 0)
				adj.closeAdjustment(jobUserId);
		});
	}
}
