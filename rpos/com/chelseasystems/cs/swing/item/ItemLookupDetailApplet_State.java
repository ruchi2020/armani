/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 04/04/2006 | Sandhya   | PCR1256   | Style locator enhancement                          |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 05/17/2005 | Vikram    | N/A       | Updated for POS_104665_FS_ItemLookup_Rev1          |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04/27/2005 | Vikram    | N/A       | POS_104665_FS_ItemLookup_Rev1                      |
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.swing.item;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.appmgr.state.*;

/**
 * <p>Title:ItemLookupDetailApplet_State </p>
 * <p>Description: State Object for ItemLookupListApplet</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillnetInc</p>
 * @author Vikram Mundhra
 * @version 1.0
 */
public class ItemLookupDetailApplet_State {

	/**
	 *
	 * @param theAppMgr IApplicationManager
	 * @return int
	 * @throws StateException
	 */
	public int ADD_ITEM(IApplicationManager theAppMgr) throws StateException {
		return 0;
	}

	public int STOCK_REQUEST(IApplicationManager theAppMgr)
			throws StateException {
		return -1;
	}

	public int SEND_REQUEST(IApplicationManager theAppMgr)
			throws StateException {
		return -1;

	}
	/**
	 *
	 * @param theAppMgr IApplicationManager
	 * @return int
	 * @throws StateException
	 */
	public int PREV(IApplicationManager theAppMgr) throws StateException {
		theAppMgr.goBack();
		return -1;
	}

	/**
	 * 
	 * @param theAppMgr
	 * @return
	 * @throws StateException
	 */
	public int NEXT_COLOR(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	/**
	 * 
	 * @param theAppMgr
	 * @return
	 * @throws StateException
	 */
	public int PREVIOUS_COLOR(IApplicationManager theAppMgr)
			throws StateException {
		return -1;
	}
}
