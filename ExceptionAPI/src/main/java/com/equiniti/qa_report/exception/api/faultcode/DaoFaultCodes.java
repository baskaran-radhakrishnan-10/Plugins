package com.equiniti.qa_report.exception.api.faultcode;

import com.equiniti.qa_report.exception.api.FaultCode;

/**
 * A class used for EventFaultCodes
 * 
 */
public class DaoFaultCodes extends FaultCode {

	private static final long serialVersionUID = -4119861979164882832L;

	protected DaoFaultCodes(String faultCode) {
		super(faultCode);
	}
	
	public static final DaoFaultCodes HIBERNATE_SAVE_ENTITY_ERROR = new DaoFaultCodes("DAO.001");
	
	public static final DaoFaultCodes HIBERNATE_DELETE_ENTITY_ERROR = new DaoFaultCodes("DAO.002");
	
	public static final DaoFaultCodes HIBERNATE_UPDATE_ENTITY_ERROR = new DaoFaultCodes("DAO.003");
	
	public static final DaoFaultCodes HIBERNATE_GET_ENTITY_ERROR = new DaoFaultCodes("DAO.004");

}
