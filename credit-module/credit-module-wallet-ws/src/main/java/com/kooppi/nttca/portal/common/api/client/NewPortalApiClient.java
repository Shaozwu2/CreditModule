package com.kooppi.nttca.portal.common.api.client;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.kooppi.nttca.portal.common.config.file.ConfigurationValue;
import com.kooppi.nttca.portal.common.config.file.PropertyResolver;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;
import com.kooppi.nttca.portal.newportal.dto.CustomerItemDto;
import com.kooppi.nttca.portal.newportal.dto.NewPortalBaseResponseDto;

import jersey.repackaged.com.google.common.collect.Lists;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@ApplicationScoped
public class NewPortalApiClient{
	
	private static Retrofit retrofit = null;
	
	@Inject
	@ConfigurationValue(property = "newportal.service.api.url", defaultValue = "http://192.168.112.123:8080/")
	private String baseUrl;
	
	@Inject
	private PropertyResolver propertyResolver;
	
	public NewPortalApiClient(){
	}

	@PostConstruct
	public void getClient() {
		if(baseUrl==null) {
			baseUrl = propertyResolver.getValue("newportal.service.api.url").get();
//			baseUrl = "http://192.168.112.123:8080/";
		}
	    if (retrofit==null) {
	        retrofit = new Retrofit.Builder()
	                .baseUrl(baseUrl)
	                .addConverterFactory(GsonConverterFactory.create())
	                .build();
	    }
	}
	
	public boolean isOrganizationIdExist(String organizationId) {
		PortalExceptionUtils.throwIfNull(organizationId, PortalErrorCode.MISSING_LOGIN_ID);
		SOService soService = retrofit.create(SOService.class);		
		Call<NewPortalBaseResponseDto> responseDto = soService.getCompanyByCdsCompanyId(organizationId);
		
		NewPortalBaseResponseDto newPortalBaseResponseDto = null;
		try {
			newPortalBaseResponseDto = responseDto.execute().body();
		} catch (Exception e) {
			PortalExceptionUtils.throwNow(PortalErrorCode.INTERNAL_SERVER_ERROR);
		}
		return newPortalBaseResponseDto.getResponseContent() == null ? false : true;
	}

	public List<CustomerItemDto> getAllNewPortalCustomer() {
		List<CustomerItemDto> customerItems = Lists.newArrayList();
		SOService soService = retrofit.create(SOService.class);		
		Call<NewPortalBaseResponseDto> allCustomers = soService.getAllCustomers();
		try {
			retrofit2.Response<NewPortalBaseResponseDto> response = allCustomers.execute();
			NewPortalBaseResponseDto responseDto = response.body();;
			customerItems = responseDto.getResponseContent().getCustomerItems();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customerItems;
	}

	public static void main(String[] args) {
		NewPortalApiClient newPortalApiClient = new NewPortalApiClient();
//		boolean organizationIdExist = newPortalApiClient.isOrganizationIdExist("CREDIT_CDS001");
		List<CustomerItemDto> allNewPortalCustomer = newPortalApiClient.getAllNewPortalCustomer();
		System.out.println(allNewPortalCustomer.size());
//		System.out.println("organizationIdExist: " + organizationIdExist);
	}
}
