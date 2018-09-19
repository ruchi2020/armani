// Copyright 2001,chelseamarketsystem

package com.chelseasystems.cs.authorization.bankcard;

import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.authorization.bankcard.*;
import java.lang.*;

/**
* Client-side object for retrieving and submitting.
**/
public class CMSCreditAuthClientServices extends ClientServices {

   /** Configuration manager **/
//   private ConfigMgr config = null;

   /**
   * Set the current implementation
   **/
   public CMSCreditAuthClientServices() {
      // Set up the configuration manager.
      config = new ConfigMgr("credit_auth.cfg");
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
      LoggingServices.getCurrent().logMsg("On-Line Mode for CMSCreditAuthClientServices");
      CMSCreditAuthServices serviceImpl = (CMSCreditAuthServices) config.getObject("CLIENT_IMPL");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSCreditAuthClientServices", "onLineMode()",
             "Cannot instantiate the class that provides the"
             + "implementation of CMSCreditAuthServices in credit_auth.cfg.",
             "Make sure that credit_auth.cfg contains an entry with "
             +"a key of CLIENT_IMPL and a value that is the name of a class "
             +"that provides a concrete implementation of CMSCreditAuthServices.",
             LoggingServices.MAJOR);
         setOffLineMode();
         return;
      }
     CMSCreditAuthServices.setCurrent(serviceImpl);
   }

   public void offLineMode() {
      LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSCreditAuthClientServices");
      CMSCreditAuthServices serviceImpl = (CMSCreditAuthServices) config.getObject("CLIENT_DOWNTIME");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSCreditAuthClientServices", "offLineMode()",
             "Cannot instantiate the class that provides the"
             + " implementation of CMSCreditAuthServices in credit_auth.cfg.",
             "Make sure that credit_auth.cfg contains an entry with "
             + "a key of CLIENT_DOWNTIME and a value"
             + " that is the name of a class that provides a concrete"
             + " implementation of CMSCreditAuthServices.",
             LoggingServices.CRITICAL);
      }
    CMSCreditAuthServices.setCurrent(serviceImpl);
   }

   public Object getCurrentService () {
      return  CMSCreditAuthServices.getCurrent();
   }

   /**get authorizations for requests
   *@parm requests requests
   **/
    public Object[]  getAuth(Object[] requests) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return CMSCreditAuthServices.getCurrent().getAuth(requests);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"getAuth",
            "Primary Implementation for CMSCreditAuthServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return CMSCreditAuthServices.getCurrent().getAuth(requests);
      } finally {
        this.fireWorkInProgressEvent(false);
      }
   }

   /**
   * Returns reply for each DCC request
   * @param   s  A string containing a request for DCC information
   * @return     A string containing DCC information
   */
  public Object getDCC( Object requests) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return CMSCreditAuthServices.getCurrent().getDCC(requests);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"getDCC",
            "Primary Implementation for CMSCreditAuthServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return CMSCreditAuthServices.getCurrent().getDCC(requests);
      } finally {
        this.fireWorkInProgressEvent(false);
      }
  }

  /**
   * Mark previously authorized transactions as confirmed or canceled
   * This is used when the operator validates the signature.
   * @param   s  A string containing a request for DCC information
   * @return     A string containing DCC information
   */
  public Object setSignatureValidation ( Object requests) throws Exception {
    try {
         this.fireWorkInProgressEvent(true);
         return CMSCreditAuthServices.getCurrent().setSignatureValidation(requests);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"getSignatureValidation",
            "Primary Implementation for CMSCreditAuthServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  CMSCreditAuthServices.getCurrent().setSignatureValidation (requests);
      } finally {
        this.fireWorkInProgressEvent(false);
      }
  }
  public Object setAuthCancelled ( Object requests) throws Exception {
    try {
         this.fireWorkInProgressEvent(true);
         return CMSCreditAuthServices.getCurrent().setAuthCancelled(requests);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"getSignatureValidation",
            "Primary Implementation for CMSCreditAuthServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  CMSCreditAuthServices.getCurrent().setAuthCancelled (requests);
      } finally {
        this.fireWorkInProgressEvent(false);
      }
  }

}
