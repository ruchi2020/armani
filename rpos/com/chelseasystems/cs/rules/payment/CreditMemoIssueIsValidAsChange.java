/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.payment;

import com.chelseasystems.cr.rules.*;
import java.util.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cr.paidout.*;
import com.chelseasystems.cr.swing.CMSApplet;


/**
 * <p>Title: CreditMemoIssueIsValidAsChange</p>
 * <p>Description: This business rule checks that Store credit issue is not
 * a valid change for a paidout transaction </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Anand
 * @version 1.0
 */
public class CreditMemoIssueIsValidAsChange extends Rule {

  /**
   * Default Constructor
   */
  public CreditMemoIssueIsValidAsChange() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of DueBillIssue
   * @param args - instance of PaymentTransaction
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((DueBillIssue)theParent, (PaymentTransaction)args[0]);
  }

  /**
   * Execute the Rule
   * @param theParent - instance of DueBillIssue
   * @param args - instance of PaymentTransaction
   */
  private RulesInfo execute(DueBillIssue duebillissue, PaymentTransaction paymenttransaction) {
    if (paymenttransaction instanceof PaidOutTransaction) {
      return new RulesInfo(CMSApplet.res.getString(
          "Store credit issue not valid change for a paidout transaction."));
    }
    return new RulesInfo(false);
  }

  /**
   * Returns the name of the Rule.
   * @return the name of the rule
   */
  public String getName() {
    return CMSApplet.res.getString("Store credit issue is valid as change");
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    StringBuffer buf = new StringBuffer();
    buf.append("is a store credit issue a valid change ty");
    buf.append("pe?  In some cases, but not for a cash drop for example.");
    return buf.toString();
  }
}

