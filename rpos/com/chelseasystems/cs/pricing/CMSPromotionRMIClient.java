/*
 * @copyright (c) 2003 Retek
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pricing;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.node.IRemoteServerClient;
import com.chelseasystems.cr.node.ICMSComponent;
import com.igray.naming.*;
import java.rmi.*;
import com.chelseasystems.cr.pricing.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cs.store.*;


/**
 *
 * <p>Title: CMSPromotionRMIClient</p>
 *
 * <p>Description: This class deal with client-side of an RMI connection for
 * fetching/submitting promotion object</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSPromotionRMIClient extends CMSPromotionServices implements IRemoteServerClient {

  /** The configuration manager */
  private ConfigMgr config = null;

  /** The reference to the remote implementation of the service. */
  private ICMSPromotionRMIServer cmspromotionServer = null;

  /** The maximum number of times to try to establish a connection to the RMIServerImpl */
  private int maxTries = 1;

  /**
   * Set the configuration manager and make sure that the system has a
   * security manager set.
   */
  public CMSPromotionRMIClient()
      throws DowntimeException {
    config = new ConfigMgr("promotion.cfg");
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
      System.out.println("CMSPromotionRMIClient Lookup: Complete");
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "init()"
          , "Cannot establish connection to RMI server."
          , "Make sure that the server is registered on the remote server"
          + " and that the name of the remote server and remote service are"
          + " correct in the promotion.cfg file.", LoggingServices.MAJOR, e);
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
    cmspromotionServer = (ICMSPromotionRMIServer)NamingService.lookup(connect);
  }

  /**
   * @see com.chelseasystems.cr.node.ICMSComponent
   * @return  <true> is component is available
   */
  public boolean isRemoteServerAvailable() {
    try {
      return ((ICMSComponent)this.cmspromotionServer).isAvailable();
    } catch (Exception ex) {
      return false;
    }
  }

  /**
   * Method to get an IPromotion by Id.
   * @param id specified Id.
   */
  public IPromotion findById(String id)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmspromotionServer == null)
        init();
      try {
        return cmspromotionServer.findById((String)id);
      } catch (ConnectException ce) {
        cmspromotionServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSPromotionServices");
  }

  /**
   * Method to return all IPromotion entries for a specified Store.
   * @param aStore specified store.
   */
  public IPromotion[] findAllForStore(Store aStore)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmspromotionServer == null)
        init();
      try {
        return cmspromotionServer.findAllForStore((Store)aStore);
      } catch (ConnectException ce) {
        cmspromotionServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSPromotionServices");
  }

  /**
   * Method to get a ThresholdPromotion by Id.
   * @param id specified Id.
   */
  public ThresholdPromotion findThresholdPromotionById(String id)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmspromotionServer == null)
        init();
      try {
        return cmspromotionServer.findThresholdPromotionById((String)id);
      } catch (ConnectException ce) {
        cmspromotionServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSPromotionServices");
  }

  /**
   * Method to return all ThresholdPromotions for a Store.
   * @param aStore specified store.
   */
  public ThresholdPromotion[] findThresholdPromotionsForStore(Store aStore)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmspromotionServer == null)
        init();
      try {
        return cmspromotionServer.findThresholdPromotionsForStore((Store)aStore);
      } catch (ConnectException ce) {
        cmspromotionServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSPromotionServices");
  }

  /**
   * Insert specified IPromotion into storage.
   * @param promotion specified promotion
   */
  public void insert(IPromotion promotion)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmspromotionServer == null)
        init();
      try {
        cmspromotionServer.insert((IPromotion)promotion);
        return;
      } catch (ConnectException ce) {
        cmspromotionServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSPromotionServices");
  }
}

