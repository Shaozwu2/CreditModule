package com.kooppi.nttca.portal.common.mq.domain;

import java.util.List;

public class PriceBookEmsMessage {

	private String date_date;
	private List<PricebookItem> contents;
	
	public PriceBookEmsMessage() {}

	public String getDate_date() {
		return date_date;
	}

	public void setDate_date(String date_date) {
		this.date_date = date_date;
	}

	public List<PricebookItem> getContents() {
		return contents;
	}

	public void setContents(List<PricebookItem> contents) {
		this.contents = contents;
	}
	
	
}
