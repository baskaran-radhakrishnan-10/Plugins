package com.equiniti.qa_report.exception.api.exception;

import com.equiniti.qa_report.exception.api.BaseException;
import com.equiniti.qa_report.exception.api.FaultCode;

public class DaoException extends BaseException {

	private static final long serialVersionUID = 1L;

	public DaoException(FaultCode code, Throwable exception) {
		super(code, exception);
	}
		
	public DaoException(FaultCode errorCode) {
		super(errorCode);
	}
	
}
