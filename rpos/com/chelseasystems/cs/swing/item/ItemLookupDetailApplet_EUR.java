/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 01/29/2007 | Tushar    |           | Mearge with Global Version                         |
 +------+------------+-----------+-----------+----------------------------------------------------+
 --------------------------------------------------------------------------------------------------
 *
 */


package com.chelseasystems.cs.swing.item;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.swing.panel.ItemLookupDetailPanel;
import com.chelseasystems.cs.swing.panel.ItemLookupDetailPanel_EUR;

/**
 * <p>Title:ItemLookupDetailApplet </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillnetInc</p>
 * @author Tushar Dalal
 * @version 1.0
 */
public class ItemLookupDetailApplet_EUR extends ItemLookupDetailApplet {
	/**
	 * Provides the ItemLookupDetailPanel.
	 */
	protected ItemLookupDetailPanel getItemLookupDetailPanel(IApplicationManager appMgr, CMSItem[] cmsItms, String colorId) {
		return new ItemLookupDetailPanel_EUR(appMgr, cmsItms, colorId );
	}
	
}

