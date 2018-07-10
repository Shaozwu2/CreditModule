package com.kooppi.nttca.wallet.rest.adjustment.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kooppi.nttca.ce.domain.ChargingItem;
import com.kooppi.nttca.ce.domain.Payment;
import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.ce.payment.service.PaymentService;
import com.kooppi.nttca.portal.common.email.service.UtilizationAlertService;
import com.kooppi.nttca.portal.common.filter.context.RequestContextImpl;
import com.kooppi.nttca.portal.common.utils.DateUtils;
import com.kooppi.nttca.portal.common.utils.StringUtils;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;
import com.kooppi.nttca.portal.wallet.domain.AdjustmentStatus;
import com.kooppi.nttca.portal.wallet.domain.AdjustmentType;
import com.kooppi.nttca.portal.wallet.dto.adjustment.AdjustmentSearchDto;
import com.kooppi.nttca.wallet.common.persistence.domain.Adjustment;
import com.kooppi.nttca.wallet.common.persistence.domain.ContractToBuMaster;
import com.kooppi.nttca.wallet.common.persistence.domain.ContractToPriceBook;
import com.kooppi.nttca.wallet.common.persistence.domain.CurrencyRate;
import com.kooppi.nttca.wallet.common.persistence.domain.MapChargeItemToContract;
import com.kooppi.nttca.wallet.common.persistence.domain.Transaction;
import com.kooppi.nttca.wallet.common.persistence.domain.Wallet;
import com.kooppi.nttca.wallet.rest.CurrencyRate.service.CurrencyRateService;
import com.kooppi.nttca.wallet.rest.adjustment.repository.AdjustmentRepository;
import com.kooppi.nttca.wallet.rest.chargeItem.service.ChargeItemService;
import com.kooppi.nttca.wallet.rest.mapChargeItemToContract.repository.MapChargeItemToContractRepository;
import com.kooppi.nttca.wallet.rest.transaction.service.TransactionService;
import com.kooppi.nttca.wallet.rest.wallet.service.WalletService;

@ApplicationScoped
public class AdjustmentServiceImpl implements AdjustmentService{
	
	private static final Logger logger = LoggerFactory.getLogger(AdjustmentServiceImpl.class);

	@Inject
	private AdjustmentRepository adjustmentRepository;
	
	@Inject
	private TransactionService transactionService;

	@Inject
	private PaymentService paymentService;
	
	@Inject
	private WalletService walletService;
	
	@Inject
	private ChargeItemService chargeItemService;

	@Inject
	private MapChargeItemToContractRepository mapChargeItemToContractRepository;
	
	@Inject
	private UtilizationAlertService alertService;
	
	@Inject
	private CurrencyRateService currencyRateService;
	
	@Inject
	private RequestContextImpl rc;
	
