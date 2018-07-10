package com.kooppi.nttca.wallet.rest.chargeItem.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.kooppi.nttca.ce.domain.ChargingItem;
import com.kooppi.nttca.wallet.rest.chargeItem.repository.ChargeItemRepository;

@ApplicationScoped
public class ChargeItemServiceImpl implements ChargeItemService {

	@Inject
	private ChargeItemRepository chargeItemRepository;

	@Override
	public ChargingItem createChargingItem(ChargingItem chargingItem) {
		return chargeItemRepository.saveAndRefresh(chargingItem);
	}

	@Override
	public void deleteChargeItem(ChargingItem chargingItem) {
		chargeItemRepository.deleteChargeItem(chargingItem);
	}
	

}
