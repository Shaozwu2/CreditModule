package com.kooppi.nttca.wallet.rest.tnc.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.common.api.client.NewPortalApiClient;
import com.kooppi.nttca.portal.common.auditlog.AuditingActionType;
import com.kooppi.nttca.portal.common.auditlog.Logged;
import com.kooppi.nttca.portal.common.filter.response.dto.EmptyResponseResult;
import com.kooppi.nttca.portal.common.filter.response.dto.ResponseHeader;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;
import com.kooppi.nttca.portal.wallet.dto.termsAndConditions.CreateTnCDto;
import com.kooppi.nttca.portal.wallet.dto.termsAndConditions.CreateTnCToPriceBookDto;
import com.kooppi.nttca.portal.wallet.dto.termsAndConditions.GetTnCsRequestCollectionDto;
import com.kooppi.nttca.portal.wallet.dto.termsAndConditions.GetTnCsRequestDto;
import com.kooppi.nttca.portal.wallet.dto.termsAndConditions.GetTnCsResponseCollectionDto;
import com.kooppi.nttca.portal.wallet.dto.termsAndConditions.TnCCollectionDto;
import com.kooppi.nttca.portal.wallet.dto.termsAndConditions.TnCDto;
import com.kooppi.nttca.portal.wallet.dto.termsAndConditions.TnCToPricebookCollectionDto;
import com.kooppi.nttca.portal.wallet.dto.termsAndConditions.TnCToPricebookDto;
import com.kooppi.nttca.portal.wallet.dto.termsAndConditions.UpdateTnCDto;
import com.kooppi.nttca.portal.wallet.dto.termsAndConditions.UpdateTncToPricebookDto;
import com.kooppi.nttca.wallet.common.persistence.domain.PriceBook;
import com.kooppi.nttca.wallet.common.persistence.domain.TnC;
import com.kooppi.nttca.wallet.common.persistence.domain.TnCToPricebook;
import com.kooppi.nttca.wallet.common.swagger.ui.model.BadResponseModel;
import com.kooppi.nttca.wallet.common.swagger.ui.model.GoodResponseModel;
import com.kooppi.nttca.wallet.rest.priceBook.service.PriceBookService;
import com.kooppi.nttca.wallet.rest.tnc.service.TnCTemplateService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Stateless
@Api(value = "TnC(Version 1)")
@Path("tNcs")
public class TnCResources {
	
	private Logger logger = LoggerFactory.getLogger(TnCResources.class);

	@Inject
	TnCTemplateService tncService;

	@Inject
	PriceBookService pbService;
	
	@Inject
	private NewPortalApiClient npClient;

	@GET
	@Path("search-tncs")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Search T&C", notes = "Version 1.0, Last Modified Date: 2018-05-28", response = ResponseHeader.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class),
			@ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
			@ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header") })
	public TnCCollectionDto searchTnCsByOrFilter(@QueryParam("globalFilter") String globalFilter,
			@QueryParam("sort") @DefaultValue("uid=asc") String sort,
			@QueryParam("offset") @DefaultValue("0") Integer offset,
			@QueryParam("maxRows") @DefaultValue("20") Integer maxRows) {
		
		ResultList<TnC> tncResultList = tncService.searchTnCsByOrFilter(globalFilter, sort,
				offset, maxRows);

		List<TnCDto> tncDtos = tncResultList.getItems().stream().map(tc -> tc.toTnCDto()).collect(Collectors.toList());
		TnCCollectionDto dto = TnCCollectionDto.create(tncDtos, tncResultList.getTotalNumOfItems(), offset, maxRows);
		return dto;
	}
	
