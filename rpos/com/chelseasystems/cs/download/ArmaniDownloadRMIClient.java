/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.download;

//public class ArmaniDownloadRMIClient {
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 05-10-2005 | Vikram    | N/A       |POS_104665_TS_LoyaltyManagement_Rev0                |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
import com.chelseasystems.cr.node.IRemoteServerClient;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.pos.AlterationItemGroup;
import com.chelseasystems.cs.config.ArmConfig;
import java.rmi.RMISecurityManager;
import com.chelseasystems.cr.appmgr.DowntimeException;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.config.NetworkMgr;
import com.igray.naming.NamingService;
import com.chelseasystems.cr.node.ICMSComponent;
import java.rmi.ConnectException;

import com.chelseasystems.cs.config.ArmDiscountRule;
import com.chelseasystems.cs.config.ArmPayConfigDetail;
import com.chelseasystems.cs.config.ArmPayPlanConfigDetail;
import com.chelseasystems.cs.config.ArmTaxRateConfig;

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
public class ArmaniDownloadRMIClient extends ArmaniDownloadServices implements IRemoteServerClient {

  /** The configuration manager */
  private ConfigMgr config = null;

  /** The reference to the remote implementation of the service. */
  private ArmaniDownloadRMIServer armDwnldRMIServer = null;

  /** The maximum number of times to try to establish a connection to the RMIServerImpl */
  private int maxTries = 1;

  /**
   * Set the configuration manager and make sure that the system has a
   * security manager set.
   */
  public ArmaniDownloadRMIClient()
      throws DowntimeException {
    config = new ConfigMgr("armaniDownload.cfg");
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new RMISecurityManager());
    }
    init();
  }

  /**
   * Get the remote interface from the registry.
   */
  private void init()
      throws DowntimeException {
    try {
      this.lookup();
      System.out.println("ArmaniDownloadRMIClient Lookup: Complete");
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "init()"
          , "Cannot establish connection to RMI server."
          , "Make sure that the server is registered on the remote server"
          + " and that the name of the remote server and remote service are"
          + " correct in the armaniDownload.cfg file.", LoggingServices.MAJOR, e);
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
    armDwnldRMIServer = (ArmaniDownloadRMIServer)NamingService.lookup(connect);
  }

  /**
   * @see com.chelseasystems.cr.node.ICMSComponent
   * @return  <true> is component is available
   */
  public boolean isRemoteServerAvailable() {
    try {
      return ((ICMSComponent)this.armDwnldRMIServer).isAvailable();
    } catch (Exception ex) {
      return false;
    }
  }

  /**
   * put your documentation comment here
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception Exception
   */
  public ArmConfig getConfigByCountryAndLanguage(String sCountry, String sLanguage)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (armDwnldRMIServer == null) {
        init();
      }
      try {
        return armDwnldRMIServer.getConfigByCountryAndLanguage(sCountry, sLanguage);
      } catch (ConnectException ce) {
        armDwnldRMIServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to ArmaniDownloadServices");
  }

  /**
   * put your documentation comment here
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception Exception
   */
  public ArmPayConfigDetail[] getPayConfigByCountryAndLanguage(String sCountry, String sLanguage)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (armDwnldRMIServer == null) {
        init();
      }
      try {
        return armDwnldRMIServer.getPayConfigByCountryAndLanguage(sCountry, sLanguage);
      } catch (ConnectException ce) {
        armDwnldRMIServer = null;
      } catch (Exception ex) {
        ex.printStackTrace();
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to ArmaniDownloadServices");
  }

  /**
   * put your documentation comment here
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception Exception
   */
  public AlterationItemGroup[] getAlterationItemGroupsByCountryAndLanguage(String sCountry
      , String sLanguage)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (armDwnldRMIServer == null) {
        init();
      }
      try {
        return armDwnldRMIServer.getAlterationItemGroupsByCountryAndLanguage(sCountry, sLanguage);
      } catch (ConnectException ce) {
        armDwnldRMIServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to ArmaniDownloadServices");
  }

  /**
   * put your documentation comment here
   * @return
   * @exception Exception
   */
  public AlterationItemGroup[] getAllAlterationItemGroups()
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (armDwnldRMIServer == null) {
        init();
      }
      try {
        return armDwnldRMIServer.getAllAlterationItemGroups();
      } catch (ConnectException ce) {
        armDwnldRMIServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to ArmaniDownloadServices");
  }


 public ArmPayPlanConfigDetail[] getPayPlanConfigByCountryAndLanguage(String sCountry, String sLanguage)
     throws Exception {
   for (int x = 0; x < maxTries; x++) {
     if (armDwnldRMIServer == null) {
       init();
     }
     try {
       return armDwnldRMIServer.getPayPlanConfigByCountryAndLanguage(sCountry, sLanguage);
     } catch (ConnectException ce) {
       armDwnldRMIServer = null;
     } catch (Exception ex) {
       ex.printStackTrace();
       throw new DowntimeException(ex.getMessage());
     }
   }
   throw new DowntimeException("Unable to establish connection to ArmaniDownloadServices");
 }

 /**
  * put your documentation comment here
  * @param sCountry
  * @param sLanguage
  * @return
  * @exception Exception
  */
  public ArmDiscountRule[] getDiscountRuleByCountryAndLanguage(String sCountry, String sLanguage)
     throws Exception {
   for (int x = 0; x < maxTries; x++) {
     if (armDwnldRMIServer == null) {
       init();
     }
     try {
       return armDwnldRMIServer.getDiscountRuleByCountryAndLanguage(sCountry, sLanguage);
     } catch (ConnectException ce) {
       armDwnldRMIServer = null;
     } catch (Exception ex) {
       throw new DowntimeException(ex.getMessage());
     }
   }
   throw new DowntimeException("Unable to establish connection to ArmaniDownloadServices");
 }
  
  //Added by Vivek Mishra for PCR_TaxExceptionRulesImplementationChanges_US
  //start
  
  /**
   * put your documentation comment here
   * @param sState
   * @param sZipcode
   * @return
   * @exception Exception
   */
  public ArmTaxRateConfig[] getExceptionTaxDetailByStateAndZipcode(String sState, String sZipcode)
      throws Exception {
	  for (int x = 0; x < maxTries; x++) {
		     if (armDwnldRMIServer == null) {
		       init();
		     }
		     try {
		    return armDwnldRMIServer.getExceptionTaxDetailByStateAndZipcode(sState, sZipcode);
		     } catch (ConnectException ce) {
		       armDwnldRMIServer = null;
		     } catch (Exception ex) {
		       throw new DowntimeException(ex.getMessage());
		     }
		   }
		   throw new DowntimeException("Unable to establish connection to ArmaniDownloadServices");
		 }
    
  
  //end
}

