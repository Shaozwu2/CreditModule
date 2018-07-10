package com.kooppi.nttca.wallet.config.rest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.core.HazelcastInstance;
import com.kooppi.nttca.portal.common.producer.rest.HazelCast;
import com.kooppi.nttca.wallet.common.persistence.domain.ApiUser;
import com.kooppi.nttca.wallet.rest.apiuser.repository.ApiUserRepository;

@ApplicationScoped
public class ApiUserManagerImpl implements ApiUserManager {

	private static final String API_USER_MAP_KEY = "apiUser";
	
	private static Logger logger = LoggerFactory.getLogger(ApiUserManagerImpl.class);
	
	@Inject
	private ApiUserRepository apiUserRepository;

	@Inject
	@HazelCast
	private HazelcastInstance client;

	@Override
	public void cacheAllApiUsers() {
		List<ApiUser> apiUsers = apiUserRepository.findAllApiUsers();
		apiUsers.forEach(user -> {
			Map<String, ApiUser> apiUserMap = getApiUserMap();
			logger.info("input username:{}, password:{}", user.getUsername(), user.getPassword());
			apiUserMap.put(user.getUsername(), user);
		});
	}

	@Override
	public Optional<ApiUser> findApiUser(String userName) {
		Map<String, ApiUser> apiUserMap = getApiUserMap();
		Optional<ApiUser> optApiUser = Optional.of(apiUserMap.get(userName));
		return optApiUser;
	}

	private Map<String, ApiUser> getApiUserMap() {
		return client.getMap(API_USER_MAP_KEY);
	}

}
