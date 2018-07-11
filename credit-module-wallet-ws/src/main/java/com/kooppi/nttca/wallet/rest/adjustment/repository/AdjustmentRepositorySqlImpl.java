package com.kooppi.nttca.wallet.rest.adjustment.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.base.Strings;
import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.common.filter.context.RequestContextImpl;
import com.kooppi.nttca.portal.common.repository.BasicRepository;
import com.kooppi.nttca.portal.common.utils.StringUtils;
import com.kooppi.nttca.portal.wallet.domain.AdjustmentStatus;
import com.kooppi.nttca.portal.wallet.domain.AdjustmentType;
import com.kooppi.nttca.wallet.common.persistence.domain.Adjustment;
import com.kooppi.nttca.wallet.persistence.producer.WalletDataSource;

@ApplicationScoped
public class AdjustmentRepositorySqlImpl extends BasicRepository implements AdjustmentRepository{

	private EntityManager em;
	
	AdjustmentRepositorySqlImpl() {}
	
	@Inject
	public AdjustmentRepositorySqlImpl(@WalletDataSource EntityManager em) {
		this.em = em;
	}
	
	@Override
	public Adjustment saveAndRefresh(Adjustment adjustment) {
		em.persist(adjustment);
		em.flush();
		em.refresh(adjustment);
		adjustment.updateContractId();
		return adjustment;
	}
	
    @Override
    public void updateAdjustment(Adjustment adjustment) {
        em.merge(adjustment);
    }

	@Override
	public Optional<Adjustment> findByTransactionId(String transactionId) {
		String query = "from Adjustment t where t.transactionId = :transactionId";
		TypedQuery<Adjustment> tq = em.createQuery(query, Adjustment.class);
		tq.setParameter("transactionId", transactionId);
		return getSingleResult(tq);
	}
	
	@Override
	public ResultList<Adjustment> findChildAdjustmentByParentTransactionId(String parentTransactionId) {
		String query = "from Adjustment t where t.parentTransactionId = :parentTransactionId";
		TypedQuery<Adjustment> tq = em.createQuery(query, Adjustment.class);
		tq.setParameter("parentTransactionId", parentTransactionId);
		
		List<Adjustment> result = tq.getResultList();
		return new ResultList<Adjustment>(result, result.size());
	}

