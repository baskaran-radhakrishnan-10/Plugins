package com.equiniti.qa_report.exception.api.exception;

import com.equiniti.qa_report.exception.api.BaseException;
import com.equiniti.qa_report.exception.api.FaultCode;




/**
 * All advanced API exceptions should extend this exception.
 * 
 * 
 * @author Baskaran Radhakrishnan
 * 
 * @version 1.0
 *
 */
public class APIException extends EventException {

	private static final long serialVersionUID = 6542088469001073947L;

	/**
	 * @param errorCode	
	 */
	public APIException(FaultCode errorCode) {
		super(errorCode);
	}

	/**
	 * @param code
	 * @param exception
	 */
	public APIException(FaultCode code, Throwable exception) {
		super(code, exception);

		FaultCode rootCauseFC = getFaultCode (exception);
		if (rootCauseFC != null) {
			this.faultCode = rootCauseFC;
		}
	}

	private FaultCode getFaultCode(Throwable exception) {
		FaultCode faultCode = null;
		if(exception instanceof BaseException){
			BaseException jcException = (BaseException)exception;
			FaultCode legacyFaultCode = jcException.getFaultCode();
			faultCode = FaultCode.getFaultCode(legacyFaultCode.getCode()); 
		}else if(exception instanceof BaseException){
			BaseException ex= (BaseException)exception;
			faultCode = ex.getFaultCode();
		}
		return faultCode;
	}

	/*public APIException(FaultCode code, Throwable exception, Object ... params) {
		super(new JCDynamicFaultCode(FaultCode.getFaultCode(code.getCode()),new ArrayList<Object>(Arrays.asList(params))), exception);
	}*/
}
