package com.kooppi.nttca.portal.common.portalScheduler.core.usageStatisticsJob;

import javax.inject.Inject;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

import com.kooppi.nttca.portal.common.scheduler.CdiJob;

public class MonthlyUsageStatisticsJob extends CdiJob{

	private static final String JOB_NAME = "Monthly usage statistics job";
	private static final String JOB_GROUP = "System Monthly usage statistics job";
	
	@Inject 
	private MonthlyUsageStatisticsQuartzScheduleService service;
	
	public static Trigger trigger(){
		return TriggerBuilder.newTrigger()
				.withIdentity(TriggerKey.triggerKey(JOB_NAME, JOB_GROUP))
				//trigger every monthly at yy:mm:01  dd:hh:ss  
				.withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 1 * ?")
				.withMisfireHandlingInstructionDoNothing())
				.build();
	}
	
	public static JobDetail jobDetail(){
		
		return JobBuilder.newJob(MonthlyUsageStatisticsJob.class)
					.withIdentity(jobKey())
					.withDescription("Monthly usage statistics job with NTTCA Service.")
					.build();
	}
	
	private static JobKey jobKey(){
		return new JobKey(JOB_NAME,JOB_GROUP);
	}
	
	@Override
	public void doExecute(JobExecutionContext context) throws JobExecutionException {
		service.collectMonthlyUsageStatistics();
	}
	
}