	@Transactional(value = TxType.REQUIRED)
	@Override
	public Adjustment createAdjustment(String walletId, String refNumber, AdjustmentType adjustmentType,
			String companyCode, Integer amount, BigDecimal currencyAmount, String currencyCode, BigDecimal exchangeRate,
			String description, LocalDate contractEffectiveDate, LocalDate contractTerminationDate,
			LocalDate creditExpiryDate, String terminatedReason, List<String> buLists, List<String> productLists,
			Boolean isAllBu, Boolean isAllProduct) {
		Optional<Wallet> walletOpt = walletService.findWalletById(walletId);
		Wallet wallet = PortalExceptionUtils.throwIfEmpty(walletOpt, PortalErrorCode.INVALID_WALLET_ID);
//		PortalExceptionUtils.throwIfFalse(wallet.isActive(), PortalErrorCode.WALLET_IS_INACTIVE);
		if (amount < 0) {
			PortalExceptionUtils.throwIfFalse(wallet.hasEnoughCredit(Math.abs(amount)), PortalErrorCode.ADJUSTMENT_NOT_ACCEPT);
		}
		
		Optional<CurrencyRate> currencyOpt = currencyRateService.findCurrencyByCurrencyCode(currencyCode);
		if (!currencyOpt.isPresent()) {
			currencyOpt = currencyRateService.findCurrencyByCurrencyCode("HKD");
		}
		Integer currencyRateId = currencyOpt.get().getCurrencyRateId();
		
		if (creditExpiryDate == null)
			creditExpiryDate = contractEffectiveDate.plusYears(3).minusDays(1);

		Adjustment adjustment = Adjustment.create(walletId, refNumber, adjustmentType, companyCode, amount, currencyAmount,
				currencyRateId, exchangeRate, contractEffectiveDate, contractTerminationDate, creditExpiryDate,
				description, terminatedReason, isAllBu, isAllProduct, rc.getRequestUserId());
		
		if(adjustmentType == AdjustmentType.COMPENSATION) {
			if (!isAllBu && !isAllProduct) {
				for (String bu : buLists) {
					adjustment.getBuMasters().add(ContractToBuMaster.create(null, bu));
				}
				for (String pb : productLists) {
					adjustment.getPriceBooks().add(ContractToPriceBook.create(null, pb));
				}
			} else if (!isAllBu && isAllProduct) {
				for (String bu : buLists) {
					adjustment.getBuMasters().add(ContractToBuMaster.create(null, bu));
				}
			}
		}
		adjustment = adjustmentRepository.saveAndRefresh(adjustment);
		
		//check create Transaction
		if (adjustment.isOneoff()) {
			if (LocalDate.now().equals(adjustment.getContractEffectiveDate())) {
				Transaction transaction = wallet.addOneOffAdjustment(adjustment, rc.getRequestUserId());
				transaction = transactionService.createTransaction(transaction);

				// check wallet buffer and deduce used buffer
				List<Transaction> pendingTransactions = transactionService.findPendingTransactionsByWalletId(walletId);

				// release buffer
				try {
					Integer creditToBeConsumed = 0;
					Integer creditConsumed = 0;
					for (Transaction t : pendingTransactions) {
						creditToBeConsumed = Math.abs(t.getWalletAmount());
						if (adjustment.getAmount() >= creditToBeConsumed) {
							creditConsumed = adjustment.consumeAndUpdateBalance(creditToBeConsumed, rc.getRequestUserId());
							wallet.releaseBuffer(creditConsumed, rc.getRequestUserId());
							
							Payment payment = PortalExceptionUtils.throwIfEmpty(paymentService.findByPaymentId(t.getPaymentId()), PortalErrorCode.PAYMENT_INVALID_PAYMENT_ID);
							for (ChargingItem ci : payment.getChargingItems()) {
								for (MapChargeItemToContract map : ci.getMapChargeItemToContract()) {
									if (map.getContractId() == null) 
										map.updateContractId(adjustment.getContractId());
								}
							}
							t.completeTransaction();
						}
					}
				} catch (Exception e) {
					logger.info("skip release buffer due to exception happen");
				}

				// check wallet balance and sent alert
				if (amount > 0)
					wallet.clearAlertLastSentDate();
				else
					alertService.checkAndSendAlert(wallet);
			}
		} else if (adjustment.isCompensation()) {
			if (LocalDate.now().equals(adjustment.getContractEffectiveDate())) {
				Transaction transaction = wallet.addOneOffAdjustment(adjustment, rc.getRequestUserId());
				transaction = transactionService.createTransaction(transaction);

				// check wallet balance and sent alert
				if (amount > 0)
					wallet.clearAlertLastSentDate();
				else
					alertService.checkAndSendAlert(wallet);
			}
		}

//		else if(adjustmentType==AdjustmentType.MONTHLY_RECHARGE) {
			//let background job do this
//			LocalDate transactionDate = LocalDate.now();
//			if(adjustment.getStartMonth()!=null) {
//				transactionDate = adjustment.calculateTransactionDateForMonthlyRecharge();
//			}
//			if ((LocalDate.now().isAfter(transactionDate) || LocalDate.now().isEqual(transactionDate))) {
//				Adjustment childAdjustment = adjustment.createChild(rc.getRequestUserId());
//				childAdjustment = adjustmentRepository.saveAndRefresh(childAdjustment);
//				
//				Transaction transaction = wallet.addAdjustment(childAdjustment,rc);
//				transaction = transactionService.createTransaction(transaction);
//				alertService.checkAndSendAlert(wallet);
//			}
//		}
		return adjustment;
	}
	
