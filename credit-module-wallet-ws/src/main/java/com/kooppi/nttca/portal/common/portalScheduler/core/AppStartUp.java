package com.kooppi.nttca.portal.common.portalScheduler.core;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kooppi.nttca.portal.common.config.file.ConfigurationValue;
import com.kooppi.nttca.portal.common.portalScheduler.core.monthlyRechargeJob.DailyAdjustmentJob;
import com.kooppi.nttca.portal.common.portalScheduler.core.reportJob.GenerateReportJob;
import com.kooppi.nttca.portal.common.portalScheduler.core.sendForfeitAlertJob.ScanAndSendForfeitAlertJob;
import com.kooppi.nttca.portal.common.portalScheduler.core.updatePricebookJob.DailyUpdatePricebookJob;
import com.kooppi.nttca.portal.common.scheduler.PortalScheduler;
import com.kooppi.nttca.wallet.config.rest.ApiUserManager;

@Startup
@Singleton
public class AppStartUp {
	private static final Logger logger = LoggerFactory.getLogger(AppStartUp.class);
	
	@Inject
	private PortalScheduler scheduler;
	
	@Inject
	private ApiUserManager apiUserManager;
	
	@Inject
	@ConfigurationValue(property = "portal.wallet.jobs.lockIdleWallet.timeInterval", defaultValue = "24")
	private Integer lockIdleWalletTimeInterval;
	
	@Inject
	@ConfigurationValue(property = "portal.wallet.jobs.monthlyRecharge.timeInterval", defaultValue = "24")
	private Integer monthlyRechargeTimeInterval;
	
	@Inject
	@ConfigurationValue(property = "portal.wallet.jobs.releaseExpiriedReservation.timeInterval", defaultValue = "5")
	private Integer releaseReservationTimeInterval;
	
	@Inject
	@ConfigurationValue(property = "portal.wallet.jobs.sendForfeitAlert.timeInterval", defaultValue = "24")
	private Integer sendForfeitTimeInterval;

	@PostConstruct
	public void init(){
		initScheduler();
		initHazelcastCache();
	}
	
	private void initScheduler(){
		logger.info("Begin initScheduler");
		
		logger.info("start Scheduler");
		scheduler.start();
		
		//Every time change the schedule of trigger, you should run deleteAllJobs to clean the timer first!!!!!!
		scheduler.deleteAllJobs();
		
		scheduler.scheduleOnlyIfJobNotExist(DailyAdjustmentJob.jobDetail(), DailyAdjustmentJob.trigger(monthlyRechargeTimeInterval));
		scheduler.scheduleOnlyIfJobNotExist(ScanAndSendForfeitAlertJob.jobDetail(), ScanAndSendForfeitAlertJob.trigger(sendForfeitTimeInterval));
		scheduler.scheduleOnlyIfJobNotExist(GenerateReportJob.jobDetail(), GenerateReportJob.trigger());
		scheduler.scheduleOnlyIfJobNotExist(DailyUpdatePricebookJob.jobDetail(), DailyUpdatePricebookJob.trigger());
		logger.info("End Scheduler Startup");
	}
	
	private void initHazelcastCache(){
		logger.info("Begin init hazelcast cache");
		apiUserManager.cacheAllApiUsers();
		logger.info("End init hazelcast cache");
	}
}