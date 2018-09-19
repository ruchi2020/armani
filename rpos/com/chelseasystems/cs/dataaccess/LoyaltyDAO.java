/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 05/18/2005 | Vikram    | N/A       | Added getUpdateHistoryWithNewLoyaltySQL method     |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 05/10/2005 | Vikram    | N/A       | POS_104665_TS_LoyaltyManagement_Rev0               |
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.dataaccess;

import  java.sql.SQLException;
import  java.util.Date;
import  com.chelseasystems.cs.loyalty.*;
import  com.chelseasystems.cr.database.ParametricStatement;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public interface LoyaltyDAO extends BaseDAO
{

  /**
   * insert
   * @throws SQLException
   * @param sql ParametricStatement
   */
  public abstract void execute (ParametricStatement sql) throws SQLException;



  /**
   * insert
   * @throws SQLException
   * @param sqls ParametricStatement[]
   */
  public abstract void execute (ParametricStatement[] sqls) throws SQLException;



  /**
   * insert
   * @throws SQLException
   * @param loyalty Loyalty
   */
  public abstract void insert (Loyalty loyalty) throws SQLException;



  //    /**
  //     * insertRule
  //     * @throws SQLException
  //     * @param loyaltyRule LoyaltyRule
  //     */
  //    public abstract void insertRule(LoyaltyRule loyaltyRule) throws SQLException;
  /**
   * insertHistory
   * @throws SQLException
   * @param loyaltyHistory LoyaltyHistory
   */
  public abstract void insertHistory (LoyaltyHistory loyaltyHistory) throws SQLException;



  /**
   * selectById
   * @throws SQLException
   * @param loyaltyNumber String
   * @return Loyalty
   */
  public abstract Loyalty selectById (String loyaltyNumber) throws SQLException;



  /**
   * selectByCustomerId
   * @throws SQLException
   * @param customerId String
   * @return Loyalty[]
   */
  public abstract Loyalty[] selectByCustomerId (String customerId) throws SQLException;



  /**
   * selectRuleById
   * @throws SQLException
   * @param ruleId String
   * @return LoyaltyRule
   */
  public abstract LoyaltyRule selectRuleById (String ruleId) throws SQLException;



  /**
   * selectHistoryByLoyaltyIdForDateRange
   * @throws SQLException
   * @param loyaltyNumber String
   * @param fromDate Date
   * @param toDate Date
   * @return LoyaltyHistory[]
   */
  public abstract LoyaltyHistory[] selectHistoryByLoyaltyIdForDateRange (String loyaltyNumber, Date fromDate, Date toDate) throws SQLException;

  /**
   * put your documentation comment here
   * @param loyaltyNumber
   * @return CMSPremioHistory[]
   * @exception Exception
   */
  public abstract CMSPremioHistory[] selectPremioDiscountHistoryByLoyaltyId(String loyaltyNumber) throws SQLException;
  

  /**
   * updatePoints
   * @throws SQLException
   * @param loyaltyId String
   * @param currentBalance long
   * @param lifetimeBalance long
   */
  public abstract void updatePoints (String loyaltyId, double currentBalance, double lifetimeBalance, double currentYearBalance, double lastYearBalance) throws SQLException;


  /**
   * updateStatus
   * @throws SQLException
   * @param loyaltyId String
   * @param status boolean
   */
  public abstract void updateStatus (String loyaltyId, boolean status) throws SQLException;



  //    /**
  //     * selectRewardsByCustomerId
  //     * @throws SQLException
  //     * @param customerId String
  //     * @return RewardCard[]
  //     */
  //    public abstract RewardCard[] selectRewardsByCustomerId(String customerId) throws SQLException;
  /**
   * selectRulesByStoreIdForDateRange
   * @throws SQLException
   * @param storeId String
   * @param fromDate Date
   * @param toDate Date
   * @return LoyaltyRule[]
   */
  public abstract LoyaltyRule[] selectRulesByStoreIdForDateRange (String storeId, Date fromDate, Date toDate) throws SQLException;



  /**
   * getInsertSQL
   * @throws SQLException
   * @param loyalty Loyalty
   * @return ParametricStatement
   */
  public abstract ParametricStatement getInsertSQL (Loyalty loyalty) throws SQLException;



  /**
   * getUpdateStatusSQL
   * @throws SQLException
   * @param loyaltyId String
   * @param activeStatus boolean
   * @return ParametricStatement
   */
  public abstract ParametricStatement getUpdateStatusSQL (String loyaltyId, boolean activeStatus) throws SQLException;



  /**
   * getUpdatePointsSQL
   * @throws SQLException
   * @param loyaltyId String
   * @param currentBalance long
   * @param lifetimeBalance long
   * @return ParametricStatement
   */
  public abstract ParametricStatement getUpdatePointsSQL (String loyaltyId, double currentBalance, double lifetimeBalance, double currentYearBalance, double lastYearBalance) throws SQLException;



  /**
   *
   * @param oldLoyaltyId String
   * @param newLoyalty Loyalty
   * @return ParametricStatement
   * @throws SQLException
   */
  public abstract ParametricStatement getUpdateHistoryWithNewLoyaltySQL (String oldLoyaltyId, Loyalty newLoyalty) throws SQLException;



  /**
   * @param id
   * @return
   * @exception SQLException
   */
  public abstract LoyaltyHistory[] selectHistoryByTransactionId (String id) throws SQLException;



  /**
   * @param loyaltyHistory
   * @return
   * @exception SQLException
   */
  public abstract ParametricStatement getInsertHistorySQL (LoyaltyHistory loyaltyHistory) throws SQLException;
}



