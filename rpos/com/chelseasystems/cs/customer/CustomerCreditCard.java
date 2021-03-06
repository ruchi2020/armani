/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.customer;

import java.io.Serializable;
import java.util.Date;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.payment.DigitalSignature;



/**
 * <p>Title: CustomerCreditCard.java</p>
 *
 * <p>Description:Business object for storing Customer address </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: Skillnet Inc.</p>
 *
 * @author Sumit Krishnan
 * @version 1.0
 */
public class CustomerCreditCard implements Serializable {
  /**
   * CreditCard Id
   */
  private String sCreditCardID;
  /**
   * sStore Id
   */
  private String sStoreId;
  /**
   * Card Number
   */
  private String sCreditCardNumber;
  /**
   * Credit Card HolderName
   */
  private String sCreditCardHolderName;
  /**
   * Expiration Date
   */
  private Date sExpDate;
  /**
   * Billing Zip Code
   */
  private String sBillingZipCode;
  /**
   * Use As Primary
   */
  private boolean bUseAsPrimary;
  /**
   * Country
   */
  private boolean isModified;
  private boolean isNew;
  /**
   *
   */
  private boolean isRemove;
  /**
   * Credit Card Type
   */
  private String sCreditCardType;
  protected String respValidationCode = "";

  /** Variable for determining if credit card requires authorization */
  public boolean authRequired = true;

  /** Variable for determining if manual override was selected in authorization */
  public boolean manualOverride = false;
  protected boolean isManuallyKeyed;
  public String respStatusCode = "";
//added by Sonali for AJB to add CARD_TOKEN
  private String cardToken;
  private String maskedCreditCardNum;
  //to capture signature
  protected String signatureAscii;
  protected boolean isSignatureValidationRequired = true; 
  protected boolean isSignatureValid;
  protected byte[] signByteCode;
/*  
  *//** This instance is used to create credit card validation strings *//*
  transient protected CustomerPaymentValidationRequests validationRequest = (CustomerPaymentValidationRequests)new
      ConfigMgr("credit_auth.cfg").getObject("VALIDATION_REQUESTS");
  *//**
   *  The authorizors Status Code Description used for GUI display.
   *  This field is set when the respStatusCode is set from the authorizor,
   *  therefore requires no set method.  Only valid responses are:
   *  Will return "Approved"  if respStatusCode = 01
   *  Will return "Declined" if respStatusCode = 02
   *  Will return "Call Center"  if respStatusCode = 03, 04, 05 or null
   */
  public String respStatusCodeDesc = new String("CALL CENTER");
  /**
   *  The authorizors field Authorization Code received from the approval string.
   *  Corresponds to string position <150,155> in the ISD response header
   *  with a character length of six.
   *  This is the authorization code that will be saved with the transaction
   *  as the approval code.    ISD Header response field #21.
   */
  public String respAuthorizationCode = "";
  /**
   *  The authorizors field Authorization Response Code received from the approval
   *  string. Corresponds to string position <156,159> in the ISD response
   *  header with a character length of four.   See GE Capital Corp. Point
   *  of Sale Credit Processing "Design Document" for a further explanation
   *  of these codes.    ISD Header response field #22.
   *  0000 = Transaction processed
   *  0001 = Previously declined 30 days                  respHostActionCode = 02
   *  0002 = Approved within 60 days (existing acct-call) respHostActionCode = 03
   *  0003 = Additional info needed                       respHostActionCode = 03
   *  0004 = Invalid packet number                        respHostActionCode = 05
   *  0005 = Approved more than 60 days (existing acct-call) respHostActionCode = 03
   *  0006 = Decline                                      respHostActionCode = 02
   *  0007 = Field edit errors
   *  0008 = Invalid client Id                            respHostActionCode = 05
   *  0009 = Approved with CSP insurance                  respHostActionCode = 01
   *  0010 = Approved without CSP insurance               respHostActionCode = 01
   *  0011 = GECC System network error - call             respHostActionCode = 01
   *  0012 = Application queued                           respHostActionCode = 04
   *  0013 = Invalid route code                           respHostActionCode = 05
   *  0014 = Account not found                            respHostActionCode = 03
   *  0015 = Cannot access account                        respHostActionCode = 03
   *  0016 = Credit Bureau processing - waiting           respHostActionCode = 03
   *  0017 = Authorized buyer error                       respHostActionCode = 02
   *  0018 = Invalid record length                        respHostActionCode = 05
   *  0019 = Invalid store number                         respHostActionCode = 05
   *  0020 = Account already processed                    respHostActionCode = 02
   *  0021 = Conditional approval                         respHostActionCode = 01
   *  0022 = Conditional approved with CSP insurance      respHostActionCode = 01
   *  0023 = Conditional approved without CSP insurance   respHostActionCode = 01
   *  0024 = Declined No-hit                              respHostActionCode = 02
   *  0025 = Invalid terminal Id                          respHostActionCode = 05
   */
  protected String respAuthorizationResponseCode = "";

