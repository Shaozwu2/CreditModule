package com.kooppi.nttca.portal.ce.dto.payment;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ReopenedContractIdItemDto {
	
	@XmlElement(name = "contractId")
	private String contractId;
	
	public ReopenedContractIdItemDto() {}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	
}
