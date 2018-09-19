/*
 * @copyright (c) 2002 Chelsea Market Systems
 */

package  com.chelseasystems.cs.sos;

import  com.chelseasystems.cr.config.*;
import  com.chelseasystems.cr.logging.*;
import  com.chelseasystems.cr.appmgr.*;
import  com.chelseasystems.cr.sos.*;

/**
 * Client-side object for retrieving and submitting.
 */
public class CMSTransactionSOSClientServices extends ClientServices {

   /** Configuration manager **/
//   private ConfigMgr config = null;

   /**
    * Set the current implementation
    */
   public CMSTransactionSOSClientServices() {
      // Set up the configuration manager.
      config = new ConfigMgr("sos.cfg");
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
      LoggingServices.getCurrent().logMsg("On-Line Mode for CMSTransactionSOSClientServices");
      CMSTransactionSOSServices serviceImpl = (CMSTransactionSOSServices) config.getObject("CLIENT_IMPL");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSTransactionSOSClientServices", "onLineMode()",
             "Cannot instantiate the class that provides the"
             + "implementation of CMSTransactionSOSServices in sos.cfg.",
             "Make sure that sos.cfg contains an entry with "
             +"a key of CLIENT_IMPL and a value that is the name of a class "
             +"that provides a concrete implementation of CMSTransactionSOSServices.",
             LoggingServices.MAJOR);
         setOffLineMode();
         return;
      }
      CMSTransactionSOSServices.setCurrent(serviceImpl);
   }

   public void offLineMode() {
      LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSTransactionSOSClientServices");
      CMSTransactionSOSServices serviceImpl = (CMSTransactionSOSServices) config.getObject("CLIENT_DOWNTIME");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSTransactionSOSClientServices", "offLineMode()",
             "Cannot instantiate the class that provides the"
             + " implementation of CMSTransactionSOSServices in sos.cfg.",
             "Make sure that sos.cfg contains an entry with "
             + "a key of CLIENT_DOWNTIME and a value"
             + " that is the name of a class that provides a concrete"
             + " implementation of CMSTransactionSOSServices.",
             LoggingServices.CRITICAL);
      }
      CMSTransactionSOSServices.setCurrent(serviceImpl);
   }

   public Object getCurrentService () {
      return  CMSTransactionSOSServices.getCurrent();
   }

   /**
    * Submit the specified transaction to the back end layer for persistence.
    *    Submitting this type of transaction typically means that a new session
    *    has begun on a particular terminal.
    * @param aTransactionSOS The specified transaciton to submit
    */
   public boolean submit (TransactionSOS aTransactionSOS) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (boolean) CMSTransactionSOSServices.getCurrent().submit(aTransactionSOS);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"submit",
            "Primary Implementation for CMSTransactionSOSServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (boolean) CMSTransactionSOSServices.getCurrent().submit(aTransactionSOS);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

}
