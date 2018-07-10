package com.kooppi.nttca.portal.wallet.domain;

public enum AdjustmentType {

	ONE_OFF("One Off"),
	COMPENSATION("Compensation"),
	MONTHLY_RECHARGE("Monthly Recharge");
	
	private String value;
	
	AdjustmentType(String value){
		this.value = value;
	}
	
	public String  value(){
		return value;
	}
}
