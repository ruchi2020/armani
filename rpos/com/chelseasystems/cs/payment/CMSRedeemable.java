/*
 History:
 +------+------------+-----------+-----------+-------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                     |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 2    | 04-28-2005 |Khayti     |           | Redeemable Management                                               |
 -----------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.rules.IRuleEngine;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cr.currency.ArmCurrency;


// Referenced classes of package com.chelseasystems.cr.payment:
//            DueBill
/**
 *
 * <p>Title: CMSRedeemable</p>
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
public class CMSRedeemable extends Redeemable implements IRuleEngine {
  private String controlNum;
  private String customerId;
  private boolean isManual = false;
  private String manualAuthCode = null;
  private ArmCurrency balance = null;
  //Vivke Mishra : Added to capture track data from AJB 107 response 27-SEP-2016
  private String trackData = null;
  //Vivek Mishra : Added for unique AJB sequence 27-SEP-2016
  private String ajbSequence = null;

  /**
   * Default Constructor
   */
  public CMSRedeemable() {
    this(null);
    customerId = "";
    isManual = false;
    manualAuthCode = "";
  }

  /**
   * Constructor
   * @param transactionPaymentName String
   */
  public CMSRedeemable(String transactionPaymentName) {
    super(transactionPaymentName);
    customerId = "";
    isManual = false;
    manualAuthCode = "";
  }

  /**
   * This method is used to check whether franking is required
   * @return boolean
   */
  public boolean isFrankingRequired() {
    return false;
  }

  /**
   * This method is used to check whether over payment is allowed
   * @return boolean
   */
  public boolean isOverPaymentAllowed() {
    return true;
  }

  /**
   * This method is used to check whether signature is required
   * @return boolean
   */
  public boolean isSignatureRequired() {
    return false;
  }

  /**
   * This method is used to check whether authorisation is required
   * @return boolean
   */
  public boolean isAuthRequired() {
    return false;
  }

  /**
   * Method is used to get control number
   * @return String
   */
  public String getControlNum() {
    return controlNum;
  }

  /**
   * Method is used to set control number
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
   * Method is used to get customer id
   * @return String
   */
  public String getCustomerId() {
    return this.customerId;
  }

  /**
   * Method is used to set customer id
   * @param customerId String
   */
  public void setCustomerId(String customerId) {
    doSetCustomerId(customerId);
  }

  /**
   * Method is used to set customer id
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
   * Method is used to set manual authorization code
   * @return boolean
   */
  public boolean getIsManual() {
    return this.isManual;
  }

  /**
   * Method is used to get manual authorization code
   * @return String
   */
  public String getManualAuthCode() {
    return this.manualAuthCode;
  }

  /**
   * Method is used to set manual authorization code
   * @param manualAuthCode String
   */
  public void setManualAuthCode(String manualAuthCode) {
    doSetManualAuthCode(manualAuthCode);
  }

  /**
   * Method is used to set manual authorization code
   * @param manualAuthCode String
   */
  public void doSetManualAuthCode(String manualAuthCode) {
    this.manualAuthCode = manualAuthCode;
  }

  /**
   *
   * @return Currency
   */
  public ArmCurrency getBalance() {
    return this.balance;
  }

  /**
   *
   * @param balance Currency
   */
  public void doSetBalance(ArmCurrency balance) {
    this.setAmount(balance);
  }

  /**
   *
   * @param balance Currency
   */
  public void setBalance(ArmCurrency balance) {
    this.balance = balance;
  }

  //Vivke Mishra : Added to capture track data from AJB 107 response 27-SEP-2016
  public String getTrackData() {
	return trackData;
  }

  public void setTrackData(String trackData) {
	this.trackData = trackData;
  }
  //Ends here 27-SEP-2016
  
  //Vivek Mishra : Added for unique AJB sequence 27-SEP-2016
  public String getAjbSequence() {
	return ajbSequence;
  }

  public void setAjbSequence(String ajbSequence) {
	this.ajbSequence = ajbSequence;
  }
  //Ends here 27-SEP-2016
  
}
