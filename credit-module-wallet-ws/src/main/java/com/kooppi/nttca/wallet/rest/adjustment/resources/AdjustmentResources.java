	package com.kooppi.nttca.wallet.rest.adjustment.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kooppi.nttca.portal.common.auditlog.AuditingActionType;
import com.kooppi.nttca.portal.common.auditlog.Logged;
import com.kooppi.nttca.portal.common.filter.context.RequestContextImpl;
import com.kooppi.nttca.portal.common.filter.response.dto.EmptyResponseResult;
import com.kooppi.nttca.portal.common.filter.response.dto.ResponseHeader;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;
import com.kooppi.nttca.portal.wallet.dto.adjustment.AdjustmentDto;
import com.kooppi.nttca.portal.wallet.dto.adjustment.CreateAdjustmentDto;
import com.kooppi.nttca.portal.wallet.dto.adjustment.UpdateAdjustmentDto;
import com.kooppi.nttca.portal.wallet.dto.priceBook.PriceBookDto;
import com.kooppi.nttca.wallet.common.persistence.domain.Adjustment;
import com.kooppi.nttca.wallet.common.persistence.domain.PriceBook;
import com.kooppi.nttca.wallet.common.persistence.domain.Wallet;
import com.kooppi.nttca.wallet.common.swagger.ui.model.BadResponseModel;
import com.kooppi.nttca.wallet.common.swagger.ui.model.GoodResponseModel;
import com.kooppi.nttca.wallet.rest.adjustment.service.AdjustmentService;
import com.kooppi.nttca.wallet.rest.priceBook.repository.PriceBookRepository;
import com.kooppi.nttca.wallet.rest.wallet.service.WalletService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Stateless
@Api(hidden = true)
public class AdjustmentResources {
	
	private static final Logger logger = LoggerFactory.getLogger(AdjustmentResources.class);

	@Inject
	private RequestContextImpl rc;
	
	@Inject
	private WalletService walletService;
	
	@Inject
	private AdjustmentService adjustmentService;
	
	@Inject
	private PriceBookRepository priceBookService;
	
	@POST
	@Path("create-adjustment")
	@Transactional
	@Logged(actionType = AuditingActionType.CREATE_CONTRACT)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create Adjustment", notes = "Version 1.0, Last Modified Date: 2018-03-20", response = GoodResponseModel.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public AdjustmentDto createAdjustment(@ApiParam(value = "Adjustment Dto", required = true) CreateAdjustmentDto adjustmentDto) {
		logger.debug("begin addAdjustment");
		PortalExceptionUtils.throwIfNull(adjustmentDto, PortalErrorCode.INVALID_PAYLOAD_DATA);
		//validate dto
		adjustmentDto.validateCreateAdjustment();
		//must find a active wallet
		PortalExceptionUtils.throwIfNullOrEmptyString(walletId, PortalErrorCode.MISS_PARAM_WALLET_ID);
		PortalExceptionUtils.throwIfEmpty(walletService.findWalletById(walletId), PortalErrorCode.INVALID_WALLET_ID);
				
		Adjustment adjustment = adjustmentService.createAdjustment(walletId, adjustmentDto.getRefNumber(),
				adjustmentDto.getAdjustmentType(), adjustmentDto.getCompanyCode(), adjustmentDto.getAmount(),
				adjustmentDto.getCurrencyAmount(), adjustmentDto.getCurrencyCode(), adjustmentDto.getExchangeRate(),
				adjustmentDto.getDescription(), adjustmentDto.getContractEffectiveDate(),
				adjustmentDto.getContractTerminationDate(), adjustmentDto.getCreditExpiryDate(),
				adjustmentDto.getTerminatedReason(), adjustmentDto.getBuLists(), adjustmentDto.getProductLists(),
				adjustmentDto.getIsAllBu(), adjustmentDto.getIsAllProduct());
		AdjustmentDto dto = adjustment.toAdjustmentDto();
		
		// set List<PriceBookDto> to AdjustmentDto
		List<PriceBook> priceBookList = priceBookService.findPriceBooksByPartNumber(adjustment.getPartNoList());
		List<PriceBookDto> priceBookDtoList = new ArrayList<PriceBookDto>();
		for (PriceBook pb : priceBookList)
			priceBookDtoList.add(pb.toPriceBookDto());
		dto.setPriceBookList(priceBookDtoList);
		
