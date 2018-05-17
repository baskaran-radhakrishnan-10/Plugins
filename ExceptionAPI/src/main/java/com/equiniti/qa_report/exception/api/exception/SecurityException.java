
package com.equiniti.qa_report.exception.api.exception;

import com.equiniti.qa_report.exception.api.BaseException;
import com.equiniti.qa_report.exception.api.FaultCode;

/**
 * 
 * This exception regroup all the license exceptions.
 * 
 * @author Baskaran Radhakrishnan
 * 
 */
public class SecurityException extends BaseException {

    private static final long serialVersionUID = 7895696254570225320L;

	public SecurityException(FaultCode code, Throwable exception) {
		super(code, exception);
	}
		
	public SecurityException(FaultCode errorCode) {
		super(errorCode);
	}

}
