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
 | 2    | 02-27-2005 | Anand     | N/A       | Modified to add methods to facilitate bulk   |
 |      |            |           |           | upload of threshold promotions               |
 --------------------------------------------------------------------------------------------
 */


package  com.chelseasystems.cs.dataaccess;

import  com.chelseasystems.cr.database.*;
import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  com.chelseasystems.cr.pricing.*;
import  com.chelseasystems.cs.pricing.*;
import  com.chelseasystems.cr.store.*;
import  com.chelseasystems.cr.currency.*;
import  java.sql.*;
import  java.util.*;


public interface ThresholdPromotionDAO extends BaseDAO
{

  /**
   * @param store
   * @return
   * @exception SQLException
   */
  public ThresholdPromotion[] selectByStore (Store store) throws SQLException;



  /**
   * @param id
   * @param currencyCode
   * @return
   * @exception SQLException
   */
  public ThresholdPromotion selectById (String id, String currencyCode) throws SQLException;



  /**
   * @param object
   * @exception SQLException
   */
  public void insert (ThresholdPromotion object) throws SQLException;



  /**
   * @param object
   * @param currencyCode
   * @exception SQLException
   */
  public void insert (ThresholdPromotion object, String currencyCode) throws SQLException;
}



