package com.kooppi.nttca.wallet.rest.transaction.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import com.google.common.collect.Lists;
import com.kooppi.nttca.ce.domain.ChargingItem;
import com.kooppi.nttca.ce.domain.Payment;
import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.common.filter.context.RequestContextImpl;
import com.kooppi.nttca.portal.common.utils.DateUtils;
import com.kooppi.nttca.portal.common.utils.StringUtils;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;
import com.kooppi.nttca.wallet.common.persistence.domain.Adjustment;
import com.kooppi.nttca.wallet.common.persistence.domain.MapChargeItemToContract;
import com.kooppi.nttca.wallet.common.persistence.domain.Transaction;
import com.kooppi.nttca.wallet.common.persistence.domain.Wallet;
import com.kooppi.nttca.wallet.rest.adjustment.service.AdjustmentService;
import com.kooppi.nttca.wallet.rest.mapChargeItemToContract.repository.MapChargeItemToContractRepository;
import com.kooppi.nttca.wallet.rest.transaction.repository.TransactionRepository;
import com.kooppi.nttca.wallet.rest.wallet.service.WalletService;

public class TransactionServiceImpl implements TransactionService {

	@Inject
	private TransactionRepository transactionRepository;

	@Inject
	private RequestContextImpl rc;

	@Inject
	private WalletService walletService;

	@Inject
	private AdjustmentService adjustmentService;
	
	@Inject
	private MapChargeItemToContractRepository mapChargeItemToContractRepository;

//	@Inject
//	private AdjustmentRepository adjustmentRepository;

	@Override
	public Optional<Transaction> findTransactionByWalletIdAndRecentChargeTime(String walletId,
			LocalDateTime recentChargeTime) {
		return transactionRepository.findTransactionByWalletIdAndIdlePeriod(walletId, recentChargeTime);
	}

	@Override
	public Optional<Transaction> findTransactionByTransactionId(String transactionId) {
		return transactionRepository.findTransactionByTransactionId(transactionId);
	}

	@Override
	public Optional<Transaction> findCommiteSuccessTransactionByTransactionId(String paymentId) {
		return transactionRepository.findCommiteSuccessTransactionByTransactionId(paymentId);
	}
	
	@Override
	public Optional<Transaction> findCommitePendingTransactionByTransactionId(String paymentId) {
		return transactionRepository.findCommitePendingTransactionByTransactionId(paymentId);
	}

	@Override
	public void reverseSuccessTransactionIfExist(Payment payment, String serviceId, String description) {
		// validate success transaction status
		Optional<Transaction> optTransaction = findCommiteSuccessTransactionByTransactionId(payment.getPaymentId());
		if(optTransaction.isPresent()) {
			Transaction transaction = optTransaction.get();
			PortalExceptionUtils.throwIfFalse(transaction.isCancelable(), PortalErrorCode.TRANSACTION_IS_NOT_CANCELABLE);
			
			// validate if the wallet is active
			Wallet wallet = PortalExceptionUtils.throwIfEmpty(walletService.findWalletById(transaction.getWalletId()), PortalErrorCode.INVALID_WALLET_ID);
			PortalExceptionUtils.throwIfFalse(wallet.isActive(), PortalErrorCode.WALLET_IS_INACTIVE);
			for (ChargingItem ci : payment.getChargingItems()) {
				for (MapChargeItemToContract mapItem : ci.getMapChargeItemToContract()) {
					if(mapItem.getContractId() != null) {
						Adjustment adjustment = PortalExceptionUtils.throwIfEmpty(adjustmentService.findByContractId(mapItem.getContractId()), PortalErrorCode.INVALID_CONTRACT_ID);
						adjustment.refundBalance(mapItem.getAmount(), rc.getRequestId());
					}
				}
			}
			
			Transaction rollbackTranssaction = wallet.reverseSuccessTransaction(transaction, wallet, description, rc);
			rollbackTranssaction = transactionRepository.save(rollbackTranssaction);
		}
	}
	
