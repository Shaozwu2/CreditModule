package com.kooppi.nttca.portal.wallet.dto.termsAndConditions;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.google.common.collect.Lists;
import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder ={"tncs", "count", "offset", "maxRows"})
public class TnCCollectionDto extends ResponseResult {

	@XmlElement(name = "tncs")
	private List<TnCDto> tncs = Lists.newArrayList();
	
	@XmlElement(name = "count")
	private Integer count;

	@XmlElement(name = "offset")
	private Integer offset;
	
	@XmlElement(name = "maxRows")
	private Integer maxRows;
	
	public static TnCCollectionDto create() {
		return new TnCCollectionDto();
	}
	
	public static TnCCollectionDto create(List<TnCDto> tncs, Integer count, Integer offset, Integer maxRows) {
		TnCCollectionDto dto = new TnCCollectionDto();
		dto.tncs = tncs;
		dto.count = count;
		dto.maxRows = maxRows;
		dto.offset = offset;
		return dto;
	}
	
	public static TnCCollectionDto create(List<TnCDto> tncs) {
		TnCCollectionDto dto = new TnCCollectionDto();
		dto.tncs = tncs;
		return dto;
	}

	public List<TnCDto> getTncs() {
		return tncs;
	}

	public void setTncs(List<TnCDto> tncs) {
		this.tncs = tncs;
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
		return "tncList";
	}
}
