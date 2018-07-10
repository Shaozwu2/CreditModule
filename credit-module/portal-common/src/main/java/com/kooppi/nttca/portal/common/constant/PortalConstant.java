package com.kooppi.nttca.portal.common.constant;

import java.time.format.DateTimeFormatter;

public class PortalConstant {

	public static final String PORTAL_API_SUCCESS_CODE = "0000";
	public static final String PORTAL_API_UCCESS_MESSAGE = "SUCCESS";
	public static final String SUPER_ADMIN = "Super Admin";
	public static final String ENTERPRISE_ADMIN = "Enterprise Admin";
	public static final String RESELLER_ADMIN = "Reseller Admin";
	public static final String COMPANY_ADMIN = "Company Admin";
	public static final String END_USER = "End User";
//	public static final String PORTAL_TIME_ZONE = "GMT";
	public static final DateTimeFormatter PORTAL_DATE_TIME_FORMATER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
	public static final DateTimeFormatter PORTAL_DATE_FORMATER = DateTimeFormatter.ISO_DATE;
	public static final String EMAIL_SEPARATOR = ";";

	public static final String PORTAL_HEADER_REQUEST_ID = "X-Request-ID";
	public static final String PORTAL_HEADER_APP_ID = "X-App-ID";
	public static final String PORTAL_HEADER_USER_ID = "X-User-ID";
	public static final String PORTAL_HEADER_AUTHORIZATION = "Authorization";

	public static final String PORTAL_MQ_APP_ID = "X-App-ID";
	public static final String PORTAL_MQ_REQUEST_ID = "X-Request-ID";
	public static final String PORTAL_MQ_SERVICE = "service";
	public static final String PORTAL_MQ_API = "api";
	public static final String PORTAL_MQ_VERSION = "version";

	
	public static final String WALLET_APP_ID = "wallet";

	public static final String PORTAL_RESPONSE_HEADER_REQUEST_ID = "requestId";
	public static final String PORTAL_RESPONSE_HEADER_RESPONSE_CODE = "responseCode";
	public static final String PORTAL_RESPONSE_HEADER_RESPONSE_MESSAGE = "responseMsg";
	public static final String PORTAL_RESPONSE_HEADER_RESPONSE_CONTENT = "responseContent";


}
