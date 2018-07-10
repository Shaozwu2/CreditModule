package com.kooppi.nttca.portal.wallet.dto.wallet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value ="ConfirmChargeAmountDto", description = "Confirm Charge Amount resource representation")
public class ConsumeChargeAmountDto {

	@ApiModelProperty( value = "Transaction Id", required = true)
	@XmlElement(name = "transactionId")
	private String transactionId;
	
	@ApiModelProperty( value = "Charge Amount", required = true)
	@XmlElement(name = "chargeAmount")
	private Integer chargeAmount;
	
	public ConsumeChargeAmountDto() {}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Integer getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(Integer chargeAmount) {
		this.chargeAmount = chargeAmount;
	}
	
	public void validateConfirmChargeAmountDto() {
		PortalExceptionUtils.throwIfNullOrEmptyString(transactionId, PortalErrorCode.MISS_PARAM_TRANSACTION_ID);
		PortalExceptionUtils.throwIfNull(chargeAmount, PortalErrorCode.MISS_PARAM_AMOUNT);
	}
	
	public static ConsumeChargeAmountDto createForConfirmChargeAmount(String transactionId, Integer chargeAmount) {
		ConsumeChargeAmountDto dto = new ConsumeChargeAmountDto();
		dto.transactionId = transactionId;
		dto.chargeAmount = chargeAmount;
		return dto;
	}
}
