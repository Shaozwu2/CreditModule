package com.kooppi.nttca.wallet.rest.UsageStatistics.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.kooppi.nttca.portal.common.repository.BasicRepository;
import com.kooppi.nttca.wallet.common.persistence.domain.DailyUsageStatistics;
import com.kooppi.nttca.wallet.common.persistence.domain.MonthlyUsageStatistics;
import com.kooppi.nttca.wallet.persistence.producer.WalletDataSource;

@ApplicationScoped
public class UsageStatisticsRepositorySqlImpl extends BasicRepository implements UsageStatisticsRepository{

	private EntityManager em;
	
	UsageStatisticsRepositorySqlImpl() {}
	
	@Inject
	public UsageStatisticsRepositorySqlImpl(@WalletDataSource EntityManager em) {
		this.em = em;
	}
	
	@Override
	public DailyUsageStatistics add(DailyUsageStatistics ds) {
		em.persist(ds);
		em.flush();
		em.refresh(ds);
		return ds;
	}

	@Override
	public MonthlyUsageStatistics add(MonthlyUsageStatistics ms) {
		em.persist(ms);
		em.flush();
		em.refresh(ms);
		return ms;
	}

	@Override
	public List<MonthlyUsageStatistics> findMonthlyUsageStatisticsByWalletId(String walletId) {
		LocalDateTime currentMonth = LocalDateTime.now();
		LocalDateTime last6Months = currentMonth.minusMonths(6);
		
		String query = "from MonthlyUsageStatistics m where m.walletId = :walletId and m.createdDate between :last6Months and :currentMonth";
		TypedQuery<MonthlyUsageStatistics> tq = em.createQuery(query,MonthlyUsageStatistics.class);
		
		tq.setParameter("walletId", walletId);
		tq.setParameter("currentMonth", currentMonth);
		tq.setParameter("last6Months", last6Months);
		return tq.getResultList();
	}

	@Override
	public List<DailyUsageStatistics> findDailyUsageStatisticsByWalletId(String walletId) {
		//TODO
		return null;
	}

}
