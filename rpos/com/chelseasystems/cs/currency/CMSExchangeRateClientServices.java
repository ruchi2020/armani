/**
 * History (tab separated):-
 * Vers	Date		By	Spec		                Description
 * 1	1/24/05	        KS	POS_IS_CurrencyRate_Rev1	CMSExchangeRateClientServices
 *
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.currency;

import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cs.currency.*;


/**
 *
 * <p>Title: CMSExchangeRateClientServices</p>
 *
 * <p>Description: This is client side object for retrieving and submitting
 * currency information</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Khyati Shah
 * @version 1.0
 */
public class CMSExchangeRateClientServices extends ClientServices {

  /** Configuration manager **/
  //private ConfigMgr config = null;

  /**
   * Default Constructors which set the current implementation
   */
  public CMSExchangeRateClientServices() {
    // Set up the configuration manager.
    config = new ConfigMgr("currency.cfg");
  }

  /**
   * This method initialize the primary implementation
   */
  public void init(boolean online)
      throws Exception {
    // Set up the proper implementation of the service.
    if (online) {
      onLineMode();
    } else {
      offLineMode();
    }
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
   * This method is invoked when system is online, to get the client remote
   * implementation from the currency.cfg and set the same in
   * CMSExchangeRateServices
   */
  public void onLineMode() {
    LoggingServices.getCurrent().logMsg("On-Line Mode for CMSExchangeRateClientServices");
    CMSExchangeRateServices serviceImpl = (CMSExchangeRateServices)config.getObject("CLIENT_IMPL");
    if (null == serviceImpl) {
      LoggingServices.getCurrent().logMsg("CMSExchangeRateClientServices", "onLineMode()"
          , "Cannot instantiate the class that provides the"
          + "implementation of CMSExchangeRateServices in exchangerate.cfg."
          , "Make sure that exchangerate.cfg contains an entry with "
          + "a key of CLIENT_IMPL and a value that is the name of a class "
          + "that provides a concrete implementation of CMSExchangeRateServices."
          , LoggingServices.MAJOR);
      setOffLineMode();
      return;
    }
    CMSExchangeRateServices.setCurrent(serviceImpl);
  }

  /**
   * This method is invoked when system is offline, to get the client remote
   * downtime from the currency.cfg and set the same in CMSExchangeRateServices
   */
  public void offLineMode() {
    LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSExchangeRateClientServices");
    CMSExchangeRateServices serviceImpl = (CMSExchangeRateServices)config.getObject(
        "CLIENT_LOCAL_IMPL");
    if (null == serviceImpl) {
      LoggingServices.getCurrent().logMsg("CMSExchangeRateClientServices", "offLineMode()"
          , "Cannot instantiate the class that provides the"
          + " implementation of CMSExchangeRateServices in exchangerate.cfg."
          , "Make sure that exchangerate.cfg contains an entry with "
          + "a key of CLIENT_DOWNTIME and a value"
          + " that is the name of a class that provides a concrete"
          + " implementation of CMSExchangeRateServices.", LoggingServices.CRITICAL);
    }
    CMSExchangeRateServices.setCurrent(serviceImpl);
  }

  /**
   *
   * @return Object
   */
  public Object getCurrentService() {
    return CMSExchangeRateServices.getCurrent();
  }

  /**
   * default-list
   * @param FromCurrencyType From ArmCurrency Type
   * @param ToCurrencyType To ArmCurrency type
   */
  //   public ArmCurrency getExchangeRate (CurrencyType FromCurrencyType, CurrencyType ToCurrencyType) throws Exception {
  //      try {
  //         this.fireWorkInProgressEvent(true);
  //         return null;
  //      } catch (Exception ex) {
  //         return null;
  //      } finally {
  //         this.fireWorkInProgressEvent(false);
  //      }
  //
  //     }
  /**
   * This method is used to find all currency rates
   * @return CurrencyRate[]
   * @throws Exception
   */
  public CurrencyRate[] findAllCurrencyRates()
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CurrencyRate[])((CMSExchangeRateServices)CMSExchangeRateServices.getCurrent()).
          findAllCurrencyRates();
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findAllCurrencyRates"
          , "Primary Implementation for CMSExchangeRateServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CurrencyRate[])((CMSExchangeRateServices)CMSExchangeRateServices.getCurrent()).
          findAllCurrencyRates();
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to find all currency rates
   * @return CurrencyRate[]
   * @throws Exception
   */
  public CurrencyRate[] findAllCurrencyRates(String sCountry, String sLanguage)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CurrencyRate[])((CMSExchangeRateServices)CMSExchangeRateServices.getCurrent()).
          findAllCurrencyRates(sCountry, sLanguage);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findAllCurrencyRates"
          , "Primary Implementation for CMSExchangeRateServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CurrencyRate[])((CMSExchangeRateServices)CMSExchangeRateServices.getCurrent()).
          findAllCurrencyRates(sCountry, sLanguage);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }
}

