package com.kooppi.nttca.wallet.rest.report.service;

import java.time.YearMonth;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.kooppi.nttca.wallet.common.persistence.domain.Report;
import com.kooppi.nttca.wallet.rest.report.repository.ReportRepository;

@ApplicationScoped
public class ReportServiceImpl implements ReportService {
	
	@Inject
	private ReportRepository reportRepository;
	
	@Override
	public List<Object[]> getReportDataList(Integer year, Integer month) {
		return reportRepository.getReportDataList(year, month);
	}
	
	@Override
	public void createOrUpdateReport(Report report) {
		reportRepository.createOrUpdateReport(report);
	}
	
	@Override
	public byte[] getReportDataFromDB(Integer year, Integer month) {
		return reportRepository.getReportDataFromDB(year, month);
	}
	
	@Override
	public List<YearMonth> getAllReportYearMonth() {
		return reportRepository.getAllReportYearMonth();
	}

}
