package com.chelseasystems.cs.swing.panel;

import java.awt.*;
import javax.swing.JPanel;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import javax.swing.border.*;

/**
* <p>Title:CustAdvanceSearchPanel </p>
* <p>Description: </p>
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
| 1    | 06-30-2005 | Manpreet  | N/A       | POS_104665_FS_CustomerLookup_Rev1                  |
+------+------------+-----------+-----------+----------------------------------------------------+
*/
public class CustAdvanceSearchPanel extends JPanel
{
  private JCMSLabel lblFamilyName;
  private JCMSLabel lblFirstName;
  private JCMSTextField txtFamilyName;
  private JCMSTextField txtFirstName;
  private JCMSLabel lblAddress;
  private JCMSTextField txtAddress;
  private JCMSLabel lblCity;
  private JCMSTextField txtCity;
  private JCMSLabel lblState;
  private JCMSLabel lblZipCode;
  private JCMSTextField txtState;
  private JCMSTextField txtZipCode;
  private JCMSLabel lblCountry;
  private JCMSLabel lblPhone;
  private JCMSTextField txtCountry;
  private JCMSTextField txtPhone;
  private JCMSLabel lblFiscalNumber;
  private JCMSLabel lblVatNumber;
  private JCMSLabel lblDocumentNumber;
  private JCMSTextField txtFiscalNumber;
  private JCMSTextField txtVatNumber;
  private JCMSTextField txtDocumentNumber;

  private JPanel pnlSearch;
  private JPanel pnlGap;

