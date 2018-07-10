package com.kooppi.nttca.wallet.common.swagger.ui.model;

import java.time.LocalDate;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.google.common.collect.Lists;
import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;
import com.kooppi.nttca.portal.wallet.domain.IdleUnit;
import com.kooppi.nttca.wallet.common.persistence.domain.Wallet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value ="Wallet", description = "Wallet resource representation")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateWalletDtoModel extends ResponseResult  {
	
	@ApiModelProperty( value = "Wallet's name", required = true, example ="wallet001" ) 
	@XmlElement(name = "name")
	private String name;

	@ApiModelProperty( value = "Organization's Id", required = true, example ="kooppi001" ) 
	@XmlElement(name = "organizationId")
	private String organizationId;
	
	@ApiModelProperty( value = "Wallet's maximum idle period", required = true, example="1" ) 
	@XmlElement(name = "maxIdlePeriod")
	private Integer maxIdlePeriod;

	@ApiModelProperty( value = "Wallet's idle period unit", required = true, example="YEAR/MONTH/DAY" )
	@XmlElement(name = "idleUnit")
	private IdleUnit idleUnit;
	
	@ApiModelProperty( value = "Wallet's expired date", required = true, example="2018-01-01" )
	@XmlElement(name = "expiredOn")
	private LocalDate expiredOn;

	@ApiModelProperty( value = "Wallet's credit buffer", required = true, example="200" )
	@XmlElement(name = "creditBuffer")
	private Integer creditBuffer;

	@ApiModelProperty( value = "Wallet's one time or until no credit", required = true, example="true" )
	@XmlElement(name = "isOneTime")
	private boolean isOneTime;
	
	@ApiModelProperty( value = "Monthly recharge credit amount", required = true, example="100" )
	@XmlElement(name = "monthlyRecharge")
	private Integer monthlyRecharge;

	@ApiModelProperty( value = "Monthly recharge date", required = true, example="1" )
	@XmlElement(name = "rechargeDay")
	private Integer rechargeDay;

	@ApiModelProperty( value = "Credit utilization alert 1 threshold", required = true, example="50" )
	@XmlElement(name = "utilizationAlert1Threshold")
	private Integer utilizationAlert1Threshold;

	@ApiModelProperty( value = "Utilization alert 1 email recevicer", required = true, example="xxx@gmail.com" )
	@XmlElement(name = "utilizationAlert1Receivers")
	private String utilizationAlert1Receivers;
	
	@ApiModelProperty( value = "Utilization alert 1 Bcc email recevicer", required = true, example="xxx@gmail.com" )
	@XmlElement(name = "utilizationAlert1Bccs")
	private String utilizationAlert1Bccs;
	
	@ApiModelProperty( value = "Credit utilization alert 2 threshold", required = true, example="50" )
	@XmlElement(name = "utilizationAlert2Threshold")
	private Integer utilizationAlert2Threshold;

	@ApiModelProperty( value = "Utilization alert 2 email recevicer", required = true, example="xxx@gmail.com" )
	@XmlElement(name = "utilizationAlert2Receivers")
	private String utilizationAlert2Receivers;
	
	@ApiModelProperty( value = "Utilization alert 2 Bcc email recevicer", required = true, example="xxx@gmail.com" )
	@XmlElement(name = "utilizationAlert2Bccs")
	private String utilizationAlert2Bccs;
	
	@ApiModelProperty( value = "Credit forfeit alert 1 threshold", required = true, example="50" )
	@XmlElement(name = "forfeitAlert1Threshold")
	private Integer forfeitAlert1Threshold;

	@ApiModelProperty( value = "Forfeit alert 1 email recevicer", required = true, example="xxx@gmail.com" )
	@XmlElement(name = "forfeitAlert1Receivers")
	private String forfeitAlert1Receivers;
	
	@ApiModelProperty( value = "Forfeit alert 1 Bcc email recevicer", required = true, example="xxx@gmail.com" )
	@XmlElement(name = "forfeitAlert1Bccs")
	private String forfeitAlert1Bccs;
	
	@ApiModelProperty( value = "Credit forfeit alert 2 threshold", required = true, example="50" )
	@XmlElement(name = "forfeitAlert2Threshold")
	private Integer forfeitAlert2Threshold;

	@ApiModelProperty( value = "Forfeit alert 2 email recevicer", required = true, example="xxx@gmail.com" )
	@XmlElement(name = "forfeitAlert2Receivers")
	private String forfeitAlert2Receivers;
	
	@ApiModelProperty( value = "Forfeit alert 2 Bcc email recevicer", required = true, example="xxx@gmail.com" )
	@XmlElement(name = "forfeitAlert2Bccs")
	private String forfeitAlert2Bccs;
	
	@ApiModelProperty( value = "Wallet's permitted Users", example="user001, user002")
	@XmlElement(name = "permittedUsers")
	private List<String> permittedUsers = Lists.newArrayList();
	
	public static CreateWalletDtoModel create(Wallet wallet){
		CreateWalletDtoModel dto = new CreateWalletDtoModel();
		dto.organizationId = wallet.getOrganizationId();
		dto.maxIdlePeriod = wallet.getMaxIdlePeriod();
		dto.idleUnit = wallet.getIdleUnit();
		dto.expiredOn = wallet.getExpiredOn();
		dto.creditBuffer = wallet.getCreditBuffer();
		dto.isOneTime = wallet.isOneTime();
		dto.utilizationAlert1Threshold = wallet.getUtilizationAlert1Threshold();
		dto.utilizationAlert1Receivers = wallet.getUtilizationAlert1Receivers();
		dto.utilizationAlert1Bccs = wallet.getUtilizationAlert2Bcc();
		dto.utilizationAlert2Threshold = wallet.getUtilizationAlert2Threshold();
		dto.utilizationAlert2Receivers = wallet.getUtilizationAlert2Receivers();
		dto.forfeitAlert1Threshold = wallet.getForfeitAlert1Threshold();
		dto.forfeitAlert1Receivers = wallet.getForfeitAlert1Receivers();
		dto.forfeitAlert1Bccs = wallet.getForfeitAlert2Bcc();
		dto.forfeitAlert2Threshold = wallet.getForfeitAlert2Threshold();
		dto.forfeitAlert2Receivers = wallet.getForfeitAlert2Receivers();
		dto.forfeitAlert2Bccs = wallet.getForfeitAlert1Bcc();

		return dto;
	}
	public String getName() {
		return name;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public Integer getMaxIdlePeriod() {
		return maxIdlePeriod;
	}

	public IdleUnit getIdleUnit() {
		return idleUnit;
	}
	
	public LocalDate getExpiredOn() {
		return expiredOn;
	}

	public Integer getCreditBuffer() {
		return creditBuffer;
	}
	
	public boolean isOneTime() {
		return isOneTime;
	}

	public Integer getUtilizationAlert1Threshold() {
		return utilizationAlert1Threshold;
	}

	public String getUtilizationAlert1Receivers() {
		return utilizationAlert1Receivers;
	}
	
	public String getUtilizationAlert1Bccs() {
		return utilizationAlert1Bccs;
	}
	
	public Integer getUtilizationAlert2Threshold() {
		return utilizationAlert2Threshold;
	}
	
	public String getUtilizationAlert2Receivers() {
		return utilizationAlert2Receivers;
	}
	
	public String getUtilizationAlert2Bccs() {
		return utilizationAlert2Bccs;
	}
	
	public Integer getForfeitAlert1Threshold() {
		return forfeitAlert1Threshold;
	}
	
	public String getForfeitAlert1Receivers() {
		return forfeitAlert1Receivers;
	}
	
	public String getForfeitAlert1Bccs() {
		return forfeitAlert1Bccs;
	}
	
	public Integer getForfeitAlert2Threshold() {
		return forfeitAlert2Threshold;
	}
	
	public String getForfeitAlert2Receivers() {
		return forfeitAlert2Receivers;
	}
	
	public String getForfeitAlert2Bccs() {
		return forfeitAlert2Bccs;
	}
	
	public Integer getMonthlyRecharge() {
		return monthlyRecharge;
	}

	public Integer getRechargeDay() {
		return rechargeDay;
	}

	public List<String> getPermittedUsers() {
		return permittedUsers;
	}

	@Override
	public String getResultName() {
		return "walletTest";
	}
	
}
