package com.kooppi.nttca.portal.exception.domain;

public enum PortalErrorCode {

	//permission
	NO_WALLET_VIEW_PERMISSION("WALLET-ERROR-9001","The request user id does not have permission to view the wallet.",400),
	NO_WALLET_CREATE_PERMISSION("WALLET-ERROR-9002","The request user id does not have permission to create the wallet.",400),
	NO_WALLET_UPDATE_PERMISSION("WALLET-ERROR-9003","The request user id does not have permission to update the wallet.",400),
	NO_WALLET_TERMINATE_PERMISSION("WALLET-ERROR-9004","The request user id does not have permission to terminate the wallet.",400),
	NO_USER_WALLET_VIEW_PERMISSION("WALLET-ERROR-9005","The request user id does not have permission to view the target user's wallets.",400),
	NO_CREDIT_RESERVE_PERMISSION("WALLET-ERROR-9006","The request user id does not have permission to reserve credit.",400),
	NO_CREDIT_COMSUME_PERMISSION("WALLET-ERROR-9007","The request user id does not have permission to consume credit.",400),
	NO_RESERVED_CREDIT_CANCEL_PERMISSION("WALLET-ERROR-3003","The request user id does not have permission to cancel reserved credit.",400),
	NO_WALLET_CREATE_ADJUSTMENT_PERMISSION("WALLET-ERROR-9008","The request user id does not have permission to create adjustment for the wallet.",400),
	NO_TRANSACTION_VIEW_PERMISSION("WALLET-ERROR-9009","The request user id does not have permission to view the transaction.",400),
	NO_TRANSACTION_RESERVE_PERMISSION("WALLET-ERROR-9010","The request user id does not have permission to reserve the transaction.",400),
	NO_WALLET_VIEW_ADJUSTMENT_PERMISSION("WALLET-ERROR-9011","The request user id does not have permission to view the adjustment.",400),
	NO_WALLET_UPDATE_ADJUSTMENT_PERMISSION("WALLET-ERROR-9014","The request user id does not have permission to update the adjustment.",400),
	NO_WALLET_DISABLE_PERMISSION("WALLET-ERROR-9012","The request user id does not have permission to disable the wallet.",400),
	NO_WALLET_ENABLE_PERMISSION("WALLET-ERROR-9013","The request user id does not have permission to enable the wallet.",400),
	NO_ADJUSTMWNT_SEARCH_PERMISSION("WALLET-ERROR-3006","The request user id does not have permission to search the adjustment.",400),
	NO_WALLET_ACTIVATE_PERMISSION("WALLET-ERROR-3023","The request user id does not have permission to activate the wallet.",400),
	NO_TRANSACTION_RESERVATION_VIEW_PERMISSION("WALLET-ERROR-9014","The request user id does not have permission to view the transaction reservation.",400),

	
	AUTHORIZATION_NOT_MATCHED("PORTAL-ERROR-0004", "The Authorization credential does not match our record", 401),

	//default
    INTERNAL_SERVER_ERROR("PORTAL-ERROR-0001","An unknown error has occurred when processing your request.",500),
	//system 
	MISS_HEADER_PARAM_APP_ID("PORTAL-ERROR-0002","The header parameter 'X-App-ID' is missing.",400),
	MISS_HEADER_PARAM_Request_ID("PORTAL-ERROR-0003","The header parameter 'X-Request-ID' is missing.",400),
	MISS_HEADER_PARAM_AUTHORIZATION("PORTAL-ERROR-0010","The header parameter 'Authorization' is missing.",400),
	//credit pool error code
	NO_MAIN_CREDIT_POOL("WALLET-ERROR-0004","No main credit pool found of the request wallet.",500),
	//pms error
	PMS_ERROR_ORGANIZATION_NOT_FOUND("PORTAL-ERROR-0005","Organization not found in Profile Management System.",500),
	MQ_ERROR_ERROR_ON_CONNECTION("PORTAL-ERROR-0006","Error on connecting to Rabbit MQ",500),	
	//user error code
	INVALID_REQUEST_USER_ID("PORTAL-ERROR-0007","The request user id is invalid",400),
	INACTIVE_REQUEST_USER_ID("PORTAL-ERROR-0008","User is not active or activated",400),
	//organization error code
	INVALID_REQUEST_ORG_ID("PORTAL-ERROR-1003","The request organization id is invalid",400),
	//service error code
	INVALID_REQUEST_SERVICE_ID("PORTAL-ERROR-1004","The request service id is invalid.",400),
	//authorization error code
	BASIC_AUTHORIZATION_FAILED("PORTAL-ERROR-0009","API basic authorization failed.", 400),
	
