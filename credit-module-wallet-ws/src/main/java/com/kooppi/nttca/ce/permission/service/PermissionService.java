package com.kooppi.nttca.ce.permission.service;

import com.kooppi.nttca.ce.domain.Payment;
import com.kooppi.nttca.portal.common.filter.request.RequestContext;
import com.kooppi.nttca.wallet.common.persistence.domain.Wallet;

public interface PermissionService {

	public boolean hasVeiwPaymentPermission(RequestContext rc, Payment payment); 
	
	public boolean hasCreatePaymentPermission(RequestContext rc, Wallet wallet);
	
	public boolean hasConfirmPaymentPermission(RequestContext rc, Payment payment);
	
	public boolean hasCancelReservedPaymentPermission(RequestContext rc, Payment payment);
	
	public boolean hasRefundPaymentPermission(RequestContext rc, Payment payment);



}
