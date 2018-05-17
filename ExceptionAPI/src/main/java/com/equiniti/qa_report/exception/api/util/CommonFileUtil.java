package com.equiniti.qa_report.exception.api.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

public class CommonFileUtil {
	
	private static final Logger LOG = Logger.getLogger(CommonFileUtil.class.getName());

	public  static void updatePropertyFile(Map<String, String> configMap,String filePath){
		Properties prop=null;
		FileOutputStream out=null;
		try {
			Map<String,String> map=getInitDBCredentialsProperties();
			for(String key : map.keySet()){
				if(!configMap.containsKey(key)){
					configMap.put(key, map.get(key));
				}
			}
			prop = new Properties();
			out = new FileOutputStream(filePath);
			prop.putAll(configMap);
			prop.store(out, null);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Map<String,String> getPropertiesMap(String filePath,boolean isProjectResource){
		Map<String,String> map=null;
		FileInputStream fileInput=null;
		try {
			InputStream is =getInputStream(filePath,isProjectResource);
			Properties properties = new Properties();
			properties.load(is);
			map=new HashMap<>();
			translatePropertyToMap(map, properties);
		} catch (FileNotFoundException e) {
			LOG.error(e.getMessage(), e);
			e.printStackTrace();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			e.printStackTrace();
		}finally{
			if(null  != fileInput){
				try {
					fileInput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}

	public static Map<String,String> getHqlMapFromQueryFile(){
		Map<String,String> hqlMap=null;
		hqlMap=getPropertiesMap("properties/hql-queries.properties",true);
		return hqlMap;
	}

	public static Map<String,String> getAnnotatedClassProperties(){
		Map<String,String> map=null;
		map=getPropertiesMap("properties/annotated-entities.properties",true);
		return map;
	}
	
	public static Map<String,String> getInitDBCredentialsProperties(){
		Map<String,String> map=null;
		map=getPropertiesMap("properties/db-credentials.properties",true);
		return map;
	}

	public static void translatePropertyToMap(Map<String,String> map,Properties properties){
		Set<Entry<Object, Object>> set=properties.entrySet();
		for(Entry<Object,Object> entry : set){
			map.put(entry.getKey().toString(), entry.getValue().toString());
		}
	}
	
	public static InputStream getInputStream(String filePath,boolean isProjectResource) throws FileNotFoundException{
		InputStream io=null;
		if(isProjectResource){
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			io=classloader.getResourceAsStream(filePath);
		}else{
			File externalFile=new File(filePath); 
			io=new FileInputStream(externalFile);
		}
		
		return io;
	}
	
}
