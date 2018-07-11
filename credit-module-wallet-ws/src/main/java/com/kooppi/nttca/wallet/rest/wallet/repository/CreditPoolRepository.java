package com.kooppi.nttca.wallet.rest.wallet.repository;

import java.util.Optional;

import com.kooppi.nttca.wallet.common.persistence.domain.CreditPool;

public interface CreditPoolRepository {
	
	public Optional<CreditPool> findCreditPoolByWalletId(String walletId);
}
