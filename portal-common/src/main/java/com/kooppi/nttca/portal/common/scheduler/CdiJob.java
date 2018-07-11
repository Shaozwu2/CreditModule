package com.kooppi.nttca.portal.common.scheduler;

import javax.enterprise.context.RequestScoped;

import org.apache.deltaspike.cdise.api.ContextControl;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides the integration of CDI and quartz Job.
 */
public abstract class CdiJob implements Job {

	protected static final Logger LOGGER = LoggerFactory.getLogger(CdiJob.class);

	public CdiJob() {
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOGGER.info("*********************************");
		LOGGER.info("Start {} now ", this.getClass().getSimpleName());
		ContextControl ctxCtrl = null;
		try {
			ctxCtrl = BeanProvider.getContextualReference(ContextControl.class);

			/**
			 * this will implicitly bind a new RequestContext to the current thread. Otherwise the request-scoped entity manager cannot be injected.
			 */
			ctxCtrl.startContext(RequestScoped.class);
			doExecute(context);

		} catch (JobExecutionException je) {
			LOGGER.error(String.format("Error when executing %s", this.getClass().getSimpleName()), je);
			throw je;
		} catch (Exception e) {
			LOGGER.error(String.format("Error when executing %s", this.getClass().getSimpleName()), e);
		} finally {
			// stop the RequestContext to ensure that all request-scoped beans get cleaned up.
			ctxCtrl.stopContext(RequestScoped.class);
			LOGGER.info("Finish {} now ", this.getClass().getSimpleName());
			LOGGER.info("*********************************");
		}
	}

	public abstract void doExecute(JobExecutionContext context) throws JobExecutionException;
}
