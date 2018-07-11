package com.kooppi.nttca.portal.wallet.dto.wallet;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;

import jersey.repackaged.com.google.common.collect.Lists;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder ={"transactionReservations", "count", "offset", "maxRows"})
public class TransactionReservationCollectionDto extends ResponseResult {

	@XmlElement(name = "transactionReservations")
	private List<TransactionReservationDto> transactionReservations = Lists.newArrayList();
	
	@XmlElement(name = "count")
	private Integer count;

	@XmlElement(name = "offset")
	private Integer offset;
	
	@XmlElement(name = "maxRows")
	private Integer maxRows;
	
	public static TransactionReservationCollectionDto create() {
		return new TransactionReservationCollectionDto();
	}
	
	public static TransactionReservationCollectionDto create(List<TransactionReservationDto> transactionReservations, Integer count, Integer offset, Integer maxRows) {
		TransactionReservationCollectionDto dto = new TransactionReservationCollectionDto();
		dto.transactionReservations.addAll(transactionReservations);
		dto.count = count;
		dto.maxRows = maxRows;
		dto.offset = offset;
		return dto;
	}
	
	public List<TransactionReservationDto> getTransactionReservations() {
		return transactionReservations;
	}

	public void setTransactionReservations(List<TransactionReservationDto> transactionReservations) {
		this.transactionReservations = transactionReservations;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getMaxRows() {
		return maxRows;
	}

	public void setMaxRows(Integer maxRows) {
		this.maxRows = maxRows;
	}

	@Override
	public String getResultName() {
		return "transactionReservationList";
	}

}
