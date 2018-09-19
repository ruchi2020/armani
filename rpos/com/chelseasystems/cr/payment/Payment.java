/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cr.payment;

import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.business.*;
import com.chelseasystems.cs.payment.CMSPaymentMgr;


/**
 *  Abstract class for all payments.
 *  @author John Gray
 *  @version 1.0a
 */
public abstract class Payment extends BusinessObject implements java.io.Serializable {
  private static final long serialVersionUID = -7474660546021921984L;

  /** Status of the authorization response.   01 = approved   **/
  public static final String APPROVED = "01";

  /** Status of the authorization response.   02 = declined   **/
  public static final String DECLINED = "02";

  /** Status of the authorization response.   03 = referral/warning  **/
  public static final String CALL_CENTER = "03";

  /** Status of the authorization response.   04 = timeout  **/
  public static final String TIMEOUT = "04";

  /** Status of the authorization response.   05 = system failure  **/
  public static final String FAILURE = "05";

  /** PCR 1940 Status of the authorization response.   10 = partial auth  **/
  public static final String PARTIAL_AUTH = "10";
  
  /**  currency object that contains the amount of the payment **/
  protected ArmCurrency amount;

  /**  Flag to check for payments that require approval **/
  //protected boolean isApproved = true;

  /** this is the key that represents this payment to the transaction */
  private String transactionPaymentName;

  /** this is the description that will be presented to the Eu */
  private String guiPaymentName;

  /** ks: Payment code for EU **/
  private String paymentCode;
  /**
   * Newly Added Fields to the Payment Object
   * Journal Key
   * Message Number
   * Merchant ID.
   */
  /** This is the Field that represents the extra fields
   */
  private String journalKey;
  private String messageNumber;
  private String merchantID;
  private String respMessage;
  private ArmCurrency maxChangeAllowed;
  private Object respObject;
  
  /** Poonam: this is the payment type that will be presented to the Eu */
  private String guiPaymentType;
  //Poonam:Ends here

  /**
   * Default Constructor.  Any client wishing to instantiate a payment is
   * responsible for setting the <code>transactionPaymentName</code> and
   * the <code>guiPaymentName</code>
   */
  protected Payment() {
  }
  //Vishal Yevale : Added for saving CREDIT_CARD rather than it's Merchant name : 6 March 2017
  private Boolean isUSRegion = true;
  public void setRegion(Boolean b){
	  isUSRegion = b;
  }
  
  public Boolean isUSRegion(){
	  return isUSRegion;
  }
  //end Vishal yevale : 6 march 2017
  /**
   * @param transactionPaymentName a key representing this payment
   */
  public Payment(String transactionPaymentName) {
    this.setTransactionPaymentName(transactionPaymentName);
  }

  /**
   * @param transactionPaymentName a key representing this payment
   * bug#29959
   */
  public void setTransactionPaymentName(String transactionPaymentName) {
	 if(transactionPaymentName!=null && (transactionPaymentName.contains("DCRD"))){
		 transactionPaymentName = "DEBIT_CARD";
	 }
    this.transactionPaymentName = transactionPaymentName;
  }

  /**
   *
   * @param journalKey String
   */
  public void setJournalKey(String journalKey) {
    this.journalKey = journalKey;
  }

  /**
   *
   * @param journalKey String
   */
  public String getJournalKey() {
    return journalKey;
  }

  /**
   *
   * @param Message Number  String
   */
  public void setMessageNumber(String messageNumber) {
    this.messageNumber = messageNumber;
  }

  /**
   *
   * @param messageNumber String
   */
  public String getMessageNumber() {
    return messageNumber;
  }

  /**
   *
   * @param merchantID String
   */
  public void setMerchantID(String merchantID) {
    this.merchantID = merchantID;
  }

  /**
   *
   * @param merchantID String
   */
  public String getMerchantID() {
    return merchantID;
  }

  /**
   *
   * @param respMessage String
   */
  public void setRespMessage(String respMessage) {
    this.respMessage = respMessage;
  }

  /**
   *
   * @return String
   */
  public String getRespMessage() {
    return respMessage;
  }

  /**
   * @param guiPaymentName a description of this payment.  This will be resourced
   *    by the gui.
   */
  public void setGUIPaymentName(String guiPaymentName) {
	  
  	   this.guiPaymentName = guiPaymentName;
  }

  /**
   * This used to indicate that a particular payment TYPE (e.g. Cash, CreditCard
   * MoneyOrder) required authorization.  It now indicates whether or not a particular
   * INSTANCE of any payment still needs to be sent to the authorizor for approval.
   * In order to determine whether or not a payment TYPE requires authorization as
   * a rule, check to see if it is an "instanceof IAuthRequired".   lar
   * @return true if an approval has not yet been received for this payment instance.
   */
  public abstract boolean isAuthRequired();

