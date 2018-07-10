package com.kooppi.nttca.portal.common.scheduler;

public class PortalSchedulerException extends RuntimeException {

	private static final long serialVersionUID = 2477479249322182081L;

	public PortalSchedulerException() {
	}

	public PortalSchedulerException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public PortalSchedulerException(String message) {
		super(message);
	}

	public PortalSchedulerException(Throwable throwable) {
		super(throwable);
	}
}
