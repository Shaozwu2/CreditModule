package com.kooppi.nttca.portal.common.api.client;

import com.kooppi.nttca.portal.newportal.dto.NewPortalBaseResponseDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SOService {
	@GET("/ProfileManagementWS/rest/v1.0/customer")
	Call<NewPortalBaseResponseDto> getAllCustomers();

	@GET("/ProfileManagementWS/rest/v1.0/company")
	Call<NewPortalBaseResponseDto> getCompanyByCdsCompanyId(@Query("cdsCompanyId") String organizationId);
}
