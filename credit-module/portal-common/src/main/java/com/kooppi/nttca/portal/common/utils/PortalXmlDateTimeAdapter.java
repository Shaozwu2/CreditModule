package com.kooppi.nttca.portal.common.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.kooppi.nttca.portal.common.constant.PortalConstant;

public class PortalXmlDateTimeAdapter extends XmlAdapter<String, LocalDateTime>{

	@Override
	public LocalDateTime unmarshal(String v) throws Exception {
		return ZonedDateTime.from(PortalConstant.PORTAL_DATE_TIME_FORMATER.parse(v)).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
	}

	@Override
	public String marshal(LocalDateTime v) throws Exception {
		return PortalConstant.PORTAL_DATE_TIME_FORMATER.format(ZonedDateTime.of(v, ZoneId.systemDefault()));
	}
	
	public static void main(String[] args) {
		System.out.println(PortalConstant.PORTAL_DATE_TIME_FORMATER.format(ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault())));
	}
}
