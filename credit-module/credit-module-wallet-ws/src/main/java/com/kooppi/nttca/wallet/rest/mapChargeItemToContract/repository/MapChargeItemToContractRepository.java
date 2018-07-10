package com.kooppi.nttca.wallet.rest.mapChargeItemToContract.repository;

import java.util.List;

import com.kooppi.nttca.wallet.common.persistence.domain.MapChargeItemToContract;

public interface MapChargeItemToContractRepository {
	public List<MapChargeItemToContract> findMapChargeItemToContractByContractId(String contractId);
	public void remove(MapChargeItemToContract mapChargeItemToContract);
}
