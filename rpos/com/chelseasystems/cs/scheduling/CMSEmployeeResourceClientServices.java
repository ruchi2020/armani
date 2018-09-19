// Copyright 2001,

package com.chelseasystems.cs.scheduling;

import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.scheduling.*;
import com.chelseasystems.cs.store.*;
import com.chelseasystems.cr.store.*;
import java.lang.*;

/**
* Client-side object for retrieving and submitting.
**/
public class CMSEmployeeResourceClientServices extends ClientServices {

   /** Configuration manager **/
//   private ConfigMgr config = null;

   /**
   * Set the current implementation
   **/
   public CMSEmployeeResourceClientServices() {
      // Set up the configuration manager.
      config = new ConfigMgr("emp_resource.cfg");
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
      LoggingServices.getCurrent().logMsg("On-Line Mode for CMSEmployeeResourceClientServices");
      CMSEmployeeResourceServices serviceImpl = (CMSEmployeeResourceServices) config.getObject("CLIENT_IMPL");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSEmployeeResourceClientServices", "onLineMode()",
             "Cannot instantiate the class that provides the"
             + "implementation of CMSEmployeeResourceServices in emp_resource.cfg.",
             "Make sure that emp_resource.cfg contains an entry with "
             +"a key of CLIENT_IMPL and a value that is the name of a class "
             +"that provides a concrete implementation of CMSEmployeeResourceServices.",
             LoggingServices.MAJOR);
         setOffLineMode();
         return;
      }
     CMSEmployeeResourceServices.setCurrent(serviceImpl);
   }

   public void offLineMode() {
      LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSEmployeeResourceClientServices");
      CMSEmployeeResourceServices serviceImpl = (CMSEmployeeResourceServices) config.getObject("CLIENT_DOWNTIME");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSEmployeeResourceClientServices", "offLineMode()",
             "Cannot instantiate the class that provides the"
             + " implementation of CMSEmployeeResourceServices in emp_resource.cfg.",
             "Make sure that emp_resource.cfg contains an entry with "
             + "a key of CLIENT_DOWNTIME and a value"
             + " that is the name of a class that provides a concrete"
             + " implementation of CMSEmployeeResourceServices.",
             LoggingServices.CRITICAL);
      }
    CMSEmployeeResourceServices.setCurrent(serviceImpl);
   }

   public Object getCurrentService () {
      return  CMSEmployeeResourceServices.getCurrent();
   }

   /**Return EmployeeResource of an employee.  An new one will be returned if the employee does not have one.
   *@parm empId empId
   **/
    public CMSEmployeeResource findById(String empId) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (CMSEmployeeResource) CMSEmployeeResourceServices.getCurrent().findById(empId);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findById",
            "Primary Implementation for CMSEmployeeResourceServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (CMSEmployeeResource) CMSEmployeeResourceServices.getCurrent().findById(empId);
      } finally {
        this.fireWorkInProgressEvent(false);
      }
   }

   /**
   **/
    public CMSEmployeeResource[] findByStore(Store store) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (CMSEmployeeResource[]) CMSEmployeeResourceServices.getCurrent().findByStore(store);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findByStore",
            "Primary Implementation for CMSEmployeeResourceServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (CMSEmployeeResource[]) CMSEmployeeResourceServices.getCurrent().findByStore(store);
      } finally {
        this.fireWorkInProgressEvent(false);
      }
   }


   /**add or update EmployeeResource
   *@parm employeeResource employeeResource
   **/
    public void submitEmployeeResource(CMSEmployeeResource employeeResource) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         CMSEmployeeResourceServices.getCurrent().submitEmployeeResource(employeeResource);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"submitEmployeeResource",
            "Primary Implementation for CMSEmployeeResourceServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         CMSEmployeeResourceServices.getCurrent().submitEmployeeResource(employeeResource);
      } finally {
        this.fireWorkInProgressEvent(false);
      }
   }
}
