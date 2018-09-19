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

import com.chelseasystems.cr.node.IRemoteServerClient;
import com.chelseasystems.cr.config.ConfigMgr;
import java.util.Date;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

import com.chelseasystems.cr.appmgr.DowntimeException;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.config.NetworkMgr;
import com.igray.naming.NamingService;
import com.chelseasystems.cr.node.ICMSComponent;
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
public class LoyaltyRMIClient extends LoyaltyServices implements IRemoteServerClient {

  /** The configuration manager */
  private ConfigMgr config = null;

  /** The reference to the remote implementation of the service. */
  private LoyaltyRMIServer loyaltyServer = null;

  /** The maximum number of times to try to establish a connection to the RMIServerImpl */
  private int maxTries = 1;

  /**
   * Set the configuration manager and make sure that the system has a
   * security manager set.
   */
  public LoyaltyRMIClient()
      throws DowntimeException {
    config = new ConfigMgr("loyalty.cfg");
    if (System.getSecurityManager() == null)
      System.setSecurityManager(new RMISecurityManager());
    init();
  }

  /**
   * Get the remote interface from the registry.
   */
  private void init()
      throws DowntimeException {
    try {
      this.lookup();
      System.out.println("LoyaltyRMIClient Lookup: Complete");
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "init()"
          , "Cannot establish connection to RMI server."
          , "Make sure that the server is registered on the remote server"
          + " and that the name of the remote server and remote service are"
          + " correct in the loyalty.cfg file.", LoggingServices.MAJOR, e);
      throw new DowntimeException(e.getMessage());
    }
  }

  /**
   * Perform lookup of remote server.
   * @exception Exception
   */
  public void lookup()
      throws Exception {
    NetworkMgr mgr = new NetworkMgr("network.cfg");
    maxTries = mgr.getRetryAttempts();
    String connect = mgr.getRMIMasterNode() + config.getString("REMOTE_NAME") + mgr.getQuery();
    loyaltyServer = (LoyaltyRMIServer)NamingService.lookup(connect);
  }

  /**
   * @see com.chelseasystems.cr.node.ICMSComponent
   * @return  <true> is component is available
   */
  public boolean isRemoteServerAvailable() {
    try {
      return ((ICMSComponent)this.loyaltyServer).isAvailable();
    } catch (Exception ex) {
      return false;
    }
  }

  //    /**
   //     *
   //     * @throws Exception
   //     * @param loyaltyId String
   //     * @param points int
   //     */
  //    public void addPoints(String loyaltyId, long points) throws Exception
  //    {
  //        for (int x = 0; x < maxTries; x++) {
  //           if (loyaltyServer == null)
  //              init();
  //           try {
  //              loyaltyServer.addPoints(loyaltyId, points);
  //              return;
  //           } catch (ConnectException ce) {
  //              loyaltyServer = null;
  //           } catch (Exception ex) {
  //              throw new DowntimeException(ex.getMessage());
  //           }
  //        }
  //        throw new DowntimeException("Unable to establish connection to LoyaltyServices");
  //    }
  /**
   *
   * @throws Exception
   * @param customerId String
   * @return Loyalty[]
   */
  public Loyalty[] findByCustomerId(String customerId)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (loyaltyServer == null)
        init();
      try {
        return loyaltyServer.findByCustomerId(customerId);
      } catch (ConnectException ce) {
        loyaltyServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to LoyaltyServices");
  }

  /**
   *
   * @throws Exception
   * @param loyaltyNumber String
   * @return Loyalty
   */
  public Loyalty findById(String loyaltyNumber)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (loyaltyServer == null)
        init();
      try {
        return loyaltyServer.findById(loyaltyNumber);
      } catch (ConnectException ce) {
        loyaltyServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to LoyaltyServices");
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
    for (int x = 0; x < maxTries; x++) {
      if (loyaltyServer == null)
        init();
      try {
        return loyaltyServer.findHistoryByLoyaltyIdForDateRange(loyaltyNumber, fromDate, toDate);
      } catch (ConnectException ce) {
        loyaltyServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to LoyaltyServices");
  }
  
  /**
   * put your documentation comment here
   * @param loyaltyNumber
   * @return CMSPremioHistory[]
   * @exception Exception
   */
  public CMSPremioHistory[] findPremioDiscountHistoryByLoyaltyId(String loyaltyNumber)
	      throws Exception {
	  for (int x = 0; x < maxTries; x++) {
	      if (loyaltyServer == null)
	        init();
	      try {
	        return loyaltyServer.findPremioDiscountHistoryByLoyaltyId(loyaltyNumber);
	      } catch (ConnectException ce) {
	        loyaltyServer = null;
	      } catch (Exception ex) {
	        throw new DowntimeException(ex.getMessage());
	      }
	    }
	    throw new DowntimeException("Unable to establish connection to LoyaltyServices");
  }

  /**
   *
   * @throws Exception
   * @param ruleId String
   * @return LoyaltyRule
   */
  public LoyaltyRule findRuleById(String ruleId)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (loyaltyServer == null)
        init();
      try {
        return loyaltyServer.findRuleById(ruleId);
      } catch (ConnectException ce) {
        loyaltyServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to LoyaltyServices");
  }

  //    /**
   //     *
   //     * @throws Exception
   //     * @param rewardCard RewardCard
   //     * @param loyaltyPointsToDeduct long
   //     */
  //    public void issueReward(RewardCard rewardCard, long loyaltyPointsToDeduct) throws Exception
  //    {
  //        for (int x = 0; x < maxTries; x++) {
  //           if (loyaltyServer == null)
  //              init();
  //           try {
  //              loyaltyServer.issueReward(rewardCard, loyaltyPointsToDeduct);
  //              return;
  //           } catch (ConnectException ce) {
  //              loyaltyServer = null;
  //           } catch (Exception ex) {
  //              throw new DowntimeException(ex.getMessage());
  //           }
  //       }
  //       throw new DowntimeException("Unable to establish connection to LoyaltyServices");
  //    }
  //    /**
   //     *
   //     * @throws Exception
   //     * @param loyaltyId String
   //     * @param points int
   //     */
  //    public void removePoints(String loyaltyId, long points) throws Exception
  //    {
  //        for (int x = 0; x < maxTries; x++) {
  //           if (loyaltyServer == null)
  //              init();
  //           try {
  //              loyaltyServer.removePoints(loyaltyId, points);
  //              return;
  //           } catch (ConnectException ce) {
  //              loyaltyServer = null;
  //           } catch (Exception ex) {
  //              throw new DowntimeException(ex.getMessage());
  //           }
  //       }
  //       throw new DowntimeException("Unable to establish connection to LoyaltyServices");
  //    }
  /**
   *
   * @throws Exception
   * @param loyalty Loyalty
   */
  public void submit(Loyalty loyalty)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (loyaltyServer == null)
        init();
      try {
        loyaltyServer.submit(loyalty);
        return;
      } catch (ConnectException ce) {
        loyaltyServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to LoyaltyServices");
  }

  //    /**
   //     *
   //     * @throws Exception
   //     * @param loyaltyHistory LoyaltyHistory
   //     */
  //    public void submitHistory(LoyaltyHistory loyaltyHistory) throws Exception
  //    {
  //        for (int x = 0; x < maxTries; x++) {
  //           if (loyaltyServer == null)
  //              init();
  //           try {
  //              loyaltyServer.submitHistory(loyaltyHistory);
  //           } catch (ConnectException ce) {
  //              loyaltyServer = null;
  //           } catch (Exception ex) {
  //              throw new DowntimeException(ex.getMessage());
  //           }
  //       }
  //       throw new DowntimeException("Unable to establish connection to LoyaltyServices");
  //    }
  /**
   *
   * @throws Exception
   * @param loyaltyId String
   * @param activeStatus boolean
   */
  public void updateStatus(String loyaltyId, boolean activeStatus)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (loyaltyServer == null)
        init();
      try {
        loyaltyServer.updateStatus(loyaltyId, activeStatus);
        return;
      } catch (ConnectException ce) {
        loyaltyServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to LoyaltyServices");
  }

  /**
   *
   * @throws Exception
   * @param customerId String
   */
  public RewardCard[] findRewardsByCustomerId(String customerId)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (loyaltyServer == null)
        init();
      try {
        return loyaltyServer.findRewardsByCustomerId(customerId);
      } catch (ConnectException ce) {
        loyaltyServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to LoyaltyServices");
  }

  /**
   *
   * @throws Exception
   * @param existingLoyaltyNumber String
   * @param newLoyalty Loyalty
   */
  public void reissueLoyalty(String existingLoyaltyNumber, Loyalty newLoyalty)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (loyaltyServer == null)
        init();
      try {
        loyaltyServer.reissueLoyalty(existingLoyaltyNumber, newLoyalty);
        return;
      } catch (ConnectException ce) {
        loyaltyServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to LoyaltyServices");
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
    for (int x = 0; x < maxTries; x++) {
      if (loyaltyServer == null)
        init();
      try {
        return loyaltyServer.findRulesByStoreIdForDateRange(storeId, fromDate, toDate);
      } catch (ConnectException ce) {
        loyaltyServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to LoyaltyServices");
  }
}
