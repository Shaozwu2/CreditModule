package com.kooppi.nttca.portal.common.scheduler;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.calendar.BaseCalendar;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.spi.OperableTrigger;

import com.google.common.collect.Lists;
import com.kooppi.nttca.portal.common.utils.DateUtils;

public class QuartzUtil {

	public static List<LocalDateTime> computeFireTimes(String cronExpression , LocalDateTime fromDate ,LocalDateTime toDate){
		try {
			CronTriggerImpl cron = new CronTriggerImpl();
			cron.setCronExpression(cronExpression);

			BaseCalendar calendar = new BaseCalendar();
			List<Date> result = TriggerUtils.computeFireTimesBetween(cron, calendar, DateUtils.toSchedulerDate(fromDate), DateUtils.toSchedulerDate(toDate));
			return result.stream().map(d -> DateUtils.toSchedulerLocalDateTime(d)).collect(Collectors.toList());

		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public static List<LocalDateTime> computerFireTimes(Trigger tigger, int numOfTime) {
		return TriggerUtils.computeFireTimes((OperableTrigger) tigger, new BaseCalendar(), numOfTime)
				.stream()
				.map(d -> DateUtils.toSchedulerLocalDateTime(d))
				.collect(Collectors.toList());
	}
	
	public static List<JobKey> getAllJobKey(Scheduler scheduler) {
		List<JobKey> result = Lists.newArrayList();
		try {
			for (String groupName : scheduler.getJobGroupNames()) {
				result.addAll(scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName)));

			}
		} catch (SchedulerException e) {
			throw new PortalSchedulerException(e);
		}
		return result;
	}
	
	public static String schedulerInfo(Scheduler scheduler){
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("Seheduler Name : %s ", scheduler.getSchedulerName())).append("\n");
			sb.append("*********Started?******** : " + scheduler.isStarted()).append("\n");
			sb.append("*********Shutdown?******* : " + scheduler.isShutdown()).append("\n");
			for (JobKey jobKey : getAllJobKey(scheduler)) {
				String jobName = jobKey.getName();
				String jobGroup = jobKey.getGroup();
				
				for (Trigger trigger : scheduler.getTriggersOfJob(jobKey)) {
					for (LocalDateTime nextFireTime : computerFireTimes(trigger, 20)) {
						sb.append(String.format("Job (%s) will fire at trigger point (%s)  %s (%s) ", jobName + ":" + jobGroup, trigger.getKey()
								.getName() + ":" + trigger.getKey().getGroup(), nextFireTime, nextFireTime.getDayOfWeek()));
						sb.append("\n");
					}
					sb.append("------");
					sb.append("\n");
				}
			}
			return sb.toString();
		} catch (Exception e) {
			System.out.println("Error when printing the Scheduler info");
			return "";
		}
		
	}

	public static void printSchedulerInfo(Scheduler scheduler) {
		System.out.println(schedulerInfo(scheduler));
	}
	
}
