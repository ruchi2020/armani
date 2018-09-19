package com.chelseasystems.cs.rules.returns;

import com.chelseasystems.cr.item.MiscItemManager;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.rules.Rule;
import com.chelseasystems.cr.rules.RulesInfo;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.util.LineItemPOSUtil;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Skillnet Inc</p>
 * @author Tim 
 * @version 1.0
 */
public class IsDepositAllowedForReturn extends Rule {

	private static final long serialVersionUID = 1L;

	/**
	 * put your documentation comment here
	 */
	public IsDepositAllowedForReturn() {
	}

	/**
	 * Execute the rule.
	 * @param theParent the class calling the rule (i.e. CMSCompositePOSTransaction)
	 * @param args
	 */
	public RulesInfo execute(Object theParent, Object args[]) {
		return execute((CMSCompositePOSTransaction) theParent, (POSLineItem) args[0]);
	}

	/**
	 * Execute the rule.
	 * @param theParent the class calling the rule (i.e. CMSCompositePOSTransaction)
	 * @param args
	 */
	public RulesInfo execute(CMSCompositePOSTransaction txn, POSLineItem lineItem) {
		try {
			if ((MiscItemManager.getInstance().isMiscItem(lineItem.getItem().getId()) && !LineItemPOSUtil.isDeposit(lineItem.getItem().getId()))) {
				return new RulesInfo("Cannot return Deposit item.");
			}
		} catch (Exception ex) {
			LoggingServices.getCurrent().logMsg(getClass().getName(), "execute", "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
			return new RulesInfo();
		}
		return new RulesInfo();
	}

	/**
	 * Returns the name of the rule.
	 * @return the name of the rule
	 */
	public String getName() {
		return "IsDepositAllowedForReturn";
	}

	/**
	 * Returns a user-friendly version of the rule.
	 * @return a user-friendly version of the rule.
	 */
	public String getDesc() {
		String result = "IsDepositAllowedForReturn";
		return result;
	}
}
