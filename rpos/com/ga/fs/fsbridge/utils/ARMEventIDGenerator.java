package com.ga.fs.fsbridge.utils;

import java.util.Date;

import com.ga.fs.fsbridge.object.ARMFSBridgeObject;

public class ARMEventIDGenerator {
	
	public static String generate(String operatorExternalId, String registerID, Date timeStamp){
		
		String millis = String.valueOf(timeStamp.getTime());
		String id = operatorExternalId+registerID+millis;
		
		return id;
		
	}
	
	public static String generate(Date timeStamp){
		String StoreID = new ConfigurationManager().getConfig(ARMFSBridgeObject.registerCfgFilePath, "STORE_ID");
		String RegisterID = new ConfigurationManager().getConfig(ARMFSBridgeObject.registerCfgFilePath, "REGISTER_ID");
		String millis = String.valueOf(timeStamp.getTime());
		String id = StoreID+"001"+RegisterID+millis;
		
		return id;
		
	}

}