	//customer wallet error code
	INVALID_USER_ID("WALLET-ERROR-1005","The path parameter 'userId' is invalid.",400),
	INVALID_ORGANIZATION_USER("WALLET-ERROR-1006","This api support Customer user only",400),
	INVALID_PAYLOAD_DATA("WALLET-ERROR-1007","We found no payload data, please check.",400),
	//wallet error code
	MISS_PARAM_WALLET_ID("WALLET-ERROR-1008","The path parameter 'wallet id' is missing.",400),
	INVALID_WALLET_ID("WALLET-ERROR-1009","The wallet id is invalid.",400),
	INVALID_WALLET_RECHARGEDAY("WALLET-ERROR-1010","The wallet recharge day is invalid. The recharge day should be within 1 to 28.",400),
	INVALID_WALLET_ALERTRECEIVERS("WALLET-ERROR-1011","The email format of alert receivers is invalid.",400),
	INVALID_WALLET_BCCS("WALLET-ERROR-1012","The email format of alert bcc is invalid.",400),
	INVALID_WALLET_PERMITEDUSERS("WALLET-ERROR-1013","Some permitted users do not exist.",400),
	WALLET_IS_INACTIVE("WALLET-ERROR-1014","wallet is not active.",400),
	WALLET_IS_ACTIVE("WALLET-ERROR-1015","The wallet is active.",400),
	MISS_PARAM_USER_ID("WALLET-ERROR-3020","The path parameter 'userId' is missing.", 400),
	WALLET_IS_ACTIVATED("WALLET-ERROR-3022","The wallet is already activated.",400),
	WALLET_IS_NOT_DISABLE("WALLET-ERROR-1060","Wallet should be disable before it is deleted",400),
	
	MISS_PAYLOAD_PARAM_NAME("WALLET-ERROR-1017","The payload parameter 'name' is missing.",400),
	MISS_PAYLOAD_PARAM_ORGANIZATIONID("WALLET-ERROR-1018","The payload parameter 'organizationId' is missing.",400),
	MISS_PAYLOAD_PARAM_MAX_IDLE_PERIOD("WALLET-ERROR-1019","The payload parameter 'maxIdlePeriod' is missing.",400),
	MISS_PAYLOAD_PARAM_IDLE_UNIT("WALLET-ERROR-1020","The payload parameter 'idleUnit' is missing.",400),
	MISS_PAYLOAD_PARAM_CREDIT_BUFFER("WALLET-ERROR-1021","The payload parameter 'creditBuffer' is missing.",400),
	MISS_PAYLOAD_PARAM_MONTHLY_RECHARGE("WALLET-ERROR-1022","The payload parameter 'monthlyRecharge' is missing.",400),
	MISS_PAYLOAD_PARAM_RECHARGE_DAY("WALLET-ERROR-1023","The payload parameter 'rechargeDay' is missing.",400),
	MISS_PAYLOAD_PARAM_ALERT_THRESHOLD("WALLET-ERROR-1024","The payload parameter 'alertThreshold' is missing.",400),
	MISS_PAYLOAD_PARAM_ALERT_RECEIVERS("WALLET-ERROR-1025","The payload parameter 'alertReceivers' is missing.",400),
	MISS_PAYLOAD_PARAM_ALERT_BCCS("WALLET-ERROR-1026","The payload parameter 'alertBccs' is missing.",400),
	MISS_PAYLOAD_PARAM_IS_REQUIRED_PERMISSION("WALLET-ERROR-1027","The payload parameter 'isRequiredPermission' is missing.",400),
	MISS_PAYLOAD_PARAM_REMARK("WALLET-ERROR-1028","The payload parameter 'remark' is missing.",400),
	MISS_PAYLOAD_PARAM_STATUS("WALLET-ERROR-1029", "The payload parameter 'status' is missing.", 400),
	MISS_PAYLOAD_PARAM_IS_ONE_TIME("WALLET-ERROR-1030", "The payload parameter 'isOneTime' is missing.", 400),
	EXPIRED_DATE_IS_INVALID("WALLET-ERROR-3002","The expired date exceed the limitation.",400),
	NEGATIVE_MAX_IDLE_PRERIOD("WALLET-ERROR-3024","The payload parameter 'maxIdlePeriod' is negative number.",400),
	NEGATIVE_CREDIT_BUFFER("WALLET-ERROR-3025","The payload parameter 'creditBuffer' is negative number.",400),
	NEGATIVE_ALERT_THRESHOLD("WALLET-ERROR-3026","The payload parameter 'alertThreshold' is negative number.",400),
	SECOND_UTILIZATION_ALERT_LARGER_THAN_FIRST_ALERT("WALLET-ERROR-3027", "The second utilization alert must be smaller than the first utilization alert value.", 400),
	SECOND_FORDEIT_ALERT_LARGER_THAN_FIRST_ALERT("WALLET-ERROR-3027", "The second forfeit alert day must be smaller than the first forfeit alert day.", 400),
	INVALID_CREDIT_BUFFER("WALLET-ERROR-3028","'creditBuffer' is invalid.",400),
	//main wallet error code
	TERMINATE_MAIN_WALLET("WALLET-ERROR-3018","Main wallet cannot be terminated.",400),
	INVALID_MAIN_WALLET("WALLET-ERROR-3019","Cannot find main wallet for the requested organization",400),
	
