/**
 * 
 */
package com.kooppi.nttca.wallet.common.persistence.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.kooppi.nttca.portal.common.domain.Modifiable;
import com.kooppi.nttca.portal.common.persistence.convertor.LocalDateTimePersistenceConverter;
import com.kooppi.nttca.portal.wallet.domain.TransactionReservationStatus;
import com.kooppi.nttca.portal.wallet.dto.wallet.TransactionReservationDto;

/**
 * @author jasonho
 *
 */
@Entity
//@Table(name = "transaction_reservation")
public class TransactionReservation extends Modifiable {

	private static final long serialVersionUID = -5376518531673721325L;
	private static final String TRANSACTION_ID_PREFIX = "R";
	@Column(name = "payment_id")
	private String paymentId;
	
	@Column(name = "transaction_id")
	private String transactionId;
	
	@Column(name = "wallet_id")
	private String walletId;
	
	@Column(name = "credit_pool_id")
	private String creditPoolId;
	
	@Column(name = "service_id")
	private String serviceId;
	
	@Column(name = "request_id")
	private String requestId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private TransactionReservationStatus status;
	
	@Column(name = "item")
	private String item;
	
	@Column(name = "item_type")
	private String itemType;
	
	@Column(name = "amount")
	private Integer amount;
	
	@Convert(converter = LocalDateTimePersistenceConverter.class)
	@Column(name = "expired_date")
	private LocalDateTime expiredDate;
	
	@Column(name = "action")
	private String action;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "user_id")
	private String userId;

	@OneToMany(cascade=CascadeType.PERSIST,fetch=FetchType.LAZY, orphanRemoval=true)
	@JoinColumn(name = "transaction_id",referencedColumnName = "transaction_id")
	private List<ChargeItem> chargeItems = Lists.newArrayList();
	
	public static TransactionReservation create(String walletId,String creditPoolId,TransactionReservationDto trDto, String requestId,String userId) {
		
		TransactionReservation creditReservation = new TransactionReservation();
		creditReservation.transactionId = UUID.randomUUID().toString();
		creditReservation.paymentId = trDto.getPaymentId();
		creditReservation.walletId = walletId;
		creditReservation.creditPoolId = creditPoolId;
		creditReservation.serviceId = trDto.getServiceId();
		creditReservation.requestId = requestId;
		creditReservation.status = TransactionReservationStatus.RESERVED;
		creditReservation.item = trDto.getItem();
		creditReservation.itemType = trDto.getItemType();
		creditReservation.amount = trDto.getAmount();
		creditReservation.expiredDate = trDto.getExpiredDate();
		creditReservation.action = trDto.getAction();
		creditReservation.description = trDto.getDescription();
		creditReservation.userId = userId;
		creditReservation.setModifiedUserId(userId);
		trDto.getChargingItems().forEach(c->{
			creditReservation.chargeItems.add(ChargeItem.create(creditReservation.transactionId,c));
		});
		return creditReservation;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public String getWalletId() {
		return walletId;
	}
	
	public String getPaymentId(){
		return paymentId;
	}

	public String getCreditPoolId() {
		return creditPoolId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public String getRequestId() {
		return requestId;
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

	public String getItemType() {
		return itemType;
	}

	public Integer getAmount() {
		return amount;
	}

	public LocalDateTime getExpiredDate() {
		return expiredDate;
	}

	public String getAction() {
		return action;
	}

	public String getDescription() {
		return description;
	}

	public String getUserId() {
		return userId;
	}
	
	public List<ChargeItem> getChargeItems(){
		return chargeItems;
	}
	
	public void updateTransactionId(){
		this.transactionId = Transaction.TRANSACTION_ID_PREFIX + TransactionReservation.TRANSACTION_ID_PREFIX  + Strings.padStart(getUid() + "", 10, '0');
		getChargeItems().forEach(c->c.updatePaymentId(this.transactionId));
	}
	
	public TransactionReservationDto toTransactionReservationDto() {
	TransactionReservationDto dto = new TransactionReservationDto();
	dto.setPaymentId(this.getPaymentId());
	dto.setTransactionId(this.getTransactionId());
	dto.setWalletId(this.getWalletId());
	dto.setCreditPoolId(this.getCreditPoolId());
	dto.setServiceId(this.getServiceId());
	dto.setRequestId(this.getRequestId());
	dto.setStatus(this.getStatus());
	dto.setItem(this.getItem());
	dto.setItemType(this.getItemType());
	dto.setAmount(this.getAmount());
	dto.setExpiredDate(this.getExpiredDate());
	dto.setAction(this.getAction());
	dto.setDescription(this.getDescription());
	dto.setUserId(this.getUserId());
	this.getChargeItems().forEach(c->{
		dto.getChargingItems().add(c.toChargeItemDto());
	});
	return dto;
}
	
	
}
