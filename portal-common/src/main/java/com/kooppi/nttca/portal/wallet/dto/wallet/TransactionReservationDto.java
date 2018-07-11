package com.kooppi.nttca.portal.wallet.dto.wallet;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.google.common.collect.Lists;
import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;
import com.kooppi.nttca.portal.common.utils.PortalXmlDateTimeAdapter;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;
import com.kooppi.nttca.portal.wallet.domain.TransactionReservationStatus;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value ="Transaction Reservation", description = "Transaction Reservation resource representation")
public class TransactionReservationDto extends ResponseResult {
	
	@XmlElement(name = "paymentId")
	@ApiModelProperty(value = "paymentId" ,required = true, hidden=true)
	private String paymentId;
	
	@XmlElement(name = "transactionId")
	@ApiModelProperty(required = false, hidden=true)
	private String transactionId;
	
	@XmlElement(name = "walletId")
	@ApiModelProperty(required = false, hidden=true)
	private String walletId;
	
	@XmlElement(name = "creditPoolId")
	@ApiModelProperty(required = false, hidden=true)
	private String creditPoolId;
	
	@ApiModelProperty( value = "Service Id", required = true, example ="service001" )
	@XmlElement(name = "serviceId")
	private String serviceId;
	
	@ApiModelProperty(required = false, hidden=true)
	@XmlElement(name = "requestId")
	private String requestId;
	
	@ApiModelProperty(required = false, hidden=true)
	@XmlElement(name = "status")
	private TransactionReservationStatus status;
	
	@ApiModelProperty( value = "Item", required = true, example ="item001")
	@XmlElement(name = "item")
	private String item;
	
	@ApiModelProperty( value = "Item Type", required = true, example ="type001")
	@XmlElement(name = "itemType")
	private String itemType;
	
	@ApiModelProperty( value = "Amount", required = true, example ="100")
	@XmlElement(name = "amount")
	private Integer amount;
	
	@ApiModelProperty( value = "Expired Date", required = true, dataType="date", example="2016-07-25")
	@XmlElement(name = "expiredDate")
	@XmlJavaTypeAdapter(value = PortalXmlDateTimeAdapter.class)
	private LocalDateTime expiredDate;

	@ApiModelProperty( value = "Action", required = true, example ="reverse credit")
	@XmlElement(name = "action")
	private String action;
	
	@ApiModelProperty( value = "Description", required=false)
	@XmlElement(name = "description")
	private String description;
	
	@ApiModelProperty(required = false, hidden=true)
	@XmlElement(name = "userId")
	private String userId;
	
	@ApiModelProperty(required = true, hidden=false)
	@XmlElement(name = "chargeItems")
	private List<ChargeItemDto> chargeItems = Lists.newArrayList();
	
//	public static TransactionReservationDto create(TransactionReservation transactionReservation) {
//		TransactionReservationDto dto = new TransactionReservationDto();
//		dto.paymentId = transactionReservation.getPaymentId();
//		dto.transactionId = transactionReservation.getTransactionId();
//		dto.walletId = transactionReservation.getWalletId();
//		dto.creditPoolId = transactionReservation.getCreditPoolId();
//		dto.serviceId = transactionReservation.getServiceId();
//		dto.requestId = transactionReservation.getRequestId();
//		dto.status = transactionReservation.getStatus();
//		dto.item = transactionReservation.getItem();
//		dto.itemType = transactionReservation.getItemType();
//		dto.amount = transactionReservation.getAmount();
//		dto.expiredDate = transactionReservation.getExpiredDate();
//		dto.action = transactionReservation.getAction();
//		dto.description = transactionReservation.getDescription();
//		dto.userId = transactionReservation.getUserId();
//		transactionReservation.getChargeItems().forEach(c->{
//			dto.chargeItems.add(ChargeItemDto.create(c));
//		});
//		return dto;
//	}
	
	@Override
	public String getResultName() {
		return "transactionReservation";
	}

	public String getPaymentId(){
		return paymentId;
	}
	
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getWalletId() {
		return walletId;
	}

	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

	public String getCreditPoolId() {
		return creditPoolId;
	}

	public void setCreditPoolId(String creditPoolId) {
		this.creditPoolId = creditPoolId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public TransactionReservationStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionReservationStatus status) {
		this.status = status;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public LocalDateTime getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(LocalDateTime expiredDate) {
		this.expiredDate = expiredDate;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public List<ChargeItemDto> getChargingItems(){
		return chargeItems;
	}

	
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public void  validateCreateTransactionReservation() {
		//validate mandatory field
		PortalExceptionUtils.throwIfNullOrEmptyString(paymentId, PortalErrorCode.MISS_PAYLOAD_PARAM_PAYMENT_ID);
		PortalExceptionUtils.throwIfNull(amount, PortalErrorCode.MISS_PAYLOAD_PARAM_AMOUNT);
		PortalExceptionUtils.throwIfNull(expiredDate, PortalErrorCode.MISS_PAYLOAD_PARAM_EXPIRED_DATE);
		PortalExceptionUtils.throwIfTrue(chargeItems.size() == 0 , PortalErrorCode.MISS_PAYLOAD_PARAM_CHARGE_ITEMS);
		chargeItems.forEach(c->c.validateCreateReservation());
	}
	
	public void validateExpiryDate(int configExpiryDate) {
		long diffInHours = Duration.between(LocalDateTime.now(), this.getExpiredDate()).toHours();
		PortalExceptionUtils.throwIfFalse((diffInHours < configExpiryDate) && (diffInHours >= 0), PortalErrorCode.EXPIRED_DATE_IS_INVALID);
	}
	
}
