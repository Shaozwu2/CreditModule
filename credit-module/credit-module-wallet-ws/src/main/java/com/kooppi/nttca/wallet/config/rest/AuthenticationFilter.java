package com.kooppi.nttca.wallet.config.rest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.kooppi.nttca.portal.common.constant.PortalConstant;
import com.kooppi.nttca.portal.common.filter.request.RequestContext;
import com.kooppi.nttca.portal.common.utils.AuthorizationDecoder;
import com.kooppi.nttca.portal.common.utils.AuthorizationDecoder.UserPass;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalException;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;
import com.kooppi.nttca.portal.wallet.dto.common.ErrorMessageDto;
import com.kooppi.nttca.wallet.common.persistence.domain.ApiUser;

@Provider
public class AuthenticationFilter implements ContainerRequestFilter {
	
	private static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
	
	@Context
	private ResourceInfo resourceInfo;
	
	@Inject
	private ApiUserManager apiUserManager;
	
	@Inject
	private RequestContext rc;
	
	private static final String AUTHORIZATION_PROPERTY = "Authorization";
	private static final List<String> SKIP_PATH = Lists.newArrayList("swagger.json", "swagger.yaml");

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		logger.info("Authorization filter start");
		if (SKIP_PATH.contains(requestContext.getUriInfo().getPath()))
			return;

		String requestId = requestContext.getHeaders().getFirst(PortalConstant.PORTAL_HEADER_REQUEST_ID);
		PortalExceptionUtils.throwIfNullOrEmptyString(requestId, PortalErrorCode.MISS_HEADER_PARAM_Request_ID);

		// Get request headers
		final MultivaluedMap<String, String> headers = requestContext.getHeaders();

		// Fetch authorization header
		final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);

		// If no authorization information present; block access
		if (authorization == null || authorization.isEmpty()) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
					.entity(new ErrorMessageDto("UNAUTHORIZED", "You cannot access this resource")).build());
			return;
		}

		// Get encoded username and password
		try {
			UserPass userPass = AuthorizationDecoder.fromAuthorizationValue(authorization.get(0));
			PortalExceptionUtils.throwIfTrue(!isUserAllowed(userPass.getUsername(), userPass.getPassword()),
					PortalErrorCode.AUTHORIZATION_NOT_MATCHED);
			rc.init(requestId, userPass.getUsername());
		} catch (Exception e) {
			if (e instanceof PortalException)
				throw e;
			PortalExceptionUtils.throwNow(PortalErrorCode.AUTHORIZATION_NOT_MATCHED);
		}

		logger.info("Authorization filter end");
	}

	private boolean isUserAllowed(final String username, final String password) {
		Optional<ApiUser> optApiUser = apiUserManager.findApiUser(username);
		if (optApiUser.isPresent()) {
			rc.setAppId(optApiUser.get().getIncomingChannel());
			return optApiUser.get().getPassword().equals(password);
		}
		return false;
	}
}
