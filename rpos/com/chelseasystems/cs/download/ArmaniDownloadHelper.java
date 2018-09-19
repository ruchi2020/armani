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
import com.chelseasystems.cr.appmgr.IRepositoryManager;
import com.chelseasystems.cs.pos.AlterationItemGroup;
import com.chelseasystems.cs.config.ArmConfig;
import com.chelseasystems.cs.config.ArmDiscountRule;
import com.chelseasystems.cs.config.ArmPayConfigDetail;
import com.chelseasystems.cs.config.ArmPayPlanConfigDetail;
import com.chelseasystems.cs.config.ArmTaxRateConfig;


/**
 * put your documentation comment here
 */
public class ArmaniDownloadHelper {

  /**
   * put your documentation comment here
   */
  public ArmaniDownloadHelper() {
  }

  /**
   * put your documentation comment here
   * @param theMgr
   * @return
   * @exception Exception
   */
  public static AlterationItemGroup[] getAllAlterationItemGroups(IRepositoryManager theMgr)
      throws Exception {
    ArmaniDownloadClientServices cs = (ArmaniDownloadClientServices)theMgr.getGlobalObject(
        "ARMANI_DOWNLOAD_SRVC");
    return cs.getAllAlterationItemGroups();
  }

  /**
   * put your documentation comment here
   * @param theMgr
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception Exception
   */
  public AlterationItemGroup[] getAlterationItemGroupsByCountryAndLanguage(IRepositoryManager
      theMgr, String sCountry, String sLanguage)
      throws Exception {
    ArmaniDownloadClientServices cs = (ArmaniDownloadClientServices)theMgr.getGlobalObject(
        "ARMANI_DOWNLOAD_SRVC");
    return cs.getAlterationItemGroupsByCountryAndLanguage(sCountry, sLanguage);
  }

  /**
   * put your documentation comment here
   * @param theMgr
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception Exception
   */
  public ArmConfig getConfigByCountryAndLanguage(IRepositoryManager theMgr, String sCountry
      , String sLanguage)
      throws Exception {
    ArmaniDownloadClientServices cs = (ArmaniDownloadClientServices)theMgr.getGlobalObject(
        "ARMANI_DOWNLOAD_SRVC");
    return cs.getConfigByCountryAndLanguage(sCountry, sLanguage);
  }

  /**
   * put your documentation comment here
   * @param theMgr
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception Exception
   */
  public ArmPayConfigDetail[] getPayConfigByCountryAndLanguage(IRepositoryManager theMgr
      , String sCountry, String sLanguage)
      throws Exception {
    ArmaniDownloadClientServices cs = (ArmaniDownloadClientServices)theMgr.getGlobalObject(
        "ARMANI_DOWNLOAD_SRVC");
    return cs.getPayConfigByCountryAndLanguage(sCountry, sLanguage);
  }

  public ArmPayPlanConfigDetail[] getPayPlanConfigByCountryAndLanguage(IRepositoryManager theMgr
      , String sCountry, String sLanguage)
      throws Exception {
    ArmaniDownloadClientServices cs = (ArmaniDownloadClientServices)theMgr.getGlobalObject(
        "ARMANI_DOWNLOAD_SRVC");
    return cs.getPayPlanConfigByCountryAndLanguage(sCountry, sLanguage);
  }

  /**
   * put your documentation comment here
   * @param theMgr
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception Exception
   */
  public ArmDiscountRule[] getDiscountRuleByCountryAndLanguage(IRepositoryManager theMgr, String sCountry
      , String sLanguage)
      throws Exception {
    ArmaniDownloadClientServices cs = (ArmaniDownloadClientServices)theMgr.getGlobalObject(
        "ARMANI_DOWNLOAD_SRVC");
    return cs.getDiscountRuleByCountryAndLanguage(sCountry, sLanguage);
  }
  
  //start
  
  /**
   * put your documentation comment here
   * @param theMgr
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception Exception
   */
  public ArmTaxRateConfig[] getExceptionTaxDetailByStateAndZipcode(IRepositoryManager theMgr, String sState
      , String sZipcode)
      throws Exception {
    ArmaniDownloadClientServices cs = (ArmaniDownloadClientServices)theMgr.getGlobalObject(
        "DOWNLOAD_SRVC");
    return cs.getExceptionTaxDetailByStateAndZipcode(sState, sZipcode);
  }
  
  //end
}

