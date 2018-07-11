package com.kooppi.nttca.portal.wallet.dto.termsAndConditions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "UpdateTnCToPricebook", description = "Update TnC resource representation")
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateTncToPricebookDto {
	
	@ApiModelProperty(value = "tncId", required = true, example = "123")
	@XmlElement(name = "tncId")
	private Integer tncId;
	
	@ApiModelProperty(value = "buName", required = true, example = "Hosting")
	@XmlElement(name = "buName")
	private String buName;
	
	@ApiModelProperty(value = "isDefaultBu", required = true, example = "true")
	@XmlElement(name = "isDefaultBu")
	private Boolean isDefaultBu;
	
	@ApiModelProperty(value = "isAllCustomer", required = true, example = "true")
	@XmlElement(name = "isAllCustomer")
	private Boolean isAllCustomer;

	@ApiModelProperty(value = "isAllBu", required = true, example = "true")
	@XmlElement(name = "isAllBu")
	private Boolean isAllBu;
	
	@ApiModelProperty(value = "isAllProduct", required = true, example = "true")
	@XmlElement(name = "isAllProduct")
	private Boolean isAllProduct;
	
	@ApiModelProperty(value = "organizationId", required = true, example = "organizationId")
	@XmlElement(name = "organizationId")
	private String organizationId;
	
	@ApiModelProperty(value = "partNo", required = true, example = "partNo")
	@XmlElement(name = "partNo")
	private String partNo;
	
	public void validateUpdateTnCToPricebook() {
		PortalExceptionUtils.throwIfNull(isDefaultBu, PortalErrorCode.MISS_PARAM_IS_DEFAULT_BU);
		PortalExceptionUtils.throwIfNull(isAllBu, PortalErrorCode.MISS_PARAM_IS_ALL_BU);
		PortalExceptionUtils.throwIfNull(isAllProduct, PortalErrorCode.MISS_PARAM_IS_ALL_PRODUCT);
		PortalExceptionUtils.throwIfNull(isAllCustomer, PortalErrorCode.MISS_PARAM_IS_ALL_CUSTOMER);
		PortalExceptionUtils.throwIfNull(tncId, PortalErrorCode.MISS_PARAM_TNC_ID);
	}

	public Integer getTncId() {
		return tncId;
	}

	public void setTncId(Integer tncId) {
		this.tncId = tncId;
	}

	public String getBuName() {
		return buName;
	}

	public void setBuName(String buName) {
		this.buName = buName;
	}

	public Boolean getIsDefaultBu() {
		return isDefaultBu;
	}

	public void setIsDefaultBu(Boolean isDefaultBu) {
		this.isDefaultBu = isDefaultBu;
	}

	public Boolean getIsAllCustomer() {
		return isAllCustomer;
	}

	public void setIsAllCustomer(Boolean isAllCustomer) {
		this.isAllCustomer = isAllCustomer;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public Boolean getIsAllBu() {
		return isAllBu;
	}

	public void setIsAllBu(Boolean isAllBu) {
		this.isAllBu = isAllBu;
	}

	public Boolean getIsAllProduct() {
		return isAllProduct;
	}

	public void setIsAllProduct(Boolean isAllProduct) {
		this.isAllProduct = isAllProduct;
	}
	
}
