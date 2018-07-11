package com.kooppi.nttca.portal.wallet.dto.priceBook;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class GetPriceBookByPartNoDto {

	@XmlElement(name = "partNos")
	private List<GetPriceBookByPartNoItemDto> partNos;
	
	public GetPriceBookByPartNoDto() {}

	public List<GetPriceBookByPartNoItemDto> getPartNos() {
		return partNos;
	}

	public void setPartNos(List<GetPriceBookByPartNoItemDto> partNos) {
		this.partNos = partNos;
	}
	
	


}
