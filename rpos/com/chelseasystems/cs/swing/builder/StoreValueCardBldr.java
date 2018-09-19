/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.builder;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.user.User;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.payment.StoreValueCard;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cs.authorization.bankcard.CMSCreditAuthHelper;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.item.CMSItemHelper;
import com.chelseasystems.cs.pos.CMSSpecificItem;
import com.chelseasystems.cr.item.MiscItemTemplate;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.msr.CMSMSR;
import com.chelseasystems.cs.msr.NonJavaPOSMSR;
import com.chelseasystems.cs.util.IsDigit;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.pos.POSLineItemDetail;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.menu.MenuConst;


/**
 * The purpose of a "Bldr" class is to build a complex object, and pass it back
 * to the GUI.
 * The GUI provides a store value card control number.  The store value
 * card builder takes control until it can return a store value card
 * with all properties set (from the server), or null if the store value
 * card does not exist.
 */
public class StoreValueCardBldr implements IObjectBuilder {

  /** The gift certificate to return */
  private CMSStoreValueCard storeValueCard = null;
  private IObjectBuilderManager theBldrMgr;

  /** The client-side applet */
  private CMSApplet applet;
  private IApplicationManager theAppMgr;
  private boolean isNewCard = false; // flag to specify if a new card is being created
  private CMSItem item; // holds the item created
  private CMSItemWrapper itemWrapper; //1864
  //ks: for offline validation of the tender
  private boolean isOffline = false;
  private String manualAuthCode = null;
  private CMSRedeemable cmsRedeemable = null;
  private CMSMSR cmsMSR = null;
  private boolean invokeBuilder = false;
  //Start: Added by Himani for GC fipay integration
  private static ConfigMgr config;
  private String fipay_gc_flag;
  private Double gcAmt=0.0; // Reload GC
  private boolean isReload = false;
  //End: Added by Himani for GC fipay integration

  /** Default constructor */
  public StoreValueCardBldr() {
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
  //Start: Added by Himani for GC fipay integration
    String fileName = "store_custom.cfg";
	config = new ConfigMgr(fileName);
	fipay_gc_flag = config.getString("FIPAY_GIFTCARD_INTEGRATION");
	if(fipay_gc_flag == null){
		fipay_gc_flag = "N";
	}
	//End: Added by Himani for GC fipay integration
  }

  /**
   * Cleanup before exiting.
   */
  public void cleanup() {
	  if(fipay_gc_flag==null || fipay_gc_flag.toUpperCase().equals("N")) //Added by Himani for GC fipay Integration
	  {
		  if(cmsMSR != null)
			 cmsMSR.release();
		  this.accountNum = "";
	  }
  }

