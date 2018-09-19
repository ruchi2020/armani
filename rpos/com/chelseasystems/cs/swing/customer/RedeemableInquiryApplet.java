/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.customer;

import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.Iterator;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.receipt.ReceiptConfigInfo;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cs.swing.model.RedeemableHistoryModel;
import com.chelseasystems.cr.payment.RedeemableHist;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.pos.CMSTransactionPOSHelper;
import com.chelseasystems.cs.pos.CMSRedeemableBalanceAppModel;
import com.chelseasystems.cr.util.ObjectStore;
import com.chelseasystems.cr.util.ObjectStore;
import com.chelseasystems.cs.loyalty.RewardCard;
import com.chelseasystems.cs.loyalty.LoyaltyHelper;
import com.chelseasystems.cs.loyalty.Loyalty;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.ga.fs.fsbridge.ARMFSBridge;


/**
 */
public class RedeemableInquiryApplet extends CMSApplet {
  JCMSTextField jtfId = new JCMSTextField();
  JCMSTextField jtfPurchaseDate = new JCMSTextField();
  JCMSTextField jtfCustomer = new JCMSTextField();
  JCMSTextField jtfPhone = new JCMSTextField();
  JCMSTextField jtfIssueAmt = new JCMSTextField();
  JCMSTextField jtfCurrentAmt = new JCMSTextField();
  JCMSTextField jtfType = new JCMSTextField();
  /**
   * Added New Field
   */
  JCMSTextField jtfExpDate = new JCMSMaskedTextField();
  RedeemableHistoryModel model = new RedeemableHistoryModel();
  JCMSTable tblHist = new JCMSTable(model, JCMSTable.SELECT_ROW);
  private CMSEmployee theOpr;
  private SimpleDateFormat df = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
  private CMSRedeemableBalanceAppModel appModel;
  private Vector vecRedeemableTxnIds;

