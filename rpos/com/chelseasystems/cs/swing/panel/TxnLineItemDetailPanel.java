/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 3    | 07-08-2005 | Vikram    | 406       | Display Barcode instead of SKU               |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 05-10-2005 | Anand     | N/A       |2. Modifications as per specifications for    |
 |      |            |           |           |   Txn History                                |
 -------------------------------------------------------------------------------------------|
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import java.awt.*;
import java.awt.event.ComponentEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.*;
import com.chelseasystems.cs.swing.model.TxnLineItemDetailModel;
import com.chelseasystems.cs.swing.transaction.TxnDetailApplet;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.swing.ScrollProcessor;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.swing.*;
import javax.swing.table.*;
import java.text.SimpleDateFormat;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.swing.MultiLineCellRenderer;
import javax.swing.table.TableCellRenderer;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.store.CMSStore;


/**
 */
public class TxnLineItemDetailPanel extends JPanel implements ScrollProcessor
    , PositionableDisplayer {
  ResourceBundle res;
  TxnLineItemDetailModel model = new TxnLineItemDetailModel();
  JCMSTable tblTxn;
  IApplicationManager theAppMgr;
  VariableRenderer taRenderer;
  //JCMSTable tblList = new JCMSTable(model, JCMSTable.SELECT_ROW);
  //TextRenderer renderer = new TextRenderer();
  Vector vecSeqNum = new Vector();
  
  private static final String CONFIGURATION_FILE = "pos.cfg";
  private static ConfigMgr configMgr = new ConfigMgr(CONFIGURATION_FILE);
  private static String txnLineItemDetailPanelString = 
	  configMgr.getString("TRANSACTION.TXN_LINE_ITEM_DETAIL_PANEL");

  /**
   */
  public TxnLineItemDetailPanel() {
    try {
      res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
      model = new TxnLineItemDetailModel();
      tblTxn = new JCMSTable(model, JCMSTable.SELECT_ROW);
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * @exception Exception
   */
  private void jbInit()
      throws Exception {
    this.setLayout(new BorderLayout());
    this.add(tblTxn, BorderLayout.CENTER);
    this.add(tblTxn.getTableHeader(), BorderLayout.NORTH);
    this.setPreferredSize(new Dimension(844, 405));
    DefaultTableCellRenderer rndrMiddle = new DefaultTableCellRenderer();
    rndrMiddle.setHorizontalAlignment(SwingConstants.CENTER);
    //
    taRenderer = new VariableRenderer();
    tblTxn.getColumnModel().getColumn(4).setCellRenderer(taRenderer);
    DefaultTableCellRenderer rndrRight = new DefaultTableCellRenderer();
    rndrRight.setHorizontalAlignment(SwingConstants.RIGHT);
    tblTxn.getColumnModel().getColumn(1).setCellRenderer(rndrRight);
    tblTxn.getColumnModel().getColumn(2).setCellRenderer(rndrRight);
    tblTxn.getColumnModel().getColumn(3).setCellRenderer(rndrRight);
    tblTxn.getColumnModel().getColumn(4).setCellRenderer(rndrRight);
    tblTxn.getColumnModel().getColumn(5).setCellRenderer(rndrRight);
    tblTxn.getColumnModel().getColumn(6).setCellRenderer(new MultiLineRenderer());
    //tblTxn.getColumnModel().getColumn(6).setCellRenderer(rndrRight);
    tblTxn.getColumnModel().getColumn(7).setCellRenderer(rndrRight);
    tblTxn.getColumnModel().getColumn(8).setCellRenderer(rndrRight);
    tblTxn.addComponentListener(new java.awt.event.ComponentAdapter() {

      /**
       * @param e
       */
      public void componentResized(ComponentEvent e) {
        resetColumnWidths();
      }
    });
  }

  /**
   * @param theAppMgr
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    tblTxn.setAppMgr(theAppMgr);
    tblTxn.getCMSTableHeader().setBackground(theAppMgr.getBackgroundColor());
    taRenderer.setFont(theAppMgr.getTheme().getTextFieldFont());
    //taRenderer.setForeground(theAppMgr.getTheme().getMsgTextColor());
    this.theAppMgr = theAppMgr;
  }

  /**
   * @param l
   */
  public void addMouseListener(java.awt.event.MouseListener l) {
    tblTxn.addMouseListener(l);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public TxnLineItemDetailModel getModel() {
    return (this.model);
  }

  /**
   */
  public JCMSTable getTable() {
    return (this.tblTxn);
  }

  /**
   */
  public void clear() {
    model.clear();
    repaint();
  }

  /**
   * Show the line item details in a composite pos txn
   */
  public void showTransaction(PaymentTransaction txn) {
    if (txn instanceof CompositePOSTransaction) {
      showTransaction((CompositePOSTransaction)txn);
      return;
    }
    model.clear();
  }

  /**
   * put your documentation comment here
   */
  public void selectLastRow() {
    int rowCount = tblTxn.getRowCount();
    if (rowCount < 1) {
      // why do we do this?  the table is showing zero rows.
      model.prevPage();
    }
    if (rowCount > 0) {
      tblTxn.setRowSelectionInterval(rowCount - 1, rowCount - 1);
    }
  }

  /**
   * put your documentation comment here
   */
  public void selectFirstRow() {
    selectRow(0);
  }

  /**
   * put your documentation comment here
   */
  public void fireTableDataChanged() {
    model.fireTableDataChanged();
  }

  /**
   * @return
   */
  public POSLineItem getSelectedLineItem() {
    int row = tblTxn.getSelectedRow();
    if (row < 0)
      return (null);
    return (model.getLineItem(row));
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean isRowSelected() {
    int row = tblTxn.getSelectedRow();
    if (row < 0)
      return (false);
    else
      return (true);
  }

  //   public String getSequenceNumber(int num){
  //     return (String)vecSeqNum.elementAt(num);
  //   }
  /**
   * @param txn
   */
  public void showTransaction(CompositePOSTransaction txn) {

    model.clear();
    POSLineItem[] lineItems = txn.getLineItemsArray();

    boolean vatEnabled = false;
    ArmCurrency ctAmt = txn.getCompositeTaxAmount();
    ArmCurrency crtAmt = txn.getCompositeRegionalTaxAmount();
    if (((CMSStore)txn.getStore()).isVATEnabled()
        && (ctAmt == null || ctAmt.doubleValue() == 0)
        && (crtAmt == null || crtAmt.doubleValue() == 0)) {
      vatEnabled = true;
    }
    for (int i = 0; i < lineItems.length; i++) {

      String[] rowData = new String[10];
      rowData[0] = ((CMSItem)lineItems[i].getItem()).getBarCode();
      rowData[1] = TxnDetailApplet.getDiscountPercentString(lineItems[i]);
      rowData[9] = (new Integer(lineItems[i].getSequenceNumber())).toString();
      POSLineItemDetail[] itemDetails = lineItems[i].getLineItemDetailsArray();
      for (int j = 0; j < itemDetails.length; j++) {
        //itemDetails[j].getLineItem().get
        //rowData[1] = itemDetails[j].getReductionAmount().formattedStringValue();        
        ArmCurrency totalAmountDue = itemDetails[j].getTotalAmountDue();
        ArmCurrency taxAmount = itemDetails[j].getTaxAmount();
        ArmCurrency regionalTaxAmount = itemDetails[j].getRegionalTaxAmount();
        if (itemDetails[j] instanceof ReturnLineItemDetail) {
          totalAmountDue = totalAmountDue.multiply( -1);
          taxAmount = taxAmount.multiply( -1);
          regionalTaxAmount = regionalTaxAmount.multiply( -1);
        }
        String taxString = itemDetails[j].isManualTaxAmount()
            ? taxAmount.formattedStringValue() + "*" : taxAmount.formattedStringValue();
        String regTaxString = itemDetails[j].isManualRegionalTaxAmount()
            ? regionalTaxAmount.formattedStringValue() + "*"
            : regionalTaxAmount.formattedStringValue();
        if (vatEnabled) {
          rowData[2] = itemDetails[j].getVatAmount().formattedStringValue();
        } else {
          rowData[2] = txn.getStore().usesRegionalTaxCalculations() ? taxString + "/" + regTaxString
              : taxString;
        }
        rowData[3] = totalAmountDue.formattedStringValue();
        Reduction[] reductions = itemDetails[j].getReductionsArray();
        if (reductions != null && reductions.length > 0)
          rowData[4] = res.getString(reductions[0].getReason());
        if (itemDetails[j] instanceof SaleLineItemDetail) {
          SaleLineItemDetail saleDtl = (SaleLineItemDetail)itemDetails[j];
          if (saleDtl.isReturned()) {
            StringBuffer sb = new StringBuffer();
            sb.append(res.getString("Returned By: "));
            sb.append(saleDtl.getReturnLineItemDetail().getLineItem().getTransaction().
                getCompositeTransaction().getId());
            if (rowData[4] != null) {
              rowData[4] += "\n" + sb.toString();
            } else {
              rowData[4] = sb.toString();
            }
          }
        }
        StringBuffer sBufTicket = new StringBuffer();
        int iCtr = 0;
        //CMSSaleLineItemDetail itmDets []= (CMSSaleLineItemDetail[]) lineItems[i].getLineItemDetailsArray();
        if (itemDetails[j] instanceof CMSSaleLineItemDetail) {
          AlterationLineItemDetail[] altLineDetail = ((CMSSaleLineItemDetail)itemDetails[j]).
              getAlterationLineItemDetailArray();
          if (altLineDetail != null) {
            for (int k = 0; k < altLineDetail.length; k++) {
              sBufTicket.append(altLineDetail[k].getAlterationTicketID());
              iCtr++;
              if (!(iCtr == altLineDetail.length)) {
                sBufTicket.append(",");
                //sBufTicket.append("\n");
              }
              rowData[7] = altLineDetail[k].getFitterID();
              SimpleDateFormat formatter = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
              String strDate = formatter.format(altLineDetail[k].getPromiseDate());
              rowData[8] = strDate;
            }
          }
        } else {
          rowData[7] = "";
          rowData[8] = "";
        }
        rowData[6] = sBufTicket.toString();
        Employee consultant = lineItems[i].getAdditionalConsultant();
        if (consultant != null)
        	//PCR1326 View Transaction Details fix for Armani Japan
        	if (txnLineItemDetailPanelString == null) {
        		rowData[5] = consultant.getShortName();
        	} else {
        		rowData[5] = consultant.getLastName() + " " + consultant.getFirstName();
        	}
        //rowData[6] = itemDetails[j].
        model.addRow(rowData);
        //rowData = new String[7];
      }
    }
    model.firstPage();
    if(vatEnabled) {
      tblTxn.getColumnModel().getColumn(model.TAX_AMT).setHeaderValue("Vat");
      model.setVatEnabled(true);
    } else {
      // special tax code
      tblTxn.getColumnModel().getColumn(model.TAX_AMT).setHeaderValue(txn.getStore().
          usesRegionalTaxCalculations()
          ? res.getString(txn.getStore().getTaxLabel()) + "/"
          + res.getString(txn.getStore().getRegionalTaxLabel())
          : res.getString(txn.getStore().getTaxLabel()));
      model.setVatEnabled(false);
    }
    resetColumnWidths();
  }

  /**
   * put your documentation comment here
   * @param s1
   * @param s2
   * @param s3
   */
  private void setAlterationDetails(String s1, String s2, String s3) {}

  /**
   */
  public void resetColumnWidths() {
    model.setRowsShown(tblTxn.getHeight() / tblTxn.getRowHeight());
    model.setColumnWidth(tblTxn);
    model.fireTableStructureChanged();
    tblTxn.getColumnModel().getColumn(6).setCellRenderer(new MultiLineRenderer());
  }

  /**
   */
  public void nextPage() {
    model.nextPage();
  }

  /**
   */
  public void prevPage() {
    model.prevPage();
  }

  /**
   * @return
   */
  public int getCurrentPageNumber() {
    return (model.getCurrentPageNumber());
  }

  /**
   * @return
   */
  public int getPageCount() {
    return (model.getPageCount());
  }

  /**
   * @param key
   */
  public void positionToKey(int key) {
    model.pageContainingRow(key);
  }

  /* public void positionToKey (int key)
   {
   Iterator itr = model.getAllRows().iterator();
   int i = 0;
   while(itr.hasNext())
   {
   String[] row = (String[])itr.next();
   if(Integer.parseInt(row[6]) == key)
   {
   model.pageContainingRow(i);
   break;
   }
   i++;
   }
   }*/
  /**
   * @return
   */
  public int getPositionKey() {
    int positionKey = 0;
    if (model.getRowCount() > 0)
      positionKey = model.getRowsShown() * model.getCurrentPageNumber();
    return (positionKey);
  }

  /* public int getPositionKey ()
   {
   if(model.getRowCount() > 0)
   {
   String[] firstRowInCurrentPage = (String[])model.getRowInPage(0);
   return(Integer.parseInt(firstRowInCurrentPage[6]));
   }
   else
   return(0);
   }*/
  class VariableRenderer implements TableCellRenderer {
    JTextArea textAreaRenderer = new JTextArea();
    JLabel labelRenderer = new JLabel();

    /**
     * put your documentation comment here
     */
    public VariableRenderer() {
      textAreaRenderer.setLineWrap(false);
      //setWrapStyleWord(true);
      textAreaRenderer.setOpaque(true);
      textAreaRenderer.setForeground(new Color(0, 0, 175));
      labelRenderer.setForeground(new Color(0, 0, 175));
    }

    /**
     * put your documentation comment here
     * @param font
     */
    public void setFont(Font font) {
      labelRenderer.setFont(font);
      textAreaRenderer.setFont(font);
    }

    /**
     * put your documentation comment here
     * @param table
     * @param theValue
     * @param isSelected
     * @param cellHasFocus
     * @param row
     * @param col
     * @return
     */
    public Component getTableCellRendererComponent(JTable table, Object theValue
        , boolean isSelected, boolean cellHasFocus, int row, int col) {
      if (theValue == null) {
        labelRenderer.setText("");
        return (labelRenderer);
      } else if (theValue.toString().indexOf("\n") > 0) {
        textAreaRenderer.setText(theValue.toString());
        return (textAreaRenderer);
      } else {
        labelRenderer.setText(theValue.toString());
        return (labelRenderer);
      }
    }
  }


  /**
   * put your documentation comment here
   * @param row
   */
  public void selectRow(int row) {
    ListSelectionModel model = tblTxn.getSelectionModel();
    model.setSelectionInterval(row, row);
  }

  /***********************************************************************/
  private class MultiLineRenderer extends MultiLineCellRenderer {
    private Color DefaultBackground;
    private Color DefaultForeground;

    /**
     * put your documentation comment here
     */
    public MultiLineRenderer() {
      super();
      this.setEditable(false);
      this.setRequestFocusEnabled(false);
      this.enableInputMethods(false);
      setBorder(null);
      setFont(new Font("Helvetica", 1, 12));
      setForeground(new Color(0, 0, 175));
      setBackground(Color.white);
      DefaultBackground = getBackground();
      DefaultForeground = getForeground();
      setOpaque(true);
    }

    /**
     * put your documentation comment here
     * @param table
     * @param object
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param col
     * @return
     */
    public Component getTableCellRendererComponent(JTable table, Object object, boolean isSelected
        , boolean hasFocus, int row, int col) {
      super.getTableCellRendererComponent(table, object, isSelected, hasFocus, row, col);
      if (object != null)
        setText(object.toString());
      else
        setText("");
      this.transferFocus();
      if (isSelected) {
        //setForeground(Color.BLACK);
        // determine if line item is return
        TxnLineItemDetailModel itemModel = (TxnLineItemDetailModel)table.getModel();
        setAppMgr(theAppMgr);
        //setBackground(new Color(0, 0, 128));
        //this.setSelectionColor(new Color(0, 0, 128));
      }
      return this;
    }
  }
}

