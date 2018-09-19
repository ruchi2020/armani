/*
 * @copyright (C) 1998-2002 Retek Inc
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 03-15-2005 | Khyati    | N/A       |1.BASE                                        |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 03-15-2005 | Khyati    | N/A       | Discount and Pricing specs                   |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 3    | 05-31-2005 | Sameena   | 78        | Discount/By Price: Discount not applied      |
 |      |            |           |           | correctly. Applied as "By Amount". Modified  |
 |      |            |           |           | the method 'completeAttributes'.             |
 +------+------------+-----------+-----------+----------------------------------------------+
 */


package com.chelseasystems.cs.swing.builder;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.discount.*;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cr.register.LightPoleDisplay;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cs.swing.pos.DiscountReasonHelper;
import com.chelseasystems.cs.swing.dlg.*;
import com.chelseasystems.cr.util.ResourceManager;
import com.chelseasystems.cs.discount.*;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.util.LineItemPOSUtil;
import com.chelseasystems.cs.util.Version;


/**
 *
 * Builder to construct an 'Other' <code>Discount</code> object with a single
 *    reason parameter.  The discount created is based on the initial value
 *    passed to the builder.
 */
public class GenericDiscountBldr implements IObjectBuilder {
  private CMSDiscount discount = null;
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;
  private boolean solicitReason;
  private GenericChooseFromTableDlg overRideDlg;
  private CMSCompositePOSTransaction txn;
  private CMSEmployeeDiscount empDiscount;
  private boolean isEmployeeDiscount;
  private POSLineItem lineItem;

  /**
   * @param theBldrMgr
   * @param theAppMgr
   */
  public void init(IObjectBuilderManager theBldrMgr, IApplicationManager theAppMgr) {
    this.theBldrMgr = theBldrMgr;
    this.theAppMgr = theAppMgr;
  }

  /**
   */
  public void cleanup() {
    discount = null;
  }

