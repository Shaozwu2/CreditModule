package com.kooppi.nttca.wallet.rest.UsageStatistics.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.google.common.collect.Lists;
import com.kooppi.nttca.portal.wallet.dto.statistics.StatisticsDataDto;
import com.kooppi.nttca.wallet.common.persistence.domain.DailyUsageStatistics;
import com.kooppi.nttca.wallet.common.persistence.domain.MonthlyUsageStatistics;
import com.kooppi.nttca.wallet.rest.UsageStatistics.repository.UsageStatisticsRepository;

@ApplicationScoped
public class UsageStatisticsServiceImpl implements UsageStatisticsService{
	
	@Inject
	private UsageStatisticsRepository usageStatisticsRepository;

	@Override
	public DailyUsageStatistics createDailyUsageStatistics(String walletId, Integer totalAmount, Integer month, Integer day) {
		DailyUsageStatistics dailyStatistics = DailyUsageStatistics.create(walletId, totalAmount, month, day);
		return usageStatisticsRepository.add(dailyStatistics);
		
	}

	@Override
	public MonthlyUsageStatistics createMonthlyUsageStatistics(String walletId, Integer totalAmount, Integer year, Integer month, Integer day) {
		MonthlyUsageStatistics monthlyStatistics = MonthlyUsageStatistics.create(walletId, totalAmount, year, month, day);
		return usageStatisticsRepository.add(monthlyStatistics);
	}

	@Override
	public List<StatisticsDataDto> getMonthlyUsageStatisticsDataList(String walletId) {
		List<MonthlyUsageStatistics> statisticsDatas = usageStatisticsRepository.findMonthlyUsageStatisticsByWalletId(walletId);
		
		List<StatisticsDataDto> datas = Lists.newArrayList();
		for (int i = 0; i < statisticsDatas.size(); i++) {
			datas.add(StatisticsDataDto.create(statisticsDatas.get(i).getCreatedDate(), statisticsDatas.get(i).getAmount(), statisticsDatas.get(i).getMonth()));
		}

		for (int j = 6; j > statisticsDatas.size(); j--) {
			datas.add(StatisticsDataDto.create(LocalDateTime.now().minusMonths(j), 0, LocalDateTime.now().minusMonths(j).getMonthValue()));
		}
		
		Collections.sort(datas, new UsageStatisticsComparator());
		
		return datas;
	}
	

	@Override
	public List<StatisticsDataDto> getDailyUsageStatisticsDataList(String walletId) {
		List<DailyUsageStatistics> statisticsDatas = usageStatisticsRepository.findDailyUsageStatisticsByWalletId(walletId);
		
		List<StatisticsDataDto> datas = Lists.newArrayList();
		//TODO
		
		Collections.sort(datas, new UsageStatisticsComparator());
		
		return datas;
	}

	private class UsageStatisticsComparator implements Comparator<StatisticsDataDto> {
		@Override
		public int compare(StatisticsDataDto d1, StatisticsDataDto d2) {
			return d1.getTemporarilyDate().compareTo(d2.getTemporarilyDate());
		}
	}
}
