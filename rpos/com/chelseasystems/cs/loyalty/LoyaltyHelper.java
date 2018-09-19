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

import java.util.*;
import com.chelseasystems.cr.appmgr.IRepositoryManager;


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
public class LoyaltyHelper {

  /**
   * addLoyalty
   * @throws Exception
   * @param theMgr IRepositoryManager
   * @param loyalty Loyalty
   */
  public static void addLoyalty(IRepositoryManager theMgr, Loyalty loyalty)
      throws Exception {
    LoyaltyClientServices cs = (LoyaltyClientServices)theMgr.getGlobalObject("LOYALTY_SRVC");
    cs.submit(loyalty);
  }

  //    /**
   //     * addHistory
   //     * @throws Exception
   //     * @param theMgr IRepositoryManager
   //     * @param loyaltyHistory LoyaltyHistory
   //     */
  //    public static void addHistory(IRepositoryManager theMgr, LoyaltyHistory loyaltyHistory) throws Exception
  //    {
  //        LoyaltyClientServices cs = (LoyaltyClientServices) theMgr.getGlobalObject("LOYALTY_SRVC");
  //        cs.submitHistory(loyaltyHistory);
  //    }
  /**
   * getLoyalty
   * @throws Exception
   * @param theMgr IRepositoryManager
   * @param loyaltyNumber String
   * @return Loyalty
   */
  public static Loyalty getLoyalty(IRepositoryManager theMgr, String loyaltyNumber)
      throws Exception {
    LoyaltyClientServices cs = (LoyaltyClientServices)theMgr.getGlobalObject("LOYALTY_SRVC");
    return cs.findById(loyaltyNumber);
  }

  /**
   * getCustomerLoyalties
   * @throws Exception
   * @param theMgr IRepositoryManager
   * @param customerId String
   * @return Loyalty[]
   */
  public static Loyalty[] getCustomerLoyalties(IRepositoryManager theMgr, String customerId)
      throws Exception {
    LoyaltyClientServices cs = (LoyaltyClientServices)theMgr.getGlobalObject("LOYALTY_SRVC");
    return cs.findByCustomerId(customerId);
  }

  /**
   * getCustomerLoyalties
   * @throws Exception
   * @param theMgr IRepositoryManager
   * @param customerId String
   * @return ArrayList containing Loyalty[] as elements
   */
  public static ArrayList getCustomerLoyaltiesAsList(IRepositoryManager theMgr, String customerId)
      throws Exception {
    Loyalty[] loyaltyArray =  getCustomerLoyalties(theMgr, customerId);
    if(loyaltyArray != null && loyaltyArray.length > 0){
    	ArrayList loyaltyList = new ArrayList(loyaltyArray.length);
	    for(int i=0; i<loyaltyArray.length; i++){
	    	loyaltyList.add(loyaltyArray[i]);
	    }
	    return loyaltyList;
    }else 
    	return null;
  }
  
  /**
   * getLoyaltyHistory
   * @throws Exception
   * @param theMgr IRepositoryManager
   * @param loyaltyNumber String
   * @param fromDate Date
   * @param toDate Date
   * @return LoyaltyHistory[]
   */
  public static LoyaltyHistory[] getLoyaltyHistory(IRepositoryManager theMgr, String loyaltyNumber
      , Date fromDate, Date toDate)
      throws Exception {
    LoyaltyClientServices cs = (LoyaltyClientServices)theMgr.getGlobalObject("LOYALTY_SRVC");
    return cs.findHistoryByLoyaltyIdForDateRange(loyaltyNumber, fromDate, toDate);
  }

  //    /**
   //     * addPoints
   //     * @throws Exception
   //     * @param theMgr IRepositoryManager
   //     * @param loyaltyNumber String
   //     * @param points int
   //     */
  //    public static void addPoints(IRepositoryManager theMgr, String loyaltyNumber, long points) throws Exception
  //    {
  //        LoyaltyClientServices cs = (LoyaltyClientServices) theMgr.getGlobalObject("LOYALTY_SRVC");
  //        cs.addPoints(loyaltyNumber, points);
  //    }
  //
  //    /**
   //     * removePoints
   //     * @throws Exception
   //     * @param theMgr IRepositoryManager
   //     * @param loyaltyNumber String
   //     * @param points int
   //     */
  //    public static void removePoints(IRepositoryManager theMgr, String loyaltyNumber, long points) throws Exception
  //    {
  //        LoyaltyClientServices cs = (LoyaltyClientServices) theMgr.getGlobalObject("LOYALTY_SRVC");
  //        cs.removePoints(loyaltyNumber, points);
  //    }
  /**
   * setStatus
   * @throws Exception
   * @param theMgr IRepositoryManager
   * @param loyaltyNumber String
   * @param activeStatus boolean
   */
  public static void setStatus(IRepositoryManager theMgr, String loyaltyNumber
      , boolean activeStatus)
      throws Exception {
    LoyaltyClientServices cs = (LoyaltyClientServices)theMgr.getGlobalObject("LOYALTY_SRVC");
    cs.updateStatus(loyaltyNumber, activeStatus);
  }

