
package com.equiniti.exception.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents fault code for a given error message. Every fault code is a 9 digit number.
 * Objects of this class are available as constants in each module. They are used while throwing Exceptions of type BaseException.<BR>
 */
public class FaultCode implements Serializable {

	private static final long serialVersionUID = 6234651080929476232L;
	private String code;
	private static Map faultCodes = new HashMap<>();	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return code;
	}
	
	/**
	 * Constructor of the class, which takes the fault code in the form of string
	 * and adds it to the hashmap.
	 * @param faultCode the code to set.
	 */
	protected FaultCode(String faultCode){
		this.code=faultCode;
		// Add this object to hash map
		faultCodes.put(faultCode, this);
	}
	
	/**
	 * Gets FaultCode object for the given error code, if it is defined in any of the FaultCodes.java file.
	 * If no constant is defined for the error code, new FaultCode object with FAULTCODE_NOT_FOUND error code
	 * is returned.
	 *
	 * @param faultCode string representation of the fault code
	 * @return FaultCode object for the given string faultCode.
	 */
	public static FaultCode getFaultCode(String faultCode){

		FaultCode fault =  (FaultCode)faultCodes.get(faultCode);
		// Existing code was throwing FAULTCODE_NOT_FOUND exception if it did not find the faultcode in faultCodes hashmap. 
		//If it not find that in hashmap we should load in hashmap and should throw the exception with same fault code...
		if (fault == null ){
			return new FaultCode(faultCode);
		}
		return fault;
	}

	/**
	 * Gets the string representation of faulcode instance.
	 * @return string representation of faulcode instance
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * Compares other faultcodes with the current fault codes object.
	 * @param obj the ocode to be checked for equality.
	 * @return true if the comparing faultcodes are same, else false.
	 */
	public boolean equals(Object obj)
	{
		boolean flag = false;
		if(this == obj)
		return true;

		if((obj==null) || !(obj instanceof FaultCode))
			flag = false;
		else{
				if (this.hashCode()==obj.hashCode())
				{
					flag = true;
				}
			}
		return flag;
	}
	
	/**
	 * Gets the hash code value for the faultcode object on which this method is invoked. This method returns
	 * the same value if the faultcode objects are same.
	 *
	 * @return hash code value
	 */
	public int hashCode()
	{
       return this.code.hashCode();

	}

}


