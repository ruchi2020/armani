/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.rules.discount;

import java.text.NumberFormat;

import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.discount.Discount;
import com.chelseasystems.cs.config.ArmDiscountRule;
import com.chelseasystems.cs.discount.CMSDiscount;
import com.chelseasystems.cs.discount.CMSDiscountMgr;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Sandhya Ajit
 * @version 1.0
 */
public class ValidateDiscountRule extends Rule {

  private static final long serialVersionUID = 1L;

 /**
  * put your documentation comment here
  */
  public ValidateDiscountRule() {}

  /**
   * Execute the rule.
   * @param theParent the class calling the rule (i.e. CMSCompositePOSTransaction)
   * @param args
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((CMSCompositePOSTransaction)theParent, (Discount)args[0], (POSLineItem)args[1]);
  }

  /**
   * Execute the rule.
   * @param theParent the class calling the rule (i.e. CMSCompositePOSTransaction)
   * @param args
   */
  public RulesInfo execute(CMSCompositePOSTransaction txn, Discount discount, POSLineItem lineItem) {
	try {
		StringBuffer message = new StringBuffer();
    ArmCurrency amount = new ArmCurrency(0.0d);
    ArmCurrency ruleAmount = new ArmCurrency(0.0d);
    ArmCurrency startRange = new ArmCurrency(0.0d);
    ArmCurrency endRange = new ArmCurrency(0.0d);
    	double percent = 0.00;
    	double rulePercent = 0.00;
    	
    	String discountCode = ((CMSDiscount)discount).getDiscountCode();	
    	ArmDiscountRule rule = CMSDiscountMgr.getDiscountRuleByRange(discountCode, 
    			lineItem.getItemSellingPrice());
    	NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMaximumFractionDigits(1);
  	    	  
    	if (rule != null) {
    		Discount[] discountArray = lineItem.getDiscountsArray();
    		for (int i = 0; i < discountArray.length; i++) {
				if (discountCode.equals(((CMSDiscount)discountArray[i]).getDiscountCode())) {
					message.append("A discount has already been applied for this reason code. "); 
					message.append("Please select a different reason code to continue.");
					((CMSDiscount)discount).setApplyDiscount(false);
					return new RulesInfo(message.toString());
				}
    		}
    		rulePercent = rule.getPercent().doubleValue();
    		ruleAmount = new ArmCurrency(rule.getMoDsc().doubleValue());
    		percent = discount.getPercent();
    		amount = new ArmCurrency(discount.getAmount().doubleValue());
    		startRange = new ArmCurrency(rule.getStartRange().doubleValue());
			endRange = new ArmCurrency(rule.getEndRange().doubleValue());
    		if (((CMSDiscount)discount).isLineItemDiscount) {
  				if (discount.isDiscountPercent()) {
  					if (percent > rulePercent) {
  						message.append(nf.format(percent) + " discount cannot be applied for item with selling price ");
  						message.append("between " + startRange.formattedStringValue() + " and "); 
  						message.append(endRange.formattedStringValue() + ". The applied discount for ");
  						message.append("this item is " + nf.format(rulePercent));
  						((CMSDiscount)discount).setApplyDiscount(true);
  						System.out.println("Ruchi inside ValidateDiscountRule   :"+rule.getCdNote());
  						((CMSDiscount)discount).setDiscountCodeNote(rule.getCdNote());
  						discount.setPercent(rulePercent);
  						return new RulesInfo(message.toString());
  					}  					
  				} else {
  					if (amount.greaterThan(ruleAmount)) {
  						message.append("A discount amount of " + amount.formattedStringValue() + " cannot be applied "); 
  						message.append("for item with selling price between " + startRange.formattedStringValue() + " and "); 
  						message.append(endRange.formattedStringValue() + ". The applied discount amount for "); 
  						message.append("this item is " + ruleAmount.formattedStringValue());
  						((CMSDiscount)discount).setApplyDiscount(true);
  						((CMSDiscount)discount).setDiscountCodeNote(rule.getCdNote());
  						discount.setAmount(ruleAmount);
  						return new RulesInfo(message.toString());
  					}
  				}
  			}
    	}
	} catch (Exception ex) {
		LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
				, "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
		return new RulesInfo();
	}
	return new RulesInfo();
  }

  /**
   * Returns the name of the rule.
   * @return the name of the rule
   */
  public String getName() {
    return "ValidateDiscountRule";
  }

  /**
   * Returns a user-friendly version of the rule.
   * @return a user-friendly version of the rule.
   */
  public String getDesc() {
    String result = "ValidateDiscountRule";
    return result;
  }
}

