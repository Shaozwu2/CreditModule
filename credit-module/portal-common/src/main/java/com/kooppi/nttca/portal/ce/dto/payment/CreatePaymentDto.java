package com.kooppi.nttca.portal.ce.dto.payment;

import java.time.LocalDateTime;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.google.common.collect.Lists;
import com.kooppi.nttca.portal.common.utils.PortalXmlDateTimeAdapter;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;

public class CreatePaymentDto {

	@XmlElement(name = "walletId")
	private String walletId;
	
	@XmlElement(name = "description")
	private String description;
	
	@XmlElement(name = "expiredDate")
	@XmlJavaTypeAdapter(value = PortalXmlDateTimeAdapter.class)
	private LocalDateTime expiredDate;
	
	@XmlElement(name = "chargingItems")
	private List<ChargingItemDto> chargingItems = Lists.newArrayList();
	
	public CreatePaymentDto() {}
	
	public void validateCreatePayment() {
		// validate mandatory field
		PortalExceptionUtils.throwIfNullOrEmptyString(walletId, PortalErrorCode.PAYMENT_MISS_PAYLOAD_PARAM_WALLET_ID);
		PortalExceptionUtils.throwIfTrue(chargingItems.size() == 0, PortalErrorCode.MISS_PAYLOAD_PARAM_CHARGE_ITEMS);
		chargingItems.forEach(c -> c.validateCreatePayment());
	}

	public String getWalletId() {
		return walletId;
	}

	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(LocalDateTime expiredDate) {
		this.expiredDate = expiredDate;
	}

	public List<ChargingItemDto> getChargingItems() {
		return chargingItems;
	}

	public void setChargingItems(List<ChargingItemDto> chargingItems) {
		this.chargingItems = chargingItems;
	}
	
}
