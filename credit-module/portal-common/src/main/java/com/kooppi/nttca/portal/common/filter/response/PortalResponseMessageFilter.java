package com.kooppi.nttca.portal.common.filter.response;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kooppi.nttca.portal.common.filter.request.RequestContext;
import com.kooppi.nttca.portal.common.filter.response.dto.EmptyResponseResult;
import com.kooppi.nttca.portal.common.filter.response.dto.ResponseHeader;
import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;

public class PortalResponseMessageFilter implements ContainerResponseFilter{

	@Inject
	private RequestContext portalRequestContext;
	
	private static final Logger logger = LoggerFactory.getLogger(PortalResponseMessageFilter.class);
	
	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		logger.debug("begin filter");
		Object entity = responseContext.getEntity();
		logger.debug("entity is null = {}",entity == null);

		if (entity instanceof ResponseResult) {
			ResponseResult result = null;
			if (entity instanceof EmptyResponseResult) {
				responseContext.setStatusInfo(Status.OK);
			}else {
				result = (ResponseResult) entity;
				result.setResultName();
				//since we don't have empty response, change response code 204 to 200
			}
			ResponseHeader response = ResponseHeader.create(portalRequestContext.getRequestId(), responseContext.getStatus() + "", responseContext.getStatusInfo().toString(), result);
			responseContext.setEntity(response);
		}
		logger.debug("end filter");
	}
}
