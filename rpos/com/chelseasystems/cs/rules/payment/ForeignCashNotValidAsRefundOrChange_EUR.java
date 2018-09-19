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
import com.chelseasystems.cr.paidout.PaidOutTransaction;
import com.chelseasystems.cr.swing.CMSApplet;


/**
 * <p>Title: ForeignCashNotValidAsRefundOrChange</p>
 * <p>Description: This business rule check that Foreign Cash is not a valid
 * form as Refund or Change payment.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Anand
 * @version 1.0
 */
public class ForeignCashNotValidAsRefundOrChange_EUR extends Rule {

  /**
   * put your documentation comment here
   */
  public ForeignCashNotValidAsRefundOrChange_EUR() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of Cash
   * @param args - instance of PaymentTransaction
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return (execute((CMSForeignCash)theParent, (PaymentTransaction)args[0]));
  }

  /**
   * Execute the Rule
   * @param theParent - instance of Cash
   * @param args - instance of PaymentTransaction
   */
  private RulesInfo execute(CMSForeignCash cash, PaymentTransaction paymentTransaction1) {
    try { // MP: No need to show Jap Cash in case of PaidOut
        return new RulesInfo("Foreign Cash not valid as Change OR refund");
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
    return (CMSApplet.res.getString("ForeignCashNotValidAsRefundOrChange"));
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

