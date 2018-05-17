package com.equiniti.qa_report.eventapi.eventhandling.processor;

import java.util.List;

import com.equiniti.qa_report.eventapi.eventhandling.generic.IEvent;
import com.equiniti.qa_report.eventapi.eventhandling.handler.IEventHandler;
import com.equiniti.qa_report.exception.api.exception.EventException;

public interface IEventProcessor<T extends IEvent> {
		
	
	/**
	 * @param handlersList the handlersList to set
	 */
	public void setHandlersList(List<IEventHandler<T>> handlersList) ;
	
	public void processEvent(T event) throws EventException;
	
}
