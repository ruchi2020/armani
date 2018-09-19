/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.builder;

import java.util.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.swing.*;
//import com.chelseasystems.cs.msr.*;
import com.chelseasystems.cs.payment.*;
//import com.chelseasystems.cs.util.ArmHexConvert;
import com.chelseasystems.cs.util.CreditAuthUtil;
//import com.chelseasystems.cs.util.EncryptionUtils;
//import com.chelseasystems.cs.util.ISDEncryption;
//import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.register.CMSRegister;
//import com.chelseasystems.cs.authorization.ISDValidation;
import com.chelseasystems.cs.customer.CustomerCreditCard;
//import com.isd.jec.IsdEncryptionClient;
//import com.isd.jec.crypto.CryptoException;

import java.io.IOException;
import java.text.SimpleDateFormat;


/**
 */
public class CustomerCreditCardBldr implements IObjectBuilder {
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;
  private String accountNum = null;
  private String cardHolderName = null;
  private String expDate = null;
  private String cardType = null;
  private String serviceCode = null;
  private String rawData = null;
  private String trackNumber = null;
  private String cid = null;
  private Payment thePayment;
  private CustomerCreditCard creditCard;
  private boolean manual = false;
  private boolean digitVerify = false;
 // private CMSMSR cmsMSR = null;
  private String zipCode = null;
  private String mExpDate = null;
  private boolean isModified = false;
  private String mZipCode = null;
  boolean zipTraversed = false;

