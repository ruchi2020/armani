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
import com.chelseasystems.cs.pos.AlterationItemGroup;
import com.chelseasystems.cs.config.ArmConfig;
import com.chelseasystems.cs.config.ArmDiscountRule;
import com.chelseasystems.cs.config.ArmPayConfigDetail;
import com.chelseasystems.cs.config.ArmPayPlanConfigDetail;
import com.chelseasystems.cs.config.ArmTaxRateConfig;

/**
 * put your documentation comment here
 */
public class ArmaniDownloadNullServices extends ArmaniDownloadServices {

  /**
   * put your documentation comment here
   */
  public ArmaniDownloadNullServices() {
  }

  /**
   * put your documentation comment here
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception Exception
   */
  public ArmConfig getConfigByCountryAndLanguage(String sCountry, String sLanguage)
      throws Exception {
    return new ArmConfig();
  }

  /**
   * put your documentation comment here
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception Exception
   */
  public ArmPayConfigDetail[] getPayConfigByCountryAndLanguage(String sCountry, String sLanguage)
      throws Exception {
    return new ArmPayConfigDetail[0];
  }

  /**
   * put your documentation comment here
   * @return
   * @exception Exception
   */
  public AlterationItemGroup[] getAllAlterationItemGroups()
      throws Exception {
    return new AlterationItemGroup[0];
  }

  /**
   * put your documentation comment here
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception Exception
   */
  public AlterationItemGroup[] getAlterationItemGroupsByCountryAndLanguage(String sCountry
      , String sLanguage)
      throws Exception {
    return new AlterationItemGroup[0];
  }

  public ArmPayPlanConfigDetail[] getPayPlanConfigByCountryAndLanguage(String sCountry, String sLanguage)
      throws Exception {
    return new ArmPayPlanConfigDetail[0];
  }

  /**
   * put your documentation comment here
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception Exception
   */
  public ArmDiscountRule[] getDiscountRuleByCountryAndLanguage(String sCountry, String sLanguage)
      throws Exception {
    return new ArmDiscountRule[0];
  }
  
  //start
  /**
   * put your documentation comment here
   * @param sState
   * @param sZipcode
   * @return
   * @exception Exception
   */
  public ArmTaxRateConfig[] getExceptionTaxDetailByStateAndZipcode(String sState, String sZipcode)
      throws Exception {
    return new ArmTaxRateConfig[0];
  }
  //end
}

