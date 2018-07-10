package com.kooppi.nttca.wallet.config.rest;

import java.util.Optional;

import com.kooppi.nttca.wallet.common.persistence.domain.ApiUser;

public interface ApiUserManager {
	public void cacheAllApiUsers();
	public Optional<ApiUser> findApiUser(String userName);
}
