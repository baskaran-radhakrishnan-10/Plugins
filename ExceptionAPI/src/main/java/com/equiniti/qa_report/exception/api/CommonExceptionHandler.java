package com.equiniti.qa_report.exception.api;

/*
 * Class: JCCommonExceptionHandler
 *
 * Comments for Developers Only:
 *
 ******************************************************/


import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.equiniti.qa_report.exception.api.util.CommonFileUtil;

/**
 * A class used for JCCommonExceptionHandler
 * 
 */
public class CommonExceptionHandler {

	private static Logger LOG = Logger.getLogger(CommonExceptionHandler.class.getName());

	private static String errorProperitesHome = null;	

	/**
	 * This object contains all the errors of global scope.
	 */
	private static HashMap<String, Properties> globalErrorsMap;

	private static Map<String,String> errorMessagesMap;  

	/**
	 * This object contains all the errors of marketplace level scope.
	 */
	private static HashMap<?, ?> customErrorsMap;

	private static final String defaultLocaleString = "en";

	private static boolean loadedProperties = false;

	private static final String MODULES_LIST_FILE_NAME = "errorpropertyfileslist_<<LOCALE>>.properties";

	private static final String ERROR_PROPERTIES_FILE_NAME = "Errors_<<LOCALE>>.properties";


	public Map<String, String> getErrorMessagesMap() {
		return errorMessagesMap;
	}

	public static void main(String args[]){
		getErrorMessage("DAO.001");
	}


	/**
	 * Returns error message for a given error code from loaded properties
	 * files. Returns blank message if the errorcode is not found.
	 * 
	 * @param errorKey
	 *            error code for which error messages to be found.
	 * @return error message
	 */
	public static String getErrorMessage(String errorKey) {
		return getErrorMessage(errorKey, Locale.ENGLISH);
	}


	/**
	 * Returns error message for a given error code from loaded properties
	 * files. Returns blank message if the errorcode is not found.
	 * 
	 * @param errorKey
	 * @param marketplaceAcronym
	 * @param locale
	 * @return
	 */
	public static String getErrorMessage(String errorKey, Locale locale) {
		if (!loadedProperties) {
			initProperties();
		}
		String errorMessage = null;
		boolean foundErrorMessage = false;
		if (!foundErrorMessage) {
			if (null != errorMessagesMap  && errorMessagesMap.containsKey(errorKey)) {
				errorMessage=errorMessagesMap.get(errorKey);
				foundErrorMessage=true;
			}
		}
		if (errorMessage == null) {
			errorMessage = " ";
		}
		return errorMessage;
	}

	/**
	 * Initializes all the error properties
	 */
	private static void initProperties() {
		String modulesFile = "properties/error.properties";
		loadGlobalModules(modulesFile, defaultLocaleString);
		loadedProperties = true;
	}

	/**
	 * This method loads all the error properties in the global scope
	 * 
	 * @param modulesFile
	 * @param localeString
	 */
	private static void loadGlobalModules(String modulesFile, String localeString) {
		try {
			Map<String,String> tempMap=CommonFileUtil.getPropertiesMap(modulesFile,true);
			if(null != tempMap){
				errorMessagesMap=tempMap;
			}
		} finally {
			try {
			} catch (Exception ioe) {
				LOG.error("Error in closing  : ", ioe);
			}
		}
		if (LOG.isDebugEnabled()){
			LOG.debug("loadGlobalModules() : End");
		}
	}

	/**
	 * @param errorKey
	 * @return DetailedErrorMessage as String
	 */
	public static String getDetailedErrorMessage(String errorKey) {
		return getDetailedErrorMessage(errorKey, Locale.ENGLISH);
	}

	/**
	 * @param errorKey
	 *            as String
	 * @param currentLocale
	 *            as java.util.Locale
	 * @return DetailedErrorMessage as String
	 */
	public static String getDetailedErrorMessage(String errorKey,java.util.Locale currentLocale) {

		if (LOG.isDebugEnabled()){
			LOG.debug("getDetailedErrorMessage message for currentLocale "+ errorKey);
		}

		String message = getErrorMessage(errorKey, currentLocale);
		StringBuffer msgBuffer = new StringBuffer();
		String corrActionKey = errorKey + ".CA";
		String corrAction = null;

		if (message != null) {

			msgBuffer.append(errorKey).append(" ");
			String type = getErrorType(errorKey);

			if (type != null) {
				msgBuffer.append(type).append(" error. ");
			}

			msgBuffer.append(message + " ");

			// TODO
			corrAction = getCorrectiveAction(corrActionKey, null,currentLocale); 
			if (corrAction != null)
				msgBuffer.append(corrAction);

		}
		return msgBuffer.toString();
	}

