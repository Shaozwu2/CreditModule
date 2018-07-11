package com.kooppi.nttca.ce.domain;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kooppi.nttca.portal.common.domain.BasicEntity;
import com.kooppi.nttca.portal.common.persistence.convertor.LocalDateTimePersistenceConverter;
import com.kooppi.nttca.portal.wallet.domain.TransactionReservationStatus;
import com.kooppi.nttca.portal.wallet.dto.wallet.TransactionReservationDto;

@Entity
@Table(name = "cm_wallet_transaction_reservation_history")
public class WalletTransactionReservationHistory extends BasicEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5760371395532896037L;

	@Column(name = "payment_status_uid" , updatable = false, insertable = false)
	private Long paymentStatusUid;
	
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
	
	@Column(name = "item_name")
	private String itemName;
	
	@Column(name = "item_type")
	private String itemType;
	
	@Column(name = "amount")
	private Integer amount;
	
	@Column(name = "action")
	private String action;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "user_id")
	private String userId;
	
	@Convert(converter = LocalDateTimePersistenceConverter.class)
	@Column(name = "expired_date")
	private LocalDateTime expiredDate;
	
	@OneToOne(cascade=CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_status_uid", referencedColumnName = "uid")
	private PaymentStatusHistory parentStatusHistory;

	public Long getPaymentStatusUid() {
		return paymentStatusUid;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public String getWalletId() {
		return walletId;
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

	public String getItemName() {
		return itemName;
	}

	public String getItemType() {
		return itemType;
	}

	public Integer getAmount() {
		return amount;
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

	public LocalDateTime getExpiredDate() {
		return expiredDate;
	}
	
	public void updateParentStatusHistory(PaymentStatusHistory parent){
		this.parentStatusHistory = parent;
	}
	
	public boolean isReserved(){
		return status == TransactionReservationStatus.RESERVED;
	}
	
	public static WalletTransactionReservationHistory create(TransactionReservationDto dto){
		WalletTransactionReservationHistory tr = new WalletTransactionReservationHistory();
		tr.paymentId = dto.getPaymentId();
		tr.transactionId = dto.getTransactionId();
		tr.walletId = dto.getWalletId();
		tr.creditPoolId = dto.getCreditPoolId();
		tr.serviceId = dto.getServiceId();
		tr.requestId = dto.getRequestId();
		tr.status = dto.getStatus();
		tr.itemName = dto.getItem();
		tr.itemType = dto.getItemType();
		tr.amount = dto.getAmount();
		tr.action = dto.getAction();
		tr.description = dto.getDescription();
		tr.userId = dto.getUserId();
		tr.expiredDate = dto.getExpiredDate();
		return tr;
	}
}