  //    /**
   //     * issueReward
   //     * @throws Exception
   //     * @param theMgr IRepositoryManager
   //     * @param rewardCard RewardCard
   //     * @param loyaltyPointsToDeduct long
   //     */
  //    public static void issueReward(IRepositoryManager theMgr, RewardCard rewardCard, long loyaltyPointsToDeduct) throws Exception
  //    {
  //        LoyaltyClientServices cs = (LoyaltyClientServices) theMgr.getGlobalObject("LOYALTY_SRVC");
  //        cs.issueReward(rewardCard, loyaltyPointsToDeduct);
  //    }
  /**
   * getRule
   * @throws Exception
   * @param theMgr IRepositoryManager
   * @param ruleId String
   * @return LoyaltyRule
   */
  public static LoyaltyRule getRule(IRepositoryManager theMgr, String ruleId)
      throws Exception {
    LoyaltyClientServices cs = (LoyaltyClientServices)theMgr.getGlobalObject("LOYALTY_SRVC");
    return cs.findRuleById(ruleId);
  }

  /**
   * getCustomerRewards
   * @throws Exception
   * @param theMgr IRepositoryManager
   * @param customerId String
   * @return RewardCard[]
   */
  public static RewardCard[] getCustomerRewards(IRepositoryManager theMgr, String customerId)
      throws Exception {
    LoyaltyClientServices cs = (LoyaltyClientServices)theMgr.getGlobalObject("LOYALTY_SRVC");
    return cs.findRewardsByCustomerId(customerId);
  }

  /**
   * getCustomerPremioDiscountHistory
   * @throws Exception
   * @param theMgr IRepositoryManager
   * @param customerId String
   * @return RewardCard[]
   */
  public static CMSPremioHistory[] getCustomerPremioDiscountHistory(IRepositoryManager theMgr, String loyaltyNumber)
      throws Exception {
    LoyaltyClientServices cs = (LoyaltyClientServices)theMgr.getGlobalObject("LOYALTY_SRVC");
    return cs.getCustomerPremioDiscountHistory(loyaltyNumber);
  }
  
  /**
   * getCustomerPremioDiscountHistory
   * @throws Exception
   * @param theMgr IRepositoryManager
   * @param customerId String
   * @return RewardCard[]
   */
  public static ArrayList getCustomerPremioDiscountHistoryAsList(IRepositoryManager theMgr, String loyaltyNumber)
      throws Exception {
    CMSPremioHistory[] premioHistoryArray = getCustomerPremioDiscountHistory(theMgr, loyaltyNumber);
    if(premioHistoryArray != null){
    	ArrayList premioHistoryList = new ArrayList(premioHistoryArray.length);
	    for(int i=0; i<premioHistoryArray.length; i++){
	    	premioHistoryList.add(premioHistoryArray[i]);
	    }
	    return premioHistoryList;
    }else 
    	return null;
  }

  
  
  /**
   * reissueLoyalty
   * @throws Exception
   * @param theMgr IRepositoryManager
   * @param existingLoyaltyNumber String
   * @param newLoyalty Loyalty
   */
  
  public static void reissueLoyalty(IRepositoryManager theMgr, String existingLoyaltyNumber
      , Loyalty newLoyalty)
      throws Exception {
    LoyaltyClientServices cs = (LoyaltyClientServices)theMgr.getGlobalObject("LOYALTY_SRVC");
    cs.reissueLoyalty(existingLoyaltyNumber, newLoyalty);
  }

  /**
   * getStoreRules
   * @throws Exception
   * @param theMgr IRepositoryManager
   * @param storeId String
   * @param fromDate Date
   * @param toDate Date
   * @return LoyaltyRule[]
   */
  public static LoyaltyRule[] getStoreRules(IRepositoryManager theMgr, String storeId
      , Date fromDate, Date toDate)
      throws Exception {
    LoyaltyClientServices cs = (LoyaltyClientServices)theMgr.getGlobalObject("LOYALTY_SRVC");
    return cs.findRulesByStoreIdForDateRange(storeId, fromDate, toDate);
  }
}

