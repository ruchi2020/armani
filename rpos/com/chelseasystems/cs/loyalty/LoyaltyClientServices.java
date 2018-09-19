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

import com.chelseasystems.cr.appmgr.ClientServices;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.logging.LoggingServices;
import java.util.Date;
import com.chelseasystems.cr.appmgr.DowntimeException;


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
public class LoyaltyClientServices extends ClientServices {

  /** Configuration manager **/
 // private ConfigMgr config = null;

  /**
   * Set the current implementation
   */
  public LoyaltyClientServices() {
    // Set up the configuration manager.
    config = new ConfigMgr("loyalty.cfg");
  }

  /**
   * initialize primary implementation
   */
  public void init(boolean online)
      throws Exception {
    // Set up the proper implementation of the service.
    if (online)
      onLineMode();
    else
      offLineMode();
  }

  /**
   * Reads "CLIENT_IMPL" from config file. Returns the class that defines
   * what object is providing the service to objects using this client service
   * in "on-line" mode, i.e. connected to an app server.  If null, this 
   * clientservice is not considered when determining app online status.
   * @return a class of the online service.
   */
  protected Class getOnlineService () throws ClassNotFoundException {
    String className = config.getString("CLIENT_IMPL");
    Class serviceClass = Class.forName(className);
    return  serviceClass;
  }
  
  /**
   *
   */
  public void onLineMode() {
    LoggingServices.getCurrent().logMsg("On-Line Mode for LoyaltyClientServices");
    LoyaltyServices serviceImpl = (LoyaltyServices)config.getObject("CLIENT_IMPL");
    if (null == serviceImpl) {
      LoggingServices.getCurrent().logMsg("LoyaltyClientServices", "onLineMode()"
          , "Cannot instantiate the class that provides the"
          + "implementation of LoyaltyServices in loyalty.cfg."
          , "Make sure that loyalty.cfg contains an entry with "
          + "a key of CLIENT_IMPL and a value that is the name of a class "
          + "that provides a concrete implementation of LoyaltyServices.", LoggingServices.MAJOR);
      setOffLineMode();
      return;
    }
    LoyaltyServices.setCurrent(serviceImpl);
  }

