package com.kooppi.nttca.portal.wallet.dto.priceBook;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value ="BU Master", description = "BU Master resource representation")
public class BuMasterDto extends ResponseResult {

	@XmlElement(name = "uid")
	@ApiModelProperty(value = "BU Master UID", required = true)
	private Long uid;
	
	@XmlElement(name = "name")
	@ApiModelProperty(value = "Name", required = true)
	private String name;
	
	@XmlElement(name = "glCode")
	@ApiModelProperty(value = "GL Code", required = true)
	private String glCode;
	
	@Override
	public String getResultName() {
		return "buMaster";
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGlCode() {
		return glCode;
	}

	public void setGlCode(String glCode) {
		this.glCode = glCode;
	}
}
