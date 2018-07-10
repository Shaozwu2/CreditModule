package com.kooppi.nttca.wallet.rest.reservation.resources;

import javax.ejb.Stateless;

import io.swagger.annotations.Api;

@Stateless
@Api(hidden = true)
public class TransactionReservationResources {
	
//	@Inject
//	private RequestContextImpl rc;
//	
//	@Inject
//	private WalletService walletService;
//	
//	@Inject
//	private TransactionReservationService transactionReservationService;
//	
//	@Inject
//	private WalletPermissionService walletPermissionService;
//	
//	@Inject
//	@ConfigurationValue(property = "portal.wallet.reservation.expirydate.max", defaultValue = "24")
//	private Integer configExpiryDate;
//	
//	private String walletId;
	
//	@Path("search-transaction-reservations")
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	@ApiOperation(value = "View Reservation", notes = "Version 1.0, Last Modified Date: 2018-03-20", response = ResponseHeader.class )
//	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
//	@ApiImplicitParams({
//	    @ApiImplicitParam(name = "X-App-ID", value = "appId", required = true, dataType = "string", paramType = "header"),
//	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
//	    @ApiImplicitParam(name = "X-User-ID", value = "userId", required = false, dataType = "string", paramType = "header")})
//	public TransactionReservationCollectionDto searchTransactionReservations(
//			@QueryParam("sort") @DefaultValue("uid=asc") String sort,
//			@QueryParam("offset") @DefaultValue("0") Integer offset,
//			@QueryParam("maxRows") @DefaultValue("20") Integer maxRows){
//		//validate parameter
//		PortalExceptionUtils.throwIfNullOrEmptyString(walletId, PortalErrorCode.MISS_PARAM_WALLET_ID);
//
//		//check wallet exist
//		Optional<Wallet> walltOpt = walletService.findWalletById(walletId);
//		Wallet wallet = PortalExceptionUtils.throwIfEmpty(walltOpt, PortalErrorCode.INVALID_WALLET_ID);
//		PortalExceptionUtils.throwIfTrue(wallet.isInactivated(), PortalErrorCode.WALLET_IS_NOT_ACTIVATED);
//		PortalExceptionUtils.throwIfTrue(wallet.isDeleted(), PortalErrorCode.WALLET_IS_DELETED);
//		PortalExceptionUtils.throwIfFalse(wallet.isActive(), PortalErrorCode.WALLET_IS_INACTIVE);
//		
//		//check user has permission
//		PortalExceptionUtils.throwIfFalse(walletPermissionService.hasViewTransactionReservationPermission(rc, wallet),PortalErrorCode.NO_TRANSACTION_RESERVATION_VIEW_PERMISSION);
//		
//		ResultList<TransactionReservation> transactionReservationList = transactionReservationService.searchTransactionReservation(rc, walletId, sort, offset, maxRows);
//		
//		List<TransactionReservationDto> transactionReservationDtos = transactionReservationList.getItems().stream().map(tr -> tr.toTransactionReservationDto()).collect(Collectors.toList());
//		TransactionReservationCollectionDto dto = TransactionReservationCollectionDto.create(transactionReservationDtos, transactionReservationList.getTotalNumOfItems(), offset, maxRows);
//		return dto;
//	}
//	
	
//	@POST
//	@Logged(actionType = AuditingActionType.RESERVE_CREDITS)
//	@Transactional
//	@Path("reserve-credits")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	@ApiOperation(value = "Reserve Credits", notes = "Version 1.0, Last Modified Date: 2016-07-28", response = ResponseHeader.class )
//	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
//	@ApiImplicitParams({
//	    @ApiImplicitParam(name = "X-App-ID", value = "appId", required = true, dataType = "string", paramType = "header"),
//	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
//	    @ApiImplicitParam(name = "X-User-ID", value = "userId", required = false, dataType = "string", paramType = "header")})
//	public TransactionReservationDto reserveCredits(@ApiParam(value = "Adjustment Dto", required = true) TransactionReservationDto transactionReservationdto) {
//		PortalExceptionUtils.throwIfNullOrEmptyString(walletId, PortalErrorCode.MISS_PARAM_WALLET_ID);
//		PortalExceptionUtils.throwIfNull(transactionReservationdto, PortalErrorCode.INVALID_PAYLOAD_DATA);
//		
//		//validate TransactionReservationDto
//		transactionReservationdto.validateCreateTransactionReservation();
//		
//		//validate expired date
//		transactionReservationdto.validateExpiryDate(configExpiryDate);
//				
//		//find and validate wallet
//		Optional<Wallet> walletOpt = walletService.findWalletById(walletId);
//		Wallet wallet = PortalExceptionUtils.throwIfEmpty(walletOpt, PortalErrorCode.INVALID_WALLET_ID);
//		PortalExceptionUtils.throwIfTrue(wallet.isInactivated(), PortalErrorCode.WALLET_IS_NOT_ACTIVATED);
//		PortalExceptionUtils.throwIfTrue(wallet.isDeleted(), PortalErrorCode.WALLET_IS_DELETED);
//		PortalExceptionUtils.throwIfFalse(wallet.isActive(), PortalErrorCode.WALLET_IS_INACTIVE);
//		PortalExceptionUtils.throwIfFalse(walletPermissionService.hasCreditsReservePermission(rc, wallet), PortalErrorCode.NO_CREDIT_RESERVE_PERMISSION);
//		
//		TransactionReservation transactionReservation = transactionReservationService.createTransactionReservation(wallet,transactionReservationdto);
//		TransactionReservationDto returnedDto = transactionReservation.toTransactionReservationDto();
//		return returnedDto;
//	}
	
//	@POST
//	@Logged(actionType = AuditingActionType.CONSUME_RESERVED_CREDITS)
//	@Transactional
//	@Path("consume-reservation/{transactionId}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@ApiOperation(value = "Consume Reserved Credits", notes = "Version 1.0, Last Modified Date: 2016-07-28", response = ResponseHeader.class )
//	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
//	@ApiImplicitParams({
//	    @ApiImplicitParam(name = "X-App-ID", value = "appId", required = true, dataType = "string", paramType = "header"),
//	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
//	    @ApiImplicitParam(name = "X-User-ID", value = "userId", required = false, dataType = "string", paramType = "header")})	
//	public TransactionDto consumeReservedCredits(@ApiParam(value="Transaction Id", required = true) @PathParam("transactionId") String transactionId) {
//		PortalExceptionUtils.throwIfNullOrEmptyString(walletId, PortalErrorCode.MISS_PARAM_WALLET_ID);
//		PortalExceptionUtils.throwIfNullOrEmptyString(transactionId, PortalErrorCode.MISS_PARAM_TRANSACTION_ID);
//		
//		//find and validate wallet
//		Optional<Wallet> walltOpt = walletService.findWalletById(walletId);
//		Wallet wallet = PortalExceptionUtils.throwIfEmpty(walltOpt, PortalErrorCode.INVALID_WALLET_ID);
//		PortalExceptionUtils.throwIfTrue(wallet.isInactivated(), PortalErrorCode.WALLET_IS_NOT_ACTIVATED);
//		PortalExceptionUtils.throwIfTrue(wallet.isDeleted(), PortalErrorCode.WALLET_IS_DELETED);
//		PortalExceptionUtils.throwIfFalse(wallet.isActive(), PortalErrorCode.WALLET_IS_INACTIVE);
//	
//		//get the Reservation by Id
//		Optional<TransactionReservation> transactionReservationOpt = transactionReservationService.findTransactionReservationByTransactionId(transactionId);
//		TransactionReservation transactionReservation = PortalExceptionUtils.throwIfEmpty(transactionReservationOpt, PortalErrorCode.INVALID_TRANSACTION_ID);
//		PortalExceptionUtils.throwIfFalse(walletPermissionService.hasReservedCreditsConsumePermission(rc, transactionReservation), PortalErrorCode.NO_CREDIT_COMSUME_PERMISSION);
//		PortalExceptionUtils.throwIfFalse(transactionReservation.getStatus().equals(TransactionReservationStatus.RESERVED), PortalErrorCode.RESERVATION_STATUS_IS_NOT_RESERVED);
//		
//		Transaction transaction = transactionReservationService.consumeReservedTransactionReservation(transactionReservation, wallet);
//		TransactionDto returnedDto = transaction.toTransactionDto();
//		return returnedDto;
//	}
//	
//	@POST
//	@Logged(actionType = AuditingActionType.CONSUME_CHARGE_AMOUNT)
//	@Transactional
//	@Path("consume-charge-amount")
//	@Produces(MediaType.APPLICATION_JSON)
//	@ApiOperation(value = "Consume Charge Amount", notes = "Version 1.0, Last Modified Date: 2018-04-16", response = ResponseHeader.class )
//	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
//	@ApiImplicitParams({
//	    @ApiImplicitParam(name = "X-App-ID", value = "appId", required = true, dataType = "string", paramType = "header"),
//	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
//	    @ApiImplicitParam(name = "X-User-ID", value = "userId", required = false, dataType = "string", paramType = "header")})	
//	public TransactionDto consumeChargeAmount(@ApiParam(value="Consume Charge Amount DTO", required = true) ConsumeChargeAmountDto consumeChargeAmountDto) {
//		PortalExceptionUtils.throwIfNullOrEmptyString(walletId, PortalErrorCode.MISS_PARAM_WALLET_ID);
//		PortalExceptionUtils.throwIfNull(consumeChargeAmountDto, PortalErrorCode.INVALID_PAYLOAD_DATA);
//		
//		//validate dto
//		consumeChargeAmountDto.validateConfirmChargeAmountDto();
//		
//		//find and validate wallet
//		Optional<Wallet> walltOpt = walletService.findWalletById(walletId);
//		Wallet wallet = PortalExceptionUtils.throwIfEmpty(walltOpt, PortalErrorCode.INVALID_WALLET_ID);
//		PortalExceptionUtils.throwIfTrue(wallet.isInactivated(), PortalErrorCode.WALLET_IS_NOT_ACTIVATED);
//		PortalExceptionUtils.throwIfTrue(wallet.isDeleted(), PortalErrorCode.WALLET_IS_DELETED);
//		PortalExceptionUtils.throwIfFalse(wallet.isActive(), PortalErrorCode.WALLET_IS_INACTIVE);
//	
//		//get the Reservation by Id
//		Optional<TransactionReservation> transactionReservationOpt = transactionReservationService.findTransactionReservationByTransactionId(consumeChargeAmountDto.getTransactionId());
//		TransactionReservation transactionReservation = PortalExceptionUtils.throwIfEmpty(transactionReservationOpt, PortalErrorCode.INVALID_TRANSACTION_ID);
//		PortalExceptionUtils.throwIfFalse(walletPermissionService.hasReservedCreditsConsumePermission(rc, transactionReservation), PortalErrorCode.NO_CREDIT_COMSUME_PERMISSION);
//		PortalExceptionUtils.throwIfFalse(transactionReservation.getStatus().equals(TransactionReservationStatus.RESERVED), PortalErrorCode.RESERVATION_STATUS_IS_NOT_RESERVED);
//		
//		Transaction transaction = transactionReservationService.consumeChargeAmount(transactionReservation, wallet, consumeChargeAmountDto.getChargeAmount());
//		TransactionDto returnedDto = transaction.toTransactionDto();
//		return returnedDto;
//	}
	
//	@DELETE
//	@Logged(actionType = AuditingActionType.CANCEL_RESERVATION)
//	@Transactional
//	@Path("cancel-reservation/{transactionId}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@ApiOperation(value = "Cancel Reserved Credits", notes = "Version 1.0, Last Modified Date: 2016-07-2", response = ResponseHeader.class )
//	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
//	@ApiImplicitParams({
//	    @ApiImplicitParam(name = "X-App-ID", value = "appId", required = true, dataType = "string", paramType = "header"),
//	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
//	    @ApiImplicitParam(name = "X-User-ID", value = "userId", required = false, dataType = "string", paramType = "header")})
//	public EmptyResponseResult cancelReservedCredits(@ApiParam(value="Transaction Id", required = true) @PathParam("transactionId") String transactionId) {
//		PortalExceptionUtils.throwIfNullOrEmptyString(walletId, PortalErrorCode.MISS_PARAM_WALLET_ID);
//		PortalExceptionUtils.throwIfNullOrEmptyString(transactionId, PortalErrorCode.MISS_PARAM_TRANSACTION_ID);
//		
//		//find and validate wallet
//		Optional<Wallet> walltOpt = walletService.findWalletById(walletId);
//		Wallet wallet = PortalExceptionUtils.throwIfEmpty(walltOpt, PortalErrorCode.INVALID_WALLET_ID);
//		PortalExceptionUtils.throwIfTrue(wallet.isInactivated(), PortalErrorCode.WALLET_IS_NOT_ACTIVATED);
//		PortalExceptionUtils.throwIfTrue(wallet.isDeleted(), PortalErrorCode.WALLET_IS_DELETED);
//		PortalExceptionUtils.throwIfFalse(wallet.isActive(), PortalErrorCode.WALLET_IS_INACTIVE);
//			
//		//get the Reservation by Id
//		Optional<TransactionReservation> transactionReservationOpt = transactionReservationService.findTransactionReservationByTransactionId(transactionId);
//		TransactionReservation transactionReservation = PortalExceptionUtils.throwIfEmpty(transactionReservationOpt, PortalErrorCode.INVALID_TRANSACTION_ID);
//		PortalExceptionUtils.throwIfFalse(walletPermissionService.hasReservedCreditsCancelPermission(rc, transactionReservation), PortalErrorCode.NO_RESERVED_CREDIT_CANCEL_PERMISSION);
//		PortalExceptionUtils.throwIfFalse(transactionReservation.getStatus().equals(TransactionReservationStatus.RESERVED), PortalErrorCode.RESERVATION_STATUS_IS_NOT_RESERVED);
//		
//		transactionReservationService.cancelReservedCredits(transactionReservation, wallet);
//		
//		return EmptyResponseResult.create();
//	}
//	
//	public void setWalletId(String walletId){
//		this.walletId = walletId;
//	}
}
