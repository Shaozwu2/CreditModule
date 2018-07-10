package com.kooppi.nttca.wallet.rest.transaction.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.common.filter.context.RequestContextImpl;
import com.kooppi.nttca.wallet.common.persistence.domain.Transaction;

public interface TransactionRepository {

	public Transaction save(Transaction transaction);
	
	public Optional<Transaction> findTransactionByWalletIdAndIdlePeriod(String walletId, LocalDateTime recentChargeTime);
	
	public List<Transaction> findForfeitTransactionByTransactionId(String transactionId);

	public Optional<Transaction> findTransactionByTransactionId(String transactionId);
	public Optional<Transaction> findTransactionByParentTransactionId(String parentTransactionId);

	public Optional<Number> sumUpTransactionsOfPreviousDayByWalletId(String walletId);

	public Optional<Number> sumUpTransactionsOfLastMonthByWalletId(String walletId);

	public ResultList<Transaction> searchAvailableTransactions(RequestContextImpl rc, List<String> wallets, 
			String transactionId, String walletId, String serviceOrder, String serviceId, String requestId, String item, String itemType, 
			LocalDate minContractStartDate, LocalDate maxContractStartDate,Integer minAmount, Integer maxAmount, Integer minBalance, 
			Integer maxBalance, String action, String description, String paymentId, LocalDateTime minChargeDate, LocalDateTime maxChargeDate, 
			String userId, String organizationId, String orderBy, String orderSorting, Integer offset, Integer maxRows);
	
	public ResultList<Transaction> searchTransactionsByOrFilter(RequestContextImpl rc, String walletId, List<String> wallets,
			String globalFilter, String organizationId, String orderBy, String sort, Integer offset, Integer maxRows);

	public Optional<Transaction> findCommiteSuccessTransactionByTransactionId(String paymentId);

	public Optional<Transaction> findCommitePendingTransactionByTransactionId(String paymentId);
	
	public List<Transaction> findPendingTransactionsByWalletId(String walletId);
}
