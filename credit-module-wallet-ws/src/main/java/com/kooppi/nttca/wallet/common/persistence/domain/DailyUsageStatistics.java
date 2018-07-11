package com.kooppi.nttca.wallet.common.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.kooppi.nttca.portal.common.domain.BasicEntity;

@Entity
@Table(name = "daily_usage_statistics")
public class DailyUsageStatistics extends BasicEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1263063927201406589L;

	@Column(name = "wallet_id")
	private String walletId;
	
	@Column(name = "amount")
	private Integer amount;
	
	@Column(name = "month")
	private Integer month;

	@Column(name = "day")
	private Integer day;
	
	public static DailyUsageStatistics create(String walletId, Integer amount, Integer month, Integer day) {
		DailyUsageStatistics dailyUsageStatistics = new DailyUsageStatistics();
		dailyUsageStatistics.walletId = walletId;
		dailyUsageStatistics.amount = amount;
		dailyUsageStatistics.month = month;
		dailyUsageStatistics.day = day;
		return dailyUsageStatistics;
	}
	
	public String getWalletId() {
		return walletId;
	}

	public Integer getAmount() {
		return amount;
	}

	public Integer getMonth() {
		return month;
	}

	public Integer getDay() {
		return day;
	}

}
