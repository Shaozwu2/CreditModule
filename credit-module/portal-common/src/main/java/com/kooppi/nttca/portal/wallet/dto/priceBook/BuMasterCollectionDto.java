package com.kooppi.nttca.portal.wallet.dto.priceBook;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;

import jersey.repackaged.com.google.common.collect.Lists;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder ={"buMasters", "count"})
public class BuMasterCollectionDto extends ResponseResult {

	@XmlElement(name = "buMasters")
	private List<BuMasterDto> buMasters = Lists.newArrayList();
	
	@XmlElement(name = "count")
	private Integer count;
	
	public static BuMasterCollectionDto create() {
		return new BuMasterCollectionDto();
	}
	
	public static BuMasterCollectionDto create(List<BuMasterDto> buMasters, Integer count) {
		BuMasterCollectionDto dto = new BuMasterCollectionDto();
		dto.buMasters = buMasters;
		dto.count = count;
		return dto;
	}
	
	@Override
	public String getResultName() {
		return "buMasterList";
	}

	public List<BuMasterDto> getBuMasters() {
		return buMasters;
	}

	public void setBuMasters(List<BuMasterDto> buMasters) {
		this.buMasters = buMasters;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}