  public CustAdvanceSearchPanel()
  {
    try
    {
      jbInit();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public String getFamilyName()
  {
    return txtFamilyName.getText().trim();
  }

  public String getFirstName()
  {
    return txtFirstName.getText().trim();
  }

  public String getAddressLine1()
  {
    return txtAddress.getText().trim();
  }

  public String getCity()
  {
    return txtCity.getText().trim();
  }

  public String getState()
  {
    return txtState.getText().trim();
  }

  public String getZipCode()
  {
    return txtZipCode.getText().trim();
  }

  public String getCountry()
  {
    return txtCountry.getText().trim();
  }

  public String getPhone()
  {
    return txtPhone.getText().trim();
  }

  public String getVATNumber()
  {
    return txtVatNumber.getText().trim();
  }

  public String getFiscalCode()
  {
    return txtFiscalNumber.getText().trim();
  }

  public String getDocumentNumber()
  {
    return txtDocumentNumber.getText().trim();
  }

  public void requestFocusToFamilyName()
  {
	  pnlSearch.requestFocusInWindow();
	  txtFamilyName.requestFocusInWindow();
  }

  public void reset()
  {
    txtFamilyName.setText("");
    txtFirstName.setText("");
    txtAddress.setText("");
    txtCity.setText("");
    txtState.setText("");
    txtZipCode.setText("");
    txtCountry.setText("");
    txtPhone.setText("");
    txtVatNumber.setText("");
    txtFiscalNumber.setText("");
    txtDocumentNumber.setText("");
  }

  public void setAppMgr(IApplicationManager theAppMgr)
  {
    setBackground(theAppMgr.getBackgroundColor());
    pnlSearch.setBackground(theAppMgr.getBackgroundColor());
    pnlGap.setBackground(theAppMgr.getBackgroundColor());
    lblFamilyName.setAppMgr(theAppMgr);
    lblFirstName.setAppMgr(theAppMgr);
    txtFamilyName.setAppMgr(theAppMgr);
    txtFirstName.setAppMgr(theAppMgr);
    lblAddress.setAppMgr(theAppMgr);
    txtAddress.setAppMgr(theAppMgr);
    lblCity.setAppMgr(theAppMgr);
    txtCity.setAppMgr(theAppMgr);
    lblState.setAppMgr(theAppMgr);
    lblZipCode.setAppMgr(theAppMgr);
    txtState.setAppMgr(theAppMgr);
    txtZipCode.setAppMgr(theAppMgr);
    lblCountry.setAppMgr(theAppMgr);
    lblPhone.setAppMgr(theAppMgr);
    txtCountry.setAppMgr(theAppMgr);
    txtPhone.setAppMgr(theAppMgr);
    lblFiscalNumber.setAppMgr(theAppMgr);
    lblVatNumber.setAppMgr(theAppMgr);
    lblDocumentNumber.setAppMgr(theAppMgr);
    txtFiscalNumber.setAppMgr(theAppMgr);
    txtVatNumber.setAppMgr(theAppMgr);
    txtDocumentNumber.setAppMgr(theAppMgr);
  }



  private void jbInit() throws Exception
  {
    pnlSearch = new JPanel();
    pnlGap = new JPanel();
    lblFamilyName = new JCMSLabel();
    lblFirstName = new JCMSLabel();
    txtFamilyName = new JCMSTextField();
    txtFirstName = new JCMSTextField();
    lblAddress = new JCMSLabel();
    txtAddress = new JCMSTextField();
    lblCity = new JCMSLabel();
    txtCity = new JCMSTextField();
    lblState = new JCMSLabel();
    lblZipCode = new JCMSLabel();
    txtState = new JCMSTextField();
    txtZipCode = new JCMSTextField();
    lblCountry = new JCMSLabel();
    lblPhone = new JCMSLabel();
    txtCountry = new JCMSTextField();
    txtPhone = new JCMSTextField();
    lblFiscalNumber = new JCMSLabel();
    lblVatNumber = new JCMSLabel();
    lblDocumentNumber = new JCMSLabel();
    txtFiscalNumber = new JCMSTextField();
    txtVatNumber = new JCMSTextField();
    txtDocumentNumber = new JCMSTextField();
    GridBagLayout gridBagLayout1 = new GridBagLayout();

    pnlSearch.setLayout(gridBagLayout1);
    pnlSearch.setBorder(new TitledBorder(CMSApplet.res.getString("Advanced Search")));
    pnlSearch.setPreferredSize(new Dimension(800, 343));
    pnlGap.setPreferredSize(new Dimension(800,400));

    this.setLayout(new BorderLayout());
    this.add(pnlSearch, BorderLayout.NORTH);
    this.add(pnlGap, BorderLayout.CENTER);
    lblFamilyName.setText(CMSApplet.res.getString("Family Name"));
    lblFirstName.setText(CMSApplet.res.getString("First Name"));
    lblAddress.setText(CMSApplet.res.getString("Address"));
    lblCity.setText(CMSApplet.res.getString("City/Town"));
    lblState.setText(CMSApplet.res.getString("Region/Province/State"));
    lblZipCode.setText(CMSApplet.res.getString("Postal Code"));
    lblCountry.setText(CMSApplet.res.getString("Country"));
    lblPhone.setText(CMSApplet.res.getString("Phone"));
    lblFiscalNumber.setText(CMSApplet.res.getString("Fiscal Number"));
    lblVatNumber.setText(CMSApplet.res.getString("VATNumber"));
    lblDocumentNumber.setText(CMSApplet.res.getString("Document Number"));

    pnlSearch.add(lblFamilyName,      new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 7, 0, 0), 270, 7));
    pnlSearch.add(lblFirstName,     new GridBagConstraints(3, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 25, 0, 15), 142, 7));
    pnlSearch.add(txtFamilyName,              new GridBagConstraints(0, 1, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 7, 0, 0), 80, 4));
    pnlSearch.add(txtFirstName,     new GridBagConstraints(3, 1, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 25, 0, 15), 194, 4));
    pnlSearch.add(lblAddress,  new GridBagConstraints(0, 2, 5, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(35, 7, 0, 0), 299, 7));
    pnlSearch.add(txtAddress,  new GridBagConstraints(0, 3, 5, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 7, 0, 0), 336, 4));
    pnlSearch.add(lblCity,  new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(15, 7, 0, 0), 150, 7));
    pnlSearch.add(txtCity,  new GridBagConstraints(0, 5, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 7, 0, 30), 164, 4));
    pnlSearch.add(lblState,  new GridBagConstraints(2, 4, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(15, 0, 0, 0), 20, 7));
    pnlSearch.add(lblZipCode,  new GridBagConstraints(5, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(15, 19, 0, 15), 40, 7));
    pnlSearch.add(txtState,  new GridBagConstraints(2, 5, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 132, 4));
    pnlSearch.add(txtZipCode,  new GridBagConstraints(5, 5, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 19, 0, 15), 99, 4));
    pnlSearch.add(lblCountry,  new GridBagConstraints(0, 6, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 7, 0, 0), 162, 7));
    pnlSearch.add(lblPhone,  new GridBagConstraints(2, 6, 4, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 0, 15), 233, 7));
    pnlSearch.add(txtCountry,  new GridBagConstraints(0, 7, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 7, 0, 28), 166, 4));
    pnlSearch.add(txtPhone,  new GridBagConstraints(2, 7, 4, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 15), 260, 4));
    pnlSearch.add(lblFiscalNumber,  new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(7, 7, 0, 0), 38, 7));
    pnlSearch.add(lblVatNumber,     new GridBagConstraints(1, 8, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(7, -10, 0, 0), 150, 7));
    pnlSearch.add(lblDocumentNumber,     new GridBagConstraints(5, 8, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(7, 0, 0, 0), 35, 7));
    pnlSearch.add(txtFiscalNumber,    new GridBagConstraints(0, 9, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 7, 9, 35), 140, 4));
    pnlSearch.add(txtVatNumber,            new GridBagConstraints(1, 9, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, -10, 9, 0), 70, 4));
    pnlSearch.add(txtDocumentNumber,         new GridBagConstraints(5, 9, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 9, 5), 131, 4));
  }
}
