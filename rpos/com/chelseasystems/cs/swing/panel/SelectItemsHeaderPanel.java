/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.appmgr.*;
import java.awt.*;
import javax.swing.*;


/**
 * put your documentation comment here
 */
public class SelectItemsHeaderPanel extends JPanel {
  JCMSLabel jcmslblHeader = new JCMSLabel();
  JPanel pnlEast = new JPanel();
  JPanel pnlWest = new JPanel();
  JCMSLabel jcmslblCustomer = new JCMSLabel();
  JCMSLabel jcmslblID = new JCMSLabel();
  JCMSLabel jcmslblTxnID = new JCMSLabel();
  JCMSLabel jcmslblRefID = new JCMSLabel();
  JCMSLabel jcmslblCasheirID = new JCMSLabel();
  JCMSLabel jcmslblCustValue = new JCMSLabel();
  JCMSLabel jcmsIDValue = new JCMSLabel();
  JCMSLabel jcmslblTxnIDValue = new JCMSLabel();
  JCMSLabel jcmslblRefIDValue = new JCMSLabel();
  JCMSLabel jcmslblCashierIDValue = new JCMSLabel();

  /**
   * put your documentation comment here
   */
  public SelectItemsHeaderPanel() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    Font fontTitle = theAppMgr.getTheme().getTextFieldFont();
    Font fontLabel = theAppMgr.getTheme().getLabelFont();
    this.setBackground(theAppMgr.getBackgroundColor());
    pnlEast.setBackground(theAppMgr.getBackgroundColor());
    pnlWest.setBackground(theAppMgr.getBackgroundColor());
    jcmslblHeader.setPreferredSize(new Dimension((int)(85 * r), (int)(15 * r)));
    jcmslblTxnID.setPreferredSize(new Dimension((int)(85 * r), (int)(15 * r)));
    jcmslblTxnIDValue.setPreferredSize(new Dimension((int)(85 * r), (int)(15 * r)));
    jcmslblCustomer.setPreferredSize(new Dimension((int)(85 * r), (int)(15 * r)));
    jcmslblCustValue.setPreferredSize(new Dimension((int)(85 * r), (int)(15 * r)));
    jcmslblID.setPreferredSize(new Dimension((int)(85 * r), (int)(15 * r)));
    jcmsIDValue.setPreferredSize(new Dimension((int)(85 * r), (int)(15 * r)));
    jcmslblCasheirID.setPreferredSize(new Dimension((int)(85 * r), (int)(15 * r)));
    jcmslblCashierIDValue.setPreferredSize(new Dimension((int)(85 * r), (int)(15 * r)));
    jcmslblRefID.setPreferredSize(new Dimension((int)(85 * r), (int)(15 * r)));
    jcmslblRefIDValue.setPreferredSize(new Dimension((int)(85 * r), (int)(15 * r)));
    jcmslblHeader.setAppMgr(theAppMgr);
    jcmslblTxnID.setAppMgr(theAppMgr);
    jcmslblTxnIDValue.setAppMgr(theAppMgr);
    jcmslblCustomer.setAppMgr(theAppMgr);
    jcmslblCustValue.setAppMgr(theAppMgr);
    jcmslblID.setAppMgr(theAppMgr);
    jcmsIDValue.setAppMgr(theAppMgr);
    jcmslblCasheirID.setAppMgr(theAppMgr);
    jcmslblCashierIDValue.setAppMgr(theAppMgr);
    jcmslblRefID.setAppMgr(theAppMgr);
    jcmslblRefIDValue.setAppMgr(theAppMgr);
    jcmslblHeader.setFont(theAppMgr.getTheme().getHeaderFont());
    jcmslblTxnID.setFont(fontTitle);
    jcmslblCustomer.setFont(fontTitle);
    jcmslblID.setFont(fontTitle);
    jcmslblCasheirID.setFont(fontTitle);
    jcmslblCustValue.setFont(fontTitle);
    jcmsIDValue.setFont(fontTitle);
    jcmslblTxnIDValue.setFont(fontTitle);
    jcmslblCashierIDValue.setFont(fontTitle);
    jcmslblRefID.setFont(fontTitle);
    jcmslblRefIDValue.setFont(fontTitle);
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setHeaderLabel(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    jcmslblHeader.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setCustomerLabel(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    jcmslblCustomer.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setIDLabel(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    jcmslblID.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setTxnLabel(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    jcmslblTxnID.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setCashierLabel(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    jcmslblCasheirID.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setCustomer(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    jcmslblCustValue.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setTransactionID(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    jcmslblTxnIDValue.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setCustomerID(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    jcmsIDValue.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setCashierID(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    jcmslblCashierIDValue.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setRefLabel(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    jcmslblRefID.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setRefID(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    //jcmslblRefIDValue.setText(sValue);
    jcmslblRefID.setText(jcmslblRefID.getText() + "  " + sValue);
  }

  /**
   * put your documentation comment here
   */
  public void reset() {
    jcmslblCustValue.setText("");
    jcmsIDValue.setText("");
    jcmslblTxnIDValue.setText("");
    jcmslblCashierIDValue.setText("");
    jcmslblRefIDValue.setText("");
    doLayout();
  }

  /**
   * put your documentation comment here
   * @exception Exception
   */
  private void jbInit()
      throws Exception {
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    pnlWest.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ""));
    pnlWest.setPreferredSize(new Dimension(422, 118));
    pnlWest.setRequestFocusEnabled(true);
    pnlEast.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ""));
    jcmslblHeader.setText(res.getString("Pre-Sale Details"));
    pnlWest.setLayout(null);
    this.setLayout(new BorderLayout());
    jcmslblHeader.setBounds(new Rectangle(4, 4, 155, 17));
    jcmslblCustomer.setBounds(new Rectangle(8, 21, 100, 18));
    jcmslblCustValue.setBounds(new Rectangle(104, 21, 167, 18));
    jcmslblID.setBounds(new Rectangle(8, 39, 100, 17));
    jcmsIDValue.setBounds(new Rectangle(104, 39, 168, 18));
    jcmslblTxnID.setBounds(new Rectangle(8, 57, 100, 18));
    jcmslblTxnIDValue.setBounds(new Rectangle(104, 57, 169, 18));
    jcmslblRefID.setBounds(new Rectangle(8, 75, 252, 18));
    jcmslblRefIDValue.setBounds(new Rectangle(289, 74, 13, 18));
    jcmslblCasheirID.setBounds(new Rectangle(8, 93, 100, 18));
    jcmslblCashierIDValue.setBounds(new Rectangle(104, 93, 168, 18));
    this.add(pnlWest, BorderLayout.WEST);
    this.add(pnlEast, BorderLayout.CENTER);
    this.setPreferredSize(new Dimension(844, 118));
    jcmslblCustomer.setText(res.getString("Customer:"));
    jcmslblID.setText(res.getString("ID:"));
    jcmslblTxnID.setText(res.getString("Txn ID:"));
    jcmslblCasheirID.setText(res.getString("Cashier ID:"));
    jcmslblRefID.setText(res.getString("Ref ID:"));
    pnlWest.add(jcmslblHeader, null);
    pnlWest.add(jcmslblCustomer, null);
    pnlWest.add(jcmslblID, null);
    pnlWest.add(jcmslblTxnID, null);
    pnlWest.add(jcmslblRefID, null);
    pnlWest.add(jcmslblCasheirID, null);
    pnlWest.add(jcmslblCustValue, null);
    pnlWest.add(jcmsIDValue, null);
    pnlWest.add(jcmslblTxnIDValue, null);
    pnlWest.add(jcmslblCashierIDValue, null);
    pnlWest.add(jcmslblRefIDValue, null);
  }
}