	/**
	 * This method gets the corrective action related to an error code
	 * @param correctiveActionKey
	 * @param locale
	 * @param marketplaceAcronym
	 * @return Corrective Action. If not found or empty, returns null
	 */
	public static String getCorrectiveAction(String correctiveActionKey, String marketplaceAcronym, Locale locale) {

		if (LOG.isDebugEnabled()){
			LOG.debug("getCorrectiveAction() : Start");
		}

		String correctiveAction = null;
		String correctiveActionPointer = null;
		Properties properties = null;
		String localeString = null;

		if (locale != null) {
			localeString= locale.getLanguage();
		} else {
			localeString = defaultLocaleString;
		}

		//properties = (Properties) errorMessagesMap.get(localeString);

		if (null != errorMessagesMap  && errorMessagesMap.containsKey(correctiveActionKey)) {
			correctiveAction=errorMessagesMap.get(correctiveActionKey);
		}
		
		/*if (properties != null && properties.getProperty(correctiveActionKey) != null) {
			correctiveActionPointer = properties.getProperty(correctiveActionKey);

			if (correctiveActionPointer != null) {
				correctiveAction = getErrorMessage(correctiveActionPointer, locale);
			}
		}*/

		if (" ".equals(correctiveAction)) {
			correctiveAction = null;
		}

		if (LOG.isDebugEnabled()){
			LOG.debug ("Corrective action :" + correctiveAction);	 	
			LOG.debug("getCorrectiveAction() : End");	 		
		}

		return correctiveAction;
	}

	/**
	 * @param errorKey
	 *            as String
	 * @return ErrorType as String
	 */
	public static String getErrorType(String errorKey) {
		return getErrorType(errorKey, Locale.ENGLISH);
	}

	/**
	 * @param errorKey
	 * @param currentLocale
	 * @return ErrorType as String
	 */
	public static String getErrorType(String errorKey, java.util.Locale currentLocale) {
		String typeCode = "Type." + errorKey.substring(0, 2);

		if (LOG.isDebugEnabled()){
			LOG.debug("TypeCode: " + typeCode);
		}

		String type = null;
		type = getErrorMessage(typeCode, currentLocale);

		if (LOG.isDebugEnabled()){
			LOG.debug("Type: " + type);
		}

		return type;
	}

	/**
	 * @param corrActionKey
	 * @return String
	 */
	private static String getCorrectiveAction(String corrActionKey) {
		return getCorrectiveAction(corrActionKey, null, Locale.ENGLISH);
	}

	/**
	 * Added for Globalization
	 * @param corrActionKey
	 * @param errorsProps
	 * @return String
	 */
	private static String getCorrectiveAction(String corrActionKey, Properties errorsProps) {

		String corrAction = null;
		String corrActionPointer = null;

		if (errorsProps.containsKey(corrActionKey))
			corrActionPointer = (String) errorsProps.get(corrActionKey);
		if (corrActionPointer != null)
			corrAction = (String) errorsProps.get(corrActionPointer);

		return corrAction;
	}


	/**
	 * @return ErrorPropertiesHome as String
	 */
	public static String getErrorPropertiesHome() /* throws Exception */{
		if (errorProperitesHome != null) {
			return errorProperitesHome;
		} else {
			try {
				String configHome = System.getProperty("PP_CONFIG_HOME");
				File currentPath = new File (configHome);
				if (LOG.isDebugEnabled()){
					LOG.debug("errorProperitesHome: "+currentPath.getAbsolutePath());
				}

				errorProperitesHome = currentPath.getAbsolutePath();				

			} catch (Exception ex) {
				LOG.error("Error in checking the ERROR_PROPERTIES_HOME: ", ex);
			}
		}

		return errorProperitesHome;
	}

}
