package com.equiniti.qa_report.eventapi.eventhandling.generic;

import java.io.Serializable;

import com.equiniti.qa_report.eventapi.eventhandling.eventmanager.IEventManager;


public class BaseEvent extends Event implements Serializable {
		
	private static final long serialVersionUID = -6287331787409891438L;
	
	/**
	 * Event manager associated with this event.
	 */
	protected transient IEventManager eventManager;
	
	public BaseEvent(String eventName) {
		this.setEventName(eventName);
	}
	
	public IEventManager getEventManager() {
		return eventManager;
	}

	public void setEventManager(IEventManager eventManager) {
		this.eventManager = eventManager;
	}
	
}
