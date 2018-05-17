package com.equiniti.qa_report.eventapi.eventhandling.handler;

import com.equiniti.qa_report.eventapi.eventhandling.generic.IEvent;
import com.equiniti.qa_report.exception.api.exception.EventException;

public interface IEventHandler<T extends IEvent> {

    public void processEvent(T request) throws EventException;
    
}
