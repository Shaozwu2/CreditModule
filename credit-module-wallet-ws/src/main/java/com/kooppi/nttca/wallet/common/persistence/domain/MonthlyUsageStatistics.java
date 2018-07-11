package com.kooppi.nttca.wallet.common.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.kooppi.nttca.portal.common.domain.BasicEntity;

@Entity
@Table(name = "monthly_usage_statistics")
public class MonthlyUsageStatistics extends BasicEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7690879832117058000L;

	@Column(name = "wallet_id")
	private String walletId;
	
	@Column(name = "amount")
	private Integer amount;
	
	@Column(name = "year")
	private Integer year;
	
	@Column(name = "month")
	private Integer month;
	
	@Column(name = "day")
	private Integer day;
	
	public static MonthlyUsageStatistics create(String walletId, Integer amount, Integer year, Integer month, Integer day) {
		MonthlyUsageStatistics monthlyUsageStatistics = new MonthlyUsageStatistics();
		monthlyUsageStatistics.walletId = walletId;
		monthlyUsageStatistics.amount = amount;
		monthlyUsageStatistics.year = year;
		monthlyUsageStatistics.month = month;
		monthlyUsageStatistics.day = day;
		return monthlyUsageStatistics;
	}
	
	public String getWalletId() {
		return walletId;
	}

	public Integer getAmount() {
		return amount;
	}

	public Integer getYear() {
		return year;
	}

	public Integer getMonth() {
		return month;
	}

	public Integer getDay() {
		return day;
	}

}
