package com.kooppi.nttca.portal.common.auditlog;

public enum AuditingActionType {

	DEFAULT(""),
	
	//wallet
	UPDATE_WALLET("Update Wallet"),
	CREATE_WALLET("Create Wallet"),
	
	//contract
	CREATE_CONTRACT("Create Contract"),
	UPDATE_CONTRACT("Update Contract"),
	DELETE_CONTRACT("Delete Contract"),
	FORFEIT_CONTRACT("Forfeit Contract"),
	CANCEL_FORFEIT_CONTRACT("Cancel Forfeit Contract"),

	//pricebook
	UPDATE_PRICE_BOOK("Update PriceBook"),

	//transaction
	CREATE_REALTIME_TRANSACTION("Create Real Time Transaction"),
	CONFIRM_TRANSACTION("Confirm Transaction"),
	CANCEL_TRANSACTION("Cancel Transaction"),
	
	//TnC
	CREATE_TNC("Create TnC"),
	UPDATE_TNC("Update TnC"),
	DELETE_TNC("Delete TnC"),
	DOWNLOAD_TNC("Download TnC"),
	DOWNLOAD_TNC_REPORT("Download TnC Report"),
	
	//TnC to Product
	CREATE_TNC_TO_PRODUCT("Create TnC to Product"),
	UPDATE_TNC_TO_PRODUCT("Update TnC to Product"),
	DELETE_TNC_TO_PRODUCT("Delete TnC to Product"),
	
	//finance
	DOWNLOAD_FINANCE_TNC("Download Finance Report"),
	;
	
	private String name;
	
	private AuditingActionType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
