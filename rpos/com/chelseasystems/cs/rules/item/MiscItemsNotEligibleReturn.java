package com.chelseasystems.cs.rules.item;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.pos.SaleLineItem;
import com.chelseasystems.cr.rules.Rule;
import com.chelseasystems.cr.rules.RulesInfo;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import java.util.HashMap;
import com.chelseasystems.cs.util.*;

public class MiscItemsNotEligibleReturn extends Rule {

	public MiscItemsNotEligibleReturn() {
		ineligibleItems = new HashMap();
		// changed to support Armani_JP - return all misc item execpt OPEN_DEPOSIT and CLOSE_DEPOSIT
		//        ineligibleItems.put("POSTAGE", "POSTAGE");
	}

	public RulesInfo execute(Object theParent, Object args[]) {
		return execute((CMSCompositePOSTransaction) theParent, (SaleLineItem) args[0]);
	}

	private RulesInfo execute(CMSCompositePOSTransaction cmscompositepostransaction,
			SaleLineItem saleLineItem) {
		try {
			
			 //Vivek Mishra : Merged updated code from source provided by Sergio 18-MAY-16
			      ConfigMgr itmCfg = new ConfigMgr("item.cfg");
			      String merchItem = itmCfg.getString("ALLOW_MERCHANDISE_ITEM");
			   //Ends here  
			
			if (saleLineItem.isMiscItem() && (ineligibleItems.containsKey(saleLineItem.getMiscItemId()) ||
			// only Misc items of type Deposit cannot be returned
			//Vivek Mishra : Merged updated code from source provided by Sergio 18-MAY-16
			//LineItemPOSUtil.isDeposit(saleLineItem.getMiscItemId())))
					(LineItemPOSUtil.isDeposit(saleLineItem.getMiscItemId())&& (merchItem=="FALSE") )))//Ends here
				return new RulesInfo("Cannot return Deposit item.");
		} catch (Exception ex) {
			LoggingServices.getCurrent().logMsg(getClass().getName(), "execute",
					"Rule Failed, see exception.", "N/A", 2, ex);
		}
		return new RulesInfo();
	}

	public String getName() {
		return "Misc Items Not Eligible Return";
	}

	public String getDesc() {
		StringBuffer buf = new StringBuffer();
		buf.append("Cannot return this item if it is a miscellaneous item not elgible for return");
		return buf.toString();
	}

	private HashMap ineligibleItems;
}
