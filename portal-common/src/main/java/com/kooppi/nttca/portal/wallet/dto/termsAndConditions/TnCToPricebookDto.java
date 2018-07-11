package com.kooppi.nttca.portal.wallet.dto.termsAndConditions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;

@XmlAccessorType(XmlAccessType.FIELD)
public class TnCToPricebookDto extends ResponseResult {

	@XmlElement(name = "uid")
	private Long uid;
	
	@XmlElement(name = "tncId")
	private Integer tncId;
	
	@XmlElement(name = "buName")
	private String buName;
	
	@XmlElement(name = "organizationId")
	private String organizationId;

	@XmlElement(name = "partNo")
	private String partNo;

	@XmlElement(name = "productName")
	private String productName;

	@XmlElement(name = "isDefaultBu")
	private Boolean isDefaultBu;
	
	@XmlElement(name = "isAllCustomer")
	private Boolean isAllCustomer;
	
	@XmlElement(name = "isAllBu")
	private Boolean isAllBu;
	
	@XmlElement(name = "isAllProduct")
	private Boolean isAllProduct;
	
	@XmlElement(name = "tnc")
	private TnCDto tnc;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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

	public TnCDto getTnc() {
		return tnc;
	}

	public void setTnc(TnCDto tnc) {
		this.tnc = tnc;
	}

	@Override
	public String getResultName() {
		return "tncToPricebook";
	}
}
