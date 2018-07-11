package com.kooppi.nttca.wallet.common.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import com.kooppi.nttca.portal.common.domain.Modifiable;
import com.kooppi.nttca.portal.wallet.domain.AllowedType;

/**
 * 
 */
@Entity
//@Table(name = "credit_pool")
public class CreditPool extends Modifiable {

	private static final long serialVersionUID = -7772400296440183687L;
	
	@Column(name = "credit_pool_id")
	private String creditPoolId;
	
	@Column(name = "wallet_id")
	private String walletId;
	
	@Column(name = "balance")
	private Integer balance;
	
	@Column(name = "available")
	private Integer available;
	
	@Column(name = "reserved")
	private Integer reserved;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "allowed_type")
	private AllowedType allowedType;

	public static CreditPool create(String creditPoolId, String walletId, int balance,int available, int reserved, AllowedType allowedType) {
		CreditPool creditPool = new CreditPool();
		creditPool.creditPoolId = creditPoolId;
		creditPool.walletId = walletId;
		creditPool.balance = balance;
		creditPool.available = available;
		creditPool.reserved = reserved;
		creditPool.allowedType = allowedType;
		return creditPool;
	}

	public String getCreditPoolId() {
		return creditPoolId;
	}

	public String getWalletId() {
		return walletId;
	}

	public Integer getBalance() {
		return balance;
	}

	public Integer getAvailable() {
		return available;
	}

	public Integer getReserved() {
		return reserved;
	}
	
	public AllowedType getAllowedType() {
		return allowedType;
	}
	
	public void updateWalletId(String walletId){
		this.walletId = walletId;
	}
	
	public void consumeCredit(Integer amount, String userId){
		this.available -= amount;
		this.balance -= amount;
		setModifiedUserId(userId);
	}
	
	public void reserveCredit(Integer amount, String userId){
		this.reserved += amount;
		this.available -= amount;
		setModifiedUserId(userId);
	}

	public void cancelReservedCredit(Integer amount, String userId){
		this.reserved -= amount;
		this.available += amount;
		setModifiedUserId(userId);
	}
	
	public void reverseCredit(Integer amount){
		this.balance += amount;
		this.available += amount;
	}
	
	public void consumeReservedCredit(Integer amount, String userId){
		this.reserved -= amount;
		this.balance -= amount;
		setModifiedUserId(userId);
	}
	
	public void consumeChargeAmount(Integer reservedAmount, Integer chargeAmount, String userId) {
		if (reservedAmount > chargeAmount) {
			this.reserved -= chargeAmount;
			this.balance -= chargeAmount;
		} else {
			this.reserved -= reservedAmount;
			this.balance -= chargeAmount;
		}
		setModifiedUserId(userId);
	}
	
	public void adjustBalance(Integer amount,String userId){
		this.balance += amount;
		this.available += amount;
		setModifiedUserId(userId);
	}

	public void releaseReservedCredit(Integer amount, String userId){
		this.reserved -= amount;
		this.available += amount;
		setModifiedUserId(userId);
	}
	
	public void transferBalance(Integer amount,String userId){
		this.balance += amount;
		this.available += amount;
		setModifiedUserId(userId);
	}
	
	@Transactional(value = TxType.REQUIRES_NEW)
	public void monthlyRechargeCredit(int credit){
		this.balance +=  credit;
		this.available += credit;
	}
}