  /**
   */
  public void init() {
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   *
   */
  public RedeemableInquiryApplet() {
  }

  /**
   * @exception Exception
   */
  private void jbInit()
      throws Exception {
    JPanel jPanel1 = new JPanel();
    JPanel pnlHist = new JPanel();
    /**
     * New Label
     */
    JCMSLabel lblExpDate = new JCMSLabel();
    JCMSLabel lblPurchaseDate = new JCMSLabel();
    JCMSLabel lblId = new JCMSLabel();
    JCMSLabel lblCustomer = new JCMSLabel();
    JCMSLabel lblPhone = new JCMSLabel();
    JCMSLabel lblIssueAmt = new JCMSLabel();
    JCMSLabel lblType = new JCMSLabel();
    JCMSLabel lblCurrentAmount = new JCMSLabel();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    pnlHist.setLayout(new BorderLayout());
    pnlHist.add(tblHist.getTableHeader(), BorderLayout.NORTH);
    pnlHist.add(tblHist, BorderLayout.CENTER);
    jPanel1.setLayout(gridBagLayout1);
    this.getContentPane().add(jPanel1, BorderLayout.CENTER);
    jPanel1.add(lblCustomer
        , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(7, 9, 0, 0), 70, 1));
    jPanel1.add(jtfCustomer
        , new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(2, 9, 0, 50), 250, 0));
    jPanel1.add(lblIssueAmt
        , new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(7, 9, 0, 0), 60, 0));
    jPanel1.add(jtfIssueAmt
        , new GridBagConstraints(0, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(2, 9, 0, 50), 60, 0));
    jPanel1.add(lblId
        , new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(7, 9, 0, 0), 41, 1));
    jPanel1.add(pnlHist
        , new GridBagConstraints(0, 7, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER
        , GridBagConstraints.BOTH, new Insets(7, 7, 5, 8), 487, 239));
    jPanel1.add(jtfPurchaseDate
        , new GridBagConstraints(2, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(2, 9, 0, 90), 90, 0));
    jPanel1.add(lblPurchaseDate
        , new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(7, 9, 0, 64), 0, 0));
    jPanel1.add(lblPhone
        , new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(7, 14, 0, 56), 90, 1));
    jPanel1.add(jtfPhone
        , new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(2, 9, 0, 90), 90, 0));
    jPanel1.add(jtfCurrentAmt
        , new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(2, 9, 0, 50), 60, 0));
    jPanel1.add(lblCurrentAmount
        , new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(7, 9, 0, 0), 36, 1));
    jPanel1.add(lblType
        , new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(7, 9, 0, 0), 36, 1));
    jPanel1.add(jtfType
        , new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(2, 9, 0, 50), 60, 0));
    jPanel1.add(lblExpDate
        , new GridBagConstraints(2, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(2, 9, 0, 50), 36, 0));
    jPanel1.add(jtfId
        , new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(2, 9, 0, 50), 60, 0));
    jPanel1.add(jtfExpDate
        , new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(7, 9, 0, 0), 76, 1));
    lblType.setAppMgr(theAppMgr);
    lblType.setText(res.getString("Redeemable Type"));
    jtfType.setEnabled(false);
    jtfType.setEditable(false);
    jtfType.setAppMgr(theAppMgr);
    jtfType.setEnabled(false);
    jtfType.setEditable(false);
    jtfType.setAppMgr(theAppMgr);
    lblExpDate.setAppMgr(theAppMgr);
    lblExpDate.setText(res.getString("Expiry Date"));
    jtfExpDate.setEnabled(false);
    jtfExpDate.setEditable(false);
    jtfExpDate.setAppMgr(theAppMgr);
    jtfExpDate.setEnabled(false);
    jtfExpDate.setEditable(false);
    jtfExpDate.setAppMgr(theAppMgr);
    lblId.setText(res.getString("Reedemable ID"));
    lblPurchaseDate.setText(res.getString("Purchase Date"));
    lblPurchaseDate.setAppMgr(theAppMgr);
    lblCustomer.setText(res.getString("Customer"));
    lblPhone.setText(res.getString("Customer Id"));
    lblIssueAmt.setText(res.getString("Issue Amount"));
    lblCurrentAmount.setText(res.getString("Current Balance"));
    lblId.setAppMgr(theAppMgr);
    jtfId.setAppMgr(theAppMgr);
    jtfId.setEditable(false);
    jtfId.setEnabled(false);
    jtfPurchaseDate.setAppMgr(theAppMgr);
    jtfPurchaseDate.setEditable(false);
    jtfPurchaseDate.setEnabled(false);
    lblCustomer.setAppMgr(theAppMgr);
    jtfCustomer.setAppMgr(theAppMgr);
    jtfCustomer.setEditable(false);
    jtfCustomer.setEnabled(false);
    lblPhone.setAppMgr(theAppMgr);
    jtfPhone.setAppMgr(theAppMgr);
    jtfPhone.setEditable(false);
    jtfPhone.setEnabled(false);
    lblIssueAmt.setAppMgr(theAppMgr);
    jtfIssueAmt.setAppMgr(theAppMgr);
    jtfIssueAmt.setEditable(false);
    jtfIssueAmt.setEnabled(false);
    lblCurrentAmount.setAppMgr(theAppMgr);
    jtfCurrentAmt.setAppMgr(theAppMgr);
    jtfCurrentAmt.setEditable(false);
    jtfCurrentAmt.setEnabled(false);
    tblHist.setAppMgr(theAppMgr);
    tblHist.setModel(model);
    jPanel1.setBackground(theAppMgr.getBackgroundColor());
    tblHist.addComponentListener(new java.awt.event.ComponentAdapter() {

      /**
       * @param e
       */
      public void componentResized(ComponentEvent e) {
        model.setRowsShown(tblHist.getHeight() / tblHist.getRowHeight());
      }
    });
    tblHist.addMouseListener(new MouseAdapter() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void mouseClicked(MouseEvent e) {
        // MP:Changed the signature of the funct , to seperate the func
        // when table Evnt is called or when the bldr is called.
        clickEvent(e, "ForTableModel");
      }
    });
  }

  /**
   */
  public void start() {
    vecRedeemableTxnIds = new Vector();
    RewardCard rewardCard = new RewardCard();
    theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
    theOpr = (CMSEmployee)theAppMgr.getStateObject("OPERATOR");
    theAppMgr.showMenu(MenuConst.PREV_PRINT, theOpr);
    // Clear screen if new session, Check for previous redeemale history txn.
    if (theAppMgr.getStateObject("THE_TXN") == null)
      clearFields();
    //Coming back from detail screen, remove selected row
    else
      tblHist.clearSelection(); /**
    * Populating the screen for reward Card.
    */

   rewardCard = (RewardCard)theAppMgr.getStateObject("REWARDVAL");
   if (rewardCard != null) {
      theAppMgr.removeStateObject("REWARDVAL");
      this.populateScreen(rewardCard);
    } else {
      // Calling the AppButton
      RedeemableInquiryApplet.this.appButtonEvent(new CMSActionEvent(this, 0, "REDEEM_CARD", 0));
    }
  }

  /**
   */
  public void stop() {}

  /**
   * @return
   */
  public String getVersion() {
    return ("$Revision: 1.1 $");
  }

  /**
   * @return
   */
  public String getScreenName() {
    return (res.getString("Redeemable Inquiry"));
  }

  /**
   * put your documentation comment here
   * @param anEvent
   */
  public void appButtonEvent(CMSActionEvent anEvent) {
    String sAction = anEvent.getActionCommand();
    if (sAction.equals("PRINT")) {
      if (appModel != null) {
        appModel.printReceipt(theAppMgr);
        if (ReceiptConfigInfo.getInstance().isProducingRDO()) {
          String txnClassName = appModel.getClass().getName();
          String shortClassName = txnClassName.substring(txnClassName.lastIndexOf(".") + 1
              , txnClassName.length());
          String fileName = ReceiptConfigInfo.getInstance().getPathForRDO() + shortClassName
              + ".rdo";
          try {
            ObjectStore objectStore = new ObjectStore(fileName);
            objectStore.write(appModel);
          } catch (Exception e) {
            System.out.println("exception on writing object to blueprint folder: " + e);
          }
        }
      }
    }
    //MP: For Redeemable Card Bldr.
    else if (sAction.equals("REDEEM_CARD")) {
      theAppMgr.unRegisterSingleEditArea(); // remove any builders in process
      ConfigMgr config = new ConfigMgr("payment.cfg");
      final String Builder = config.getString("REDEEMABLE_INQUIRY_BUILDER");
      System.out.println("the builder is: " + Builder);
      SwingUtilities.invokeLater(new Runnable() {

        /**
         * put your documentation comment here
         */
        public void run() {
          theAppMgr.buildObject("REDEEMABLE_INQUIRY", Builder, "");
        }
      });
    }
    //theAppMgr.setEditAreaFocus();
  }

  //    /**
   //     */
  //    public void editAreaEvent (String aCommand, String aValue)
  //    {
  //        try
  //        {
  //            if(aCommand.equals("REDEEMABLE_ID"))
  //            {
  //                com.chelseasystems.cr.appmgr.mask.TransIDMask mask = new com.chelseasystems.cr.appmgr.mask.TransIDMask();
  //                String redeemableId = ((String)mask.validateMask(theAppMgr, aValue)).toUpperCase();
  //                Redeemable received = CMSRedeemableHelper.findRedeemable(theAppMgr, redeemableId);
  //                if(received != null)
  //                {
  //                    populateScreen(received);
  //                }
  //                else
  //                {
  //                    if(theAppMgr.isOnLine())
  //                        theAppMgr.showErrorDlg(res.getString("Redeemable not found"));
  //                    else
  //                        theAppMgr.showErrorDlg(res.getString("This request cannot be completed while in offline mode.  Please try later."));
  //
  //                    clearFields();
  //                }
  //
  //
  //            }
  //        }
  //        catch(Exception ex)
  //        {
  //            theAppMgr.showExceptionDlg(ex);
  //        }
  //    }
  //
  /**
   */
  private void populateScreen(Redeemable redeemable) {
    this.clearFields();
    appModel = new CMSRedeemableBalanceAppModel(redeemable);
    jtfId.setText(appModel.getRedeemableID());
    jtfPurchaseDate.setText(df.format(appModel.getCreationDate()));
    jtfCustomer.setText(appModel.getCustomerName());
    //        jtfPhone.setText(appModel.ge.getCustomerPhoneNumber());
    jtfPhone.setText(appModel.getCustomerId());
    jtfIssueAmt.setText(appModel.getIssueAmount().formattedStringValue());
    jtfId.setText(appModel.getRedeemableID());
    jtfType.setText(appModel.getGUIPaymentName());
    /**
     * Need to add method
     */
    if (redeemable instanceof RewardCard) {
      RewardCard reward = (RewardCard)redeemable;
      jtfId.setText(reward.getId());
      jtfPhone.setText(reward.getCustomerId());
      String dateString = null;
      try {
        SimpleDateFormat df = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
        dateString = df.format(reward.getExpDate());
      } catch (Exception e) {}
      jtfExpDate.setText(dateString);
    } else
      jtfExpDate.setText("");
    //        try
    //        {
    jtfCurrentAmt.setText(appModel.getRemainingBalance().formattedStringValue());
    //        }
    //        catch(CurrencyException ce)
    //        {
    //            jtfCurrentAmt.setText("");
    //            System.err.println("RedeemableBuyBackApplet.displayRedeemable()->" + ce);
    //        }
    Vector history = appModel.getRedemptionHistory();
    if (history != null) {
      for (Iterator it = history.iterator(); it.hasNext(); ) {
        RedeemableHist redeemHist = (RedeemableHist)it.next();
        vecRedeemableTxnIds.addElement(redeemHist.getTransactionIdUsed());
        model.addRow(redeemHist);
      }
    }
    tblHist.repaint();
  }

  // MP: Chnged the signature.
  public void clickEvent(MouseEvent me, String val) {
    RedeemableHist redeemHist = model.getRedeemableHist(tblHist.getSelectedRow());
    if (redeemHist != null)
      try {
        PaymentTransaction theTxn = (PaymentTransaction)CMSTransactionPOSHelper.findById(theAppMgr
            , redeemHist.getTransactionIdUsed());
        if (theTxn != null) {
          theAppMgr.addStateObject("THE_TXN", theTxn);
          theAppMgr.addStateObject("TXN_HEADER_ROW", new Integer(tblHist.getSelectedRow()));
          theAppMgr.addStateObject("ARM_TXN_IDS_FOUND"
              , (String[])vecRedeemableTxnIds.toArray(new String[vecRedeemableTxnIds.size()]));
          theAppMgr.fireButtonEvent("OK");
        } else {
          theAppMgr.showErrorDlg(res.getString("Cannot find transaction."));
        }
      } catch (Exception ex) {
        theAppMgr.showExceptionDlg(ex);
      }
  }

  /**
   */
  private void clearFields() {
    jtfId.setText("");
    jtfPurchaseDate.setText("");
    jtfCustomer.setText("");
    jtfPhone.setText("");
    jtfIssueAmt.setText("");
    jtfCurrentAmt.setText("");
    jtfType.setText("");
    jtfExpDate.setText("");
    model.clear();
    tblHist.repaint();
    appModel = null;
  }

  /**
   * callback when <b>Page Down</b> icon is clicked
   */
  public void pageDown(MouseEvent e) {
    if (model.getRowCount() == 0)
      return;
    model.nextPage();
    theAppMgr.showPageNumber(e, model.getCurrentPageNumber() + 1, model.getPageCount());
  }

  /**
   * callback when <b>Page Up</b> icon is clicked
   */
  public void pageUp(MouseEvent e) {
    if (model.getRowCount() == 0)
      return;
    model.prevPage();
    theAppMgr.showPageNumber(e, model.getCurrentPageNumber() + 1, model.getPageCount());
  }

  // MP: For handling obj event.
  public void objectEvent(String Command, Object obj) {
    if (Command.equals("REDEEMABLE_INQUIRY")) {
      if (obj != null) {
        populateScreen((Redeemable)obj);
        RedeemableInquiryApplet.this.appButtonEvent(new CMSActionEvent(this, 0, "REDEEM_CARD", 0));
      }
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

