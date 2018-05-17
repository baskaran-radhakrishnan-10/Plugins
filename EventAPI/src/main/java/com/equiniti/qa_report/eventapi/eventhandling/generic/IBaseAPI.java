package com.equiniti.qa_report.eventapi.eventhandling.generic;


public interface IBaseAPI {

	/**
	 * Get the event instance of the given class.
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public <T> T getEvent(Class<T> clazz);
	
}
