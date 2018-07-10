package com.kooppi.nttca.wallet.rest.UsageStatistics.repository;

import java.util.List;

import com.kooppi.nttca.wallet.common.persistence.domain.DailyUsageStatistics;
import com.kooppi.nttca.wallet.common.persistence.domain.MonthlyUsageStatistics;

public interface UsageStatisticsRepository {
	
	public DailyUsageStatistics add(DailyUsageStatistics ds);
	
	public MonthlyUsageStatistics add(MonthlyUsageStatistics ms);
	
	public List<MonthlyUsageStatistics> findMonthlyUsageStatisticsByWalletId(String walletId);

	public List<DailyUsageStatistics> findDailyUsageStatisticsByWalletId(String walletId);
}
