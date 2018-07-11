package com.kooppi.nttca.portal.wallet.dto.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;
import com.kooppi.nttca.portal.common.utils.PortalXmlDateAdapter;
import com.kooppi.nttca.portal.common.utils.PortalXmlDateTimeAdapter;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;
import com.kooppi.nttca.portal.wallet.domain.Source;
import com.kooppi.nttca.portal.wallet.domain.TransactionState;
import com.kooppi.nttca.portal.wallet.domain.TransactionStatus;
import com.kooppi.nttca.portal.wallet.dto.wallet.ChargeItemDto;

import io.swagger.annotations.ApiModelProperty;
import jersey.repackaged.com.google.common.collect.Lists;

@XmlAccessorType(XmlAccessType.FIELD)
public class TransactionDto extends ResponseResult{
	
	@XmlElement(name = "paymentId")
	@ApiModelProperty(required = false)
	private String paymentId;
	
	@XmlElement(name = "transactionId")
	@ApiModelProperty(required = false, hidden=true)
	private String transactionId;

	@XmlElement(name = "parentTransactionId")
	@ApiModelProperty(required = false, hidden=true)
	private String parentTransactionId;
	
	@XmlElement(name = "state")
	@ApiModelProperty(required = false, hidden=true)
	private TransactionState state;
	
	@XmlElement(name = "status")
	@ApiModelProperty(required = false, hidden=true)
	private TransactionStatus status;
	
	@XmlElement(name = "walletId")
	@ApiModelProperty(required = false, hidden=true)
	private String walletId;
	
	@XmlElement(name = "serviceOrder")
	@ApiModelProperty(required = false, hidden=true)
	private String serviceOrder;
	
	@XmlElement(name = "contractEffectiveDate")
	@ApiModelProperty(required = false, hidden=true)
	@XmlJavaTypeAdapter(value = PortalXmlDateAdapter.class)
	private LocalDate contractEffectiveDate;
	
	@XmlElement(name = "serviceId")
	@ApiModelProperty( value = "Service Id", required = true, example ="service001" )
	private String serviceId;
	
	@XmlElement(name = "requestId")
	@ApiModelProperty(required = false, hidden=true)
	private String requestId;
	
	@XmlElement(name = "item")
	@ApiModelProperty(required = false, hidden=true)
	private String item;
	
	@XmlElement(name = "amount")
	@ApiModelProperty(required = false, hidden=true)
	private Integer walletAmount;

	@XmlElement(name = "compensationAmount")
	@ApiModelProperty(required = false, hidden=true)
	private Integer compensationAmount;
	
	@XmlElement(name = "currencyAmount")
	@ApiModelProperty(required = false, hidden=true)
	private BigDecimal currencyAmount;
	
	@XmlElement(name = "balance")
	@ApiModelProperty(required = false, hidden=true)
	private Integer balance;
	
	@XmlElement(name = "action")
	@ApiModelProperty(required = false, hidden=true)
	private String action;
	
	@XmlElement(name = "description")
	@ApiModelProperty( value = "Description", required = true, example ="description" )
	private String description;
	
	@XmlElement(name = "itemType")
	@ApiModelProperty( value = "itemType", required = true, example ="VM operation" )
	private String itemType;
	
	@XmlElement(name = "source")
	@ApiModelProperty( value = "source", required = true, example ="SERVICE/ADJUSTMENT" )
	private Source source;
	
	@XmlElement(name = "chargeDate")
	@ApiModelProperty(required = false, hidden=true)
	@XmlJavaTypeAdapter(value = PortalXmlDateTimeAdapter.class)
	private LocalDateTime chargeDate;

	@XmlElement(name = "userId")
	@ApiModelProperty(required = false, hidden=true)
	private String userId;
	
