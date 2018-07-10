package com.kooppi.nttca.portal.wallet.dto.wallet;

import java.time.LocalDate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;
import com.kooppi.nttca.portal.wallet.domain.IdleUnit;
import com.kooppi.nttca.portal.wallet.domain.WalletStatus;
import com.kooppi.nttca.portal.wallet.domain.WalletType;

@XmlAccessorType(XmlAccessType.FIELD)
public class WalletDto extends ResponseResult {
	
	@XmlElement(name = "walletId")
	private String walletId;

	@XmlElement(name = "name")
	private String name;

	@XmlElement(name = "organizationId")
	private String organizationId;

	@XmlElement(name = "organizationName")
	private String organizationName;
	
	@XmlElement(name = "walletBalance")
	private Integer walletBalance;

	@XmlElement(name = "walletAvailable")
	private Integer walletAvailable;
	
	@XmlElement(name = "compensationAmount")
	private Integer compensationAmount;

	@XmlElement(name = "reserved")
	private Integer reserved;

	@XmlElement(name = "walletType")
	private WalletType walletType;
	
	@XmlElement(name = "status")
	private WalletStatus walletStatus;

	@XmlElement(name = "remark")
	private String remark;
	
	@XmlElement(name = "maxIdlePeriod")
	private Integer maxIdlePeriod;
	
	@XmlElement(name = "idleUnit")
	private IdleUnit idleUnit;
	
	@XmlElement(name = "expiredOn")
	private LocalDate expiredOn;

	@XmlElement(name = "creditBuffer")
	private Integer creditBuffer;
	
	@XmlElement(name = "creditRemainBuffer")
	private Integer creditRemainBuffer;
	
	@XmlElement(name = "isOneTime")
	private boolean isOneTime;

	@XmlElement(name = "monthlyRecharge")
	private Integer monthlyRecharge;

	@XmlElement(name = "rechargeDay")
	private Integer rechargeDay;

	@XmlElement(name = "utilizationAlert1Threshold")
	private Integer utilizationAlert1Threshold;

	@XmlElement(name = "utilizationAlert1Receivers")
	private String utilizationAlert1Receivers;
	
	@XmlElement(name = "utilizationAlert1Bccs")
	private String utilizationAlert1Bccs;
	
	@XmlElement(name = "utilizationAlert2Threshold")
	private Integer utilizationAlert2Threshold;

	@XmlElement(name = "utilizationAlert2Receivers")
	private String utilizationAlert2Receivers;
	
	@XmlElement(name = "utilizationAlert2Bccs")
	private String utilizationAlert2Bccs;

	@XmlElement(name = "forfeitAlert1Threshold")
	private Integer forfeitAlert1Threshold;

	@XmlElement(name = "forfeitAlert1Receivers")
	private String forfeitAlert1Receivers;
	
	@XmlElement(name = "forfeitAlert1Bccs")
	private String forfeitAlert1Bccs;
	
	@XmlElement(name = "forfeitAlert2Threshold")
	private Integer forfeitAlert2Threshold;

	@XmlElement(name = "forfeitAlert2Receivers")
	private String forfeitAlert2Receivers;
	
	@XmlElement(name = "forfeitAlert2Bccs")
	private String forfeitAlert2Bccs;
	
	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public void setReserved(Integer reserved) {
		this.reserved = reserved;
	}

	public void setWalletType(WalletType walletType) {
		this.walletType = walletType;
	}