  /**
   */
  public CustomerCreditCardBldr() {
  }

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
    //cmsMSR.release();
  }

  /**
   * @param theCommand
   * @param theEvent
   */
  public void EditAreaEvent(String theCommand, Object theEvent) {
    if (theCommand.equals("CREDIT_CARD")) {
      processSwipe((String)theEvent);
      return;
    }
    /*        if (theCommand.equals("M_CREDIT_CARD")) {
     System.out.println("CustomerCreditCardBldr::EditAreaEvent:--- Before call  processModSwipe");
     processModSwipe((String) theEvent);
     System.out.println("CustomerCreditCardBldr::EditAreaEvent:--- After call  processModSwipe");
     return;
     }
     */
    if (theCommand.equals("ACCOUNT")) {
      accountNum = (String)theEvent;
      thePayment = CreditCardBldrUtil.allocCreditCardObject(accountNum);
      // if payment is still null (manual account is bad then
      // return null and start over
      if (thePayment == null) {
        theAppMgr.showErrorDlg(applet.res.getString("The account number is invalid."));
        theBldrMgr.processObject(applet, "CREDIT_CARD", null, this);
        return;
      }
      creditCard.setCreditCardNumber(accountNum);
      creditCard.setCreditCardType(thePayment.getGUIPaymentName());
      manual = true;
      digitVerify = true;
    }
    if (theCommand.equals("ZIPCODE")) {
      if (((String)theEvent != null) && (!((String)theEvent).equals(""))) {
        if (((String)theEvent).length() != 5) {
          theAppMgr.showErrorDlg(applet.res.getString("The zipcode should be 5 digit string"));
          return;
        }
        try {
          new Long(((String)theEvent));
        } catch (Exception e) {
          // Parsing error .. characters present.
          theAppMgr.showErrorDlg(applet.res.getString("The zipcode should be 5 digit string"));
          return;
        }
      }
      zipCode = (String)theEvent;
      creditCard.setBillingZipCode(zipCode);
    }
    // Modify Credit Card
    if (theCommand.equals("M_ZIPCODE")) {
      String zipCode=(String)theEvent;
      if (zipCode == null||zipCode.equals("")||zipCode.length()==0){
        theAppMgr.showErrorDlg(applet.res.getString("The zipcode should be 5 digit string"));
        return;
      }
      else if (((String)theEvent != null) && (!((String)theEvent).equals(""))) {
        if (((String)theEvent).length() != 5) {
          theAppMgr.showErrorDlg(applet.res.getString("The zipcode should be 5 digit string"));
          return;
        }
        try {
          new Long(((String)theEvent));
        } catch (Exception e) {
          // Parsing error .. characters present.
          theAppMgr.showErrorDlg(applet.res.getString("The zipcode should be 5 digit string"));
          return;
        }
      }
      zipCode = (String)theEvent;
      creditCard.setBillingZipCode(zipCode);
      manual = true;
      isModified = true;
      completeModAttributes();
    }
    /*
     if (theCommand.equals("M_CARDHOLDER_NAME")) {
     cardHolderName = ((String) theEvent).trim();
     creditCard.setCreditCardHolderName(cardHolderName);
     }
     */
    if (theCommand.equals("M_DATE")) {
      // Expiration date on the magstripe is coded YYMM.  Jay wanted the manual entry
      // of an expiration date to come in MMYY.  So we have to reverse the month
      // and year here for the getCalendar() method.
      String testExpDate = (String)theEvent;
      if (4 == testExpDate.length()) {
        StringBuffer buf = new StringBuffer(testExpDate.substring(2));
        buf.append(testExpDate.substring(0, 2));
        testExpDate = buf.toString();
        Calendar cal = CreditCardBldrUtil.getCalendar(testExpDate);
        if (cal == null) {} else {
          if (CreditCardBldrUtil.validateDate(cal)) {
            mExpDate = (String)theEvent;
            Date dt = cal.getTime();
            creditCard.setExpDate(dt);
          } else {
            theAppMgr.showErrorDlg(applet.res.getString("This card has passed its expiration date."));
          }
        }
      } else {
        theAppMgr.showErrorDlg(applet.res.getString(
            "The expiration date must be formatted as MMYY."));
      }
    }
    // End Modify
    /*
     if (theCommand.equals("CARDHOLDER_NAME")) {
     cardHolderName = ((String) theEvent).trim();
     creditCard.setCreditCardHolderName(cardHolderName);
     }
     */
    if (theCommand.equals("DATE")) {
      // Expiration date on the magstripe is coded YYMM.  Jay wanted the manual entry
      // of an expiration date to come in MMYY.  So we have to reverse the month
      // and year here for the getCalendar() method.
      String testExpDate = (String)theEvent;
      if (4 == testExpDate.length()) {
        StringBuffer buf = new StringBuffer(testExpDate.substring(2));
        buf.append(testExpDate.substring(0, 2));
        testExpDate = buf.toString();
        Calendar cal = CreditCardBldrUtil.getCalendar(testExpDate);
        if (cal == null) {} else {
          if (CreditCardBldrUtil.validateDate(cal)) {
            expDate = (String)theEvent;
            Date dt = cal.getTime();
            creditCard.setExpDate(dt);
          } else {
            theAppMgr.showErrorDlg(applet.res.getString("This card has passed its expiration date."));
          }
        }
      } else {
        theAppMgr.showErrorDlg(applet.res.getString(
            "The expiration date must be formatted as MMYY."));
      }
    }
    if (isModified) {
      if (completeModAttributes()) {
        creditCard.setIsModified(true);
        //Commented ISD Changes
        // added for CC encrypted data
//    	if ("US".equalsIgnoreCase(Version.CURRENT_REGION)) {
//        ISDEncryption encryption = null;
//        byte[] encryptedValue = null;
//        ISDValidation validation = new ISDValidation();
//        String plainText = validation.createPlainDataString(creditCard.getCreditCardNumber(), mExpDate);
//        try {
//			encryptedValue = IsdEncryptionClient.isdencrypt(encryption.getHexEncryptedKey(), plainText.getBytes());
//		  if(encryptedValue!=null){
//				String encrHexString = ArmHexConvert.toHex(encryptedValue);
//				//System.out.println("Inside CustomerCreditCardBldr    :"+encrHexString);
//				creditCard.setEncryptedCCData(encrHexString);
//				//System.out.println("Inside CustomerCreditCardBldr    :"+ISDEncryption.getKeyID());
//				creditCard.setKey_id(ISDEncryption.getKeyID());
// 				String maskCCNumber  = CreditAuthUtil.maskCreditCardNo(creditCard.getCreditCardNumber());
// 				creditCard.setCreditCardNumber(maskCCNumber);
// 			}
//		} catch (CryptoException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	}
//    	else{
//    		String ccNum = creditCard.getCreditCardNumber();
//    		EncryptionUtils encryptionUtils = new EncryptionUtils();
//    		String encryptedCCNum = encryptionUtils.encrypt(ccNum);
//    		creditCard.setCreditCardNumber(encryptedCCNum);
//    		
//    	}
		//vivek ended
        theBldrMgr.processObject(applet, "CREDIT_CARD", creditCard, this);
      }
    } else {
      if (completeAttributes()) {
        String storeId = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getId();
        String registerId = ((CMSRegister)theAppMgr.getGlobalObject("REGISTER")).getId();
        creditCard.setStoreId(storeId);
        creditCard.setIsNew(true);
        //Commenting ISD Changes
        //vivek added for new customer.
//    	if ("US".equalsIgnoreCase(Version.CURRENT_REGION)) {
//        if(accountNum!= null && expDate != null){
//        	 ISDEncryption encryption = null;
//             byte[] encryptedValue = null;
//             ISDValidation validation = new ISDValidation();
//             String plainText = validation.createPlainDataString(creditCard.getCreditCardNumber(), expDate);
//             try {
//     			encryptedValue = IsdEncryptionClient.isdencrypt(encryption.getHexEncryptedKey(), plainText.getBytes());
//     			if(encryptedValue!=null){
//     				String encrHexString = ArmHexConvert.toHex(encryptedValue);
//     				creditCard.setEncryptedCCData(encrHexString);
//     				creditCard.setKey_id(ISDEncryption.getKeyID());
//     				String maskCCNumber  = CreditAuthUtil.maskCreditCardNo(creditCard.getCreditCardNumber());
//     				creditCard.setCreditCardNumber(maskCCNumber);
//     		    }
//     		} catch (CryptoException e) {
//     			// TODO Auto-generated catch block
//     			e.printStackTrace();
//     		} catch (IOException e) {
//     			// TODO Auto-generated catch block
//     			e.printStackTrace();
//     		} catch (Exception e) {
//     			// TODO Auto-generated catch block
//     			e.printStackTrace();
//     		}
//        }
//    	}else{
//    		String ccNum = creditCard.getCreditCardNumber();
//    		EncryptionUtils encryptionUtils = new EncryptionUtils();
//    		String encryptedCCNum = encryptionUtils.encrypt(ccNum);
//    		creditCard.setCreditCardNumber(encryptedCCNum);
//    	}
    	
    	//vivek ended
        
        theBldrMgr.processObject(applet, "CREDIT_CARD", creditCard, this);
        thePayment = null;
      }
    }
  }

  /**
   * @param Command
   * @param applet
   * @param initValue
   */
  public void build(String Command, CMSApplet applet, Object initValue) {
    if (initValue instanceof CustomerCreditCard) {
      creditCard = (CustomerCreditCard)initValue;
      resetModAttributes((CustomerCreditCard)initValue);
      this.applet = applet;
      zipTraversed = false;
      //theAppMgr.setSingleEditArea(applet.res.getString("Swipe credit card or press 'Enter' for manual Modification of Customer Credit Card "),
      //                            "M_CREDIT_CARD",IApplicationManager.CREDIT_CARD_MASK);
      theAppMgr.setSingleEditArea(applet.res.getString("Modify customer billing zip code")
          , "M_ZIPCODE", creditCard.getBillingZipCode());
      theAppMgr.setEditAreaFocus();
      /*CMSMSR cmsMSR = CMSMSR.getInstance();
      cmsMSR.registerCreditCardBuilder(this);
      cmsMSR.activate();*/
    } else {
      resetAttributes();
      this.applet = applet;
      zipTraversed = false;
      theAppMgr.setSingleEditArea(applet.res.getString(
          "Swipe credit card or press 'Enter' for manual entry."), "CREDIT_CARD"
          , IApplicationManager.CREDIT_CARD_MASK);
      theAppMgr.setEditAreaFocus();
      /*CMSMSR cmsMSR = CMSMSR.getInstance();
      cmsMSR.registerCreditCardBuilder(this);
      cmsMSR.activate();*/
    }
  }

  /**
   * @param input
   */
  public void processModSwipe(String input) {
    if (input == null || input.length() == 0) {
      manual = true;
      completeModAttributes();
      return;
    }
  }

  /**
   * @param input
   */
  public void processSwipe(String input) {
    if (input == null || input.length() == 0) {
      completeAttributes();
      return;
    }
    if (getCreditCardInfo(input)) { //... and exp date is valid
      thePayment = CreditCardBldrUtil.allocCreditCardPayment(accountNum);
      if (thePayment != null) {
        populateCreditCard();
        // Set the Credit Card Type
        creditCard.setCreditCardType(thePayment.getGUIPaymentName());
        completeAttributes();
        if (completeAttributes()) {
          String storeId = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getId();
          //String registerId = ((CMSRegister) theAppMgr.getGlobalObject("REGISTER")).getId();
          creditCard.setStoreId(storeId);
          creditCard.setIsNew(true);
          theBldrMgr.processObject(applet, "CREDIT_CARD", creditCard, this);
          thePayment = null;
        }
        return;
      } else {
        theAppMgr.showErrorDlg(applet.res.getString("The account number is invalid."));
      }
    }
    // the card is expired or unreadable
    theBldrMgr.processObject(applet, "CREDIT_CARD", null, this);
  }

  /**
   * @return
   */
  private boolean completeAttributes() {
    // didn't swipe, get account number for payment type
    if (thePayment == null) {
      theAppMgr.setSingleEditArea(applet.res.getString("Enter account number."), "ACCOUNT");
      return (false);
    }
    if ((thePayment instanceof CreditCard) && (zipCode == null || zipCode.equals(""))) {
      theAppMgr.setSingleEditArea(applet.res.getString("Enter customer billing zip code")
          , "ZIPCODE");
      return false;
    }
    /*
     if ((manual) && (null == cardHolderName)) {
     theAppMgr.setSingleEditArea(applet.res.getString("Enter card holder's name if different than customer, or press 'Enter'."),
     "CARDHOLDER_NAME", theAppMgr.UPPER_CASE_MASK);
     return (false);
     }
     */
    if (expDate == null) {
      theAppMgr.setSingleEditArea(applet.res.getString("Enter expiration date.  MMYY"), "DATE");
      return (false);
    }
    return (true);
  }

  /**
   * @return
   */
  private boolean completeModAttributes() {
    /*
     if ((manual) && (null == cardHolderName)) {
     theAppMgr.setSingleEditArea(applet.res.getString("Enter card holder's name if different than customer, or press 'Enter'."),
     "M_CARDHOLDER_NAME", theAppMgr.UPPER_CASE_MASK);
     return (false);
     }
     */
    if (mExpDate == null) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("MMyy");
      theAppMgr.setSingleEditArea(applet.res.getString("Modify expiration date.  MMYY"), "M_DATE"
          , dateFormat.format(creditCard.getExpDate()));
      return (false);
    }
    /*
     if (manual) {
     theAppMgr.showErrorDlg(applet.res.getString("Don't forget to get an imprint of the customer's card."));
     }
     */
    return (true);
  }

  /**
   */
  private void resetAttributes() {
    thePayment = null;
    creditCard = new CustomerCreditCard();
    manual = false;
    digitVerify = false;
    accountNum = null;
    cardHolderName = null;
    expDate = null;
    serviceCode = null;
    cid = null;
  }

  /**
   */
  private void resetModAttributes(CustomerCreditCard creditCard) {
    thePayment = null;
    this.creditCard = creditCard;
    manual = false;
    isModified = false;
    accountNum = creditCard.getCreditCardNumber();
    cardHolderName = creditCard.getCreditCardHolderName();
    mZipCode = null;
    mExpDate = null;
  }

  /**
   */
  private void populateCreditCard() {
    if (accountNum != null) {
      creditCard.setCreditCardNumber(accountNum);
    }
    if (cardHolderName != null) {
      creditCard.setCreditCardHolderName(cardHolderName);
    }
    if (expDate != null) {
      Calendar cal = CreditCardBldrUtil.getCalendar(expDate);
      if (cal == null) {
        theAppMgr.showErrorDlg(applet.res.getString("The expiration date is not valid."));
      } else {
        Date dt = cal.getTime();
        creditCard.setExpDate(dt);
      }
    }
  }

  /**
   * put your documentation comment here
   * @param inputStr
   * @return
   */
  public boolean getCreditCardInfo(String inputStr) {
	  // MSR deactivated to make it PCI compliance
   /* if (this.cmsMSR instanceof NonJavaPOSMSR)
      if (!(((NonJavaPOSMSR)cmsMSR).extractDataToBuilder(inputStr))) {
        theAppMgr.showErrorDlg(applet.res.getString("Failure reading credit card data."));
        return (false);
      }*/
    Calendar cal = CreditCardBldrUtil.getCalendar(expDate);
    if (cal == null)
      theAppMgr.showErrorDlg(applet.res.getString("The expiration date is not valid."));
    if (cal != null && !CreditCardBldrUtil.validateDate(cal)) {
      theAppMgr.showErrorDlg(applet.res.getString("This card has passed its expiration date."));
      return (false);
    }
    return (true);
  }

  /**
   * put your documentation comment here
   * @param cmsMSR
   */
  /*public void setCMSMSR(CMSMSR cmsMSR) {
    this.cmsMSR = cmsMSR;
  }*/

  /**
   * put your documentation comment here
   * @param accountNum
   */
  public void setAccountNum(String accountNum) {
    this.accountNum = accountNum;
  }

  /**
   * put your documentation comment here
   * @param cardType
   */
  public void setCardType(String cardType) {
    this.cardType = cardType;
  }

  /**
   * put your documentation comment here
   * @param cardHolderName
   */
  public void setCardHolderName(String cardHolderName) {
    this.cardHolderName = cardHolderName;
  }

  /**
   * put your documentation comment here
   * @param mmyyExpDate
   */
  public void setExpDate(String mmyyExpDate) {
    this.expDate = mmyyExpDate;
  }
  /**
   * put your documentation comment here
   * @param bytes
   * @return
   */
/*  private static String bytes2String(byte[] bytes) {
    StringBuffer stringBuffer = new StringBuffer();
    for (int i = 0; i < bytes.length; i++) {
      stringBuffer.append((char)bytes[i]);
    }
    System.out.println("data set in Customer CC object>>>> "+stringBuffer);
    return stringBuffer.toString();
  }*/
  
 
}

