package com.kooppi.nttca.portal.common.email.service;

import com.kooppi.nttca.wallet.common.persistence.domain.Wallet;

public interface ForfeitAlertService {

	public void checkAndSendAlert(Wallet wallet);
}
