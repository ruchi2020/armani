/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
// Copyright 2001, Chelsea Market Systems


package com.chelseasystems.cs.tax;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.node.IRemoteServerClient;
import com.chelseasystems.cr.node.ICMSComponent;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.tax.*;
import com.chelseasystems.cr.node.IRemoteServerClient;
import com.chelseasystems.cr.node.ICMSComponent;
import com.igray.naming.*;
import java.lang.*;
import java.rmi.*;
import java.util.*;


/**
 *
 * <p>Title: CMSTaxRMIClient</p>
 *
 * <p>Description: The client-side of an RMI connection for retrieving tax information.</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSTaxRMIClient extends CMSTaxServices implements IRemoteServerClient {

  /** The configuration manager */
  private ConfigMgr config = null;

  /** The reference to the remote implementation of the service. */
  private ICMSTaxRMIServer cmsTaxServer = null;

  /** The maximum number of times to try to establish a connection to the RMIServerImpl */
  private int maxTries = 1;

  /**
   * Set the configuration manager and make sure that the system has a
   * security manager set.
   **/
  public CMSTaxRMIClient()
      throws DowntimeException {
    config = new ConfigMgr("tax.cfg");
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
      System.out.println("CMSTaxRMIClient Lookup: Complete");
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "init()"
          , "Cannot establish connection to RMI server."
          , "Make sure that the server is registered on the remote server"
          + " and that the name of the remote server and remote service are"
          + " correct in the update.cfg file.", LoggingServices.MAJOR, e);
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
    cmsTaxServer = (ICMSTaxRMIServer)NamingService.lookup(connect);
  }

  /**
   * @see com.chelseasystems.cr.node.ICMSComponent
   * @return  <true> is component is available
   */
  public boolean isRemoteServerAvailable() {
    try {
      return ((ICMSComponent)this.cmsTaxServer).isAvailable();
    } catch (Exception ex) {
      return false;
    }
  }

  /**
   * Return an array of SaleTax objects that represent the tax on each of the
   * taxable line item details.
   * @param aTxn the transaction for which to calculate taxes
   * @param fromStore the store that serves as the point-of-origin of the sale
   * @param toStore the store that serves as the point-of-acceptance of the sale
   * @param aProcessDate the processing date for which to calculate tax
   */
  public SaleTax getTaxAmounts(CompositePOSTransaction aTxn, Store fromStore, Store toStore
      , Date aProcessDate)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsTaxServer == null)
        init();
      try {
        return cmsTaxServer.getTaxAmounts(aTxn, fromStore, toStore, aProcessDate);
      } catch (ConnectException ce) {
        cmsTaxServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTaxServices");
  }

  
  public SaleTax getTaxAmounts(CompositePOSTransaction aTxn, Store fromStore, Store toStore
	      , Date aProcessDate, HashMap<String, Object[]> taxDetailMap)
	      throws Exception {
	    for (int x = 0; x < maxTries; x++) {
	      if (cmsTaxServer == null)
	        init();
	      try {
	        return cmsTaxServer.getTaxAmounts(aTxn, fromStore, toStore, aProcessDate, taxDetailMap);
	      } catch (ConnectException ce) {
	        cmsTaxServer = null;
	      } catch (Exception ex) {
	        throw new DowntimeException(ex.getMessage());
	      }
	    }
	    throw new DowntimeException("Unable to establish connection to CMSTaxServices");
	  }
  /**
   * put your documentation comment here
   * @param zipCode
   * @return
   * @exception Exception
   */
  public ArmTaxRate[] findByZipCode(String zipCode)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsTaxServer == null)
        init();
      try {
        return cmsTaxServer.findByZipCode(zipCode);
      } catch (ConnectException ce) {
        cmsTaxServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTaxServices");
  }
}