	@Transactional
	@Override
	public Adjustment updateAdjustment(String walletId, Adjustment adjustment, String refNumber, String companyCode, Integer amount, 
			BigDecimal currencyAmount, String currencyCode, BigDecimal exchangeRate, String description, LocalDate contractEffectiveDate, 
			LocalDate contractTerminationDate, LocalDate creditExpiryDate, String terminatedReason, List<String> buLists, List<String> productLists,
			Boolean isAllBu, Boolean isAllProduct) {
		Wallet wallet = PortalExceptionUtils.throwIfEmpty(walletService.findWalletById(walletId), PortalErrorCode.INVALID_WALLET_ID);
		PortalExceptionUtils.throwIfFalse(wallet.isActive(), PortalErrorCode.WALLET_IS_INACTIVE);
		if (amount < 0) {
			PortalExceptionUtils.throwIfFalse(wallet.hasEnoughCredit(Math.abs(amount)), PortalErrorCode.ADJUSTMENT_NOT_ACCEPT);
		}
		
		Optional<CurrencyRate> currencyOpt = currencyRateService.findCurrencyByCurrencyCode(currencyCode);
		if (!currencyOpt.isPresent()) {
			currencyOpt = currencyRateService.findCurrencyByCurrencyCode("HKD");
		}
		Integer currencyRateId = currencyOpt.get().getCurrencyRateId();
		adjustment.update(refNumber, companyCode, amount, currencyAmount, currencyRateId, exchangeRate, 
				contractEffectiveDate, contractTerminationDate, creditExpiryDate, description, terminatedReason, rc.getRequestUserId(),
				isAllBu, isAllProduct);
		
		if(adjustment.getAdjustmentType() == AdjustmentType.COMPENSATION && adjustment.getStatus().equals(AdjustmentStatus.PENDING)) {
			adjustment.getBuMasters().clear();
			adjustment.getPriceBooks().clear();
			if (!isAllBu && !isAllProduct) {
				//clear all and add all
				for (String bu : buLists) {
					adjustment.getBuMasters().add(ContractToBuMaster.create(null, bu));
				}
				for (String pb : productLists) {
					adjustment.getPriceBooks().add(ContractToPriceBook.create(null, pb));
				}
			} else if (!isAllBu && isAllProduct) {
				// not all bu and all product clear all product
				for (String bu : buLists) {
					adjustment.getBuMasters().add(ContractToBuMaster.create(null, bu));
				}
			}
		}
		
		
		
		if(adjustment.isOneoff() || adjustment.isCompensation()) {
			//check create Transaction
			if (LocalDate.now().equals(contractEffectiveDate)) {
				Transaction transaction = wallet.addOneOffAdjustment(adjustment,rc.getRequestUserId());
				transaction = transactionService.createTransaction(transaction);
				
				if(amount>0)
					wallet.clearAlertLastSentDate();
				else 
					alertService.checkAndSendAlert(wallet);
			}
		}
//		else if(adjustment.getAdjustmentType()==AdjustmentType.MONTHLY_RECHARGE) {
			//let background job do this
//			//check create Transaction and add child adjustment
//			LocalDate transactionDate = adjustment.calculateTransactionDateForMonthlyRecharge();
//			if ((LocalDate.now().isAfter(transactionDate) || LocalDate.now().isEqual(transactionDate))) {
//				ResultList<Adjustment> childAdjustmentsResult = adjustmentRepository.findChildAdjustmentByParentTransactionId(adjustment.getTransactionId());
//				
//				boolean createChildAdjustment = !childAdjustmentsResult.stream().anyMatch(adj ->{
//					//if has child adjustment, which is equal to the current month =>no need create new adjustment
//					if(adj.isSuccess() && adj.getTransactionDate().getMonth().equals(LocalDate.now().getMonth())){
//						return true;
//					} else return false;
//				});
//				
//				if(createChildAdjustment) {
//					Adjustment childAdjustment = adjustment.createChild(rc.getRequestUserId());
//					childAdjustment = adjustmentRepository.saveAndRefresh(childAdjustment);
//					
//					Transaction transaction = wallet.addAdjustment(childAdjustment,rc);
//					transaction = transactionService.createTransaction(transaction);
//					alertService.checkAndSendAlert(wallet);
//				}	
//			}
//		}
		return adjustment;
	}

