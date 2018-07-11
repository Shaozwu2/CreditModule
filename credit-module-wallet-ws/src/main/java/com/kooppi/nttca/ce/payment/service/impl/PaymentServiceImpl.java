package com.kooppi.nttca.ce.payment.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.logging.log4j.core.util.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kooppi.nttca.ce.domain.Payment;
import com.kooppi.nttca.ce.domain.WalletTransactionHistory;
import com.kooppi.nttca.ce.domain.WalletTransactionReservationHistory;
import com.kooppi.nttca.ce.payment.repository.PaymentRepository;
import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.ce.payment.service.PaymentService;
import com.kooppi.nttca.portal.ce.domain.ChargeType;
import com.kooppi.nttca.portal.ce.dto.payment.ChargingItemDto;
import com.kooppi.nttca.portal.ce.dto.payment.CreatePaymentDto;
import com.kooppi.nttca.portal.ce.dto.payment.PaymentDto;
import com.kooppi.nttca.portal.common.config.file.ConfigurationValue;
import com.kooppi.nttca.portal.common.email.service.UtilizationAlertService;
import com.kooppi.nttca.portal.common.filter.context.RequestContextImpl;
import com.kooppi.nttca.portal.common.utils.StringUtils;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;
import com.kooppi.nttca.portal.wallet.domain.TransactionReservationStatus;
import com.kooppi.nttca.portal.wallet.dto.transaction.TransactionDto;
import com.kooppi.nttca.portal.wallet.dto.wallet.TransactionReservationDto;
import com.kooppi.nttca.wallet.common.persistence.domain.PriceBook;
import com.kooppi.nttca.wallet.common.persistence.domain.Transaction;
import com.kooppi.nttca.wallet.common.persistence.domain.TransactionReservation;
import com.kooppi.nttca.wallet.common.persistence.domain.Wallet;
import com.kooppi.nttca.wallet.rest.priceBook.repository.PriceBookRepository;
import com.kooppi.nttca.wallet.rest.reservation.service.TransactionReservationService;
import com.kooppi.nttca.wallet.rest.transaction.service.TransactionService;
import com.kooppi.nttca.wallet.rest.wallet.repository.WalletRepository;

@ApplicationScoped
public class PaymentServiceImpl implements PaymentService {
	
	private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

	@Inject
	@ConfigurationValue(property = "ce.payment.expired_date.max.default", defaultValue = "60")
	private Integer MAX_RESERVED_PERIOD_IN_MIN;
	
	@Inject
	@ConfigurationValue(property = "portal.wallet.reservation.expirydate.max", defaultValue = "24")
	private Integer configExpiryDate;
	
	@Inject
	private RequestContextImpl rc;
	
	@Inject
	private PaymentRepository paymentRepository;
	
	@Inject
	private WalletRepository walletRepository;
	
	@Inject
	private PriceBookRepository priceBookService;
	
	@Inject
	private TransactionReservationService transactionReservationService;
	
	@Inject
	private TransactionService transactionService;
	
	@Inject
	private UtilizationAlertService alertService;
	
	@Override
	public Optional<Payment> findByPaymentId(String paymentId) {
		return paymentRepository.findByPaymentId(paymentId);
	}
	
