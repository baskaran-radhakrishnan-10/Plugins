package com.equiniti.persistance_api.hibernate.api;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public interface HibernateConfigurationAPI {
	
	public SessionFactory getSessionFactory();
	
	public Session getSession();

}
