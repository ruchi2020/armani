/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.builder;

import java.util.Enumeration;
import java.util.Hashtable;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.payment.RedeemableHist;
import com.chelseasystems.cr.payment.StoreValueCard;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cs.currency.CMSExchangeRateHelper;
import com.chelseasystems.cs.currency.CurrencyRate;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.item.CMSItemHelper;
import com.chelseasystems.cs.pos.CMSPaymentTransactionAppModel;
import com.chelseasystems.cs.pos.CMSSpecificItem;
import com.chelseasystems.cr.item.MiscItemTemplate;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.msr.CMSMSR;
import com.chelseasystems.cs.msr.NonJavaPOSMSR;
import com.chelseasystems.cs.util.IsDigit;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.pos.POSLineItemDetail;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.CMSAppModelFactory;
import com.chelseasystems.cs.swing.dlg.ForeignCurrencyExchangeRateListDlg;


/**
 * The purpose of a "Bldr" class is to build a complex object, and pass it back
 * to the GUI.
 * The GUI provides a store value card control number.  The store value
 * card builder takes control until it can return a store value card
 * with all properties set (from the server), or null if the store value
 * card does not exist.
 */
public class StoreValueCardBldr_EUR implements IObjectBuilder {

  /** The gift certificate to return */
  private CMSStoreValueCard storeValueCard = null;
  private IObjectBuilderManager theBldrMgr;
//Vishal Yevale : changes for issuer giftcard can use in same country
  private boolean invalidCard=false;
//end Vishal 8-July-2016
  /** The client-side applet */
  private CMSApplet applet;
  private IApplicationManager theAppMgr;
  private boolean isNewCard = false; // flag to specify if a new card is being created
  private CMSItem item; // holds the item created
  private CMSItemWrapper itemWrapper; //1864
  //ks: for offline validation of the tender
  private boolean isOffline = false;
  private String manualAuthCode = null;
  private static Hashtable htRedeemKeys = null;
  private CMSRedeemable cmsRedeemable = null;
  private CMSMSR cmsMSR = null;
  private boolean invokeBuilder = false;
  private CurrencyType foreignType;
  private Double conversionRateForStoreValueCard = new Double(0.0d);
  CurrencyRate curRate = null;
  ArmCurrency amt = null;
  ArmCurrency foreignAmt = null;
  CurrencyType curType = null;

  /** Default constructor */
  public StoreValueCardBldr_EUR() {
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
  }

  /**
   * Cleanup before exiting.
   */
  public void cleanup() {
	if(cmsMSR != null)
    cmsMSR.release();
    this.accountNum = "";
  }

  /**
   * Try to find the gift certificate on the server, using the record id or the
   * control number.
   * @param theCommand the description of what the user typed.
   * @param theEvent what the user typed (the gift certificate id or control
   *        number.
   */
  public void EditAreaEvent(String theCommand, Object theEvent) {
//	  if (theEvent == null || ((String)theEvent).length() == 0 ||((String)theEvent).length() < 13) {
//		    theAppMgr.showErrorDlg(applet.res.getString("Enter Gift Card Id"));
//		    theAppMgr.setSingleEditArea(applet.res.getString(
//	        "Swipe card or press 'Enter' for manual entry."), "STORE_VALUE_CARD_ID");
//	        return;
//	      }
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
    } else if (theCommand.equals("GC_AMOUNT")) {
    	
        if(isNewCard){
        	ArmCurrency giftCardAmount = RedeemableBldrUtil_EUR.getGiftCardAmount(storeValueCard.getControlNum());
        	ArmCurrency enteredAmount = (ArmCurrency) theEvent;        	
        	boolean validAmount = false;        	
        	try{
        		//validAmount = enteredAmount.equals(giftCardAmount);
        		validAmount = enteredAmount.lessThanOrEqualTo(giftCardAmount); // equals(giftCardAmount);
        	}catch(Exception e){}
        	
        	if(validAmount)
        	{
    	      if (isOffline) {
    	          cmsRedeemable.setAmount((ArmCurrency)theEvent);
    	        } else {
    	          storeValueCard.setAmount((ArmCurrency)theEvent);
    	        }
        	}
        	else{
                theAppMgr.showErrorDlg(applet.res.getString("Gift card amount doesn't match the barcode."));
                return;
        	}
        } 
        else
        {
  	      if (isOffline) {
	          cmsRedeemable.setAmount((ArmCurrency)theEvent);
	        } else {
	        	double enteredAmount = ((ArmCurrency)theEvent).doubleValue();
	        	//Double rate = new Double(0.0d);
	        	try {
	        		String localCurrCode = ArmCurrency.getBaseCurrencyType().getCode();
	        		String storeValueCardCurrCode = storeValueCard.getRemainingBalance().getCurrencyType().getCode();
					if(!localCurrCode.equals(storeValueCardCurrCode) ){
						ArmCurrency pmtAmt = new ArmCurrency(curType,enteredAmount);
						    try {
//							System.out.println("foeign amount   :"+foreignAmt.doubleValue());
//							System.out.println("conversionRateForStoreValueCard    :"+conversionRateForStoreValueCard);
							pmtAmt = pmtAmt.convertTo(ArmCurrency.getBaseCurrencyType(), conversionRateForStoreValueCard);
							storeValueCard.setAmount((ArmCurrency)pmtAmt);
							PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
							CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)theTxn.getAppModel(
							CMSAppModelFactory.getInstance(), theAppMgr);
							} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	     
					}else
						storeValueCard.setAmount((ArmCurrency)theEvent);
				} catch (CurrencyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }        	
        }
    }
    if (completeAttributes()) {
      // The store value card object is built, so return it (and control)
      // to the applet.
    	//Vishal Yevale : changes for issuer giftcard can use in same country
    	if(invalidCard){
    		isNewCard=true;
    	}
    	//end Vishal 8-July-2016
    	else if (!isNewCard && !isOffline) {
        theBldrMgr.processObject(applet, "PAYMENT", storeValueCard, this);
      } else if (!isNewCard && isOffline) {
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
            spcItem.setGiftCertificateId(storeValueCard.getId());
            spcItem.setUnitPrice(storeValueCard.getAmount());
          }
          //___Tim: 1864: Return item Wrapper instead of spcItem
          theBldrMgr.processObject(applet, "SPECIFIC", itemWrapper, this);
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
      
      theAppMgr.showErrorDlg(applet.res.getString("This function isn't available in offline mode."));
      return;      
      
//      if (initValue.equals("CREATE")) {
//        this.isNewCard = true;
//      }
//      cmsRedeemable = new CMSRedeemable((String)initValue);
    } else {
      if (initValue.equals("CREATE")) {
        this.isNewCard = true;
        storeValueCard = new CMSStoreValueCard("STORE_VALUE_CARD");
      } else {
        storeValueCard = new CMSStoreValueCard((String)initValue);
      }
    }
    this.applet = applet;
    theAppMgr.setSingleEditArea(applet.res.getString(
        "Swipe card or press 'Enter' for manual entry."), "STORE_VALUE_CARD_ID");
    // register for call backs
    //completeAttributes();
    theAppMgr.setEditAreaFocus();
    System.out.println("credit card builder getting instance of CMSMSR...");
    