	@Override
	public Payment createRealTimePayment(CreatePaymentDto createPaymentDto) {
		
		/*
		 * OLD VERSION
		 * 
		 * //validate input dto createPaymentDto.validateCreatePayment();
		 * 
		 * //validate wallet exist Optional<Wallet> walletOpt =
		 * walletRepository.findWalletById(createPaymentDto.getWalletId()); Wallet
		 * wallet = PortalExceptionUtils.throwIfEmpty(walletOpt,
		 * PortalErrorCode.PAYMENT_INVALID_WALLET_ID);
		 * PortalExceptionUtils.throwIfTrue(wallet.isInactivated(),
		 * PortalErrorCode.WALLET_IS_NOT_ACTIVATED);
		 * PortalExceptionUtils.throwIfTrue(wallet.isDeleted(),
		 * PortalErrorCode.WALLET_IS_DELETED);
		 * PortalExceptionUtils.throwIfFalse(wallet.isActive(),
		 * PortalErrorCode.WALLET_IS_INACTIVE);
		 * 
		 * // get all pricebook from multi part no createPaymentDto =
		 * preparePayment(createPaymentDto); // calculate total amount Integer
		 * totalAmount = 0; for (ChargingItemDto itemDto :
		 * createPaymentDto.getChargingItems()) { if (itemDto.getTotalAmount() != null)
		 * totalAmount += itemDto.getTotalAmount(); } Payment payment =
		 * Payment.createRealTime(wallet, rc, totalAmount,
		 * createPaymentDto.getDescription());
		 * 
		 * PortalExceptionUtils.throwIfFalse(wallet.hasEnoughCredit(totalAmount),
		 * PortalErrorCode.NO_ENOUGH_AVAILABLE_CREDIT);
		 * walletService.consumeCreditsDirectly(wallet, payment, createPaymentDto);
		 * 
		 * return payment;
		 */
		
		//generate parameters for stored procedure, sort in partNo ASC
		List<ChargingItemDto> chargingItemList = createPaymentDto.getChargingItems().stream()
				.sorted((o1, o2)->o1.getPartNo().compareTo(o2.getPartNo()))
				.collect(Collectors.toList());
		String partNoList = generateParameterForStoredProcedure(chargingItemList, 0);
		String chargeTypeList = generateParameterForStoredProcedure(chargingItemList, 1);
		String quantityList = generateParameterForStoredProcedure(chargingItemList, 2);
		String unitPriceList = generateParameterForStoredProcedure(chargingItemList, 3);
		
		//call stored procedure
		Object[] obj = null;
		Payment payment = null;
		try {
			obj = paymentRepository.callCreateRealTimePaymentStoredProcedure(createPaymentDto.getWalletId(), partNoList,
					chargeTypeList, unitPriceList, quantityList, createPaymentDto.getDescription(), rc.getRequestUserId(),
					rc.getRequestId());
			payment = Payment.generatePaymentFromObject(obj);
		} catch (Exception e) {
			String msg = Throwables.getRootCause(e).getMessage();
			logger.debug("e root cause msg = " + msg);
			if ("PRICE-BOOK-ERROR-0009".equalsIgnoreCase(msg)) {
				PortalExceptionUtils.throwIfTrue(true, PortalErrorCode.INVALID_PART_NUMBER);
			} else if ("PRICE-BOOK-ERROR-0010".equalsIgnoreCase(msg)) {
				PortalExceptionUtils.throwIfTrue(true, PortalErrorCode.INVALID_UNIT_PRICE);
			} else if ("WALLET-ERROR-1009".equalsIgnoreCase(msg)) {
				PortalExceptionUtils.throwIfTrue(true, PortalErrorCode.INVALID_WALLET_ID);
			} else if ("WALLET-ERROR-1014".equalsIgnoreCase(msg)) {
				PortalExceptionUtils.throwIfTrue(true, PortalErrorCode.WALLET_IS_INACTIVE);
			} else if ("WALLET-ERROR-3021".equalsIgnoreCase(msg)) {
				PortalExceptionUtils.throwIfTrue(true, PortalErrorCode.UNABLE_TO_FIND_VALID_CONTRACT);
			} else if ("WALLET-ERROR-1029".equalsIgnoreCase(msg)) {
				PortalExceptionUtils.throwIfTrue(true, PortalErrorCode.NO_ENOUGH_AVAILABLE_CREDIT);
			} else {
				PortalExceptionUtils.throwNow(PortalErrorCode.MYSQL_EXCEPTION);
			}
		}
		
		//check and send alert
		Wallet wallet = Wallet.generateWalletFromObject(obj);
		alertService.checkAndSendAlert(wallet);
		
		return payment;
	}

	/*
	 * get attribute according to flag:
	 * 
	 * 0  -->  partNo
	 * 1  -->  chargeType
	 * 2  -->  quantity
	 * 3  -->  unitPrice
	 */
	private String generateParameterForStoredProcedure(List<ChargingItemDto> itemList, int flag) {
		String result = "";
		
		for (ChargingItemDto item : itemList) {
			String increment = "";
			if (flag == 0) {
				increment = item.getPartNo() + ",";
			} else if (flag == 1) {
				increment = item.getChargeType() + ",";
			} else if (flag == 2) {
				increment = item.getQuantity() + ",";
			} else if (flag == 3) {
				increment = item.getUnitPrice() + ",";
			}
			result = result + increment;
		}
		
		if (result != null && result.length() > 0 && result.charAt(result.length() - 1) == ',')
			result = result.substring(0, result.length() - 1);
		
		return result;
	}
	
