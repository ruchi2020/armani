/**
 * @copyright (c) 1998-2003 Retek Inc.  All Rights Reserved.
 */

package com.chelseasystems.cr.config;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.ResourceBundle;

import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.util.INIFile;
import com.chelseasystems.cr.util.Trace;

import org.w3c.dom.Element;
import java.util.*;
import java.text.*;
import com.chelseasystems.cr.util.*;

/**
 */
public class ConfigText implements IConfigFile, Serializable {

	static final long serialVersionUID = -2004440665126842877L;
	private String configFile;
	INIFile ini;

	/**
	 * @param    String configFile
	 */
	public ConfigText(String configFile) {
		this.configFile = configFile;
		try {
			ini = new INIFile(configFile, true);
		} catch (Exception ex) {
		}
	}

	/**
	 * convertFileSeperators
	 * Converts the file seperator to the correct one for the OS
	 * @param aString string to be converted
	 * @return String the string that has been converted
	 */
	private String convertFileSeperators(String aString) {
		String sTemp = aString.replace('/', File.separatorChar);
		return (sTemp.replace('\\', File.separatorChar));
	}

	/**
	 * getObject
	 * @param aKey A key to the properties file
	 * @return Object an instance of the object based on a key
	 */
	public Object getObject(String aKey) {
		try {
			//      INIFile ini = new INIFile(configFile, false);
			Class cls = Class.forName(ini.getValue(aKey).trim());
			Object obj = cls.newInstance();
			return (obj);
		} catch (Exception ex) {
			//Trace.ex(ex);
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getObject", "Unable to getObject() for Key: " + aKey, "Make sure config file entry is correct", LoggingServices.INFO);
			return (null);
		}
	}

