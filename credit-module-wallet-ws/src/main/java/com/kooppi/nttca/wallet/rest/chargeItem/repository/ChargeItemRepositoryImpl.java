package com.kooppi.nttca.wallet.rest.chargeItem.repository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.kooppi.nttca.ce.domain.ChargingItem;
import com.kooppi.nttca.wallet.persistence.producer.WalletDataSource;

@ApplicationScoped
public class ChargeItemRepositoryImpl implements ChargeItemRepository {

	private EntityManager em;
	
	public ChargeItemRepositoryImpl() {}
	
	@Inject
	public ChargeItemRepositoryImpl(@WalletDataSource EntityManager em) {
		this.em = em;
	}

	@Override
	public ChargingItem saveAndRefresh(ChargingItem chargingItem) {
		em.persist(chargingItem);
		em.flush();
		em.refresh(chargingItem);
		return chargingItem;
	}

	@Override
	public void deleteChargeItem(ChargingItem chargingItem) {
		em.remove(chargingItem);
	}

}
