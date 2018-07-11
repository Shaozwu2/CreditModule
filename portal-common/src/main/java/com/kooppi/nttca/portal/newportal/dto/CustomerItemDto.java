package com.kooppi.nttca.portal.newportal.dto;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data public class CustomerItemDto {
	@SerializedName("cdsCompanyId")
	private String customerId;
	@SerializedName("name")
	private String customerName;
	
}