  /**
   *
   * Default value for all payments, to be overriden by payments that actually require authorization
   */
  public String getRespStatusCode() {
    return APPROVED;
  }

  /**
   * Tests if the payment is valid as change given back to the customer on
   * an overpayment.
   * @return true if the payment can be returned as change to the customer
   */
  public void isValidAsChange(PaymentTransaction theTxn)
      throws BusinessRuleException {
    RuleEngine.execute(this.getClass().getName(), "isValidAsChange", this, new Object[] {theTxn
    });
  }

  /**
   * Tests if the payment can be given by the customer.
   * @return true if the payment can be given by the customer
   */
  public void isValidAsPayment(PaymentTransaction theTxn)
      throws BusinessRuleException {
    RuleEngine.execute(this.getClass().getName(), "isValidAsPayment", this, new Object[] {theTxn
    });
  }

  /**
   * Tests if the payment is valid as a refund on a customer return transaction
   *    transaction.
   * @return true if the payment can be given to the customer as a refund on
   * a return transaction
   */
  public void isValidAsRefund(PaymentTransaction theTxn)
      throws BusinessRuleException {
    RuleEngine.execute(this.getClass().getName(), "isValidAsRefund", this, new Object[] {theTxn
    });
  }

  /**
   * Tests if the payment requires the customer to sign a receipt.
   * @return true if the payment requires the customer to sign a receipt
   */
  public abstract boolean isSignatureRequired();

  /**
   * Tests if the payment is valid for more than the current amount due.
   * @return true if the payment is valid for more than the current amount due
   */
  public abstract boolean isOverPaymentAllowed();

  /**
   * Tests if the payment requires the cash register to print on the back of
   *    the document submitted for payment.
   * @return true if the payment requires the cash register to print on the
   * back of the document submitted for payment
   */
  public abstract boolean isFrankingRequired();

  /**
   * Returns <code>true</code> if this Payment object is designated foreign
   *    by having a ISO 4217 currency type key and an underscore prepended to its
   *    <code>transactionPaymentName</code>. Ex. ITL_CASH
   * @return boolean <code>true</code> if this Payment object is considered foreign
   *    to this system.
   */
  public boolean isForeign() {
    if (getTransactionPaymentName() != null && getTransactionPaymentName().length() > 3) {
      String currencyCode = getTransactionPaymentName().substring(0, 4);
      if (currencyCode.charAt(3) == '_') {
        currencyCode = currencyCode.substring(0, 3);
        try {
          return (!CurrencyType.getCurrencyType(currencyCode).equals(ArmCurrency.getBaseCurrencyType()));
        } catch (UnsupportedCurrencyTypeException ucte) {
          //System.err.println("Payment.isForeign()" + ucte);
        }
      }
    }
    return (false);
  }

  /**
   * Description of this payment.  This will be resourced by the gui.  This
   *    will lazily initialize this value if it is null when accessed.
   * @return the name on the screen
   */
  public String getGUIPaymentName() {
    if (guiPaymentName == null) {
      guiPaymentName = PaymentMgr.getPaymentDescriptionKey(getTransactionPaymentName());
    }
 //   guiPaymentName = PaymentMgr.getPaymentDescriptionKey(getTransactionPaymentName());
     if((guiPaymentName == null)||("CREDIT_CARD".equalsIgnoreCase(guiPaymentName)) 
    		  ||("Credit Card".equalsIgnoreCase(guiPaymentName))) {
  	  //Vishal Yevale : Added for saving CREDIT_CARD rather than it's Merchant name : 6 March 2017
    		if(!isUSRegion && "CREDIT_CARD".equalsIgnoreCase(guiPaymentName)){
    			return guiPaymentName;
    		}
    		//end Vishal Yevale 
    	 	return getGUIPaymentNameForDulicateReceipt();
    }
    return (guiPaymentName);
  }
 
    
  //Fix for issue #1881 For Duplicate receipt issue  

  public String getGUIPaymentNameForDulicateReceipt() {
  	String paymentDescByCode = CMSPaymentMgr.getPaymentDescByCode(paymentCode);
  	if ((paymentDescByCode == null)||("".equals(paymentDescByCode.trim()))){
  	   return "Credit Card";
  	}else
  		return paymentDescByCode;
	}


	/**
   * Returns the name in the server transaction record.
   * @return the name in the server transaction record
   */
  public String getTransactionPaymentName() {
    return (transactionPaymentName);
  }

  /**
   * Returns the detailed payment information formatted for display
   * eg, credit card number, driver's license number, etc, as appropriate to the
   * extending class.
   */
  public final String getGUIPaymentDetail() {
	 return PaymentMgr.getRendererClassForPayment(this).getGUIPaymentDetail();
  }

