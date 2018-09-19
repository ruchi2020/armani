/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04/19/2005 | Anand     | N/A       | Customizations as per Paid-Out Specifications.     |
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.paidouts;

import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.paidout.PaidOutTransaction;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.swing.JColorScrollBar;
import com.chelseasystems.cr.swing.bean.JCMSTextArea;
import com.chelseasystems.cr.swing.event.TextCompFocusListener;
import com.chelseasystems.cs.paidout.CMSMiscPaidOut;
import com.chelseasystems.cs.store.CMSStore;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.*;
import com.chelseasystems.cs.customer.CMSCustomer;


/**
 */
public class PaidOutPanelCMSMiscPaidOut extends PaidOutPanel {
  private AmountTxtFld amtFld = null;
  JCMSTextArea commentTxtArea = new JCMSTextArea();
  JScrollPane pane = new JScrollPane(commentTxtArea);

  /**
   * @param    PaidoutApplet owner
   * @param    String displayName
   */
  public PaidOutPanelCMSMiscPaidOut(PaidoutApplet owner, String displayName) {
    super(owner, displayName);
    try {
      amtFld = new AmountTxtFld(owner.getAppMgr());
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   */
  public void jbInit() {
    JPanel jPanel1 = new JPanel();
    JPanel jPanel2 = new JPanel();
    JPanel jPanel3 = new JPanel();
    JLabel jLabel3 = new JLabel();
    FlowLayout flowLayout1 = new FlowLayout();
    JLabel jLabel2 = new JLabel();
    JPanel jPanel5 = new JPanel();
    JPanel jPanel6 = new JPanel();
    JPanel jPanel8 = new JPanel();
    commentTxtArea.addFocusListener(new TextCompFocusListener());
    this.setLayout(new BorderLayout());
    jPanel1.setBackground(owner.getAppMgr().getBackgroundColor());
    jPanel1.setLayout(new BorderLayout());
    commentTxtArea.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel2.setBackground(owner.getAppMgr().getBackgroundColor());
    jPanel2.setLayout(new BorderLayout());
    commentTxtArea.setSelectionColor(new java.awt.Color(0, 0, 128));
    jPanel3.setOpaque(false);
    jPanel3.setLayout(flowLayout1);
    commentTxtArea.setAppMgr(owner.theAppMgr);
    amtFld.setFont(owner.theAppMgr.getTheme().getTextFieldFont());
    jLabel3.setFont(owner.theAppMgr.getTheme().getLabelFont());
    jLabel2.setFont(owner.theAppMgr.getTheme().getLabelFont());
    flowLayout1.setAlignment(0);
    jLabel2.setText(owner.res.getString("Comments:"));
    jLabel2.setVerticalAlignment(3);
    jPanel5.setOpaque(false);
    jPanel6.setOpaque(false);
    jPanel8.setOpaque(false);
    jLabel3.setVerticalAlignment(3);
    jLabel3.setText(owner.res.getString("Paidout Amount") + ":");
    this.setPreferredSize(new Dimension((int)(580 * owner.r), (int)(160 * owner.r)));
    jPanel2.setPreferredSize(new Dimension((int)(10 * owner.r), (int)(75 * owner.r)));
    //commentTxtArea.setPreferredSize(new Dimension((int)(740*owner.r), (int)(93*owner.r)));
    pane.setPreferredSize(new Dimension((int)(740 * owner.r), (int)(93 * owner.r)));
    jPanel3.setPreferredSize(new Dimension((int)(10 * owner.r), (int)(20 * owner.r)));
    amtFld.setPreferredSize(new Dimension((int)(250 * owner.r), (int)(25 * owner.r)));
    jLabel2.setPreferredSize(new Dimension((int)(740 * owner.r), (int)(15 * owner.r)));
    jPanel6.setPreferredSize(new Dimension((int)(210 * owner.r), (int)(20 * owner.r)));
    jPanel8.setPreferredSize(new Dimension((int)(340 * owner.r), (int)(10 * owner.r)));
    jLabel3.setPreferredSize(new Dimension((int)(250 * owner.r), (int)(15 * owner.r)));
    this.add(jPanel1, BorderLayout.CENTER);
    jPanel1.add(jPanel6, BorderLayout.NORTH);
    jPanel6.add(jLabel2, null);
    jPanel1.add(jPanel5, BorderLayout.CENTER);
    //jPanel5.add(commentTxtArea, null);
    jPanel5.add(pane, null);
    this.add(jPanel2, BorderLayout.NORTH);
    jPanel2.add(jPanel3, BorderLayout.NORTH);
    jPanel2.add(jPanel8, BorderLayout.WEST);
    jPanel8.add(jLabel3, null);
    jPanel8.add(amtFld, null);
    pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    pane.setVerticalScrollBar(new JColorScrollBar(owner.getAppMgr().getTheme().getBackground()));
    registerTextFields();
  }

  /**
   * Method that prepares the included text fields for editing.
   */
  public void registerTextFields() {}

  /**
   */
  public void initialFocus() {
    SwingUtilities.invokeLater(new Runnable() {

      /**
       */
      public void run() {
        clearFields();
        amtFld.requestFocus();
      }
    });
  }

  /**
   */
  public void clearFields() {
    amtFld.setText("");
    commentTxtArea.setText("");
  }

  /**
   * @return
   * @exception BusinessRuleException
   */
  public PaidOutTransaction exportData()
      throws BusinessRuleException {
    //checking if any required field is missing
    ArmCurrency amount = new ArmCurrency(amtFld.getText().trim());
    if (amtFld.getText().length() <= 0 || amount.doubleValue() == 0.00) {
      amtFld.requestFocus();
      owner.getAppMgr().showErrorDlg(owner.res.getString("Amount must be a value greater than zero"));
      return null;
    }
    if (commentTxtArea.getText().length() <= 0) {
      commentTxtArea.requestFocus();
      owner.getAppMgr().showErrorDlg(owner.res.getString(
          "The required information is not complete."));
      return null;
    }
    if (!amtFld.verifyAmount())
      return null;
    CMSMiscPaidOut txn = new CMSMiscPaidOut("MISC_PAID_OUT", (CMSStore)owner.theStore);
    amount = amount.multiply(-1);
    txn.setAmount(amount);
    txn.setComment(commentTxtArea.getText());
    CMSCustomer cmsCustomer= (CMSCustomer)owner.getAppMgr().getStateObject("TXN_CUSTOMER");
    txn.setCustomer(cmsCustomer);
    return txn;
  }
}

