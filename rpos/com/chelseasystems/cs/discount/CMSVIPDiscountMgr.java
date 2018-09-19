/**
 * This class is created for VIP membership discount by Vivek Sawant
 * @author vivek.sawant
 *  
**/
package com.chelseasystems.cs.discount;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import com.chelseasystems.cr.appmgr.IObjectBuilder;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.util.ResourceManager;
import com.chelseasystems.cs.config.ArmConfigDetail;
import com.chelseasystems.cs.config.ArmDiscountRule;
import com.chelseasystems.cs.util.ArmConfigLoader;

public class CMSVIPDiscountMgr {
	  private static String types[] = null;
	  private static String notUsedTypes[] = null;
	  private static Hashtable htClasses = null;
	  private static Hashtable htBuilders = null;
	  private static Hashtable htAppliers = null;
	  private static Hashtable htLabels = null;
	  private static Hashtable htDiscountRules = null;
	  private static ResourceBundle res = null;
	  private static boolean solicitReasons = false;
	  private static Hashtable htDiscountReasons = null;
	  private static ArrayList discountList = null;
	  private static String reasonCodes[] = null;
	  private static String employeeReasonCode = null;
	  private static String discountCode = null;
	  private static String priorityCode = null;


	  /**
	   * This static block read the discount.cfg file and populate the collections of
	   * available discount type, discount label, discount builder, discount class
	   */
	  static {
	    htClasses = new Hashtable();
	    htBuilders = new Hashtable();
	    htLabels = new Hashtable();
	    htAppliers = new Hashtable();
	    htDiscountRules = new Hashtable();
	    res = ResourceManager.getResourceBundle();
	    try {
	      ConfigMgr config = new ConfigMgr("discount.cfg");
	      String discTypes = config.getString("VIP_DISCOUNT_TYPE");
	      StringTokenizer stk = new StringTokenizer(discTypes, ",");
	      types = new String[stk.countTokens()];
	      initializeTypes(config, stk, types);
	      ConfigMgr discConfig = new ConfigMgr("discount_rules.cfg");
	      if (discConfig != null) {
	    	  discTypes = discConfig.getString("DISCOUNT_TYPES");
	    	  if (discTypes != null && discTypes.length() > 0) {
	    		  stk = new StringTokenizer(discTypes, ",");
	    		  initializeDiscountRules(discConfig, stk);
	    	  }
	      }
	    } catch (Exception e) {
	      System.out.println("\t\t*** Exception in CMSVIPDiscountMgr static initializer: " + e);
	    }
	  }


	  /**
	   *
	   * @param config ConfigMgr
	   * @param stk StringTokenizer
	   * @param discountTypes String[]
	   * @throws Exception
	   */
	  private static void initializeTypes(ConfigMgr config, StringTokenizer stk, String[] discountTypes)
	      throws Exception {
	    int i = -1;
	    String discLbl;
	    for (; stk.hasMoreTokens() || stk.hasMoreTokens(); htLabels.put(discountTypes[i], discLbl)) {
	      discountTypes[++i] = stk.nextToken();
	      String clsName = config.getString(discountTypes[i] + ".CLASS");
	      Class cls = Class.forName(clsName);
	      CMSDiscount dsc = (CMSDiscount)cls.newInstance();
	      dsc.setType(discountTypes[i]);
	      discLbl = res.getString(config.getString(discountTypes[i] + ".LABEL"));
	      dsc.setGuiLabel(discLbl);
	      System.out.println("Label for Other: " + discLbl);
	      String discApplyTo = config.getString(discountTypes[i] + ".APPLY_TO");
	      dsc.setApplyTo(Boolean.getBoolean(discApplyTo));
	      String signReqStr = config.getString(discountTypes[i] + ".SIGNATURE_REQUIRED");
	      dsc.setIsSignatureRequired(Boolean.valueOf(signReqStr).booleanValue());
	       String discIsPctStr = config.getString(discountTypes[i] + ".DISCOUNT_IS_PERCENT");
	      dsc.setIsDiscountPercent(Boolean.valueOf(discIsPctStr).booleanValue());
	      String manualEntry = config.getString(discountTypes[i] + ".MANUAL_ENTRY");
	      dsc.setManualEntry(new Boolean(manualEntry).booleanValue());

	      htClasses.put(discountTypes[i], dsc);
	      clsName = config.getString(discountTypes[i] + ".BUILDER");
	      htBuilders.put(discountTypes[i], clsName);
	    }
	  }


	  /**
	   *
	   * @param sReason String
	   */
	  private static void setSolicitReasons(String sReason) {
	    solicitReasons = sReason.equalsIgnoreCase("true");
	  }


	  /**
	   *
	   * @return boolean
	   */
	  public static boolean getSolicitReasons() {
	    return solicitReasons;
	  }


	  /**
	   * This method returns all the discount types
	   * @return String[]
	   */
	  public static String[] getDiscountTypes() {
	    String array[] = new String[types.length];
	    System.arraycopy(types, 0, array, 0, types.length);
	    return array;
	  }

