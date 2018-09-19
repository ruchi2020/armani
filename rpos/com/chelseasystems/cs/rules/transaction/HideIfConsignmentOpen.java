/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 09-26-2005 | Vikram    | 321       | This class implements business rule to enable|
 |      |            |           |           | hiding of buttons for Consignment-open.      |
 +------+------------+-----------+-----------+----------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.transaction;

import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.swing.pos.InitialSaleApplet;


/**
 * <p>Title: HideIfConsignmentOpen</p>
 * <p>Description: This class checks the business rule to enable  hiding of
 * discount buttons.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Vikram
 * @version 1.0
 */
public class HideIfConsignmentOpen extends Rule {

  /**
   */
  public HideIfConsignmentOpen() {
  }

  /**
   * Execute the Rule
   * @param theParent Object
   * @param args Object[]
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute();
  }

  /**
   * Execute the Rule
   */
  private RulesInfo execute() {
    if (CMSApplet.theAppMgr.getStateObject("TXN_MODE") != null
        && ((Integer)CMSApplet.theAppMgr.getStateObject("TXN_MODE")).intValue()
        == InitialSaleApplet.CONSIGNMENT_OPEN) {
      return new RulesInfo(CMSApplet.res.getString("To be hidden because in consignment open mode."));
    }
    return new RulesInfo();
  }

  /**
   * Returns the name of the Rule.
   * @return the name of the rule
   */
  public String getName() {
    return CMSApplet.res.getString("Hide in consignment open mode.");
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    return CMSApplet.res.getString("Rule should enable hiding in consignment open mode.");
  }
}

