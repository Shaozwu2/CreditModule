package com.kooppi.nttca.wallet.rest.priceBook.resources;

import java.util.List;
import java.util.Optional;
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

import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.common.auditlog.AuditingActionType;
import com.kooppi.nttca.portal.common.auditlog.Logged;
import com.kooppi.nttca.portal.common.filter.response.dto.ResponseHeader;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;
import com.kooppi.nttca.portal.wallet.dto.priceBook.GetPriceBookByPartNoDto;
import com.kooppi.nttca.portal.wallet.dto.priceBook.PriceBookCollectionDto;
import com.kooppi.nttca.portal.wallet.dto.priceBook.PriceBookDto;
import com.kooppi.nttca.portal.wallet.dto.priceBook.PriceBookItemDto;
import com.kooppi.nttca.portal.wallet.dto.priceBook.PriceBookItemsDto;
import com.kooppi.nttca.portal.wallet.dto.priceBook.UpdatePriceBookDto;
import com.kooppi.nttca.wallet.common.persistence.domain.PriceBook;
import com.kooppi.nttca.wallet.common.swagger.ui.model.BadResponseModel;
import com.kooppi.nttca.wallet.common.swagger.ui.model.GoodResponseModel;
import com.kooppi.nttca.wallet.rest.priceBook.service.PriceBookService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Stateless
@Api(value = "PriceBook(Version 1)")
@Path("priceBooks")
public class PriceBookResources {

	@Inject
	private PriceBookService priceBookService;
	
	@GET
	@Path("search-pricebook")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Search price book", notes = "Version 1.0, Last Modified Date: 2018-04-09", response = ResponseHeader.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public PriceBookCollectionDto searchPriceBooksByOrFilter(
			@QueryParam("globalFilter") String globalFilter,
			@QueryParam("sort") @DefaultValue("uid=asc") String sort,
			@QueryParam("offset") @DefaultValue("0") Integer offset,
			@QueryParam("maxRows") @DefaultValue("20") Integer maxRows) {
		ResultList<PriceBook> priceBookResultList = priceBookService.searchPriceBooksByOrFilter(globalFilter, sort, offset, maxRows);
		
		List<PriceBookDto> priceBookDtos = priceBookResultList.getItems().stream().map(pb -> pb.toPriceBookDto()).collect(Collectors.toList());
		PriceBookCollectionDto dto = PriceBookCollectionDto.create(priceBookDtos, priceBookResultList.getTotalNumOfItems(), offset, maxRows);
		return dto;
	}
	
	@GET
	@Path("get-pricebook-by-part-number/{partNo}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get price book by part no", notes = "Version 1.0, Last Modified Date: 2018-04-09", response = ResponseHeader.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public PriceBookDto getPriceBookByPartNumber(@PathParam("partNo") String partNo) {
		PortalExceptionUtils.throwIfNullOrEmptyString(partNo, PortalErrorCode.MISS_PARAM_PART_NO);
		Optional<PriceBook> pbOpt = priceBookService.findByPartNumber(partNo);
		PriceBook priceBook = PortalExceptionUtils.throwIfEmpty(pbOpt, PortalErrorCode.INVALID_PART_NO);
		PriceBookDto dto = priceBook.toPriceBookDto();
		return dto;
	}
	
	@POST
	@Logged(actionType = AuditingActionType.UPDATE_PRICE_BOOK)
	@Path("update-pricebook/{priceBookId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update price book", notes = "Version 1.0, Last Modified Date: 2018-04-09", response = ResponseHeader.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public PriceBookDto updatePriceBook(
			@ApiParam(value="Price Book Id", required = true) @PathParam("priceBookId") Long priceBookId,
			@ApiParam(value = "Price Book Dto", required = true) UpdatePriceBookDto dto) {
		PortalExceptionUtils.throwIfNull(priceBookId, PortalErrorCode.MISS_PARAM_PRICE_BOOK_ID);
		Optional<PriceBook> priceBookOpt = priceBookService.findByUid(priceBookId);
		PortalExceptionUtils.throwIfEmpty(priceBookOpt, PortalErrorCode.INVALID_PRICE_BOOK_ID);
		PriceBook priceBook = priceBookOpt.get();
		priceBook.setBuName(dto.getBuName());
		PriceBookDto result = priceBook.toPriceBookDto();
		
		return result;
	}
	
	@POST
	@Path("get-pricebooks")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "get price books", notes = "Version 1.0, Last Modified Date: 2018-04-09", response = ResponseHeader.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public PriceBookItemsDto getPriceBooksByPartNumber(GetPriceBookByPartNoDto getPriceBookByPartNoDto) {
//		PortalExceptionUtils.throwIfNullOrEmptyString(partNos, PortalErrorCode.MISS_PARAM_PART_NO);
		List<PriceBookItemDto> priceBookResultList = priceBookService.findPriceBooksByPartNumber(getPriceBookByPartNoDto);
		PriceBookItemsDto dto = PriceBookItemsDto.create(priceBookResultList);
		return dto;
	}
	
	@GET
	@Path("get-pricebook-by-buname/{buName}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get price book by buname", notes = "Version 1.0, Last Modified Date: 2018-05-15", response = ResponseHeader.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public PriceBookCollectionDto getPriceBookByBuName(@PathParam("buName") String buName) {
		PortalExceptionUtils.throwIfNullOrEmptyString(buName, PortalErrorCode.MISS_PARAM_BU_NAME);
		Optional<List<PriceBook>> pbOpt = priceBookService.findByBuName(buName);
		List<PriceBook> priceBook = PortalExceptionUtils.throwIfEmpty(pbOpt, PortalErrorCode.INVALID_BU_NAME);
		List<PriceBookDto> priceBookDtos = priceBook.stream().map(pb -> pb.toPriceBookDto()).collect(Collectors.toList());
		PriceBookCollectionDto dto = PriceBookCollectionDto.create(priceBookDtos, priceBookDtos.size(), 0, 0);
		return dto;
	}
}
