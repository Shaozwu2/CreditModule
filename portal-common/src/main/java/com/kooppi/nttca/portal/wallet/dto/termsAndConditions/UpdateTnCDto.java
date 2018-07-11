package com.kooppi.nttca.portal.wallet.dto.termsAndConditions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "UpdateTnC", description = "update TnC resource representation")
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateTnCDto {
	@ApiModelProperty(value = "name", required = false, example = "file1")
	@XmlElement(name = "name")
	private String name;

	@ApiModelProperty(value = "description", required = true, example = "description")
	@XmlElement(name = "description")
	private String description;

	@ApiModelProperty(value = "buName", required = true, example = "Hosting")
	@XmlElement(name = "buName")
	private String buName;
	
	@ApiModelProperty(value = "isDefault", required = true, example = "true")
	@XmlElement(name = "isDefault")
	private Boolean isDefault;
	
	@ApiModelProperty(value = "isVisible", required = true, example = "true")
	@XmlElement(name = "isVisible")
	private Boolean isVisible;

	public void validateUpdateTnC() {
		PortalExceptionUtils.throwIfNull(name, PortalErrorCode.MISS_PARAM_NAME);
		PortalExceptionUtils.throwIfNull(isDefault, PortalErrorCode.MISS_PARAM_IS_DEFAULT);
		PortalExceptionUtils.throwIfNull(isVisible, PortalErrorCode.MISS_PARAM_IS_VISIBLE);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBuName() {
		return buName;
	}

	public void setBuName(String buName) {
		this.buName = buName;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public Boolean getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
	}

}