	@Override
	public ResultList<Adjustment> searchAdjustment(RequestContextImpl rc, String walletId, String organizationId, String transactionId, String parentTransactionId, String refNumber, AdjustmentType adjustmentType, LocalDateTime minTransactionDate, LocalDateTime maxTransactionDate, Integer minAmount, Integer maxAmount, AdjustmentStatus status, String orderBy, String orderSorting, Integer offset, Integer maxRows, Boolean isChild) {
		
		StringBuffer sb = new StringBuffer(" from Adjustment a JOIN FETCH a.wallet w where 1=1");
		StringBuffer countSb = new StringBuffer(" select count(a.uid) from Adjustment a JOIN FETCH a.wallet w where 1=1");
		
		if (!Strings.isNullOrEmpty(walletId)) {
			sb.append(" and a.walletId = :walletId");
			countSb.append(" and a.walletId = :walletId");
		}

				
		if(!Strings.isNullOrEmpty(transactionId)){
			sb.append(" and a.transactionId like :transactionId");
			countSb.append(" and a.transactionId like :transactionId");
		}

		if(!Strings.isNullOrEmpty(parentTransactionId)) {
			sb.append(" and a.parentTransactionId like :parentTransactionId");
			countSb.append(" and a.parentTransactionId like :parentTransactionId");
		}
			
		if(!Strings.isNullOrEmpty(refNumber)){
			sb.append(" and a.refNumber like :refNumber");
			countSb.append(" and a.refNumber like :refNumber");
		}
			
		if(adjustmentType!=null){
			sb.append(" and a.adjustmentType = :adjustmentType");
			countSb.append(" and a.adjustmentType = :adjustmentType");
		}

		if(minAmount != null ){
			sb.append(" and a.amount >= :minAmount");
			countSb.append(" and a.amount >= :minAmount");
		}
			
		if(maxAmount != null ){
			sb.append(" and a.amount <= :maxAmount");
			countSb.append(" and a.amount <= :maxAmount");
		}
			
		if(status!=null){
			sb.append(" and a.status = :status");
			countSb.append(" and a.status = :status");
		}
		
		if(minTransactionDate != null){
			sb.append(" and a.transactionDate >= :minTransactionDate");
			countSb.append(" and a.transactionDate >= :minTransactionDate");
		}
			
		if(maxTransactionDate != null){
			sb.append(" and a.transactionDate <= :maxTransactionDate");
			countSb.append(" and a.transactionDate <= :maxTransactionDate");
		}
		
		if(isChild!=null) {
			if(isChild) {
				sb.append(" and a.parentTransactionId IS NOT NULL");
				countSb.append(" and a.parentTransactionId IS NOT NULL");
			} else {
				sb.append(" and a.parentTransactionId IS NULL");
				countSb.append(" and a.parentTransactionId IS NULL");
			}
		}
			
		if (orderBy != null) {
			sb.append(" order by a." + orderBy);
			
			if (orderSorting != null) {
				sb.append(" " + orderSorting);
			}
		} else {
			sb.append(" order by a.uid");
			if (orderSorting == null) {
				sb.append(" asc");
			}
		}
			
		String sql = sb.toString();
		String countSql = countSb.toString();
			
		TypedQuery<Adjustment> tq = em.createQuery(sql, Adjustment.class);
		TypedQuery<Long> cq = em.createQuery(countSql, Long.class);

		if (!Strings.isNullOrEmpty(walletId)) {
			tq.setParameter("walletId", walletId);
			cq.setParameter("walletId", walletId);
		}
		if (!Strings.isNullOrEmpty(transactionId)) {
			tq.setParameter("transactionId", StringUtils.enclosePercentWildcard(transactionId));
			cq.setParameter("transactionId", StringUtils.enclosePercentWildcard(transactionId));
		}
		if (!Strings.isNullOrEmpty(parentTransactionId)) {
			tq.setParameter("parentTransactionId", StringUtils.enclosePercentWildcard(parentTransactionId));
			cq.setParameter("parentTransactionId", StringUtils.enclosePercentWildcard(parentTransactionId));
		}
		if (!Strings.isNullOrEmpty(refNumber)) {
			tq.setParameter("refNumber", StringUtils.enclosePercentWildcard(refNumber));
			cq.setParameter("refNumber", StringUtils.enclosePercentWildcard(refNumber));
		}
		if (adjustmentType != null) {
			tq.setParameter("adjustmentType", adjustmentType);
			cq.setParameter("adjustmentType", adjustmentType);
		}
		if (minAmount != null) {
			tq.setParameter("minAmount", minAmount);
			cq.setParameter("minAmount", minAmount);

		}
		if (maxAmount != null) {
			tq.setParameter("maxAmount", maxAmount);
			cq.setParameter("maxAmount", maxAmount);
		}
		if (status != null) {
			tq.setParameter("status", status);
			cq.setParameter("status", status);
		}
		if (minTransactionDate != null) {
			tq.setParameter("minTransactionDate", minTransactionDate);
			cq.setParameter("minTransactionDate", minTransactionDate);
		}
		if (maxTransactionDate != null) {
			tq.setParameter("maxTransactionDate", maxTransactionDate);
			cq.setParameter("maxTransactionDate", maxTransactionDate);
		}
		if (offset != null && offset > 0) {
			tq.setFirstResult(offset);
		}
		if (maxRows != null && maxRows > 0) {
			tq.setMaxResults(maxRows);
		}
	    	
		List<Adjustment> result = tq.getResultList();
		Long totalCount = cq.getSingleResult();

		return new ResultList<Adjustment>(result, totalCount.intValue());
	}

	@Override
	public List<Adjustment> findAllScheduledAdjustmentOnToday() {
		//will select next_recharge_date <= current_date => adjustment which is not adjusted before, will be done late
		String query = "from Adjustment t where 1=1 " +
						" and ((t.adjustmentType in (:serviceOrder,:compensationAdjustment) and t.contractEffectiveDate <= :currentDate and t.status = :pendingStatus)" + 
						" or (t.adjustmentType = :monthlyRecharge and t.contractTerminationDate > :currentDate and t.status in (:pendingStatus, :inUseStatus)))";
		TypedQuery<Adjustment> tq = em.createQuery(query, Adjustment.class);
		
		LocalDate currentDate = LocalDate.now();
		tq.setParameter("currentDate", currentDate);
		tq.setParameter("pendingStatus", AdjustmentStatus.PENDING);
		tq.setParameter("inUseStatus", AdjustmentStatus.IN_USE);
		tq.setParameter("serviceOrder", AdjustmentType.ONE_OFF);
		tq.setParameter("compensationAdjustment", AdjustmentType.COMPENSATION);
		tq.setParameter("monthlyRecharge", AdjustmentType.MONTHLY_RECHARGE);
		return tq.getResultList();
	}
	
	@Override
	public List<Adjustment> findAllPendingAndInUseAdjustment() {
		String query = " from Adjustment t where t.status in (:pendingStatus, :inUseStatus) ";
		
		TypedQuery<Adjustment> tq = em.createQuery(query, Adjustment.class);
		tq.setParameter("pendingStatus", AdjustmentStatus.PENDING);
		tq.setParameter("inUseStatus", AdjustmentStatus.IN_USE);
		return tq.getResultList();
	}

