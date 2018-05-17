

package com.equiniti.qa_report.eventapi.eventhandling.generic;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;


public abstract class Event implements Serializable, IEvent {
	
	private static final long serialVersionUID = -8240686729584168914L;

	private String eventName;

	private String eventId;

	private String parentEventId;

	private Map<String, Object> responseMap;

	private String createdBy;
	private String status;
	private String description;
	private String eventSource;	
	
	private String eventInstanceID; 
	private Timestamp startTime;
	private Timestamp endTime;
	

	private String userAccessInfo;
	
	public Event(String eventName) {
		this.eventName=eventName;
	}
	
	public String getUserAccessInfo() {
		return userAccessInfo;
	}

	public void setUserAccessInfo(String userAccessInfo) {
		this.userAccessInfo = userAccessInfo;
	}

	public Event() {
		responseMap = new HashMap<String, Object>();
	}
	
	public void putInResponse(String key, Object value) {
		responseMap.put(key, value);
	}
	
	public Object getFromResponse(String key) {
		return (responseMap.get(key));
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getParentEventId() {
		return parentEventId;
	}

	public void setParentEventId(String parentEventId) {
		this.parentEventId = parentEventId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEventSource() {
		return eventSource;
	}

	public void setEventSource(String eventSource) {
		this.eventSource = eventSource;
	}

	public String getEventInstanceID() {
		return eventInstanceID;
	}

	public void setEventInstanceID(String eventInstanceID) {
		this.eventInstanceID = eventInstanceID;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
 
	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
}
