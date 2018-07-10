package com.kooppi.nttca.portal.common.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;

public class StringUtils {
	private static Pattern FILE_EXTENSION_PATTERN = Pattern.compile("([^\\s]+(\\.(?i)(doc|pdf))$)");
	
    public static boolean validateFileExtn(String userName){
        Matcher mtch = FILE_EXTENSION_PATTERN.matcher(userName);
        if(mtch.matches()){
            return true;
        }
        return false;
    }
    
	public static String enclosePercentWildcard(String str) {
		str = MoreObjects.firstNonNull(str, "");
		return "%" + str + "%";
	}
	
	public static Map<String, String> parseQueryParam(String sort) {
		Map<String, String> stringMap = Maps.newHashMap();
		String[] sortStr = sort.split("=", 2);
		stringMap.put("orderBy", sortStr[0]);
		stringMap.put("orderSorting", sortStr[1]);
		return stringMap;
	}
	
}
