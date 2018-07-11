package com.kooppi.nttca.wallet.rest.transaction.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.google.common.base.Strings;
import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.common.filter.context.RequestContextImpl;
import com.kooppi.nttca.portal.common.repository.BasicRepository;
import com.kooppi.nttca.portal.common.utils.StringUtils;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;
import com.kooppi.nttca.portal.wallet.domain.TransactionState;
import com.kooppi.nttca.portal.wallet.domain.TransactionStatus;
import com.kooppi.nttca.wallet.common.persistence.domain.Transaction;
import com.kooppi.nttca.wallet.persistence.producer.WalletDataSource;

@ApplicationScoped
public class TransactionRepositorySqlImpl extends BasicRepository implements TransactionRepository{

	private EntityManager em;
	
	TransactionRepositorySqlImpl() {}
	
	@Inject
	public TransactionRepositorySqlImpl(@WalletDataSource EntityManager em) {
		this.em = em;
	}
	
	@Override
	public Transaction save(Transaction transaction) {
		em.persist(transaction);
		em.flush();
		em.refresh(transaction);
		if (transaction.getTransactionId() == null) {
			transaction.updateTransactionId();
		}
		return transaction;
	}

	@Override
	public Optional<Transaction> findTransactionByTransactionId(String transactionId) {
		PortalExceptionUtils.throwIfNull(transactionId, PortalErrorCode.INVALID_TRANSACTION_ID);
		String query = "from Transaction t where t.transactionId = :transactionId";
		TypedQuery<Transaction> tq = em.createQuery(query, Transaction.class);
		tq.setParameter("transactionId", transactionId);
		return getSingleResult(tq);
	}
	
	@Override
	public Optional<Transaction> findTransactionByWalletIdAndIdlePeriod(String walletId, LocalDateTime recentChargeTime) {
		LocalDateTime currentChargeTime = LocalDateTime.now();
		
		String query = "from Transaction t where t.walletId = :walletId and t.chargeDate between :recentChargeTime and :currentChargeTime";
		TypedQuery<Transaction> tq = em.createQuery(query,Transaction.class);
		tq.setParameter("walletId", walletId);
		tq.setParameter("recentChargeTime", recentChargeTime);
		tq.setParameter("currentChargeTime", currentChargeTime);
		return getSingleResult(tq);
	}
	

	@Override
	public Optional<Transaction> findCommiteSuccessTransactionByTransactionId(String paymentId) {
		String query = "from Transaction t where t.paymentId = :paymentId and t.state = :state and t.status = :status";
		TypedQuery<Transaction> tq = em.createQuery(query,Transaction.class);
		tq.setParameter("paymentId", paymentId);
		tq.setParameter("state", TransactionState.COMMITE);
		tq.setParameter("status", TransactionStatus.SUCCESS);
		return getSingleResult(tq);
	}

	@Override
	public List<Transaction> findForfeitTransactionByTransactionId(String transactionId) {
		PortalExceptionUtils.throwIfNull(transactionId, PortalErrorCode.INVALID_TRANSACTION_ID);
		String query = "from Transaction t where t.transactionId = :transactionId and t.state = :state and t.status = :status";
		TypedQuery<Transaction> tq = em.createQuery(query, Transaction.class);
		tq.setParameter("transactionId", transactionId);
		tq.setParameter("state", TransactionState.FORFEIT);
		tq.setParameter("status", TransactionStatus.SUCCESS);
		return tq.getResultList();
	}
	
