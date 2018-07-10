package com.kooppi.nttca.wallet.common.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.kooppi.nttca.portal.common.domain.BasicEntity;

/**
 * 
 */
@Entity
//@Table(name = "wallet_permission")
public class WalletPermission extends BasicEntity {

	private static final long serialVersionUID = -5246525376802672104L;
	
	@Column(name = "wallet_id")
	private String walletId;
	
	@Column(name = "user_id")
	private String userId;

	public static WalletPermission create(String walletId, String userId) {
		WalletPermission walletPermission = new WalletPermission();
		walletPermission.walletId = walletId;
		walletPermission.userId = userId;
		return walletPermission;
	}

	public String getWalletId() {
		return walletId;
	}

	public String getUserId() {
		return userId;
	}
	
	public void updateWalletId(String walletId){
		this.walletId = walletId;
	}
}