  /**
   * Returns the masked payment information formatted for display
   * eg, credit card number, driver's license number, etc, as appropriate to the
   * extending class.  Default is to return an empty String.
   */
  public final String getMaskedPaymentDetail() {
    return PaymentMgr.getRendererClassForPayment(this).getMaskedPaymentDetail();
  }

  /**
   * @return CurrencyType the first CurrencyType that the amount was.
   */
  public CurrencyType getCurrencyTypeConvertedFrom() {
    ArmCurrency curr = this.getAmount();
    if (curr != null) {
      CurrencyType currencyType = null;
      while (curr.getConvertedFrom() != null) {
        curr = curr.getConvertedFrom();
        currencyType = curr.getCurrencyType();
      }
      return (currencyType);
    }
    return (null);
  }

  /**
   * Returns the money amount (currency).
   * @return the money amount in (currency)
   */
  public ArmCurrency getAmount() {
    return (amount);
  }

  /**
   * Sets the (possibly foreign) money amount.
   * @param unconvertedAmount the (possibly foreign) money amount
   */
  public void setAmount(ArmCurrency amount) {
	   this.amount = amount;
  }

  /**
   * Returns false if the payment has not been approved by the credit authorizor,
   * true otherwise.
   * @return false if the payment has not been approved by the credit authorizor,
   * true otherwise.
   */
  //public boolean isApproved() {return isApproved;}
  /**
   * Set by the credit authorizor return response in the CreditCard or BankCheck class
   * @param approval set to false if the payment has not been approved by the
   * credit authorizor, true otherwise.
   **/
  //protected void setIsApproved(boolean approval) {
  //   this.isApproved = approval;
  //}
  /**
   * This method returns a string containing a credit card
   * validation request.
   * This method should be overridden in classes where
   * authorization is required.
   * @param   store     Store Number
   * @param   terminal  Terminal Number
   * @return            String containing validation request
   */
  public Object getValidationRequest(String store, String terminal) {
    String str = "";
    return (str);
  }

  /**
   * This method sets authorization based upon the
   * authorization responce.
   * This method should be overridden in classes where
   * authorization is required.
   * @param responce  - Contains the authorization responce
   * @return          - String containing the authorizationStatusCode
   */
  public String setCreditAuthorization(Object responce) {
    return (APPROVED);
  }

  /**
   *
   * @return Currency
   */
  public ArmCurrency getMaxChangeAllowed() {
    if (maxChangeAllowed == null) {
      doSetMaxChangeAllowed(CMSPaymentMgr.getMaxChangeAllowed(getTransactionPaymentName()));
    }
    return this.maxChangeAllowed;
  }

  /**
   *
   * @param maxChangeAllowed Currency
   */
  public void setMaxChangeAllowed(ArmCurrency maxChangeAllowed) {
    doSetMaxChangeAllowed(maxChangeAllowed);
  }

  /**
   *
   * @param maxChangeAllowed Currency
   */
  public void doSetMaxChangeAllowed(ArmCurrency maxChangeAllowed) {
    this.maxChangeAllowed = maxChangeAllowed;
  }

  /**
   *
   * @return String
   */
  public String getPaymentCode() {
    return this.paymentCode;
  }

  /**
   *
   * @param paymentCode String
   */
  public void setPaymentCode(String paymentCode) {
    doSetPaymentCode(paymentCode);
  }

  /**
   *
   * @param paymentCode String
   */
  public void doSetPaymentCode(String paymentCode) {
    this.paymentCode = paymentCode;
  }
  
  /**
   * 
   * @return
   * New Payment buttons for Canada
   */
  public String getGUIPaymentNameForMobileTerminal() {
	    if (guiPaymentName == null) {
	      guiPaymentName = PaymentMgr.getPaymentDescriptionKeyforMobileTerminal(getTransactionPaymentName());
	     }
	    if((guiPaymentName == null)||("CREDIT_CARD".equalsIgnoreCase(guiPaymentName)) 
	    		  ||("Credit Card".equalsIgnoreCase(guiPaymentName))) {
	    	 	return getGUIPaymentNameForDulicateReceipt();
	    }
	    return (guiPaymentName);
	  }

  	public void setRespObject(Object respObject) {
	    this.respObject = respObject;
	}

    public Object getRespObject() {
    	return respObject;
    }
  
    protected boolean SAFAble = false;
	public boolean isSAFable(){
		return SAFAble;
	}
	
	public void setSAFable(boolean SAFAble){
		this.SAFAble = SAFAble;
	}

	//Poonam: Added to show actual payment type on screen
	/**
	 * @return the guiPaymentType
	 */
	public String getGuiPaymentType() {
		return guiPaymentType;
	}

	/**
	 * @param guiPaymentType the guiPaymentType to set
	 */
	public void setGuiPaymentType(String guiPaymentType) {
		this.guiPaymentType = guiPaymentType;
	}
	//Poonam: Ends here
}

