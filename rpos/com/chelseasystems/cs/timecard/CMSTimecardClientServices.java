/*
 * @copyright (c) 1998-2002 Chelsea Market Systems LLC
 */


package  com.chelseasystems.cs.timecard;

import  com.chelseasystems.cr.config.*;
import  com.chelseasystems.cr.logging.*;
import  com.chelseasystems.cr.appmgr.*;
import  com.chelseasystems.cr.timecard.*;
import  java.lang.*;
import  com.chelseasystems.cr.employee.*;
import  java.util.*;
import  com.chelseasystems.cr.store.*;
import  com.chelseasystems.cs.employee.*;
import  com.chelseasystems.cs.store.*;


/**
 * Client-side object for retrieving and submitting.
 */
public class CMSTimecardClientServices extends ClientServices {

   /** Configuration manager **/
//   private ConfigMgr config = null;

   /**
    * Set the current implementation
    */
   public CMSTimecardClientServices () {
      // Set up the configuration manager.
      config = new ConfigMgr("timecard.cfg");
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
      LoggingServices.getCurrent().logMsg("On-Line Mode for CMSTimecardClientServices");
      CMSTimecardServices serviceImpl = (CMSTimecardServices)config.getObject("CLIENT_IMPL");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSTimecardClientServices", "onLineMode()",
               "Cannot instantiate the class that provides the" + "implementation of CMSTimecardServices in timecard.",
               "Make sure that timecard contains an entry with " + "a key of CLIENT_IMPL and a value that is the name of a class "
               + "that provides a concrete implementation of CMSTimecardServices.",
               LoggingServices.MAJOR);
         setOffLineMode();
         return;
      }
      CMSTimecardServices.setCurrent(serviceImpl);
   }

   /**
    */
   public void offLineMode () {
      LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSTimecardClientServices");
      CMSTimecardServices serviceImpl = (CMSTimecardServices)config.getObject("CLIENT_DOWNTIME");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSTimecardClientServices", "offLineMode()",
               "Cannot instantiate the class that provides the" + " implementation of CMSTimecardServices in timecard.",
               "Make sure that timecard contains an entry with " + "a key of CLIENT_DOWNTIME and a value"
               + " that is the name of a class that provides a concrete" +
               " implementation of CMSTimecardServices.", LoggingServices.CRITICAL);
      }
      CMSTimecardServices.setCurrent(serviceImpl);
   }

   public Object getCurrentService () {
      return  CMSTimecardServices.getCurrent();
   }

   /**
    * submit an array of timecard transaction
    * @param transactionTimecards transactionTimecards
    */
   public boolean submitTimecard (TransactionTimecard transactionTimecards) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (boolean)CMSTimecardServices.getCurrent().submitTimecard(transactionTimecards);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "submitTimecard",
               "Primary Implementation for CMSTimecardServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (boolean)CMSTimecardServices.getCurrent().submitTimecard(transactionTimecards);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

  /**
   * Perform the finalization process for the employees
   * @parm employees the employees whose timecard to be finalized
   * @Parm finalizedTime the time the request was submitted
   * @parm finalizedManager the manager who approved the timecards
   * @parm weekEndingDate the week ending date for the timecards
   */
   public boolean processFinalization(Employee[] employees, Calendar finalizedTime, Employee finalizedManager, Calendar weekEndingDate) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (boolean)((CMSTimecardServices)CMSTimecardServices.getCurrent()).processFinalization(employees, finalizedTime, finalizedManager, weekEndingDate);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "processFinalization",
               "Primary Implementation for CMSTimecardServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (boolean)((CMSTimecardServices)CMSTimecardServices.getCurrent()).processFinalization(employees, finalizedTime, finalizedManager, weekEndingDate);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * submit an array of timecard transaction
    * @param transactionTimecards transactionTimecards
    */
   public EmployeeTimecard submitTimecard (TransactionTimecard[] transactionTimecards) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (EmployeeTimecard)CMSTimecardServices.getCurrent().submitTimecard(transactionTimecards);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "submitTimecard",
               "Primary Implementation for CMSTimecardServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (EmployeeTimecard)CMSTimecardServices.getCurrent().submitTimecard(transactionTimecards);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * find an employee timecard
    * @param employee employee
    * @param requestDate requestDate
    * @deprecated use findTimecard (CMSEmployee, String)
    */
   public EmployeeTimecard findTimecard (CMSEmployee employee, Date requestDate) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (EmployeeTimecard)CMSTimecardServices.getCurrent().findTimecard(employee,
               requestDate);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "findTimecard",
               "Primary Implementation for CMSTimecardServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (EmployeeTimecard)CMSTimecardServices.getCurrent().findTimecard(employee,
               requestDate);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * find an employee timecard
    * @param employee employee
    * @param requestDate requestDate
    */
   public EmployeeTimecard findTimecard (CMSEmployee employee, String requestDate) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (EmployeeTimecard)CMSTimecardServices.getCurrent().findTimecard(employee,
               requestDate);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "findTimecard",
               "Primary Implementation for CMSTimecardServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (EmployeeTimecard)CMSTimecardServices.getCurrent().findTimecard(employee,
               requestDate);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * get the clock-in timecard for a given store
    * @param storeId storeId
    */
   public EmployeeTimecard[] getOnClockEmployees (String storeId) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (EmployeeTimecard[])CMSTimecardServices.getCurrent().getOnClockEmployees(storeId);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "getOnClockEmployees",
               "Primary Implementation for CMSTimecardServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (EmployeeTimecard[])CMSTimecardServices.getCurrent().getOnClockEmployees(storeId);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * Find all employees to finalize at a given store.
    * @param store store
    */
   public EmployeeTimecard[] getEmployees (CMSStore store) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (EmployeeTimecard[])CMSTimecardServices.getCurrent().getEmployees(store);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "getEmployees",
               "Primary Implementation for CMSTimecardServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (EmployeeTimecard[])CMSTimecardServices.getCurrent().getEmployees(store);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }
}



