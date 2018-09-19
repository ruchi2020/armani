/*
 History:
 +------+------------+-----------+----------+----------------------------------------------------------------+
 | Ver# | Date       | By        | Defect # | Description                                                    |
 +------+------------+-----------+----------+----------------------------------------------------------------+
 | 9    | 07-18-2005 | Vikram    | 618      | GUI Builder prompts were not correct after system goes into    |
 |      |            |           |          | off-line mode during redeem.                                   |
 +------+------------+-----------+----------+----------------------------------------------------------------+
 | 8    | 06-21-2005 | Vikram    | N/A      | Added check for both Gift card and Due Bill bin range          |
 +------+------------+-----------+----------+----------------------------------------------------------------+
 | 7    | 06-21-2005 | Vikram    | 195      | Added support for delete-status check while processing         |
 +------+------------+-----------+----------+----------------------------------------------------------------+
 | 6    | 06-15-2005 | Vikram    | 144      | Modified to support MSR                                        |
 +------+------------+-----------+----------+----------------------------------------------------------------+
 | 5    | 06/06/2005 | Vikram    | 70       | Customer is required if tender type is Gift Card / Credit Note.|
 |      |            |           |          | Customer is checked after payment details are build.           |
 -------------------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.builder;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.payment.DueBill;
import com.chelseasystems.cs.payment.CMSRedeemableHelper;
import com.chelseasystems.cs.payment.CMSRedeemable;
import com.chelseasystems.cs.payment.CMSDueBill;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.ajbauthorization.AJBRequestResponseMessage;
import com.chelseasystems.cs.ajbauthorization.AJBServiceManager;
import com.chelseasystems.cs.authorization.bankcard.CMSCreditAuthHelper;
import com.chelseasystems.cs.msr.CMSMSR;
import com.chelseasystems.cs.msr.NonJavaPOSMSR;
import com.chelseasystems.cs.util.IsDigit;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cs.payment.CMSStoreValueCard;
import com.chelseasystems.cs.payment.CMSDueBillIssue;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.CMSAppModelFactory;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSPaymentTransactionAppModel;
import com.chelseasystems.cs.util.TransactionUtil;


/**
 * The purpose of a "Bldr" class is to build a complex object, and pass it back
 * to the GUI.
 * The GUI provides a due bill id.  The due bill
 * builder takes control until it can return a due bill
 * with all properties set (from the server), or null if the due bill
 * does not exist.
 * @author John Gray
 * @version 1.0a
 */
public class DueBillBldr implements IObjectBuilder {
  private Redeemable dBill = null;
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;
  private boolean flag;
  //ks: for offline validation of the tender
  private boolean isOffline = false;
  private String manualAuthCode = null;
  private CMSRedeemable cmsRedeemable = null;
  private CMSMSR cmsMSR = null;
  private CMSPaymentTransactionAppModel theTxn = null;
  
  private static ConfigMgr config;
  private String fipay_flag;
  private String fipay_GiftCard_flag;
  private static Logger log = Logger.getLogger(AJBServiceManager.class.getName());
  boolean isManualKeyEntry;

  // The following line is for testing only...TBD
  public DueBillBldr() {
  }

  /**
   * Initialize the environment.
   * @param theBldrMgr the builder manager
   * @param theAppMgr the application manager
   */
  public void init(IObjectBuilderManager theBldrMgr, IApplicationManager theAppMgr) {
    this.theBldrMgr = theBldrMgr;
    this.theAppMgr = theAppMgr;
    this.accountNum = "";
    // Start Mayuri Edhara::
    String fileName = "store_custom.cfg";    
	config = new ConfigMgr(fileName);
	fipay_flag = config.getString("FIPAY_Integration");
    fipay_GiftCard_flag = config.getString("FIPAY_GIFTCARD_INTEGRATION");
    if(fipay_GiftCard_flag == null){
  	fipay_GiftCard_flag = "N";
    }

    // end Mayuri Edhara::
  }

  /**
   * Cleanup before exiting.
   */
  public void cleanup() {
	  if(cmsMSR!=null)
    cmsMSR.release();
    this.accountNum = "";
  }

