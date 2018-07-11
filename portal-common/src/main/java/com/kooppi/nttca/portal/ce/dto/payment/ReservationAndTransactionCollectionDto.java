package com.kooppi.nttca.portal.ce.dto.payment;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;

import jersey.repackaged.com.google.common.collect.Lists;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder ={"reservationAndTransactions", "count", "offset", "maxRows"})
public class ReservationAndTransactionCollectionDto extends ResponseResult {

	@Override
	public String getResultName() {
		return "reservationAndTransactionList";
	}

	@XmlElement(name = "reservationAndTransactions")
	private List<ReservationAndTransactionDto> reservationAndTransactions = Lists.newArrayList();
	
	@XmlElement(name = "count")
	private Integer count;

	@XmlElement(name = "offset")
	private Integer offset;
	
	@XmlElement(name = "maxRows")
	private Integer maxRows;
	
	public static ReservationAndTransactionCollectionDto create() {
		return new ReservationAndTransactionCollectionDto();
	}
	
	public static ReservationAndTransactionCollectionDto create(List<ReservationAndTransactionDto> reservationAndTransactions, Integer count, Integer offset, Integer maxRows) {
		ReservationAndTransactionCollectionDto dto = new ReservationAndTransactionCollectionDto();
		dto.reservationAndTransactions = reservationAndTransactions;
		dto.count = count;
		dto.maxRows = maxRows;
		dto.offset = offset;
		return dto;
	}

	public List<ReservationAndTransactionDto> getReservationAndTransactions() {
		return reservationAndTransactions;
	}

	public void setReservationAndTransactions(List<ReservationAndTransactionDto> reservationAndTransactions) {
		this.reservationAndTransactions = reservationAndTransactions;
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
}
