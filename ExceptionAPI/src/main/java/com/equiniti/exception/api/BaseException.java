package com.equiniti.exception.api;

/*
 * Class: BaseException
 *
 * Comments for Developers Only:
 *
 * Version History:
 * 
 * Ver  Date         Who                Release     What and Why
 * ---  ----------   ----------         -------     ---------------------------------------
 * 1.0  03/11/2015  Baskaran Radhakrishnan		 1.0		Exception Framework
 * 
 *
 *
 * 
 ******************************************************/


import java.io.PrintWriter;
import java.io.StringWriter;


import org.apache.log4j.Logger;

/**
 * A class used for BaseException
 * 
 */
public class BaseException extends Exception {

	private static final long serialVersionUID = 7303294259057377618L;

	private static Logger LOG = Logger.getLogger(BaseException.class.getName());

	protected FaultCode faultCode;
	protected String shortMessage = "";
	protected String occurence;
	protected String detailedMessage = null;


	/**
	 * @param FaultCode
	 * @param Throwable
	 */
	public BaseException(FaultCode code, Throwable exception)
	{
		super(code.toString(), exception);
		this.faultCode = code;
		shortMessage = CommonExceptionHandler.getErrorMessage(faultCode.toString());
	}    

	/**
	 * @param FaultCode
	 */
	public BaseException(FaultCode errorCode)
	{
		super(errorCode.toString());
		this.faultCode = errorCode;
	}


	/**
	 * @return FaultCode
	 */
	public FaultCode getFaultCode() {
		return faultCode;
	} 

	/**
	 * @return String
	 */
	public String getOccurence() {
		return occurence;
	}

	/**
	 * @param occurence as String
	 */
	public void setOccurence(String occurence) {
		this.occurence = occurence;
	}    

	/* (non-Javadoc)
	 * @see java.lang.Throwable#toString()
	 */
	public String toString(){
		return (faultCode == null ? super.getMessage() : (faultCode.toString() + " : " + shortMessage));
	}


	/* 
	 * Overriding super class method to return error description
	 * (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage(){
		if(detailedMessage  == null)
		{
			detailedMessage = CommonExceptionHandler.getDetailedErrorMessage(faultCode.toString());
			if (LOG.isDebugEnabled()){
				LOG.debug("Message inside getMessage(): "+ detailedMessage);
				LOG.debug("FaultCode class:" + faultCode.getClass());						
			}
		}
		return detailedMessage;
	}	

	/**
	 * @param exception as Throwable
	 * @return String
	 */
	public String getStackTrace(Throwable exception){
		StringWriter sw=new  StringWriter();
		PrintWriter pw=new PrintWriter(sw);
		exception.printStackTrace(pw);
		return sw.toString();
	}


}
