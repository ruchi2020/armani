/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
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


/**
 * <p>Title: MailCheckIsValidAsPayment</p>
 * <p>Description: This business rule check that mail checks is valid form
 * of Payment.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author
 * @version 1.0
 */
public class MailCheckIsValidAsPayment extends Rule {

  /**
   * Default Constructor
   */
  public MailCheckIsValidAsPayment() {
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
      return new RulesInfo("Cannot issue a mail Check as a form of payment from the customer.");
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
    return "Mail Check is valid as payment";
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    StringBuffer buf = new StringBuffer();
    buf.append("This rule should determine whether issue");
    buf.append("ing a Mail Check is valid form of payment.");
    return buf.toString();
  }
}

