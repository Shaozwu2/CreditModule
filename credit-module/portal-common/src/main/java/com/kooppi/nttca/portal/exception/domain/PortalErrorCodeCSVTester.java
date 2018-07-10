package com.kooppi.nttca.portal.exception.domain;

import java.util.Map;

import com.google.common.collect.Maps;

public class PortalErrorCodeCSVTester {

	public static void main(String[] args){
		
		Map<String, Integer> codeCountMap = Maps.newHashMap();
		
		System.out.println("\"" + "code" + "\"," + "\"" + "responseCode" + "\"," + "\"" + "responseMsg" + "\"");
		for (PortalErrorCode code : PortalErrorCode.values()) {
			Integer count = codeCountMap.get(code.getResponseCode());
			if (count == null) {
				count = 1;
			}else {
				count +=1;
			}
			
			codeCountMap.put(code.getResponseCode(), count);
			
			System.out.println("\"" + code + "\"," + "\"" + code.getResponseCode() + "\"," + "\"" + code.getResponseMsg() + "\"," + "\"" + count + "\"");
		}
	}
	
}
