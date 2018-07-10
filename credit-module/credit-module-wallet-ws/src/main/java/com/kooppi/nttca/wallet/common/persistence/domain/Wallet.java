package com.kooppi.nttca.wallet.common.persistence.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.kooppi.nttca.ce.domain.ChargingItem;
import com.kooppi.nttca.ce.domain.Payment;
import com.kooppi.nttca.portal.ce.dto.payment.ChargingItemDto;
import com.kooppi.nttca.portal.ce.dto.payment.CreatePaymentDto;
import com.kooppi.nttca.portal.common.constant.PortalConstant;
import com.kooppi.nttca.portal.common.domain.Modifiable;
import com.kooppi.nttca.portal.common.filter.context.RequestContextImpl;
import com.kooppi.nttca.portal.common.persistence.convertor.LocalDatePersistenceConverter;
import com.kooppi.nttca.portal.common.persistence.convertor.LocalDateTimePersistenceConverter;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;
import com.kooppi.nttca.portal.wallet.domain.IdleUnit;
import com.kooppi.nttca.portal.wallet.domain.Source;
import com.kooppi.nttca.portal.wallet.domain.TransactionState;
import com.kooppi.nttca.portal.wallet.domain.TransactionStatus;
import com.kooppi.nttca.portal.wallet.domain.WalletStatus;
import com.kooppi.nttca.portal.wallet.dto.wallet.ConsumeCreditDto;
import com.kooppi.nttca.portal.wallet.dto.wallet.TransactionReservationDto;
import com.kooppi.nttca.portal.wallet.dto.wallet.WalletDto;

/**
 * 
 */
@Entity
@Table(name = "wallet")
public class Wallet extends Modifiable {

	private static final long serialVersionUID = -5838625388368168654L;
	private static final String EMAIL_SEPARATOR = PortalConstant.EMAIL_SEPARATOR;
	private static final String WALLET_ID_PREFIX = "NTTCA-";

	@Column(name = "wallet_id")
	private String walletId;
	
	@Column(name = "organization_id")
	private String organizationId;

	@Column(name = "organization_name")
	private String organizationName;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private WalletStatus walletStatus;

	@Column(name = "max_idle_period")
	private Integer maxIdlePeriod;

	@Enumerated(EnumType.STRING)
	@Column(name = "idle_unit")
	private IdleUnit idleUnit;
	
	@Convert(converter = LocalDatePersistenceConverter.class)
	@Column(name = "expired_on")
	private LocalDate expiredOn;

	@Column(name = "credit_buffer")
	private Integer creditBuffer;
	
	@Column(name = "remain_buffer")
	private Integer remainBuffer;
	
	@Column(name = "is_one_time")
	private boolean isOneTime;
	
	@Column(name = "balance")
	private Integer balance;
	
	@Column(name = "available")
	private Integer available;
	
	@Column(name = "reserved")
	private Integer reserved;
	
	@Column(name = "utilization_alert_1_threshold")
	private Integer utilizationAlert1Threshold;

	@Column(name = "utilization_alert_1_receivers")
	private String utilizationAlert1Receivers;

	@Column(name = "utilization_alert_1_bcc")
	private String utilizationAlert1Bcc;

	@Column(name = "utilization_alert_2_threshold")
	private Integer utilizationAlert2Threshold;

	@Column(name = "utilization_alert_2_receivers")
	private String utilizationAlert2Receivers;

	@Column(name = "utilization_alert_2_bcc")
	private String utilizationAlert2Bcc;
	
	@Column(name = "forfeit_alert_1_threshold")
	private Integer forfeitAlert1Threshold;

	@Column(name = "forfeit_alert_1_receivers")
	private String forfeitAlert1Receivers;

	@Column(name = "forfeit_alert_1_bcc")
	private String forfeitAlert1Bcc;
	
	@Column(name = "forfeit_alert_2_threshold")
	private Integer forfeitAlert2Threshold;

	@Column(name = "forfeit_alert_2_receivers")
	private String forfeitAlert2Receivers;

	@Column(name = "forfeit_alert_2_bcc")
	private String forfeitAlert2Bcc;
	
	@Convert(converter = LocalDateTimePersistenceConverter.class)
	@Column(name = "utilization_threshold_1_last_sent_date")
	private LocalDateTime utilizationThreshold1LastSentDate;

	@Convert(converter = LocalDateTimePersistenceConverter.class)
	@Column(name = "utilization_negative_1_last_sent_date")
	private LocalDateTime utilizationNegative1LastSentDate;
	
	@Convert(converter = LocalDateTimePersistenceConverter.class)
	@Column(name = "utilization_threshold_2_last_sent_date")
	private LocalDateTime utilizationThreshold2LastSentDate;

	@Convert(converter = LocalDateTimePersistenceConverter.class)
	@Column(name = "utilization_negative_2_last_sent_date")
	private LocalDateTime utilizationNegative2LastSentDate;
	
	@Convert(converter = LocalDateTimePersistenceConverter.class)
	@Column(name = "forfeit_threshold_1_last_sent_date")
	private LocalDateTime forfeitThreshold1LastSentDate;

	@Convert(converter = LocalDateTimePersistenceConverter.class)
	@Column(name = "forfeit_negative_1_last_sent_date")
	private LocalDateTime forfeitNegative1LastSentDate;
	
