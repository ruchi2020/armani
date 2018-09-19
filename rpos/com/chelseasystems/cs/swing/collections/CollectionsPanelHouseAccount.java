/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04/19/2005 | Anand     | N/A       | Customizations as per Collections Specifications   |
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.collections;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.*;
import com.chelseasystems.cr.collection.CollectionTransaction;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.swing.JColorScrollBar;
import com.chelseasystems.cr.swing.bean.JCMSTextArea;
import com.chelseasystems.cr.swing.event.TextCompFocusListener;
import com.chelseasystems.cs.collection.CMSMiscCollection;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.paidouts.AmountTxtFld;
import com.chelseasystems.cr.swing.bean.JCMSTextField;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.payment.HouseAccount;
import java.awt.*;


/**
 */
public class CollectionsPanelHouseAccount extends CollectionsPanel {
  private AmountTxtFld amtFld = null;
  private JPanel jPanel9;
  private JCMSTextField accountFld;
  JCMSTextArea commentTxtArea = new JCMSTextArea();
  JScrollPane pane = new JScrollPane(commentTxtArea);

  /**
   * @param    CollectionsApplet owner
   * @param    String displayName
   */
  public CollectionsPanelHouseAccount(CollectionsApplet owner, String displayName) {
    super(owner, displayName);
    try {
      amtFld = new AmountTxtFld(owner.theAppMgr);
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   */
  public void jbInit() {
    FlowLayout flowLayout1 = new FlowLayout();
    JPanel jPanel1 = new JPanel();
    JPanel jPanel2 = new JPanel();
    JPanel jPanel3 = new JPanel();
    JLabel jLabel3 = new JLabel();
    JLabel jLabel2 = new JLabel();
    JLabel jLabel4 = new JLabel();
    JPanel jPanel5 = new JPanel();
    JPanel jPanel6 = new JPanel();
    JPanel jPanel8 = new JPanel();
    JPanel jPanel10 = new JPanel();
    jPanel10.setLayout(new BorderLayout());
    jPanel9 = new JPanel();
    accountFld = new JCMSTextField();
    commentTxtArea.addFocusListener(new TextCompFocusListener());
    this.setLayout(new BorderLayout());
    jPanel1.setBackground(owner.theAppMgr.getBackgroundColor());
    jPanel1.setLayout(new BorderLayout());
    commentTxtArea.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel2.setBackground(owner.theAppMgr.getBackgroundColor());
    jPanel2.setLayout(new BorderLayout());
    commentTxtArea.setSelectionColor(new java.awt.Color(0, 0, 128));
    jPanel3.setOpaque(false);
    jPanel3.setLayout(flowLayout1);
    commentTxtArea.setAppMgr(owner.theAppMgr);
    amtFld.setFont(owner.theAppMgr.getTheme().getTextFieldFont());
    jLabel3.setFont(owner.theAppMgr.getTheme().getLabelFont());
    jLabel2.setFont(owner.theAppMgr.getTheme().getLabelFont());
    flowLayout1.setAlignment(0);
    jLabel2.setText(owner.res.getString("Comments") + ":");
    jLabel2.setVerticalAlignment(3);
    jPanel5.setOpaque(false);
    jPanel6.setOpaque(false);
    jPanel8.setOpaque(false);
    jPanel10.setOpaque(false);
    jPanel9.setOpaque(false);
    jLabel3.setVerticalAlignment(3);
    jLabel3.setText(owner.res.getString("Collected Amount") + ":");
    jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel3.setPreferredSize(new Dimension((int)(250 * owner.r), (int)(15 * owner.r)));
    jLabel4.setPreferredSize(new Dimension((int)(250 * owner.r), (int)(15 * owner.r)));
    jLabel4.setVerticalAlignment(3);
    jLabel4.setText(owner.res.getString("Account No") + ":");
    jLabel4.setFont(owner.theAppMgr.getTheme().getLabelFont());
    jLabel4.setHorizontalAlignment(SwingConstants.RIGHT);
    this.add(jPanel1, BorderLayout.SOUTH);
    jPanel1.add(jPanel6, BorderLayout.CENTER);
    jPanel6.add(jLabel2, null);
    jPanel1.add(jPanel5, BorderLayout.SOUTH);
    jPanel5.add(pane, null);
    this.add(jPanel2, BorderLayout.NORTH);
    jPanel2.add(jPanel10, BorderLayout.WEST);
    jPanel10.add(jPanel8, BorderLayout.NORTH);
    jPanel10.add(jPanel9, BorderLayout.SOUTH);
    jPanel8.setLayout(new BorderLayout());
    jPanel8.setVisible(true);
    jPanel8.add(jLabel3, BorderLayout.WEST);
    jPanel8.add(amtFld, BorderLayout.CENTER);
    jPanel9.setLayout(new BorderLayout());
    jPanel9.add(jLabel4, BorderLayout.WEST);
    jPanel9.add(accountFld, BorderLayout.CENTER);
    pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    pane.setVerticalScrollBar(new JColorScrollBar(owner.theAppMgr.getTheme().getBackground()));
    this.setPreferredSize(new Dimension((int)(580 * owner.r), (int)(160 * owner.r)));
    jPanel2.setPreferredSize(new Dimension((int)(10 * owner.r), (int)(75 * owner.r)));
    pane.setPreferredSize(new Dimension((int)(740 * owner.r), (int)(93 * owner.r)));
    amtFld.setPreferredSize(new Dimension((int)(340 * owner.r), (int)(25 * owner.r)));
    accountFld.setPreferredSize(new Dimension((int)(340 * owner.r), (int)(25 * owner.r)));
    jLabel2.setPreferredSize(new Dimension((int)(740 * owner.r), (int)(15 * owner.r)));
    jPanel6.setPreferredSize(new Dimension((int)(210 * owner.r), (int)(20 * owner.r)));
    jPanel8.setPreferredSize(new Dimension((int)(500 * owner.r), (int)(25 * owner.r)));
    jPanel9.setPreferredSize(new Dimension((int)(500 * owner.r), (int)(25 * owner.r)));
    jPanel10.setPreferredSize(new Dimension((int)(500 * owner.r), (int)(75 * owner.r)));
  }

  /**
   * put your documentation comment here
   */
  public void initialFocus() {
    SwingUtilities.invokeLater(new Runnable() {

      /**
       * put your documentation comment here
       */
      public void run() {
        String sReasonCode = (String)CMSApplet.theAppMgr.getStateObject("REASON_CODE");
        if (CMSApplet.theAppMgr.getStateObject("TXN_POS") == null
            && CMSApplet.theAppMgr.getStateObject("TXN_CUSTOMER") == null)
          clearFields();
        SwingUtilities.invokeLater(new Runnable() {

          /**
           * put your documentation comment here
           */
          public void run() {
            amtFld.requestFocus();
            //accountFld.setEditable(false);
          }
        });
      }
    });
    this.repaint();
  }

  /**
   */
  public void clearFields() {
    amtFld.setText("");
    commentTxtArea.setText("");
    accountFld.setText("");
  }

  /**
   * put your documentation comment here
   */
  public void setVisible() {
    jPanel9.setVisible(true);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public JCMSTextField getAccountFld() {
    return this.accountFld;
  }

  /**
   * @return
   * @exception BusinessRuleException
   */
  public CollectionTransaction exportData()
      throws BusinessRuleException {
    //checking if any required field is missing
    ArmCurrency amount = new ArmCurrency(amtFld.getText().trim());
    if (amtFld.getText().length() <= 0 || amount.doubleValue() == 0.00) {
      amtFld.requestFocus();
//      owner.theAppMgr.showErrorDlg(owner.res.getString("Amount must be a value greater than zero"));
      if(owner.getCMSCustomer()== null)
       owner.theAppMgr.showErrorDlg(owner.res.getString("Check the amount entered and associate a customer to this transaction."));
      else
       owner.theAppMgr.showErrorDlg(owner.res.getString("Check the amount entered."));
      return null;
    }
    if (!amtFld.verifyAmount())
      return null;
    if (owner.getHouseAccount() == null) {
      CMSApplet.theAppMgr.showErrorDlg(CMSApplet.res.getString(
          "Please select an House Account to continue"));
      return null;
    }
    if (owner.getCMSCustomer() == null) {
      CMSApplet.theAppMgr.showErrorDlg(CMSApplet.res.getString(
          "Please select a customer to continue"));
      return null;
    }
    String sReasonCode = (String)CMSApplet.theAppMgr.getStateObject("REASON_CODE");
    CMSMiscCollection txn = new CMSMiscCollection(sReasonCode, (CMSStore)owner.theStore);
    txn.setAmount(amount);
    txn.setComment(commentTxtArea.getText());
    txn.setCustomer(owner.getCMSCustomer());
    if (owner.getHouseAccount() != null) {
      txn.setRedeemable((HouseAccount)owner.getHouseAccount());
      try {
        ((HouseAccount)txn.getRedeemable()).setIssueAmount((owner.getHouseAccount().getIssueAmount()).
            add(txn.getAmount()));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return txn;
  }
}

