package com.kooppi.nttca.wallet.rest.CurrencyRate.service;

import java.util.Optional;

import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.wallet.common.persistence.domain.CurrencyRate;

public interface CurrencyRateService {

	public ResultList<CurrencyRate> getAllCurrencyRates();
	
	public Optional<CurrencyRate> findCurrencyByCurrencyCode(String currencyCode);
}
