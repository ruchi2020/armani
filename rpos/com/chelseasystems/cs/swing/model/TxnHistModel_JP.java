/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.swing.model;

import java.util.*;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableCellRenderer;
import java.text.SimpleDateFormat;
import com.chelseasystems.cr.pos.TransactionHeader;
import com.chelseasystems.cs.pos.CMSTransactionHeader;
import java.util.ResourceBundle;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.util.HTMLColumnHeaderUtil;
import java.util.Comparator;

/**
 * <p>Title:TxnHistModel_JP.java </p>
 *
 * <p>Description: Model displays transaction history </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: SkillNet Inc. </p>
 *
 * @author Sandhya Ajit
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 03-06-2006 | Sandhya   | PCR1326	 | Model displays transaction history                 |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */

public class TxnHistModel_JP extends TxnHistModel {
  private SimpleDateFormat format;
  private INIFile file;
  private HashMap tranCode;
  private final String COLUMN_NAMES[] = new String[] {("Date"), ("Store Name"), ("Txn ID")
	      , ("Txn Type"), ("Assoc."), ("Txn Total")
  }; 
  public static final int COL_DATE = 0;
  public static final int COL_STORE_ID = 1;
  public static final int COL_TXN_ID = 2;
  public static final int COL_TXN_TYPE = 3;
  public static final int COL_ASSOCIATE = 4;
  public static final int COL_TXN_TOTAL = 5;
  private String sHotKeys[];
  private int iCurrentSortColIdx = Integer.MAX_VALUE;
  boolean bReverseSort = false;
  ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
  private int lastSelectedTxnRow = -1;  
  private static final String CONFIGURATION_FILE = "pos.cfg";
  private static final ConfigMgr configMgr = new ConfigMgr(CONFIGURATION_FILE);

  /**
   */
  public TxnHistModel_JP() {
    format = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateTimeFormat();
    makeColumnHeaders();
    /// MSB - 05/05/05
    // Introduced method to make column headers
    // with hotkeys
    //        this.setColumnIdentifiers(new String[] {
    //                                   res.getString("<u>D</u>ate"),
    //                                  res.getString("<u>S</u>tore ID"),
    //                                  res.getString("T<u>x</u>n ID"),
    //                                  res.getString("Txn T<u>y</u>pe"),
    //                                  res.getString("<u>A</u>ssoc."),
    //                                  res.getString("Txn <u>T</u>otal")
    //        });
    try {
      file = new INIFile(FileMgr.getLocalFile("config", "pos.cfg"), true);
      tranCode = new HashMap();
      String transTypes = file.getValue("TRANS_TYPES");
      StringTokenizer st = new StringTokenizer(transTypes, ",");
      while (st.hasMoreElements()) {
        String key = (String)st.nextElement();
        String transTypeCode = file.getValue(key, "CODE", "");
        String transTypeShortDesc = file.getValue(key, "SHORT_DESC", "");
        tranCode.put(transTypeCode, transTypeShortDesc);
      }
    } catch (Exception e) {
      CMSApplet.theAppMgr.showExceptionDlg(e);
    }
  }

  /**
   * Make column headers
   * Picks up column names and fetches their hot key from
   * message bundle.
   */
  private void makeColumnHeaders() {
    HTMLColumnHeaderUtil util = new HTMLColumnHeaderUtil(CMSApplet.theAppMgr.getTheme().
        getTextFieldFont());
    sHotKeys = new String[COLUMN_NAMES.length];
    String sTmp = "";
    StringBuffer columnTag = new StringBuffer();
    String col_identifiers[] = new String[COLUMN_NAMES.length];
    for (int iCtr = 0; iCtr < COLUMN_NAMES.length; iCtr++) {
      sTmp = res.getString(COLUMN_NAMES[iCtr]);
      columnTag = new StringBuffer(res.getString(COLUMN_NAMES[iCtr]));
      sHotKeys[iCtr] = res.getString(COLUMN_NAMES[iCtr] + "_HOT_KEY").toUpperCase();
      try {
        columnTag.insert(sTmp.toUpperCase().indexOf(sHotKeys[iCtr]), "<U>");
        columnTag.insert(sTmp.toUpperCase().indexOf(sHotKeys[iCtr]) + 4, "</U>");
      } catch (Exception e) {
        //e.printStackTrace();
        System.out.println("!!!----------  ERROR: HotKey character '" + sHotKeys[iCtr]
            + "' missing in column name '" + sTmp + "'");
      }
      col_identifiers[iCtr] = util.getHTMLHeaderFor(columnTag.toString());
    }
    this.setColumnIdentifiers(col_identifiers);
  }

