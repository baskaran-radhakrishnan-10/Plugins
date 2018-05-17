package com.equiniti.exception.api.exception;

import com.equiniti.exception.api.FaultCode;



public class EventException extends DaoException {
	
	private static final long serialVersionUID = -136341024193327855L;

	/**
	 * @param FaultCode and exception.
	 */
	public EventException(FaultCode code, Throwable exception){
		super(code, exception);
	}    

	/**
	 * @param FaultCode
	 */
	public EventException(FaultCode errorCode){
		super(errorCode);		
	}

}
