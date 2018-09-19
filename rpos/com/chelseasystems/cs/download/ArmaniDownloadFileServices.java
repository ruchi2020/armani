/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.download;

/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 08-24-2005 | Manpreet  | N/A       | ArmaniDownload spec.                               |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cs.pos.AlterationItemGroup;
import com.chelseasystems.cs.config.ArmConfig;
import com.chelseasystems.cs.xml.*;
import java.io.*;
import java.util.Vector;

import com.chelseasystems.cs.config.ArmDiscountRule;
import com.chelseasystems.cs.config.ArmPayConfigDetail;
import com.chelseasystems.cs.config.ArmPayPlanConfigDetail;
import com.chelseasystems.cs.config.ArmTaxRateConfig;


/**
 *
 * <p>Title: ArmaniDownloadFileServices</p>
 * <p>Description: Used to download files on Client </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class ArmaniDownloadFileServices extends ArmaniDownloadServices {
  /**
   * AlterationItemGroups
   */
  static private Vector vecAlterationItmGrps = null;
  /**
   * Alteration XML file
   */
  static private String alterationFileName = FileMgr.getLocalFile("xml", "Alterations.xml");

  /**
   * Default Constructor
   */
  public ArmaniDownloadFileServices() {
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Vector getAllLoadedAlterationItemGroups() {
    if (vecAlterationItmGrps == null) {
      loadAllAlterationItemGrpsFromFile();
    }
    return vecAlterationItmGrps;
  }

  /**
   * put your documentation comment here
   */
  protected void loadAllAlterationItemGrpsFromFile() {
    long begin = new java.util.Date().getTime();
    if (alterationFileName == null || alterationFileName.equals("")) {
      LoggingServices.getCurrent().logMsg(getClass().getName()
          , "loadAllAlterationItemGrpsFromFile()", "Missing data file name."
          , "Make sure the data file is there.", LoggingServices.CRITICAL);
      System.exit(1);
    }
    try {
      vecAlterationItmGrps = (new AlterationXML()).toObjects(alterationFileName);
    } catch (org.xml.sax.SAXException saxException) {
      LoggingServices.getCurrent().logMsg(getClass().getName()
          , "loadAllAlterationItemGrpsFromFile()", "Cannot parse the Alterations data file."
          , "Verify the integrity of the employee data file", LoggingServices.CRITICAL
          , saxException);
    } catch (IOException e) {
      LoggingServices.getCurrent().logMsg(getClass().getName()
          , "loadAllAlterationItemGrpsFromFile()", "Cannot process the Alterations data file."
          , "Verify the integrity of the employee data file", LoggingServices.CRITICAL, e);
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName()
          , "loadAllAlterationItemGrpsFromFile()", "Cannot process the Alterations data file (2)."
          , "Unknow exception: " + e, LoggingServices.CRITICAL, e);
    }
    long end = new java.util.Date().getTime();
    if (vecAlterationItmGrps != null) {
      System.out.println("Number of Alteration item groups loaded: " + vecAlterationItmGrps.size()
          + " (" + (end - begin) + "ms)");
    } else {
      System.out.println("Number of Alteration item groups loaded: None (" + (end - begin) + "ms)");
    }
  }

  /**
   * put your documentation comment here
   * @return
   * @exception Exception
   */
  public AlterationItemGroup[] getAllAlterationItemGroups()
      throws Exception {
    return new AlterationItemGroup[0];
  }

  /**
   * put your documentation comment here
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception Exception
   */
  public AlterationItemGroup[] getAlterationItemGroupsByCountryAndLanguage(String sCountry
      , String sLanguage)
      throws Exception {
    return new AlterationItemGroup[0];
  }

  /**
   * put your documentation comment here
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception Exception
   */
  public ArmConfig getConfigByCountryAndLanguage(String sCountry, String sLanguage)
      throws Exception {
    return new ArmConfig();
  }

  /**
   * put your documentation comment here
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception Exception
   */
  public ArmPayConfigDetail[] getPayConfigByCountryAndLanguage(String sCountry, String sLanguage)
      throws Exception {
    return new ArmPayConfigDetail[0];
  }

  public ArmPayPlanConfigDetail[] getPayPlanConfigByCountryAndLanguage(String sCountry, String sLanguage)
      throws Exception {
    return new ArmPayPlanConfigDetail[0];
  }

  /**
   * put your documentation comment here
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception Exception
   */
  public ArmDiscountRule[] getDiscountRuleByCountryAndLanguage(String sCountry, String sLanguage)
      throws Exception {
    return new ArmDiscountRule[0];
  }
  
  //Added by Vivek Mishra for PCR_TaxExceptionRulesImplementationChanges_US
  //start
  /**
   * put your documentation comment here
   * @param sState
   * @param sZipcode
   * @return
   * @exception Exception
   */
  public ArmTaxRateConfig[] getExceptionTaxDetailByStateAndZipcode(String sState, String sZipcode)
      throws Exception {
    return new ArmTaxRateConfig[0];
  }
  //end
}

