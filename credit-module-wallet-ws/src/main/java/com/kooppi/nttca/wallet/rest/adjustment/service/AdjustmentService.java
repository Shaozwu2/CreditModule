package com.kooppi.nttca.wallet.rest.adjustment.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.common.filter.context.RequestContextImpl;
import com.kooppi.nttca.portal.wallet.domain.AdjustmentStatus;
import com.kooppi.nttca.portal.wallet.domain.AdjustmentType;
import com.kooppi.nttca.portal.wallet.dto.adjustment.AdjustmentSearchDto;
import com.kooppi.nttca.wallet.common.persistence.domain.Adjustment;

public interface AdjustmentService {

	public Adjustment createAdjustment(String walletId, String refNumber, AdjustmentType adjustmentType,
			String companyCode, Integer amount, BigDecimal currencyAmount, String currencyCode, BigDecimal exchangeRate,
			String description, LocalDate contractEffectiveDate, LocalDate contractTerminationDate,
			LocalDate creditExpiryDate, String terminatedReason, List<String> buLists, List<String> productLists,
			Boolean is_all_bu, Boolean is_all_product);
	
	public Adjustment updateAdjustment(String walletId, Adjustment adjustment, String refNumber, String companyCode, Integer amount, 
			BigDecimal currencyAmount, String currencyCode, BigDecimal exchangeRate, String description, LocalDate contractEffectiveDate, 
			LocalDate contractTerminationDate, LocalDate creditExpiryDate, String terminatedReason, List<String> buLists, List<String> productLists,
			Boolean is_all_bu, Boolean is_all_product);
	
	public Optional<Adjustment> findByTransactionId(String transactionId);

	public ResultList<Adjustment> serachAdjustment(RequestContextImpl rc, String walletId, String organizationId, String transactionId, String parentTransactionId, String refNumber, AdjustmentType adjustmentType, Timestamp minTransactionTimestamp, Timestamp maxTransactionTimestamp, Integer minAmount, Integer maxAmount, AdjustmentStatus status, String sort, Integer offset, Integer maxRows, Boolean isChild);

	public ResultList<Adjustment> searchAdjustmentsByOrFilter(RequestContextImpl rc, String organizationId, String walletId, AdjustmentStatus adjustmentStatus, Boolean isExpiredCotract, String globalFilter, String sort, Integer offset, Integer maxRows, AdjustmentSearchDto searchDto);
	
	public List<Adjustment> findAllScheduledAdjustmentOnToday();
	
	public void deleteOrCancelAdjustment(Adjustment adjustment, String userId);

	public void forfeitAdjustment(Adjustment adjustment, String userId);

	public void cancelForfeitAdjustment(Adjustment adjustment, String userId);
	
	public List<Adjustment> findAllPendingAndInUseAdjustment();

	public Optional<Adjustment> findByContractId(String contractId);
}
