package com.kooppi.nttca.wallet.rest.transaction.resources;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.common.filter.context.RequestContextImpl;
import com.kooppi.nttca.portal.common.filter.response.dto.ResponseHeader;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;
import com.kooppi.nttca.portal.wallet.dto.transaction.TransactionCollectionDto;
import com.kooppi.nttca.portal.wallet.dto.transaction.TransactionDto;
import com.kooppi.nttca.wallet.common.persistence.domain.Transaction;
import com.kooppi.nttca.wallet.common.swagger.ui.model.BadResponseModel;
import com.kooppi.nttca.wallet.common.swagger.ui.model.GoodResponseModel;
import com.kooppi.nttca.wallet.rest.transaction.dto.TransactionBreakdownCsv;
import com.kooppi.nttca.wallet.rest.transaction.dto.TransactionSummaryCsv;
import com.kooppi.nttca.wallet.rest.transaction.service.TransactionService;
import com.opencsv.CSVWriter;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Stateless
@Api(value = "Transaction(Version 1)")
@Path("transactions")
public class TransactionResources {
	
	private static final Logger logger = LoggerFactory.getLogger(TransactionResources.class);
	
	@Inject
	private RequestContextImpl rc;
	
	@Inject
	private TransactionService transactionService;
	
	@GET
	@Path("get-transaction/{transactionId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get Transaction", notes = "Version 1.0, Last Modified Date: 2018-03-20", response = ResponseHeader.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public TransactionDto getTransaction(@ApiParam(value = "Transaction Id", required = true) @PathParam("transactionId") String transactionId){
		logger.debug("begin getTransaction, transactionId = {}", transactionId);
		PortalExceptionUtils.throwIfNullOrEmptyString(transactionId, PortalErrorCode.MISS_PARAM_TRANSACTION_ID);
		
		Transaction transaction = PortalExceptionUtils.throwIfEmpty(transactionService.findTransactionByTransactionId(transactionId), PortalErrorCode.INVALID_TRANSACTION_ID);
		TransactionDto dto = transaction.toTransactionDto();
		return dto;
	}
	
	@GET
	@Path("search-transactions")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Search Available Transactions", notes = "Version 1.0, Last Modified Date: 2018-03-20", response = ResponseHeader.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public TransactionCollectionDto searchTransactionsByAndFilters(
			@ApiParam(value = "Transaction Id", required = false) @QueryParam("transactionId") String transactionId,
			@ApiParam(value = "Wallet Id", required = false) @QueryParam("walletId") String walletId,
			@ApiParam(value = "Service order", required = false) @QueryParam("serviceOrder") String serviceOrder,
			@ApiParam(value = "Service Id", required = false) @QueryParam("serviceId") String serviceId,
			@ApiParam(value = "Request Id", required = false) @QueryParam("requestId") String requestId,
			@ApiParam(value = "Item", required = false) @QueryParam("item") String item,
			@ApiParam(value = "ItemType", required = false) @QueryParam("itemType") String itemType,
			@ApiParam(value = "MinContractStartTimeStamp", required = false) @QueryParam("minContractStartTimeStamp") Timestamp minContractStartTimeStamp, @ApiParam(value = "MaxContractStartTimeStamp", required = false) @QueryParam("maxContractStartTimeStamp") Timestamp maxContractStartTimeStamp,
			@ApiParam(value = "MinAmount", required = false) @QueryParam("minAmount") Integer minAmount, @ApiParam(value = "MaxAmount", required = false) @QueryParam("maxAmount") Integer maxAmount,
			@ApiParam(value = "MinBalance", required = false) @QueryParam("minBalance") Integer minBalance, @ApiParam(value = "MaxBalance", required = false) @QueryParam("maxBalance") Integer maxBalance,
			@ApiParam(value = "Action", required = false) @QueryParam("action") String action,
			@ApiParam(value = "Description", required = false) @QueryParam("description") String description,
			@ApiParam(value = "Payment Id", required = false) @QueryParam("paymentId") String paymentId,
			@ApiParam(value = "MinChargeTimeStamp", required = false) @QueryParam("minChargeTimeStamp") Timestamp minChargeTimeStamp, @ApiParam(value = "MaxChargeTimeStamp", required = false) @QueryParam("maxChargeTimeStamp") Timestamp maxChargeTimeStamp,
			@ApiParam(value = "User Id", required = false) @QueryParam("userId") String userId,
			@ApiParam(value = "Organization Id", required = true) @QueryParam("organizationId") String organizationId,
			@ApiParam(value = "Sort", required = false) @QueryParam("sort") @DefaultValue("uid=asc") String sort,
			@ApiParam(value = "Offset", required = false) @QueryParam("offset") @DefaultValue("0") Integer offset,
			@ApiParam(value = "MaxRows", required = false) @QueryParam("maxRows") @DefaultValue("20") Integer maxRows) {
		//get transaction list
		ResultList<Transaction> transactionResultList = transactionService.searchTransactionsByAndFilters(rc, transactionId,
						walletId, serviceOrder, serviceId, requestId, item, itemType, minContractStartTimeStamp, maxContractStartTimeStamp,
						minAmount, maxAmount, minBalance, maxBalance, action,description, paymentId, minChargeTimeStamp, maxChargeTimeStamp,
						userId, organizationId, sort, offset, maxRows);
		
		//create transaction dto
		List<TransactionDto> transactionDtos = transactionResultList.getItems().stream().map(t->t.toTransactionDto()).collect(Collectors.toList());
		TransactionCollectionDto dto = TransactionCollectionDto.create(transactionDtos,
				transactionResultList.getTotalNumOfItems(), offset, maxRows);
		
		return dto;
	}
	
