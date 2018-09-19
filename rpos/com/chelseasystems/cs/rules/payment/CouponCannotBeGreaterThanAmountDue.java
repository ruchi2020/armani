/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.payment;

import com.chelseasystems.cr.rules.*;
import java.util.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.swing.CMSAppModelFactory;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cr.swing.CMSApplet;


/**
 * <p>Title: CouponCannotBeGreaterThanAmountDue</p>
 * <p>Description: This business rule checks that Coupon cannot be greater than
 * the total amount due. </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Anand
 * @version 1.0
 */
public class CouponCannotBeGreaterThanAmountDue extends Rule {

  /**
   * put your documentation comment here
   */
  public CouponCannotBeGreaterThanAmountDue() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of CMSCompositePOSTransaction
   * @param args - instance of Payment
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((PaymentTransaction)theParent, (Payment)args[0]);
  }

  /**
   * Execute the Rule
   * @param theParent - instance of CMSCompositePOSTransaction
   * @param args - instance of Payment
   */
  private RulesInfo execute(PaymentTransaction cmscompositepostransaction, Payment payment) {
    try {
    	
    	// Changed by Satin for coupon management PCR.
    	if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
      if (payment instanceof CMSCoupon) {
        if (payment.getAmount().greaterThan(cmscompositepostransaction.getAppModel(
            CMSAppModelFactory.getInstance(), com.chelseasystems.cr.swing.CMSApplet.theAppMgr).
            getCompositeTotalAmountDue().subtract(cmscompositepostransaction.getTotalPaymentAmount())))
          return new RulesInfo("Coupons cannot be greater than the total amount due.");
      }
      }
    	
    	
    	else if (payment instanceof Coupon) {
            if (payment.getAmount().greaterThan(cmscompositepostransaction.getAppModel(
                    CMSAppModelFactory.getInstance(), com.chelseasystems.cr.swing.CMSApplet.theAppMgr).
                    getCompositeTotalAmountDue().subtract(cmscompositepostransaction.getTotalPaymentAmount())))
                  return new RulesInfo("Coupons cannot be greater than the total amount due.");
              }
    } catch (Exception ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
          , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
    }
    return new RulesInfo();
  }

  /**
   * Returns the name of the Rule.
   * @return the name of the rule
   */
  public String getName() {
    return "Coupon Cannot Be Greater Than Amount Due";
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    StringBuffer buf = new StringBuffer();
    buf.append("Coupon cannot be greater than amou");
    buf.append("nt due.  You do not want to give cahnge back from a coupon");
    return buf.toString();
  }
}

