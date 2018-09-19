/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.payment.BankCheck;
import com.chelseasystems.cr.payment.CreditCard;
import com.chelseasystems.cr.payment.Payment;


/**
 */
public class CMSPaymentAppModel implements java.io.Serializable {
  static final long serialVersionUID = 4226671535815953973L;

  /**
   * put your documentation comment here
   * @param payments
   * @return
   */
  public static CMSPaymentAppModel[] getPaymentModelArray(Payment[] payments) {
    if (payments == null || payments.length == 0) {
      return new CMSPaymentAppModel[0];
    }
    List list = new ArrayList();
    for (int i = 0; i < payments.length; i++) {
      if (payments[i] != null) {
        list.add(new CMSPaymentAppModel(payments[i]));
      }
    }
    return (CMSPaymentAppModel[])list.toArray(new CMSPaymentAppModel[0]);
  }

  private Payment payment = null;

  /**
   * put your documentation comment here
   * @param   Payment payment
   */
  public CMSPaymentAppModel(Payment payment) {
    this.payment = payment;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Payment getPayment() {
    return this.payment;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getAuthCode() {
    String authCode = null;
    if (this.payment instanceof CreditCard) {
      authCode = ((CreditCard)this.payment).getRespAuthorizationCode();
    } else if (this.payment instanceof BankCheck) {
      authCode = ((BankCheck)this.payment).getRespAuthorizationCode();
    }
    if (authCode != null && authCode.length() > 0) {
      return authCode;
    } else {
      return null;
    }
  }

  /**
   * Get Payment Plan code.
   * @return String
   */
  public String getPaymentPlanDescription() {
    if (this.getPayment() instanceof CreditCard) {
      String sGUIPlanDesc = getGUICardPlanDesc((CreditCard)this.getPayment());
      if (sGUIPlanDesc != null && sGUIPlanDesc.length() > 0
          &&
          sGUIPlanDesc.indexOf("null") == -1
          )
        return sGUIPlanDesc;
    }
    return "";
  }

  private String getGUICardPlanDesc(CreditCard cCard) {
		if (cCard.getCardPlanCode() != null) {
			ConfigMgr config = new ConfigMgr("ArmCreditCardPlans.cfg");
			String sCreditCardPlan = "CREDIT_CARD_" + cCard.getPaymentCode() + "_CARD_PLAN_"
				+ cCard.getCardPlanCode();
			String strCardPlanDesc = config.getString(sCreditCardPlan + ".LABEL");
			return strCardPlanDesc;
		} else {
			return "";
		}
	}
  
  public String getPaymentPlanCode() {
    if (this.getPayment() instanceof CreditCard) {
      String sPlanCode = ( (CreditCard)this.getPayment()).getCardPlanCode();
      if (sPlanCode != null && sPlanCode.length() > 0
          &&
          sPlanCode.indexOf("null") == -1
          )
        return sPlanCode;
    }
    return "";
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String description() {
    String s = "";

    if (this.payment.getGUIPaymentName() != null) {
      s += (this.payment.getGUIPaymentName() + " ");
    }
    if (this.payment.getMaskedPaymentDetail() != null) {
      s += this.payment.getMaskedPaymentDetail();
    }
    return s;
  }
  
  //Vivek Mishra : Added to show customer signature on receipt
  //Vivek Mishra : Commented the code as signature display is not required on receipt as per Armani.
  /**
   * put your documentation comment here
   * @return
   */
  /*public ImageIcon getSignature() {
    ImageIcon signature = null;
    if (this.payment instanceof CreditCard) {
    	signature = ((CreditCard)this.payment).getSignature();
    }
      return signature;
  }*/

//Vishal Yevale 31 Aug 2016 = added to display EMV Fields on Receipt.
  public String getAID(){
	  String AID=null;
	    if (this.payment instanceof CreditCard) {	
	    	AID= ((CreditCard)this.payment).getAID();
	    }

	    if(AID==null){
	    	return null;
	    }else{
	    	return AID;
	    }
  }
//Vishal Yevale 31 Aug 2016 = added to display EMV Fields on Receipt.

  public String getTVR(){
	  String TVR=null;
	    if (this.payment instanceof CreditCard) {	
	    	TVR= ((CreditCard)this.payment).getTVR();
	    }

	    if(TVR==null){
	    	return null;
	    }else{
	    	return TVR;
	    }
  }
//Vishal Yevale 31 Aug 2016 = added to display EMV Fields on Receipt.

  public String getIAD(){
	  String IAD=null;
	    if (this.payment instanceof CreditCard) {	
	    	IAD= ((CreditCard)this.payment).getIAD();
	    }

	    if(IAD==null){
	    	return null;
	    }else{
	    	return IAD;
	    }
  }
//Vishal Yevale 31 Aug 2016 = added to display EMV Fields on Receipt.

  public String getTSI(){
	  String TSI=null;
	    if (this.payment instanceof CreditCard) {	
	    	TSI= ((CreditCard)this.payment).getTSI();
	    }

	    if(TSI==null){
	    	return null;
	    }else{
	    	return TSI;
	    }
  }
//Vishal Yevale 31 Aug 2016 = added to display EMV Fields on Receipt.

  public String getARC(){
	  String ARC=null;
	    if (this.payment instanceof CreditCard) {	
	    	ARC= ((CreditCard)this.payment).getARC();
	    }

	    if(ARC==null){
	    	return null;
	    }else{
	    	return ARC;
	    }
  }
  
  public String getCVM(){
	  String CVM=null;
	    if (this.payment instanceof CreditCard) {	
	    	CVM= ((CreditCard)this.payment).getCVM();
	    }

	    if(CVM==null){
	    	return null;
	    }else{
	    	return CVM;
	    }
  }
//END ** Vishal Yevale 31 Aug 2016 = added to display EMV Fields on Receipt.
 
  //Anjana added this to print Entry mode in receipt
  
  /**
   * put your documentation comment here
   * @return
   */
  public String getEntryMode() {
    String entryMode = null;
 //   if (this.payment instanceof CreditCard || this.payment instanceof CMSBankCheck) {
    if (this.payment instanceof CreditCard) {	
    	//if(((((CreditCard)this.payment).getCreditCardHolderName())==null) || ((((CreditCard)this.payment).getCreditCardHolderName()).equals(""))){
    	    entryMode = ((CreditCard)this.payment).getLuNmbCrdSwpKy();
    	  //Vivek Mishra : Added to fix null entry mode issue
    	    if(entryMode==null){
    	    	return null;
    	    }
    	//Ends
    	//Vivek Mishra : Added for Check entry mode
    /*	else if(this.payment instanceof CMSBankCheck)
    		entryMode = ((CMSBankCheck)this.payment).getLuNmbChkSwpKy();*/
        //Ends
    if(entryMode.contains("CEM_Manual")){
    	entryMode = "Keyed";
    }
    // Vishal Yevale : 8 Sept 2016 added for entry mode for Fallback Emv
    else if(entryMode.contains("CEM_Swipe") && entryMode.toUpperCase().contains("FALLBACK")){
    	entryMode = "Chip/Swipe";
    }
    // Vishal Yevale End: 8 Sept 2016 added for entry mode for Fallback Emv

    else if(entryMode.contains("CEM_Swipe")){
    	entryMode = "Swiped";
    }
    //mayuri edhara : 18 Oct 2016 modified entry mode to check uppercase
    else if(entryMode.toUpperCase().contains("CONTACTLESS")){
    	entryMode = "Contactless";
    }
    else if(entryMode.contains("CEM_Insert")){
    	entryMode = "Chip";
    }
    else if(entryMode.contains("Mobile")){
    	entryMode = "Mobile";
    }
    else	
    	entryMode = "Keyed";
    }
    if (entryMode != null && entryMode.length() > 0) {
      return entryMode;
    } else {
      return null;
    }
  }

}
