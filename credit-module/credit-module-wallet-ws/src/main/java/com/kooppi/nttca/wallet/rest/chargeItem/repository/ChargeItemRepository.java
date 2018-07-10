package com.kooppi.nttca.wallet.rest.chargeItem.repository;

import com.kooppi.nttca.ce.domain.ChargingItem;

public interface ChargeItemRepository {
	public ChargingItem saveAndRefresh(ChargingItem chargingItem);
	public void deleteChargeItem(ChargingItem chargingItem);

}
