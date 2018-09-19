/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.loyalty;

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
 | 1    | 05-03-2005 | Khyati    | N/A       | Loyalty Management                           |
 --------------------------------------------------------------------------------------------
 */
import com.chelseasystems.cr.xml.XMLUtil;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cs.xml.*;
import java.io.*;
import java.util.*;


/**
 * put your documentation comment here
 */
public class LoyaltyFileServices extends LoyaltyServices {
  static private Vector allLoyaltyRules = null;
  static private String loyaltyRulesFileName = FileMgr.getLocalFile("xml", "loyalty_rules.xml");
  static private int numberOfUsing = 0;

  /**
   * Reads a file with the given filename and places the lines, each of which
   * contains delimited data for a single customer, into a hashtable for
   * later retrieval.
   **/
  public LoyaltyFileServices() {
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Vector getAllLoyaltyRules() {
    if (allLoyaltyRules == null)
      loadAllLoyaltyRulesFromFile();
    return allLoyaltyRules;
  }

  /**
   * put your documentation comment here
   */
  protected void loadAllLoyaltyRulesFromFile() {
    long begin = new java.util.Date().getTime();
    if (loyaltyRulesFileName == null || loyaltyRulesFileName.equals("")) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadAllLoyaltyRulesFromFile()"
          , "Missing data file name.", "Make sure the data file is there."
          , LoggingServices.CRITICAL);
      System.exit(1);
    }
    try {
      allLoyaltyRules = (new LoyaltyXML()).toObjects(loyaltyRulesFileName);
    } catch (org.xml.sax.SAXException saxException) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadAllLoyaltyRulesFromFile()"
          , "Cannot parse the loyalty rules data file."
          , "Verify the integrity of the employee data file", LoggingServices.CRITICAL
          , saxException);
      //         System.exit(1);
    } catch (IOException e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadAllLoyaltyRulesFromFile()"
          , "Cannot process the loyalty rules data file."
          , "Verify the integrity of the employee data file", LoggingServices.CRITICAL, e);
      //         System.exit(1);
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadAllLoyaltyRulesFromFile()"
          , "Cannot process the loyalty rules data file (2).", "Unknow exception: " + e
          , LoggingServices.CRITICAL, e);
      //         System.exit(1);
    }
    long end = new java.util.Date().getTime();
    if (allLoyaltyRules != null)
      System.out.println("Number of loyalty rules loaded: " + allLoyaltyRules.size() + " ("
          + (end - begin) + "ms)");
    else
      System.out.println("Number of loyalty rules loaded: None (" + (end - begin) + "ms)");
  }

  /**
   *
   * @throws Exception
   * @param customerId String
   * @return Loyalty[]
   * @todo Implement this com.chelseasystems.cs.loyalty.LoyaltyServices
   *   method
   */
  public Loyalty[] findByCustomerId(String customerId)
      throws Exception {
    return new Loyalty[0];
  }

  /**
   *
   * @throws Exception
   * @param loyaltyNumber String
   * @return Loyalty
   * @todo Implement this com.chelseasystems.cs.loyalty.LoyaltyServices
   *   method
   */
  public Loyalty findById(String loyaltyNumber)
      throws Exception {
    return null;
  }

  /**
   *
   * @throws Exception
   * @param loyaltyNumber String
   * @param fromDate Date
   * @param toDate Date
   * @return LoyaltyHistory[]
   * @todo Implement this com.chelseasystems.cs.loyalty.LoyaltyServices
   *   method
   */
  public LoyaltyHistory[] findHistoryByLoyaltyIdForDateRange(String loyaltyNumber, Date fromDate
      , Date toDate)
      throws Exception {
    return new LoyaltyHistory[0];
  }

  /**
   * put your documentation comment here
   * @param loyaltyNumber
   * @return CMSPremioHistory[]
   * @exception Exception
   */
  public CMSPremioHistory[] findPremioDiscountHistoryByLoyaltyId(String loyaltyNumber)
	      throws Exception {
	  return new CMSPremioHistory[0];
  }

  /**
   *
   * @throws Exception
   * @param ruleId String
   * @return LoyaltyRule
   * @todo Implement this com.chelseasystems.cs.loyalty.LoyaltyServices
   *   method
   */
  public LoyaltyRule findRuleById(String ruleId)
      throws Exception {
    return null;
  }

  /**
   *
   * @throws Exception
   * @param loyalty Loyalty
   * @todo Implement this com.chelseasystems.cs.loyalty.LoyaltyServices
   *   method
   */
  public void submit(Loyalty loyalty)
      throws Exception {}

  /**
   *
   * @throws Exception
   * @param loyaltyId String
   * @param activeStatus boolean
   * @todo Implement this com.chelseasystems.cs.loyalty.LoyaltyServices
   *   method
   */
  public void updateStatus(String loyaltyId, boolean activeStatus)
      throws Exception {}

  /**
   *
   * @throws Exception
   * @param customerId String
   * @todo Implement this com.chelseasystems.cs.loyalty.LoyaltyServices
   *   method
   */
  public RewardCard[] findRewardsByCustomerId(String customerId)
      throws Exception {
    return new RewardCard[0];
  }

  /**
   *
   * @throws Exception
   * @param existingLoyaltyNumber String
   * @param newLoyalty Loyalty
   * @todo Implement this com.chelseasystems.cs.loyalty.LoyaltyServices
   *   method
   */
  public void reissueLoyalty(String existingLoyaltyNumber, Loyalty newLoyalty)
      throws Exception {}

  /**
   *
   * @throws Exception
   * @param storeId String
   * @param fromDate Date
   * @param toDate Date
   * @todo Implement this com.chelseasystems.cs.loyalty.LoyaltyServices
   *   method
   */
  public LoyaltyRule[] findRulesByStoreIdForDateRange(String storeId, Date fromDate, Date toDate)
      throws Exception {
    return new LoyaltyRule[0];
  }
}

