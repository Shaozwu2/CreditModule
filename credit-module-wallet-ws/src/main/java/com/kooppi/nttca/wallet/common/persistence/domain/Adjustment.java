package com.kooppi.nttca.wallet.common.persistence.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.common.base.Strings;
import com.kooppi.nttca.portal.common.domain.Modifiable;
import com.kooppi.nttca.portal.common.persistence.convertor.LocalDatePersistenceConverter;
import com.kooppi.nttca.portal.common.persistence.convertor.LocalDateTimePersistenceConverter;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;
import com.kooppi.nttca.portal.wallet.domain.AdjustmentStatus;
import com.kooppi.nttca.portal.wallet.domain.AdjustmentType;
import com.kooppi.nttca.portal.wallet.dto.adjustment.AdjustmentDto;

import jersey.repackaged.com.google.common.collect.Lists;

@Entity
@Table(name = "contract")
public class Adjustment extends Modifiable {

	private static final long serialVersionUID = 723270455339210940L;
	private static final String CONTRACT_ID_PREFIX = "TX-C";

	@Column(name = "contract_id")
	private String contractId;
	
	@Column(name = "wallet_id")
	private String walletId;
	
	@Column(name = "ref_number")
	private String refNumber;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private AdjustmentType adjustmentType;
	
	@Column(name = "company_code")
	private String companyCode;
	
	@Column(name = "amount")
	private Integer amount;
	
	@Column(name = "currency_amount")
	private BigDecimal currencyAmount;
	
	@Column(name = "balance")
	private Integer balance;
	
	@Column(name = "currency_balance")
	private BigDecimal currencyBalance;
	
	@Column(name = "currency_rate_id")
	private Integer currencyRateId;
	
	@Column(name = "exchange_rate")
	private BigDecimal exchangeRate;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private AdjustmentStatus status;

	@Convert(converter = LocalDatePersistenceConverter.class)
	@Column(name = "last_reopen_date")
	private LocalDate lastReopenDate;

	@Convert(converter = LocalDatePersistenceConverter.class)
	@Column(name = "contract_effective_date")
	private LocalDate contractEffectiveDate;
	
	@Convert(converter = LocalDatePersistenceConverter.class)
	@Column(name = "contract_termination_date")
	private LocalDate contractTerminationDate;
	
	@Convert(converter = LocalDatePersistenceConverter.class)
	@Column(name = "credit_expiry_date")
	private LocalDate creditExpiryDate;
	
	@Convert(converter = LocalDateTimePersistenceConverter.class)
	@Column(name = "last_recharge_date")
	private LocalDateTime lastRechargeDate;
	
	@Column(name = "terminated_reason")
	private String terminatedReason;
	
	@Convert(converter = LocalDateTimePersistenceConverter.class)
	@Column(name = "transaction_date")
	private LocalDateTime transactionDate;
	
