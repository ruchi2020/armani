/*
 * @copyright (c) 1998-2002 Chelsea Market Systems LLC
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.tax;

import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.tax.*;
import java.lang.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.store.*;
import java.util.*;
import com.chelseasystems.cr.currency.*;


/**
 *
 * <p>Title: CMSTaxClientServices</p>
 *
 * <p>Description: Client-side object for retrieving and submitting.</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSTaxClientServices extends ClientServices {

  /** Configuration manager **/
//  private ConfigMgr config = null;

  /**
   * Set the current implementation
   */
  public CMSTaxClientServices() {
    // Set up the configuration manager.
    config = new ConfigMgr("tax.cfg");
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
   * This method is invoked when system is online, to get the client remote
   * implementation from the tax.cfg and set the same in CMSTaxServices
   */
  public void onLineMode() {
    LoggingServices.getCurrent().logMsg("On-Line Mode for CMSTaxClientServices");
    CMSTaxServices serviceImpl = (CMSTaxServices)config.getObject("CLIENT_IMPL");
    if (null == serviceImpl) {
      LoggingServices.getCurrent().logMsg("CMSTaxClientServices", "onLineMode()"
          , "Cannot instantiate the class that provides the"
          + "implementation of CMSTaxServices in tax.cfg."
          , "Make sure that tax.cfg contains an entry with "
          + "a key of CLIENT_IMPL and a value that is the name of a class "
          + "that provides a concrete implementation of CMSTaxServices.", LoggingServices.MAJOR);
      setOffLineMode();
      return;
    }
    CMSTaxServices.setCurrent(serviceImpl);
  }

  /**
   * This method is invoked when system is offline, to get the client remote
   * downtime from the tax.cfg and set the same in CMSTaxServices
   */
  public void offLineMode() {
    LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSTaxClientServices");
    CMSTaxServices serviceImpl = (CMSTaxServices)config.getObject("CLIENT_DOWNTIME");
    if (null == serviceImpl) {
      LoggingServices.getCurrent().logMsg("CMSTaxClientServices", "offLineMode()"
          , "Cannot instantiate the class that provides the"
          + " implementation of CMSTaxServices in tax.cfg."
          , "Make sure that tax.cfg contains an entry with "
          + "a key of CLIENT_DOWNTIME and a value"
          + " that is the name of a class that provides a concrete"
          + " implementation of CMSTaxServices.", LoggingServices.CRITICAL);
    }
    CMSTaxServices.setCurrent(serviceImpl);
  }

  /**
   *
   * @return Object
   */
  public Object getCurrentService() {
    return CMSTaxServices.getCurrent();
  }

  /**
   * returns a array of SaleTax based in the array of line items in the transaction
   * @param aTxn aTxn
   * @param srcStore srcStore
   * @param destStore destStore
   * @param aDate aDate
   */
  public SaleTax getTaxAmounts(CMSCompositePOSTransaction aTxn, CMSStore srcStore
      , CMSStore destStore, Date aDate)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (SaleTax)CMSTaxServices.getCurrent().getTaxAmounts(aTxn, srcStore, destStore, aDate);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "getTaxAmounts"
          , "Primary Implementation for CMSTaxServices failed, going Off-Line...", "See Exception"
          , LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (SaleTax)CMSTaxServices.getCurrent().getTaxAmounts(aTxn, srcStore, destStore, aDate);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  
  public SaleTax getTaxAmounts(CMSCompositePOSTransaction aTxn, CMSStore srcStore
	      , CMSStore destStore, Date aDate, HashMap<String, Object[]> taxDetailMap)
	      throws Exception {
	    try {
	      this.fireWorkInProgressEvent(true);
	      return (SaleTax)CMSTaxServices.getCurrent().getTaxAmounts(aTxn, srcStore, destStore, aDate, taxDetailMap);
	    } catch (DowntimeException ex) {
	      LoggingServices.getCurrent().logMsg(getClass().getName(), "getTaxAmounts"
	          , "Primary Implementation for CMSTaxServices failed, going Off-Line...", "See Exception"
	          , LoggingServices.MAJOR, ex);
	      offLineMode();
	      setOffLineMode();
	      return (SaleTax)CMSTaxServices.getCurrent().getTaxAmounts(aTxn, srcStore, destStore, aDate, taxDetailMap);
	    } finally {
	      this.fireWorkInProgressEvent(false);
	    }
	  }
  /**
   * put your documentation comment here
   * @param zipCode
   * @return
   * @exception Exception
   */
  public ArmTaxRate[] findByZipCode(String zipCode)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (ArmTaxRate[])((CMSTaxServices)(CMSTaxServices.getCurrent())).findByZipCode(zipCode);
    } catch (DowntimeException ex) {
      ex.printStackTrace();
      LoggingServices.getCurrent().logMsg(getClass().getName(), "selectZipCode"
          , "Primary Implementation for CMSTaxServices failed, going Off-Line...", "See Exception"
          , LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (ArmTaxRate[])((CMSTaxServices)(CMSTaxServices.getCurrent())).findByZipCode(zipCode);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }
}

