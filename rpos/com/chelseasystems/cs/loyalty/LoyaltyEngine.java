/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.loyalty;

import com.chelseasystems.cr.currency.*;
import java.util.*;

import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.util.LineItemPOSUtil;
import com.chelseasystems.cs.customer.*;
import com.chelseasystems.cs.item.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.item.MiscItemManager;


/**
 * <p>Title: Loyalty Engine</p>
 *
 * <p>Description: Loyalty Engine class to calculate the loyalty points of each sale line item</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class LoyaltyEngine {
  private static Vector loyaltyRules = new Vector();
  private static Hashtable rulesByCustType = new Hashtable();
  public static double defaultPointRatio = 0.0;
  public static boolean truncatePoints = false;
  public static double rewdRedemptionRatio = 0.0;
  static {
    // Load the rules from the loyalty.xml file here
    ConfigMgr config = new ConfigMgr("loyalty.cfg");
    try {
      defaultPointRatio = new Double(config.getString("DEFAULT_POINT_RATIO")).doubleValue();
    } catch (Exception e) {
      defaultPointRatio = 0.0;
    }
    String rewdRedemptionRatioStr = config.getString("LOYALTY_REWARD_REDEMPTION_RATIO");
    if (rewdRedemptionRatioStr != null) {
      rewdRedemptionRatio = Double.parseDouble(rewdRedemptionRatioStr);
    }
    if (config.getString("TRUNCATE_POINTS") != null) {
      if (config.getString("TRUNCATE_POINTS").equalsIgnoreCase("true")) {
        truncatePoints = true;
      }
    }
    LoyaltyFileServices loyaltyFileServices = (LoyaltyFileServices)config.getObject(
        "CLIENT_LOCAL_IMPL");
    loyaltyRules = loyaltyFileServices.getAllLoyaltyRules();
    if (loyaltyRules == null)
      loyaltyRules = new Vector();
    // Poulate the cust rules hashtable
    for (int i = 0; i < loyaltyRules.size(); i++) {
      LoyaltyRule r = (LoyaltyRule)loyaltyRules.get(i);
      if (rulesByCustType.get(r.getCustType()) == null) {
        Vector custRules = new Vector();
        custRules.add(r);
        rulesByCustType.put(r.getCustType(), custRules);
      } else {
        Vector custRules = (Vector)rulesByCustType.get(r.getCustType());
        custRules.add(r);
        rulesByCustType.put(r.getCustType(), custRules);
      }
    }
  }

  /**
   * Default constructor
   */
  public LoyaltyEngine() {
  }

  /**
   * Calculates and sets the loyalty point on each line item detail
   * @param lineItem
   * @return
   */
  public double calculateLoyaltyPoints(POSLineItem lineItem) {
	    CMSCompositePOSTransaction posTxn = (CMSCompositePOSTransaction)lineItem.getTransaction().
	        getCompositeTransaction();
	    double pointsRatio = getBestPointsRatio(lineItem);
    double loyaltyPointEarnRatio = posTxn.getLoyaltyPointEarnRatio();
	    POSLineItemDetail[] dets = lineItem.getLineItemDetailsArray();
	    if (lineItem instanceof CMSSaleLineItem) {
	      for (int i = 0; i < dets.length; i++) {
	        com.chelseasystems.cr.currency.ArmCurrency netAmount = new com.chelseasystems.cr.currency.
	            ArmCurrency(0.0d);
	        try {
	          netAmount = dets[i].getNetAmount().subtract(
          		dets[i].getNetAmount().multiply(loyaltyPointEarnRatio));
	        } catch (Exception ce) {

	        }
	        // If the lineItemDetail has a reduction or discount applied to it, then it is not eligible for LoyaltyPoints
        if (((CMSSaleLineItemDetail)dets[i]).getReductionAmount().doubleValue() > 0 || 
        	netAmount.doubleValue()<=0	) {
	          ((CMSSaleLineItemDetail)dets[i]).setLoyaltyPoints(0);
	        } else {
	          ((CMSSaleLineItemDetail)dets[i]).setLoyaltyPoints(netAmount.doubleValue() * pointsRatio);
	        }
	      }
	      return ((CMSSaleLineItem)lineItem).getLoyaltyPoints();
	    } else if (lineItem instanceof CMSReturnLineItem) {
	      for (int i = 0; i < dets.length; i++) {
	        double amt = dets[i].getNetAmount().doubleValue();
	        // If the lineItemDetail has a reduction or discount applied to it, then it is not eligible for LoyaltyPoints
        if(((CMSReturnLineItemDetail)dets[i]).getSaleLineItemDetail()!=null){
        	continue;
        }
	        try {
	          amt = dets[i].getNetAmount().subtract(
              dets[i].getNetAmount().multiply(loyaltyPointEarnRatio)).doubleValue();
	        } catch (Exception ce) {}

	        if (((CMSReturnLineItemDetail)dets[i]).getReductionAmount().doubleValue() > 0) {
	          ((CMSReturnLineItemDetail)dets[i]).setLoyaltyPoints(amt * 0);
	        } else {
	          ((CMSReturnLineItemDetail)dets[i]).setLoyaltyPoints(amt * pointsRatio);
	        }
	      }
	      return ((CMSReturnLineItem)lineItem).getLoyaltyPoints();
	    }
	    return 0.0;
	  }

  /**
   * Filters the best available point ratio for a sale line item based on cust type and item
   * @param det the sale line item
   * @return returns the best point ratio based on rules available
   */
  private double getBestPointsRatio(POSLineItem det) {
	    CMSCustomer cust = (CMSCustomer)((CMSCompositePOSTransaction)det.getTransaction().
	        getCompositeTransaction()).getCustomer();
	    if (cust != null) {
	      String custType = cust.getCustomerType();
	      if (custType != null) {
	        Vector rules = (Vector)rulesByCustType.get(custType);
	        CMSItem itm = (CMSItem)det.getItem();
	        double pointRatio = getPointsRatioByStyleNumber(rules, itm);
	        if (pointRatio == 0.0) {
	          pointRatio = getPointsRatioByDeptClassSubClass(rules, itm);
	        }
	        if (pointRatio == 0.0) {
	          pointRatio = getPointsRatioByDeptClass(rules, itm);
	        }
	        if (pointRatio == 0.0) {
	          pointRatio = getPointsRatioByDept(rules, itm);
	        }
	        if (pointRatio == 0.0) {
	          // Default return here if there are no rules for the customer type
	          // Should be from the config file
	          if (!MiscItemManager.getInstance().isMiscItem(itm.getId())) {
	            return defaultPointRatio;
	          }
	          String notOnFileItemId = null;
	          notOnFileItemId = LineItemPOSUtil.getNotOnFileItemId(det);
	          if (notOnFileItemId != null && !notOnFileItemId.equals("")) {
	            pointRatio = getPointsRatioByDeptClassForNotOnFileItem(rules, det);
	            if (pointRatio == 0.0) {
	              return defaultPointRatio;
	            }
	          }
	        } else {
	          return pointRatio;
	        }

	      }
	    }
	    return 0;
	  }

  /**
   * Matches the rule based on Style Number and returns the points ratio
   * @param rules
   * @param itm
   * @return
   */
  double getPointsRatioByStyleNumber(Vector rules, CMSItem itm) {
    if (rules != null) {
      for (int i = 0; i < rules.size(); i++) {
        LoyaltyRule r = (LoyaltyRule)rules.get(i);
        if ((r.getStyleNumber() != null && !r.getStyleNumber().equals(""))
            && (r.getItemDepart() == null || r.getItemDepart().equals(""))
            && (r.getItemClass() == null || r.getItemClass().equals(""))
            && (r.getItemSubclass() == null || r.getItemSubclass().equals(""))) {
          if (r.getStyleNumber().equals(itm.getStyleNum())) {
            return r.getPointsRatio();
          }
        }
      }
    }
    return 0.0;
  }

  /**
   * Matches the rule based on Department, Class and Sub-class and returns the points ratio
   * @param rules
   * @param itm
   * @return
   */
  double getPointsRatioByDeptClassSubClass(Vector rules, CMSItem itm) {
    if (rules != null) {
      for (int i = 0; i < rules.size(); i++) {
        LoyaltyRule r = (LoyaltyRule)rules.get(i);
        if ((r.getItemDepart() != null && !r.getItemDepart().equals(""))
            && (r.getItemClass() != null && !r.getItemClass().equals(""))
            && (r.getItemSubclass() != null && !r.getItemSubclass().equals(""))) {
          if (r.getItemDepart().equals(itm.getDepartment())
              && r.getItemClass().equals(itm.getClassId())
              && r.getItemSubclass().equals(itm.getSubClassId())) {
            return r.getPointsRatio();
          }
        }
      }
    }
    return 0.0;
  }

  /**
   * Matches the rule based on Department and Class Id and returns the points ratio
   * @param rules
   * @param itm
   * @return
   */
  double getPointsRatioByDeptClass(Vector rules, CMSItem itm) {
    if (rules != null) {
      for (int i = 0; i < rules.size(); i++) {
        LoyaltyRule r = (LoyaltyRule)rules.get(i);
        if ((r.getItemDepart() != null && !r.getItemDepart().equals(""))
            && (r.getItemClass() != null && !r.getItemClass().equals(""))
            && (r.getItemSubclass() == null || r.getItemSubclass().equals(""))) {
          if (r.getItemDepart().equals(itm.getDepartment())
              && r.getItemClass().equals(itm.getClassId())) {
            return r.getPointsRatio();
          }
        }
      }
    }
    return 0.0;
  }
  
  /**
   * Matches the rule based on Department and Class Id and returns the point ratio
   * for Not On File Item
   * @param rules
   * @param itm
   * @return
   */
  double getPointsRatioByDeptClassForNotOnFileItem(Vector rules, POSLineItem itm) {
	  if (rules != null) {
		  for (int i = 0; i < rules.size(); i++) {
		  LoyaltyRule r = (LoyaltyRule)rules.get(i);	      	
	        if ((r.getItemDepart() != null && !r.getItemDepart().equals(""))
	            && (r.getItemClass() != null && !r.getItemClass().equals(""))) {
	          if (r.getItemDepart().equals(LineItemPOSUtil.getNotOnFileItemDept(itm))
	              && r.getItemClass().equals(LineItemPOSUtil.getNotOnFileItemClass(itm))) {	        	  
	            return r.getPointsRatio();
	          }
	        }
	      }
	  }
	  return 0.0;
  }

  /**
   * Matches the rule based on Department and returns the points ratio
   * @param rules
   * @param itm
   * @return
   */
  double getPointsRatioByDept(Vector rules, CMSItem itm) {
    if (rules != null) {
      for (int i = 0; i < rules.size(); i++) {
        LoyaltyRule r = (LoyaltyRule)rules.get(i);
        if ((r.getItemDepart() != null && !r.getItemDepart().equals(""))
            && (r.getItemClass() == null || r.getItemClass().equals(""))
            && (r.getItemSubclass() == null || r.getItemSubclass().equals(""))) {
          if (r.getItemDepart().equals(itm.getDepartment())) {
            return r.getPointsRatio();
          }
        }
      }
    }
    return 0.0;
  }

  /**
   * put your documentation comment here
   * @return
   */
  static public boolean isTruncatedPoints() {
    return truncatePoints;
  }
  
  public static double getPremioRewardRatioMultiplier() {
	    double multiplier = 1;
	    if (rewdRedemptionRatio != 0) {
	      multiplier = defaultPointRatio / rewdRedemptionRatio;
	    }
	    return multiplier;
	  }
}
