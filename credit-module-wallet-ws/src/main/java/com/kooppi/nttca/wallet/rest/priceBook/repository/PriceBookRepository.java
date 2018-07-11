package com.kooppi.nttca.wallet.rest.priceBook.repository;

import java.util.List;
import java.util.Optional;

import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.wallet.common.persistence.domain.PriceBook;

public interface PriceBookRepository {

	public Optional<PriceBook> findByUid(Long uid);
	
	public Optional<PriceBook> findByPartNumber(String partNumber);
	
	public ResultList<PriceBook> searchPriceBooksByOrFilter(String globalFilter, String orderBy, String orderSorting, Integer offset, Integer maxRows);

	public List<PriceBook> findPriceBooksByPartNumber(List<String> partNumbers);
	
	public List<PriceBook> likeSearchPriceBooksByPartNumberAndProductName(String partNo, String productName);

	public List<PriceBook> getAllPriceBooks();

	public Optional<List<PriceBook>> findPriceBooksByBuName(String buName);

	public void create(PriceBook pb);
	public void update(PriceBook pb);
}
