/**
 * History (tab separated):-
 * Vers	Date		By	Spec		                Description
 * 1	1/24/05	        KS	POS_IS_CurrencyRate_Rev1	CMSExchangeRateRMIClient
 *
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.currency;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.node.IRemoteServerClient;
import com.chelseasystems.cr.node.ICMSComponent;
import com.igray.naming.*;
import java.rmi.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cs.currency.*;


/**
 *
 * <p>Title: CMSExchangeRateRMIClient</p>
 *
 * <p>Description: The client-side of an RMI connection for fetching/submitting
 * ArmCurrency object.</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSExchangeRateRMIClient extends CMSExchangeRateServices implements
    IRemoteServerClient {

  /** The configuration manager */
  private ConfigMgr config = null;

  /** The reference to the remote implementation of the service. */
  private ICMSExchangeRateRMIServer cmsexchangerateServer = null;

  /** The maximum number of times to try to establish a connection to the RMIServerImpl */
  private int maxTries = 1;

  /**
   * This method set the configuration manager and make sure that the system
   * has a security manager set.
   */
  public CMSExchangeRateRMIClient()
      throws DowntimeException {
    config = new ConfigMgr("currency.cfg");
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new RMISecurityManager());
    }
    init();
  }

  /**
   * This method is used to lookup the remote object from the RMI registry.
   */
  private void init()
      throws DowntimeException {
    try {
      this.lookup();
      System.out.println("CMSExchangeRateRMIClient Lookup: Complete");
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "init()"
          , "Cannot establish connection to RMI server."
          , "Make sure that the server is registered on the remote server"
          + " and that the name of the remote server and remote service are"
          + " correct in the exchangerate.cfg file.", LoggingServices.MAJOR, e);
      throw new DowntimeException(e.getMessage());
    }
  }

  /**
   * This method is used to lookup the remote object from the RMI registry.
   * @exception Exception
   */
  public void lookup()
      throws Exception {
    NetworkMgr mgr = new NetworkMgr("network.cfg");
    maxTries = mgr.getRetryAttempts();
    String connect = mgr.getRMIMasterNode() + config.getString("REMOTE_NAME") + mgr.getQuery();
    cmsexchangerateServer = (ICMSExchangeRateRMIServer)NamingService.lookup(connect);
  }

  /**
   * This method is used to check whether remote server is available or not
   * @return  <true> is component is available
   */
  public boolean isRemoteServerAvailable() {
    try {
      return ((ICMSComponent)this.cmsexchangerateServer).isAvailable();
    } catch (Exception ex) {
      return false;
    }
  }

  /**
   * This method is used to find all currency rates
   */
  public CurrencyRate[] findAllCurrencyRates()
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsexchangerateServer == null) {
        init();
      }
      try {
        return cmsexchangerateServer.findAllCurrencyRates();
      } catch (ConnectException ce) {
        cmsexchangerateServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSExchangeRateServices");
  }

  /**
   * This method is used to find all currency rates
   */
  public CurrencyRate[] findAllCurrencyRates(String sCountry, String sLanguage)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsexchangerateServer == null) {
        init();
      }
      try {
        return cmsexchangerateServer.findAllCurrencyRates(sCountry, sLanguage);
      } catch (ConnectException ce) {
        cmsexchangerateServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSExchangeRateServices");
  }

  /**
   * put your documentation comment here
   * @param from
   * @param to
   * @return
   * @exception MissingExchangeRateException
   */
  public ArmCurrency getExchangeRate(CurrencyType from, CurrencyType to)
      throws MissingExchangeRateException {
    for (int x = 0; x < maxTries; x++) {
      if (cmsexchangerateServer == null) {
        try {
          init();
        } catch (Exception ex) {
          LoggingServices.getCurrent().logMsg(getClass().getName(), "init()"
              , "Cannot establish connection to RMI server."
              , "Make sure that the server is registered on the remote server"
              + " and that the name of the remote server and remote service are"
              + " correct in the exchangerate.cfg file.", LoggingServices.MAJOR, ex);
        }
      }
      try {
        return cmsexchangerateServer.getExchangeRate(from, to);
      } catch (ConnectException ce) {
        cmsexchangerateServer = null;
      } catch (Exception ex) {
        LoggingServices.getCurrent().logMsg(getClass().getName(), "getExchangeRate()"
            , "Cannot establish connection to RMI server."
            , "Make sure that the server is registered on the remote server"
            + " and that the name of the remote server and remote service are"
            + " correct in the exchangerate.cfg file.", LoggingServices.MAJOR, ex);
        //throw new DowntimeException(ex.getMessage());
      }
    }
    return null;
  }
}