	//credit error code
	NO_ENOUGH_AVAILABLE_CREDIT("WALLET-ERROR-1029","wallet do not have enough credit.",400),
	
	//transaction reservation error code
	MISS_PATH_PARAM_TRANSACTION_ID("WALLET-ERROR-1056","The path parameter 'transactionId' is missing",400),
	MISS_PAYLOAD_PARAM_SERVICEID("WALLET-ERROR-1030","The payload parameter 'serviceId' is missing.",400),
	MISS_PAYLOAD_PARAM_ITEM("WALLET-ERROR-1031","The payload parameter 'item' is missing.",400),
	MISS_PAYLOAD_PARAM_AMOUNT("WALLET-ERROR-1032","The payload parameter 'amount' is missing.",400),
	MISS_PAYLOAD_PARAM_EXPIRED_DATE("WALLET-ERROR-1033","The payload parameter 'expiredDate' is missing.",400),
	MISS_PAYLOAD_PARAM_ACTION("WALLET-ERROR-1034","The payload parameter 'action' is missing.",400),
	MISS_PAYLOAD_PARAM_DESCRIPTION("WALLET-ERROR-1035","The payload parameter 'description' is missing.",400),
	MISS_PAYLOAD_PARAM_USERID("WALLET-ERROR-1036","The payload parameter 'userId' is missing.",400),
	MISS_PAYLOAD_PARAM_CURRENCY_AMOUNT("WALLET-ERROR-1037","The payload parameter 'currency amount' is missing.",400),
	MISS_PAYLOAD_PARAM_COMPANY_CODE("WALLET-ERROR-1038","The payload parameter 'companyCode' is missing.",400),
	RESERVATION_STATUS_IS_NOT_RESERVED("WALLET-ERROR-3001","The transaction reservation status is not reserved.",400),
	WALLET_DONT_HAVE_THE_TRANSACTION_RESERVATION("WALLET-ERROR-3004","Do not have the transaction reservation in the requested wallet.",400),
	MISS_PAYLOAD_PARAM_ITEM_TYPE("WALLET-ERROR-3008","The payload parameter 'itemType' is missing.",400),
	MISS_PAYLOAD_PARAM_PAYMENT_ID("WALLET-ERROR-3009","The payload parameter 'paymentId' is missing.",400),

