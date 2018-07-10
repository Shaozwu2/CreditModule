package com.kooppi.nttca.portal.common.utils;

import java.time.LocalDate;

import javax.ws.rs.ext.ParamConverter;

import com.kooppi.nttca.portal.common.constant.PortalConstant;

public class LocalDateParameterConverter implements ParamConverter<LocalDate> {

	@Override
	public LocalDate fromString(String value) {
		return LocalDate.from(PortalConstant.PORTAL_DATE_FORMATER.parse(value));
	}

	@Override
	public String toString(LocalDate value) {
		return PortalConstant.PORTAL_DATE_FORMATER.format(value);
	}

}