	@Override
	public ResultList<Adjustment> searchAdjustmentsByOrFilter( RequestContextImpl rc, String organizationId, String walletId, AdjustmentStatus adjustmentStatus, Boolean isExpiredCotract, String globalFilter, String orderBy,
			String orderSorting, Integer offset, Integer maxRows, List<String> organizationNames) {
		
		StringBuffer sb = new StringBuffer(" from Adjustment a JOIN FETCH a.wallet w where 1=1");
		StringBuffer countSb = new StringBuffer(" select count(a.uid) from Adjustment a JOIN FETCH a.wallet w where 1=1");
		
		if(walletId!=null){
			sb.append(" and a.walletId = :walletId");
			countSb.append(" and a.walletId = :walletId");
		}
		
		if(adjustmentStatus!=null){
			sb.append(" and a.status = :status");
			countSb.append(" and a.status = :status");
		}

		if(isExpiredCotract!=null){
			sb.append(" and a.creditExpiryDate <= :creditExpiryDate");
			countSb.append(" and a.creditExpiryDate <= :creditExpiryDate");
		}
		
		if(!Strings.isNullOrEmpty(globalFilter)){
			sb.append(" and (");
			countSb.append(" and (");

			sb.append(" a.refNumber like :refNumber");
			countSb.append(" a.refNumber like :refNumber");
			
			sb.append(" or function('date_format', a.creditExpiryDate, '%Y-%m-%d %h:%m:%s') like :creditExpiryDateOr");
			countSb.append(" or function('date_format', a.creditExpiryDate, '%Y-%m-%d %h:%m:%s') like :creditExpiryDateOr");
			
			sb.append(" or a.wallet.organizationId like :organizationId");
			countSb.append(" or a.wallet.organizationId like :organizationId");
			
			if (CollectionUtils.isNotEmpty(organizationNames)) {
				sb.append(" or a.wallet.organizationId in :organizationIds");
				countSb.append(" or a.wallet.organizationId in :organizationIds");
			}
			
			sb.append(" )");
			countSb.append(" )");
		}
			
		if (orderBy != null) {
			sb.append(" order by a." + orderBy);
			
			if (orderSorting != null) {
				sb.append(" " + orderSorting);
			}
		} else {
			sb.append(" order by w.organizationName asc, a.refNumber asc, a.contractEffectiveDate asc");
//			if (orderSorting == null) {
//				sb.append(" asc");
//			}
		}
			
		String sql = sb.toString();
		String countSql = countSb.toString();
			
		TypedQuery<Adjustment> tq = em.createQuery(sql, Adjustment.class);
		TypedQuery<Long> cq = em.createQuery(countSql, Long.class);
		
		if(walletId!=null){
			tq.setParameter("walletId", walletId);
			cq.setParameter("walletId", walletId);
		}
		if(adjustmentStatus!=null){
			tq.setParameter("status", adjustmentStatus);
			cq.setParameter("status", adjustmentStatus);
		}
		
		if(isExpiredCotract!=null){
			tq.setParameter("creditExpiryDate", LocalDate.now());
			cq.setParameter("creditExpiryDate", LocalDate.now());
		}
		
		if(!Strings.isNullOrEmpty(globalFilter)){

			tq.setParameter("refNumber", StringUtils.enclosePercentWildcard(globalFilter));
			cq.setParameter("refNumber", StringUtils.enclosePercentWildcard(globalFilter));
			
			tq.setParameter("creditExpiryDateOr", StringUtils.enclosePercentWildcard(globalFilter));
			cq.setParameter("creditExpiryDateOr", StringUtils.enclosePercentWildcard(globalFilter));
			
			tq.setParameter("organizationId", StringUtils.enclosePercentWildcard(globalFilter));
			cq.setParameter("organizationId", StringUtils.enclosePercentWildcard(globalFilter));

			if (CollectionUtils.isNotEmpty(organizationNames)) {
				tq.setParameter("organizationIds", organizationNames);
				cq.setParameter("organizationIds", organizationNames);
			}
		}
		if (offset != null && offset > 0) {
			tq.setFirstResult(offset);
		}
		if (maxRows != null && maxRows > 0) {
			tq.setMaxResults(maxRows);
		}
	    	
		List<Adjustment> result = tq.getResultList();
		Long totalCount = cq.getSingleResult();

		return new ResultList<Adjustment>(result, totalCount.intValue());
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
	public void deleteAdjustment(Adjustment adjustment) {
		em.remove(adjustment);
	}

	@Override
	public Optional<Adjustment> findByContractId(String contractId) {
		String query = "from Adjustment t where t.contractId = :contractId";
		TypedQuery<Adjustment> tq = em.createQuery(query, Adjustment.class);
		tq.setParameter("contractId", contractId);
		return getSingleResult(tq);
	}

}
