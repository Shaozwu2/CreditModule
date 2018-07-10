package com.kooppi.nttca.portal.ce.dto.payment;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.google.common.collect.Lists;

public class ReopenedContractIdDto {

	@XmlElement(name = "contractIds")
	private List<ReopenedContractIdItemDto> contractIds = Lists.newArrayList();
	
	public ReopenedContractIdDto() {}

	public List<ReopenedContractIdItemDto> getContractIds() {
		return contractIds;
	}

	public void setContractIds(List<ReopenedContractIdItemDto> contractIds) {
		this.contractIds = contractIds;
	}

}
