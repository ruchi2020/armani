/*
 History:
 +------+------------+-----------+-----------+-------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                     |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 1    | 04-28-2005 |Khayti     |           | Redeemable Management                                                |
 -----------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.rules.IRuleEngine;
import com.chelseasystems.cr.authorization.PaymentValidationRequests;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.payment.DueBill;
import com.chelseasystems.cr.payment.PaymentMgr;
import com.chelseasystems.cs.ajbauthorization.AJBValidation;


/**
 *
 * <p>Title: CMSDueBill</p>
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
public class CMSDueBill extends DueBill {
  private String manualAuthCode = null;
  private String customerId = null;
  private boolean isManual = false;
  private boolean status = true;
  
//Vivek Mishra : Added for track data from 107 AJB response 12-SEP-2016
  private String trackData;
//Vivek Mishra : Added for unique AJB sequence 13-SEP-2016
  private String ajbSequence;
  // Vishal Yevale 3 oct 2016 : added for manual authorization payment Giftcard Tender 
  private String respStatusCode="";
  private static ConfigMgr storeConfig;
  private static String gcInt_flag;
  private boolean authRequired;
  protected boolean partialAuth = false;
  protected String respStatusCodeDesc = new String("CALL CENTER");
//Vivek Mishra : Added to capture AJB response auth code and error message 11-NOV-2016
  protected String respAuthorizationCode = "";
  private String errordiscription; 
  //Ends here 11-NOV-2016
  public void setRespStatusCode(String respStatusCode) {
		this.respStatusCode = respStatusCode;
	}
  
  public String getRespStatusCode() {
		return respStatusCode;
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
   * put your documentation comment here
   * @param   String transactionPaymentName
   */
  public CMSDueBill(String transactionPaymentName) {
    super(transactionPaymentName);
    customerId = "";
    isManual = false;
    manualAuthCode = "";
  }

  /**
   * Method returns customer id
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
   * Method used to set authorization is manual or not
   * @param isManual boolean
   */
  public void setIsManual(boolean isManual) {
    this.isManual = isManual;
  }

  /**
   * Method used to check whether authorization is manual
   * @return boolean
   */
  public boolean getIsManual() {
    return this.isManual;
  }

  /**
   * Method returns manual authorization code
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
	  respStatusCode = "0";
      respStatusCodeDesc = manualAuthCode;
    doSetManualAuthCode(manualAuthCode);
    this.authRequired = false;
  }

  /**
   * Method used to set manual authorization code
   * @param manualAuthCode String
   */
  public void doSetManualAuthCode(String manualAuthCode) {
    this.manualAuthCode = manualAuthCode;
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
    return (((AJBValidation)validationRequest).getDueBillCardValidationRequest(this, store, terminal, isRefundPaymentRequired, isManualOverride));
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
