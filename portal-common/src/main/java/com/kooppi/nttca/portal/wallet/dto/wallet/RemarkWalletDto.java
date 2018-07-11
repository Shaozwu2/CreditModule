package com.kooppi.nttca.portal.wallet.dto.wallet;

import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlElement;

import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;

public class RemarkWalletDto {
	@XmlElement(name = "remark")
	@ApiModelProperty( value = "Wallet's permitted Users", required = true, example="remark")
	private String remark;

	public String getRemark() {
		return remark;
	}
	
	public void validateTermnateWallet(){
		PortalExceptionUtils.throwIfNullOrEmptyString(remark, PortalErrorCode.MISS_PAYLOAD_PARAM_REMARK);
	}
	
}
