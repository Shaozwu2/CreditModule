package com.kooppi.nttca.portal.common.portalScheduler.core.monthlyRechargeJob;

import java.time.LocalDateTime;

import javax.inject.Inject;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kooppi.nttca.portal.common.scheduler.CdiJob;
import com.kooppi.nttca.portal.common.utils.DateUtils;

public class DailyAdjustmentJob extends CdiJob{

	private static final Logger logger = LoggerFactory.getLogger(DailyAdjustmentJob.class);	
	private static final String JOB_NAME = "Monthly recharge wallet job";
	private static final String JOB_GROUP = "System monthly recharge job";
	
	@Inject 
	private MonthlyRechargeQuartzScheduleService monthlyRechargeService;
	
	@Inject
	private CloseAdjustmentQuartzScheduleService closeAdjustmentService;
	
	public static Trigger trigger(Integer timeInterval){
		LocalDateTime startTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
		startTime = startTime.plusDays(1);
		
		return TriggerBuilder.newTrigger()
				.withIdentity(TriggerKey.triggerKey(JOB_NAME, JOB_GROUP))
				.startAt(DateUtils.toSchedulerDate(startTime))
				.withSchedule(SimpleScheduleBuilder
				.repeatHourlyForever(timeInterval))
				.build();
	}
	
	public static JobDetail jobDetail(){
		return JobBuilder.newJob(DailyAdjustmentJob.class)
					.withIdentity(jobKey())
					.withDescription("Do Scheduled Adjustment job with NTTCA Service.")
					.build();
	}
	
	private static JobKey jobKey(){
		return new JobKey(JOB_NAME,JOB_GROUP);
	}
	
	@Override
	public void doExecute(JobExecutionContext context) throws JobExecutionException {
		logger.info("Execute DailyDoScheduledAdjustmentJob on: "+ LocalDateTime.now());
		monthlyRechargeService.monthlyRechargeWallets();
		closeAdjustmentService.closeAdjustment();
	}

}
