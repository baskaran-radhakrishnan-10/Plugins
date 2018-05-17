package com.equiniti.exception.api.faultcode;

import com.equiniti.exception.api.FaultCode;

/**
 * A class used for EventFaultCodes
 * 
 */
public class UserFaultCodes extends FaultCode {

	private static final long serialVersionUID = -4119861979164882832L;

	protected UserFaultCodes(String faultCode) {
		super(faultCode);
	}
	
	public static final UserFaultCodes EVENT_FAULT_CODE = new UserFaultCodes("12COEF0001");

}
