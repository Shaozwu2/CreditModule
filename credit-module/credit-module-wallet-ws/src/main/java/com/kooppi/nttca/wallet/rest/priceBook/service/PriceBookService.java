package com.kooppi.nttca.wallet.rest.priceBook.service;

import java.util.List;
import java.util.Optional;

import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.wallet.dto.priceBook.GetPriceBookByPartNoDto;
import com.kooppi.nttca.portal.wallet.dto.priceBook.PriceBookItemDto;
import com.kooppi.nttca.wallet.common.persistence.domain.PriceBook;

public interface PriceBookService {

	public Optional<PriceBook> findByUid(Long uid);
	
	public Optional<PriceBook> findByPartNumber(String partNumber);
	
	public ResultList<PriceBook> searchPriceBooksByOrFilter(String globalFilter, String sort, Integer offset, Integer maxRows);

	public List<PriceBookItemDto> findPriceBooksByPartNumber(GetPriceBookByPartNoDto partNo);

	public List<PriceBook> likeSearchPriceBooksByPartNumberAndProductName(String partNo, String productName);

	public List<PriceBook> getAllPriceBooks();

	public Optional<List<PriceBook>> findByBuName(String buName);

	public void createPricebook(PriceBook pb);
	public void updatePricebook(PriceBook pb);

	public void createOrUpdatePricebookFromEms(String mqMessage);
}
