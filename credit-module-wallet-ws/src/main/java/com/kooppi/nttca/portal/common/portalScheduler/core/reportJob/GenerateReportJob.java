package com.kooppi.nttca.portal.common.portalScheduler.core.reportJob;

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
import com.kooppi.nttca.wallet.rest.report.resources.ReportResources;

public class GenerateReportJob extends CdiJob {
	
	private static final String JOB_NAME = "Monthly generate report job";
	private static final String JOB_GROUP = "System monthly generate report job";
	
	@Inject 
	private ReportResources reportResources;
	
	public static Trigger trigger(){
		return TriggerBuilder.newTrigger()
				.withIdentity(TriggerKey.triggerKey(JOB_NAME, JOB_GROUP))
				.withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 1/1 * ? *") // for temporary use, trigger everyday, Cron Expression = "0 0 0 1/1 * ? *"
				.withMisfireHandlingInstructionDoNothing())
				.build();
	}
	
	public static JobDetail jobDetail(){
		
		return JobBuilder.newJob(GenerateReportJob.class)
					.withIdentity(jobKey())
					.withDescription("Monthly generate report job.")
					.build();
	}
	
	private static JobKey jobKey(){
		return new JobKey(JOB_NAME,JOB_GROUP);
	}
	
	@Override
	public void doExecute(JobExecutionContext context) throws JobExecutionException {
		reportResources.generateReportAndSaveToDB();
	}

}