	@Override
	public Payment reservePayment(CreatePaymentDto createPaymentDto) {
		//validate wallet exist
		Wallet wallet = PortalExceptionUtils.throwIfEmpty(walletRepository.findWalletById(createPaymentDto.getWalletId()), PortalErrorCode.PAYMENT_INVALID_WALLET_ID);
		PortalExceptionUtils.throwIfFalse(wallet.isActive(), PortalErrorCode.WALLET_IS_INACTIVE);
		
		//prepare payment:validate expired date
		createPaymentDto = preparePayment(createPaymentDto);
		LocalDateTime expiredDate = createPaymentDto.getExpiredDate() == null ? defaultExpiredDate() : createPaymentDto.getExpiredDate();
		Payment payment = Payment.create(wallet, createPaymentDto, expiredDate, rc);
		payment = paymentRepository.saveAndRefresh(payment);
		
		TransactionReservationDto reserveCreditDto = payment.toTransactionReservation();
		reserveCreditDto.validateCreateTransactionReservation();
		reserveCreditDto.validateExpiryDate(configExpiryDate);

		TransactionReservation transactionReservation = transactionReservationService.createTransactionReservation(wallet,reserveCreditDto);
		TransactionReservationDto returnedDto = transactionReservation.toTransactionReservationDto();
		
		WalletTransactionReservationHistory trh = WalletTransactionReservationHistory.create(returnedDto);
//		payment.getCurrentStatus().get().updateReservationResult(trh);
		return payment;
	}
	
	private LocalDateTime defaultExpiredDate() {
		return LocalDateTime.now().plusMinutes(MAX_RESERVED_PERIOD_IN_MIN);
	}
	
	private CreatePaymentDto preparePayment(CreatePaymentDto createPaymentDto) {
		//get all pricetbook first
		List<String> partNos = createPaymentDto.getChargingItems().stream().map(ci -> ci.getPartNo()).collect(Collectors.toList());
		List<PriceBook> pricebookByPartNos = priceBookService.findPriceBooksByPartNumber(partNos);
		
		for (ChargingItemDto item : createPaymentDto.getChargingItems()) {
			Optional<PriceBook> priceBookOp = pricebookByPartNos.stream().filter(pb -> pb.getPartNo().equals(item.getPartNo())).findAny();
			PriceBook priceBook = priceBookOp.get();
			String productName = priceBook.getProductName();
			Integer amount = 0;
			if (item.getChargeType().equals(ChargeType.ONE_OFF))
				amount = priceBook.getOneOffPrice();
			else
				amount = priceBook.getRecurringPrice();
			item.updateEmsPrice(item.getPartNo(), productName, amount);
		}
		return createPaymentDto;
	}

	@Override
	public Payment confirmPayment(String paymentId, String description) {
		//check exist
		Payment payment = PortalExceptionUtils.throwIfEmpty(findByPaymentId(paymentId), PortalErrorCode.PAYMENT_INVALID_PAYMENT_ID);
		//status is reserved
		PortalExceptionUtils.throwIfFalse(payment.isReserved(), PortalErrorCode.PAYMENT_NOT_RESERVED_STATUS);
		//payment is expired
		PortalExceptionUtils.throwIfTrue(payment.isExpired(), PortalErrorCode.PAYMENT_RESERVATION_EXPIRED);
		//do confirm on local
		payment.confirmPayment(rc, description);
		
		//do confirm on wallet
//		String transactionId = currentStautsHistory.getTransactionId();
		String transactionId = "";

		//validate wallet exist
		Wallet wallet = PortalExceptionUtils.throwIfEmpty(walletRepository.findWalletById(payment.getWalletId()), PortalErrorCode.PAYMENT_INVALID_WALLET_ID);
		PortalExceptionUtils.throwIfFalse(wallet.isActive(), PortalErrorCode.WALLET_IS_INACTIVE);
		
		//get the Reservation by Id
		Optional<TransactionReservation> transactionReservationOpt = transactionReservationService.findTransactionReservationByTransactionId(transactionId);
		TransactionReservation transactionReservation = PortalExceptionUtils.throwIfEmpty(transactionReservationOpt, PortalErrorCode.INVALID_TRANSACTION_ID);
		PortalExceptionUtils.throwIfFalse(transactionReservation.getStatus().equals(TransactionReservationStatus.RESERVED), PortalErrorCode.RESERVATION_STATUS_IS_NOT_RESERVED);
		Transaction transaction = transactionReservationService.consumeReservedTransactionReservation(transactionReservation, wallet);
		TransactionDto returnedDto = transaction.toTransactionDto();
		
		//update local
		WalletTransactionHistory tran = WalletTransactionHistory.create(returnedDto);
//		payment.getCurrentStatus().get().updateTransactionResult(tran);
		return payment;
	}

