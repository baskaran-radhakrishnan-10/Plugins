package com.equiniti.persistance_api.audit.util;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.joda.time.DateTime;

import com.equiniti.persistance_api.audit.api.IAuditLog;
import com.equiniti.persistance_api.entity.AuditLog;
import com.equiniti.persistance_api.hibernate.HibernateConfiguration;

public class AuditLogUtil{

	private static final Logger LOG=Logger.getLogger(AuditLogUtil.class);

	public static void LogIt(String action,IAuditLog entity,HibernateConfiguration configuration){
		Session tempSession = configuration.getSessionFactory().openSession();
		Transaction transaction=null;
		try {
			if(null != entity.getLogDeatil() && null != entity.getId()){
				AuditLog auditRecord = new AuditLog();
				auditRecord.setAction(action);
				auditRecord.setCreatedDate(new DateTime());
				auditRecord.setDetail(entity.getLogDeatil());
				auditRecord.setEntityId(entity.getId());
				auditRecord.setEntityName(entity.getClass().toString());
				transaction=tempSession.beginTransaction();
				tempSession.save(auditRecord);
				transaction.commit();
				tempSession.flush();
			}
		}catch(Exception e){
			LOG.error(e.getMessage(), e);
			if(null != transaction && !transaction.wasRolledBack()){
				tempSession.getTransaction().rollback();
			}
			e.printStackTrace();
		}finally {
			tempSession.close();
		}

	}
}