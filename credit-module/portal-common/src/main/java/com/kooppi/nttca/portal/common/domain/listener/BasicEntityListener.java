package com.kooppi.nttca.portal.common.domain.listener;

import javax.inject.Inject;
import javax.persistence.PrePersist;

import com.kooppi.nttca.portal.common.domain.BasicEntity;
import com.kooppi.nttca.portal.common.filter.request.RequestContext;

public class BasicEntityListener {

	@Inject
	private RequestContext rc;
	
	@PrePersist
	public void prePersist(BasicEntity entity){
		if (rc != null) {
			entity.prepareCreateAudit(rc.getRequestUserId());
		}
	}

}
