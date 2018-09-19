/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.collections;

/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04/19/2005 | Anand     | N/A       | Customizations as per Collections Specifications   |
 --------------------------------------------------------------------------------------------------
 */
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
import com.chelseasystems.cr.swing.bean.JCMSComboBox;
import com.chelseasystems.cr.swing.bean.JCMSLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 */
public class CollectionsPanelOpenDeposit extends CollectionsPanel {
  private AmountTxtFld amtFld = null;
  private JPanel jPanel9;
  //    JCMSTextArea commentTxtArea = new JCMSTextArea();
  //   JScrollPane pane = new JScrollPane(commentTxtArea);
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
  JCMSTextArea commentTxtArea = new JCMSTextArea();
  private JScrollPane pane = null;
  private JPanel pnlComponents;

  /**
   * @param    CollectionsApplet owner
   * @param    String displayName
   */
  public CollectionsPanelOpenDeposit(CollectionsApplet owner, String displayName) {
    super(owner, displayName);
    try {
      amtFld = new AmountTxtFld(owner.theAppMgr);
      pane = new JScrollPane(commentTxtArea);
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   */
  public void jbInit() {
    pnlComponents = new JPanel();
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
    lblComments.setFont(CMSApplet.theAppMgr.getTheme().getLabelFont());
    lblName.setFont(CMSApplet.theAppMgr.getTheme().getLabelFont());
    txtPrevBalance.setFont(CMSApplet.theAppMgr.getTheme().getLabelFont());
    lblCustId.setText(CMSApplet.res.getString("Customer ID")+":");
    lblFamilyName.setText(CMSApplet.res.getString("Family Name")+": ");
    lblName.setText(CMSApplet.res.getString("Name")+": ");
    lblCollectedAmount.setText(CMSApplet.res.getString("Collected Amount")+": ");
    txtPrevBalance.setText(CMSApplet.res.getString("Previous Balance")+": ");
    lblComments.setText(CMSApplet.res.getString("Comments")+" :");
    txtPrevBalance.setBorder(BorderFactory.createEmptyBorder());
    txtPrevBalance.setBackground((CMSApplet.theAppMgr).getBackgroundColor());
    this.setLayout(new BorderLayout());
    txtCustId.setBackground((CMSApplet.theAppMgr).getBackgroundColor());
    txtFamilyName.setBackground((CMSApplet.theAppMgr).getBackgroundColor());
    txtName.setBackground((CMSApplet.theAppMgr).getBackgroundColor());
    amtFld.setBackground((CMSApplet.theAppMgr).getBackgroundColor());
    pnlComponents.setLayout(new GridBagLayout());
    pnlComponents.setPreferredSize(new Dimension(833, 300));
    commentTxtArea.setBorder(BorderFactory.createLoweredBevelBorder());
    commentTxtArea.setSelectionColor(new Color(0, 0, 128));
    pnlComponents.setBackground(owner.theAppMgr.getBackgroundColor());
    pane.setHorizontalScrollBarPolicy(31);
    pane.setVerticalScrollBar(new JColorScrollBar(CMSApplet.theAppMgr.getTheme().getBackground()));
    pane.setPreferredSize(new Dimension((int)(740D * CMSApplet.r), (int)(93D * CMSApplet.r)));
    commentTxtArea.addFocusListener(new TextCompFocusListener());
    commentTxtArea.setAppMgr(CMSApplet.theAppMgr);
    this.add(pnlComponents, java.awt.BorderLayout.CENTER);
    setPanelSize();
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
            && CMSApplet.theAppMgr.getStateObject("TXN_CUSTOMER") == null) {
          clearFields();
        }
        SwingUtilities.invokeLater(new Runnable() {

          /**
           * put your documentation comment here
           */
          public void run() {
        	  if(txtCustId.isEnabled()) {
        		  txtCustId.requestFocus();
        	  }
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
      this.txtPrevBalance.setText(CMSApplet.res.getString("Previous Balance")+":   " + prevBalance.formattedStringValue());
    }
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
  public CollectionTransaction exportData()
      throws BusinessRuleException {
    //checking if any required field is missing
    if (txtCustId.getText() != null && txtCustId.getText().length() > 0) {
      amtFld.setEnabled(true);
      commentTxtArea.setEnabled(true);
      amtFld.requestFocus();
    }
    ArmCurrency amount = new ArmCurrency(amtFld.getText().trim());
    if (amtFld.getText().length() <= 0 || amount.doubleValue() == 0.00) {
      amtFld.requestFocus();
      owner.theAppMgr.showErrorDlg(owner.res.getString("Amount must be a value greater than zero"));
      return null;
    }
    if (!amtFld.verifyAmount()) {
      return null;
    }
    String sReasonCode = (String)CMSApplet.theAppMgr.getStateObject("REASON_CODE");
    CMSMiscCollection txn = new CMSMiscCollection(sReasonCode, (CMSStore)owner.theStore);
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
    txtPrevBalance.setText(CMSApplet.res.getString("Previous Balance")+": ");
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

  /**
   * put your documentation comment here
   */
  public void setPanelSize() {
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
}

