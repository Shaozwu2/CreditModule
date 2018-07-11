package com.kooppi.nttca.portal.common.mq.domain;

import java.time.LocalDateTime;

import com.kooppi.nttca.portal.wallet.domain.PriceBookEnum.CategoryStatus;
import com.kooppi.nttca.portal.wallet.domain.PriceBookEnum.PriceBookStatus;
import com.kooppi.nttca.portal.wallet.domain.PriceBookEnum.ProductStatus;
import com.kooppi.nttca.portal.wallet.domain.PriceBookEnum.ServiceStatus;

public class PricebookItem {

//	private String buName;
	private String service_family;
	private String service_name;
	private String short_name;
	private ServiceStatus service_status;
	private String category_no;
	private String category_name;
	private String gl_code;
	private CategoryStatus category_status;
	private String part_no;
	private String product_name;
	private ProductStatus product_status;
	private String currency;
	private Float oneoff_price;
	private Float recurring_price;
	private LocalDateTime pricebook_effective_date;
	private PriceBookStatus pricebook_status;
	private String providing_company;
//	private Boolean primary_provider;
	
	public PricebookItem() {}

	public String getService_family() {
		return service_family;
	}

	public void setService_family(String service_family) {
		this.service_family = service_family;
	}

	public String getService_name() {
		return service_name;
	}

	public void setService_name(String service_name) {
		this.service_name = service_name;
	}

	public String getShort_name() {
		return short_name;
	}

	public void setShort_name(String short_name) {
		this.short_name = short_name;
	}

	public ServiceStatus getService_status() {
		return service_status;
	}

	public void setService_status(ServiceStatus service_status) {
		this.service_status = service_status;
	}

	public String getCategory_no() {
		return category_no;
	}

	public void setCategory_no(String category_no) {
		this.category_no = category_no;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getGl_code() {
		return gl_code;
	}

	public void setGl_code(String gl_code) {
		this.gl_code = gl_code;
	}

	public CategoryStatus getCategory_status() {
		return category_status;
	}

	public void setCategory_status(CategoryStatus category_status) {
		this.category_status = category_status;
	}

	public String getPart_no() {
		return part_no;
	}

	public void setPart_no(String part_no) {
		this.part_no = part_no;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public ProductStatus getProduct_status() {
		return product_status;
	}

	public void setProduct_status(ProductStatus product_status) {
		this.product_status = product_status;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Float getOneoff_price() {
		return oneoff_price;
	}

	public void setOneoff_price(Float oneoff_price) {
		this.oneoff_price = oneoff_price;
	}

	public Float getRecurring_price() {
		return recurring_price;
	}

	public void setRecurring_price(Float recurring_price) {
		this.recurring_price = recurring_price;
	}

	public LocalDateTime getPricebook_effective_date() {
		return pricebook_effective_date;
	}

	public void setPricebook_effective_date(LocalDateTime pricebook_effective_date) {
		this.pricebook_effective_date = pricebook_effective_date;
	}

	public PriceBookStatus getPricebook_status() {
		return pricebook_status;
	}

	public void setPricebook_status(PriceBookStatus pricebook_status) {
		this.pricebook_status = pricebook_status;
	}

	public String getProviding_company() {
		return providing_company;
	}

	public void setProviding_company(String providing_company) {
		this.providing_company = providing_company;
	}
	
	
}