	@Override
	public void reversePendingTransactionIfExist(Payment payment, String serviceId, String description) {
		// validate pengding transaction status
		Optional<Transaction> optTransaction = findCommitePendingTransactionByTransactionId(payment.getPaymentId());
		if(optTransaction.isPresent()) {
			Transaction transaction = optTransaction.get();
//			PortalExceptionUtils.throwIfFalse(transaction.isCancelable(), PortalErrorCode.TRANSACTION_IS_NOT_CANCELABLE);
			
			// validate if the wallet is active
			Wallet wallet = PortalExceptionUtils.throwIfEmpty(walletService.findWalletById(transaction.getWalletId()), PortalErrorCode.INVALID_WALLET_ID);
			PortalExceptionUtils.throwIfFalse(wallet.isActive(), PortalErrorCode.WALLET_IS_INACTIVE);
			for (ChargingItem ci : payment.getChargingItems()) {
				for (MapChargeItemToContract mapItem : ci.getMapChargeItemToContract()) {
					if(mapItem.getContractId() == null) {
						wallet.releaseBufferOnly(mapItem.getAmount(), rc.getRequestId());
						mapChargeItemToContractRepository.remove(mapItem);
					}
				}
			}
			
			wallet.reversePendingTransaction(transaction);
		}
	}

	@Override
	public Transaction createTransaction(Transaction transaction) {
		return transactionRepository.save(transaction);
	}

	@Override
	public ResultList<Transaction> searchTransactionsByAndFilters(RequestContextImpl rc, String transactionId,
			String walletId, String serviceOrder, String serviceId, String requestId, String item, String itemType,
			Timestamp minContractStartTimeStamp, Timestamp maxContractStartTimeStamp, Integer minAmount,
			Integer maxAmount, Integer minBalance, Integer maxBalance, String action, String description,
			String paymentId, Timestamp minChargeTimeStamp, Timestamp maxChargeTimeStamp, String userId,
			String organizationId, String sort, Integer offset, Integer maxRows) {

		// timeStamp to localDate
		LocalDate minContractStartLocalDate = DateUtils.TimestampToLocalDate(minContractStartTimeStamp);
		LocalDate maxContractStartLocalDate = DateUtils.TimestampToLocalDate(maxContractStartTimeStamp);

		// timeStamp to localDateTime
		LocalDateTime minChargeLocalDateTime = DateUtils.TimestampToLocalDateTime(minChargeTimeStamp);
		LocalDateTime maxChargeLocalDateTime = DateUtils.TimestampToLocalDateTime(maxChargeTimeStamp);

		// parse "orderBy=orderSorting"
		String orderBy = null;
		String orderSorting = null;
		if (!sort.isEmpty() && sort != null) {
			orderBy = StringUtils.parseQueryParam(sort).get("orderBy");
			orderSorting = StringUtils.parseQueryParam(sort).get("orderSorting");
		}

		// get permitted Wallet list
		List<String> walletIds = Lists.newArrayList();

		return transactionRepository.searchAvailableTransactions(rc, walletIds, transactionId, walletId, serviceOrder,
				serviceId, requestId, item, itemType, minContractStartLocalDate, maxContractStartLocalDate, minAmount,
				maxAmount, minBalance, maxBalance, action, description, paymentId, minChargeLocalDateTime,
				maxChargeLocalDateTime, userId, organizationId, orderBy, orderSorting, offset, maxRows);
	}

	@Override
	public ResultList<Transaction> searchTransactionsByOrFilter(RequestContextImpl rc, String walletId,
			String globalFilter, String organizationId, String sort, Integer offset, Integer maxRows) {
		// parse "orderBy=orderSorting"
		String orderBy = null;
		String orderSorting = null;
		if (!sort.isEmpty() && sort != null) {
			orderBy = StringUtils.parseQueryParam(sort).get("orderBy");
			orderSorting = StringUtils.parseQueryParam(sort).get("orderSorting");
		}

		// get permitted Wallet list
		List<String> walletIds = Lists.newArrayList();

		return transactionRepository.searchTransactionsByOrFilter(rc, walletId, walletIds, globalFilter, organizationId,
				orderBy, orderSorting, offset, maxRows);
	}

	@Override
	public List<Transaction> findForfeitedTransactionsByTransactionId(String transactionId) {
		return transactionRepository.findForfeitTransactionByTransactionId(transactionId);
	}

	@Override
	public Optional<Transaction> findTransactionByParentTransactionId(String parentTransactionId) {
		return transactionRepository.findTransactionByParentTransactionId(parentTransactionId);
	}

	@Override
	public List<Transaction> findPendingTransactionsByWalletId(String walletId) {
		return transactionRepository.findPendingTransactionsByWalletId(walletId);
	}
}
