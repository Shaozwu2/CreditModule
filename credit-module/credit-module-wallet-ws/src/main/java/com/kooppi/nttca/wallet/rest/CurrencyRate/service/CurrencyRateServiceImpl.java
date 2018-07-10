package com.kooppi.nttca.wallet.rest.CurrencyRate.service;

import java.util.Optional;

import javax.inject.Inject;

import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.wallet.common.persistence.domain.CurrencyRate;
import com.kooppi.nttca.wallet.rest.CurrencyRate.repository.CurrencyRateRepository;

public class CurrencyRateServiceImpl implements CurrencyRateService {

	@Inject
	private CurrencyRateRepository currencyRateRepository;

	@Override
	public Optional<CurrencyRate> findCurrencyByCurrencyCode(String currencyCode) {
		return currencyRateRepository.findCurrencyByCurrencyCode(currencyCode);
	}

	@Override
	public ResultList<CurrencyRate> getAllCurrencyRates() {
		return currencyRateRepository.getAllCurrencyRates();
	}
}
