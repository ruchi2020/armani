// Copyright 2001,

package com.chelseasystems.cs.goaling;

import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.goaling.*;
import java.lang.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.employee.*;
import com.chelseasystems.cs.store.*;
import com.chelseasystems.cs.employee.*;

/**
* Client-side object for retrieving and submitting.
**/
public class CMSGoalingClientServices extends ClientServices {

   /** Configuration manager **/
//   private ConfigMgr config = null;

   /**
   * Set the current implementation
   **/
   public CMSGoalingClientServices() {
      // Set up the configuration manager.
      config = new ConfigMgr("goal.cfg");
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
      LoggingServices.getCurrent().logMsg("On-Line Mode for CMSGoalingClientServices");
      CMSGoalingServices serviceImpl = (CMSGoalingServices) config.getObject("CLIENT_IMPL");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSGoalingClientServices", "onLineMode()",
             "Cannot instantiate the class that provides the"
             + "implementation of CMSGoalingServices in goal.cfg.",
             "Make sure that goal.cfg contains an entry with "
             +"a key of CLIENT_IMPL and a value that is the name of a class "
             +"that provides a concrete implementation of CMSGoalingServices.",
             LoggingServices.MAJOR);
         setOffLineMode();
         return;
      }
     CMSGoalingServices.setCurrent(serviceImpl);
   }

   public void offLineMode() {
      LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSGoalingClientServices");
      CMSGoalingServices serviceImpl = (CMSGoalingServices) config.getObject("CLIENT_DOWNTIME");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSGoalingClientServices", "offLineMode()",
             "Cannot instantiate the class that provides the"
             + " implementation of CMSGoalingServices in goal.cfg.",
             "Make sure that goal.cfg contains an entry with "
             + "a key of CLIENT_DOWNTIME and a value"
             + " that is the name of a class that provides a concrete"
             + " implementation of CMSGoalingServices.",
             LoggingServices.CRITICAL);
      }
    CMSGoalingServices.setCurrent(serviceImpl);
   }

   public Object getCurrentService () {
      return  CMSGoalingServices.getCurrent();
   }

   /**get the StoreGoal for a store
   *@parm store store
   *@parm theOperator theOperator
   **/
    public CMSStoreGoal getStoreGoal(CMSStore store,CMSEmployee theOperator) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (CMSStoreGoal) CMSGoalingServices.getCurrent().getStoreGoal(store,theOperator);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"getStoreGoal",
            "Primary Implementation for CMSGoalingServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (CMSStoreGoal) CMSGoalingServices.getCurrent().getStoreGoal(store,theOperator);
      } finally {
        this.fireWorkInProgressEvent(false);
      }
   }
   /**add or update a store goal
   *@parm storeGoal storeGoal
   **/
    public boolean submit(CMSStoreGoal storeGoal) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (boolean) CMSGoalingServices.getCurrent().submit(storeGoal);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"submit",
            "Primary Implementation for CMSGoalingServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (boolean) CMSGoalingServices.getCurrent().submit(storeGoal);
      } finally {
        this.fireWorkInProgressEvent(false);
      }
   }
}
