package com.kooppi.nttca.wallet.rest.reservation.service;

import java.util.Optional;

import javax.inject.Inject;

import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.common.email.service.UtilizationAlertService;
import com.kooppi.nttca.portal.common.filter.context.RequestContextImpl;
import com.kooppi.nttca.portal.common.utils.StringUtils;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;
import com.kooppi.nttca.portal.wallet.domain.TransactionReservationStatus;
import com.kooppi.nttca.portal.wallet.dto.wallet.TransactionReservationDto;
import com.kooppi.nttca.wallet.common.persistence.domain.Transaction;
import com.kooppi.nttca.wallet.common.persistence.domain.TransactionReservation;
import com.kooppi.nttca.wallet.common.persistence.domain.Wallet;
import com.kooppi.nttca.wallet.rest.adjustment.service.AdjustmentService;
import com.kooppi.nttca.wallet.rest.reservation.repository.TransactionReservationRepository;
import com.kooppi.nttca.wallet.rest.transaction.service.TransactionService;

public class TransactionReservationServiceImpl implements TransactionReservationService {
	@Inject
	RequestContextImpl rc;
	
	@Inject
	TransactionService transactionService;
	
	@Inject
	UtilizationAlertService alertService;
	
	@Inject
	AdjustmentService adjustmentService;
	
	@Inject
	TransactionReservationRepository transactionReservationRepository;
	
	@Override
	public Optional<TransactionReservation> findTransactionReservationByTransactionId(String transactionId) {
		return transactionReservationRepository.findTransactionReservationByTransactionId(transactionId);
	}

	@Override
	public TransactionReservation createTransactionReservation(Wallet wallet,TransactionReservationDto trDto) {
		TransactionReservation transactionReservation = wallet.reserveCredit(trDto, rc);
		return transactionReservationRepository.add(transactionReservation); 
	}
	
	@Override
	public Transaction consumeReservedTransactionReservation(TransactionReservation transactionReservation, Wallet wallet) {
		findTransactionReservationInWallet(transactionReservation, wallet);
		
		Transaction transaction = wallet.consumeReservedCredit(transactionReservation, wallet, rc);
		transaction = transactionService.createTransaction(transaction);
		//consume contract credit from charge items
		wallet.consumeContractReserveCredit(transaction, transactionReservation.getChargeItems(), rc);
		//update Reservation status
		transactionReservation.setStatus(TransactionReservationStatus.COMMITED);
		transactionReservation.setModifiedUserId(rc.getRequestUserId());	
		
		//send Email Alert
		alertService.checkAndSendAlert(wallet);
		return transaction;
	}
	
//	@Override
//	public Transaction consumeChargeAmount(TransactionReservation transactionReservation, Wallet wallet, Integer chargeAmount) {
//		findTransactionReservationInWallet(transactionReservation, wallet);
//		
//		Transaction transaction = wallet.consumeChargeAmount(transactionReservation, wallet, chargeAmount, rc);
//		transaction = transactionService.createTransaction(transaction);
//		
//		//update Reservation status
//		transactionReservation.setStatus(TransactionReservationStatus.COMMITED);
//		transactionReservation.setModifiedUserId(rc.getRequestUserId());	
//		
//		//send Email Alert
//		alertService.checkAndSendAlert(wallet);
//		return transaction;
//	}

	@Override
	public void cancelReservedCredits(TransactionReservation transactionReservation, Wallet wallet) {
		findTransactionReservationInWallet(transactionReservation, wallet);
		wallet.cancelReservedCredit(transactionReservation.getAmount(), rc);
		
		//update Reservation status
		transactionReservation.setStatus(TransactionReservationStatus.DELETED);
		transactionReservation.setModifiedUserId(rc.getRequestUserId());
	}

	@Override
	public void releaseExpiredCredits(TransactionReservation transactionReservation, Wallet wallet) {
		findTransactionReservationInWallet(transactionReservation, wallet);
		wallet.releaseExpiredCredit(transactionReservation.getAmount(), rc.getRequestUserId());
		
		//update Reservation status
		transactionReservation.setStatus(TransactionReservationStatus.EXPIRED);
		transactionReservation.setModifiedUserId(rc.getRequestUserId());
	}

	public void findTransactionReservationInWallet(TransactionReservation transactionReservation, Wallet wallet) {
		PortalExceptionUtils.throwIfFalse(transactionReservation.getWalletId().equals(wallet.getWalletId()), PortalErrorCode.WALLET_DONT_HAVE_THE_TRANSACTION_RESERVATION);
	}

	@Override
	public ResultList<TransactionReservation> searchTransactionReservation(RequestContextImpl rc, String walletId,
			String sort, Integer offset, Integer maxRows) {
		String orderBy = null;
		String orderSorting = null;
		if (!sort.isEmpty() && sort != null) {
			orderBy = StringUtils.parseQueryParam(sort).get("orderBy");
			orderSorting = StringUtils.parseQueryParam(sort).get("orderSorting");
		}
		
		return transactionReservationRepository.findTransactionReservations(rc, walletId, orderBy, orderSorting, offset, maxRows);
	}

}
