package com.kooppi.nttca.portal.wallet.dto.wallet;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.google.common.collect.Lists;
import com.kooppi.nttca.portal.ce.dto.payment.ChargingItemDto;
import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value ="ConsumeCreditDto", description = "Consume Credit resource representation")
public class ConsumeCreditDto extends ResponseResult{

	@ApiModelProperty( value = "paymentId", required = true) 
	@XmlElement(name = "paymentId")
	private String paymentId;
	
	@ApiModelProperty( value = "Service Id", required = true, example ="service001") 
	@XmlElement(name = "serviceId")
	private String serviceId;
	
	@ApiModelProperty( value = "Item", required = true, example ="item001") 
	@XmlElement(name = "item")
	private String item;
	
	@ApiModelProperty( value = "Item Type", required = true, example ="type001")
	@XmlElement(name = "itemType")
	private String itemType;
	
	@ApiModelProperty( value = "Amount", required = true, example ="100")
	@XmlElement(name = "amount")
	private Integer amount;
	
	@ApiModelProperty( value = "Currency Amount", required = true, example ="100")
	@XmlElement(name = "currencyAmount")
	private BigDecimal currencyAmount;
	
	@ApiModelProperty( value = "Action", required = true, example ="consumeCredit")
	@XmlElement(name = "action")
	private String action;
	
	@ApiModelProperty(required = false, hidden=true)
	@XmlElement(name = "description")
	private String description;
	
	@ApiModelProperty(required = true, hidden=false)
	@XmlElement(name = "chargeItems")
	private List<ChargingItemDto> chargeItems = Lists.newArrayList();
	
	@ApiModelProperty(required = false, hidden=true)
	@Override
	public String getResultName() {
		return "consumeCredits";
	}

	public String getPaymentId(){
		return paymentId;
	}
	
	public String getServiceId() {
		return serviceId;
	}

	public String getItem() {
		return item;
	}

	public String getItemType() {
		return itemType;
	}

	public Integer getAmount() {
		return amount;
	}
	
	public BigDecimal getCurrencyAmount() {
		return currencyAmount;
	}

	public String getAction() {
		return action;
	}

	public String getDescription() {
		return description;
	}

	public List<ChargingItemDto> getChargeItems(){
		return chargeItems;
	}
	
	public void validateConsumeCredit() {
		PortalExceptionUtils.throwIfNullOrEmptyString(paymentId, PortalErrorCode.MISS_PAYLOAD_PARAM_PAYMENT_ID);
		PortalExceptionUtils.throwIfNullOrEmptyString(serviceId, PortalErrorCode.MISS_PARAM_SERVICE_ID);
		PortalExceptionUtils.throwIfNull(amount, PortalErrorCode.MISS_PARAM_AMOUNT);
		PortalExceptionUtils.throwIfTrue(chargeItems.size() == 0 , PortalErrorCode.MISS_PAYLOAD_PARAM_CHARGE_ITEMS);
		chargeItems.forEach(c->c.validateCreatePayment());
	}
	
	public static ConsumeCreditDto create(String paymentId, String serviceId, Integer amount, String description, List<ChargingItemDto> chargeItems) {
		ConsumeCreditDto dto = new ConsumeCreditDto();
		dto.paymentId = paymentId;
		dto.serviceId = serviceId;
		dto.amount = amount;
		dto.description = description;
		dto.chargeItems = chargeItems;
		dto.action = "Real time payment";
		return dto;
	}
}
