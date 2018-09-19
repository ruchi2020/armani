/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 05-10-2005 | Anand     | N/A       |2. Modifications as per specifications for    |
 |      |            |           |           |   Txn History                                |
 -------------------------------------------------------------------------------------------|
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.model;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.POSLineItemDetail;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import java.util.*;


/**
 */
public class TxnLineItemDetailModel extends ScrollableTableModel {
  public static final int ITEM_CODE = 0;
  public static final int DISC_MKDN = 1;
  public static final int TAX_AMT = 2;
  public static final int AMT_PAID = 3;
  public static final int REASON = 4;
  public static final int CONSULTANT = 5;
  public static final int ALTERATION_ID = 6;
  public static final int FILTER_ID = 7;
  public static final int PROMISE_DATE = 8;
  public static final String[] COLUMN_NAMES = new String[] {"Item Code", "Disc/Mkdn", "Tax Amt"
      , "Amt Paid", "Reason", "Assoc.", "Alteration ID", "Fitter ID", "Promise Dt"
  };
  java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
  private String[] NEW_COLUMN_NAMES = COLUMN_NAMES;

  /**
   */
  public TxnLineItemDetailModel() {
    this.setColumnIdentifiers(COLUMN_NAMES);
  }

  /**
   * @return
   */
  public int getColumnCount() {
    return NEW_COLUMN_NAMES.length;
  }

  /**
   * put your documentation comment here
   * @param iCtr
   * @return
   */
  public String getColumnName(int iCtr) {
    return res.getString(NEW_COLUMN_NAMES[iCtr]);
  }

  /**
   * @param row
   * @param column
   * @return
   */
  public boolean isCellEditable(int row, int column) {
    return (false);
  }

  /**
   * @param row
   * @param column
   * @return
   */
  public Object getValueAt(int row, int column) {
    java.util.Vector vRows = this.getCurrentPage();
    String[] rowData = (String[])vRows.elementAt(row);
    return (rowData[column] == null ? "" : rowData[column]);
  }

  /**
   * put your documentation comment here
   * @param row
   * @return
   */
  public POSLineItem getLineItem(int row) {
    // this
    String strSeqNum = (String)getValueAt(row, 9);
    CMSCompositePOSTransaction theTxn = null;
    if (CMSApplet.theAppMgr.getStateObject("THE_TXN") != null) {
      theTxn = (CMSCompositePOSTransaction)CMSApplet.theAppMgr.getStateObject("THE_TXN");
    } else if (CMSApplet.theAppMgr.getStateObject("TXN_POS") != null) {
      theTxn = (CMSCompositePOSTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS");
    }
    if (theTxn != null) {
      POSLineItem[] lineItems = theTxn.getLineItemsArray();
      for (int iCtr = 0; iCtr < lineItems.length; iCtr++) {
        if (new Integer(lineItems[iCtr].getSequenceNumber()).toString().equals(strSeqNum)) {
          return lineItems[iCtr];
        }
      }
    }
    //POSLineItem lineItem = (POSLineItem)((POSLineItemDetail)this.getRowInPage(row)).getLineItem();
    return null;
  }

  /**
   * @param tblList
   */
  public void setColumnWidth(JTable tblList) {
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    setRowsShown(tblList.getHeight() / tblList.getRowHeight());
    TableColumn col0 = tblList.getColumnModel().getColumn(ITEM_CODE);
    col0.setPreferredWidth((int)(88 * r));
    TableColumn col1 = tblList.getColumnModel().getColumn(DISC_MKDN);
    col1.setPreferredWidth((int)(67 * r));
    TableColumn col2 = tblList.getColumnModel().getColumn(TAX_AMT);
    col2.setPreferredWidth((int)(80 * r));
    TableColumn col3 = tblList.getColumnModel().getColumn(AMT_PAID);
    col3.setPreferredWidth((int)(87 * r));
    TableColumn col5 = tblList.getColumnModel().getColumn(CONSULTANT);
    col5.setPreferredWidth((int)(115 * r));
    TableColumn col6 = tblList.getColumnModel().getColumn(ALTERATION_ID);
    col5.setPreferredWidth((int)(88 * r));
    TableColumn col7 = tblList.getColumnModel().getColumn(FILTER_ID);
    col5.setPreferredWidth((int)(87 * r));
    TableColumn col8 = tblList.getColumnModel().getColumn(PROMISE_DATE);
    col5.setPreferredWidth((int)(88 * r));
    tblList.getColumnModel().getColumn(REASON).setPreferredWidth(tblList.getWidth()
        - (col0.getPreferredWidth() + col1.getPreferredWidth() + col2.getPreferredWidth()
        + col3.getPreferredWidth() + col5.getPreferredWidth() + col6.getPreferredWidth()
        + col7.getPreferredWidth() + col8.getPreferredWidth()));
  }

  public void setVatEnabled(boolean vatEnabled)
  {
    NEW_COLUMN_NAMES = new String[COLUMN_NAMES.length];
    for(int i=0; i<COLUMN_NAMES.length; i++) {
      if(i==2 && vatEnabled)
        NEW_COLUMN_NAMES[i] = "Vat";
      else
        NEW_COLUMN_NAMES[i] = COLUMN_NAMES[i];
    }
    setColumnIdentifiers(NEW_COLUMN_NAMES);
    fireTableDataChanged();
  }
}

