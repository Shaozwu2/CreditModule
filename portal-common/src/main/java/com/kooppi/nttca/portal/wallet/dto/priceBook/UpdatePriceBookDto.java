package com.kooppi.nttca.portal.wallet.dto.priceBook;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value ="UpdatePriceBook", description = "Update Price Book resource representation")
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdatePriceBookDto {

	@ApiModelProperty( value = "buName", required = true)
	@XmlElement(name = "buName")
	private String buName;
	
	@ApiModelProperty( value = "tcId", required = true)
	@XmlElement(name = "tcId")
	private Long tcId;

	public String getBuName() {
		return buName;
	}

	public void setBuName(String buName) {
		this.buName = buName;
	}

	public Long getTcId() {
		return tcId;
	}

	public void setTcId(Long tcId) {
		this.tcId = tcId;
	}
}
