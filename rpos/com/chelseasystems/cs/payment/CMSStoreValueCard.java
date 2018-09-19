/*
 History:
 +------+------------+-----------+-----------+-------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                     |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 4    | 06-21-2005 |  Vikram   | 195       |Added delete attribute                           |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 3    | 05-05-2005 |  Khyati   | N/A       |Added methods for store ID, customerId, issuance |
 |                                           |date, expiration date                            |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 2    | 04-28-2005 |Khayti     |           | Redeemable Management                                               |
 -----------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.authorization.PaymentValidationRequests;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.payment.PaymentMgr;
import com.chelseasystems.cr.payment.StoreValueCard;

import java.util.Date;

import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.rules.RuleEngine;
import com.chelseasystems.cs.ajbauthorization.AJBValidation;


/**
 *
 * <p>Title: CMSStoreValueCard</p>
 *
 * <p>Description: Type of payment</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSStoreValueCard extends StoreValueCard {
  private String manualAuthCode = null;
  private String customerId = null;
  private boolean isManual = false;
  //ks: add storeId, customerId, issuance date, expiration date
  private String storeId;
  private Date issuanceDate;
  private Date expirationDate;
  private boolean status = true;
  //Vivek Mishra : Added for track data from 107 AJB response 12-SEP-2016
  private String trackData;
  //Vivek Mishra : Added for unique AJB sequence 13-SEP-2016
  private String ajbSequence;
  //vishal
  protected String respStatusCode = "";
  protected boolean partialAuth = false;
  private static ConfigMgr storeConfig;
  private static String gcInt_flag;
  private boolean authRequired;
  protected String respStatusCodeDesc = new String("CALL CENTER");
  //Vivek Mishra : Added to capture AJB response auth code and error message 02-NOV-2016
  protected String respAuthorizationCode = "";
  private String errordiscription; 
  //Ends here 02-NOV-2016
  public String getRespStatusCode() {
	return respStatusCode;
}

public void setRespStatusCode(String respStatusCode) {
	this.respStatusCode = respStatusCode;
}


public void setAuthRequired(boolean isAuthRequired) {
	this.authRequired = isAuthRequired;
}


int count=0;
public String getRespStatusCodeDesc() {
	  // Set the Description for the status.  Display the auth code if approved.
	 if (respStatusCode.equals("0")) {
		 if(manualAuthCode!=null && !(manualAuthCode.equals("")))
	      respStatusCodeDesc = this.manualAuthCode;
		 else
			 respStatusCodeDesc=this.respAuthorizationCode;
	    } else if (respStatusCode.equals("1")) {
	      respStatusCodeDesc = "DECLINED";
	    } else if (respStatusCode.equals("2")) {
	      respStatusCodeDesc = new String(PaymentMgr.getDefaultCallCenterDisplay(
	          getTransactionPaymentName()));
	    } else if (respStatusCode.equals("3")) {
	      respStatusCodeDesc = "DECLINED";
	    }
	      else if (respStatusCode.equals("5")) {
	          respStatusCodeDesc = new String(PaymentMgr.getDefaultCallCenterDisplay(
	              getTransactionPaymentName()));
	    } else if (respStatusCode.equals("6")) {
	        respStatusCodeDesc = "DECLINED";
	    } else if (respStatusCode.equals("8")) {
	      respStatusCodeDesc = "TRY LATER";//put dialogue for cashier --> try later on click of OK it will go on payment screen 
	      }
	    //timeout
	    else if(respStatusCode.equals("10")){
	    	respStatusCodeDesc = new String(PaymentMgr.getDefaultCallCenterDisplay(
	  	          getTransactionPaymentName()));   // Manually authorize if time out happens	
	    }//Vivek Mishra : Added for new return flow
	    else if (respStatusCode.equals("99")) {
	        respStatusCodeDesc = "NOT REFUNDED";
	    }//Ends
	    else if(respStatusCodeDesc.equalsIgnoreCase(manualAuthCode)){
	    	
	    }
	    else {
	    	//Vivek Mishra : Added for displaying Default call center number in case of AJB offline scenario.	  
	      //respStatusCodeDesc = "PENDING POST";
	    	respStatusCodeDesc = new String(PaymentMgr.getDefaultCallCenterDisplay(
	    	          getTransactionPaymentName()));
	    	//Ends
	    }
	    return (respStatusCodeDesc);
}

public boolean isAuthRequired() {
		return authRequired;
  }
//end vishal 28 sept 2016
//vishal : giftcard balance 14 oct 2016
private ArmCurrency giftcardBalance;



public ArmCurrency getGiftcardBalance() {
	return giftcardBalance;
}

public void setGiftcardBalance(ArmCurrency giftcardBalance) {
	this.giftcardBalance = giftcardBalance;
}

//end vishalyevale 14 oct 2016
//vishal 25 oct 2016 to check partial auth
public boolean isPartialAuth() {
	return partialAuth;
}

public void setPartialAuth(boolean partialAuth) {
	this.partialAuth = partialAuth;
}
//end vishal 25 oct  2016

  /**
   * Constructor
   * @param transactionPaymentName String
   */
  public CMSStoreValueCard(String transactionPaymentName) {
    super(transactionPaymentName);
    customerId = "";
    isManual = false;
    manualAuthCode = "";
    
  }

  /**
   * Method used to get the customer id
   * @return String
   */
  public String getCustomerId() {
    return this.customerId;
  }

  /**
   * Method used to set the customer id
   * @param customerId String
   */
  public void setCustomerId(String customerId) {
    doSetCustomerId(customerId);
  }

  /**
   * Method used to set the customer id
   * @param customerId String
   */
  public void doSetCustomerId(String customerId) {
    this.customerId = customerId;
  }

  /**
   * Method used to set the manual authorization code
   * @param isManual boolean
   */
  public void setIsManual(boolean isManual) {
    this.isManual = isManual;
  }

  /**
   * Method used to check manual authorization code
   * @return boolean
   */
  public boolean getIsManual() {
    return this.isManual;
  }

  /**
   * Method used to get manual authorization code
   * @return String
   */
  public String getManualAuthCode() {
    return this.manualAuthCode;
  }

  /**
   * Method used to set manual authorization code
   * @param manualAuthCode String
   */
  public void setManualAuthCode(String manualAuthCode) {
	  //vishal 
	 
      respStatusCode = "0";
      respStatusCodeDesc = manualAuthCode;
    doSetManualAuthCode(manualAuthCode);
    //Mayuri Edhara :: 15-MAR-17 added for manual authorization check
    this.authRequired = false;
  }

  /**
   * Method used to set manual authorization code
   * @param manualAuthCode String
   */
  public void doSetManualAuthCode(String manualAuthCode) {
    this.manualAuthCode = manualAuthCode;
  }

  //ks: add storeId, customerId, issuance date, expiration date
  /**
   * Method used to get store id
   * @return String
   */
  public String getStoreId() {
    return this.storeId;
  }

  /**
   * Method used to set store id
   * @param storeId String
   */
  public void setStoreId(String storeId) {
    doSetStoreId(storeId);
  }

  /**
   * Method is used to set store id
   * @param storeId String
   */
  public void doSetStoreId(String storeId) {
    this.storeId = storeId;
  }

  /**
   * Method used to get issuance date of store value card
   * @return Date
   */
  public Date getIssuanceDate() {
    return this.issuanceDate;
  }

  /**
   * Method used to set issuance date of store value card
   * @param issuanceDate Date
   */
  public void setIssuanceDate(Date issuanceDate) {
    doSetIssuanceDate(issuanceDate);
  }

  /**
   * Method used to set issuance date of store value card
   * @param issuanceDate Date
   */
  public void doSetIssuanceDate(Date issuanceDate) {
    this.issuanceDate = issuanceDate;
  }

  /**
   * Method used to get expiry date of store value card
   * @return Date
   */
  public Date getExpirationDate() {
    return this.expirationDate;
  }

  /**
   * Method used to set expiry date of store value card
   * @param expirationDate Date
   */
  public void setExpirationDate(Date expirationDate) {
    doSetExpirationDate(expirationDate);
  }

  /**
   * Method used to set expiry date of store value card
   * @param expirationDate Date
   */
  public void doSetExpirationDate(Date expirationDate) {
    this.issuanceDate = expirationDate;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean getStatus() {
    return this.status;
  }

  /**
   * put your documentation comment here
   * @param Status
   */
  public void doSetStatus(boolean Status) {
    this.status = Status;
  }

  /**
   * put your documentation comment here
   * @param Status
   */
  public void setStatus(boolean Status) {
    doSetStatus(Status);
  }

  /**
   * Tests if the issue is valid.
   * @return true if the StoreValueCard can be issued
   */
  public void isValidForIssue(int txnAppletMode)
      throws BusinessRuleException {
    RuleEngine.execute(this.getClass().getName(), "isValidForIssue", this
        , new Object[] {new Integer(txnAppletMode)
    });
  }
  
  //Vivek Mishra : Added for track data from 107 AJB response 12-SEP-2016
  public String getTrackData() {
	return trackData;
  }

  public void setTrackData(String trackData) {
	this.trackData = trackData;
  }
  //Ends here 12-SEP-2016

  //Vivek Mishra : Added for unique AJB sequence 13-SEP-2016
  public String getAjbSequence() {
	return ajbSequence;
  }

  public void setAjbSequence(String ajbSequence) {
	this.ajbSequence = ajbSequence;
  }
  //Ends here 13-SEP-2016
  
  //mayuri edhara:: 
  /** This instance is used to create credit card validation strings */
  transient protected PaymentValidationRequests validationRequest = (PaymentValidationRequests)new
      ConfigMgr("credit_auth.cfg").getObject("VALIDATION_REQUESTS");
  
  public Object getValidationRequest(String store, String terminal, boolean isRefundPaymentRequired, boolean isManualOverride) {
    // If at all we need to make changes to the what type of method is
    // called, based on the type of the cc, amex or any other card...
    // call the method from here.
	  if(validationRequest==null){
		  validationRequest = (PaymentValidationRequests)new
			      ConfigMgr("credit_auth.cfg").getObject("VALIDATION_REQUESTS");
	  }
    return (((AJBValidation)validationRequest).getStoreValueCardValidationRequest(this, store, terminal, isRefundPaymentRequired, isManualOverride));
  }
//Vivek Mishra : Added to capture AJB response auth code and error message 02-NOV-2016
  public String getRespAuthorizationCode() {
	return respAuthorizationCode;
  }

  public void setRespAuthorizationCode(String respAuthorizationCode) {
	this.respAuthorizationCode = respAuthorizationCode;
  }

  public String getErrordiscription() {
	return errordiscription;
  }

  public void setErrordiscription(String errordiscription) {
	this.errordiscription = errordiscription;
  }
//Ends here 02-NOV-2016    
}
