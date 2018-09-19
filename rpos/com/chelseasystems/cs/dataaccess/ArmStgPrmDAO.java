/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 02-27-2005 | Anand     | N/A       | Modified to add methods to facilitate deletion|
 |      |            |           |           | and updation of of records in DB              |
 --------------------------------------------------------------------------------------------
 */


package  com.chelseasystems.cs.dataaccess;

import  java.sql.SQLException;
import  com.chelseasystems.cs.rms.CMSPosPromDetail;
import  com.chelseasystems.cr.database.*;


public interface ArmStgPrmDAO extends BaseDAO
{

  /**
   * @return
   * @exception SQLException
   */
  public CMSPosPromDetail[] selectByStgStatus () throws SQLException;



  /**
   * @param stgStatus
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getDeleteSQL (String stgStatus) throws SQLException;



  /**
   * @param cmsposPromDetail
   * @exception SQLException
   */
  public void delete (CMSPosPromDetail cmsposPromDetail) throws SQLException;



  /**
   * @param cmsposPromDetail
   * @exception SQLException
   */
  public void update (CMSPosPromDetail cmsposPromDetail) throws SQLException;
}



