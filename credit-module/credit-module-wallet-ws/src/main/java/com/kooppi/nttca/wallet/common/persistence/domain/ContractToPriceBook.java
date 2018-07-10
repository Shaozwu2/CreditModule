package com.kooppi.nttca.wallet.common.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.kooppi.nttca.portal.common.domain.BasicEntity;

@Entity
@Table(name = "contract_to_price_book")
public class ContractToPriceBook extends BasicEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6898771618691590209L;
	
	@Column(name = "contract_id")
	private String contractId;
	
	@Column(name = "part_no")
	private String partNo;

	public static ContractToPriceBook create(String contractId,String partNo){
		ContractToPriceBook map = new ContractToPriceBook();
		map.partNo = partNo;
		map.contractId = contractId;
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

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

}