	//adjustment error code
	MISS_PAYLOAD_PARAM_PAMOUNT("WALLET-ERROR-1037","The payload parameter 'amount' is missing.",400),
	MISS_PATH_PARAM_CONTRACT_ID("WALLET-ERROR-1100","The path parameter 'contractId' is missing",400),
	INVALID_CONTRACT_ID("WALLET-ERROR-1101","The contract id is invalid.",400),
	CONTRACT_IS_NOT_IN_USE("WALLET-ERROR-1102","The contract is not effective yet.",400),
	CONTRACT_WAS_CLOSED("WALLET-ERROR-1102","The contract was closed .",400),
	CONTRACT_WAS_CANCELLED("WALLET-ERROR-1102","The contract was cancelled .",400),
	
	MISS_PARAM_IS_SCHEDULED_ADJUSTMENT("WALLET-ERROR-1038","The payload parameter 'isScheduledAdjustment' is missing.",400),
	MISS_PARAM_SCHEDULED_DATE("WALLET-ERROR-1039","The payload parameter 'scheduledDate' is missing.",400),
	POSITIVE_ADJUSTMENT_IS_REQUIRED_FOR_SCHEDULED_TYPE("WALLET-ERROR-1040","The amount must be possitive for scheduled type adjustment.",400),
	MISS_PARAM_REF_NUMBER("WALLET-ERROR-1041","The payload parameter 'refNumber' is mandatory when the adjustment type is SERVICE_ORDER.",400),
	ADJUSTMENT_NOT_ACCEPT("WALLET-ERROR-1042","The adjustment is rejected because of not enough credits.",400),
	MISS_PARAM_CONTRACT_EFFECTIVE_DATE("WALLET-ERROR-1043","The payload parameter 'contractEffectiveDate' is missing.",400),
	MISS_PARAM_START_MONTH("WALLET-ERROR-3027","The payload parameter 'startMonth' is mandatory when the adjustment type is MONTHLY_RECHARGE.",400),
	MISS_PARAM_END_MONTH("WALLET-ERROR-3010","The payload parameter 'endMonth' is mandatory when the adjustment type is MONTHLY_RECHARGE.",400),
	MISS_PARAM_TRANSACTION_DAY("WALLET-ERROR-3011","The payload parameter 'transactionDay' is mandatory when the adjustment type is MONTHLY_RECHARGE.",400),
	MISS_PARAM_ADJUSTMENT_TYPE("WALLET-ERROR-3012","The payload parameter 'adjustmentType' is missing.",400),
	MISS_PARAM_TERMINATED_REASON("WALLET-ERROR-3013","The payload parameter 'terminatedReason' is mandatory when the endMonth is provided.",400),
	INVALID_TRANSACTION_DAY("WALLET-ERROR-3014","The transaction day is invalid. It should be within 1 to 28.",400),
	INVALID_SCHEDULED_DATE("WALLET-ERROR-3015","The scheduled date is invalid. It should be no earlier than today.",400),
	INVALID_RECHARGE_DATE("WALLET-ERROR-3016","The next recharge date is invalid. It should be no earlier than today.",400),
	INVALID_LAST_RECHARGE_DATE("WALLET-ERROR-3017","The last recharge date is invalid. It should be no earlier than the first recharge date.",400),
	INVALID_CONTRACT_EFFECTIVE_DATE("WALLET-ERROR-3018","The contract effective date is invalid. It should be no earlier than today.",400),
	INVALID_CONTRACT_TERMINATION_DATE("WALLET-ERROR-3019", "The contract termination date is invalid. It should be no earlier than the contract effective date.", 400),
	MISS_IS_TERMINATE_ON_END_DATE("WALLET-ERROR-3020", "The payload parameter 'isTerminateOnEndDate' is missing", 400),
	UNABLE_TO_FIND_VALID_CONTRACT("WALLET-ERROR-3021", "Can not find valid contract to consume credit", 400),
	MISS_PRODUCT_ITEM("WALLET-ERROR-3022", "The product list is empty", 400),
	MISS_BU("WALLET-ERROR-3023", "The BU list is empty", 400),
	MISS_PARAM_IS_ALL_BU("WALLET-ERROR-3024", "The payload parameter 'is_all_bu' is missing", 400),
	MISS_PARAM_IS_ALL_PRODUCT("WALLET-ERROR-3025", "The payload parameter 'is_all_product' is missing", 400),
	COMPENSATION_CONTRACT_EDIT_NOT_ALLOWED_FIELDS("WALLET-ERROR-1102", "On or after Contract Effective Date, only allow to edit Credit Expiry Date and Description", 400),
	