  /**
   * @param txn
   */
  public void addTxnHeader(TransactionHeader txn) {
    addRow(txn);
  }

  /**
   * @param row
   * @return
   */
  public TransactionHeader getTransactionHeader(int row) {
    return (TransactionHeader)getRowInPage(row);
  }

  /**
   * @return
   */
  public int getColumnCount() {
    return COLUMN_NAMES.length;
  }

  /**
   * Get HotKey Character for Column
   * @param iIndex ColumnNumber
   * @return char
   */
  public String getColumnHotKey(int iIndex) {
    if (iIndex < 0 || iIndex >= COLUMN_NAMES.length)
      return null;
    return sHotKeys[iIndex];
  }

  /**
   * @param row
   * @param column
   * @return
   */
  public boolean isCellEditable(int row, int column) {
    return false;
  }

  /**
   * @param row
   * @param column
   * @return
   */
  public Object getValueAt(int row, int column) {
    Vector vTemp = getCurrentPage();
    TransactionHeader txnHeader = (TransactionHeader)vTemp.elementAt(row);
    CMSTransactionHeader cmsTxnHeader = (CMSTransactionHeader)txnHeader;
    switch (column) {
      case COL_DATE:
        if (cmsTxnHeader.getSubmitDate() != null)
          return format.format(txnHeader.getSubmitDate());
      case COL_STORE_ID:
    	//PCR1326 TransactionHistory fix for Armani Japan    	 
        return cmsTxnHeader.getStoreName();        
      case COL_TXN_ID:
        return cmsTxnHeader.getId();
      case COL_TXN_TYPE:
        return tranCode.get(cmsTxnHeader.getTransactionType());
      case COL_ASSOCIATE:
    	//PCR1326 TransactionHistory fix for Armani Japan
  	    return cmsTxnHeader.getConsultantLastName() + " " + cmsTxnHeader.getConsultantFirstName();
        //return txnHeader.getConsultantId();
        //                if (!txnHeader.getConsultantId().equals(""))
        //                    return  txnHeader.getConsultantFirstName() + " " + txnHeader.getConsultantLastName();
        //                else
        //                    return  "";
      case COL_TXN_TOTAL:
        return cmsTxnHeader.getTotalAmountDue().formattedStringValue();
      default:
        return " ";
    }
  }

