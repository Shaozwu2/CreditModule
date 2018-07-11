//package com.kooppi.nttca.wallet.rest.wallet.resources;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import javax.ejb.Stateless;
//import javax.inject.Inject;
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
//
//import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
//import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;
//import com.kooppi.nttca.portal.pms.client.organization.domain.OrganizationType;
//import com.kooppi.nttca.portal.pms.client.user.domain.User;
//import com.kooppi.nttca.portal.pms.client.user.service.UserService;
//import com.kooppi.nttca.portal.wallet.dto.wallet.WalletCollectionDto;
//import com.kooppi.nttca.portal.wallet.dto.wallet.WalletDto;
//import com.kooppi.nttca.wallet.common.persistence.domain.Wallet;
//import com.kooppi.nttca.wallet.common.swagger.ui.model.BadResponseModel;
//import com.kooppi.nttca.wallet.common.swagger.ui.model.GoodResponseModel;
//import com.kooppi.nttca.wallet.rest.wallet.service.WalletService;
//
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiResponse;
//import io.swagger.annotations.ApiResponses;
//
//@Stateless
//@Path("users/{userId}/wallets")
//public class UserWalletResources {
//	
//	@Inject
//	private UserService userService;
//	
//	@Inject
//	private WalletService walletService;
//	
//	@GET
//	@Path("get-customer-user-wallets")
//	@Produces(MediaType.APPLICATION_JSON)
//	@ApiOperation(value = "Get customer Wallets", notes = "Version 1.0, Last Modified Date: 2018-03-05", response = GoodResponseModel.class )
//	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
//	@ApiImplicitParams({
//	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
//	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
//	public WalletCollectionDto getCustomerUserWallets(@PathParam("userId") String userId){
//		
//		PortalExceptionUtils.throwIfNullOrEmptyString(userId, PortalErrorCode.MISS_PARAM_USER_ID);
//		//validate user
//		Optional<User> userOpt = userService.findUserByUserId(userId);
//		User user = PortalExceptionUtils.throwIfEmpty(userOpt, PortalErrorCode.INVALID_USER_ID);
//		
//		//only accept query customer user's wallet
//		PortalExceptionUtils.throwIfTrue(user.getOrganizationType() != OrganizationType.CUSTOMER, PortalErrorCode.INVALID_ORGANIZATION_USER);
//		
//		List<Wallet> wallets = walletService.findCustomerWalletByUser(user);
//		List<WalletDto> walletDtos = wallets.stream().map(w->w.toWalletDto()).collect(Collectors.toList());
//		WalletCollectionDto dto = WalletCollectionDto.create(walletDtos, null, null, null);
//		return dto;
//	}
//	
//}
