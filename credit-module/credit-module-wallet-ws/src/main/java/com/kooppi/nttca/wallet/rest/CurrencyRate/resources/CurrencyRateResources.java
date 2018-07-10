package com.kooppi.nttca.wallet.rest.CurrencyRate.resources;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.wallet.dto.adjustment.CurrencyRateCollectionDto;
import com.kooppi.nttca.portal.wallet.dto.adjustment.CurrencyRateDto;
import com.kooppi.nttca.wallet.common.persistence.domain.CurrencyRate;
import com.kooppi.nttca.wallet.common.swagger.ui.model.BadResponseModel;
import com.kooppi.nttca.wallet.common.swagger.ui.model.GoodResponseModel;
import com.kooppi.nttca.wallet.rest.CurrencyRate.service.CurrencyRateService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Stateless
@Api(value = "CurrencyRate (Version 1)")
@Path("currencyRates")
public class CurrencyRateResources {

	@Inject
	private CurrencyRateService currencyRateService;
	
	@GET
	@Path("get-all-currency-rates")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get All currencies", notes = "Version 1.0, Last Modified Date: 2018-03-26", response = GoodResponseModel.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public CurrencyRateCollectionDto getAllCurrencyRates() {
		ResultList<CurrencyRate> currencyRateList = currencyRateService.getAllCurrencyRates();
		List<CurrencyRateDto> currencyRateDtos = currencyRateList.getItems().stream().map(cr -> cr.toCurrencyRateDto()).collect(Collectors.toList());
		CurrencyRateCollectionDto dto = CurrencyRateCollectionDto.create(currencyRateDtos, currencyRateList.getTotalNumOfItems());
		return dto;
	}
}
