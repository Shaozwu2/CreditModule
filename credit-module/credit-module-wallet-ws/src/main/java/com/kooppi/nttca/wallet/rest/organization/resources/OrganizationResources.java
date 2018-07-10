package com.kooppi.nttca.wallet.rest.organization.resources;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.common.api.client.NewPortalApiClient;
import com.kooppi.nttca.portal.common.filter.response.dto.EmptyResponseResult;
import com.kooppi.nttca.portal.newportal.dto.CustomerItemDto;
import com.kooppi.nttca.portal.wallet.domain.IdleUnit;
import com.kooppi.nttca.wallet.common.persistence.domain.Wallet;
import com.kooppi.nttca.wallet.common.swagger.ui.model.BadResponseModel;
import com.kooppi.nttca.wallet.common.swagger.ui.model.GoodResponseModel;
import com.kooppi.nttca.wallet.rest.wallet.service.WalletService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Stateless
@Api(value = "Organization(Version 1)")
@Path("organizations")
public class OrganizationResources {

	private static final Logger logger = LoggerFactory.getLogger(OrganizationResources.class);

	@Inject
	private NewPortalApiClient npClient;

	@Inject
	private WalletService walletService;

	@POST
	@Path("patch-wallet")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Patch wallet to existing customer", notes = "Version 1.0, Last Modified Date: 2016-07-26", response = GoodResponseModel.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class),
			@ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-App-ID", value = "appId", required = true, dataType = "string", paramType = "header"),
			@ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
			@ApiImplicitParam(name = "X-User-ID", value = "userId", required = false, dataType = "string", paramType = "header") })
	public EmptyResponseResult patchWalletToExistingCustomer() {
		logger.info("Start data patching.....");
		List<CustomerItemDto> allNewPortalCustomer = npClient.getAllNewPortalCustomer();

		List<String> cdsCompanyIdList = allNewPortalCustomer.stream().map(c -> c.getCustomerId())
				.collect(Collectors.toList());

		for (String cdsCompanyId : cdsCompanyIdList) {
			ResultList<Wallet> walletResultList = walletService.searchWalletByCustomer(cdsCompanyId);
			if (walletResultList.getTotalNumOfItems() == 0) {
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
				logger.info("Creating a default wallet for customer with cdsCompanyId = " + cdsCompanyId);
				walletService.createWallet(cdsCompanyId, null, null,
						null, maxIdlePeriod, idleUnit, expiredOn, creditBuffer, isOneTime,
						utilizationAlert1Threshold, utilizationAlert1Receivers, utilizationAlert1Bccs,
						utilizationAlert2Threshold, utilizationAlert2Receivers, utilizationAlert2Bccs,
						forfeitAlert1Threshold, forfeitAlert1Receivers, forfeitAlert1Bccs, forfeitAlert2Threshold,
						forfeitAlert2Receivers, forfeitAlert2Bccs);
			}
		}
		logger.info("Finish data patching.....");
		return EmptyResponseResult.create();
	}
}
