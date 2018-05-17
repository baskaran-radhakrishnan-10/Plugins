package com.equiniti.qa_report.exception.api.faultcode;

import com.equiniti.qa_report.exception.api.FaultCode;

/**
 * A class used for EventFaultCodes
 * 
 */
public class EventFaultCodes extends FaultCode {

	private static final long serialVersionUID = -4119861979164882832L;

	protected EventFaultCodes(String faultCode) {
		super(faultCode);
	}
	
	public static final EventFaultCodes UN_KNOWN_ERROR = new EventFaultCodes("12COEF0001");

}
