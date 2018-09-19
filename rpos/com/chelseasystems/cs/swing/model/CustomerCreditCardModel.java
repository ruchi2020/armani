/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.model;

import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cs.util.HTMLColumnHeaderUtil;
import com.chelseasystems.cs.customer.CustomerCreditCard;
import java.util.Vector;
import com.chelseasystems.cs.util.CreditAuthUtil;
import java.text.SimpleDateFormat;


/**
 * <p>Title: CustomerCreditCardModel.java </p>
 *
 * <p>Description: Model for CustomerAddress </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company:SkillNet Inc. </p>
 *
 * @author Sumit Krishnan
 * @version 1.0
 */
public class CustomerCreditCardModel extends ScrollableTableModel {
  private String COLUMN_NAMES[] = {"Store ID", "Card Type", "Card Number", "Expiration Date"
      , "Billing Zipcode"
  };
  public static final int STORE_ID = 0;
  public static final int CARD_TYPE = 1;
  public static final int CARD_NUMBER = 2;
  public static final int EXPIRATION_DATE = 3;
  public static final int BILLING_ZIPCODE = 4;
  private Vector vRemoveCreditCard = new Vector();

  /**
   * put your documentation comment here
   */
  public CustomerCreditCardModel() {
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    String sIdentiFiers[] = new String[COLUMN_NAMES.length];
    HTMLColumnHeaderUtil htmlUtil = new HTMLColumnHeaderUtil(CMSApplet.theAppMgr.getTheme().
        getTextFieldFont());
    for (int iCtr = 0; iCtr < COLUMN_NAMES.length; iCtr++) {
      sIdentiFiers[iCtr] = htmlUtil.getHTMLHeaderFor(res.getString(COLUMN_NAMES[iCtr]));
    }
    this.setColumnIdentifiers(sIdentiFiers);
  }

  /**
   * put your documentation comment here
   * @param creditCard
   */
  public void addCreditCard(CustomerCreditCard creditCard) {
    if (creditCard == null)
      return;
    addRow(creditCard);
    this.fireTableDataChanged();
  }

  /**
   * put your documentation comment here
   * @param row
   */
  public void deleteCreditCard(int row) {
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
  public CustomerCreditCard getCreditCardAt(int row) {
    if (row < 0)
      return null;
    return (CustomerCreditCard)this.getRowInPage(row);
  }

  /**
   * put your documentation comment here
   * @param iRow
   * @param creditCard
   */
  public void setCreditCardAt(int iRow, CustomerCreditCard creditCard) {
    if (iRow < 0)
      return;
    this.fireTableDataChanged();
  }

  /**
   * put your documentation comment here
   * @param row
   * @param column
   * @return
   */
  public Object getValueAt(int row, int column) {
    CustomerCreditCard creditCard = (CustomerCreditCard)this.getCurrentPage().elementAt(row);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMyy");
    switch (column) {
      case STORE_ID:
        return creditCard.getStoreId();
      case CARD_TYPE:
        return creditCard.getCreditCardType();
      case CARD_NUMBER:
    	  //sonali:AJB added to directly fetch masked credit card number
       // return CreditAuthUtil.maskCreditCardNo(creditCard.getCreditCardNumber());
        return creditCard.getMaskedCreditCardNum();
      case EXPIRATION_DATE:
        return dateFormat.format(creditCard.getExpDate());
      case BILLING_ZIPCODE:
        return creditCard.getBillingZipCode();
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
   * @param creditCard
   */
  public void addVRemoveCC(CustomerCreditCard creditCard) {
    this.vRemoveCreditCard.add(creditCard);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Vector getVRemoveCC() {
    return this.vRemoveCreditCard;
  }
}

