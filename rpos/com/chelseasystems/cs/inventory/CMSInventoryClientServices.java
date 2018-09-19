/*
 * @copyright (c) 2001 Chelsea Market Systems
 */

package  com.chelseasystems.cs.inventory;

import  com.chelseasystems.cr.config.*;
import  com.chelseasystems.cr.logging.*;
import  com.chelseasystems.cr.appmgr.*;
import  com.chelseasystems.cr.inventory.*;
import  com.chelseasystems.cr.item.*;
import  com.chelseasystems.cr.store.*;
import  com.chelseasystems.cs.item.*;
import  com.chelseasystems.cs.store.*;

/**
 * Client-side object for retrieving and submitting.
 */
public class CMSInventoryClientServices extends ClientServices {

   /** Configuration manager **/
 //  private ConfigMgr config = null;

   /**
    * Set the current implementation
    */
   public CMSInventoryClientServices() {
      // Set up the configuration manager.
      config = new ConfigMgr("inventory.cfg");
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
      LoggingServices.getCurrent().logMsg("On-Line Mode for CMSInventoryClientServices");
      CMSInventoryServices serviceImpl = (CMSInventoryServices) config.getObject("CLIENT_IMPL");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSInventoryClientServices", "onLineMode()",
             "Cannot instantiate the class that provides the"
             + "implementation of CMSInventoryServices in inventory.cfg.",
             "Make sure that inventory.cfg contains an entry with "
             +"a key of CLIENT_IMPL and a value that is the name of a class "
             +"that provides a concrete implementation of CMSInventoryServices.",
             LoggingServices.MAJOR);
         setOffLineMode();
         return;
      }
      CMSInventoryServices.setCurrent(serviceImpl);
   }

   public void offLineMode() {
      LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSInventoryClientServices");
      CMSInventoryServices serviceImpl = (CMSInventoryServices) config.getObject("CLIENT_DOWNTIME");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSInventoryClientServices", "offLineMode()",
             "Cannot instantiate the class that provides the"
             + " implementation of CMSInventoryServices in inventory.cfg.",
             "Make sure that inventory.cfg contains an entry with "
             + "a key of CLIENT_DOWNTIME and a value"
             + " that is the name of a class that provides a concrete"
             + " implementation of CMSInventoryServices.",
             LoggingServices.CRITICAL);
      }
      CMSInventoryServices.setCurrent(serviceImpl);
   }

   public Object getCurrentService () {
      return  CMSInventoryServices.getCurrent();
   }

   /**
   * Return a list of store inventory objects for the stores where an item is available.
    * @param itemCode the item code
    */
   public CMSStoreInventory[]  findStoresWithItemAvailable (String itemCode) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (CMSStoreInventory[]) CMSInventoryServices.getCurrent().findStoresWithItemAvailable(itemCode);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findStoresWithItemAvailable",
            "Primary Implementation for CMSInventoryServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (CMSStoreInventory[]) CMSInventoryServices.getCurrent().findStoresWithItemAvailable(itemCode);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * Find the number of items available in the store.
    * @param itemCode the item code
    * @param store store
    */
   public int findAvailableQuantity (String itemCode, Store store) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (int) CMSInventoryServices.getCurrent().findAvailableQuantity(itemCode, store);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findAvailableQuantity",
            "Primary Implementation for CMSInventoryServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (int) CMSInventoryServices.getCurrent().findAvailableQuantity(itemCode, store);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * Find the number of items available in the store.
    * @param itemCode the item code
    * @param store store
    */
   public int[] findAvailableQuantities (String[] itemCodes, Store store) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (int[]) CMSInventoryServices.getCurrent().findAvailableQuantities(itemCodes, store);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findAvailableQuantities",
            "Primary Implementation for CMSInventoryServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (int[]) CMSInventoryServices.getCurrent().findAvailableQuantities(itemCodes, store);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * Find the number of items removed from the store.
    * @param itemCode the item code
    * @param store store
    */
   public int findRemovedQuantity (String itemCode, Store store) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (int) CMSInventoryServices.getCurrent().findRemovedQuantity(itemCode, store);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findRemovedQuantity",
            "Primary Implementation for CMSInventoryServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (int) CMSInventoryServices.getCurrent().findRemovedQuantity(itemCode, store);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * Find the number of items reserved in the store.
    * @param itemCode the item code
    * @param store store
    */
   public int findReservedQuantity (String itemCode, Store store) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (int) CMSInventoryServices.getCurrent().findReservedQuantity(itemCode, store);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findReservedQuantity",
            "Primary Implementation for CMSInventoryServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (int) CMSInventoryServices.getCurrent().findReservedQuantity(itemCode, store);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * Find the number of items on layaway in the store.
    * @param itemCode the item code
    * @param store store
    */
   public int findLayawayQuantity (String itemCode, Store store) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (int) CMSInventoryServices.getCurrent().findLayawayQuantity(itemCode, store);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findLayawayQuantity",
            "Primary Implementation for CMSInventoryServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (int) CMSInventoryServices.getCurrent().findLayawayQuantity(itemCode, store);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * Find the number of items in transit from the store.
    * @param itemCode the item code
    * @param store store
    */
   public int findInTransitQuantity (String itemCode, Store store) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (int) CMSInventoryServices.getCurrent().findInTransitQuantity(itemCode, store);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findInTransitQuantity",
            "Primary Implementation for CMSInventoryServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (int) CMSInventoryServices.getCurrent().findInTransitQuantity(itemCode, store);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * Find the number of items rented from the store.
    * @param itemCode the item code
    * @param store store
    */
   public int findRentedQuantity (String itemCode, Store store) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return (int) CMSInventoryServices.getCurrent().findRentedQuantity(itemCode, store);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"findRentedQuantity",
            "Primary Implementation for CMSInventoryServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (int) CMSInventoryServices.getCurrent().findRentedQuantity(itemCode, store);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

}
