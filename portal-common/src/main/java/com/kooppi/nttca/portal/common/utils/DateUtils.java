package com.kooppi.nttca.portal.common.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
	
	public static final DateTimeFormatter CLIENT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	
	public static final DateTimeFormatter CLIENT_DISPLAY_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

	public static final DateTimeFormatter STATISTICS_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd", Locale.getDefault());
	
	public static LocalDateTime toSchedulerLocalDateTime(Date date) {
		if (date == null) {
			return null;
		}
		Instant instant = Instant.ofEpochMilli(date.getTime());
		LocalDateTime res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		return res;
	}
	
	public static Date toSchedulerDate(LocalDate localDate) {
		if (localDate == null) {
			return null;
		}
		Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
		Date res = Date.from(instant);
		return res;
	}
	
	public static Date toSchedulerDate(LocalDateTime localDateTime){
		if(localDateTime == null){
			return null;
		}
		Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
		return Date.from(instant);
	}
	
	public static String formatClient(LocalDateTime localDateTime) {
		return CLIENT_DATE_FORMATTER.format(ZonedDateTime.of(localDateTime, ZoneId.systemDefault()));
	}

	public static String formatStatisticsDate(LocalDateTime localDateTime) {
		return STATISTICS_DATE_FORMATTER.format(ZonedDateTime.of(localDateTime, ZoneId.of("UTC")));
	}
	
	public static String formatClientDisplay(LocalDateTime localDateTime) {
		return CLIENT_DISPLAY_DATETIME_FORMATTER.format(ZonedDateTime.of(localDateTime, ZoneId.systemDefault()));
	}
	
	public static String formatClientDisplay(Date dateToConvert) {
		LocalDateTime localDateTime = LocalDateTime.ofInstant(dateToConvert.toInstant(), ZoneId.systemDefault());
		return CLIENT_DISPLAY_DATETIME_FORMATTER.format(localDateTime);
	}

	public static java.sql.Timestamp localDateTimeToTimestamp(LocalDateTime entityValue) {
		return (entityValue == null) ? null : Timestamp.valueOf(entityValue);
	}

	public static java.sql.Timestamp localDateToTimestamp(LocalDate entityValue) {
		return (entityValue == null) ? null : Timestamp.valueOf(entityValue
				.atStartOfDay());
	}
	
	public static LocalDateTime TimestampToLocalDateTime(Timestamp databaseValue) {
		return databaseValue == null ? null : databaseValue.toLocalDateTime();
	}

	public static LocalDate TimestampToLocalDate(Timestamp databaseValue) {
		return databaseValue == null ? null : databaseValue.toLocalDateTime().toLocalDate();
	}
	//convert int month to "MMM" format.
	public static String formatMonth(int month) {

	    String monthName="";
	    try {

	        Calendar calendar = Calendar.getInstance();
	        calendar.set(Calendar.MONTH, month-1);

	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM", Locale.ENGLISH);
	        simpleDateFormat.setCalendar(calendar);
	        monthName = simpleDateFormat.format(calendar.getTime());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return monthName;
	}
	
	public static void main(String[] args) {
		System.out.println(DateUtils.formatMonth(12));
	}

}
