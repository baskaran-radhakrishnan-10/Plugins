package com.equiniti.persistance_api.hibernate.api;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.equiniti.exception.api.exception.DaoException;
import com.equiniti.persistance_api.consenum.QueryOperationType;
import com.equiniti.persistance_api.consenum.QueryType;

public interface AbstractHibernateDAOAPI<E> {

	public E loadEntity(Class<E> entityClass,int entityKey) throws DaoException;

	public E getEntityByCriteria(Class<E> entityClass,Map<String,Object> restrictionMap) throws DaoException;

	public List<E> getEntityListByCriteria(Class<E> entityClass,Map<String,Object> restrictionMap) throws DaoException;

	public E getEntity(Class<E> entityClass,Map<String,Object> restrictionMap) throws DaoException;

	public List<E> getEntityList(Class<E> entityClass,Map<String,Object> restrictionMap) throws DaoException;

	public void deleteEntityList(Class<E> entityClass,Map<String,Object> restrictionMap) throws DaoException;

	public void deleteEntity(Class<E> entityClass,Map<String,Object> restrictionMap) throws DaoException;

	public int saveEntity(E entityClass) throws DaoException;

	public void saveEntityList(List<E> entityClassList) throws DaoException;

	public void updateEntity(E entityClass) throws DaoException;

	public void updateEntityList(List<E> entityClassList) throws DaoException;

	public List<?> processQuery(String queryKey,String[] keys,Object[] values,QueryOperationType queryOperationType,QueryType queryType,String dynamicQuery) throws DaoException;
	
	public E processUniqueQuery(String queryKey,String[] keys,Object[] values,QueryOperationType queryOperationType,QueryType queryType,String dynamicQuery) throws DaoException;
	
	public void bulkSQLNativeOperation(List<String> queryList);
	
	public Connection getConnection();
	
	public String constructQuery(Map<String, String> queryInput);

	public Map<String, Object> callProcedure(String procedureQuery, Map<String, Object> inParamMap,Map<String, Integer> outParamMap) throws DaoException;

}
