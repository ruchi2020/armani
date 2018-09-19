/*
 * @copyright (c) 2003 Retek
 */

package  com.chelseasystems.cs.pricing;

import  com.chelseasystems.cr.config.*;
import  com.chelseasystems.cr.logging.*;
import  com.chelseasystems.cr.appmgr.*;
import  com.chelseasystems.cr.pricing.*;
import  com.chelseasystems.cr.store.*;
import  com.chelseasystems.cs.store.*;

/**
 * Client-side object for retrieving and submitting.
 */
public class CMSPromotionClientServices extends ClientServices {

   /** Configuration manager **/
//   private ConfigMgr config = null;

   /**
    * Set the current implementation
    */
   public CMSPromotionClientServices() {
      // Set up the configuration manager.
      config = new ConfigMgr("promotion.cfg");
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
      LoggingServices.getCurrent().logMsg("On-Line Mode for CMSPromotionClientServices");
      CMSPromotionServices serviceImpl = (CMSPromotionServices) config.getObject("CLIENT_IMPL");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSPromotionClientServices", "onLineMode()",
             "Cannot instantiate the class that provides the"
             + "implementation of CMSPromotionServices in promotion.cfg.", 
             "Make sure that promotion.cfg contains an entry with "
             +"a key of CLIENT_IMPL and a value that is the name of a class "
             +"that provides a concrete implementation of CMSPromotionServices.",
             LoggingServices.MAJOR);
         setOffLineMode();
         return;
      }
      CMSPromotionServices.setCurrent(serviceImpl);
   }

   public void offLineMode() {
      LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSPromotionClientServices");
      CMSPromotionServices serviceImpl = (CMSPromotionServices) config.getObject("CLIENT_DOWNTIME");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSPromotionClientServices", "offLineMode()",
             "Cannot instantiate the class that provides the"
             + " implementation of CMSPromotionServices in promotion.cfg.",
             "Make sure that promotion.cfg contains an entry with "
             + "a key of CLIENT_DOWNTIME and a value"
             + " that is the name of a class that provides a concrete"
             + " implementation of CMSPromotionServices.",
             LoggingServices.CRITICAL);
      }
      CMSPromotionServices.setCurrent(serviceImpl);
   }

      public Object getCurrentService () {
         return  CMSPromotionServices.getCurrent();
      }

   /**
    * Method to get an IPromotion by Id. 
    * @param id specified Id.
    */
   public IPromotion findById (String id) throws Exception { 
      try {
         this.fireWorkInProgressEvent(true);
         return (IPromotion) CMSPromotionServices.getCurrent().findById(id);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findById(String)",
            "Primary Implementation for CMSPromotionServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (IPromotion) CMSPromotionServices.getCurrent().findById(id);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * Method to return all IPromotion entries for a specified Store. 
    * @param aStore specified store.
    */
   public IPromotion[] findAllForStore (Store aStore) throws Exception { 
      try {
         this.fireWorkInProgressEvent(true);
         return (IPromotion[]) CMSPromotionServices.getCurrent().findAllForStore(aStore);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findAllForStore(Store)",
            "Primary Implementation for CMSPromotionServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (IPromotion[]) CMSPromotionServices.getCurrent().findAllForStore(aStore);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * Method to get a ThresholdPromotion by Id. 
    * @param id specified Id.
    */
   public ThresholdPromotion findThresholdPromotionById (String id) throws Exception { 
      try {
         this.fireWorkInProgressEvent(true);
         return (ThresholdPromotion) CMSPromotionServices.getCurrent().findThresholdPromotionById(id);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findThresholdPromotionById(String)",
            "Primary Implementation for CMSPromotionServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (ThresholdPromotion) CMSPromotionServices.getCurrent().findThresholdPromotionById(id);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * Method to return all ThresholdPromotions for a Store. 
    * @param aStore specified store.
    */
   public ThresholdPromotion[] findThresholdPromotionsForStore (Store aStore) throws Exception { 
      try {
         this.fireWorkInProgressEvent(true);
         return (ThresholdPromotion[]) CMSPromotionServices.getCurrent().findThresholdPromotionsForStore(aStore);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findThresholdPromotionsForStore(Store)",
            "Primary Implementation for CMSPromotionServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (ThresholdPromotion[]) CMSPromotionServices.getCurrent().findThresholdPromotionsForStore(aStore);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * Insert specified IPromotion into storage. 
    * @param promotion specified promotion
    */
   public void insert (IPromotion promotion) throws Exception { 
      try {
         this.fireWorkInProgressEvent(true);
         CMSPromotionServices.getCurrent().insert(promotion);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"insert(IPromotion)",
            "Primary Implementation for CMSPromotionServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         CMSPromotionServices.getCurrent().insert(promotion);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

}
