/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 05-10-2005 | Vikram    | N/A       |POS_104665_TS_LoyaltyManagement_Rev0                |
 +------+------------+-----------+-----------+----------------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.loyalty;

import java.rmi.Remote;
import com.igray.naming.IPing;
import java.rmi.RemoteException;
import java.util.Date;


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
public interface LoyaltyRMIServer extends Remote, IPing {

  /**
   * submit
   * @throws RemoteException
   * @param loyalty Loyalty
   */
  public abstract void submit(Loyalty loyalty)
      throws RemoteException;


  //    /**
   //     * submitHistory
   //     * @throws RemoteException
   //     * @param loyaltyHistory LoyaltyHistory
   //     */
  //    public abstract void submitHistory(LoyaltyHistory loyaltyHistory) throws RemoteException;
  /**
   * findById
   * @throws RemoteException
   * @param loyaltyNumber String
   * @return Loyalty
   */
  public abstract Loyalty findById(String loyaltyNumber)
      throws RemoteException;


  /**
   * findByCustomerId
   * @throws RemoteException
   * @param customerId String
   * @return Loyalty[]
   */
  public abstract Loyalty[] findByCustomerId(String customerId)
      throws RemoteException;


  /**
   * findHistoryByLoyaltyIdForDateRange
   * @throws RemoteException
   * @param loyaltyNumber String
   * @param fromDate Date
   * @param toDate Date
   * @return LoyaltyHistory[]
   */
  public abstract LoyaltyHistory[] findHistoryByLoyaltyIdForDateRange(String loyaltyNumber
      , Date fromDate, Date toDate)
      throws RemoteException;

  /**
   * put your documentation comment here
   * @param loyaltyNumber
   * @return CMSPremioHistory[]
   * @exception Exception
   */
  public abstract CMSPremioHistory[] findPremioDiscountHistoryByLoyaltyId(String loyaltyNumber)
      throws Exception;
  //    /**
   //     * addPoints
   //     * @throws RemoteException
   //     * @param loyaltyId String
   //     * @param points int
   //     */
  //    public abstract void addPoints(String loyaltyId, long points) throws RemoteException;
  //
  //    /**
   //     * removePoints
   //     * @throws RemoteException
   //     * @param loyaltyId String
   //     * @param points int
   //     */
  //    public abstract void removePoints(String loyaltyId, long points) throws RemoteException;
  /**
   * updateStatus
   * @throws RemoteException
   * @param loyaltyId String
   * @param activeStatus boolean
   */
  public abstract void updateStatus(String loyaltyId, boolean activeStatus)
      throws RemoteException;


  //    /**
   //     * issueReward
   //     * @throws RemoteException
   //     * @param rewardCard RewardCard
   //     * @param loyaltyPointsToDeduct long
   //     */
  //    public abstract void issueReward(RewardCard rewardCard, long loyaltyPointsToDeduct) throws RemoteException;
  /**
   * findRuleById
   * @throws RemoteException
   * @param ruleId String
   * @return LoyaltyRule
   */
  public abstract LoyaltyRule findRuleById(String ruleId)
      throws RemoteException;


  /**
   * findRewardsByCustomerId
   * @throws RemoteException
   * @param customerId String
   * @return RewardCard[]
   */
  public abstract RewardCard[] findRewardsByCustomerId(String customerId)
      throws RemoteException;


  /**
   * reissueLoyalty
   * @throws RemoteException
   * @param existingLoyaltyNumber String
   * @param newLoyalty Loyalty
   */
  public abstract void reissueLoyalty(String existingLoyaltyNumber, Loyalty newLoyalty)
      throws RemoteException;


  /**
   * findRulesByStoreIdForDateRange
   * @throws RemoteException
   * @param storeId String
   * @param fromDate Date
   * @param toDate Date
   * @return LoyaltyRule[]
   */
  public abstract LoyaltyRule[] findRulesByStoreIdForDateRange(String storeId, Date fromDate
      , Date toDate)
      throws RemoteException;
}
