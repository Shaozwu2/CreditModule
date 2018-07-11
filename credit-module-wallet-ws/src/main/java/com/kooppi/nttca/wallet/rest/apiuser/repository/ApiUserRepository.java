package com.kooppi.nttca.wallet.rest.apiuser.repository;

import java.util.List;
import java.util.Optional;

import com.kooppi.nttca.wallet.common.persistence.domain.ApiUser;

public interface ApiUserRepository {
	
	public Optional<ApiUser> findApiUser(String username);

	public List<ApiUser> findAllApiUsers();

}
