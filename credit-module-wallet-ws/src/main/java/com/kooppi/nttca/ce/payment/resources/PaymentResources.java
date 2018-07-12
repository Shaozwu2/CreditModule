package com.kooppi.nttca.ce.payment.resources;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kooppi.nttca.ce.domain.Payment;
import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.ce.payment.service.PaymentService;
import com.kooppi.nttca.portal.ce.dto.payment.CreatePaymentDto;
import com.kooppi.nttca.portal.ce.dto.payment.PaymentDto;
import com.kooppi.nttca.portal.ce.dto.payment.ReservationAndTransactionCollectionDto;
import com.kooppi.nttca.portal.ce.dto.payment.ReservationAndTransactionDto;
import com.kooppi.nttca.portal.ce.dto.payment.ReservationAndTransactionSearchDto;
import com.kooppi.nttca.portal.common.auditlog.AuditingActionType;
import com.kooppi.nttca.portal.common.auditlog.Logged;
import com.kooppi.nttca.portal.common.utils.LocalDateParameterConverter;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;
import com.kooppi.nttca.wallet.common.persistence.domain.Transaction;
import com.kooppi.nttca.wallet.rest.transaction.service.TransactionService;

import io.swagger.annotations.Api;

@Stateless
@Api(value = "Payment(Version 1)")
@Path("payments")
public class PaymentResources {

	private static final Logger logger = LoggerFactory.getLogger(PaymentResources.class);

	@Inject
	private PaymentService paymentService;

	@Inject
	private TransactionService transactionService;
	
	@POST
	@Path("quick-search-payments")
	@Produces(MediaType.APPLICATION_JSON)
	public ReservationAndTransactionCollectionDto searchPaymentsByOrFilter(
			@QueryParam("globalFilter") String globalFilter,
			@QueryParam("sort") @DefaultValue("uid=asc") String sort,
			@QueryParam("offset") @DefaultValue("0") Integer offset,
			@QueryParam("maxRows") @DefaultValue("20") Integer maxRows, 
			ReservationAndTransactionSearchDto reservationAndTransactionSearchDto) {
		ResultList<Payment> paymentResultList = paymentService.searchByOrFilters(globalFilter, sort, offset, maxRows, reservationAndTransactionSearchDto.getOrganizationNames());
		
		List<ReservationAndTransactionDto> dtos = paymentResultList.getItems().stream().map(p -> p.toReservationAndTransactionDto()).collect(Collectors.toList());
		ReservationAndTransactionCollectionDto dto = ReservationAndTransactionCollectionDto.create(dtos, paymentResultList.getTotalNumOfItems(), offset, maxRows);
		return dto;
	}
	
	@GET
	@Path("advance-search-payments")
	@Produces(MediaType.APPLICATION_JSON)
	public ReservationAndTransactionCollectionDto searchPaymentsByAndFilter(
			@QueryParam("organizationId") String organizationId,
			@QueryParam("paymentId") String paymentId,
			@QueryParam("transactionDate") String _transactionDate,
			@QueryParam("sort") @DefaultValue("uid=asc") String sort,
			@QueryParam("offset") @DefaultValue("0") Integer offset,
			@QueryParam("maxRows") @DefaultValue("20") Integer maxRows) {
		LocalDate transactionDate = null;
		LocalDateParameterConverter converter = new LocalDateParameterConverter();
		if (_transactionDate != null) {
			transactionDate = converter.fromString(_transactionDate);
		}
		
		ResultList<Payment> paymentResultList = paymentService.searchByAndFilters(organizationId, paymentId, transactionDate, sort, offset, maxRows);
		
		List<ReservationAndTransactionDto> dtos = paymentResultList.getItems().stream().map(p -> p.toReservationAndTransactionDto()).collect(Collectors.toList());
		ReservationAndTransactionCollectionDto dto = ReservationAndTransactionCollectionDto.create(dtos, paymentResultList.getTotalNumOfItems(), offset, maxRows);
		return dto;
	}
	
