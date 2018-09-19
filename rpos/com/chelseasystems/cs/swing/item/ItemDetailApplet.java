/**
 * <p>Title: ItemDetailApplet.java </p>
 *
 * <p>Description: Displays all information for Item context.</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: SkillNetInc </p>
 *
 * @author Bawa Manpreet Singh
 * @version 1.0
 * Date Created : 02/11/2005
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 05/17/2005 | Vikram    | N/A       | Updated for Escape Key                             |
 --------------------------------------------------------------------------------------------------
 | 1    | 02-11-2005 | Manpreet  | N/A       | Original Version                                   |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */


package com.chelseasystems.cs.swing.item;

import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.appmgr.*;
import java.awt.*;
import java.awt.event.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cs.swing.panel.ItemDetailPanel;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cr.discount.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cs.tax.*;
import com.chelseasystems.cs.store.CMSStore;
import javax.swing.JPanel;
import com.chelseasystems.cr.util.ResourceManager;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.*;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.ga.fs.fsbridge.ARMFSBridge;


/**
 * put your documentation comment here
 */
public class ItemDetailApplet extends CMSApplet {
  private JPanel pnlHeader;
  private ItemDetailPanel pnlItemDetails;
  private CMSCompositePOSTransaction theTxn;
  private POSLineItem lineItem;
  private boolean bStartup = true;
  public static boolean bItemLookUpApplet = false;
  private JCMSLabel lblSKU;
  private JCMSLabel lblSKUValue;
  private JCMSLabel lblDescription;
  private JCMSLabel lblDescriptionValue;
  //  private ScrollableToolBarPanel screen;
  private ResourceBundle resource;
  GridBagLayout gridBagLayout1 = new GridBagLayout();

  /**
   * getScreenName
   *
   * @return String
   * @todo Implement this com.chelseasystems.cr.swing.CMSApplet method
   */
  public String getScreenName() {
    return "Item Details";
  }

  /**
   * getVersion
   *
   * @return String
   * @todo Implement this com.chelseasystems.cr.swing.CMSApplet method
   */
  public String getVersion() {
    return ("$Revision: 1.17.2.1.4.1 $");
  }

