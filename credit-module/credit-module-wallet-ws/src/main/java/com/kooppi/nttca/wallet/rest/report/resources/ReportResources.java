package com.kooppi.nttca.wallet.rest.report.resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kooppi.nttca.portal.common.config.file.PropertyResolver;
import com.kooppi.nttca.portal.common.filter.response.dto.ResponseHeader;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;
import com.kooppi.nttca.portal.wallet.dto.report.ReportYearMonthDto;
import com.kooppi.nttca.wallet.common.persistence.domain.Report;
import com.kooppi.nttca.wallet.common.swagger.ui.model.BadResponseModel;
import com.kooppi.nttca.wallet.common.swagger.ui.model.GoodResponseModel;
import com.kooppi.nttca.wallet.rest.report.dto.ReportCsv;
import com.kooppi.nttca.wallet.rest.report.service.ReportService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Stateless
@Path("reports")
public class ReportResources {
	
	private static final Logger logger = LoggerFactory.getLogger(ReportResources.class);
	
	@Inject
	private ReportService reportService;
	
	@Inject
	private PropertyResolver propertyResolver;
	
	@GET
	@Path("generate-report-by-year-and-month")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Generate report", notes = "Version 1.0, Last Modified Date: 2018-05-23", response = ResponseHeader.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public Response generateReportByYearAndMonth(@QueryParam("year")  Integer year, @QueryParam("month")  Integer month) {
		logger.debug("start to generate report...");
		
		// Validate parameters
		PortalExceptionUtils.throwIfTrue(year == null, PortalErrorCode.MISS_PARAM_YEAR);
		PortalExceptionUtils.throwIfTrue(month == null, PortalErrorCode.MISS_PARAM_MONTH);
		
		// Call stored procedure to get report object[] list
		List<Object[]> reportDataList = reportService.getReportDataList(year, month);
		logger.debug("generate report list size = " + reportDataList.size());
		
		// Generate report
		return Response.ok(new StreamingOutput() {
			
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				// Use Apache POI to generate Excel Report
				SXSSFWorkbook workBook = new SXSSFWorkbook();
				Sheet sheet = workBook.createSheet("Generated Report");
				
				// Add Report Header and set 'Bold' font
				CellStyle style = workBook.createCellStyle();
				Font font = workBook.createFont();
				font.setBold(true);
				style.setFont(font);
				
				Row headerRow = sheet.createRow(0);
				String header[] = ReportCsv.getHeader();
				for (int i = 0; i < header.length; i++) {
					headerRow.createCell(i).setCellValue(header[i]);
					headerRow.getCell(i).setCellStyle(style);
				}
				
				// Add Report Title and set 'Bold' 'Green' font
				CellStyle style2 = workBook.createCellStyle();
				Font font2 = workBook.createFont();
				font2.setBold(true);
				font2.setColor(IndexedColors.GREEN.getIndex());
				style2.setFont(font2);
				
				Row titleRow = sheet.createRow(1);
				String title[] = ReportCsv.getTitle(year, month);
				for (int i = 0; i < title.length; i++) {
					titleRow.createCell(i).setCellValue(title[i]);
					titleRow.getCell(i).setCellStyle(style2);
				}
				
				// Add Report Data
		        int rowNum = 2;
		        for (Object[] reportData : reportDataList) {
		        		// Handle data to match report format
		        		Row currentRow = sheet.createRow(rowNum);
		        		String str[] = ReportCsv.create(reportData);
		        		
		        		// Handle forfeit / cancel forfeit cases
		        		if ("forfeit".equalsIgnoreCase(str[6])) {
		        			str[11] = propertyResolver.getValue("forfeit.gl.code").get();
		        			str[12] = propertyResolver.getValue("forfeit.charge.type").get();
		        			str[13] = propertyResolver.getValue("forfeit.provider").get();
		        			str[14] = propertyResolver.getValue("forfeit.provider.rate").get();
		        		} else if ("cancel forfeit".equalsIgnoreCase(str[6])) {
		        			str[11] = propertyResolver.getValue("cancel.forfeit.gl.code").get();
		        			str[12] = propertyResolver.getValue("cancel.forfeit.charge.type").get();
		        			str[13] = propertyResolver.getValue("cancel.forfeit.provider").get();
		        			str[14] = propertyResolver.getValue("cancel.forfeit.provider.rate").get();
		        		}
		        		
		        		// Create Excel cell
					for (int i = 0; i < str.length; i++) {
						currentRow.createCell(i).setCellValue(str[i]);
					}
					rowNum++;
				}
		        workBook.write(output);
		        workBook.close();
			}
		}).header("Content-Disposition", "attachment; filename=\"test.xlsx\"").build();
	}
	
	@GET
	@Path("generate-report-and-save-to-db")
	public void generateReportAndSaveToDB() {
		logger.debug("generate and save to db, start...");
		
		LocalDate today = LocalDate.now();
		Integer year = today.getYear();
		Integer month = today.getMonthValue();
		
		Response r = generateReportByYearAndMonth(year, month);
		StreamingOutput stream = (StreamingOutput) r.getEntity();
		ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
		try {
			stream.write(byteArrayStream);
		} catch (Exception e) {
			logger.debug("StreamingOutput write to ByteArrayOutputStream error, msg = " + e.getMessage());
		}
		byte[] reportFile = byteArrayStream.toByteArray();
		
		Report report = new Report();
		report.setReportDate(new Date());
		report.setReportFile(reportFile);
		
		reportService.createOrUpdateReport(report);
	}
	
	@GET
	@Path("download-report-by-year-and-month")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Download report", notes = "Version 1.0, Last Modified Date: 2018-05-23", response = ResponseHeader.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public Response downloadReportByYearAndMonth(@QueryParam("year")  Integer year, @QueryParam("month")  Integer month) {
		logger.debug("start to download report...");
		
		// Validate parameters
		PortalExceptionUtils.throwIfTrue(year == null, PortalErrorCode.MISS_PARAM_YEAR);
		PortalExceptionUtils.throwIfTrue(month == null, PortalErrorCode.MISS_PARAM_MONTH);
		
		// Get back data from DB
		byte[] reportFile = reportService.getReportDataFromDB(year, month);
		
		// Generate report
		return Response.ok(new StreamingOutput() {
			
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				output.write(reportFile);
				output.close();
			}
		}).header("Content-Disposition", "attachment; filename=\"test.xlsx\"").build();
	}
	
	@GET
	@Path("get-all-report-year-month")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get all reports' YearMonth info", notes = "Version 1.0, Last Modified Date: 2018-05-24", response = ResponseHeader.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = GoodResponseModel.class), @ApiResponse(code = 400, message = "Bad Request", response = BadResponseModel.class), })
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "X-Request-ID", value = "requestId", required = true, dataType = "string", paramType = "header"),
	    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "string", paramType = "header")})
	public ReportYearMonthDto getAllReportYearMonth() {
		logger.debug("start to get all reports' YearMonth...");
		
		List<YearMonth> resultList = reportService.getAllReportYearMonth();
		logger.debug("YearMonth list = " + resultList.toString());
		
		ReportYearMonthDto dto = new ReportYearMonthDto();
		dto.setYearMonth(resultList);
		return dto;
	}

}
