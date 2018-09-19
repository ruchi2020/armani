package com.ga.fs.fsbridge.utils;

import com.ga.fs.fsbridge.object.ARMFSBridgeObject;

public class MessageTypeCode {
	
	public static final String MTC_TRANSACTION = new ConfigurationManager().
	getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath, "MTC_TRANSACTION");
	
	public static final String MTC_POS_EVENT = new ConfigurationManager().
	getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath, "MTC_POS_EVENT");
	
	public static final String MTC_CONFIGURATION_MESSAGE = new ConfigurationManager().
	getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath, "MTC_CONFIGURATION_MESSAGE");
	

}
