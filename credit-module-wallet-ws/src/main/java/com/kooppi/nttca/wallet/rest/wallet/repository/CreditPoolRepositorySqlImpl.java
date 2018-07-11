package com.kooppi.nttca.wallet.rest.wallet.repository;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.kooppi.nttca.portal.common.repository.BasicRepository;
import com.kooppi.nttca.wallet.common.persistence.domain.CreditPool;
import com.kooppi.nttca.wallet.persistence.producer.WalletDataSource;

@ApplicationScoped
public class CreditPoolRepositorySqlImpl extends BasicRepository implements CreditPoolRepository{

	private EntityManager em;
	
	CreditPoolRepositorySqlImpl(){}
	
	@Inject
	public CreditPoolRepositorySqlImpl(@WalletDataSource EntityManager em) {
		this.em = em;
	}
	
	@Override
	public Optional<CreditPool> findCreditPoolByWalletId(String walletId) {
		String query = "from CreditPool w where w.walletId = :walletId";
		TypedQuery<CreditPool> tq = em.createQuery(query,CreditPool.class);
		tq.setParameter("walletId", walletId);
		return getSingleResult(tq);
	}

}
