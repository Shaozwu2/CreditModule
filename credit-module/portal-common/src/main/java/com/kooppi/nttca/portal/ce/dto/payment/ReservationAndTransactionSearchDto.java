package com.kooppi.nttca.portal.ce.dto.payment;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class ReservationAndTransactionSearchDto {

	@XmlElement(name = "organizationNames")
	private List<String> organizationNames;
	
	public ReservationAndTransactionSearchDto() {}

	public List<String> getOrganizationNames() {
		return organizationNames;
	}

	public void setOrganizationNames(List<String> organizationNames) {
		this.organizationNames = organizationNames;
	}

	
}
