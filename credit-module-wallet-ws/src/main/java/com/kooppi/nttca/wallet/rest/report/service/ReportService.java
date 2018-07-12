package com.kooppi.nttca.wallet.rest.report.service;

import java.time.YearMonth;
import java.util.List;

import com.kooppi.nttca.wallet.common.persistence.domain.Report;

public interface ReportService {
	
	public List<Object[]> getReportDataList(Integer year, Integer month);
	
	public void createOrUpdateReport(Report report, Integer year, Integer month);
	
	public byte[] getReportDataFromDB(Integer year, Integer month);
	
	public List<YearMonth> getAllReportYearMonth();

}