	/**
	 * getString
	 * @param aKey A key to the properties file
	 * @return String a String based on a key
	 */
	public String getString(String aKey) {
		try {
			//      INIFile ini = new INIFile(configFile, false);
			String sResult = ini.getValue(aKey).trim();
			return (sResult);
		} catch (Exception ex) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getString", "Unable to getString() for Key: " + aKey, "Make sure config file entry is correct", LoggingServices.INFO);
			return (null);
		}
	}

	/**
	 * getFileName string
	 * @param aKey A key in the config file that points to a filename that you wish to read
	 * @return String a String of a filename with the file seperators the correct
	 * way for the os to read the file
	 */
	public String getFileName(String aKey) {
		try {
			//      INIFile ini = new INIFile(configFile, false);
			String sResult = ini.getValue(aKey).trim();
			sResult = convertFileSeperators(sResult);
			return (sResult);
		} catch (Exception ex) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getFileName", "Unable to getFileName() for Key: " + aKey, "Make sure config file entry is correct", LoggingServices.INFO);
			return (null);
		}
	}

	/**
	 * getInteger
	 * @param aKey A key to the properties file
	 * @return Integer an Integer based on a key
	 */
	public Integer getInteger(String aKey) {
		try {
			//      INIFile ini = new INIFile(configFile, false);
			return (new Integer(ini.getValue(aKey).trim()));
		} catch (Exception ex) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getInteger", "Unable to getInteger() for Key: " + aKey, "Make sure config file entry is correct", LoggingServices.INFO);
			return (null);
		}
	}

	/**
	 * getInt
	 * @param aKey A key to the properties file
	 * @return an int based on a key
	 */
	public int getInt(String aKey) {
		try {
			//      INIFile ini = new INIFile(configFile, false);
			return (new Integer(ini.getValue(aKey).trim())).intValue();
		} catch (Exception ex) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getInt", "Unable to getInt() for Key: " + aKey, "Make sure config file entry is correct", LoggingServices.INFO);
			return (0);
		}
	}

	/**
	 * getLong
	 * @param aKey A key to the properties file
	 * @return Long a Long based on a key
	 */
	public Long getLong(String aKey) {
		try {
			//      INIFile ini = new INIFile(configFile, false);
			return (new Long(ini.getValue(aKey).trim()));
		} catch (Exception ex) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getLong", "Unable to getLong() for Key: " + aKey, "Make sure config file entry is correct", LoggingServices.INFO);
			return (null);
		}
	}

	/**
	 * getLong
	 * @param aKey A key to the properties file
	 * @return Double a Double based on a key
	 */
	public Double getDouble(String aKey) {
		try {
			//      INIFile ini = new INIFile(configFile, false);
			return (new Double(ini.getValue(aKey).trim()));
		} catch (Exception ex) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getDouble", "Unable to getDouble() for Key: " + aKey, "Make sure config file entry is correct", LoggingServices.INFO);
			return (null);
		}
	}

	/**
	 * @return
	 */
	public Enumeration getKeys() {
		try {
			//      INIFile ini = new INIFile(configFile, false);
			return (ini.getKeys());
		} catch (Exception ex) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getKey", "Unable to getKeys()", "Make sure config file name is correct", LoggingServices.INFO);
			return (null);
		}
	}

	/**
	 * @param key
	 * @param value
	 * @exception ValueException
	 */
	public void setString(String key, String value) throws ValueException {
		try {
			//      INIFile ini = new INIFile(configFile, false);
			ini.writeEntry(key, value);
		} catch (Exception ex) {
			throw new ValueException("Unable to set the string. Reason: " + ex.getMessage());
		}
	}

	/**
	 * This method will only accept dates in shot date format.  In US this format
	 * would be "MM/dd/yyyy".  This value is resourced for i18n.
	 * @see java.text.SimpleDateFormat
	 * @see com.chelseasystems.cr.util.ResourceManager#getResourceBundle
	 * @param key
	 * @param value
	 * @exception ValueException
	 */
	public void setDate(String key, String value) throws ValueException {
		ResourceBundle res = null;
		try {
			try {
				res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
				if (res != null) {
					SimpleDateFormat fmt = new SimpleDateFormat(res.getString("MM/dd/yyyy"));
					fmt.setLenient(false);
					fmt.parse(value);
				}
			} catch (MissingResourceException ex) {
				// ignore this
			}
			//      INIFile ini = new INIFile(configFile, false);
			ini.writeEntry(key, value);
		} catch (Exception ex) {
			throw new ValueException("Unable to set the date. Reason: " + ex.getMessage());
		}
	}

	/**
	 * @param key
	 * @param value
	 * @exception ValueException
	 */
	public void setDouble(String key, String value) throws ValueException {
		try {
			Double aDouble = new Double(value);
			//      INIFile ini = new INIFile(configFile, false);
			ini.writeEntry(key, value);
		} catch (Exception ex) {
			throw new ValueException("Unable to set the date. Reason: " + ex.getMessage());
		}
	}

	/**
	 * @param key
	 * @param value
	 * @exception ValueException
	 */
	public void setLong(String key, String value) throws ValueException {
		try {
			Long aLong = new Long(value);
			//      INIFile ini = new INIFile(configFile, false);
			ini.writeEntry(key, value);
		} catch (Exception ex) {
			throw new ValueException("Unable to set the date. Reason: " + ex.getMessage());
		}
	}

	/**
	 * @param key
	 * @param value
	 * @exception ValueException
	 */
	public void setInteger(String key, String value) throws ValueException {
		try {
			Integer aInteger = new Integer(value);
			//      INIFile ini = new INIFile(configFile, false);
			ini.writeEntry(key, value);
		} catch (Exception ex) {
			throw new ValueException("Unable to set the date. Reason: " + ex.getMessage());
		}
	}

	/**
	 * @param key
	 * @exception ValueException
	 */
	public void removeKeyValue(String key) throws ValueException {
		throw new ValueException("This implementation does not provide this functionality.");
	}

	/**
	 * @param key
	 * @param isSystem
	 * @exception ValueException
	 */
	public void setSystem(String key, boolean isSystem) throws ValueException {
		throw new ValueException("This implementation does not provide this functionality.");
	}

	/**
	 * @param key
	 * @return
	 * @exception ValueException
	 */
	public boolean isSystem(String key) throws ValueException {
		throw new ValueException("This implementation does not provide this functionality.");
	}

	/**
	 * @return
	 */
	public String[] getDisplay() {
		return (new String[] { "Unable to display key/value Pairs.  Use text editor." });
	}

	/**
	 * @return
	 */
	public Element getElement() {
		LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getConfigElement", "method is for XML config file only", "method is for XML config file only", LoggingServices.INFO,
				new Exception("method is for XML config file only"));
		return (null);
	}

	/**
	 * @param element
	 * @exception ValueException
	 */
	public void setElement(Element element) throws ValueException {
		throw new ValueException("ConfigObject.setConfigElement should use on XML config file only.");
	}
}
