package com.kooppi.nttca.wallet.rest.UsageStatistics.service;

import java.util.List;

import com.kooppi.nttca.portal.wallet.dto.statistics.StatisticsDataDto;
import com.kooppi.nttca.wallet.common.persistence.domain.DailyUsageStatistics;
import com.kooppi.nttca.wallet.common.persistence.domain.MonthlyUsageStatistics;

public interface UsageStatisticsService {

	public DailyUsageStatistics createDailyUsageStatistics(String walletId, Integer totalAmount, Integer month, Integer day);
	
	public MonthlyUsageStatistics createMonthlyUsageStatistics(String walletId, Integer totalAmount, Integer year, Integer month, Integer day);
	
	public List<StatisticsDataDto> getMonthlyUsageStatisticsDataList(String walletId);

	public List<StatisticsDataDto> getDailyUsageStatisticsDataList(String walletId);
}
