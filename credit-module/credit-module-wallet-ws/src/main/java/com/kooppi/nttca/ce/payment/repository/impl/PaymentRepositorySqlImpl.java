package com.kooppi.nttca.ce.payment.repository.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.EnumUtils;

import com.google.common.base.Strings;
import com.kooppi.nttca.ce.domain.Payment;
import com.kooppi.nttca.ce.payment.repository.PaymentRepository;
import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.ce.domain.PaymentStatus;
import com.kooppi.nttca.portal.common.repository.BasicRepository;
import com.kooppi.nttca.portal.common.utils.StringUtils;
import com.kooppi.nttca.wallet.persistence.producer.WalletDataSource;

@ApplicationScoped
public class PaymentRepositorySqlImpl extends BasicRepository implements PaymentRepository {

	private EntityManager em;
	
	PaymentRepositorySqlImpl(){}
	
	@Inject
	private PaymentRepositorySqlImpl(@WalletDataSource EntityManager em) {
		this.em = em;
	}
	
	@Override
	public Optional<Payment> findByUid(Long uid) {
		Optional<Payment> paymentOpt = Optional.ofNullable(em.find(Payment.class, uid));
		return paymentOpt;
	}

	@Override
	public Optional<Payment> findByPaymentId(String paymentId) {
		String query = "Select p from Payment p where p.paymentId = :paymentId";
		TypedQuery<Payment> tq = em.createQuery(query,Payment.class);
		tq.setParameter("paymentId", paymentId);
		return getSingleResult(tq);
	}

	@Override
	public Payment saveAndRefresh(Payment payment) {
		em.persist(payment);
		em.flush();
		em.refresh(payment);
		payment.updatePaymentId();
		return payment;
	}

	@Override
	public ResultList<Payment> searchByOrFilter(String globalFilter, String orderBy, String orderSorting,
			Integer offset, Integer maxRows, List<String> organizationNames) {
		StringBuffer sb = new StringBuffer(" select p from Payment p where 1=1 ");
		StringBuffer countSb = new StringBuffer(" select count(p.uid) from Payment p where 1=1 ");
		
		if (!Strings.isNullOrEmpty(globalFilter)) {
			sb.append(" and (");
			countSb.append(" and (");

			sb.append(" p.paymentId like :paymentId");
			countSb.append(" p.paymentId like :paymentId");
			
			if (EnumUtils.isValidEnum(PaymentStatus.class, globalFilter)) {
				sb.append(" or p.currentStatus.status = :status");
				countSb.append(" or p.currentStatus.status = :status");
			}
			
			sb.append(" or function('date_format', p.createdDate, '%Y-%m-%d %h:%m:%s') like :createdDate");
			countSb.append(" or function('date_format', p.createdDate, '%Y-%m-%d %h:%m:%s') like :createdDate");
			
			if (CollectionUtils.isNotEmpty(organizationNames)) {
				sb.append(" or p.organizationId in :organizationId");
				countSb.append(" or p.organizationId in :organizationId");
			}
			
			sb.append(" )");
			countSb.append(" )");
		}
		
		if (orderBy != null) {
			sb.append(" order by p." + orderBy);
			
			if (orderSorting != null) {
				sb.append(" " + orderSorting);
			}
		} else {
			sb.append(" order by p.uid");
			if (orderSorting == null) {
				sb.append(" asc");
			}
		}
		
		String sql = sb.toString();
		String countSql = countSb.toString();
		System.out.println("SQL: " + sql);
		
		TypedQuery<Payment> tq = em.createQuery(sql, Payment.class);
		TypedQuery<Long> cq = em.createQuery(countSql, Long.class);
		
		if (!Strings.isNullOrEmpty(globalFilter)) {
			
			tq.setParameter("paymentId", StringUtils.enclosePercentWildcard(globalFilter));
			cq.setParameter("paymentId", StringUtils.enclosePercentWildcard(globalFilter));
			
			if (EnumUtils.isValidEnum(PaymentStatus.class, globalFilter)) {
				tq.setParameter("status", PaymentStatus.valueOf(globalFilter));
				cq.setParameter("status", PaymentStatus.valueOf(globalFilter));
			}
			
			if (CollectionUtils.isNotEmpty(organizationNames)) {
				tq.setParameter("organizationId", organizationNames);
				cq.setParameter("organizationId", organizationNames);
			}
			
			tq.setParameter("createdDate", StringUtils.enclosePercentWildcard(globalFilter));
			cq.setParameter("createdDate", StringUtils.enclosePercentWildcard(globalFilter));
		}
		
		if (offset != null && offset > 0) {
			tq.setFirstResult(offset);
		}
		if (maxRows != null && maxRows > 0) {
			tq.setMaxResults(maxRows);
		}
		
		List<Payment> result = tq.getResultList();
		Long totalCount = cq.getSingleResult();
		return new ResultList<Payment>(result, totalCount.intValue());
	}