	@POST
	@Path("create-tnc")
	@Transactional
	@Logged(actionType = AuditingActionType.CREATE_TNC)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create T&C", notes = "Version 1.0, Last Modified Date: 2018-05-28", response = ResponseHeader.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class),
			@ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class)})
	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
			@ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header") })
	public TnCDto CreateTermsAndConditions(@ApiParam(value = "CreateTnCDto Dto", required = true) CreateTnCDto dto) {
		PortalExceptionUtils.throwIfNull(dto, PortalErrorCode.INVALID_PAYLOAD_DATA);
		dto.validateCreateTnC();
		
		TnC tnc = tncService.createTnc(dto.getName(), dto.getDescription(), dto.getBuName(), dto.getIsDefault(),
				dto.getIsVisible(), dto.getFileName(), dto.getSize(), dto.getAttachmentData());
		TnCDto tnCDto = tnc.toTnCDto();
		
		return tnCDto;
	}
	
	@PUT
	@Transactional
	@Path("update-tnc/{tncId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Logged(actionType = AuditingActionType.UPDATE_TNC)
	@ApiOperation(value = "Update T&C", notes = "Version 1.0, Last Modified Date: 2018-05-28", response = ResponseHeader.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class),
			@ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class)})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
		@ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header") })
	public TnCDto UpdateTermsAndConditions(@ApiParam(value = "UpdateTnCDto Dto", required = true) UpdateTnCDto dto,
			@ApiParam(value = "tncId Id", required = true) @PathParam("tncId") Long tncId) {
		
		PortalExceptionUtils.throwIfNull(dto, PortalErrorCode.INVALID_PAYLOAD_DATA);
		PortalExceptionUtils.throwIfNull(tncId, PortalErrorCode.INVALID_PAYLOAD_DATA);
		dto.validateUpdateTnC();
		
		TnC tnc = PortalExceptionUtils.throwIfEmpty(tncService.findByUid(tncId), PortalErrorCode.UNABLE_TO_FIND_TNC_RECORD);
		
		// modified on July 12th 2018
		String finalFileName = null;
		if (dto.getAttachmentData() != null) {
			// means new document uploaded, createFile and return finalFileName(table column = file_name)
			finalFileName = tncService.createFile(dto.getFileName(), dto.getAttachmentData());
		}
		logger.debug("finalFileName = {}", finalFileName);
		
		// update other fields
		tnc.update(dto.getName(), dto.getDescription(), dto.getBuName(), dto.getIsDefault(), dto.getIsVisible(),
				dto.getAttachmentData() != null, dto.getFileName(), finalFileName);
		
		TnCDto tnCDto = tnc.toTnCDto();
		return tnCDto;
	}
	
	@GET
	@Path("download-tnc/{tncId}")
	@Logged(actionType = AuditingActionType.DOWNLOAD_TNC)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Download tnc", notes = "Version 1.0, Last Modified Date: 2018-06-11", response = ResponseHeader.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public Response downloadTnCByTnCId(@ApiParam(value = "tncId Id", required = true) @PathParam("tncId") Long tncId) {
		PortalExceptionUtils.throwIfNull(tncId, PortalErrorCode.INVALID_PAYLOAD_DATA);
		
		TnC tnc = PortalExceptionUtils.throwIfEmpty(tncService.findByUid(tncId), PortalErrorCode.UNABLE_TO_FIND_TNC_RECORD);
		String uploadedName = tnc.getUploadedName();
		byte[] tnCFile = tncService.getTnCFile(tnc);
		return Response.ok(new StreamingOutput() {
			
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				output.write(tnCFile);
				output.close();
			}
		}).header("Content-Disposition", "attachment; filename=\""+ uploadedName +"\"").build();
	}

	@GET
	@Path("download-tnc")
	@Logged(actionType = AuditingActionType.DOWNLOAD_TNC)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Download tnc", notes = "Version 1.0, Last Modified Date: 2018-06-11", response = ResponseHeader.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
			@ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header") })
	public Response downloadTnCWithAuth(
			@ApiParam(value = "CustomerId", required = true) @QueryParam("customerId") String customerId,
			@ApiParam(value = "mapUid", required = true) @QueryParam("mapUid") Long mapUid,
			@ApiParam(value = "PartNo", required = true) @QueryParam("partNo") String partNo) {
		PortalExceptionUtils.throwIfNull(mapUid, PortalErrorCode.MISS_PARAM_TNC_TO_PRODUCT_UID);
		PortalExceptionUtils.throwIfNull(partNo, PortalErrorCode.MISS_PARAM_PART_NO);
		PortalExceptionUtils.throwIfNull(customerId, PortalErrorCode.MISS_PARAM_ORGNIAZATION_ID);
		
		TnCToPricebook tncToPricebook = PortalExceptionUtils.throwIfEmpty(tncService.findTncToPricebookByUid(mapUid), PortalErrorCode.INVALID_TNC_TO_PRODUCT_UID);
		
		PortalExceptionUtils.throwIfFalse(tncToPricebook.getOrganizationId() == customerId, PortalErrorCode.INVALID_ORGANIZATION_USER);
		
		PriceBook pb = PortalExceptionUtils.throwIfEmpty(pbService.findByPartNumber(partNo), PortalErrorCode.INVALID_PART_NO);
		
		TnCToPricebook tNcToPricebook = PortalExceptionUtils.throwIfEmpty(tncService.searchTnCToPricebooksByCustomerIdAndPartNo(pb.getBuName(), partNo, customerId), PortalErrorCode.UNABLE_TO_FIND_TNC_RECORD);
		
		TnC tnc = PortalExceptionUtils.throwIfEmpty(tncService.findByUid(Long.valueOf(tNcToPricebook.getTncId())), PortalErrorCode.UNABLE_TO_FIND_TNC_RECORD);
		String uploadedName = tnc.getUploadedName();
		byte[] tnCFile = tncService.getTnCFile(tnc);
		return Response.ok(new StreamingOutput() {

			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				output.write(tnCFile);
				output.close();
			}
		}).header("Content-Disposition", "attachment; filename=\"" + uploadedName + "\"").build();
	}

	@GET
	@Path("download-tnc-to-product-report")
	@Logged(actionType = AuditingActionType.DOWNLOAD_TNC_REPORT)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Download tnc report", notes = "Version 1.0, Last Modified Date: 2018-06-11", response = ResponseHeader.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
		@ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header") })
	public Response downloadTnCMappingReport(
			@ApiParam(value = "buFilter", required = true) @QueryParam("buFilter") String buFilter,
			@ApiParam(value = "partNoFilter", required = true) @QueryParam("partNoFilter") String partNoFilter,
			@ApiParam(value = "productFilter", required = true) @QueryParam("productFilter") String productFilter,
			@ApiParam(value = "customerFilter", required = true) @QueryParam("customerFilter") String customerFilter,
			@ApiParam(value = "documentFilter", required = true) @QueryParam("documentFilter") String documentFilter,
			@QueryParam("sort") String sort,
			@QueryParam("offset") @DefaultValue("0") Integer offset,
			@QueryParam("maxRows") @DefaultValue("20") Integer maxRows) {

		
		return Response.ok(new StreamingOutput() {
			
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				List<String> headerLists = Lists.newArrayList("Part NO.", "Product"); 
				List<String> partNoToTnCLists = Lists.newArrayList(); 
//				List<TnCReportData> dataLists = Lists.newArrayList(); 
				//customer filter
				List<String> customerLists = Lists.newArrayList();
				if (Strings.isNullOrEmpty(customerFilter)) {
					customerLists = npClient.getAllNewPortalCustomer().stream().map(c -> c.getCustomerId()).collect(Collectors.toList());
					headerLists.addAll(customerLists);
				} else {
					PortalExceptionUtils.throwIfFalse(npClient.isOrganizationIdExist(customerFilter), PortalErrorCode.INVALID_CDS_COMPANY_ID);
					customerLists.add(customerFilter);
					headerLists.add(customerFilter);
				}
				
				// Use Apache POI to generate Excel Report
				SXSSFWorkbook workBook = new SXSSFWorkbook();
				Sheet sheet = workBook.createSheet("TnC Report");
				
				// Add Report Header and set 'Bold' font
				CellStyle style = workBook.createCellStyle();
				Font font = workBook.createFont();
				font.setBold(true);
				style.setFont(font);
				
				Row titleRow = sheet.createRow(0);
				String title[] = {"Customer, Product ad T&C Document Mapping"};
				for (int i = 0; i < title.length; i++) {
					titleRow.createCell(i).setCellValue(title[i]);
					titleRow.getCell(i).setCellStyle(style);
				}
				
				Row headerRow = sheet.createRow(2);
				for (int i = 0; i < headerLists.size(); i++) {
					headerRow.createCell(i).setCellValue(headerLists.get(i));
					headerRow.getCell(i).setCellStyle(style);
				}
				
				// Add Report Data
		        int rowNum = 3;
				
				//pricebook filter
				List<PriceBook> priceBooks = null;
				if (Strings.isNullOrEmpty(partNoFilter) && Strings.isNullOrEmpty(productFilter)) {
					priceBooks = pbService.getAllPriceBooks();
				} else {
					priceBooks = pbService.likeSearchPriceBooksByPartNumberAndProductName(partNoFilter, productFilter);
				}
				System.out.println(priceBooks.size());
				for (PriceBook priceBook : priceBooks) {
					partNoToTnCLists.add(priceBook.getPartNo());
					partNoToTnCLists.add(priceBook.getProductName());
					List<String> searchTnCNameByPartNoAndCustomerList = null;
					if (Strings.isNullOrEmpty(buFilter)) {
						searchTnCNameByPartNoAndCustomerList = tncService.searchTnCNameByPartNoAndCustomerList(priceBook.getBuName(), priceBook.getPartNo(), customerLists);
						partNoToTnCLists.addAll(searchTnCNameByPartNoAndCustomerList);
						Row currentRow = sheet.createRow(rowNum);
						// Create Excel cell
						for (int i = 0; i < customerLists.size() + 2; i++) {
							currentRow.createCell(i).setCellValue(partNoToTnCLists.get(i));
						}
						rowNum++;
					} else if(!Strings.isNullOrEmpty(buFilter) && priceBook.getBuName().equals(buFilter)){
						searchTnCNameByPartNoAndCustomerList = tncService.searchTnCNameByPartNoAndCustomerList(buFilter, priceBook.getPartNo(), customerLists);
						partNoToTnCLists.addAll(searchTnCNameByPartNoAndCustomerList);
						Row currentRow = sheet.createRow(rowNum);
						// Create Excel cell
						for (int i = 0; i < customerLists.size() + 2; i++) {
							currentRow.createCell(i).setCellValue(partNoToTnCLists.get(i));
						}
						rowNum++;
					}
					partNoToTnCLists.clear();
				}
		        workBook.write(output);
		        workBook.close();
			}
		}).header("Content-Disposition", "attachment; filename=\"" + "TnCReport" + "\"").build();
	}

	@POST
	@Path("get-tncs-by-customer-and-partno")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "get tncs", notes = "Version 1.0, Last Modified Date: 2018-06-22", response = ResponseHeader.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
		@ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header") })
	public GetTnCsResponseCollectionDto getTnCsByCustomerIdAndPartNo(GetTnCsRequestCollectionDto collectuonDto) {
		GetTnCsResponseCollectionDto tncList = new GetTnCsResponseCollectionDto();
		List<TnCDto> tncs = tncList.getTncs();
		
		List<GetTnCsRequestDto> requestParams = collectuonDto.getRequestParams();
		for (GetTnCsRequestDto req : requestParams) {
			PortalExceptionUtils.throwIfNull(req.getPartNo(), PortalErrorCode.MISS_PARAM_PART_NO);
			PortalExceptionUtils.throwIfNull(req.getCustomerId(), PortalErrorCode.MISS_PARAM_ORGNIAZATION_ID);
			PriceBook pb = PortalExceptionUtils.throwIfEmpty(pbService.findByPartNumber(req.getPartNo()), PortalErrorCode.INVALID_PART_NO);
			
			TnCToPricebook tNcToPricebook = PortalExceptionUtils.throwIfEmpty(tncService.searchTnCToPricebooksByCustomerIdAndPartNo(pb.getBuName(), req.getPartNo(), req.getCustomerId()), PortalErrorCode.UNABLE_TO_FIND_TNC_RECORD);
			
			TnC tnc = PortalExceptionUtils.throwIfEmpty(tncService.findByUid(Long.valueOf(tNcToPricebook.getTncId())), PortalErrorCode.UNABLE_TO_FIND_TNC_RECORD);
			TnCDto tnCDto = tnc.toTnCDto();
			tncs.add(tnCDto);
		}
		return tncList;
	}

	@DELETE
	@Path("delete-tnc/{tncId}")
	@Transactional
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Logged(actionType = AuditingActionType.DELETE_TNC)
	@ApiOperation(value = "Delete TnC", notes = "Version 1.0, Last Modified Date: 2018-05-28", response = GoodResponseModel.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public EmptyResponseResult deleteTnC(@ApiParam(value = "tncId Id", required = true) @PathParam("tncId") Long tncId) {
		//validate parameter
		PortalExceptionUtils.throwIfNull(tncId, PortalErrorCode.MISS_PARAM_TNC_ID);
		
		TnC tnc = PortalExceptionUtils.throwIfEmpty(tncService.findByUid(tncId), PortalErrorCode.UNABLE_TO_FIND_TNC_RECORD);
		tncService.deleteTnC(tnc);
		
		return EmptyResponseResult.create();
	}
	
	@POST
	@Transactional
	@Path("create-tnc-to-pricebook")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Logged(actionType = AuditingActionType.CREATE_TNC_TO_PRODUCT)
	@ApiOperation(value = "Create T&C to pricebook mapping", notes = "Version 1.0, Last Modified Date: 2018-06-11", response = ResponseHeader.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class),
			@ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class)})
	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
			@ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header") })
	public TnCToPricebookDto CreateTnCToPriceBook(@ApiParam(value = "CreateTnCToPriceBook Dto", required = true) CreateTnCToPriceBookDto dto) {
		PortalExceptionUtils.throwIfNull(dto, PortalErrorCode.INVALID_PAYLOAD_DATA);
		dto.validateCreateTnCToPriceBook();
		
		TnCToPricebook tnCToPricebook = tncService.createTnCToPricebook(dto.getTncId(), dto.getBuName(), dto.getPartNo(), dto.getOrganizationId(),
				dto.getIsDefaultBu(), dto.getIsAllCustomer(), dto.getIsAllBu(), dto.getIsAllProduct());
		TnCToPricebookDto tnCToPricebookDto = tnCToPricebook.toTnCToPricebookDto();
		
		return tnCToPricebookDto;
	}
	
	/* 
	 * 5 filters:
	 * buFilter			--> ttp.buName
	 * partNoFilter		--> ttp.pb.partNo
	 * productFilter		--> ttp.pb.productName
	 * customerFilter	--> ttp.customer
	 * documentFilter	--> tnc.templateName
	 */
	@GET
	@Path("search-tnc-to-pricebooks")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Search T&C to pricebook mappings", notes = "Version 1.0, Last Modified Date: 2018-06-13", response = ResponseHeader.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class),
			@ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
			@ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header") })
	public TnCToPricebookCollectionDto searchTnCToPricebooksByAndFilter(
			@QueryParam("buFilter") String buFilter,
			@QueryParam("partNoFilter") String partNoFilter,
			@QueryParam("productFilter") String productFilter,
			@QueryParam("customerFilter") String customerFilter,
			@QueryParam("documentFilter") String documentFilter,
			@QueryParam("sort") String sort,
			@QueryParam("offset") @DefaultValue("0") Integer offset,
			@QueryParam("maxRows") @DefaultValue("20") Integer maxRows) {
		
		ResultList<TnCToPricebook> tncToPricebookResultList =
				tncService.searchTnCToPricebooksByAndFilter(buFilter, partNoFilter, productFilter, customerFilter, documentFilter, sort, offset, maxRows);
		logger.debug("search tncToPricebook list result size = " + tncToPricebookResultList.getTotalNumOfItems());

		List<TnCToPricebookDto> tnCToPricebookDtos = tncToPricebookResultList.getItems().stream().map(tc -> tc.toTnCToPricebookDto()).collect(Collectors.toList());
		logger.debug("tnCToPricebookDtos size = " + tnCToPricebookDtos.size());
		
		TnCToPricebookCollectionDto dto = TnCToPricebookCollectionDto.create(tnCToPricebookDtos, tncToPricebookResultList.getTotalNumOfItems(), offset, maxRows);
		return dto;
	}
	
	@PUT
	@Transactional
	@Path("update-tnc-to-pricebook/{uid}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Logged(actionType = AuditingActionType.UPDATE_TNC_TO_PRODUCT)
	@ApiOperation(value = "Update T&C to pricebook mapping", notes = "Version 1.0, Last Modified Date: 2018-06-13", response = ResponseHeader.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class),
			@ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class)})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
		@ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header") })
	public TnCToPricebookDto UpdateTncToPriceBook(@ApiParam(value = "UpdateTnCToPricebook Dto", required = true) UpdateTncToPricebookDto dto,
			@ApiParam(value = "tncToPricebook uid", required = true) @PathParam("uid") Long uid) {
		
		logger.debug("uid = " + uid);
		
		PortalExceptionUtils.throwIfNull(dto, PortalErrorCode.INVALID_PAYLOAD_DATA);
		PortalExceptionUtils.throwIfNull(uid, PortalErrorCode.INVALID_PAYLOAD_DATA);
		dto.validateUpdateTnCToPricebook();
		
		TnCToPricebook tnCToPricebook = PortalExceptionUtils.throwIfEmpty(tncService.findTncToPricebookByUid(uid), PortalErrorCode.UNABLE_TO_FIND_TNC_TO_PRICEBOOK_RECORD);
		
		tnCToPricebook.update(dto.getTncId(), dto.getBuName(), dto.getPartNo(), dto.getOrganizationId(), dto.getIsDefaultBu(), dto.getIsAllCustomer(), dto.getIsAllBu(), dto.getIsAllProduct());
		TnCToPricebookDto tnCToPricebookDto = tnCToPricebook.toTnCToPricebookDto();
		
		return tnCToPricebookDto;
	}
	
	@Path("delete-tnc-to-pricebook/{uid}")
	@Transactional
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Logged(actionType = AuditingActionType.DELETE_TNC_TO_PRODUCT)
	@ApiOperation(value = "Delete TnC to pricebook mapping", notes = "Version 1.0, Last Modified Date: 2018-06-14", response = GoodResponseModel.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public EmptyResponseResult deleteTnCToPricebook(@ApiParam(value = "tnCToPricebook uid", required = true) @PathParam("uid") Long uid) {
		//validate parameter
		PortalExceptionUtils.throwIfNull(uid, PortalErrorCode.MISS_PARAM_TNC_ID);
		
		TnCToPricebook tnCToPricebook = PortalExceptionUtils.throwIfEmpty(tncService.findTncToPricebookByUid(uid), PortalErrorCode.UNABLE_TO_FIND_TNC_TO_PRICEBOOK_RECORD);
		tncService.deleteTnCToPricebook(tnCToPricebook);
		
		return EmptyResponseResult.create();
	}
	
	@GET
	@Path("find-default-tnc")
	@Produces(MediaType.APPLICATION_JSON)
	public TnCDto findDefaultTnC() {
		TnC tnc = tncService.findDefaultTnC();
		if (tnc != null)
			return tnc.toTnCDto();
		else
			return new TnCDto();
	}
	
	@GET
	@Path("swap-tnc-mapping-priority/{uid1}/{uid2}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public EmptyResponseResult swapTnCMappingPriority(@PathParam("uid1") Long uid1, @PathParam("uid2") Long uid2) {
		tncService.swapTnCMappingPriority(uid1, uid2);
		return EmptyResponseResult.create();
	}
	
}
