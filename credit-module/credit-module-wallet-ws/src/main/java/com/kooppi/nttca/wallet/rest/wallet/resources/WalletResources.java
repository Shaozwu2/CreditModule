package com.kooppi.nttca.wallet.rest.wallet.resources;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
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

import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.common.auditlog.AuditingActionType;
import com.kooppi.nttca.portal.common.auditlog.Logged;
import com.kooppi.nttca.portal.common.filter.context.RequestContextImpl;
import com.kooppi.nttca.portal.common.filter.response.dto.EmptyResponseResult;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;
import com.kooppi.nttca.portal.wallet.domain.IdleUnit;
import com.kooppi.nttca.portal.wallet.domain.WalletStatus;
import com.kooppi.nttca.portal.wallet.domain.WalletType;
import com.kooppi.nttca.portal.wallet.dto.wallet.CreateWalletDto;
import com.kooppi.nttca.portal.wallet.dto.wallet.UpdateWalletDto;
import com.kooppi.nttca.portal.wallet.dto.wallet.WalletCollectionDto;
import com.kooppi.nttca.portal.wallet.dto.wallet.WalletDto;
import com.kooppi.nttca.wallet.common.persistence.domain.Wallet;
import com.kooppi.nttca.wallet.common.swagger.ui.model.BadResponseModel;
import com.kooppi.nttca.wallet.common.swagger.ui.model.GoodResponseModel;
import com.kooppi.nttca.wallet.rest.adjustment.resources.AdjustmentResources;
import com.kooppi.nttca.wallet.rest.wallet.service.WalletService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Stateless
@Api(value = "Wallet(Version 1)")
@Path("wallets")
public class WalletResources {

	private static final Logger logger = LoggerFactory.getLogger(WalletResources.class);

	@Inject
	private RequestContextImpl rc;
	
	@Inject
	private WalletService walletService;

	@Inject
	private AdjustmentResources adjustmentresources;
	
	@GET
	@Path("get-wallet/{walletId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get Wallet", notes = "Version 1.0, Last Modified Date: 2018-03-05", response = GoodResponseModel.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public WalletDto getWallet(@ApiParam(value = "Wallet ID", required = true) @PathParam("walletId") String walletId){
		logger.debug("begin getWallet, walletId = {}",walletId);
		PortalExceptionUtils.throwIfNullOrEmptyString(walletId, PortalErrorCode.MISS_PARAM_WALLET_ID);
		
		Wallet wallet = PortalExceptionUtils.throwIfEmpty(walletService.findWalletById(walletId), PortalErrorCode.INVALID_WALLET_ID);
		WalletDto dto = wallet.toWalletDto();
		return dto;
	}
	
	@POST
	@Logged(actionType = AuditingActionType.CREATE_WALLET)
	@Path("create-wallet")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create Wallet", notes = "Version 1.0, Last Modified Date: 2018-03-05", response = GoodResponseModel.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public WalletDto createWallet(@ApiParam(value = "Create Wallet Params", required = true) CreateWalletDto createWalletDto) {
		//validate dto
		PortalExceptionUtils.throwIfNull(createWalletDto, PortalErrorCode.INVALID_PAYLOAD_DATA);
		createWalletDto.validateCreateWallet();
		
		String organizationId = createWalletDto.getOrganizationId();
		String organizationName = createWalletDto.getOrganizationName();
		String parentOrganizationId = createWalletDto.getParentOrganizationId();
		String parentOrganizationName = createWalletDto.getParentOrganizationName();
		Integer maxIdlePeriod = 0;
		IdleUnit idleUnit = IdleUnit.DAY;
		LocalDate expiredOn = null;
		Integer creditBuffer = 0;
		boolean isOneTime = true;
		Integer utilizationAlert1Threshold = null;
		String utilizationAlert1Receivers = null;
		String utilizationAlert1Bccs = null;
		Integer utilizationAlert2Threshold = null;
		String utilizationAlert2Receivers = null;
		String utilizationAlert2Bccs = null;
		Integer forfeitAlert1Threshold = null;
		String forfeitAlert1Receivers = null;
		String forfeitAlert1Bccs = null;
		Integer forfeitAlert2Threshold = null;
		String forfeitAlert2Receivers = null;
		String forfeitAlert2Bccs = null;
		
		Wallet wallet = walletService.createWallet(organizationId, organizationName, parentOrganizationId, parentOrganizationName, maxIdlePeriod, idleUnit, expiredOn, creditBuffer, 
				isOneTime, utilizationAlert1Threshold, utilizationAlert1Receivers, utilizationAlert1Bccs, utilizationAlert2Threshold, utilizationAlert2Receivers, 
				utilizationAlert2Bccs, forfeitAlert1Threshold, forfeitAlert1Receivers, forfeitAlert1Bccs, forfeitAlert2Threshold, forfeitAlert2Receivers, forfeitAlert2Bccs);
		WalletDto returnedWalletDto = wallet.toWalletDto();
		return returnedWalletDto;
	}
	