	@ManyToOne(targetEntity = Wallet.class,fetch = FetchType.LAZY)
	@JoinColumn(name = "wallet_id",referencedColumnName = "wallet_id",updatable = false, insertable = false)
	private Wallet wallet;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="currency_rate_id",referencedColumnName = "currency_rate_id",insertable=false,updatable = false)
	private CurrencyRate currencyRate;
	
	@Column(name = "description")
	private String description;
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY, orphanRemoval=true)
	@JoinColumn(name = "contract_id",referencedColumnName = "contract_id")
	private List<ContractToBuMaster> buMasters = Lists.newArrayList();
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY, orphanRemoval=true)
	@JoinColumn(name = "contract_id",referencedColumnName = "contract_id")
	private List<ContractToPriceBook> priceBooks = Lists.newArrayList();

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY, orphanRemoval=true)
	@JoinColumn(name = "contract_id",referencedColumnName = "contract_id")
	private List<MapChargeItemToContract> chargingItems = Lists.newArrayList();
	
	@Column(name = "is_all_bu")
	private Boolean isAllBu;
	
	@Column(name = "is_forfeited")
	private Boolean isForfeited;

	@Column(name = "forfeit_amount")
	private Integer forfeitAmount;

	@Column(name = "forfeit_currency_amount")
	private BigDecimal forfeitCurrencyAmount;
	
	@Column(name = "is_all_product")
	private Boolean isAllProduct;

	public static Adjustment create(String walletId, String refNumber,
			AdjustmentType adjustmentType, String companyCode, Integer amount, BigDecimal currencyAmount,
			Integer currencyRateId, BigDecimal exchangeRate, LocalDate contractEffectiveDate,
			LocalDate contractTerminationDate, LocalDate expiryDate, String description, String terminatedReason,
			Boolean isAllBu, Boolean isAllProduct, String userId) {

		Adjustment adjustment = new Adjustment();
		adjustment.walletId = walletId;
		adjustment.contractId = UUID.randomUUID().toString();
		adjustment.refNumber = refNumber;
		adjustment.adjustmentType = adjustmentType;
		adjustment.companyCode = companyCode;
		adjustment.amount = amount;
		adjustment.currencyAmount = currencyAmount;
		adjustment.currencyRateId = currencyRateId;
		adjustment.exchangeRate = exchangeRate;
		adjustment.description = description;
		adjustment.status = AdjustmentStatus.PENDING;
		adjustment.contractEffectiveDate = contractEffectiveDate;
		adjustment.contractTerminationDate = contractTerminationDate;
		adjustment.creditExpiryDate = expiryDate;
		adjustment.terminatedReason = terminatedReason;
		adjustment.isAllBu = Optional.ofNullable(isAllBu).orElse(false);
		adjustment.isAllProduct = Optional.ofNullable(isAllProduct).orElse(false);
		adjustment.isForfeited = false;
		adjustment.forfeitAmount = 0;
		adjustment.forfeitCurrencyAmount = BigDecimal.ZERO;
		adjustment.setModifiedUserId(userId);
		return adjustment;
	}
	
	public void update(String refNumber, String companyCode, Integer amount, BigDecimal currencyAmount, Integer currencyRateId, 
			BigDecimal exchangeRate, LocalDate contractEffectiveDate, LocalDate contractTerminationDate, LocalDate creditExpiryDate,
			String description, String terminatedReason, String modifiedUser, Boolean isAllBu, Boolean isAllProduct) {
		this.refNumber = refNumber;
		this.companyCode = companyCode;
		this.amount = amount;
		this.currencyAmount = currencyAmount;
		this.currencyRateId = currencyRateId;
		this.exchangeRate = exchangeRate;
		this.description = description;
		this.contractEffectiveDate = contractEffectiveDate;
		this.contractTerminationDate = contractTerminationDate;
		this.creditExpiryDate = creditExpiryDate;
		this.terminatedReason = terminatedReason;
		this.isAllBu = isAllBu;
		this.isAllProduct = isAllProduct;
		this.setModifiedUserId(modifiedUser);
	}
	
	public void cancel(Integer balance, String modifiedUser) {
		this.wallet.consumeCredit(balance, modifiedUser);
		this.status = AdjustmentStatus.CANCELLED;
		this.balance = 0;
		this.currencyAmount = BigDecimal.ZERO;
		this.setModifiedUserId(modifiedUser);
	}

	public void forfeit(String modifiedUser) {
		this.wallet.consumeCredit(balance, modifiedUser);
		this.status = AdjustmentStatus.CLOSED;
		this.isForfeited = true;
		this.forfeitAmount += getBalance();
		this.forfeitCurrencyAmount = this.forfeitCurrencyAmount.add(getCurrencyBalance());
		this.balance = 0;
		this.currencyBalance = BigDecimal.ZERO;
		this.setModifiedUserId(modifiedUser);
	}

	public void cancelForfeit(Integer balance, BigDecimal currencyBalance, String modifiedUser) {
		this.wallet.adjustBalance(balance, modifiedUser);
		this.status = AdjustmentStatus.IN_USE;
		this.lastReopenDate = LocalDate.now();
		this.isForfeited = false;
		this.forfeitAmount = 0;
		this.forfeitCurrencyAmount = BigDecimal.ZERO;
		this.balance += balance;
		this.currencyBalance = this.currencyBalance.add(currencyBalance);
		this.setModifiedUserId(modifiedUser);
	}

	public String getContractId() {
		return contractId;
	}

	public String getWalletId() {
		return walletId;
	}

	public String getRefNumber() {
		return refNumber;
	}

	public AdjustmentType getAdjustmentType() {
		return adjustmentType;
	}

	public Integer getAmount() {
		return amount;
	}
	
	public BigDecimal getCurrencyAmount() {
		return currencyAmount;
	}
	
	public Integer getBalance() {
		return balance;
	}
	
	public void setBalance(Integer balance) {
		this.balance = balance;
	}
	
	public BigDecimal getCurrencyBalance() {
		return currencyBalance;
	}
	
	public void setCurrencyBalance(BigDecimal currencyBalance) {
		this.currencyBalance = currencyBalance;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public Integer getCurrencyRateId() {
		return currencyRateId;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public String getDescription() {
		return description;
	}

	public AdjustmentStatus getStatus() {
		return status;
	}

	public LocalDate getContractEffectiveDate() {
		return contractEffectiveDate;
	}

	public LocalDate getContractTerminationDate() {
		return contractTerminationDate;
	}

	public LocalDate getCreditExpiryDate() {
		return creditExpiryDate;
	}
	
	public String getTerminatedReason() {
		return terminatedReason;
	}
	
	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}

	public List<ContractToBuMaster> getBuMasters() {
		return buMasters;
	}

	public List<ContractToPriceBook> getPriceBooks() {
		return priceBooks;
	}

	public List<MapChargeItemToContract> getChargingItems() {
		return chargingItems;
	}

	public void setChargingItems(List<MapChargeItemToContract> chargingItems) {
		this.chargingItems = chargingItems;
	}

	public Integer getForfeitAmount() {
		return forfeitAmount;
	}

	public BigDecimal getForfeitCurrencyAmount() {
		return forfeitCurrencyAmount;
	}

	public void completeOneoffAdjustment(){
		PortalExceptionUtils.throwIfTrue(!(isOneoff()||isCompensation()), PortalErrorCode.METHOD_ONLY_FOR_ONEOFF_ADJUSTMENT);
		this.balance = this.amount;
		this.currencyBalance = this.currencyAmount;
		this.status = AdjustmentStatus.IN_USE;
		this.transactionDate = LocalDateTime.now();
	}
	
	public void closeAdjustment(String userId) {
		this.status = AdjustmentStatus.CLOSED;
		this.setModifiedUserId(userId);
	}
	
	public void reopenAdjustment(String userId) {
		this.status = AdjustmentStatus.IN_USE;
		this.lastReopenDate = LocalDate.now();
		this.setModifiedUserId(userId);
	}
	
	public void completeOneCycleAdjustment(String userId) {
		//add new adjustment
		//update last recharge date
		//if this is last recharge, update status = success
		PortalExceptionUtils.throwIfTrue(isOneoff(), PortalErrorCode.METHOD_ONLY_FOR_RECURRSIVE_ADJUSTMENT);
		PortalExceptionUtils.throwIfFalse(isTimeToMonthlyRecharge(), PortalErrorCode.CHILD_ADJUSTMENT_ALREADY_CREATED);
		
		//Update 'balance' & 'currency_balance'
		Integer balance = this.getBalance() == null ? 0 : this.getBalance();
		BigDecimal currencyBalance = this.getCurrencyBalance() == null ? BigDecimal.ZERO : this.getCurrencyBalance();
		if (this.getAmount() != null)
			balance += this.getAmount();
		if (this.getCurrencyAmount() != null)
			currencyBalance = currencyBalance.add(this.getCurrencyAmount());
		this.balance = balance;
		this.currencyBalance = currencyBalance;
		
		this.status = AdjustmentStatus.IN_USE;
		this.transactionDate = LocalDateTime.now();
		this.setModifiedUserId(userId);
		this.lastRechargeDate = LocalDateTime.now();
	}
	
	public boolean isTimeToMonthlyRecharge() {
		boolean result = true;
		PortalExceptionUtils.throwIfTrue(isOneoff(), PortalErrorCode.METHOD_ONLY_FOR_RECURRSIVE_ADJUSTMENT);
		LocalDateTime current = LocalDateTime.now();
		List<Integer> nextRechargeDayList = Lists.newArrayList(28, 29, 30, 31);
		if (lastRechargeDate == null) {
			if (contractEffectiveDate.getYear() == current.getYear() && contractEffectiveDate.getMonthValue() == current.getMonthValue() && contractEffectiveDate.getDayOfMonth() == current.getDayOfMonth()) {
				result = true;
			} else {
				result = false;
			}
		} else {
			Integer rechargeDay = lastRechargeDate.getDayOfMonth();
			if (nextRechargeDayList.contains(lastRechargeDate.getDayOfMonth())) {
				rechargeDay = 28;
			}
			Integer rechargeMonth = lastRechargeDate.plusMonths(1).getMonthValue();
			Integer rechargeYear = lastRechargeDate.plusMonths(1).getYear();
			
			if (rechargeDay == current.getDayOfMonth() && rechargeMonth == current.getMonthValue() && rechargeYear == current.getYear()) {
				result = true;
			} else {
				result = false;
			}
			
			if (contractTerminationDate != null) {
				if (!current.isBefore(LocalDateTime.of(contractEffectiveDate, LocalTime.MIDNIGHT)))
					result = false;
			}
		}
		
		return result;
	}
	
	public boolean isCurrentMonthAdjustmentCreated() {
		PortalExceptionUtils.throwIfTrue(isOneoff(), PortalErrorCode.METHOD_ONLY_FOR_RECURRSIVE_ADJUSTMENT);
		LocalDateTime current = LocalDateTime.now();
		return lastRechargeDate!=null && lastRechargeDate.getYear() == current.getYear() && lastRechargeDate.getMonthValue() == current.getMonthValue();
	}
	

	public boolean isScheduledAdjustment(){
		if(this.getAdjustmentType() == AdjustmentType.ONE_OFF || this.getAdjustmentType() == AdjustmentType.COMPENSATION)
			return (this.contractEffectiveDate!=null);
		else return false;
	}
	
	public boolean isOneoff(){
		return (this.adjustmentType == AdjustmentType.ONE_OFF);
	}

	public boolean isCompensation(){
		return (this.adjustmentType == AdjustmentType.COMPENSATION);
	}
	
	public boolean isPending(){
		return this.status == AdjustmentStatus.PENDING;
	}
	
	public boolean isInUse(){
		return this.status == AdjustmentStatus.IN_USE;
	}

	public boolean isClosed(){
		return this.status == AdjustmentStatus.CLOSED;
	}
	
	public boolean isCancelled(){
		return this.status == AdjustmentStatus.CANCELLED;
	}
	
	public Wallet getWallet(){
		return wallet;
	}
	
	public CurrencyRate getCurrencyRate() {
		return currencyRate;
	}
	
	public LocalDateTime getLastRechargeDate(){
		return lastRechargeDate;
	}
	
	public Boolean isAllBu() {
		return isAllBu;
	}

	public Boolean isAllProduct() {
		return isAllProduct;
	}

	public Boolean isForfeited() {
		return isForfeited;
	}

	public Boolean getIsAllBu() {
		return isAllBu;
	}

	public Boolean getIsForfeited() {
		return isForfeited;
	}

	public Boolean getIsAllProduct() {
		return isAllProduct;
	}

	public void updateContractId(){
		this.contractId = Adjustment.CONTRACT_ID_PREFIX + Strings.padStart(getUid() + "", 10, '0');
		getBuMasters().forEach(bu->{
			bu.updateContractId(contractId);
		});
		getPriceBooks().forEach(pb->{
			pb.updateContractId(contractId);
		});
	}
	
	//Need to update adjustment's balance & currencyBalance while the balance of its wallet's credit pool is being modified.
	public Integer consumeAndUpdateBalance(Integer credit, String userId) {
		if (this.balance > credit) {
			this.balance -= credit;
			BigDecimal currencyCredit = this.currencyAmount.multiply(new BigDecimal(credit)).divide(new BigDecimal(this.amount), 5, RoundingMode.HALF_EVEN);
			this.currencyBalance = this.currencyBalance.subtract(currencyCredit);
			setModifiedUserId(userId);
			return credit;
		} else {
			Integer creditToDeleted = this.balance;
			this.balance = 0;
			this.status = AdjustmentStatus.CLOSED;
			this.currencyBalance = BigDecimal.ZERO;
			setModifiedUserId(userId);
			return creditToDeleted;
		}
	}
	
	public String refundBalance(Integer credit, String userId) {
		BigDecimal currencyCredit = this.currencyAmount.multiply(new BigDecimal(credit)).divide(new BigDecimal(this.amount), 5, RoundingMode.HALF_EVEN);
		this.balance += credit;
		this.currencyBalance = this.currencyBalance.add(currencyCredit);
		setModifiedUserId(userId);
		if (this.status.equals(AdjustmentStatus.CLOSED)) {
			reopenAdjustment(userId);
			return this.contractId;
		} else {
			return null;
		}
	}
	
	public AdjustmentDto toAdjustmentDto(){
		AdjustmentDto dto = new AdjustmentDto();
		dto.setWalletId(this.getWalletId());
		dto.setId(this.getContractId());
		dto.setRefNumber(this.getRefNumber());
		dto.setCompanyCode(this.getCompanyCode());
		dto.setAdjustmentType(this.getAdjustmentType());
		if (getCurrencyRate() != null) {
			dto.setCurrency(this.getCurrencyRate().getFromCurrencyCode());
			dto.setExchangeRate(this.getExchangeRate());
		}
		AdjustmentType adjustmentType = this.getAdjustmentType();
		if (adjustmentType.equals(AdjustmentType.ONE_OFF) || adjustmentType.equals(AdjustmentType.COMPENSATION)) {
			dto.setNttDollarSO(this.getAmount());
			dto.setCurrencyAmountSO(this.getCurrencyAmount());
		} else {
//			dto.setNttDollarMonthly(this.getAmount());
//			dto.setCurrencyAmountMonthly(this.getCurrencyAmount());
//			
//			Integer nttDollarAmountSO = 0;
//			BigDecimal currencyAmountSO = BigDecimal.ZERO;
//			List<Transaction> txList = this.getTransactiosns();
//			for (Transaction tx : txList) {
//				if (tx.getSource() != null && tx.getAction() != null) {
//					if (tx.getSource().equals(Source.ADJUSTMENT) && tx.getAction().equalsIgnoreCase("Monthly Recharge")) {
//						if (tx.getAmount() != null)
//							nttDollarAmountSO += tx.getAmount();
//						if (tx.getCurrencyAmount() != null)
//							currencyAmountSO = currencyAmountSO.add(tx.getCurrencyAmount());
//					}
//				}
//			}
//			dto.setNttDollarSO(nttDollarAmountSO);
//			dto.setCurrencyAmountSO(currencyAmountSO);
		}
		if(adjustmentType.equals(AdjustmentType.COMPENSATION)) {
			List<String> buList = Lists.newArrayList();
			getBuMasters().forEach(bu->{
				buList.add(bu.getBuName());
			});
			List<String> productList = Lists.newArrayList();
			getPriceBooks().forEach(pb->{
				productList.add(pb.getPartNo());
			});
//			System.out.println("productList: " + productList.size());
			dto.setBuLists(buList);
			dto.setProductList(productList);
		}
		
		dto.setNttDollarBalance(getBalance());
		dto.setCurrencyAmountBalance(getCurrencyBalance());
		
		setNextRechargeDateInDto(dto);
		
		dto.setStatus(getStatus());
		dto.setDescription(getDescription());
		dto.setContractEffectiveDate(getContractEffectiveDate());
		dto.setContractTerminationDate(getContractTerminationDate());
		dto.setCreditExpiryDate(getCreditExpiryDate());
		dto.setTerminatedReason(getTerminatedReason());
		dto.setTransactionDate(getTransactionDate());
		dto.setIsForfeited(isForfeited());
		dto.setIsAllBu(isAllBu());
		dto.setIsAllProduct(isAllProduct());
		dto.setUserId(getModifiedBy());
		dto.setOrganizationId(getWallet().getOrganizationId());
		dto.setNttDollarForfeit(getForfeitAmount());
		dto.setCurrencyAmountForfeit(getForfeitCurrencyAmount());
		return dto;
	}
	
	private void setNextRechargeDateInDto(AdjustmentDto dto) {
		//Show 'Next Recharge Date', only when 'Monthly Recharge' contract.
		//If the recharge day is 29th, 30th, 31st, recharge on 28th from the next month onwards.
		//Only show it during the contract period.
		if (this.getAdjustmentType() != null && getAdjustmentType().equals(AdjustmentType.MONTHLY_RECHARGE)) {
			LocalDate contractStartDate = getContractEffectiveDate();
			LocalDate contractEndDate = getContractTerminationDate();
			if (contractStartDate != null) {
				Integer dayOfRechargeDate = contractStartDate.getDayOfMonth();
				if (dayOfRechargeDate == 29 || dayOfRechargeDate == 30 || dayOfRechargeDate == 31)
					dayOfRechargeDate = 28;
				LocalDateTime now = LocalDateTime.now();
				LocalDateTime nextRechargeDate1 = now.withDayOfMonth(dayOfRechargeDate);
				LocalDateTime nextRechargeDate2 = now.plusMonths(1).withDayOfMonth(dayOfRechargeDate);
				LocalDateTime nextRechargeDate = now.isBefore(nextRechargeDate1) ? nextRechargeDate1 : nextRechargeDate2;
				nextRechargeDate = nextRechargeDate.withHour(0).withMinute(0).withSecond(0);
				
				if (nextRechargeDate.isAfter(contractStartDate.atStartOfDay())) {
					if (contractEndDate == null) {
						dto.setNextRechargeDate(nextRechargeDate);
					} else {
						if (nextRechargeDate.isBefore(contractEndDate.atStartOfDay()) || nextRechargeDate.isEqual(contractEndDate.atStartOfDay())) {
							dto.setNextRechargeDate(nextRechargeDate);
						}
					}
				}
			}
		}
	}
	
	public List<String> getPartNoList() {
		List<String> partNoList = new ArrayList<String>();
		for (ContractToPriceBook pb: priceBooks) {
			String partNo = pb.getPartNo();
			if (partNo != null && !partNo.isEmpty())
				partNoList.add(partNo);
		}
		if (partNoList.isEmpty())
			partNoList.add("-1");
		return partNoList;
	}
	
	public static void main(String[] args) {
		
//		String CONTRACT_ID_PREFIX = "TX-C";
//		String WALLET_ID_PREFIX = "NTTCA-";
//		String ORG_PREFIX = "CREDIT_CDS";
		
		//*********Create Contract***********
//		String Ref_Number = "REF-";
//		for (int i = 51; i <= 100; i++) {
//			
//			String contractId = CONTRACT_ID_PREFIX+ Strings.padStart(i + "", 10, '0');
//			String walletId = WALLET_ID_PREFIX + Strings.padStart(i + "", 10, '0');
//			String refNumber = Ref_Number + Strings.padStart(i + "", 10, '0');
//			System.out.println(String.format(
//					"INSERT INTO `load_test_wallet`.`contract` (uid, `contract_id`, `wallet_id`, `ref_number`, `type`, `company_code`, `amount`, `currency_amount`, `balance`, `currency_balance`, "
//							+ "`currency_rate_id`, `exchange_rate`, `description`, `status`, `is_all_bu`, `is_all_product`, `contract_effective_date`, `credit_expiry_date`, `transaction_date`, "
//							+ "`version`, `modified_by`, `modified_date`, `created_by`, `created_date`) "
//							+ "VALUES (%s, '%s', '%s', '%s', 'ONE_OFF', 'HKNET', '100000000', '10000000.00000000', '100000000', '10000000.00000000', "
//							+ "'1', '1.00000000', '', 'IN_USE', '0', '0', '2018-04-25', '2021-04-25', '2018-04-25 15:24:03',"
//							+ " '1', 'system', '2018-05-04 11:34:31', 'sa@sa.com', '2018-04-25 15:24:03');",
//					i, contractId,walletId,refNumber));
//		}
		
		//*********Create Wallet***********
//		for (int i = 51; i <= 100; i++) {
//			
//			String walletId = WALLET_ID_PREFIX + Strings.padStart(i + "", 10, '0');
//			String orgId = ORG_PREFIX + Strings.padStart(i + "", 4, '0');
//			System.out.println(String.format(
//					"INSERT INTO `load_test_wallet`.`wallet` (`uid`, `wallet_id`, `organization_id`, `organization_name`, `status`, `max_idle_period`, `idle_unit`, `credit_buffer`, `balance`, `available`, "
//					+ "`reserved`, `utilization_alert_1_receivers`, `utilization_alert_1_bcc`, `version`, `modified_by`, `modified_date`, `created_by`, `created_date`) "
//					+ "VALUES (%s, '%s', '%s', '%s', 'ACTIVE', '0', 'DAY', '0', '100000000', '100000000', "
//					+ "'0', '', '', '1', 'system', '2016-10-18 12:41:00', 'system', '2016-10-18 12:41:00');" 
//					,i, walletId,orgId,orgId));
//		}
		
		//*********Create Request data***********
//		for (int i = 1; i <= 100; i++) {
//			
//			String walletId = WALLET_ID_PREFIX + Strings.padStart(i + "", 10, '0');
//			String desc = "GatlingTest-" + Strings.padStart(i + "", 3, '0');
//			System.out.println(String.format(" \"%s\", \"%s\", 1, \"S4174\", \"ONE_OFF\"", 
//					walletId,desc));
//		}
		
		
		List<Integer> list = Lists.newArrayList(1,21,43,23,12);
		int sum = list.stream().mapToInt(i -> i).sum();
		System.out.println(sum);
	}
}
