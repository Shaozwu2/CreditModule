package com.kooppi.nttca.portal.common.filter.response;

import com.kooppi.nttca.portal.common.auditlog.WS_AuditLog;

public class CreateAuditLogRequest {

	private WS_AuditLog auditLog;

	public WS_AuditLog getAuditLog() {
		return auditLog;
	}

	public void setAuditLog(WS_AuditLog auditLog) {
		this.auditLog = auditLog;
	}
}
