package com.chelseasystems.cs.dataaccess;

import  java.sql.SQLException;
import  com.chelseasystems.cs.config.ArmTaxRateConfig;

public interface ArmTaxRateExcDAO extends BaseDAO{
	
	/**
	   * @param sState
	   * @param sZipcode
	   * @return
	   * @exception SQLException
	   */
	
	public ArmTaxRateConfig[] getExceptionTaxDetailByStateAndZipcode(String sState, String sZipcode) throws SQLException;
	
	/**Added by anjana to calculate Send sale tax
	   * @param sState
	   * @param sZipcode
	   * @return
	   * @exception SQLException
	   */
	
	
	/*Dec 29, 2016 vishal_k:Added product number as argument for fetching record from ARM_TAX_RATE_EXC with combination of state, zip, catagory,  and product .
	 * because of getting exact tax rate of any item with these combination.
	 * Modified method : vishal Yevale : added process date to validate return result
	 */
	
	public ArmTaxRateConfig selectByZipCityStateForSAP(String sState, String sZipcode, String category, String product, java.sql.Date process_dt) throws SQLException;
	
	public ArmTaxRateConfig selectByZipCityStateForSAP(String sState, String sZipcode, String category) throws SQLException;

	public ArmTaxRateConfig[] getExceptionTaxDetailByStateAndZipcodeCategory(String sState, String sZipcode, String category) throws SQLException;
	
}
