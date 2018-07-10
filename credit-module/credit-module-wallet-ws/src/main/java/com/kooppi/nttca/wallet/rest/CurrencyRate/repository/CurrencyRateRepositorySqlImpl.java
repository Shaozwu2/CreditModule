package com.kooppi.nttca.wallet.rest.CurrencyRate.repository;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.common.repository.BasicRepository;
import com.kooppi.nttca.wallet.common.persistence.domain.CurrencyRate;
import com.kooppi.nttca.wallet.persistence.producer.WalletDataSource;

@ApplicationScoped
public class CurrencyRateRepositorySqlImpl extends BasicRepository implements CurrencyRateRepository {

	private EntityManager em;
	
	@Inject
	public CurrencyRateRepositorySqlImpl(@WalletDataSource EntityManager em) {
		this.em = em;
	}
	
	@Override
	public Optional<CurrencyRate> findCurrencyByCurrencyCode(String currencyCode) {
		String query = " from CurrencyRate cr where cr.fromCurrencyCode = :currencyCode ";
		TypedQuery<CurrencyRate> tq = em.createQuery(query, CurrencyRate.class);
		tq.setParameter("currencyCode", currencyCode);
		return getSingleResult(tq);
	}

	@Override
	public ResultList<CurrencyRate> getAllCurrencyRates() {
		StringBuffer sb = new StringBuffer(" from CurrencyRate cr order by cr.fromCurrencyCode ");
		StringBuffer countSb = new StringBuffer(" select count(cr.currencyRateId) from CurrencyRate cr");
		
		String sql = sb.toString();
		String countSql = countSb.toString();
		
		TypedQuery<CurrencyRate> tq = em.createQuery(sql, CurrencyRate.class);
		TypedQuery<Long> cq = em.createQuery(countSql, Long.class);
		
		List<CurrencyRate> result = tq.getResultList();
		Long totalCount = cq.getSingleResult();
		return new ResultList<>(result, totalCount.intValue());
	}

}
