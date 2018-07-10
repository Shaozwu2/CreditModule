package com.kooppi.nttca.portal.common.domain.listener;

import javax.inject.Inject;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.kooppi.nttca.portal.common.domain.Modifiable;
import com.kooppi.nttca.portal.common.filter.request.RequestContext;

public class ModifiableEntityListener {

	@Inject
	private RequestContext rc;
	
	@PrePersist
	public void prePersist(Modifiable entity){
		if (rc != null) {
			entity.prepareCreateAudit(rc.getRequestUserId());
		}
	}

	@PreUpdate
	void beforeUpdate(Modifiable entity) {
		if (rc != null) {
			entity.prepareUpdateAudit(rc.getRequestUserId());
		}
	}
}
