package com.kooppi.nttca.portal.common.filter.context;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.kooppi.nttca.portal.common.config.file.ConfigurationValue;
import com.kooppi.nttca.portal.common.constant.PortalConstant;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;

public class PortalRequestHeaderFilter implements ContainerRequestFilter{

	private static final Logger logger = LoggerFactory.getLogger(PortalRequestHeaderFilter.class);
	
	@Inject
	@ConfigurationValue(property = "portal.app.user.id.default", defaultValue = "system")
	private String DEFAULT_USER_ID;
	
	private static final List<String> SKIP_PATH = Lists.newArrayList("swagger.json");
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String path = requestContext.getUriInfo().getPath();
		if (SKIP_PATH.contains(path)) return;
		logger.debug("url:{}",requestContext.getUriInfo().getPath());
		String appId = requestContext.getHeaders().getFirst(PortalConstant.PORTAL_HEADER_APP_ID);
		String requestId = requestContext.getHeaders().getFirst(PortalConstant.PORTAL_HEADER_REQUEST_ID);
		String userId = requestContext.getHeaders().getFirst(PortalConstant.PORTAL_HEADER_USER_ID);
		String authorization = requestContext.getHeaders().getFirst(PortalConstant.PORTAL_HEADER_AUTHORIZATION);
		logger.debug("appId={},requestId={},userId={},authorization={}",appId,requestId,userId,authorization);
		//validate mandatory field:requestID, appId
		PortalExceptionUtils.throwIfNullOrEmptyString(appId, PortalErrorCode.MISS_HEADER_PARAM_APP_ID);
		PortalExceptionUtils.throwIfNullOrEmptyString(requestId, PortalErrorCode.MISS_HEADER_PARAM_Request_ID);
		PortalExceptionUtils.throwIfNullOrEmptyString(authorization, PortalErrorCode.MISS_HEADER_PARAM_AUTHORIZATION);
		
		String[] auth = authorization.split("\\s+");
		String authInfo = auth[1];
		byte[] bytes = Base64.getDecoder().decode(authInfo);
		String decodedStr = new String(bytes);
		PortalExceptionUtils.throwIfFalse(decodedStr.equals(userId), PortalErrorCode.BASIC_AUTHORIZATION_FAILED);
	}

}
