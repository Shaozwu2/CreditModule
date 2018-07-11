package com.kooppi.nttca.wallet.rest.transaction.dto;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.google.common.collect.Lists;
import com.kooppi.nttca.wallet.common.persistence.domain.Transaction;

public class TransactionBreakdownCsv {

	private static final DateTimeFormatter DATE_FORMATER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private static final String[] HEADER = {"Transaction Date","PaymentId","Transaction ID", "Transaction Type", "Service","Item Name","Item Type","Product Name","Product Unit",
			"No. of Product Purchase","Amount/Unit","Service Order ID","Action","Credit","Action By","Description"
			};
	
	public static List<String[]> create(Transaction transaction){
		
		List<String[]> result = Lists.newArrayList();
		
//		if (!transaction.getChargeItems().isEmpty()) {
//			for (ChargeItem item : transaction.getChargeItems()) {
//				String[] record = {
//						DATE_FORMATER.format(transaction.getChargeDate()),
//						transaction.getPaymentId(),
//						transaction.getTransactionId(),
//						CsvTransactionType.from(transaction.getSource()).toString(),
//						transaction.getServiceId(),
//						"",
//						transaction.getItemType(),
//						item.getProductName(),
//						"",
//						item.getQuantity()+"",
//						item.getAmount()+"",
//						transaction.getServiceOrder(),
//						transaction.getAction(),
//						item.getAmount()+"",
//						transaction.getUserId(),
//						transaction.getDescription()
//				};
//				result.add(record);
//			}
//		}else {
//			String[] record = {
//					DATE_FORMATER.format(transaction.getChargeDate()),
//					transaction.getPaymentId(),
//					transaction.getTransactionId(),
//					CsvTransactionType.from(transaction.getSource()).toString(),
//					transaction.getServiceId(),
//					"",
//					transaction.getItemType(),
//					"",
//					"",
//					"",
//					"",
//					transaction.getServiceOrder(),
//					transaction.getAction(),
//					transaction.getAmount()+"",
//					transaction.getUserId(),
//					transaction.getDescription()
//			};
//			result.add(record);
//		}
		return result;
	}
	
	public static String[] getHeader(){
		return HEADER;
	}
	
}
