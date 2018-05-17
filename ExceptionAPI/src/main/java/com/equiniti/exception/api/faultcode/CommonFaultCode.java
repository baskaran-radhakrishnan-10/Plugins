package com.equiniti.exception.api.faultcode;

import com.equiniti.exception.api.FaultCode;

/**
 * Common Fault code
 * 
 * @author Baskaran Radhakrishnan
 * @version 1.0
 */

public class CommonFaultCode extends FaultCode {

	private static final long serialVersionUID = 2578778562198352572L;
	
	protected CommonFaultCode(String faultCode) {
		super(faultCode);
	}

	public static final CommonFaultCode UNKNOWN_ERROR = new CommonFaultCode("COMMON.001");

	public static final CommonFaultCode UNSUPPORTED_EVENT = new CommonFaultCode("COMMON.002");
	
	public static final CommonFaultCode INVALID_LICENSE_ERROR = new CommonFaultCode("COMMON.003");
	
	public static final CommonFaultCode CACHE_FAILED_ERROR = new CommonFaultCode("COMMON.004");
	
	public static final CommonFaultCode EMPTY_SESSION_ERROR = new CommonFaultCode("COMMON.005");
	
	public static final CommonFaultCode PARSER_ERROR = new CommonFaultCode("COMMON.006");
	
	public static final CommonFaultCode OBJECT_MAPPER_FAILED_ERROR = new CommonFaultCode("COMMON.007");
	
}
