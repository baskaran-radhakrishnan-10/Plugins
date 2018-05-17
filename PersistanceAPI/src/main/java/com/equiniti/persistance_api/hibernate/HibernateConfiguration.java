package com.equiniti.persistance_api.hibernate;

import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.equiniti.exception.api.exception.HibernateSessionFactoryException;
import com.equiniti.exception.api.util.CommonFileUtil;
import com.equiniti.persistance_api.hibernate.api.HibernateConfigurationAPI;


public class HibernateConfiguration implements HibernateConfigurationAPI{

	private static final Logger LOG = Logger.getLogger(HibernateConfiguration.class.getName());

	private PropertyPlaceholderConfigurer hibernateProperties;

	private SessionFactory sessionFactory=null;

	private String driverClass;

	private String dialect;

	private String mode;

	private String urlPattern;

	private String showSql;

	private String hibernateDDL;

	private String providerClass;

	private String currentSession;

	private String userName;

	private String password;
	
	private Session session;
	
	private String batchSize;
	
	private Map<String,String> hqlMap;
	
	private String annotatedEntitiesFilePath;
	
	private String hqlQueriesFilePath;
	
	private boolean windowsAuthenticationRequired;
	
	private HibernateConfiguration(String driverClass,String dialect,String mode,String url,String showSql,String hibernateDDL,String providerClass,String currentSession,String userName,String password,String batchSize,String annotatedEntitiesFilePath,String hqlQueriesFilePath,String windowsAuthenticationRequired) throws HibernateSessionFactoryException{

		try {
			
			this.annotatedEntitiesFilePath=annotatedEntitiesFilePath;
			
			this.hqlQueriesFilePath=hqlQueriesFilePath;

			this.driverClass=driverClass;

			this.dialect=dialect;

			this.mode=mode;

			this.urlPattern=url;

			this.showSql=showSql;

			this.hibernateDDL=hibernateDDL;

			this.providerClass=providerClass;

			this.currentSession=currentSession;

			this.userName=userName;
			
			this.windowsAuthenticationRequired=Boolean.valueOf(windowsAuthenticationRequired);
			
			this.password=password;
			
			/*EncryptionDecryption crypto;*/
			
			/*try {
				
				//String keyValue = WindowRegistryHelper.getPswdKeyFromRegistry();
				//crypto = new EncryptionDecryption(keyValue);
				this.password="baskiJava10$";
				
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			this.batchSize=batchSize;

			Configuration configuration = new Configuration();

			configuration=getConfigurationWithAnnotationClasses(configuration);

			configuration=getConfigurationWithHibernateProperties(configuration);

			ServiceRegistry serviceRegistry =new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();

			SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

			setSessionFactory(sessionFactory);
			
			setSession(this.sessionFactory.getCurrentSession());
			
			setHqlMap(CommonFileUtil.getPropertiesMap(this.hqlQueriesFilePath,true));
			
		} catch (JDBCConnectionException ex) {
			LOG.error("Exception Occured while creating Hibernate Session Factory "+ex.getMessage(),ex);
			throw new HibernateSessionFactoryException(null, ex);
		}

	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public PropertyPlaceholderConfigurer getHibernateProperties() {
		return hibernateProperties;
	}

	public void setHibernateProperties(PropertyPlaceholderConfigurer hibernateProperties) {
		this.hibernateProperties = hibernateProperties;
	}

	public void setSession(Session session) {
		this.session = session;
	}
	
	public Map<String, String> getHqlMap() {
		return hqlMap;
	}

	public void setHqlMap(Map<String, String> hqlMap) {
		this.hqlMap = hqlMap;
	}
	
	public String getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(String batchSize) {
		this.batchSize = batchSize;
	}

	private Configuration getConfigurationWithAnnotationClasses(Configuration configuration) throws HibernateSessionFactoryException{
		Map<String,String> annotationEntityMap=CommonFileUtil.getPropertiesMap(this.annotatedEntitiesFilePath,true);
		LOG.debug(annotationEntityMap);
		if(null != annotationEntityMap && !annotationEntityMap.isEmpty()){
			Set<String> entitySet=annotationEntityMap.keySet();
			for(String entity : entitySet){
				String className=annotationEntityMap.get(entity);
				LOG.debug("className :"+className);
				try {
					configuration.addAnnotatedClass(Class.forName(className));
				} catch (ClassNotFoundException e) {
					LOG.error("Class Cast Exception Occured"+e.getMessage(), e);
					throw new HibernateSessionFactoryException(null,e);
				}
			}
		}
		return configuration;
	}

	private Configuration getConfigurationWithHibernateProperties(Configuration configuration){
		configuration.setProperty("hibernate.connection.driver_class", driverClass);
		configuration.setProperty("hibernate.dialect", dialect);
		configuration.setProperty("javax.persistence.validation.mode", mode);
		configuration.setProperty("hibernate.connection.url", urlPattern);
		if(!windowsAuthenticationRequired){
			configuration.setProperty("hibernate.connection.username", userName);
			configuration.setProperty("hibernate.connection.password", password);
		}
		configuration.setProperty("hibernate.show_sql", showSql);
		configuration.setProperty("hibernate.hbm2ddl.auto", hibernateDDL);
		configuration.setProperty("hibernate.cache.provider_class", providerClass);
		configuration.setProperty("hibernate.current_session_context_class", currentSession);
		configuration.setProperty("hibernate.jdbc.batch_size", batchSize);
		configuration.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
		configuration.setProperty("hibernate.cache.use_second_level_cache", "true");
		configuration.setProperty("hibernate.cache.use_query_cache", "true");
		configuration.setProperty("net.sf.ehcache.configurationResourceName", "/cache/hibernate-cache.xml");
		return configuration;
	}

	@Override
	public Session getSession() {
		return this.session;
	}

}


