/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.transaction;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.appmgr.state.*;
import com.chelseasystems.cr.util.ResourceManager;
import java.util.ResourceBundle;


/**
 * <p>Title: InitialPreSalesApplet_State </p>
 *
 * <p>Description: Used for PreSales and Consignments </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: SkillNet Inc</p>
 *
 * @author Manpreet S Bawa
 * @version 1.0
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1   | 04-13-2005 | Manpreet  | N/A       |1.PreSale Open Specification                  |
 --------------------------------------------------------------------------------------------
 */
public class InitialPreSalesApplet_State {
  private ResourceBundle res;

  /**
   * put your documentation comment here
   */
  public InitialPreSalesApplet_State() {
    res = ResourceManager.getResourceBundle();
  }

  /**
   * Return to InitialSaleApplet
   * @param theAppMgr IApplicationManager
   * @throws StateException
   * @return int
   */
  public int PRESALE_OPEN(IApplicationManager theAppMgr)
      throws StateException {
    return 0;
  }

  /**
   * Return to ListItemsApplet
   * @param theAppMgr IApplicationManager
   * @throws StateException
   * @return int
   */
  public int PRESALE_CLOSE(IApplicationManager theAppMgr)
      throws StateException {
    return 1;
  }

  /**
   * Go back.
   * @param theAppMgr IApplicationManager
   * @throws StateException
   * @return int
   */
  public int PREV(IApplicationManager theAppMgr)
      throws StateException {
    return 0;
  }
}

