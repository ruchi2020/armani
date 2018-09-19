/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.model;

import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cs.address.Address;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.address.AddressMgr;
import com.chelseasystems.cs.util.HTMLColumnHeaderUtil;


/**
 * <p>Title: CustomerAddressModel.java </p>
 *
 * <p>Description: Model for CustomerAddress </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company:SkillNet Inc. </p>
 *
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 03-14-2005 | Manpreet  | N/A       | POS_104665_FS_CustomerManagement_Rev2              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 09-06-2005 | Manpreet  | N/A       | Decoding AddressType and Country                   |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class CustomerAddressModel extends ScrollableTableModel {
  private String COLUMN_NAMES[] = {"Type", "Cntry", "Address Line 1", "Address Line 2"
      , "City & State/Province/ \nPrefecture", "Postal\nCode"
  };
  public static final int TYPE = 0;
  public static final int COUNTRY = 1;
  public static final int ADDRESS1 = 2;
  public static final int ADDRESS2 = 3;
  public static final int CITY_STATE = 4;
  public static final int POSTAL_CODE = 5;
  private AddressMgr addressMgr;

  /**
   * put your documentation comment here
   */
  public CustomerAddressModel() {
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    String sIdentiFiers[] = new String[COLUMN_NAMES.length];
    HTMLColumnHeaderUtil htmlUtil = new HTMLColumnHeaderUtil(CMSApplet.theAppMgr.getTheme().
        getTextFieldFont());
    for (int iCtr = 0; iCtr < COLUMN_NAMES.length; iCtr++) {
      sIdentiFiers[iCtr] = htmlUtil.getHTMLHeaderFor(res.getString(COLUMN_NAMES[iCtr]));
    }
    this.setColumnIdentifiers(sIdentiFiers);
    addressMgr = new AddressMgr();
  }

  /**
   * put your documentation comment here
   * @param address
   */
  public void addAddress(Address address) {
    if (address == null)
      return;
    addRow(address);
    this.fireTableDataChanged();
  }

  /**
   * put your documentation comment here
   * @param row
   */
  public void deleteAddress(int row) {
    if (row < 0)
      return;
    removeRowInPage(row);
    this.fireTableRowsDeleted(row, row);
  }

  /**
   * put your documentation comment here
   * @param row
   * @return
   */
  public Address getAddressAt(int row) {
    if (row < 0)
      return null;
    return (Address)this.getRowInPage(row);
  }

  /**
   * put your documentation comment here
   * @param iRow
   * @param address
   */
  public void setAddressAt(int iRow, Address address) {
    if (iRow < 0)
      return;
    //  getAddressAt(iRow);
    this.fireTableDataChanged();
    //vRows.set(iRow, address);
    //    deleteAddress(iRow);
    //    vRows.add(iRow, address);
    //    this.fireTableRowsInserted(iRow, iRow);
  }

  /**
   * put your documentation comment here
   * @param row
   * @param column
   * @return
   */
  public Object getValueAt(int row, int column) {
    Address address = (Address)this.getCurrentPage().elementAt(row);
    switch (column) {
      case TYPE:
        return decodeAddressType(address.getAddressType());
      case COUNTRY:
        return decodeCountry(address.getCountry());
      case ADDRESS1:
        return address.getAddressLine1();
      case ADDRESS2:
        return address.getAddressLine2();
      case CITY_STATE:
        if (address.getCity() == null)
          return address.getState();
        return address.getCity() + ", " + address.getState();
      case POSTAL_CODE:
        return address.getZipCode();
    }
    return null;
  }

  /**
   * put your documentation comment here
   * @param RowsShown
   */
  public void setRowsShown(int RowsShown) {
    super.setRowsShown(RowsShown);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getColumnCount() {
    return COLUMN_NAMES.length;
  }

  /**
   * put your documentation comment here
   * @param sCountry
   * @return
   */
  private String decodeCountry(String sCountry) {
    int iIndex = addressMgr.getCountryKeys().indexOf(sCountry);
    if (iIndex != -1 && addressMgr.getCountryLabels().size() > iIndex) {
      sCountry = (String)addressMgr.getCountryLabels().elementAt(iIndex);
    }
    return sCountry;
  }

  /**
   * put your documentation comment here
   * @param sAddressType
   * @return
   */
  private String decodeAddressType(String sAddressType) {
    int iIndex = addressMgr.getAddressTypeKeys().indexOf(sAddressType);
    if (iIndex != -1 && addressMgr.getAddressTypes().size() > iIndex) {
      sAddressType = (String)addressMgr.getAddressTypes().elementAt(iIndex);
    }
    return sAddressType;
  }
}

