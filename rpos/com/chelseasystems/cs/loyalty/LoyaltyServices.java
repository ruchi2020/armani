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

import java.util.Date;
import com.chelseasystems.cr.config.ConfigMgr;


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
public abstract class LoyaltyServices {
  private static LoyaltyServices current = null;

  /**
   *
   * @return LoyaltyServices
   */
  public static LoyaltyServices getCurrent() {
    if (null == current) {
      System.out.println("Automatically setting current implementation of LoyaltyServices.");
      ConfigMgr config = new ConfigMgr("loyalty.cfg");
      Object obj = config.getObject("LOYALTY_SERVICES_IMPL");
      current = (LoyaltyServices)obj;
    }
    return current;
  }

  /**
   *
   * @param aService LoyaltyServices
   */
  public static void setCurrent(LoyaltyServices aService) {
    System.out.println("Setting current implementation of LoyaltyServices.");
    current = aService;
  }

  /**
   * submit
   * @throws Exception
   * @param loyalty Loyalty
   */
  public abstract void submit(Loyalty loyalty)
      throws Exception;

  //    /**
   //     * submitHistory
   //     * @throws Exception
   //     * @param loyaltyHistory LoyaltyHistory
   //     */
  //    public abstract void submitHistory(LoyaltyHistory loyaltyHistory) throws Exception;
  /**
   * findById
   * @throws Exception
   * @param loyaltyNumber String
   * @return Loyalty
   */
  public abstract Loyalty findById(String loyaltyNumber)
      throws Exception;

  /**
   * findByCustomerId
   * @throws Exception
   * @param customerId String
   * @return Loyalty[]
   */
  public abstract Loyalty[] findByCustomerId(String customerId)
      throws Exception;

  /**
   * findHistoryByLoyaltyIdForDateRange
   * @throws Exception
   * @param loyaltyNumber String
   * @param fromDate Date
   * @param toDate Date
   * @return LoyaltyHistory[]
   */
  public abstract LoyaltyHistory[] findHistoryByLoyaltyIdForDateRange(String loyaltyNumber
      , Date fromDate, Date toDate)
      throws Exception;

  /**
   * put your documentation comment here
   * @param loyaltyNumber
   * @return CMSPremioHistory[]
   * @exception Exception
   */
  public abstract CMSPremioHistory[] findPremioDiscountHistoryByLoyaltyId(String loyaltyNumber)
      throws Exception;
  
  
  /**
   * findRewardsByCustomerId
   * @throws Exception
   * @param customerId String
   * @return RewardCard[]
   */
  public abstract RewardCard[] findRewardsByCustomerId(String customerId)
      throws Exception;

  //    /**
   //     * addPoints
   //     * @throws Exception
   //     * @param loyaltyId String
   //     * @param points int
   //     */
  //    public abstract void addPoints(String loyaltyId, long points) throws Exception;
  //
  //    /**
   //     * removePoints
   //     * @throws Exception
   //     * @param loyaltyId String
   //     * @param points int
   //     */
  //    public abstract void removePoints(String loyaltyId, long points) throws Exception;
  /**
   * updateStatus
   * @throws Exception
   * @param loyaltyId String
   * @param activeStatus boolean
   */
  public abstract void updateStatus(String loyaltyId, boolean activeStatus)
      throws Exception;

  //    /**
   //     * issueReward
   //     * @throws Exception
   //     * @param rewardCard RewardCard
   //     * @param loyaltyPointsToDeduct long
   //     */
  //    public abstract void issueReward(RewardCard rewardCard, long loyaltyPointsToDeduct) throws Exception;
  /**
   * findRuleById
   * @throws Exception
   * @param ruleId String
   * @return LoyaltyRule
   */
  public abstract LoyaltyRule findRuleById(String ruleId)
      throws Exception;

  /**
   * reissueLoyalty
   * @throws Exception
   * @param existingLoyaltyNumber String
   * @param newLoyalty Loyalty
   */
  public abstract void reissueLoyalty(String existingLoyaltyNumber, Loyalty newLoyalty)
      throws Exception;

  /**
   * findRulesByStoreIdForDateRange
   * @throws Exception
   * @param storeId String
   * @param fromDate Date
   * @param toDate Date
   * @return LoyaltyRule[]
   */
  public abstract LoyaltyRule[] findRulesByStoreIdForDateRange(String storeId, Date fromDate
      , Date toDate)
      throws Exception;
}

