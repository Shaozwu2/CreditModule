package com.kooppi.nttca.wallet.rest.tnc.service;

import java.util.List;
import java.util.Optional;

import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.exception.domain.PortalException;
import com.kooppi.nttca.wallet.common.persistence.domain.TnC;
import com.kooppi.nttca.wallet.common.persistence.domain.TnCToPricebook;

public interface TnCTemplateService {

	public Optional<TnC> findByUid(Long uid);

	public ResultList<TnC> searchTnCsByOrFilter(String globalFilter, String sort, Integer offset, Integer maxRows);
	
	public TnC createTnc(String name, String description, String buName, boolean isDefault, boolean isVisible, String fileName, Integer size, String attachmentData);

	public void deleteTnC(TnC tnc);
	
	public TnCToPricebook createTnCToPricebook(Integer tncId, String buName, String partNo, String organizationId, boolean isDefaultBu, boolean isAllCustomer, boolean isAllBu, boolean isAllProduct);
	
	public ResultList<TnCToPricebook> searchTnCToPricebooksByAndFilter(String buFilter, String partNoFilter, String productFilter, String customerFilter, String documentFilter,
			String sort, Integer offset, Integer maxRows);

	public Optional<TnCToPricebook> searchTnCToPricebooksByCustomerIdAndPartNo(String buname, String partNo, String organizationId);

	public List<String> searchTnCNameByPartNoAndCustomerList(String buname, String partNo, List<String> customers);

	public byte[] getTnCFile(TnC tnc) throws PortalException;
	
	public Optional<TnCToPricebook> findTncToPricebookByUid(Long uid);
	
	public void deleteTnCToPricebook(TnCToPricebook tnCToPricebook);
	
	public TnC findDefaultTnC();
	
	public void swapTnCMappingPriority(Long uid1, Long uid2);
	
	public String createFile(String fileName, String attachmentData);

}
