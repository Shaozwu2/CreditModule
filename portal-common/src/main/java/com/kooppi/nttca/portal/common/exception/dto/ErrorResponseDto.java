package com.kooppi.nttca.portal.common.exception.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.google.common.base.Objects;
import com.kooppi.nttca.portal.common.constant.PortalConstant;
import com.kooppi.nttca.portal.common.utils.JAXBUtil;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;

/**
 * Represent a error response
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorResponseDto {

	private static final PortalErrorCode DEFAULT_PORTAL_ERROR_CODE = PortalErrorCode.INTERNAL_SERVER_ERROR;
	private static final String DEFAULT_REQUEST_ID = "UNKNOW";

	
	@XmlElement(name = PortalConstant.PORTAL_RESPONSE_HEADER_REQUEST_ID)
	private String requestId;
	
	@XmlElement(name = PortalConstant.PORTAL_RESPONSE_HEADER_RESPONSE_CODE)
	private String responseCode;
	
	@XmlElement(name = PortalConstant.PORTAL_RESPONSE_HEADER_RESPONSE_MESSAGE)
	private String responseMsg;
	
	@XmlElement(name = PortalConstant.PORTAL_RESPONSE_HEADER_RESPONSE_CONTENT)
	private String responseContent;
	
	@XmlTransient
	private PortalErrorCode errorCode;
	
	@XmlTransient
	private Integer httpResponseCode;
    
	
    private ErrorResponseDto(PortalErrorCode errorCode,String requestId) {
    	this.requestId = requestId;
        this.responseCode = errorCode.getResponseCode();
        this.responseMsg= errorCode.getResponseMsg();
        this.httpResponseCode = errorCode.getHttpResponseCode();
        this.errorCode = errorCode;
    }
    
    // For Moxy used only
    ErrorResponseDto() {
        this(DEFAULT_PORTAL_ERROR_CODE,DEFAULT_REQUEST_ID);
    }


    
    public static ErrorResponseDto create(PortalErrorCode errorCode,String requestId){
    	ErrorResponseDto msg = new ErrorResponseDto(errorCode,requestId);
    	return msg;
    }

    public PortalErrorCode getErrorCode(){
    	return errorCode;
    }
    
	public String getRequestId() {
		return requestId;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public String getResponseContent() {
		return responseContent;
	}

	public Integer getHttpResponseCode(){
		return this.httpResponseCode;
	}
	
	@Override
    public int hashCode() {
	    return Objects.hashCode(responseCode);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ErrorResponseDto other = (ErrorResponseDto) obj;
        return Objects.equal(other.responseCode, this.responseCode);
    }

    @Override
    public String toString() {
    	return String.format("Http Reponse code: %s \nRequest Body: \n%s ", this.responseCode , JAXBUtil.toJson(this));
    }

}
