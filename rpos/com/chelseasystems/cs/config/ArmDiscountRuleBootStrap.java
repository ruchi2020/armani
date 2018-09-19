/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.config;

/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 12/15/2006 | Sandhya   | PCR16     | New business rule based on the discount code |
 +------+------------+-----------+-----------+----------------------------------------------+
 */
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Sandhya Ajit
 * @version 1.0
 */
import java.awt.*;
import java.util.*;
import java.util.List;
import java.io.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.appmgr.bootstrap.*;
import com.chelseasystems.cs.download.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.util.DateUtil;
import com.chelseasystems.cs.store.CMSStore;

/**
 * put your documentation comment here
 */
public class ArmDiscountRuleBootStrap implements IBootStrap {
  /**
   * BootstrapManager
   */
  private BootStrapManager bootMgr;
  /**
   * BrowserManager
   */
  private IBrowserManager theMgr;

  /**
   * put your documentation comment here
   */
  public ArmDiscountRuleBootStrap() {
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getName() {
    return "Armani Discount Rule Configuration BootStrap";
  }

  /**
   * returns the long description of the bootstrap
   * @return String the long description of the bootstrap
   */
  public String getDesc() {
    return "This bootstrap determines if the configuraiton file needs to be downloaded.";
  }

  /**
   * put your documentation comment here
   * @param theMgr
   * @param parentFrame
   * @param bootMgr
   * @return
   */
  public BootStrapInfo start(IBrowserManager theMgr, Window parentFrame, BootStrapManager bootMgr) {
    try {
      this.bootMgr = bootMgr;
      this.theMgr = theMgr;
      // check to make sure that discount_rules.cfg exists and its not in the backup
      File discountRulesConfig = new File(FileMgr.getLocalFile("config", "discount_rules" +
      		".cfg"));
      if (!discountRulesConfig.exists()) {
        File fileBackup = new File(FileMgr.getLocalFile("config", "discount_rules.bkup"));
        if (fileBackup.exists()) {
          fileBackup.renameTo(discountRulesConfig);
        } else {
          // no files, so delete date from last configuration download
          File fileDate = new File(FileMgr.getLocalFile("repository", "ARMANI_DISCOUNT_RULE_DOWNLOAD_DATE"));
          fileDate.delete();
        }
      }
      if (!theMgr.isOnLine()) {
        return new BootStrapInfo(this.getClass().getName());
      }
//      bootMgr.setBootStrapStatus("Checking Armani Discount Rule download date");
      Date date = (Date)theMgr.getGlobalObject("ARMANI_DISCOUNT_RULE_DOWNLOAD_DATE");
      if (date == null || DateUtil.isDateAtLeastHoursOld(date, 12)) {
          // Needed for offline scenario.
    	  downloadFile();
      }
    } catch (Exception ex) {
      System.out.println("Exception ArmDiscountRuleBootStrap.start()->" + ex);
      ex.printStackTrace();
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "start", "Exception"
          , "See Exception", LoggingServices.MAJOR, ex);
    }
    return new BootStrapInfo(this.getClass().getName());
  }

