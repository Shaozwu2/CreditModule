package com.kooppi.nttca.portal.ce.dto.payment;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;

@XmlAccessorType(XmlAccessType.FIELD)
public class ReasonDto extends ResponseResult{

	@Override
	public String getResultName() {
		return "reason";
	}
	
	@XmlElement(name = "reason")
	private String reason;
	
	public String getReason() {
		return reason;
	}
	
	public void validateReason(){
		PortalExceptionUtils.throwIfNullOrEmptyString(reason, PortalErrorCode.PAYMENT_MISS_PAYLOAD_PARAM_REASON);
	}

	
}