  /**
   * Try to find the due bill on the server, using the record id.
   * @param theCommand the description of what the user typed.
   * @param theEvent what the user typed (the due bill id).
   */
  public void EditAreaEvent(String theCommand, Object theEvent) {
    if (theCommand.equals("DUE_BILL_ID")) {
      processSwipe((String)theEvent);
      return;
    } else if (theCommand.equals("MANUAL")) {
      if (isOffline) {
        cmsRedeemable.setIsManual(true);
      } else if (dBill instanceof CMSRedeemable) {
        ((CMSRedeemable)dBill).setIsManual(true);
      }
      theAppMgr.setSingleEditArea(applet.res.getString("Enter Gift Card or Credit Note ID."), "ID"
          , theAppMgr.UPPER_CASE_MASK);
      return;
    } else if (theCommand.equals("ID")) {
      if (!processID((String)theEvent))
        return;
    } else if (theCommand.equals("DB_AMOUNT")) { // end of id branch
      if (isOffline) {
        cmsRedeemable.setAmount((ArmCurrency)theEvent);
      } else {
        String paymentTypeView = dBill.getGUIPaymentName();
        String paymentType = dBill.getTransactionPaymentName();
        if (TransactionUtil.validateChangeAmount(theAppMgr, paymentType, paymentTypeView
            , (ArmCurrency)theEvent))
          dBill.setAmount((ArmCurrency)theEvent);
      }
    }
    //ks AUTH_CODE
    else if (theCommand.equals("AUTH_CODE")) {
      if (theEvent == null || ((String)theEvent).length() == 0) {
        theAppMgr.showErrorDlg(applet.res.getString("Auth Code cannot be blank."));
        return;
      } else {
        manualAuthCode = (String)theEvent;
        cmsRedeemable.setManualAuthCode(manualAuthCode);
        cmsRedeemable.setGUIPaymentName(applet.res.getString("REDEEMABLE"));
      }
    }
    //Mayuri edhara :: 10-MAR-17 - to add the flow to process the enterd Gift card number.
    else if(theCommand.equals("GIFTCARD_ID") && fipay_GiftCard_flag.equalsIgnoreCase("Y")){
    	if (!isOffline) {
    		if(!processGiftCardId((String)theEvent)){
			    theAppMgr.setSingleEditArea(applet.res.getString("Please Enter Gift Card ID :")
				            , "GIFTCARD_ID" , theAppMgr.UPPER_CASE_MASK);
    			return;
    		}
    	}
    }
    // Start Mayuri Edhara:: & vishal Yevale 13 oct 2016
    else if(theCommand.equals("AMOUNT") && fipay_GiftCard_flag.equalsIgnoreCase("Y")) { 			  
		  if (!isOffline) {
			  String error_message = "All the Ajb Servers are down at the moment";
			  //mayuri edhara :: renamed giftCardSwipeOrManualResponse to giftCardSwipeResponse as we are getting the Gift card number from swipe only.	
			  	Object giftCardSwipeResponse = null; 		  	
				String trackData = null;
				
				
				try{
				//Anjana added March 20 for overpayment not allowed 	
					if(getAmountDue().lessThan((ArmCurrency)theEvent)) {
						 theAppMgr.showErrorDlg("No over payment allowed.");	
						 dBill.setId(null);
						 completeAttributes();
						return;
					}//ends
					// ASk for normal swipe or manual swipe based on the manual Key entry from payment applet.
					if(!isManualKeyEntry){
						giftCardSwipeResponse = CMSCreditAuthHelper.getGCSwipeResponse(theAppMgr);
					//} else {
					//	giftCardSwipeOrManualResponse = CMSCreditAuthHelper.getGCManualEntryResponse(theAppMgr);
					//}

						//giftCardSwipeOrManualResponse = "107,2,4924190000005797=09121017265104678392";
					
						if (giftCardSwipeResponse == null)
						{
							System.out.println("Ruchi Gift Card swipe response :   "+error_message);
							theAppMgr.showErrorDlg(error_message);
							completeAttributes();
							return;
						}else if(giftCardSwipeResponse.toString().contains(error_message)){
							theAppMgr.showErrorDlg(error_message);
							completeAttributes();
							return;
						}else if(giftCardSwipeResponse.toString().contains("*ERROR")){
							
							if(giftCardSwipeResponse.toString().contains("InvalidCardType")){						
								theAppMgr.showErrorDlg("This is not a valid Gift card");
								completeAttributes();
								return;
								
							}else if(giftCardSwipeResponse.toString().contains("UserCanceled")){						
								theAppMgr.showErrorDlg("User pressed Cancel button");
								completeAttributes();
								return;
							}
							else if(giftCardSwipeResponse.toString().contains("Timeout")){
								theAppMgr.showErrorDlg("Timeout occured, try again.");
								completeAttributes();
								return;
							}
							
						}else if(giftCardSwipeResponse.toString().startsWith("107")){
							
							trackData = new AJBRequestResponseMessage(giftCardSwipeResponse.toString()).getValue(2); 
							
							setCardData(trackData);								
							
						}					
					}
					//Mayuri edhara :: 13-MAR-17 -added to show error message incase gift card funds are not sufficent.
					if(dBill instanceof CMSStoreValueCard){
						if( ((CMSStoreValueCard) dBill).getGiftcardBalance() != null 
								&& (((CMSStoreValueCard) dBill).getGiftcardBalance().lessThan((ArmCurrency)theEvent)) ) {
				        	  theAppMgr.showErrorDlg(applet.res.getString("Gift Card funds not sufficent. Current Balance on card is " 
				        			  	+ ((CMSStoreValueCard) dBill).getGiftcardBalance() 
				        			  	+ "Please proceed with another Gift Card/Payment")); 
				        	  dBill.setId(null); 
				        	  completeAttributes();
					          return;
				          }
					}else if(dBill instanceof CMSDueBill){
						if( ((CMSDueBill) dBill).getGiftcardBalance() != null 
								&& ( ((CMSDueBill) dBill).getGiftcardBalance().lessThan((ArmCurrency)theEvent)) ) {
				        	  theAppMgr.showErrorDlg(applet.res.getString("Gift Card funds not sufficent. Current Balance on card is " 
				        			  	+ ((CMSDueBill) dBill).getGiftcardBalance() 
				        			  	+ "Please proceed with another Gift Card/Payment")); 
				        	  dBill.setId(null);
				        	  completeAttributes();
					          return;
				          }
					}
					
					
				}catch (Exception e) {
					LoggingServices.getCurrent().logMsg(
							DueBillBldr.class.toString());
					log.error(e.toString());
				}
				
				//Mayuri edhara :: 13-MAR-17 -added to show error message incase gift card funds are not sufficent.
				if(dBill != null && dBill.getId() != null){
			        String paymentTypeView = dBill.getGUIPaymentName();
			        String paymentType = dBill.getTransactionPaymentName();
			        if (TransactionUtil.validateChangeAmount(theAppMgr, paymentType, paymentTypeView
			                , (ArmCurrency)theEvent))
			              dBill.setAmount((ArmCurrency)theEvent);
				 
		        
				 if(dBill instanceof CMSStoreValueCard){
					 ((CMSStoreValueCard) dBill).setAuthRequired(true);
				 }else if(dBill instanceof CMSDueBill){
					 ((CMSDueBill) dBill).setAuthRequired(true);
				 }
				 
				 				 
				}
		      }
		  }
    //end Mayuri Edhara  & vishal yevale 13 oct 2016
    
 
    //VM: the processObject now points to STORE_VALUE_CREDIT_MEMO_PAYMENT for customer requirement
    //Mayuri Edhara ::
    if (completeAttributes()) {
    	if(fipay_GiftCard_flag.equalsIgnoreCase("Y")) {
		     if (!isOffline){
		        theBldrMgr.processObject(applet, "STORE_VALUE_CREDIT_MEMO_PAYMENT", dBill, this);
		     }
		     dBill = null;
    	} else {
	      if (!isOffline)
	        theBldrMgr.processObject(applet, "STORE_VALUE_CREDIT_MEMO_PAYMENT", dBill, this);
	      else
	        theBldrMgr.processObject(applet, "STORE_VALUE_CREDIT_MEMO_PAYMENT", cmsRedeemable, this);
	      dBill = null;
	      cmsRedeemable = null;
    	} 
    }
  }
  
