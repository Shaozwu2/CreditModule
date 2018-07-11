package com.kooppi.nttca.wallet.rest.wallet.repository;

import java.util.List;
import java.util.Optional;

import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.common.filter.context.RequestContextImpl;
import com.kooppi.nttca.portal.wallet.domain.WalletStatus;
import com.kooppi.nttca.portal.wallet.domain.WalletType;
import com.kooppi.nttca.wallet.common.persistence.domain.Wallet;

public interface WalletRepository {

	public Optional<Wallet> findWalletById(String walletId);
	
	public Optional<Wallet> findMainWalletByOrganizationId(String organizationId);

	public Wallet add(Wallet cm);
	
	public List<Wallet> findWalletByOrganizationId(String organizationId);

	public List<Wallet> findAllWallets();
	
//	public void adjustBalance(Wallet wallet,Integer adjustment);

	public ResultList<Wallet> searchWalletByCustomer(String organizationId);
	
	public ResultList<Wallet> searchWalletsByAndFilters(RequestContextImpl rc, String organizationId, String organizationName, String userId, String parentOrganizationName, 
			String parentOrganizationId, String walletId, String walletName, WalletType walletType, WalletStatus status, Integer minBalance, 
			Integer maxBalance, Integer minAvailable, Integer maxAvailable, Integer minCreditBuffer, Integer maxCreditBuffer,
			Integer minAlertThreshold, Integer maxAlertThreshold, String orderBy, String orderSorting, Integer offset, Integer maxRows);

	public ResultList<Wallet> searchWalletsByOrFilter(String organizationId, String globalFilter, String orderBy, String orderSorting, Integer offset, Integer maxRows);
}