	@POST
	@Logged(actionType = AuditingActionType.ACTIVATE_WALLET)
	@Path("activate-wallet/{walletId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Activate Wallet", notes = "Version 1.0, Last Modified Date: 2016-07-27", response = GoodResponseModel.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public EmptyResponseResult activateWallet(@ApiParam(value="Wallet Id", required = true) @PathParam("walletId") String walletId) {
		logger.debug("begin activateWallet, walletId = {}",walletId);
		PortalExceptionUtils.throwIfNullOrEmptyString(walletId, PortalErrorCode.MISS_PARAM_WALLET_ID);
		
		Wallet wallet = PortalExceptionUtils.throwIfEmpty(walletService.findWalletById(walletId), PortalErrorCode.INVALID_WALLET_ID);
		PortalExceptionUtils.throwIfFalse(wallet.isInactive(), PortalErrorCode.WALLET_IS_INACTIVE);
				
		wallet.activateWallet(rc.getRequestUserId());

		return EmptyResponseResult.create();
	}
	
	
	@POST
	@Logged(actionType = AuditingActionType.UPDATE_WALLET)
	@Transactional
	@Path("update-wallet/{walletId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update Wallet", notes = "Version 1.0, Last Modified Date: 2018-03-05", response = GoodResponseModel.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public WalletDto updateWallet(@ApiParam(value = "Wallet Id", required = true) @PathParam("walletId") String walletId, 
			@ApiParam(value = "Update Wallet Dto", required = true) UpdateWalletDto updateWalletDto) {
		logger.debug("begin updateWallet, walletId = {}",walletId);
		
		//validate input
		PortalExceptionUtils.throwIfNull(updateWalletDto, PortalErrorCode.INVALID_PAYLOAD_DATA);
		PortalExceptionUtils.throwIfNullOrEmptyString(walletId, PortalErrorCode.MISS_PARAM_WALLET_ID);
		updateWalletDto.validateUpdateWallet();
		
		Wallet wallet = PortalExceptionUtils.throwIfEmpty(walletService.findWalletById(walletId), PortalErrorCode.INVALID_WALLET_ID);
		
		//credit buffer vailidation
		Integer remianBuffer = 0;
		if (wallet.getCreditBuffer() >= updateWalletDto.getCreditBuffer()) {
			PortalExceptionUtils.throwIfTrue(updateWalletDto.getCreditBuffer() < wallet.getRemainBuffer(), PortalErrorCode.INVALID_CREDIT_BUFFER);
			remianBuffer = wallet.getRemainBuffer() - Math.abs((wallet.getCreditBuffer() - updateWalletDto.getCreditBuffer()));

		} else {
			remianBuffer = wallet.getRemainBuffer() + Math.abs((wallet.getCreditBuffer() - updateWalletDto.getCreditBuffer()));
		}

		WalletStatus status = updateWalletDto.getWalletStatus();
		Integer maxIdlePeriod = updateWalletDto.getMaxIdlePeriod();
		IdleUnit idleUnit = updateWalletDto.getIdleUnit();
		Integer creditBuffer = updateWalletDto.getCreditBuffer();
		boolean isOneTime = updateWalletDto.isOneTime();
		Integer utilizationAlert1Threshold = updateWalletDto.getUtilizationAlert1Threshold();
		String utilizationAlert1Receivers = updateWalletDto.getUtilizationAlert1Receivers();
		String utilizationAlert1Bccs = updateWalletDto.getUtilizationAlert1Bccs();
		Integer utilizationAlert2Threshold = updateWalletDto.getUtilizationAlert2Threshold();
		String utilizationAlert2Receivers = updateWalletDto.getUtilizationAlert2Receivers();
		String utilizationAlert2Bccs = updateWalletDto.getUtilizationAlert2Bccs();
		Integer forfeitAlert1Threshold = updateWalletDto.getForfeitAlert1Threshold();
		String forfeitAlert1Receivers = updateWalletDto.getForfeitAlert1Receivers();
		String forfeitAlert1Bccs = updateWalletDto.getForfeitAlert1Bccs();
		Integer forfeitAlert2Threshold = updateWalletDto.getForfeitAlert2Threshold();
		String forfeitAlert2Receivers = updateWalletDto.getForfeitAlert2Receivers();
		String forfeitAlert2Bccs = updateWalletDto.getForfeitAlert2Bccs();
		
		walletService.updateWallet(wallet, status, maxIdlePeriod, idleUnit, creditBuffer, remianBuffer, isOneTime, utilizationAlert1Threshold, 
				utilizationAlert1Receivers, utilizationAlert1Bccs, utilizationAlert2Threshold, utilizationAlert2Receivers, 
				utilizationAlert2Bccs, forfeitAlert1Threshold, forfeitAlert1Receivers, forfeitAlert1Bccs, 
				forfeitAlert2Threshold, forfeitAlert2Receivers, forfeitAlert2Bccs);
		WalletDto result  = wallet.toWalletDto();
		return result;
	}
	
	@GET
	@Path("search-wallets-by-customer")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Search Wallets", notes = "Version 1.0, Last Modified Date: 2018-03-05", response = GoodResponseModel.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public WalletCollectionDto searchWalletByCustomer(@ApiParam(value = "Organization Id", required = true) @QueryParam("organizationId") String organizationId) {
		PortalExceptionUtils.throwIfNullOrEmptyString(organizationId, PortalErrorCode.MISS_PARAM_ORGNIAZATION_ID);
		
		ResultList<Wallet> walletResultList = walletService.searchWalletByCustomer(organizationId);
		List<WalletDto> walletDtos = walletResultList.getItems().stream().map(w->w.toWalletDto()).collect(Collectors.toList());
		//create wallet dto
		WalletCollectionDto dto = new WalletCollectionDto();
		dto.setWallets(walletDtos);
		
		return dto;
	}
	
	@GET
	@Path("search-wallets")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Search Wallets", notes = "Version 1.0, Last Modified Date: 2018-03-05", response = GoodResponseModel.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public WalletCollectionDto searchWalletsByAndFilters(
			@ApiParam(value = "Organization Name", required = false) @QueryParam("organizationName") String organizationName,
			@ApiParam(value = "Organization Id", required = false) @QueryParam("organizationId") String organizationId,
			@ApiParam(value = "User Id", required = true) @QueryParam("userId") String userId,
			@ApiParam(value = "Parent Organization Name", required = false) @QueryParam("parentOrganizationName") String parentOrganizationName,
			@ApiParam(value = "Parent Organization Id", required = false) @QueryParam("parentOrganizationId") String parentOrganizationId,
			@ApiParam(value = "Wallet Id", required = false) @QueryParam("walletId") String walletId,
			@ApiParam(value = "Wallet Name", required = false) @QueryParam("walletName") String walletName,
			@ApiParam(value = "Wallet Type", required = false) @QueryParam("walletType") WalletType walletType,
			@ApiParam(value = "walletStatus", required = false) @QueryParam("walletStatus") WalletStatus status,
			@ApiParam(value = "Minimun Balance", required = false) @QueryParam("minBalance") Integer minBalance,
			@ApiParam(value = "Maximun Balance", required = false) @QueryParam("maxBalance") Integer maxBalance,
			@ApiParam(value = "Minimun Available", required = false) @QueryParam("minAvailable") Integer minAvailable, 
			@ApiParam(value = "Maximun Available", required = false) @QueryParam("maxAvailable") Integer maxAvailable,
			@ApiParam(value = "Minimun Credit buffer", required = false) @QueryParam("minCreditBuffer") Integer minCreditBuffer, 
			@ApiParam(value = "Maximun Credit buffer", required = false) @QueryParam("maxCreditBuffer") Integer maxCreditBuffer,
			@ApiParam(value = "Minimun Alert Threshod", required = false) @QueryParam("minAlertThreshold") Integer minAlertThreshold, 
			@ApiParam(value = "Maximun Alert Threshod", required = false) @QueryParam("maxAlertThreshold") Integer maxAlertThreshold,
			@ApiParam(value = "Sort", required = false) @QueryParam("sort") @DefaultValue("uid=asc") String sort,
			@ApiParam(value = "Offset", required = false) @QueryParam("offset") @DefaultValue("0") Integer offset,
			@ApiParam(value = "MaxRows", required = false) @QueryParam("maxRows") @DefaultValue("20") Integer maxRows) {
		
		// get wallet list
		ResultList<Wallet> walletResultList = walletService.searchWalletsByAndFilters(rc, organizationId, organizationName,userId, 
				parentOrganizationName, parentOrganizationId, walletId, walletName, walletType, status, minBalance, maxBalance,minAvailable, 
				maxAvailable, minCreditBuffer,maxCreditBuffer, minAlertThreshold, maxAlertThreshold, sort, offset, maxRows);
		
		List<WalletDto> walletDtos = walletResultList.getItems().stream().map(w->w.toWalletDto()).collect(Collectors.toList());
		//create wallet dto
		WalletCollectionDto dto = WalletCollectionDto.create(walletDtos,walletResultList.getTotalNumOfItems(),offset,maxRows);
		
		return dto;
	}
	