	@GET
	@Path("search-transaction")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Search Available Transactions", notes = "Version 1.0, Last Modified Date: 2018-03-20", response = ResponseHeader.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public TransactionCollectionDto searchTransactionsByOrFilter(
			@ApiParam(value = "Global Filter", required = false) @QueryParam("globalFilter") String globalFilter,
			@ApiParam(value = "Organization Id", required = true) @QueryParam("organizationId") String organizationId,
			@ApiParam(value = "Wallet Id", required = true) @QueryParam("walletId") String walletId,
			@ApiParam(value = "Sort", required = false) @QueryParam("sort") @DefaultValue("uid=asc") String sort,
			@ApiParam(value = "Offset", required = false) @QueryParam("offset") @DefaultValue("0") Integer offset,
			@ApiParam(value = "MaxRows", required = false) @QueryParam("maxRows") @DefaultValue("20") Integer maxRows) {
		//get transaction list
		ResultList<Transaction> transactionResultList = transactionService.searchTransactionsByOrFilter(rc, walletId, globalFilter, organizationId, sort, offset, maxRows);
		
		//create transaction dto
		List<TransactionDto> transactionDtos = transactionResultList.getItems().stream().map(t->t.toTransactionDto()).collect(Collectors.toList());
		TransactionCollectionDto dto = TransactionCollectionDto.create(transactionDtos,
				transactionResultList.getTotalNumOfItems(), offset, maxRows);
		
		return dto;
	}
	
	
	@GET
	@Path("export-transactionsSummaryCsv")
	@ApiOperation(value = "Export Transactions Summary Csv", notes = "Version 1.0, Last Modified Date: 2016-07-28", response = ResponseHeader.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public Response exportTransactionsSummaryByAndFilters(
			@ApiParam(value = "Transaction Id", required = false) @QueryParam("transactionId") String transactionId,
			@ApiParam(value = "Wallet Id", required = false) @QueryParam("walletId") String walletId,
			@ApiParam(value = "Service order", required = false) @QueryParam("serviceOrder") String serviceOrder,
			@ApiParam(value = "Service Id", required = false) @QueryParam("serviceId") String serviceId,
			@ApiParam(value = "Request Id", required = false) @QueryParam("requestId") String requestId,
			@ApiParam(value = "Item", required = false) @QueryParam("item") String item,
			@ApiParam(value = "ItemType", required = false) @QueryParam("itemType") String itemType,
			@ApiParam(value = "MinContractStartTimeStamp", required = false) @QueryParam("minContractStartTimeStamp") Timestamp minContractStartTimeStamp, @ApiParam(value = "MaxContractStartTimeStamp", required = false) @QueryParam("maxContractStartTimeStamp") Timestamp maxContractStartTimeStamp,
			@ApiParam(value = "MinAmount", required = false) @QueryParam("minAmount") Integer minAmount, @ApiParam(value = "MaxAmount", required = false) @QueryParam("maxAmount") Integer maxAmount,
			@ApiParam(value = "MinBalance", required = false) @QueryParam("minBalance") Integer minBalance, @ApiParam(value = "MaxBalance", required = false) @QueryParam("maxBalance") Integer maxBalance,
			@ApiParam(value = "Action", required = false) @QueryParam("action") String action,
			@ApiParam(value = "Description", required = false) @QueryParam("description") String description,
			@ApiParam(value = "Payment Id", required = false) @QueryParam("paymentId") String paymentId,
			@ApiParam(value = "MinChargeTimeStamp", required = false) @QueryParam("minChargeTimeStamp") Timestamp minChargeTimeStamp, @ApiParam(value = "MaxChargeTimeStamp", required = false) @QueryParam("maxChargeTimeStamp") Timestamp maxChargeTimeStamp,
			@ApiParam(value = "User Id", required = false) @QueryParam("userId") String userId,
			@ApiParam(value = "Organization Id", required = true) @QueryParam("organizationId") String organizationId,
			@ApiParam(value = "Sort", required = false) @QueryParam("sort") @DefaultValue("uid=asc") String sort
			) {
		//get transaction list
		ResultList<Transaction> transactionResultList = transactionService.searchTransactionsByAndFilters(rc, transactionId,
						walletId, serviceOrder, serviceId, requestId, item, itemType, minContractStartTimeStamp, maxContractStartTimeStamp,
						minAmount, maxAmount, minBalance, maxBalance, action,description, paymentId, minChargeTimeStamp, maxChargeTimeStamp,
						userId, organizationId, sort, null, null);
		
		logger.debug("result size: {}",transactionResultList.getItems().size());
		return Response.ok(new StreamingOutput() {
			
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				Writer writer = new BufferedWriter(new OutputStreamWriter(output));
				CSVWriter csvWriter = new CSVWriter(writer);
				csvWriter.writeNext(TransactionSummaryCsv.getHeader());
				for (Transaction transaction:transactionResultList.getItems()) {
					csvWriter.writeNext(TransactionSummaryCsv.create(transaction));
				}
				csvWriter.flush();
				csvWriter.close();
			}
		}).header("Content-Disposition", "attachment; filename=\"test.csv\"").build();

	}
	
	@GET
	@Path("export-transactionsBreakdownCsv")
	@ApiOperation(value = "Export Transactions Breakdown Csv", notes = "Version 1.0, Last Modified Date: 2016-07-28", response = ResponseHeader.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
		@ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public Response exportTransactionsBreakdownAndFilters(
			@ApiParam(value = "Transaction Id", required = false) @QueryParam("transactionId") String transactionId,
			@ApiParam(value = "Wallet Id", required = false) @QueryParam("walletId") String walletId,
			@ApiParam(value = "Service order", required = false) @QueryParam("serviceOrder") String serviceOrder,
			@ApiParam(value = "Service Id", required = false) @QueryParam("serviceId") String serviceId,
			@ApiParam(value = "Request Id", required = false) @QueryParam("requestId") String requestId,
			@ApiParam(value = "Item", required = false) @QueryParam("item") String item,
			@ApiParam(value = "ItemType", required = false) @QueryParam("itemType") String itemType,
			@ApiParam(value = "MinContractStartTimeStamp", required = false) @QueryParam("minContractStartTimeStamp") Timestamp minContractStartTimeStamp, @ApiParam(value = "MaxContractStartTimeStamp", required = false) @QueryParam("maxContractStartTimeStamp") Timestamp maxContractStartTimeStamp,
			@ApiParam(value = "MinAmount", required = false) @QueryParam("minAmount") Integer minAmount, @ApiParam(value = "MaxAmount", required = false) @QueryParam("maxAmount") Integer maxAmount,
			@ApiParam(value = "MinBalance", required = false) @QueryParam("minBalance") Integer minBalance, @ApiParam(value = "MaxBalance", required = false) @QueryParam("maxBalance") Integer maxBalance,
			@ApiParam(value = "Action", required = false) @QueryParam("action") String action,
			@ApiParam(value = "Description", required = false) @QueryParam("description") String description,
			@ApiParam(value = "Payment Id", required = false) @QueryParam("paymentId") String paymentId,
			@ApiParam(value = "MinChargeTimeStamp", required = false) @QueryParam("minChargeTimeStamp") Timestamp minChargeTimeStamp, @ApiParam(value = "MaxChargeTimeStamp", required = false) @QueryParam("maxChargeTimeStamp") Timestamp maxChargeTimeStamp,
			@ApiParam(value = "User Id", required = false) @QueryParam("userId") String userId,
			@ApiParam(value = "Organization Id", required = true) @QueryParam("organizationId") String organizationId,
			@ApiParam(value = "Sort", required = false) @QueryParam("sort") @DefaultValue("uid=asc") String sort
			) {
		
		//get transaction list
		ResultList<Transaction> transactionResultList = transactionService.searchTransactionsByAndFilters(rc, transactionId,
				walletId, serviceOrder, serviceId, requestId, item, itemType, minContractStartTimeStamp, maxContractStartTimeStamp,
				minAmount, maxAmount, minBalance, maxBalance, action,description, paymentId, minChargeTimeStamp, maxChargeTimeStamp,
				userId, organizationId, sort, null, null);
		
		logger.debug("result size: {}",transactionResultList.getItems().size());
		return Response.ok(new StreamingOutput() {
			
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				Writer writer = new BufferedWriter(new OutputStreamWriter(output));
				CSVWriter csvWriter = new CSVWriter(writer);
				csvWriter.writeNext(TransactionBreakdownCsv.getHeader());
				for (Transaction transaction:transactionResultList.getItems()) {
					csvWriter.writeAll(TransactionBreakdownCsv.create(transaction));
				}
				csvWriter.flush();
				csvWriter.close();
			}
		}).header("Content-Disposition", "attachment; filename=\"test.csv\"").build();
		
	}
	
	@GET
	@Path("export-transactionSummaryCsv")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Export Transactions Summary Csv", notes = "Version 1.0, Last Modified Date: 2016-07-28", response = ResponseHeader.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public Response exportTransactionsSummaryByOrFilter(
			@ApiParam(value = "Global Filter", required = false) @QueryParam("globalFilter") String globalFilter,
			@ApiParam(value = "Organization Id", required = true) @QueryParam("organizationId") String organizationId,
			@ApiParam(value = "Wallet Id", required = true) @QueryParam("walletId") String walletId,
			@ApiParam(value = "Sort", required = false) @QueryParam("sort") @DefaultValue("uid=asc") String sort
			) {
		
		//get transaction list
		ResultList<Transaction> transactionResultList = transactionService.searchTransactionsByOrFilter(rc, walletId, globalFilter, organizationId, sort, null, null);

		return Response.ok(new StreamingOutput() {

			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				Writer writer = new BufferedWriter(new OutputStreamWriter(output));
				CSVWriter csvWriter = new CSVWriter(writer);
				csvWriter.writeNext(TransactionSummaryCsv.getHeader());
				for (Transaction transaction : transactionResultList.getItems()) {
					csvWriter.writeNext(TransactionSummaryCsv.create(transaction));
				}
				csvWriter.flush();
				csvWriter.close();
			}
		}).header("Content-Disposition", "attachment; filename=\"test.csv\"").build();

	}

	@GET
	@Path("export-transactionBreakdownCsv")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Export Transaction Breakdown Csv", notes = "Version 1.0, Last Modified Date: 2016-07-28", response = ResponseHeader.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
		@ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public Response exportTransactionsBreakdownByOrFilter(
			@ApiParam(value = "Global Filter", required = false) @QueryParam("globalFilter") String globalFilter,
			@ApiParam(value = "Organization Id", required = true) @QueryParam("organizationId") String organizationId,
			@ApiParam(value = "Wallet Id", required = true) @QueryParam("walletId") String walletId,
			@ApiParam(value = "Sort", required = false) @QueryParam("sort") @DefaultValue("uid=asc") String sort
			) {
		
		//get transaction list
		ResultList<Transaction> transactionResultList = transactionService.searchTransactionsByOrFilter(rc, walletId, globalFilter, organizationId, sort, null, null);
		
		return Response.ok(new StreamingOutput() {
			
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				Writer writer = new BufferedWriter(new OutputStreamWriter(output));
				CSVWriter csvWriter = new CSVWriter(writer);
				csvWriter.writeNext(TransactionBreakdownCsv.getHeader());
				for (Transaction transaction : transactionResultList.getItems()) {
					csvWriter.writeAll(TransactionBreakdownCsv.create(transaction));
				}
				csvWriter.flush();
				csvWriter.close();
			}
		}).header("Content-Disposition", "attachment; filename=\"test.csv\"").build();
		
	}

}
