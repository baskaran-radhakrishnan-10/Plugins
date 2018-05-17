package com.equiniti.exception.api.exception;

import com.equiniti.exception.api.FaultCode;

public class ControllerException extends APIException {

	private static final long serialVersionUID = 1L;

	public ControllerException(FaultCode code, Throwable exception) {
		super(code, exception);
	}
		
	public ControllerException(FaultCode errorCode) {
		super(errorCode);
	}
	
}
