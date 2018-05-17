package com.equiniti.qa_report.eventapi.eventhandling.eventmanager;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.equiniti.qa_report.eventapi.eventhandling.generic.IEvent;
import com.equiniti.qa_report.eventapi.eventhandling.processor.IEventProcessor;
import com.equiniti.qa_report.exception.api.exception.DaoException;
import com.equiniti.qa_report.exception.api.exception.EventException;

public class EventManagerImpl implements IEventManager {
	private static final Logger LOG = Logger.getLogger(EventManagerImpl.class.getName());
	private Map<String, IEventProcessor<IEvent>> eventProcessorMap;
	
	
	public void processEvent(IEvent event) throws EventException {
		
		String eventname = null;
		IEventProcessor<IEvent> eventprocessor = null;
		
		try{
			/**
			 * Validate user input			
			 */
			if (event == null || event.getEventName() == null) {
				throw new IllegalArgumentException("Invalid event name");
			}
			
			eventname=event.getEventName();
			
			/**
			 * Check for the event processor associated with event.
			 */
			if (eventProcessorMap == null || 
					eventProcessorMap.get(eventname) == null) {
				LOG.error("Could not locate event processor for the given event: "+eventname);
				//throw new JCBaseRunTimeException(EventFaultCodes.EVENT_FAULT_CODE);
			}
			
			eventprocessor=eventProcessorMap.get(eventname);
			eventprocessor.processEvent(event);
			
		} catch(EventException e) {
			LOG.error("Error in processEvent:", e);
			throw e;
		} catch(@SuppressWarnings("hiding") DaoException e) { //we may not reach this block
			LOG.error("Error in processEvent:", e);
			throw new EventException(e.getFaultCode(), e);
		}
	}

	public void setEventProcessorMap(Map<String, IEventProcessor<IEvent>> eventProcessorMap) {
		if(this.eventProcessorMap == null)
			this.eventProcessorMap = new HashMap<String, IEventProcessor<IEvent>>();
		this.eventProcessorMap.putAll(eventProcessorMap);
	}
	
	public Map<String, IEventProcessor<IEvent>> getEventProcessorMap() {
		return eventProcessorMap;
	}
	
	/**
	 * Method a register new event post initilization of the Event Manager.
	 * @param eventProcessorKey
	 * @param eventProcessor
	 */
	public void registerEventProcessor(String eventProcessorKey, IEventProcessor<IEvent> eventProcessor){
		if(this.eventProcessorMap == null)
			this.eventProcessorMap = new HashMap<String, IEventProcessor<IEvent>>();
		eventProcessorMap.put(eventProcessorKey, eventProcessor);
	}
	
	/**
	 * Method a register new events post initilization of the Event Manager.
	 * @param eventProcessorKey
	 * @param eventProcessor
	 */
	public void registerEventProcessors(Map<String, IEventProcessor<IEvent>> eventProcessorMap){
		if(this.eventProcessorMap == null)
			this.eventProcessorMap = new HashMap<String, IEventProcessor<IEvent>>();
		this.eventProcessorMap.putAll(eventProcessorMap);
	}
	
}
