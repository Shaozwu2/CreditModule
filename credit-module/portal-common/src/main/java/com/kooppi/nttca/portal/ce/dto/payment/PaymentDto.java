package com.kooppi.nttca.portal.ce.dto.payment;

import java.time.LocalDateTime;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.google.common.collect.Lists;
import com.kooppi.nttca.portal.ce.domain.PaymentStatus;
import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;
import com.kooppi.nttca.portal.common.utils.PortalXmlDateTimeAdapter;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;

@XmlType(name="")
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentDto extends ResponseResult {

	@Override
	public String getResultName() {
		return "payment";
	}
	
	@XmlElement(name = "paymentId")
	private String paymentId;
	
	@XmlElement(name = "walletId")
	private String walletId;
	
	@XmlElement(name = "userId")
	private String userId;
	
	@XmlElement(name = "organizationName")
	 private String organizationName;
	
	@XmlElement(name = "walletTotalAmount")
	private Integer walletTotalAmount;

	@XmlElement(name = "compensationTotalAmount")
	private Integer compensationTotalAmount;
	
	@XmlElement(name = "description")
	private String description;
	
	@XmlElement(name = "expiredDate")
	@XmlJavaTypeAdapter(value = PortalXmlDateTimeAdapter.class)
	private LocalDateTime expiredDate;
	
	@XmlElement(name = "chargingItems")
	private List<ChargingItemDto> chargingItems = Lists.newArrayList();

	@XmlElement(name = "status")
	private PaymentStatus paymentStatus;
	
	@XmlTransient
	private boolean isPaymentReady = false;
	
	@XmlElement(name = "transactionDate")
	 @XmlJavaTypeAdapter(value = PortalXmlDateTimeAdapter.class)
	 private LocalDateTime transactionDate;
	
	@XmlElement(name = "reopenedContractIds")
	private ReopenedContractIdDto reopenedContractIds;
	
	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getWalletId() {
		return walletId;
	}

	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getWalletTotalAmount() {
		return walletTotalAmount;
	}

	public void setWalletTotalAmount(Integer walletTotalAmount) {
		this.walletTotalAmount = walletTotalAmount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(LocalDateTime expiredDate) {
		this.expiredDate = expiredDate;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public List<ChargingItemDto> getChargingItems() {
		return chargingItems;
	}
	
	public void setChargingItems(List<ChargingItemDto> chargingItems) {
		this.chargingItems = chargingItems;
	}

	public boolean isPaymentReady() {
		return isPaymentReady;
	}

	public void setPaymentReady(boolean isPaymentReady) {
		this.isPaymentReady = isPaymentReady;
	}

	public int calculateTotalAmount(){
		PortalExceptionUtils.throwIfFalse(isPaymentReady, PortalErrorCode.PAYMENT_NOT_READY);
		int total = 0;
		for (ChargingItemDto chargingItemDto : chargingItems) {
			total += chargingItemDto.getTotalAmount();
		}
		return total;
	}
	
	public void updatePayment(){
		this.isPaymentReady = true;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}

	public ReopenedContractIdDto getReopenedContractIds() {
		return reopenedContractIds;
	}

	public void setReopenedContractIds(ReopenedContractIdDto reopenedContractIds) {
		this.reopenedContractIds = reopenedContractIds;
	}

	public Integer getCompensationTotalAmount() {
		return compensationTotalAmount;
	}

	public void setCompensationTotalAmount(Integer compensationTotalAmount) {
		this.compensationTotalAmount = compensationTotalAmount;
	}
}
