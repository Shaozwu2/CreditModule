package com.kooppi.nttca.portal.common.scheduler;

import java.util.Set;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

/**
 * Wraps the quartz scheduler such that we can use CDI to manage its life-cycle and customize 
 * the JobFactory to allow to inject CDI bean in the jobs
 * @see http://quartz-scheduler.org/documentation/quartz-2.x/cookbook/
 */
@ApplicationScoped
public class PortalScheduler {

	static Logger logger = LoggerFactory.getLogger(PortalScheduler.class);

	protected Scheduler scheduler;

	public PortalScheduler() {
	}

	@Inject
	public PortalScheduler(JobFactory jobFactory) {
		try {
			intialize(new StdSchedulerFactory().getScheduler(), jobFactory);
		} catch (SchedulerException e) {
			logger.error("Error occurs when setup the scheduler.", e);
			throw new PortalSchedulerException(e);
		}
	}

	public PortalScheduler(Scheduler scheduler, JobFactory jobFactory) {
		intialize(scheduler, jobFactory);
	}

	private void intialize(Scheduler scheduler, JobFactory jobFactory) {
		try {
			this.scheduler = scheduler;
			this.scheduler.setJobFactory(jobFactory);
			logger.info("Finish initializing the scheduler {} now...", scheduler.getSchedulerName());
		} catch (SchedulerException e) {
			logger.error("Error occurs when setup the scheduler.", e);
			throw new PortalSchedulerException(e);
		}
	}

	/**
	 * Schedule a job. 
	 * Note: a job can be scheduled to be executed in multiple times.
	 * If the same trigger is already scheduled , it will replace the existing trigger.
	 */
	public void scheduleJob(JobDetail jobDetail, Trigger trigger) {
		scheduleJob(jobDetail, Sets.newHashSet(trigger));
	}

	public void scheduleJob(JobDetail jobDetail, Set<? extends Trigger> trigger) {
		execute(s -> s.scheduleJob(jobDetail, trigger, true), "Error occurs when scheduling the job");
	}
	
	/**
	 * Only schedule if the job does not exist .Useful for the case that to ensure the job 
	 * is scheduled at one execution time but not care about its scheduled time.
	 */
	public void scheduleOnlyIfJobNotExist(JobDetail jobDetail, Trigger trigger) {
		execute(s -> {
			if (!s.checkExists(jobDetail.getKey())) {
				s.scheduleJob(jobDetail, trigger);
			}
		}, "Error occurs when scheduling the job");
	}

	public void start() {
		execute(s -> s.start(), "Error occurs when starting the scheduler");
	}

	@PreDestroy
	public void shutdown() {
		logger.info("Shut down scheduler now...");
		execute(s -> s.shutdown(), "Error occurs when shuting the scheduler");
	}

	public void printSchedulerInformation() {
		QuartzUtil.printSchedulerInfo(this.scheduler);
	}
	
	public String getSchedulerInformation(){
		return QuartzUtil.schedulerInfo(this.scheduler);
	}
	
	/**
	 * Note: This only removes the schedule to be executed at a given time. The job will still execute 
	 * if it is scheduled to be executed at many times (i.e have many trigger).
	 * 
	 * To make a job does not execute anymore , we should remove all of its trigger or use {@link #deleteJob(JobKey)}
	 * to remove this job.
	 */
	public void unscheduleJob(TriggerKey triggerKey) {
		execute(s -> s.unscheduleJob(triggerKey), String.format("Error occurs when unscheduling the Job. Trigger Key : %s", triggerKey));
	}

	public void rescheduleJob(TriggerKey triggerKey, Trigger newTrigger) {
		execute(s -> s.rescheduleJob(triggerKey, newTrigger), String.format("Error occurs when rescheduling the Job. Trigger Key : %s", triggerKey));
	}

	public void deleteJob(JobKey jobKey) {
		execute(s -> s.deleteJob(jobKey), String.format("Error occurs when deleting the Job (%s)", jobKey));
	}

	public void deleteAllJobs() {
		for (JobKey jobKey : QuartzUtil.getAllJobKey(scheduler)) {
			try {
				deleteJob(jobKey);
			} catch (Exception e) {
				logger.error(String.format("Error occur when deleting the job (ID:%s)", jobKey));
			}
		}
	}

	private void execute(SchedulerConsumer consumer, String logErrorMessage) {
		try {
			if (scheduler != null) {
				consumer.accept(this.scheduler);
			}
		} catch (SchedulerException e) {
			logger.error(logErrorMessage, e);
			throw new PortalSchedulerException(e);
		}
	}

	@FunctionalInterface
	private interface SchedulerConsumer {
		public void accept(Scheduler scheduler) throws SchedulerException;
	}

//	public static void main(String[] args) {
//		LocalDateTime onceShotTime = LocalDateTime.now().plusSeconds(10).minusDays(10);
//
//		Trigger trigger = TriggerBuilder.newTrigger().startAt(DateUtil.toDate(onceShotTime))
//				.withSchedule(CronScheduleBuilder.atHourAndMinuteOnGivenDaysOfWeek(10, 10, DateBuilder.MONDAY, DateBuilder.WEDNESDAY)).build();
//
//		for (LocalDateTime d : QuartzUtil.computerFireTimes(trigger, 100)) {
//			System.out.println(String.format("%s (%s)", d, d.getDayOfWeek()));
//		}
//	}

}
