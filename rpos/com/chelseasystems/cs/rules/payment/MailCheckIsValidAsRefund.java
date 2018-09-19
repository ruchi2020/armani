/* History:-
 +--------+------------+-----------+------------------+--------------------------------+
 | Ver#   |    Date    |   By      |        Defect #  |              Description       |
 +--------+------------+-----------+------------------+--------------------------------+
 | 1.1    | 11-18-2005 | Vikram    |         -        | Modifications per Japan spec   |
 +--------+------------+-----------+------------------+--------------------------------+
 */


package com.chelseasystems.cs.rules.payment;

import com.chelseasystems.cr.rules.*;
import java.util.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cr.paidout.PaidOutTransaction;
import com.chelseasystems.cr.swing.CMSApplet;


/**
 * <p>Title: MailCheckIsValidAsRefund</p>
 * <p>Description: This business rule is to check that mail checks is not valid form
 * of refund payment.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Vikram
 * @version 1.0
 */
public class MailCheckIsValidAsRefund extends Rule {

  /**
   * Default Constructor
   */
  public MailCheckIsValidAsRefund() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of MailCheck
   * @param args - instance of PaymentTransaction
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((MailCheck)theParent, (PaymentTransaction)args[0]);
  }

  /**
   * Execute the Rule
   * @param theParent - instance of MailCheck
   * @param args - instance of PaymentTransaction
   */
  private RulesInfo execute(MailCheck mailCheck, PaymentTransaction paymenttransaction) {
    try {
//      if (paymenttransaction instanceof PaidOutTransaction) {
//        if (((PaidOutTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS")).getType().equals(
//            "CLOSE_DEPOSIT")) {
//          return new RulesInfo();
//        } else
//          return new RulesInfo(CMSApplet.res.getString(
//              "A Mail Check is not a valid form of change payment."));
//      }
      return new RulesInfo("A Mail Check is not a valid form of return payment.");
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
    return CMSApplet.res.getString("Mail Check valid as return");
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    StringBuffer buf = new StringBuffer();
    buf.append(CMSApplet.res.getString("Mail Check valid as return"));
    return buf.toString();
  }
}

