package com.kooppi.nttca.wallet.rest.report.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReportCsv {

	private static final SimpleDateFormat DATE_FORMATER = new SimpleDateFormat("dd/MM/yyyy");;
	
	private static final String[] HEADER = {
			"Contract No", "EMS Customer ID", "Company Code", "Customer Name", "Transaction Date",
			"Reference Document No", "Description", "Quantity", "Unit of Measure", "Currency Code",
			"Amount", "GL Code", "Charge Type", "Intercompany Provider", "Intercompany %", "SO Status"
	};
	
	public static String[] create(Object[] reportData){
		String[] result = new String[16];
		for (int i = 0; i< reportData.length; i++) {
			if (i == 4) {
				result[i] = DATE_FORMATER.format((Date) reportData[i]);
			} else if (i == 7) {
				result[i] = String.valueOf(reportData[i]);
				if (!"forfeit".equalsIgnoreCase(result[6]) && !"cancel forfeit".equalsIgnoreCase(result[6])) {
					Integer quantity = Integer.valueOf(result[i]);
					if (quantity > 0)
						result[6] = "Deduction of " + quantity + " unit";
				}
			} else if (i == 15) {
				result[i] = "ACTIVE";
			} else if (i == 16) {
				// new added column, contract last reopen date, no need to write into result
				if (reportData[16] != null) {
					Date contractLastReopenDate = (Date) reportData[16];
					Date chargeDate = (Date) reportData[4];
					if (isSameYearAndMonth(contractLastReopenDate, chargeDate)) {
						// set SO Status to REACTIVE
						result[15] = "REACTIVE";
					}
				}
			} else {
				result[i] = String.valueOf(reportData[i]);
			}
		}
		return result;
	}
	
	public static String[] getHeader(){
		return HEADER;
	}
	
	public static String[] getTitle(Integer year, Integer month) {
		String[] result = new String[16];
		for (int i = 0; i < 16; i++) {
			if (i == 0)
				result[i] = "Report of " + getMonthString(month) + " " + year;
			else
				result[i] = "";
		}
		return result;
	}
	
	private static String getMonthString(Integer month) {
		SimpleDateFormat monthParse = new SimpleDateFormat("MM");
	    SimpleDateFormat monthDisplay = new SimpleDateFormat("MMM");
	    try {
			return monthDisplay.format(monthParse.parse(month.toString()));
		} catch (ParseException e) {
			return "NULL";
		}
	}
	
	private static boolean isSameYearAndMonth(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH));
	}
}
