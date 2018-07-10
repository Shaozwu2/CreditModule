package com.kooppi.nttca.portal.wallet.dto.priceBook;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.google.common.collect.Lists;
import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder ={"priceBooks", "count", "offset", "maxRows"})
public class PriceBookCollectionDto extends ResponseResult {

	@XmlElement(name = "priceBooks")
	private List<PriceBookDto> priceBooks = Lists.newArrayList();
	
	@XmlElement(name = "count")
	private Integer count;

	@XmlElement(name = "offset")
	private Integer offset;
	
	@XmlElement(name = "maxRows")
	private Integer maxRows;
	
	public static PriceBookCollectionDto create() {
		return new PriceBookCollectionDto();
	}
	
	public static PriceBookCollectionDto create(List<PriceBookDto> priceBooks, Integer count, Integer offset, Integer maxRows) {
		PriceBookCollectionDto dto = new PriceBookCollectionDto();
		dto.priceBooks = priceBooks;
		dto.count = count;
		dto.maxRows = maxRows;
		dto.offset = offset;
		return dto;
	}
	
	public static PriceBookCollectionDto create(List<PriceBookDto> priceBooks) {
		PriceBookCollectionDto dto = new PriceBookCollectionDto();
		dto.priceBooks = priceBooks;
		return dto;
	}

	public List<PriceBookDto> getPriceBooks() {
		return priceBooks;
	}

	public void setPriceBooks(List<PriceBookDto> priceBooks) {
		this.priceBooks = priceBooks;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getMaxRows() {
		return maxRows;
	}

	public void setMaxRows(Integer maxRows) {
		this.maxRows = maxRows;
	}

	@Override
	public String getResultName() {
		return "priceBookList";
	}
}