	@Override
	public ResultList<Transaction> searchAvailableTransactions(RequestContextImpl rc, List<String> endUserWalletIds, 
			String transactionId, String walletId, String serviceOrder, String serviceId, String requestId, String item, String itemType, 
			LocalDate minContractStartDate, LocalDate maxContractStartDate, Integer minAmount,Integer maxAmount, Integer minBalance, 
			Integer maxBalance, String action, String description, String paymentId, LocalDateTime minChargeDate,LocalDateTime maxChargeDate, String userId, 
			String organizationId, String orderBy, String orderSorting, Integer offset, Integer maxRows) {

		StringBuffer sb = new StringBuffer(" from Transaction t JOIN FETCH t.wallet w where 1=1");
		StringBuffer countSb = new StringBuffer(" select count(t.transactionId) from Transaction t JOIN FETCH t.wallet w where 1=1");
		
		sb.append(" and t.status = :status");
		countSb.append(" and t.status = :status");
		
		if(!Strings.isNullOrEmpty(transactionId)){
			sb.append(" and t.transactionId like :transactionId");
			countSb.append(" and t.transactionId like :transactionId");
		}
		
		if(!Strings.isNullOrEmpty(walletId)){
			sb.append(" and t.walletId like :walletId");
			countSb.append(" and t.walletId like :walletId");
		}
		
		if(!Strings.isNullOrEmpty(serviceOrder)){
			sb.append(" and t.serviceOrder like :serviceOrder");
			countSb.append(" and t.serviceOrder like :serviceOrder");
		}
		
		if(!Strings.isNullOrEmpty(serviceId)){
			sb.append(" and t.serviceId like :serviceId");
			countSb.append(" and t.serviceId like :serviceId");
		}
		
		if(!Strings.isNullOrEmpty(requestId)){
			sb.append(" and t.requestId like :requestId");
			countSb.append(" and t.requestId like :requestId");
		}
		
		if(!Strings.isNullOrEmpty(item)){
			sb.append(" and t.item like :item");
			countSb.append(" and t.item like :item");
		}

		if(!Strings.isNullOrEmpty(itemType)){
			sb.append(" and t.itemType like :itemType");
			countSb.append(" and t.itemType like :itemType");
		}
		
		if(minContractStartDate != null){
			sb.append(" and t.contractStartDate >= :minContractStartDate");
			countSb.append(" and t.contractStartDate >= :minContractStartDate");
		}
		
		if(maxContractStartDate != null){
			sb.append(" and t.contractStartDate <= :maxContractStartDate");
			countSb.append(" and t.contractStartDate <= :maxContractStartDate");
		}
		
		if(minAmount != null ){
			sb.append(" and t.amount >= :minAmount");
			countSb.append(" and t.amount >= :minAmount");
		}
		
		if(maxAmount != null ){
			sb.append(" and t.amount <= :maxAmount");
			countSb.append(" and t.amount <= :maxAmount");
		}
		
		if(minBalance != null ){
			sb.append(" and t.balance >= :minBalance");
			countSb.append(" and t.balance >= :minBalance");
		}
		
		if(maxBalance != null ){
			sb.append(" and t.balance <= :maxBalance");
			countSb.append(" and t.balance <= :maxBalance");
		}
		
		if(!Strings.isNullOrEmpty(action)){
			sb.append(" and t.action like :action");
			countSb.append(" and t.action like :action");
		}
		
		if(!Strings.isNullOrEmpty(item)){
			sb.append(" and t.item like :item");
			countSb.append(" and t.item like :item");
		}
		
		if(!Strings.isNullOrEmpty(description)){
			sb.append(" and t.description like :description");
			countSb.append(" and t.description like :description");
		}
		
		if(!Strings.isNullOrEmpty(paymentId)){
			sb.append(" and t.paymentId like :paymentId");
			countSb.append(" and t.paymentId like :paymentId");
		}
		
		if(minChargeDate != null){
			sb.append(" and t.chargeDate >= :minChargeDate");
			countSb.append(" and t.chargeDate >= :minChargeDate");
		}
		
		if(maxChargeDate != null){
			sb.append(" and t.chargeDate <= :maxChargeDate");
			countSb.append(" and t.chargeDate <= :maxChargeDate");
		}
		
		if(!Strings.isNullOrEmpty(userId)){
			sb.append(" and t.userId like :userId");
			countSb.append(" and t.userId like :userId");
		}
		
		if (orderBy != null) {
			sb.append(" order by t." + orderBy);
			
			if (orderSorting != null) {
				sb.append(" " + orderSorting);
			}
		} else {
			sb.append(" order by t.uid");
			if (orderSorting == null) {
				sb.append(" asc");
			}
		}
		
		String sql = sb.toString();
		String countSql = countSb.toString();
		
		TypedQuery<Transaction> tq = em.createQuery(sql,Transaction.class);
		TypedQuery<Long> cq = em.createQuery(countSql,Long.class);
		
		tq.setParameter("status", TransactionStatus.SUCCESS);
		cq.setParameter("status", TransactionStatus.SUCCESS);

		if(!Strings.isNullOrEmpty(transactionId)){
			tq.setParameter("transactionId", StringUtils.enclosePercentWildcard(transactionId));
			cq.setParameter("transactionId", StringUtils.enclosePercentWildcard(transactionId));
		}
		
		if(!Strings.isNullOrEmpty(walletId)){
			tq.setParameter("walletId", StringUtils.enclosePercentWildcard(walletId));
			cq.setParameter("walletId", StringUtils.enclosePercentWildcard(walletId));
		}
		
		if(!Strings.isNullOrEmpty(serviceOrder)){
			tq.setParameter("serviceOrder", StringUtils.enclosePercentWildcard(serviceOrder));
			cq.setParameter("serviceOrder", StringUtils.enclosePercentWildcard(serviceOrder));
		}
		
		if(!Strings.isNullOrEmpty(serviceId)){
			tq.setParameter("serviceId", StringUtils.enclosePercentWildcard(serviceId));
			cq.setParameter("serviceId", StringUtils.enclosePercentWildcard(serviceId));
		}
		
		if(!Strings.isNullOrEmpty(requestId)){
			tq.setParameter("requestId", StringUtils.enclosePercentWildcard(requestId));
			cq.setParameter("requestId", StringUtils.enclosePercentWildcard(requestId));
		}
		
		if(!Strings.isNullOrEmpty(item)){
			tq.setParameter("item", StringUtils.enclosePercentWildcard(item));
			cq.setParameter("item", StringUtils.enclosePercentWildcard(item));
		}
		
		if(!Strings.isNullOrEmpty(itemType)){
			tq.setParameter("itemType", StringUtils.enclosePercentWildcard(itemType));
			cq.setParameter("itemType", StringUtils.enclosePercentWildcard(itemType));
		}
		
		if(minContractStartDate != null){
			tq.setParameter("minContractStartDate", minContractStartDate);
			cq.setParameter("minContractStartDate", minContractStartDate);
		}
		
		if(maxContractStartDate != null){
			tq.setParameter("maxContractStartDate", maxContractStartDate);
			cq.setParameter("maxContractStartDate", maxContractStartDate);
		}
		
		if(minAmount != null ){
			tq.setParameter("minAmount", minAmount);
			cq.setParameter("minAmount", minAmount);
		}
		
		if(maxAmount != null ){
			tq.setParameter("maxAmount", maxAmount);
			cq.setParameter("maxAmount", maxAmount);
		}
		
		if(minBalance != null ){
			tq.setParameter("minBalance", minBalance);
			cq.setParameter("minBalance", minBalance);
		}
		
		if(maxBalance != null ){
			tq.setParameter("maxBalance", maxBalance);
			cq.setParameter("maxBalance", maxBalance);
		}
		
		if(!Strings.isNullOrEmpty(action)){
			tq.setParameter("action", StringUtils.enclosePercentWildcard(action));
			cq.setParameter("action", StringUtils.enclosePercentWildcard(action));
		}
		
		if(!Strings.isNullOrEmpty(item)){
			tq.setParameter("item", StringUtils.enclosePercentWildcard(item));
			cq.setParameter("item", StringUtils.enclosePercentWildcard(item));
		}
		
		if(!Strings.isNullOrEmpty(description)){
			tq.setParameter("description", StringUtils.enclosePercentWildcard(description));
			cq.setParameter("description", StringUtils.enclosePercentWildcard(description));
		}
		
		if(!Strings.isNullOrEmpty(paymentId)){
			tq.setParameter("paymentId", StringUtils.enclosePercentWildcard(paymentId));
			cq.setParameter("paymentId", StringUtils.enclosePercentWildcard(paymentId));
		}
		
		if(!Strings.isNullOrEmpty(description)){
			tq.setParameter("description", StringUtils.enclosePercentWildcard(description));
			cq.setParameter("description", StringUtils.enclosePercentWildcard(description));
		}

		if(minChargeDate != null){
			tq.setParameter("minChargeDate", minChargeDate);
			cq.setParameter("minChargeDate", minChargeDate);
		}
		
		if(maxChargeDate != null){
			tq.setParameter("maxChargeDate", maxChargeDate);
			cq.setParameter("maxChargeDate", maxChargeDate);
		}
		
		if(!Strings.isNullOrEmpty(userId)){
			tq.setParameter("userId", StringUtils.enclosePercentWildcard(userId));
			cq.setParameter("userId", StringUtils.enclosePercentWildcard(userId));
		}
		
		if (offset !=null && offset > 0) {
    		tq.setFirstResult(offset);
    	}
		
    	if (maxRows !=null && maxRows > 0) {
    		tq.setMaxResults(maxRows);
    	}
    	
    	List<Transaction> result = tq.getResultList();
		Long totalCount = cq.getSingleResult();
		
		return new ResultList<Transaction>(result, totalCount.intValue());
	}

