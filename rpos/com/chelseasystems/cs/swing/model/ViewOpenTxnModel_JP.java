
/*
 * Description: Table model for viewing open transactions
 * Created By: ken
 * Created Date: 10/01/07
 */

/*
 * History (tab separated):
 * Vers	Date		By			Spec	Comments
 * 1	10/01/07	Ken					Modified view registration table for Japan
 */

package com.chelseasystems.cs.swing.model;

import java.util.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.chelseasystems.cs.pos.*;
import java.text.SimpleDateFormat;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.store.CMSStore;

/**
 */
public class ViewOpenTxnModel_JP extends ViewOpenTxnModel {
  private final int TXN_DATE = 0;
  private final int TXN_ID = 1;
  private final int ASSOCIATE_ID = 2;
  private final int CUSTOMER_ID = 3;
  private final int TOTAL_AMOUNT = 4;
  private final int DEPOSIT_AMOUNT = 5;
  
  private final String COLUMN_NAMES_JP[] = {"Txn Date", "Txn ID", "Assoc."
      , "Customer", "Total Amount", "Deposit Amount"
  };
  private String sNameSeperator;
  /**
   * put your documentation comment here
   */
  public ViewOpenTxnModel_JP() {
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    sNameSeperator = res.getString("NAME_SEPERATOR");
    if(sNameSeperator.equals("NAME_SEPERATOR")) sNameSeperator = ",";

    String sIdentiFiers[];
    int iCtr=0;
        sIdentiFiers = new String[COLUMN_NAMES_JP.length];
      for (iCtr = 0; iCtr < COLUMN_NAMES_JP.length; iCtr++) {
        sIdentiFiers[iCtr] = res.getString(COLUMN_NAMES_JP[iCtr]);
      }
     this.setColumnIdentifiers(sIdentiFiers);
  }

  /**
   * put your documentation comment here
   * @param cmsTransaction
   */
  public void addTransactionHeader(CMSTransactionHeader cmsTransaction) {
    addRow(cmsTransaction);
    if (getTotalRowCount() > getRowsShown()) {
      lastPage();
    }
  }

  /**
   * @param row
   */
  public void deleteLineItem(int row) {
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
  public CMSTransactionHeader getTransactionHeader(int row) {
    if (row < 0)
      return null;
    return ((CMSTransactionHeader)this.getRowInPage(row));
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getColumnCount() {
		  return COLUMN_NAMES_JP.length;
  }

  /**
   * put your documentation comment here
   * @param row
   * @param column
   * @return
   */
  public boolean isCellEditable(int row, int column) {
    return (false);
  }

  /**
   * put your documentation comment here
   * @param row
   * @param column
   * @return
   */
  public Object getValueAt(int row, int column) {
    Vector vTemp = this.getCurrentPage();
    CMSTransactionHeader cmsHeader = (CMSTransactionHeader)vTemp.elementAt(row);
    switch (column) {
      case TXN_DATE:
        return getFormattedDate(cmsHeader.getProcessDate());
      case TXN_ID:
        return cmsHeader.getId();
      case ASSOCIATE_ID:
          return getNameString(cmsHeader.getConsultantLastName(),
                               cmsHeader.getConsultantFirstName());
      case CUSTOMER_ID:
          return getNameString(cmsHeader.getCustomerLastName(),
                               cmsHeader.getCustomerFirstName());
       case TOTAL_AMOUNT:
    	  try {	
    		  return cmsHeader.getRetailAmount().subtract(cmsHeader.getTotalAmountDue()).stringValue();
    	  } catch (Exception e){
    		  e.printStackTrace();
    	  }
      case DEPOSIT_AMOUNT:
    	  return cmsHeader.getTotalAmountDue().stringValue();
      default:
        return (" ");
    }
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
   * @param dtToFormat
   * @return
   */
  private String getFormattedDate(Date dtToFormat) {
    SimpleDateFormat dFormat = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
    String sTmp = "";
    try {
      sTmp = dFormat.format(dtToFormat);
    } catch (Exception e) {}
    return sTmp;
  }

  /**
   * Return LastName + FirstName
   * @param sLastName String
   * @param sFirstName String
   * @return String
   */
  private String getNameString(String sLastName, String sFirstName) {
    String sName = "";
    if (sLastName != null && sLastName.indexOf("null") == -1)
      sName = sLastName;

    if (sFirstName != null && sFirstName.indexOf("null") == -1) {
      if (sName.length() > 0) sName += sNameSeperator;
      sName += sFirstName;
    }
    return sName;
  }
}