  /**
   * 
   * */
   private void setCardData(String cardData){
	   
	   Object giftCardBalanceInquiryResponse = null;
	   ArmCurrency giftcardBalance=null;
	   String cardNum = null;
	   
	   try {
		   
		   if(cardData.contains("=")){
				
				cardNum = cardData.substring(0,cardData.indexOf("="));
				
			}else{
				
				cardNum = cardData;
			}
			
			//cardNum = "1591234789";
			
			Redeemable received = CMSRedeemableHelper.findRedeemable(theAppMgr, cardNum);
			// GiftCard Balance inquiry : vishal yevale 13 oct 2016
			giftCardBalanceInquiryResponse=CMSCreditAuthHelper.getGCBalanceInquiryResponse(theAppMgr, cardData, false);
			String response=new AJBRequestResponseMessage(giftCardBalanceInquiryResponse.toString()).getValue(3);
			if(response!=null && response.equalsIgnoreCase("0")){
				if(giftCardBalanceInquiryResponse!=null){
					double balance=Double.valueOf(new AJBRequestResponseMessage(giftCardBalanceInquiryResponse.toString()).getValue(51));
					balance=balance/100;
					giftcardBalance=new ArmCurrency(balance);
				}
			}else if(response == null || !(response.equalsIgnoreCase("0"))){
				theAppMgr.showErrorDlg(applet.res.getString("GiftCard Failed to Retrieve Balance due to an Issue. Please use the card at a later time or try again."));
				dBill.setId(null);
				completeAttributes();
	            return;
			}
			if(received != null){
				if (received instanceof CMSStoreValueCard) {
			          if (!((CMSStoreValueCard)received).getStatus()) {
			            theAppMgr.showErrorDlg(applet.res.getString("This card is no more valid."));
			            completeAttributes();
			            return;
			          }
			          
			        //Mayuri edhara :: 13-MAR-17 - to validate card Balance.
			          if(giftcardBalance!=null){
				          dBill = (CMSStoreValueCard)received;
				          dBill.setId(cardNum);
			          // Mayuri Edhara ::  05-26-17 - needed to set to null to call for enter the amount edit area event.
				          dBill.setAmount(null);
				          
			        	  ((CMSStoreValueCard) dBill).setGiftcardBalance(giftcardBalance);
			          }
			          else if(giftcardBalance == null){
			        	  theAppMgr.showErrorDlg(applet.res.getString("Gift Card Balance Not available")); 
			        	  completeAttributes();
				          return;
			          }
			          
			    } else if (received instanceof DueBill) {
			          if ((received instanceof CMSDueBillIssue && !((CMSDueBillIssue)received).getStatus())
			                  || (received instanceof CMSDueBill && !((CMSDueBill)received).getStatus())) {
			                theAppMgr.showErrorDlg(applet.res.getString("This card is no more valid."));
			                completeAttributes();
			                return;
			          }

			        //Mayuri edhara :: 13-MAR-17 - to validate card Balance.
	            	  if(giftcardBalance!=null){
				          dBill = toCMSDueBill((DueBill)received);
			              dBill.setId(cardNum);

				         // Mayuri Edhara :: 05-26-17 - needed to set to null explicitly to call for enter the amount edit area event.
				          //as the toCMSDueBill sets it a issue amount.
				          dBill.setAmount(null);
				          
			        	  ((CMSDueBill) dBill).setGiftcardBalance(giftcardBalance);
			          }
	            	  else if(giftcardBalance == null){
			        	  theAppMgr.showErrorDlg(applet.res.getString("Gift Card Balance Not available")); 
			        	  completeAttributes();
				          return;
			          }
		              
			   }// end vishal 13 oct 2016
			}else if(received == null || !(received instanceof DueBill || received instanceof CMSStoreValueCard)) {
				theAppMgr.showErrorDlg(applet.res.getString("Cannot find the Gift Card or Credit Note"));
				completeAttributes();
		        return;
			}
	   }catch(Exception e){
		   
	   }
   }