	@GET
	@Path("search-wallet")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Search Wallet", notes = "Version 1.0, Last Modified Date: 2018-03-05", response = GoodResponseModel.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public WalletCollectionDto searchWalletsByOrFilter(
			@ApiParam(value = "Organization Id", required = false) @QueryParam("organizationId") String organizationId,
			@ApiParam(value = "Global Filter", required = false) @QueryParam("globalFilter") String globalFilter,
			@ApiParam(value = "Sort", required = false) @QueryParam("sort") @DefaultValue("uid=asc") String sort,
			@ApiParam(value = "Offset", required = false) @QueryParam("offset") @DefaultValue("0") Integer offset,
			@ApiParam(value = "MaxRows", required = false) @QueryParam("maxRows") @DefaultValue("20") Integer maxRows) {
		// get wallet list
		ResultList<Wallet> walletResultList = walletService.searchWalletsByOrFilter(organizationId, globalFilter, sort, offset, maxRows);
		List<WalletDto> walletDtos = walletResultList.getItems().stream().map(w->w.toWalletDto()).collect(Collectors.toList());
		//create wallet dto
		WalletCollectionDto dto = WalletCollectionDto.create(walletDtos,walletResultList.getTotalNumOfItems(),offset,maxRows);
		
		return dto;
	}
	
	@POST
	@Logged(actionType = AuditingActionType.DISABLE_WALLET)
	@Transactional
	@Path("disable-wallet/{walletId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Disable Wallet", notes = "Version 1.0, Last Modified Date: 2016-07-28", response = GoodResponseModel.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public WalletDto disableWallet(@PathParam("walletId") String walletId, WalletDto walletDto) {
		logger.debug("begin disableWallet, walletId = {}",walletId);
		PortalExceptionUtils.throwIfNullOrEmptyString(walletId, PortalErrorCode.MISS_PARAM_WALLET_ID);
		PortalExceptionUtils.throwIfNull(walletDto, PortalErrorCode.INVALID_PAYLOAD_DATA);
		walletDto.validateDisableWallet();

		Wallet wallet = PortalExceptionUtils.throwIfEmpty(walletService.findWalletById(walletId), PortalErrorCode.INVALID_WALLET_ID);
		PortalExceptionUtils.throwIfFalse(wallet.isActive(), PortalErrorCode.WALLET_IS_INACTIVE);
		
		wallet.disable(rc.getRequestUserId());
		return wallet.toWalletDto();
	}
	
	
	@POST
	@Logged(actionType = AuditingActionType.ENABLE_WALLET)
	@Transactional
	@Path("enable-wallet/{walletId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Enable Wallet", notes = "Version 1.0, Last Modified Date: 2016-07-28", response = GoodResponseModel.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public WalletDto enableWallet(@PathParam("walletId") String walletId, WalletDto walletDto) {
		logger.debug("begin enableWallet, walletId = {}",walletId);
		PortalExceptionUtils.throwIfNullOrEmptyString(walletId, PortalErrorCode.MISS_PARAM_WALLET_ID);
		PortalExceptionUtils.throwIfNull(walletDto, PortalErrorCode.INVALID_PAYLOAD_DATA);
		walletDto.validateEnableWallet();

		Wallet wallet = PortalExceptionUtils.throwIfEmpty(walletService.findWalletById(walletId), PortalErrorCode.INVALID_WALLET_ID);
		PortalExceptionUtils.throwIfFalse(wallet.isInactive(), PortalErrorCode.WALLET_IS_ACTIVE);
		
		wallet.enableWallet(rc.getRequestUserId());
		return wallet.toWalletDto();
	}
	
	@Path("{walletId}/adjustments")
	public AdjustmentResources getAdjustmentResources(@ApiParam(value="Wallet Id", required = true) @PathParam("walletId") String walletId){
		adjustmentresources.setWalletId(walletId);
		return adjustmentresources;
	}

}
