package com.equiniti.qa_report.exception.api.faultcode;

import com.equiniti.qa_report.exception.api.FaultCode;

public class SecurityFaultCodes extends FaultCode {

	private static final long serialVersionUID = -4119861979164882832L;

	protected SecurityFaultCodes(String faultCode) {
		super(faultCode);
	}
	
	public static final SecurityFaultCodes INVALID_KEY_ERROR = new SecurityFaultCodes("SECURITY.001");
	
	public static final SecurityFaultCodes BLACK_LISTED_KEY_ERROR = new SecurityFaultCodes("SECURITY.002");
	
	public static final SecurityFaultCodes SECURITY_FILE_NOT_AVAILABLE_ERROR = new SecurityFaultCodes("SECURITY.003");
	
	public static final SecurityFaultCodes SECURITY_FILE_LOADING_ERROR = new SecurityFaultCodes("SECURITY.004");
	
	public static final SecurityFaultCodes EMPTY_PROPERTIES_ERROR = new SecurityFaultCodes("SECURITY.005");
	
	public static final SecurityFaultCodes SIGNATURE_NULL_OR_EMPTY_ERROR = new SecurityFaultCodes("SECURITY.006");
	
	public static final SecurityFaultCodes SECURITYKEY_NULL_OR_EMPTY_ERROR = new SecurityFaultCodes("SECURITY.007");
	
	public static final SecurityFaultCodes SECURITYKEY_IS_INVALID_ERROR = new SecurityFaultCodes("SECURITY.008");
	
	public static final SecurityFaultCodes SECURITY_INFO_NOT_AVAILABLE_ERROR = new SecurityFaultCodes("SECURITY.009");
	
	public static final SecurityFaultCodes SIGNATURE_IS_INVALID_ERROR = new SecurityFaultCodes("SECURITY.010");
	
	public static final SecurityFaultCodes SECURITY_DATE_INFO_NULL_ERROR = new SecurityFaultCodes("SECURITY.011");
	
	public static final SecurityFaultCodes SECURITY_EXPIRED_ERROR = new SecurityFaultCodes("SECURITY.012");
	
	public static final SecurityFaultCodes SECURE_TOKEN_NULL_OR_EMPTY_ERROR = new SecurityFaultCodes("SECURITY.013");
	
	public static final SecurityFaultCodes SECURE_TOKEN_IS_INVALID_ERROR = new SecurityFaultCodes("SECURITY.014");
	
	public static final SecurityFaultCodes MAC_ADDRESS_MISMATCH_ERROR = new SecurityFaultCodes("SECURITY.015");
	
	
	public static final SecurityFaultCodes SECURITY_CRYPTO_INIT_FAILED_ERROR = new SecurityFaultCodes("SECURITY.016");
	
	public static final SecurityFaultCodes SECURITY_CRYPTO_ENCRIPTION_FAILED_ERROR = new SecurityFaultCodes("SECURITY.017");
	
	public static final SecurityFaultCodes SECURITY_CRYPTO_DECRIPTION_FAILED_ERROR = new SecurityFaultCodes("SECURITY.018");
	
	public static final SecurityFaultCodes SECURITY_SIGNATURE_SIGN_FAILED_ERROR = new SecurityFaultCodes("SECURITY.019");
	
	public static final SecurityFaultCodes SECURITY_SIGNATURE_VERIFY_FAILED_ERROR = new SecurityFaultCodes("SECURITY.020");
	
	public static final SecurityFaultCodes SECURITY_CRYPTO_KEYS_READ_FAILED= new SecurityFaultCodes("SECURITY.021");
	
	public static final SecurityFaultCodes SECURITY_USER_NAME_ALREADY_EXSIST= new SecurityFaultCodes("SECURITY.022");
	
	public static final SecurityFaultCodes SECURITY_DATE_PARSE_ERROR= new SecurityFaultCodes("SECURITY.023");
	
	public static final SecurityFaultCodes SECURITY_RESTAPI_FAILURE_ERROR= new SecurityFaultCodes("SECURITY.024");
	
	public static final SecurityFaultCodes SECURITY_DB_CLONE_FAILURE_ERROR= new SecurityFaultCodes("SECURITY.025");
	
	public static final SecurityFaultCodes WINDOWS_REGISTRY_FILE_MISSING_ERROR= new SecurityFaultCodes("SECURITY.026");
	
	
}
