/*
 * @copyright (c) 2001 Chelsea Market Systems
 */

package  com.chelseasystems.cs.merchandise;

import  com.chelseasystems.cr.config.*;
import  com.chelseasystems.cr.logging.*;
import  com.chelseasystems.cr.appmgr.*;
import  com.chelseasystems.cr.merchandise.*;
import  com.chelseasystems.cr.rules.*;
import  com.chelseasystems.cs.rules.*;
import  java.util.*;

/**
 * Client-side object for retrieving and submitting.
 */
public class CMSMerchandiseClientServices extends ClientServices {

   /** Configuration manager **/
 //  private ConfigMgr config = null;

   /**
    * Set the current implementation
    */
   public CMSMerchandiseClientServices() {
      // Set up the configuration manager.
      config = new ConfigMgr("merchandise.cfg");
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
      LoggingServices.getCurrent().logMsg("On-Line Mode for CMSMerchandiseClientServices");
      CMSMerchandiseServices serviceImpl = (CMSMerchandiseServices) config.getObject("CLIENT_IMPL");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSMerchandiseClientServices", "onLineMode()",
             "Cannot instantiate the class that provides the"
             + "implementation of CMSMerchandiseServices in merchandise.cfg.",
             "Make sure that merchandise.cfg contains an entry with "
             +"a key of CLIENT_IMPL and a value that is the name of a class "
             +"that provides a concrete implementation of CMSMerchandiseServices.",
             LoggingServices.MAJOR);
         setOffLineMode();
         return;
      }
      CMSMerchandiseServices.setCurrent(serviceImpl);
   }

   public void offLineMode() {
      LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSMerchandiseClientServices");
      CMSMerchandiseServices serviceImpl = (CMSMerchandiseServices) config.getObject("CLIENT_DOWNTIME");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSMerchandiseClientServices", "offLineMode()",
             "Cannot instantiate the class that provides the"
             + " implementation of CMSMerchandiseServices in merchandise.cfg.",
             "Make sure that merchandise.cfg contains an entry with "
             + "a key of CLIENT_DOWNTIME and a value"
             + " that is the name of a class that provides a concrete"
             + " implementation of CMSMerchandiseServices.",
             LoggingServices.CRITICAL);
      }
      CMSMerchandiseServices.setCurrent(serviceImpl);
   }

   public Object getCurrentService () {
      return  CMSMerchandiseServices.getCurrent();
   }

   /**    */
   public Merchandise[]  getMerchandise () throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (Merchandise[] ) CMSMerchandiseServices.getCurrent().getMerchandise();
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"getMerchandise",
            "Primary Implementation for CMSMerchandiseServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (Merchandise[] ) CMSMerchandiseServices.getCurrent().getMerchandise();
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * Method that will delete the merchandise specified.
    * @param aMerchandise aMerchandise
    */
   public RulesInfo deleteMerchandise (Merchandise aMerchandise) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (RulesInfo) CMSMerchandiseServices.getCurrent().deleteMerchandise(aMerchandise);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"deleteMerchandise",
            "Primary Implementation for CMSMerchandiseServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (RulesInfo) CMSMerchandiseServices.getCurrent().deleteMerchandise(aMerchandise);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * Method to add the specified merchandise to the rest
    * @param aMerchandise aMerchandise
    */
   public RulesInfo addMerchandise (Merchandise aMerchandise) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (RulesInfo) CMSMerchandiseServices.getCurrent().addMerchandise(aMerchandise);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"addMerchandise",
            "Primary Implementation for CMSMerchandiseServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (RulesInfo) CMSMerchandiseServices.getCurrent().addMerchandise(aMerchandise);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    */
   public CorpMsg[]  getCorpMsg () throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (CorpMsg[] ) CMSMerchandiseServices.getCurrent().getCorpMsg();
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"getCorpMsg",
            "Primary Implementation for CMSMerchandiseServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (CorpMsg[] ) CMSMerchandiseServices.getCurrent().getCorpMsg();
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * Method to get all the corporate messages valid for the specified date
    * @param aDate aDate
    */
   public CorpMsg[]  getCorpMsg (Date aDate) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (CorpMsg[] ) CMSMerchandiseServices.getCurrent().getCorpMsg(aDate);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"getCorpMsg",
            "Primary Implementation for CMSMerchandiseServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (CorpMsg[] ) CMSMerchandiseServices.getCurrent().getCorpMsg(aDate);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * Method to add corporate messages to the rest
    * @param aCorpMsg aCorpMsg
    */
   public RulesInfo addCorpMsg (CorpMsg aCorpMsg) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (RulesInfo) CMSMerchandiseServices.getCurrent().addCorpMsg(aCorpMsg);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"addCorpMsg",
            "Primary Implementation for CMSMerchandiseServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (RulesInfo) CMSMerchandiseServices.getCurrent().addCorpMsg(aCorpMsg);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * Method to delete the specified corporate message
    * @param aCoprMsg aCoprMsg
    */
   public RulesInfo deleteCorpMsg (CorpMsg aCoprMsg) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (RulesInfo) CMSMerchandiseServices.getCurrent().deleteCorpMsg(aCoprMsg);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"deleteCorpMsg",
            "Primary Implementation for CMSMerchandiseServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (RulesInfo) CMSMerchandiseServices.getCurrent().deleteCorpMsg(aCoprMsg);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

}