	  /**
	   * This method is used to create discount object on the basis of discount type
	   * @param discountType String
	   * @return CMSDiscount
	   */
	  public static CMSDiscount createDiscount(String discountType) {
	    CMSDiscount d = (CMSDiscount)htClasses.get(discountType);
	    return null != d ? (CMSDiscount)d.clone() : null;
	  }


	  /**
	   * This method is used to get builder name for discount
	   * @param discountType String
	   * @return String
	   */
	  public static String getBuilderName(String discountType) {
	    return (String)htBuilders.get(discountType);
	  }


	  /**
	   * This method is used to create builder for discount
	   * @param discountType String
	   * @return IObjectBuilder
	   */
	  public static IObjectBuilder getBuilder(String discountType) {
	    try {
	      String clsName = (String)htBuilders.get(discountType);
	      if (null == clsName) {
	        return null;
	      } else {
	        Class cls = Class.forName(clsName);
	        return (IObjectBuilder)cls.newInstance();
	      }
	    } catch (Exception e) {
	      LoggingServices.getCurrent().logMsg("CMSVIPDiscountMgr", "getBuilder()"
	          , "Cannot create instance of IObjectBuilder for discount type " + discountType + "."
	          , "Make sure the discount.cfg file is in order.", 2, e);
	    }
	    return null;
	  }


	  /**
	   * This method is used to get label of discount
	   * @param discountType String
	   * @return String
	   */
	  public static String getLabel(String discountType) {
	    return (String)htLabels.get(discountType);
	  }


	  /**
	   * This method is used to get reasons of discount
	   * @return Hashtable
	   */
	  public static Hashtable getReasonHashtable() {
	    return htDiscountReasons;
	  }
	  
	  /**
	   * This method is used to get reasons of discount
	   * @return Hashtable
	   */
	  public static ArrayList getConfigDetailList() {
	    return discountList;
	  }

	  public static void setEmployeeReasonCode(String reasonCode) {
	    employeeReasonCode = reasonCode;
	  }


	  public static String getEmployeeReasonCode() {
	    return employeeReasonCode;
	  }

	  public static boolean isEmployeeReasonCode(String code) {
	    if (employeeReasonCode != null && employeeReasonCode.equals(code))
	      return true;
	    return false;
	  }
	  
	  /**
	  *
	  * @param discConfig ConfigMgr
	  * @param typeStk StringTokenizer 
	  * @throws Exception
	  */
	 private static void initializeDiscountRules(ConfigMgr discConfig, StringTokenizer typeStk)
	     throws Exception {
	   List discountRuleList = null;
	   StringTokenizer rangeStk = null;
	   String rangeStr = "";
	   String range = "";
	   String type = "";
	   String key = "";
	   String value = "";
	   while (typeStk.hasMoreElements()) {
		   type = (String)typeStk.nextElement();
		   rangeStr = discConfig.getString(type + ".RANGES");
		   rangeStk = new StringTokenizer(rangeStr, ",");
		   int noOfRanges = rangeStk.countTokens();
		   int ctr = 0;
		   discountRuleList = new ArrayList();
		   while (rangeStk.hasMoreElements()) {
			   range = (String)rangeStk.nextElement();
			   if (ctr < noOfRanges) {
				   ArmDiscountRule rule = new ArmDiscountRule();
				   key = type + "." + range + ".START_RANGE";
				   value = discConfig.getString(key);
				   rule.setStartRange(Double.valueOf(value));
				   key = type + "." + range + ".END_RANGE";
				   value = discConfig.getString(key);
				   rule.setEndRange(Double.valueOf(value));
				   key = type + "." + range + ".DISCOUNT_IS_PERCENT";
				   value = discConfig.getString(key);
				   rule.setIsDscPercent(Boolean.valueOf(value));
				   discountRuleList.add(rule);
				   ctr++;
			   }
		   }
		   htDiscountRules.put(type, discountRuleList);
	   }
	 }
	 
	 /**
	  * This method is used to get the discount rule for the specified discount type
	  * @param discountCode String
	  * @return List
	  */
	 public static List getDiscountRule(String discountCode) {
	   List discountRuleList = (List)htDiscountRules.get(discountCode);
	   if (discountRuleList == null) {
		   discountRuleList = new ArrayList();
	   }
	   return discountRuleList;
	 }
	 
	 /**
	  * This method returns discount rule object based on the selling price
	  * @param amount
	  * @return ArmDiscountRule
	  */
	 public static ArmDiscountRule getDiscountRuleByRange(String discountCode, ArmCurrency sellingPrice) 
	   	throws CurrencyException {
	   List discountRules = getDiscountRule(discountCode);
	   ArmDiscountRule rule = new ArmDiscountRule();
	   ArmCurrency startRange = new ArmCurrency(0.0d);
	   ArmCurrency endRange = new ArmCurrency(0.0d);
	   for (int i = 0; i < discountRules.size(); i++) {
		  rule = (ArmDiscountRule)discountRules.get(i);
		  startRange = new ArmCurrency(rule.getStartRange().doubleValue());
		  endRange = new ArmCurrency(rule.getEndRange().doubleValue());
		  if (sellingPrice.greaterThan(startRange) && sellingPrice.lessThan(endRange)) {
			  return rule;
		  }
	   }
	   return null;
	 }
	



}
