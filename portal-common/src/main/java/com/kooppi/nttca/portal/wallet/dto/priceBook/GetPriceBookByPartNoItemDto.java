package com.kooppi.nttca.portal.wallet.dto.priceBook;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class GetPriceBookByPartNoItemDto {
	
	@XmlElement(name = "partNo")
	private String partNo;
	
	public GetPriceBookByPartNoItemDto() {}

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	
}
