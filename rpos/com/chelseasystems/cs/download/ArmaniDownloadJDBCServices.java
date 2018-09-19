/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.download;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cs.dataaccess.*;
import com.chelseasystems.cs.pos.AlterationItemGroup;
import com.chelseasystems.cs.config.ArmConfig;
import com.chelseasystems.cs.config.ArmDiscountRule;
import com.chelseasystems.cs.config.ArmPayConfigDetail;
import com.chelseasystems.cs.config.ArmPayPlanConfigDetail;
import com.chelseasystems.cs.config.ArmTaxRateConfig;


/**
 * put your documentation comment here
 */
public class ArmaniDownloadJDBCServices extends ArmaniDownloadServices {
	private ArmAlternGrpDAO armAlternGrpDAO;
	private ArmCfgDetailDAO armCfgDetailDAO;
	private ArmDscRuleDAO armDscRuleDAO;
	private ArmPayCfgDetailDAO armPayCfgDetailDAO;
	private ArmTaxRateExcDAO armTaxRateExcDAO;

	/**
	 * put your documentation comment here
	 */
	public ArmaniDownloadJDBCServices() {
		ConfigMgr configMgr = new ConfigMgr("jdbc.cfg");
		armAlternGrpDAO = (ArmAlternGrpDAO) configMgr.getObject("ARM_ALTERN_GRP_DAO");
		armCfgDetailDAO = (ArmCfgDetailDAO) configMgr.getObject("ARM_CONFIG_DAO");
		armPayCfgDetailDAO = (ArmPayCfgDetailDAO) configMgr.getObject("ARM_PAY_CONFIG_DAO");
		armDscRuleDAO = (ArmDscRuleDAO) configMgr.getObject("ARM_DSC_RULE_DAO");
		armTaxRateExcDAO = (ArmTaxRateExcDAO)configMgr.getObject("ARM_TAX_RATE_EXC_DAO");
	}

	/**
	 * put your documentation comment here
	 * @param sCountry
	 * @param sLanguage
	 * @return
	 * @exception Exception
	 */
	public ArmConfig getConfigByCountryAndLanguage(String sCountry, String sLanguage) throws Exception {
		try {
			return armCfgDetailDAO.selectByCountryAndLanguage(sCountry, sLanguage);
		} catch (Exception exception) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getConfigByCountryAndLanguage", "Exception", "See Exception", LoggingServices.MAJOR, exception);
			throw exception;
		}
	}

	/**
	 * put your documentation comment here
	 * @param sCountry
	 * @param sLanguage
	 * @return ArmPayPlanConfigDetail
	 * @exception Exception
	 */
	public ArmPayConfigDetail[] getPayConfigByCountryAndLanguage(String sCountry, String sLanguage) throws Exception {
		try {
			return armPayCfgDetailDAO.selectByCountryAndLanguage(sCountry, sLanguage);
		} catch (Exception exception) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getPayConfigByCountryAndLanguage", "Exception", "See Exception", LoggingServices.MAJOR, exception);
			throw exception;
		}
	}

	/**
	 * put your documentation comment here
	 * @return
	 * @exception Exception
	 */
	public AlterationItemGroup[] getAllAlterationItemGroups() throws Exception {
		try {
			return armAlternGrpDAO.selectAllGroups();
		} catch (Exception exception) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getAllAlterationItemGroups", "Exception", "See Exception", LoggingServices.MAJOR, exception);
			throw exception;
		}
	}

	/**
	 * put your documentation comment here
	 * @param sCountry
	 * @param sLanguage
	 * @return
	 * @exception Exception
	 */
	public AlterationItemGroup[] getAlterationItemGroupsByCountryAndLanguage(String sCountry, String sLanguage) throws Exception {
		try {
			return armAlternGrpDAO.selectByCountryAndLanguage(sCountry, sLanguage);
		} catch (Exception exception) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getAlterationItemGroupsByCountryAndLanguage", "Exception", "See Exception", LoggingServices.MAJOR, exception);
			throw exception;
		}
	}

	/**
	 * put your documentation comment here
	 * @param sCountry
	 * @param sLanguage
	 * @return
	 * @exception Exception
	 */
	public ArmPayPlanConfigDetail[] getPayPlanConfigByCountryAndLanguage(String sCountry, String sLanguage) throws Exception {
		try {
			return armPayCfgDetailDAO.selectPlansByCountryAndLanguage(sCountry, sLanguage);
		} catch (Exception exception) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getPayPlanConfigByCountryAndLanguage", "Exception", "See Exception", LoggingServices.MAJOR, exception);
			throw exception;
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param sCountry
	 * @param sLanguage
	 * @return
	 * @exception Exception
	 */
	public ArmDiscountRule[] getDiscountRuleByCountryAndLanguage(String sCountry, String sLanguage) throws Exception {
		try {
			return armDscRuleDAO.selectDiscountRuleByCountryAndLanguage(sCountry, sLanguage);
		} catch (Exception exception) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getDiscountRuleByCountryAndLanguage", "Exception", "See Exception", LoggingServices.MAJOR, exception);
			throw exception;
		}
	}
	
	//Added by Vivek Mishra for PCR_TaxExceptionRulesImplementationChanges_US
	//start
	/**
	 * put your documentation comment here
	 * @param sState
	 * @param sZipcode
	 * @return
	 * @exception Exception
	 */
	public ArmTaxRateConfig[] getExceptionTaxDetailByStateAndZipcode(String sState, String sZipcode) throws Exception {
		try {
			
			return armTaxRateExcDAO.getExceptionTaxDetailByStateAndZipcode(sState, sZipcode);
		} catch (Exception exception) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getExceptionTaxDetailByStateAndZipcode", "Exception", "See Exception", LoggingServices.MAJOR, exception);
			throw exception;
		}
	}
	//end
}
