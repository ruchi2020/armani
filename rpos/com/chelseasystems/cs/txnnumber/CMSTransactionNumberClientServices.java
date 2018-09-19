/*
 * @copyright (c) 1998-2002 Chelsea Market Systems LLC
 */


package  com.chelseasystems.cs.txnnumber;

import  com.chelseasystems.cr.config.*;
import  com.chelseasystems.cr.logging.*;
import  com.chelseasystems.cr.appmgr.*;
import  com.chelseasystems.cr.txnnumber.*;
import  java.lang.*;
import  com.chelseasystems.cs.store.*;
import  com.chelseasystems.cs.register.*;


/**
 * Client-side object for retrieving and submitting.
 */
public class CMSTransactionNumberClientServices extends ClientServices {

   /** Configuration manager **/
//   private ConfigMgr config = null;

   /**
    * Set the current implementation
    */
   public CMSTransactionNumberClientServices () {
      // Set up the configuration manager.
      config = new ConfigMgr("txnnumber.cfg");
   }

   /**
    * initialize primary implementation
    */
   public void init (boolean online) throws Exception {
      // Set up the proper implementation of the service.
      if (online)
         onLineMode();
      else
         offLineMode();
   }

   /**
    */
   public void onLineMode () {
      LoggingServices.getCurrent().logMsg("On-Line Mode for CMSTransactionNumberClientServices");
      CMSTransactionNumberServices serviceImpl = (CMSTransactionNumberServices)config.getObject("CLIENT_IMPL");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSTransactionNumberClientServices",
               "onLineMode()", "Cannot instantiate the class that provides the"
               + "implementation of CMSTransactionNumberServices in txnnumber.cfg.",
               "Make sure that txnnumber.cfg contains an entry with " + "a key of CLIENT_IMPL and a value that is the name of a class "
               + "that provides a concrete implementation of CMSTransactionNumberServices.",
               LoggingServices.MAJOR);
         setOffLineMode();
         return;
      }
      CMSTransactionNumberServices.setCurrent(serviceImpl);
   }

   /**
    */
   public void offLineMode () {
      LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSTransactionNumberClientServices");
      CMSTransactionNumberServices serviceImpl = (CMSTransactionNumberServices)config.getObject("CLIENT_DOWNTIME");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSTransactionNumberClientServices",
               "offLineMode()", "Cannot instantiate the class that provides the"
               + " implementation of CMSTransactionNumberServices in txnnumber.cfg.",
               "Make sure that txnnumber.cfg contains an entry with " + "a key of CLIENT_DOWNTIME and a value"
               + " that is the name of a class that provides a concrete" +
               " implementation of CMSTransactionNumberServices.", LoggingServices.CRITICAL);
      }
      CMSTransactionNumberServices.setCurrent(serviceImpl);
   }

   public Object getCurrentService () {
      return  CMSTransactionNumberServices.getCurrent();
   }

   /**
    * Gets the next transaction number
    * @param aStore aStore
    * @param aRegister aRegister
    */
   public String getNextTransactionNumber (CMSStore aStore, CMSRegister aRegister) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (String)CMSTransactionNumberServices.getCurrent().getNextTransactionNumber(aStore,
               aRegister);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "getNextTransactionNumber",
               "Primary Implementation for CMSTransactionNumberServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (String)CMSTransactionNumberServices.getCurrent().getNextTransactionNumber(aStore,
               aRegister);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * @return
    * @exception Exception
    */
   public String peekSequenceNumber () throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (String)CMSTransactionNumberServices.getCurrent().peekSequenceNumber();
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "peekSequenceNumber",
               "Primary Implementation for CMSTransactionNumberServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (String)CMSTransactionNumberServices.getCurrent().peekSequenceNumber();
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * @exception Exception
    */
   public void resetSequenceNumber () throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         CMSTransactionNumberServices.getCurrent().resetSequenceNumber();
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "resetSequenceNumber",
               "Primary Implementation for CMSTransactionNumberServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         CMSTransactionNumberServices.getCurrent().resetSequenceNumber();
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

      /**
    * @exception Exception
    */
   public void resetSequenceNumber (int newSequenceNumber) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         CMSTransactionNumberServices.getCurrent().resetSequenceNumber(newSequenceNumber);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "resetSequenceNumber",
               "Primary Implementation for CMSTransactionNumberServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         CMSTransactionNumberServices.getCurrent().resetSequenceNumber();
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

}



