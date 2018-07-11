package com.kooppi.nttca.portal.wallet.dto.transaction;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.google.common.collect.Lists;
import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;

public class TransactionCollectionDto extends ResponseResult{
	
	@XmlElement(name = "transactions")
	private List<TransactionDto> transactions = Lists.newArrayList(); 

	@XmlElement(name = "count")
	private Integer count;

	@XmlElement(name = "offset")
	private Integer offset;
	
	@XmlElement(name = "maxRows")
	private Integer maxRows;
	
	public static TransactionCollectionDto create(List<TransactionDto> transactions, Integer count, Integer offset, Integer maxRows) {
		TransactionCollectionDto dto  = new TransactionCollectionDto();
		dto.getTransactionDtos().addAll(transactions);
		dto.count = count;
		dto.maxRows = maxRows;
		dto.offset = offset;
		return dto;
		
	}

	public List<TransactionDto> getTransactionDtos() {
		return transactions;
	}

	public void setTransactionDtos(List<TransactionDto> transactionDtos) {
		this.transactions = transactionDtos;
	}

	public Integer getCount() {
		return count;
	}

	public Integer getOffset() {
		return offset;
	}

	public Integer getMaxRows() {
		return maxRows;
	}

	@Override
	public String getResultName() {
		return "transactionlist";
	}
	
}
