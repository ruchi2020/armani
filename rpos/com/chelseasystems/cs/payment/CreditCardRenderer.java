/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.util.CreditAuthUtil;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class CreditCardRenderer extends PaymentRenderer {

  /*
   StringBuffer buf = new StringBuffer();
   if(getAmount().isConverted())
   {
   buf.append(getAmount().formatForeignCurrency());
   }
   return(buf.toString());
   */
  public CreditCardRenderer() {
  }

  /**
   * @return
   */
  public String getGUIPaymentDetail() {
	  //sonali:Added for using masked card num for card on file
	  if(((CreditCard)this.getPayment()).getAccountNumber().equals("")){
		  return ((CreditCard)this.getPayment()).getMaskCardNum();
		  }else
    return ("Act: "
        + CreditAuthUtil.maskCreditCardNo(((CreditCard)this.getPayment()).getAccountNumber()));
  }

  /**
   * @return
   */
  public String getMaskedPaymentDetail() {
    StringBuffer rtnVal = new StringBuffer();
    for (int idx = 0; idx < ((CreditCard)this.getPayment()).getAccountNumber().length() - 4; idx++) {
      rtnVal.append("x");
    }
//Merged from Europe
    //Sergio
    String appo = ((CreditCard)this.getPayment()).getAccountNumber();
    int l = appo.length();
    if (l - 4 <= 0)
      rtnVal.append(appo);
    else
    rtnVal.append(((CreditCard)this.getPayment()).getAccountNumber().substring(((CreditCard)this.
        getPayment()).getAccountNumber().length() - 4));
    return (rtnVal.toString());
  }
}
