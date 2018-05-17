package com.equiniti.exception.api.exception;

import com.equiniti.exception.api.BaseException;
import com.equiniti.exception.api.FaultCode;

public class DaoException extends BaseException {

	private static final long serialVersionUID = 1L;

	public DaoException(FaultCode code, Throwable exception) {
		super(code, exception);
	}
		
	public DaoException(FaultCode errorCode) {
		super(errorCode);
	}
	
}
