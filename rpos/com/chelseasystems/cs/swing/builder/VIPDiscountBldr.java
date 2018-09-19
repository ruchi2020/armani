/**
 * This class is created for VIP membership discount by Vivek Sawant
 * @author vivek.sawant
 *  
**/
package com.chelseasystems.cs.swing.builder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.chelseasystems.cr.appmgr.AppletManager;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.appmgr.IObjectBuilder;
import com.chelseasystems.cr.appmgr.IObjectBuilderManager;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.util.ResourceManager;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.customer.CMSCustomerHelper;
import com.chelseasystems.cs.customer.CMSVIPMembershipDetail;
import com.chelseasystems.cs.discount.CMSVIPDiscount;
import com.chelseasystems.cs.discount.CMSVIPDiscountMgr;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSReturnLineItem;
import com.chelseasystems.cs.pos.CMSSaleLineItem;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.dlg.GenericChooseFromTableDlg;
import com.chelseasystems.cs.swing.pos.DiscountReasonHelper;

public class VIPDiscountBldr implements IObjectBuilder{
	 private ConfigMgr config = null;
	 private IObjectBuilderManager theBldrMgr;
	 private IApplicationManager theAppMgr;
	 private CMSVIPDiscount discount = null;
	 private CMSApplet applet;
	 private CMSCompositePOSTransaction theTxn;
	 private double discount_pct;
	 private CMSCustomer customer= null;
	 private boolean solicitReason;
	 private GenericChooseFromTableDlg overRideDlg;
	 private CMSVIPMembershipDetail membershipDetail = null;
	 String Command;
	 CMSApplet appletBuild;
	 Object initValue;
	 public VIPDiscountBldr() {
	  }

