/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//


package  com.chelseasystems.cs.dataaccess;

import  com.chelseasystems.cr.database.*;
import  java.sql.SQLException;
import  com.chelseasystems.cr.payment.Redeemable;


public interface RedeemableIssueDAO extends BaseDAO
{

  /**
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (Redeemable object) throws SQLException;



  /**
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getUpdateSQL (Redeemable object) throws SQLException;



  /**
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getDeleteSQL (Redeemable object) throws SQLException;



  /**
   * @param object
   * @exception SQLException
   */
  public void insert (Redeemable object) throws SQLException;



  /**
   * @param object
   * @exception SQLException
   */
  public void update (Redeemable object) throws SQLException;



  /**
   * @param object
   * @exception SQLException
   */
  public void delete (Redeemable object) throws SQLException;



  /**
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement getUpdateGiftCertSQLForLogicalDelete (Redeemable object) throws SQLException;



  /**
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement getUpdateStoreValueCardSQLForLogicalDelete (Redeemable object) throws SQLException;



  /**
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getUpdateRewardCardSQLForLogicalDelete (Redeemable object) throws SQLException;



  /**
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement getUpdateDueBillSQLForLogicalDelete (Redeemable object) throws SQLException;



  /**
   * @param id
   * @return
   * @exception SQLException
   */
  public Redeemable selectRedeemableById (String id) throws SQLException;



  /**
   * @param id
   * @return
   * @exception SQLException
   */
  public Redeemable selectStoreValueCardById (String id) throws SQLException;



  /**
   * @param id
   * @return
   * @exception SQLException
   */
  public Redeemable selectHouseAccountById (String id) throws SQLException;



  /**
   * @param id
   * @return
   * @exception SQLException
   */
  public Redeemable selectGiftCertById (String id) throws SQLException;



  /**
   * @param id
   * @return
   * @exception SQLException
   */
  public Redeemable selectDueBillById (String id) throws SQLException;



  /**
   * @param id
   * @return
   * @exception SQLException
   */
  public Redeemable selectRewardCardById (String id) throws SQLException;



  //  public Redeemable selectDueBillByControlNumber(String controlNumber) throws SQLException;
  public Redeemable selectRedeemableByControlNumber (String controlNumber) throws SQLException;



  /**
   * @param controlNumber
   * @return
   * @exception SQLException
   */
  public Redeemable selectGiftCertByControlNumber (String controlNumber) throws SQLException;



  /**
   * @param controlNumber
   * @return
   * @exception SQLException
   */
  public Redeemable selectStoreValueCardByControlNumber (String controlNumber) throws SQLException;



  //  public Redeemable selectHouseAccountByControlNumber(String controlNumber) throws SQLException;
  public Redeemable selectRewardCardByControlNumber (String controlNumber) throws SQLException;



  /**
   * @param controlNumber
   * @return
   * @exception SQLException
   */
  public boolean isGiftCertControlNumberUsed (String controlNumber) throws SQLException;



  /**
   * @param customerId
   * @return
   * @exception SQLException
   */
  public Redeemable[] selectByCustomerId (String customerId) throws SQLException;



  //  public Redeemable[] selectHouseAccountByCustomerId(String customerId) throws SQLException;
  public Redeemable selectRedeemableById (String type, String Id) throws SQLException;



  /**
   * @param type
   * @param customerId
   * @return
   * @exception SQLException
   */
  public Redeemable[] selectByCustomerId (String type, String customerId) throws SQLException;
}



