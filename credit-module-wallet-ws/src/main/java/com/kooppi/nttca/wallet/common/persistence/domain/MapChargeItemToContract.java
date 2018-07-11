package com.kooppi.nttca.wallet.common.persistence.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.kooppi.nttca.ce.domain.ChargingItem;
import com.kooppi.nttca.portal.common.domain.BasicEntity;

import jersey.repackaged.com.google.common.collect.Lists;

@Entity
@Table(name = "map_charge_item_to_contract")
public class MapChargeItemToContract extends BasicEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6898771618691590209L;
	
	@Column(name = "charge_item_id")
	private String chargeItemId;
	
	@Column(name = "contract_id")
	private String contractId;
	
	@Column(name = "amount")
	private Integer amount;

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY, orphanRemoval=true)
	@JoinColumn(name = "charge_item_id",referencedColumnName = "charge_item_id")
	private List<ChargingItem> chargeItems = Lists.newArrayList();
	
	public static MapChargeItemToContract create(String contractId,String chargeItemId, Integer amount){
		MapChargeItemToContract map = new MapChargeItemToContract();
		map.amount = amount;
		map.contractId = contractId;
		map.chargeItemId = chargeItemId;
		return map;
	}
	
	public void updateContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getChargeItemId() {
		return chargeItemId;
	}

	public String getContractId() {
		return contractId;
	}

	public Integer getAmount() {
		return amount;
	}

	public List<ChargingItem> getChargeItems() {
		return chargeItems;
	}

}
