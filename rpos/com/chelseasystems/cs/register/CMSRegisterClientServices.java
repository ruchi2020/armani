/*
 * @copyright (c) 1998-2002 Chelsea Market Systems LLC
 History:
 +------+------------+-----------+-----------+-------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                     |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 2    | 04-28-2005 |Megha      |           |  Added new method selectByStoreAndRegID
 |      |            |           |           |   selectByStoreID                               |
 |      |            |           |           |                                                 |
 |      |            |           |           |                                                 |
 -----------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.register;

import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.register.*;

import java.lang.*;
import java.util.HashMap;

import com.chelseasystems.cr.store.*;
import com.chelseasystems.cs.store.*;
import com.chelseasystems.cs.register.CMSRegisterServices.*;


/**
 *
 * <p>Title:CMSRegisterClientServices </p>
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
public class CMSRegisterClientServices extends ClientServices {

  /** Configuration manager **/
 // private ConfigMgr config = null;

  /**
   * Set the current implementation
   */
  public CMSRegisterClientServices() {
    // Set up the configuration manager.
    config = new ConfigMgr("register.cfg");
  }

  /**
   * initialize primary implementation
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
   * implementation from the register.cfg and set the same in CMSRegisterServices
   */
  public void onLineMode() {
    LoggingServices.getCurrent().logMsg("On-Line Mode for CMSRegisterClientServices");
    CMSRegisterServices serviceImpl = (CMSRegisterServices)config.getObject("CLIENT_IMPL");
    if (null == serviceImpl) {
      LoggingServices.getCurrent().logMsg("CMSRegisterClientServices", "onLineMode()"
          , "Cannot instantiate the class that provides the"
          + "implementation of CMSRegisterServices in register."
          , "Make sure that register contains an entry with "
          + "a key of CLIENT_IMPL and a value that is the name of a class "
          + "that provides a concrete implementation of CMSRegisterServices."
          , LoggingServices.MAJOR);
      setOffLineMode();
      return;
    }
    CMSRegisterServices.setCurrent(serviceImpl);
  }

  /**
   * This method is invoked when system is offline, to get the client remote
   * downtime from the register.cfg and set the same in CMSRegisterServices
   */
  public void offLineMode() {
    LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSRegisterClientServices");
    CMSRegisterServices serviceImpl = (CMSRegisterServices)config.getObject("CLIENT_DOWNTIME");
    if (null == serviceImpl) {
      LoggingServices.getCurrent().logMsg("CMSRegisterClientServices", "offLineMode()"
          , "Cannot instantiate the class that provides the"
          + " implementation of CMSRegisterServices in register."
          , "Make sure that register contains an entry with "
          + "a key of CLIENT_DOWNTIME and a value"
          + " that is the name of a class that provides a concrete"
          + " implementation of CMSRegisterServices.", LoggingServices.CRITICAL);
    }
    CMSRegisterServices.setCurrent(serviceImpl);
  }

  /**
   *
   * @return Object
   */
  public Object getCurrentService() {
    return CMSRegisterServices.getCurrent();
  }

  /**
   * This method used to get the next register
   * @param store store
   */
  public CMSRegister getNextRegister(CMSStore store)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSRegister)CMSRegisterServices.getCurrent().getNextRegister(store);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "getNextRegister"
          , "Primary Implementation for CMSRegisterServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSRegister)CMSRegisterServices.getCurrent().getNextRegister(store);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to update a Register
   * @param register register
   */
  public void updateRegister(CMSRegister register)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      CMSRegisterServices.getCurrent().updateRegister(register);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "updateRegister"
          , "Primary Implementation for CMSRegisterServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      CMSRegisterServices.getCurrent().updateRegister(register);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to select by a RegisterID & StoreID
   * @param register register
   */
  public CMSRegister selectByStoreAndRegID(String register, String store)
      throws Exception {
    CMSRegister reg = null;
    try {
      this.fireWorkInProgressEvent(true);
      reg = ((CMSRegisterServices)CMSRegisterServices.getCurrent()).selectByStoreAndRegID(register
          , store);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "selectByStoreAndRegID"
          , "Primary Implementation for CMSRegisterServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      reg = ((CMSRegisterServices)CMSRegisterServices.getCurrent()).selectByStoreAndRegID(register
          , store);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
    return reg;
  }

  /**
   * This method is used to get all   Registers by  StoreID
   * @param register register
   */
  public CMSRegister[] selectByStoreID(String store)
      throws Exception {
    CMSRegister reg[] = null;
    try {
      this.fireWorkInProgressEvent(true);
      reg = ((CMSRegisterServices)CMSRegisterServices.getCurrent()).selectByStoreID(store);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "selectByStoreID"
          , "Primary Implementation for CMSRegisterServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      reg = ((CMSRegisterServices)CMSRegisterServices.getCurrent()).selectByStoreID(store);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
    return reg;
  }
  
  /**
   * Vishal Yevale : Dec 2017 : POS_TS_NF25 Implementation PCR (FRANCE)
   * @param txnObject (CMSTransactionSOS OR CMSTransactionEOD)
   * @return MAP (Calculated  netAmount of Month and Year (if end of year)
   * @throws Exception
   */
  public HashMap submitAndReturnNetAmount(Object txnObject) throws Exception {
	    try {
	        this.fireWorkInProgressEvent(true);
	        return (HashMap)((CMSRegisterServices)CMSRegisterServices.getCurrent()).submitAndReturnNetAmount( txnObject);
	      } catch (DowntimeException ex) {
	        LoggingServices.getCurrent().logMsg(getClass().getName(), "merge"
	            , "Primary Implementation for CMSRegisterServices failed, going Off-Line..."
	            , "See Exception", LoggingServices.MAJOR, ex);
	        offLineMode();
	        setOffLineMode();
	        return (HashMap)((CMSRegisterServices)CMSRegisterServices.getCurrent()).submitAndReturnNetAmount( txnObject);
	      } finally {
	        this.fireWorkInProgressEvent(false);
	      }
	}
}

