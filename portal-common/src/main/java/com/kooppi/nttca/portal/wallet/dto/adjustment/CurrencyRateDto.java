package com.kooppi.nttca.portal.wallet.dto.adjustment;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value ="Currency Rate", description = "Currency Rate resource representation")
public class CurrencyRateDto extends ResponseResult {

	@XmlElement(name = "currencyRateId")
	@ApiModelProperty(value = "Currency Rate Id", required = true)
	private Integer currencyRateId;
	
	@XmlElement(name = "fromCurrencyCode")
	@ApiModelProperty(value = "From Currency Code", required = true)
	private String fromCurrencyCode;
	
	@XmlElement(name = "toCurrencyCode")
	@ApiModelProperty(value = "To Currency Code", required = true)
	private String toCurrencyCode;
	
	@XmlElement(name = "exchangeRate")
	@ApiModelProperty(value = "Exchange Rate", required = true)
	private BigDecimal exchangeRate;
	
	@Override
	public String getResultName() {
		return "currencyRate";
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
}
