package com.kooppi.nttca.portal.wallet.dto.termsAndConditions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class GetTnCsRequestDto {
	
	@XmlElement(name = "partNo")
	private String partNo;

	@XmlElement(name = "customerId")
	private String customerId;
	
	public GetTnCsRequestDto() {}

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
}
