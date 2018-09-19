/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//Done by Anand Kannan


package  com.chelseasystems.cs.dataaccess;

import  com.chelseasystems.cr.employee.*;
import  com.chelseasystems.cs.employee.*;
import  java.sql.*;


public interface EmployeeDAO extends BaseDAO
{

  /**
   * @param id
   * @return
   * @exception SQLException
   */
  public Employee selectById (String id) throws SQLException;



  // following method added by Anand on 02/14/2005 for finding by external HRMS ID
  public CMSEmployee selectByExternalId (String id) throws SQLException;



  /**
   * @param ssn
   * @return
   * @exception SQLException
   */
  public Employee[] selectBySSN (String ssn) throws SQLException;



  /**
   * @param homeStoreId
   * @return
   * @exception SQLException
   */
  public Employee[] selectByHomeStoreId (String homeStoreId) throws SQLException;



  /**
   * @param shortName
   * @return
   * @exception SQLException
   */
  public Employee[] selectByShortName (String shortName) throws SQLException;


  /**
   * @param lastName
   * @return
   * @exception SQLException
   */
  public Employee[] selectByLastName (String shortName) throws SQLException;
  

  /**
   * @param object
   * @exception SQLException
   */
  public void insert (Employee object) throws SQLException;



  /**
   * @param object
   * @exception SQLException
   */
  public void update (Employee object) throws SQLException;
}



