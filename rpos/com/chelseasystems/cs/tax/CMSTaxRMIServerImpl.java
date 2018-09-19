/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
// Copyright 2001, Chelsea Market Systems


package com.chelseasystems.cs.tax;

import com.chelseasystems.cr.appmgr.DowntimeException;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.node.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.tax.*;
import com.igray.naming.*;
import java.lang.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;


/**
 *
 * <p>Title: CMSTaxRMIServerImpl</p>
 *
 * <p>Description: This is the server side of the RMI connection used for
 * retrieving tax information. This class delgates all method calls to the
 * object referenced by the return value from CMSTaxServices.getCurrent(). </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSTaxRMIServerImpl extends CMSComponent implements ICMSTaxRMIServer {

  /**
   * Constructor
   */
  public CMSTaxRMIServerImpl(Properties props)
      throws RemoteException {
    super(props);
    setImpl();
    init();
  }

  /**
   * Sets the current implementation.
   **/
  private void setImpl() {
    Object obj = getConfigManager().getObject("SERVER_IMPL");
    if (null == obj) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "setImpl()"
          , "Could not instantiate SERVER_IMPL.", "Make sure tax.cfg contains SERVER_IMPL"
          , LoggingServices.MAJOR);
    }
    CMSTaxServices.setCurrent((CMSTaxServices)obj);
  }

  /**
   * Initialize the object using the contents of the tax.cfg file.
   */
  private void init() {
    System.out.println("Binding to RMIRegistry...");
    String theName = getConfigManager().getString("REMOTE_NAME");
    if (null != theName) {
      bind(theName, this);
    } else {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "init()"
          , "Could not find name to bind to in registry."
          , "Make sure tax.cfg contains a RMIREGISTRY entry.", LoggingServices.MAJOR);
    }
  }

  /**
   * Receives callback when the config file changes.
   * @param aKey an array of keys that have changed
   */
  protected void configEvent(String[] aKey) {}

  /**
   * Used by the DowntimeManager to determine when this object is available.
   * Just because this process is up doesn't mean that the clients can come up.
   * Make sure that the database is available.
   * @return boolean <code>true</code> indicates that this class is available.
   **/
  public boolean ping()
      throws RemoteException {
    return true;
  }

  /**
   * Return an array of SaleTax objects that represent the tax on each of the
   * taxable line item details.
   * @param aTxn the transaction for which to calculate taxes
   * @param fromStore the store that serves as the point-of-origin of the sale
   * @param toStore the store that serves as the point-of-acceptance of the sale
   * @param aDate the processing date for which to calculate tax
   */
  public SaleTax getTaxAmounts(CompositePOSTransaction aTxn, Store fromStore, Store toStore
      , Date aDate)
      throws Exception {
    long start = getStartTime();
    try {
      if (CMSTaxServices.getCurrent() == null)
        System.out.println("Impl id null");
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return CMSTaxServices.getCurrent().getTaxAmounts(aTxn, fromStore, toStore, aDate);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getTaxAmounts", start);
      decConnection();
    }
  }
  
  public SaleTax getTaxAmounts(CompositePOSTransaction aTxn, Store fromStore, Store toStore
	      , Date aDate, HashMap<String, Object[]> taxDetailMap)
	      throws Exception {
	    long start = getStartTime();
	    try {
	      if (CMSTaxServices.getCurrent() == null)
	        System.out.println("Impl id null");
	      if (!isAvailable())
	        throw new ConnectException("Service is not available");
	      incConnection();
	      return CMSTaxServices.getCurrent().getTaxAmounts(aTxn, fromStore, toStore, aDate, taxDetailMap);
	    } catch (Exception e) {
	      throw new RemoteException(e.getMessage(), e);
	    } finally {
	      addPerformance("getTaxAmounts", start);
	      decConnection();
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
    long start = getStartTime();
    try {
      if (CMSTaxServices.getCurrent() == null)
        System.out.println("Impl id null");
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (ArmTaxRate[])((CMSTaxServices)(CMSTaxServices.getCurrent())).findByZipCode(zipCode);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByZipCode", start);
      decConnection();
    }
  }
}