    CMSMSR cmsMSR = null;
    if(cmsMSR != null){
    	cmsMSR = CMSMSR.getInstance();	
    	cmsMSR.registerCreditCardBuilder(this);
    	cmsMSR.activate();
    }
    
    
  }

  /**
   * If the user has not provided either a transaction number or a control
   * number (for the find to use as a key), prompt them to do so.
   */
  private boolean completeAttributes() {
    //Create new Gift card object
    if (this.isNewCard && item == null) {
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
        if (invokeBuilder) {
          theAppMgr.setSingleEditArea(applet.res.getString(
              "Swipe Gift Card or press 'Enter' for manual entry."), "STORE_VALUE_CARD_ID");
          invokeBuilder = false;
          return false;
        }
    	//Vishal Yevale : changes for issuer giftcard can use in same country
        if(invalidCard){
            theBldrMgr.processObject(applet, "PAYMENT", null, this);
        	return true;
        }
      //end Vishal 8-July-2016
        if ((storeValueCard.getId() == null || storeValueCard.getId().length() == 0)
            && (storeValueCard.getControlNum() == null
            || storeValueCard.getControlNum().length() == 0)) {
          theAppMgr.setSingleEditArea(applet.res.getString("Enter Gift Card Id."), "ID"
              , theAppMgr.UPPER_CASE_MASK);
           return false;
        }
        //CMSStoreValueCard attributes completed here
//        if ((storeValueCard.getAmount() == null || storeValueCard.getAmount().doubleValue() == 0)) {
//          theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "GC_AMOUNT"
//              , storeValueCard.getRemainingBalance(), theAppMgr.CURRENCY_MASK);
//          return false;
//        }          
        if ((storeValueCard.getAmount() == null || storeValueCard.getAmount().doubleValue() == 0)) {
        	 PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
           	 String localCurrencyCode = ArmCurrency.getBaseCurrencyType().getCode();
           	 String storeValueCardCurrencyCode = storeValueCard.getRemainingBalance().getCurrencyType().getCode();
            if(!localCurrencyCode.equals(storeValueCardCurrencyCode)){
            CMSStore store = (CMSStore)theAppMgr.getGlobalObject("STORE");
            CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)theTxn.getAppModel(
                CMSAppModelFactory.getInstance(), theAppMgr);
             CurrencyRate currencyRates[]= null;
			try {
				currencyRates = CMSExchangeRateHelper.findAllCurrencyRates(theAppMgr
					      , store.getPreferredISOCountry(), store.getPreferredISOLanguage());
				amt = appModel.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
			} catch (Exception e) {
//				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int i = 0 ;i<currencyRates.length;i++){
				curType = CurrencyType.getCurrencyType(currencyRates[i].getFromCurrency());
				 if(storeValueCard.getRemainingBalance().getCurrencyType().getCode().equals(curType.getCode())){
					 foreignAmt = amt.convertTo(curType, currencyRates[i].getConversionRate());
					 conversionRateForStoreValueCard = currencyRates[i].getConversionRate();
					break;
			}
			}
			theAppMgr.setSingleEditArea(applet.res.getString("Amount as foreign currency.")
		            , "GC_AMOUNT", foreignAmt.absoluteValue(), theAppMgr.CURRENCY_MASK);
           
             theAppMgr.setEditAreaFocus();
             return false;
            }
        	//}
            theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "GC_AMOUNT"
                    , theAppMgr.CURRENCY_MASK);
            return false;
			}
              
        if (!isNewCard
            && (storeValueCard.getAmount().doubleValue()
            > storeValueCard.getRemainingBalance().doubleValue())) {
          theAppMgr.showErrorDlg(applet.res.getString(
          "The amount applied can not be greater than the remaining balance."));
      theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "GC_AMOUNT"
          ,  theAppMgr.CURRENCY_MASK);
          return false;
        }
      } 
        //else {