		return dto;
	}
	
	
	@GET
	@Path("get-adjustment/{contractId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get Adjustment", notes = "Version 1.0, Last Modified Date: 2018-03-20", response = ResponseHeader.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public AdjustmentDto getAdjustment(@ApiParam(value="Contract Id", required = true) @PathParam("contractId") String contractId){
		//validate parameter
		PortalExceptionUtils.throwIfNullOrEmptyString(walletId, PortalErrorCode.MISS_PARAM_WALLET_ID);
		PortalExceptionUtils.throwIfNullOrEmptyString(contractId, PortalErrorCode.MISS_PATH_PARAM_CONTRACT_ID);

		Wallet wallet = PortalExceptionUtils.throwIfEmpty(walletService.findWalletById(walletId), PortalErrorCode.INVALID_WALLET_ID);
		PortalExceptionUtils.throwIfFalse(wallet.isActive(), PortalErrorCode.WALLET_IS_INACTIVE);
		
		Adjustment adj = PortalExceptionUtils.throwIfEmpty(adjustmentService.findByContractId(contractId), PortalErrorCode.INVALID_CONTRACT_ID);
		
		//check adjustment belong to the wallet
		PortalExceptionUtils.throwIfFalse(adj.getWalletId().equals(wallet.getWalletId()), PortalErrorCode.INVALID_CONTRACT_ID);
			
		AdjustmentDto dto = adj.toAdjustmentDto();
		
		// set List<PriceBookDto> to AdjustmentDto
		List<PriceBook> priceBookList = priceBookService.findPriceBooksByPartNumber(adj.getPartNoList());
		List<PriceBookDto> priceBookDtoList = new ArrayList<PriceBookDto>();
		for (PriceBook pb : priceBookList)
			priceBookDtoList.add(pb.toPriceBookDto());
		dto.setPriceBookList(priceBookDtoList);
				
		return dto;
	}
	
	@POST
	@Path("update-adjustment/{contractId}")
	@Transactional
	@Logged(actionType = AuditingActionType.UPDATE_CONTRACT)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update Adjustment", notes = "Version 1.0, Last Modified Date: 2018-03-20", response = ResponseHeader.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public AdjustmentDto updateAdjustment(@ApiParam(value="Contract Id", required = true) @PathParam("contractId") String contractId, 
			@ApiParam(value = "Adjustment Dto", required = true) UpdateAdjustmentDto dto){
		//validate parameter
		PortalExceptionUtils.throwIfNullOrEmptyString(walletId, PortalErrorCode.MISS_PARAM_WALLET_ID);
		PortalExceptionUtils.throwIfNullOrEmptyString(contractId, PortalErrorCode.MISS_PATH_PARAM_CONTRACT_ID);
		
		Wallet wallet = PortalExceptionUtils.throwIfEmpty(walletService.findWalletById(walletId), PortalErrorCode.INVALID_WALLET_ID);
		PortalExceptionUtils.throwIfFalse(wallet.isActive(), PortalErrorCode.WALLET_IS_INACTIVE);
		
		Adjustment adj = PortalExceptionUtils.throwIfEmpty(adjustmentService.findByContractId(contractId), PortalErrorCode.INVALID_CONTRACT_ID);

		//check adjustment belong to the wallet
		PortalExceptionUtils.throwIfFalse(adj.getWalletId().equals(wallet.getWalletId()), PortalErrorCode.INVALID_CONTRACT_ID);
		
		//validate dto
		dto.validateUpdateAdjustment(adj.toAdjustmentDto());
		
		Adjustment updatedAdjustment = adjustmentService.updateAdjustment(walletId, adj, dto.getRefNumber(),
				dto.getCompanyCode(), dto.getAmount(), dto.getCurrencyAmount(), dto.getCurrencyCode(),
				dto.getExchangeRate(), dto.getDescription(), dto.getContractEffectiveDate(),
				dto.getContractTerminationDate(), dto.getCreditExpiryDate(), dto.getTerminatedReason(),
				dto.getBuLists(), dto.getProductLists(), dto.getIsAllBu(), dto.getIsAllProduct());
		AdjustmentDto result = updatedAdjustment.toAdjustmentDto();
		
		// set List<PriceBookDto> to AdjustmentDto
		List<PriceBook> priceBookList = priceBookService.findPriceBooksByPartNumber(updatedAdjustment.getPartNoList());
		List<PriceBookDto> priceBookDtoList = new ArrayList<PriceBookDto>();
		for (PriceBook pb : priceBookList)
			priceBookDtoList.add(pb.toPriceBookDto());
		result.setPriceBookList(priceBookDtoList);

		return result;
	}
	
	@DELETE
	@Path("delete-adjustment/{contractId}")
	@Transactional
	@Logged(actionType = AuditingActionType.DELETE_CONTRACT)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Delete Adjustment", notes = "Version 1.0, Last Modified Date: 2018-03-20", response = GoodResponseModel.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public EmptyResponseResult deleteAdjustment(@PathParam("contractId") String contractId) {
		//validate parameter
		PortalExceptionUtils.throwIfNullOrEmptyString(walletId, PortalErrorCode.MISS_PARAM_WALLET_ID);
		PortalExceptionUtils.throwIfNullOrEmptyString(contractId, PortalErrorCode.MISS_PATH_PARAM_CONTRACT_ID);
		
		Wallet wallet = PortalExceptionUtils.throwIfEmpty(walletService.findWalletById(walletId), PortalErrorCode.INVALID_WALLET_ID);
		PortalExceptionUtils.throwIfFalse(wallet.isActive(), PortalErrorCode.WALLET_IS_INACTIVE);
		
		Adjustment adj = PortalExceptionUtils.throwIfEmpty(adjustmentService.findByContractId(contractId), PortalErrorCode.INVALID_CONTRACT_ID);

		//check adjustment belong to the wallet
		PortalExceptionUtils.throwIfFalse(adj.getWalletId().equals(wallet.getWalletId()), PortalErrorCode.INVALID_CONTRACT_ID);
		
		adjustmentService.deleteOrCancelAdjustment(adj, rc.getRequestUserId());
		return EmptyResponseResult.create();
	}
	
	@POST
	@Path("forfeit-adjustment/{contractId}")
	@Transactional
	@Logged(actionType = AuditingActionType.FORFEIT_CONTRACT)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Forfeit Adjustment", notes = "Version 1.0, Last Modified Date: 2018-05-15", response = ResponseHeader.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public EmptyResponseResult forfeitAdjustment(@ApiParam(value="Contract Id", required = true) @PathParam("contractId") String contractId){
		//validate parameter
		PortalExceptionUtils.throwIfNullOrEmptyString(walletId, PortalErrorCode.MISS_PARAM_WALLET_ID);
		PortalExceptionUtils.throwIfNullOrEmptyString(contractId, PortalErrorCode.MISS_PATH_PARAM_CONTRACT_ID);
		
		Wallet wallet = PortalExceptionUtils.throwIfEmpty(walletService.findWalletById(walletId), PortalErrorCode.INVALID_WALLET_ID);
		Adjustment adj = PortalExceptionUtils.throwIfEmpty(adjustmentService.findByContractId(contractId), PortalErrorCode.INVALID_CONTRACT_ID);
		PortalExceptionUtils.throwIfTrue(adj.isPending(), PortalErrorCode.CONTRACT_IS_NOT_IN_USE);
		PortalExceptionUtils.throwIfTrue(adj.isClosed(), PortalErrorCode.CONTRACT_WAS_CLOSED);
		PortalExceptionUtils.throwIfTrue(adj.isCancelled(), PortalErrorCode.CONTRACT_WAS_CANCELLED);
		
		adjustmentService.forfeitAdjustment(adj, rc.getRequestUserId());
		//check adjustment belong to the wallet
		PortalExceptionUtils.throwIfFalse(adj.getWalletId().equals(wallet.getWalletId()), PortalErrorCode.INVALID_CONTRACT_ID);
		return EmptyResponseResult.create();
	}
	
	@POST
	@Path("cancel-forfeit-adjustment/{contractId}")
	@Transactional
	@Logged(actionType = AuditingActionType.CANCEL_FORFEIT_CONTRACT)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Cancel Forfeit Adjustment", notes = "Version 1.0, Last Modified Date: 2018-05-15", response = ResponseHeader.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
		@ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public EmptyResponseResult cancelForfeitAdjustment(@ApiParam(value="Contract Id", required = true) @PathParam("contractId") String contractId){
		//validate parameter
		PortalExceptionUtils.throwIfNullOrEmptyString(walletId, PortalErrorCode.MISS_PARAM_WALLET_ID);
		PortalExceptionUtils.throwIfNullOrEmptyString(contractId, PortalErrorCode.MISS_PATH_PARAM_CONTRACT_ID);
		
		Wallet wallet = PortalExceptionUtils.throwIfEmpty(walletService.findWalletById(walletId), PortalErrorCode.INVALID_WALLET_ID);
		Adjustment adj = PortalExceptionUtils.throwIfEmpty(adjustmentService.findByContractId(contractId), PortalErrorCode.INVALID_CONTRACT_ID);
		PortalExceptionUtils.throwIfTrue(adj.isPending(), PortalErrorCode.CONTRACT_IS_NOT_IN_USE);
		PortalExceptionUtils.throwIfTrue(adj.isCancelled(), PortalErrorCode.CONTRACT_WAS_CANCELLED);
		
		adjustmentService.cancelForfeitAdjustment(adj, rc.getRequestUserId());
		//check adjustment belong to the wallet
		PortalExceptionUtils.throwIfFalse(adj.getWalletId().equals(wallet.getWalletId()), PortalErrorCode.INVALID_CONTRACT_ID);
		return EmptyResponseResult.create();
	}
	
	private String walletId;
	
	public void setWalletId(String walletId){
		this.walletId = walletId;
	}
}
