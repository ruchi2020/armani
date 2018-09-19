/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.fiscaldocument;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 06-24-2005 | Megha     | N/A       |  TaxFree                                     |
 --------------------------------------------------------------------------------------------
 */
import  com.chelseasystems.cr.xml.XMLUtil;
import  com.chelseasystems.cr.store.Store;
import  com.chelseasystems.cr.config.*;
import  com.chelseasystems.cr.logging.*;
import  com.chelseasystems.cr.util.*;
import  com.chelseasystems.cr.appmgr.*;
import  com.chelseasystems.cs.xml.*;
import  java.io.*;
import  java.util.*;


/**
 * put your documentation comment here
 */
public class TaxFreeFileServices extends TaxFreeServices {
  static private Vector allTaxFree = null;
  static private String taxFreeFileName = FileMgr.getLocalFile("xml", "tax_free.xml");
  static private int numberOfUsing = 0;

  /**
   * Reads a file with the given filename and places the lines, each of which
   * contains delimited data for a single customer, into a hashtable for
   * later retrieval.
   **/
  public TaxFreeFileServices () {
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Vector getAllTaxFree () {
    if (allTaxFree == null)
      loadAllTaxFreeFromFile();
    return  allTaxFree;
  }

  /**
   * put your documentation comment here
   */
  protected void loadAllTaxFreeFromFile () {
    long begin = new java.util.Date().getTime();
    if (taxFreeFileName == null || taxFreeFileName.equals("")) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadAllTaxFreeFromFile()", "Missing data file name.", "Make sure the data file is there.", LoggingServices.CRITICAL);
      System.exit(1);
    }
    try {
      allTaxFree = (new LoyaltyXML()).toObjects(taxFreeFileName);
    } catch (org.xml.sax.SAXException saxException) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadAllTaxFreeFromFile()", "Cannot parse the loyalty rules data file.", "Verify the integrity of the employee data file", LoggingServices.CRITICAL, saxException);
      //         System.exit(1);
    } catch (IOException e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadAllTaxFreeFromFile()", "Cannot process the loyalty rules data file.", "Verify the integrity of the employee data file", LoggingServices.CRITICAL, e);
      //         System.exit(1);
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadAllTaxFreeFromFile()", "Cannot process the loyalty rules data file (2).", "Unknow exception: " + e, LoggingServices.CRITICAL, e);
      //         System.exit(1);
    }
    long end = new java.util.Date().getTime();
    if (allTaxFree != null)
      System.out.println("Number of tax free loaded: " + allTaxFree.size() + " (" + (end - begin) + "ms)");
    else
      System.out.println("Number of tax free loaded: None (" + (end - begin) + "ms)");
  }

  /**
   * put your documentation comment here
   * @return
   * @exception Exception
   */
  public TaxFree[] findAllTaxFree () throws Exception {
    return  new TaxFree[0];
  }

  /**
   * put your documentation comment here
   * @return
   * @exception Exception
   */
  public TaxFree[] findTaxFreeForStore (String storeID) throws Exception {
    return  new TaxFree[0];
  }

}