  /**
   * Try to find the gift certificate on the server, using the record id or the
   * control number.
   * @param theCommand the description of what the user typed.
   * @param theEvent what the user typed (the gift certificate id or control
   *        number.
   */
  public void EditAreaEvent(String theCommand, Object theEvent) {
	if(fipay_gc_flag==null || fipay_gc_flag.toUpperCase().equals("N")) //Added by Himani for GC fipay Integration
	{ 
		if (theCommand.equals("STORE_VALUE_CARD_ID")) {
			processSwipe((String)theEvent);
			return;
		}
		if (theCommand.equals("MANUAL")) {
			if (isOffline) {
				cmsRedeemable.setIsManual(true);
			} else {
				storeValueCard.setIsManual(true);
			}
			theAppMgr.setSingleEditArea(applet.res.getString("Enter Gift Card Id."), "ID"
					, theAppMgr.UPPER_CASE_MASK);
			return;
		}
	 
		if (theCommand.equals("ID")) {
			processID((String)theEvent);
		}
    
		if (theCommand.equals("GC_AMOUNT")) {
			if (isOffline) {
				cmsRedeemable.setAmount((ArmCurrency)theEvent);
			} else {
				storeValueCard.setAmount((ArmCurrency)theEvent);
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
	}
	//Start: Added by Himani for gc fipay integration
	else
	{
		if (theCommand.equals("GC_AMOUNT")) {
			if (theEvent == null) {
    	        theAppMgr.showErrorDlg(applet.res.getString("Amount cannot be blank."));
    	        return;
    	      } else 
    	      {
    	    	  boolean amt=validateAmount((ArmCurrency)theEvent);
    	    	  if(amt==false)
    	    	  {
    	    		  theAppMgr.showErrorDlg(applet.res.getString("Amount cannot be greater than $"+ (2500-gcAmt)));  
    	    		  return;
    	    	  }
    	      }
			if (isOffline) {
				cmsRedeemable.setAmount((ArmCurrency)theEvent);
			} else {
				storeValueCard.setAmount((ArmCurrency)theEvent);
				storeValueCard.setGiftcardBalance(new ArmCurrency((((ArmCurrency)theEvent).doubleValue()+gcAmt)));
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
	}
	//End: Added by Himani for gc fipay integration
		if (completeAttributes()) {
			// The store value card object is built, so return it (and control)
			// to the applet.
			if (!isNewCard && !isReload && !isOffline) { // Modified by Himani for Reload GC on 20-SEP-2016
				System.out.println("Inside  EditAreaEvent   :"+storeValueCard);
				theBldrMgr.processObject(applet, "PAYMENT", storeValueCard, this);
			} else if (!isNewCard && !isReload && isOffline) { // Modified by Himani for Reload GC on 20-SEP-2016
				theBldrMgr.processObject(applet, "PAYMENT", cmsRedeemable, this);
			} else {
				try {
					CMSSpecificItem spcItem = new CMSSpecificItem(item);
					//___Tim: 1864
					itemWrapper = new CMSItemWrapper();
					itemWrapper.setSpecificItem(spcItem);
					if (isOffline) {
						spcItem.setGiftCertificateId(cmsRedeemable.getId());
						spcItem.setUnitPrice(cmsRedeemable.getAmount());
						spcItem.setManualAuthCode(cmsRedeemable.getManualAuthCode());
					} else {
						System.out.println("Inside EditAreaEvent2   :"+storeValueCard.getId());
						// Modified by Himani for Reload GC on 20-SEP-2016
						if(isReload){
							spcItem.setGiftCertificateId(storeValueCard.getControlNum());
							spcItem.setGiftCardBalance(storeValueCard.getGiftcardBalance());  // added by vishal yevale to print reloaded gc balance
						}
						else{
							System.out.println("Printing store value card id  :"+storeValueCard.getId());
							spcItem.setGiftCertificateId(storeValueCard.getId());
							
						}
						spcItem.setUnitPrice(storeValueCard.getAmount());
							
					}
					//___Tim: 1864: Return item Wrapper instead of spcItem
					theBldrMgr.processObject(applet, "SPECIFIC", itemWrapper, this);
					//Himani: Added below code to land back to sale screen and menu once the gc item is added
					User theOpr;
					theOpr = (CMSEmployee) theAppMgr.getStateObject("OPERATOR");
					theAppMgr.showMenu(MenuConst.ADD_ITEM_MENU, theOpr);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			storeValueCard = null;
		}
	
  }

  /**
   * Create a new StoreValueCard object, and call the completeAttributes method to
   * get the search criteria.
   * @param applet the applet calling the builder
   * @param initValue (?)
   */
  public void build(String Command, CMSApplet applet, Object initValue) {
    if (!theAppMgr.isOnLine()) {
    	isOffline = true;   
      if (initValue.equals("CREATE")) {
        this.isNewCard = true;
      }
      //Start: Added by Himani for Reload GC on 19-SEP-2016
      else if(initValue.equals("RELOAD"))
      {
    	  this.isReload = true;
      }
      //End: Added by Himani for Reload GC on 19-SEP-2016
      cmsRedeemable = new CMSRedeemable((String)initValue);
      System.out.println("inside build   :"+cmsRedeemable);
    } else {
      if (initValue.equals("CREATE")) {
        this.isNewCard = true;
        storeValueCard = new CMSStoreValueCard("STORE_VALUE_CARD");
      } 
      else if (initValue.equals("RELOAD")){ // Modified by Himani for Reload GC on 20-SEP-2016
    	  this.isReload = true;
          storeValueCard = new CMSStoreValueCard("STORE_VALUE_CARD");
      }
      else {
        storeValueCard = new CMSStoreValueCard((String)initValue);
      }
    }
    this.applet = applet;
    // Added by Himani for GC fipay integration
    if(fipay_gc_flag!=null && fipay_gc_flag.toUpperCase().equals("Y"))
    {
    	//Start: Added by Himani for Reload GC on 19-SEP-2016
    	if(initValue.equals("RELOAD"))
    	{
    		Object gcDetails = CMSCreditAuthHelper.getGCTrackData(theAppMgr);
    		if(gcDetails!=null){
    		String gcDetail[] = gcDetails.toString().split(",");
    		String gcTrackData = gcDetail[0];
    		String gcBalance = gcDetail[1];
    		if(!gcBalance.equals(null))
    		{
    			gcAmt= Double.parseDouble(gcBalance);
    			if(gcAmt < 2500)
    			{
    				theAppMgr.showErrorDlg("Available Balance is $"+ gcAmt +".                             You can reload Giftcard upto $" + (2500 - gcAmt));
    				
    				theAppMgr.setSingleEditArea(applet.res.getString("Enter Amount for Reloading Giftcard."), "GC_AMOUNT"
    	                    , theAppMgr.CURRENCY_MASK);
    				this.isReload = true;
    				storeValueCard.doSetControlNum(gcTrackData);
    			}
    			else
    			{
    				theAppMgr.showErrorDlg("GiftCard Balance is Full, Cannot Reload further");
    			}
    			
    		}
    		}
    		else
    		{
    			theAppMgr.showErrorDlg("GiftCard cannot be reloaded. Please try later.");
    		}
    	} //End: Added by Himani for Reload GC on 19-SEP-2016
    	else
    	{
    		theAppMgr.setSingleEditArea(applet.res.getString("Enter Amount for New Giftcard."), "GC_AMOUNT"
                , theAppMgr.CURRENCY_MASK); 
    	}
    }
    else{
    theAppMgr.setSingleEditArea(applet.res.getString(
        "Swipe card or press 'Enter' for manual entry."), "STORE_VALUE_CARD_ID");
  //fix for US Issue 7370 (All the gift cards stopped working)
    // if(cmsMSR != null){
    	cmsMSR = CMSMSR.getInstance();	
    	cmsMSR.registerCreditCardBuilder(this);
    	cmsMSR.activate();
    //}
    }
   
      
    // register for call backs
    //completeAttributes();
    theAppMgr.setEditAreaFocus();
    
    
    
  }
  //Start: Added by Himani for GC amount validation
  private boolean validateAmount(ArmCurrency amount)
  {
	  if(((amount.doubleValue()+gcAmt)) <= 2500) // Added by Himani for Reload GC on 19-SEP-2016
		  return true;
	  else
		  return false;
  }
  //End: Added by Himani for GC amount validation
  /**
   * If the user has not provided either a transaction number or a control
   * number (for the find to use as a key), prompt them to do so.
   */
  private boolean completeAttributes() {
	  //Create new Gift card object
	  //System.out.println("Inside CompleteAttributes 1  :"+item.getBarCode());
	  if ((this.isNewCard || this.isReload) && item == null) {
      MiscItemTemplate miscItemTemplate = (MiscItemTemplate)theAppMgr.getStateObject(
          "ARM_STORE_VALUE_ID");
      try {
        String itemId = miscItemTemplate.getBaseItemId();
        theAppMgr.removeStateObject("ARM_STORE_VALUE_ID");
        String storeId = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getId();
        item = CMSItemHelper.findByBarCode(theAppMgr, itemId, storeId);
        if (item == null) {
          theAppMgr.showErrorDlg(applet.res.getString("Cannot find item."));
          theBldrMgr.processObject(applet, "SPECIFIC", null, this);
          return true;
        } else {
          item.setDescription(miscItemTemplate.getMiscItemDescription());
          if (!miscItemTemplate.getTaxable())
            item.setNotTaxable();
        }
        ConfigMgr config = new ConfigMgr("item.cfg");
        item.setRedeemableType(config.getString(miscItemTemplate.getKey().trim() + ".TYPE"));
      } catch (Exception ex) {
        theAppMgr.showExceptionDlg(ex);
      }
    }
    try {
      if (isOffline == false) {
    	if(fipay_gc_flag==null || fipay_gc_flag.toUpperCase().equals("N")){ // Added by Himani for gc fipay integration
        if (invokeBuilder) {
        	 
          theAppMgr.setSingleEditArea(applet.res.getString(
              "Swipe Gift Card or press 'Enter' for manual entry."), "STORE_VALUE_CARD_ID");
          
        	
        	invokeBuilder = false;
        	 
          return false;
        }
        if ((storeValueCard.getId() == null || storeValueCard.getId().length() == 0)
            && (storeValueCard.getControlNum() == null
            || storeValueCard.getControlNum().length() == 0)) {
          theAppMgr.setSingleEditArea(applet.res.getString("Enter Gift Card Id."), "ID"
              , theAppMgr.UPPER_CASE_MASK);
          return false;
        }
    	}
        //CMSStoreValueCard attributes completed here
        if ((storeValueCard.getAmount() == null || storeValueCard.getAmount().doubleValue() == 0)) {
          theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "GC_AMOUNT"
              , storeValueCard.getRemainingBalance(), theAppMgr.CURRENCY_MASK);
          return false;
        }
        if (!isNewCard && !isReload
            && (storeValueCard.getAmount().doubleValue()
            > storeValueCard.getRemainingBalance().doubleValue())) {
          theAppMgr.showErrorDlg(applet.res.getString(
              "The amount applied can not be greater than the remaining balance."));
          theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "GC_AMOUNT"
              , storeValueCard.getRemainingBalance(), theAppMgr.CURRENCY_MASK);
          return false;
        }
      } else {
        //CMSRedeemable attributes completed here
    	if(fipay_gc_flag==null || fipay_gc_flag.toUpperCase().equals("N")){ // Added by Himani for gc fipay integration
        if ((cmsRedeemable.getId() == null || cmsRedeemable.getId().length() == 0)
            && (cmsRedeemable.getControlNum() == null
            || cmsRedeemable.getControlNum().length() == 0)) {
          theAppMgr.setSingleEditArea(applet.res.getString("Enter Gift Card Id."), "ID"
              , theAppMgr.UPPER_CASE_MASK);
          return false;
        }
    	}
        if ((cmsRedeemable.getAmount() == null || cmsRedeemable.getAmount().doubleValue() == 0)) {
          theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "GC_AMOUNT"
              , cmsRedeemable.getRemainingBalance(), theAppMgr.CURRENCY_MASK);
          return false;
        }
        if (manualAuthCode == null) {
          theAppMgr.setSingleEditArea(applet.res.getString("Enter Manual Auth Code."), "AUTH_CODE");
          return false;
        }
      }
    } catch (CurrencyException ce) {
      System.err.println("StoreValueCardBldr.completeAttributes()->" + ce);
      theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "GC_AMOUNT"
          , theAppMgr.CURRENCY_MASK);
    }
    return true;
  }

  /**
   * put your documentation comment here
   * @param theEvent
   * @return
   */
  private boolean processID(String theEvent) {
     String card = (String)theEvent;
    //card = card.replace('.', '*');
    IsDigit digitCheck = new IsDigit();
     if (!digitCheck.isDigit(card)) {
      theAppMgr.showErrorDlg(applet.res.getString("Invalid Card ID."));
      invokeBuilder = true;
      storeValueCard.setIsManual(false);
      return false;
    }
    //validate the entered account number
    try {
      boolean success = RedeemableBldrUtil.validateEnteredCard("STORE_VALUE_CARD",card);
      //prompt for amount
      if (!success) {
        theAppMgr.showErrorDlg(applet.res.getString("Invalid Card ID."));
        invokeBuilder = true;
        storeValueCard.setIsManual(false);
        return false;
      }
      //         else {
      //           if (isOffline)
      //             cmsRedeemable.setId(card);
      //         }
      //VM: Check to see if the card is already being used in this Txn
      CompositePOSTransaction theTxn = (CompositePOSTransaction)theAppMgr.getStateObject("TXN_POS");
      if (theTxn != null) {
        POSLineItem[] lines = ((CompositePOSTransaction)theTxn).getSaleLineItemsArray();
        for (int i = 0; i < lines.length; i++) {
          POSLineItemDetail[] details = lines[i].getLineItemDetailsArray();
          for (int j = 0; j < details.length; j++) {
            String id = details[j].getGiftCertificateId();
            if (id != null && id.length() > 0 && id.trim().equals(card.trim())) {
              theAppMgr.showErrorDlg(applet.res.getString(
                  "This gift card has already been added to the transaction."));
              return false;
            }
          }
        }
      }
      if (!isOffline) {
        //build the store value card
        //Base Code starts from here
        if (isOffline == false) {
          Redeemable received = CMSRedeemableHelper.findRedeemable(theAppMgr, card);
           //ks: throw error if used for payment
          if (received == null) {
            theAppMgr.addStateObject("THE_EVENT", "FAILURE");
            if (!isNewCard) {
              //Incorporated by Anand to accomodate CustlistApplet related changes
              theAppMgr.showErrorDlg(applet.res.getString("Cannot find store value card"
                  + "id or control number.  Call help desk, or select another payment type."
                  + "  ID entered:") + " " + theEvent);
              // Send a null back (to get out of this class).
              theBldrMgr.processObject(applet, "PAYMENT", null, this);
              return false;
            } else {
              //storeValueCard = new CMSStoreValueCard("STORE_VALUE_CARD");
              storeValueCard.setId((String)theEvent);
              storeValueCard.doSetControlNum((String)theEvent);
              return true;
            }
          } else if (received != null) {
            theAppMgr.addStateObject("THE_EVENT", "SUCCESS");
            StoreValueCard receivedCard = null;
            if (received instanceof StoreValueCard)
              receivedCard = (StoreValueCard)received;
            else {
              theAppMgr.showErrorDlg(applet.res.getString(
                  "This is an already issued Credit Note. Please select another card."));
              // Send a null back (to get out of this class).
              //theBldrMgr.processObject(applet, "PAYMENT", null, this);
              return false;
            }
            if (!isNewCard) {
              // check for remaing balance
              if (receivedCard.getRemainingBalance().equals(new ArmCurrency(0.0))) {
                theAppMgr.showErrorDlg(applet.res.getString("Store Value Card has a zero balance."));
                // Send a null back (to get out of this class).
                theBldrMgr.processObject(applet, "PAYMENT", null, this);
                return false;
              } else {
                storeValueCard = (CMSStoreValueCard)receivedCard;
              }
            } else if (isNewCard) {
              theAppMgr.showErrorDlg(applet.res.getString("Gift Card already issued."));
              invokeBuilder = true;
              storeValueCard.setIsManual(false);
              return false;
            }
            //                 else {
            //                   storeValueCard = (CMSStoreValueCard) receivedCard;
            //                 }
          } // end received != null
        } // end !isNewCard
      } else {
        cmsRedeemable.setId((String)theEvent);
        cmsRedeemable.doSetControlNum((String)theEvent);
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
      LoggingServices.getCurrent().logMsg(getClass().getName(), "EditAreaEvent"
          , "Cannot read store value card file.", "See Exception", LoggingServices.MAJOR, e);
      // Send a null back (to get out of this class).
      theBldrMgr.processObject(applet, "PAYMENT", null, this);
    }
    return false;
  }

  /**
   * @param input
   */
  public void processSwipe(String input) {
    //System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~ In processSwipe - input="+input);
    if ((input == null || input.trim().length() == 0)
        //|| processID(accountNum))
        || (getStoreValueCardInfo(input) && processID(accountNum))) {
      //System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~ In processSwipe - accountNum="+accountNum);
      completeAttributes();
      return;
    }
    //       if (getStoreValueCardInfo(input))
    //       {
    //         try {
    //
    //             if (RedeemableBldrUtil.validateEnteredCard("STORE_VALUE_CARD", input)) {
    //
    //                 if (isOffline){
    //                     cmsRedeemable.setId(input);
    //                     cmsRedeemable.doSetControlNum(input);
    //                 }else {
    //                     storeValueCard.setId(input);
    //                     storeValueCard.doSetControlNum(input);
    //                 }
    //               completeAttributes();
    //               return;
    //             }
    //             else {
    //                 if (isOffline){
    //                   cmsRedeemable.setIsManual(true);
    //                 }else {
    //                   storeValueCard.setIsManual(true);
    //                 }
    //               theAppMgr.showErrorDlg(applet.res.getString(
    //                   "Invalid Card ID."));
    //             }
    //         }
    //         catch (Exception e) {
    //           e.printStackTrace();
    //         }
    //
    //       }
    // the card is expired or unreadable
    // Issue # 147
    //theBldrMgr.processObject(applet, "SPECIFIC", null, this);
    //Commented by Himani for removing swipe option for GC
    /*theAppMgr.setSingleEditArea(applet.res.getString(
        "Swipe Gift Card or press 'Enter' for manual entry."), "STORE_VALUE_CARD_ID");*/
    
    invokeBuilder = false;
  }

  /**
   * put your documentation comment here
   * @param inputStr
   * @return
   */
  public boolean getStoreValueCardInfo(String inputStr) {
    //System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~ In getStoreValueCardInfo  "+inputStr);
    //System.out.println("this.cmsMSR instanceof NonJavaPOSMSR   :"+(this.cmsMSR instanceof NonJavaPOSMSR));
    if (this.cmsMSR instanceof NonJavaPOSMSR) {
      //System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~ In getStoreValueCardInfo - <cmsMSR instanceof NonJavaPOSMSR>"+inputStr);
      if (!(((NonJavaPOSMSR)cmsMSR).extractDataToBuilder(inputStr))) {
        theAppMgr.showErrorDlg(applet.res.getString("Error reading data from MSR."));
        return (false);
      }
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
    //System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~ In setAccountNum - raw accountNum="+accountNum);
    this.accountNum = new IsDigit().filterToGetDigits(accountNum);
    //System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~ In setAccountNum - accountNum="+accountNum);
  }

  private String accountNum = "";
}

