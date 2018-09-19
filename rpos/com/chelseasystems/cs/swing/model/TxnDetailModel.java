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
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.util.Version;


/**
 */
public class TxnDetailModel extends ScrollableTableModel {
  public static final int ITEM_CODE = 0;
  public static final int ITEM_DESCRIPTION = 1;
  public static final int ASSOCIATE = 2;
  public static final int QUANITITY = 3;
  public static final int UNIT_PRICE = 4;
  public static final int DISC_MKDN = 5;
  public static final int AMT_PAID = 6;

  /**
   */
  public TxnDetailModel() {
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    //added by shushma for promotion
    if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
    this.setColumnIdentifiers(new String[] {res.getString("Item Code")
        , res.getString("Item Description"), res.getString("Assoc."), res.getString("Quantity")
        , res.getString("Unit Price"), res.getString("Disc/Mkdn/Promo Code"), res.getString("Amount Paid")
    });
    }
    else
    {
    	 this.setColumnIdentifiers(new String[] {res.getString("Item Code")
    		        , res.getString("Item Description"), res.getString("Assoc."), res.getString("Quantity")
    		        , res.getString("Unit Price"), res.getString("Disc/Mkdn"), res.getString("Amount Paid")
    		    });
    }
  }

  /**
   * @return
   */
  public int getColumnCount() {
    return (7);
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
    Object container = vRows.elementAt(row);
    if (container instanceof DetailContainer) {
      String[] array = ((DetailContainer)container).data;
      //  CMSApplet.theAppMgr.showErrorDlg(new Integer(array.length).toString());
      return (array[column]);
    } else {
      if (column == 0)
        return (String)container;
      else
        return (null);
    }
  }

  /**
   * @param container
   */
  public void addContainer(DetailContainer container) {
    addRow(container);
  }

  /**
   * @param row
   * @return
   */
  public boolean isSale(int row) {
    DetailContainer container = (DetailContainer)getRowInPage(row);
    return (container.type.equals("SALE"));
  }

  /**
   * @param row
   * @return
   */
  public boolean isReturn(int row) {
    DetailContainer container = (DetailContainer)getRowInPage(row);
    return (container.type.equals("VSAL"));
  }

  /**
   * @param row
   * @return
   */
  public boolean isPayment(int row) {
    DetailContainer container = (DetailContainer)getRowInPage(row);
    return (container.type.equals("PAYMENT"));
  }

  /**
   * @param row
   * @return
   */
  public boolean isLayaway(int row) {
    DetailContainer container = (DetailContainer)getRowInPage(row);
    return (container.type.equals("LAY"));
  }

  /**
   * @param tblList
   */
  public void setColumnWidth(JTable tblList) {
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    setRowsShown(tblList.getHeight() / tblList.getRowHeight());
    TableColumn ItemCol = tblList.getColumnModel().getColumn(0);
    ItemCol.setPreferredWidth((int)(120 * r));
    TableColumn AssocCol = tblList.getColumnModel().getColumn(2);
    AssocCol.setPreferredWidth((int)(165 * r));
    TableColumn QtyCol = tblList.getColumnModel().getColumn(3);
    QtyCol.setPreferredWidth((int)(40 * r));
    TableColumn UnitPriceCol = tblList.getColumnModel().getColumn(4);
    UnitPriceCol.setPreferredWidth((int)(95 * r));
    TableColumn MarkdownCol = tblList.getColumnModel().getColumn(5);
    if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
    //added by shushma for promotion
    MarkdownCol.setPreferredWidth((int)(200 * r));
    }
    else{
    	  MarkdownCol.setPreferredWidth((int)(120 * r));
    }
    TableColumn AmtDueCol = tblList.getColumnModel().getColumn(6);
    AmtDueCol.setPreferredWidth((int)(100 * r));
    TableColumn DescriptionCol = tblList.getColumnModel().getColumn(1);
    DescriptionCol.setPreferredWidth(tblList.getWidth()
        - (ItemCol.getPreferredWidth() + QtyCol.getPreferredWidth() + AssocCol.getPreferredWidth()
        + UnitPriceCol.getPreferredWidth() + AmtDueCol.getPreferredWidth()
        + MarkdownCol.getPreferredWidth()));
  }
}