	@GET
	@Path("get-payment/{paymentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public PaymentDto getPayment(@PathParam("paymentId") String paymentId) {
		logger.debug("getPayment");
		String configEnv = System.getenv("CONFIG_LOCATION");
		System.out.println("#######CONFIG_LOCATION#####: " + configEnv);
		logger.debug("#######CONFIG_LOCATION#####: " + configEnv);
		PortalExceptionUtils.throwIfNullOrEmptyString(paymentId, PortalErrorCode.PAYMENT_MISS_PATH_PARAM_PAYMENT_ID);
		Transaction transaction = PortalExceptionUtils.throwIfEmpty(transactionService.findCommiteSuccessTransactionByTransactionId(paymentId), PortalErrorCode.PAYMENT_MISSING_TRANSACTION);

		Payment payment = PortalExceptionUtils.throwIfEmpty(paymentService.findByPaymentId(paymentId), PortalErrorCode.PAYMENT_INVALID_PAYMENT_ID);
		payment.setCompensationTotalAmount(transaction.getCompensationAmount());
		
		PaymentDto dto = payment.toPaymentDtoWithUnitPrice();
		return dto;
	}
	
	//tested
	//TODO: new portal create real time payment use another api, which has audit trail 
	@POST
	@Path("create-real-time-payment")
	@Logged(actionType = AuditingActionType.CREATE_REALTIME_TRANSACTION)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public PaymentDto createRealTimeTransaction(CreatePaymentDto createPaymentDto) {
		PortalExceptionUtils.throwIfNull(createPaymentDto, PortalErrorCode.INVALID_PAYLOAD_DATA);
		createPaymentDto.validateCreatePayment();
		
		Payment payment = paymentService.createRealTimePayment(createPaymentDto);
		return payment.toPaymentDto();
	}
	
	@POST
	@Path("create-payment-and-reserve")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public PaymentDto createPaymentAndReserve(CreatePaymentDto createPaymentDto) {
		logger.debug("createPaymentAndReserve");
		PortalExceptionUtils.throwIfNull(createPaymentDto, PortalErrorCode.INVALID_PAYLOAD_DATA);
		createPaymentDto.validateCreatePayment();
		
		Payment payment = paymentService.reservePayment(createPaymentDto);
		return payment.toPaymentDto();
	}
	
	@POST
	@Logged(actionType = AuditingActionType.CONFIRM_TRANSACTION)
	@Path("{paymentId}/confirm")
	@Produces(MediaType.APPLICATION_JSON)
	public PaymentDto confirmPayment(@PathParam("paymentId") String paymentId, PaymentDto paymentDto) {
		PortalExceptionUtils.throwIfNullOrEmptyString(paymentId, PortalErrorCode.PAYMENT_MISS_PATH_PARAM_PAYMENT_ID);
		PortalExceptionUtils.throwIfNull(paymentDto, PortalErrorCode.INVALID_PAYLOAD_DATA);
		
		Payment payment = paymentService.confirmPayment(paymentId, paymentDto.getDescription());
		return payment.toPaymentDtoWithoutChargingItemsAndExpiredDate();
	}
	
	@POST
	@Path("{paymentId}/cancel-reservation")
	@Produces(MediaType.APPLICATION_JSON)
	public PaymentDto cancelReservedPayment(@PathParam("paymentId") String paymentId, PaymentDto paymentDto) {
		PortalExceptionUtils.throwIfNullOrEmptyString(paymentId, PortalErrorCode.PAYMENT_MISS_PATH_PARAM_PAYMENT_ID);
		PortalExceptionUtils.throwIfNull(paymentDto, PortalErrorCode.INVALID_PAYLOAD_DATA);
		
		Payment payment = paymentService.cancelReservedPayment(paymentId, paymentDto.getDescription());
		return payment.toPaymentDtoWithoutChargingItemsAndExpiredDate();
	}
	
	@POST
	@Logged(actionType = AuditingActionType.CANCEL_TRANSACTION)
	@Path("{paymentId}/refund")
	@Produces(MediaType.APPLICATION_JSON)
	public PaymentDto refundPayment(@PathParam("paymentId") String paymentId, PaymentDto paymentDto) {
		PortalExceptionUtils.throwIfNullOrEmptyString(paymentId, PortalErrorCode.PAYMENT_MISS_PATH_PARAM_PAYMENT_ID);

		PaymentDto refundPayment = paymentService.refundPayment(paymentId, paymentDto.getDescription());
		return refundPayment;
	}
	
}
