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
 | 1    | 06-06-2006 | Sandhya   | 1478      | This class checks the business rule to hide  |
 |      |            |           |           | account maintenance for loyalty in a non-EA  |
 |      |            |           |           | store.                                       |
 +------+------------+-----------+-----------+----------------------------------------------+
 */


package com.chelseasystems.cs.rules.loyalty;

import java.util.StringTokenizer;

import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.appmgr.menu.CMSMenuOption;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cr.swing.CMSApplet;


/**
 * <p>Title: HideAccountMaintenance</p>
 * <p>Description: This class checks the business rule to hide account
 * maintenance for loyalty in a non-EA store</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Sandhya
 * @version 1.0
 */
public class HideAccountMaintenance extends Rule {
  private CMSCompositePOSTransaction theTxn;
	
  /**
   */
  public HideAccountMaintenance() {
  }

  /**
   * Execute the Rule
   * @param theParent Object
   * @param args Object[]
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((CMSMenuOption)theParent);
  }

  /**
   * Execute the Rule
   */
  private RulesInfo execute(CMSMenuOption cmsmenuoption) {
	ConfigMgr loyaltyCfg = null;
	String brandID = null;
	String loyaltyCards = null;
	String loyaltyType = null;
	String storeType = null;
	boolean storePremio = false;
	
	theTxn = ((CMSCompositePOSTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS"));
	if (theTxn != null) {
		brandID = ((CMSStore)theTxn.getStore()).getBrandID();
		loyaltyCfg = new ConfigMgr("loyalty.cfg");
		loyaltyCards = loyaltyCfg.getString("LOYALTY_CARDS");
		
		for (StringTokenizer tokenizer = new StringTokenizer(loyaltyCards, ","); tokenizer.hasMoreElements();) {
			loyaltyType = (String)tokenizer.nextElement();
			storeType = loyaltyCfg.getString(loyaltyType + ".TYPE");	
			if (storeType != null && brandID != null && storeType.equalsIgnoreCase(brandID)) {
				storePremio = true;
				break;
			}
		}
	}
	
	if (storePremio) {
		return new RulesInfo();
    } else {
    	return new RulesInfo(CMSApplet.res.getString("To be hidden for loyalty in non-EA store."));
    }
  }

  /**
   * Returns the name of the Rule.
   * @return the name of the rule
   */
  public String getName() {
    return CMSApplet.res.getString("Hide in an non-EA store.");
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    return CMSApplet.res.getString("Rule should enable hiding of account maintenance option in a non-EA store");
  }
}

