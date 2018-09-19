/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.download;

/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 08-24-2005 | Manpreet  | N/A       |Download services                                   |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.pos.AlterationItemGroup;
import com.chelseasystems.cs.config.ArmConfig;
import com.chelseasystems.cs.config.ArmDiscountRule;
import com.chelseasystems.cs.config.ArmPayConfigDetail;
import com.chelseasystems.cs.config.ArmPayPlanConfigDetail;
import com.chelseasystems.cs.config.ArmTaxRateConfig;

/**
 * put your documentation comment here
 */
public abstract class ArmaniDownloadServices {
  private static ArmaniDownloadServices armDwnLdSrvsCurrent = null;

  /**
   * put your documentation comment here
   * @return
   */
  public static ArmaniDownloadServices getCurrent() {
    if (null == armDwnLdSrvsCurrent) {
      System.out.println("Automatically setting current implementation of ArmaniDownloadServices.");
      ConfigMgr config = new ConfigMgr("armaniDownload.cfg");
      Object obj = config.getObject("ARM_DWNLD_SERVICES_IMPL");
      armDwnLdSrvsCurrent = (ArmaniDownloadServices)obj;
    }
    return armDwnLdSrvsCurrent;
  }

  /**
   * put your documentation comment here
   * @param aService
   */
  public static void setCurrent(ArmaniDownloadServices aService) {
    System.out.println("Setting current implementation of ArmaniDownloadServices.");
    armDwnLdSrvsCurrent = aService;
  }

  /**
   * put your documentation comment here
   * @return
   * @exception Exception
   */
  public abstract AlterationItemGroup[] getAllAlterationItemGroups()
      throws Exception;

  /**
   * put your documentation comment here
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception Exception
   */
  public abstract AlterationItemGroup[] getAlterationItemGroupsByCountryAndLanguage(String sCountry
      , String sLanguage)
      throws Exception;

  /**
   * put your documentation comment here
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception Exception
   */
  public abstract ArmConfig getConfigByCountryAndLanguage(String sCountry, String sLanguage)
      throws Exception;

  /**
   * put your documentation comment here
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception Exception
   */
  public abstract ArmPayConfigDetail[] getPayConfigByCountryAndLanguage(String sCountry
      , String sLanguage)
      throws Exception;

 public abstract ArmPayPlanConfigDetail[] getPayPlanConfigByCountryAndLanguage(String sCountry
     , String sLanguage)
     throws Exception;

  /**
   * put your documentation comment here
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception Exception
  */
  public abstract ArmDiscountRule[] getDiscountRuleByCountryAndLanguage(String sCountry, String sLanguage)
     throws Exception;
  
  //Added by Vivek Mishra for PCR_TaxExceptionRulesImplementationChanges_US
  //start
  
  /**
   * put your documentation comment here
   * @param sState
   * @param sZipcode
   * @return
   * @exception Exception
   */
  public abstract ArmTaxRateConfig[] getExceptionTaxDetailByStateAndZipcode(String sState, String sZipcode)
      throws Exception;
  
  //end

}

