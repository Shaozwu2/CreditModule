package com.kooppi.nttca.portal.common.filter.response;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.annotation.Priority;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.ExtendedUriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.kooppi.nttca.portal.common.auditlog.AuditingActionType;
import com.kooppi.nttca.portal.common.auditlog.Logged;
import com.kooppi.nttca.portal.common.auditlog.WS_AuditLog;
import com.kooppi.nttca.portal.common.filter.request.RequestContext;
import com.kooppi.nttca.portal.common.mq.MqService;
import com.kooppi.nttca.portal.common.utils.DateUtils;
import com.kooppi.nttca.portal.common.utils.NetworkUtils;

@Provider
@Logged
@Priority(100)
public class AuditLogResponseFilter implements ContainerResponseFilter {

	private static final Logger log = LoggerFactory.getLogger(AuditLogResponseFilter.class);
	
	@Inject
	private RequestContext rc;
    
    @EJB
    private MqService messageProducer;

    @Context
    private HttpServletRequest servletRequest;

    @Context
    private ExtendedUriInfo extendedUriInfo;
	
	@Override
	public void filter(ContainerRequestContext requestCtx, ContainerResponseContext responseCtx) throws IOException {
	
				Gson gson = new Gson();
				CreateAuditLogRequest auditLogReq = new CreateAuditLogRequest();
				WS_AuditLog auditlog = new WS_AuditLog();
				auditlog.setOrganizationId("");
				auditlog.setUserId(rc.getRequestUserId());
				auditlog.setApplication("Credit Module");
				auditlog.setRequestMsg(requestCtx.getUriInfo().getRequestUri().toString());
				auditlog.setTargetObject(rc.getRequestBody());
				auditlog.setResponseMsg(gson.toJson(responseCtx.getEntity()));
				auditlog.setSourceIp(NetworkUtils.getIpAddr(servletRequest));
				auditlog.setResult(getResult(responseCtx.getStatus()));
				auditlog.setResultDescription(
						responseCtx.getStatus() + " - " + responseCtx.getStatusInfo().getReasonPhrase());
				auditlog.setRecordTime(DateUtils.formatClient(LocalDateTime.now()));

				auditlog.setTargetUserId(rc.getRequestUserId());
				auditlog.setEnableForUser(true);

				Logged logged = extendedUriInfo.getMatchedResourceMethod().getInvocable().getHandlingMethod()
						.getAnnotation(Logged.class);
				AuditingActionType auditingActionType = logged.actionType();
				auditlog.setAction(auditingActionType.getName());

				auditLogReq.setAuditLog(auditlog);

				try {
					if (AuditingActionType.CREATE_REALTIME_TRANSACTION.equals(auditingActionType)) {
						new Thread(new Runnable() {
							public void run() {
								try {
									messageProducer.sentAuditLog(auditLogReq);
								} catch (Exception e) {
									log.error(e.getMessage());
								}
							}
						}).start();
					} else {
						messageProducer.sentAuditLog(auditLogReq);
					}
				} catch (Exception e) {
					log.error(e.getMessage());
				}
		
	}

	private String getResult(int statusCode) {
        String result = null;
        if (statusCode == HttpServletResponse.SC_OK || statusCode == HttpServletResponse.SC_PARTIAL_CONTENT) {
            result = "SUCCESS";
        }
        if (statusCode == HttpServletResponse.SC_BAD_REQUEST || statusCode == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
            result = "FAILURE";
        }
        return result;
    }
}
