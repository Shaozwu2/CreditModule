package com.kooppi.nttca.portal.exception.domain;

import java.util.Collection;
import java.util.Optional;

import com.google.common.base.Strings;

public class PortalExceptionUtils extends PortalException{

	private static final long serialVersionUID = 6816212791635793096L;

	protected final PortalErrorCode errorCode;

	public PortalExceptionUtils(final PortalErrorCode errorCode) {
	    this.errorCode = errorCode;
	}
	
	@Override
	public PortalErrorCode getErrorCode() {
		return errorCode;
	}

	public static void throwIfTrue(boolean condition, PortalErrorCode errorCode) throws PortalExceptionUtils {
        if (condition) {
            throw new PortalExceptionUtils(errorCode);
        }
    }
    
    public static void throwIfTrue(boolean condition, PortalExceptionUtils e) throws PortalExceptionUtils {
        if (condition) {
            throw e;
        }
    }
    
    public static void throwIfFalse(boolean condition, PortalErrorCode errorCode) throws PortalExceptionUtils {
       throwIfTrue(!condition, errorCode);
    }
    
    public static <T> T throwIfNull(T record, PortalErrorCode errorCode) throws PortalExceptionUtils {
        throwIfTrue(record == null,errorCode);
        return record;
    }

    public static <T> T throwIfEmpty(Optional<T> record, PortalErrorCode errorCode) throws PortalExceptionUtils {
        return throwIfNull(record.orElse(null), errorCode);
    }
    
    public static void throwIfNullOrEmptyString(String str, PortalErrorCode errorCode) throws PortalExceptionUtils {
        throwIfTrue(Strings.isNullOrEmpty(str),errorCode);
    }
    
    public static <T> void throwIfEmpty(Collection<T> record , PortalErrorCode errorCode) throws PortalExceptionUtils{
    	throwIfNull(record , errorCode);
    	throwIfTrue(record.isEmpty(),errorCode);
    }
    
    public static void throwNow(PortalErrorCode errorCode) throws PortalExceptionUtils {
        throw new PortalExceptionUtils(errorCode);
    }
	
}
