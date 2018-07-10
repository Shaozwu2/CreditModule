package com.kooppi.nttca.wallet.rest.reservation.service;

import java.util.Optional;

import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.common.filter.context.RequestContextImpl;
import com.kooppi.nttca.portal.wallet.dto.wallet.TransactionReservationDto;
import com.kooppi.nttca.wallet.common.persistence.domain.Transaction;
import com.kooppi.nttca.wallet.common.persistence.domain.TransactionReservation;
import com.kooppi.nttca.wallet.common.persistence.domain.Wallet;

public interface TransactionReservationService {

	public ResultList<TransactionReservation> searchTransactionReservation(RequestContextImpl rc, String walletId, String sort, Integer offset, Integer maxRows);
	
	public Optional<TransactionReservation> findTransactionReservationByTransactionId(String transactionId);
	
	public TransactionReservation createTransactionReservation(Wallet wallet,TransactionReservationDto trDto);
	
	public Transaction consumeReservedTransactionReservation(TransactionReservation transactionReservation, Wallet wallet);
	
//	public Transaction consumeChargeAmount(TransactionReservation transactionReservation, Wallet wallet, Integer chargeAmount);
	
	public void cancelReservedCredits(TransactionReservation transactionReservation, Wallet wallet);
	
	public void releaseExpiredCredits(TransactionReservation transactionReservation, Wallet wallet);
}
