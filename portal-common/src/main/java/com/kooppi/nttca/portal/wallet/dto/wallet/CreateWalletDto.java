package com.kooppi.nttca.portal.wallet.dto.wallet;

import java.time.LocalDate;

import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.validator.routines.EmailValidator;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.kooppi.nttca.portal.common.constant.PortalConstant;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;
import com.kooppi.nttca.portal.wallet.domain.IdleUnit;
import com.kooppi.nttca.portal.wallet.domain.WalletStatus;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlDiscriminatorValue("CreateWalletDto")
@ApiModel(value ="CreateWallet", description = "Create Wallet resource representation")
public class CreateWalletDto {

	@ApiModelProperty(value = "Organization's Id", required = true, example ="kooppi001" ) 
	@XmlElement(name = "organizationId")
	private String organizationId;
	
	@ApiModelProperty(value = "Organization's Name", required = true, example ="kooppi001" ) 
	@XmlElement(name = "organizationName")
	private String organizationName;
	
	@ApiModelProperty(value = "Parent Organization's Id", required = true, example ="kooppi001" ) 
	@XmlElement(name = "parentOrganizationId")
	private String parentOrganizationId;
	
	@ApiModelProperty(value = "Parent Organization's Name", required = true, example ="kooppi001" ) 
	@XmlElement(name = "parentOrganizationName")
	private String parentOrganizationName;
	
	@ApiModelProperty( value = "Wallet's maximum idle period", required = false, example="1" ) 
	@XmlElement(name = "maxIdlePeriod")
	private Integer maxIdlePeriod;

	@ApiModelProperty( value = "Wallet's idle period unit", required = false, example="YEAR/MONTH/DAY" )
	@XmlElement(name = "idleUnit")
	private IdleUnit idleUnit;
	
	@ApiModelProperty( value = "Wallet's expired date", required = false, example="2018-01-01" )
	@XmlElement(name = "expiredOn")
	private LocalDate expiredOn;
	
	@ApiModelProperty( value = "Wallet's credit buffer", required = false, example="200" )
	@XmlElement(name = "creditBuffer")
	private Integer creditBuffer;
	
	@ApiModelProperty( value = "Wallet's one time or until no credit", required = false, example="true" )
	@XmlElement(name = "isOneTime")
	private boolean isOneTime;

	@ApiModelProperty( value = "Credit utilization alert 1 threshold", required = false, example="50" )
	@XmlElement(name = "utilizationAlert1Threshold")
	private Integer utilizationAlert1Threshold;

	@ApiModelProperty( value = "Utilization alert 1 email recevicer", required = false, example="xxx@gmail.com" )
	@XmlElement(name = "utilizationAlert1Receivers")
	private String utilizationAlert1Receivers;
	
	@ApiModelProperty( value = "Utilization alert 1 Bcc email recevicer", required = false, example="xxx@gmail.com" )
	@XmlElement(name = "utilizationAlert1Bccs")
	private String utilizationAlert1Bccs;
	
	@ApiModelProperty( value = "Credit utilization alert 2 threshold", required = false, example="50" )
	@XmlElement(name = "utilizationAlert2Threshold")
	private Integer utilizationAlert2Threshold;

	@ApiModelProperty( value = "Utilization alert 2 email recevicer", required = false, example="xxx@gmail.com" )
	@XmlElement(name = "utilizationAlert2Receivers")
	private String utilizationAlert2Receivers;
	
	@ApiModelProperty( value = "Utilization alert 2 Bcc email recevicer", required = false, example="xxx@gmail.com" )
	@XmlElement(name = "utilizationAlert2Bccs")
	private String utilizationAlert2Bccs;
	
	@ApiModelProperty( value = "Credit forfeit alert 1 threshold", required = false, example="50" )
	@XmlElement(name = "forfeitAlert1Threshold")
	private Integer forfeitAlert1Threshold;

	@ApiModelProperty( value = "Forfeit alert 1 email recevicer", required = false, example="xxx@gmail.com" )
	@XmlElement(name = "forfeitAlert1Receivers")
	private String forfeitAlert1Receivers;
	
