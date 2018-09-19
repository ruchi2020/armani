/*
 * @copyright (c) 2001 Chelsea Market Systems
 */

package  com.chelseasystems.cs.forms;

import  com.chelseasystems.cr.config.*;
import  com.chelseasystems.cr.logging.*;
import  com.chelseasystems.cr.appmgr.*;
import  com.chelseasystems.cr.forms.*;
import  java.lang.*;

/**
 * Client-side object for retrieving and submitting.
 */
public class CMSFormClientServices extends ClientServices {

   /** Configuration manager **/
//   private ConfigMgr config = null;

   /**
    * Set the current implementation
    */
   public CMSFormClientServices() {
      // Set up the configuration manager.
      config = new ConfigMgr("form.cfg");
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
      LoggingServices.getCurrent().logMsg("On-Line Mode for CMSFormClientServices");
      CMSFormServices serviceImpl = (CMSFormServices) config.getObject("CLIENT_IMPL");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSFormClientServices", "onLineMode()",
             "Cannot instantiate the class that provides the"
             + "implementation of CMSFormServices in form.cfg.",
             "Make sure that form.cfg contains an entry with "
             +"a key of CLIENT_IMPL and a value that is the name of a class "
             +"that provides a concrete implementation of CMSFormServices.",
             LoggingServices.MAJOR);
         setOffLineMode();
         return;
      }
      CMSFormServices.setCurrent(serviceImpl);
   }

   public void offLineMode() {
      LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSFormClientServices");
      CMSFormServices serviceImpl = (CMSFormServices) config.getObject("CLIENT_DOWNTIME");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSFormClientServices", "offLineMode()",
             "Cannot instantiate the class that provides the"
             + " implementation of CMSFormServices in form.cfg.",
             "Make sure that form.cfg contains an entry with "
             + "a key of CLIENT_DOWNTIME and a value"
             + " that is the name of a class that provides a concrete"
             + " implementation of CMSFormServices.",
             LoggingServices.CRITICAL);
      }
      CMSFormServices.setCurrent(serviceImpl);
   }

   public Object getCurrentService () {
      return  CMSFormServices.getCurrent();
   }

   /**
    * Retrieve a <code>W4</code> object from a database.
    * @param ssn ssn
    */
   public CMSW4 findW4 (String ssn) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (CMSW4) CMSFormServices.getCurrent().findW4(ssn);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findW4",
            "Primary Implementation for CMSFormServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (CMSW4) CMSFormServices.getCurrent().findW4(ssn);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * Post a <code>Form</code> object to a database.
    * @param aForm aForm
    */
   public boolean submitForm (Form aForm) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (boolean) CMSFormServices.getCurrent().submitForm(aForm);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"submitForm",
            "Primary Implementation for CMSFormServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (boolean) CMSFormServices.getCurrent().submitForm(aForm);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * Retrieve a <code>VacationRequest</code> object from a database.
    * @param ssn ssn
    */
   public CMSVacationRequest findVacationRequest (String ssn) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (CMSVacationRequest) CMSFormServices.getCurrent().findVacationRequest(ssn);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findVacationRequest",
            "Primary Implementation for CMSFormServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (CMSVacationRequest) CMSFormServices.getCurrent().findVacationRequest(ssn);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * Retrieve an <code>Employment Application</code> object from a database.
    * @param ssn ssn
    */
   public CMSEmploymentApplication findEmploymentApplication (String ssn) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (CMSEmploymentApplication) CMSFormServices.getCurrent().findEmploymentApplication(ssn);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findEmploymentApplication",
            "Primary Implementation for CMSFormServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (CMSEmploymentApplication) CMSFormServices.getCurrent().findEmploymentApplication(ssn);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * Retrieve an <code>I9</code> object from a database.
    * @param ssn ssn
    */
   public CMSI9 findI9 (String ssn) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (CMSI9) CMSFormServices.getCurrent().findI9(ssn);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findI9",
            "Primary Implementation for CMSFormServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (CMSI9) CMSFormServices.getCurrent().findI9(ssn);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * Retrieve a <code>Form</code> object by specifying its unique type and id.
    * @param type type
    * @param id id
    */
   public Form findFormByType (String type,String id) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (Form) CMSFormServices.getCurrent().findFormByType(type,id);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findFormByType",
            "Primary Implementation for CMSFormServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (Form) CMSFormServices.getCurrent().findFormByType(type,id);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * Submit a <code>Form</code> object to a database for purposes of updating.
    * @param aForm aForm
    */
   public boolean updateForm (Form aForm) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (boolean) CMSFormServices.getCurrent().updateForm(aForm);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"updateForm",
            "Primary Implementation for CMSFormServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (boolean) CMSFormServices.getCurrent().updateForm(aForm);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

}