  /**
   * init
   *
   * @todo Implement this com.chelseasystems.cr.swing.CMSApplet method
   */
  public void init() {
    try {
      pnlHeader = new JPanel();
      resource = ResourceManager.getResourceBundle();
      jbinit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   */
  public void start() {
    try {
      //      if(!bStartup)
      //      {
      if (pnlItemDetails == null) {
        pnlItemDetails = new ItemDetailPanel();
        this.getContentPane().add(pnlItemDetails, BorderLayout.CENTER);
      }
      //If the Calling Applet is IntialSaleApplet
      // oR ItemLookUpApplet.
      lineItem = (POSLineItem)theAppMgr.getStateObject("InitialSale_POSLineItem");
      bItemLookUpApplet = false;
      if (lineItem == null) {
        bItemLookUpApplet = true;
        lineItem = (POSLineItem)theAppMgr.getStateObject("ItemLookUp_POSLineItem");
        if (lineItem == null)
          return;
      }
      pnlItemDetails.setPOSLineItem(lineItem, theAppMgr);
      setItemSKU(lineItem.getItem().getId());
      setItemDescription(lineItem.getItem().getDescription());
      this.repaint();
      //      }
      theAppMgr.showMenu(MenuConst.PREV_ONLY, theOpr);
      theAppMgr.removeStateObject("InitialSale_POSLineItem");
      theAppMgr.removeStateObject("ItemLookUp_POSLineItem");
      bStartup = false;
      //      screen = (ScrollableToolBarPanel) AppManager.getCurrent().getMainFrame().getAppToolBar();
      //      screen.registerKeyboardAction(new ActionListener()
      //      {
      //        public void actionPerformed(ActionEvent e)
      //        {
      //        //  doPrevious();
      //        }
      //      }
      //      , "ESC_CMD", KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
      //          JComponent.WHEN_IN_FOCUSED_WINDOW);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   */
  private void doPrevious() {
    theAppMgr.removeStateObject("InitialSale_POSLineItem");
    reset();
    //    theAppMgr.goBack();
    //    screen = (ScrollableToolBarPanel) AppManager.getCurrent().getMainFrame().getAppToolBar();
    //    screen.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
  }

  /**
   * put your documentation comment here
   */
  public void stop() {
    theAppMgr.removeStateObject("InitialSale_POSLineItem");
    reset();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setItemSKU(String sValue) {
    lblSKUValue.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setItemDescription(String sValue) {
    lblDescriptionValue.setText(sValue);
  }

  /**
   * put your documentation comment here
   */
  private void reset() {
    lblSKUValue.setText("");
    lblDescriptionValue.setText("");
    //    pnlItemDetails.clear();
  }

  /**
   * put your documentation comment here
   * @exception Exception
   */
  private void jbinit()
      throws Exception {
    theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
    theTxn = (CMSCompositePOSTransaction)theAppMgr.getStateObject("TXN_POS");
    theOpr = (CMSEmployee)theAppMgr.getStateObject("OPERATOR");
    //
    //    lineItem = (POSLineItem)theAppMgr.getStateObject("InitialSale_POSLineItem");
    //
    //    if(lineItem == null)
    //    {
    //      bItemLookUpApplet = true;
    //      lineItem = (POSLineItem)theAppMgr.getStateObject("ItemLookUp_POSLineItem");
    //      if(lineItem == null) throw new Exception("LineItem not found");
    //    }
    //    try
    //    {
    //    TaxUtilities.applyTax(theAppMgr, theTxn, (CMSStore)theTxn.getStore(),
    //                            (CMSStore)theTxn.getStore(), theTxn.getProcessDate());
    //    }
    //   catch(Exception ex)
    //   {
    //     System.out.println("Exception invoking tax engine");
    //   }
    lblSKU = new JCMSLabel();
    lblSKUValue = new JCMSLabel();
    lblDescription = new JCMSLabel();
    lblDescriptionValue = new JCMSLabel();
    lblSKU.setAppMgr(theAppMgr);
    lblSKU.setFont(theAppMgr.getTheme().getLabelFont());
    lblSKUValue.setAppMgr(theAppMgr);
    lblSKUValue.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblDescription.setAppMgr(theAppMgr);
    lblDescription.setFont(theAppMgr.getTheme().getLabelFont());
    lblDescriptionValue.setAppMgr(theAppMgr);
    lblDescriptionValue.setFont(theAppMgr.getTheme().getTextFieldFont());
    if (resource != null) {
      lblSKU.setText(resource.getString("SKU"));
      lblDescription.setText(resource.getString("Desc"));
    } else {
      lblSKU.setText("SKU");
      lblDescription.setText("Desc");
    }
    lblSKU.setPreferredSize(new Dimension(25, 19));
    //    lblSKUValue.setText(lineItem.getItem().getId());
    lblSKUValue.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblSKUValue.setBorder(BorderFactory.createEtchedBorder());
    lblDescription.setPreferredSize(new Dimension(25, 19));
    //    lblDescriptionValue.setText(lineItem.getItem().getDescription());
    lblDescriptionValue.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblDescriptionValue.setBorder(BorderFactory.createEtchedBorder());
    lblSKU.setAppMgr(theAppMgr);
    lblSKUValue.setAppMgr(theAppMgr);
    lblDescription.setAppMgr(theAppMgr);
    lblDescriptionValue.setAppMgr(theAppMgr);
    lblSKU.setFont(theAppMgr.getTheme().getLabelFont());
    lblDescription.setFont(theAppMgr.getTheme().getLabelFont());
    pnlHeader.setPreferredSize(new Dimension(833, 37));
    pnlHeader.setLayout(new GridBagLayout());
    pnlHeader.add(lblSKU
        , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(3, 8, 6, 0), 7, 3));
    pnlHeader.add(lblSKUValue
        , new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(3, 0, 6, 2), 87, 0));
    pnlHeader.add(lblDescription
        , new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(3, 10, 6, 0), 6, 3));
    pnlHeader.add(lblDescriptionValue
        , new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(3, 0, 6, 7), 271, 0));
    //    pnlItemDetails = new ItemDetailPanel(lineItem);
    pnlItemDetails = new ItemDetailPanel();
    //    pnlItemDetails.setAppMgr(theAppMgr);
    this.getContentPane().removeAll();
    this.setBackground(theAppMgr.getBackgroundColor());
    pnlHeader.setBackground(theAppMgr.getBackgroundColor());
    this.getContentPane().add(pnlHeader, BorderLayout.NORTH);
    this.getContentPane().add(pnlItemDetails, BorderLayout.CENTER);
    this.doLayout();
  }

  /**
   * put your documentation comment here
   * @param e
   */
  public void pageDown(MouseEvent e) {
    pnlItemDetails.nextPage();
    theAppMgr.showPageNumber(e, pnlItemDetails.getCurrentPageNumber() + 1
        , pnlItemDetails.getTotalPages());
  }

  /**
   * put your documentation comment here
   * @param e
   */
  public void pageUp(MouseEvent e) {
    pnlItemDetails.prevPage();
    theAppMgr.showPageNumber(e, pnlItemDetails.getCurrentPageNumber() + 1
        , pnlItemDetails.getTotalPages());
  }

  /**
   * put your documentation comment here
   * @param anEvent
   */
  public void appButtonEvent(CMSActionEvent anEvent) {
    String sActionCommand = anEvent.getActionCommand().toUpperCase();
    if (sActionCommand.equals("PREV")) {
      doPrevious();
    }
  }

  /**
   * MP: Home pressed at customer display exits transaction with no message
   * @return
   */
  public boolean isHomeAllowed() {
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