	@XmlElement(name = "chargeItems")
	@ApiModelProperty(required = false, hidden=true)
	private List<ChargeItemDto> chargeItems = Lists.newArrayList();
	
//	public static TransactionDto create(Transaction transaction){
//		
//		TransactionDto dto = new TransactionDto();
//		dto.paymentId = transaction.getPaymentId();
//		dto.transactionId = transaction.getTransactionId();
//		dto.parentTransactionId = transaction.getParentTransactionId();
//		dto.state = transaction.getState();
//		dto.status = transaction.getStatus();
//		dto.walletId = transaction.getWalletId();
//		dto.serviceOrder = transaction.getServiceOrder();
//		dto.contractStartDate = transaction.getContractStartDate();
//		dto.serviceId = transaction.getServiceId();
//		dto.requestId = transaction.getRequestId();
//		dto.item = transaction.getItem();
//		dto.itemType = transaction.getItemType();
//		dto.amount = transaction.getAmount();
//		dto.balance = transaction.getBalance();
//		dto.action = transaction.getAction();
//		dto.source = transaction.getSource();
//		dto.description = transaction.getDescription();
//		dto.chargeDate = transaction.getChargeDate();
//		dto.userId = transaction.getUserId();
//		transaction.getChargeItems().forEach(c->{
//			dto.chargeItems.add(c.toChargeItemDto());
//		});
//		return dto;
//	}
	
	@Override
	public String getResultName() {
		return "transaction";
	}

	public String getTransactionId() {
		return transactionId;
	}

	public String getParentTransactionId() {
		return parentTransactionId;
	}
	public TransactionState getState() {
		return state;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public String getWalletId() {
		return walletId;
	}

	public String getServiceOrder() {
		return serviceOrder;
	}

	public LocalDate getContractEffectiveDate() {
		return contractEffectiveDate;
	}

	public String getServiceId() {
		return serviceId;
	}

	public String getRequestId() {
		return requestId;
	}

	public String getItem() {
		return item;
	}

	public Integer getAmount() {
		return walletAmount;
	}

	public Integer getBalance() {
		return balance;
	}

	public String getAction() {
		return action;
	}

	public String getDescription() {
		return description;
	}

	public String getItemType() {
		return itemType;
	}
	public Source getSource() {
		return source;
	}
	public LocalDateTime getChargeDate() {
		return chargeDate;
	}

	public String getUserId() {
		return userId;
	}
	
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public void setParentTransactionId(String parentTransactionId) {
		this.parentTransactionId = parentTransactionId;
	}

	public void setState(TransactionState state) {
		this.state = state;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

	public void setServiceOrder(String serviceOrder) {
		this.serviceOrder = serviceOrder;
	}

	public void setContractEffectiveDate(LocalDate contractEffectiveDate) {
		this.contractEffectiveDate = contractEffectiveDate;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public void setAmount(Integer amount) {
		this.walletAmount = amount;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public void setChargeDate(LocalDateTime chargeDate) {
		this.chargeDate = chargeDate;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	public List<ChargeItemDto> getChargeItems() {
		return chargeItems;
	}

	public void setChargeItems(List<ChargeItemDto> chargeItems) {
		this.chargeItems = chargeItems;
	}
	
	public String getPaymentId() {
		return paymentId;
	}

	public BigDecimal getCurrencyAmount() {
		return currencyAmount;
	}

	public void setCurrencyAmount(BigDecimal currencyAmount) {
		this.currencyAmount = currencyAmount;
	}

	public void validateReverseTransaction() {
		//validate mandatory field
		PortalExceptionUtils.throwIfNullOrEmptyString(serviceId, PortalErrorCode.MISS_PARAM_SERVICE_ID);
//		PortalExceptionUtils.throwIfNullOrEmptyString(description, PortalErrorCode.MISS_PARAM_DESCRIPTION);
//		WalletException.throwIfNullOrEmptyString(itemType, PortalErrorCode.MISS_PARAM_ITEM_TYPE);
//		WalletException.throwIfNullOrEmptyString(source, PortalErrorCode.MISS_PARAM_DESCRIPTION);
	}
	
	public static TransactionDto createForReverse(String serviceId, String description){
		TransactionDto dto = new TransactionDto();
		dto.serviceId = serviceId;
		dto.description = description;
		return dto;
	}
}
