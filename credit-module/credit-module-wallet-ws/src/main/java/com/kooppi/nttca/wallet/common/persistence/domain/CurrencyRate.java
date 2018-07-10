package com.kooppi.nttca.wallet.common.persistence.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.kooppi.nttca.portal.wallet.dto.adjustment.CurrencyRateDto;

@Entity
@Table(name = "currency_rate")
public class CurrencyRate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "currency_rate_id")
	private Integer currencyRateId;
	
	@Column(name = "from_currency_code")
	private String fromCurrencyCode;

	
	@Column(name = "to_currency_code")
	private String toCurrencyCode;
	
	@Column(name = "exchange_rate")
	private BigDecimal exchangeRate;
	
	public CurrencyRate create(String fromCurrencyCode, BigDecimal exchangeRate) {
		CurrencyRate currencyRate = new CurrencyRate();
		currencyRate.fromCurrencyCode = fromCurrencyCode;
		currencyRate.toCurrencyCode = "HKD";
		currencyRate.exchangeRate = exchangeRate;
		return currencyRate;
	}

	public Integer getCurrencyRateId() {
		return currencyRateId;
	}

	public void setCurrencyRateId(Integer currencyRateId) {
		this.currencyRateId = currencyRateId;
	}

	public String getFromCurrencyCode() {
		return fromCurrencyCode;
	}

	public void setFromCurrencyCode(String fromCurrencyCode) {
		this.fromCurrencyCode = fromCurrencyCode;
	}

	public String getToCurrencyCode() {
		return toCurrencyCode;
	}

	public void setToCurrencyCode(String toCurrencyCode) {
		this.toCurrencyCode = toCurrencyCode;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	
	public CurrencyRateDto toCurrencyRateDto() {
		CurrencyRateDto dto = new CurrencyRateDto();
		dto.setCurrencyRateId(this.getCurrencyRateId());
		dto.setFromCurrencyCode(this.fromCurrencyCode);
		dto.setToCurrencyCode(this.toCurrencyCode);
		dto.setExchangeRate(this.exchangeRate);
		return dto;
	}
}
