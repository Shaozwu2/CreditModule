package com.kooppi.nttca.portal.common.utils;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.kooppi.nttca.portal.common.constant.PortalConstant;

public class PortalXmlDateAdapter extends XmlAdapter<String, LocalDate>{

	@Override
	public LocalDate unmarshal(String v) throws Exception {
		return LocalDate.from(PortalConstant.PORTAL_DATE_FORMATER.parse(v));
	}

	@Override
	public String marshal(LocalDate v) throws Exception {
		return PortalConstant.PORTAL_DATE_FORMATER.format(v);
	}
	
	public static void main(String args[]){
		System.out.println("" + LocalDate.from(PortalConstant.PORTAL_DATE_FORMATER.parse("2016-07-22")));
	}
	
}