  /**
   * @param theCommand
   * @param theEvent
   */
  public void EditAreaEvent(String theCommand, Object theEvent) {
    if (theCommand.equals("AMOUNT") || theCommand.equals("BY_PRICE")) {
      ArmCurrency disAmount = (ArmCurrency)theEvent;
      lineItem = (POSLineItem)theAppMgr.getStateObject("SELECTED_LINE_ITEM"); 
      POSLineItem[] lineItemArray = txn.getLineItemsArray();
      try {
        // Check for line discounts
    	  // start code for Issue #1947 LightPole Display by Neeti
        if (discount.getIsLineItemDiscount()) {
          POSLineItem lineItem = (POSLineItem)theAppMgr.getStateObject("SELECTED_LINE_ITEM");
          if (theCommand.equals("AMOUNT")) {
            if (disAmount.doubleValue() > Math.abs(lineItem.getNetAmount().doubleValue())) {
              theAppMgr.showErrorDlg(applet.res.getString(
                  "Discount amount cannot be greater than the unit item's due amount"));
              completeAttributes();
              return;
            } if("US".equalsIgnoreCase(Version.CURRENT_REGION)) {
           	 if(lineItem.getDiscount() == null){
              	LightPoleDisplay.getInstance().itemSoldForDiscount(lineItem, lineItem.getExtendedRetailAmount().formattedStringValue(), (lineItem.getExtendedRetailAmount().subtract(disAmount)).formattedStringValue());
              }
              else{
              	LightPoleDisplay.getInstance().itemSoldForDiscount(lineItem, lineItem.getExtendedRetailAmount().formattedStringValue(), txn.getCompositeNetAmount().subtract(disAmount).formattedStringValue());
              }
         }    
          }
          if (theCommand.equals("BY_PRICE")) {
            ArmCurrency sellingPrice = LineItemPOSUtil.getSellingPrice(lineItem);
            if (disAmount.doubleValue() > sellingPrice.doubleValue()) {
              theAppMgr.showErrorDlg(applet.res.getString(
                  "Price Discount amount cannot be greater than the selling price of the item"));
              completeAttributes();
              return;
            }else{
            	if("US".equalsIgnoreCase(Version.CURRENT_REGION)){
           		 lineItem = (POSLineItem)theAppMgr.getStateObject("SELECTED_LINE_ITEM");         	   
       			 double curr = disAmount.doubleValue();
       			 ArmCurrency currdisc = new ArmCurrency(curr); 
       			 if(lineItem.getDiscount() == null){
       				 LightPoleDisplay.getInstance().itemSoldForDiscount(lineItem, lineItem.getExtendedRetailAmount().formattedStringValue(),currdisc.formattedStringValue()); 
       			 }
       			 else{			
       				 double netDiscount,extRetailamt, extReductionAmt;
       				 extRetailamt= lineItem.getExtendedRetailAmount().getDoubleValue();
       				 extReductionAmt=lineItem.getExtendedReductionAmount().getDoubleValue();
       				 netDiscount = extReductionAmt/extRetailamt;
       				 ArmCurrency totalAmount = currdisc.multiply(netDiscount);				
       				 LightPoleDisplay.getInstance().itemSoldForDiscount(lineItem, lineItem.getExtendedRetailAmount().formattedStringValue(),currdisc.subtract(totalAmount).formattedStringValue());
       			 }			 	
                   
          	}
           }		
          }
        } else if(discount.isMultiDiscount){ 
        	if("US".equalsIgnoreCase(Version.CURRENT_REGION)){
        		if(theCommand.equals("AMOUNT")){        		 	 
              	  for(int i=0; i<lineItemArray.length ; i++){
              		  lineItem = lineItemArray[i];  
              		  if(lineItem.getDiscount() == null){            			  
              			  LightPoleDisplay.getInstance().itemSoldForDiscount(lineItem, lineItem.getExtendedRetailAmount().formattedStringValue(), (lineItem.getExtendedRetailAmount().subtract(disAmount)).formattedStringValue());            			  
              		  }
              		  else{              			  
                     		LightPoleDisplay.getInstance().itemSoldForDiscount(lineItem, lineItem.getExtendedRetailAmount().formattedStringValue(),lineItem.getExtendedNetAmount().subtract(disAmount).formattedStringValue());                   		
              		  }            	  
              	  }            	
          	}
          	if(theCommand.equals("BY_PRICE")){
          		for(int i=0; i<lineItemArray.length ; i++){
            		  lineItem = lineItemArray[i];
  	          		double curr = disAmount.doubleValue();
  	          		ArmCurrency currdisc = new ArmCurrency(curr);  
  	          		if(lineItem.getDiscount()==null){
  	          			LightPoleDisplay.getInstance().itemSoldForDiscount(lineItem, lineItem.getExtendedRetailAmount().formattedStringValue(), currdisc.formattedStringValue());
  	          		}
  	          		
  	          		else{
  	          			 double netDiscount,extRetailamt, extReductionAmt;
  	    				 extRetailamt= lineItem.getExtendedRetailAmount().getDoubleValue();
  	    				 extReductionAmt=lineItem.getExtendedReductionAmount().getDoubleValue();
  	    				 netDiscount = extReductionAmt/extRetailamt;
  	    				 ArmCurrency totalAmount = currdisc.multiply(netDiscount);				
  	    				 LightPoleDisplay.getInstance().itemSoldForDiscount(lineItem, lineItem.getExtendedRetailAmount().formattedStringValue(),currdisc.subtract(totalAmount).formattedStringValue());
  	          		}
            	  }           		
          	}
          
        	}
        } else if(discount.isSubTotalDiscount){ 
        	if("US".equalsIgnoreCase(Version.CURRENT_REGION)){
        		if(theCommand.equals("AMOUNT")){        		 	 
              	  for(int i=0; i<lineItemArray.length ; i++){
              		  lineItem = lineItemArray[i];  
              		  if(lineItem.getDiscount() == null){
              			  LightPoleDisplay.getInstance().itemSoldForDiscount(lineItem, lineItem.getExtendedRetailAmount().formattedStringValue(), (lineItem.getExtendedRetailAmount().subtract(disAmount)).formattedStringValue());
              		  }
              		  else{   			
                     		LightPoleDisplay.getInstance().itemSoldForDiscount(lineItem, lineItem.getExtendedRetailAmount().formattedStringValue(),lineItem.getExtendedNetAmount().subtract(disAmount).formattedStringValue());
              		  }
              	  }            	
          	}
        	}    
    }else{          	 
    	if("US".equalsIgnoreCase(Version.CURRENT_REGION)){
    		if(theCommand.equals("AMOUNT")){        		 	 
          	  for(int i=0; i<lineItemArray.length ; i++){
          		  lineItem = lineItemArray[i];        			 
       			 LightPoleDisplay.getInstance().itemSoldForDiscount(lineItem, lineItem.getExtendedRetailAmount().formattedStringValue(),lineItem.getExtendedNetAmount().subtract(disAmount).formattedStringValue());
       		  }            	
      	}
    	}
    	
    }
    // End code for Issue #1947 LightPole Display by Neeti
        // Check only for sub total discount
        if (txn.getCompositeNetAmount().absoluteValue().lessThan(disAmount) && discount.getIsSubTotalDiscount()) {
          theAppMgr.showErrorDlg(applet.res.getString(
              "Discount amount cannot be greater than the total amount due "));
          completeAttributes();
          return;
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      discount.doSetAmount(disAmount);
    } else if (theCommand.equals("OTHER")) {
      Double disPercent = (Double)theEvent;
      lineItem = (POSLineItem)theAppMgr.getStateObject("SELECTED_LINE_ITEM");        
      POSLineItem[] lineItemArray = txn.getLineItemsArray();  
      if (disPercent.doubleValue() > 100.00d) {
        theAppMgr.showErrorDlg(applet.res.getString(
            "Discount percentage cannot be greater than the total amount due "));
        completeAttributes();
        return;
      }//Start for #1947 pole display by Neeti
      if (discount.getIsLineItemDiscount()) {
    	  if("US".equalsIgnoreCase(Version.CURRENT_REGION)){
    			try {      
    				 double currentDisc = disPercent.doubleValue();  
    				 double discountPercent = currentDisc/100;      		
    				 ArmCurrency lineItemfordiscount = lineItem.getExtendedRetailAmount().multiply(discountPercent);
    				 if(lineItem.getDiscount() == null){
    					LightPoleDisplay.getInstance().itemSoldForDiscount(lineItem, lineItem.getExtendedRetailAmount().formattedStringValue(), (lineItem.getExtendedRetailAmount().subtract(lineItemfordiscount)).formattedStringValue());
    				 }
    				 else{				
    					ArmCurrency disc = txn.getCompositeNetAmount().multiply(discountPercent); 
    					LightPoleDisplay.getInstance().itemSoldForDiscount(lineItem, lineItem.getExtendedRetailAmount().formattedStringValue(), txn.getCompositeNetAmount().subtract(disc).formattedStringValue());
    				 }          		
    				 
    				} catch (CurrencyException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    	  }
      
      }   
      
      else if(discount.isSubTotalDiscount) { 
    	  if("US".equalsIgnoreCase(Version.CURRENT_REGION)){
    		  for(int i=0; i<lineItemArray.length ; i++){
        		  lineItem = lineItemArray[i];
        		  double curr = disPercent.doubleValue();  
        		  double discountPercent = curr/100;      		
        		  ArmCurrency lineItemfordiscount = lineItem.getExtendedRetailAmount().multiply(discountPercent);
            	  try {
        			 if(lineItem.getDiscount()==null){
        				 LightPoleDisplay.getInstance().itemSoldForDiscount(lineItem, lineItem.getExtendedRetailAmount().formattedStringValue(), (lineItem.getExtendedRetailAmount().subtract(lineItemfordiscount)).formattedStringValue());
        			 }
        			 else{
        				 ArmCurrency disc = txn.getCompositeNetAmount().multiply(discountPercent);        		
        	       			LightPoleDisplay.getInstance().itemSoldForDiscount(lineItem, lineItem.getExtendedRetailAmount().formattedStringValue(), txn.getCompositeNetAmount().subtract(disc).formattedStringValue());
        			 }
    			  } catch (CurrencyException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
        	  }  
    	  }
    	  	 
      }
      else{    	
    	  if("US".equalsIgnoreCase(Version.CURRENT_REGION)){
    		  try{
    	      	  for(int i=0; i<lineItemArray.length ; i++){
    	      	  lineItem = lineItemArray[i]; 
    	      	  double curr = disPercent.doubleValue();
    	      	  double discountPercent = curr/100;    	 
    	      	  ArmCurrency lineItemfordiscount = lineItem.getExtendedNetAmount().multiply(discountPercent);    	 
    	  		  ArmCurrency totalAmount = lineItemfordiscount.add(lineItem.getExtendedReductionAmount());  		 		
    	          LightPoleDisplay.getInstance().itemSoldForDiscount(lineItem, lineItem.getExtendedRetailAmount().formattedStringValue(),lineItem.getExtendedRetailAmount().subtract(totalAmount).formattedStringValue());    	  		  
    	         }
    	        } catch (CurrencyException e) {
    	  			// TODO Auto-generated catch block
    	  			e.printStackTrace();
    	  		}
    	  }
      	
  	  }
      
  	//Ended for #1947 pole display by Neeti
      discount.doSetPercent(disPercent.doubleValue() / 100);
    }
    if (completeAttributes()) {
      //           Discount[] aDiscount = txn.getDiscountsArray();
      //           if (aDiscount != null)
      //             discount.setSequenceNumber(aDiscount.length);
      //           else
      //             discount.setSequenceNumber(0);
      if (txn.getEmployeeSale() && CMSDiscountMgr.isEmployeeReasonCode(discount.getDiscountCode())) {
        buildEmployeeDiscount();
      }
      if (isEmployeeDiscount) {
        theBldrMgr.processObject(applet, "DISCOUNT", empDiscount, this);
      } else {
        theBldrMgr.processObject(applet, "DISCOUNT", discount, this);
      }
    }
  }

  /**
   * @param Command
   * @param applet
   * @param initValue
   */
  public void build(String Command, CMSApplet applet, Object initValue) {
    discount = CMSDiscountMgr.createDiscount(initValue.toString());
    this.applet = applet;
    theAppMgr.showMenu(MenuConst.CANCEL_ONLY, "GENERIC_DISCOUNT", applet.theOpr);
    // register for call backs
    txn = (CMSCompositePOSTransaction)theAppMgr.getStateObject("TXN_POS");
    Boolean isSubTotalDiscount = new Boolean(false);
    isSubTotalDiscount = (Boolean)theAppMgr.getStateObject("IS_SUBTOTAL_DISCOUNT");
    Boolean isLineItemDiscount = new Boolean(false);
    isLineItemDiscount = (Boolean)theAppMgr.getStateObject("IS_LINE_ITEM_DISCOUNT");
    Boolean isMultiDiscount = new Boolean(false);
    isMultiDiscount = (Boolean)theAppMgr.getStateObject("IS_MULTI_DISCOUNT");
    if (isSubTotalDiscount != null && isSubTotalDiscount.booleanValue()) {
      discount.setIsSubTotalDiscount(true);
      theAppMgr.removeStateObject("IS_SUBTOTAL_DISCOUNT");
    }
    if (isLineItemDiscount != null && isLineItemDiscount.booleanValue()) {
      discount.setIsLineItemDiscount(true);
      theAppMgr.removeStateObject("IS_LINE_ITEM_DISCOUNT");
    }
    if (isMultiDiscount != null && isMultiDiscount.booleanValue()) {
      discount.setIsMultiDiscount(true);
      theAppMgr.removeStateObject("IS_MULTI_DISCOUNT");
    }
    if (completeAttributes()) {
      //        Discount[] aDiscount = txn.getDiscountsArray();
      //        if (aDiscount != null)
      //          discount.setSequenceNumber(aDiscount.length);
      //        else
      //          discount.setSequenceNumber(0);
      if (txn.getEmployeeSale() && CMSDiscountMgr.isEmployeeReasonCode(discount.getDiscountCode())) {
        buildEmployeeDiscount();
      }
      if (isEmployeeDiscount) {
        theBldrMgr.processObject(applet, "DISCOUNT", empDiscount, this);
      } else {
        theBldrMgr.processObject(applet, "DISCOUNT", discount, this);
      }
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  private boolean buildEmployeeDiscount() {
    empDiscount = (CMSEmployeeDiscount)CMSDiscountMgr.createDiscount("EMPLOYEE");
    try {
      if (empDiscount instanceof CMSEmployeeDiscount) {
        CMSEmployeeDiscount armEmpDisocunt = (CMSEmployeeDiscount)theAppMgr.getStateObject(
            "ARM_EMPLOYEE_DISCOUNT");
        if (armEmpDisocunt != null)
          empDiscount.setEmployee(armEmpDisocunt.getEmployee());
        else
          empDiscount.setEmployee(txn.getEmployee());
        empDiscount.setApplyTo(discount.getApplyTo());
        empDiscount.setPercent(discount.getPercent());
        empDiscount.setPromoDiscountPercent(discount.getPercent());
        empDiscount.setAmount(discount.getAmount());
        empDiscount.setIsSignatureRequired(discount.isSignatureRequired());
        empDiscount.setIsInAdditionToMarkdown(discount.isInAdditionToMarkdown());
        empDiscount.setIsDiscountPercent(discount.isDiscountPercent());
        empDiscount.setMethodOfReduction(discount.getMethodOfReduction());
        empDiscount.setConvertToMarkdown(discount.getConvertToMarkdown());
        empDiscount.setManualEntry(discount.isManualEntry());
        empDiscount.setIsLineItemDiscount(discount.isLineItemDiscount);
        empDiscount.setIsSubTotalDiscount(discount.isSubTotalDiscount);
        empDiscount.setIsMultiDiscount(discount.isMultiDiscount);
        empDiscount.setSequenceNumber(discount.getSequenceNumber());
        empDiscount.setIsOverridden(true);
        empDiscount.setType(discount.getType());
        //Fix for issue # 29880 discount reason code was not getting populated for employee discount.
        empDiscount.setDiscountCode(discount.getDiscountCode());
        isEmployeeDiscount = true;
        // Replacing the state employee discount object in case of new transaction level discount
        if (!empDiscount.isLineItemDiscount && !empDiscount.isSubTotalDiscount
            && !empDiscount.isMultiDiscount)
          theAppMgr.addStateObject("ARM_EMPLOYEE_DISCOUNT", empDiscount);
        return true;
        
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      theAppMgr.showErrorDlg(applet.res.getString("Error adding Additional Employee Discount: "
          + ex.getMessage()));
    }
    isEmployeeDiscount = false;
    return false;
  }

  /**
   * @return
   */
  private boolean completeAttributes() {
	  ConfigMgr config = new ConfigMgr("discount.cfg");
      String sDefaultReasonCode = config.getString("DEFAULT_REASON_CODE");
     System.out.println("Ruchi inside GenericDiscountBldr  :"+sDefaultReasonCode);
    if (discount.getReason() == null || discount.getReason().length() == 0) {
      displayDiscountReason();
      Boolean overrideEmployeeDiscount = (Boolean)theAppMgr.getStateObject(
          "OVERRIDE_EMPLOYEE_DISCOUNT");
      theAppMgr.removeStateObject("OVERRIDE_EMPLOYEE_DISCOUNT");
      if (overrideEmployeeDiscount != null && overrideEmployeeDiscount.booleanValue()) {
        discount.setDiscountCode(CMSDiscountMgr.getEmployeeReasonCode());
        discount.doSetReason((String)CMSDiscountMgr.getReasonHashtable().get(CMSDiscountMgr.getEmployeeReasonCode()));
        if (discount.getType() == null || !discount.getType().equals("BY_PRICE_DISCOUNT")) {
        	System.out.println("Inside GenericDiscountBldr discountreason code   :"+discount.getReason());
          discount.doSetType(discount.getReason());
        }
      } else if (solicitReason) {
        overRideDlg.setVisible(true);
        if (overRideDlg.isOK()) {
          Object reasonCode = overRideDlg.getSelectedRow().getRowKeyData();
          //Vivek Mishra : Added region check for EUROPE as this control is no longer required there : 02-AUG-2016
          if (!("EUR".equalsIgnoreCase(Version.CURRENT_REGION)) && CMSDiscountMgr.isEmployeeReasonCode((String)reasonCode) && !txn.getEmployeeSale()) {
            theAppMgr.showErrorDlg(applet.res.getString("Employee discount reason can only be used with Employee Sale transaction (See menu Other Txns/Employee Sale)"));
            theBldrMgr.processObject(applet, "DISCOUNT", null, this);
            return false;
          }
          discount.setDiscountCode((String)reasonCode);
          Object reasons[] = overRideDlg.getSelectedRow().getDisplayRow();
          // SP: modified the code to not set the type as the selected reason code
          // in case if the discount type is 'BY_PRICE_DISCOUNT'. This change is so
          // that the code in InitialSaleApplet for this type of discount gets
          // executed
          if (discount.getType() == null || !discount.getType().equals("BY_PRICE_DISCOUNT")) {
            discount.doSetType((String)reasons[0]);
          }
          System.out.println("Ruchi printing discount reason code   :"+(String)reasons[0]);
          discount.doSetReason((String)reasons[0]);
          //            return true;
        } else {
          // Issue # 1229
          // When Click on cancel, it should go back to Add items menu
          theBldrMgr.processObject(applet, "DISCOUNT", null, this);
          return false;
        }
        //Anjana added to set default reason code if solicit flag is false
      }else if(!solicitReason){
    	  if(sDefaultReasonCode!=null){
    		  System.out.println("Ruchi inside GenericDiscountBldr  solicitReason is false :"+sDefaultReasonCode);
    	  discount.setDiscountCode(sDefaultReasonCode);
    	  }
      }
    }
    if (buildDiscount())
      return true;
    else
      return false;
  }

  /**
   * put your documentation comment here
   * @return
   */
  private boolean buildDiscount() {
    if (discount.isManualEntry() && (discount.getMethodOfReduction() == CMSDiscount.TOTAL_PRICE_OFF)
        && (discount.getAmount().doubleValue() == 0.0d)) {
      theAppMgr.setSingleEditArea(applet.res.getString("Enter discount amount for selected item(s)")
          , "AMOUNT", theAppMgr.CURRENCY_MASK);
      return false;
    } else if (discount.isManualEntry() && (discount.getMethodOfReduction() == CMSDiscount.UNIT_PRICE_OFF)
        && (discount.getAmount().doubleValue() == 0.0d)) {
      theAppMgr.setSingleEditArea(applet.res.getString("Enter discount amount for selected item(s)")
          , "AMOUNT", theAppMgr.CURRENCY_MASK);
      return false;
    }
    else if (discount.isManualEntry()
        && (discount.getMethodOfReduction() == CMSDiscount.FIXED_TOTAL_PRICE)
        && (discount.getAmount().doubleValue() == 0.0d)) {
      theAppMgr.setSingleEditArea(applet.res.getString(
          "Enter new discounted selling price for selected item(s)"), "BY_PRICE"
          , theAppMgr.CURRENCY_MASK);
      return false;
    } else if (discount.isManualEntry()
        && (discount.getMethodOfReduction() == CMSDiscount.PERCENTAGE_OFF)
        && (discount.getPercent() == 0.0d)) {
      theAppMgr.setSingleEditArea(applet.res.getString(
          "Enter discount percent for selected item(s)"), "OTHER", theAppMgr.DOUBLE_MASK);
      return false;
    }
    return true;
  }

  /**
   *
   */
  private void displayDiscountReason() {
    DiscountReasonHelper discountReasonHelper = new DiscountReasonHelper();
    solicitReason = discountReasonHelper.isSolicitReasons();
    if (solicitReason) {
      String[] titles = {ResourceManager.getResourceBundle().getString("Discount Reason")
      };
      overRideDlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame(), theAppMgr
          , discountReasonHelper.getTabelData(), titles);
    }
  }
}

