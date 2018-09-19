/*
 * @copyright (c) 1998-2002 Chelsea Market Systems LLC
 */


package  com.chelseasystems.cs.scheduling;

import  com.chelseasystems.cr.config.*;
import  com.chelseasystems.cr.logging.*;
import  com.chelseasystems.cr.appmgr.*;
import  com.chelseasystems.cr.scheduling.*;
import  java.lang.*;
import  com.chelseasystems.cr.store.*;
import  com.chelseasystems.cs.store.*;
import  java.util.Date;

/**
 * Client-side object for retrieving and submitting.
 */
public class CMSScheduleClientServices extends ClientServices {

   /** Configuration manager **/
//   private ConfigMgr config = null;

   /**
    * Set the current implementation
    */
   public CMSScheduleClientServices () {
      // Set up the configuration manager.
      config = new ConfigMgr("schedule.cfg");
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
      LoggingServices.getCurrent().logMsg("On-Line Mode for CMSScheduleClientServices");
      CMSScheduleServices serviceImpl = (CMSScheduleServices)config.getObject("CLIENT_IMPL");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSScheduleClientServices", "onLineMode()",
               "Cannot instantiate the class that provides the" + "implementation of CMSScheduleServices in schedule.cfg.",
               "Make sure that schedule.cfg contains an entry with " + "a key of CLIENT_IMPL and a value that is the name of a class "
               + "that provides a concrete implementation of CMSScheduleServices.",
               LoggingServices.MAJOR);
         setOffLineMode();
         return;
      }
      CMSScheduleServices.setCurrent(serviceImpl);
   }

   /**
    */
   public void offLineMode () {
      LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSScheduleClientServices");
      CMSScheduleServices serviceImpl = (CMSScheduleServices)config.getObject("CLIENT_DOWNTIME");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSScheduleClientServices", "offLineMode()",
               "Cannot instantiate the class that provides the" + " implementation of CMSScheduleServices in schedule.cfg.",
               "Make sure that schedule.cfg contains an entry with " + "a key of CLIENT_DOWNTIME and a value"
               + " that is the name of a class that provides a concrete" +
               " implementation of CMSScheduleServices.", LoggingServices.CRITICAL);
      }
      CMSScheduleServices.setCurrent(serviceImpl);
   }

   public Object getCurrentService () {
      return  CMSScheduleServices.getCurrent();
   }

   /**
    * Find a schedule
    * @param id id
    */
   public CMSSchedule findById (String id) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (CMSSchedule)CMSScheduleServices.getCurrent().findById(id);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "findById",
               "Primary Implementation for CMSScheduleServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (CMSSchedule)CMSScheduleServices.getCurrent().findById(id);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * return the schedules of a store
    * @param store store
    */
   public CMSSchedule[] findByStore (CMSStore store) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (CMSSchedule[])CMSScheduleServices.getCurrent().findByStore(store);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "findByStore",
               "Primary Implementation for CMSScheduleServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (CMSSchedule[])CMSScheduleServices.getCurrent().findByStore(store);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * return the schedules of a store
    * @param store store
    * @parm beginDate begin date
    * @parm endDate end date
    */
   public CMSSchedule[] findByStoreAndOverlapDateRange (CMSStore store, Date beginDate, Date endDate) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (CMSSchedule[])CMSScheduleServices.getCurrent().findByStoreAndOverlapDateRange(store, beginDate, endDate);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "findByStore",
               "Primary Implementation for CMSScheduleServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (CMSSchedule[])CMSScheduleServices.getCurrent().findByStoreAndOverlapDateRange(store, beginDate, endDate);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * find array of schedule header the the store.
    * @param store a store
    */
   public ScheduleHeader[]  findHeaderByStore (CMSStore store) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (ScheduleHeader[] ) CMSScheduleServices.getCurrent().findHeaderByStore(store);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findHeaderByStore",
            "Primary Implementation for CMSScheduleServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (ScheduleHeader[] ) CMSScheduleServices.getCurrent().findHeaderByStore(store);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * get an array of schedule for the given store and time period
    * @param store a store
    * @param beginDate the begin date
    * @param endDate the end date
    */
   public ScheduleHeader[]  findByHeaderStoreAndOverlapDateRange (CMSStore store, Date beginDate, Date endDate) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (ScheduleHeader[] ) CMSScheduleServices.getCurrent().findHeaderByStoreAndOverlapDateRange(store, beginDate, endDate);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findHeaderByStoreAndOverlapDateRange",
            "Primary Implementation for CMSScheduleServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (ScheduleHeader[] ) CMSScheduleServices.getCurrent().findHeaderByStoreAndOverlapDateRange(store, beginDate, endDate);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * add a new schedule
    * @param schedule schedule
    */
   public void addSchedule (CMSSchedule schedule) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         CMSScheduleServices.getCurrent().addSchedule(schedule);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "addSchedule",
               "Primary Implementation for CMSScheduleServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         CMSScheduleServices.getCurrent().addSchedule(schedule);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * delete a schedule
    * @param id id
    */
   public void deleteSchedule (String id) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         CMSScheduleServices.getCurrent().deleteSchedule(id);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "deleteSchedule",
               "Primary Implementation for CMSScheduleServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         CMSScheduleServices.getCurrent().deleteSchedule(id);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * update submitted schedule
    * @param schedule schedule
    */
   public void updateSchedule (CMSSchedule schedule) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         CMSScheduleServices.getCurrent().updateSchedule(schedule);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "updateSchedule",
               "Primary Implementation for CMSScheduleServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         CMSScheduleServices.getCurrent().updateSchedule(schedule);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }
}



