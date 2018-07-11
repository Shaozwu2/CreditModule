package com.kooppi.nttca.ce.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.kooppi.nttca.portal.ce.dto.payment.CustomDataDto;
import com.kooppi.nttca.portal.common.domain.Modifiable;

@Entity
@Table(name = "cm_custom_data")
public class CustomData extends Modifiable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2170374259368977803L;

	@Column(name = "payment_id")
	private String paymentId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "value")
	private String value;

	public String getPaymentId() {
		return paymentId;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public CustomDataDto toCustomDataDto(){
		return CustomDataDto.create(this.getName(), this.getValue());
	}
	
	public static CustomData create(CustomDataDto dto){
		CustomData data = new CustomData();
		data.name = dto.getName();
		data.value = dto.getValue();
		return data;
	}
}
