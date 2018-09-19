/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import com.chelseasystems.cs.swing.panel.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.address.*;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import java.awt.*;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * <p>Title:PrintFiscalDocumentCustPanel </p>
 * <p>Description: GUI for PrintFiscalDocument PAGE 1 </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 05-26-2005 | Manpreet  | N/A       | POS_104665_TS_FiscalDocuments_Rev0                 |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class PrintFiscalDocumentCustPanel extends JPanel {
  /**
   * Europe Address Panel
   */
  private EURAddressPanel eurAddressPanel;
  /**
   * Customer Panel
   */
  private JPanel pnlCustomer;
  /**
   * Customer Info Panel
   */
  private JPanel pnlCustInfo;
  /**
   * Company Info Panel
   */
  private JPanel pnlCompany;
  /**
   * Address Panel
   */
  private JPanel pnlAddress;
  /**
   * Gap panel
   */
  private JPanel pnlGapEast;
  /**
   * Company Code
   */
  private JCMSLabel lblCompanyCode;
  /**
   * Store Code
   */
  private JCMSLabel lblStoreCode;
  /**
   * Register ID
   */
  private JCMSLabel lblRegisterID;
  /**
   * Fiscal Receipt Number
   */
  private JCMSLabel lblFiscalRecNum;
  /**
   * Fistcal Receipt Date
   */
  private JCMSLabel lblFiscalReceiptDate;
  /**
   * Compnay Code Text
   */
  private JCMSTextField txtCompanyCode;
  /**
   * Store Code Text
   */
  private JCMSTextField txtStoreCode;
  /**
   * Register ID text
   */
  private JCMSTextField txtRegisterID;
  /**
   * Fiscal Receipt Number text
   */
  private JCMSTextField txtFiscalRecNum;
  /**
   * Fiscal Receipt Date
   */
  private JCMSTextField txtFiscalRecDate;
  /**
   * ComanyName 1
   */
  private JCMSLabel lblCompanyName1;
  /**
   * CompanyName1 Text
   */
  private JCMSTextField txtCompanyName1;
  /**
   * CompanyName2 Text
   */
  private JCMSTextField txtCompanyName2;
  /**
   * ComanyName2
   */
  private JCMSLabel lblCompanyName2;
  /**
   * DateFormat
   */
  private final SimpleDateFormat DATE_FORMAT = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
  public static final int COMPANY_NAME1 = 0;
  public static final int COMPANY_NAME2 = 1;
  public static final int ADDRESS_LINE1 = 2;
  public static final int CITY = 3;
  public static final int COUNTY = 4;
  public static final int ZIPCODE = 5;
  public static final int STATE = 6;
  public static final int COUNTRY = 7;
  public static final int PHONE = 8;

  /**
   * Default Constructor
   */
  public PrintFiscalDocumentCustPanel() {
    try {
      jbInit();
      setReadOnlyFields();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Set Application Manager
   * @param theAppMgr IApplicationManager
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    this.setBackground(theAppMgr.getBackgroundColor());
    pnlCustomer.setBackground(theAppMgr.getBackgroundColor());
    pnlCustInfo.setBackground(theAppMgr.getBackgroundColor());
    pnlCompany.setBackground(theAppMgr.getBackgroundColor());
    pnlAddress.setBackground(theAppMgr.getBackgroundColor());
    pnlGapEast.setBackground(theAppMgr.getBackgroundColor());
    eurAddressPanel.setAppMgr(theAppMgr);
    lblCompanyCode.setAppMgr(theAppMgr);
    lblStoreCode.setAppMgr(theAppMgr);
    lblRegisterID.setAppMgr(theAppMgr);
    lblFiscalRecNum.setAppMgr(theAppMgr);
    lblFiscalReceiptDate.setAppMgr(theAppMgr);
    txtCompanyCode.setAppMgr(theAppMgr);
    txtStoreCode.setAppMgr(theAppMgr);
    txtRegisterID.setAppMgr(theAppMgr);
    txtFiscalRecNum.setAppMgr(theAppMgr);
    txtFiscalRecDate.setAppMgr(theAppMgr);
    lblCompanyName1.setAppMgr(theAppMgr);
    txtCompanyName1.setAppMgr(theAppMgr);
    txtCompanyName2.setAppMgr(theAppMgr);
    lblCompanyName2.setAppMgr(theAppMgr);
  }

  /**
   * Reset the GUI
   */
  public void reset() {
    txtCompanyCode.setText("");
    txtStoreCode.setText("");
    txtRegisterID.setText("");
    txtFiscalRecNum.setText("");
    txtFiscalRecDate.setText("");
    txtCompanyName1.setText("");
    txtCompanyName2.setText("");
    eurAddressPanel.reset();
  }

  /**
   * Enable/Disable GUI
   * @param bEnabled boolean
   */
  public void setEnabled(boolean bEnabled) {
    //    txtCompanyCode.setEnabled(bEnabled);
    //    txtStoreCode.setEnabled(bEnabled);
    //    txtRegisterID.setEnabled(bEnabled);
    //    txtFiscalRecNum.setEnabled(bEnabled);
    //    txtFiscalRecDate.setEnabled(bEnabled);
    txtCompanyName1.setEnabled(bEnabled);
    txtCompanyName2.setEnabled(bEnabled);
    eurAddressPanel.setEnabled(bEnabled);
  }

  /**
   * Set Address
   * @param address Address
   */
  public void setAddress(Address address) {
    if (address == null)
      return;
    eurAddressPanel.setCustomerAddress(address);
  }

  /**
   * Get Address
   * @return Address
   */
  public Address getAddress() {
//    return eurAddressPanel.getCustomerAddress();
    return eurAddressPanel.getCustomerAddressForFiscalDocument();
  }

  /**
   * Company Code
   * @param sValue String
   */
  public void setCompanyCode(String sValue) {
    if (sValue == null || sValue.length() < 0)
      return;
    txtCompanyCode.setText(sValue);
  }

  /**
   * Company Code
   * @return String
   */
  public String getCompanyCode() {
    return txtCompanyCode.getText().trim();
  }

  /**
   * StoreCode
   * @param sValue String
   */
  public void setStoreCode(String sValue) {
    if (sValue == null || sValue.length() < 0)
      return;
    txtStoreCode.setText(sValue);
  }

  /**
   * StoreCode
   * @return String
   */
  public String getStoreCode() {
    return txtStoreCode.getText().trim();
  }

  /**
   * RegisterID
   * @param sValue String
   */
  public void setRegisterID(String sValue) {
    if (sValue == null || sValue.length() < 0)
      return;
    txtRegisterID.setText(sValue);
  }

  /**
   * RegisterID
   * @return String
   */
  public String getRegisterID() {
    return txtRegisterID.getText().trim();
  }

  /**
   * FiscalReceiptNumber
   * @param sValue String
   */
  public void setFiscalReceiptNumber(String sValue) {
    if (sValue == null || sValue.length() < 0)
      return;
    txtFiscalRecNum.setText(sValue);
  }

  /**
   * FiscalReceiptNumber
   * @return String
   */
  public String getFiscalReceiptNumber() {
    return txtFiscalRecNum.getText().trim();
  }

  /**
   * FiscalReceiptDate
   * @param sValue String
   */
  public void setFiscalReceiptDate(Date dtValue) {
    if (dtValue == null)
      return;
    try {
      txtFiscalRecDate.setText(DATE_FORMAT.format(dtValue));
    } catch (Exception e) {}
  }

  /**
   * put your documentation comment here
   * @param iField
   */
  public void requestFocusTo(int iField) {
    switch (iField) {
      case COMPANY_NAME1:
        txtCompanyName1.requestFocus();
        break;
      case COMPANY_NAME2:
        txtCompanyName2.requestFocus();
        break;
      case ADDRESS_LINE1:
        eurAddressPanel.getAddressLine1Focus();
        break;
      case CITY:
        eurAddressPanel.getCityFocus();
        break;
      case COUNTY:

        //eurAddressPanel.get
        break;
      case ZIPCODE:
        eurAddressPanel.getZipCodeFocus();
        break;
      case STATE:
        eurAddressPanel.getStateFocus();
        break;
      case COUNTRY:
        eurAddressPanel.getCountryFocus();
        break;
      case PHONE:
        eurAddressPanel.getPhone1Focus();
    }
  }

  /**
   * FiscalReceiptDate
   * @return String
   */
  public Date getFiscalReceiptDate() {
    Date dtTmp = null;
    try {
      dtTmp = DATE_FORMAT.parse(txtFiscalRecDate.getText().trim());
    } catch (Exception e) {}
    return dtTmp;
  }

  /**
   * CompanyName1
   * @param sValue String
   */
  public void setCompanyName1(String sValue) {
    if (sValue == null || sValue.length() < 0)
      return;
    txtCompanyName1.setText(sValue);
  }

  /**
   * CompanyNam1
   * @return String
   */
  public String getCompanyName1() {
    return txtCompanyName1.getText().trim();
  }

  /**
   * CompanyName2
   * @param sValue String
   */
  public void setCompanyName2(String sValue) {
    if (sValue == null || sValue.length() < 0)
      return;
    txtCompanyName2.setText(sValue);
  }

  /**
   * put your documentation comment here
   */
  public void clearCustomerDetails() {
    txtCompanyName1.setText("");
    txtCompanyName2.setText("");
    eurAddressPanel.reset();
  }

  /**
   * CompanyNam2
   * @return String
   */
  public String getCompanyName2() {
    return txtCompanyName2.getText().trim();
  }

  /**
   * put your documentation comment here
   */
  private void setReadOnlyFields() {
    txtCompanyCode.setEnabled(false);
    txtStoreCode.setEnabled(false);
    txtRegisterID.setEnabled(false);
    txtFiscalRecNum.setEnabled(false);
    txtFiscalRecDate.setEnabled(false);
  }

  /**
   * Initialize Components
   * @throws Exception
   */
  private void jbInit()
      throws Exception {
    eurAddressPanel = new EURAddressPanel(true);
    AddressMgr addressMgr = new AddressMgr();
    eurAddressPanel.setAddressTypes(addressMgr.getAddressTypes(), addressMgr.getAddressTypeKeys());
    eurAddressPanel.setPhoneTypes(addressMgr.getPhoneTypes(), addressMgr.getPhoneTypeKeys());
    pnlCustomer = new JPanel();
    pnlCustInfo = new JPanel();
    pnlCompany = new JPanel();
    pnlAddress = new JPanel();
    pnlGapEast = new JPanel();
    lblCompanyCode = new JCMSLabel();
    lblStoreCode = new JCMSLabel();
    lblRegisterID = new JCMSLabel();
    lblFiscalRecNum = new JCMSLabel();
    lblFiscalReceiptDate = new JCMSLabel();
    txtCompanyCode = new JCMSTextField();
    txtStoreCode = new JCMSTextField();
    txtRegisterID = new JCMSTextField();
    txtFiscalRecNum = new JCMSTextField();
    txtFiscalRecDate = new JCMSTextField();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    lblCompanyName1 = new JCMSLabel();
    txtCompanyName1 = new JCMSTextField();
    txtCompanyName2 = new JCMSTextField();
    lblCompanyName2 = new JCMSLabel();
    GridBagLayout gridBagLayout3 = new GridBagLayout();
    this.setLayout(new BorderLayout());
    pnlCompany.setLayout(gridBagLayout2);
    pnlAddress.setLayout(new BorderLayout());
    pnlCompany.setPreferredSize(new Dimension(507, 50));
    pnlAddress.setPreferredSize(new Dimension(322, 226));
    pnlGapEast.setPreferredSize(new Dimension(200, 227));
    lblCompanyCode.setText(CMSApplet.res.getString("Co. Code"));
    lblStoreCode.setText(CMSApplet.res.getString("Store Code"));
    lblRegisterID.setText(CMSApplet.res.getString("Reg. ID"));
    lblFiscalRecNum.setText(CMSApplet.res.getString("Fiscal Receipt No."));
    lblFiscalReceiptDate.setText(CMSApplet.res.getString("Fiscal Receipt Date"));
    lblCompanyName1.setText(CMSApplet.res.getString("FamilyName/CompanyName"));
    lblCompanyName2.setText(CMSApplet.res.getString("Sir Name/Company Name 2"));
    pnlCustInfo.setLayout(gridBagLayout3);
    this.add(pnlCompany, BorderLayout.NORTH);
    this.add(pnlCustomer, BorderLayout.CENTER);
    pnlCustomer.setLayout(new BorderLayout());
    pnlCustInfo.setPreferredSize(new Dimension(833, 50));
    pnlCustomer.add(pnlCustInfo, BorderLayout.NORTH);
    pnlCustomer.add(pnlGapEast, BorderLayout.EAST);
    pnlCustomer.add(pnlAddress, BorderLayout.CENTER);
    pnlAddress.add(eurAddressPanel, BorderLayout.CENTER);
    pnlAddress.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder()
        , CMSApplet.res.getString("Address")));
    pnlCustomer.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder()
        , CMSApplet.res.getString("Customer Information")));
    setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ""));
    pnlCompany.add(lblCompanyCode
        , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(5, 4, 0, 0), 23, 7));
    pnlCompany.add(lblStoreCode
        , new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 31, 7));
    pnlCompany.add(lblFiscalRecNum
        , new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 7, 7));
    pnlCompany.add(lblFiscalReceiptDate
        , new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(5, 6, 0, 2), 9, 7));
    pnlCompany.add(lblRegisterID
        , new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 39, 7));
    pnlCompany.add(txtCompanyCode
        , new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 4, 6, 5), 66, 4));
    pnlCompany.add(txtStoreCode
        , new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 0, 6, 5), 85, 4));
    pnlCompany.add(txtRegisterID
        , new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 0, 6, 5), 73, 4));
    pnlCompany.add(txtFiscalRecNum
        , new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 0, 6, 5), 98, 4));
    pnlCompany.add(txtFiscalRecDate
        , new GridBagConstraints(4, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 7, 6, 5), 106, 4));
    pnlCustInfo.add(txtCompanyName1
        , new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 4, 9, 0), 215, 4));
    pnlCustInfo.add(lblCompanyName1
        , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(2, 4, 0, 48), 18, 9));
    pnlCustInfo.add(lblCompanyName2
        , new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(2, 28, 0, 56), 29, 9));
    pnlCustInfo.add(txtCompanyName2
        , new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 28, 9, 18), 211, 4));
  }
}

