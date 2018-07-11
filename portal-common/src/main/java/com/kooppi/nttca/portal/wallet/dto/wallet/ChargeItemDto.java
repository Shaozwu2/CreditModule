package com.kooppi.nttca.portal.wallet.dto.wallet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value ="Charge Item", description = "Charge Item resource representation")
public class ChargeItemDto {

	@XmlElement(name = "quantity")
	@ApiModelProperty(required = true, example = "100", dataType = "Integer")
	private Integer quantity;
	
	@XmlElement(name = "amount")
	@ApiModelProperty(required = true ,example = "100", dataType = "Integer")
	private Integer amount;

	public void validateCreateReservation() {
		PortalExceptionUtils.throwIfNull(quantity, PortalErrorCode.MISS_PAYLOAD_PARAM_QUANTITY);
		PortalExceptionUtils.throwIfNull(amount, PortalErrorCode.MISS_PAYLOAD_PARAM_AMOUNT);
	}
	
	public int getQuantity() {
		return quantity;
	}

	public int getAmount() {
		return amount;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
}