	UNSUPPORT_CONTRACT_TYPE("WALLET-ERROR-1103", "This contract type is not supported", 400),
	//transaction error code
	MISS_PARAM_TRANSACTION_ID("WALLET-ERROR-1043","The path parameter 'transaction id' is missing.",400),
	INVALID_TRANSACTION_ID("WALLET-ERROR-1044","The transaction id is invalid.",400),
	TRASACTION_NOT_COMMIT("WALLET-ERROR-1045","The transaction state is not commit",400),
	TRASACTION_NOT_SUCCESS("WALLET-ERROR-1046","The transaction status is not success",400),
	MISS_PARAM_SERVICE_ORDER("WALLET-ERROR-1047","The parameter 'serviceOder' is missing.",400),
	MISS_PARAM_SERVICE_ID("WALLET-ERROR-1048","The parameter 'serviceId' is missing.",400),
	MISS_PARAM_REQUEST_ID("WALLET-ERROR-1049","The parameter 'requestId' is missing.",400),
	MISS_PARAM_ITEM("WALLET-ERROR-1050","The parameter 'item' is missing.",400),
	MISS_PARAM_BALANCE("WALLET-ERROR-1051","The parameter 'balance' is missing.",400),
	MISS_PARAM_ACTION("WALLET-ERROR-1052","The parameter 'action' is missing.",400),
	MISS_PARAM_DESCRIPTION("WALLET-ERROR-1053","The parameter 'description' is missing.",400),
	MISS_PARAM_AMOUNT("WALLET-ERROR-3005","The parameter 'amount' is missing.",400),
	MISS_PARAM_CURRENCY_AMOUNT("WALLET-ERROR-3006","The parameter 'currency amount' is missing.",400),
	INVALID_REQUEST_USER_PORTAL_ROLE("WALLET-ERROR-1054","Permitted users should be end user.",400),
	INVALID_REQUEST_USER_ORGANIZATION("WALLET-ERROR-1055","Permitted users should be in the organization.",400),
	TRANSACTION_IS_NOT_CANCELABLE("WALLET-ERROR-1068","A cancelable transaction must be commited,successful and not rollbacked before.",400),
	MISS_PAYLOAD_PARAM_SERVICE_ID("WALLET-ERROR-1057","The payload parameter 'serviceId' is missing.",400),
	ADJUSTMENT_SHOULD_BE_SCHEDULED("WALLET-ERROR-1058","The saved adjustment must be scheduled adjustment.",400),
	ADJUSTMENT_SHOULD_BE_ON_SCHEDULE("WALLET-ERROR-1059","The saved adjustment must be on schedule.",400),
	PAYMENT_MISSING_TRANSACTION("WALLET-ERROR-1060","Unable to find the transaction record of this payment.",400),
	
	MISS_PARAM_ORGNIAZATION_ID("WALLET-ERROR-2001","The parameter 'orgniazationId' is missing.",400),
	MISS_PARAM_ITEM_TYPE("WALLET-ERROR-3007","The parameter 'itemType' is missing.",400),

	CHILD_ADJUSTMENT_ALREADY_CREATED("WALLET-ERROR-1061","The child adjustment already generated in this month.",500),
	METHOD_ONLY_FOR_ONEOFF_ADJUSTMENT("WALLET-ERROR-1062","This method only applicable for one-off adjustment.",500),
	METHOD_ONLY_FOR_RECURRSIVE_ADJUSTMENT("WALLET-ERROR-1069","This method only applicable for recurrsive adjustment.",500),
	MISS_PAYLOAD_PARAM_PRODUCT_NAME("WALLET-ERROR-1063","The payload parameter 'productName' is missing.",500),
	MISS_PAYLOAD_PARAM_PRODUCT_UNIT("WALLET-ERROR-1064","The payload parameter 'productUnit' is missing.",500),
	MISS_PAYLOAD_PARAM_QUANTITY("WALLET-ERROR-1065","The payload parameter 'quantity' is missing.",500),
	MISS_PAYLOAD_PARAM_CHARGE_ITEMS("WALLET-ERROR-1066","The payload parameter 'chargeItems' is missing.",500),
	MISS_PAYLOAD_PARAM_PRODUCT_ID("WALLET-ERROR-1067","The payload parameter 'productId' is missing.",500),
	