	public void setWalletStatus(WalletStatus walletStatus) {
		this.walletStatus = walletStatus;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setMaxIdlePeriod(Integer maxIdlePeriod) {
		this.maxIdlePeriod = maxIdlePeriod;
	}

	public void setIdleUnit(IdleUnit idleUnit) {
		this.idleUnit = idleUnit;
	}
	
	public void setExpiredOn(LocalDate expiredOn) {
		this.expiredOn = expiredOn;
	}

	public void setCreditBuffer(Integer creditBuffer) {
		this.creditBuffer = creditBuffer;
	}
	
	public Integer getCreditRemainBuffer() {
		return creditRemainBuffer;
	}

	public void setCreditRemainBuffer(Integer creditRemainBuffer) {
		this.creditRemainBuffer = creditRemainBuffer;
	}

	public void setIsOneTime(boolean isOneTime) {
		this.isOneTime = isOneTime;
	}

	public void setMonthlyRecharge(Integer monthlyRecharge) {
		this.monthlyRecharge = monthlyRecharge;
	}

	public void setRechargeDay(Integer rechargeDay) {
		this.rechargeDay = rechargeDay;
	}

	public void setUtilizationAlert1Threshold(Integer utilizationAlert1Threshold) {
		this.utilizationAlert1Threshold = utilizationAlert1Threshold;
	}

	public void setUtilizationAlert1Receivers(String utilizationAlert1Receivers) {
		this.utilizationAlert1Receivers = utilizationAlert1Receivers;
	}

	public void setUtilizationAlert1Bccs(String utilizationAlert1Bccs) {
		this.utilizationAlert1Bccs = utilizationAlert1Bccs;
	}

	public void setUtilizationAlert2Threshold(Integer utilizationAlert2Threshold) {
		this.utilizationAlert2Threshold = utilizationAlert2Threshold;
	}

	public void setUtilizationAlert2Receivers(String utilizationAlert2Receivers) {
		this.utilizationAlert2Receivers = utilizationAlert2Receivers;
	}

	public void setUtilizationAlert2Bccs(String utilizationAlert2Bccs) {
		this.utilizationAlert2Bccs = utilizationAlert2Bccs;
	}

	public void setForfeitAlert1Threshold(Integer forfeitAlert1Threshold) {
		this.forfeitAlert1Threshold = forfeitAlert1Threshold;
	}

	public void setForfeitAlert1Receivers(String forfeitAlert1Receivers) {
		this.forfeitAlert1Receivers = forfeitAlert1Receivers;
	}

	public void setForfeitAlert1Bccs(String forfeitAlert1Bccs) {
		this.forfeitAlert1Bccs = forfeitAlert1Bccs;
	}

	public void setForfeitAlert2Threshold(Integer forfeitAlert2Threshold) {
		this.forfeitAlert2Threshold = forfeitAlert2Threshold;
	}

	public void setForfeitAlert2Receivers(String forfeitAlert2Receivers) {
		this.forfeitAlert2Receivers = forfeitAlert2Receivers;
	}

	public void setForfeitAlert2Bccs(String forfeitAlert2Bccs) {
		this.forfeitAlert2Bccs = forfeitAlert2Bccs;
	}

	@Override
	public String getResultName() {
		return "wallet";
	}

	public String getWalletId() {
		return walletId;
	}

	public String getName() {
		return name;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public Integer getReserved() {
		return reserved;
	}

	public WalletType getWalletType() {
		return walletType;
	}

	public WalletStatus getWalletStatus() {
		return walletStatus;
	}

	public String getRemark() {
		return remark;
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

	public Integer getWalletBalance() {
		return walletBalance;
	}

	public void setWalletBalance(Integer walletBalance) {
		this.walletBalance = walletBalance;
	}

	public Integer getWalletAvailable() {
		return walletAvailable;
	}

	public void setWalletAvailable(Integer walletAvailable) {
		this.walletAvailable = walletAvailable;
	}

	public Integer getCompensationAmount() {
		return compensationAmount;
	}

	public void setCompensationAmount(Integer compensationAmount) {
		this.compensationAmount = compensationAmount;
	}

	public void validateDisableWallet(){
		PortalExceptionUtils.throwIfNullOrEmptyString(remark, PortalErrorCode.MISS_PAYLOAD_PARAM_REMARK);
	}
	
	public void validateEnableWallet(){
		PortalExceptionUtils.throwIfNullOrEmptyString(remark, PortalErrorCode.MISS_PAYLOAD_PARAM_REMARK);
	}
}
