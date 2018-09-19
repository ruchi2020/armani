/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import javax.swing.JPanel;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.swing.CMSApplet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import java.util.Vector;
import java.util.StringTokenizer;
import java.text.SimpleDateFormat;
import com.chelseasystems.cs.pos.ASISTxnData;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.collection.CMSMiscCollection;
import com.chelseasystems.cr.paidout.PaidOutTransaction;
import com.chelseasystems.cs.paidout.CMSPaidOutTransaction;
import com.chelseasystems.cr.swing.TextFilter;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 06-09-2005 | Megha     | N/A       | Initial                                            |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class ASISTxnDataPanel extends JPanel {

  private static final long serialVersionUID = 1L;
  
  public static final String ALPHA_NUMERIC_SPEC =
      "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.@_!` #$%&*()_-+=[]'{}/;\" ";

  /**
   * put your documentation comment here
   */
  public ASISTxnDataPanel() {
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private JCMSLabel lblCompanyCode;
  private JCMSLabel lblStoreId;
  private JCMSLabel lblRegId;
  private JCMSTextField txtCompanyCode;
  private JCMSTextField txtStoreId;
  private JCMSTextField txtRegId;
  private JCMSLabel lblTxnNo;
  private JCMSLabel lblTxnDate;
  private JCMSLabel lblTxnAmount;
  private JCMSTextField txtTxnNo;
  private JCMSTextField txtTxnDate;
  private JCMSTextField txtTxnAmount;
  private JCMSLabel lblFiscalReceipt;
  private JCMSTextField txtFiscalReceipt;
  private JCMSLabel lblFiscalDate;
  private JCMSTextField txtFiscalDate;
  private JCMSLabel lblFiscalDoc;
  private JCMSTextField txtFiscalDoc;
  private JCMSLabel lblFiscalDocDate;
  private JCMSTextField txtFiscalDocDate;
  private JCMSLabel lblDocType;
  private JCMSComboBox cbDocType;
  private JCMSLabel lblCustNo;
  private JCMSTextField txtCustNo;
  private JCMSLabel lblCustName;
  private JCMSTextField txtCustName;
  private JCMSLabel lblComments;
  private JCMSTextField txtComments;
  private JCMSLabel lblOrderNo;
  private JCMSTextField txtOrderNo;
  private JCMSLabel lblOrderDate;
  private JCMSTextField txtOrderDate;
  private JCMSLabel lblSupplierNo;
  private JCMSTextField txtSupplierNo;
  private JCMSLabel lblSupplierDate;
  private JCMSTextField txtSupplierDate;
  private JCMSLabel lblNotes;
  private JCMSTextField txtNotes;
  private Vector vecDocTypeCodes;
  /**
   * Components panel
   */
  private JPanel pnlComponents;
  private JPanel pnlOrderLinkingComp;

  /**
   * put your documentation comment here
   * @exception Exception
   */
  private void jbInit()
      throws Exception {
    vecDocTypeCodes = new Vector();
    pnlComponents = new JPanel();
    pnlOrderLinkingComp = new JPanel();
    lblCompanyCode = new JCMSLabel();
    lblStoreId = new JCMSLabel();
    lblRegId = new JCMSLabel();
    txtCompanyCode = new JCMSTextField();
    txtStoreId = new JCMSTextField();
    txtRegId = new JCMSTextField();
    lblTxnNo = new JCMSLabel();
    lblTxnDate = new JCMSLabel();
    lblTxnAmount = new JCMSLabel();
    txtTxnNo = new JCMSTextField();
    txtTxnDate = new JCMSTextField();
    txtTxnAmount = new JCMSTextField();
    lblFiscalReceipt = new JCMSLabel();
    txtFiscalReceipt = new JCMSTextField();
    lblFiscalDate = new JCMSLabel();
    txtFiscalDate = new JCMSTextField();
    lblFiscalDoc = new JCMSLabel();
    txtFiscalDoc = new JCMSTextField();
    lblFiscalDocDate = new JCMSLabel();
    txtFiscalDocDate = new JCMSTextField();
    lblDocType = new JCMSLabel();
    cbDocType = new JCMSComboBox();
    lblCustNo = new JCMSLabel();
    txtCustNo = new JCMSTextField();
    lblCustName = new JCMSLabel();
    txtCustName = new JCMSTextField();
    lblComments = new JCMSLabel();
    txtComments = new JCMSTextField();
    lblCompanyCode.setText("Company Code");
    lblStoreId.setText("Store ID");
    lblRegId.setText("Register ID");
    lblTxnNo.setText("Transaction No.");
    lblTxnDate.setText("Transaction Date");
    lblTxnAmount.setText("Transaction Amount");
    lblFiscalReceipt.setText("Fiscal Receipt No.");
    lblFiscalDate.setText("Date");
    lblFiscalDoc.setText("Fiscal Document No.");
    lblFiscalDocDate.setText("Date");
    lblDocType.setText("Document Type");
    lblCustNo.setText("Customer No.");
    lblCustName.setText("Customer Name ");
    lblComments.setText("Comments");
    this.setLayout(new BorderLayout());
    lblCustName.setText("Customer Name ");
    lblOrderNo = new JCMSLabel();
    lblOrderDate = new JCMSLabel();
    lblSupplierNo = new JCMSLabel();
    lblSupplierDate = new JCMSLabel();
    lblNotes = new JCMSLabel();
    txtOrderNo = new JCMSTextField();
    txtOrderDate = new JCMSTextField();
    txtSupplierNo = new JCMSTextField();
    txtSupplierDate = new JCMSTextField();
    txtNotes = new JCMSTextField();
    lblOrderNo.setText("Order No.");
    lblOrderDate.setText("Date");
    lblSupplierNo.setText("Supplier Order");
    lblSupplierDate.setText("Date");
    lblNotes.setText("Notes");
    cbDocType.setPreferredSize(new Dimension(120, 20));
    cbDocType.setMinimumSize(new Dimension(120, 20));
    pnlComponents.setLayout(new GridBagLayout());
    pnlComponents.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder()
        , CMSApplet.res.getString("ASIS Transaction Data")));
    populateDocumentTypes();
    pnlComponents.add(lblCompanyCode
            , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.NONE, new Insets(2, 5, 0, 3), 22, 7));
    pnlComponents.add(txtCompanyCode
            , new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 3), 22, 4));
    pnlComponents.add(lblStoreId
            , new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.NONE, new Insets(2, 2, 0, 3), 22, 7));
    pnlComponents.add(txtStoreId
            , new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 3), 22, 4));
    pnlComponents.add(lblRegId
            , new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.NONE, new Insets(2, 2, 0, 5), 22, 7));
    pnlComponents.add(txtRegId
    		, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
    		, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 5), 22, 4));
    pnlComponents.add(lblTxnNo
            , new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.NONE, new Insets(2, 5, 0, 3), 22, 7));
    pnlComponents.add(txtTxnNo
            , new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 3), 22, 4));
    pnlComponents.add(lblTxnDate
            , new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.NONE, new Insets(2, 2, 0, 3), 22, 5));
    pnlComponents.add(txtTxnDate
            , new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 3), 22, 4));
    pnlComponents.add(lblTxnAmount
            , new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.NONE, new Insets(2, 2, 0, 5), 22, 7));
    pnlComponents.add(txtTxnAmount
    		, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
    		, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 5), 22, 4));
    pnlComponents.add(lblFiscalReceipt
            , new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.NONE, new Insets(2, 5, 0, 3), 22, 7));
    pnlComponents.add(txtFiscalReceipt
            , new GridBagConstraints(0, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 3), 22, 4));
    pnlComponents.add(lblFiscalDate
            , new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.NONE, new Insets(2, 2, 0, 3), 22, 7));
    pnlComponents.add(txtFiscalDate
            , new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 3), 22, 4));
    pnlComponents.add(lblFiscalDoc
            , new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.NONE, new Insets(2, 5, 0, 3), 22, 7));
    pnlComponents.add(txtFiscalDoc
            , new GridBagConstraints(0, 7, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 3), 22, 4));
    pnlComponents.add(lblFiscalDocDate
            , new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.NONE, new Insets(2, 2, 0, 3), 22, 7));
    pnlComponents.add(txtFiscalDocDate
            , new GridBagConstraints(1, 7, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 3), 22, 4));
    pnlComponents.add(lblDocType
            , new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.NONE, new Insets(2, 2, 0, 5), 22, 7));
    pnlComponents.add(cbDocType
            , new GridBagConstraints(2, 7, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 5), 22, 7));
    pnlComponents.add(lblCustNo
            , new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.NONE, new Insets(2, 5, 0, 3), 22, 7));
    pnlComponents.add(txtCustNo
            , new GridBagConstraints(0, 9, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 3), 22, 4));
    pnlComponents.add(lblCustName
            , new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.NONE, new Insets(2, 2, 0, 3), 22, 7));
    pnlComponents.add(txtCustName
            , new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 3), 22, 4));    
    pnlComponents.add(lblComments
    		, new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
    		, GridBagConstraints.NONE, new Insets(2, 5, 0, 3), 22, 7));   
    pnlComponents.add(txtComments
            , new GridBagConstraints(0, 11, 3, 1, 0.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 22, 4));
    this.add(pnlComponents, java.awt.BorderLayout.NORTH);
    pnlOrderLinkingComp.setLayout(new GridBagLayout());
    pnlOrderLinkingComp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder()
        , CMSApplet.res.getString("Order Reference Data")));
    pnlOrderLinkingComp.add(lblOrderNo
            , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.NONE, new Insets(2, 5, 0, 3), 22, 7));
    pnlOrderLinkingComp.add(txtOrderNo
            , new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 3), 22, 4));
    pnlOrderLinkingComp.add(lblOrderDate
            , new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.NONE, new Insets(2, 2, 0, 3), 22, 7));
    pnlOrderLinkingComp.add(txtOrderDate
            , new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 3), 22, 4));
    pnlOrderLinkingComp.add(lblSupplierNo
            , new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.NONE, new Insets(2, 5, 0, 3), 22, 7));
    pnlOrderLinkingComp.add(txtSupplierNo
            , new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 3), 22, 4));
    pnlOrderLinkingComp.add(lblSupplierDate
            , new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.NONE, new Insets(2, 2, 0, 5), 22, 7));
    pnlOrderLinkingComp.add(txtSupplierDate
            , new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 5), 22, 4));
    pnlOrderLinkingComp.add(lblNotes
            , new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.NONE, new Insets(2, 5, 0, 5), 22, 7));
    pnlOrderLinkingComp.add(txtNotes
            , new GridBagConstraints(0, 5, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 22, 4));
    this.add(pnlOrderLinkingComp, java.awt.BorderLayout.CENTER);
    txtCompanyCode.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 20));
    txtStoreId.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 20));
    txtRegId.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 20));
    txtTxnNo.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 50));
    txtTxnDate.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 10));
    txtFiscalReceipt.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 50));
    txtFiscalDate.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 10));
    txtFiscalDoc.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 20));
    txtFiscalDocDate.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 10));
    txtCustNo.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 50));
    txtCustName.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 50));
    txtComments.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 200));
    txtOrderNo.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 50));
    txtOrderDate.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 10));
    txtSupplierNo.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 50));
    txtSupplierDate.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 10));
    txtNotes.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 200));
  }

  /**
   * Reset the GUI
   */
  public void reset() {
    txtCompanyCode.setText("");
    txtStoreId.setText("");
    txtRegId.setText("");
    txtTxnNo.setText("");
    txtTxnDate.setText("");
    txtTxnAmount.setText("");
    txtFiscalReceipt.setText("");
    txtFiscalDate.setText("");
    txtFiscalDoc.setText("");
    txtFiscalDocDate.setText("");
    txtCustNo.setText("");
    txtCustName.setText("");
    txtComments.setText("");
    if (cbDocType.getItemCount() > 0)
      cbDocType.setSelectedIndex(0);
    txtOrderNo.setText("");
    txtOrderDate.setText("");
    txtSupplierNo.setText("");
    txtSupplierDate.setText("");
    txtNotes.setText("");
  }

  /**
   * Enable/Disable GUI
   * @param bEnabled boolean
   */
  public void setEnabled(boolean bEnabled) {
    txtCompanyCode.setEnabled(bEnabled);
    txtStoreId.setEnabled(bEnabled);
    txtRegId.setEnabled(bEnabled);
    txtTxnNo.setEnabled(bEnabled);
    txtTxnDate.setEnabled(bEnabled);
    txtTxnAmount.setEnabled(bEnabled);
    txtFiscalReceipt.setEnabled(bEnabled);
    txtFiscalDate.setEnabled(bEnabled);
    txtFiscalDoc.setEnabled(bEnabled);
    txtFiscalDocDate.setEnabled(bEnabled);
    txtCustNo.setEnabled(bEnabled);
    txtCustName.setEnabled(bEnabled);
    txtComments.setEnabled(bEnabled);
    cbDocType.setEnabled(bEnabled);
    txtOrderNo.setEnabled(bEnabled);
    txtOrderDate.setEnabled(bEnabled);
    txtSupplierNo.setEnabled(bEnabled);
    txtSupplierDate.setEnabled(bEnabled);
    txtNotes.setEnabled(bEnabled);
  }

  /**
   * Set Application Manager
   * @param theAppMgr IApplicationManager
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    this.setBackground(theAppMgr.getBackgroundColor());
    pnlComponents.setBackground(theAppMgr.getBackgroundColor());
    pnlOrderLinkingComp.setBackground(theAppMgr.getBackgroundColor());
    txtCompanyCode.setAppMgr(theAppMgr);
    txtStoreId.setAppMgr(theAppMgr);
    txtRegId.setAppMgr(theAppMgr);
    txtTxnNo.setAppMgr(theAppMgr);
    txtTxnDate.setAppMgr(theAppMgr);
    txtTxnAmount.setAppMgr(theAppMgr);
    txtFiscalReceipt.setAppMgr(theAppMgr);
    txtFiscalDate.setAppMgr(theAppMgr);
    txtFiscalDoc.setAppMgr(theAppMgr);
    txtFiscalDocDate.setAppMgr(theAppMgr);
    txtCustNo.setAppMgr(theAppMgr);
    txtCustName.setAppMgr(theAppMgr);
    txtComments.setAppMgr(theAppMgr);
    cbDocType.setAppMgr(theAppMgr);
    lblCompanyCode.setAppMgr(theAppMgr);
    lblStoreId.setAppMgr(theAppMgr);
    lblRegId.setAppMgr(theAppMgr);
    lblTxnNo.setAppMgr(theAppMgr);
    lblTxnAmount.setAppMgr(theAppMgr);
    lblTxnDate.setAppMgr(theAppMgr);
    lblFiscalReceipt.setAppMgr(theAppMgr);
    lblFiscalDate.setAppMgr(theAppMgr);
    lblFiscalDoc.setAppMgr(theAppMgr);
    lblFiscalDocDate.setAppMgr(theAppMgr);
    lblDocType.setAppMgr(theAppMgr);
    lblCustNo.setAppMgr(theAppMgr);
    lblCustName.setAppMgr(theAppMgr);
    lblComments.setAppMgr(theAppMgr);
    lblOrderNo.setAppMgr(theAppMgr);
    lblOrderDate.setAppMgr(theAppMgr);
    lblSupplierNo.setAppMgr(theAppMgr);
    lblSupplierDate.setAppMgr(theAppMgr);
    lblNotes.setAppMgr(theAppMgr);
    txtOrderNo.setAppMgr(theAppMgr);
    txtOrderDate.setAppMgr(theAppMgr);
    txtSupplierNo.setAppMgr(theAppMgr);
    txtSupplierDate.setAppMgr(theAppMgr);
    txtNotes.setAppMgr(theAppMgr);
  }

  /**
   * Company Code
   * @param sValue String
   */
  public void setCompanyCode(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtCompanyCode.setText(sValue);
  }

  /**
   * Store Id
   * @return String
   */
  public String getCompanyCode() {
    return txtCompanyCode.getText().trim();
  }

  /**
   * Store Id
   * @param sValue String
   */
  public void setStoreId(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtStoreId.setText(sValue);
  }

  /**
   * Store Id
   * @return String
   */
  public String getStoreId() {
    return txtStoreId.getText().trim();
  }

  /**
   * RegId
   * @param sValue String
   */
  public void setRegId(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtRegId.setText(sValue);
  }

  /**
   * RegId
   * @return String
   */
  public String getRegId() {
    return txtRegId.getText().trim();
  }

  /**
   * TxnNo
   * @param sValue String
   */
  public void setTxnNo(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtTxnNo.setText(sValue);
  }

  /**
   * TxnNo
   * @return String
   */
  public String getTxnNo() {
    return txtTxnNo.getText().trim();
  }

  /**
   * TxnDate
   * @param sValue String
   */
  public void setTxnDate(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtTxnDate.setText(sValue);
  }

  /**
   * TxnDate
   * @return String
   */
  public String getTxnDate() {
    return txtTxnDate.getText().trim();
  }

  /**
   * FiscalReceiptNo
   * @param sValue String
   */
  public void setFiscalReceiptNo(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtFiscalReceipt.setText(sValue);
  }

  /**
   * FiscalReceiptNo
   * @return String
   */
  public String getFiscalReceiptNo() {
    return txtFiscalReceipt.getText().trim();
  }

  /**
   * FiscalReceiptDate
   * @param sValue String
   */
  public void setFiscalReceiptDate(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtFiscalDate.setText(sValue);
  }

  /**
   * FiscalReceiptDate
   * @return String
   */
  public String getFiscalReceiptDate() {
    return txtFiscalDate.getText().trim();
  }

  /**
   * FiscalDocNo
   * @param sValue String
   */
  public void setFiscalDocNo(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtFiscalDoc.setText(sValue);
  }

  /**
   * FiscalDocNo
   * @return String
   */
  public String getFiscalDocNo() {
    return txtFiscalDoc.getText().trim();
  }

  /**
   * FiscalDocDate
   * @param sValue String
   */
  public void setFiscalDocDate(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtFiscalDocDate.setText(sValue);
  }

  /**
   * FiscalDocDate
   * @return String
   */
  public String getFiscalDocDate() {
    return txtFiscalDocDate.getText().trim();
  }

  /**
   * DocType
   * @param sValue String
   */
  public void setDocType(String sValue) {
    if (sValue == null || sValue.length() < 1 || vecDocTypeCodes.indexOf(sValue) == -1)
      return;
    cbDocType.setSelectedIndex(vecDocTypeCodes.indexOf(sValue));
  }

  /**
   * DocType
   * @return String
   */
  public String getDocType() {
	if (cbDocType.getSelectedIndex() > -1) {
    return (String)vecDocTypeCodes.elementAt(cbDocType.getSelectedIndex());
	} else {
		return (String)vecDocTypeCodes.elementAt(0);
	}
  }

  /**
   * CustNo
   * @param sValue String
   */
  public void setCustNo(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtCustNo.setText(sValue);
  }

  /**
   * CustNo
   * @return String
   */
  public String getCustNo() {
    return txtCustNo.getText().trim();
  }

  /**
   * CustName
   * @param sValue String
   */
  public void setCustName(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtCustName.setText(sValue);
  }

  /**
   * CustName
   * @return String
   */
  public String getCustName() {
    return txtCustName.getText().trim();
  }

  /**
   * Comments
   * @param sValue String
   */
  public void setComments(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtComments.setText(sValue);
  }

  /**
   * Comments
   * @return String
   */
  public String getComments() {
    return txtComments.getText().trim();
  }

  /**
   * Txn Amount
   * @param sValue
   */
  public void setTxnAmount(String sValue) {
	if (sValue == null || sValue.length() < 1)
      return;
	txtTxnAmount.setText(sValue);
  }

  /**
   * Txn Amount
   * @return
   */
  public String getTxnAmount() {
	return txtTxnAmount.getText().trim();
  }
  
  /**
   * OrderNo
   * @param sValue String
   */
  public void setOrderNo(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtOrderNo.setText(sValue);
  }

  /**
   * OrderNo
   * @return String
   */
  public String getOrderNo() {
    return txtOrderNo.getText().trim();
  }

  /**
   * OrderDate
   * @param sValue String
   */
  public void setOrderDate(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtOrderDate.setText(sValue);
  }
  
  /**
   * OrderDate
   * @return String
   */
  public String getOrderDate() {
    return txtOrderDate.getText().trim();
  }
  
  /**
   * SupplierNo
   * @param sValue String
   */
  public void setSupplierNo(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtSupplierNo.setText(sValue);
  }

  /**
   * SupplierNo
   * @return String
   */
  public String getSupplierNo() {
    return txtSupplierNo.getText().trim();
  }
  
  /**
   * SupplierDate
   * @param sValue String
   */
  public void setSupplierDate(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtSupplierDate.setText(sValue);
  }
  
  /**
   * SupplierDate
   * @return String
   */
  public String getSupplierDate() {
    return txtSupplierDate.getText().trim();
  }
  
  /**
   * Notes
   * @param sValue String
   */
  public void setNotes(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtNotes.setText(sValue);
  }

  /**
   * Notes
   * @return String
   */
  public String getNotes() {
    return txtNotes.getText().trim();
  }

  /**
   * put your documentation comment here
   */
  private void populateDocumentTypes() {
    ConfigMgr config = new ConfigMgr("fiscal_document.cfg");
    String fiscalDocTypes = config.getString("FISCAL_DOCUMENT_TYPES");
    if(fiscalDocTypes==null)
      return;
    StringTokenizer sTokens = new StringTokenizer(fiscalDocTypes, ",");
    String sCode, sLabel, sTmp;
    while (sTokens.hasMoreTokens()) {
      sTmp = sTokens.nextToken();
      sCode = config.getString(sTmp + ".CODE");
      sLabel = config.getString(sTmp + ".LABEL");
      if (sCode != null && sLabel != null) {
        vecDocTypeCodes.add(sCode);
        cbDocType.addItem(sLabel);
      }
    }
  }

  /**
   * put your documentation comment here
   */
  public void requestFocusOnFirstField() {
    txtCompanyCode.requestFocus();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public JCMSTextField getTxnDateJTXT() {
    return txtTxnDate;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public JCMSTextField getFiscalReceiptDateJTXT() {
    return txtFiscalDate;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public JCMSTextField getFiscalDocDateJTXT() {
    return txtFiscalDocDate;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public JCMSTextField getTxnAmountJTXT() {
    return txtTxnAmount;
  }
  
  /**
   * put your documentation comment here
   * @return
   */
  public JCMSTextField getOrderDateJTXT() {
    return txtOrderDate;
  }
  
  /**
   * put your documentation comment here
   * @return
   */
  public JCMSTextField getSupplierDateJTXT() {
    return txtSupplierDate;
  }

  public void showTransaction(PaymentTransaction aTxn, IApplicationManager theAppMgr) {
    SimpleDateFormat fmt = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
    java.util.Date txnDate = null;
    java.util.Date fiscalDate = null;
    java.util.Date fiscalDocDate = null;
    java.util.Date orderDate = null;
    java.util.Date supplierDate = null;
    String fiscalReceiptNo;
    String companyCode = "";
    String storeId = "";
    String registerId = "";
    String txnNo = "";
    String txnAmount = "";
    String fiscalDocNo = "";
    String docType = "";
    String custNo = "";
    String custName = "";
    String comments = "";
    String orderNo = "";
    String supplierNo = "";
    String notes = "";

    if (aTxn instanceof CMSMiscCollection) {
      fiscalReceiptNo = ((CMSMiscCollection)aTxn).getFiscalReceiptNumber();
      fiscalDate = ((CMSMiscCollection)aTxn).getFiscalReceiptDate();
    } else if (aTxn instanceof PaidOutTransaction) {
      fiscalReceiptNo = ((CMSPaidOutTransaction)aTxn).getFiscalReceiptNumber();
      fiscalDate = ((CMSPaidOutTransaction)aTxn).getFiscalReceiptDate();
    } else {
      fiscalReceiptNo = ((CMSCompositePOSTransaction)aTxn).getFiscalReceiptNumber();
      fiscalDate = ((CMSCompositePOSTransaction)aTxn).getFiscalReceiptDate();
    }   

    if (theAppMgr.getStateObject("FROM_TXN_DETAIL_APPLET") != null) {
    	ASISTxnData asisTxnData = (ASISTxnData)theAppMgr.getStateObject("ASIS_TXN_DATA");
    	if (asisTxnData != null) {
    		companyCode = asisTxnData.getCompanyCode();
    		storeId = asisTxnData.getStoreId();
    		registerId = asisTxnData.getRegId();
    		txnNo = asisTxnData.getTxnNo();
    		txnDate = asisTxnData.getTxnDate();
    		txnAmount = asisTxnData.getTxnAmount().stringValue();
    		fiscalReceiptNo = asisTxnData.getFiscalReceiptNo();
    		fiscalDate = asisTxnData.getFiscalReceiptDate();
    		fiscalDocNo = asisTxnData.getFiscalDocNo();
    		fiscalDocDate = asisTxnData.getFiscalDocDate();
    		docType = asisTxnData.getDocType();
    		custNo = asisTxnData.getCustNo();
    		custName = asisTxnData.getCustName();
    		comments = asisTxnData.getComments();
    		orderNo = asisTxnData.getOrderNo();
    		orderDate = asisTxnData.getOrderDate();
    		supplierNo = asisTxnData.getSupplierNo();
    		supplierDate = asisTxnData.getSupplierDate();
    		notes = asisTxnData.getNotes();
    	}
    } else {
    	companyCode = ((CMSStore)aTxn.getStore()).getCompanyCode();
    	storeId = ((CMSStore)aTxn.getStore()).getShopCode();
    	registerId = aTxn.getRegisterId();
    	txnNo = aTxn.getId();
    	txnDate = aTxn.getPostDate();
    	txnAmount = aTxn.getTotalPaymentAmount().stringValue();
    }
    if (companyCode != null)
      txtCompanyCode.setText(companyCode);
    else
      txtCompanyCode.setText("");
    if (storeId != null)
      txtStoreId.setText(storeId);
    else
      txtStoreId.setText("");

    if (registerId != null)
      txtRegId.setText(registerId);
    else
      txtRegId.setText("");

    if (txnNo != null)
      txtTxnNo.setText(txnNo);
    else
      txtTxnNo.setText("");

    if (txnDate != null)
      txtTxnDate.setText(fmt.format(txnDate));
    else
      txtTxnDate.setText("");

    if (txnAmount != null)
      txtTxnAmount.setText(txnAmount);
    else
      txtTxnAmount.setText("");

    if (fiscalReceiptNo != null)
      txtFiscalReceipt.setText(fiscalReceiptNo);
    else
      txtFiscalReceipt.setText("");

    if (fiscalDate != null)
      txtFiscalDate.setText(fmt.format(fiscalDate));
    else
      txtFiscalDate.setText("");

    if (fiscalDocNo != null)
      txtFiscalDoc.setText(fiscalDocNo);
    else
      txtFiscalDoc.setText("");

    if (fiscalDocDate != null)
      txtFiscalDocDate.setText(fmt.format(fiscalDocDate));
    else
      txtFiscalDocDate.setText("");

    if (docType != null)
      cbDocType.setSelectedIndex(vecDocTypeCodes.indexOf(docType));
    else
      cbDocType.setSelectedIndex(0);

    if (custNo != null)
      txtCustNo.setText(custNo);
    else
      txtCustNo.setText("");

    if (custName != null)
      txtCustName.setText(custName);
    else
      txtCustName.setText("");

    if (comments != null)
      txtComments.setText(comments);
    else
      txtComments.setText("");
    
    if (orderNo != null)
      txtOrderNo.setText(orderNo);
    else
      txtOrderNo.setText("");

    if (orderDate != null)
      txtOrderDate.setText(fmt.format(orderDate));
    else
      txtOrderDate.setText("");
    
    if (supplierNo != null)
      txtSupplierNo.setText(supplierNo);
    else
      txtSupplierNo.setText("");

    if (supplierDate != null)
      txtSupplierDate.setText(fmt.format(supplierDate));
    else
      txtSupplierDate.setText("");
    
    if (notes != null)
      txtNotes.setText(notes);
    else
      txtNotes.setText("");
  }
}