  /**
   * Creates a new due bill to populate.
   * @param applet the applet calling the builder
   * @param initValue
   */
  public void build(String command, CMSApplet applet, Object initValue) {
	// Start Mayuri Edhara::
	  if(fipay_GiftCard_flag.equalsIgnoreCase("Y")){
		  if (theAppMgr.isOnLine()) {
		      dBill = new CMSRedeemable((String)initValue);
		      ((CMSRedeemable)dBill).setIsManual(false);
		    } else if(!theAppMgr.isOnLine()) {
		    	dBill = null;
		    	theAppMgr.showErrorDlg(applet.res.getString("GiftCards are not valid while in offline mode."));
		    	ArmCurrency amountDue = getAmountDue();
			       theAppMgr.setSingleEditArea(applet.res.getString("Press Enter for Gift Card Payment.")
			            , "AMOUNT", amountDue, theAppMgr.CURRENCY_MASK);
	            return;
		    }
		  this.applet = applet;
		  //vishal yevale 13 oct 2016
		  if(command.equalsIgnoreCase("MANUAL")){
			  isManualKeyEntry=true;
			  if (isOffline == false) {			  
				  if((dBill.getId() == null || dBill.getId().length() == 0) && isManualKeyEntry){
					  theAppMgr.setSingleEditArea(applet.res.getString("Please Enter Gift Card ID :")
					            , "GIFTCARD_ID" , theAppMgr.UPPER_CASE_MASK);
				  }
		//Mayuri Edhara : 10-MAR-17 - commented the below as we need to call the above edit area event to get the gift card number
				/*  if ((dBill.getAmount() == null || dBill.getAmount().doubleValue() == 0
				  			|| dBill.getId() == null || dBill.getId().length() == 0) && !isManualKeyEntry) {
						  ArmCurrency amountDue = getAmountDue();
					        theAppMgr.showErrorDlg("Please enter card number on pinpad.");
				        theAppMgr.setSingleEditArea(applet.res.getString("Enter amount.")
				            , "AMOUNT", amountDue, theAppMgr.CURRENCY_MASK);				
				     }	*/
			  }
		  }//end vishal yevale
		  else{  
		  completeAttributes();
		  }
		  theAppMgr.setEditAreaFocus();
		  System.out.println("Gift Card builder ...");
		  
	  }// end Mayuri Edhara::
	  else {
	  
	    if (!theAppMgr.isOnLine()) {
	      isOffline = true;
	      cmsRedeemable = new CMSRedeemable((String)initValue);
	      cmsRedeemable.setIsManual(false);
	      //         theAppMgr.showErrorDlg(applet.res.getString("Store credits are not valid while in offline mode."));
	      //         return;
	      dBill = null;
	    } else {
	      //dBill = new CMSDueBill( (String) initValue);
	      dBill = new CMSRedeemable((String)initValue);
	      ((CMSRedeemable)dBill).setIsManual(false);
	      cmsRedeemable = null;
	    }
	    this.applet = applet;
	    theAppMgr.setSingleEditArea(applet.res.getString(
	        "Swipe Gift Card/Credit Note or press 'Enter' for manual entry."), "DUE_BILL_ID");
	    // register for call backs
	    //completeAttributes();
	    theAppMgr.setEditAreaFocus();
	    System.out.println("credit card builder getting instance of CMSMSR...");
	    CMSMSR cmsMSR = CMSMSR.getInstance();
	    cmsMSR.registerCreditCardBuilder(this);
	    cmsMSR.activate();
	  }
  }

