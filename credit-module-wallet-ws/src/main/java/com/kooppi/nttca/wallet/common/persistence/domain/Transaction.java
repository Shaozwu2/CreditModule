package com.kooppi.nttca.wallet.common.persistence.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.google.common.base.Strings;
import com.kooppi.nttca.portal.common.domain.Modifiable;
import com.kooppi.nttca.portal.common.persistence.convertor.LocalDatePersistenceConverter;
import com.kooppi.nttca.portal.common.persistence.convertor.LocalDateTimePersistenceConverter;
import com.kooppi.nttca.portal.wallet.domain.Source;
import com.kooppi.nttca.portal.wallet.domain.TransactionState;
import com.kooppi.nttca.portal.wallet.domain.TransactionStatus;
import com.kooppi.nttca.portal.wallet.dto.transaction.TransactionDto;

@Entity
@Table(name = "transaction")
public class Transaction extends Modifiable {

	private static final long serialVersionUID = -611514586635454355L;
	public static final String REVERSE_TRANSACTION_ACTION = "Rollback Transaction ";
	public static final String Cancel_FORFEIT_TRANSACTION_ACTION = "Cancel Forfeit Transaction ";
	public static final String TRANSACTION_ID_PREFIX = "TX-";
	public static final String TRANSACTION_ID_COMMITED_PREFIX = "TX-N";
	public static final String TRANSACTION_ID_ROLLBACK_PREFIX = "TX-D";
	public static final String TRANSACTION_ID_FORFEIT_PREFIX = "TX-F";
	public static final String TRANSACTION_ID_CANCEL_FORFEIT_PREFIX = "TX-C";

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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="wallet_id",referencedColumnName = "wallet_id",insertable=false,updatable = false)
	private Wallet wallet;
	
	@Column(name = "service_order")
	private String serviceOrder;
	
	@Convert(converter = LocalDatePersistenceConverter.class)
	@Column(name = "contract_effective_date")
	private LocalDate contractEffectiveDate;
	
	@Column(name = "service_id")
	private String serviceId;
	
	@Column(name = "request_id")
	private String requestId;
	
	@Column(name = "amount")
	private Integer walletAmount;
	
	@Column(name = "compensation_amount")
	private Integer compensationAmount;
	
	@Column(name = "currency_amount")
	private BigDecimal currencyAmount;

	@Column(name = "balance")
	private Integer balance;
	
	@Column(name = "action")
	private String action;
	
	@Column(name = "description")
	private String description;
	
	@Convert(converter = LocalDateTimePersistenceConverter.class)
	@Column(name = "charge_date")
	private LocalDateTime chargeDate;
	
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "user_name")
	private String userName;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "source")
	private Source source;
	
	public static Transaction create(String paymentId,String transactionId, String parentTransactionId, TransactionState state,
			TransactionStatus status, Wallet wallet, String serviceOrder,LocalDate contractEffectiveDate, String serviceId, 
			String requestId, String item, String itemType, Source source, Integer amount, Integer compensationAmount, BigDecimal currencyAmount, Integer balance, 
			String action, String description, LocalDateTime chargeDate, String userId, String userName) {
		Transaction transaction = new Transaction();

		transaction.paymentId = paymentId;
		transaction.transactionId = transactionId;
		transaction.parentTransactionId = parentTransactionId;
		transaction.state = state;
		transaction.status = status;
		transaction.walletId = wallet.getWalletId();
		transaction.serviceOrder = serviceOrder;
		transaction.contractEffectiveDate = contractEffectiveDate;
		transaction.serviceId = serviceId;
		transaction.requestId = requestId;
		transaction.source = source;
		transaction.walletAmount = amount;
		transaction.compensationAmount = compensationAmount;
		transaction.currencyAmount = currencyAmount;
		transaction.balance = balance;
		transaction.action = action;
		transaction.description = description;
		transaction.chargeDate = chargeDate;
		transaction.userId = userId;
		transaction.userName = userName;
		transaction.setModifiedUserId(userId);
		return transaction;
	}
	
	public void concalForfeitTransaction() {
		this.state = TransactionState.CANCEL_FORFEIT;
	}
	
	public String getPaymentId(){
		return paymentId;
	}
	
	public String getTransactionId() {
		return transactionId;
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

	public Integer getWalletAmount() {
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

	public LocalDateTime getChargeDate() {
		return chargeDate;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}
	
	public BigDecimal getCurrencyAmount() {
		return currencyAmount;
	}
	
	public boolean isCancelable() {
		return (this.status == TransactionStatus.SUCCESS && this.state == TransactionState.COMMITE);
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}
	
	public String getParentTransactionId() {
		return parentTransactionId;
	}

	public Integer getCompensationAmount() {
		return compensationAmount;
	}

	public void updateTransactionId() {
		String transactionId;

		if (state == TransactionState.ROLLBACK) {
			transactionId = Transaction.TRANSACTION_ID_ROLLBACK_PREFIX + Strings.padStart(getUid() + "", 10, '0');
		} else if (state == TransactionState.FORFEIT) {
			transactionId = Transaction.TRANSACTION_ID_FORFEIT_PREFIX+ Strings.padStart(getUid() + "", 10, '0');
		} else if (state == TransactionState.CANCEL_FORFEIT) {
			transactionId = Transaction.TRANSACTION_ID_CANCEL_FORFEIT_PREFIX + Strings.padStart(getUid() + "", 10, '0');
		} else {
			transactionId = Transaction.TRANSACTION_ID_COMMITED_PREFIX + Strings.padStart(getUid() + "", 10, '0');
		}
		this.transactionId = transactionId;
	}
	
	public void completeTransaction() {
		this.status = TransactionStatus.SUCCESS;
	}

	public void rollbackPengdingTransaction() {
		this.state = TransactionState.ROLLBACK;
		this.status = TransactionStatus.SUCCESS;
		this.action = REVERSE_TRANSACTION_ACTION + this.transactionId;
		this.currencyAmount = currencyAmount.abs();
		this.compensationAmount = compensationAmount * -1;
	}
	
	public TransactionDto toTransactionDto(){
	
		TransactionDto dto = new TransactionDto();
		dto.setPaymentId(this.getPaymentId());
		dto.setTransactionId(this.getTransactionId());
		dto.setParentTransactionId(this.getParentTransactionId());
		dto.setState(this.getState());
		dto.setStatus(this.getStatus());
		dto.setWalletId(this.getWalletId());
		dto.setServiceOrder(this.getServiceOrder());
		dto.setContractEffectiveDate(this.getContractEffectiveDate());
		dto.setServiceId(this.getServiceId());
		dto.setRequestId(this.getRequestId());
		dto.setAmount(this.getWalletAmount());
		dto.setCurrencyAmount(this.getCurrencyAmount());
		dto.setBalance(this.getBalance());
		dto.setAction(this.getAction());
		dto.setSource(this.getSource());
		dto.setDescription(this.getDescription());
		dto.setChargeDate(this.getChargeDate());
		dto.setUserId(this.getUserId());
		return dto;
	}
	
}
