package com.kooppi.nttca.portal.common.utils;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class NetworkUtils {

	private static List<String> PROXY_IP_ADDRESS_HEADER = Lists.newArrayList("X-Forwarded-For","Proxy-Client-IP","WL-Proxy-Client-IP","HTTP_CLIENT_IP","HTTP_X_FORWARDED_FOR");
   
	public static String getIpAddr(HttpServletRequest request) {
		String ip = "";
		for (String header : PROXY_IP_ADDRESS_HEADER) {
			ip = request.getHeader(header);
			if (!Strings.isNullOrEmpty(ip) && !"unknown".equals(ip)) {
				break;
			}
		}
		
		if (Strings.isNullOrEmpty(ip) || "unknown".equals(ip)) {
			 ip = request.getRemoteAddr();
		}
		return ip;
    }
}
