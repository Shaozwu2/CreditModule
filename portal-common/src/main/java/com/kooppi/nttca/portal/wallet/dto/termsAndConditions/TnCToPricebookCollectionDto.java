package com.kooppi.nttca.portal.wallet.dto.termsAndConditions;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.google.common.collect.Lists;
import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder ={"tncToPricebooks", "count", "offset", "maxRows"})
public class TnCToPricebookCollectionDto extends ResponseResult {
	
	@XmlElement(name = "tncToPricebooks")
	private List<TnCToPricebookDto> tncToPricebooks = Lists.newArrayList();
	
	@XmlElement(name = "count")
	private Integer count;

	@XmlElement(name = "offset")
	private Integer offset;
	
	@XmlElement(name = "maxRows")
	private Integer maxRows;
	
	public static TnCToPricebookCollectionDto create() {
		return new TnCToPricebookCollectionDto();
	}
	
	public static TnCToPricebookCollectionDto create(List<TnCToPricebookDto> tncToPricebooks, Integer count, Integer offset, Integer maxRows) {
		TnCToPricebookCollectionDto dto = new TnCToPricebookCollectionDto();
		dto.tncToPricebooks = tncToPricebooks;
		dto.count = count;
		dto.maxRows = maxRows;
		dto.offset = offset;
		return dto;
	}
	
	public static TnCToPricebookCollectionDto create(List<TnCToPricebookDto> tncToPricebooks) {
		TnCToPricebookCollectionDto dto = new TnCToPricebookCollectionDto();
		dto.tncToPricebooks = tncToPricebooks;
		return dto;
	}
	
	@Override
	public String getResultName() {
		return "tncToPricebookList";
	}

	public List<TnCToPricebookDto> getTncToPricebooks() {
		return tncToPricebooks;
	}

	public void setTncToPricebooks(List<TnCToPricebookDto> tncToPricebooks) {
		this.tncToPricebooks = tncToPricebooks;
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

}
