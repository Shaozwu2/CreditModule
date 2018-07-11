package com.kooppi.nttca.portal.wallet.dto.priceBook;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.google.common.collect.Lists;
import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder ={"priceBookItems"})
public class PriceBookItemsDto extends ResponseResult {

	@XmlElement(name = "priceBookItems")
	private List<PriceBookItemDto> priceBookItems = Lists.newArrayList();
	
	public static PriceBookItemsDto create() {
		return new PriceBookItemsDto();
	}
	
	public static PriceBookItemsDto create(List<PriceBookItemDto> priceBookItems) {
		PriceBookItemsDto dto = new PriceBookItemsDto();
		dto.priceBookItems = priceBookItems;
		return dto;
	}
	
	public List<PriceBookItemDto> getPriceBookItems() {
		return priceBookItems;
	}

	public void setPriceBookItems(List<PriceBookItemDto> priceBookItems) {
		this.priceBookItems = priceBookItems;
	}

	@Override
	public String getResultName() {
		return "priceBookItems";
	}
}
