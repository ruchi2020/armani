/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
package com.chelseasystems.cs.swing.panel;

import javax.swing.*;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.swing.bean.JCMSTextField;
import com.chelseasystems.cr.swing.bean.JCMSLabel;
import com.chelseasystems.cr.swing.bean.JCMSComboBox;
import com.chelseasystems.cr.swing.bean.JCMSCheckBox;
import java.awt.*;
import java.util.Date;

import com.chelseasystems.cs.swing.CMSTextFilter;

/**
 * <p>Title:CustomerDetailPanel_EUR.java </p>
 *
 * <p>Description: Displays Customer Detail Information for Europe</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company:SkillNet Inc. </p>
 *
 * @author Sandhya Ajit
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 11/29/2006 | Sandhya   | PCR 9     |Move age range and the birth dates.                 |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class CustomerDetailPanel_EUR extends CustomerDetailPanel {
  
  private static final long serialVersionUID = 1L;
  
  private CustomerBasicPanel_EUR pnlCustBasic;
  
  /**
   * Primary Email
   */
  protected JCMSLabel lblPrimaryEmail;
  
  /**
   * Secondary Email
   */
  protected JCMSLabel lblSecondEmail;
  
  /**
   * PrimaryEmail Value
   */
  protected JCMSTextField txtPrimaryEmail;
  
  /**
   * SecondaryEmail Value
   */
  protected JCMSTextField txt2ndEmail;
    
  /**
   * Return Mail
   */
  protected JCMSCheckBox chbReturnMail;

  /**
   * Default Constructor
   */
  public CustomerDetailPanel_EUR() {
    try {
      jbInit();
      populatePaymentTypes();
      populateSupplierPaymentTypes();
      setEnabled(false);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  /**
   * put your documentation comment here
   */
  public void reset() {
    txtCompany.setText("");
    txtInterCoCode.setText("");
    txtAcctNo.setText("");
    txtBarcode.setText("");
    txtVatNo.setText("");
    if (cbxPaymentType.getItemCount() > 0)
      cbxPaymentType.setSelectedIndex(0);
    txtFiscalCode.setText("");
    txtIDType.setText("");
    txtDocNo.setText("");
    if (cbxSupPayment.getItemCount() > 0)
      cbxSupPayment.setSelectedIndex(0);
    txtBank.setText("");
    txtPlcOfIssue.setText("");
    txtDateOfIssue.setText("");
    txtPrimaryEmail.setText("");
    txt2ndEmail.setText("");
  }
  
  /**
   * put your documentation comment here
   * @param bEnabled
   */
  public void setEnabled(boolean bEnabled) {
    txtVatNo.setEnabled(bEnabled);
    cbxPaymentType.setEnabled(bEnabled);
    txtFiscalCode.setEnabled(bEnabled);
    txtIDType.setEnabled(bEnabled);
    txtDocNo.setEnabled(bEnabled);
    cbxSupPayment.setEnabled(bEnabled);
    txtBank.setEnabled(bEnabled);
    txtPlcOfIssue.setEnabled(bEnabled);
    txtDateOfIssue.setEnabled(bEnabled);
    txtPrimaryEmail.setEnabled(bEnabled);
    txt2ndEmail.setEnabled(bEnabled);
  }
  
  /**
   * Set Application Manager
   * @param theAppMgr ApplicationManager
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    this.theAppMgr = theAppMgr;
    this.setBackground(theAppMgr.getBackgroundColor());
    //Loop through and set Application managers for all
    // JCMS components.
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    pnlPersInfo.setPreferredSize(new Dimension((int)(400 * r), (int)(120 * r)));
    pnlFiscal.setPreferredSize(new Dimension((int)(400 * r), (int)(100 * r)));
    pnlCompanyInfo.setPreferredSize(new Dimension((int)(400 * r), (int)(120 * r)));
    Component comps[] = this.getComponents();
    for (int iCtr = 0; iCtr < comps.length; iCtr++) {
      if (comps[iCtr] instanceof JPanel) {
        comps[iCtr].setBackground(theAppMgr.getBackgroundColor());
        getJCMSComponent((JPanel)comps[iCtr], theAppMgr);
      } else if (comps[iCtr].getClass().getName().indexOf("JCMS") != -1) {
        setAppMgrForJCMSBean(comps[iCtr], theAppMgr);
      }
    }
  }
  
  /**
   * Set primary Email.
   * @param sValue PrimaryEmail/SMS
   */
  public void setPrimaryEmail(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtPrimaryEmail.setText(sValue);
  }

  /**
   * Get PrimaryEmail
   * @return PrimaryEmail/SMS
   */
  public String getPrimaryEmail() {
    return txtPrimaryEmail.getText();
  }

  /**
   * Set SecondaryEmail.
   * @param sValue SecondaryEmail/SMS
   */
  public void setSecondaryEmail(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txt2ndEmail.setText(sValue);
  }

  /**
   * Set SecondaryEmail
   * @return SecondaryEmail/SMS
   */
  public String getSecondaryEmail() {
    return txt2ndEmail.getText();
  }

  /**
   * Set ReturnMail
   * @param bReturnMail True/False
   */
  public void setReturnMail(boolean bReturnMail) {
    chbReturnMail.setSelected(bReturnMail);
  }

  /**
   * GetReturnMail
   * @return ReturnMail
   */
  public boolean getReturnMail() {
    return chbReturnMail.isSelected();
  }

  /**
   * Set ReturnMail
   * @param bReturnMail Y/N
   */
  public void setReturnMail(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    chbReturnMail.setSelected(sValue.toUpperCase().equals("Y"));
  }

  /**
   * Get ReturnMail as String
   * @return Y or N
   */
  public String getReturnMailString() {
    if (chbReturnMail.isSelected())
      return "Y";
    return "N";
  }
  
  /************* Reference Methods Starts  *************/
  /**
   * BirthDay
   * @param sValue BirthDay
   */
  public void setBirthDay(String sValue) {
	(this.pnlCustBasic).setBirthDay(sValue);
  }  
  
  /**
   * BirthDay
   * @return BirthDay
   */
  public String getBirthDay() {
    return (this.pnlCustBasic.getBirthDay());
  }
  
  /**
   * BirthMonth
   * @param sValue BirthMonth
   */
  public void setBirthMonth(String sValue) {
    (this.pnlCustBasic).setBirthMonth(sValue);
  }

  /**
   * BirthMonth
   * @return BirthMonth
   */
  public String getBirthMonth() {
    return (this.pnlCustBasic.getBirthMonth());
  }
  
  /**
   * isValidDateOfBirth
   */
  public boolean isValidDateOfBirth() {
	return this.pnlCustBasic.isValidDateOfBirth();
  }
  
  /**
   * Date of Birth
   */
  public void setDateOfBirth(String sValue, String ageRange) {
	this.pnlCustBasic.setDateOfBirth(sValue, ageRange);
  }
  
  /**
   * Date of Birth
   */
  public Date getBirthDate() throws BusinessRuleException {
	return this.pnlCustBasic.getBirthDate();  
  }
  
  /**
   * Age
   */
  public void setAgeRangeCode(String ageRange) {
	this.pnlCustBasic.setAgeRangeCode(ageRange);
  }
  
  /**
   * Age
   */
  public String getAgeRangeCode() {
	return this.pnlCustBasic.getAgeRangeCode();
  }
  
  /**
   * Sets the basic ref.
   * @param CustomerBasicPanel_EUR pnlCustBasic
   */
  public void setBasicReference(CustomerBasicPanel_EUR pnlCustBasic) {
	this.pnlCustBasic = pnlCustBasic;
  }
  /************* Reference Methods Ends  *************/
 
  /**
   * Initialize components.
   */
  private void initComponents() {
    pnlPersInfo = new JPanel();
    pnlCompanyInfo = new JPanel();
    pnlFiscal = new JPanel();
    lblCompany = new JCMSLabel();
    txtCompany = new JCMSTextField();
    lblInterCoCode = new JCMSLabel();
    txtInterCoCode = new JCMSTextField();
    lblAcctNo = new JCMSLabel();
    txtAcctNo = new JCMSTextField();
    lblBarcode = new JCMSLabel();
    txtBarcode = new JCMSTextField();
    lblStatus = new JCMSLabel();
    lblStatusValue = new JCMSLabel();
    lblVatNo = new JCMSLabel();
    txtVatNo = new JCMSTextField();
    lblPaymentType = new JCMSLabel();
    cbxPaymentType = new JCMSComboBox();
    lblFiscalCode = new JCMSLabel();
    txtFiscalCode = new JCMSTextField();
    lblIDType = new JCMSLabel();
    txtIDType = new JCMSTextField();
    lblDocNo = new JCMSLabel();
    txtDocNo = new JCMSTextField();
    lblSupplier = new JCMSLabel();
    cbxSupPayment = new JCMSComboBox();
    lblBank = new JCMSLabel();
    txtBank = new JCMSTextField();
    lblPlcOfIssue = new JCMSLabel();
    txtPlcOfIssue = new JCMSTextField();
    lblDateOfIssue = new JCMSLabel();
    txtDateOfIssue = new JCMSTextField();
    txtDateOfIssue.setName("DateOfIssue");
    lblPrimaryEmail = new JCMSLabel();
    lblSecondEmail = new JCMSLabel();
    txtPrimaryEmail = new JCMSTextField();
    txt2ndEmail = new JCMSTextField();
    chbReturnMail = new JCMSCheckBox();
    chbReturnMail.setEnabled(false);
    makeReadOnly();
    lblCompany.setLabelFor(txtCompany);
    lblCompany.setText(RESOURCE.getString("Company"));
    lblInterCoCode.setLabelFor(txtInterCoCode);
    lblInterCoCode.setText(RESOURCE.getString("Inter Co Code"));
    lblAcctNo.setLabelFor(txtAcctNo);
    lblAcctNo.setText(RESOURCE.getString("Account No"));
    lblBarcode.setLabelFor(txtBarcode);
    lblBarcode.setText(RESOURCE.getString("Barcode"));
    lblStatus.setLabelFor(lblStatusValue);
    lblStatus.setPreferredSize(new Dimension(20, 15));
    lblStatus.setText(RESOURCE.getString("Status"));
    lblStatusValue.setText("");
    lblVatNo.setLabelFor(txtVatNo);
    lblVatNo.setText(RESOURCE.getString("Vat No"));
    lblPaymentType.setLabelFor(cbxPaymentType);
    lblPaymentType.setText(RESOURCE.getString("Payment Type"));
    lblFiscalCode.setLabelFor(txtFiscalCode);
    lblFiscalCode.setText(RESOURCE.getString("Fiscal Code"));
    lblIDType.setLabelFor(txtIDType);
    lblIDType.setText(RESOURCE.getString("ID Type"));
    lblDocNo.setLabelFor(txtDocNo);
    lblDocNo.setText(RESOURCE.getString("Document Number"));
    lblSupplier.setLabelFor(cbxSupPayment);
    lblSupplier.setText(RESOURCE.getString("Supplier Payment"));
    lblBank.setLabelFor(txtBank);
    lblBank.setText(RESOURCE.getString("Bank"));
    lblPlcOfIssue.setLabelFor(txtPlcOfIssue);
    lblPlcOfIssue.setText(RESOURCE.getString("Place of Issue"));
    lblDateOfIssue.setLabelFor(txtDateOfIssue);
    lblDateOfIssue.setText(RESOURCE.getString("Date of Issue"));
    lblPrimaryEmail.setText(RESOURCE.getString("Primary Email/SMS Address"));
    lblSecondEmail.setText(RESOURCE.getString("2nd Email/SMS Address"));
    chbReturnMail.setHorizontalTextPosition(SwingConstants.LEADING);
    chbReturnMail.setText(RESOURCE.getString("Return Mail"));
    pnlPersInfo.setLayout(new BorderLayout());
    pnlFiscal.setLayout(new GridBagLayout());
    pnlFiscal.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder()
        , RESOURCE.getString("Fiscal Details")));
    pnlCompanyInfo.setLayout(new GridBagLayout());
    pnlCompanyInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder()
        , ""));
    pnlPersInfo.add(pnlCompanyInfo, java.awt.BorderLayout.CENTER);
  }
  
  /**
   * Layout components.
   * @throws Exception
   */
  protected void jbInit()
      throws Exception {
    initComponents();
    this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder()
        , "Detail Information"));
    this.setLayout(new BorderLayout());
    this.add(pnlPersInfo, java.awt.BorderLayout.NORTH);
    this.add(pnlFiscal, java.awt.BorderLayout.CENTER);
    pnlCompanyInfo.add(lblCompany
        , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 75, 0));
    pnlCompanyInfo.add(lblInterCoCode
        , new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 75, 0));
    pnlCompanyInfo.add(txtInterCoCode
        , new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 75, 1));
    pnlCompanyInfo.add(lblBarcode
        , new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 75, 0));
    
    pnlFiscal.add(txtPrimaryEmail
            , new GridBagConstraints(0, 9, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.HORIZONTAL, new Insets(0, 4, 4, 1), 165, 1));
    pnlFiscal.add(txt2ndEmail
            , new GridBagConstraints(1, 9, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.HORIZONTAL, new Insets(0, 4, 4, 1), 151, 1));
   
    pnlFiscal.add(lblIDType
        , new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 4, 0, 78), 46, 0));
    pnlFiscal.add(txtFiscalCode
        , new GridBagConstraints(0, 3, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.BOTH, new Insets(0, 4, 0, -30), 3, 1));
    pnlFiscal.add(txtIDType
        , new GridBagConstraints(0, 5, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 4, 0, -30), 4, 1));
    pnlCompanyInfo.add(lblAcctNo
        , new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 75, 0));
    pnlCompanyInfo.add(txtAcctNo
        , new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 0, 4, 5), 75, 1));
    pnlCompanyInfo.add(txtBarcode
        , new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 0, 4, 0), 75, 1));
    
    pnlFiscal.add(lblPrimaryEmail
    		, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.NONE, new Insets(0, 4, 0, 0), 65, 0));
    pnlFiscal.add(lblSecondEmail
            , new GridBagConstraints(1, 8, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.NONE, new Insets(0, 4, 0, 0), 51, 0));
    pnlFiscal.add(chbReturnMail
            , new GridBagConstraints(3, 8, 1, 2, 1.0, 0.0, GridBagConstraints.CENTER
            , GridBagConstraints.NONE, new Insets(0, 13, 7, 3), 51, 1));     
   
    pnlFiscal.add(lblFiscalCode
        , new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 4, 0, 0), 19, 0));
    pnlFiscal.add(lblVatNo
        , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets( -6, 4, 0, 0), 22, 0));
    pnlFiscal.add(lblDocNo
        , new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 4, 0, 68), 69, 0));
    pnlFiscal.add(txtVatNo
        , new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
        , GridBagConstraints.BOTH, new Insets(0, 4, 0, -30), 2, 1));
    pnlFiscal.add(txtDocNo
        , new GridBagConstraints(0, 7, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 4, 3, 25), -1, 1));
    pnlFiscal.add(lblPlcOfIssue
        , new GridBagConstraints(1, 6, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, -20, 0, 52), 66, 0));
    pnlFiscal.add(lblBank
        , new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 45, 0, 0), 77, 0));
    pnlFiscal.add(lblSupplier
        , new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 45, 0, 0), 8, 0));
    pnlFiscal.add(lblPaymentType
        , new GridBagConstraints(2, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets( -6, 45, 0, 0), 89, 0));
    pnlFiscal.add(cbxSupPayment
        , new GridBagConstraints(2, 3, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 45, 0, 0), 44, 1));
    pnlFiscal.add(cbxPaymentType
        , new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 45, 0, 0), 44, 1));
    pnlFiscal.add(txtPlcOfIssue
        , new GridBagConstraints(1, 7, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, -20, 3, -20), 2, 1));
    pnlFiscal.add(lblDateOfIssue
        , new GridBagConstraints(3, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 25, 0, 4), 94, 0));
    pnlFiscal.add(txtBank
        , new GridBagConstraints(2, 5, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 45, 0, 2), 11, 0));
    pnlFiscal.add(txtDateOfIssue
        , new GridBagConstraints(3, 7, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 25, 3, 2), 26, 1));
    pnlCompanyInfo.add(lblStatus
        , new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(2, 5, 0, 0), 30, 0));
    pnlCompanyInfo.add(lblStatusValue
        , new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(2, 1, 0, 5), 35, 0));
    pnlCompanyInfo.add(txtCompany
        , new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 75, 1));
    /**
     * Added TextFilter
     */
    // MSB 02/10/2006
    // To resolve Double byte issues
    // we need to make sure text isn't filtered
    // and only length restriction is imposed on input.
    txtVatNo.setDocument(new CMSTextFilter(25));
    txtFiscalCode.setDocument(new CMSTextFilter(25));
    txtIDType.setDocument(new CMSTextFilter(20));
    txtDocNo.setDocument(new CMSTextFilter(25));
    txtPlcOfIssue.setDocument(new CMSTextFilter(25));
    txtDateOfIssue.setDocument(new CMSTextFilter(10));
    txtAcctNo.setDocument(new CMSTextFilter(11));
    txtBarcode.setDocument(new CMSTextFilter(11));
    txtInterCoCode.setDocument(new CMSTextFilter(11));
    txtCompany.setDocument(new CMSTextFilter(11));
    txtPrimaryEmail.setDocument(new CMSTextFilter(40));
    txt2ndEmail.setDocument(new CMSTextFilter(40));
  }
  
  /**
   * put your documentation comment here
   */
  public void requestFocusOnPrimaryEmail() {
    SwingUtilities.invokeLater(new Runnable() {

      /**
       * put your documentation comment here
       */
      public void run() {
        txtPrimaryEmail.requestFocus();
      }
    });
  }

  /**
   * put your documentation comment here
   */
  public void requestFocusOnSecondaryEmail() {
    SwingUtilities.invokeLater(new Runnable() {

      /**
       * put your documentation comment here
       */
      public void run() {
        txt2ndEmail.requestFocus();
      }
    });
  }
  
  /**
   * put your documentation comment here
   */
  public void requestFocusOnFirstField() {
	  requestFocusOnVat();
	  //requestFocusOnPrimaryEmail();
  }

  public void requestFocusOnVat() {
	    SwingUtilities.invokeLater(new Runnable() {
	      /**
	       * put your documentation comment here
	       */
	      public void run() {
	    	  txtVatNo.requestFocusInWindow();
	      }
	    });
  }
  
	public JCMSTextField getLastFieldOnScreen() {
		return txt2ndEmail;
	}

}
