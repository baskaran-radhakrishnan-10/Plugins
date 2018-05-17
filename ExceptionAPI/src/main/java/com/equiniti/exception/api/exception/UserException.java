package com.equiniti.exception.api.exception;

import com.equiniti.exception.api.FaultCode;

public class UserException extends APIException {

	private static final long serialVersionUID = 1L;

	public UserException(FaultCode code, Throwable exception) {
		super(code, exception);
	}
		
	public UserException(FaultCode errorCode) {
		super(errorCode);
	}
	
}
