package com.kooppi.nttca.wallet.common.persistence.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.kooppi.nttca.portal.common.domain.Modifiable;
import com.kooppi.nttca.portal.common.mq.domain.PricebookItem;
import com.kooppi.nttca.portal.common.persistence.convertor.LocalDateTimePersistenceConverter;
import com.kooppi.nttca.portal.wallet.domain.PriceBookEnum.CategoryStatus;
import com.kooppi.nttca.portal.wallet.domain.PriceBookEnum.PriceBookStatus;
import com.kooppi.nttca.portal.wallet.domain.PriceBookEnum.ProductStatus;
import com.kooppi.nttca.portal.wallet.domain.PriceBookEnum.ServiceStatus;
import com.kooppi.nttca.portal.wallet.dto.priceBook.PriceBookDto;
import com.kooppi.nttca.portal.wallet.dto.priceBook.PriceBookItemDto;

@Entity
@Table(name = "price_book")
public class PriceBook extends Modifiable {

	private static final long serialVersionUID = 1552743593336456426L;

	@Column(name = "service_family")
	private String serviceFamily;
	
	@Column(name = "bu_name")
	private String buName;
	
	@Column(name = "service_name")
	private String serviceName;
	
	@Column(name = "short_name")
	private String shortName;
	
	@Column(name = "category_no")
	private String categoryNo;
	
	@Column(name = "category_name")
	private String categoryName;
	
	@Column(name = "part_no")
	private String partNo;
	
	@Column(name = "product_name")
	private String productName;
	
	@Column(name = "currency_code")
	private String currencyCode;
	
	@Column(name = "one_off_price")
	private Integer oneOffPrice;
	
	@Column(name = "recurring_price")
	private Integer recurringPrice;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "service_status")
	private ServiceStatus serviceStatus;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "category_status")
	private CategoryStatus categoryStatus;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "product_status")
	private ProductStatus productStatus;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "pricebook_status")
	private PriceBookStatus priceBookStatus;
	
	@Column(name = "gl_code")
	private String glCode;
	
	@Column(name = "provider")
	private String provider;
	
	@Column(name = "provider_rate")
	private BigDecimal providerRate;
	
	@Convert(converter = LocalDateTimePersistenceConverter.class)
	@Column(name = "effective_date")
	private LocalDateTime effectiveDate;
	
	public PriceBook() {}

	public String getServiceFamily() {
		return serviceFamily;
	}

	public void setServiceFamily(String serviceFamily) {
		this.serviceFamily = serviceFamily;
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
	
	public String getBuName() {
		return buName;
	}

	public void setBuName(String buName) {
		this.buName = buName;
	}

	public PriceBookDto toPriceBookDto() {
		PriceBookDto dto = new PriceBookDto();
		dto.setUid(this.getUid());
		dto.setServiceFamily(this.getServiceFamily());
		dto.setBuName(this.getBuName());
		dto.setServiceName(this.getServiceName());
		dto.setShortName(this.getShortName());
		dto.setCategoryNo(this.getCategoryNo());
		dto.setCategoryName(this.getCategoryName());
		dto.setPartNo(this.getPartNo());
		dto.setProductName(this.getProductName());
		dto.setCurrencyCode(this.getCurrencyCode());
		dto.setOneOffPrice(this.getOneOffPrice());
		dto.setRecurringPrice(this.getRecurringPrice());
		dto.setServiceStatus(this.getServiceStatus());
		dto.setCategoryStatus(this.getCategoryStatus());
		dto.setProductStatus(this.getProductStatus());
		dto.setPriceBookStatus(this.getPriceBookStatus());
		dto.setGlCode(this.getGlCode());
		dto.setProvider(this.getProvider());
		dto.setProviderRate(this.getProviderRate());
		dto.setEffectiveDate(this.getEffectiveDate());
		return dto;
	}
	
	public PriceBookItemDto toPricebookItemDto() {
		PriceBookItemDto dto = new PriceBookItemDto();
		dto.setPartNo(this.partNo);
		dto.setProductName(this.productName);
		dto.setRecursivePrice(this.recurringPrice);
		dto.setOneOffPrice(this.oneOffPrice);
		if (priceBookStatus.equals(PriceBookStatus.Active) && serviceStatus.equals(ServiceStatus.Active) 
				&& categoryStatus.equals(CategoryStatus.Active) && productStatus.equals(ProductStatus.Active)) {
			dto.setStatus("ACTIVE");
		} else {
			dto.setStatus("INACTIVE");
		}
		return dto;
	}
	
	public PriceBook(PricebookItem pbI) {
		super();
		this.serviceFamily = pbI.getService_family();
		this.serviceName = pbI.getService_name();
		this.shortName = pbI.getService_name();
		this.serviceStatus = pbI.getService_status();
		this.categoryNo = pbI.getCategory_no();
		this.categoryName = pbI.getCategory_name();
		this.glCode = pbI.getGl_code();
		this.categoryStatus = pbI.getCategory_status();
		this.partNo = pbI.getPart_no();
		this.productName = pbI.getProduct_name();
		this.productStatus = pbI.getProduct_status();
		this.currencyCode = pbI.getCurrency();
		this.oneOffPrice = Math.round(pbI.getOneoff_price());
		this.recurringPrice = Math.round(pbI.getRecurring_price());
		this.effectiveDate = pbI.getPricebook_effective_date();
		this.priceBookStatus = pbI.getPricebook_status();
		this.provider = pbI.getProviding_company();
		this.providerRate = BigDecimal.valueOf(95);
	}
	
	public void edit(PricebookItem pbI) {
		this.serviceFamily = pbI.getService_family();
		this.serviceName = pbI.getService_name();
		this.shortName = pbI.getService_name();
		this.serviceStatus = pbI.getService_status();
		this.categoryNo = pbI.getCategory_no();
		this.categoryName = pbI.getCategory_name();
		this.glCode = pbI.getGl_code();
		this.categoryStatus = pbI.getCategory_status();
		this.productName = pbI.getProduct_name();
		this.productStatus = pbI.getProduct_status();
		this.currencyCode = pbI.getCurrency();
		this.oneOffPrice = Math.round(pbI.getOneoff_price());
		this.recurringPrice = Math.round(pbI.getRecurring_price());
		this.effectiveDate = pbI.getPricebook_effective_date();
		this.priceBookStatus = pbI.getPricebook_status();
		this.provider = pbI.getProviding_company();
	}
}
