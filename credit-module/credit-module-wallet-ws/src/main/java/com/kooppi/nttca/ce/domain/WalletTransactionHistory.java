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
import com.kooppi.nttca.portal.wallet.domain.Source;
import com.kooppi.nttca.portal.wallet.domain.TransactionState;
import com.kooppi.nttca.portal.wallet.domain.TransactionStatus;
import com.kooppi.nttca.portal.wallet.dto.transaction.TransactionDto;

@Entity
@Table(name = "cm_wallet_transaction_history")
public class WalletTransactionHistory extends BasicEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 897054779680750697L;

	@Column(name = "payment_status_uid",updatable = false,insertable = false)
	private Long paymentStatusUid;
	
	@Column(name = "payment_id")
	private String paymentId;
	
	@Column(name = "transaction_id")
	private String transactionId;
	
	@Column(name = "parent_transaction_id")
	private String parentTransactionId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "state")
	private TransactionState state;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private TransactionStatus status;
	
	@Column(name = "wallet_id")
	private String walletId;
	
	@Column(name = "service_id")
	private String serviceId;
	
	@Column(name = "request_id")
	private String requestId;
	
	@Column(name = "item_name")
	private String itemName;
	
	@Column(name = "item_type")
	private String itemType;
	
	@Column(name = "amount")
	private Integer amount;
	
	@Column(name = "balance")
	private Integer balance;
	
	@Column(name = "action")
	private String action;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "source")
	private Source source;
	
	@Convert(converter = LocalDateTimePersistenceConverter.class)
	@Column(name = "transaction_date")
	private LocalDateTime transactionDate;
	
	@Column(name = "user_id")
	private String userId;
	
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

	public String getServiceId() {
		return serviceId;
	}

	public String getRequestId() {
		return requestId;
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

	public Integer getBalance() {
		return balance;
	}

	public String getAction() {
		return action;
	}

	public String getDescription() {
		return description;
	}

	public Source getSource() {
		return source;
	}

	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}

	public String getUserId() {
		return userId;
	}
	
	public boolean isCommitAndSuccess(){
		return (status == TransactionStatus.SUCCESS && state == TransactionState.COMMITE);
	}
	
	public boolean isRollbackAndSuccess(){
		return (status == TransactionStatus.SUCCESS && state == TransactionState.ROLLBACK);
	}
	
	public void updateParentStatusHistory(PaymentStatusHistory parent) {
		this.parentStatusHistory = parent;
	}
	
	public static WalletTransactionHistory create(TransactionDto dto) {
		WalletTransactionHistory tran = new WalletTransactionHistory();
		tran.paymentId = dto.getPaymentId();
		tran.transactionId = dto.getTransactionId();
		tran.parentTransactionId = dto.getParentTransactionId();
		tran.state = dto.getState();
		tran.status = dto.getStatus();
		tran.walletId = dto.getWalletId();
		tran.serviceId = dto.getServiceId();
		tran.requestId = dto.getRequestId();
		tran.status = dto.getStatus();
		tran.itemName = dto.getItem();
		tran.itemType = dto.getItemType();
		tran.amount = dto.getAmount();
		tran.action = dto.getAction();
		tran.description = dto.getDescription();
		tran.userId = dto.getUserId();
		tran.balance = dto.getBalance();
		tran.source = dto.getSource();
		tran.transactionDate = dto.getChargeDate();
		return tran;
	}

}