	 /**
	   * @param theBldrMgr
	   * @param theAppMgr
	   */
	  public void init(IObjectBuilderManager theBldrMgr, IApplicationManager theAppMgr) {
	    config = new ConfigMgr(System.getProperty("USER_CONFIG"));
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
		  if (theCommand.equals("VIP_DISCOUNT")) {
				// this is done by vivek sawant for validating Expiry date and allocating membership ID
			   String membershipID = (String)theEvent;
			   theTxn.setVipMembershipID(membershipID);
			   CMSStore store = (CMSStore) theTxn.getStore();
				String brand_ID = store.getBrandID();
			   if(theAppMgr.isOnLine()){
				   membershipDetail = CMSCustomerHelper.selectByMembershipNumber(theAppMgr , membershipID, brand_ID);
					 try {
						  if(membershipDetail.getCustomer_id()!=null)
						  customer = CMSCustomerHelper.findById(theAppMgr , membershipDetail.getCustomer_id());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					Date expiry_dt = membershipDetail.getExpiry_dt();
					if(membershipDetail.getMembership_id()!=null && membershipDetail.getCustomer_id()!=null){
						discount_pct = membershipDetail.getDiscount_pct();
						checkExpiryDate(expiry_dt);						
						theTxn.setVipMembershipID(membershipDetail.getMembership_id());
					}
					else{
						theAppMgr.showErrorDlg("Please Enter correct Membership ID");
						completeAttributes();
					}  
			   }
		   }
	  }
	  
	  
	  private boolean completeAttributes() {
		  theAppMgr.setSingleEditArea(applet.res.getString("Enter Membership ID."), "VIP_DISCOUNT"
		          , theAppMgr.NO_MASK);
		  
		      return (false);
	  }
	public void build(String Command, CMSApplet applet, Object initValue) {
            
		 Command= this.Command;
		 appletBuild = this.applet;
		 initValue = this.initValue;
	    discount = (CMSVIPDiscount)CMSVIPDiscountMgr.createDiscount("VIP_DISCOUNT");
	    theTxn = (CMSCompositePOSTransaction)theAppMgr.getStateObject("TXN_POS");
	    this.applet = applet;
	    // register for call backs
	  //  removeDiscountFromTxnObject();
	    if (completeAttributes()) {
	      if (theAppMgr.getStateObject("VIP_DISCOUNT") != null)
	        theAppMgr.removeStateObject("VIP_DISCOUNT");
	      theAppMgr.addStateObject("VIP_DISCOUNT", discount);
	      theBldrMgr.processObject(applet, "VIP_DISCOUNT", discount, this);
	    }

	}
	
	public void checkExpiryDate (Date expiry_dt){
		boolean flag;
		if(expiry_dt!=null){
			Date currentDate = new Date();
			String DATE_FORMAT = "MM/dd/yyyy";
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
			String strExpiry_dt = dateFormat.format(expiry_dt);
			String strCurrentDate = dateFormat.format(currentDate);
			try {
				expiry_dt = dateFormat.parse(strExpiry_dt);
				currentDate = dateFormat.parse(strCurrentDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 int i = expiry_dt.compareTo(currentDate);
            	 if(i>(-1)|| i==0){
            		 double displayDiscount = discount_pct*100; 
            	     String message = "VIP Customer Found. The discount applied is : "+displayDiscount+". Do u want to continue?";
            	     flag = theAppMgr.showOptionDlg("VIP Membership Expiry Date ",message); 
           		if(membershipDetail.getDiscount_pct()!= 0.0d && flag){
           		 try {
           			discount_pct = membershipDetail.getDiscount_pct();
          	         POSLineItem[] posLineItems = (POSLineItem[])theTxn.getLineItemsArray();
      		          if (posLineItems != null ) {
      		            for (int j = 0; j < posLineItems.length; j++) {
      		            	if(posLineItems[j].getDiscount()==null){
      		                   	if (posLineItems[j] instanceof CMSSaleLineItem) {
		      		        	  discount.setIsLineItemDiscount(true);
		                	      discount.setPercent(discount_pct);
								  //Vivek Mishra : Merged updated code from source provided by Sergio 24-MAY-2016
		                	      // added by Riccardo 
		                	      discount.setDiscountCode("86");
								  //Ends
		                          posLineItems[j].addDiscount(discount);
		      		              }
      		                   	else if (posLineItems[j] instanceof CMSReturnLineItem
		      		                  && ((CMSReturnLineItem)posLineItems[j]).isMiscReturn()) {
		      		            	  discount.setIsLineItemDiscount(true);
		      	          	          discount.setPercent(discount_pct);
									  //Vivek Mishra : Merged updated code from source provided by Sergio 24-MAY-2016
		      	          	          // added by Riccardo 
		      	          	          discount.setDiscountCode("86");
									  //Ends
		      	                   	  posLineItems[j].addDiscount(discount);
      		                     }
      		                   	else
      		                continue; 
      		            	}
      		            	else if (posLineItems[j].getDiscount()!=null && posLineItems[j].getDiscount().getPercent()<discount_pct){
      		                  posLineItems[j].removeDiscount(posLineItems[j].getDiscount());
      		            	  discount.setIsLineItemDiscount(true);
	                	      discount.setPercent(discount_pct);
							  //Vivek Mishra : Merged updated code from source provided by Sergio 24-MAY-2016
	                	      // added by Riccardo 
	                	      discount.setDiscountCode("86");
							  //Ends
	                          posLineItems[j].addDiscount(discount);
      		            	}
      		            }//for loop
      		          }
      		        } catch (Exception e) {
      		          System.out.println(e);
      		        }
      		      try {
             			if(customer!=null){
             				theTxn.setCustomerPostTransaction(customer);
  						theTxn.setCustomer(customer);
             			}
  					if(discount!=null){
  						theTxn.addDiscount(discount);
  						
  					}
  							} catch (BusinessRuleException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				}
           		}
           		
           		
           	//	theAppMgr.showErrorDlg("Current Date :"+currentDate+" is less than Expiry date :"+expiry_dt);
           		/* double displayDiscount = discount_pct*100;
				 String message = "VIP Customer Found. The discount applied is : "+displayDiscount+". Do u want to continue?";
				 boolean flag = theAppMgr.showOptionDlg("VIP Membership Expiry Date ",message);
				 */ ConfigMgr config = new ConfigMgr("pos.cfg");
		         String vipMembershipPaymentApplet = config.getString("VIP_MEMBERSHIP_PAYMENT_APPLET");
				 if(flag){
					 AppletManager app = new AppletManager(theAppMgr);
					 app.getCMSApplet(vipMembershipPaymentApplet);
					theAppMgr.showApplet(vipMembershipPaymentApplet);
				 }
				 else{
					 completeAttributes();
					 return;
				 }
           	}
           	else{     
           		theAppMgr.showErrorDlg("Current Date :"+currentDate+" is Greator than Expiry date :"+expiry_dt);
                }
        	}
			else{
				 String message = "The VIP membership Date is Expired";
				theAppMgr.showOptionDlg("VIP Membership Expiry Date ",message);
			
			}
	    theBldrMgr.processObject(applet, "VIP_DISCOUNT", discount, this);
	}
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
