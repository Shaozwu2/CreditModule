package com.kooppi.nttca.ce.payment.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.kooppi.nttca.ce.domain.Payment;
import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.ce.dto.payment.CreatePaymentDto;
import com.kooppi.nttca.portal.ce.dto.payment.PaymentDto;

public interface PaymentService {

	public Optional<Payment> findByPaymentId(String paymentId);
	
	public Payment createRealTimePayment(CreatePaymentDto createPaymentDto);
	
	public Payment reservePayment(CreatePaymentDto createPaymentDto);
	
	public Payment confirmPayment(String paymentId, String description);
		
	public Payment cancelReservedPayment(String paymentId, String description);
	
	public PaymentDto refundPayment(String paymentId, String description);
	
	public ResultList<Payment> searchByOrFilters(String globalFilter, String sort, Integer offset, Integer maxRows, List<String> organizationNames);
	
	public ResultList<Payment> searchByAndFilters(String organizationId, String paymentId, LocalDate transactionDate, String sort, Integer offset, Integer maxRows);
}
