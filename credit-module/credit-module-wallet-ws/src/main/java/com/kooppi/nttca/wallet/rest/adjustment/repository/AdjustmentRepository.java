package com.kooppi.nttca.wallet.rest.adjustment.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.common.filter.context.RequestContextImpl;
import com.kooppi.nttca.portal.wallet.domain.AdjustmentStatus;
import com.kooppi.nttca.portal.wallet.domain.AdjustmentType;
import com.kooppi.nttca.wallet.common.persistence.domain.Adjustment;

public interface AdjustmentRepository {

	public Adjustment saveAndRefresh(Adjustment adjustment);

	public Optional<Adjustment> findByTransactionId(String transactionId);

	public ResultList<Adjustment> searchAdjustment(RequestContextImpl rc, String walletId, String organizationId, String transactionId, String parentTransactionId, String refNumber, AdjustmentType adjustmentType, LocalDateTime minTransactionDate, LocalDateTime maxTransactionDate, Integer minAmount, Integer maxAmount, AdjustmentStatus status, String orderBy, String orderSorting, Integer offset, Integer maxRows, Boolean isChild);

	public ResultList<Adjustment> searchAdjustmentsByOrFilter(RequestContextImpl rc, String organizationId, String walletId, AdjustmentStatus adjustmentStatus, Boolean isExpiredCotract, String globalFilter, String orderBy, String orderSorting, Integer offset, Integer maxRows, List<String> organizationNames);

	public ResultList<Adjustment> findChildAdjustmentByParentTransactionId(String parentTransactionId);
	
	public List<Adjustment> findAllScheduledAdjustmentOnToday();
	
	public List<Adjustment> findAllPendingAndInUseAdjustment();
	
	public void deleteAdjustment(Adjustment adjustment);

	public Optional<Adjustment> findByContractId(String contractId);

	public void updateAdjustment(Adjustment adjustment);
}
