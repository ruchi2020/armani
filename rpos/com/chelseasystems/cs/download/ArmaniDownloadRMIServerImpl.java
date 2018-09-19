/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.download;

/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 08-24-2005 | Manpreet  | N/A       |Download services                                   |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
import com.chelseasystems.cr.node.CMSComponent;
import com.chelseasystems.cs.pos.AlterationItemGroup;
import com.chelseasystems.cs.config.ArmConfig;
import java.rmi.RemoteException;
import java.util.Properties;
import com.chelseasystems.cr.logging.LoggingServices;
import java.rmi.ConnectException;

import com.chelseasystems.cs.config.ArmDiscountRule;
import com.chelseasystems.cs.config.ArmPayConfigDetail;
import com.chelseasystems.cs.config.ArmPayPlanConfigDetail;
import com.chelseasystems.cs.config.ArmTaxRateConfig;

/**
 * <p>Title:ArmaniDownloadRMIServerImpl </p>
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
public class ArmaniDownloadRMIServerImpl extends CMSComponent implements ArmaniDownloadRMIServer {

  /**
   * put your documentation comment here
   * @param   Properties props
   */
  public ArmaniDownloadRMIServerImpl(Properties props)
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
          , "Could not instantiate SERVER_IMPL."
          , "Make sure armaniDownload.cfg contains SERVER_IMPL", LoggingServices.MAJOR);
    }
    ArmaniDownloadServices.setCurrent((ArmaniDownloadServices)obj);
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
          , "Make sure armaniDownload.cfg contains a RMIREGISTRY entry.", LoggingServices.MAJOR);
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

  /**
   * Retreive all AlterationItemGroups
   * @return AlterationItemGroup[]
   */
  public AlterationItemGroup[] getAllAlterationItemGroups()
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return ArmaniDownloadServices.getCurrent().getAllAlterationItemGroups();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getAllAlterationItemGroups", start);
      decConnection();
    }
  }

  /**
   * put your documentation comment here
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception RemoteException
   */
  public AlterationItemGroup[] getAlterationItemGroupsByCountryAndLanguage(String sCountry
      , String sLanguage)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return ArmaniDownloadServices.getCurrent().getAlterationItemGroupsByCountryAndLanguage(
          sCountry, sLanguage);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getAlterationItemGroupsByCountryAndLanguage", start);
      decConnection();
    }
  }

  /**
   * put your documentation comment here
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception RemoteException
   */
  public ArmConfig getConfigByCountryAndLanguage(String sCountry, String sLanguage)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return ArmaniDownloadServices.getCurrent().getConfigByCountryAndLanguage(sCountry, sLanguage);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getConfigByCountryAndLanguage", start);
      decConnection();
    }
  }

  /**
   * put your documentation comment here
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception RemoteException
   */
  public ArmPayConfigDetail[] getPayConfigByCountryAndLanguage(String sCountry, String sLanguage)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return ArmaniDownloadServices.getCurrent().getPayConfigByCountryAndLanguage(sCountry
          , sLanguage);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getPayConfigByCountryAndLanguage", start);
      decConnection();
    }
  }
//
  public ArmPayPlanConfigDetail[] getPayPlanConfigByCountryAndLanguage(String sCountry, String sLanguage)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return ArmaniDownloadServices.getCurrent().getPayPlanConfigByCountryAndLanguage(sCountry
          , sLanguage);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getPayPlanConfigByCountryAndLanguage", start);
      decConnection();
    }
  }

  /**
   * put your documentation comment here
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception RemoteException
   */
  public ArmDiscountRule[] getDiscountRuleByCountryAndLanguage(String sCountry, String sLanguage)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return ArmaniDownloadServices.getCurrent().getDiscountRuleByCountryAndLanguage(sCountry, sLanguage);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getDiscountRuleByCountryAndLanguage", start);
      decConnection();
    }
  }
  
  //Added by Vivek Mishra for PCR_TaxExceptionRulesImplementationChanges_US
  //start
  
  /**
   * put your documentation comment here
   * @param sState
   * @param sZipcode
   * @return
   * @exception RemoteException
   */
  public ArmTaxRateConfig[] getExceptionTaxDetailByStateAndZipcode(String sState, String sZipcode)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
     return ArmaniDownloadServices.getCurrent().getExceptionTaxDetailByStateAndZipcode(sState, sZipcode);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getExceptionTaxDetailByStateAndZipcode", start);
      decConnection();
    }
  }
  
  //end
}