	@Override
	public Payment cancelReservedPayment(String paymentId, String description) {
		//check exist
		Payment payment = PortalExceptionUtils.throwIfEmpty(findByPaymentId(paymentId), PortalErrorCode.PAYMENT_INVALID_PAYMENT_ID);
		//status is reserved
		PortalExceptionUtils.throwIfFalse(payment.isReserved(), PortalErrorCode.PAYMENT_NOT_RESERVED_STATUS);
		//payment is expired
		PortalExceptionUtils.throwIfTrue(payment.isExpired(), PortalErrorCode.PAYMENT_RESERVATION_EXPIRED);
		//do confirm on local
		payment.cancelReservedPayment(rc, description);
		
		//do confirm on wallet
		String transactionId = "";		
		//validate wallet exist
		Wallet wallet = PortalExceptionUtils.throwIfEmpty(walletRepository.findWalletById(payment.getWalletId()), PortalErrorCode.PAYMENT_INVALID_WALLET_ID);
		PortalExceptionUtils.throwIfFalse(wallet.isActive(), PortalErrorCode.WALLET_IS_INACTIVE);
		
		Optional<TransactionReservation> transactionReservationOpt = transactionReservationService.findTransactionReservationByTransactionId(transactionId);
		TransactionReservation transactionReservation = PortalExceptionUtils.throwIfEmpty(transactionReservationOpt, PortalErrorCode.INVALID_TRANSACTION_ID);
		PortalExceptionUtils.throwIfFalse(transactionReservation.getStatus().equals(TransactionReservationStatus.RESERVED), PortalErrorCode.RESERVATION_STATUS_IS_NOT_RESERVED);
		
		transactionReservationService.cancelReservedCredits(transactionReservation, wallet);
		//update local
		return payment;
	}

	@Override
	public PaymentDto refundPayment(String paymentId, String description) {
		//check exist
		Payment payment = PortalExceptionUtils.throwIfEmpty(findByPaymentId(paymentId), PortalErrorCode.PAYMENT_INVALID_PAYMENT_ID);
		PortalExceptionUtils.throwIfFalse(payment.isConfirm(), PortalErrorCode.PAYMENT_NOT_CONFIRMED_STATUS);
		payment.refundPayment(rc, description);
		
		transactionService.reverseSuccessTransactionIfExist(payment, rc.getAppId(), description);
		transactionService.reversePendingTransactionIfExist(payment, rc.getAppId(), description);
//		ReopenedContractIdDto reopenedContractIdDto = new ReopenedContractIdDto();
		
//		for (String id : reopenContractIds) {
//			System.out.println("Id: " + id);
//			ReopenedContractIdItemDto reopenedContractIdItemDto = new ReopenedContractIdItemDto();
//			reopenedContractIdItemDto.setContractId(id);
//			reopenedContractIdDto.getContractIds().add(reopenedContractIdItemDto);
//		}
		PaymentDto dto = payment.toPaymentDtoWithoutChargingItemsAndExpiredDate();
//		dto.setReopenedContractIds(reopenedContractIdDto);
		
		return dto;
	}

	@Override
	public ResultList<Payment> searchByOrFilters(String globalFilter, String sort, Integer offset, Integer maxRows, List<String> organizationNames) {
		String orderBy = null;
		String orderSorting = null;
		if (!sort.isEmpty() && sort != null) {
			orderBy = StringUtils.parseQueryParam(sort).get("orderBy");
			orderSorting = StringUtils.parseQueryParam(sort).get("orderSorting");
		}
		
		return paymentRepository.searchByOrFilter(globalFilter, orderBy, orderSorting, offset, maxRows, organizationNames);
	}

	@Override
	public ResultList<Payment> searchByAndFilters(String organizationId, String paymentId, LocalDate transactionDate, String sort, Integer offset, Integer maxRows) {
		String orderBy = null;
		String orderSorting = null;
		if (!sort.isEmpty() && sort != null) {
			orderBy = StringUtils.parseQueryParam(sort).get("orderBy");
			orderSorting = StringUtils.parseQueryParam(sort).get("orderSorting");
		}
		
		return paymentRepository.searchByAndFilter(organizationId, paymentId, transactionDate, orderBy, orderSorting, offset, maxRows);
	}
}