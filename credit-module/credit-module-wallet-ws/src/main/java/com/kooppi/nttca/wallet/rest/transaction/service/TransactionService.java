package com.kooppi.nttca.wallet.rest.transaction.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.kooppi.nttca.ce.domain.Payment;
import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.common.filter.context.RequestContextImpl;
import com.kooppi.nttca.wallet.common.persistence.domain.Transaction;

public interface TransactionService {

	public Optional<Transaction> findTransactionByWalletIdAndRecentChargeTime(String walletId, LocalDateTime recentChargeTime);

	public List<Transaction> findForfeitedTransactionsByTransactionId(String transactionId);
	
	public Optional<Transaction> findTransactionByTransactionId(String transactionId);

	public Optional<Transaction> findTransactionByParentTransactionId(String parentTransactionId);
	
	public ResultList<Transaction> searchTransactionsByAndFilters(
			RequestContextImpl rc, String transactionId, String walletId, String serviceOrder, String serviceId,
			String requestId, String item, String itemType, Timestamp minContractStartTimeStamp, Timestamp maxContractStartTimeStamp, 
			Integer minAmount, Integer maxAmount, Integer minBalance, Integer maxBalance, String action, String description, String paymentId, 
			Timestamp minChargeTimeStamp, Timestamp maxChargeTimeStamp, String userId, String organizationId, String sort, Integer offset, Integer maxRows);
	
	public ResultList<Transaction> searchTransactionsByOrFilter(RequestContextImpl rc, String walletId, String globalFilter, String organizationId, String sort, Integer offset, Integer maxRows);

	public Transaction createTransaction(Transaction transaction);
	
	public Optional<Transaction> findCommiteSuccessTransactionByTransactionId(String paymentId);

	public Optional<Transaction> findCommitePendingTransactionByTransactionId(String paymentId);

	public void reverseSuccessTransactionIfExist(Payment payment, String serviceId, String description);

	public void reversePendingTransactionIfExist(Payment payment, String serviceId, String description);
	
	public List<Transaction>  findPendingTransactionsByWalletId(String walletId);
}
