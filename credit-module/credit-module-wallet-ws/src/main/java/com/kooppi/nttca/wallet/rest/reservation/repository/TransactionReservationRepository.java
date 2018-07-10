package com.kooppi.nttca.wallet.rest.reservation.repository;

import java.util.List;
import java.util.Optional;

import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.common.filter.context.RequestContextImpl;
import com.kooppi.nttca.wallet.common.persistence.domain.TransactionReservation;

public interface TransactionReservationRepository {

	public TransactionReservation add(TransactionReservation cm);

	public Optional<TransactionReservation> findTransactionReservationByTransactionId(String transactionId);
	
	public List<TransactionReservation> findAllReservedTransactionReservations();
	
	public ResultList<TransactionReservation> findTransactionReservations(RequestContextImpl rc, String walletId, String orderBy, String orderSorting, Integer offset, Integer maxRows);
}
