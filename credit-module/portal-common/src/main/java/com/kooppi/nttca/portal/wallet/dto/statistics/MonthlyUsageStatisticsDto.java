package com.kooppi.nttca.portal.wallet.dto.statistics;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;

@XmlAccessorType(XmlAccessType.FIELD)
public class MonthlyUsageStatisticsDto extends ResponseResult{

	@XmlElement(name = "availableAmount")
	@ApiModelProperty(required = false, hidden=true)
	private Integer availableAmount;
	
	@XmlElement(name = "d1")
	@ApiModelProperty(required = false, hidden=true)
	private StatisticsDataDto data1;
	
	@XmlElement(name = "d2")
	@ApiModelProperty(required = false, hidden=true)
	private StatisticsDataDto data2;
	
	@XmlElement(name = "d3")
	@ApiModelProperty(required = false, hidden=true)
	private StatisticsDataDto data3;

	@XmlElement(name = "d4")
	@ApiModelProperty(required = false, hidden=true)
	private StatisticsDataDto data4;
	
	@XmlElement(name = "d5")
	@ApiModelProperty(required = false, hidden=true)
	private StatisticsDataDto data5;
	
	@XmlElement(name = "d6")
	@ApiModelProperty(required = false, hidden=true)
	private StatisticsDataDto data6;
	
	public static MonthlyUsageStatisticsDto create (Integer availableAmount, List<StatisticsDataDto>statisticData){
		MonthlyUsageStatisticsDto dto = new MonthlyUsageStatisticsDto();
		dto.availableAmount = availableAmount;
		dto.data1 = statisticData.get(0);
		dto.data2 = statisticData.get(1);
		dto.data3 = statisticData.get(2);
		dto.data4 = statisticData.get(3);
		dto.data5 = statisticData.get(4);
		dto.data6 = statisticData.get(5);
		return dto;
	}
	
	@Override
	public String getResultName() {
		return "monthlyUsageStatistics";
	}

}
