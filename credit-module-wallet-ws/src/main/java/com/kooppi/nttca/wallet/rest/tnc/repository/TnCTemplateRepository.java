package com.kooppi.nttca.wallet.rest.tnc.repository;

import java.util.List;
import java.util.Optional;

import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.wallet.common.persistence.domain.TnC;
import com.kooppi.nttca.wallet.common.persistence.domain.TnCToPricebook;

public interface TnCTemplateRepository {

	public Optional<TnC> findById(Long uid);
	
	public ResultList<TnC> searchTnCsByOrFilter(String globalFilter, String orderBy, String orderSorting, Integer offset, Integer maxRows);
	
	public TnC saveAndRefresh(TnC tnc);
	
	public void deleteTnC(TnC tnc);
	
	public TnCToPricebook saveAndRefresh(TnCToPricebook tnCToPricebook);
	
	public ResultList<TnCToPricebook> searchTnCToPricebooksByAndFilter(String buFilter, String partNoFilter, String productFilter, String customerFilter, String documentFilter,
			String orderBy, String orderSorting, Integer offset, Integer maxRows);

	public List<TnCToPricebook> searchTnCToPricebooksByCustomerIdAndPartNo(String buName, String organizationId, String partNo);
	
	public Optional<TnCToPricebook> findTncToPricebookByUid(Long uid);
	
	public void deleteTnCToPricebook(TnCToPricebook tnCToPricebook);
	
	public TnC findDefaultTnC();
	
	public void swapTnCMappingPriority(Long uid1, Long uid2);
}
