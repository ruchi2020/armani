/*
 History:
 +------+------------+-----------+-----------+-------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                     |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 3    | 05-05-2005 |  Khyati   | N/A       |Added methods for store ID, customerId, issuance |
 |                                           |date, expiration date                            |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 2    | 04-28-2005 |Khayti     |           | Store Close Out                                 |
 -----------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.rules.IRuleEngine;
import com.chelseasystems.cr.authorization.PaymentValidationRequests;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.payment.DueBillIssue;
import com.chelseasystems.cr.payment.PaymentMgr;
import com.chelseasystems.cs.ajbauthorization.AJBValidation;

import java.util.Date;


/**
 *
 * <p>Title: CMSDueBillIssue</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSDueBillIssue extends DueBillIssue implements IRuleEngine {
  public static final String CREDIT_NOTE_ISSUE_TYPE = "CN";
  private String controlNum;
  private String customerId;
  private boolean isManual = false;
  //ks: add storeId, customerId, issuance date, expiration date
  private String storeId = null;
  private Date issuanceDate = null;
  private Date expirationDate = null;
  private boolean status = true;
  private static ConfigMgr storeConfig;
  private static String gcInt_flag;
  // vishal yevale 19 oct 2016
  private String trackData;
  private String ajbSequence;
  protected boolean partialAuth = false;
  //vishal end 19 oct 2016
  // Vishal Yevale 3 oct 2016 : added for manual authorization payment Giftcard Tender 
  private String respStatusCode="";
  private String manualAuthCode="";
  private boolean authRequired;
  protected String respStatusCodeDesc = new String("CALL CENTER");
//Vivek Mishra : Added to capture AJB response auth code and error message 11-NOV-2016
  protected String respAuthorizationCode = "";
  private String errordiscription; 
  //Ends here 11-NOV-2016
  public String getRespStatusCode() {
	return respStatusCode;
}

  public void setManualAuthCode(String manualAuthCode){
	  respStatusCode = "0";
      respStatusCodeDesc = manualAuthCode;
	  this.manualAuthCode=manualAuthCode;
	  this.authRequired = false;
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
	    else if(this.manualAuthCode.length()>=1 ){
	    	if(count==1){
	    		return respStatusCodeDesc;
	    	}
			respStatusCodeDesc= this.manualAuthCode;
			count=1;
		}
	    else {
	    	respStatusCodeDesc = new String(PaymentMgr.getDefaultCallCenterDisplay(
	    	          getTransactionPaymentName()));
	    }
	    return (respStatusCodeDesc);
}

public boolean isAuthRequired() {
    return authRequired;
  }

//end vishal 3 oct 2016
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
   * Default Constructor
   */
  public CMSDueBillIssue() {
    this(null);
    controlNum = "";
    customerId = "";
    isManual = false;
    
  }

  /**
   * Constructor
   * @param transactionPaymentName String
   */
  public CMSDueBillIssue(String transactionPaymentName) {
    super(transactionPaymentName);
    controlNum = "";
    customerId = "";
    isManual = false;
  }

  /**
   * Method used to get control number
   * @return String
   */
  public String getControlNum() {
    return controlNum;
  }

  /**
   * Method used to set control number
   * @param aString String
   */
  public void doSetControlNum(String aString) {
    if (aString == null) {
      return;
    } else {
      controlNum = aString;
      return;
    }
  }

  /**
   * Method used to get customer id
   * @return String
   */
  public String getCustomerId() {
    return this.customerId;
  }

  /**
   * Method used to set customer id
   * @param customerId String
   */
  public void setCustomerId(String customerId) {
    doSetCustomerId(customerId);
  }

  /**
   * Method used to set customer id
   * @param customerId String
   */
  public void doSetCustomerId(String customerId) {
    this.customerId = customerId;
  }

  /**
   *
   * @param isManual boolean
   */
  public void setIsManual(boolean isManual) {
    this.isManual = isManual;
  }

  /**
   *
   * @return boolean
   */
  public boolean getIsManual() {
    return this.isManual;
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
    return;
  }

  /**
   * Method used to set store id
   * @param storeId String
   */
  public void doSetStoreId(String storeId) {
    this.storeId = storeId;
    return;
  }

  /**
   * Method used to get issuance date
   * @return Date
   */
  public Date getIssuanceDate() {
    return this.issuanceDate;
  }

  /**
   * Method used to set issuance date
   * @param issuanceDate Date
   */
  public void setIssuanceDate(Date issuanceDate) {
    doSetIssuanceDate(issuanceDate);
  }

  /**
   * Method used to set issuance date
   * @param issuanceDate Date
   */
  public void doSetIssuanceDate(Date issuanceDate) {
    this.issuanceDate = issuanceDate;
  }

  /**
   * Method used to get expiry date
   * @return Date
   */
  public Date getExpirationDate() {
    return this.expirationDate;
  }

  /**
   * Method used to set expiry date
   * @param expirationDate Date
   */
  public void setExpirationDate(Date expirationDate) {
    doSetExpirationDate(expirationDate);
  }

  /**
   * Method used to set expiry date
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
  public void doSetStatus(boolean status) {
    this.status = status;
  }

  /**
   * put your documentation comment here
   * @param Status
   */
  public void setStatus(boolean status) {
    doSetStatus(status);
  }

//vishal yevale 19 oct 2016:: 
  public String getAjbSequence() {
	return ajbSequence;
  }

  public void setAjbSequence(String ajbSequence) {
	this.ajbSequence = ajbSequence;
  }
  public String getTrackData() {
	return trackData;
  }

  public void setTrackData(String trackData) {
	this.trackData = trackData;
  }
  //Ends here 12-SEP-2016
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
    return (((AJBValidation)validationRequest).getDueBillIssueCardValidationRequest(this, store, terminal, isRefundPaymentRequired, isManualOverride));
  }
  
//Vivek Mishra : Added to capture AJB response auth code and error message 11-NOV-2016
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
//Ends here 11-NOV-2016 
  
}
