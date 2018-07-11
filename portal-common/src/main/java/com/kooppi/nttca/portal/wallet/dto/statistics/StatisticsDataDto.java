package com.kooppi.nttca.portal.wallet.dto.statistics;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;
import com.kooppi.nttca.portal.common.utils.DateUtils;

@XmlAccessorType(XmlAccessType.FIELD)
public class StatisticsDataDto extends ResponseResult{
	
	@XmlElement(name = "date")
	private String date;
	
	@XmlElement(name = "amonut")
	private Integer amonut;
	
	//for sorting
	@XmlTransient
	private LocalDateTime temporarilyDate;
	
	public StatisticsDataDto() {
	}
	
	public static StatisticsDataDto create(LocalDateTime date, Integer amount, Integer month){
		StatisticsDataDto statisticsData = new StatisticsDataDto();
		
		statisticsData.date = DateUtils.formatMonth(month);
		statisticsData.temporarilyDate =date;
		statisticsData.amonut = amount;
		return statisticsData;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getAmonut() {
		return amonut;
	}

	public void setAmonut(Integer amonut) {
		this.amonut = amonut;
	}

	public LocalDateTime getTemporarilyDate() {
		return temporarilyDate;
	}

	@Override
	public String getResultName() {
		return "statisticsDataDto";
	}

}

