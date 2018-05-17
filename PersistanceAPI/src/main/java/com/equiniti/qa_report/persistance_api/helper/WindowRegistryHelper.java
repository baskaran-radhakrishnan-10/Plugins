package com.equiniti.qa_report.persistance_api.helper;

import org.apache.log4j.Logger;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinReg;



public class WindowRegistryHelper {

	private static final Logger LOG=Logger.getLogger(WindowRegistryHelper.class);

	private static final String REGISTRY_KEY_PATH="SOFTWARE\\MADRONE\\KEY";

	public static void persistLicenseKeyInRegistry(String keyValue){
		Advapi32Util.registryCreateKey(WinReg.HKEY_CURRENT_USER, REGISTRY_KEY_PATH);
		Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, REGISTRY_KEY_PATH, PersistanceConstants.PSWD_KEY, keyValue);
	}

	public static String getPswdKeyFromRegistry() {
		String registryValue=null;
		try{
			registryValue=Advapi32Util.registryGetStringValue(WinReg.HKEY_CURRENT_USER, REGISTRY_KEY_PATH,  PersistanceConstants.PSWD_KEY);
		}catch(Win32Exception e){
			if(e.getMessage().indexOf("The system cannot find the file specified") != -1){
			}
		}
		return registryValue;
	}
	
}
