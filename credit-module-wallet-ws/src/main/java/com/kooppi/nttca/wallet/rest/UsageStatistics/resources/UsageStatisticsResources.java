package com.kooppi.nttca.wallet.rest.UsageStatistics.resources;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kooppi.nttca.portal.common.filter.response.dto.ResponseHeader;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;
import com.kooppi.nttca.portal.wallet.dto.statistics.MonthlyUsageStatisticsDto;
import com.kooppi.nttca.portal.wallet.dto.statistics.StatisticsDataDto;
import com.kooppi.nttca.wallet.common.persistence.domain.Wallet;
import com.kooppi.nttca.wallet.common.swagger.ui.model.BadResponseModel;
import com.kooppi.nttca.wallet.common.swagger.ui.model.GoodResponseModel;
import com.kooppi.nttca.wallet.rest.UsageStatistics.service.UsageStatisticsService;
import com.kooppi.nttca.wallet.rest.wallet.service.WalletService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Stateless
@Api(value = "Statistics(Version 1)")
@Path("statistics")
public class UsageStatisticsResources {
	
	private static final Logger logger = LoggerFactory.getLogger(UsageStatisticsResources.class);
	
	@Inject
	private UsageStatisticsService usageStatisticsService;
	
	@Inject
	private WalletService walletService;
	
	@GET
	@Path("get-monthlyStatistics/{walletId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get Monthly Statistics", notes = "Version 1.0, Last Modified Date: 2016-08-12", response = ResponseHeader.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public MonthlyUsageStatisticsDto getMonthlyStatistics(@ApiParam(value = "Wallet Id", required = true) @PathParam("walletId") String walletId){
		logger.debug("begin get monthly statistics, walletId = {}", walletId);
		PortalExceptionUtils.throwIfNullOrEmptyString(walletId, PortalErrorCode.MISS_PARAM_WALLET_ID);
		
		//get last 6 months usage records.
		List<StatisticsDataDto> statisticDatas = usageStatisticsService.getMonthlyUsageStatisticsDataList(walletId);
		
		//get available credit.
		Optional<Wallet> optWallet = walletService.findWalletById(walletId);
		Wallet wallet = PortalExceptionUtils.throwIfEmpty(optWallet, PortalErrorCode.INVALID_WALLET_ID);
		
		return MonthlyUsageStatisticsDto.create(wallet.getAvailableCredits(), statisticDatas);
	}

	@GET
	@Path("get-dailyStatistics/{walletId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get Daily Statistics", notes = "Version 1.0, Last Modified Date: 2016-08-12", response = ResponseHeader.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
		@ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public MonthlyUsageStatisticsDto getDailylyStatistics(@ApiParam(value = "Wallet Id", required = true) @PathParam("walletId") String walletId){
		logger.debug("begin get daily statistics, walletId = {}", walletId);
		PortalExceptionUtils.throwIfNullOrEmptyString(walletId, PortalErrorCode.MISS_PARAM_WALLET_ID);
		
		//get last 6 months usage records.
		List<StatisticsDataDto> statisticDatas = usageStatisticsService.getDailyUsageStatisticsDataList(walletId);
		
		//get available credit.
		Optional<Wallet> optWallet = walletService.findWalletById(walletId);
		Wallet wallet = PortalExceptionUtils.throwIfEmpty(optWallet, PortalErrorCode.INVALID_WALLET_ID);
		
		return MonthlyUsageStatisticsDto.create(wallet.getAvailableCredits(), statisticDatas);
	}
	
}
