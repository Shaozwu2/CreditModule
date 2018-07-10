package com.kooppi.nttca.portal.common.rest.repository;

import java.util.Base64;
import java.util.Optional;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kooppi.nttca.portal.common.constant.PortalConstant;
import com.kooppi.nttca.portal.common.filter.request.RequestContext;
import com.kooppi.nttca.portal.common.filter.response.dto.EmptyResponseResult;
import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;
import com.kooppi.nttca.portal.common.utils.JAXBUtil;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;

public abstract class BasicRestRepository {

	private static final Logger logger = LoggerFactory.getLogger(BasicRestRepository.class);
	
	protected <T> Optional<T> get(WebTarget target,Class<T> responseType){
		logger.debug("target url = {}",target.getUri().toString());
		Response response =  target.request(MediaType.APPLICATION_JSON_TYPE).get();
		return parseResponse(response, responseType);
	}
	
	protected <T> Optional<T> get(WebTarget target, RequestContext rc, Class<T> responseType){
		logger.debug("target url = {}",target.getUri().toString());
		String authorization = authorizationEncode(rc.getRequestUserId());
		Response response =  target.request(MediaType.APPLICATION_JSON_TYPE)
				.header(PortalConstant.PORTAL_HEADER_AUTHORIZATION, authorization)
				.get();
		return parseResponse(response, responseType);
	}
	
	protected <T> Optional<T> delete(WebTarget target,Class<T> responseType){
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE).delete();
		return parseResponse(response, responseType);
	}
	
	protected <T> Optional<T> delete(WebTarget target, RequestContext rc, Class<T> responseType) {
		String authorization = authorizationEncode(rc.getRequestUserId());
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
				.header(PortalConstant.PORTAL_HEADER_AUTHORIZATION, authorization)
				.delete();
		return parseResponse(response, responseType);
	}
	
	protected <T> Optional<T> post(WebTarget target,Object object,Class<T> responseType){
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(object, MediaType.APPLICATION_JSON_TYPE));
		return parseResponse(response, responseType);
	}
	
	protected <T> Optional<T> post(WebTarget target,Object object, RequestContext rc, Class<T> responseType) {
		String authorization = authorizationEncode(rc.getRequestUserId());
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
				.header(PortalConstant.PORTAL_HEADER_AUTHORIZATION, authorization)
				.post(Entity.entity(object, MediaType.APPLICATION_JSON_TYPE));
		return parseResponse(response, responseType);
	}
	
	protected <T> Optional<T> put(WebTarget target,Object object,Class<T> responseType){
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE).put(Entity.entity(object, MediaType.APPLICATION_JSON_TYPE));
		return parseResponse(response, responseType);
	}
	
	protected <T> Optional<T> put(WebTarget target, RequestContext rc, Object object,Class<T> responseType) {
		String authorization = authorizationEncode(rc.getRequestUserId());
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
				.header(PortalConstant.PORTAL_HEADER_AUTHORIZATION, authorization)
				.put(Entity.entity(object, MediaType.APPLICATION_JSON_TYPE));
		return parseResponse(response, responseType);
	}
	
	private <T> Optional<T> parseResponse(Response response,Class<T> responseType){
		
		if (ResponseResult.class.isAssignableFrom(responseType)) {
			response.bufferEntity();
			try {
				JSONObject header = new JSONObject(response.readEntity(String.class));
				String responseCode = header.getString(PortalConstant.PORTAL_RESPONSE_HEADER_RESPONSE_CODE);
				//if success
				if ("200".equals(responseCode)) {
					
					if (responseType == EmptyResponseResult.class) {
						return Optional.ofNullable(responseType.newInstance());
					}else {
						JSONObject responseContent = header.getJSONObject(PortalConstant.PORTAL_RESPONSE_HEADER_RESPONSE_CONTENT);
						String result = responseContent.get(((ResponseResult)responseType.newInstance()).getResultName()).toString();
						logger.warn("result : {}",result);
						return Optional.ofNullable(JAXBUtil.jsonToObject(result, responseType));
					}
				}else {
					//if has error
					logger.error("ErrorFound on server side ,{}",response.toString());
					PortalErrorCode errorCode = PortalErrorCode.fromResponseCode(responseCode);
					if (errorCode != PortalErrorCode.UNKNOW_ERROR_CODE) {
						PortalExceptionUtils.throwNow(errorCode);
					}else {
						return Optional.empty();
					}
				}
				
			} catch (JAXBException | JSONException | InstantiationException | IllegalAccessException e) {
				logger.error("ErrorFound during parse the result ,{}",e);
				return Optional.empty();
			}
		}
		
		//for response beside New Portal API
		if (response.getStatus() < 199  && response.getStatus() >= 300) {
			logger.error("ErrorFound during Exception,{}",response.toString());
			return Optional.empty();
		}
		return Optional.ofNullable(response.readEntity(responseType));
	}
	
	private String authorizationEncode(String userId) {
		String result = Base64.getEncoder().encodeToString(userId.getBytes());
		return "Basic " + result;
	}
	
}
