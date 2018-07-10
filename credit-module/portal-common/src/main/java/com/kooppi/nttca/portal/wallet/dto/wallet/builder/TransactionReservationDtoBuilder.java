package com.kooppi.nttca.portal.wallet.dto.wallet.builder;

import java.time.LocalDateTime;

import com.kooppi.nttca.portal.wallet.dto.wallet.ChargeItemDto;
import com.kooppi.nttca.portal.wallet.dto.wallet.TransactionReservationDto;

public class TransactionReservationDtoBuilder {

	private TransactionReservationDto dto = new TransactionReservationDto();
	
	private TransactionReservationDtoBuilder(){
	}
	
	public static TransactionReservationDtoBuilder create(String paymentId){
		TransactionReservationDtoBuilder builder = new TransactionReservationDtoBuilder();
		builder.dto.setPaymentId(paymentId);
		return builder;
	}
	
	public TransactionReservationDtoBuilder serviceId(String serviceId){
		dto.setServiceId(serviceId);
		return this;
	}
	
	public TransactionReservationDtoBuilder itemName(String itemName){
		dto.setItem(itemName);
		return this;
	}
	
	public TransactionReservationDtoBuilder itemType(String itemType){
		this.dto.setItemType(itemType);
		return this;
	}
	
	public TransactionReservationDtoBuilder amount(Integer amount){
		dto.setAmount(amount);
		return this;
	}
	
	public TransactionReservationDtoBuilder expiredDate(LocalDateTime expiredDate){
		dto.setExpiredDate(expiredDate);
		return this;
	}
	
	public TransactionReservationDtoBuilder action(String action){
		dto.setAction(action);
		return this;
	}
	
	public TransactionReservationDtoBuilder chargeItem(ChargeItemDto chargeItemDto){
		dto.getChargingItems().add(chargeItemDto);
		return this;
	}

	//no need to set user id
//	public TransactionReservationDtoBuilder userId(String userId){
//		dto.setUserId(userId);
//		return this;
//	}
	
	public TransactionReservationDto build(){
		dto.validateCreateTransactionReservation();
		return dto;
	}
	
	/**
		PortalExceptionUtils.throwIfTrue(chargeItems.size() == 0 , PortalErrorCode.MISS_PAYLOAD_PARAM_CHARGE_ITEMS);
		chargeItems.forEach(c->c.validateCreateReservation());
	 */
}
