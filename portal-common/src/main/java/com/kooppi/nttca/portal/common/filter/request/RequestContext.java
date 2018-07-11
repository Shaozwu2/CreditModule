package com.kooppi.nttca.portal.common.filter.request;

public interface RequestContext {

	public String getAppId();
	
	public String getRequestUserId();
	
	public String getRequestId();
	
	public String getRequestBody();
	
	public void setAppId(String appId);
	
	public void setRequestBody(String requestBody);
		
	public void init(String requestId, String username);
}
