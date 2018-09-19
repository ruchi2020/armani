/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.dataaccess;

import  java.sql.SQLException;
import  com.chelseasystems.cs.pos.AlterationItemGroup;


public interface ArmAlternGrpDAO extends BaseDAO
{

  /**
   * @param groupId
   * @return
   * @exception SQLException
   */
  public AlterationItemGroup[] selectByGroupId (String groupId) throws SQLException;



  /**
   * @return
   * @exception SQLException
   */
  public AlterationItemGroup[] selectAllGroups () throws SQLException;



  /**
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception SQLException
   */
  public AlterationItemGroup[] selectByCountryAndLanguage (String sCountry, String sLanguage) throws SQLException;
}



