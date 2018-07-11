package com.kooppi.nttca.wallet.rest.mapChargeItemToContract.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.kooppi.nttca.wallet.common.persistence.domain.MapChargeItemToContract;
import com.kooppi.nttca.wallet.persistence.producer.WalletDataSource;

@ApplicationScoped
public class MapChargeItemToContractRepositoryImpl implements MapChargeItemToContractRepository {

	private EntityManager em;
	
	public MapChargeItemToContractRepositoryImpl() {}
	
	@Inject
	public MapChargeItemToContractRepositoryImpl(@WalletDataSource EntityManager em) {
		this.em = em;
	}

	@Override
	public List<MapChargeItemToContract> findMapChargeItemToContractByContractId(String contractId) {
		StringBuffer sb = new StringBuffer(" from MapChargeItemToContract map where map.contractId =:contractId");
		String sql = sb.toString();
		TypedQuery<MapChargeItemToContract> tq = em.createQuery(sql, MapChargeItemToContract.class);
		tq.setParameter("contractId", contractId);
		return tq.getResultList();
	}

	@Override
	public void remove(MapChargeItemToContract mapChargeItemToContract) {
//		synchronized (em) {
//			if (!em.contains(mapChargeItemToContract))
//				mapChargeItemToContract = em.merge(mapChargeItemToContract);
			em.remove(mapChargeItemToContract);
//		}
	}
}
