package com.kooppi.nttca.portal.ce.dto.payment;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.kooppi.nttca.portal.ce.domain.PaymentStatus;
import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;

@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentStatusHistoryDto extends ResponseResult{

	@Override
	public String getResultName() {
		return "paymentStatus";
	}
	
	@XmlElement(name = "paymentId")
	private String paymentId;
	
	@XmlElement(name = "requestId")
	private String requestId;
	
	@XmlElement(name = "status")
	private PaymentStatus status;
	
	@XmlElement(name = "amount")
	private Integer amount;
	
	@XmlElement(name = "remark")
	private String remark;

	public static PaymentStatusHistoryDto create(String paymentId,String requestId,PaymentStatus status,Integer amount,String remark){
		PaymentStatusHistoryDto dto = new PaymentStatusHistoryDto();
		dto.paymentId = paymentId;
		dto.requestId = requestId;
		dto.status = status;
		dto.amount = amount;
		dto.remark = remark;
		return dto;
	}
	
}
