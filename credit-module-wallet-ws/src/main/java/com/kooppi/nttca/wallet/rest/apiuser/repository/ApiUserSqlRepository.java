package com.kooppi.nttca.wallet.rest.apiuser.repository;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.kooppi.nttca.portal.common.repository.BasicRepository;
import com.kooppi.nttca.wallet.common.persistence.domain.ApiUser;
import com.kooppi.nttca.wallet.persistence.producer.WalletDataSource;

@ApplicationScoped
public class ApiUserSqlRepository extends BasicRepository  implements ApiUserRepository {
	
	private EntityManager em;

	public ApiUserSqlRepository() {}
	
	@Inject
	public ApiUserSqlRepository(@WalletDataSource EntityManager em) {
		this.em = em;
	}

	@Override
	public Optional<ApiUser> findApiUser(String username) {
		return Optional.ofNullable(em.find(ApiUser.class, username));
	}

	@Override
	public List<ApiUser> findAllApiUsers() {
		StringBuffer sb = new StringBuffer(" from ApiUser a order by a.username ");
		String sql = sb.toString();
		TypedQuery<ApiUser> tq = em.createQuery(sql, ApiUser.class);
		return tq.getResultList();
	}

}
