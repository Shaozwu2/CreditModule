package com.kooppi.nttca.wallet.rest.CurrencyRate.repository;

import java.util.Optional;

import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.wallet.common.persistence.domain.CurrencyRate;

public interface CurrencyRateRepository {

	public ResultList<CurrencyRate> getAllCurrencyRates();
	
	public Optional<CurrencyRate> findCurrencyByCurrencyCode(String currencyCode);
}
