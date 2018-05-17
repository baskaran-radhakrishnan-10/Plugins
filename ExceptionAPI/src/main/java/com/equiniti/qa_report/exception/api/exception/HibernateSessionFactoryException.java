package com.equiniti.qa_report.exception.api.exception;

import com.equiniti.qa_report.exception.api.BaseException;
import com.equiniti.qa_report.exception.api.FaultCode;

public class HibernateSessionFactoryException extends BaseException {

	private static final long serialVersionUID = 1L;

	public HibernateSessionFactoryException(FaultCode code, Throwable exception) {
		super(code, exception);
	}
		
	public HibernateSessionFactoryException(FaultCode errorCode) {
		super(errorCode);
	}
	
}
