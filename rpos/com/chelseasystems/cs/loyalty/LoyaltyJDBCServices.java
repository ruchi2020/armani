/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 5    | 05-18-2005 | Vikram    | N/A       | updated reissueLoyalty                             |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 05-18-2005 | Vikram    | N/A       | updated to update loyaltyFlag in Customer table    |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 05-10-2005 | Vikram    | N/A       |POS_104665_TS_LoyaltyManagement_Rev0                |
 +------+------------+-----------+-----------+----------------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.loyalty;

import java.util.*;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.dataaccess.LoyaltyDAO;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cs.dataaccess.RedeemableIssueDAO;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cs.dataaccess.CustomerDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.ArtsConstants;


//import com.chelseasystems.cs.dataaccess.artsoracle.dao.RedeemableIssueOracleDAO;
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
public class LoyaltyJDBCServices extends LoyaltyServices {
  LoyaltyDAO loyaltyDAO;
  RedeemableIssueDAO redeemableIssueDAO;
  CustomerDAO customerDAO;

  /**
   * put your documentation comment here
   */
  public LoyaltyJDBCServices() {
    ConfigMgr configMgr = new ConfigMgr("jdbc.cfg");
    loyaltyDAO = (LoyaltyDAO)configMgr.getObject("LOYALTY_DAO");
    redeemableIssueDAO = (RedeemableIssueDAO)configMgr.getObject("REDEEMABLEISSUE_DAO");
    customerDAO = (CustomerDAO)configMgr.getObject("CUSTOMER_DAO");
  }

  /**
   *
   * @throws Exception
   * @param customerId String
   * @return Loyalty[]
   */
  public Loyalty[] findByCustomerId(String customerId)
      throws Exception {
    try {
      return loyaltyDAO.selectByCustomerId(customerId);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByCustomerId"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   *
   * @throws Exception
   * @param loyaltyNumber String
   * @return Loyalty
   */
  public Loyalty findById(String loyaltyNumber)
      throws Exception {
    try {
      return loyaltyDAO.selectById(loyaltyNumber);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findById", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   *
   * @throws Exception
   * @param loyaltyNumber String
   * @param fromDate Date
   * @param toDate Date
   * @return LoyaltyHistory[]
   */
  public LoyaltyHistory[] findHistoryByLoyaltyIdForDateRange(String loyaltyNumber, Date fromDate
      , Date toDate)
      throws Exception {
    try {
      return loyaltyDAO.selectHistoryByLoyaltyIdForDateRange(loyaltyNumber, fromDate, toDate);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName()
          , "findHistoryByLoyaltyIdForDateRange", "Exception", "See Exception"
          , LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * put your documentation comment here
   * @param loyaltyNumber
   * @return CMSPremioHistory[]
   * @exception Exception
   */
  public CMSPremioHistory[] findPremioDiscountHistoryByLoyaltyId(String loyaltyNumber)
	      throws Exception {
	  try {
	      return loyaltyDAO.selectPremioDiscountHistoryByLoyaltyId(loyaltyNumber);
	    } catch (Exception exception) {
	      LoggingServices.getCurrent().logMsg(this.getClass().getName()
	          , "findHistoryByLoyaltyIdForDateRange", "Exception", "See Exception"
	          , LoggingServices.MAJOR, exception);
	      throw exception;
	    }
  }
  
  /**
   *
   * @throws Exception
   * @param ruleId String
   * @return LoyaltyRule
   */
  public LoyaltyRule findRuleById(String ruleId)
      throws Exception {
    try {
      return loyaltyDAO.selectRuleById(ruleId);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findRuleById", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  //    /**
   //     *
   //     * @throws Exception
   //     * @param rewardCard RewardCard
   //     * @param loyaltyPointsToDeduct long
   //     */
  //    public void issueReward(RewardCard rewardCard, long loyaltyPointsToDeduct) throws Exception
  //    {
  //    }
  //
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
  //
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
   */
  public void submit(Loyalty loyalty)
      throws Exception {
    try {
      ArrayList list = new ArrayList();
      list.add(loyaltyDAO.getInsertSQL(loyalty));
      if (loyalty.getCustomer() != null && loyalty.getCustomer().getId() != null)
        list.add(customerDAO.getUpdateSQLForCustomerLoyaltyFlag(loyalty.getCustomer().getId(), true));
      loyaltyDAO.execute((ParametricStatement[])list.toArray(new ParametricStatement[list.size()]));
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "submit", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  //    /**
   //     *
   //     * @throws Exception
   //     * @param loyaltyHistory LoyaltyHistory
   //     */
  //    public void submitHistory(LoyaltyHistory loyaltyHistory) throws Exception
  //    {
  //        try
  //        {
  //            loyaltyDAO.insertHistory(loyaltyHistory);
  //        }
  //        catch (Exception exception)
  //        {
  //            LoggingServices.getCurrent().logMsg(this.getClass().getName(), "submitHistory",
  //                                                "Exception", "See Exception",
  //                                                LoggingServices.MAJOR, exception);
  //            throw exception;
  //        }
  //    }
  /**
   *
   * @throws Exception
   * @param loyaltyId String
   * @param activeStatus boolean
   */
  public void updateStatus(String loyaltyId, boolean activeStatus)
      throws Exception {
    try {
      loyaltyDAO.updateStatus(loyaltyId, activeStatus);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "updateStatus", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   *
   * @throws Exception
   * @param customerId String
   */
  public RewardCard[] findRewardsByCustomerId(String customerId)
      throws Exception {
    try {
      Redeemable[] redeemables = redeemableIssueDAO.selectByCustomerId(ArtsConstants.
          DISCOUNT_TYPE_REWARD_CARD, customerId);
      if (redeemables == null)
        return null;
      //System.out.println("********************* "+redeemables+":"+redeemables.length);
      RewardCard[] rewardCards = new RewardCard[redeemables.length];
      for (int i = 0; i < redeemables.length; i++) {
        //System.out.println(redeemables[i].getClass()+":"+redeemables[i].getId());
        rewardCards[i] = (RewardCard)redeemables[i];
      }
      //return (RewardCard[])redeemableIssueDAO.selectByCustomerId(RewardCard.REWARD_CARD_TYPE, customerId);
      return rewardCards;
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findRewardsByCustomerId"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   *
   * @throws Exception
   * @param existingLoyaltyNumber String
   * @param newLoyalty Loyalty
   */
  public void reissueLoyalty(String existingLoyaltyNumber, Loyalty newLoyalty)
      throws Exception {
    try {
      ArrayList list = new ArrayList();
      list.add(loyaltyDAO.getUpdateStatusSQL(existingLoyaltyNumber, false));
      list.add(loyaltyDAO.getInsertSQL(newLoyalty));
      list.add(loyaltyDAO.getUpdateHistoryWithNewLoyaltySQL(existingLoyaltyNumber, newLoyalty));
      loyaltyDAO.execute((ParametricStatement[])list.toArray(new ParametricStatement[list.size()]));
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "reissueLoyalty", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   *
   * @throws Exception
   * @param storeId String
   * @param fromDate Date
   * @param toDate Date
   */
  public LoyaltyRule[] findRulesByStoreIdForDateRange(String storeId, Date fromDate, Date toDate)
      throws Exception {
    try {
      return loyaltyDAO.selectRulesByStoreIdForDateRange(storeId, fromDate, toDate);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName()
          , "findRulesByStoreIdForDateRange", "Exception", "See Exception", LoggingServices.MAJOR
          , exception);
      throw exception;
    }
  }
}

