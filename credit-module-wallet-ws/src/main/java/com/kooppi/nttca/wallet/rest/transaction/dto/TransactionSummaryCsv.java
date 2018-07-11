package com.kooppi.nttca.wallet.rest.transaction.dto;

import java.time.format.DateTimeFormatter;

import com.kooppi.nttca.wallet.common.persistence.domain.Transaction;

public class TransactionSummaryCsv {

	private static final DateTimeFormatter DATE_FORMATER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	private static final String[] HEADER = {"Transaction Date","Payment ID","Transaction ID","Transaction Type","Service","Item Name","Item Type",
			"Service Order ID","Action","Credit","Balance","Action By","Description"
	};
	
	public static String[] create(Transaction transaction){
		String[] result = {
				DATE_FORMATER.format(transaction.getChargeDate()),
				transaction.getPaymentId(),
				transaction.getTransactionId(),
				CsvTransactionType.from(transaction.getSource()).toString(),
				transaction.getServiceId(),
				"",
				"",
				transaction.getServiceOrder(),
				transaction.getAction(),
				transaction.getWalletAmount()+"",
				transaction.getBalance()+"",
				transaction.getUserId(),
				transaction.getDescription()
		};
		return result;
	}
	
	public static String[] getHeader(){
		return HEADER;
	}
}