	@Override
	public Optional<Adjustment> findByTransactionId(String transactionId) {
		return adjustmentRepository.findByTransactionId(transactionId);
	}
	
	@Override
	public Optional<Adjustment> findByContractId(String contractId) {
		return adjustmentRepository.findByContractId(contractId);
	}

	@Override
	public ResultList<Adjustment> serachAdjustment(RequestContextImpl rc, String walletId, String organizationId, String transactionId, String parentTransactionId, String refNumber, AdjustmentType adjustmentType, Timestamp minTransactionTimestamp, Timestamp maxTransactionTimestamp, Integer minAmount, Integer maxAmount, AdjustmentStatus status, String sort, Integer offset, Integer maxRows, Boolean isChild) {
		// timeStamp to localDate
		LocalDateTime minTransactionDate = DateUtils.TimestampToLocalDateTime(minTransactionTimestamp);
		LocalDateTime maxTransactionDate = DateUtils.TimestampToLocalDateTime(maxTransactionTimestamp);
		
		String orderBy = null;
		String orderSorting = null;
		if (!sort.isEmpty() && sort != null) {
			orderBy = StringUtils.parseQueryParam(sort).get("orderBy");
			orderSorting = StringUtils.parseQueryParam(sort).get("orderSorting");
		}
				
		return adjustmentRepository.searchAdjustment(rc, walletId, organizationId, transactionId, parentTransactionId, refNumber, adjustmentType, minTransactionDate, maxTransactionDate, minAmount, maxAmount, status, orderBy, orderSorting, offset, maxRows, isChild);
	}

	@Override
	public List<Adjustment> findAllScheduledAdjustmentOnToday() {
		return adjustmentRepository.findAllScheduledAdjustmentOnToday();
	}
	
	@Override
	public List<Adjustment> findAllPendingAndInUseAdjustment() {
		return adjustmentRepository.findAllPendingAndInUseAdjustment();
	}

	@Override
	public ResultList<Adjustment> searchAdjustmentsByOrFilter(RequestContextImpl rc, String organizationId, String walletId, AdjustmentStatus adjustmentStatus,
			Boolean isExpiredCotract, String globalFilter, String sort, Integer offset, Integer maxRows, AdjustmentSearchDto searchDto) {
		
		String orderBy = null;
		String orderSorting = null;
		if (sort != null && !sort.isEmpty()) {
			orderBy = StringUtils.parseQueryParam(sort).get("orderBy");
			orderSorting = StringUtils.parseQueryParam(sort).get("orderSorting");
		}
		
		return adjustmentRepository.searchAdjustmentsByOrFilter(rc, organizationId, walletId, adjustmentStatus, isExpiredCotract, globalFilter, orderBy, orderSorting, offset, maxRows, searchDto.getOrganizationNames());
	}