  /**
   *
   */
  public void offLineMode() {
    LoggingServices.getCurrent().logMsg("Off-Line Mode for LoyaltyClientServices");
    LoyaltyServices serviceImpl = (LoyaltyServices)config.getObject("CLIENT_DOWNTIME");
    if (null == serviceImpl) {
      LoggingServices.getCurrent().logMsg("LoyaltyClientServices", "offLineMode()"
          , "Cannot instantiate the class that provides the"
          + " implementation of LoyaltyServices in loyalty.cfg."
          , "Make sure that loyalty.cfg contains an entry with "
          + "a key of CLIENT_DOWNTIME and a value"
          + " that is the name of a class that provides a concrete"
          + " implementation of LoyaltyServices.", LoggingServices.CRITICAL);
    }
    LoyaltyServices.setCurrent(serviceImpl);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Object getCurrentService() {
    return LoyaltyServices.getCurrent();
  }

  /**
   * put your documentation comment here
   * @param loyalty
   * @exception Exception
   */
  public void submit(Loyalty loyalty)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      ((LoyaltyServices)LoyaltyServices.getCurrent()).submit(loyalty);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "submit"
          , "Primary Implementation for LoyaltyServices failed, going Off-Line...", "See Exception"
          , LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      ((LoyaltyServices)LoyaltyServices.getCurrent()).submit(loyalty);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  //    public void submitHistory(LoyaltyHistory loyaltyHistory) throws Exception
  //    {
  //        try {
  //           this.fireWorkInProgressEvent(true);
  //           ((LoyaltyServices)LoyaltyServices.getCurrent()).submitHistory(loyaltyHistory);
  //        } catch (DowntimeException ex) {
  //           LoggingServices.getCurrent().logMsg(getClass().getName(),"submitHistory",
  //              "Primary Implementation for LoyaltyServices failed, going Off-Line...",
  //              "See Exception", LoggingServices.MAJOR, ex);
  //           offLineMode();
  //           setOffLineMode();
  //           ((LoyaltyServices)LoyaltyServices.getCurrent()).submitHistory(loyaltyHistory);
  //        } finally {
  //           this.fireWorkInProgressEvent(false);
  //        }
  //    }
  public Loyalty findById(String loyaltyNumber)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return ((LoyaltyServices)LoyaltyServices.getCurrent()).findById(loyaltyNumber);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findById"
          , "Primary Implementation for LoyaltyServices failed, going Off-Line...", "See Exception"
          , LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return ((LoyaltyServices)LoyaltyServices.getCurrent()).findById(loyaltyNumber);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * put your documentation comment here
   * @param customerId
   * @return
   * @exception Exception
   */
  public Loyalty[] findByCustomerId(String customerId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return ((LoyaltyServices)LoyaltyServices.getCurrent()).findByCustomerId(customerId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByCustomerId"
          , "Primary Implementation for LoyaltyServices failed, going Off-Line...", "See Exception"
          , LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return ((LoyaltyServices)LoyaltyServices.getCurrent()).findByCustomerId(customerId);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * put your documentation comment here
   * @param loyaltyNumber
   * @param fromDate
   * @param toDate
   * @return
   * @exception Exception
   */
  public LoyaltyHistory[] findHistoryByLoyaltyIdForDateRange(String loyaltyNumber, Date fromDate
      , Date toDate)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return ((LoyaltyServices)LoyaltyServices.getCurrent()).findHistoryByLoyaltyIdForDateRange(
          loyaltyNumber, fromDate, toDate);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName()
          , "findHistoryByLoyaltyIdForDateRange"
          , "Primary Implementation for LoyaltyServices failed, going Off-Line...", "See Exception"
          , LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return ((LoyaltyServices)LoyaltyServices.getCurrent()).findHistoryByLoyaltyIdForDateRange(
          loyaltyNumber, fromDate, toDate);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * put your documentation comment here
   * @param loyaltyNumber
   * @return CMSPremioHistory[]
   * @exception Exception
   */
  public CMSPremioHistory[] getCustomerPremioDiscountHistory(String loyaltyNumber)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return ((LoyaltyServices)LoyaltyServices.getCurrent()).findPremioDiscountHistoryByLoyaltyId(loyaltyNumber);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName()
          , "findPremioDiscountHistoryByLoyaltyId"
          , "Primary Implementation for LoyaltyServices failed, going Off-Line...", "See Exception"
          , LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return ((LoyaltyServices)LoyaltyServices.getCurrent()).findPremioDiscountHistoryByLoyaltyId(loyaltyNumber);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }
  
  //    public void addPoints(String loyaltyId, long points) throws Exception
  //    {
  //        try {
  //           this.fireWorkInProgressEvent(true);
  //           ((LoyaltyServices)LoyaltyServices.getCurrent()).addPoints(loyaltyId, points);
  //        } catch (DowntimeException ex) {
  //           LoggingServices.getCurrent().logMsg(getClass().getName(),"addPoints",
  //              "Primary Implementation for LoyaltyServices failed, going Off-Line...",
  //              "See Exception", LoggingServices.MAJOR, ex);
  //           offLineMode();
  //           setOffLineMode();
  //           ((LoyaltyServices)LoyaltyServices.getCurrent()).addPoints(loyaltyId, points);
  //        } finally {
  //           this.fireWorkInProgressEvent(false);
  //        }
  //    }
  //
  //    public void removePoints(String loyaltyId, long points) throws Exception
  //    {
  //        try {
  //           this.fireWorkInProgressEvent(true);
  //           ((LoyaltyServices)LoyaltyServices.getCurrent()).removePoints(loyaltyId, points);
  //        } catch (DowntimeException ex) {
  //           LoggingServices.getCurrent().logMsg(getClass().getName(),"removePoints",
  //              "Primary Implementation for LoyaltyServices failed, going Off-Line...",
  //              "See Exception", LoggingServices.MAJOR, ex);
  //           offLineMode();
  //           setOffLineMode();
  //           ((LoyaltyServices)LoyaltyServices.getCurrent()).removePoints(loyaltyId, points);
  //        } finally {
  //           this.fireWorkInProgressEvent(false);
  //        }
  //    }
  public void updateStatus(String loyaltyId, boolean activeStatus)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      ((LoyaltyServices)LoyaltyServices.getCurrent()).updateStatus(loyaltyId, activeStatus);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "updateStatus"
          , "Primary Implementation for LoyaltyServices failed, going Off-Line...", "See Exception"
          , LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      ((LoyaltyServices)LoyaltyServices.getCurrent()).updateStatus(loyaltyId, activeStatus);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  //    public void issueReward(RewardCard rewardCard, long loyaltyPointsToDeduct) throws Exception
  //    {
  //        try {
  //           this.fireWorkInProgressEvent(true);
  //           ((LoyaltyServices)LoyaltyServices.getCurrent()).issueReward(rewardCard, loyaltyPointsToDeduct);
  //        } catch (DowntimeException ex) {
  //           LoggingServices.getCurrent().logMsg(getClass().getName(),"issueReward",
  //              "Primary Implementation for LoyaltyServices failed, going Off-Line...",
  //              "See Exception", LoggingServices.MAJOR, ex);
  //           offLineMode();
  //           setOffLineMode();
  //           ((LoyaltyServices)LoyaltyServices.getCurrent()).issueReward(rewardCard, loyaltyPointsToDeduct);
  //        } finally {
  //           this.fireWorkInProgressEvent(false);
  //        }
  //    }
  public LoyaltyRule findRuleById(String ruleId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return ((LoyaltyServices)LoyaltyServices.getCurrent()).findRuleById(ruleId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findRuleById"
          , "Primary Implementation for LoyaltyServices failed, going Off-Line...", "See Exception"
          , LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return ((LoyaltyServices)LoyaltyServices.getCurrent()).findRuleById(ruleId);
    } finally {
      this.fireWorkInProgressEvent(false);
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
      this.fireWorkInProgressEvent(true);
      return ((LoyaltyServices)LoyaltyServices.getCurrent()).findRewardsByCustomerId(customerId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findRewardsByCustomerId"
          , "Primary Implementation for LoyaltyServices failed, going Off-Line...", "See Exception"
          , LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return ((LoyaltyServices)LoyaltyServices.getCurrent()).findRewardsByCustomerId(customerId);
    } finally {
      this.fireWorkInProgressEvent(false);
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
      this.fireWorkInProgressEvent(true);
      ((LoyaltyServices)LoyaltyServices.getCurrent()).reissueLoyalty(existingLoyaltyNumber
          , newLoyalty);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "reissueLoyalty"
          , "Primary Implementation for LoyaltyServices failed, going Off-Line...", "See Exception"
          , LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      ((LoyaltyServices)LoyaltyServices.getCurrent()).reissueLoyalty(existingLoyaltyNumber
          , newLoyalty);
    } finally {
      this.fireWorkInProgressEvent(false);
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
      this.fireWorkInProgressEvent(true);
      return ((LoyaltyServices)LoyaltyServices.getCurrent()).findRulesByStoreIdForDateRange(storeId
          , fromDate, toDate);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findRulesByStoreIdForDateRange"
          , "Primary Implementation for LoyaltyServices failed, going Off-Line...", "See Exception"
          , LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return ((LoyaltyServices)LoyaltyServices.getCurrent()).findRulesByStoreIdForDateRange(storeId
          , fromDate, toDate);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }
}