	@Override
	public ResultList<Payment> searchByAndFilter(String organizationId, String paymentId, LocalDate transactionDate, String orderBy, String orderSorting, Integer offset, Integer maxRows) {
		StringBuffer sb = new StringBuffer(" from Payment p where 1=1 ");
		StringBuffer countSb = new StringBuffer(" select count(p.uid) from Payment p where 1=1 ");
		
		if (!Strings.isNullOrEmpty(organizationId)) {
			sb.append(" and p.organizationId = :organizationId ");
			countSb.append(" and p.organizationId = :organizationId ");
		}
		
		if (!Strings.isNullOrEmpty(paymentId)) {
			sb.append(" and p.paymentId like :paymentId ");
			countSb.append(" and p.paymentId like :paymentId ");
		}
		
		if (transactionDate != null) {
			sb.append(" and p.createdDate >= :startDate and p.createdDate <= :endDate ");
			countSb.append(" and p.createdDate >= :startDate and p.createdDate <= :endDate ");
		}
		
		if (orderBy != null) {
			sb.append(" order by p." + orderBy);
			
			if (orderSorting != null) {
				sb.append(" " + orderSorting);
			}
		} else {
			sb.append(" order by p.uid");
			if (orderSorting == null) {
				sb.append(" asc");
			}
		}
		
		String sql = sb.toString();
		String countSql = countSb.toString();
		
		TypedQuery<Payment> tq = em.createQuery(sql, Payment.class);
		TypedQuery<Long> cq = em.createQuery(countSql, Long.class);
		
		if (!Strings.isNullOrEmpty(organizationId)) {
			tq.setParameter("organizationId", organizationId);
			cq.setParameter("organizationId", organizationId);
		}
		
		if (!Strings.isNullOrEmpty(paymentId)) {
			tq.setParameter("paymentId", StringUtils.enclosePercentWildcard(paymentId));
			cq.setParameter("paymentId", StringUtils.enclosePercentWildcard(paymentId));
		}
		
		if (transactionDate != null) {
			tq.setParameter("startDate", LocalDateTime.of(transactionDate.getYear(), transactionDate.getMonthValue(), transactionDate.getDayOfMonth(), 0, 0, 0));
			cq.setParameter("startDate", LocalDateTime.of(transactionDate.getYear(), transactionDate.getMonthValue(), transactionDate.getDayOfMonth(), 0, 0, 0));
			
			tq.setParameter("endDate", LocalDateTime.of(transactionDate.getYear(), transactionDate.getMonthValue(), transactionDate.getDayOfMonth(), 23, 59, 59));
			cq.setParameter("endDate", LocalDateTime.of(transactionDate.getYear(), transactionDate.getMonthValue(), transactionDate.getDayOfMonth(), 23, 59, 59));
		}
		
		if (offset != null && offset > 0) {
			tq.setFirstResult(offset);
		}
		if (maxRows != null && maxRows > 0) {
			tq.setMaxResults(maxRows);
		}
		
		List<Payment> result = tq.getResultList();
		Long totalCount = cq.getSingleResult();
		return new ResultList<Payment>(result, totalCount.intValue());
	}
	
	@Override
	public Object[] callCreateRealTimePaymentStoredProcedure(String walletId, String partNoList, String chargeTypeList, String unitPriceList, String quantityList, String description, String userId, String requestId) throws Exception {
		StoredProcedureQuery query = em.createStoredProcedureQuery("createRealTimePayment")
				.registerStoredProcedureParameter("walletId", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("partNoList", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("chargeTypeList", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("unitPriceList", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("quantityList", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("description", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("userId", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("requestId", String.class, ParameterMode.IN)
				.setParameter("walletId", walletId)
				.setParameter("partNoList", partNoList)
				.setParameter("unitPriceList", unitPriceList)
				.setParameter("chargeTypeList", chargeTypeList)
				.setParameter("quantityList", quantityList)
				.setParameter("description", description)
				.setParameter("userId", userId)
				.setParameter("requestId", requestId);

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = query.getResultList();
		
		// Object[] = payment attributes + wallet attributes, in table column order
		return resultList.get(0);
	}

	public static void main(String[] args) {
		LocalDate now = LocalDate.now();
		System.out.println(LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 0, 0, 0));
		System.out.println(LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 23, 59, 59));
	}

}
