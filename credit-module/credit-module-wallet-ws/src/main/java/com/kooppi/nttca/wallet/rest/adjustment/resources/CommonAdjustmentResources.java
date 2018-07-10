package com.kooppi.nttca.wallet.rest.adjustment.resources;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.common.filter.context.RequestContextImpl;
import com.kooppi.nttca.portal.wallet.domain.AdjustmentStatus;
import com.kooppi.nttca.portal.wallet.domain.AdjustmentType;
import com.kooppi.nttca.portal.wallet.dto.adjustment.AdjustmentCollectionDto;
import com.kooppi.nttca.portal.wallet.dto.adjustment.AdjustmentDto;
import com.kooppi.nttca.portal.wallet.dto.adjustment.AdjustmentSearchDto;
import com.kooppi.nttca.wallet.common.persistence.domain.Adjustment;
import com.kooppi.nttca.wallet.common.swagger.ui.model.BadResponseModel;
import com.kooppi.nttca.wallet.common.swagger.ui.model.GoodResponseModel;
import com.kooppi.nttca.wallet.rest.adjustment.service.AdjustmentService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Stateless
@Path("adjustments")
public class CommonAdjustmentResources {

	@Inject
	private RequestContextImpl rc;
	
	@Inject
	private AdjustmentService adjustmentService;
	
	@GET
	@Path("search-adjustments")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Search Adjustment by and filter", notes = "Version 1.0, Last Modified Date: 2018-03-20", response = GoodResponseModel.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public AdjustmentCollectionDto searchAdjustmentsByAndFilters(
			@QueryParam("walletId") String walletId,
			@QueryParam("organizationId") String organizationId,
			@QueryParam("transactionId") String transactionId,
			@QueryParam("parentTransactionId") String parentTransactionId,
			@QueryParam("refNumber") String refNumber,
			@QueryParam("adjustmentType") AdjustmentType adjustmentType,
			@QueryParam("minTransactionTimeStamp") Timestamp minTransactionTimeStamp, @QueryParam("maxTransactionTimeStamp") Timestamp maxTransactionTimeStamp,
			@QueryParam("minAmount") Integer minAmount, @QueryParam("maxAmount") Integer maxAmount,
			@QueryParam("adjustmentStatus") AdjustmentStatus adjustmentStatus,
			@QueryParam("sort") @DefaultValue("uid=asc") String sort,
			@QueryParam("offset") @DefaultValue("0") Integer offset,
			@QueryParam("maxRows") @DefaultValue("20") Integer maxRows,
			@QueryParam("isChild") Boolean isChild) {
		
		//call Ser
		ResultList<Adjustment> adjustmentResultList = adjustmentService.serachAdjustment(rc, walletId, organizationId, transactionId, parentTransactionId, refNumber, adjustmentType, minTransactionTimeStamp, maxTransactionTimeStamp, minAmount, maxAmount, adjustmentStatus, sort, offset, maxRows, isChild);
		
		List<AdjustmentDto> adjustmentDtos = adjustmentResultList.getItems().stream().map(a->a.toAdjustmentDto()).collect(Collectors.toList());
		AdjustmentCollectionDto dto = AdjustmentCollectionDto.create(adjustmentDtos, adjustmentResultList.getTotalNumOfItems(), offset, maxRows);
		return dto;
	}
	
	@POST
	@Path("search-adjustment")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Search Adjustment by or filter", notes = "Version 1.0, Last Modified Date: 2018-05-21", response = GoodResponseModel.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public AdjustmentCollectionDto searchAdjustmentsByOrFilters(
			@QueryParam("organizationId") String organizationId,
			@QueryParam("walletId") String walletId,
			@QueryParam("isExpiredCotract") Boolean isExpiredCotract,
			@QueryParam("globalFilter") String globalFilter,
			@QueryParam("adjustmentStatus") AdjustmentStatus adjustmentStatus,
			@QueryParam("sort") String sort,
			@QueryParam("offset") @DefaultValue("0") Integer offset,
			@QueryParam("maxRows") @DefaultValue("20") Integer maxRows,
			AdjustmentSearchDto searchDto) {
		//call Ser
		ResultList<Adjustment> adjustmentResultList = adjustmentService.searchAdjustmentsByOrFilter(rc, organizationId, walletId, adjustmentStatus, isExpiredCotract, globalFilter, sort, offset, maxRows, searchDto);
		
		List<AdjustmentDto> adjustmentDtos = adjustmentResultList.getItems().stream().map(a->a.toAdjustmentDto()).collect(Collectors.toList());
		AdjustmentCollectionDto dto = AdjustmentCollectionDto.create(adjustmentDtos, adjustmentResultList.getTotalNumOfItems(), offset, maxRows);
		return dto;
	}
}
