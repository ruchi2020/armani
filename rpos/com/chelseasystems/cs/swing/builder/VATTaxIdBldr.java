/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------|
 | 1    | 06-13-2005 | Khyati    |           |Europe: Tax Exempt                            |
 --------------------------------------------------------------------------------------------
 | 0    | 06-13-2005 | Khyati    |           |Added From Global Implementation              |
 --------------------------------------------------------------------------------------------
 */


package com.chelseasystems.cs.swing.builder;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.swing.dlg.TaxExemptDlg;
import com.chelseasystems.cr.util.ResourceManager;
import com.chelseasystems.cs.swing.dlg.*;


/**
 */
public class VATTaxIdBldr implements IObjectBuilder {
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;
  private String taxId;
  private String strActionCommand;
  private GenericChooseFromTableDlg overRideDlg;

  /**
   */
  public VATTaxIdBldr() {
  }

  /**
   * @param theBldrMgr
   * @param theAppMgr
   */
  public void init(IObjectBuilderManager theBldrMgr, IApplicationManager theAppMgr) {
    this.theBldrMgr = theBldrMgr;
    this.theAppMgr = theAppMgr;
  }

  /**
   */
  public void cleanup() {}

  /**
   * @param theCommand
   * @param theEvent
   */
  public void EditAreaEvent(String theCommand, Object theEvent) {
    if (theEvent == null || ((String)theEvent).trim().length() < 1)
      return;
    if (theCommand == strActionCommand) {
      taxId = ((String)theEvent).toUpperCase();
    }
    if (completeAttributes())
      theBldrMgr.processObject(applet, strActionCommand, taxId, this);
  }

  /**
   * @param Command
   * @param applet
   * @param initValue
   */
  public void build(String Command, CMSApplet applet, Object initValue) {
    this.strActionCommand = Command;
    System.out.println("strCommand " + strActionCommand);
    taxId = null;
    this.applet = applet;
    //   theAppMgr.showMenu(MenuConst.TAX_EXEMPT, applet.theOpr);
    // register for call backs
    if (completeAttributes())
      theBldrMgr.processObject(applet, strActionCommand, taxId, this);
  }

  /**
   * @return
   */
  private boolean completeAttributes() {
    if (taxId == null) {
      // theAppMgr.setSingleEditArea(applet.res.getString("Enter tax exempt ID.  Press 'Enter' to remove ID."), strActionCommand);
      //ks: Comment enter tax exempt ID for Europe and display tax exempt reason dlg box
      //        theAppMgr.setSingleEditArea(applet.res.getString("Enter tax exempt ID."), strActionCommand);
      theAppMgr.setSingleEditArea(applet.res.getString("Select VAT Exempt Reason."));
      //Display tax exempt dlg box
      displayTaxExemptReasons();
      overRideDlg.setVisible(true);
      if (overRideDlg.isOK()) {
        Object reasonCode = overRideDlg.getSelectedRow().getRowKeyData();
        taxId = (String)reasonCode;
        Object reasons[] = overRideDlg.getSelectedRow().getDisplayRow();
        theAppMgr.showMenu(MenuConst.ADD_ITEM_MENU, applet.theOpr, null);
        theAppMgr.setSingleEditArea(applet.res.getString(
            "Enter or scan item code; enter \"S\" to search."), "ITEM", theAppMgr.ITEM_MASK);
      } else {
        taxId = "";
        theAppMgr.setSingleEditArea(applet.res.getString("Select option"));
        theAppMgr.showMenu(MenuConst.TAX_EXEMPT, "TAX_EXEMPT", applet.theOpr);
        return false;
      }
    }
    return true;
  }

  /**
   *Display Tax Exempt Reason description
   */
  private void displayTaxExemptReasons() {
    TaxExemptDlg taxReasonHelper = new TaxExemptDlg();
    String[] titles = {ResourceManager.getResourceBundle().getString("Vat Exempt Reason")
    };
    overRideDlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame(), theAppMgr
        , taxReasonHelper.getTabelData(), titles);
  }
}

