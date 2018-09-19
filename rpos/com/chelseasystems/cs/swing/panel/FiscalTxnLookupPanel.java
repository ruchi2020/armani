/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import java.awt.*;
import javax.swing.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.config.ConfigMgr;
import javax.swing.border.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.StringTokenizer;
import java.text.SimpleDateFormat;

/**
 * <p>Title:FiscalTxnLookupPanel </p>
 * <p>Description: SearchPanel for FiscalTxn Search </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class FiscalTxnLookupPanel extends JPanel {
  /**
   * TransactionNumber Label
   */
  private JCMSLabel lblTxnNumber;


  /**
   * TransactionNumber Text
   */
  private JCMSTextField txtTxnNumber;


  /**
   * Date label
   */
  private JCMSLabel lblDate;


  /**
   * Date Text
   */
  private JCMSTextField txtDate;


  /**
   * CompanyCode Label
   */
  private JCMSLabel lblCompanyCode;


  /**
   * CompanyCode text
   */
  private JCMSTextField txtCompanyCode;


  /**
   * StoreID label
   */
  private JCMSLabel lblStoreId;


  /**
   * StoreId text
   */
  private JCMSTextField txtStoreId;


  /**
   * RegisterId label
   */
  private JCMSLabel lblRegisterId;


  /**
   * RegisterId text
   */
  private JCMSTextField txtRegisterId;


  /**
   * FiscalReceiptNumber label
   */
  private JCMSLabel lblFiscalReceiptNo;


  /**
   * FiscalReceipt Number text
   */
  private JCMSTextField txtReceiptNum;


  /**
   * FiscalDocumentNumber label
   */
  private JCMSLabel lblFiscalDocNum;


  /**
   * FiscalDocumentNumber text
   */
  private JCMSTextField txtFiscalDocNum;


  /**
   * Fiscal Document Type label
   */
  private JCMSLabel lblFiscalDocType;


  /**
   * FiscalDocument type list
   */
  private JCMSComboBox cbxDocType;


  /**
   * FiscalDocument Type Codes.
   */
  private Vector vecDocTypeCodes;

  private String sBlankCode;
  private SimpleDateFormat dateFormat;
  /**
   * Default Constructor
   */
  public FiscalTxnLookupPanel() {
    try {
      vecDocTypeCodes = new Vector();
      sBlankCode = new String("");
      jbInit();
      populateDocumentTypes();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Reset the GUI
   */
  public void reset() {
    txtTxnNumber.setText("");
    txtDate.setText("");
    txtCompanyCode.setText("");
    txtRegisterId.setText("");
    txtReceiptNum.setText("");
    txtFiscalDocNum.setText("");
    if(cbxDocType.getItemCount() >0)
    cbxDocType.setSelectedIndex(0);
  }


  /**
   * Set ApplicationManager for Gui
   * @param theAppMgr IApplicationManager
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    setBackground(theAppMgr.getBackgroundColor());
    lblTxnNumber.setAppMgr(theAppMgr);
    txtTxnNumber.setAppMgr(theAppMgr);
    lblDate.setAppMgr(theAppMgr);
    txtDate.setAppMgr(theAppMgr);
    lblCompanyCode.setAppMgr(theAppMgr);
    txtCompanyCode.setAppMgr(theAppMgr);
    lblStoreId.setAppMgr(theAppMgr);
    txtStoreId.setAppMgr(theAppMgr);
    lblRegisterId.setAppMgr(theAppMgr);
    txtRegisterId.setAppMgr(theAppMgr);
    lblFiscalReceiptNo.setAppMgr(theAppMgr);
    txtReceiptNum.setAppMgr(theAppMgr);
    lblFiscalDocNum.setAppMgr(theAppMgr);
    txtFiscalDocNum.setAppMgr(theAppMgr);
    cbxDocType.setAppMgr(theAppMgr);
    lblFiscalDocType.setAppMgr(theAppMgr);
  }


  /**
   * Set TransactionNumber
   * @param sTxnNumber String
   */
  public void setTransactionNumber(String sTxnNumber) {
    if (sTxnNumber == null || sTxnNumber.trim().length() < 1)
      return;
    txtTxnNumber.setText(sTxnNumber);
  }


  /**
   * Get Transaction Number
   * @return String
   */
  public String getTransactionNumber() {
    return txtTxnNumber.getText().trim();
  }


  /**
   * GetCompany Code
   * @return String
   */
  public String getCompanyCode() {
    return txtCompanyCode.getText().trim();
  }


  /**
   * Set CompanyCode
   * @param sCompanyCode String
   */
  public void setCompanyCode(String sCompanyCode) {
    if (sCompanyCode == null || sCompanyCode.length() < 1)
      return;
    txtCompanyCode.setText(sCompanyCode);
  }


  /**
   * Get Date
   * @return String
   */
  public String getDate() {
    return txtDate.getText().trim();
  }


  /**
   * Set Date
   * @param sDate String
   */
  public void setDate(String sDate) {
    if (sDate == null || sDate.length() < 1)
      return;
    txtDate.setText(sDate);
  }


  /**
   * Get RegisterId
   * @return String
   */
  public String getRegisterID() {
    return txtRegisterId.getText().trim();
  }


  /**
   * Set RegisterId
   * @param sValue String
   */
  public void setRegisterID(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtRegisterId.setText(sValue);
  }

  public String getStoreID()
  {
    return txtStoreId.getText().trim();
  }

  public void setStoreID(String sValue)
  {
    if (sValue == null || sValue.length() < 1)
      return;
    txtStoreId.setText(sValue);
  }
  /**
   * Get FiscalReceiptNumber
   */
  public String getFiscalReceiptNumber() {
    return txtReceiptNum.getText().trim();
  }


  /**
   * Set FiscalReceipt Number
   * @param sValue String
   */
  public void setFiscalReceiptNumber(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtReceiptNum.setText(sValue);
  }


  /**
   * Get FiscalDocument Number
   * @return String
   */
  public String getFiscalDocumentNumber() {
    return txtFiscalDocNum.getText().trim();
  }


  /**
   * Set FiscalDocumentNumber
   * @param sValue String
   */
  public void setFiscalDocumentNumber(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtFiscalDocNum.setText(sValue);
  }


  /**
   * DocType
   * @param sValue String
   */
  public void setFiscalDocumentType(String sValue) {
    if (sValue == null || sValue.length() < 1 || vecDocTypeCodes.indexOf(sValue) == -1)
      return;
    cbxDocType.setSelectedIndex(vecDocTypeCodes.indexOf(sValue));
  }


  /**
   * DocType
   * @return String
   */
  public String getFiscalDocumentType() {
    if( ((String) vecDocTypeCodes.elementAt(cbxDocType.getSelectedIndex())).equalsIgnoreCase(sBlankCode)) return "";
    return (String)vecDocTypeCodes.elementAt(cbxDocType.getSelectedIndex());
  }


  /**
   * put your documentation comment here
   */
  private void populateDocumentTypes() {
    ConfigMgr config = new ConfigMgr("fiscal_document.cfg");
    StringTokenizer sTokens = new StringTokenizer(config.getString("FISCAL_DOCUMENT_TYPES"), ",");
    String sCode, sLabel, sTmp;
    while (sTokens.hasMoreTokens()) {
      sTmp = sTokens.nextToken();
      if(sTmp.equalsIgnoreCase("NA"))
        sBlankCode = config.getString(sTmp + ".CODE");
      sCode = config.getString(sTmp + ".CODE");
      sLabel = config.getString(sTmp + ".LABEL");
      if (sCode != null && sLabel != null) {
        vecDocTypeCodes.add(sCode);
        cbxDocType.addItem(sLabel);
      }
    }
  }

  private class DateListener implements KeyListener {
    private final String KEYS_ALLOWED = "0123456789/";

    /**
     * put your documentation comment here
     * @param ke
     */
    public void keyPressed(KeyEvent ke) {}

    /**
     * put your documentation comment here
     * @param ke
     */
    public void keyTyped(KeyEvent ke) {
      JCMSTextField txt = (JCMSTextField)ke.getComponent();
      if (KEYS_ALLOWED.indexOf(ke.getKeyChar()) == -1) {
        ke.consume();
        return;
      }
      if (txt.getText().trim().length() + 1 > 10) {
        ke.consume();
        return;
      }
    }

    /**
     * put your documentation comment here
     * @param ke
     */
    public void keyReleased(KeyEvent ke) {}
  }

  /**
   * Initialize components
   * @throws Exception
   */
  private void jbInit()
      throws Exception {
    lblTxnNumber = new JCMSLabel();
    txtTxnNumber = new JCMSTextField();
    lblDate = new JCMSLabel();
    txtDate = new JCMSTextField();
    txtDate.addKeyListener(new DateListener());
    lblCompanyCode = new JCMSLabel();
    txtCompanyCode = new JCMSTextField();
    lblStoreId = new JCMSLabel();
    txtStoreId = new JCMSTextField();
    txtStoreId.setEditable(false);
    txtStoreId.setRequestFocusEnabled(false);
    lblRegisterId = new JCMSLabel();
    txtRegisterId = new JCMSTextField();
    lblFiscalReceiptNo = new JCMSLabel();
    txtReceiptNum = new JCMSTextField();
    lblFiscalDocNum = new JCMSLabel();
    txtFiscalDocNum = new JCMSTextField();
    lblFiscalDocType = new JCMSLabel();
    cbxDocType = new JCMSComboBox();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    setLayout(gridBagLayout1);
    setBorder(new TitledBorder(BorderFactory.createTitledBorder(CMSApplet.res.getString(
        "Fiscal Search"))));
    lblTxnNumber.setText(CMSApplet.res.getString("Transaction Number"));
    lblCompanyCode.setText(CMSApplet.res.getString("Company Code"));
    lblStoreId.setText(CMSApplet.res.getString("Store Id"));
    lblRegisterId.setText(CMSApplet.res.getString("Register Id"));
    lblFiscalReceiptNo.setText(CMSApplet.res.getString("Fiscal Receipt Number"));
    lblFiscalDocNum.setText(CMSApplet.res.getString("Fiscal Document Number"));
    lblFiscalDocType.setText(CMSApplet.res.getString("Document Type"));
    lblDate.setText(CMSApplet.res.getString("Date"));
    lblStoreId.setHorizontalAlignment(SwingConstants.RIGHT);
    lblRegisterId.setHorizontalAlignment(SwingConstants.RIGHT);
    lblFiscalDocNum.setHorizontalAlignment(SwingConstants.RIGHT);
    lblDate.setHorizontalAlignment(SwingConstants.RIGHT);
    lblFiscalDocType.setHorizontalAlignment(SwingConstants.RIGHT);
    txtDate.addActionListener(new FiscalTxnLookupPanel_txtDate_actionAdapter(this));
    this.add(lblTxnNumber
        , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets( -1, 5, 0, 0), 14, 3));
    this.add(txtTxnNumber
        , new GridBagConstraints(1, 0, 4, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.BOTH, new Insets( -1, 0, 0, 30), 215, 3));
    this.add(lblDate
        , new GridBagConstraints(5, 0, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST
        , GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 100, 3));
    this.add(txtDate
        , new GridBagConstraints(7, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 60, 3));
    this.add(lblFiscalReceiptNo
        , new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(8, 5, 1, 0), 5, 3));
    this.add(txtReceiptNum
        , new GridBagConstraints(1, 2, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(8, 0, 1, 0), 75, 3));
    this.add(lblFiscalDocNum
        , new GridBagConstraints(6, 2, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST
        , GridBagConstraints.HORIZONTAL, new Insets(8, 0, 1, 5), 4, 3));
    this.add(lblFiscalDocType
        , new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST
        , GridBagConstraints.NONE, new Insets(8, 0, 1, 5), 50, 3));
    this.add(lblCompanyCode
        , new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(8, 5, 0, 0), 43, 3));
    this.add(txtCompanyCode
        , new GridBagConstraints(1, 1, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.VERTICAL, new Insets(8, 0, 0, -20), 75, 3));
    this.add(lblStoreId
        , new GridBagConstraints(2, 1, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(8, 0, 0, 5), 55, 3));
    this.add(txtStoreId
        , new GridBagConstraints(4, 1, 3, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.BOTH, new Insets(8, 0, 0, 0), 100, 3));
    this.add(lblRegisterId
        , new GridBagConstraints(7, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(8, 30, 0, 3), 35, 3));
    this.add(txtFiscalDocNum
        , new GridBagConstraints(8, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(8, 0, 1, 0), 77, 3));
    this.add(txtRegisterId
        , new GridBagConstraints(8, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.BOTH, new Insets(8, 0, 0, 0), 150, 3));
    this.add(cbxDocType
        , new GridBagConstraints(5, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER
        , GridBagConstraints.HORIZONTAL, new Insets(8, 0, 1, 0), 100, -4));
  }

  public void requestFocusToTxtDate()
  {
    txtDate.requestFocus();
  }

  /**
   * put your documentation comment here
   * @param e
   */
  void txtDate_actionPerformed(ActionEvent e) {}
}


/**
 * put your documentation comment here
 */
class FiscalTxnLookupPanel_txtDate_actionAdapter implements java.awt.event.ActionListener {
  FiscalTxnLookupPanel adaptee;

  /**
   * put your documentation comment here
   * @param   FiscalTxnLookupPanel adaptee
   */
  FiscalTxnLookupPanel_txtDate_actionAdapter(FiscalTxnLookupPanel adaptee) {
    this.adaptee = adaptee;
  }


  /**
   * put your documentation comment here
   * @param e
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.txtDate_actionPerformed(e);
  }


}

