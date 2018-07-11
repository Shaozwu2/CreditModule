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

@ApiModel(value ="UpdateAdjustment", description = "Update Adjustment resource representation")
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateAdjustmentDto {
	
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
	
	@ApiModelProperty( value = "description", required = false, hidden = true)
	@XmlElement(name = "description")
	private String description;
	
	@ApiModelProperty( value = "refNumber", required = false, hidden = true)
	@XmlElement(name = "refNumber")
	private String refNumber;
	
	@ApiModelProperty( value = "contractEffectiveDate", required = false, example="2016-12-01")
	@XmlElement(name = "contractEffectiveDate")
	@XmlJavaTypeAdapter(value = PortalXmlDateAdapter.class)
	private LocalDate contractEffectiveDate;

	@ApiModelProperty( value = "contractTerminationDate", required = false, example="2016-12-01")
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
	
//	@ApiModelProperty( value = "assignedBuLists", required = false, example="assignedBuLists{}")
//	@XmlElement(name = "assignedBuLists")
//	private List<String> assignedBuLists;
//	
//	@ApiModelProperty( value = "removedBuLists", required = false, example="removedBuLists{}")
//	@XmlElement(name = "removedBuLists")
//	private List<String> removedBuLists;
//
//	@ApiModelProperty( value = "assignedProductLists", required = false, example="assignedProductLists{} ")
//	@XmlElement(name = "assignedProductLists")
//	private List<String> assignedProductLists;
//
//	@ApiModelProperty( value = "removedProductLists", required = false, example="removedProductLists{} ")
//	@XmlElement(name = "removedProductLists")
//	private List<String> removedProductLists;
	
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
	
	public String getCompanyCode() {
		return companyCode;
	}
	
	public Integer getAmount() {
		return amount;
	}

	public String getDescription() {
		return description;
	}

	public String getRefNumber() {
		return refNumber;
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

	public String getTerminatedReason() {
		return terminatedReason;
	}

	public Boolean getIsAllBu() {
		return isAllBu;
	}

	public Boolean getIsAllProduct() {
		return isAllProduct;
	}

	public List<String> getBuLists() {
		return buLists;
	}

	public List<String> getProductLists() {
		return productLists;
	}

	public void validateUpdateAdjustment(AdjustmentDto adj) {
		AdjustmentType targetType = adj.getAdjustmentType();
		PortalExceptionUtils.throwIfNullOrEmptyString(companyCode, PortalErrorCode.MISS_PAYLOAD_PARAM_COMPANY_CODE);
		PortalExceptionUtils.throwIfNull(amount, PortalErrorCode.MISS_PAYLOAD_PARAM_AMOUNT);
		PortalExceptionUtils.throwIfNull(currencyAmount, PortalErrorCode.MISS_PAYLOAD_PARAM_CURRENCY_AMOUNT);
		PortalExceptionUtils.throwIfNull(targetType, PortalErrorCode.MISS_PARAM_ADJUSTMENT_TYPE);
		
		if (targetType == AdjustmentType.ONE_OFF || targetType == AdjustmentType.MONTHLY_RECHARGE) {
			PortalExceptionUtils.throwIfNull(refNumber, PortalErrorCode.MISS_PARAM_REF_NUMBER);
		}
		
		// On or after (which means not before) Contract Effective Date, only allow to edit Credit Expiry Date and Description
		if (!LocalDate.now().isBefore(contractEffectiveDate)) {
			boolean isValid = false;
			
			if (isEqualValue(refNumber, adj.getRefNumber())
					&& isEqualValue(companyCode, adj.getCompanyCode())
					&& isEqualValue(amount, adj.getNttDollarSO())
					&& isEqualValue(currencyAmount, adj.getCurrencyAmountSO())
					&& isEqualValue(exchangeRate, adj.getExchangeRate())
					&& isEqualValue(contractEffectiveDate, adj.getContractEffectiveDate())
					&& isEqualValue(contractTerminationDate, adj.getContractTerminationDate())
					&& isEqualValue(terminatedReason, adj.getTerminatedReason()))
				isValid = true;
			
			PortalExceptionUtils.throwIfFalse(isValid, PortalErrorCode.COMPENSATION_CONTRACT_EDIT_NOT_ALLOWED_FIELDS);
		}
		
		PortalExceptionUtils.throwIfTrue(amount < 0, PortalErrorCode.POSITIVE_ADJUSTMENT_IS_REQUIRED_FOR_SCHEDULED_TYPE);
		PortalExceptionUtils.throwIfNull(contractEffectiveDate, PortalErrorCode.MISS_PARAM_CONTRACT_EFFECTIVE_DATE);
		
		if(contractTerminationDate != null) 
			PortalExceptionUtils.throwIfFalse(contractEffectiveDate.isBefore(contractTerminationDate), PortalErrorCode.INVALID_CONTRACT_TERMINATION_DATE);
	}
	
	private boolean isEqualValue(Object s1, Object s2) {
		if (s1 == null && s2 == null) {
			return true;
		} else if (s1 == null && s2 != null) {
			return false;
		} else if (s1 != null && s2 == null) {
			return false;
		} else {
			if (s1.equals(s2))
				return true;
			else 
				return false;
		}
	}
}