	PAYMENT_NOT_READY("PAYMENT-ERROR-0001","Internal System Error: The payment is not ready.",500),

	PAYMENT_NO_PAYMENT_VIEW_PERMISSION("PAYMENT-ERROR-9001","The request user id does not have permission to view the payment.",400),
	PAYMENT_NO_PAYMENT_CREATE_PERMISSION("PAYMENT-ERROR-9002","The request user id does not have permission to create the payment.",400),
	PAYMENT_NO_PAYMENT_CONFIRM_PERMISSION("PAYMENT-ERROR-9003","The request user id does not have permission to confirm the payment.",400),
	PAYMENT_NO_PAYMENT_CANCEL_PERMISSION("PAYMENT-ERROR-9004","The request user id does not have permission to cancel the payment reservation.",400),
	PAYMENT_NO_PAYMENT_REFUND_PERMISSION("PAYMENT-ERROR-9005","The request user id does not have permission to refund the payment.",400),

	PAYMENT_MISS_PATH_PARAM_PAYMENT_ID("PAYMENT-ERROR-1001","The path parameter 'payment id' is missing.",400),
	PAYMENT_INVALID_PAYMENT_ID("PAYMENT-ERROR-1002","The paymentId is invalid.",400),

	PAYMENT_MISS_PAYLOAD_PARAM_SERVICE_ID("PAYMENT-ERROR-1003","The payload parameter 'serviceId' is missing.",400),
	PAYMENT_MISS_PAYLOAD_PARAM_WALLET_ID("PAYMENT-ERROR-1004","The payload parameter 'walletId' is missing.",400),
	PAYMENT_MISS_PAYLOAD_PARAM_CHARGE_TYPE("PAYMENT-ERROR-1005","The payload parameter 'chargeType' is missing.",400),
	PAYMENT_MISS_PAYLOAD_PARAM_CURRENCY("PAYMENT-ERROR-1006","The payload parameter 'currency' is missing.",400),
	PAYMENT_MISS_PAYLOAD_PARAM_ITEM_NAME("PAYMENT-ERROR-1007","The payload parameter 'itemName' is missing.",400),
	PAYMENT_MISS_PAYLOAD_PARAM_ITEM_TYPE("PAYMENT-ERROR-1008","The payload parameter 'itemType' is missing.",400),
	PAYMENT_MISS_PAYLOAD_PARAM_ACTION("PAYMENT-ERROR-1009","The payload parameter 'action' is missing.",400),
	PAYMENT_MISS_PAYLOAD_PARAM_DESCRIPTION("PAYMENT-ERROR-1010","The payload parameter 'description' is missing.",400),
	PAYMENT_MISS_PAYLOAD_PARAM_EXPIRED_DATE("PAYMENT-ERROR-1011","The payload parameter 'expiredDate' is missing.",400),
	PAYMENT_MISS_PAYLOAD_PARAM_CHARGING_ITEM("PAYMENT-ERROR-1012","The payload parameter 'chargingItems' is missing.",400),

