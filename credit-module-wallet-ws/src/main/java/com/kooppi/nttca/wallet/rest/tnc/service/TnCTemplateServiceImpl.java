package com.kooppi.nttca.wallet.rest.tnc.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.google.common.base.Strings;
import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.common.config.file.ConfigurationValue;
import com.kooppi.nttca.portal.common.utils.IOUtils;
import com.kooppi.nttca.portal.common.utils.StringUtils;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalException;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;
import com.kooppi.nttca.wallet.common.persistence.domain.TnC;
import com.kooppi.nttca.wallet.common.persistence.domain.TnCToPricebook;
import com.kooppi.nttca.wallet.rest.tnc.repository.TnCTemplateRepository;

import jersey.repackaged.com.google.common.collect.Lists;

@ApplicationScoped
public class TnCTemplateServiceImpl implements TnCTemplateService {

	@Inject
	@ConfigurationValue(property = "portal.wallet.tnc.template.basepath", defaultValue = "/root")
	private String tncBasePath;
	
	@Inject
	private TnCTemplateRepository tncRepository;
	
	@Override
	public Optional<TnC> findByUid(Long uid) {
		return tncRepository.findById(uid);
	}

	@Override
	public ResultList<TnC> searchTnCsByOrFilter(String globalFilter, String sort, Integer offset,
			Integer maxRows) {
		String orderBy = null;
		String orderSorting = null;
		if (!sort.isEmpty() && sort != null) {
			orderBy = StringUtils.parseQueryParam(sort).get("orderBy");
			orderSorting = StringUtils.parseQueryParam(sort).get("orderSorting");
		}
		
		return tncRepository.searchTnCsByOrFilter(globalFilter, orderBy, orderSorting, offset, maxRows);
	}

	@Override
	public TnC createTnc(String templateName, String description, String buName, boolean isDefault, boolean isVisible, String uploadedName, Integer size, String base64data) {
		PortalExceptionUtils.throwIfFalse(StringUtils.validateFileExtn(uploadedName), PortalErrorCode.FILE_TYPE_NOT_ALLOWED);
		
		// uploadedName + "_" + random UUID = fileName
		String fileName = uploadedName.substring(0, uploadedName.lastIndexOf("."))
				.concat("_")
				.concat(UUID.randomUUID().toString())
				.concat(uploadedName.substring(uploadedName.lastIndexOf("."), uploadedName.length()));
		
		IOUtils.createFile(tncBasePath, base64data, fileName);
		TnC tnc = TnC.create(templateName, buName, description, isDefault, isVisible, uploadedName, fileName);
		return tncRepository.saveAndRefresh(tnc);
	}
	
	@Override
	public String createFile(String fileName, String attachmentData) {
		PortalExceptionUtils.throwIfFalse(StringUtils.validateFileExtn(fileName), PortalErrorCode.FILE_TYPE_NOT_ALLOWED);
		
		// finalFileName = fileName + "_" + random UUID
		String finalFileName = fileName.substring(0, fileName.lastIndexOf(".")).concat("_")
				.concat(UUID.randomUUID().toString())
				.concat(fileName.substring(fileName.lastIndexOf("."), fileName.length()));

		IOUtils.createFile(tncBasePath, attachmentData, finalFileName);
		
		return finalFileName;
	}
	
	@Override
	public void deleteTnC(TnC tnc) {
		
		//delete file if exist
		IOUtils.createParentFoldersIfNotExist(tncBasePath);
		File file = new File(tncBasePath + "/" +tnc.getFileName());
		try {
			Files.deleteIfExists(file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		//delete record
		tncRepository.deleteTnC(tnc);
	}
	
	@Override
	public TnCToPricebook createTnCToPricebook(Integer tncId, String buName, String partNo, String organizationId, boolean isDefaultBu, boolean isAllCustomer, boolean isAllBu, boolean isAllProduct) {
		TnCToPricebook tnCToPricebook = TnCToPricebook.create(tncId, buName, partNo, organizationId, isDefaultBu, isAllCustomer, isAllBu, isAllProduct);
		return tncRepository.saveAndRefresh(tnCToPricebook);
	}
	
	@Override
	public ResultList<TnCToPricebook> searchTnCToPricebooksByAndFilter(String buFilter, String partNoFilter, String productFilter, String customerFilter, String documentFilter,
			String sort, Integer offset, Integer maxRows) {
		String orderBy = null;
		String orderSorting = null;
		if (!Strings.isNullOrEmpty(sort)) {
			orderBy = StringUtils.parseQueryParam(sort).get("orderBy");
			orderSorting = StringUtils.parseQueryParam(sort).get("orderSorting");
		}
		
		return tncRepository.searchTnCToPricebooksByAndFilter(buFilter, partNoFilter, productFilter, customerFilter, documentFilter,
				orderBy, orderSorting, offset, maxRows);
	}

	@Override
	public byte[] getTnCFile(TnC tnc) throws PortalException{
		byte[] result = null;
		IOUtils.createParentFoldersIfNotExist(tncBasePath);
		File file = new File(tncBasePath + "/" + tnc.getFileName());
		try {
			result = Files.readAllBytes(file.toPath());
		} catch (NoSuchFileException e) {
			//can not find TnC in the system
			PortalExceptionUtils.throwNow(PortalErrorCode.UNABLE_TO_FIND_TNC_IN_FILE_SYSTEM);
		} catch (IOException e) {
			PortalExceptionUtils.throwNow(PortalErrorCode.INTERNAL_SERVER_ERROR);
		}
		return result;
	}
	
	@Override
	public Optional<TnCToPricebook> findTncToPricebookByUid(Long uid) {
		return tncRepository.findTncToPricebookByUid(uid);
	}
	
	@Override
	public void deleteTnCToPricebook(TnCToPricebook tnCToPricebook) {
		tncRepository.deleteTnCToPricebook(tnCToPricebook);
	}
	
	@Override
	public TnC findDefaultTnC() {
		return tncRepository.findDefaultTnC();
	}

	@Override
	public Optional<TnCToPricebook> searchTnCToPricebooksByCustomerIdAndPartNo(String buName, String partNo, String organizationId) {
		Optional<TnCToPricebook> optResult = Optional.empty();
		List<TnCToPricebook> tNcToPricebookResultList = tncRepository.searchTnCToPricebooksByCustomerIdAndPartNo(buName, organizationId, partNo);
		if (!tNcToPricebookResultList.isEmpty())
			optResult = Optional.of(tNcToPricebookResultList.get(0));
		return optResult;
	}
	
	@Override
	public void swapTnCMappingPriority(Long uid1, Long uid2) {
		tncRepository.swapTnCMappingPriority(uid1, uid2);
	}
	
	@Override
	public List<String> searchTnCNameByPartNoAndCustomerList(String buName, String partNo, List<String> customers) {
		List<String> resultList= Lists.newArrayList();
		for (String customer : customers) {
			TnCToPricebook tnCToPricebook = tncRepository.searchTnCToPricebooksByCustomerIdAndPartNo(buName, customer, partNo).get(0);
			resultList.add(tnCToPricebook.getTnc().getTemplateName());
		}
		return resultList;
	}
}
