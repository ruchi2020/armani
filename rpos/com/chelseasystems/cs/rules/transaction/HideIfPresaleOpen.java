/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 09-26-2005 | Vikram    | 568       | This class implements business rule to enable|
 |      |            |           |           | hiding of discount buttons for Presale-open. |
 +------+------------+-----------+-----------+----------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.transaction;

import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.swing.pos.InitialSaleApplet;


/**
 * <p>Title: HideIfPresaleOpen</p>
 * <p>Description: This class checks the business rule to enable  hiding of
 * discount buttons.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Vikram
 * @version 1.0
 */
public class HideIfPresaleOpen extends Rule {

  /**
   */
  public HideIfPresaleOpen() {
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
        == InitialSaleApplet.PRE_SALE_OPEN) {
      return new RulesInfo(CMSApplet.res.getString("To be hidden because in pre-sale open mode."));
    }
    return new RulesInfo();
  }

  /**
   * Returns the name of the Rule.
   * @return the name of the rule
   */
  public String getName() {
    return CMSApplet.res.getString("Hide in pre-sale open mode.");
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    return CMSApplet.res.getString("Rule should enable hiding in pre-sale open mode.");
  }
}