  /**
   * @param tblList
   */
  public void setColumnWidth(JTable tblList) {
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    setRowsShown(tblList.getHeight() / tblList.getRowHeight());
    TableColumn datecol = tblList.getColumnModel().getColumn(COL_DATE);
    datecol.setPreferredWidth((int)(150 * r));
    TableColumn storeCol = tblList.getColumnModel().getColumn(COL_STORE_ID);
    storeCol.setPreferredWidth((int)(125 * r));
    TableColumn transNocol = tblList.getColumnModel().getColumn(COL_TXN_ID);
    transNocol.setPreferredWidth((int)(135 * r));
    TableColumn transTypecol = tblList.getColumnModel().getColumn(COL_TXN_TYPE);
    transTypecol.setPreferredWidth((int)(150 * r));
    TableColumn transTotalcol = tblList.getColumnModel().getColumn(COL_TXN_TOTAL);
    transTotalcol.setPreferredWidth((int)(110 * r));
    TableColumn consCol = tblList.getColumnModel().getColumn(COL_ASSOCIATE);
    consCol.setPreferredWidth(140);
    DefaultTableCellRenderer ctrRenderer = new DefaultTableCellRenderer();
    ctrRenderer.setHorizontalAlignment(0);
    tblList.getColumnModel().getColumn(2).setCellRenderer(ctrRenderer);
    DefaultTableCellRenderer HorAlignRenderer = new DefaultTableCellRenderer();
    HorAlignRenderer.setHorizontalAlignment(4);
    transTotalcol.setCellRenderer(HorAlignRenderer);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getCurrentSortColumnAndType() {
    if (iCurrentSortColIdx == 0 && bReverseSort)
      return Integer.MIN_VALUE;
    return (bReverseSort) ? -iCurrentSortColIdx : iCurrentSortColIdx;
  }

  /**
   * Sorts on following basis:
   * 1). Most current Transaction to oldest for Current Store.
   * 2). Most current Transaction to oldest Store independent.
   * @param selectedTranHeaderID String
   * @return int
   */
  public int doInitialSort(String selectedTranHeaderID)
  {
    if (getAllRows().size() < 1)
      return -1;
    int iSelectedRow = -1;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssS");
    Vector vecSortedHeaders;
    TreeMap currentStoreMap = new TreeMap();
    TreeMap otherStoresMap = new TreeMap();
    Vector vecRows = this.getAllRows();
    int iCtr = 0;
    int iTranCtr=0;
    TransactionHeader tHeader;
    String sCurrentStoreId = CMSApplet.theStore.getId();
    for (iCtr = 0; iCtr < vecRows.size(); iCtr++) {
      tHeader = (TransactionHeader) vecRows.elementAt(iCtr);

      if (tHeader.getStoreId().equals(sCurrentStoreId)) {
        currentStoreMap.put(dateFormat.format(tHeader.getSubmitDate()) + "," +
                            tHeader.getId()
                            , tHeader);
      }
      else {
        otherStoresMap.put(dateFormat.format(tHeader.getSubmitDate()) + "," +
                           tHeader.getId()
                           , tHeader);
      }
    }

    if(currentStoreMap.size()>0 || otherStoresMap.size()>0)
      super.clear();

    if (currentStoreMap.size() > 0) {
      vecSortedHeaders = new Vector(currentStoreMap.values());
      for (int i = 0; i < vecSortedHeaders.size(); i++) {
        removeRowInModel(vecSortedHeaders.elementAt(i));
      }
      for (iCtr = 0; iCtr < vecSortedHeaders.size(); iCtr++,iTranCtr++) {
        tHeader = (TransactionHeader)vecSortedHeaders.elementAt(vecSortedHeaders.size() - iCtr
            - 1);
        addTxnHeader(tHeader);
        if (selectedTranHeaderID != null && tHeader.getId().equals(selectedTranHeaderID))
          iSelectedRow = iTranCtr;
      }

      fireTableDataChanged();
    }

    if (otherStoresMap.size() > 0) {
      vecSortedHeaders = new Vector(otherStoresMap.values());
      for (int i = 0; i < vecSortedHeaders.size(); i++) {
        removeRowInModel(vecSortedHeaders.elementAt(i));
      }
      for (iCtr = 0; iCtr < vecSortedHeaders.size(); iCtr++,iTranCtr++) {
        tHeader = (TransactionHeader)vecSortedHeaders.elementAt(vecSortedHeaders.size() - iCtr
            - 1);
        addTxnHeader(tHeader);
        if (selectedTranHeaderID != null && tHeader.getId().equals(selectedTranHeaderID))
          iSelectedRow = iTranCtr;
      }
      fireTableDataChanged();
    }
    iCurrentSortColIdx = Integer.MAX_VALUE;
    bReverseSort = false;
    return iSelectedRow;
  }

  /**
   * Sorts table by column index
   * @param sortColumn ColumnIndex
   * @param selectedTranHeader TransactionHeader
   * @return int
   */
  public int sortByColumnType(int sortColumn, String selectedTranHeaderID) {
    if (getAllRows().size() < 1)
      return -1;

    if(sortColumn == Integer.MAX_VALUE)
      return doInitialSort(selectedTranHeaderID);
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssS");
    TreeMap sortColumnMap = new TreeMap();
    int selectedRow = -1;
    if (sortColumn < 0) {
      bReverseSort = !bReverseSort;
      if (sortColumn == Integer.MIN_VALUE)
        sortColumn = 0;
      else
        sortColumn *= -1;
    } else {
      if (iCurrentSortColIdx == sortColumn && iCurrentSortColIdx != Integer.MAX_VALUE)
        bReverseSort = !bReverseSort;
      else {
        bReverseSort = false;
        iCurrentSortColIdx = sortColumn;
      }
    }
    //        if(this.iCurrentSortColIdx==sortColumn && !bReverseSort)
    //            bReverseSort = true;
    //        else
    //            bReverseSort = false;
    //        this.iCurrentSortColIdx = sortColumn;
    Vector vecRows = this.getAllRows();
    int iCtr = 0;
    TransactionHeader tHeader;
    if (sortColumn == COL_TXN_TOTAL) {
      super.clear();
      Collections.sort(vecRows, new TotalAmountDueComparator());
      for (int i = 0; i < vecRows.size(); i++) {
        removeRowInModel(vecRows.elementAt(i));
      }
      for (iCtr = 0; iCtr < vecRows.size(); iCtr++) {
        if (bReverseSort) {
          tHeader = (TransactionHeader)vecRows.elementAt(vecRows.size() - iCtr - 1);
        } else {
          tHeader = (TransactionHeader)vecRows.elementAt(iCtr);
        }
        addTxnHeader(tHeader);
        if (selectedTranHeaderID != null && tHeader.getId().equals(selectedTranHeaderID))
          selectedRow = iCtr;
      }
      fireTableDataChanged();
      //return selectedRow;
    } else {
      for (iCtr = 0; iCtr < vecRows.size(); iCtr++) {
        tHeader = (TransactionHeader)vecRows.elementAt(iCtr);
        switch (sortColumn) {
          case COL_STORE_ID:
            sortColumnMap.put(tHeader.getStoreId() + "," + tHeader.getId(), tHeader);
            break;
            //                case COL_TXN_ID:
            //                    sortColumnMap.put(tHeader.getId(), tHeader);
            //                    break;
          case COL_TXN_TYPE:
            sortColumnMap.put(tHeader.getTransactionType() + "," + tHeader.getId(), tHeader);
            break;
          case COL_ASSOCIATE:
            sortColumnMap.put(tHeader.getConsultantId() + "," + tHeader.getId()
                + dateFormat.format(tHeader.getSubmitDate()), tHeader);
            break;
          case COL_TXN_ID:
            sortColumnMap.put(tHeader.getId(), tHeader);
            break;
            //case COL_DATE:
          default:
            sortColumnMap.put(dateFormat.format(tHeader.getSubmitDate()) + "," + tHeader.getId()
                , tHeader);
        }
      }
      if (sortColumnMap.size() > 0) {
        super.clear();
        Vector vecSortedHeaders = new Vector(sortColumnMap.values());
        for (int i = 0; i < vecSortedHeaders.size(); i++) {
          removeRowInModel(vecSortedHeaders.elementAt(i));
        }
        for (iCtr = 0; iCtr < vecSortedHeaders.size(); iCtr++) {
          if (bReverseSort) {
            tHeader = (TransactionHeader)vecSortedHeaders.elementAt(vecSortedHeaders.size() - iCtr
                - 1);
          } else {
            tHeader = (TransactionHeader)vecSortedHeaders.elementAt(iCtr);
          }
          addTxnHeader(tHeader);
          if (selectedTranHeaderID != null && tHeader.getId().equals(selectedTranHeaderID))
            selectedRow = iCtr;
        }
        fireTableDataChanged();
      }
    }
    lastSelectedTxnRow = selectedRow;
    return selectedRow;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getLastSelectedTxnRow() {
    return lastSelectedTxnRow;
  }

  /**
   * put your documentation comment here
   * @param lastSelectedTxnRow
   */
  public void setLastSelectedTxnRow(int lastSelectedTxnRow) {
    this.lastSelectedTxnRow = lastSelectedTxnRow;
  }

  private class TotalAmountDueComparator implements Comparator {

    /**
     * put your documentation comment here
     * @param obj1
     * @param obj2
     * @return
     */
    public int compare(Object obj1, Object obj2) {
      TransactionHeader header1 = (TransactionHeader)obj1;
      TransactionHeader header2 = (TransactionHeader)obj2;
      if (header1.getTotalAmountDue().doubleValue() < header2.getTotalAmountDue().doubleValue())
        return -1;
      if (header1.getTotalAmountDue().doubleValue() > header2.getTotalAmountDue().doubleValue())
        return 1;
      //if(header1.getTotalAmountDue().doubleValue() == header2.getTotalAmountDue().doubleValue())
      return header1.getId().compareTo(header2.getId());
      //return 0;
    }
  }


  /**
   * put your documentation comment here
   */
  public void clear() {
    //tranCode = new HashMap();
    bReverseSort = false;
    iCurrentSortColIdx = Integer.MAX_VALUE;
    super.clear();
  }
}