	PAYMENT_MISS_PAYLOAD_PARAM_PRODUCT_ID("PAYMENT-ERROR-1013","The payload parameter 'productId' is missing.",400),
	PAYMENT_MISS_PAYLOAD_PARAM_UNIT_OF_MEASURE("PAYMENT-ERROR-1014","The payload parameter 'unitOfMeasure' is missing.",400),
	PAYMENT_MISS_PAYLOAD_PARAM_QUANTITY("PAYMENT-ERROR-1015","The payload parameter 'quantity' is missing.",400),
	PAYMENT_MISS_PAYLOAD_PARAM_PRICE_SOURCE("PAYMENT-ERROR-1016","The payload parameter 'priceSource' is missing.",400),
	PAYMENT_MISS_PAYLOAD_PARAM_TOTAL_AMOUNT("PAYMENT-ERROR-1017","The payload parameter 'totalAmount' is missing.",400),
	PAYMENT_ACCEPT_PORTAL_USER_ONLY("PAYMENT-ERROR-1018","Please provide a real user account in 'X-User-ID' in header parameters.",400),
//	PAYMENT_INVALID_WALLET_ID("WALLET-ERROR-1009","The wallet id is invalid.",400),
	PAYMENT_INVALID_WALLET_ID("PAYMENT-ERROR-1019","The wallet id is invalid.",400),
	PAYMENT_EXPIRED_DATE_LESS_THAN_CURRENT_DATE("PAYMENT-ERROR-1020","The expired date is less than current date.",400),
	PAYMENT_EXPIRED_DATE_LARGER_THAN_MAX_DATE("PAYMENT-ERROR-1021","The expired date is larger than max. reservation period.",400),
	PAYMENT_UNKNOW_PRICE_SOIURCE("PAYMENT-ERROR-1022","Unknow price source,please contact the system administrator.",400),
	PAYMENT_INVALID_APP_ID("PAYMENT-ERROR-1023","The target payment does not belong to the client service.",400),
	PAYMENT_NOT_RESERVED_STATUS("PAYMENT-ERROR-1024","The target payment status is not 'RESERVED'.",400),
	PAYMENT_RESERVATION_EXPIRED("PAYMENT-ERROR-1025","The target payment is expired.",400),
	PAYMENT_MISS_PAYLOAD_PARAM_REASON("PAYMENT-ERROR-1026","The payload parameter 'reason' is missing.",400),
	PAYMENT_NOT_CONFIRMED_STATUS("PAYMENT-ERROR-1027","The target payment status is not 'CONFIRMED'.",400),
	PAYMENT_MISS_PAYLOAD_PARAM_PARTNO("PAYMENT-ERROR-1028", "The payload parameter 'partNo' is missing.",400),
	PAYMENT_MISS_PATH_PARAM_CHARGE_AMOUNT("PAYMENT-ERROR-1029", "The path parameter 'charge amount' is missing.", 400),
	
	//Mysql exception
	MYSQL_EXCEPTION("PAYMENT-ERROR-4444", "SQL Exception", 400),

	//Price book error
	MISS_PARAM_PRICE_BOOK_ID("PRICE-BOOK-ERROR-0001", "The paramter 'priceBookId' is missing.", 400),
	INVALID_PRICE_BOOK_ID("PRICE-BOOK-ERROR-0002", "The price book id is invalid", 400),
	INVALID_BU_MASTER_NAME("PRICE-BOOK-ERROR-0003", "The bu master name is invalid", 400),
	INVALID_TERMS_AND_CONDITIONS_TEMPLATE_ID("PRICE-BOOK-ERROR-0004", "The terms and conditions template id.", 400),
	MISS_PARAM_PART_NO("PRICE-BOOK-ERROR-0005", "The parameter 'partNo' is missing.", 400),
	INVALID_PART_NO("PRICE-BOOK-ERROR-0006", "The part number is invalid", 400),
	MISS_PARAM_BU_NAME("PRICE-BOOK-ERROR-0007", "The parameter 'buName' is missing.", 400),
	INVALID_BU_NAME("PRICE-BOOK-ERROR-0008", "The bu name is invalid", 400),
	INVALID_PART_NUMBER("PRICE-BOOK-ERROR-0009", "The price book part number is invalid", 400),
	INVALID_UNIT_PRICE("PRICE-BOOK-ERROR-0010", "Charge item unit price not equal to price book", 400),

	//Optimistic lock retry expection
	INVALID_PARAM_TIMES("OPTICMISTIC-LOCK-ERROR-0001", "@Retry{times} should be greater than 0!", 400),
	INVALID_PARAM_RETRY_ON("OPTICMISTIC-LOCK-ERROR-0002", "@Retry{on} should have at least one Throwable!", 400),
	
	UNKNOW_ERROR_PAYMENT_HAS_NO_STATUS("SYSTEM-ERROR-0003","The target payment does not have stauts.",500),
	UNKNOW_ERROR_ON_WALLET_API("SYSTEM-ERROR-0002","Unknow error found during invoke wallet api.",500),
	UNKNOW_ERROR_CODE("SYSTEM-ERROR-0001","Unknow error.",500),
	
