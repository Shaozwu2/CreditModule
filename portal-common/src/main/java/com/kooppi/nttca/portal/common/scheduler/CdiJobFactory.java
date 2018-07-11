package com.kooppi.nttca.portal.common.scheduler;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.quartz.Job;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

/**
 * <p>
 * Quartz and CDI injection which allow to inject CDI bean into the Quartz Job
 */
public class CdiJobFactory implements JobFactory {

	@Inject
	private Instance<Job> jobs;

	public CdiJobFactory() {
		System.out.println("CdiJobFactory#constructor execute....");
	}

	@Override
	public Job newJob(TriggerFiredBundle bundle, org.quartz.Scheduler scheduler) throws SchedulerException {
		try {
			return jobs.select(bundle.getJobDetail().getJobClass()).get();
		} catch (Exception e) {
			e.printStackTrace();
			throw e; 
		}
	}

}
