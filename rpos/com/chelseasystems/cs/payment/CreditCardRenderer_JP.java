

package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cr.swing.CMSApplet;


public class CreditCardRenderer_JP extends PaymentRenderer {


  public CreditCardRenderer_JP() {
  }


  public String getGUIPaymentDetail() {
    String sTmp="";
    String sGUIPlanDesc = getGUICardPlanDesc((CreditCard)this.getPayment());
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    if(sGUIPlanDesc!=null && sGUIPlanDesc.length()>0
       &&
       sGUIPlanDesc.indexOf("null")==-1
       )
    {
      //sTmp = res.getString("Plan")+": "+ sGUIPlanDesc;
      sTmp = sGUIPlanDesc;
    }
    return sTmp;
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

  /**
   * @return
   */
  public String getMaskedPaymentDetail() {
    return getGUIPaymentDetail();
  }
}
