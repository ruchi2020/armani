/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 01-27-2007 | Tushar    | Mearge    | Mearge with GLOBAL Version                         |
 +------+------------+-----------+-----------+----------------------------------------------------+
 +------+------------+-----------+-----------+----------------------------------------------------+
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.swing.model.ItemLookupDetailModel;
import com.chelseasystems.cs.swing.model.ItemLookupDetailModel_EUR;
import com.chelseasystems.cr.appmgr.IApplicationManager;

/**
 * <p>Title:ItemLookupDetailPanel_EUR </p>
 * <p>Description: View Grid of Items with different stores, color and size</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillnetInc</p>
 * @author Vikram Mundhra
 * @version 1.0
 */
public class ItemLookupDetailPanel_EUR extends ItemLookupDetailPanel {
 
	public ItemLookupDetailPanel_EUR(IApplicationManager theAppMgr, CMSItem[] cmsItems) {
		super(theAppMgr,cmsItems);
	}

	public ItemLookupDetailPanel_EUR(IApplicationManager theAppMgr, CMSItem[] cmsItems, String currentColorId) {
       super(theAppMgr,cmsItems, currentColorId);
    }

  	/**
  	 * Provides the New instance of ItemLookupModel.
  	 * @param cmsItms
  	 * @param hStoreId
  	 * @param sColorId
  	 * @return
  	 */
  	protected ItemLookupDetailModel getItemLoolupModel(CMSItem[] cmsItms, String hStoreId, String sColorId) {
        return new ItemLookupDetailModel_EUR(cmsItms, hStoreId, sColorId);
  	}
	
	protected String getNoCellValueText() {
		return "N/A";
	}
}

