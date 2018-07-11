package com.kooppi.nttca.portal.wallet.dto.termsAndConditions;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.google.common.collect.Lists;
import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;

@XmlAccessorType(XmlAccessType.FIELD)
public class GetTnCsResponseCollectionDto extends ResponseResult {

	@XmlElement(name = "tncs")
	private List<TnCDto> tncs = Lists.newArrayList();

	public List<TnCDto> getTncs() {
		return tncs;
	}

	public void setTncs(List<TnCDto> tncs) {
		this.tncs = tncs;
	}

	@Override
	public String getResultName() {
		return "tnc";
	}

}
