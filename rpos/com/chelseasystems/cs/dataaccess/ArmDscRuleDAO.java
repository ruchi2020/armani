/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.dataaccess;

import  java.sql.SQLException;
import com.chelseasystems.cs.config.ArmDiscountRule;

public interface ArmDscRuleDAO extends BaseDAO {

  /**
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception SQLException
   */
  public ArmDiscountRule[] selectDiscountRuleByCountryAndLanguage (String sCountry, String sLanguage) throws SQLException;
}