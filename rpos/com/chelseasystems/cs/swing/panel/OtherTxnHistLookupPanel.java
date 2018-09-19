/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import java.awt.*;
import javax.swing.*;
import java.util.Date;
import java.util.Vector;
import java.util.StringTokenizer;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.swing.CMSInputVerifier;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cs.store.CMSStoreHelper;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cr.config.ConfigMgr;


/**
 * put your documentation comment here
 */
public class OtherTxnHistLookupPanel extends JPanel implements ItemListener {
  private JPanel pnlNorth;
  private JPanel pnlCustomer;
  private JPanel pnlStore;
  private JPanel pnlCenter;
  private JPanel pnlPerson;
  private JPanel pnlTxn;
  private JPanel pnlSouth;
  private JPanel pnlItemDetails;
  private JPanel pnlDate;
  private JCMSLabel lblID;
  private JCMSLabel lblFamilyName;
  private JCMSLabel lblName;
  private JCMSLabel lblRegisterID;
  private JCMSLabel lblStoreID;
  private JCMSLabel lblCompanyCode;
  private JCMSLabel lblType;
  private JCMSLabel lblAssociate;
  private JCMSLabel lblCashier;
  private JCMSLabel lblTxnType;
  private JCMSLabel lblAmount;
  private JCMSLabel lblBetween;
  private JCMSLabel lblAnd;
  private JCMSLabel lblModel;
  private JCMSLabel lblStyle;
  private JCMSLabel lblSupplier;
  private JCMSLabel lblSku;
  private JCMSLabel lblFabric;
  private JCMSLabel lblColor;
  private JCMSLabel lblYear;
  private JCMSLabel lblSeason;
  private JCMSLabel lblStartDate;
  private JCMSLabel lblEndDate;
  private JCMSComboBox cbxTxnType;
  private JCMSComboBox cbxStoreId;
  private JCMSComboBox cbxStoreType;
  private JCMSTextField txtID;
  private JCMSTextField txtFamilyName;
  private JCMSTextField txtName;
  private JCMSTextField txtRegisterID;
  private JCMSTextField txtCompanyCode;
  private JCMSTextField txtAssociate;
  private JCMSTextField txtCashier;
  private JCMSTextField txtCurrencyCode;
  private JCMSTextField txtAmountStart;
  private JCMSTextField txtAmountEnd;
  private JCMSTextField txtSku;
  private JCMSTextField txtModel;
  private JCMSTextField txtStyle;
  private JCMSTextField txtSupplier;
  private JCMSTextField txtFabric;
  private JCMSTextField txtColor;
  private JCMSTextField txtYear;
  private JCMSTextField txtSeason;
  private JCMSTextField txtStartDate;
  private JCMSTextField txtEndDate;
  private SimpleDateFormat dateFormat;
  private Vector vecTransTypeKeys;
  private CMSStore cmsStores[];
  private CMSStore theStore;
  private IApplicationManager theAppMgr;
  /**
   * put your documentation comment here
   */
  public OtherTxnHistLookupPanel() {
    try {
      jbInit();
      dateFormat = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
//      populateStoreIdsAndTypes();
      populateTransactionTypes();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setCustomerID(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtID.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getCustomerID() {
    return txtID.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setCustomerName(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtName.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getCustomerName() {
    return txtName.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setFamilyName(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtFamilyName.setText(sValue);
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
   * @param sValue
   */
  public void setRegisterID(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtRegisterID.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getRegisterID() {
    return txtRegisterID.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setCompanyCode(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtCompanyCode.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getCompanyCode() {
    return txtCompanyCode.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setAssociate(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtAssociate.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getAssociate() {
    return txtAssociate.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setCashier(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtCashier.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getCashier() {
    return txtCashier.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setCurrencyCode(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtCurrencyCode.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getCurrencyCode() {
    return txtCurrencyCode.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setAmountStartRange(ArmCurrency sValue) {
    if (sValue == null)
      return;
    txtAmountStart.setText(sValue.stringValue());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getAmountStartRange() {
    if (txtAmountStart.getText().trim().length() < 1)
      return null;
    return new ArmCurrency(Double.parseDouble(txtAmountStart.getText().trim()));
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setAmountEndRange(ArmCurrency sValue) {
    if (sValue == null)
      return;
    txtAmountEnd.setText(sValue.stringValue());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getAmountEndRange() {
    if (txtAmountStart.getText().trim().length() < 1)
      return null;
    return new ArmCurrency(Double.parseDouble(txtAmountEnd.getText().trim()));
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setSku(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtSku.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getSku() {
    return txtSku.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setModel(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtModel.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getModel() {
    return txtModel.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setStyle(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtStyle.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getStyle() {
    return txtStyle.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setSupplier(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtSupplier.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getSupplier() {
    return txtSupplier.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setFabric(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtFabric.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getFabric() {
    return txtFabric.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setColor(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtColor.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getColor() {
    return txtColor.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setYear(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtYear.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getYear() {
    return txtYear.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setSeason(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtSeason.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getSeason() {
    return txtSeason.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param dtStart
   */
  public void setStartDate(Date dtStart) {
    if (dtStart == null)
      return;
    txtStartDate.setText(dateFormat.format(dtStart));
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Date getStartDate() {
    Date dtStart = null;
    try {
      dtStart = dateFormat.parse(txtStartDate.getText());
    } catch (Exception e) {}
    return dtStart;
  }

  /**
   * put your documentation comment here
   * @param dtEnd
   */
  public void setEndDate(Date dtEnd) {
    if (dtEnd == null)
      return;
    txtEndDate.setText(dateFormat.format(dtEnd));
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Date getEndDate() {
    Date dtEnd = null;
    try {
      dtEnd = dateFormat.parse(txtEndDate.getText());
    } catch (Exception e) {}
    return dtEnd;
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setSelectedStore(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    cbxStoreId.removeItemListener(this);
    cbxStoreId.setSelectedItem(sValue);
    cbxStoreId.addItemListener(this);
    if (cbxStoreId.getSelectedIndex() < 1 || cbxStoreId.getSelectedIndex() > cmsStores.length - 2)
      return;
    setSelectedStoreType(cmsStores[cbxStoreId.getSelectedIndex()-1].getBrandID());
    setCompanyCode(cmsStores[cbxStoreId.getSelectedIndex()-1].getCompanyCode());
    setCurrencyCode(cmsStores[cbxStoreId.getSelectedIndex()].getCurrencyType().getCode());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getSelectedStore() {
    return (String)cbxStoreId.getSelectedItem();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setSelectedStoreType(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    cbxStoreType.setSelectedItem(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getSelectedStoreType() {
    return (String)cbxStoreType.getSelectedItem();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setSelectedTxnType(String sValue) {
    if (sValue == null || sValue.length() < 1 || vecTransTypeKeys.indexOf(sValue) == -1)
      return;
    cbxTxnType.setSelectedIndex(vecTransTypeKeys.indexOf(sValue));
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getSelectedTxnType() {
    if (cbxTxnType.getSelectedIndex() < 0 || cbxTxnType.getSelectedIndex() >= vecTransTypeKeys.size())
      return "";
    return (String)vecTransTypeKeys.elementAt(cbxTxnType.getSelectedIndex());
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    this.theAppMgr = theAppMgr;
    this.setBackground(theAppMgr.getBackgroundColor());
    pnlNorth.setBackground(theAppMgr.getBackgroundColor());
    pnlCustomer.setBackground(theAppMgr.getBackgroundColor());
    pnlStore.setBackground(theAppMgr.getBackgroundColor());
    pnlPerson.setBackground(theAppMgr.getBackgroundColor());
    pnlTxn.setBackground(theAppMgr.getBackgroundColor());
    pnlSouth.setBackground(theAppMgr.getBackgroundColor());
    pnlCenter.setBackground(theAppMgr.getBackgroundColor());
    pnlItemDetails.setBackground(theAppMgr.getBackgroundColor());
    pnlDate.setBackground(theAppMgr.getBackgroundColor());
    txtID.setAppMgr(theAppMgr);
    txtName.setAppMgr(theAppMgr);
    txtFamilyName.setAppMgr(theAppMgr);
    txtRegisterID.setAppMgr(theAppMgr);
    txtCompanyCode.setAppMgr(theAppMgr);
    txtAssociate.setAppMgr(theAppMgr);
    txtCashier.setAppMgr(theAppMgr);
    txtCurrencyCode.setAppMgr(theAppMgr);
    lblAmount.setAppMgr(theAppMgr);
    txtAmountStart.setAppMgr(theAppMgr);
    txtAmountEnd.setAppMgr(theAppMgr);
    txtSku.setAppMgr(theAppMgr);
    txtModel.setAppMgr(theAppMgr);
    txtStyle.setAppMgr(theAppMgr);
    txtSupplier.setAppMgr(theAppMgr);
    txtFabric.setAppMgr(theAppMgr);
    txtColor.setAppMgr(theAppMgr);
    txtYear.setAppMgr(theAppMgr);
    txtSeason.setAppMgr(theAppMgr);
    txtStartDate.setAppMgr(theAppMgr);
    txtEndDate.setAppMgr(theAppMgr);
    lblID.setAppMgr(theAppMgr);
    lblFamilyName.setAppMgr(theAppMgr);
    lblName.setAppMgr(theAppMgr);
    lblStoreID.setAppMgr(theAppMgr);
    lblRegisterID.setAppMgr(theAppMgr);
    lblCompanyCode.setAppMgr(theAppMgr);
    lblType.setAppMgr(theAppMgr);
    lblAssociate.setAppMgr(theAppMgr);
    lblCashier.setAppMgr(theAppMgr);
    lblTxnType.setAppMgr(theAppMgr);
    lblAmount.setAppMgr(theAppMgr);
    lblBetween.setAppMgr(theAppMgr);
    lblAnd.setAppMgr(theAppMgr);
    lblSku.setAppMgr(theAppMgr);
    lblModel.setAppMgr(theAppMgr);
    lblStyle.setAppMgr(theAppMgr);
    lblSupplier.setAppMgr(theAppMgr);
    lblFabric.setAppMgr(theAppMgr);
    lblColor.setAppMgr(theAppMgr);
    lblYear.setAppMgr(theAppMgr);
    lblSeason.setAppMgr(theAppMgr);
    lblStartDate.setAppMgr(theAppMgr);
    lblEndDate.setAppMgr(theAppMgr);
    cbxTxnType.setAppMgr(theAppMgr);
    cbxStoreId.setAppMgr(theAppMgr);
    cbxStoreType.setAppMgr(theAppMgr);
  }

  public CMSStore getStoreForId(String sStoreId) {
    for (int iCtr = 0; iCtr < cmsStores.length; iCtr++) {
      if (cmsStores[iCtr].getId().equals(sStoreId))return cmsStores[iCtr];
    }
    return null;
  }

  /**
   * put your documentation comment here
   */
  public void reset() {
    txtID.setText("");
    txtName.setText("");
    txtFamilyName.setText("");
    txtRegisterID.setText("");
    txtCompanyCode.setText("");
    txtAssociate.setText("");
    txtCashier.setText("");
    lblAmount.setToolTipText("");
    txtAmountStart.setText("");
    txtAmountEnd.setText("");
    txtSku.setText("");
    txtModel.setText("");
    txtStyle.setText("");
    txtSupplier.setText("");
    txtFabric.setText("");
    txtColor.setText("");
    txtYear.setText("");
    txtSeason.setText("");
    //PCR 1817 - Improvements for Europe
    if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)) {
  	  setStartDate(new Date());
  	  setEndDate(new Date());
    } else {
    	txtStartDate.setText("");
    	txtEndDate.setText("");
	}
    cbxTxnType.setSelectedItem("");
    setSelectedStore(theStore.getId());
    setCurrencyCode(theStore.getCurrencyType().getCode());
    cbxStoreId.requestFocus();
  }

  /**
   * put your documentation comment here
   * @exception Exception
   */
  private void jbInit()
      throws Exception {
    GridBagLayout gridBagLayout1;
    GridBagLayout gridBagLayout2;
    GridBagLayout gridBagLayout3;
    GridBagLayout gridBagLayout4;
    GridBagLayout gridBagLayout5;
    GridBagLayout gridBagLayout6;
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    pnlNorth = new JPanel();
    pnlCustomer = new JPanel();
    pnlStore = new JPanel();
    pnlCenter = new JPanel();
    pnlPerson = new JPanel();
    pnlTxn = new JPanel();
    pnlSouth = new JPanel();
    pnlItemDetails = new JPanel();
    pnlDate = new JPanel();
    lblID = new JCMSLabel();
    lblFamilyName = new JCMSLabel();
    lblName = new JCMSLabel();
    txtID = new JCMSTextField();
    txtFamilyName = new JCMSTextField();
    txtName = new JCMSTextField();
    //    txtID.setEditable(false);
    //    txtID.setRequestFocusEnabled(false);
    //    txtFamilyName.setEditable(false);
    //    txtFamilyName.setRequestFocusEnabled(false);
    //    txtName.setEditable(false);
    //    txtName.setRequestFocusEnabled(false);
    txtID.setEnabled(false);
    txtFamilyName.setEnabled(false);
    txtName.setEnabled(false);
    lblStoreID = new JCMSLabel();
    cbxStoreId = new JCMSComboBox();
//    cbxStoreId.addItemListener(this);
//    cbxStoreId.addActionListener(this);
    lblRegisterID = new JCMSLabel();
    txtRegisterID = new JCMSTextField();
    lblCompanyCode = new JCMSLabel();
    txtCompanyCode = new JCMSTextField();
    lblType = new JCMSLabel();
    cbxStoreType = new JCMSComboBox();
    cbxStoreType.setEnabled(false);
    txtCompanyCode.setEnabled(false);
    lblAssociate = new JCMSLabel();
    txtAssociate = new JCMSTextField();
    lblCashier = new JCMSLabel();
    txtCashier = new JCMSTextField();
    lblTxnType = new JCMSLabel();
    cbxTxnType = new JCMSComboBox();
    txtCurrencyCode = new JCMSTextField();
    txtCurrencyCode.setEnabled(false);
//    txtCurrencyCode.setEditable(false);
    lblAmount = new JCMSLabel();
    lblBetween = new JCMSLabel();
    txtAmountStart = new JCMSTextField();
    lblAnd = new JCMSLabel();
    txtAmountEnd = new JCMSTextField();
    lblSku = new JCMSLabel();
    txtSku = new JCMSTextField();
    lblModel = new JCMSLabel();
    lblStyle = new JCMSLabel();
    lblSupplier = new JCMSLabel();
    txtModel = new JCMSTextField();
    txtStyle = new JCMSTextField();
    txtSupplier = new JCMSTextField();
    lblFabric = new JCMSLabel();
    lblColor = new JCMSLabel();
    lblYear = new JCMSLabel();
    lblSeason = new JCMSLabel();
    txtFabric = new JCMSTextField();
    txtColor = new JCMSTextField();
    txtYear = new JCMSTextField();
    txtSeason = new JCMSTextField();
    lblStartDate = new JCMSLabel();
    txtStartDate = new JCMSTextField();
    lblEndDate = new JCMSLabel();
    txtEndDate = new JCMSTextField();
    txtStartDate.setVerifier(new Verifier());
    txtEndDate.setVerifier(new Verifier());
    txtEndDate.addKeyListener(new DateListener());
    txtStartDate.addKeyListener(new DateListener());
    txtYear.addKeyListener(new NumericListener());
    txtAmountStart.addKeyListener(new NumericListener1());
    txtAmountEnd.addKeyListener(new NumericListener1());
    gridBagLayout1 = new GridBagLayout();
    gridBagLayout2 = new GridBagLayout();
    gridBagLayout3 = new GridBagLayout();
    gridBagLayout4 = new GridBagLayout();
    gridBagLayout5 = new GridBagLayout();
    gridBagLayout6 = new GridBagLayout();
    pnlCustomer.setLayout(gridBagLayout1);
    pnlStore.setLayout(gridBagLayout2);
    pnlPerson.setLayout(gridBagLayout3);
    pnlTxn.setLayout(gridBagLayout4);
    pnlItemDetails.setLayout(gridBagLayout5);
    pnlDate.setLayout(gridBagLayout6);
    this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder()
        , res.getString("Other Txn Lookup")));
    pnlCustomer.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder()
        , res.getString("Customer")));
    pnlStore.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Store"));
    pnlPerson.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder()
        , res.getString("Person")));
    pnlTxn.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder()
        , res.getString("Transaction")));
    pnlItemDetails.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder()
        , res.getString("Item Details")));
    lblID.setText(res.getString("ID"));
    lblFamilyName.setText(res.getString("Family Name"));
    lblName.setText(res.getString("Name"));
    lblStoreID.setText(res.getString("Store ID"));
    lblRegisterID.setText(res.getString("Register ID"));
    lblCompanyCode.setText(res.getString("Company Code"));
    lblType.setText(res.getString("Type"));
    lblAssociate.setText(res.getString("Associate"));
    lblCashier.setText(res.getString("Cashier"));
    lblTxnType.setText(res.getString("Type"));
    lblAmount.setText(res.getString("Amount"));
    lblBetween.setText(res.getString("between"));
    lblAnd.setText(res.getString("and"));
    lblSku.setText(res.getString("SKU"));
    lblModel.setText(res.getString("Model"));
    lblStyle.setText(res.getString("Style/Ref"));
    lblSupplier.setText(res.getString("Supplier"));
    lblFabric.setText(res.getString("Fabric"));
    lblColor.setText(res.getString("Color"));
    lblYear.setText(res.getString("Year"));
    lblSeason.setText(res.getString("Season"));
    lblStartDate.setText(res.getString("Start Date (MM/DD/YYYY)"));
    lblEndDate.setText(res.getString("End Date (MM/DD/YYYY)"));
    this.setLayout(new BorderLayout());
    pnlNorth.setLayout(new BorderLayout());
    pnlSouth.setLayout(new BorderLayout());
    pnlCenter.setLayout(new BorderLayout());
    this.add(pnlNorth, BorderLayout.NORTH);
    this.add(pnlCenter, BorderLayout.CENTER);
    this.add(pnlSouth, BorderLayout.SOUTH);
    pnlNorth.setPreferredSize(new Dimension(833, 140));
    pnlCenter.setPreferredSize(new Dimension(833, 210));
    pnlSouth.setPreferredSize(new Dimension(833, 200));
    pnlCustomer.setPreferredSize(new Dimension(300, 140));
    pnlStore.setPreferredSize(new Dimension(300, 140));
    pnlNorth.add(pnlCustomer, BorderLayout.CENTER);
    pnlNorth.add(pnlStore, BorderLayout.EAST);
    pnlStore.add(lblRegisterID
        , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(5, 1, 5, 0), 48, 4));
    pnlStore.add(txtRegisterID
        , new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 48, 4));
    pnlStore.add(lblStoreID
        , new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(5, 1, 5, 0), 48, 4));
    pnlStore.add(cbxStoreId
        , new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER
        , GridBagConstraints.HORIZONTAL, new Insets(1, 5, 2, 5), -4, -5));
    pnlCenter.add(pnlTxn, BorderLayout.EAST);
    pnlTxn.setPreferredSize(new Dimension(300, 210));
    pnlCenter.add(pnlPerson, BorderLayout.CENTER);
    pnlPerson.setPreferredSize(new Dimension(300, 210));
    pnlPerson.add(lblAssociate
        , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(4, 1, 0, 0), 32, 8));
    pnlItemDetails.setPreferredSize(new Dimension(400, 165));
    pnlDate.setPreferredSize(new Dimension(400, 35));
    pnlSouth.add(pnlItemDetails, BorderLayout.NORTH);
    pnlItemDetails.add(lblSku
        , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(10, 2, 0, 0), 68, 7));
    pnlItemDetails.add(txtSku
        , new GridBagConstraints(1, 0, 4, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 17), 509, 4));
    pnlSouth.add(pnlDate, BorderLayout.SOUTH);
    pnlDate.add(lblStartDate
        , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(3, 3, 3, 0), 23, 7));
    pnlCustomer.add(lblID
        , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(2, 3, 5, 0), 30, 4));
    lblID.setPreferredSize(new Dimension(50, 25));
    txtID.setPreferredSize(new Dimension(180, 25));
    pnlCustomer.add(txtID
        , new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 55), 35, 5));
    pnlCustomer.add(lblFamilyName
        , new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 3, 5, 0), 30, 4));
    pnlCustomer.add(txtFamilyName
        , new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 5), 109, 5));
    pnlCustomer.add(lblName
        , new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(2, 3, 5, 0), 30, 5));
    pnlCustomer.add(txtName
        , new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(2, 0, 5, 5), 109, 3));
    pnlStore.add(lblCompanyCode
        , new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 1, 5, 0), 5, 6));
    pnlStore.add(txtCompanyCode
        , new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 111, 4));
    pnlStore.add(lblType
        , new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 1, 5, 0), 65, 6));
    pnlStore.add(cbxStoreType
        , new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER
        , GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), -3, -4));
    pnlPerson.add(txtAssociate
        , new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(4, 8, 0, 5), 124, 5));
    pnlPerson.add(lblCashier
        , new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(12, 1, 13, 0), 42, 7));
    pnlPerson.add(txtCashier
        , new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(11, 9, 13, 5), 123, 4));
    pnlTxn.add(lblTxnType
        , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(4, 3, 0, 0), 31, 6));
    pnlTxn.add(lblAmount
        , new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 16, 4));
    pnlTxn.add(lblBetween
        , new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets( -5, 3, 6, 0), 11, 4));
    pnlTxn.add(cbxTxnType
        , new GridBagConstraints(1, 0, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER
        , GridBagConstraints.HORIZONTAL, new Insets(4, 8, 0, 0), 17, -4));
    pnlTxn.add(txtCurrencyCode
        , new GridBagConstraints(4, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.BOTH, new Insets(4, 5, 0, 5), 33, 4));
    pnlTxn.add(txtAmountStart
        , new GridBagConstraints(1, 1, 1, 2, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(10, 7, 17, 3), 75, 3));
    pnlTxn.add(txtAmountEnd
        , new GridBagConstraints(3, 1, 2, 2, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(10, 0, 17, 5), 75, 3));
    pnlTxn.add(lblAnd
        , new GridBagConstraints(2, 1, 1, 2, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(10, 0, 17, 0), 8, 6));
    pnlItemDetails.add(lblModel
        , new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(10, 2, 0, 0), 139, 7));
    pnlItemDetails.add(lblStyle
        , new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(10, 33, 0, 0), 127, 7));
    pnlItemDetails.add(txtModel
        , new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 0), 164, 4));
    pnlItemDetails.add(txtStyle
        , new GridBagConstraints(2, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 33, 0, 0), 166, 4));
    pnlItemDetails.add(lblSupplier
        , new GridBagConstraints(3, 1, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(10, 16, 0, 17), 171, 7));
    pnlItemDetails.add(txtSupplier
        , new GridBagConstraints(3, 2, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 16, 0, 17), 208, 4));
    pnlItemDetails.add(lblFabric
        , new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 140, 7));
    pnlItemDetails.add(lblColor
        , new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 33, 0, 0), 145, 7));
    pnlItemDetails.add(lblYear
        , new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 42, 7));
    pnlItemDetails.add(lblSeason
        , new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 0, 0, 17), 104, 7));
    pnlItemDetails.add(txtFabric
        , new GridBagConstraints(0, 4, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 2, 4, 0), 165, 4));
    pnlItemDetails.add(txtColor
        , new GridBagConstraints(2, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 33, 4, 0), 166, 4));
    pnlItemDetails.add(txtYear
        , new GridBagConstraints(3, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 15, 4, 5), 58, 4));
    pnlItemDetails.add(txtSeason
        , new GridBagConstraints(4, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 0, 4, 17), 138, 4));
    pnlDate.add(txtStartDate
        , new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(3, 7, 3, 0), 155, 4));
    pnlDate.add(lblEndDate
        , new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(3, 7, 3, 0), 8, 7));
    pnlDate.add(txtEndDate
        , new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(3, 0, 3, 6), 133, 4));
    cbxStoreId.requestFocus();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean completeAttributes() {
    if (txtAmountStart.getText().length() > 0) {
      if (txtAmountEnd.getText().length() < 1) {
        txtAmountEnd.requestFocus();
        CMSApplet.theAppMgr.showErrorDlg(CMSApplet.res.getString("Enter an end amount"));
        return false;
      }
    }
    if (txtAmountEnd.getText().length() > 0) {
      if (txtAmountStart.getText().length() < 1) {
        txtAmountStart.requestFocus();
        CMSApplet.theAppMgr.showErrorDlg(CMSApplet.res.getString("Enter a start amount"));
        return false;
      }
    }
    if (txtStartDate.getText().length() > 0) {
      try {
        Date dtTxt = dateFormat.parse(txtStartDate.getText());
        if (dtTxt.after(new Date())) {
          txtStartDate.requestFocus();
          CMSApplet.theAppMgr.showErrorDlg("Start Date shouldn't be a future date");
          return false;
        }
        if (txtEndDate.getText().length() < 1) {
          txtEndDate.requestFocus();
          CMSApplet.theAppMgr.showErrorDlg(CMSApplet.res.getString("Please enter an End date"));
          return false;
        }
        try {
          Date endDate = dateFormat.parse(txtEndDate.getText());
          if (dtTxt.after(endDate)) {
            txtStartDate.requestFocus();
            CMSApplet.theAppMgr.showErrorDlg(CMSApplet.res.getString(
                "Start date can't be later than end date"));
            return false;
          }
        } catch (Exception e) {
          txtEndDate.requestFocus();
          CMSApplet.theAppMgr.showErrorDlg(CMSApplet.res.getString("Enter a valid End Date"));
          return false;
        }
        return true;
      } catch (Exception E) {
        txtStartDate.requestFocus();
        CMSApplet.theAppMgr.showErrorDlg(CMSApplet.res.getString("Enter a valid Start Date"));
        return false;
      }
    }
    if (txtEndDate.getText().length() > 0) {
      try {
        Date dtTxt = dateFormat.parse(txtEndDate.getText());
        if (dtTxt.after(new Date())) {
          txtEndDate.requestFocus();
          CMSApplet.theAppMgr.showErrorDlg(CMSApplet.res.getString(
              "End Date shouldn't be a future date"));
          return false;
        }
        if (txtStartDate.getText().length() < 1) {
          txtStartDate.requestFocus();
          CMSApplet.theAppMgr.showErrorDlg(CMSApplet.res.getString("Please enter a Start date"));
          return false;
        }
        try {
          Date startDate = dateFormat.parse(txtStartDate.getText());
          if (dtTxt.before(startDate)) {
            txtEndDate.requestFocus();
            CMSApplet.theAppMgr.showErrorDlg("End date can't be earlier than Start date");
            return false;
          }
        } catch (Exception e) {
          txtStartDate.requestFocus();
          CMSApplet.theAppMgr.showErrorDlg(CMSApplet.res.getString("Enter a valid Start Date"));
          return false;
        }
        return true;
      } catch (Exception E) {
        txtEndDate.requestFocus();
        CMSApplet.theAppMgr.showErrorDlg(CMSApplet.res.getString("Enter a valid End Date"));
        return false;
      }
    }
    if (getRegisterID() != null && getRegisterID().trim().length() != 0
        && (getSelectedStore() == null || getSelectedStore().trim().length() == 0)) {
      cbxStoreId.requestFocus();
      CMSApplet.theAppMgr.showErrorDlg(CMSApplet.res.getString(
          "Please select a Store for the Register"));
      return false;
    }
    return true;
  }

  /**
   * put your documentation comment here
   */
  public void populateStoreIdsAndTypes() {
    try {
      cmsStores = CMSStoreHelper.findAllStores(theAppMgr);
      if (cmsStores == null)
        return;
      Vector vecStoreTypes = new Vector();
      theStore = ((CMSStore)theAppMgr.getGlobalObject("STORE"));
      cbxStoreId.addItem("");
      cbxStoreType.addItem("");
      for (int iCtr = 0; iCtr < cmsStores.length; iCtr++) {
        cbxStoreId.addItem(cmsStores[iCtr].getId());
        String sTmp = cmsStores[iCtr].getBrandID();
        if (sTmp != null && sTmp.length() > 1 && !vecStoreTypes.contains(sTmp)) {
          vecStoreTypes.addElement(sTmp);
          cbxStoreType.addItem(sTmp);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   * @param ie
   */
  public void itemStateChanged(ItemEvent anEvent) {
    if ( (String) anEvent.getItem() == "") {
      cbxStoreType.setEnabled(true);
      txtCompanyCode.setEnabled(true);
      setCurrencyCode(theStore.getCurrencyType().getCode());
    }
    else {
      CMSStore selectedStore = getStoreForId( (String) anEvent.getItem());
      if (selectedStore == null)return;
      setSelectedStoreType(selectedStore.getBrandID());
      setCompanyCode(selectedStore.getCompanyCode());
      setCurrencyCode(selectedStore.getCurrencyType().getCode());
      cbxStoreType.setEnabled(false);
      txtCompanyCode.setEnabled(false);
    }
  }

  /**
   * put your documentation comment here
   * @exception Exception
   */
  private void populateTransactionTypes()
      throws Exception {
    StringTokenizer sTokens;
    try {
      ConfigMgr config = new ConfigMgr("pos.cfg");
      sTokens = new StringTokenizer(config.getString("TRANS_TYPES"), ",");
      vecTransTypeKeys = new Vector();
      String sTmp;
      vecTransTypeKeys.addElement("");
      cbxTxnType.addItem("");
      while (sTokens.hasMoreTokens()) {
        sTmp = sTokens.nextToken();
        if (config.getString(sTmp + ".CODE").length() > 0
            && config.getString(sTmp + ".DESC_KEY").length() > 0) {
          vecTransTypeKeys.addElement(config.getString(sTmp + ".CODE"));
          cbxTxnType.addItem(config.getString(sTmp + ".DESC_KEY"));
        }
      }
    } catch (Exception e) {}
  }

  /**  CMSPaymentMgr
   * Verifier for Date
   * <p>Title: Verifier</p>
   * <p>Description: Verifier for Date</p>
   * <p>Copyright: Copyright (c) 2005</p>
   * <p>Company: SkillNet Inc</p>
   * @author Manpreet S Bawa
   * @version 1.0
   */
  private class Verifier extends CMSInputVerifier {

    /**
     * Verify if input is valid
     * @param comp JComponent
     * @return boolean
     */
    public boolean verify(JComponent comp) {
      JCMSTextField txt = (JCMSTextField)comp;
      String sTxt = txt.getText().trim();
      if (sTxt.length() > 0) {
        try {
          if (sTxt.length() != 10)
            throw new Exception("not valid date");
          Date dtTxt = dateFormat.parse(sTxt);
          if (dtTxt.after(new Date())) {
            txt.requestFocus();
            CMSApplet.theAppMgr.showErrorDlg(CMSApplet.res.getString(
                "Date shouldn't be a future date"));
            return false;
          }
          if (txt == txtStartDate) {
            try {
              if (txtEndDate.getText().length() < 1)
                return true;
              Date endDate = dateFormat.parse(txtEndDate.getText());
              if (dtTxt.after(endDate)) {
                txt.requestFocus();
                CMSApplet.theAppMgr.showErrorDlg(CMSApplet.res.getString(
                    "Start date can't be later than end date"));
                return false;
              }
            } catch (Exception e) {
              txtEndDate.requestFocus();
              CMSApplet.theAppMgr.showErrorDlg(CMSApplet.res.getString("Enter a valid End Date"));
              return false;
            }
          }
          if (txt == txtEndDate) {
            try {
              Date dtStart = dateFormat.parse(txtStartDate.getText());
              if (dtTxt.before(dtStart)) {
                txt.requestFocus();
                CMSApplet.theAppMgr.showErrorDlg(CMSApplet.res.getString(
                    "End date can't be earlier than Start date"));
                return false;
              }
            } catch (Exception e) {
              txtStartDate.requestFocus();
              CMSApplet.theAppMgr.showErrorDlg(CMSApplet.res.getString("Please enter start date"));
              return false;
            }
          }
        } catch (Exception e) {
          txt.requestFocus();
          CMSApplet.theAppMgr.showErrorDlg(CMSApplet.res.getString("Enter a valid date"));
          return false;
        }
      }
      return true;
    }
  }


  private class NumericListener implements KeyListener {
    protected String KEYS_ALLOWED = "0123456789";

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
      if (txt == txtYear && txt.getText().trim().length() + 1 > 4) {
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

  private class NumericListener1 extends NumericListener{
	  NumericListener1(){
		  KEYS_ALLOWED = "0123456789-";  // 1508 reverted.
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
      //      if(ke.getKeyChar() == '/'
      //         &&
      //         (txt.getCaretPosition()!=2
      //         ||
      //         txt.getCaretPosition()!=5)
      //         )
      //      {
      //        ke.consume();
      //        return;
      //      }
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
}