//        //CMSRedeemable attributes completed here
//        if ((cmsRedeemable.getId() == null || cmsRedeemable.getId().length() == 0)
//            && (cmsRedeemable.getControlNum() == null
//            || cmsRedeemable.getControlNum().length() == 0)) {
//          theAppMgr.setSingleEditArea(applet.res.getString("Enter Gift Card Id."), "ID"
//              , theAppMgr.UPPER_CASE_MASK);
//          return false;
//        }
//        if ((cmsRedeemable.getAmount() == null || cmsRedeemable.getAmount().doubleValue() == 0)) {
//          theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "GC_AMOUNT"
//              , cmsRedeemable.getRemainingBalance(), theAppMgr.CURRENCY_MASK);
//          return false;
//        }
//        if (manualAuthCode == null) {
//          theAppMgr.setSingleEditArea(applet.res.getString("Enter Manual Auth Code."), "AUTH_CODE");
//          return false;
//        }
//      }
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
    //System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~ In setAccountNum - theEvent="+theEvent);
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
      boolean success = RedeemableBldrUtil_EUR.validateEnteredCard(card);
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
          //Vishal Yevale : changes for issuer giftcard can use in same country
          CMSStoreValueCard cmsstorevaluecard=(CMSStoreValueCard)received;
          String issueStoreId=null;
          if(received !=null){
           issueStoreId=cmsstorevaluecard.getStoreId();
          }
          String currentStoreId=theTxn.getStore().getCompanyId();
          //end Vishal 8-July-2016
          //System.out.println("received " + received + " isNewCard " + isNewCard);
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
          //Vishal Yevale : changes for issuer giftcard can use in same country
          }else if(issueStoreId!=null && !issueStoreId.substring(0, 3).equalsIgnoreCase(currentStoreId.substring(0, 3))){
        	  theAppMgr.addStateObject("THE_EVENT", "SUCCESS");
        	  invalidCard=true;
        	  theAppMgr.showErrorDlg(applet.res.getString("Not possible to spend a Gift Card issued in another Country"));
                  // Send a null back (to get out of this class).
                  return false;
          }
        //end Vishal 8-July-2016
          else if (received != null) {
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
            //CurrencyType currType = storeValueCard.getRemainingBalance().getCurrencyType();
            //System.out.println("currType   :"+currType.getCode());
            if (!isNewCard ){
            	// check for remaing balance
//            	Enumeration theHistory = receivedCard.getRedemptionHistory().elements();
//            	RedeemableHist histItem = null;
//            	ArmCurrency subtractAmt = null;
//            	do
//                {
//                    if(!theHistory.hasMoreElements())
//                        break;
//                    histItem = (RedeemableHist)theHistory.nextElement();
//                    for(subtractAmt = histItem.getAmountUsed(); ;)
//                    {
//                    	System.out.println ("Transaction id  :"+histItem.getTransactionIdUsed()+"Historical tx = " + histItem.getDateUsed() + " amt used : " + histItem.getAmountUsed());
//                        subtractAmt = subtractAmt.getConvertedFrom();
//                        if(subtractAmt == null)
//                            throw new MissingExchangeRateException("redeemable history missing conversion history");
//                    }
//
//                    //remainingBalance = remainingBalance.subtract(subtractAmt);
//                } while(true);
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
    theAppMgr.setSingleEditArea(applet.res.getString(
        "Swipe Gift Card or press 'Enter' for manual entry."), "STORE_VALUE_CARD_ID");
    theAppMgr.setEditAreaFocus();
    if(input.trim().equals("")){
    	theAppMgr.setSingleEditArea(applet.res.getString("Enter Gift Card number."), "STORE_VALUE_CARD_ID");
      return;
    }
    invokeBuilder = false;
  }

  /**
   * put your documentation comment here
   * @param inputStr
   * @return
   */
  public boolean getStoreValueCardInfo(String inputStr) {
    //System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~ In getStoreValueCardInfo");
    if (this.cmsMSR instanceof NonJavaPOSMSR) {
      //System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~ In getStoreValueCardInfo - <cmsMSR instanceof NonJavaPOSMSR>");
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

