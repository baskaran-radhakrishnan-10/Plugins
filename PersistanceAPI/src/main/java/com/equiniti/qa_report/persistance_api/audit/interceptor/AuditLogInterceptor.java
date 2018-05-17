package com.equiniti.qa_report.persistance_api.audit.interceptor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import com.equiniti.qa_report.persistance_api.audit.api.IAuditLog;
import com.equiniti.qa_report.persistance_api.audit.util.AuditLogUtil;
import com.equiniti.qa_report.persistance_api.hibernate.HibernateConfiguration;

@SuppressWarnings("rawtypes")
public class AuditLogInterceptor extends EmptyInterceptor{
	
	private static final Logger LOG = Logger.getLogger(AuditLogInterceptor.class.getName());

	private static final long serialVersionUID = 7330047871846825422L;
	
	private HibernateConfiguration hibernateConfiguration;

	private Set inserts = new HashSet();
	private Set updates = new HashSet();
	private Set deletes = new HashSet();

	public void setHibernateConfiguration(HibernateConfiguration hibernateConfiguration) {
		this.hibernateConfiguration = hibernateConfiguration;
	}

	public boolean onSave(Object entity,Serializable id,Object[] state,String[] propertyNames,Type[] types)throws CallbackException {
		LOG.debug("onSave");
		if (entity instanceof IAuditLog){
			inserts.add(entity);
		}
		return false;
	}

	public boolean onFlushDirty(Object entity,Serializable id,Object[] currentState,Object[] previousState,String[] propertyNames,Type[] types)throws CallbackException {
		LOG.debug("onFlushDirty");
		if (entity instanceof IAuditLog){
			updates.add(entity);
		}
		return false;
	}

	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames,Type[] types) {
		LOG.debug("onDelete");
		if (entity instanceof IAuditLog){
			deletes.add(entity);
		}
	}

	//called before commit into database
	public void preFlush(Iterator iterator) {
		LOG.debug("preFlush");
	}	

	//called after committed into database
	public void postFlush(Iterator iterator) {
		LOG.debug("postFlush");
		try{
			Iterator iterator1=inserts.iterator();
			while(iterator1.hasNext()){
				IAuditLog entity = (IAuditLog)iterator1.next();
				LOG.debug("postFlush - insert");
				AuditLogUtil.LogIt("Saved",entity,hibernateConfiguration);
			}

			Iterator iterator2=updates.iterator();
			while(iterator2.hasNext()){
				IAuditLog entity = (IAuditLog) iterator2.next();
				LOG.debug("postFlush - update");
				AuditLogUtil.LogIt("Updated",entity,hibernateConfiguration);
			}

			Iterator iterator3=deletes.iterator();
			while(iterator3.hasNext()){
				IAuditLog entity = (IAuditLog) iterator3.next();
				LOG.debug("postFlush - delete");
				AuditLogUtil.LogIt("Deleted",entity,hibernateConfiguration);
			}

		} finally {
			
			inserts.clear();
			updates.clear();
			deletes.clear();
		}
	}	

}
