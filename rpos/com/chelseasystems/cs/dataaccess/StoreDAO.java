/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//


package  com.chelseasystems.cs.dataaccess;

import  com.chelseasystems.cr.store.*;
import  com.chelseasystems.cs.store.CMSStore;

import java.sql.SQLException;
import  java.util.*;


public interface StoreDAO extends BaseDAO
{

  /**
   * @param id
   * @return
   * @exception SQLException
   */
  public Store selectById (String id) throws SQLException;


  /**Poonam S.
   * Added for Expiry_DATE issue on Nov 22,2016 
   * @param id
   * @return
   * @exception SQLException
   */
  public Store selectByStoreId (String id,java.sql.Date process_dt) throws SQLException;
  //ends here

  /**
   * @param object
   * @exception SQLException
   */
  public void update (Store object) throws SQLException;



  /**
   * @param object
   * @exception SQLException
   */
  public void insert (Store object) throws SQLException;



  /**
   * @param city
   * @return
   * @exception SQLException
   */
  public Store[] selectByCity (String city) throws SQLException;



  /**
   * @param state
   * @return
   * @exception SQLException
   */
  public Store[] selectByState (String state) throws SQLException;



  /**
   * @param city
   * @param state
   * @return
   * @exception SQLException
   */
  public Store[] selectByCityAndState (String city, String state) throws SQLException;



  /**
   * @return
   * @exception SQLException
   */
  public Store[] selectAll () throws SQLException;



  /**
   * @return
   * @exception SQLException
   */
  public CMSStore[] selectAllStores () throws SQLException;
}



