package com.kooppi.nttca.wallet.common.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.kooppi.nttca.portal.common.domain.BasicEntity;

@Entity
@Table(name = "contract_to_bu_master")
public class ContractToBuMaster extends BasicEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6898771618691590209L;
	
	@Column(name = "contract_id")
	private String contractId;
	
	@Column(name = "bu_name")
	private String buName;

	public static ContractToBuMaster create(String contractId, String buName){
		ContractToBuMaster map = new ContractToBuMaster();
		map.contractId = contractId;
		map.buName = buName;
		return map;
	}
	
	public void updateContractId(String contractId){
		this.contractId = contractId;
	}
	
	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getBuName() {
		return buName;
	}

	public void setBuName(String buName) {
		this.buName = buName;
	}
}
