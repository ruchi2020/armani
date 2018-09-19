// Copyright 2001,Chelsea Market Systems

package com.chelseasystems.cs.layaway;

import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.layaway.*;
import java.lang.*;
import java.util.Date;

/**
* Client-side object for retrieving and submitting.
**/
public class CMSLayawayClientServices extends ClientServices {

   /** Configuration manager **/
//   private ConfigMgr config = null;

   /**
   * Set the current implementation
   **/
   public CMSLayawayClientServices() {
      // Set up the configuration manager.
      config = new ConfigMgr("layaway.cfg");
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
      LoggingServices.getCurrent().logMsg("On-Line Mode for CMSLayawayClientServices");
      CMSLayawayServices serviceImpl = (CMSLayawayServices) config.getObject("CLIENT_IMPL");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSLayawayClientServices", "onLineMode()",
             "Cannot instantiate the class that provides the"
             + "implementation of CMSLayawayServices in layaway.cfg.",
             "Make sure that layaway.cfg contains an entry with "
             +"a key of CLIENT_IMPL and a value that is the name of a class "
             +"that provides a concrete implementation of CMSLayawayServices.",
             LoggingServices.MAJOR);
         setOffLineMode();
         return;
      }
     CMSLayawayServices.setCurrent(serviceImpl);
   }

   public void offLineMode() {
      LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSLayawayClientServices");
      CMSLayawayServices serviceImpl = (CMSLayawayServices) config.getObject("CLIENT_DOWNTIME");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSLayawayClientServices", "offLineMode()",
             "Cannot instantiate the class that provides the"
             + " implementation of CMSLayawayServices in layaway.cfg.",
             "Make sure that layaway.cfg contains an entry with "
             + "a key of CLIENT_DOWNTIME and a value"
             + " that is the name of a class that provides a concrete"
             + " implementation of CMSLayawayServices.",
             LoggingServices.CRITICAL);
      }
    CMSLayawayServices.setCurrent(serviceImpl);
   }

   public Object getCurrentService () {
      return  CMSLayawayServices.getCurrent();
   }

   /**search by layaway id
   *@parm id id
   **/
    public CMSLayaway findById(String id) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (CMSLayaway) CMSLayawayServices.getCurrent().findById(id);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findById",
            "Primary Implementation for CMSLayawayServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (CMSLayaway) CMSLayawayServices.getCurrent().findById(id);
      } finally {
        this.fireWorkInProgressEvent(false);
      }
   }
   /**search by customer id
   *@parm customerId customerId
   **/
    public CMSLayaway[] findByCustomerId(String customerId) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (CMSLayaway[]) CMSLayawayServices.getCurrent().findByCustomerId(customerId);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findByCustomerId",
            "Primary Implementation for CMSLayawayServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (CMSLayaway[]) CMSLayawayServices.getCurrent().findByCustomerId(customerId);
      } finally {
        this.fireWorkInProgressEvent(false);
      }
   }
   /**search non-zero balance layaway by store id
   *@parm storeId storeId
   **/
    public CMSLayaway[] findCurrentByStoreId(String storeId) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (CMSLayaway[]) CMSLayawayServices.getCurrent().findCurrentByStoreId(storeId);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findCurrentByStoreId",
            "Primary Implementation for CMSLayawayServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (CMSLayaway[]) CMSLayawayServices.getCurrent().findCurrentByStoreId(storeId);
      } finally {
        this.fireWorkInProgressEvent(false);
      }
   }
   /**search overdue layaway by store id
   *@parm storeId storeId
   *@parm shortName shortName
   **/
    public CMSLayaway[] findOverDueByStoreId(String storeId,String shortName) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (CMSLayaway[]) CMSLayawayServices.getCurrent().findOverDueByStoreId(storeId,shortName);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findOverDueByStoreId",
            "Primary Implementation for CMSLayawayServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (CMSLayaway[]) CMSLayawayServices.getCurrent().findOverDueByStoreId(storeId,shortName);
      } finally {
        this.fireWorkInProgressEvent(false);
      }
   }

   /**search overdue layaway by store id
   *@parm storeId storeId
   *@parm shortName shortName
   *@parm aBeginDate a begin date to search against
   **/
    public CMSLayaway[] findOverDueByStoreId(String storeId,String shortName, Date aBeginDate) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (CMSLayaway[]) CMSLayawayServices.getCurrent().findOverDueByStoreId(storeId,shortName, aBeginDate);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findOverDueByStoreId",
            "Primary Implementation for CMSLayawayServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (CMSLayaway[]) CMSLayawayServices.getCurrent().findOverDueByStoreId(storeId,shortName,aBeginDate);
      } finally {
        this.fireWorkInProgressEvent(false);
      }
   }

}
