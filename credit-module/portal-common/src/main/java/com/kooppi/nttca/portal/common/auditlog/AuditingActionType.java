package com.kooppi.nttca.portal.common.auditlog;

public enum AuditingActionType {

	DEFAULT(""),
	
	//wallet
	UPDATE_WALLET("Update wallet"),
	CREATE_WALLET("Create wallet"),
	ACTIVATE_WALLET("Activate wallet"),
	TERMINATE_WALLET("Terminate wallet"),
	DISABLE_WALLET("Disable wallet"),
	ENABLE_WALLET("Enable wallet"),
	CONSUME_CREDIT_DIRECTLY("Consume credit directly"),
	
	//contract
	CREATE_CONTRACT("Create contract"),
	UPDATE_CONTRACT("Update contract"),
	FORFEIT_CONTRACT("Forfeit contract"),
	CANCEL_FORFEIT_CONTRACT("Cancel forfeit contract"),
	DELETE_CONTRACT("Delete contract"),

	//pricebook
	UPDATE_PRICE_BOOK("Update price book"),

	//transaction
	CREATE_REALTIME_TRANSACTION("create real time transaction"),
	
	//TnC
	CREATE_TNC("Create TnC"),
	UPDATE_TNC("Update TnC"),
	DELETE_TNC("Delete TnC"),
	;
	
	private String name;
	
	private AuditingActionType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
