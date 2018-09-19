/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 05/23/2005 | Vikram    | N/A       | Updated for Version                             |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 05/17/2005 | Vikram    | N/A       | Updated for Escape Key                             |
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing;

import com.chelseasystems.cr.swing.*;
import java.awt.event.ActionEvent;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.util.LocaleManager;
import java.util.Locale;


//import com.chelseasystems.cs.appmgr.CMSAppManager;
/**
 * <p>Title: CMSMainFrame</p>
 *
 * <p>Description: Main Frame for POS</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Vikram Mundhra
 * @version 1.0
 */
public class CMSMainFrame extends MainFrame {
  static {
    com.chelseasystems.cs.util.Version.applyVersion();
    Locale.setDefault(LocaleManager.getInstance().getDefaultLocale());
  }

  public CMSMainFrame() {
    Locale defaultLocale = LocaleManager.getInstance().getDefaultLocale();
    setLocale(defaultLocale);
    getGlobalBar().setLocale(defaultLocale);
  }

  /**
   * @param anEvent
   */
  public void applicationKeyPressed(ActionEvent anEvent) {
    super.applicationKeyPressed(anEvent);
    CMSActionEvent cmsActionEvent = null;
    if (anEvent instanceof CMSActionEvent)
      cmsActionEvent = (CMSActionEvent)anEvent;
    else
      cmsActionEvent = new CMSActionEvent(anEvent);
    if (cmsActionEvent.getActionCommand().equals("PREV")) {
      //            if(CMSApplet.theAppMgr instanceof CMSAppManager
      //               && ((CMSAppManager)CMSApplet.theAppMgr).getMenuHeader() != null)
      //            {
      //                CMSApplet.theAppMgr.getAppletManager().getCurrentCMSApplet().appButtonEvent(((CMSAppManager)CMSApplet.theAppMgr).getMenuHeader(), cmsActionEvent);
      //            }
      CMSApplet.theAppMgr.getAppletManager().getCurrentCMSApplet().appButtonEvent(cmsActionEvent);
      if (!cmsActionEvent.isConsumed())
        CMSApplet.theAppMgr.fireButtonEvent("PREV");
    }
  }
}

