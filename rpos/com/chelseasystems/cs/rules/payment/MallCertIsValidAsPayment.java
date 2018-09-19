/* History:-
 +--------+------------+-----------+------------------+------------------------------+
 | Ver#   |    Date    |   By      |        Defect #  |              Description     |
 +--------+------------+-----------+------------------+------------------------------+
 | 1      | 04-19-2005 | Anand     | Original         | Original Version per spec    |
 |        |            |           |                  | to accomodate paid-in type   |
 +--------+------------+-----------+------------------+------------------------------+
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
import com.chelseasystems.cr.collection.CollectionTransaction;
import com.chelseasystems.cs.collection.CMSMiscCollection;


/**
 * <p>Title: MallCertIsValidAsPayment</p>
 * <p>Description: This business rule check that Gift certificate is valid
 * as Payment.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Anand
 * @version 1.0
 */
public class MallCertIsValidAsPayment extends Rule {

  /**
   * Default Constructor
   */
  public MallCertIsValidAsPayment() {
  }

  /**
   * Execute the rule.
   *
   * @param object theParent - instance of CMSMenuOption
   * @param args - instance of Employee
   * @param args - instance of Store
   * @return RulesInfo
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((MallCert)theParent, (PaymentTransaction)args[0]);
  }

  /**
   * Execute the Rule
   * @param theParent - instance of MailCheck
   * @param args - instance of PaymentTransaction
   */
  private RulesInfo execute(MallCert cash, PaymentTransaction paymentTransaction) {
    try {
      if (paymentTransaction instanceof CollectionTransaction) {
        if (((CollectionTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS")).getType().equals(
            "NFS_CHECK_PAYMENT")
            || ((CollectionTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS")).getType().
            equals("HOUSE_ACCOUNT_PAYMENT"))
          return new RulesInfo(CMSApplet.res.getString("should NOT be enabled"));
      }
    } catch (Exception ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
          , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
    }
    return new RulesInfo();
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    return (CMSApplet.res.getString("Suppress menu button if not appropriate."));
  }

  /**
   * Return the name of the rule.
   * @return name of the rule.
   */
  public String getName() {
    return CMSApplet.res.getString("SuppressOrShowMallCertMenuButton");
  }
}

