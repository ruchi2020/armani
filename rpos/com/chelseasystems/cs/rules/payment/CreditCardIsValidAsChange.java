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
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSReturnLineItem;
import com.chelseasystems.cs.pos.CMSReturnLineItemDetail;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.paidout.PaidOutTransaction;
import com.chelseasystems.cs.collection.CMSMiscCollection;
import com.chelseasystems.cr.paidout.PaidOutTransaction;
import com.chelseasystems.cr.swing.CMSApplet;


/**
 * <p>Title: CreditCardIsValidAsChange</p>
 * <p>Description: This business rule checks that Credit card is a valid form
 * of change payment </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Anand
 * @version 1.0
 */
public class CreditCardIsValidAsChange extends Rule {

  /**
   */
  public CreditCardIsValidAsChange() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of CreditCard
   * @param args - instance of PaymentTransaction
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((CreditCard)theParent, (PaymentTransaction)args[0]);
  }

  /**
   * Execute the Rule
   * @param theParent - instance of CreditCard
   * @param args - instance of PaymentTransaction
   */
  private RulesInfo execute(CreditCard creditcard, PaymentTransaction paymenttransaction) {
    try {
      if (paymenttransaction instanceof CMSCompositePOSTransaction) {
        CMSCompositePOSTransaction txn = (CMSCompositePOSTransaction)paymenttransaction;
        POSLineItem[] returnlines = txn.getReturnLineItemsArray();
        for (int i = returnlines.length - 1; i >= 0; i--) {
          CMSReturnLineItem line = (CMSReturnLineItem)returnlines[i];
          POSLineItemDetail[] returndetails = line.getLineItemDetailsArray();
          for (int j = returndetails.length - 1; j >= 0; j--) {
            CMSReturnLineItemDetail detail = (CMSReturnLineItemDetail)returndetails[j];
            CMSCompositePOSTransaction origTxn = (CMSCompositePOSTransaction)detail.
                getSaleLineItemDetail().getLineItem().getTransaction().getCompositeTransaction();
            Payment[] pays = origTxn.getPaymentsArray();
            for (int k = pays.length - 1; k >= 0; k--) {
              if (pays[k] instanceof CreditCard && pays[k].getAmount().greaterThan(new ArmCurrency(0d))) {
                /*
                 * any old txn with a credit card payment will allow this
                 * new txn to have credit pard payments made on it
                 */
                return new RulesInfo();
              }
            }
          }
        }
      }
      if (paymenttransaction instanceof PaidOutTransaction) {
        if (!((PaidOutTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS")).getType().equals(
            "CASH_TRANSFER")) {
          return new RulesInfo();
        } else
          return new RulesInfo(CMSApplet.res.getString(
              "A Credit Card is not a valid form of change payment."));
      }
      return new RulesInfo(CMSApplet.res.getString(
          "Credit cards are not valid as a form of change payments."));
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
    return CMSApplet.res.getString("CreditCard is valid as change payment");
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    StringBuffer buf = new StringBuffer();
    buf.append("This rule should determine if a Credit C");
    buf.append("ard should be allowed as change payment.");
    return buf.toString();
  }
}

