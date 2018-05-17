package com.equiniti.qa_report.eventapi.eventhandling.handler;


public interface AuditableEvent {
	
	/**
	 * returns entity type
	 * @return
	 */
	String getEntityType();
	/**
	 * return entity id
	 * @return
	 */
	String getEntityId();
	/**
	 * returns parent entity name
	 * @return
	 */
	String getEntityName();
	
	String getTaskName();
	
	String getTaskType();
	
	String getSubEntityName();
	
	String getSubEntityId();
	
	String getSubTaskName();
	
}
