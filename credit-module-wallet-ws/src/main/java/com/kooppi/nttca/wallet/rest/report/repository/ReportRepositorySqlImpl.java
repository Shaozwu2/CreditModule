package com.kooppi.nttca.wallet.rest.report.repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import com.kooppi.nttca.portal.common.repository.BasicRepository;
import com.kooppi.nttca.wallet.common.persistence.domain.Report;
import com.kooppi.nttca.wallet.persistence.producer.WalletDataSource;

@ApplicationScoped
public class ReportRepositorySqlImpl extends BasicRepository implements ReportRepository {
	
	private EntityManager em;
	
	ReportRepositorySqlImpl() {}
	
	@Inject
	public ReportRepositorySqlImpl(@WalletDataSource EntityManager em) {
		this.em = em;
	}
	
	@Override
	public List<Object[]> getReportDataList(Integer year, Integer month) {
		StoredProcedureQuery query = em.createStoredProcedureQuery("generateReport")
				.registerStoredProcedureParameter("inputYear", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("inputMonth", Integer.class, ParameterMode.IN)
				.setParameter("inputYear", year)
				.setParameter("inputMonth", month);

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = query.getResultList();
		
		return resultList;
	}
	
	@Override
	public void createOrUpdateReport(Report report, Integer year, Integer month) {
		// delete record if already exists
		Query query = em.createQuery("DELETE FROM Report r WHERE FUNC('YEAR', r.reportDate) = :year AND FUNC('MONTH', r.reportDate) = :month");
		query.setParameter("year", year);
		query.setParameter("month", month);
		query.executeUpdate();
		
		// insert new report record
		em.persist(report);
		em.flush();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public byte[] getReportDataFromDB(Integer year, Integer month) {
		Query query = em.createQuery("SELECT r FROM Report r WHERE FUNC('YEAR', r.reportDate) = :year AND FUNC('MONTH', r.reportDate) = :month");
		query.setParameter("year", year);
		query.setParameter("month", month);
		List<Report> reportList = query.getResultList();
		
		if (reportList != null && reportList.size() > 0) {
			return reportList.get(0).getReportFile();
		} else {
			return new byte[0];
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<YearMonth> getAllReportYearMonth() {
		Query query = em.createQuery("SELECT r.reportDate FROM Report r ORDER BY r.reportDate DESC");
		List<Date> reportDateList = query.getResultList();
		
		List<YearMonth> resultList = new ArrayList<>();
		for (Date reportDate : reportDateList) {
			LocalDate localDate = reportDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			YearMonth yearMonth = YearMonth.from(localDate);
			resultList.add(yearMonth);
		}
		
		return resultList;
	}

}
