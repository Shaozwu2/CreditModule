package com.kooppi.nttca.wallet.rest.transaction.dto;

import com.kooppi.nttca.portal.wallet.domain.Source;

public enum CsvTransactionType {

	Contract,Charge;
	
	public static CsvTransactionType from(Source source){
		switch (source) {
		case ADJUSTMENT:
			return Contract;
		case SERVICE:
			return Charge;
		}
		return null;
	}
}
