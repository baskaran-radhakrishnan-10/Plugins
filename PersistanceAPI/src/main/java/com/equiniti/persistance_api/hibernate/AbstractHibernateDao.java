package com.equiniti.persistance_api.hibernate;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.PersistenceException;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.SessionImpl;
import org.hibernate.jdbc.Work;
import org.hibernate.metadata.ClassMetadata;

import com.equiniti.exception.api.exception.DaoException;
import com.equiniti.exception.api.faultcode.DaoFaultCodes;
import com.equiniti.persistance_api.audit.interceptor.AuditLogInterceptor;
import com.equiniti.persistance_api.consenum.QueryOperationType;
import com.equiniti.persistance_api.consenum.QueryType;
import com.equiniti.persistance_api.hibernate.api.AbstractHibernateDAOAPI;

public class AbstractHibernateDao<E> implements  AbstractHibernateDAOAPI<E>{

	private static final Logger LOG = Logger.getLogger(AbstractHibernateDao.class.getName());

	private AuditLogInterceptor auditInterceptor;

	SessionFactory sessionFactory;

	Map<String,String> hqlMap;

	int batchSize;

	private String whereCluse=" where";

	private String andCluse=" and";

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setHqlMap(Map<String, String> hqlMap) {
		this.hqlMap = hqlMap;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	public void setAuditInterceptor(AuditLogInterceptor auditInterceptor) {
		this.auditInterceptor = auditInterceptor;
	}

	public int saveEntity(E entityClass) throws DaoException	{

		LOG.debug("saveEntity(Class<E> entityClass start ");
		Session session = sessionFactory.withOptions().interceptor(auditInterceptor).openSession();
		int serializable=0;
		try {
			session.beginTransaction();
			serializable=(int) session.save(entityClass);
			if(!session.getTransaction().wasCommitted()){
				session.getTransaction().commit();
			}
		} catch (HibernateException e) {
			LOG.error(e.getMessage(), e);
			session.getTransaction().rollback();
			throw new DaoException(DaoFaultCodes.HIBERNATE_SAVE_ENTITY_ERROR, e);
		}finally{
			if(session!=null){
				session.close();
			}
		}
		LOG.debug("saveEntity(Class<E> entityClass end ");
		return serializable;
	}

	@Override
	public Map<String,Object> callProcedure(String procedureQuery,Map<String,Object> inParamMap,Map<String,Integer> outParamMap) throws DaoException{

		Connection dbConnection = null;

		CallableStatement callableStatement = null;

		Map<String,Object> resultMap = null;

		try {

			String getDBUSERByUserIdSql = this.hqlMap.get(procedureQuery);

			dbConnection = getConnection();

			callableStatement = dbConnection.prepareCall(getDBUSERByUserIdSql);

			int index = 1,outParamIndex=0;

			Set<String> inParamKeySet = inParamMap.keySet();

			for(String key : inParamKeySet){

				Object obj = inParamMap.get(key);

				if(obj instanceof String){

					callableStatement.setString(index, obj.toString());

				}else if(obj instanceof String){

					callableStatement.setInt(index, Integer.parseInt(obj.toString()));

				}else if(obj instanceof Long){

					callableStatement.setLong(index, Long.parseLong(obj.toString()));

				}

				index++;

			}

			Set<String> outParamKeySet = outParamMap.keySet();

			for(String key : outParamKeySet){
				
				if(0 == outParamIndex){
					outParamIndex = index;
				}

				int type = outParamMap.get(key);

				callableStatement.registerOutParameter(index, type);

				index++;

			}

			// execute getDBUSERByUserId store procedure
			callableStatement.executeUpdate();

			resultMap = new HashMap<>();

			for(String key : outParamKeySet){

				int type = outParamMap.get(key);

				switch(type){
					case java.sql.Types.VARCHAR : 
						resultMap.put(key,callableStatement.getString(outParamIndex));
						break;
					case java.sql.Types.INTEGER : 
						resultMap.put(key,callableStatement.getInt(outParamIndex));
						break;
					case java.sql.Types.DATE : 
						resultMap.put(key,callableStatement.getDate(outParamIndex));	
					break;	 
				}
				
				outParamIndex++;

			}

			System.out.println(resultMap);


		} catch (SQLException e) {

			LOG.error(e.getMessage(), e);
			
			throw new DaoException(DaoFaultCodes.HIBERNATE_CALL_PROCEDURE_ERROR, e);

		} finally {

			if (callableStatement != null) {
				try {
					callableStatement.close();
				} catch (SQLException e) {
					throw new DaoException(DaoFaultCodes.RESOURCE_CLEARUP_FAILED_ERROR, e);
				}
			}

			if (dbConnection != null) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					throw new DaoException(DaoFaultCodes.RESOURCE_CLEARUP_FAILED_ERROR, e);
				}
			}

		}
		
