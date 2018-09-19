/**
 * History (tab separated):-
 * Vers	Date		By	Spec		                Description
 * 1	1/24/05	        KS	POS_IS_CurrencyRate_Rev1	CMSExchangeRateRMIServerImpl
 *
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.currency;

import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.node.*;
import java.rmi.*;
import java.util.*;
import com.chelseasystems.cs.currency.*;
import com.chelseasystems.cr.currency.CurrencyType;
import com.chelseasystems.cr.currency.MissingExchangeRateException;
import com.chelseasystems.cr.currency.ArmCurrency;


/**
 *
 * <p>Title: CMSExchangeRateRMIServerImpl</p>
 *
 * <p>Description: This is the server side of the RMI connection used for
 * fetching/submitting information.  This class delgates all method calls to
 * the object referenced by the return value from
 * CMSExchangeRateServices.getCurrent(). </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSExchangeRateRMIServerImpl extends CMSComponent implements ICMSExchangeRateRMIServer {

  /**
   * put your documentation comment here
   * @param   Properties props
   */
  public CMSExchangeRateRMIServerImpl(Properties props)
      throws RemoteException {
    super(props);
    setImpl();
    init();
  }

  /**
   * This method is used to set the server side implementation
   */
  private void setImpl() {
    Object obj = getConfigManager().getObject("SERVER_IMPL");
    if (null == obj) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "setImpl()"
          , "Could not instantiate SERVER_IMPL.", "Make sure exchangerate.cfg contains SERVER_IMPL"
          , LoggingServices.MAJOR);
    }
    CMSExchangeRateServices.setCurrent((CMSExchangeRateServices)obj);
  }

  /**
   * This method is used to bind employee object to RMI registery
   */
  private void init() {
    System.out.println("Binding to RMIRegistry...");
    String theName = getConfigManager().getString("REMOTE_NAME");
    if (null != theName) {
      bind(theName, this);
    } else {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "init()"
          , "Could not find name to bind to in registry."
          , "Make sure exchangerate.cfg contains a REMOTE_NAME entry.", LoggingServices.MAJOR);
    }
  }

  /**
   * This method receives callback when the config file changes
   * @param aKey an array of keys that have changed
   */
  protected void configEvent(String[] aKey) {}

  /**
   * This method is used by the DowntimeManager to determine when this object is available.
   * Just because this process is up doesn't mean that the clients can come up.
   * Make sure that the database is available.
   * @return boolean <code>true</code> indicates that this class is available.
   */
  public boolean ping()
      throws RemoteException {
    return true;
  }

  /**
   * default-list
   * @param FromCurrencyType From ArmCurrency Type
   * @param ToCurrencyType To ArmCurrency type
   */
  //   public ArmCurrency getExchangeRate (CurrencyType FromCurrencyType, CurrencyType ToCurrencyType) throws RemoteException {
  //      long start = getStartTime();
  //      try {
  //         if (!isAvailable())
  //            throw  new ConnectException("Service is not available");
  //         incConnection();
  //         return (ArmCurrency) CMSExchangeRateServices.getCurrent().getExchangeRate(FromCurrencyType, ToCurrencyType);
  //      } catch (Exception e) {
  //         throw new RemoteException(e.getMessage(), e);
  //      } finally {
  //         addPerformance("getExchangeRate(CurrencyType,CurrencyType)", start);
  //         decConnection();
  //      }
  //   }
  /**
   * This method is used to find all currency rates
   */
  public CurrencyRate[] findAllCurrencyRates()
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return (CurrencyRate[])((CMSExchangeRateServices)CMSExchangeRateServices.getCurrent()).
          findAllCurrencyRates();
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findAllCurrencyRates", start);
      decConnection();
    }
  }

  /**
   * This method is used to find all currency rates
   */
  public CurrencyRate[] findAllCurrencyRates(String sCountry, String sLanguage)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return (CurrencyRate[])((CMSExchangeRateServices)CMSExchangeRateServices.getCurrent()).
          findAllCurrencyRates(sCountry, sLanguage);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findAllCurrencyRates", start);
      decConnection();
    }
  }

  /**
   * put your documentation comment here
   * @param from
   * @param to
   * @return
   * @exception MissingExchangeRateException, RemoteException
   */
  public ArmCurrency getExchangeRate(CurrencyType from, CurrencyType to)
      throws MissingExchangeRateException, RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return (ArmCurrency)((CMSExchangeRateServices)CMSExchangeRateServices.getCurrent()).
          getExchangeRate(from, to);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getExchangeRate", start);
      decConnection();
    }
  }
}

