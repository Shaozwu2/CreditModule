package com.kooppi.nttca.wallet.rest.wallet.service;

import java.time.LocalDate;
import java.util.Optional;

import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.common.filter.context.RequestContextImpl;
import com.kooppi.nttca.portal.wallet.domain.IdleUnit;
import com.kooppi.nttca.portal.wallet.domain.WalletStatus;
import com.kooppi.nttca.portal.wallet.domain.WalletType;
import com.kooppi.nttca.wallet.common.persistence.domain.Wallet;

public interface WalletService {

	public Optional<Wallet> findWalletById(String walletId);

	public Wallet createWallet(String organizationId, String organizationName, String parentOrganizationId, String parentOrganizationName, Integer maxIdlePeriod, IdleUnit idleUnit, 
			LocalDate expiredOn, Integer creditBuffer, boolean isOneTime, Integer utilizationAlert1Threshold, String utilizationAlert1Receivers, String utilizationAlert1Bccs, 
			Integer utilizationAlert2Threshold, String utilizationAlert2Receivers, String utilizationAlert2Bcc, Integer forfeitAlert1Threshold, 
			String forfeitAlert1Receivers, String forfeitAlert1Bcc, Integer forfeitAlert2Threshold, String forfeitAlert2Receivers, String forfeitAlert2Bcc);
	
	public Wallet updateWallet(Wallet wallet, WalletStatus status, Integer maxIdlePeriod, IdleUnit idleUnit, Integer creditBuffer, Integer remainBuffer, boolean isOneTime, 
			Integer utilizationAlert1Threshold, String utilizationAlert1Receivers, String utilizationAlert1Bccs, Integer utilizationAlert2Threshold, 
			String utilizationAlert2Receivers, String utilizationAlert2Bccs, Integer forfeitAlert1Threshold, String forfeitAlert1Receivers, 
			String forfeitAlert1Bccs, Integer forfeitAlert2Threshold, String forfeitAlert2Receivers, String forfeitAlert2Bccs);
	
	public void lockIdleWallet(Wallet wallet, String userName);

//	public Transaction consumeCreditsDirectly(Wallet wallet, ConsumeCreditDto dto);
	
	public ResultList<Wallet> searchWalletByCustomer(String organizationId);
	
	public ResultList<Wallet> searchWalletsByAndFilters(RequestContextImpl rc, String organizationId, String organizationName, String userId, String parentOrganizationName, 
			String parentOrganizationId, String walletId, String walletName, WalletType walletType, WalletStatus status, Integer minBalance, 
			Integer maxBalance, Integer minAvailable, Integer maxAvailable, Integer minCreditBuffer, Integer maxCreditBuffer,
			Integer minAlertThreshold, Integer maxAlertThreshold, String sort, Integer offset, Integer maxRows);

	public ResultList<Wallet> searchWalletsByOrFilter(String organizationId, String globalFilter, String sort, Integer offset, Integer maxRows);

}
