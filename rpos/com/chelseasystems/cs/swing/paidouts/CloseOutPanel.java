/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.paidouts;

/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04/19/2005 | Anand     | N/A       | Customizations as per Collections Specifications   |
 --------------------------------------------------------------------------------------------------
 */
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
import java.awt.GridBagConstraints;
import com.chelseasystems.cr.swing.bean.JCMSLabel;
import com.chelseasystems.cr.swing.bean.JCMSTextField;
import java.awt.GridBagLayout;
import java.awt.Insets;
import com.chelseasystems.cr.swing.CMSApplet;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import com.chelseasystems.cr.swing.bean.DateChooserUI;


/**
 */
public class CloseOutPanel extends PaidOutPanel {
  private AmountTxtFld amtFld = null;
  JCMSTextArea commentTxtArea = new JCMSTextArea();
  JScrollPane pane = new JScrollPane(commentTxtArea);
  private JPanel jPanel9;
  //    JCMSTextArea commentTxtArea = new JCMSTextArea();
  JCMSTextField txtCustId = new JCMSTextField();
  JCMSTextField txtName = new JCMSTextField();
  JCMSTextField txtFamilyName = new JCMSTextField();
  private JCMSLabel lblCustId;
  private JCMSLabel lblFamilyName;
  private ArmCurrency prevBalance = new ArmCurrency("");
  private JCMSLabel lblName;
  private JCMSLabel lblCollectedAmount;
  private JCMSTextField txtFiscalReceipt;
  private JCMSTextField txtPrevBalance;
  private JCMSLabel lblComments;

