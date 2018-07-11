

import java.time.LocalDateTime;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.simpl.SimpleJobFactory;

import com.kooppi.nttca.portal.common.portalScheduler.core.lockIdleWalletJob.ScanAndLockIdleWalletJob;
import com.kooppi.nttca.portal.common.scheduler.QuartzTool;
import com.kooppi.nttca.portal.common.scheduler.PortalScheduler;
import com.kooppi.nttca.portal.common.utils.DateUtils;

public class TestWalletScheduler {

	public static void main(String[] args) throws Exception {
		PortalScheduler scheudler = new PortalScheduler(QuartzTool.createScheduler(), new SimpleJobFactory());

		JobDetail jobDetail=  JobBuilder.newJob(ScanAndLockIdleWalletJob.class)
				.withIdentity(JobKey.jobKey("sampleJob","sampleJob"))
				.build();
		
		Trigger trigger = TriggerBuilder.newTrigger()
				        .withIdentity(TriggerKey.triggerKey("sampleJobTrigger6", "sampleJobTrigger"))
				        .startAt(DateUtils.toSchedulerDate(LocalDateTime.now().plusDays(100)))
				        .build();
		//scheudler.addScheduleJob(jobDetail, trigger);
		//scheudler.deleteAllJobs();
		scheudler.scheduleOnlyIfJobNotExist(jobDetail, trigger);
		//scheudler.rescheduleJob(TriggerKey.triggerKey("sampleJobTrigger6", "sampleJobTrigger"), trigger);
		scheudler.printSchedulerInformation();
		scheudler.shutdown();
	}
}
