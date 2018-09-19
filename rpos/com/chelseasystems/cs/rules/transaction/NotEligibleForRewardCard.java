/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 04-26-2005 | Megha   | N/A         |1.This class is new class created to achive   |
 |      |            |           |           | new business rule whether a cust gets a      |
 |            |           |           | reward card                                  |
 --------------------------------------------------------------------------------------------
 */


package com.chelseasystems.cs.rules.transaction;

import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cs.collection.CMSMiscCollection;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cr.appmgr.menu.CMSMenuOption;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.config.ConfigMgr;


/**
 */
public class NotEligibleForRewardCard extends Rule {
  /**
   */
  IApplicationManager myAppMgr;

  /**
   * put your documentation comment here
   */
  public NotEligibleForRewardCard() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of CompositePOSTransaction
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((CMSMenuOption)theParent, (Employee)args[0], (Store)args[1]);
  }

  /**
   * Execute the Rule
   * @param theParent - instance of PaymentTransaction
   */
  private RulesInfo execute(CMSMenuOption cmsmenuoption, Employee employee, Store store) {
    try {
      ConfigMgr config = new ConfigMgr("loyalty.cfg");
      String loyaltyRewardRatio = config.getString("LOYALTY_REWARD_REDEMPTION_RATIO");
      String loyaltyAmount = config.getString("LOYALTY_REWARD_AMOUNT");
      double rewardPointsLevel = (new Double(loyaltyRewardRatio)).doubleValue()
          * (new Double(loyaltyAmount)).doubleValue();
      PaymentTransaction txn = (PaymentTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS");
      if (txn instanceof CMSCompositePOSTransaction) {
        CMSCompositePOSTransaction theTxn = ((CMSCompositePOSTransaction)txn);
        if (theTxn.getTransactionType().startsWith("SALE")
            || theTxn.getTransactionType().startsWith("RETN")) {
          if (theTxn.getLoyaltyCard() == null
              || (theTxn.getLoyaltyCard().getCurrBalance() + theTxn.getLoyaltyPoints())
              < rewardPointsLevel) {
            return new RulesInfo("Can't Hide when loyalty obj is present");
          }
        } else {
          return new RulesInfo("Reward card button not applicable");
        }
      } else {
        return new RulesInfo("Reward card button not applicable");
      }
    } catch (Exception ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
          , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
      ex.printStackTrace();
    }
    return new RulesInfo();
  }

  /**
   * Returns the name of the Rule.
   * @return the name of the rule
   */
  public String getName() {
    return "NotEligibleForRewardCard";
  }

  /**
   * Returns a user-friendly version of the rule.
   * @returns a user-friendly version of the rule.
   */
  public String getDesc() {
    return "Rule should determine whether Issue Reward   " + "  is showable";
  }
}

