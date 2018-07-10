package com.kooppi.nttca.portal.ce.dto.payment;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.kooppi.nttca.portal.ce.domain.ChargeType;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;

@XmlAccessorType(XmlAccessType.FIELD)
public class ChargingItemDto {
	
	@XmlElement(name = "chargeType")
	private ChargeType chargeType;
	
	@XmlElement(name = "unitPrice")
	private BigDecimal unitPrice;
	
	@XmlElement(name = "quantity")
	private Integer quantity;
	
	@XmlElement(name = "totalAmount")
	private Integer totalAmount;
	
	@XmlElement(name = "partNo")
	private String partNo;
	
	@XmlElement(name = "productName")
	private String productName;
	
	@XmlTransient
	private boolean isItemReady = false;

	public boolean isItemReady() {
		return isItemReady;
	}
	
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	public ChargeType getChargeType() {
		return chargeType;
	}

	public void setChargeType(ChargeType chargeType) {
		this.chargeType = chargeType;
	}

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void updateEmsPrice(String partNo, String productName, Integer price) {
		this.partNo = partNo;
		this.productName = productName;
		this.totalAmount = price * quantity;
		this.isItemReady = true;
	}
	
	public void validateCreatePayment() {
		PortalExceptionUtils.throwIfNullOrEmptyString(partNo, PortalErrorCode.PAYMENT_MISS_PAYLOAD_PARAM_PARTNO);
		PortalExceptionUtils.throwIfNull(chargeType, PortalErrorCode.PAYMENT_MISS_PAYLOAD_PARAM_CHARGE_TYPE);
		PortalExceptionUtils.throwIfNull(quantity, PortalErrorCode.PAYMENT_MISS_PAYLOAD_PARAM_QUANTITY);
	}
}
