package com.equiniti.qa_report.eventapi.eventhandling.processor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.equiniti.qa_report.eventapi.eventhandling.generic.IEvent;
import com.equiniti.qa_report.eventapi.eventhandling.handler.IEventHandler;
import com.equiniti.qa_report.exception.api.exception.EventException;


public class BaseEventProcessor<T extends IEvent> implements IEventProcessor<T> {
	private static final Logger LOG = Logger.getLogger(BaseEventProcessor.class.getName());
	private List<IEventHandler<T>> handlersList = new ArrayList<IEventHandler<T>>();

	public List<IEventHandler<T>> getHandlersList() {
		return handlersList;
	}

	public void setHandlersList(List<IEventHandler<T>> handlersList) {
		this.handlersList = handlersList;
	}

	public void processEvent(T event)throws EventException {
		LOG.debug("Start: Base Event processor: "+event.getEventName());
		Iterator<IEventHandler<T>> handlers=handlersList.iterator();
		while(handlers.hasNext()){
			handlers.next().processEvent(event);
		}
	}
}
