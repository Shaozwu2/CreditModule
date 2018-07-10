package com.kooppi.nttca.portal.common.scheduler;

import java.util.Properties;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Manage the quartz scheduler in JavaSE
 */
public class QuartzTool {

	public QuartzTool() {
	}

	public static Scheduler createScheduler() throws Exception {
		Properties properties = new Properties();
		properties.put("org.quartz.scheduler.instanceName", "GatewayScheduler");
		properties.put("org.quartz.scheduler.instanceId", "AUTO");
		properties.put("org.quartz.threadPool.threadCount", "1");
		properties.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX ");
		properties.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
		properties.put("org.quartz.jobStore.dataSource", "ds");
		properties.put("org.quartz.jobStore.isClustered", "true");
		properties.put("org.quartz.dataSource.ds.driver", "com.mysql.jdbc.Driver");
		properties.put("org.quartz.dataSource.ds.URL", "jdbc:mysql://192.168.12.244/mvno");
		properties.put("org.quartz.dataSource.ds.user", "root");
		properties.put("org.quartz.dataSource.ds.password", "1q0p2w9o3e8i4r7u5t6y");
		properties.put("org.quartz.scheduler.skipUpdateCheck", "true");
		
//		properties.put("org.quartz.dataSource.ds.URL", "jdbc:mysql://127.0.0.1:30010/clouds");
//		properties.put("org.quartz.dataSource.ds.user", "clouds");
//		properties.put("org.quartz.dataSource.ds.password", "clouds");

		Scheduler scheudler = new StdSchedulerFactory(properties).getScheduler();
		return scheudler;
	}
	
	public static interface SchedulerConsumer{
		void accept(Scheduler s) throws Exception;
	}
	
	private static void conumseScheduler(SchedulerConsumer schedulerConsumer)  {
		try {
			Scheduler schuduler = createScheduler();
			schedulerConsumer.accept(schuduler);
			schuduler.shutdown();
		} catch (SchedulerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void deleteJob(String jobGroup, String jobName) {
		conumseScheduler(s -> s.deleteJob(new JobKey(jobName, jobGroup)));
	}

	public static void printSchedulerInformation() {
		conumseScheduler(s -> QuartzUtil.printSchedulerInfo(s));
	}
	
	public static void scheduleJob(JobDetail jobDetail , Trigger trigger){
		conumseScheduler(s -> s.scheduleJob(jobDetail, trigger));
	}
	
	public static void startSchedule(){
		conumseScheduler(s -> s.start());
	}
	
	public static void clear(){
		conumseScheduler(s -> s.clear());
	}
	
	public static void reSchedule(TriggerKey triggerKey, Trigger newTrigger){
		conumseScheduler(s -> s.rescheduleJob(triggerKey, newTrigger));
	}


	public static void deleteAllJobs(){
		conumseScheduler(s -> {
			for (JobKey jobKey : QuartzUtil.getAllJobKey(s)) {
				try {
					s.deleteJob(jobKey);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void deleteSingleJob(String jobGroup, String jobName) throws Exception {
		conumseScheduler(s -> s.deleteJob(JobKey.jobKey(jobName, jobGroup)));
	}

	public static void main(String[] args) {
		try {
			 QuartzTool.printSchedulerInformation();
			 
//			Trigger newTrigger = TriggerBuilder
//				.newTrigger()
//				.withIdentity("ChargeNetworkServiceTigger", "ChargeNetworkServiceTigger")
//				.withSchedule(
//						CronScheduleBuilder.cronSchedule("0 0 0-23 * * ?")
//						.withMisfireHandlingInstructionDoNothing())
//				.build();
//			 
//			 QuartzTool.reSchedule(TriggerKey.triggerKey("ChargeNetworkServiceTigger", "ChargeNetworkServiceTigger")
//					, newTrigger);
			 //QuartzTool.clear();
			// QuartzTool.startSchedule();
			// QuartzTool.deleteAllJobs();
		//	QuartzTool.deleteJob("Charging", "ChargeNetworkServiceJob");
		//	QuartzTool.deleteJob("RouterMangement","LogRouterConfigJob");
//			QuartzTool.deleteJob("IpManagement", "ReleaseCoolDownIpJob");
//			QuartzTool.deleteJob("BHEC" , "SyncBhecJob");
//			QuartzTool.deleteJob("BandwidthManagement", "UpdateBandwidthStatisticAndAutoScalingJob");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
