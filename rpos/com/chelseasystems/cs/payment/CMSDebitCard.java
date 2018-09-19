/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.payment.DebitCard;
import com.chelseasystems.cs.ajbauthorization.AJBValidation;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class CMSDebitCard extends DebitCard {
  private String issueNumber = null;

  /**
   *
   * @param transactionPaymentName String
   */
  public CMSDebitCard(String transactionPaymentName) {
    super(transactionPaymentName);
  }

  /**
   *
   */
  public CMSDebitCard() {
    super();
  }

  /**
   *
   * @return String
   */
  public String getIssueNumber() {
    return this.issueNumber;
  }

  /**
   *
   * @param issueNumber String
   */
  public void setIssueNumber(String issueNumber) {
    doSetIssueNumber(issueNumber);
  }

  /**
   *
   * @param issueNumber int
   */
  public void setIssueNumber(int issueNumber) {
    try {
      setIssueNumber(new Integer(issueNumber).toString());
    } catch (Exception ex) {
      setIssueNumber("");
    }
  }

  /**
   *
   * @param issueNumber String
   */
  public void doSetIssueNumber(String issueNumber) {
    this.issueNumber = issueNumber;
  }
  
  //Vivek Mishra : Added for generating AJB validation request
  
  public Object getValidationRequest(String store, String terminal, boolean isRefundPaymentRequired) {
	    // If at all we need to make changes to the what type of method is
	    // called, based on the type of the cc, amex or any other card...
	    // call the method from here.
	    return (((AJBValidation)validationRequest).getDebitCardValidationRequest(this, store, terminal, isRefundPaymentRequired));
	  }
  //End
}
