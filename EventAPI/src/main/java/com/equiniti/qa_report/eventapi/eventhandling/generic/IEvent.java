/*
 * 
 * Class: IEvent.java
 *
 * Comments for Developers Only:
 *
 * Version History:
 * 
 * Ver  Date          Who                Release     What and Why
 * ---  ----------    ----------         -------     ---------------------------------------

 *      06-AUG-2010   Hareendranath		 1.0		 Added Member Variable startTime
 * 
 * This software is the confidential and proprietary information of Jamcracker, Inc. 
 * ("Confidential Information").  You shall not disclose such Confidential Information
 *  and shall use it only in accordance with the terms of the license agreement you 
 *  entered into with Jamcracker, Inc. Copyright (c) 2000 Jamcracker, Inc.  All Rights    
 *  Reserved
 *
 *
 * 
 *****************************************************
 */

package com.equiniti.qa_report.eventapi.eventhandling.generic;

import java.sql.Timestamp;

public interface IEvent {

	/**
	 * @return the eventId
	 */
	public abstract String getEventId();

	/**
	 * @param eventId 
	 *            the eventId to set
	 */
	public abstract void setEventId(String eventId);

	/**
	 * @param the InResponse
	 */
	public void putInResponse(String key, Object value);
	/**
	 * @return the FromResponse
	 */
	public Object getFromResponse(String key);
	
	/**
	 * @return the parentEventId - this is the parent event instance id
	 */
	public abstract String getParentEventId();

	/**
	 * @param parentEventId
	 *            the parentEventId to set
	 */
	public abstract void setParentEventId(String parentEventId);

	/**
	 * @return the eventName
	 */
	public abstract String getEventName();

	/**
	 * @param eventName
	 *            the eventName to set
	 */
	public abstract void setEventName(String eventName);
	
	// Added by ElayaRaja
	/**
	 * @return CreatedBy as an int
	 */
	public abstract String getCreatedBy();
	/**
	 * @param createdBy
	 */
	public abstract void setCreatedBy(String createdBy);
	/**
	 * @return status
	 */
	public abstract String getStatus();
	/**
	 * @param status
	 */
	public abstract void setStatus(String status);
	/**
	 * @return description
	 */
	public abstract String getDescription();
	/**
	 * @param description
	 */
	public abstract void setDescription(String description);
	/**
	 * @return eventSource
	 */
	public abstract String getEventSource();
	/**
	 * @param eventSource
	 */
	public abstract void setEventSource(String eventSource);
	
	
	public abstract String getEventInstanceID() ;

	/**
	 * 
	 * @param eventInstanceID Event instance id for an event of eventID
	 */
	public abstract  void setEventInstanceID(String eventInstanceID) ;
	
	/**
	 * @return the startTime
	 */
	public Timestamp getStartTime();

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Timestamp startTime);
	/**
	 * @return the startTime
	 */
	/**
	 * @return the endTime
	 */
	public Timestamp getEndTime();

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Timestamp endTime);
	
	
	/**
	 * @return the userAccessInfo
	 */
	public abstract String getUserAccessInfo();
	
	/**
	 * @param userAccessInfo the userAccessInfo to set
	 */
	public abstract void setUserAccessInfo(String userAccessInfo);
}