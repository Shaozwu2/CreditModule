package com.kooppi.nttca.portal.wallet.dto.adjustment;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;

import jersey.repackaged.com.google.common.collect.Lists;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder ={"currencyRates", "count"})
public class CurrencyRateCollectionDto extends ResponseResult {

	@XmlElement(name = "currencyRates")
	private List<CurrencyRateDto> currencyRates = Lists.newArrayList();
	
	@XmlElement(name = "count")
	private Integer count;
	
	public static CurrencyRateCollectionDto create() {
		return new CurrencyRateCollectionDto();
	}
	
	public static CurrencyRateCollectionDto create(List<CurrencyRateDto> currencyRates, Integer count) {
		CurrencyRateCollectionDto dto = new CurrencyRateCollectionDto();
		dto.currencyRates = currencyRates;
		dto.count = count;
		return dto;
	}
	
	@Override
	public String getResultName() {
		return "currencyRateList";
	}

	public List<CurrencyRateDto> getCurrencyRates() {
		return currencyRates;
	}

	public void setCurrencyRates(List<CurrencyRateDto> currencyRates) {
		this.currencyRates = currencyRates;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}
