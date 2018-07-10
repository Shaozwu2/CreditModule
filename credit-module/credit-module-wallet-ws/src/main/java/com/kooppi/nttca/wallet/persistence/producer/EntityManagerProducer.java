package com.kooppi.nttca.wallet.persistence.producer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class EntityManagerProducer {

	@PersistenceContext(unitName = "credit_module")
	private EntityManager walletEntityManager;
	
	@Produces
	@RequestScoped
	@WalletDataSource
	public EntityManager getWalletEntityManager(){
		return walletEntityManager;
	}

}