	@ApiModelProperty( value = "Forfeit alert 1 Bcc email recevicer", required = false, example="xxx@gmail.com" )
	@XmlElement(name = "forfeitAlert1Bccs")
	private String forfeitAlert1Bccs;
	
	@ApiModelProperty( value = "Credit forfeit alert 2 threshold", required = false, example="50" )
	@XmlElement(name = "forfeitAlert2Threshold")
	private Integer forfeitAlert2Threshold;

	@ApiModelProperty( value = "Forfeit alert 2 email recevicer", required = false, example="xxx@gmail.com" )
	@XmlElement(name = "forfeitAlert2Receivers")
	private String forfeitAlert2Receivers;
	
	@ApiModelProperty( value = "Forfeit alert 2 Bcc email recevicer", required = false, example="xxx@gmail.com" )
	@XmlElement(name = "forfeitAlert2Bccs")
	private String forfeitAlert2Bccs;
	
	@XmlElement(name = "walletId")
	@ApiModelProperty(required = false, hidden=true)
	private String walletId;
	
	@XmlElement(name = "balance")
	@ApiModelProperty(required = false, hidden=true)
	private Integer balance;

	@XmlElement(name = "available")
	@ApiModelProperty(required = false, hidden=true)
	private Integer available;

	@XmlElement(name = "reserved")
	@ApiModelProperty(required = false, hidden=true)
	private Integer reserved;

	@XmlElement(name = "status")
	@ApiModelProperty(required = false, hidden=true)
	private WalletStatus walletStatus;

	@XmlElement(name = "remark")
	@ApiModelProperty(required = false, hidden=true)
	private String remark;

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public Integer getMaxIdlePeriod() {
		return maxIdlePeriod;
	}

	public void setMaxIdlePeriod(Integer maxIdlePeriod) {
		this.maxIdlePeriod = maxIdlePeriod;
	}

	public IdleUnit getIdleUnit() {
		return idleUnit;
	}

	public void setIdleUnit(IdleUnit idleUnit) {
		this.idleUnit = idleUnit;
	}

	public LocalDate getExpiredOn() {
		return expiredOn;
	}

	public void setExpiredOn(LocalDate expiredOn) {
		this.expiredOn = expiredOn;
	}

	public Integer getCreditBuffer() {
		return creditBuffer;
	}

	public void setCreditBuffer(Integer creditBuffer) {
		this.creditBuffer = creditBuffer;
	}

	public boolean isOneTime() {
		return isOneTime;
	}

	public void setOneTime(boolean isOneTime) {
		this.isOneTime = isOneTime;
	}

	public String getParentOrganizationId() {
		return parentOrganizationId;
	}

	public void setParentOrganizationId(String parentOrganizationId) {
		this.parentOrganizationId = parentOrganizationId;
	}

	public String getParentOrganizationName() {
		return parentOrganizationName;
	}

	public void setParentOrganizationName(String parentOrganizationName) {
		this.parentOrganizationName = parentOrganizationName;
	}

	public Integer getUtilizationAlert1Threshold() {
		return utilizationAlert1Threshold;
	}

	public void setUtilizationAlert1Threshold(Integer utilizationAlert1Threshold) {
		this.utilizationAlert1Threshold = utilizationAlert1Threshold;
	}

	public String getUtilizationAlert1Receivers() {
		return utilizationAlert1Receivers;
	}

	public void setUtilizationAlert1Receivers(String utilizationAlert1Receivers) {
		this.utilizationAlert1Receivers = utilizationAlert1Receivers;
	}

	public String getUtilizationAlert1Bccs() {
		return utilizationAlert1Bccs;
	}

	public void setUtilizationAlert1Bccs(String utilizationAlert1Bccs) {
		this.utilizationAlert1Bccs = utilizationAlert1Bccs;
	}

	public Integer getUtilizationAlert2Threshold() {
		return utilizationAlert2Threshold;
	}

	public void setUtilizationAlert2Threshold(Integer utilizationAlert2Threshold) {
		this.utilizationAlert2Threshold = utilizationAlert2Threshold;
	}

	public String getUtilizationAlert2Receivers() {
		return utilizationAlert2Receivers;
	}

