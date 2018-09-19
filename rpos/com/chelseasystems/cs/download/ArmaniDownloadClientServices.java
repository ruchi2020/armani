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
import com.chelseasystems.cr.appmgr.ClientServices;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cs.pos.AlterationItemGroup;
import com.chelseasystems.cr.appmgr.DowntimeException;
import com.chelseasystems.cs.config.ArmConfig;
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
public class ArmaniDownloadClientServices extends ClientServices {
  //private ConfigMgr config = null;

  /**
   * Set the current implementation
   */
  public ArmaniDownloadClientServices() {
    config = new ConfigMgr("armaniDownload.cfg");
  }

  /**
   * initialize primary implementation
   */
  public void init(boolean online)
      throws Exception {
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
   * Set current services in OnlineMode.
   */
  public void onLineMode() {
    LoggingServices.getCurrent().logMsg("On-Line Mode for ArmaniDownloadServices");
    ArmaniDownloadServices serviceImpl = (ArmaniDownloadServices)config.getObject("CLIENT_IMPL");
    if (null == serviceImpl) {
      LoggingServices.getCurrent().logMsg("ArmaniDownloadClientServices", "onLineMode()"
          , "Cannot instantiate the class that provides the"
          + "implementation of ArmaniDownloadServices in armaniDownload.cfg."
          , "Make sure that armaniDownload.cfg contains an entry with "
          + "a key of CLIENT_IMPL and a value that is the name of a class "
          + "that provides a concrete implementation of ArmaniDownloadServices."
          , LoggingServices.MAJOR);
      setOffLineMode();
      return;
    }
    ArmaniDownloadServices.setCurrent(serviceImpl);
  }

  /**
   * Set in offline mode
   */
  public void offLineMode() {
    LoggingServices.getCurrent().logMsg("Off-Line Mode for ArmaniDownloadClientServices");
    ArmaniDownloadServices serviceImpl = (ArmaniDownloadServices)config.getObject("CLIENT_DOWNTIME");
    if (null == serviceImpl) {
      LoggingServices.getCurrent().logMsg("ArmaniDownloadServices", "offLineMode()"
          , "Cannot instantiate the class that provides the"
          + " implementation of ArmaniDownloadClientServices in armaniDownload.cfg."
          , "Make sure that armaniDownload.cfg contains an entry with "
          + "a key of CLIENT_DOWNTIME and a value"
          + " that is the name of a class that provides a concrete"
          + " implementation of ArmaniDownloadServices.", LoggingServices.CRITICAL);
    }
    ArmaniDownloadServices.setCurrent(serviceImpl);
  }

  /**
   * Get Current ArmaniDownloadServices.
   * @return Object
   */
  public Object getCurrentService() {
    return ArmaniDownloadServices.getCurrent();
  }

  /**
   * Get All AlterationItemGroups
   * @throws Exception
   * @return AlterationItemGroup[]
   */
  public AlterationItemGroup[] getAllAlterationItemGroups()
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (ArmaniDownloadServices.getCurrent()).getAllAlterationItemGroups();
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "getAllAlterationItemGroups"
          , "Primary Implementation for ArmaniDownloadServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (ArmaniDownloadServices.getCurrent()).getAllAlterationItemGroups();
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * Get AlterationGroups by Locale (Country and Language)
   * @param sCountry Country
   * @param sLanguage Language
   * @throws Exception
   * @return AlterationItemGroup[]
   */
  public AlterationItemGroup[] getAlterationItemGroupsByCountryAndLanguage(String sCountry
      , String sLanguage)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (ArmaniDownloadServices.getCurrent()).getAlterationItemGroupsByCountryAndLanguage(
          sCountry, sLanguage);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName()
          , "getAlterationItemGroupsByCountryAndLanguage"
          , "Primary Implementation for ArmaniDownloadServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (ArmaniDownloadServices.getCurrent()).getAlterationItemGroupsByCountryAndLanguage(
          sCountry, sLanguage);
    } finally {
      this.fireWorkInProgressEvent(false);
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
    try {
      this.fireWorkInProgressEvent(true);
      return (ArmaniDownloadServices.getCurrent()).getConfigByCountryAndLanguage(sCountry
          , sLanguage);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "getConfigByCountryAndLanguage"
          , "Primary Implementation for ArmaniDownloadServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (ArmaniDownloadServices.getCurrent()).getConfigByCountryAndLanguage(sCountry
          , sLanguage);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
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
    try {
      this.fireWorkInProgressEvent(true);
      return (ArmaniDownloadServices.getCurrent()).getPayConfigByCountryAndLanguage(sCountry
          , sLanguage);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "getPayConfigByCountryAndLanguage"
          , "Primary Implementation for ArmaniDownloadServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (ArmaniDownloadServices.getCurrent()).getPayConfigByCountryAndLanguage(sCountry
          , sLanguage);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  //
  public ArmPayPlanConfigDetail[] getPayPlanConfigByCountryAndLanguage(String sCountry, String sLanguage)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (ArmaniDownloadServices.getCurrent()).getPayPlanConfigByCountryAndLanguage(sCountry
          , sLanguage);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "getPayPlanConfigByCountryAndLanguage"
          , "Primary Implementation for ArmaniDownloadServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (ArmaniDownloadServices.getCurrent()).getPayPlanConfigByCountryAndLanguage(sCountry
          , sLanguage);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
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
    try {
      this.fireWorkInProgressEvent(true);
      return (ArmDiscountRule[])((ArmaniDownloadServices)ArmaniDownloadServices.getCurrent()).
      	getDiscountRuleByCountryAndLanguage(sCountry, sLanguage);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "getDiscountRuleByCountryAndLanguage"
          , "Primary Implementation for ArmaniDownloadServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (ArmDiscountRule[])((ArmaniDownloadServices)ArmaniDownloadServices.getCurrent()).
    	getDiscountRuleByCountryAndLanguage(sCountry, sLanguage);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
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
    try {
      this.fireWorkInProgressEvent(true);
      return (ArmTaxRateConfig[])(ArmaniDownloadServices.getCurrent()).getExceptionTaxDetailByStateAndZipcode(sState, sZipcode);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "getExceptionTaxDetailByStateAndZipcode"
          , "Primary Implementation for ArmaniDownloadServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (ArmaniDownloadServices.getCurrent()).getExceptionTaxDetailByStateAndZipcode(sState
          , sZipcode);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }
  
  //end
}

