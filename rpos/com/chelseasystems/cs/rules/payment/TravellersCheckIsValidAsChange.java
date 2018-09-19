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
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.paidout.CMSPaidOutTransaction;


/**
 * <p>Title: TravellersCheckIsValidAsChange</p>
 * <p>Description: This business rule check that Travellers checks are not valid
 * as a form of change payments. </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Anand
 * @version 1.0
 */
public class TravellersCheckIsValidAsChange extends Rule {

  /**
   * put your documentation comment here
   */
  public TravellersCheckIsValidAsChange() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of TravellersCheck
   * @param args - instance of PaymentTransaction
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((TravellersCheck)theParent, (PaymentTransaction)args[0]);
  }

  /**
   * Execute the Rule
   * @param theParent - instance of TravellersCheck
   * @param args - instance of PaymentTransaction
   */
  private RulesInfo execute(TravellersCheck travellerscheck, PaymentTransaction paymenttransaction) {
    try {
      if (paymenttransaction instanceof CMSPaidOutTransaction) {
        if (((CMSPaidOutTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS")).getType().equals(
            "MISC_PAID_OUT")) {
          return new RulesInfo();
        }
        else {
          return new RulesInfo(CMSApplet.res.getString(
            "Travellers checks are not valid as a form of change payments."));
        }
      } else
        return new RulesInfo(CMSApplet.res.getString(
            "Travellers checks are not valid as a form of change payments."));
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
    return "Travellers Check is valid as change payment";
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    StringBuffer buf = new StringBuffer();
    buf.append(CMSApplet.res.getString(
        "Rule should determine whether a Travellers check is valid a change payment."));
    return buf.toString();
  }
}

