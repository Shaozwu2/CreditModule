package com.kooppi.nttca.wallet.rest.reservation.repository;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.google.common.base.Strings;
import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.common.filter.context.RequestContextImpl;
import com.kooppi.nttca.portal.common.repository.BasicRepository;
import com.kooppi.nttca.portal.wallet.domain.TransactionReservationStatus;
import com.kooppi.nttca.wallet.common.persistence.domain.TransactionReservation;
import com.kooppi.nttca.wallet.persistence.producer.WalletDataSource;

@ApplicationScoped
public class TransactionReservationRepositorySqlImpl extends BasicRepository implements TransactionReservationRepository{
	
	private EntityManager em;
	
	TransactionReservationRepositorySqlImpl() {}
	
	@Inject
	public TransactionReservationRepositorySqlImpl(@WalletDataSource EntityManager em) {
		this.em = em;
	}
	
	@Override
	public TransactionReservation add(TransactionReservation cm) {
		em.persist(cm);
		em.flush();
		em.refresh(cm);
		cm.updateTransactionId();
		return cm;
	}
	
	@Override
	public Optional<TransactionReservation> findTransactionReservationByTransactionId(String transactionId) {
		String query = "from TransactionReservation t where t.transactionId = :transactionId";
		TypedQuery<TransactionReservation> tq = em.createQuery(query,TransactionReservation.class);
		tq.setParameter("transactionId", transactionId);
		return getSingleResult(tq);
	}

	@Override
	public List<TransactionReservation> findAllReservedTransactionReservations() {
		String query = "from TransactionReservation u where u.status = :reserved";
		TypedQuery<TransactionReservation> tq = em.createQuery(query,TransactionReservation.class);
		tq.setParameter("reserved", TransactionReservationStatus.RESERVED);
		return tq.getResultList();
	}

	@Override
	public ResultList<TransactionReservation> findTransactionReservations(RequestContextImpl rc, String walletId,
			String orderBy, String orderSorting, Integer offset, Integer maxRows) {
		StringBuffer sb = new StringBuffer(" from TransactionReservation tr where 1=1 ");
		StringBuffer countSb = new StringBuffer(" select count(tr.uid) from TransactionReservation tr where 1=1 ");
		
		if (!Strings.isNullOrEmpty(walletId)) {
			sb.append(" and tr.walletId = :walletId");
			countSb.append(" and tr.walletId = :walletId");
		}
		
		if (orderBy != null) {
			sb.append(" order by tr." + orderBy);
			
			if (orderSorting != null) {
				sb.append(" " + orderSorting);
			}
		} else {
			sb.append(" order by tr.uid");
			if (orderSorting == null) {
				sb.append(" asc");
			}
		}
		
		String sql = sb.toString();
		String countSql = countSb.toString();
		
		TypedQuery<TransactionReservation> tq = em.createQuery(sql, TransactionReservation.class);
		TypedQuery<Long> cq = em.createQuery(countSql, Long.class);
		
		if (!Strings.isNullOrEmpty(walletId)) {
			tq.setParameter("walletId", walletId);
			cq.setParameter("walletId", walletId);
		}
		if (offset != null && offset > 0) {
			tq.setFirstResult(offset);
		}
		if (maxRows != null && maxRows > 0) {
			tq.setMaxResults(maxRows);
		}
		
		List<TransactionReservation> result = tq.getResultList();
		Long totalCount = cq.getSingleResult();
		
		return new ResultList<TransactionReservation>(result, totalCount.intValue());
	}
}
