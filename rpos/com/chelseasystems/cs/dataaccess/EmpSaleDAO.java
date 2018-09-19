/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//


package  com.chelseasystems.cs.dataaccess;

import  com.chelseasystems.cr.database.*;
import  java.sql.SQLException;
import  java.util.Date;
import  com.chelseasystems.cr.pos.*;
import  com.chelseasystems.cr.currency.*;
import  com.chelseasystems.cr.employee.*;
import  com.chelseasystems.cs.pos.CMSEmpSales;


public interface EmpSaleDAO extends BaseDAO
{

  /**
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (VoidTransaction object) throws SQLException;



  /**
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (CompositePOSTransaction object) throws SQLException;



  /**
   * @param object
   * @exception SQLException
   */
  public void insert (VoidTransaction object) throws SQLException;



  /**
   * @param object
   * @exception SQLException
   */
  public void insert (CompositePOSTransaction object) throws SQLException;



  /**
   * @param storeId
   * @param begin
   * @param end
   * @return
   * @exception SQLException
   */
  public Employee[] selectEmployeesByStoreAndDates (String storeId, Date begin, Date end) throws SQLException;



  /**
   * @param employeeId
   * @param begin
   * @param end
   * @return
   * @exception SQLException
   */
  public CMSEmpSales[] selectByEmployeeAndDates (String employeeId, Date begin, Date end) throws SQLException;



  /**
   * @param storeId
   * @param begin
   * @param end
   * @return
   * @exception SQLException
   */
  public CMSEmpSales[] selectByStoreAndDates (String storeId, Date begin, Date end) throws SQLException;



  /**
   * @param storeId
   * @param begin
   * @param end
   * @return
   * @exception SQLException
   */
  public CMSEmpSales[] selectTotalSaleByStoreAndDates (String storeId, Date begin, Date end) throws SQLException;
}