  /**
   * Returns true if all questions have answers.
   * @return true if all questions have answers
   */
  private boolean completeAttributes() {
	// Start Mayuri Edhara::
	  if(fipay_GiftCard_flag.equalsIgnoreCase("Y")){
		  if (isOffline == false) {
			   if(isManualKeyEntry && (dBill.getId() == null || dBill.getId().length() == 0)){
				   theAppMgr.setSingleEditArea(applet.res.getString("Press Enter Gift Card ID.")
				            , "GIFTCARD_ID", theAppMgr.UPPER_CASE_MASK);
				   return false;
			   } 			   
			   if(isManualKeyEntry && (dBill.getAmount() == null || dBill.getAmount().doubleValue() == 0)){
				   ArmCurrency amountDue = getAmountDue();
			       theAppMgr.setSingleEditArea(applet.res.getString("Press Enter Amount for Gift Card Payment.")
			            , "AMOUNT", amountDue, theAppMgr.CURRENCY_MASK);
				   return false;
			   }
			   if(!isManualKeyEntry && (dBill.getId() == null || dBill.getId().length() == 0 
					   || dBill.getAmount() == null || dBill.getAmount().doubleValue() == 0)) {
				    ArmCurrency amountDue = getAmountDue();
		            theAppMgr.setSingleEditArea(applet.res.getString("Press Enter for Gift Card Payment.")
		            	, "AMOUNT", amountDue, theAppMgr.CURRENCY_MASK);
		            return false;
			   }	
		   }
	  } // end Mayuri Edhara::
	  else {
	    ArmCurrency currBal = new ArmCurrency("0.0");
	    if (isOffline == false) {
	      if (dBill.getId() == null || dBill.getId().length() == 0) {
	        theAppMgr.setSingleEditArea(applet.res.getString("Enter Gift Card or Credit Note ID.")
	            , "ID", theAppMgr.UPPER_CASE_MASK);
	        return false;
	      }
	      if (!flag) {
	        try {
	          currBal = getCurrentBal(dBill.getRemainingBalance());
	          theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "DB_AMOUNT", currBal
	              , theAppMgr.CURRENCY_MASK);
	          flag = true;
	          return false;
	        } catch (CurrencyException ce) {
	          System.err.println("DueBillBldr.completeAttributes()->" + ce);
	          theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "DB_AMOUNT", currBal
	              , theAppMgr.CURRENCY_MASK);
	        }
	      } else {
	        try {
	          currBal = getCurrentBal(dBill.getRemainingBalance());
	          if (dBill.getAmount() == null) {
	            theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "DB_AMOUNT", currBal
	                , theAppMgr.CURRENCY_MASK);
	            return false;
	          } else if (dBill.getAmount().greaterThan(dBill.getRemainingBalance())) {
	            theAppMgr.showErrorDlg(applet.res.getString(
	                "The amount applied can not be greater than the remaining balance."));
	            theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "DB_AMOUNT", currBal
	                , theAppMgr.CURRENCY_MASK);
	            return false;
	          } else {
	            flag = false;
	            return true;
	          }
	        } catch (CurrencyException ce) {
	          System.err.println("DueBillBldr.completeAttributes()->" + ce);
	          theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "DB_AMOUNT", currBal
	              , theAppMgr.CURRENCY_MASK);
	        } //end catch
	      } //end else
	    } else {
	      //Check and populate redeemable
	      //CMSRedeemable attributes completed here
	      if ((cmsRedeemable.getId() == null || cmsRedeemable.getId().length() == 0)
	          && (cmsRedeemable.getControlNum() == null || cmsRedeemable.getControlNum().length() == 0)) {
	        theAppMgr.setSingleEditArea(applet.res.getString("Enter Gift Card or Credit Note ID.")
	            , "ID", theAppMgr.UPPER_CASE_MASK);
	        return false;
	      }
	      if ((cmsRedeemable.getAmount() == null || cmsRedeemable.getAmount().doubleValue() == 0)) {
	        theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "DB_AMOUNT"
	            , theAppMgr.CURRENCY_MASK);
	        return false;
	      }
	      if (manualAuthCode == null) {
	        theAppMgr.setSingleEditArea(applet.res.getString("Enter Manual Auth Code."), "AUTH_CODE");
	        return false;
	      }
	    }
	  }
    return true;
  }

  /**
   * put your documentation comment here
   * @param dueBill
   * @return
   * @exception Exception
   */
  private CMSDueBill toCMSDueBill(DueBill dueBill)
      throws Exception {
    try {
      if (dueBill == null)
        return null;
      if (dueBill instanceof CMSDueBill)
        return (CMSDueBill)dueBill;
      CMSDueBill cmsDueBill = new CMSDueBill(IPaymentConstants.CREDIT_MEMO);
      cmsDueBill.setAmount(dueBill.getIssueAmount().absoluteValue());
      cmsDueBill.setId(dueBill.getId());
      cmsDueBill.setType(dueBill.getType());
      cmsDueBill.setIssueAmount(dueBill.getIssueAmount().absoluteValue());
      cmsDueBill.setCreateDate(dueBill.getCreateDate());
      cmsDueBill.setAuditNote(dueBill.getAuditNote());
      cmsDueBill.setFirstName(dueBill.getFirstName());
      cmsDueBill.setLastName(dueBill.getLastName());
      cmsDueBill.setPhoneNumber(dueBill.getPhoneNumber());
      cmsDueBill.setRedemptionHistory(dueBill.getRedemptionHistory());
      return cmsDueBill;
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "toCMSDueBill", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }
  
  /**
   *  Mayuri Edhara :: added to get the card number from the Edit area Event.
   *  Enter the GIFT CARD ID : for Fipay Gift CARD INTEGRATION. Verify the Digit check and Bin range check.
   **/
 private boolean processGiftCardId(String giftCardNum){
	try{
		boolean success = RedeemableBldrUtil.validateGiftCard("CREDIT_MEMO", giftCardNum)
		          || RedeemableBldrUtil.validateGiftCard("STORE_VALUE_CARD", giftCardNum);
		  if(!success) {
		        theAppMgr.showErrorDlg(applet.res.getString("Invalid Card ID."));
		        completeAttributes();
		        return false;
	      } else {	    	  
	    	  setCardData(giftCardNum);
	      }
	}catch (Exception e) {
	      e.printStackTrace();
	      LoggingServices.getCurrent().logMsg(getClass().getName(), "EditAreaEvent"
	          , "Bin Range check Failed for the Gift Card.", "See Exception", LoggingServices.MAJOR, e);
	      // Send a null back (to get out of this class).
	      //VM: the processObject now points to STORE_VALUE_CREDIT_MEMO_PAYMENT for customer requirement
	      //          theBldrMgr.processObject(applet, "STORE_VALUE_CREDIT_MEMO_PAYMENT", null, this);
	      doRestart();
	      return false;
	    }
	return true;
 }
  /**
   * put your documentation comment here
   * @param card
   * @return
   */
  private boolean processID(String card) {
    try {
      //card = card.replace('.', '*');
      IsDigit digitCheck = new IsDigit();
      if (!digitCheck.isDigit(card)) {
        theAppMgr.showErrorDlg(applet.res.getString("Invalid Card ID."));
        doRestart();
        return false;
      }
      //ks: Check the entered Id
      //VM: added check for both Due Bill and gift card
      boolean success = RedeemableBldrUtil.validateEnteredCard("CREDIT_MEMO", card)
          || RedeemableBldrUtil.validateEnteredCard("STORE_VALUE_CARD", card);
      if (!success) {
        theAppMgr.showErrorDlg(applet.res.getString("Invalid Card ID."));
        doRestart();
        return false;
      }
      if (isOffline == false) {
        //CMSDueBill received = (CMSDueBill) CMSRedeemableHelper.findDueBill(
        //    theAppMgr,(String) theEvent);
        Redeemable received = CMSRedeemableHelper.findRedeemable(theAppMgr, card);
        if (received == null) {
          theAppMgr.showErrorDlg(applet.res.getString("Cannot find the Gift Card or Credit Note"));
          //VM: the processObject now points to STORE_VALUE_CREDIT_MEMO_PAYMENT for customer requirement
          //              theBldrMgr.processObject(applet, "STORE_VALUE_CREDIT_MEMO_PAYMENT", null, this);
          doRestart();
          return false;
        } else if (!(received instanceof DueBill || received instanceof CMSStoreValueCard)) {
          theAppMgr.showErrorDlg(applet.res.getString("This is not a Gift Card or Credit Note."));
          //theBldrMgr.processObject(applet, "PAYMENT", null, this);
          doRestart();
          return false;
        } else if (received.getRemainingBalance().lessThanOrEqualTo(new ArmCurrency(0.0))) {
          theAppMgr.showErrorDlg(applet.res.getString("Gift card / Store credit balance is") + " "
              + received.getRemainingBalance().formattedStringValue() + ".");
          //VM: the processObject now points to STORE_VALUE_CREDIT_MEMO_PAYMENT for customer requirement
          //              theBldrMgr.processObject(applet, "STORE_VALUE_CREDIT_MEMO_PAYMENT", null, this);
          doRestart();
          return false;
        } else if (received instanceof DueBill) {
          if ((received instanceof CMSDueBillIssue && !((CMSDueBillIssue)received).getStatus())
              || (received instanceof CMSDueBill && !((CMSDueBill)received).getStatus())) {
            theAppMgr.showErrorDlg(applet.res.getString("This card is no more valid."));
            doRestart();
            return false;
          }
          dBill = toCMSDueBill((DueBill)received);
          dBill.setId(card);
          return true;
        } else if (received instanceof CMSStoreValueCard) {
          if (!((CMSStoreValueCard)received).getStatus()) {
            theAppMgr.showErrorDlg(applet.res.getString("This card is no more valid."));
            doRestart();
            return false;
          }
          dBill = (CMSStoreValueCard)received;
          dBill.setId(card);
          return true;
        }
      } else {
        cmsRedeemable.setId(card);
        cmsRedeemable.doSetControlNum(card);
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
      LoggingServices.getCurrent().logMsg(getClass().getName(), "EditAreaEvent"
          , "Cannot read store credit file", "See Exception", LoggingServices.MAJOR, e);
      // Send a null back (to get out of this class).
      //VM: the processObject now points to STORE_VALUE_CREDIT_MEMO_PAYMENT for customer requirement
      //          theBldrMgr.processObject(applet, "STORE_VALUE_CREDIT_MEMO_PAYMENT", null, this);
      doRestart();
      return false;
    }
    return false;
  }

  /**
   * @param input
   */
  public void processSwipe(String input) {
    if ((input == null || input.trim().length() == 0)
        //|| processID(accountNum))
        || (getDueBillInfo(input) && processID(accountNum))) {
      if (input == null || input.trim().length() == 0) {
        if (isOffline) {
          cmsRedeemable.setIsManual(true);
        } else if (dBill instanceof CMSRedeemable) {
          ((CMSRedeemable)dBill).setIsManual(true);
        }
      } else {
        if (isOffline) {
          cmsRedeemable.setIsManual(false);
        } else if (dBill instanceof CMSRedeemable) {
          ((CMSRedeemable)dBill).setIsManual(false);
        }
      }
      completeAttributes();
      return;
    }
    //Mayuri Edhara 07-18-2016: commented the below and added return stmt so that it stays on the existing edit area.
    // the card is expired or unreadable or if entered the card number instead of swipe
   // theBldrMgr.processObject(applet, "PAYMENT", null, this);
    return;
  }

  /**
   * put your documentation comment here
   */
  private void doRestart() {
    String transactionPaymentName = null;
    boolean isManual = false;
    if (dBill != null) {
      transactionPaymentName = dBill.getTransactionPaymentName();
      if (dBill instanceof CMSRedeemable)
        isManual = ((CMSRedeemable)dBill).getIsManual();
    } else if (cmsRedeemable != null) {
      transactionPaymentName = cmsRedeemable.getTransactionPaymentName();
      isManual = cmsRedeemable.getIsManual();
    }
    if (!theAppMgr.isOnLine() && cmsRedeemable == null) {
      isOffline = true;
      cmsRedeemable = new CMSRedeemable((String)transactionPaymentName);
      cmsRedeemable.setIsManual(isManual);
      dBill = null;
    } else if (theAppMgr.isOnLine() && dBill == null) {
      isOffline = false;
      dBill = new CMSRedeemable((String)transactionPaymentName);
      ((CMSRedeemable)dBill).setIsManual(isManual);
      cmsRedeemable = null;
    }
    if (isOffline) {
      if (cmsRedeemable.getIsManual()) {
        theAppMgr.setSingleEditArea(applet.res.getString("Enter Gift Card or Credit Note ID.")
            , "ID", theAppMgr.UPPER_CASE_MASK);
      } else {
        theAppMgr.setSingleEditArea(applet.res.getString(
            "Swipe Gift Card/Credit Note or press 'Enter' for manual entry."), "DUE_BILL_ID");
      }
    } else if (dBill instanceof CMSRedeemable) {
      if (((CMSRedeemable)dBill).getIsManual()) {
        theAppMgr.setSingleEditArea(applet.res.getString("Enter Gift Card or Credit Note ID.")
            , "ID", theAppMgr.UPPER_CASE_MASK);
      } else {
        theAppMgr.setSingleEditArea(applet.res.getString(
            "Swipe Gift Card/Credit Note or press 'Enter' for manual entry."), "DUE_BILL_ID");
      }
      theAppMgr.setEditAreaFocus();
    }
  }

  /**
   * put your documentation comment here
   * @param inputStr
   * @return
   */
  public boolean getDueBillInfo(String inputStr) {
    if (this.cmsMSR instanceof NonJavaPOSMSR){
      if (!(((NonJavaPOSMSR)cmsMSR).extractDataToBuilder(inputStr))){
        return (false);
      }
     } 
  //Mayuri Edhara 07-18-2016: added the else loop to add the error dialog incase the cashier enter the card instead of swipe.
    else if(!(this.cmsMSR instanceof NonJavaPOSMSR) && ("US".equalsIgnoreCase(Version.CURRENT_REGION))){
    	theAppMgr.showErrorDlg(applet.res.getString("Please try again if you did not press the ENTER key before manually keying in the gift card number."));
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                theAppMgr.setSingleEditArea(applet.res.getString(
                    "Swipe Gift Card/Credit Note or press 'Enter' for manual entry."), "DUE_BILL_ID");
              }
            });
          }
        });
    	  return (false);
     }
    return (true);
  }

  /**
   * put your documentation comment here
   * @param cmsMSR
   */
  public void setCMSMSR(CMSMSR cmsMSR) {
    this.cmsMSR = cmsMSR;
  }

  /**
   * put your documentation comment here
   * @param accountNum
   */
  public void setAccountNum(String accountNum) {
    this.accountNum = new IsDigit().filterToGetDigits(accountNum);
  }

  /**
   * MP: return redeemable bal in case if transaction amt is more
   * else returns transaction amt
   * @param balAmount Currency
   * @return Currency
   */
  public ArmCurrency getCurrentBal(ArmCurrency balAmount) {
    PaymentTransaction txn = (PaymentTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS");
    // MP: Gets the composite total amt Due.
    CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)txn.getAppModel(
        CMSAppModelFactory.getInstance(), theAppMgr);
    ArmCurrency amt = new ArmCurrency("0.0");
    try {
    	theTxn = (CMSPaymentTransactionAppModel) ((PaymentTransaction) theAppMgr.getStateObject("TXN_POS")).getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
	      //Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
    	if ((theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
					&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
				amt = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue().subtract(appModel.getTotalPaymentAmount());
			}
			//Ends
			else		
             amt = appModel.getCompositeTotalAmountDue().subtract(txn.getTotalPaymentAmount());
      if (amt.lessThanOrEqualTo(balAmount)) {
        return amt;
      } else
        return balAmount;
    } catch (Exception e) {
      return balAmount;
    }
  }
  //Mayuri edhara :: added to get the amountdue from payment applet when the edit area is loaded.
  /**
  *
  * @param 
  * @return amount due from payment applet
  */
 public ArmCurrency getAmountDue() {
   PaymentTransaction txn = (PaymentTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS");
   CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)txn.getAppModel(
       CMSAppModelFactory.getInstance(), theAppMgr);
   ArmCurrency amt = new ArmCurrency("0.0");
   try {
   	theTxn = (CMSPaymentTransactionAppModel) ((PaymentTransaction) theAppMgr.getStateObject("TXN_POS")).getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
	      //Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
   	if ((theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
					&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
				amt = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue().subtract(appModel.getTotalPaymentAmount());
			}
			//Ends
			else		
            amt = appModel.getCompositeTotalAmountDue().subtract(txn.getTotalPaymentAmount());
   	
   	return amt;

   } catch (Exception e) {
     return amt;
   }
 }
//end mayuri edhara
  private String accountNum = "";
}

