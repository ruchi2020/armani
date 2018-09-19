/*
 * @copyright (c) 1998-2002 Chelsea Market Systems LLC
 */


package  com.chelseasystems.cs.transfer;

import  com.chelseasystems.cr.config.*;
import  com.chelseasystems.cr.logging.*;
import  com.chelseasystems.cr.appmgr.*;
import  com.chelseasystems.cr.transfer.*;
import  java.lang.*;
import  java.util.*;


/**
 * Client-side object for retrieving and submitting.
 */
public class CMSTransferClientServices extends ClientServices {

   /** Configuration manager **/
 //  private ConfigMgr config = null;

   /**
    * Set the current implementation
    */
   public CMSTransferClientServices () {
      // Set up the configuration manager.
      config = new ConfigMgr("transfer.cfg");
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
      LoggingServices.getCurrent().logMsg("On-Line Mode for CMSTransferClientServices");
      CMSTransferServices serviceImpl = (CMSTransferServices)config.getObject("CLIENT_IMPL");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSTransferClientServices", "onLineMode()",
               "Cannot instantiate the class that provides the" + "implementation of CMSTransferServices in transfer.cfg.",
               "Make sure that transfer.cfg contains an entry with " + "a key of CLIENT_IMPL and a value that is the name of a class "
               + "that provides a concrete implementation of CMSTransferServices.",
               LoggingServices.MAJOR);
         setOffLineMode();
         return;
      }
      CMSTransferServices.setCurrent(serviceImpl);
   }

   /**
    */
   public void offLineMode () {
      LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSTransferClientServices");
      CMSTransferServices serviceImpl = (CMSTransferServices)config.getObject("CLIENT_DOWNTIME");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSTransferClientServices", "offLineMode()",
               "Cannot instantiate the class that provides the" + " implementation of CMSTransferServices in transfer.cfg.",
               "Make sure that transfer.cfg contains an entry with " + "a key of CLIENT_DOWNTIME and a value"
               + " that is the name of a class that provides a concrete" +
               " implementation of CMSTransferServices.", LoggingServices.CRITICAL);
      }
      CMSTransferServices.setCurrent(serviceImpl);
   }

   public Object getCurrentService () {
      return  CMSTransferServices.getCurrent();
   }

   /**
    * submit a transfer transaction
    * @param transfer transfer
    */
   public boolean submit (Transfer transfer) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (boolean)CMSTransferServices.getCurrent().submit(transfer);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "submit",
               "Primary Implementation for CMSTransferServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (boolean)CMSTransferServices.getCurrent().submit(transfer);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * find TransferOut by its unique Id
    * @param transferId transferId
    */
   public TransferOut findTransferOutById (String transferId) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (TransferOut)CMSTransferServices.getCurrent().findTransferOutById(transferId);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "findTransferOutById",
               "Primary Implementation for CMSTransferServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (TransferOut)CMSTransferServices.getCurrent().findTransferOutById(transferId);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * find transfer in by its unique id
    * @param transferId transferId
    */
   public TransferIn findTransferInById (String transferId) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (TransferIn)CMSTransferServices.getCurrent().findTransferInById(transferId);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "findTransferInById",
               "Primary Implementation for CMSTransferServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (TransferIn)CMSTransferServices.getCurrent().findTransferInById(transferId);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * search transaction outs
    * @param storeId storeId
    * @param beginDate beginDate
    * @param endDate endDate
    */
   public TransferOut[] findByFromStoreIdAndDate (String storeId, Date beginDate,
         Date endDate) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (TransferOut[])CMSTransferServices.getCurrent().findByFromStoreIdAndDate(storeId,
               beginDate, endDate);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "findByFromStoreIdAndDate",
               "Primary Implementation for CMSTransferServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (TransferOut[])CMSTransferServices.getCurrent().findByFromStoreIdAndDate(storeId,
               beginDate, endDate);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * search transaction outs
    * @param storeId storeId
    * @param beginDate beginDate
    * @param endDate endDate
    */
   public TransferOut[] findByToStoreIdAndDate (String storeId, Date beginDate,
         Date endDate) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (TransferOut[])CMSTransferServices.getCurrent().findByToStoreIdAndDate(storeId,
               beginDate, endDate);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "findByToStoreIdAndDate",
               "Primary Implementation for CMSTransferServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (TransferOut[])CMSTransferServices.getCurrent().findByToStoreIdAndDate(storeId,
               beginDate, endDate);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * search transaction outs
    * @param storeId storeId
    */
   public TransferOut[] findPendingByFromStoreId (String storeId) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (TransferOut[])CMSTransferServices.getCurrent().findPendingByFromStoreId(storeId);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "findPendingByFromStoreId",
               "Primary Implementation for CMSTransferServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (TransferOut[])CMSTransferServices.getCurrent().findPendingByFromStoreId(storeId);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * search transaction outs
    * @param storeId storeId
    */
   public TransferOut[] findPendingByToStoreId (String storeId) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (TransferOut[])CMSTransferServices.getCurrent().findPendingByToStoreId(storeId);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "findPendingByToStoreId",
               "Primary Implementation for CMSTransferServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (TransferOut[])CMSTransferServices.getCurrent().findPendingByToStoreId(storeId);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * search transaction outs
    * @param storeId storeId
    * @param beginDate beginDate
    * @param endDate endDate
    */
   public TransferOut[] findPendingByFromStoreIdAndDate (String storeId, Date beginDate,
         Date endDate) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (TransferOut[])CMSTransferServices.getCurrent().findPendingByFromStoreIdAndDate(storeId,
               beginDate, endDate);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "findPendingByFromStoreIdAndDate",
               "Primary Implementation for CMSTransferServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (TransferOut[])CMSTransferServices.getCurrent().findPendingByFromStoreIdAndDate(storeId,
               beginDate, endDate);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * search transaction outs
    * @param storeId storeId
    * @param beginDate beginDate
    * @param endDate endDate
    */
   public TransferOut[] findPendingByToStoreIdAndDate (String storeId, Date beginDate,
         Date endDate) throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (TransferOut[])CMSTransferServices.getCurrent().findPendingByToStoreIdAndDate(storeId,
               beginDate, endDate);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "findPendingByToStoreIdAndDate",
               "Primary Implementation for CMSTransferServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (TransferOut[])CMSTransferServices.getCurrent().findPendingByToStoreIdAndDate(storeId,
               beginDate, endDate);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

   /**
    * @return
    * @exception Exception
    */
   public String getNextTransferId () throws Exception {
      try {
         this.fireWorkInProgressEvent(true);
         return  (String)CMSTransferServices.getCurrent().getNextTransferId();
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "getNextTransferId",
               "Primary Implementation for CMSTransferServices failed, going Off-Line...",
               "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return  (String)CMSTransferServices.getCurrent().getNextTransferId();
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }
}



