package com.kooppi.nttca.wallet.rest.report.repository;

import java.time.YearMonth;
import java.util.List;

import com.kooppi.nttca.wallet.common.persistence.domain.Report;

public interface ReportRepository {
	
	public List<Object[]> getReportDataList(Integer year, Integer month);
	
	public void createOrUpdateReport(Report report, Integer year, Integer month);
	
	public byte[] getReportDataFromDB(Integer year, Integer month);
	
	public List<YearMonth> getAllReportYearMonth();

}
