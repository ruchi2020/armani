/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.pos;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.appmgr.state.*;


/**
 *
 * <p>Title:PrintFiscalDocumentApplet_State </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class PrintFiscalDocumentApplet_State {

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int MOD_CUST(IApplicationManager theAppMgr)
      throws StateException {
    theAppMgr.addStateObject("CUST_MGMT_MODE", "MODIFY");
    return 2;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int CHANGE_FISCAL_ADDRESS(IApplicationManager theAppMgr)
      throws StateException {
    return 1;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int CUST_LOOKUP(IApplicationManager theAppMgr)
      throws StateException {
    theAppMgr.addStateObject("ARM_DIRECT_TO", "PRINT_FISCAL");
    theAppMgr.addStateObject("ARM_DIRECTED_FROM", "PRINT_FISCAL");
    theAppMgr.addStateObject("ARM_CUST_REQUIRED", "TRUE");
    return 0;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int PREV(IApplicationManager theAppMgr)
      throws StateException {
    removeStateObjects(theAppMgr);
    if (theAppMgr.getStateObject("TXN_MODE") != null) {
      int iMode = ((Integer)theAppMgr.getStateObject("TXN_MODE")).intValue();
      if (iMode == InitialSaleApplet_EUR.NO_SALE_MODE || iMode == InitialSaleApplet_EUR.NO_RETURN_MODE)
        return 4;
    }
    if(theAppMgr.getStateObject("ARM_TXN_HIST_FISCAL_TXN") != null)
      return 5;
    return 3;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int SELECT_ITEMS(IApplicationManager theAppMgr)
      throws StateException {
    return -1;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  private void removeStateObjects(IApplicationManager theAppMgr) {
    theAppMgr.removeStateObject("ARM_DIRECT_TO");
    theAppMgr.removeStateObject("ARM_DIRECTED_FROM");
    theAppMgr.removeStateObject("CUST_MGMT_MODE");
    theAppMgr.removeStateObject("ARM_CUST_REQUIRED");
    theAppMgr.removeStateObject("CUSTOMER_LOOKUP");
    theAppMgr.removeStateObject("ARM_FISCAL_ADDRESS");
    theAppMgr.removeStateObject("ARM_FISCAL_CUSTOMER");
    theAppMgr.removeStateObject("PRINT_FISCAL_DOCUMENT");
    //     theAppMgr.removeStateObject("ARM_PRINT_FISCAL_MODE");
  }


  public int MODIFY_DOC_NUMBER (IApplicationManager theAppMgr) throws StateException{
    return -1;
  }

  public int MODIFY_TAX_NO (IApplicationManager theAppMgr) throws StateException{
    return -1;
  }

  public int MODIFY_DDT_NO (IApplicationManager theAppMgr) throws StateException{
    return -1;
  }

  public int MODIFY_CREDIT_NOTE (IApplicationManager theAppMgr) throws StateException{
    return -1;
  }

}