	public void setUtilizationAlert2Receivers(String utilizationAlert2Receivers) {
		this.utilizationAlert2Receivers = utilizationAlert2Receivers;
	}

	public String getUtilizationAlert2Bccs() {
		return utilizationAlert2Bccs;
	}

	public void setUtilizationAlert2Bccs(String utilizationAlert2Bccs) {
		this.utilizationAlert2Bccs = utilizationAlert2Bccs;
	}

	public Integer getForfeitAlert1Threshold() {
		return forfeitAlert1Threshold;
	}

	public void setForfeitAlert1Threshold(Integer forfeitAlert1Threshold) {
		this.forfeitAlert1Threshold = forfeitAlert1Threshold;
	}

	public String getForfeitAlert1Receivers() {
		return forfeitAlert1Receivers;
	}

	public void setForfeitAlert1Receivers(String forfeitAlert1Receivers) {
		this.forfeitAlert1Receivers = forfeitAlert1Receivers;
	}

	public String getForfeitAlert1Bccs() {
		return forfeitAlert1Bccs;
	}

	public void setForfeitAlert1Bccs(String forfeitAlert1Bccs) {
		this.forfeitAlert1Bccs = forfeitAlert1Bccs;
	}

	public Integer getForfeitAlert2Threshold() {
		return forfeitAlert2Threshold;
	}

	public void setForfeitAlert2Threshold(Integer forfeitAlert2Threshold) {
		this.forfeitAlert2Threshold = forfeitAlert2Threshold;
	}

	public String getForfeitAlert2Receivers() {
		return forfeitAlert2Receivers;
	}

	public void setForfeitAlert2Receivers(String forfeitAlert2Receivers) {
		this.forfeitAlert2Receivers = forfeitAlert2Receivers;
	}

	public String getForfeitAlert2Bccs() {
		return forfeitAlert2Bccs;
	}

	public void setForfeitAlert2Bccs(String forfeitAlert2Bccs) {
		this.forfeitAlert2Bccs = forfeitAlert2Bccs;
	}
	
