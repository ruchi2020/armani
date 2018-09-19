package com.chelseasystems.cs.rules.transaction;

import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.appmgr.menu.CMSMenuOption;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.rules.RulesInfo;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.pos.Transaction;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.util.DateUtil;
import java.util.Date;
/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class HideIfTransactionNotSameBusinessDate extends Rule{
  public HideIfTransactionNotSameBusinessDate() {
  }

  /**
   * Execute the rule.
   *
   * @param object theParent - instance of CMSMenuOption
   * @param args - instance of Employee
   * @param args - instance of Store
   * @return RulesInfo
   */
  public RulesInfo execute(Object theParent, Object[] args) {
    return execute((CMSMenuOption)theParent, (Employee)args[0], (Store)args[1]);
  }

  /**
   * Execute the Rule
   * @param cmsmenuoption theParent
   * @param employee Employee
   * @param store Store
   * @return RulesInfo
   */
  private RulesInfo execute(CMSMenuOption cmsmenuoption, Employee employee, Store store) {
    try {
      if (CMSApplet.theAppMgr.getStateObject("THE_TXN") != null) {
        Transaction transaction = (Transaction)CMSApplet.theAppMgr.getStateObject("THE_TXN");
        if (transaction instanceof CMSCompositePOSTransaction) {
          if (DateUtil.isSameDay(transaction.getProcessDate(), (Date)CMSApplet.theAppMgr.getGlobalObject(
              "PROCESS_DATE")))
            return new RulesInfo();
          else
            return new RulesInfo("Hide if Dummy Operator");
        }
        else
          return new RulesInfo("Hide if Dummy Operator");
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
    return ("Suppress menu button if not appropriate.");
  }

  /**
   * Return the name of the rule.
   * @return name of the rule.
   */
  public String getName() {
    return "HideIfDummyOperator";
  }

}
