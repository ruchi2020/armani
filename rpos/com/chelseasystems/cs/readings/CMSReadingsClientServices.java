/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 */


package  com.chelseasystems.cs.readings;

import  com.chelseasystems.cr.config.*;
import  com.chelseasystems.cr.logging.*;
import  com.chelseasystems.cr.appmgr.*;
import  com.chelseasystems.cr.readings.*;
import  java.lang.*;
import  com.chelseasystems.cr.employee.*;
import  java.util.*;
import  com.chelseasystems.cr.store.*;
import  com.chelseasystems.cr.currency.*;
import  com.chelseasystems.cs.employee.CMSEmployee;


/**
 * Client-side object for retrieving and submitting.
 */
public class CMSReadingsClientServices extends ClientServices {

   /** Configuration manager **/
 //  private ConfigMgr config = null;

   /**
    * Set the current implementation
    */
   public CMSReadingsClientServices () {
      // Set up the configuration manager.
      config = new ConfigMgr("readings.cfg");
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
      LoggingServices.getCurrent().logMsg("On-Line Mode for CMSReadingsClientServices");
      CMSReadingsServices serviceImpl = (CMSReadingsServices)config.getObject("CLIENT_IMPL");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSReadingsClientServices", "onLineMode()",
               "Cannot instantiate the class that provides the" + "implementation of CMSReadingsServices in readings.cfg.",
               "Make sure that readings.cfg contains an entry with " + "a key of CLIENT_IMPL and a value that is the name of a class "
               + "that provides a concrete implementation of CMSReadingsServices.",
               LoggingServices.MAJOR);
         setOffLineMode();
         return;
      }
      CMSReadingsServices.setCurrent(serviceImpl);
   }

   /**
    */
   public void offLineMode () {
      LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSReadingsClientServices");
      CMSReadingsServices serviceImpl = (CMSReadingsServices)config.getObject("CLIENT_DOWNTIME");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSReadingsClientServices", "offLineMode()",
               "Cannot instantiate the class that provides the"
                + " implementation of CMSReadingsServices in readings.cfg.",
               "Make sure that readings.cfg contains an entry with "
                + "a key of CLIENT_DOWNTIME and a value"
               + " that is the name of a class that provides a concrete" +
               " implementation of CMSReadingsServices.", LoggingServices.CRITICAL);
      }
      CMSReadingsServices.setCurrent(serviceImpl);
   }

   public Object getCurrentService () {
      return  CMSReadingsServices.getCurrent();
   }

   /**
    * default-list
    * @param emp emp
    */
   public CMSEmployeeSales[] getSales (Store store, Employee emp) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (CMSEmployeeSales[])CMSReadingsServices.getCurrent().getSales(store, emp);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "getSales",
               "Primary Implementation for CMSReadingsServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (CMSEmployeeSales[])CMSReadingsServices.getCurrent().getSales(store, emp);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * default-list
    * @param emp emp
    * @param date date
    */
   public CMSEmployeeSales[] getSales (Store store, Employee emp, Date date) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (CMSEmployeeSales[])CMSReadingsServices.getCurrent().getSales(store, emp,
               date);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "getSales",
               "Primary Implementation for CMSReadingsServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (CMSEmployeeSales[])CMSReadingsServices.getCurrent().getSales(store, emp,
               date);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * default-list
    * @param store store
    */
   public CMSMerchandiseSales[] getSales (Store store) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (CMSMerchandiseSales[])CMSReadingsServices.getCurrent().getSales(store);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "getSales",
               "Primary Implementation for CMSReadingsServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (CMSMerchandiseSales[])CMSReadingsServices.getCurrent().getSales(store);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * default-list
    * @param store store
    * @param date date
    */
   public CMSMerchandiseSales[] getSales (Store store, Date date) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (CMSMerchandiseSales[])CMSReadingsServices.getCurrent().getSales(store,
               date);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "getSales",
               "Primary Implementation for CMSReadingsServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (CMSMerchandiseSales[])CMSReadingsServices.getCurrent().getSales(store,
               date);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * default-list
    * @param store store
    * @param date date
    */
   public CMSEmployee[] findEmployeesForReadings (Store store, Date date) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (CMSEmployee[])CMSReadingsServices.getCurrent().findEmployeesForReadings(store,
               date);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "findEmployeesForReadings",
               "Primary Implementation for CMSReadingsServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (CMSEmployee[])CMSReadingsServices.getCurrent().findEmployeesForReadings(store,
               date);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * default-list
    * @param emp emp
    */
   public CMSCommissions[] getCommissions (Employee emp) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (CMSCommissions[])CMSReadingsServices.getCurrent().getCommissions(emp);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "getCommissions",
               "Primary Implementation for CMSReadingsServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (CMSCommissions[])CMSReadingsServices.getCurrent().getCommissions(emp);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * default-list
    * @param store store
    * @param date date
    */
   public CMSTrialBalance getTrialBalance (Store store, Date date) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (CMSTrialBalance)CMSReadingsServices.getCurrent().getTrialBalance(store,
               date);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "getTrialBalance",
               "Primary Implementation for CMSReadingsServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (CMSTrialBalance)CMSReadingsServices.getCurrent().getTrialBalance(store,
               date);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * default-list
    * @param store store
    * @param date date
    * @param employee oper
    */
   public CMSTrialBalance getOperatorTrialBalance (Store store, Date date,
         Employee oper) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (CMSTrialBalance)CMSReadingsServices.getCurrent().getOperatorTrialBalance(store,
               date, oper);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "getOperatorTrialBalance",
               "Primary Implementation for CMSReadingsServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (CMSTrialBalance)CMSReadingsServices.getCurrent().getOperatorTrialBalance(store,
               date, oper);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }
}



