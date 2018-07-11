package com.kooppi.nttca.portal.ce.dto.payment;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.kooppi.nttca.portal.ce.domain.PaymentStatus;
import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;
import com.kooppi.nttca.portal.common.utils.PortalXmlDateTimeAdapter;

@XmlType(name="")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReservationAndTransactionDto extends ResponseResult {

	@Override
	public String getResultName() {
		return "transactionAndReservation";
	}

	@XmlElement(name = "uid")
	private Long uid;
	
	@XmlElement(name = "userId")
	private String userId;
	
	@XmlElement(name = "organizationName")
	private String organizationName;
	
	@XmlElement(name = "organizationId")
	private String organizationId;
	
	@XmlElement(name = "paymentId")
	private String paymentId;
	
	@XmlElement(name = "status")
	private PaymentStatus paymentStatus;
	
	@XmlElement(name = "transactionDate")
	@XmlJavaTypeAdapter(value = PortalXmlDateTimeAdapter.class)
	private LocalDateTime transactionDate;
	
	@XmlElement(name = "lastUpdateDate")
	@XmlJavaTypeAdapter(value = PortalXmlDateTimeAdapter.class)
	private LocalDateTime lastUpdateDate;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}

	public LocalDateTime getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
}
