package com.kooppi.nttca.portal.wallet.dto.priceBook;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;
import com.kooppi.nttca.portal.common.utils.PortalXmlDateTimeAdapter;
import com.kooppi.nttca.portal.wallet.domain.PriceBookEnum.CategoryStatus;
import com.kooppi.nttca.portal.wallet.domain.PriceBookEnum.PriceBookStatus;
import com.kooppi.nttca.portal.wallet.domain.PriceBookEnum.ProductStatus;
import com.kooppi.nttca.portal.wallet.domain.PriceBookEnum.ServiceStatus;

@XmlAccessorType(XmlAccessType.FIELD)
public class PriceBookDto extends ResponseResult {

	@XmlElement(name = "uid")
	private Long uid;
	
	@XmlElement(name = "serviceFamily")
	private String serviceFamily;
	
	@XmlElement(name = "buName")
	private String buName;
	
	@XmlElement(name = "tcId")
	private Long tcId;
	
	@XmlElement(name = "serviceName")
	private String serviceName;
	
	@XmlElement(name = "shortName")
	private String shortName;
	
	@XmlElement(name = "categoryNo")
	private String categoryNo;
	
	@XmlElement(name = "categoryName")
	private String categoryName;
	
	@XmlElement(name = "partNo")
	private String partNo;
	
	@XmlElement(name = "productName")
	private String productName;
	
	@XmlElement(name = "currencyCode")
	private String currencyCode;
	
	@XmlElement(name = "oneOffPrice")
	private Integer oneOffPrice;
	
	@XmlElement(name = "recurringPrice")
	private Integer recurringPrice;
	
	@XmlElement(name = "serviceStatus")
	private ServiceStatus serviceStatus;
	
	@XmlElement(name = "categoryStatus")
	private CategoryStatus categoryStatus;
	
	@XmlElement(name = "productStatus")
	private ProductStatus productStatus;
	
	@XmlElement(name = "priceBookStatus")
	private PriceBookStatus priceBookStatus;
	
	@XmlElement(name = "glCode")
	private String glCode;
	
	@XmlElement(name = "provider")
	private String provider;
	
	@XmlElement(name = "providerRate")
	private BigDecimal providerRate;
	
	@XmlElement(name = "effectiveDate")
	@XmlJavaTypeAdapter(value = PortalXmlDateTimeAdapter.class)
	private LocalDateTime effectiveDate;
	
	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getServiceFamily() {
		return serviceFamily;
	}

	public void setServiceFamily(String serviceFamily) {
		this.serviceFamily = serviceFamily;
	}

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

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getCategoryNo() {
		return categoryNo;
	}

	public void setCategoryNo(String categoryNo) {
		this.categoryNo = categoryNo;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public Integer getOneOffPrice() {
		return oneOffPrice;
	}

	public void setOneOffPrice(Integer oneOffPrice) {
		this.oneOffPrice = oneOffPrice;
	}

	public Integer getRecurringPrice() {
		return recurringPrice;
	}

	public void setRecurringPrice(Integer recurringPrice) {
		this.recurringPrice = recurringPrice;
	}

	public ServiceStatus getServiceStatus() {
		return serviceStatus;
	}

	public void setServiceStatus(ServiceStatus serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

	public CategoryStatus getCategoryStatus() {
		return categoryStatus;
	}

	public void setCategoryStatus(CategoryStatus categoryStatus) {
		this.categoryStatus = categoryStatus;
	}

	public ProductStatus getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(ProductStatus productStatus) {
		this.productStatus = productStatus;
	}

	public PriceBookStatus getPriceBookStatus() {
		return priceBookStatus;
	}

	public void setPriceBookStatus(PriceBookStatus priceBookStatus) {
		this.priceBookStatus = priceBookStatus;
	}

	public String getGlCode() {
		return glCode;
	}

	public void setGlCode(String glCode) {
		this.glCode = glCode;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public BigDecimal getProviderRate() {
		return providerRate;
	}

	public void setProviderRate(BigDecimal providerRate) {
		this.providerRate = providerRate;
	}

	public LocalDateTime getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(LocalDateTime effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	@Override
	public String getResultName() {
		return "priceBook";
	}
}
