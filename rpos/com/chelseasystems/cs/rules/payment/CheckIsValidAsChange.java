/* History:-
 +--------+------------+-----------+------------------+--------------------------------+
 | Ver#   |    Date    |   By      |        Defect #  |              Description       |
 +--------+------------+-----------+------------------+--------------------------------+
 | 1.1     | 04-23-2005 | Anand     |         -        | Modifications per Paid-out spec|
 +--------+------------+-----------+------------------+--------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.payment;

import com.chelseasystems.cr.rules.*;
import java.util.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.paidout.PaidOutTransaction;
import com.chelseasystems.cr.swing.CMSApplet;


/**
 * <p>Title: CheckIsValidAsChange</p>
 * <p>Description: This business rule checks that Check is not a valid form of
 * change Payment </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Anand
 * @version 1.0
 */
public class CheckIsValidAsChange extends Rule {

  /**Default constructor
   */
  public CheckIsValidAsChange() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of DueBill
   * @param args - instance of PaymentTransaction
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((BankCheck)theParent, (PaymentTransaction)args[0]);
  }

  /**
   * Execute the Rule
   * @param theParent - instance of DueBill
   * @param args - instance of PaymentTransaction
   */
  private RulesInfo execute(BankCheck duebillIssue, PaymentTransaction paymenttransaction) {
    try {
      if (paymenttransaction instanceof PaidOutTransaction) {
        if (!((PaidOutTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS")).getType().equals(
            "CASH_TRANSFER")) {
          return new RulesInfo();
        } else
          return new RulesInfo("Credit memo is not a valid form of change payment.");
      }
      return new RulesInfo("Credit memo is not a valid form of change payment.");
    } catch (Exception ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
          , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
      return new RulesInfo();
    }
  }

  /**
   * Returns the name of the Rule.
   * @return the name of the rule
   */
  public String getName() {
    return "credit memo is valid as change payment";
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    StringBuffer buf = new StringBuffer();
    buf.append("Determines whether a store credit is allowed as change on this transaction.");
    return buf.toString();
  }
}

