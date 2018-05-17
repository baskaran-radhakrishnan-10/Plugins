package com.equiniti.exception.api.exception;

import com.equiniti.exception.api.FaultCode;

public class UIException extends ControllerException {

	private static final long serialVersionUID = 1L;

	public UIException(FaultCode code, Throwable exception) {
		super(code, exception);
	}
		
	public UIException(FaultCode errorCode) {
		super(errorCode);
	}
	
}
