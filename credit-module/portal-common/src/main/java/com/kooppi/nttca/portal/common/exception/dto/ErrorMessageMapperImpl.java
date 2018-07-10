package com.kooppi.nttca.portal.common.exception.dto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.kooppi.nttca.portal.common.filter.request.RequestContext;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalException;

@ApplicationScoped
public class ErrorMessageMapperImpl implements ErrorMessageMapper{

//	private static final Logger logger = LoggerFactory.getLogger(ErrorMessageMapper.class);

	@Inject
	private RequestContext rc;
	
	@Override
	public PortalErrorCode getDefaultErrorCode(){
		return PortalErrorCode.INTERNAL_SERVER_ERROR;
	}
	
	@Override
	public ErrorResponseDto toErrorMessage(Throwable exp) {
		
		PortalErrorCode errorCode = getDefaultErrorCode();
		
		if (exp != null) {
			if (exp instanceof PortalException) {
				PortalException portalException = (PortalException) exp;
				errorCode = portalException.getErrorCode();
			} 
		}
		
		if (errorCode == null) {
			errorCode = getDefaultErrorCode();
		}
		
		ErrorResponseDto message = toErrorMessage(errorCode);
		return message;
	}
	
	private ErrorResponseDto toErrorMessage(PortalErrorCode errorCode) {
        return ErrorResponseDto.create(errorCode, rc.getRequestId());
	}

}
