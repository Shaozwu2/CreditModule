package com.kooppi.nttca.portal.common.exception.dto;

import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;

public interface ErrorMessageMapper {

   public PortalErrorCode getDefaultErrorCode();
    
	public ErrorResponseDto toErrorMessage(Throwable exp);
}