	@Convert(converter = LocalDateTimePersistenceConverter.class)
	@Column(name = "forfeit_threshold_2_last_sent_date")
	private LocalDateTime forfeitThreshold2LastSentDate;

	@Convert(converter = LocalDateTimePersistenceConverter.class)
	@Column(name = "forfeit_negative_2_last_sent_date")
	private LocalDateTime forfeitNegative2LastSentDate;
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name = "wallet_id",referencedColumnName = "wallet_id",updatable = false, insertable = false)
	private List<Adjustment> adjustments = Lists.newArrayList();
	
	public static Wallet create(String organizationId, String organizationName, Integer maxIdlePeriod, IdleUnit idleUnit, LocalDate expiredOn, 
			Integer creditBuffer, boolean isOneTime, Integer utilizationAlert1Threshold, String utilizationAlert1Receivers, 
			String utilizationAlert1Bcc, Integer utilizationAlert2Threshold, String utilizationAlert2Receivers, String utilizationAlert2Bcc, 
			Integer forfeitAlert1Threshold, String forfeitAlert1Receivers, String forfeitAlert1Bcc, Integer forfeitAlert2Threshold, 
			String forfeitAlert2Receivers, String forfeitAlert2Bcc, String createdUserId) {
		Wallet wallet = new Wallet();
		wallet.walletId = UUID.randomUUID().toString();
		wallet.organizationId = organizationId;
		wallet.organizationName = organizationName;
		wallet.walletStatus = WalletStatus.INACTIVE;
		wallet.maxIdlePeriod = maxIdlePeriod;
		wallet.idleUnit = idleUnit;
		wallet.expiredOn = expiredOn;
		wallet.creditBuffer = creditBuffer;
		wallet.isOneTime = isOneTime;
		wallet.utilizationAlert1Threshold = utilizationAlert1Threshold;
		wallet.utilizationAlert1Receivers = utilizationAlert1Receivers;
		wallet.utilizationAlert1Bcc = utilizationAlert1Bcc;
		wallet.utilizationAlert2Threshold = utilizationAlert2Threshold;
		wallet.utilizationAlert2Receivers = utilizationAlert2Receivers;
		wallet.utilizationAlert2Bcc = utilizationAlert2Bcc;
		wallet.forfeitAlert1Threshold = forfeitAlert1Threshold;
		wallet.forfeitAlert1Receivers = forfeitAlert1Receivers;
		wallet.forfeitAlert1Bcc = forfeitAlert1Bcc;
		wallet.forfeitAlert2Threshold = forfeitAlert2Threshold;
		wallet.forfeitAlert2Receivers = forfeitAlert2Receivers;
		wallet.forfeitAlert2Bcc = forfeitAlert2Bcc;
		wallet.balance = 0;
		wallet.available = 0;
		wallet.reserved = 0;
		wallet.remainBuffer = creditBuffer;
		wallet.setModifiedUserId(createdUserId);
		return wallet;
	}
	
	public void updateWallet(WalletStatus status, Integer maxIdlePeriod, IdleUnit idleUnit, Integer creditBuffer, Integer remainBuffer, boolean isOneTime, Integer utilizationAlert1Threshold, 
			String utilizationAlert1Receivers, String utilizationAlert1Bcc, Integer utilizationAlert2Threshold, String utilizationAlert2Receivers, 
			String utilizationAlert2Bcc, Integer forfeitAlert1Threshold, String forfeitAlert1Receivers, String forfeitAlert1Bcc, Integer forfeitAlert2Threshold, 
			String forfeitAlert2Receivers, String forfeitAlert2Bcc, String modifiedUser) {
		this.walletStatus = status;
		this.maxIdlePeriod = maxIdlePeriod;
		this.idleUnit = idleUnit;
		this.creditBuffer = creditBuffer;
		this.remainBuffer = remainBuffer;
		this.isOneTime = isOneTime;
		this.utilizationAlert1Threshold = utilizationAlert1Threshold;
		this.utilizationAlert1Receivers = utilizationAlert1Receivers;
		this.utilizationAlert1Bcc = utilizationAlert1Bcc;
		this.utilizationAlert2Threshold = utilizationAlert2Threshold;
		this.utilizationAlert2Receivers = utilizationAlert2Receivers;
		this.utilizationAlert2Bcc = utilizationAlert2Bcc;
		this.forfeitAlert1Threshold = forfeitAlert1Threshold;
		this.forfeitAlert1Receivers = forfeitAlert1Receivers;
		this.forfeitAlert1Bcc = forfeitAlert1Bcc;
		this.forfeitAlert2Threshold = forfeitAlert2Threshold;
		this.forfeitAlert2Receivers = forfeitAlert2Receivers;
		this.forfeitAlert2Bcc = forfeitAlert2Bcc;	
		
		this.setModifiedUserId(modifiedUser);
	}
	
	public boolean isActive() {
		return this.walletStatus == WalletStatus.ACTIVE;
	}
	
	public boolean isInactive() {
		return this.walletStatus == WalletStatus.INACTIVE;
	}
	
	public Integer getRemainBuffer() {
		return remainBuffer;
	}

	public List<String> getUtilizationAlert1ReceiverList() {
		return Lists.newArrayList(Splitter.on(EMAIL_SEPARATOR).split(utilizationAlert1Receivers));
	}

	public List<String> getUtilizationAlert1BccList() {
		return Lists.newArrayList(Splitter.on(EMAIL_SEPARATOR).split(this.utilizationAlert1Bcc));
	}
	
	public List<String> getUtilizationAlert2ReceiverList() {
		return Lists.newArrayList(Splitter.on(EMAIL_SEPARATOR).split(utilizationAlert2Receivers));
	}

	public List<String> getUtilizationAlert2BccList() {
		return Lists.newArrayList(Splitter.on(EMAIL_SEPARATOR).split(this.utilizationAlert2Bcc));
	}
	
	public List<String> getForfeitAlert1ReceiverList() {
		return Lists.newArrayList(Splitter.on(EMAIL_SEPARATOR).split(forfeitAlert1Receivers));
	}

	public List<String> getForfeitAlert1BccList() {
		return Lists.newArrayList(Splitter.on(EMAIL_SEPARATOR).split(this.forfeitAlert1Bcc));
	}
	
	public List<String> getForfeitAlert2ReceiverList() {
		return Lists.newArrayList(Splitter.on(EMAIL_SEPARATOR).split(forfeitAlert2Receivers));
	}

	public List<String> getForfeitAlert2BccList() {
		return Lists.newArrayList(Splitter.on(EMAIL_SEPARATOR).split(this.forfeitAlert2Bcc));
	}
	
	//Most available means adjustment of wallet that:
	//1. Still has credit for consumption.
	//2. Still IN USE.
	//3. With the earliest contract effective time.
	//4. with the so name( ref number)
	public Adjustment getMostAvailableAdjustment() {
	
		List<Adjustment> sortedAdjustmentList = adjustments.stream()
				.filter(a -> {
					return a.getContractEffectiveDate() != null 
							&& !LocalDate.now().isBefore(a.getContractEffectiveDate()) 
							&& a.getBalance() != null && a.getBalance() > 0 
							&& a.getCurrencyBalance() != null;
				})
				.sorted(Comparator.comparing(Adjustment::getContractEffectiveDate).thenComparing(Adjustment::getRefNumber))
//				.sorted((a1, a2) -> a1.getContractEffectiveDate().compareTo(a2.getContractEffectiveDate()))
				.collect(Collectors.toList());
		
		PortalExceptionUtils.throwIfEmpty(sortedAdjustmentList, PortalErrorCode.UNABLE_TO_FIND_VALID_CONTRACT);
		
		return sortedAdjustmentList.get(0);
	}

	public String getWalletId() {
		return walletId;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public WalletStatus getStatus() {
		return walletStatus;
	}

	public Integer getMaxIdlePeriod() {
		return maxIdlePeriod;
	}

	public IdleUnit getIdleUnit() {
		return idleUnit;
	}

	public LocalDate getExpiredOn() {
		return expiredOn;
	}

	public Integer getCreditBuffer() {
		return creditBuffer;
	}

	public boolean isOneTime() {
		return isOneTime;
	}

	public Integer getUtilizationAlert1Threshold() {
		return utilizationAlert1Threshold;
	}

	public String getUtilizationAlert1Receivers() {
		return utilizationAlert1Receivers;
	}

	public String getUtilizationAlert1Bcc() {
		return utilizationAlert1Bcc;
	}

	public Integer getUtilizationAlert2Threshold() {
		return utilizationAlert2Threshold;
	}

	public String getUtilizationAlert2Receivers() {
		return utilizationAlert2Receivers;
	}

	public String getUtilizationAlert2Bcc() {
		return utilizationAlert2Bcc;
	}

	public Integer getForfeitAlert1Threshold() {
		return forfeitAlert1Threshold;
	}

	public String getForfeitAlert1Receivers() {
		return forfeitAlert1Receivers;
	}

	public String getForfeitAlert1Bcc() {
		return forfeitAlert1Bcc;
	}

	public Integer getForfeitAlert2Threshold() {
		return forfeitAlert2Threshold;
	}

	public String getForfeitAlert2Receivers() {
		return forfeitAlert2Receivers;
	}

	public String getForfeitAlert2Bcc() {
		return forfeitAlert2Bcc;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void disable(String userId) {
		this.walletStatus = WalletStatus.INACTIVE;
		this.setModifiedUserId(userId);
	}
	
	public void enableWallet(String userId) {
		this.walletStatus = WalletStatus.ACTIVE;
		this.setModifiedUserId(userId);
	}
	
	public boolean hasEnoughCredit(int requiredCredits){
//		CreditPool mainPool = getMainCreditPool();
//		int availableCredit = mainPool.getAvailable() + creditBuffer;
//		return availableCredit >= requiredCredits;
		int availableCredit = available + creditBuffer;
		return availableCredit >= requiredCredits;
	}
	
	public Integer getBalance(){
		return balance - getCompensationAmount();
	}
	
	public Integer getAvailableCredits(){
		return available - getCompensationAmount();
	}
	
	public Integer getCompensationAmount() {
		return adjustments.stream().filter(i -> i.isCompensation()).map(i -> i.getBalance()).mapToInt(i -> i).sum();
	}

	public Integer getReservedCredits(){
		return reserved;
	}

	public LocalDateTime getUtilizationThreshold1LastSentDate() {
		return utilizationThreshold1LastSentDate;
	}

	public void setUtilizationThreshold1LastSentDate(LocalDateTime utilizationThreshold1LastSentDate) {
		this.utilizationThreshold1LastSentDate = utilizationThreshold1LastSentDate;
	}

	public LocalDateTime getUtilizationNegative1LastSentDate() {
		return utilizationNegative1LastSentDate;
	}

	public void setUtilizationNegative1LastSentDate(LocalDateTime utilizationNegative1LastSentDate) {
		this.utilizationNegative1LastSentDate = utilizationNegative1LastSentDate;
	}

	public LocalDateTime getUtilizationThreshold2LastSentDate() {
		return utilizationThreshold2LastSentDate;
	}

	public void setUtilizationThreshold2LastSentDate(LocalDateTime utilizationThreshold2LastSentDate) {
		this.utilizationThreshold2LastSentDate = utilizationThreshold2LastSentDate;
	}

	public LocalDateTime getUtilizationNegative2LastSentDate() {
		return utilizationNegative2LastSentDate;
	}

	public void setUtilizationNegative2LastSentDate(LocalDateTime utilizationNegative2LastSentDate) {
		this.utilizationNegative2LastSentDate = utilizationNegative2LastSentDate;
	}

	public LocalDateTime getForfeitThreshold1LastSentDate() {
		return forfeitThreshold1LastSentDate;
	}

	public void setForfeitThreshold1LastSentDate(LocalDateTime forfeitThreshold1LastSentDate) {
		this.forfeitThreshold1LastSentDate = forfeitThreshold1LastSentDate;
	}

	public LocalDateTime getForfeitNegative1LastSentDate() {
		return forfeitNegative1LastSentDate;
	}

	public void setForfeitNegative1LastSentDate(LocalDateTime forfeitNegative1LastSentDate) {
		this.forfeitNegative1LastSentDate = forfeitNegative1LastSentDate;
	}

	public LocalDateTime getForfeitThreshold2LastSentDate() {
		return forfeitThreshold2LastSentDate;
	}

	public void setForfeitThreshold2LastSentDate(LocalDateTime forfeitThreshold2LastSentDate) {
		this.forfeitThreshold2LastSentDate = forfeitThreshold2LastSentDate;
	}

	public LocalDateTime getForfeitNegative2LastSentDate() {
		return forfeitNegative2LastSentDate;
	}

	public void setForfeitNegative2LastSentDate(LocalDateTime forfeitNegative2LastSentDate) {
		this.forfeitNegative2LastSentDate = forfeitNegative2LastSentDate;
	}

	public TransactionReservation reserveCredit(TransactionReservationDto trDto, RequestContextImpl rc){
		PortalExceptionUtils.throwIfFalse(hasEnoughCredit(trDto.getAmount()), PortalErrorCode.NO_ENOUGH_AVAILABLE_CREDIT);
		reserveCredit(trDto.getAmount(), rc.getRequestUserId());
		  
		TransactionReservation rollbackTransactionReservation = TransactionReservation.create(
				walletId, 
				"",		
				trDto,
				rc.getRequestId(),
				rc.getRequestUserId());
		return rollbackTransactionReservation; 
	}
	
	public void cancelReservedCredit(Integer amount,  RequestContextImpl rc){
		cancelReservedCredit(amount, rc.getRequestUserId());
	}

	public void releaseExpiredCredit(Integer amount, String userId){
		releaseReservedCredit(amount, userId);
	}
	
	public Transaction consumeReservedCredit(TransactionReservation transactionReservation, Wallet wallet, RequestContextImpl rc){
		Integer amount = transactionReservation.getAmount();
		
		PortalExceptionUtils.throwIfFalse(hasEnoughCredit(amount), PortalErrorCode.NO_ENOUGH_AVAILABLE_CREDIT);
		consumeReservedCredit(amount, rc.getRequestUserId());
		
		//TODO:TBC
		Transaction rollbackTransaction =null;
//		Transaction rollbackTransaction = Transaction.create(
//				transactionReservation.getPaymentId(),
//				transactionReservation.getTransactionId(),
//				null,
//				TransactionState.COMMITE,
//				TransactionStatus.SUCCESS,
//				wallet, 
//				null, 
//				null, 
//				transactionReservation.getServiceId(),
//				rc.getRequestId(),
//				transactionReservation.getItem(),
//				transactionReservation.getItemType(), 
//				Source.SERVICE,
//				transactionReservation.getAmount() * -1,
//				transactionReservation.getCompensationAmount() * -1,
//				null,
//				getBalance(),
//				transactionReservation.getAction(),
//				transactionReservation.getDescription(), 
//				LocalDateTime.now(), 
//				rc.getRequestUserId(), 
//				rc.getRequestUserId());
		return rollbackTransaction;
	}
	
	public Transaction consumeChargeAmount(TransactionReservation transactionReservation, Wallet wallet, Integer chargeAmount, RequestContextImpl rc) {
		Integer reservedAmount = transactionReservation.getAmount();
		
		PortalExceptionUtils.throwIfFalse(hasEnoughCredit(chargeAmount), PortalErrorCode.NO_ENOUGH_AVAILABLE_CREDIT);
		consumeChargeAmount(reservedAmount, chargeAmount, rc.getRequestUserId());
		Adjustment mostAvailableAdjustment = getMostAvailableAdjustment();
		if (mostAvailableAdjustment != null)
			mostAvailableAdjustment.consumeAndUpdateBalance(chargeAmount, rc.getRequestUserId());
		
		//TODO:TBC
		Transaction rollbackTransaction =null;
//		Transaction rollbackTransaction = Transaction.create(
//				transactionReservation.getPaymentId(),
//				transactionReservation.getTransactionId(),
//				null,
//				TransactionState.COMMITE,
//				TransactionStatus.SUCCESS,
//				wallet, 
//				null, 
//				null, 
//				transactionReservation.getServiceId(),
//				rc.getRequestId(),
//				transactionReservation.getItem(),
//				transactionReservation.getItemType(), 
//				Source.SERVICE,
//				chargeAmount * -1,
//				null,
//				getBalance(),
//				transactionReservation.getAction(),
//				transactionReservation.getDescription(), 
//				LocalDateTime.now(), 
//				rc.getRequestUserId(), 
//				rc.getRequestUserId());
		return rollbackTransaction;
	}
	
	public Transaction reverseSuccessTransaction(Transaction transaction, Wallet wallet, String description, RequestContextImpl rc){
		Integer amount = transaction.getWalletAmount() * -1;
		BigDecimal currencyAmount = transaction.getCurrencyAmount().abs();
		Integer compensationAmount = transaction.getCompensationAmount() * -1;
		
		adjustBalance(amount,rc.getRequestUserId());
		
		Transaction rollbackTransaction = Transaction.create(
						transaction.getPaymentId(),
						null, 
						transaction.getTransactionId(), 
						TransactionState.ROLLBACK, 
						TransactionStatus.SUCCESS, 
						wallet,
						transaction.getServiceOrder(), 
						transaction.getContractEffectiveDate(), 
						rc.getAppId(), 
						rc.getRequestId(), 
						null, 
						null, 
						Source.SERVICE,
						amount,
						compensationAmount,
						currencyAmount,
						getBalance(), 
						Transaction.REVERSE_TRANSACTION_ACTION + transaction.getTransactionId(), 
						description, 
						LocalDateTime.now(), 
						rc.getRequestUserId(), rc.getRequestUserId());
		return rollbackTransaction;
	}

	public void reversePendingTransaction(Transaction transaction) {
		transaction.rollbackPengdingTransaction();
	}
	
	public Transaction addOneOffAdjustment(Adjustment adjustment, String userId){
		int requiredCredits = adjustment.getAmount() * -1;
		PortalExceptionUtils.throwIfFalse(hasEnoughCredit(requiredCredits), PortalErrorCode.NO_ENOUGH_AVAILABLE_CREDIT);
		adjustBalance(adjustment.getAmount(),userId);
		adjustment.completeOneoffAdjustment();
		Transaction transaction = Transaction.create(null, 
				adjustment.getContractId(), 
				null, 
				TransactionState.COMMITE, 
				TransactionStatus.SUCCESS, 
				adjustment.getWallet(), 
				adjustment.getRefNumber(), 
				adjustment.getContractEffectiveDate(),
				null,
				userId, 
				null, 
				null, 
				Source.ADJUSTMENT,
				adjustment.isOneoff() ? adjustment.getAmount() : null,
				adjustment.isCompensation() ? adjustment.getAmount() : null,
				adjustment.getCurrencyAmount(),
				getBalance(),
				adjustment.getAdjustmentType().value(), 
				adjustment.getDescription(), 
				LocalDateTime.now(), 
				userId, 
				userId);
		
		return transaction;
	}
	
	public Transaction addRecurrsiveAdjustment(Adjustment adjustment, String userId){
		int requiredCredits = adjustment.getAmount() * -1;
		PortalExceptionUtils.throwIfFalse(hasEnoughCredit(requiredCredits), PortalErrorCode.NO_ENOUGH_AVAILABLE_CREDIT);
		adjustBalance(adjustment.getAmount(),userId);
		
		adjustment.completeOneCycleAdjustment(userId);
		
		Transaction transaction = Transaction.create(
				null,
				adjustment.getContractId(),
				null, 
				TransactionState.COMMITE, 
				TransactionStatus.SUCCESS, 
				adjustment.getWallet(),
				adjustment.getRefNumber(), 
				LocalDate.now(),
				null,
				userId, 
				null, 
				null, Source.ADJUSTMENT,
				adjustment.getAmount(),
				null,
				adjustment.getCurrencyAmount(),
				getBalance(),
				adjustment.getAdjustmentType().value(), 
				adjustment.getDescription(), 
				LocalDateTime.now(), 
				userId, userId);
		
		return transaction;
	}
	
	public Transaction deleteOrCancelAdjustment(Adjustment adjustment, RequestContextImpl rc, Integer transactionAmount, BigDecimal transactionCurrencyAmount){
		
		Transaction transaction = Transaction.create(null, 
				adjustment.getContractId(), 
				null, 
				TransactionState.COMMITE, 
				TransactionStatus.SUCCESS, 
				adjustment.getWallet(), 
				adjustment.getRefNumber(), 
				adjustment.getContractEffectiveDate(),
				null,
				rc.getRequestUserId(), 
				null, 
				null, 
				Source.ADJUSTMENT,
				adjustment.isOneoff() ? transactionAmount * -1 : null,
				adjustment.isCompensation() ? transactionAmount * -1 : null,
				transactionCurrencyAmount,
				getBalance(),
				adjustment.getAdjustmentType().value(), 
				adjustment.getDescription(), 
				LocalDateTime.now(), 
				rc.getRequestUserId(), 
				rc.getRequestUserId());
		
		return transaction;
	}
	
	public Transaction forfeitAdjustment(Adjustment adjustment, RequestContextImpl rc, Integer forfeitAmount, BigDecimal forfeitCurrencyAmount){
		
		Transaction transaction = Transaction.create(null, 
				null, 
				null, 
				TransactionState.FORFEIT, 
				TransactionStatus.SUCCESS, 
				adjustment.getWallet(), 
				null, 
				null,
				null,
				rc.getRequestUserId(), 
				null, 
				null, 
				Source.ADJUSTMENT,
				adjustment.isOneoff() ? forfeitAmount * -1 : null,
				adjustment.isCompensation() ? forfeitAmount * -1 : null,
				forfeitCurrencyAmount.negate(),
				getBalance(),
				"Forfeit Credit", 
				"forfeit", 
				LocalDateTime.now(), 
				rc.getRequestUserId(), 
				rc.getRequestUserId());
		
		return transaction;
	}

	public Transaction cancelForfeitAdjustment(Adjustment adjustment, RequestContextImpl rc, String parentTransactionId, Integer cancelForfeitAmount, BigDecimal cancelForfeitCurrencyAmount){
		
		Transaction transaction = Transaction.create(null, 
				null, 
				parentTransactionId, 
				TransactionState.CANCEL_FORFEIT, 
				TransactionStatus.SUCCESS, 
				adjustment.getWallet(), 
				null, 
				null,
				null,
				rc.getRequestUserId(), 
				null, 
				null, 
				Source.ADJUSTMENT,
				adjustment.isOneoff() ? cancelForfeitAmount * -1 : null,
				adjustment.isCompensation() ? cancelForfeitAmount * -1 : null,
				cancelForfeitCurrencyAmount,
				getBalance(),
				Transaction.Cancel_FORFEIT_TRANSACTION_ACTION + parentTransactionId, 
				"cancel forfeit", 
				LocalDateTime.now(), 
				rc.getRequestUserId(), 
				rc.getRequestUserId());
		
		return transaction;
	}
	
	public Transaction consumeWalletCreditDirectly(Wallet wallet, String paymentId, Integer totalAmount, String description, RequestContextImpl rc) {
//		PortalExceptionUtils.throwIfFalse(hasEnoughCredit(payment.getAmount()), PortalErrorCode.NO_ENOUGH_AVAILABLE_CREDIT);
		//consume wallet credit
		consumeCredit(totalAmount, rc.getRequestUserId());
		
		Transaction transaction = Transaction.create(
				paymentId,
				null, 
				null, 
				TransactionState.COMMITE, 
				TransactionStatus.SUCCESS, 
				wallet,
				null, 
				null, 
				rc.getAppId(), 
				rc.getRequestId(), 
				null, 
				null, 
				Source.SERVICE,
				totalAmount*-1,
				null,
				null,
				getBalance(),
				"Real time payment", 
				description, 
				LocalDateTime.now(), 
				rc.getRequestUserId(), 
				rc.getRequestUserId());
		return transaction;
	}
	
	public void consumeContractCredit(Transaction transaction, ConsumeCreditDto dto, RequestContextImpl rc) {
		for (ChargingItemDto chargeItem : dto.getChargeItems()) {
			Integer itemTotalAmount = chargeItem.getTotalAmount();
			// create chargeItem
			ChargeItem ci = ChargeItem.create(transaction.getTransactionId(), chargeItem);
			while (itemTotalAmount > 0) {
				Adjustment adjustment = getMostAvailableAdjustment();
				Integer creditConsumed = adjustment.consumeAndUpdateBalance(itemTotalAmount, rc.getRequestUserId());

				itemTotalAmount -= creditConsumed;

				// add chargeItem to contract mapping
				ci.getMapChargeItemToContract().add(MapChargeItemToContract.create(adjustment.getContractId(),
						ci.getChargeItemId(), creditConsumed));

//				transaction.getChargeItems().add(ci);
			}
		}
	}
	
	public void consumeContractCredit(Payment payment, CreatePaymentDto dto, RequestContextImpl rc) {
		for (ChargingItemDto itemDto : dto.getChargingItems()) {
			Integer itemTotalAmount = itemDto.getTotalAmount();
			// create chargeItem
			ChargingItem createChargingItem = ChargingItem.create(itemDto);
			while (itemTotalAmount > 0) {
				Adjustment adjustment = getMostAvailableAdjustment();
				Integer creditConsumed = adjustment.consumeAndUpdateBalance(itemTotalAmount, rc.getRequestUserId());

				itemTotalAmount -= creditConsumed;

				// add chargeItem to contract mapping
				createChargingItem.getMapChargeItemToContract().add(MapChargeItemToContract.create(adjustment.getContractId(),
						createChargingItem.getChargeItemId(), creditConsumed));
				payment.getChargingItems().add(createChargingItem);
			}
		}
	}
	
	public void consumeContractReserveCredit(Transaction transaction, List<ChargeItem> chargeItems, RequestContextImpl rc) {
		for (ChargeItem chargeItem : chargeItems) {
			Integer itemTotalAmount = chargeItem.getAmount();
			// create chargeItem
			while (itemTotalAmount > 0) {
				Adjustment adjustment = getMostAvailableAdjustment();
				Integer creditConsumed = adjustment.consumeAndUpdateBalance(itemTotalAmount, rc.getRequestUserId());

				itemTotalAmount -= creditConsumed;
				// add chargeItem to contract mapping
				chargeItem.getMapChargeItemToContract().add(MapChargeItemToContract.create(adjustment.getContractId(),
						chargeItem.getChargeItemId(), creditConsumed));
			}
		}
	}
	
//	public List<String> rollbackContractCredit(Payment payment, RequestContextImpl rc) {
//		List<String> reopenContractIds = Lists.newArrayList();
//		for (ChargingItem ci : payment.getChargingItems()) {
//			List<MapChargeItemToContract> mapChargeItemToContracts = ci.getMapChargeItemToContract();
//			for (MapChargeItemToContract mapItem : mapChargeItemToContracts) {
//				Adjustment adjustment = mapItem.getAdjustment();
//				String refundAndReopenContractId = adjustment.refundBalance(mapItem.getAmount(), rc.getRequestId());
//				if (refundAndReopenContractId != null) {
//					reopenContractIds.add(refundAndReopenContractId);
//				}
//			}
//		}
//		return reopenContractIds.stream().distinct().collect(Collectors.toList());
//	}
	
	public void generateWalletId(){
		String walletId = WALLET_ID_PREFIX + Strings.padStart(getUid() + "", 10, '0');
		this.walletId = walletId;		
	}
	
	public void activateWallet(String userId) {
		this.walletStatus = WalletStatus.ACTIVE;
		this.setModifiedUserId(userId);
	}
	
	public void consumeCredit(Integer amount, String userId){
		this.available -= amount;
		this.balance -= amount;
		this.setModifiedUserId(userId);
	}
	
	public void reserveCredit(Integer amount, String userId){
		this.reserved += amount;
		this.available -= amount;
		this.setModifiedUserId(userId);
	}

	public void cancelReservedCredit(Integer amount, String userId){
		this.reserved -= amount;
		this.available += amount;
		this.setModifiedUserId(userId);
	}
	
	public void reverseCredit(Integer amount){
		this.balance += amount;
		this.available += amount;
	}
	
	public void consumeReservedCredit(Integer amount, String userId){
		this.reserved -= amount;
		this.balance -= amount;
		this.setModifiedUserId(userId);
	}
	
	public void consumeChargeAmount(Integer reservedAmount, Integer chargeAmount, String userId) {
		if (reservedAmount > chargeAmount) {
			this.reserved -= chargeAmount;
			this.balance -= chargeAmount;
		} else {
			this.reserved -= reservedAmount;
			this.balance -= chargeAmount;
		}
		this.setModifiedUserId(userId);
	}
	
	public void adjustBalance(Integer amount,String userId){
		this.balance += amount;
		this.available += amount;
		this.setModifiedUserId(userId);
	}

	public void releaseReservedCredit(Integer amount, String userId){
		this.reserved -= amount;
		this.available += amount;
		this.setModifiedUserId(userId);
	}
	
	public void transferBalance(Integer amount,String userId){
		this.balance += amount;
		this.available += amount;
		this.setModifiedUserId(userId);
	}
	
	public void releaseBuffer(Integer amount, String userId){
		this.available -= amount;
		this.balance -= amount;
		this.remainBuffer += amount;
		this.setModifiedUserId(userId);
	}
	
	public void releaseBufferOnly(Integer amount, String userId){
		this.remainBuffer += amount;
		this.setModifiedUserId(userId);
	}
	
	@Transactional(value = TxType.REQUIRES_NEW)
	public void monthlyRechargeCredit(int credit){
		this.balance +=  credit;
		this.available += credit;
	}
	
	public void clearAlertLastSentDate() {
		this.setUtilizationThreshold1LastSentDate(null);
		this.setUtilizationNegative1LastSentDate(null);
		this.setUtilizationThreshold2LastSentDate(null);
		this.setUtilizationNegative2LastSentDate(null);
		this.setForfeitThreshold1LastSentDate(null);
		this.setForfeitNegative1LastSentDate(null);
		this.setForfeitThreshold2LastSentDate(null);
		this.setForfeitNegative2LastSentDate(null);
	}
	
	public WalletDto toWalletDto() {
		WalletDto dto = new WalletDto();
		dto.setWalletId(this.getWalletId());
		dto.setOrganizationId(this.getOrganizationId());
		dto.setOrganizationName(this.getOrganizationName());
		dto.setWalletBalance(this.getBalance());
		dto.setWalletAvailable(this.getAvailableCredits());
		dto.setCompensationAmount(this.getCompensationAmount());
		dto.setReserved(this.getReservedCredits());
		dto.setWalletStatus(this.getStatus());
		dto.setMaxIdlePeriod(this.getMaxIdlePeriod());
		dto.setIdleUnit(this.getIdleUnit());
		dto.setExpiredOn(this.getExpiredOn());
		dto.setCreditBuffer(this.getCreditBuffer());
		dto.setCreditRemainBuffer(this.getRemainBuffer());
		dto.setIsOneTime(this.isOneTime());
		dto.setUtilizationAlert1Threshold(this.getUtilizationAlert1Threshold());
		dto.setUtilizationAlert1Receivers(this.getUtilizationAlert1Receivers());
		dto.setUtilizationAlert1Bccs(this.getUtilizationAlert1Bcc());
		dto.setUtilizationAlert2Threshold(this.getUtilizationAlert2Threshold());
		dto.setUtilizationAlert2Receivers(this.getUtilizationAlert2Receivers());
		dto.setUtilizationAlert2Bccs(this.getUtilizationAlert2Bcc());
		dto.setForfeitAlert1Threshold(this.getForfeitAlert1Threshold());
		dto.setForfeitAlert1Receivers(this.getForfeitAlert1Receivers());
		dto.setForfeitAlert1Bccs(this.getForfeitAlert1Bcc());
		dto.setForfeitAlert2Threshold(this.getForfeitAlert2Threshold());
		dto.setForfeitAlert2Receivers(this.getForfeitAlert2Receivers());
		dto.setForfeitAlert2Bccs(this.getForfeitAlert2Bcc());
		
		return dto;
	}
	
	public static Wallet generateWalletFromObject(Object[] obj) {
		Wallet wallet = new Wallet();
		
		//only set necessary info for alertService.checkAndSendAlert(wallet)
		wallet.uid = obj[7] == null ? null : Long.parseLong(String.valueOf(obj[7]));
		wallet.walletId = obj[8] == null ? null : String.valueOf(obj[8]);
		wallet.balance = obj[9] == null ? null : Integer.parseInt(String.valueOf(obj[9]));
		wallet.utilizationAlert1Threshold = obj[10] == null ? null : Integer.parseInt(String.valueOf(obj[10]));
		wallet.utilizationAlert1Receivers = obj[11] == null ? null : String.valueOf(obj[11]);
		wallet.utilizationAlert1Bcc = obj[12] == null ? null : String.valueOf(obj[12]);
		wallet.utilizationAlert2Threshold = obj[13] == null ? null : Integer.parseInt(String.valueOf(obj[13]));
		wallet.utilizationAlert2Receivers = obj[14] == null ? null : String.valueOf(obj[14]);
		wallet.utilizationAlert2Bcc = obj[15] == null ? null : String.valueOf(obj[15]);
		wallet.utilizationThreshold1LastSentDate = obj[16] == null ? null : LocalDateTime.parse(String.valueOf(obj[16]));
		wallet.utilizationNegative1LastSentDate = obj[17] == null ? null : LocalDateTime.parse(String.valueOf(obj[17]));
		wallet.utilizationThreshold2LastSentDate = obj[18] == null ? null : LocalDateTime.parse(String.valueOf(obj[18]));
		wallet.utilizationNegative2LastSentDate = obj[19] == null ? null : LocalDateTime.parse(String.valueOf(obj[19]));
		
		return wallet;
	}
	
	public static void main(String[] args) {
//		Adjustment a1 = Adjustment.create("1", "1", "1", AdjustmentType.ONE_OFF, "1", 1, new BigDecimal(1), 1, new BigDecimal(1), LocalDate.of(2018, 1, 3), null, null, "", "", "");
//		a1.setBalance(70);
//		Adjustment a2 = Adjustment.create("2", "2", "2", AdjustmentType.ONE_OFF, "2", 1, new BigDecimal(1), 1, new BigDecimal(1), LocalDate.of(2018, 1, 1), null, null, "", "", "");
//		a2.setBalance(0);
//		Adjustment a3 = Adjustment.create("3", "3", "3", AdjustmentType.ONE_OFF, "3", 1, new BigDecimal(1), 1, new BigDecimal(1), null, null, null, "", "", "");
//		Adjustment a4 = Adjustment.create("4", "4", "4", AdjustmentType.ONE_OFF, "4", 1, new BigDecimal(1), 1, new BigDecimal(1), LocalDate.of(2018, 5, 2), null, null, "", "", "");
//		a4.setBalance(60);
//		Adjustment a5 = Adjustment.create("5", "5", "5", AdjustmentType.ONE_OFF, "5", 1, new BigDecimal(1), 1, new BigDecimal(1), null, null, null, "", "", "");
//		List<Adjustment> adjustmentList = Lists.newArrayList(a1, a2, a3, a4, a5);
//		
//		List<Adjustment> sortedAdjustmentList = adjustmentList.stream()
//				.filter(a -> {
//					return a.getContractEffectiveDate() != null && LocalDate.now().isAfter(a.getContractEffectiveDate()) && a.getBalance() != null && a.getBalance() > 0;
//				})
//				.sorted((adj1, adj2) -> adj1.getContractEffectiveDate().compareTo(adj2.getContractEffectiveDate()))
//				.collect(Collectors.toList());
//		
//		sortedAdjustmentList.stream().forEach(a -> System.out.println(a.getWalletId()));
	}
}