	public void validateCreateWallet() {
		//validate mandatory field
		PortalExceptionUtils.throwIfNullOrEmptyString(organizationId, PortalErrorCode.MISS_PAYLOAD_PARAM_ORGANIZATIONID);
		//WalletException.throwIfNull(maxIdlePeriod, PortalErrorCode.MISS_PAYLOAD_PARAM_MAX_IDLE_PERIOD);
//		PortalExceptionUtils.throwIfNull(idleUnit, PortalErrorCode.MISS_PAYLOAD_PARAM_IDLE_UNIT);
//		PortalExceptionUtils.throwIfNull(creditBuffer, PortalErrorCode.MISS_PAYLOAD_PARAM_CREDIT_BUFFER);
//		PortalExceptionUtils.throwIfFalse(creditBuffer>=0, PortalErrorCode.NEGATIVE_CREDIT_BUFFER);
		
		if(maxIdlePeriod!=null) {
			PortalExceptionUtils.throwIfFalse(maxIdlePeriod>=0, PortalErrorCode.NEGATIVE_MAX_IDLE_PRERIOD);
		}
		if(utilizationAlert1Threshold!=null) {
			PortalExceptionUtils.throwIfFalse(utilizationAlert1Threshold>=0, PortalErrorCode.NEGATIVE_ALERT_THRESHOLD);
			PortalExceptionUtils.throwIfNullOrEmptyString(utilizationAlert1Receivers, PortalErrorCode.MISS_PAYLOAD_PARAM_ALERT_RECEIVERS);
		}
		if(utilizationAlert2Threshold!=null) {
			PortalExceptionUtils.throwIfFalse(utilizationAlert2Threshold>=0, PortalErrorCode.NEGATIVE_ALERT_THRESHOLD);
			PortalExceptionUtils.throwIfNullOrEmptyString(utilizationAlert2Receivers, PortalErrorCode.MISS_PAYLOAD_PARAM_ALERT_RECEIVERS);
		}
		if(forfeitAlert1Threshold!=null) {
			PortalExceptionUtils.throwIfFalse(forfeitAlert1Threshold>=0, PortalErrorCode.NEGATIVE_ALERT_THRESHOLD);
			PortalExceptionUtils.throwIfNullOrEmptyString(forfeitAlert1Receivers, PortalErrorCode.MISS_PAYLOAD_PARAM_ALERT_RECEIVERS);
		}
		if(forfeitAlert2Threshold!=null) {
			PortalExceptionUtils.throwIfFalse(forfeitAlert2Threshold>=0, PortalErrorCode.NEGATIVE_ALERT_THRESHOLD);
			PortalExceptionUtils.throwIfNullOrEmptyString(forfeitAlert2Receivers, PortalErrorCode.MISS_PAYLOAD_PARAM_ALERT_RECEIVERS);
		}
		
		//validate the recharge day
		//WalletException.throwIfFalse((rechargeDay>0 && rechargeDay<29), PortalErrorCode.INVALID_WALLET_RECHARGEDAY);
		
		//validate the alert receivers
		if (!Strings.isNullOrEmpty(utilizationAlert1Receivers)) {
			Splitter.on(PortalConstant.EMAIL_SEPARATOR).split(utilizationAlert1Receivers).forEach(receiver->{
				PortalExceptionUtils.throwIfFalse(EmailValidator.getInstance().isValid(receiver), PortalErrorCode.INVALID_WALLET_ALERTRECEIVERS);
			});
		}
		if (!Strings.isNullOrEmpty(utilizationAlert2Receivers)) {
			Splitter.on(PortalConstant.EMAIL_SEPARATOR).split(utilizationAlert2Receivers).forEach(receiver->{
				PortalExceptionUtils.throwIfFalse(EmailValidator.getInstance().isValid(receiver), PortalErrorCode.INVALID_WALLET_ALERTRECEIVERS);
			});
		}
		if (!Strings.isNullOrEmpty(forfeitAlert1Receivers)) {
			Splitter.on(PortalConstant.EMAIL_SEPARATOR).split(forfeitAlert1Receivers).forEach(receiver->{
				PortalExceptionUtils.throwIfFalse(EmailValidator.getInstance().isValid(receiver), PortalErrorCode.INVALID_WALLET_ALERTRECEIVERS);
			});
		}
		if (!Strings.isNullOrEmpty(forfeitAlert2Receivers)) {
			Splitter.on(PortalConstant.EMAIL_SEPARATOR).split(forfeitAlert2Receivers).forEach(receiver->{
				PortalExceptionUtils.throwIfFalse(EmailValidator.getInstance().isValid(receiver), PortalErrorCode.INVALID_WALLET_ALERTRECEIVERS);
			});
		}
		
		//validate if the email format is correct
		if (!Strings.isNullOrEmpty(utilizationAlert1Bccs)) {
			Splitter.on(PortalConstant.EMAIL_SEPARATOR).split(utilizationAlert1Bccs).forEach(bcc ->{
				PortalExceptionUtils.throwIfFalse(EmailValidator.getInstance().isValid(bcc), PortalErrorCode.INVALID_WALLET_BCCS);
			});
		}
		if (!Strings.isNullOrEmpty(utilizationAlert2Bccs)) {
			Splitter.on(PortalConstant.EMAIL_SEPARATOR).split(utilizationAlert2Bccs).forEach(bcc ->{
				PortalExceptionUtils.throwIfFalse(EmailValidator.getInstance().isValid(bcc), PortalErrorCode.INVALID_WALLET_BCCS);
			});
		}
		if (!Strings.isNullOrEmpty(forfeitAlert1Bccs)) {
			Splitter.on(PortalConstant.EMAIL_SEPARATOR).split(forfeitAlert1Bccs).forEach(bcc ->{
				PortalExceptionUtils.throwIfFalse(EmailValidator.getInstance().isValid(bcc), PortalErrorCode.INVALID_WALLET_BCCS);
			});
		}
		if (!Strings.isNullOrEmpty(forfeitAlert2Bccs)) {
			Splitter.on(PortalConstant.EMAIL_SEPARATOR).split(forfeitAlert2Bccs).forEach(bcc ->{
				PortalExceptionUtils.throwIfFalse(EmailValidator.getInstance().isValid(bcc), PortalErrorCode.INVALID_WALLET_BCCS);
			});
		}
	}
}
