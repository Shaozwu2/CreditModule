package com.kooppi.nttca.ce.payment.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.kooppi.nttca.ce.domain.Payment;

public interface PaymentRepository {

	public Optional<Payment> findByUid(Long uid);
	
	public Optional<Payment> findByPaymentId(String paymentId);

	public Payment saveAndRefresh(Payment payment);
	
	public ResultList<Payment> searchByOrFilter(String globalFilter, String orderBy, String orderSorting, Integer offset, Integer maxRows, List<String> organizationNames);
	
	public ResultList<Payment> searchByAndFilter(String organizationId, String paymentId, LocalDate transactionDate, String orderBy, String orderSorting, Integer offset, Integer maxRows);
	
	public Object[] callCreateRealTimePaymentStoredProcedure(String walletId, String partNoList, String chargeTypeList, String unitPriceList, String quantityList, String description, String userId, String requestId) throws Exception;
}
