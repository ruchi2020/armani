/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.dataaccess;

import  java.sql.SQLException;
import  com.chelseasystems.cs.config.ArmPayConfigDetail;
import  com.chelseasystems.cs.config.ArmPayPlanConfigDetail;



public interface ArmPayCfgDetailDAO extends BaseDAO
{

  /**
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception SQLException
   */
  public ArmPayConfigDetail[] selectByCountryAndLanguage (String sCountry, String sLanguage) throws SQLException;
  public ArmPayPlanConfigDetail[] selectPlansByCountryAndLanguage(String sCountry, String sLanguage) throws SQLException;
}



