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
public class LoyaltyNullServices extends LoyaltyServices {

  /**
   * put your documentation comment here
   */
  public LoyaltyNullServices() {
  }

  //    /**
   //     *
   //     * @throws Exception
   //     * @param loyaltyId String
   //     * @param points int
   //     * @todo Implement this com.chelseasystems.cs.loyalty.LoyaltyServices
   //     *   method
   //     */
  //    public void addPoints(String loyaltyId, long points) throws Exception
  //    {
  //    }
  /**
   *
   * @throws Exception
   * @param customerId String
   * @return Loyalty[]
   * @todo Implement this com.chelseasystems.cs.loyalty.LoyaltyServices
   *   method
   */
  public Loyalty[] findByCustomerId(String customerId)
      throws Exception {
    return new Loyalty[0];
  }

  /**
   *
   * @throws Exception
   * @param loyaltyNumber String
   * @return Loyalty
   * @todo Implement this com.chelseasystems.cs.loyalty.LoyaltyServices
   *   method
   */
  public Loyalty findById(String loyaltyNumber)
      throws Exception {
    return null;
  }

  /**
   *
   * @throws Exception
   * @param loyaltyNumber String
   * @param fromDate Date
   * @param toDate Date
   * @return LoyaltyHistory[]
   * @todo Implement this com.chelseasystems.cs.loyalty.LoyaltyServices
   *   method
   */
  public LoyaltyHistory[] findHistoryByLoyaltyIdForDateRange(String loyaltyNumber, Date fromDate
      , Date toDate)
      throws Exception {
    return new LoyaltyHistory[0];
  }

  /**
   *
   * @throws Exception
   * @param ruleId String
   * @return LoyaltyRule
   * @todo Implement this com.chelseasystems.cs.loyalty.LoyaltyServices
   *   method
   */
  public LoyaltyRule findRuleById(String ruleId)
      throws Exception {
    return null;
  }

  //    /**
   //     *
   //     * @throws Exception
   //     * @param rewardCard RewardCard
   //     * @param loyaltyPointsToDeduct long
   //     * @todo Implement this com.chelseasystems.cs.loyalty.LoyaltyServices
   //     *   method
   //     */
  //    public void issueReward(RewardCard rewardCard, long loyaltyPointsToDeduct) throws Exception
  //    {
  //    }
  //    /**
   //     *
   //     * @throws Exception
   //     * @param loyaltyId String
   //     * @param points int
   //     * @todo Implement this com.chelseasystems.cs.loyalty.LoyaltyServices
   //     *   method
   //     */
  //    public void removePoints(String loyaltyId, long points) throws Exception
  //    {
  //    }
  /**
   *
   * @throws Exception
   * @param loyalty Loyalty
   * @todo Implement this com.chelseasystems.cs.loyalty.LoyaltyServices
   *   method
   */
  public void submit(Loyalty loyalty)
      throws Exception {}

  //    /**
   //     *
   //     * @throws Exception
   //     * @param loyaltyHistory LoyaltyHistory
   //     * @todo Implement this com.chelseasystems.cs.loyalty.LoyaltyServices
   //     *   method
   //     */
  //    public void submitHistory(LoyaltyHistory loyaltyHistory) throws Exception
  //    {
  //    }
  /**
   *
   * @throws Exception
   * @param loyaltyId String
   * @param activeStatus boolean
   * @todo Implement this com.chelseasystems.cs.loyalty.LoyaltyServices
   *   method
   */
  public void updateStatus(String loyaltyId, boolean activeStatus)
      throws Exception {}

  /**
   *
   * @throws Exception
   * @param customerId String
   * @todo Implement this com.chelseasystems.cs.loyalty.LoyaltyServices
   *   method
   */
  public RewardCard[] findRewardsByCustomerId(String customerId)
      throws Exception {
    return new RewardCard[0];
  }

  /**
   *
   * @throws Exception
   * @param existingLoyaltyNumber String
   * @param newLoyalty Loyalty
   * @todo Implement this com.chelseasystems.cs.loyalty.LoyaltyServices
   *   method
   */
  public void reissueLoyalty(String existingLoyaltyNumber, Loyalty newLoyalty)
      throws Exception {}

  /**
   *
   * @throws Exception
   * @param storeId String
   * @param fromDate Date
   * @param toDate Date
   * @todo Implement this com.chelseasystems.cs.loyalty.LoyaltyServices
   *   method
   */
  public LoyaltyRule[] findRulesByStoreIdForDateRange(String storeId, Date fromDate, Date toDate)
      throws Exception {
    return new LoyaltyRule[0];
  }
  
  /**
   * put your documentation comment here
   * @param loyaltyNumber
   * @return CMSPremioHistory[]
   * @exception Exception
   */
  public CMSPremioHistory[] findPremioDiscountHistoryByLoyaltyId(String loyaltyNumber)
	      throws Exception {
	  return new CMSPremioHistory[0];
  }
}

