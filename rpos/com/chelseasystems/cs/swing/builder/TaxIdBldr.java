/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.builder;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.util.Version;


/**
 */
public class TaxIdBldr implements IObjectBuilder {
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;
  private String taxId;
  private String strActionCommand;

  /**
   */
  public TaxIdBldr() {
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
//edited for tax exempt id validation on Dec 09, 2016    
    if("JP".equalsIgnoreCase(Version.CURRENT_REGION) && ((String)theEvent).trim().length() > 20){
    	theAppMgr.showErrorDlg("Tax Exempt Id should not be greater than 20 digit. Please Enter Valid Tax Exempt Id");
    	completeAttributes();
    	return;
    }
//ends here
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
    taxId = null;
    this.applet = applet;
    //   theAppMgr.showMenu(MenuConst.TAX_EXEMPT, applet.theOpr);
    // register for call backs
    completeAttributes();
  }

  /**
   * @return
   */
  private boolean completeAttributes() {
    if (taxId == null) {
      // theAppMgr.setSingleEditArea(applet.res.getString("Enter tax exempt ID.  Press 'Enter' to remove ID."), strActionCommand);
      theAppMgr.setSingleEditArea(applet.res.getString("Enter tax exempt ID."), strActionCommand);
      return false;
    }
    return true;
  }
}

