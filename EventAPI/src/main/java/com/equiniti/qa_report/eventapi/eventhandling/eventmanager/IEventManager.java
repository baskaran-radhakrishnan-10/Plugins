package com.equiniti.qa_report.eventapi.eventhandling.eventmanager;

import java.util.Map;

import com.equiniti.qa_report.eventapi.eventhandling.generic.IEvent;
import com.equiniti.qa_report.eventapi.eventhandling.processor.IEventProcessor;
import com.equiniti.qa_report.exception.api.exception.EventException;


public interface IEventManager {
	
	public void processEvent(IEvent event) throws EventException;

	public void setEventProcessorMap(Map<String,IEventProcessor<IEvent>> eventProcessorMap);
	
	public Map<String, IEventProcessor<IEvent>> getEventProcessorMap();
	
	/**
	 * Method a register new event post initilization of the Event Manager.
	 * @param eventProcessorKey
	 * @param eventProcessor
	 */
	void registerEventProcessor(String eventProcessorKey, IEventProcessor<IEvent> eventProcessor);
	
	/**
	 * Method a register new events post initilization of the Event Manager.
	 * @param eventProcessorKey
	 * @param eventProcessor
	 */
	void registerEventProcessors(Map<String, IEventProcessor<IEvent>> eventProcessorMap);
}
