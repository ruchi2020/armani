/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.dataaccess;

import  java.sql.SQLException;
import  com.chelseasystems.cs.config.ArmConfig;


public interface ArmCfgDetailDAO extends BaseDAO
{

  /**
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception SQLException
   */
  public ArmConfig selectByCountryAndLanguage (String sCountry, String sLanguage) throws SQLException;
}



