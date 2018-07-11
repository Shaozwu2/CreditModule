package com.kooppi.nttca.portal.common.portalScheduler.core.updatePricebookJob;

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

public class DailyUpdatePricebookJob extends CdiJob{

	private static final String JOB_NAME = "Daily update pricebook job";
	private static final String JOB_GROUP = "System daily pricebook job";
	
	@Inject
	private DailyUpdatePricebookService mqMessageConsumer;
	
	public static Trigger trigger(){
		return TriggerBuilder.newTrigger()
				.withIdentity(TriggerKey.triggerKey(JOB_NAME, JOB_GROUP))
				.withSchedule(CronScheduleBuilder.cronSchedule(" 0 0 3 * * ?")
				.withMisfireHandlingInstructionDoNothing())
				.build();
	}
	
	public static JobDetail jobDetail(){
		return JobBuilder.newJob(DailyUpdatePricebookJob.class)
					.withIdentity(jobKey())
					.withDescription("Daily update pricebook from EMS")
					.build();
	}
	
	private static JobKey jobKey(){
		return new JobKey(JOB_NAME,JOB_GROUP);
	}
	
	@Override
	public void doExecute(JobExecutionContext context) throws JobExecutionException {
		mqMessageConsumer.initConstant();
	}

	
}
