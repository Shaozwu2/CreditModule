package com.kooppi.nttca.portal.wallet.dto.termsAndConditions;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.google.common.collect.Lists;

@XmlAccessorType(XmlAccessType.FIELD)
public class GetTnCsRequestCollectionDto {

	@XmlElement(name = "requestParams")
	private List<GetTnCsRequestDto> requestParams = Lists.newArrayList();

	public List<GetTnCsRequestDto> getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(List<GetTnCsRequestDto> requestParams) {
		this.requestParams = requestParams;
	}

}
