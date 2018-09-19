package com.ga.fs.fsbridge.utils;

import com.ga.fs.fsbridge.object.ARMFSBridgeObject;

public class TransactionTypeCode {
	
	public static final String TTC_TRAINING_TRANSACTION= new ConfigurationManager().
	getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath, "TTC_TRAINING_TRANSACTION");
	
	public static final String TTC_SALES_TRANSACTION = new ConfigurationManager().
	getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath, "TTC_SALES_TRANSACTION");

	public static final String TTC_SIGN_ON_TRANSACTION = new ConfigurationManager().
	getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath, "TTC_SIGN_ON_TRANSACTION");
	
	public static final String TTC_SIGN_OFF_TRANSACTION = new ConfigurationManager().
	getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath, "TTC_SIGN_OFF_TRANSACTION");
	
	public static final String TTC_CASHIER_DECLARATION_TRANSACTION =  new ConfigurationManager().
	getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath, "TTC_CASHIER_DECLARATION_TRANSACTION");
	
	public static final String TTC_CASH_IN = new ConfigurationManager().
	getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath, "TTC_CASH_IN");
	
	public static final String TTC_CASH_OUT =  new ConfigurationManager().
	getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath, "TTC_CASH_OUT");
	
	public static final String TTC_INVOICE_TRANSACTION =  new ConfigurationManager().
	getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath, "TTC_INVOICE_TRANSACTION");
	
	public static final String TTC_ITEM_EXCHANGE =  new ConfigurationManager().
	getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath, "TTC_ITEM_EXCHANGE");
	
	public static final String TTC_PRO_FORMA_INVOICE =  new ConfigurationManager().
	getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath, "TTC_PRO_FORMA_INVOICE");
	
	public static final String TTC_RETURN_TRANSACTION =  new ConfigurationManager().
	getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath, "TTC_RETURN_TRANSACTION");
	
	public static final String TTC_RETURN_WITH_SALES =  new ConfigurationManager().
	getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath, "TTC_RETURN_WITH_SALES");
	
	public static final String TTC_VOID_TRANSACTION =  new ConfigurationManager().
	getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath, "TTC_VOID_TRANSACTION");
	
	public static final String TTC_END_OF_DAY =  new ConfigurationManager().
	getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath, "TTC_END_OF_DAY");
	
	public static final String TTC_END_OF_MONTH =  new ConfigurationManager().
	getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath, "TTC_END_OF_MONTH");
	
	public static final String TTC_END_OF_YEAR =  new ConfigurationManager().
	getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath, "TTC_END_OF_YEAR");
	
	public static final String TTC_INITIAL_FLOAT =  new ConfigurationManager().
	getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath, "TTC_INITIAL_FLOAT");
	
	public static final String TTC_PAY_IN =  new ConfigurationManager().
	getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath, "TTC_PAY_IN");
	
	public static final String TTC_PAY_OUT =  new ConfigurationManager().
	getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath, "TTC_PAY_OUT");
	
	public static final String TTC_RECEIPT_COPY =  new ConfigurationManager().
	getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath, "TTC_RECEIPT_COPY");
	

	
	

	
}
