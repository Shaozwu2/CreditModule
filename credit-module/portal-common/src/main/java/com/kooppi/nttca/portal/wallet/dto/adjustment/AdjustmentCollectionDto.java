package com.kooppi.nttca.portal.wallet.dto.adjustment;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.google.common.collect.Lists;
import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder ={"adjustments", "count", "offset", "maxRows"})
public class AdjustmentCollectionDto extends ResponseResult{

	@XmlElement(name = "adjustments")
	private List<AdjustmentDto>  adjustments = Lists.newArrayList(); 
	
	@XmlElement(name = "count")
	private Integer count;

	@XmlElement(name = "offset")
	private Integer offset;
	
	@XmlElement(name = "maxRows")
	private Integer maxRows;
	
	public static AdjustmentCollectionDto create() {
		return new AdjustmentCollectionDto();
	}
	
	public static AdjustmentCollectionDto create(List<AdjustmentDto> adjustments, Integer count, Integer offset, Integer maxRows) {
		AdjustmentCollectionDto dto = new AdjustmentCollectionDto();
		dto.adjustments.addAll(adjustments);
		dto.count = count;
		dto.maxRows = maxRows;
		dto.offset = offset;
		return dto;
	}
	
	public List<AdjustmentDto> getAdjustmentDtos() {
		return adjustments;
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
		return "adjustmentlist";
	}
}
