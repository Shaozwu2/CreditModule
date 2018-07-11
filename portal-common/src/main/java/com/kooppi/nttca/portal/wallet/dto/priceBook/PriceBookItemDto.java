package com.kooppi.nttca.portal.wallet.dto.priceBook;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class PriceBookItemDto {
	
	@XmlElement(name = "productName")
	private String productName;
	
	@XmlElement(name = "status")
	private String status;
	
	@XmlElement(name = "partNo")
	private String partNo;
	
	@XmlElement(name = "oneOffPrice")
	private Integer oneOffPrice;
	
	@XmlElement(name = "recursivePrice")
	private Integer recursivePrice;
	
	public PriceBookItemDto() {}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public Integer getOneOffPrice() {
		return oneOffPrice;
	}

	public void setOneOffPrice(Integer oneOffPrice) {
		this.oneOffPrice = oneOffPrice;
	}

	public Integer getRecursivePrice() {
		return recursivePrice;
	}

	public void setRecursivePrice(Integer recursivePrice) {
		this.recursivePrice = recursivePrice;
	}

	
}
