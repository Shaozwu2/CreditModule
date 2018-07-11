package com.kooppi.nttca.portal.wallet.dto.adjustment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.kooppi.nttca.portal.common.utils.PortalXmlDateAdapter;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;
import com.kooppi.nttca.portal.wallet.domain.AdjustmentType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value ="CreateAdjustment", description = "Create Adjustment resource representation")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateAdjustmentDto {
	
	@ApiModelProperty( value = "Ref Number", required=false, example="ref001")
	@XmlElement(name = "refNumber")
	private String refNumber;
	
	@ApiModelProperty( value = "Adjustment Type", required = true, example= "SERVICE_ORDER/MANUAL_ADJUSTMENT/MONTHLY_RECHARGE")
	@XmlElement(name = "adjustmentType")
	private AdjustmentType adjustmentType;
	
	@ApiModelProperty( value = "companyCode", required = true)
	@XmlElement(name = "companyCode")
	private String companyCode;
	
	@ApiModelProperty( value = "amount", required = true)
	@XmlElement(name = "amount")
	private Integer amount;
	
	@ApiModelProperty( value = "currencyAmount", required = true)
	@XmlElement(name = "currencyAmount")
	private BigDecimal currencyAmount;
	
	@ApiModelProperty( value = "currencyCode", required = true)
	@XmlElement(name = "currencyCode")
	private String currencyCode;
	
	@ApiModelProperty( value = "exchangeRate", required = true)
	@XmlElement(name = "exchangeRate")
	private BigDecimal exchangeRate;
	
	@ApiModelProperty( value = "description", required = true, example="create adjustment")
	@XmlElement(name = "description")
	private String description;
	
	@ApiModelProperty( value = "contractEffectiveDate", required = false, example="2016-12-01")
	@XmlElement(name = "contractEffectiveDate")
	@XmlJavaTypeAdapter(value = PortalXmlDateAdapter.class)
	private LocalDate contractEffectiveDate;
	
	@ApiModelProperty( value = "contractTerminationDate", required = false, example="2017-12-01")
	@XmlElement(name = "contractTerminationDate")
	@XmlJavaTypeAdapter(value = PortalXmlDateAdapter.class)
	private LocalDate contractTerminationDate;
	
	@ApiModelProperty( value = "creditExpiryDate", required = false, example="2016-12-01")
	@XmlElement(name = "creditExpiryDate")
	@XmlJavaTypeAdapter(value = PortalXmlDateAdapter.class)
	private LocalDate creditExpiryDate;

	@ApiModelProperty( value = "terminatedReason", required = false, example="Contract expiration")
	@XmlElement(name = "terminatedReason")
	private String terminatedReason;
	
	@ApiModelProperty( value = "buLists", required = false, example="buLists{}")
	@XmlElement(name = "bus")
	private List<String> buLists;

	@ApiModelProperty( value = "productLists", required = false, example="productLists{} ")
	@XmlElement(name = "products")
	private List<String> productLists;

	@ApiModelProperty( value = "isAllBu", required = false, example="true")
	@XmlElement(name = "isAllBu")
	private Boolean isAllBu;

	@ApiModelProperty( value = "isAllProduct", required = false, example="true")
	@XmlElement(name = "isAllProduct")
	private Boolean isAllProduct;
	
	public String getRefNumber() {
		return refNumber;
	}

	public String getCompanyCode() {
		return companyCode;
	}
	
	public BigDecimal getCurrencyAmount() {
		return currencyAmount;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public LocalDate getContractEffectiveDate() {
		return contractEffectiveDate;
	}

	public LocalDate getContractTerminationDate() {
		return contractTerminationDate;
	}

	public LocalDate getCreditExpiryDate() {
		return creditExpiryDate;
	}

	public AdjustmentType getAdjustmentType() {
		return adjustmentType;
	}

	public Integer getAmount() {
		return amount;
	}

	public String getDescription() {
		return description;
	}

	public String getTerminatedReason() {
		return terminatedReason;
	}

	public List<String> getBuLists() {
		return buLists;
	}

	public List<String> getProductLists() {
		return productLists;
	}

	public Boolean getIsAllBu() {
		return isAllBu;
	}

	public Boolean getIsAllProduct() {
		return isAllProduct;
	}

	//TODO
	public void validateCreateAdjustment() {
		PortalExceptionUtils.throwIfNull(adjustmentType, PortalErrorCode.MISS_PARAM_ADJUSTMENT_TYPE);
		PortalExceptionUtils.throwIfNull(amount, PortalErrorCode.MISS_PAYLOAD_PARAM_AMOUNT);
		PortalExceptionUtils.throwIfNull(contractEffectiveDate, PortalErrorCode.MISS_PARAM_CONTRACT_EFFECTIVE_DATE);
		PortalExceptionUtils.throwIfTrue(adjustmentType == AdjustmentType.MONTHLY_RECHARGE, PortalErrorCode.UNSUPPORT_CONTRACT_TYPE);
		
		if (adjustmentType == AdjustmentType.ONE_OFF) {
			PortalExceptionUtils.throwIfNull(refNumber, PortalErrorCode.MISS_PARAM_REF_NUMBER);
			PortalExceptionUtils.throwIfNull(companyCode, PortalErrorCode.MISS_PAYLOAD_PARAM_COMPANY_CODE);
			PortalExceptionUtils.throwIfNull(currencyAmount, PortalErrorCode.MISS_PAYLOAD_PARAM_CURRENCY_AMOUNT);
		} else if (adjustmentType == AdjustmentType.COMPENSATION) {
			PortalExceptionUtils.throwIfNull(isAllBu, PortalErrorCode.MISS_PARAM_IS_ALL_BU);
			PortalExceptionUtils.throwIfNull(isAllProduct, PortalErrorCode.MISS_PARAM_IS_ALL_PRODUCT);
			if (!isAllBu && !isAllProduct) {
				PortalExceptionUtils.throwIfEmpty(productLists, PortalErrorCode.MISS_PRODUCT_ITEM);
				PortalExceptionUtils.throwIfEmpty(buLists, PortalErrorCode.MISS_PRODUCT_ITEM);
			} else if (!isAllBu && isAllProduct) {
				PortalExceptionUtils.throwIfEmpty(buLists, PortalErrorCode.MISS_PRODUCT_ITEM);
			}
		}
		//schedule adjustment allowed to set amount to negative 
		if(LocalDate.now().isBefore(contractEffectiveDate)) {
			PortalExceptionUtils.throwIfTrue(amount < 0, PortalErrorCode.POSITIVE_ADJUSTMENT_IS_REQUIRED_FOR_SCHEDULED_TYPE);
		}
		PortalExceptionUtils.throwIfTrue((LocalDate.now().isAfter(contractEffectiveDate)), PortalErrorCode.INVALID_SCHEDULED_DATE);
		if (contractTerminationDate != null)
			PortalExceptionUtils.throwIfTrue(contractTerminationDate.isBefore(contractEffectiveDate), PortalErrorCode.INVALID_CONTRACT_TERMINATION_DATE);
		
//			PortalExceptionUtils.throwIfTrue(amount < 0, PortalErrorCode.POSITIVE_ADJUSTMENT_IS_REQUIRED_FOR_SCHEDULED_TYPE);
//			PortalExceptionUtils.throwIfFalse(LocalDate.now().isBefore(contractEffectiveDate), PortalErrorCode.INVALID_CONTRACT_EFFECTIVE_DATE);
//			
//			if(contractTerminationDate != null) {
//				PortalExceptionUtils.throwIfTrue(contractTerminationDate.isBefore(contractEffectiveDate), PortalErrorCode.INVALID_CONTRACT_TERMINATION_DATE);
//			}
	}
	
}