		return resultMap;

	}

	public void saveEntityList(List<E> entityClassList) throws DaoException	{
		LOG.debug("saveEntityList(Class<E> entityClass start ");
		Session session = sessionFactory.withOptions().interceptor(auditInterceptor).openSession();
		try {
			session.beginTransaction();
			int size=entityClassList.size();
			for(int index=0;index<size;index++){
				session.save(entityClassList.get(index));
				if( index % batchSize == 0 ) {
					session.flush();
					session.clear();
				}
			}
			if(!session.getTransaction().wasCommitted()){
				session.getTransaction().commit();
			}
		} catch (HibernateException e) {
			LOG.error(e.getMessage(), e);
			session.getTransaction().rollback();
			throw new DaoException(DaoFaultCodes.HIBERNATE_SAVE_ENTITY_ERROR, e);
		}finally{
			if(session!=null){
				session.close();
			}
		}
		LOG.debug("saveEntityList(Class<E> entityClass end ");
	}

	public void updateEntity(E entityClass) throws DaoException	{
		LOG.debug("updateEntity  start ");
		Transaction transaction=null;
		Session session = sessionFactory.withOptions().interceptor(auditInterceptor).openSession();
		try {
			transaction=session.beginTransaction();
			session.update(entityClass);
			if(transaction.isActive() && !transaction.wasCommitted()){
				transaction.commit();
			}
		} catch (HibernateException e) {
			LOG.error(e.getMessage(), e);
			transaction.rollback();
			throw new DaoException(DaoFaultCodes.HIBERNATE_UPDATE_ENTITY_ERROR, e);
		}finally{
			if(session!=null){
				session.close();
			}
		}
		LOG.debug("updateEntity end ");
	}

	public void updateEntityList(List<E> entityClassList) throws DaoException	{
		LOG.debug("updateEntityList  start ");
		Session session = sessionFactory.withOptions().interceptor(auditInterceptor).openSession();
		try {
			session.beginTransaction();

			int size=entityClassList.size();
			for(int index=0;index<size;index++){
				session.update(entityClassList.get(index));
				if( index % batchSize == 0 ) {
					session.flush();
					session.clear();
				}
			}
			if(!session.getTransaction().wasCommitted()){
				session.getTransaction().commit();
			}
		} catch (HibernateException e) {
			LOG.error(e.getMessage(), e);
			session.getTransaction().rollback();
			throw new DaoException(DaoFaultCodes.HIBERNATE_UPDATE_ENTITY_ERROR, e);
		}finally{
			if(session!=null){
				session.close();
			}
		}
		LOG.debug("updateEntityList end ");
	}

	@SuppressWarnings("unchecked")
	public void deleteEntityList(Class<E> entityClass,Map<String,Object> restrictionMap) throws DaoException	{
		LOG.debug("deleteEntityList  start");
		LOG.debug("restrictionMap :"+restrictionMap);
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria=getCriteriaWithRestriction(entityClass,restrictionMap);
			List<E> resultList=criteria.list();
			session.beginTransaction();
			int size=resultList.size();
			for(int index=0;index<size;index++){
				session.delete(resultList.get(index));
				if( index % batchSize == 0 ) {
					session.flush();
					session.clear();
				}
			}
			if(!session.getTransaction().wasCommitted()){
				session.getTransaction().commit();
			}
		} catch (HibernateException e) {
			LOG.error(e.getMessage(), e);
			session.getTransaction().rollback();
			throw new DaoException(DaoFaultCodes.HIBERNATE_DELETE_ENTITY_ERROR, e);
		}finally{
			if(session!=null){
				session.close();
			}
		}
		LOG.debug("deleteEntityList  end");
	}

	@SuppressWarnings("unchecked")
	public void deleteEntity(Class<E> entityClass,Map<String,Object> restrictionMap) throws DaoException	{
		LOG.debug("deleteEntity  start");
		LOG.debug("restrictionMap :"+restrictionMap);
		List<E> resultList=null;
		Session session = sessionFactory.getCurrentSession();
		try {
			if(null != restrictionMap && restrictionMap.size() == 1){
				Set<String> keySet=restrictionMap.keySet();
				for(String key : keySet){
					int entityKey=(int) restrictionMap.get(key);
					session.beginTransaction();
					session.delete(loadEntity(entityClass, entityKey));
					if(!session.getTransaction().wasCommitted()){
						session.getTransaction().commit();
					}
				}
			}else{
				Criteria criteria=getCriteriaWithRestriction(entityClass,restrictionMap);
				resultList=criteria.list();
				if(null != resultList && resultList.size() == 1){
					session.beginTransaction();
					session.delete(resultList.get(0));
					if(!session.getTransaction().wasCommitted()){
						session.getTransaction().commit();
					}
				}
			}
		} catch (HibernateException e) {
			LOG.error(e.getMessage(), e);
			session.getTransaction().rollback();
			throw new DaoException(DaoFaultCodes.HIBERNATE_DELETE_ENTITY_ERROR, e);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
		}
		LOG.debug("deleteEntity  end");
	}

	@SuppressWarnings("unchecked")
	public E loadEntity(Class<E> entityClass,int entityKey) throws DaoException{
		LOG.debug("getEntity  start");
		E entity=null;
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			entity=(E) session.load(entityClass, entityKey);
			LOG.debug(" Entity : "+entity);
			if(!session.getTransaction().wasCommitted()){
				session.getTransaction().commit();
			}
		} catch (HibernateException e) {
			LOG.error(e.getMessage(), e);
			session.getTransaction().rollback();
			throw new DaoException(DaoFaultCodes.HIBERNATE_GET_ENTITY_ERROR, e);
		}finally{
			if(session!=null){
				session.close();
			}
		}
		LOG.debug("getEntity  end");
		return entity;
	}

	@SuppressWarnings("unchecked")
	public E getEntityByCriteria(Class<E> entityClass,Map<String,Object> restrictionMap) throws DaoException {
		LOG.debug("getEntity  start");
		LOG.debug("restrictionMap :"+restrictionMap);
		E entity=null;
		try{
			Criteria criteria=getCriteriaWithRestriction(entityClass,restrictionMap);
			List<E> resultList=criteria.list();
			if(null != resultList && resultList.size() == 1){
				entity=resultList.get(0);
			}
		}catch (HibernateException e) {
			LOG.error(e.getMessage(), e);
			throw new DaoException(DaoFaultCodes.HIBERNATE_GET_ENTITY_ERROR, e);
		}
		LOG.debug("getEntity  end");
		return entity;
	}

	@SuppressWarnings("unchecked")
	public List<E> getEntityListByCriteria(Class<E> entityClass,Map<String,Object> restrictionMap) throws DaoException {
		LOG.debug("getEntityList  start");
		LOG.debug("restrictionMap :"+restrictionMap);
		List<E> entityList=null;
		try{
			Criteria criteria=getCriteriaWithRestriction(entityClass,restrictionMap);
			entityList=criteria.list();
		}catch (HibernateException e) {
			LOG.error(e.getMessage(), e);
			throw new DaoException(DaoFaultCodes.HIBERNATE_GET_ENTITY_ERROR, e);
		}
		LOG.debug("getEntityList  end");
		return entityList;
	}

	public E getEntity(Class<E> entityClass,Map<String,Object> restrictionMap) throws DaoException {
		LOG.debug("getEntity  start");
		LOG.debug("restrictionMap :"+restrictionMap);
		E entity=null;
		List<E> entityList=null;
		try{
			entityList=getEntityList(entityClass, restrictionMap);			
			if(null != entityList && entityList.size() > 0){
				entity=entityList.get(0);
				if(restrictionMap.containsKey("INDEX")){
					entity=entityList.get(Integer.valueOf(restrictionMap.get("INDEX").toString()));
				}
			}
		}catch (HibernateException e) {
			LOG.error(e.getMessage(), e);
			throw new DaoException(DaoFaultCodes.HIBERNATE_GET_ENTITY_ERROR, e);
		}
		LOG.debug("getEntity  end");
		return entity;
	}



	public List<E> getEntityList(Class<E> entityClass,Map<String,Object> restrictionMap) throws DaoException {
		LOG.debug("getEntityList  start");
		LOG.debug("restrictionMap :"+restrictionMap);
		List<E> entityList=null;
		try{
			StringBuffer queryBuffer=new StringBuffer(); 
			queryBuffer.append("from ").append(entityClass.getCanonicalName());
			if(null == restrictionMap){
				entityList=processQuery(null, null, null, QueryOperationType.SELECT, QueryType.HQL, queryBuffer.toString());
			}else{
				if(null != restrictionMap && !restrictionMap.isEmpty()){
					List<String> keySetList=new ArrayList<>(restrictionMap.keySet());
					if(!keySetList.isEmpty()){
						for(int index=0;index<keySetList.size();index++){
							String key=keySetList.get(index);
							Object value=restrictionMap.get(key);
							if(index == 0){
								queryBuffer=constructQueryBuffer(key, value,whereCluse, queryBuffer);
							}else {
								queryBuffer=constructQueryBuffer(key, value, andCluse, queryBuffer);
							}
						}
					}
				}
				entityList=processQuery(null, null, null, QueryOperationType.SELECT, QueryType.HQL, queryBuffer.toString());
			}
		}catch (DaoException e) {
			LOG.error(e.getMessage(), e);
			throw new DaoException(DaoFaultCodes.HIBERNATE_GET_ENTITY_ERROR, e);
		}catch (HibernateException e) {
			LOG.error(e.getMessage(), e);
			throw new DaoException(DaoFaultCodes.HIBERNATE_GET_ENTITY_ERROR, e);
		}
		LOG.debug("getEntityList  end");
		return entityList;
	}

	private StringBuffer constructQueryBuffer(String key,Object value,String cluse,StringBuffer queryBuffer){
		if(value instanceof String){
			queryBuffer.append(cluse).append(" ").append(key).append(" = '").append(value).append("'");
		}else{
			queryBuffer.append(cluse).append(" ").append(key).append(" = ").append(value);
		}
		return queryBuffer;
	}

	private Criteria getCriteriaWithRestriction(Class<E> entityClass,Map<String, Object> propertyNameValues){
		LOG.debug("getCriteriaWithRestriction  start");
		LOG.debug("propertyNameValues :"+propertyNameValues);
		Session session = sessionFactory.openSession();
		Criteria criteria=session.createCriteria(entityClass);
		if(null != propertyNameValues && !propertyNameValues.isEmpty()){
			criteria.add(Restrictions.allEq(propertyNameValues));
		}
		LOG.debug("getCriteriaWithRestriction  end");
		return criteria;
	}

	@SuppressWarnings("unchecked")
	public E processUniqueQuery(String queryKey,String[] keys,Object[] values,QueryOperationType queryOperationType,QueryType queryType,String dynamicQuery) throws DaoException{
		LOG.debug("processQuery(String queryKey,Map<String,Object> parameterMap,int queryType) START ");
		LOG.debug("QUERY KEY :"+queryKey);
		LOG.debug("QUERY TYPE :"+queryType);
		LOG.debug("KEYS :"+keys);
		LOG.debug("VALUES :"+values);
		LOG.debug("DYNAMIC QUERY :"+dynamicQuery);
		LOG.debug("QueryOperationType:"+queryOperationType);
		LOG.debug("QueryType:"+queryType);
		Transaction transaction=null;
		E entity=null;
		String queryVal=(null == dynamicQuery || "".intern() == dynamicQuery.intern()) ? this.hqlMap.get(queryKey) : dynamicQuery;
		if(null != queryVal){
			Query query=null;
			Session session = sessionFactory.getCurrentSession();
			try{
				transaction=session.beginTransaction();
				switch(queryType){
				case HQL:
					query= session.createQuery(queryVal);
					break;
				case SQL:
					query=session.createSQLQuery(queryVal);
					query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
					break;
				}
				if(null != keys){
					for(int i=0;i<keys.length;i++){
						query.setParameter(keys[i].toString(), values[i]);
					}
				}
				switch(queryOperationType){
				case SELECT:
					entity=(E) query.uniqueResult();
					break;
				case INSERT_UPDATE_DELETE:
					int rowsModified=query.executeUpdate();
					LOG.debug("No of rows affected : "+rowsModified);
					break;
				}
				if(!transaction.wasCommitted()){
					transaction.commit();
				}
			}catch (HibernateException e) {
				LOG.error(e.getMessage(), e);
				transaction.rollback();
				throw new DaoException(DaoFaultCodes.HIBERNATE_GET_ENTITY_ERROR, e);
			}catch (PersistenceException e) {
				LOG.error(e.getMessage(), e);
				transaction.rollback();
				throw new DaoException(DaoFaultCodes.HIBERNATE_GET_ENTITY_ERROR, e);
			}finally{
				if(session.isOpen()){
					session.close();
				}
			}
		}
		LOG.debug("processQuery(String queryKey,Map<String,Object> parameterMap,int queryType) END ");
		return entity;
	}

	@Override
	public String constructQuery(Map<String,String> queryInput){
		StringBuffer buffer=new StringBuffer();
		if(null != queryInput && !queryInput.isEmpty()){
			Set<String> keySet=queryInput.keySet();
			for(String queryKey : keySet){
				if(hqlMap.containsKey(queryKey)){
					buffer.append(hqlMap.get(queryKey));
				}else if("CUSTOM_QUERY".intern() == queryKey.intern()){
					buffer.append(" ");
					buffer.append(queryInput.get(queryKey));
				}
			}
		}
		return buffer.toString();
	}

	@SuppressWarnings("unchecked")
	public List<E> processQuery(String queryKey,String[] keys,Object[] values,QueryOperationType queryOperationType,QueryType queryType,String dynamicQuery) throws DaoException{
		LOG.debug("processQuery(String queryKey,Map<String,Object> parameterMap,int queryType) START ");
		LOG.debug("QUERY KEY :"+queryKey);
		LOG.debug("QUERY TYPE :"+queryType);
		LOG.debug("KEYS :"+keys);
		LOG.debug("VALUES :"+values);
		LOG.debug("DYNAMIC QUERY :"+dynamicQuery);
		LOG.debug("QueryOperationType:"+queryOperationType);
		LOG.debug("QueryType:"+queryType);
		Transaction transaction=null;
		List<E> list=null;
		String queryVal=(null == dynamicQuery || "".intern() == dynamicQuery.intern()) ? this.hqlMap.get(queryKey) : dynamicQuery;
		if(null != queryVal){
			Query query=null;
			Session session = sessionFactory.getCurrentSession();
			try{
				transaction=session.beginTransaction();
				switch(queryType){
				case HQL:
					query= session.createQuery(queryVal);
					break;
				case SQL:
					query=session.createSQLQuery(queryVal);
					query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
					break;
				}
				if(null != keys){
					for(int i=0;i<keys.length;i++){
						query.setParameter(keys[i].toString(), values[i]);
					}
				}
				switch(queryOperationType){
				case SELECT:
					list=query.list();
					break;
				case INSERT_UPDATE_DELETE:
					int rowsModified=query.executeUpdate();
					LOG.debug("No of rows affected : "+rowsModified);
					break;
				}
				if(!transaction.wasCommitted()){
					transaction.commit();
				}
			}catch (HibernateException e) {
				LOG.error(e.getMessage(), e);
				transaction.rollback();
				throw new DaoException(DaoFaultCodes.HIBERNATE_GET_ENTITY_ERROR, e);
			}catch (PersistenceException e) {
				LOG.error(e.getMessage(), e);
				transaction.rollback();
				throw new DaoException(DaoFaultCodes.HIBERNATE_GET_ENTITY_ERROR, e);
			}finally{
				if(session.isOpen()){
					session.close();
				}
			}
		}
		LOG.debug("LIST:"+list);
		LOG.debug("processQuery(String queryKey,Map<String,Object> parameterMap,int queryType) END ");
		return list;
	}

	public Map<String,ClassMetadata> getDBMetaData(){
		Map<String, ClassMetadata>  metaDataMap = (Map<String, ClassMetadata>) sessionFactory.getAllClassMetadata();
		return metaDataMap;
	}

	/*	public Map<Object,Object> getDBMetaData(String entityName){
		Map<Object,Object> returnMap=new HashMap<>();
		AbstractEntityPersister persister=getEntityPersisterByEntityName(entityName);
		if(null != persister){
			Class<?> classIns=persister.getClassMetadata().getMappedClass();
			int maxEntityId=getMaxEntityId(classIns,persister.getIdentifierPropertyName());
			String tableEntityKey=persister.getIdentifierColumnNames()[0];
			String tableName=persister.getTableName();
			Map<Object,Object> inputMap=new LinkedHashMap<>();
			List<String> columnList=getTableColumnNameList(entityName);
			for(String propertyName : columnList){
				String columnName = persister.getPropertyColumnNames(propertyName)[0];
				Type columnsType=persister.getPropertyType(propertyName);
				CollectionUtil.fillMapData(inputMap, propertyName, columnName, columnsType);
			}
			returnMap.put(tableName, inputMap);
			returnMap.put(StringUtil.concateString("EntityColumn~~", tableEntityKey), maxEntityId);
		}
		return returnMap;
	}*/

	public void bulkSQLNativeOperation(List<String> queryList){
		Session session=null;
		Transaction transaction=null;
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			session.doWork(new Work() {
				@Override
				public void execute(Connection conn) throws SQLException {
					PreparedStatement pstmt = null;
					try{
						for(String query : queryList){
							pstmt = conn.prepareStatement(query);
							pstmt.executeUpdate();
						}
					}
					finally{
						pstmt .close();
					}                                
				}
			});
			if(!transaction.wasCommitted()){
				transaction.commit();
			}
		}catch (HibernateException e) {
			LOG.error(e.getMessage(), e);
			transaction.rollback();
		}catch (PersistenceException e) {
			LOG.error(e.getMessage(), e);
			transaction.rollback();
		}finally{
			if(session.isOpen()){
				session.close();
			}
		}
	}

	public Connection getConnection(){
		SessionImpl sessionImpl = (SessionImpl) sessionFactory.openSession();
		return sessionImpl.connection();
	}



}