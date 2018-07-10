package com.kooppi.nttca.portal.common.filter.response.dto;

import java.security.cert.CertPathValidatorException.Reason;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kooppi.nttca.portal.ce.dto.payment.PaymentDto;
import com.kooppi.nttca.portal.ce.dto.payment.ReservationAndTransactionCollectionDto;
import com.kooppi.nttca.portal.wallet.dto.adjustment.AdjustmentCollectionDto;
import com.kooppi.nttca.portal.wallet.dto.adjustment.AdjustmentDto;
import com.kooppi.nttca.portal.wallet.dto.adjustment.CurrencyRateCollectionDto;
import com.kooppi.nttca.portal.wallet.dto.adjustment.CurrencyRateDto;
import com.kooppi.nttca.portal.wallet.dto.priceBook.BuMasterCollectionDto;
import com.kooppi.nttca.portal.wallet.dto.priceBook.BuMasterDto;
import com.kooppi.nttca.portal.wallet.dto.priceBook.PriceBookCollectionDto;
import com.kooppi.nttca.portal.wallet.dto.priceBook.PriceBookItemsDto;
import com.kooppi.nttca.portal.wallet.dto.statistics.MonthlyUsageStatisticsDto;
import com.kooppi.nttca.portal.wallet.dto.statistics.StatisticsDataDto;
import com.kooppi.nttca.portal.wallet.dto.termsAndConditions.GetTnCsResponseCollectionDto;
import com.kooppi.nttca.portal.wallet.dto.termsAndConditions.TnCCollectionDto;
import com.kooppi.nttca.portal.wallet.dto.termsAndConditions.TnCToPricebookCollectionDto;
import com.kooppi.nttca.portal.wallet.dto.transaction.TransactionCollectionDto;
import com.kooppi.nttca.portal.wallet.dto.transaction.TransactionDto;
import com.kooppi.nttca.portal.wallet.dto.wallet.TransactionReservationCollectionDto;
import com.kooppi.nttca.portal.wallet.dto.wallet.TransactionReservationDto;
import com.kooppi.nttca.portal.wallet.dto.wallet.WalletCollectionDto;

@XmlSeeAlso({AdjustmentDto.class, WalletCollectionDto.class, TransactionReservationDto.class, AdjustmentCollectionDto.class,
	TransactionDto.class, TransactionCollectionDto.class, EmptyResponseResult.class, MonthlyUsageStatisticsDto.class,
	StatisticsDataDto.class, PriceBookCollectionDto.class, BuMasterCollectionDto.class, BuMasterDto.class, TnCCollectionDto.class, TnCToPricebookCollectionDto.class,
	//Charging Engine
	PaymentDto.class,Reason.class,TransactionReservationCollectionDto.class, CurrencyRateCollectionDto.class, CurrencyRateDto.class,
	ReservationAndTransactionCollectionDto.class, PriceBookItemsDto.class,
	GetTnCsResponseCollectionDto.class

})
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class ResponseResult {

	@XmlTransient
	private String RESULT_NAME;

	//hide from swagger ui 
	@JsonIgnore
	public abstract String getResultName();
	
	public void setResultName(){
		this.RESULT_NAME = getResultName();
	}
}
