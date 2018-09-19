 /*
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.payment;

import com.chelseasystems.cr.rules.*;
import java.util.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.paidout.CMSPaidOutTransaction;


/**
 * <p>Title: ForeignCashNotValidAsRefundOrChange</p>
 * <p>Description: This business rule check that Cash is not a valid
 * form as Refund or Change payment.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Sumit Krishnan
 * @version 1.0
 */
public class CashNotValidAsRefundOrChange_EUR extends Rule {

  /**
   * put your documentation comment here
   */
  public CashNotValidAsRefundOrChange_EUR() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of Cash
   * @param args - instance of PaymentTransaction
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return (execute((Cash)theParent, (PaymentTransaction)args[0]));
  }

  /**
   * Execute the Rule
   * @param theParent - instance of Cash
   * @param args - instance of PaymentTransaction
   */
  private RulesInfo execute(Cash cash, PaymentTransaction paymentTransaction) {
    try {
      if (paymentTransaction instanceof CMSPaidOutTransaction) {
        if (((CMSPaidOutTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS")).
            getType().equals("CLOSE_DEPOSIT")){
          if (cash.isForeign()) {
            return new RulesInfo(CMSApplet.res.getString("NO"));
          } else
            return (new RulesInfo());
        }else{
          return (new RulesInfo());
        }
      } else {
        return (new RulesInfo());
      }
    } catch (Exception ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
          , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
    }
    return (new RulesInfo());
  }

  /**
   * Returns the name of the Rule.
   * @return the name of the rule
   */
  public String getName() {
    return (CMSApplet.res.getString("CashNotValidAsRefundOrChange_EUR"));
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    StringBuffer buf = new StringBuffer();
    buf.append("");
    return (buf.toString());
  }
}