	@Override
	public void deleteOrCancelAdjustment(Adjustment adjustment, String userId) {
		LocalDate contractEffectiveDay = adjustment.getContractEffectiveDate();
		LocalDate now = LocalDate.now();
		PortalExceptionUtils.throwIfNull(contractEffectiveDay, PortalErrorCode.MISS_PARAM_CONTRACT_EFFECTIVE_DATE);
		
		if (now.isBefore(contractEffectiveDay)) {
			//allow to delete SO
			adjustmentRepository.deleteAdjustment(adjustment);
		} else {
			// keep value for transaction use
			Integer transactionAmount = adjustment.getAmount() * (-1);
			BigDecimal transactionCurrencyAmount = adjustment.getCurrencyAmount().negate();
			
			// allow to cancel SO
			Integer soBalance = adjustment.getBalance();
			adjustment.cancel(soBalance, userId);

			// in this case, need create transaction, find wallet first
			Wallet wallet = PortalExceptionUtils.throwIfEmpty(walletService.findWalletById(adjustment.getWalletId()), PortalErrorCode.INVALID_WALLET_ID);

			// create Transaction and check alert
			logger.debug("adjustment type = " + adjustment.getAdjustmentType());
			if (adjustment.isOneoff() || adjustment.isCompensation()) {
				Transaction transaction = wallet.deleteOrCancelAdjustment(adjustment, rc, transactionAmount, transactionCurrencyAmount);
				transaction = transactionService.createTransaction(transaction);
			}
			alertService.checkAndSendAlert(wallet);
		}
	}

	@Override
	public void forfeitAdjustment(Adjustment adjustment, String userId) {
		Wallet wallet = PortalExceptionUtils.throwIfEmpty(walletService.findWalletById(adjustment.getWalletId()), PortalErrorCode.INVALID_WALLET_ID);
		
		Integer remainingBalance = adjustment.getBalance();
		BigDecimal remainingCurrency = adjustment.getCurrencyBalance();
		
		//forfeit contract
		adjustment.forfeit(userId);
		
		if (adjustment.isOneoff() || adjustment.isCompensation()) {
			
			// create Transaction and check alert
			Transaction transaction = wallet.forfeitAdjustment(adjustment, rc, remainingBalance, remainingCurrency);
			transaction = transactionService.createTransaction(transaction);

			ChargingItem chargingItem = ChargingItem.createForfeitItem(remainingBalance, transaction.getTransactionId());
			chargingItem.getMapChargeItemToContract().add(MapChargeItemToContract.create(adjustment.getContractId(),
					chargingItem.getChargeItemId(), chargingItem.getTotalAmount()));
			chargeItemService.createChargingItem(chargingItem);
		}
		alertService.checkAndSendAlert(wallet);
	}

	@Override
	public void cancelForfeitAdjustment(Adjustment adjustment, String userId) {
		Wallet wallet = PortalExceptionUtils.throwIfEmpty(walletService.findWalletById(adjustment.getWalletId()), PortalErrorCode.INVALID_WALLET_ID);
		List<MapChargeItemToContract> mapChargeItemToContracts = mapChargeItemToContractRepository.findMapChargeItemToContractByContractId(adjustment.getContractId());
		int amount = 0;
		BigDecimal currency = BigDecimal.ZERO;
		for (MapChargeItemToContract mapChargeItemToContract : mapChargeItemToContracts) {
			for (ChargingItem chargingItem : mapChargeItemToContract.getChargeItems()) {
				String transactionId = chargingItem.getTransactionId();
				if (transactionId != null) {
					List<Transaction> transactions = transactionService.findForfeitedTransactionsByTransactionId(transactionId);
					for (Transaction tx : transactions) {
						Optional<Transaction> optTransaction = transactionService.findTransactionByParentTransactionId(tx.getTransactionId());
						if(!optTransaction.isPresent()) {
							amount = Math.abs(tx.getWalletAmount());
							currency = tx.getCurrencyAmount().abs();
							
							// cancel forfeit contract
							adjustment.cancelForfeit(amount, currency, userId);
							
							Transaction transaction = wallet.cancelForfeitAdjustment(adjustment, rc, tx.getTransactionId(), amount, currency);
							transaction = transactionService.createTransaction(transaction);
						}
					}
					//remove chargeItem
//					chargeItemService.deleteChargeItem(chargingItem);
				}
			}
		}
	}
}
