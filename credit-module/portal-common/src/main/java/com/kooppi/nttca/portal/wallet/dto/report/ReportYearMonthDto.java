package com.kooppi.nttca.portal.wallet.dto.report;

import java.time.YearMonth;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ReportYearMonthDto {

	@XmlElement(name = "yearMonth")
	List<YearMonth> yearMonth;

	public List<YearMonth> getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(List<YearMonth> yearMonth) {
		this.yearMonth = yearMonth;
	}

}
