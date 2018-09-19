/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.payment;

import com.chelseasystems.cr.rules.*;
import java.util.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cr.swing.CMSApplet;


/**
 * <p>Title: DueBillisValidAsPayment</p>
 * <p>Description: This business rule check Store credit is not a valid form
 * of change payment. </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Anand
 * @version 1.0
 */
public class DueBillisValidAsChange extends Rule {

  /**
   * Default Constructor
   */
  public DueBillisValidAsChange() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of DueBill
   * @param args - instance of PaymentTransaction
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((DueBill)theParent, (PaymentTransaction)args[0]);
  }

  /**
   * Execute the Rule
   * @param theParent - instance of DueBill
   * @param args - instance of PaymentTransaction
   */
  private RulesInfo execute(DueBill duebill, PaymentTransaction paymenttransaction) {
    try {
      return new RulesInfo(CMSApplet.res.getString(
          "Store credit is not a valid form of change payment."));
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
    return CMSApplet.res.getString("Store credit is valid as change payment");
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    StringBuffer buf = new StringBuffer();
    buf.append(CMSApplet.res.getString(
        "Determines whether a store credit is allowed as change on this transaction."));
    return buf.toString();
  }
}

