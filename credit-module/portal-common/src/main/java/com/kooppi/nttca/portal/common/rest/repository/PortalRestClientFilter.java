package com.kooppi.nttca.portal.common.rest.repository;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

import com.kooppi.nttca.portal.common.config.file.ConfigurationValue;
import com.kooppi.nttca.portal.common.constant.PortalConstant;
import com.kooppi.nttca.portal.common.filter.request.RequestContext;

@ApplicationScoped
public class PortalRestClientFilter implements ClientRequestFilter{

	@Inject
	private RequestContext rc;
	
	@Inject
	@ConfigurationValue(property = "portal.app.id.default",defaultValue = "portal-charging-engine")
	private String APP_ID;
	
	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {
		requestContext.getHeaders().add(PortalConstant.PORTAL_HEADER_APP_ID, APP_ID);
		if (rc != null) {
			requestContext.getHeaders().add(PortalConstant.PORTAL_HEADER_USER_ID, rc.getRequestUserId());
			requestContext.getHeaders().add(PortalConstant.PORTAL_HEADER_REQUEST_ID, rc.getRequestId());
		}
	}
}
