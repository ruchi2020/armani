/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.rb;

import  com.chelseasystems.cr.appmgr.*;
import  com.chelseasystems.cr.receipt.*;
import  com.chelseasystems.cr.config.*;
import  com.chelseasystems.cr.logging.*;
import  com.chelseasystems.cr.util.*;
import  com.chelseasystems.cr.receipt.ReceiptLocaleSettable;
import  com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.util.Version;

import  java.util.*;
import  java.io.*;
import  jpos.*;


/**
 * @author Dan Reading
 */
public class ReceiptFactory
      implements ReceiptBlueprintInventory, IReceipt, IConfig, JposConst, POSPrinterConst, ReceiptLocaleSettable {
   private ReceiptBlueprint receiptBlueprint;
   static private POSPrinter posPrinter = CMSPrinter.getInstance();
   private boolean trainingFlag;
   private ReceiptConfigInfo receiptInfo = ReceiptConfigInfo.getInstance();
   private Object[] receiptObjects;
   private static HashMap blueprintLibrary = new HashMap();
   ResourceBundle res = ResourceManager.getResourceBundle();

   /**
    * @param    Object[] receiptObjects
    * @param    String blueprintName
    */
   public ReceiptFactory (Object[] receiptObjects, String blueprintName) {
      this.receiptObjects = receiptObjects;
      if (null == receiptInfo.getPrinterName()) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), blueprintName, "Cannot get receipt config info",
               "Make sure JPOS_peripherals.cfg contains an entry" + " with key LOGICAL_PRINTER_NAME that matches"
               + " a logical printer name defined in JavaPOS.inf.", LoggingServices.MAJOR);
      }
      receiptBlueprint = loadReceiptBlueprint(blueprintName);
      if (receiptBlueprint == null) {
         if(CMSApplet.theAppMgr != null)
            CMSApplet.theAppMgr.showExceptionDlg(new Exception("Receipt Blueprint could not be loaded: " + blueprintName));
         else
            System.out.println("Receipt Blueprint could not be loaded: " + blueprintName);
         return;
      }
      //System.out.println("unserialized blueprint " + blueprintName + "= " + receiptBlueprint);
      //receiptBlueprint.initialize(this);
   }

   /**
    * @return
    */
   protected Object[] getReceiptObjects () {
      return  receiptObjects;
   }

   /**
    * @return
    */
   protected ReceiptConfigInfo getReceiptConfigInfo () {
      return  receiptInfo;
   }

   /**
    * This method unserializes the desired receipt blueprint object from the library.
    * ***  Blueprints are now cached to avoid read from disk with every receipt print.
    * ***  Since the print destroys the blueprint, pass back a deep clone not the
    * ***  cached reference itself.
    */
   public ReceiptBlueprint loadReceiptBlueprint (String blueprintName) {
      return loadReceiptBlueprint (blueprintName,".bpt");
   }
   public ReceiptBlueprint loadReceiptFooter (String footerName) {
      if(footerName == null || footerName.length() == 0 || footerName.equals("NO FOOTER"))
         return null;
      else
         return loadReceiptBlueprint (footerName,".ftr");
   }
   private ReceiptBlueprint loadReceiptBlueprint (String blueprintName,String extension) {
      try {
         if(blueprintLibrary.containsKey(blueprintName + extension)) {
            ReceiptBlueprint blueprintClone = ((ReceiptBlueprint)blueprintLibrary.get(blueprintName + extension)).cloneAsCurrentClassVersion();
            blueprintClone.initialize(this);
            return blueprintClone;
         }
         String bluePrintFile = FileMgr.getLocalFile("receiptblueprints", blueprintName + extension);
         ObjectStore objectStore = new ObjectStore(bluePrintFile);
         ReceiptBlueprint blueprint = (ReceiptBlueprint)objectStore.read();
         blueprintLibrary.put(blueprintName + extension,blueprint);
         ReceiptBlueprint blueprintClone = blueprint.cloneAsCurrentClassVersion();
         blueprintClone.initialize(this);
         return blueprintClone;
      } catch (Exception ex) {
         System.out.println("ReceiptFactory.loadReceiptBlueprint()->" + ex);
         return  null;
      }
   }

   /**
    * Force a reload from disk of a blueprint.   Call this method to replace a blueprint
    * without shutting down the application.  Specify the extension in the name - .ftr or .bpt
    * @param blueprintName
    */
   public static void invalidateBlueprint(String blueprintName) {
      blueprintLibrary.remove(blueprintName);
   }

   public Vector printForExportOnly (IReceiptAppManager appMgr) {
      Vector receiptReportsExport = new Vector();
      if (receiptBlueprint == null)
         return receiptReportsExport;
      printEJ(appMgr);
      DummyPOSPrinter dummyPOSPrinter = new DummyPOSPrinter();
      receiptBlueprint.setCMSPrinterManager(new CMSPrinterManager(true));
      try {
         receiptReportsExport.addAll(receiptBlueprint.printStore(dummyPOSPrinter));
         receiptReportsExport.addAll(receiptBlueprint.printCust(dummyPOSPrinter));
      } catch (Exception ex) {}
      return receiptReportsExport;
   }

   public void validateRDO (IReceiptAppManager appMgr) throws InvalidRDOException {
      try {
        if (receiptBlueprint == null)
            throw new InvalidRDOException("Blueprint is not available");
        //printEJ(appMgr);
        DummyPOSPrinter dummyPOSPrinter = new DummyPOSPrinter();
        receiptBlueprint.setCMSPrinterManager(new CMSPrinterManager(true));


        receiptBlueprint.printStore(dummyPOSPrinter);
        receiptBlueprint.printCust(dummyPOSPrinter);
        System.out.println("RDO Has been validated successfully");
      } catch (NoSuchMethodException nsm) {
         throw new InvalidRDOException("Warning: The RDO does not appear to be valid for this blueprint");
      } catch (Exception ex) {
           System.out.println("Exception thrown upon validation of RDO: " + ex);
           ex.printStackTrace();
           throw new InvalidRDOException("Exception occurs upon attempt to validate RDO: " + ex);
      }
   }

   /**
    * Print the full receipt(s) to the current receipt printer.
    * @param appMgr
    */
   public Vector print (IReceiptAppManager appMgr) {
      if (receiptBlueprint == null)
         return new Vector();
      printEJ(appMgr);
      return printPaper(appMgr);
   }

   private Vector printPaper (IReceiptAppManager appMgr) {
      System.out.println("ENTERING PRINT PAPER METHOD");
      Vector receiptReportsExport = new Vector();
      if(!receiptBlueprint.isPaperReceiptsExist()) {
         return receiptReportsExport;
      }
      try {
         System.out.println("OPEN PRINTER");
         if(CMSPrinter.openDevice(receiptInfo.getPrinterName(), appMgr)) {
            System.out.println("RECLAIM PRINTER");
            if(CMSPrinter.reclaim(appMgr))
               {}
            else {
               CMSPrinter.release();
               return receiptReportsExport;
            }
          }
          else {
             CMSPrinter.release();
             return receiptReportsExport;
          }

         //if(receiptInfo.isPrinterIBM()) {
            System.out.println("ENTER TRANSACTION PRINT");
            posPrinter.transactionPrint(PTR_S_RECEIPT,PTR_TP_TRANSACTION);
         //}
         receiptReportsExport.addAll(receiptBlueprint.printStore(posPrinter));
         receiptReportsExport.addAll(receiptBlueprint.printCust(posPrinter));


         //if(receiptInfo.isPrinterIBM()) {

            posPrinter.transactionPrint(PTR_S_RECEIPT,PTR_TP_NORMAL);
         //}

         System.out.println("RELEASING THRU CMS PRINTER");
         CMSPrinter.release();

         //if (posPrinter.getClaimed()) {
         //   posPrinter.release();
         //}
      } catch (JposException jpe) {
         LoggingServices.getCurrent().logMsg("com.chelseasystems.receipt.CMSPrinter", "print()", "Error during receipt printing.",
               "N/A", LoggingServices.MAJOR, jpe);
               
         //Sergio
         boolean responseToContinue;     
         //String errMessage = res.getString("Printer Error:  ") + res.getString(jpe.getMessage()) + ".   " + res.getString("Do you wish to re-try?");
         //boolean responseToContinue = appMgr.showOptionDlg("Printer Error.", errMessage);
         //Uncomment this code for issue # 1871 RPOS does not reprint receipt when out of paper
         if("US".equalsIgnoreCase(Version.CURRENT_REGION)) {
        	 String errMessage = res.getString("Printer Error:  ") + res.getString(jpe.getMessage()) + ".   " + res.getString("Do you wish to re-try?");
           responseToContinue = appMgr.showOptionDlg("Printer Error.", errMessage);
         }else
          responseToContinue=false;
         CMSPrinter.release();   // eats exception
         if (responseToContinue) {
            receiptBlueprint = this.loadReceiptBlueprint(receiptBlueprint.getName());
            return print(appMgr);
         }
         else {
            return receiptReportsExport;
         }
      } catch (Exception ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "print()", "Error during receipt printing.",
               "N/A", LoggingServices.MAJOR, ex);
         ex.printStackTrace();
         String errMessage = res.getString("Printer Error:  ") + ex.getMessage() + ".   " + res.getString("Do you wish to re-try?");
         boolean responseToContinue = appMgr.showOptionDlg("Printer Error.", errMessage);
         if (responseToContinue) {
            return print(appMgr);
         }
         else {
            return receiptReportsExport;
         }
      } finally {


         return receiptReportsExport;
      }
   }

   private void printEJ (IReceiptAppManager appMgr) {
      try {
         if(appMgr.getGlobalObject("REGISTER") == null) {
            System.out.println("Skip print to EJ because register number is not known.  This is normal for Receipt Architect Test printing.");
            return;
         }
         ReceiptAuditFile.getInstance().open(((com.chelseasystems.cr.register.Register)appMgr.getGlobalObject("REGISTER")).getId(),
               (Date)appMgr.getGlobalObject("PROCESS_DATE"));
         receiptBlueprint.printElectronicJournal();
         ReceiptAuditFile.getInstance().close();
      } catch (Exception e) {
         System.out.println("exception on writing EJ: " + e);
         e.printStackTrace();
         LoggingServices.getCurrent().logMsg(getClass().getName(), "print()", "Error during write of electronic journal.",
               "N/A", LoggingServices.MAJOR, e);
         String errMessage = "I/O error on electronic journal:  " + e.getMessage();
         appMgr.showErrorDlg(errMessage);
      }
   }

   /**
    * @note Currently not implemented.
    * @param appMgr
    */
   public void reprint (IReceiptAppManager appMgr) {
       if (receiptBlueprint == null)
         return;
       receiptBlueprint.setDupFlag(true);
       print(appMgr);
   }

   /**
    * @param appMgr
    * @param printType
    */
   public void reprint (IReceiptAppManager appMgr, int printType) {
       if (receiptBlueprint == null)
         return;
       receiptBlueprint.setDupFlag(true);
       print(appMgr,printType);
   }

   /**
    * @param appMgr
    * @param printType
    */
   public void print (IReceiptAppManager appMgr, int printType) {
      if (receiptBlueprint == null)
         return;
      printEJ(appMgr);
      printPaper(appMgr, printType);
   }

   private void printPaper(IReceiptAppManager appMgr, int printType) {
      if(!receiptBlueprint.isPaperReceiptsExist())
         return;
      try {
         CMSPrinter.openDevice(receiptInfo.getPrinterName(), appMgr);
         CMSPrinter.reclaim(appMgr);
         if (printType == this.CUST_COPY)
            receiptBlueprint.printCust(posPrinter);
         else
            receiptBlueprint.printStore(posPrinter);
      } catch (JposException jpe) {
         LoggingServices.getCurrent().logMsg("com.chelseasystems.receipt.CMSPrinter", "print()", "Error during receipt printing.",
               "N/A", LoggingServices.MAJOR, jpe);
         String errMessage = res.getString("Printer Error:  ") + res.getString(jpe.getMessage()) + ".   " + res.getString("Do you wish to re-try?");
         boolean responseToContinue = appMgr.showOptionDlg("Printer Error.", errMessage);
         if (responseToContinue) {
            print(appMgr);
         }
         else {
            return;
         }
      } catch (Exception ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "print()", "Error during receipt printing.",
               "N/A", LoggingServices.MAJOR, ex);
         String errMessage = "Printer Error:  " + ex.getMessage() + ".   Do you wish to re-try?";
         boolean responseToContinue = appMgr.showOptionDlg("Printer Error.", errMessage);
         if (responseToContinue) {
            print(appMgr);
         }
         else {
            return;
         }
      } finally {
         ReceiptAuditFile.getInstance().close();
         CMSPrinter.release();
      }
   }

   /**
    * It doesn't make sense to have this object update itself on the fly since
    * it only exists for the duration of printing a receipt.
    **/
   public void processConfigEvent (String[] keys) {}

   /**
    * @return
    */
   public String getName () {
      return  "ReceiptFactory";
   }

   /**
    * @return
    */
   public String getStatus () {
      return  "OK";
   }

   public void setStoreLocale(Locale locale) {
      receiptBlueprint.setStoreLocale(locale);
   }
   public void setCustomerLocale(Locale locale) {
      receiptBlueprint.setCustomerLocale(locale);
   }
   public void setEJLocale(Locale locale) {
      receiptBlueprint.setEJLocale(locale);
   }

   protected void setLocale(Locale locale) {
      if(locale != null)
         receiptBlueprint.setLocale(locale);
   }

}               //ReceiptFactory



