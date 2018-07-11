package com.kooppi.nttca.ce.permission.service.impl;

import javax.enterprise.context.ApplicationScoped;

import com.kooppi.nttca.ce.domain.Payment;
import com.kooppi.nttca.ce.permission.service.PermissionService;
import com.kooppi.nttca.portal.common.filter.request.RequestContext;
import com.kooppi.nttca.wallet.common.persistence.domain.Wallet;

@ApplicationScoped
public class PermissionServicePortalRoleImpl implements PermissionService{

	@Override
	public boolean hasVeiwPaymentPermission(RequestContext rc, Payment payment) {
		/**
		 * Check Service has permission
		 * Check user has permission
		 */
//		validateService(rc.getAppId(),payment);
		return true;
	}

	@Override
	public boolean hasCreatePaymentPermission(RequestContext rc, Wallet wallet) {
		return true;
	}


	@Override
	public boolean hasConfirmPaymentPermission(RequestContext rc, Payment payment) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean hasCancelReservedPaymentPermission(RequestContext rc, Payment payment) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean hasRefundPaymentPermission(RequestContext rc, Payment payment) {
		// TODO Auto-generated method stub
		return true;
	}


}
