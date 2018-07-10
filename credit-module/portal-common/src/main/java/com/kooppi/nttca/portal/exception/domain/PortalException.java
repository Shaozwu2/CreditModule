package com.kooppi.nttca.portal.exception.domain;

public abstract class PortalException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3165184863594379874L;

	public abstract PortalErrorCode getErrorCode();
}
