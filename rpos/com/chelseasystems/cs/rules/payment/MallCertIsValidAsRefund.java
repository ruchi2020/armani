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
import com.chelseasystems.cs.payment.MallCert;
import com.chelseasystems.cr.swing.CMSApplet;


/**
 * <p>Title: MallCertIsValidAsRefund</p>
 * <p>Description: This business rule check that Gift certificate is not valid
 * as refund.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Anand
 * @version 1.0
 */
public class MallCertIsValidAsRefund extends Rule {

  /**
   * Default Constructor
   */
  public MallCertIsValidAsRefund() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of MallCert
   * @param args - instance of PaymentTransaction
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((MallCert)theParent, (PaymentTransaction)args[0]);
  }

  /**
   * Execute the Rule
   * @param theParent - instance of MallCert
   * @param args - instance of PaymentTransaction
   */
  private RulesInfo execute(MallCert mallCert, PaymentTransaction paymenttransaction) {
    try {
      // place business logic here
      return new RulesInfo(CMSApplet.res.getString("Gift certificate is not valid as refund."));
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
    return CMSApplet.res.getString("Mall certificate is valid as refund");
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    StringBuffer buf = new StringBuffer();
    buf.append("");
    return buf.toString();
  }
}

