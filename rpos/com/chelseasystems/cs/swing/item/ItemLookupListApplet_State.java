/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 5    | 07/09/2005 | Vikram    | 437       | New menu option "Item Details" for Item Lookup     |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 05/17/2005 | Vikram    | N/A       | Updated for POS_104665_FS_ItemLookup_Rev1          |
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
 * <p>Title:ItemLookupListApplet_State </p>
 * <p>Description: State Object for ItemLookupListApplet</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillnetInc</p>
 * @author Vikram Mundhra
 * @version 1.0
 */
public class ItemLookupListApplet_State {

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int SEARCH(IApplicationManager theAppMgr)
      throws StateException {
    return -1;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int PREV(IApplicationManager theAppMgr)
      throws StateException {
    theAppMgr.goBack();
    return -1;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int LOCATE(IApplicationManager theAppMgr)
      throws StateException {
    return 0;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int ITEM_DETAILS(IApplicationManager theAppMgr)
      throws StateException {
    return 1;
  }
}