	@Override
	public Optional<Number> sumUpTransactionsOfPreviousDayByWalletId(String walletId) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime currentDay = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 0, 0, 0);
		
		LocalDateTime previousDay = currentDay.minusDays(1);
		String query = "select sum(t.amount) from Transaction t where t.walletId = :walletId "
//						+ "and t.source = :source "
						+ "and t.chargeDate between :previousDay and :currentDay";
		
		TypedQuery<Number> tq = em.createQuery(query, Number.class);
		tq.setParameter("walletId", walletId);
//		tq.setParameter("source", Source.SERVICE);
		tq.setParameter("previousDay", previousDay);
		tq.setParameter("currentDay", currentDay);
		try {
    		return Optional.ofNullable(tq.getSingleResult());
    	} catch (NoResultException e) {
    		return Optional.empty();
    	}
	}

	@Override
	public Optional<Number> sumUpTransactionsOfLastMonthByWalletId(String walletId) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime currentMonth = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 0, 0, 0);
		
		LocalDateTime lastMonth = currentMonth.minusMonths(1);
		String query = "select SUM(t.amount) from Transaction t where t.walletId = :walletId "
//						+ "and t.source = :source "
						+ "and t.chargeDate between :lastMonth and :currentMonth";
		
		TypedQuery<Number> tq = em.createQuery(query, Number.class);
		tq.setParameter("walletId", walletId);
