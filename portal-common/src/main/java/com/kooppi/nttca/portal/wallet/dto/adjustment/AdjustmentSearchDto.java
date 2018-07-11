package com.kooppi.nttca.portal.wallet.dto.adjustment;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.google.common.collect.Lists;

public class AdjustmentSearchDto {

	@XmlElement(name = "organizationNames")
	private List<String> organizationNames = Lists.newArrayList();
	
	public AdjustmentSearchDto() {}

	public List<String> getOrganizationNames() {
		return organizationNames;
	}

	public void setOrganizationNames(List<String> organizationNames) {
		this.organizationNames = organizationNames;
	}
}
