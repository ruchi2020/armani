// Copyright 2001,

package com.chelseasystems.cs.scheduling;

import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.scheduling.*;
import java.lang.*;

/**
* Client-side object for retrieving and submitting.
**/
public class CMSRoleClientServices extends ClientServices {

   /** Configuration manager **/
//   private ConfigMgr config = null;

   /**
   * Set the current implementation
   **/
   public CMSRoleClientServices() {
      // Set up the configuration manager.
      config = new ConfigMgr("role.cfg");
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
      LoggingServices.getCurrent().logMsg("On-Line Mode for CMSRoleClientServices");
      CMSRoleServices serviceImpl = (CMSRoleServices) config.getObject("CLIENT_IMPL");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSRoleClientServices", "onLineMode()",
             "Cannot instantiate the class that provides the"
             + "implementation of CMSRoleServices in role.cfg.",
             "Make sure that role.cfg contains an entry with "
             +"a key of CLIENT_IMPL and a value that is the name of a class "
             +"that provides a concrete implementation of CMSRoleServices.",
             LoggingServices.MAJOR);
         setOffLineMode();
         return;
      }
     CMSRoleServices.setCurrent(serviceImpl);
   }

   public void offLineMode() {
      LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSRoleClientServices");
      CMSRoleServices serviceImpl = (CMSRoleServices) config.getObject("CLIENT_DOWNTIME");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSRoleClientServices", "offLineMode()",
             "Cannot instantiate the class that provides the"
             + " implementation of CMSRoleServices in role.cfg.",
             "Make sure that role.cfg contains an entry with "
             + "a key of CLIENT_DOWNTIME and a value"
             + " that is the name of a class that provides a concrete"
             + " implementation of CMSRoleServices.",
             LoggingServices.CRITICAL);
      }
    CMSRoleServices.setCurrent(serviceImpl);
   }

   public Object getCurrentService () {
      return  CMSRoleServices.getCurrent();
   }

   /**find Role by its unique id
   *@parm id id
   **/
    public CMSRole findById(String id) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (CMSRole) CMSRoleServices.getCurrent().findById(id);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findById",
            "Primary Implementation for CMSRoleServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (CMSRole) CMSRoleServices.getCurrent().findById(id);
      } finally {
        this.fireWorkInProgressEvent(false);
      }
   }
   /**   **/
    public CMSRole[] findAllRoles() throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (CMSRole[]) CMSRoleServices.getCurrent().findAllRoles();
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findAllRoles",
            "Primary Implementation for CMSRoleServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (CMSRole[]) CMSRoleServices.getCurrent().findAllRoles();
      } finally {
        this.fireWorkInProgressEvent(false);
      }
   }
}
