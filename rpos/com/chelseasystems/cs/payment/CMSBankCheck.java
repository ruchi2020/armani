/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.payment.*;


/**
 * <p>Title: CMSBankCheck</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class CMSBankCheck extends BankCheck {

  /**
   * Default Constructor
   */

  //Vivek Mishra : Added for capturing the unique AJBSequesnce number which goes with every request as Invoice number
	
  private String ajbSequence;		
  
//Vivek Mishra : Added for Check entry mode
  protected String luNmbChkSwpKy = " ";
//ends
	
  public CMSBankCheck() {
    this(null);
  }

  /**
   * Constructor
   * @param transactionPaymentName a key representing this payment
   */
  public CMSBankCheck(String transactionPaymentName) {
    super(transactionPaymentName);
  }

  /**
   *
   * @param responce Object
   * @return String
   */
  public String setCreditAuthorization(Object response) {
    if (response instanceof String)
      return super.setCreditAuthorization((String)response); //call to super.setCreditAuthorization(String)
    else
      validationRequest.setCheckAuthorization(response, this);
    return respStatusCode;
  }
  //Added by sonali for displaying Status of authorization
  public String getRespStatusCodeDesc()
  {
    if (this.amount.getDoubleValue() < 0.0D)
    {
      return "";
    }

    if (this.respStatusCode.equals("0"))
    {
      this.respStatusCodeDesc = this.respAuthorizationCode;
    }
  //Vivek Mishra : Commented the code to restrict sending 100 request to Fipay requested by Jason on 02-NOV-2015. This code needs to be uncommented if they need request in future.
    /*else if (this.respStatusCode.equals("1"))
    {
      this.respStatusCodeDesc = new String("DECLINED");
    }
    else if (this.respStatusCode.equals("2"))
    {
      this.respStatusCodeDesc = PaymentMgr.getDefaultCallCenterDisplay(getTransactionPaymentName());
    }
//modified by Sonali to display phone num on bank down
    //AJB cant connect to bank and shows declined
    else if (this.respStatusCode.equals("3"))
    {
    	 this.respStatusCodeDesc =  new String("DECLINED");
    }

    else if (this.respStatusCode.equals("5"))
    {
    	 this.respStatusCodeDesc = new String("LINE ISSUE");
    }
    else if (this.respStatusCode.equals("6"))
    {
    	 this.respStatusCodeDesc = new String("FORMATTING ISSUE");
    }
    else if (this.respStatusCode.equals("8"))
    {
    	 this.respStatusCodeDesc = new String("TRY AGAIN");
    }
    else if (this.respStatusCode.equals("10"))
    {
    	 this.respStatusCodeDesc = new String("TIME OUT");
    }
    else if (this.respStatusCode.equals("14"))
    {
    	 this.respStatusCodeDesc = new String("REQUEST NOT SUPPORTED");
    }//Anjana added to not show phone number for chekc until amount due is zero
    //Anjana placed back the phone number code as we manually authorize first before going ahead with second transaction*/
    else 
    {
      this.respStatusCodeDesc = PaymentMgr.getDefaultCallCenterDisplay(getTransactionPaymentName());
    }  

    return this.respStatusCodeDesc;
  }
  
  //Anjana added to show the manual override value in payment screen 
  public void setManualOverride(String approvalCode)
  {
      manualOverride = true;
      respStatusCode = "0";
      respStatusCodeDesc = approvalCode;
      respAuthorizationCode = approvalCode;
      authRequired = false;
  }

//Vivek Mishra : Added for capturing the unique AJBSequesnce number which goes with every request as Invoice number
	public String getAjbSequence() {
		return ajbSequence;
	}

	public void setAjbSequence(String ajbSequence) {
		this.ajbSequence = ajbSequence;
	}
//Ends	

	//Vivek Mishra : Added for Check entry mode
	public String getLuNmbChkSwpKy() {
		return luNmbChkSwpKy;
	}

	public void setLuNmbChkSwpKy(String luNmbChkSwpKy) {
		this.luNmbChkSwpKy = luNmbChkSwpKy;
	}
	//Ends
	
}
