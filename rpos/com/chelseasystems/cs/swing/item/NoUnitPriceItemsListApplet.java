/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+ 
 | 1    | 10/17/2006 | Sandhya   | N/A       | Displays items with zero unit price                |
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.swing.item;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.item.CMSItemHelper;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.swing.panel.NoUnitPriceItemsListPanel;
import com.ga.fs.fsbridge.ARMFSBridge;


/**
 * <p>Title:NoUnitPriceItemsListApplet </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillnetInc</p>
 * @author Sandhya Ajit
 * @version 1.0
 */
public class NoUnitPriceItemsListApplet extends CMSApplet {	
  private static final long serialVersionUID = 0;
  private NoUnitPriceItemsListPanel pnlNoUnitPriceItemsList = new NoUnitPriceItemsListPanel();
  private CMSItem[] cmsItems;

  /**
   * put your documentation comment here
   */
  public void init() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private void jbInit()
      throws Exception {
    JPanel pnlNorth = new JPanel();
    JLabel lblTitle = new JLabel();
    pnlNoUnitPriceItemsList.setOpaque(false);
    pnlNoUnitPriceItemsList.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
    this.getContentPane().add(pnlNorth, BorderLayout.NORTH);
    this.getContentPane().add(pnlNoUnitPriceItemsList, BorderLayout.CENTER);
    pnlNorth.add(lblTitle, null);
    pnlNorth.setOpaque(false);
    lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
    lblTitle.setText("List of items with zero unit price");
    lblTitle.setFont(theAppMgr.getTheme().getHeaderFont());
    this.setBackground(theAppMgr.getBackgroundColor());
    pnlNoUnitPriceItemsList.setAppMgr(theAppMgr);
  }

  public void start() {
    theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
    reset();
    populateItems();
    theAppMgr.showMenu(MenuConst.PREV_ONLY, theOpr);
    theAppMgr.setSingleEditArea("");
  }
  
  /**
   */
  private void populateItems() {
    try {
      cmsItems = (CMSItem[])CMSItemHelper.findItemsWithNoUnitPrice(theAppMgr);
      if (cmsItems == null || cmsItems.length < 1) {
    	showNoItemFound();
    	return;
      }
      pnlNoUnitPriceItemsList.setItems(cmsItems);
    } catch (Exception ex) {
      theAppMgr.showExceptionDlg(ex);
    }
  }
  
  /**
   * put your documentation comment here
   */
  public void stop() {}

  /**
   * put your documentation comment here
   */
  private void reset() {
    pnlNoUnitPriceItemsList.clear();
  }  
    
  /**
   * put your documentation comment here
   */
  private void showNoItemFound() {
    theAppMgr.showErrorDlg(res.getString("Sorry, no Item found"));
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getVersion() {
    return ("$Revision: 1.1 $");
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getScreenName() {
    return "Zero Unit Price Items";
  }
  
  /**
   * callback when page down is pressed
   */
  public void pageDown(MouseEvent e) {
	pnlNoUnitPriceItemsList.nextPage();
    theAppMgr.showPageNumber(e, pnlNoUnitPriceItemsList.getCurrentPageNumber() + 1, pnlNoUnitPriceItemsList.getTotalPages());
  }

  /**
   * callback when page up is pressed
   */
  public void pageUp(MouseEvent e) {
	pnlNoUnitPriceItemsList.prevPage();
    theAppMgr.showPageNumber(e, pnlNoUnitPriceItemsList.getCurrentPageNumber() + 1, pnlNoUnitPriceItemsList.getTotalPages());
  } 

  /**
   * MP: Home pressed at customer display exits transaction with no message
   * @return
   */
  public boolean isHomeAllowed() 
	{
		CMSCompositePOSTransaction cmsTxn = (CMSCompositePOSTransaction) theAppMgr
				.getStateObject("TXN_POS");
		if (cmsTxn == null) {
			return (true);
		}
		/*
		 * Added by Yves Agbessi (05-Dec-2017) Handles the posting of the Sign
		 * Off event for Fiscal Solutions Service START--
		 */
		boolean goToHomeView = (theAppMgr
				.showOptionDlg(
						res.getString("Cancel Transaction"),
						res.getString("Are you sure you want to cancel this transaction?")));
		if (goToHomeView) {
			ARMFSBridge.postARMSignOffTransaction((CMSEmployee) theOpr);
		}

	return goToHomeView;
		/*
		 * --END
		 */

 }
}