  // Start ISD Credit Card RESPONSE fields (
  // Credit Card response fields start at position 191.  Only pulling the ones we need.
  /**
   *  The POS Entry Mode field required by the authorizor ISD.  Corresponds
   *  to string position <195,196> in the ISD CCard response with a character length
   *  of two (2).
   */
  public CustomerCreditCard() {
  }

  /**
   * Set Card Number
   * @param sValue CreditCardNumber
   */
  public void setStoreId(String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.sStoreId = sValue;
  }

  /**
   * Get Card Number
   * @return CreditCardNumber
   */
  public String getStoreId() {
    return this.sStoreId;
  }

  /**
   * put your documentation comment here
   * @param val
   */
  public void setIsNew(boolean val) {
    this.isNew = val;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean isNew() {
    return this.isNew;
  }

  /**
   * Set Card Number
   * @param sValue CreditCardNumber
   */
  public void setCreditCardNumber(String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.sCreditCardNumber = sValue;
  }

  /**
   * Get Card Number
   * @return CreditCardNumber
   */
  public String getCreditCardNumber() {
    return this.sCreditCardNumber;
  }

  /**
   * Set Card Number
   * @param sValue CreditCardNumber
   */
  public void setCreditCardHolderName(String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.sCreditCardHolderName = sValue;
  }

  /**
   * Get Card Number
   * @return CreditCardHolderName
   */
  public String getCreditCardHolderName() {
    return this.sCreditCardHolderName;
  }

  /**
   * Set ExpDate
   * @param sValue ExpDate
   */
  public void setExpDate(Date sExpDate) {
    this.sExpDate = sExpDate;
  }

  /**
   * Get ExpDate
   * @return ExpDate
   */
  public Date getExpDate() {
    return this.sExpDate;
  }

  /**
   * Set BillingZipCode
   * @param sValue BillingZipCode
   */
  public void setBillingZipCode(String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.sBillingZipCode = sValue;
  }

  /**
   * Get ExpDate
   * @return ExpDate
   */
  public String getBillingZipCode() {
    return this.sBillingZipCode;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean isUseAsPrimary() {
    return this.bUseAsPrimary;
  }

  /**
   * Set Use as Primary
   * @param bUsePrimary true/false
   */
  public void setUseAsPrimary(boolean bUsePrimary) {
    this.bUseAsPrimary = bUsePrimary;
  }

  /**
   * put your documentation comment here
   * @param val
   */
  public void setIsModified(boolean val) {
    this.isModified = val;
  }

  /**
   * Method check is address modified
   * @return boolean
   */
  public boolean isModified() {
    return this.isModified;
  }

  /**
   * Method used to set address is removed
   * @param val boolean
   */
  public void setIsRemove(boolean val) {
    this.isRemove = val;
  }

  /**
   * Method check is address removed
   * @return boolean
   */
  public boolean isRemove() {
    return this.isRemove;
  }

  /**
   * Sets the manual override information for a credit card
   * @param the approval code received from the call to the authorizor
   */
  public void setManualOverride(String approvalCode) {
    this.manualOverride = true;
    //Anjana added the proper approval code
    this.respStatusCode = "0";
    // Display the approval code instead of the word "APPROVED"
    this.respStatusCodeDesc = approvalCode;
    this.respAuthorizationCode = approvalCode;
    this.authRequired = false;
    //super.setIsApproved( true );
  }

  /**
   * Get Address Type
   * @return AddressType
   */
  public String getCreditCardType() {
    return this.sCreditCardType;
  }

  /**
   * Set CreditCard Type
   * @param sValue CreditCardType
   */
  public void setCreditCardType(String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.sCreditCardType = sValue;
  }

  /**
   * Set Address Id
   * @param sValue AddressID
   */
  public void setCreditCardId(String sValue) {
    this.sCreditCardID = sValue;
  }

  /**
   * Get Address ID
   * @return Address Id
   */
  public String getCreditCardId() {
    return this.sCreditCardID;
  }
  /**
   * Set Address Line 1
   * @param sValue AddressLine1
   */
//added by Sonali for AJB to add CARD_TOKEN
  public void setCardToken(String cardToken) {
  	this.cardToken = cardToken;
  }

  public String getCardToken() {
  	return cardToken;
  }

  public void setMaskedCreditCardNum(String maskedCreditCardNum) {
  	this.maskedCreditCardNum = maskedCreditCardNum;
  }

  public String getMaskedCreditCardNum() {
  	return maskedCreditCardNum;
  }
  
  //Anjana added for signature capture 
  public void setRespStatusCode(String respStatusCode) {
	  	this.respStatusCode = respStatusCode;
	  }

	  public String getRespStatusCode() {
	  	return respStatusCode;
	  }
  

	  public boolean isSignatureValidationRequired() {
	    return isSignatureValidationRequired;
	  }


	  public void setSignatureValidationRequired(boolean isSignatureValidationRequired) {
	    this.isSignatureValidationRequired = isSignatureValidationRequired;
	  }


	  public boolean isSignatureValid() {
	    System.out.println("\n In new CC\n");
	    return isSignatureValid;
	  }
	  
	
	  public void setSignatureValidation(Object response) {
				String signAscii = "Qk1yOAAAAAAAADYAAAAoAAAATgAAAD0AAAABABgAAAAAADw4AAAAAAAAAAAAAAAAAAAAAAAA////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////AAD///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////8AAP///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////wAA////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////AAAA////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////AAD///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////8AAAD///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////8AAP///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////wAAAP///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////wAA////////////////////////////////////////////////////////////////////////////////////////////AAAA////////////////////////////////////////////////////////////////////////////////AAAAAAAA////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////AAD///////////////////////////////////////////////////////////////////////////////////////////8AAAD///////////8AAAAAAAD///////////////////////////////////////////////////////8AAAAAAAAAAAD///////////////8AAAD///////////////////////////////////////////////////////////////////////////////////////////////////////////8AAP///////////////////////////////////////////////////////////////////////////////////////wAAAAAAAP///////wAAAAAAAAAAAP///////////////////////////////////////////////////wAAAP///////wAAAP///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA////////////////AAAAAAAA////////AAAA////AAAA////////////////////////////////AAAA////////AAAAAAAAAAAA////////AAAA////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////AAD///////////8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD///////////////////////8AAAAAAAAAAAAAAAAAAAAAAAD///////8AAAD///8AAAD///////////////////////////8AAAAAAAD///8AAAAAAAAAAAD///////////8AAAD///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////8AAP///////////////////////////////////////////////wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP///////wAAAP///wAAAAAAAAAAAAAAAP///////////////wAAAP///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////wAA////////////////////////////////////////////////////////////////////////////////////AAAA////AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA////////////////AAAA////////////////////////////////AAAAAAAA////////////////////////////////////////////////////////////////////////////////////////AAD///////////////////////////////////////////////////////////////////////////////8AAAD///////////8AAAAAAAD///////8AAAAAAAAAAAD///////8AAAD///////////8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD///////////////8AAAAAAAD///////////////////////////////////////////////////////////////////////////////////////8AAP///////////////////////////////////////////////////////////////////////////wAAAAAAAAAAAP///////wAAAP///////////////////////////////////////////////wAAAAAAAP///wAAAP///////////////wAAAP///////wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP///////////////////////////////////////////////////////////////////////////////////////wAA////////////////////////////////////////////////////////////////////////////AAAA////AAAAAAAAAAAAAAAA////////////////////////////////////////////////AAAA////////AAAAAAAA////////////AAAA////////////////////////////AAAA////AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA////////////////////////////////////////////////////////////AAD///////////////////////////////////////////////////////////////////////////8AAAD///////8AAAAAAAAAAAD///////////////////////////////////////////////////////////////8AAAD///////////8AAAD///////////////////8AAAAAAAD///////8AAAD///////////8AAAAAAAD///////8AAAAAAAAAAAAAAAAAAAAAAAAAAAD///////////////////////////////8AAP///////////////////////////////////////////////////////////////////////wAAAP///////////////////////wAAAAAAAP///////////////////////////////////////////////////////////////////////wAAAP///////////////////wAAAP///////////wAAAP///////////////////wAAAP///////////////////////////////wAAAAAAAAAAAP///////////////////wAA////////////////////////////////////////////////////////////////////////AAAA////////////////////////AAAAAAAAAAAAAAAAAAAA////////////////////////////////////////////////////////////AAAA////////////////////AAAA////////////////AAAA////////////////AAAA////////////////////////////////////////////AAAAAAAA////////////AAD///////////////////////////////////////////////////////////////////////8AAAD///////////////////////8AAAD///////////8AAAAAAAD///////////////////////////////////////////////////////8AAAD///////////////8AAAD///////////////////8AAAD///////////////8AAAD///////////////////////////////////////////////////////////////8AAP///////////////////////////////////////////////////////////////////////wAAAP///////////////////////wAAAP///////////////////wAAAAAAAP///////////////////////////////////////////////wAAAP///////////////wAAAP///////////////////wAAAP///////////wAAAP///////////////////////////////////////////////////////////////////wAA////////////////////////////////////////////////////////////////////AAAAAAAA////////////////////////AAAA////////////////////////////AAAAAAAA////////////////////////////////////////AAAA////////////AAAAAAAA////////////////////AAAAAAAAAAAAAAAA////////////////////////////////////////////////////////////////////////AAD///////////////////////////////////////////////////////////////////8AAAD///////////////////////////8AAAAAAAD///////////////////////////////8AAAD///////////////////////////////////////8AAAD///8AAAD///////////////////////////8AAAAAAAD///////////////////////////////////////////////////////////////////////////////8AAP///////////////////////////////////////////////////////////////////wAAAP///////////////////////////////wAAAP///////////////////////////////////wAAAP///////////////////////////////////wAAAP///wAAAP///////////////////////////////////////////////////////////////////////////////////////////////////////////////////wAA////////////////////////////////////////////////////////////////AAAAAAAA////////////////////////////////AAAA////////////////////////////////////AAAAAAAA////////////////////////////////AAAA////AAAA////////////////////////////////////////////////////////////////////////////////////////////////////////////////////AAD///////////////////////////////////////////////////////////////8AAAD///////////////////////////////////8AAAD///////////////////////////////////////////8AAAD///////////////////////////8AAAAAAAD///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////8AAP///////////////////////////////////////////////////////////////wAAAP///////////////////////////////////wAAAP///////////////////////////////////////////////wAAAAAAAP///////////////////wAAAP///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////wAA////////////////////////////////////////////////////////////////AAAA////////////////////////////////////AAAA////////////////////////////////////////////////////////AAAA////////////////AAAA////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////AAD///////////////////////////////////////////////////////////8AAAD///////////////////////////////////////8AAAD///////////////////////////////////////////////////////8AAAAAAAD///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////8AAP///////////////////////////////////////////////////////////wAAAP///////////////////////////////////////////wAAAP///////////////////////////////////////////////////////wAAAAAAAP///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////wAA////////////////////////////////////////////////////////AAAAAAAA////////////////////////////////////////////AAAA////////////////////////////////////////////////////////////////AAAA////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////AAD///////////////////////////////////////////////////////8AAAD///////////////////////////////////////////////8AAAD///////////////////////////////////////////////////////////////////8AAAD///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////8AAP///////////////////////////////////////////////////wAAAP///////////////////////////////////////////////////wAAAAAAAP///////////////////////////////////////////////////////////////////wAAAP///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////wAA////////////////////////////////////////////////////AAAA////////////////////////////////////////////////////////AAAA////////////////////////////////////////////////////////////////////////AAAAAAAA////////////////////////////////////////////////////////////////////////////////////////////////////////////////////AAD///////////////////////////////////////////////////8AAAD///////////////////////////////////////////////////////8AAAD///////////////////////////////////////////////////////////////////////////8AAAD///////////////////////////////////////////////////////////////////////////////////////////////////////////////////8AAP///////////////////////////////////////////////////wAAAP///////////////////////////////////////////////////////wAAAP///////////////////////////////////////////////////////////////////////////////wAAAAAAAP///////////////////////////////////////////////////////////////////////////////////////////////////////////wAA////////////////////////////////////////////////AAAA////////////////////////////////////////////////////////////AAAA////////////////////////////////////////////////////////////////////////////////////////AAAAAAAA////////////////////////////////////////////////////////////////////////////////////////////////////AAD///////////////////////////////////////////////8AAAD///////////////////////////////////////////////////////////8AAAAAAAD///////////////////////////////////////////////////////////////////////////////////////////8AAAD///////////////////////////////////////////////////////////////////////////////////////////////8AAP///////////////////////////////////////////////wAAAP///////////////////////////////////////////////////////////////wAAAP///////////////////////////////////////////////////////////////////////////////////////////////wAAAP///////////////////////////////////////////////////////////////////////////////////////////wAA////////////////////////////////////////////////AAAA////////////////////////////////////////////////////////////////AAAA////////////////////////////////////////////////////////////////////////////////////////////////////AAAA////////////////////////////////////////////////////////////////////////////////////////AAD///////////////////////////////////////////8AAAAAAAD///////////////////////////////////////////////////////////////8AAAAAAAD///////////////////////////////////////////////////////////////////////////////////////////////////8AAAAAAAD///////////////////////////////////////////////////////////////////////////////8AAP///////////////////////////////////////////wAAAP///////////////////////////////////////////////////////////////////////////wAAAAAAAP///////////////////////////////////////////////////////////////////////////////////////////////wAAAAAAAP///////////////////////////////////////////////////////////////////////////wAA////////////////////////////////////////////AAAA////////////////////////////////////////////////////////////////////////////////////AAAAAAAAAAAAAAAAAAAA////////////////////////////////////////////////////////////////////////////////////AAAA////////////////////////////////////////////////////////////////////////AAD///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////8AAAAAAAD///////////////////////////////////////////////////////////////////////////////8AAAAAAAD///////////////////////////////////////////////////////////////8AAP///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////wAAAAAAAAAAAP///////////////////////////////////////////////////////////////////////////wAAAAAAAAAAAP///////////////////////////////////////////////////wAA////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////AAAA////////////////////////////////////////////////////////////////////////////////////////AAAAAAAA////////////////////////////////////////////AAD///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////8AAAAAAAAAAAAAAAD///////////////////////////////////////////////////////////////////////////////8AAAD///////////////////////////////////////8AAP///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////wAAAAAAAP///////////////////////////////////////////////////////////////////////////////wAAAAAAAAAAAP///////////////////////////wAA////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////AAAAAAAAAAAAAAAAAAAAAAAAAAAA////////////////////////////////////////////////////////////AAAA////////////////////////////AAD///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////8AAAAAAAAAAAD///////////////////////////////////////////////8AAAAAAAAAAAD///////////////////8AAP///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////wAAAAAAAAAAAAAAAAAAAP///////////////////////////////////////wAAAP///////////////////wAA////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////AAAAAAAA////////////////////////////////////AAAA////////////////AAD///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////8AAAAAAAAAAAAAAAAAAAD///////////////////8AAAAAAAD///////8AAP///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////wAAAAAAAAAAAAAAAP///////wAAAAAAAP///wAA////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////AAAAAAAAAAAAAAAAAAAAAAD///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////8AAP///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////wAA////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////AAD///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////8AAP///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////wAA////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////AAA=";
				this.setSignatureAscii(signAscii);
		        //End
		
		  }


	  public void setSignatureValid(boolean isSignatureValid) {
	    setSignatureValidationRequired(false);
	    this.isSignatureValid = isSignatureValid;
	  }

	  //Anjana : Added for capturing signature in 3-byte Ascii as received from AJB with Signature Capture 150 message response. 
	  
	  public String getSignatureAscii() 
	  {
		return signatureAscii;
	  }
	 
	  public void setSignatureAscii(String signatureAscii) 
	  {
		this.signatureAscii = signatureAscii;
	  }

	  public byte[] getSignByteCode() 
	  {
		return signByteCode;
	  }

	  public void setSignByteCode(byte[] signByteCode) 
	  {
		this.signByteCode = signByteCode;
	  }

	  //End
  public String getMaskedlast4CreditCardNum() {
	  	int strLength = maskedCreditCardNum.length();
		String lastStr = maskedCreditCardNum.substring(strLength - 4);
		return "************"+lastStr;

	  }
}

