package com.kooppi.nttca.portal.common.api.client;

import lombok.extern.slf4j.Slf4j;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Slf4j
public class RetrofitClient {

	private Retrofit retrofit = null;
	private String baseUrl;
	 
	public RetrofitClient() {
		getClient();
	}

	public RetrofitClient(String baseUrl) {
		this.baseUrl = baseUrl;
		log.info("API client base path set to: {}", baseUrl);
		getClient();
	}
	
	public Retrofit getClient() {
	    if (retrofit==null) {
	        retrofit = new Retrofit.Builder()
	                .baseUrl(baseUrl)
	                .addConverterFactory(GsonConverterFactory.create())
	                .build();
	    }
	    return retrofit;
	}
	
}
