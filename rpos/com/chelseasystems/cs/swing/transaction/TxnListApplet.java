/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.transaction;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cs.swing.panel.TxnHistPanel;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cr.swing.bean.JCMSLabel;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.receipt.IReceipt;


/**
 * put your documentation comment here
 */
public class TxnListApplet extends CMSApplet {
  //public class TxnListApplet extends JApplet {
  private CMSTransactionHeader[] headers = null;
  private String callingAppletName = null;
  JLabel dateLbl = new JLabel(res.getString("Date"));
  JLabel miscLbl = new JLabel(res.getString("View Txns for"));
  TxnHistPanel pnlTxnHist = new TxnHistPanel();

  //Construct the applet
  public TxnListApplet() {
  }

  //Initialize the applet
  public void init() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //Component initialization
  private void jbInit()
      throws Exception {
    JPanel pnlWest = new JPanel();
    JPanel pnlEast = new JPanel();
    JPanel pnlNorth = new JPanel();
    JPanel pnlSouth = new JPanel();
    this.getContentPane().add(pnlWest, BorderLayout.WEST);
    this.getContentPane().add(pnlEast, BorderLayout.EAST);
    this.getContentPane().add(pnlNorth, BorderLayout.NORTH);
    this.getContentPane().add(pnlSouth, BorderLayout.SOUTH);
    this.getContentPane().add(pnlTxnHist, BorderLayout.CENTER);
    pnlWest.setBackground(theAppMgr.getBackgroundColor());
    pnlEast.setBackground(theAppMgr.getBackgroundColor());
    pnlNorth.setBackground(theAppMgr.getBackgroundColor());
    pnlNorth.add(miscLbl, null);
    pnlNorth.add(dateLbl, null);
    pnlSouth.setBackground(theAppMgr.getBackgroundColor());
    pnlTxnHist.setAppMgr(theAppMgr);
    pnlTxnHist.addMouseListener(new MouseAdapter() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void mouseClicked(MouseEvent e) {
        clickEvent(e);
      }
    });
    dateLbl.setFont(theAppMgr.getTheme().getTextFieldFont());
    miscLbl.setFont(theAppMgr.getTheme().getTextFieldFont());
    pnlWest.setPreferredSize(new Dimension((int)(5 * r), (int)(10 * r)));
    pnlEast.setPreferredSize(new Dimension((int)(5 * r), (int)(10 * r)));
    pnlNorth.setPreferredSize(new Dimension((int)(5 * r), (int)(35 * r)));
    pnlSouth.setPreferredSize(new Dimension((int)(5 * r), (int)(10 * r)));
  }

  //Start the applet
  public void start() {
    theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
    // show buttons
    theOpr = (CMSEmployee)theAppMgr.getStateObject("OPERATOR");
    theAppMgr.showMenu(MenuConst.PREV_ONLY, theOpr, theAppMgr.CANCEL_BUTTON);
    String titleStr = (String)theAppMgr.getStateObject("TITLE_STRING");
    String dateStr = (String)theAppMgr.getStateObject("DATE_STRING");
    miscLbl.setText(titleStr + " " + res.getString("on"));
    dateLbl.setText(dateStr);
    headers = (CMSTransactionHeader[])theAppMgr.getStateObject("TXN_HEADER");
    populateScreen();
    theAppMgr.setSingleEditArea(res.getString("Select a transaction to view detail."));
  }

  /**
   * put your documentation comment here
   * @param anEvent
   */
  public void appButtonEvent(CMSActionEvent anEvent) {
    String sAction = anEvent.getActionCommand();
    if (sAction.equals("PREV")) {
      theAppMgr.removeStateObject("TXN_HEADER");
    }
  }

  //Stop the applet
  public void stop() {}

  //Get Applet information
  public String getScreenName() {
    return res.getString("Transaction List");
  }

  //Get Applet information
  public String getVersion() {
    return "$Revision: 1.3.2.1 $";
  }

  /**
   * put your documentation comment here
   */
  private void populateScreen() {
    pnlTxnHist.clear();
    pnlTxnHist.setTxnHeaders(headers);
  }

  /**
   * @param customerId
   */
  public TransactionHeader[] getHeaders(String customerId) {
    try {
      headers = CMSTransactionPOSHelper.findByCustomerIdHeader(theAppMgr, customerId);
    } catch (Exception ex) {
      theAppMgr.showExceptionDlg(ex);
    }
    return headers;
  }

  /**
   * callback when <b>Page Down</b> icon is clicked
   */
  public void pageDown(MouseEvent e) {
    pnlTxnHist.nextPage();
    theAppMgr.showPageNumber(e, pnlTxnHist.getCurrentPageNumber() + 1, pnlTxnHist.getTotalPages());
  }

  /**
   * callback when <b>Page Up</b> icon is clicked
   */
  public void pageUp(MouseEvent e) {
    pnlTxnHist.prevPage();
    theAppMgr.showPageNumber(e, pnlTxnHist.getCurrentPageNumber() + 1, pnlTxnHist.getTotalPages());
  }

  /**
   * put your documentation comment here
   * @param me
   */
  public void clickEvent(MouseEvent me) {
    TransactionHeader header = pnlTxnHist.getSelectedTransactionHeader();
    if (header != null)
      txnHdrSelected(header);
  }

  /**
   * put your documentation comment here
   * @param header
   */
  public void txnHdrSelected(TransactionHeader header) {
    try {
      PaymentTransaction theTxn = (PaymentTransaction)CMSTransactionPOSHelper.findById(theAppMgr
          , header.getId());
      if (theTxn != null) {
        //String txnType = header.getTransactionType();
        /*if ((txnType.equalsIgnoreCase("EOD")) || (txnType.equalsIgnoreCase("PHINV"))
         || (txnType.equalsIgnoreCase("RCPT")) || (txnType.equalsIgnoreCase("SOD"))
         || (txnType.equalsIgnoreCase("FCHG")) || (txnType.equalsIgnoreCase("FDEL"))) {
         theAppMgr.showErrorDlg("Transaction type not viewable.");*/
        //else
        theAppMgr.addStateObject("TXN_HEADER_LIST", headers);
        theAppMgr.addStateObject("TXN_HEADER_ROW", new Integer(pnlTxnHist.getSelectedRow()));
        theAppMgr.addStateObject("THE_TXN", theTxn);
        theAppMgr.fireButtonEvent("OK");
      } else {
        theAppMgr.showErrorDlg(res.getString("Cannot find transaction."));
      }
    } catch (Exception ex) {
      theAppMgr.showExceptionDlg(ex);
    }
  }
}

