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
import com.chelseasystems.cs.payment.HouseAccount;
import com.chelseasystems.cr.paidout.PaidOutTransaction;
import com.chelseasystems.cr.swing.CMSApplet;


/**
 * <p>Title: HouseAccountIsValidAsChange</p>
 * <p>Description: This business rule check that house account is not a valid
 * form of change payment.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Anand
 * @version 1.0
 */
public class HouseAccountIsValidAsChange extends Rule {

  /**
   * Default Constructor
   */
  public HouseAccountIsValidAsChange() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of HouseAccount
   * @param args - instance of PaymentTransaction
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((HouseAccount)theParent, (PaymentTransaction)args[0]);
  }

  /**
   * Execute the Rule
   * @param theParent - instance of HouseAccount
   * @param args - instance of PaymentTransaction
   */
  private RulesInfo execute(HouseAccount houseAccount, PaymentTransaction paymenttransaction) {
    try {
      if (paymenttransaction instanceof PaidOutTransaction) {
        if (!((PaidOutTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS")).getType().equals(
            "CASH_TRANSFER")) {
          return new RulesInfo();
        } else
          return new RulesInfo(CMSApplet.res.getString(
              "A House Account is not a valid form of change payment."));
      }
      return new RulesInfo(CMSApplet.res.getString(
          "A House Account is not a valid form of change payment."));
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
    return CMSApplet.res.getString("House is valid as change payment");
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    StringBuffer buf = new StringBuffer();
    buf.append(CMSApplet.res.getString(
        "This rule determines whether a House Account is valid as a change payment."));
    return buf.toString();
  }
}