//		tq.setParameter("source", Source.SERVICE);
		tq.setParameter("lastMonth", lastMonth);
		tq.setParameter("currentMonth", currentMonth);
    	try {
    		return Optional.ofNullable(tq.getSingleResult());
    	} catch (NoResultException e) {
    		return Optional.empty();
    	}
	}
	@Override
	public ResultList<Transaction> searchTransactionsByOrFilter(RequestContextImpl rc, String walletId, List<String> endUserWalletIds,
		String globalFilter, String organizationId, String orderBy, String orderSorting, Integer offset, Integer maxRows) {

		StringBuffer sb = new StringBuffer(" from Transaction t JOIN FETCH t.wallet w where 1=1");
		StringBuffer countSb = new StringBuffer(" select count(t.transactionId) from Transaction t JOIN FETCH t.wallet w where 1=1");
		
		sb.append(" and t.status = :status");
		countSb.append(" and t.status = :status");
		
		//search by walletId ???(ui is search by walletId now)	
		if (!Strings.isNullOrEmpty(walletId)) {
			sb.append(" and t.walletId = :walletId");
			countSb.append(" and t.walletId = :walletId");
		}
	
		if(!Strings.isNullOrEmpty(globalFilter)){
			sb.append(" and (");
			countSb.append(" and (");

			sb.append(" t.transactionId like :transactionId");
			countSb.append(" t.transactionId like :transactionId");
				
			sb.append(" or t.serviceOrder like :serviceOrder");
			countSb.append(" or t.serviceOrder like :serviceOrder");
		
			sb.append(" or t.serviceId like :serviceId");
			countSb.append(" or t.serviceId like :serviceId");
		
			sb.append(" or t.item like :item");
			countSb.append(" or t.item like :item");
		
			if(isInteger(globalFilter)){
				sb.append(" or t.amount like :amount");
				countSb.append(" or t.amount like :amount");
				
				sb.append(" or t.balance like :balance");
				countSb.append(" or t.balance like :balance");
			}
		
			sb.append(" or t.action like :action");
			countSb.append(" or t.action like :action");
		
			sb.append(" or t.itemType like :itemType");
			countSb.append(" or t.itemType like :itemType");
		
			sb.append(" or t.description like :description");
			countSb.append(" or t.description like :description");
			
			sb.append(" or t.paymentId like :paymentId");
			countSb.append(" or t.paymentId like :paymentId");
			
	//		sb.append(" and t.chargeDate like :chargeDate");
	//		countSb.append(" and t.chargeDate like :chargeDate");
		
			sb.append(" or t.userId like :userId");
			countSb.append(" or t.userId like :userId");
			
			sb.append(" )");
			countSb.append(" )");
		}
		if (orderBy != null) {
			sb.append(" order by t." + orderBy);
			
			if (orderSorting != null) {
				sb.append(" " + orderSorting);
			}
		} else {
			sb.append(" order by t.uid");
			if (orderSorting == null) {
				sb.append(" asc");
			}
		}
		
		String sql = sb.toString();
		String countSql = countSb.toString();
		
		TypedQuery<Transaction> tq = em.createQuery(sql,Transaction.class);
		TypedQuery<Long> cq = em.createQuery(countSql,Long.class);
		
		tq.setParameter("status", TransactionStatus.SUCCESS);
		cq.setParameter("status", TransactionStatus.SUCCESS);

		if (!Strings.isNullOrEmpty(walletId)) {
			tq.setParameter("walletId", walletId);
			cq.setParameter("walletId", walletId);
		}

		if(!Strings.isNullOrEmpty(globalFilter)){
			tq.setParameter("transactionId", StringUtils.enclosePercentWildcard(globalFilter));
			cq.setParameter("transactionId", StringUtils.enclosePercentWildcard(globalFilter));
		
		
			tq.setParameter("serviceOrder", StringUtils.enclosePercentWildcard(globalFilter));
			cq.setParameter("serviceOrder", StringUtils.enclosePercentWildcard(globalFilter));
		
			tq.setParameter("serviceId", StringUtils.enclosePercentWildcard(globalFilter));
			cq.setParameter("serviceId", StringUtils.enclosePercentWildcard(globalFilter));
		
			tq.setParameter("item", StringUtils.enclosePercentWildcard(globalFilter));
			cq.setParameter("item", StringUtils.enclosePercentWildcard(globalFilter));
		
			if (isInteger(globalFilter)) {
				tq.setParameter("amount", Integer.parseInt(globalFilter));
				cq.setParameter("amount", Integer.parseInt(globalFilter));
	
				tq.setParameter("balance", Integer.parseInt(globalFilter));
				cq.setParameter("balance", Integer.parseInt(globalFilter));
			}
			
			tq.setParameter("action", StringUtils.enclosePercentWildcard(globalFilter));
			cq.setParameter("action", StringUtils.enclosePercentWildcard(globalFilter));
		
			tq.setParameter("itemType", StringUtils.enclosePercentWildcard(globalFilter));
			cq.setParameter("itemType", StringUtils.enclosePercentWildcard(globalFilter));
		
			tq.setParameter("description", StringUtils.enclosePercentWildcard(globalFilter));
			cq.setParameter("description", StringUtils.enclosePercentWildcard(globalFilter));
			
			tq.setParameter("paymentId", StringUtils.enclosePercentWildcard(globalFilter));
			cq.setParameter("paymentId", StringUtils.enclosePercentWildcard(globalFilter));
			
			tq.setParameter("userId", StringUtils.enclosePercentWildcard(globalFilter));
			cq.setParameter("userId", StringUtils.enclosePercentWildcard(globalFilter));
		}
		if (offset !=null && offset > 0) {
    		tq.setFirstResult(offset);
    	}
		
    	if (maxRows !=null && maxRows > 0) {
    		tq.setMaxResults(maxRows);
    	}
    	
    	List<Transaction> result = tq.getResultList();
		Long totalCount = cq.getSingleResult();
		
		return new ResultList<Transaction>(result, totalCount.intValue());
	}
	
	private boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}

	@Override
	public Optional<Transaction> findTransactionByParentTransactionId(String parentTransactionId) {
		PortalExceptionUtils.throwIfNull(parentTransactionId, PortalErrorCode.INVALID_TRANSACTION_ID);
		String query = "from Transaction t where t.parentTransactionId = :parentTransactionId";
		TypedQuery<Transaction> tq = em.createQuery(query, Transaction.class);
		tq.setParameter("parentTransactionId", parentTransactionId);
		return getSingleResult(tq);
	}

	@Override
	public List<Transaction> findPendingTransactionsByWalletId(String walletId) {
		PortalExceptionUtils.throwIfNull(walletId, PortalErrorCode.INVALID_WALLET_ID);
		String query = "from Transaction t where t.walletId = :walletId and t.state = :state and t.status = :status";
		TypedQuery<Transaction> tq = em.createQuery(query, Transaction.class);
		tq.setParameter("walletId", walletId);
		tq.setParameter("state", TransactionState.COMMITE);
		tq.setParameter("status", TransactionStatus.PENDING);
		return tq.getResultList();
	}

	@Override
	public Optional<Transaction> findCommitePendingTransactionByTransactionId(String paymentId) {
		String query = "from Transaction t where t.paymentId = :paymentId and t.state = :state and t.status = :status";
		TypedQuery<Transaction> tq = em.createQuery(query,Transaction.class);
		tq.setParameter("paymentId", paymentId);
		tq.setParameter("state", TransactionState.COMMITE);
		tq.setParameter("status", TransactionStatus.PENDING);
		return getSingleResult(tq);
	}

}
