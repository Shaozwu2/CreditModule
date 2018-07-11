package com.kooppi.nttca.wallet.rest.wallet.service;

import java.time.LocalDate;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.common.filter.context.RequestContextImpl;
import com.kooppi.nttca.portal.common.utils.StringUtils;
import com.kooppi.nttca.portal.wallet.domain.IdleUnit;
import com.kooppi.nttca.portal.wallet.domain.WalletStatus;
import com.kooppi.nttca.portal.wallet.domain.WalletType;
import com.kooppi.nttca.wallet.common.persistence.domain.Wallet;
import com.kooppi.nttca.wallet.rest.wallet.repository.WalletRepository;

@ApplicationScoped
public class WalletServiceImpl implements WalletService{

	@Inject
	private WalletRepository walletRepository;
	
	@Inject
	private RequestContextImpl rc;
	
	@Override
	public Optional<Wallet> findWalletById(String walletId) {
		return walletRepository.findWalletById(walletId);
	}
	
	@Override
	public Wallet createWallet(String organizationId, String organizationName, String parentOrganizationId, String parentOrganizationName, Integer maxIdlePeriod, IdleUnit idleUnit, 
			LocalDate expiredOn, Integer creditBuffer, boolean isOneTime, Integer utilizationAlert1Threshold, String utilizationAlert1Receivers, String utilizationAlert1Bccs, 
			Integer utilizationAlert2Threshold, String utilizationAlert2Receivers, String utilizationAlert2Bcc, Integer forfeitAlert1Threshold, String forfeitAlert1Receivers, 
			String forfeitAlert1Bcc, Integer forfeitAlert2Threshold, String forfeitAlert2Receivers, String forfeitAlert2Bcc) {
		
		maxIdlePeriod = maxIdlePeriod == null ? 0 : maxIdlePeriod;
		
		Wallet wallet = Wallet.create(organizationId, organizationName, maxIdlePeriod, idleUnit, expiredOn,
				creditBuffer, isOneTime, utilizationAlert1Threshold, utilizationAlert1Receivers, utilizationAlert1Bccs,
				utilizationAlert2Threshold, utilizationAlert2Receivers, utilizationAlert2Bcc, forfeitAlert1Threshold,
				forfeitAlert1Receivers, forfeitAlert1Bcc, forfeitAlert2Threshold, forfeitAlert2Receivers, forfeitAlert2Bcc,rc.getRequestUserId());
		return walletRepository.add(wallet);
	}
	
	@Transactional(value = TxType.REQUIRES_NEW)
	@Override
	public void lockIdleWallet(Wallet wallet, String userName) {
		wallet.disable(userName);
	}

	@Override
	public Wallet updateWallet(Wallet wallet, WalletStatus status, Integer maxIdlePeriod, IdleUnit idleUnit, Integer creditBuffer, Integer remainBuffer, boolean isOneTime, 
			Integer utilizationAlert1Threshold, String utilizationAlert1Receivers, String utilizationAlert1Bccs, Integer utilizationAlert2Threshold, 
			String utilizationAlert2Receivers, String utilizationAlert2Bccs, Integer forfeitAlert1Threshold, String forfeitAlert1Receivers, 
			String forfeitAlert1Bccs, Integer forfeitAlert2Threshold, String forfeitAlert2Receivers, String forfeitAlert2Bccs) {
		
		maxIdlePeriod = maxIdlePeriod == null ? 0 : maxIdlePeriod;
		
		wallet.updateWallet(status, maxIdlePeriod, idleUnit, creditBuffer, remainBuffer, isOneTime, utilizationAlert1Threshold, utilizationAlert1Receivers, utilizationAlert1Bccs, 
				utilizationAlert2Threshold, utilizationAlert2Receivers, utilizationAlert2Bccs, forfeitAlert1Threshold, forfeitAlert1Receivers, forfeitAlert1Bccs,
				forfeitAlert2Threshold, forfeitAlert2Receivers, forfeitAlert2Bccs, rc.getRequestUserId());
		return wallet;
	}

	@Override
	public ResultList<Wallet> searchWalletByCustomer(String organizationId) {
		return walletRepository.searchWalletByCustomer(organizationId);
	}

	@Override
	public ResultList<Wallet> searchWalletsByAndFilters(RequestContextImpl rc, String organizationId, String organizationName, String userId,
			String parentOrganizationName, String parentOrganizationId, String walletId, String walletName, WalletType walletType, WalletStatus status,
			Integer minBalance, Integer maxBalance, Integer minAvailable, Integer maxAvailable, Integer minCreditBuffer,
			Integer maxCreditBuffer, Integer minAlertThreshold, Integer maxAlertThreshold, String sort, Integer offset, Integer maxRows) {
			
		// parse "orderBy=orderSorting"
		String orderBy = null;
		String orderSorting = null;
		if (!sort.isEmpty() && sort != null) {
			orderBy = StringUtils.parseQueryParam(sort).get("orderBy");
			orderSorting = StringUtils.parseQueryParam(sort).get("orderSorting");
		}
		
		return walletRepository.searchWalletsByAndFilters(rc, organizationId, organizationName, userId, parentOrganizationName,
				parentOrganizationId, walletId, walletName, walletType, status, minBalance, maxBalance, minAvailable, maxAvailable, minCreditBuffer,
				maxCreditBuffer, minAlertThreshold, maxAlertThreshold, orderBy, orderSorting, offset, maxRows);
	}

	@Override
	public ResultList<Wallet> searchWalletsByOrFilter(String organizationId, String globalFilter, String sort, Integer offset, Integer maxRows) {
		// parse "orderBy=orderSorting"
				String orderBy = null;
				String orderSorting = null;
				if (!sort.isEmpty() && sort != null) {
					orderBy = StringUtils.parseQueryParam(sort).get("orderBy");
					orderSorting = StringUtils.parseQueryParam(sort).get("orderSorting");
				}
			return walletRepository.searchWalletsByOrFilter(organizationId, globalFilter, orderBy, orderSorting, offset, maxRows);
	}
}
