package com.kooppi.nttca.portal.common.filter.context;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.kooppi.nttca.portal.common.config.file.ConfigurationValue;
import com.kooppi.nttca.portal.common.filter.request.RequestContext;

@RequestScoped
public class RequestContextImpl implements RequestContext{

	@Inject
	@ConfigurationValue(property = "portal.app.user.id.default", defaultValue = "system")
	private String WALLET_HEADER_USER_ID_SYSTEM;
	
	private String requestId;
		
	private String appId;
	
	private boolean isSystemRequest = true;
	
	private String requestBody;
	
	private String requestUserId;
		
	public void init(String requestId, String username){
		this.requestId = requestId;
		this.requestUserId = username;
	}

	public String getAppId() {
		return appId;
	}

	public String getRequestId(){
		return requestId;
	}
	
	public String getRequestUserId(){
//		if (isSystemRequest) {
//			return WALLET_HEADER_USER_ID_SYSTEM;
//		}else {
			return requestUserId;
//		}
	}
	
	public boolean isSystemRequest(){
		return isSystemRequest;
	}
	
	public String getRequestBody() {
		return requestBody;
	}
	
	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	

	
}
