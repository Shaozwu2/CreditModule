package com.kooppi.nttca.wallet.rest.chargeItem.service;

import com.kooppi.nttca.ce.domain.ChargingItem;

public interface ChargeItemService {
	public ChargingItem createChargingItem(ChargingItem chargingItem);

	public void deleteChargeItem(ChargingItem chargingItem);

}