  /**
   * Download discount_rules.cfg on Client
   * @throws Exception
   */
  private void downloadFile()
      throws Exception {
    try {
      Vector rulesVec = new Vector();
      Map rangeMap = new TreeMap();
  	  List rangeList = new ArrayList();
  	  ArmDiscountRule obj = new ArmDiscountRule();
  	  String label = "";
  	  String cdDsc = "";
  	  Double startRange = new Double(0.00);
  	  Double endRange = new Double(0.00);
  	  Boolean isDscpercent;
  	  Double percent = new Double(0.00);
  	  Double moDsc = new Double(0.00);
  	  String cdNote = "";
  	  String rule = "";
  	  String discountType = "";
  	  String rangeStr = "";
  	  
      File backup = new File(FileMgr.getLocalFile("config", "discount_rules.bkup"));
      backup.delete();
      File discountRulesConfig = new File(FileMgr.getLocalFile("config", "discount_rules.cfg"));
      discountRulesConfig.renameTo(backup);
      discountRulesConfig.delete();
      FileWriter fileWriter = new FileWriter("..//files//prod//config//discount_rules.cfg");
      BufferedWriter buffWriter = new BufferedWriter(fileWriter);
      ConfigMgr config = new ConfigMgr("armaniDownload.cfg");
      ArmaniDownloadServices armDownloadServices = (ArmaniDownloadServices)config.getObject(
          "CLIENT_IMPL");
      //bootMgr.setBootStrapStatus("Downloading Armani Discount Rule Configurations file");
      CMSStore store = (CMSStore)theMgr.getGlobalObject("STORE");
      ArmDiscountRule[] armDiscountRule = armDownloadServices.getDiscountRuleByCountryAndLanguage(store.
          getPreferredISOCountry(), store.getPreferredISOLanguage());
      if (armDiscountRule != null && armDiscountRule.length > 0) {
    	  for (int i = 0; i < armDiscountRule.length; i++) {
    		  rulesVec.add(armDiscountRule[i]);
    	  }
    	  Collections.sort(rulesVec, new DiscountTypeComparator());
    	  for (int i = 0; i < rulesVec.size(); i++) {
    		  obj = (ArmDiscountRule)rulesVec.get(i);
    		  cdDsc = obj.getCdDsc();
        	  startRange = obj.getStartRange();
        	  endRange = obj.getEndRange();
        	  isDscpercent = obj.getIsDscPercent();
        	  percent = obj.getPercent();
        	  moDsc = obj.getMoDsc();
        	  cdNote = obj.getCdNote();
        	  
        	  //Construct the key
        	  label = cdDsc + "." + startRange + "-" + endRange;
        	  
        	  //Write key:value to config file
        	  rule = label + ".START_RANGE=" + startRange;
        	  writeRuleToConfigFile(buffWriter, rule);
        	  rule = label + ".END_RANGE=" + endRange;
        	  writeRuleToConfigFile(buffWriter, rule);
        	  rule = label + ".DISCOUNT_IS_PERCENT=" + isDscpercent;
        	  writeRuleToConfigFile(buffWriter, rule);
    		  rule = label + ".DISCOUNT_PCT=" + percent;
    		  writeRuleToConfigFile(buffWriter, rule);
    		  rule = label + ".DISCOUNT_AMT=" + moDsc;
    		  writeRuleToConfigFile(buffWriter, rule);
    		  rule = label + ".CODE_NOTE=" + cdNote;
    		  writeRuleToConfigFile(buffWriter, rule);
    		  
    		  rangeStr = startRange + "-" + endRange;
    		  rangeList = (List)rangeMap.get(cdDsc);
    		  if (rangeList == null) {
    			  rangeList = new ArrayList();
    			  rangeList.add(rangeStr);
    			  discountType += cdDsc + ",";
    		  }  else {
    			  rangeList.add(rangeStr);
    		  }
    		  rangeMap.put(cdDsc, rangeList);
    	  }
    	  //Write DISCOUNT_TYPES to config file
    	  if (discountType != null && discountType.length() > 0) {
    		  if (discountType.lastIndexOf(",") > -1) {
    			  discountType = discountType.substring(0, discountType.lastIndexOf(",")); 
    			  rule = "DISCOUNT_TYPES=" + discountType;
    			  writeRuleToConfigFile(buffWriter, rule);
    		  }
    	  }
    	  //Write RANGES to config file
    	  if (rangeMap != null && rangeMap.size() > 0) {
    		  Iterator keyValuePairs  = rangeMap.entrySet().iterator();
    		  for (int j = 0; j < rangeMap.size(); j++) {
    			  Map.Entry entry = (Map.Entry) keyValuePairs.next();
    			  rangeList = (List)entry.getValue();
    			  rangeStr = "";
    			  for (int k = 0; k < rangeList.size(); k++) {
    				  rangeStr += rangeList.get(k) + ",";
    			  }
    			  if (rangeStr.lastIndexOf(",") > -1) {
    				  rangeStr = rangeStr.substring(0, rangeStr.lastIndexOf(","));
    			  }
    			  rule = entry.getKey() + ".RANGES=" + rangeStr;
    			  writeRuleToConfigFile(buffWriter, rule);
    		 }
    	  }
    	  buffWriter.flush();
    	  buffWriter.close();
    	  fileWriter.close();
      }
      theMgr.addGlobalObject("ARMANI_DISCOUNT_RULE_DOWNLOAD_DATE", new java.util.Date(), true);
    } catch (Exception ex) {
      ex.printStackTrace();
      try {
        File discountRulesConfig = new File(FileMgr.getLocalFile("config", "discount_rules.cfg"));
        discountRulesConfig.delete();
        File backup = new File(FileMgr.getLocalFile("config", "discount_rules.bkup"));
        backup.renameTo(discountRulesConfig);
      } catch (Exception ex1) {}
      System.out.println("Exception downloadFile()->" + ex);
      ex.printStackTrace();
    } finally {
      if (theMgr instanceof IApplicationManager) {
        ((IApplicationManager)theMgr).closeStatusDlg();
      }
    }
  }
  
  /**
   * Writes to config file
   * @param buffWriter
   * @param sDscRule
   * @throws Exception
   */
  private void writeRuleToConfigFile(BufferedWriter buffWriter, String sDscRule) 
  		throws Exception {
	  buffWriter.write(sDscRule);
	  buffWriter.newLine();
  }
  
  private class DiscountTypeComparator implements Comparator {
	public int compare(Object obj1, Object obj2) {
		String value1 = ((ArmDiscountRule) obj1).getCdDsc();
	    String value2 = ((ArmDiscountRule) obj2).getCdDsc();
	    return value1.compareTo(value2);
	}
  }
}
