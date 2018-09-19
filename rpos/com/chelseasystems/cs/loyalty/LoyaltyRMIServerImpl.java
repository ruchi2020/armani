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

//import java.rmi.*;
//import java.util.*;
import com.chelseasystems.cr.node.CMSComponent;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Properties;
import com.chelseasystems.cr.logging.LoggingServices;
import java.rmi.ConnectException;


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
public class LoyaltyRMIServerImpl extends CMSComponent implements LoyaltyRMIServer {

  /**
   * put your documentation comment here
   * @param   Properties props
   */
  public LoyaltyRMIServerImpl(Properties props)
      throws RemoteException {
    super(props);
    setImpl();
    init();
  }

  /**
   * Sets the current implementation
   */
  private void setImpl() {
    Object obj = getConfigManager().getObject("SERVER_IMPL");
    if (null == obj) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "setImpl()"
          , "Could not instantiate SERVER_IMPL.", "Make sure loyalty.cfg contains SERVER_IMPL"
          , LoggingServices.MAJOR);
    }
    LoyaltyServices.setCurrent((LoyaltyServices)obj);
  }

  /**
   * put your documentation comment here
   */
  private void init() {
    System.out.println("Binding to RMIRegistry...");
    String theName = getConfigManager().getString("REMOTE_NAME");
    if (null != theName) {
      bind(theName, this);
    } else {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "init()"
          , "Could not find name to bind to in registry."
          , "Make sure loyalty.cfg contains a RMIREGISTRY entry.", LoggingServices.MAJOR);
    }
  }

  /**
   * Receives callback when the config file changes
   * @param aKey an array of keys that have changed
   */
  protected void configEvent(String[] aKey) {}

  /**
   * ping
   *
   * @return boolean
   * @throws RemoteException
   *
   * Used by the DowntimeManager to determine when this object is available.
   * Just because this process is up doesn't mean that the clients can come up.
   * Make sure that the database is available.
   * @return boolean <code>true</code> indicates that this class is available.
   */
  public boolean ping()
      throws RemoteException {
    return true;
  }

  //    /**
   //     *
   //     * @throws RemoteException
   //     * @param loyaltyId String
   //     * @param points int
   //     */
  //    public void addPoints(String loyaltyId, long points) throws RemoteException
  //    {
  //        long start = getStartTime();
  //        try {
  //           if (!isAvailable())
  //              throw  new ConnectException("Service is not available");
  //           incConnection();
  //           LoyaltyServices.getCurrent().addPoints(loyaltyId, points);
  //        } catch (Exception e) {
  //           throw new RemoteException(e.getMessage(), e);
  //        } finally {
  //           addPerformance("addPoints", start);
  //           decConnection();
  //        }
  //    }
  /**
   *
   * @throws RemoteException
   * @param customerId String
   * @return Loyalty[]
   */
  public Loyalty[] findByCustomerId(String customerId)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return LoyaltyServices.getCurrent().findByCustomerId(customerId);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByCustomerId", start);
      decConnection();
    }
  }

  /**
   *
   * @throws RemoteException
   * @param loyaltyNumber String
   * @return Loyalty
   */
  public Loyalty findById(String loyaltyNumber)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return LoyaltyServices.getCurrent().findById(loyaltyNumber);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findById", start);
      decConnection();
    }
  }

  /**
   *
   * @throws RemoteException
   * @param loyaltyNumber String
   * @param fromDate Date
   * @param toDate Date
   * @return LoyaltyHistory[]
   */
  public LoyaltyHistory[] findHistoryByLoyaltyIdForDateRange(String loyaltyNumber, Date fromDate
      , Date toDate)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return LoyaltyServices.getCurrent().findHistoryByLoyaltyIdForDateRange(loyaltyNumber
          , fromDate, toDate);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findHistoryByLoyaltyIdForDateRange", start);
      decConnection();
    }
  }

  /**
   * put your documentation comment here
   * @param loyaltyNumber
   * @return CMSPremioHistory[]
   * @exception Exception
   */
  public CMSPremioHistory[] findPremioDiscountHistoryByLoyaltyId(String loyaltyNumber)
	      throws RemoteException {
	    long start = getStartTime();
	    try {
	      if (!isAvailable())
	        throw new ConnectException("Service is not available");
	      incConnection();
	      return LoyaltyServices.getCurrent().findPremioDiscountHistoryByLoyaltyId(loyaltyNumber);
	    } catch (Exception e) {
	      e.printStackTrace();
	      throw new RemoteException(e.getMessage(), e);
	    } finally {
	      addPerformance("findHistoryByLoyaltyIdForDateRange", start);
	      decConnection();
	    }
	  }

  /**
   *
   * @throws RemoteException
   * @param ruleId String
   * @return LoyaltyRule
   */
  public LoyaltyRule findRuleById(String ruleId)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return LoyaltyServices.getCurrent().findRuleById(ruleId);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findRuleById", start);
      decConnection();
    }
  }

  //    /**
   //     *
   //     * @throws RemoteException
   //     * @param rewardCard RewardCard
   //     * @param loyaltyPointsToDeduct long
   //     */
  //    public void issueReward(RewardCard rewardCard, long loyaltyPointsToDeduct) throws RemoteException
  //    {
  //        long start = getStartTime();
  //        try {
  //           if (!isAvailable())
  //              throw  new ConnectException("Service is not available");
  //           incConnection();
  //           LoyaltyServices.getCurrent().issueReward(rewardCard, loyaltyPointsToDeduct);
  //        } catch (Exception e) {
  //           throw new RemoteException(e.getMessage(), e);
  //        } finally {
  //           addPerformance("issueReward", start);
  //           decConnection();
  //        }
  //    }
  //    /**
   //     *
   //     * @throws RemoteException
   //     * @param loyaltyId String
   //     * @param points int
   //     */
  //    public void removePoints(String loyaltyId, long points) throws RemoteException
  //    {
  //        long start = getStartTime();
  //        try {
  //           if (!isAvailable())
  //              throw  new ConnectException("Service is not available");
  //           incConnection();
  //           LoyaltyServices.getCurrent().removePoints(loyaltyId, points);
  //        } catch (Exception e) {
  //           throw new RemoteException(e.getMessage(), e);
  //        } finally {
  //           addPerformance("removePoints", start);
  //           decConnection();
  //        }
  //    }
  /**
   *
   * @throws RemoteException
   * @param loyalty Loyalty
   */
  public void submit(Loyalty loyalty)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      LoyaltyServices.getCurrent().submit(loyalty);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("submit", start);
      decConnection();
    }
  }

  //    /**
   //     *
   //     * @throws RemoteException
   //     * @param loyaltyHistory LoyaltyHistory
   //     */
  //    public void submitHistory(LoyaltyHistory loyaltyHistory) throws RemoteException
  //    {
  //        long start = getStartTime();
  //        try {
  //           if (!isAvailable())
  //              throw  new ConnectException("Service is not available");
  //           incConnection();
  //           LoyaltyServices.getCurrent().submitHistory(loyaltyHistory);
  //        } catch (Exception e) {
  //           throw new RemoteException(e.getMessage(), e);
  //        } finally {
  //           addPerformance("submitHistory", start);
  //           decConnection();
  //        }
  //    }
  /**
   *
   * @throws RemoteException
   * @param loyaltyId String
   * @param activeStatus boolean
   */
  public void updateStatus(String loyaltyId, boolean activeStatus)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      LoyaltyServices.getCurrent().updateStatus(loyaltyId, activeStatus);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("updateStatus", start);
      decConnection();
    }
  }

  /**
   *
   * @throws RemoteException
   * @param customerId String
   */
  public RewardCard[] findRewardsByCustomerId(String customerId)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return LoyaltyServices.getCurrent().findRewardsByCustomerId(customerId);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findRewardsByCustomerId", start);
      decConnection();
    }
  }

  /**
   *
   * @throws RemoteException
   * @param existingLoyaltyNumber String
   * @param newLoyalty Loyalty
   */
  public void reissueLoyalty(String existingLoyaltyNumber, Loyalty newLoyalty)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      LoyaltyServices.getCurrent().reissueLoyalty(existingLoyaltyNumber, newLoyalty);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("reissueLoyalty", start);
      decConnection();
    }
  }

  /**
   *
   * @throws RemoteException
   * @param storeId String
   * @param fromDate Date
   * @param toDate Date
   */
  public LoyaltyRule[] findRulesByStoreIdForDateRange(String storeId, Date fromDate, Date toDate)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return LoyaltyServices.getCurrent().findRulesByStoreIdForDateRange(storeId, fromDate, toDate);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findRulesByStoreIdForDateRange", start);
      decConnection();
    }
  }
}
