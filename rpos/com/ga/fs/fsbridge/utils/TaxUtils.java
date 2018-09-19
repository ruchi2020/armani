package com.ga.fs.fsbridge.utils;

import com.ga.fs.fsbridge.ARMFSBridge;
import com.ga.fs.fsbridge.object.ARMFSBridgeObject;

public class TaxUtils {
	
	public static double getTaxableAmount(double total){
		
		double taxPercent = Double.parseDouble(new ConfigurationManager()
						.getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath, "TAX_PERCENT"));
		double taxableAmount = 0.0;
		taxableAmount = total - ((total / 100)*taxPercent);
		
		return taxableAmount;
		
	}
	
	
	public static double getTaxAmount(double total){
		
		double taxPercent = Double.parseDouble(new ConfigurationManager()
						.getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath, "TAX_PERCENT"));
		double taxAmount = 0.0;
		taxAmount = ((total / 100)*taxPercent);
		
		return taxAmount;
		
	}

}