  /**
   * @param    PaidoutApplet owner
   * @param    String displayName
   */
  public CloseOutPanel(PaidoutApplet owner, String displayName) {
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
    JPanel pnlComponents = new JPanel();
    lblCustId = new JCMSLabel();
    lblFamilyName = new JCMSLabel();
    txtCustId = new JCMSTextField();
    txtFamilyName = new JCMSTextField();
    lblName = new JCMSLabel();
    txtName = new JCMSTextField();
    lblCollectedAmount = new JCMSLabel();
    txtPrevBalance = new JCMSTextField();
    lblComments = new JCMSLabel();
    lblCustId.setFont(CMSApplet.theAppMgr.getTheme().getLabelFont());
    lblFamilyName.setFont(CMSApplet.theAppMgr.getTheme().getLabelFont());
    lblCollectedAmount.setFont(CMSApplet.theAppMgr.getTheme().getLabelFont());
    txtPrevBalance.setFont(CMSApplet.theAppMgr.getTheme().getLabelFont());
    lblComments.setFont(CMSApplet.theAppMgr.getTheme().getLabelFont());
    lblName.setFont(CMSApplet.theAppMgr.getTheme().getLabelFont());
    lblCustId.setText(CMSApplet.res.getString("Customer Id")+": ");
    lblFamilyName.setText("Family Name: ");
    lblName.setText("Name : ");
    lblCollectedAmount.setText("Paid-Out Amount: ");
    txtPrevBalance.setText("Available Balance: ");
    lblComments.setText("Comments");
    txtCustId.setBackground((CMSApplet.theAppMgr).getBackgroundColor());
    txtFamilyName.setBackground((CMSApplet.theAppMgr).getBackgroundColor());
    txtName.setBackground((CMSApplet.theAppMgr).getBackgroundColor());
    amtFld.setBackground((CMSApplet.theAppMgr).getBackgroundColor());
    txtPrevBalance.setBackground((CMSApplet.theAppMgr).getBackgroundColor());
    txtPrevBalance.setBorder(BorderFactory.createEmptyBorder());
    this.setLayout(new BorderLayout());
    //GridBagLayout gb1 = new GridBagLayout();
    pnlComponents.setLayout(new GridBagLayout());
    //pnlComponents.setPreferredSize(new Dimension(833, 300));
    pnlComponents.setBackground(owner.theAppMgr.getBackgroundColor());
    commentTxtArea.setBorder(BorderFactory.createLoweredBevelBorder());
    commentTxtArea.setSelectionColor(new Color(0, 0, 128));
    pane.setHorizontalScrollBarPolicy(31);
    pane.setVerticalScrollBar(new JColorScrollBar(CMSApplet.theAppMgr.getTheme().getBackground()));
    //pane.setPreferredSize(new Dimension( (int) (740D * CMSApplet.r),
    //  (int) (93D * CMSApplet.r)));
    commentTxtArea.addFocusListener(new TextCompFocusListener());
    commentTxtArea.setAppMgr(CMSApplet.theAppMgr);
    this.add(pnlComponents, java.awt.BorderLayout.CENTER);
    pnlComponents.add(lblCustId
        , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(6, 1, 0, 0), 24, 7));
    pnlComponents.add(txtCustId
        , new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(6, 0, 0, 0), 171, 4));
    pnlComponents.add(lblFamilyName
        , new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(6, 9, 0, 0), 24, 6));
    pnlComponents.add(txtFamilyName
        , new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(6, 7, 0, 4), 137, 3));
    pnlComponents.add(lblName
        , new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 8, 0, 0), 65, 6));
    pnlComponents.add(txtName
        , new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 7, 0, 4), 137, 4));
    pnlComponents.add(lblCollectedAmount
        , new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 1, 0, 0), 7, 7));
    pnlComponents.add(amtFld
        , new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 78, 4));
    pnlComponents.add(txtPrevBalance
        , new GridBagConstraints(2, 2, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 7, 0, 51), 95, 4));
    pnlComponents.add(lblComments
        , new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 1, 0, 0), 41, 7));
    pnlComponents.add(pane
        , new GridBagConstraints(0, 4, 6, 1, 1.0, 0.0, GridBagConstraints.CENTER
        , GridBagConstraints.NONE, new Insets(0, 0, 35, 5), 700, 26));
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getCustId() {
    return txtCustId.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param custId
   */
  public void setCustId(String custId) {
    this.txtCustId.setText(custId);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getFamilyName() {
    return txtFamilyName.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param cust
   */
  public void setFamilyName(String cust) {
    this.txtFamilyName.setText(cust);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getCollectedAmount() {
    return amtFld.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param custId
   */
  public void setCollectedAmount(String custId) {
    this.amtFld.setText(custId);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getNameCust() {
    return txtName.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param cust
   */
  public void setNameCust(String cust) {
    this.txtName.setText(cust);
  }

  /**
   * put your documentation comment here
   * @param val
   */
  public void setPrevBalance(ArmCurrency val) {
    this.prevBalance = val;
    if (prevBalance != null) {
      this.txtPrevBalance.setText(CMSApplet.res.getString("Available Balance")+":   " + prevBalance.formattedStringValue());
    }
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
        if(txtCustId.isEnabled() ) {
        	txtCustId.requestFocus();
        }
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
   * put your documentation comment here
   */
  public void setEnabled() {
    txtCustId.setEnabled(false);
    txtFamilyName.setEnabled(false);
    txtName.setEnabled(false);
    amtFld.setEnabled(false);
    commentTxtArea.setEnabled(false);
    txtPrevBalance.setEnabled(false);
  }

  /**
   * put your documentation comment here
   */
  public void setEnableAfter() {
    amtFld.setEnabled(true);
    commentTxtArea.setEnabled(true);
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
    /*if (commentTxtArea.getText().length() <= 0) {
      commentTxtArea.requestFocus();
      owner.getAppMgr().showErrorDlg(owner.res.getString(
          "The required information is not complete."));
      return null;
    }*/
    if (!amtFld.verifyAmount()) {
      return null;
    }
    String sReasonCode = (String)CMSApplet.theAppMgr.getStateObject("REASON_CODE");
    CMSMiscPaidOut txn = new CMSMiscPaidOut(sReasonCode, (CMSStore)owner.theStore);
    amount = amount.multiply(-1);
    txn.setAmount(amount);
    txn.setComment(commentTxtArea.getText());
    return txn;
  }

  /**
   * put your documentation comment here
   */
  public void reset() {
    txtCustId.setText("");
    txtFamilyName.setText("");
    txtName.setText("");
    txtPrevBalance.setText(CMSApplet.res.getString("Available Balance")+": ");
    amtFld.setText("");
    commentTxtArea.setText("");
  }

  /**
   * put your documentation comment here
   */
  public void setFocusOnAmount() {
    SwingUtilities.invokeLater(new Runnable() {

      /**
       * put your documentation comment here
       */
      public void run() {
        amtFld.requestFocus();
      }
    });
  }

  /**
   * put your documentation comment here
   */
  public void setBackgroundColorTowhite() {
    amtFld.setBackground(Color.white);
  }

  /**
   * put your documentation comment here
   */
  public void setBackgroundColorToGrey() {
    amtFld.setBackground((CMSApplet.theAppMgr).getBackgroundColor());
  }
}

