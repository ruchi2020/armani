// Copyright 2001,Chelsea Market Systems

package com.chelseasystems.cs.eod;

import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.eod.*;
import java.lang.*;

/**
* Client-side object for retrieving and submitting.
**/
public class CMSTransactionEODClientServices extends ClientServices {

   /** Configuration manager **/
//   private ConfigMgr config = null;

   /**
   * Set the current implementation
   **/
   public CMSTransactionEODClientServices() {
      // Set up the configuration manager.
      config = new ConfigMgr("eod.cfg");
   }

   /**
   * initialize primary implementation
   */
   public void init(boolean online) throws Exception {
       // Set up the proper implementation of the service.
      if (online)
         onLineMode();
      else
         offLineMode();
   }

   public void onLineMode() {
      LoggingServices.getCurrent().logMsg("On-Line Mode for CMSTransactionEODClientServices");
      CMSTransactionEODServices serviceImpl = (CMSTransactionEODServices) config.getObject("CLIENT_IMPL");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSTransactionEODClientServices", "onLineMode()",
             "Cannot instantiate the class that provides the"
             + "implementation of CMSTransactionEODServices in eod.cfg.",
             "Make sure that eod.cfg contains an entry with "
             +"a key of CLIENT_IMPL and a value that is the name of a class "
             +"that provides a concrete implementation of CMSTransactionEODServices.",
             LoggingServices.MAJOR);
         setOffLineMode();
         return;
      }
     CMSTransactionEODServices.setCurrent(serviceImpl);
   }

   public void offLineMode() {
      LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSTransactionEODClientServices");
      CMSTransactionEODServices serviceImpl = (CMSTransactionEODServices) config.getObject("CLIENT_DOWNTIME");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSTransactionEODClientServices", "offLineMode()",
             "Cannot instantiate the class that provides the"
             + " implementation of CMSTransactionEODServices in eod.cfg.",
             "Make sure that eod.cfg contains an entry with "
             + "a key of CLIENT_DOWNTIME and a value"
             + " that is the name of a class that provides a concrete"
             + " implementation of CMSTransactionEODServices.",
             LoggingServices.CRITICAL);
      }
    CMSTransactionEODServices.setCurrent(serviceImpl);
   }

   public Object getCurrentService () {
      return  CMSTransactionEODServices.getCurrent();
   }

   /**default-list
   *@parm EndOfDayTrans EndOfDayTrans
   **/
    public boolean submit(TransactionEOD EndOfDayTrans) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (boolean) CMSTransactionEODServices.getCurrent().submit(EndOfDayTrans);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"submit",
            "Primary Implementation for CMSTransactionEODServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (boolean) CMSTransactionEODServices.getCurrent().submit(EndOfDayTrans);
      } finally {
        this.fireWorkInProgressEvent(false);
      }
   }
   /**default-list
   *@parm EODTransID EODTransID
   **/
    public CMSTransactionEOD findById(String EODTransID) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (CMSTransactionEOD) CMSTransactionEODServices.getCurrent().findById(EODTransID);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findById",
            "Primary Implementation for CMSTransactionEODServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (CMSTransactionEOD) CMSTransactionEODServices.getCurrent().findById(EODTransID);
      } finally {
        this.fireWorkInProgressEvent(false);
      }
   }
}
