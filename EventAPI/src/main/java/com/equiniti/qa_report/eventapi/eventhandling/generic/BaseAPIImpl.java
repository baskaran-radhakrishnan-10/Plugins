package com.equiniti.qa_report.eventapi.eventhandling.generic;

import com.equiniti.qa_report.eventapi.eventhandling.eventmanager.IEventManager;
import com.equiniti.qa_report.exception.api.exception.EventException;

public class BaseAPIImpl implements IBaseAPI {

	private IEventManager eventManager;
	
	@SuppressWarnings("unchecked")
	public <T> T getEvent(Class<T> clazz) { 
		T event = null;
		try {
			Object eventInstance = clazz.newInstance();
			if (eventInstance instanceof BaseEvent) {
				BaseEvent baseEvent = (BaseEvent)eventInstance;
				baseEvent.setEventManager(eventManager);
			}
			event = (T) eventInstance;
		} catch (InstantiationException e) {
			throw new RuntimeException("Unable to instantiate event object", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Unable to instantiate event object", e);
		}
		return event;
	}
	
	protected BaseEvent processEvent(BaseEvent event) throws EventException { 
		try {
			getEventManager().processEvent(event);
		} catch (Exception e) {
			throw e;
		} 
		return event;
	}
	
	public IEventManager getEventManager() {
		return eventManager;
	}

	public void setEventManager(IEventManager eventManager) {
		this.eventManager = eventManager;
	}
		
}