	//Generate report error
	MISS_PARAM_YEAR("REPORT-ERROR-0001", "The paramter 'year' is missing.", 400),
	MISS_PARAM_MONTH("REPORT-ERROR-0002", "The paramter 'month' is missing.", 400),
	
	//TnC error code	
	MISS_PARAM_NAME("TnC-ERROR-0001","The payload parameter 'name' is missing.",400),
	MISS_PARAM_IS_DEFAULT("TnC-ERROR-0002","The payload parameter 'isDefault' is missing.",400),
	MISS_PARAM_IS_VISIBLE("TnC-ERROR-0003","The payload parameter 'isVisible' is missing.",400),
	MISS_PARAM_FILE_NAME("TnC-ERROR-0004","The payload parameter 'fileName' is missing.",400),
	MISS_PARAM_FILE_DATA("TnC-ERROR-0005","The payload parameter 'attachmentData' is missing.",400),
	MISS_PARAM_FILE_SIZE("TnC-ERROR-0006","The payload parameter 'size' is missing.",400),
	MISS_PARAM_TNC_ID("TnC-ERROR-0007","The payload parameter 'tncId' is missing.",400),
	MISS_PARAM_TNC_TO_PRODUCT_UID("TnC-ERROR-0008","The payload parameter 'mapUid' is missing.",400),
	
	FILE_TYPE_NOT_ALLOWED("TnC-ERROR-0008","The file type only support '.doc' and '.pdf'.",400),
	FILE_NAME_DEPLICATE("TnC-ERROR-0009","The file name already exist.",400),
	UNABLE_TO_FIND_TNC_RECORD("TnC-ERROR-0010","The TNC record can not be found in db",400),
	UNABLE_TO_FIND_TNC_TO_PRICEBOOK_RECORD("TnC-ERROR-0011","The TNC To Pricebook record can not be found in db",400),
	UNABLE_TO_FIND_TNC_IN_FILE_SYSTEM("TnC-ERROR-0012","The TNC Tmeplate can not be found in file system",400),
	
	//TnC to price book error code	
	MISS_PARAM_IS_DEFAULT_BU("TnC-ERROR-0011","The payload parameter 'isDefaultBu' is missing.",400),
	MISS_PARAM_IS_ALL_CUSTOMER("TnC-ERROR-0012","The payload parameter 'isAllCustomer' is missing.",400),
	TNC_TO_PRICEBOOK_DUPLICATE("TnC-ERROR-0013","The T&C Mapping already exists.",400),
	INVALID_TNC_TO_PRODUCT_UID("TnC-BOOK-ERROR-0014", "The 'mapUid' is invalid", 400),
	TNC_TO_PRICEBOOK_DEFAULT_DUPLICATE("TnC-ERROR-0015","The Default T&C Mapping already exists.",400),
	
	//New Portal
	UNSUCCESSFUL_NEW_PORTAL_API_CALL("NEW-PORTAL_ERROR-0001","New portal return error.",400),
	MISSING_LOGIN_ID("NEW-PORTAL_ERROR-0002","The 'loginId' is missing.",400),
	INVALID_CDS_COMPANY_ID("NEW-PORTAL_ERROR-0003","The 'organizationId' is invalid.",400),
	;
	
	private String responseCode = "";
	private String responseMsg = "";
	private Integer httpResponseCode = 400;


	private PortalErrorCode(String responseCode,String responseMsg,  Integer httpResponseCode){
		this.responseMsg = responseMsg;
		this.httpResponseCode=httpResponseCode;
		this.responseCode = responseCode;
	}

	public String getResponseCode(){
		return responseCode;
	}
	
	public String getResponseMsg() {
		return responseMsg;
	}

	public Integer getHttpResponseCode() {
		return httpResponseCode;
	}
	
	public static PortalErrorCode fromResponseCode(String responseCode){
		for (PortalErrorCode errorCode : PortalErrorCode.values()) {
			if (errorCode.responseCode.equals(responseCode)) {
				return errorCode;
			}
		}
		return UNKNOW_ERROR_CODE;
	}
	
	
}
