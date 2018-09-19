/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cs.swing.model.ViewOpenTxnModel;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cs.pos.*;
import javax.swing.table.TableCellRenderer;


/**
 */
public class ViewOpenTxnPanel extends JPanel implements PageNumberGetter {
  ViewOpenTxnModel modelList = new ViewOpenTxnModel();
  JCMSTable tblList = new JCMSTable(modelList, JCMSTable.SELECT_ROW);
  TextRenderer renderer = new TextRenderer();
  private final int EXPIRY_DATE = 0;
  private final int TXN_DATE = 1;
  private final int TXN_ID = 2;
  private final int ASSOCIATE_ID = 3;
  private final int CUSTOMER_ID = 4;

  /**
   * Default Constructor
   */
  public ViewOpenTxnPanel() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    if (theAppMgr != null) {
      tblList.setAppMgr(theAppMgr);
      renderer.setFont(theAppMgr.getTheme().getTextFieldFont());
    }
  }

  /**
   * @exception Exception
   */
  private void jbInit()
      throws Exception {
    this.setLayout(new BorderLayout());
    tblList.getTableHeader().setPreferredSize(new Dimension(833, 45));
    this.add(tblList.getTableHeader(), BorderLayout.NORTH);
    this.add(tblList, BorderLayout.CENTER);
    modelList.setRowsShown(13);
    TableColumnModel modelColumn = tblList.getColumnModel();
    for (int i = 0; i < modelList.getColumnCount(); i++) {
      modelColumn.getColumn(i).setCellRenderer(renderer);
    }
    this.addComponentListener(new java.awt.event.ComponentAdapter() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void componentResized(ComponentEvent e) {
        resetColumnWidths();
      }
    }); tblList.setRequestFocusEnabled(false);
    tblList.setCellSelectionEnabled(false);
    tblList.setColumnSelectionAllowed(false);
    tblList.setRowSelectionAllowed(true);
  }

  /**
   */
  public void clear() {
    modelList.clear();
  }

  /**
   */
  public void update() {
    modelList.fireTableDataChanged();
  }

  /**
   * @param row
   */
  public void selectRow(int row) {
    ListSelectionModel model = tblList.getSelectionModel();
    model.setSelectionInterval(row, row);
  }

  /**
   */
  public ViewOpenTxnModel getModel() {
    return (this.modelList);
  }

  /**
   */
  public JCMSTable getTable() {
    return (this.tblList);
  }

  /**
   * put your documentation comment here
   * @param ml
   */
  public void addMouseListener(MouseListener ml) {
    tblList.addMouseListener(ml);
  }

  /**
   */
  public void selectLastRow() {
    int rowCount = tblList.getRowCount();
    if (rowCount < 1) {
      modelList.prevPage();
    }
    if (rowCount > 0) {
      tblList.setRowSelectionInterval(rowCount - 1, rowCount - 1);
    }
  }

  /**
   */
  public void selectFirstRow() {
    selectRow(0);
  }

  public void selectFirstRowFirstPage() {
	  modelList.firstPage();
	    selectRow(0);
	  }
  
  /**
   * @param line
   */
  public void addTransactionHeader(CMSTransactionHeader tranHeader) {
    modelList.addTransactionHeader(tranHeader);
    selectLastRow();
  }

  /**
   */
  public void nextPage() {
    modelList.nextPage();
    selectFirstRow();
  }

  /**
   */
  public void prevPage() {
    modelList.prevPage();
    selectLastRow();
  }

  /**
   * @return
   */
  public int getCurrentPageNumber() {
    return (modelList.getCurrentPageNumber());
  }

  /**
   * @return
   */
  public int getTotalPages() {
    return (modelList.getPageCount());
  }

  /**
   * @param row
   * @return
   */
  public CMSTransactionHeader getTranHeader(int row) {
    return (modelList.getTransactionHeader(row));
  }

  /**
   * @return
   */
  public CMSTransactionHeader getSelectedTranHeader() {
    int row = tblList.getSelectedRow();
    if (row < 0)
      return (null);
    return (modelList.getTransactionHeader(row));
  }

  /**
   */
  public void deleteSelectedTranHeader()
      throws BusinessRuleException {
    int row = tblList.getSelectedRow();
    CMSTransactionHeader tranHeader = getTranHeader(row);
    deleteTranHeader(tranHeader);
  }

  /**
   */
  public void deleteTranHeader(CMSTransactionHeader tranHeader)
      throws BusinessRuleException {
    modelList.removeRowInModel(tranHeader);
    modelList.fireTableDataChanged();
    this.selectLastRow();
  }

  /**
   * @return
   */
  public boolean isRowSelected() {
    int row = tblList.getSelectedRow();
    if (row < 0)
      return (false);
    else
      return (true);
  }

  private class TextRenderer extends JLabel implements TableCellRenderer {
    private Color DefaultBackground;
    private Color DefaultForeground;

    /**
     * put your documentation comment here
     */
    public TextRenderer() {
      super();
      setFont(new Font("Helvetica", 1, 12));
      setForeground(new Color(0, 0, 175));
      setBackground(Color.white);
      setOpaque(true);
      DefaultBackground = getBackground();
      DefaultForeground = getForeground();
    }

    /**
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected
        , boolean hasFocus, int row, int col) {
      if (value != null)
        setText(value.toString());
      else
        setText("");
      switch (col) {
        case EXPIRY_DATE:
        case TXN_DATE:
        case TXN_ID:
        case ASSOCIATE_ID:
        case CUSTOMER_ID:
          this.setHorizontalAlignment(JLabel.CENTER);
      }
      if (isSelected) {
        setForeground(Color.white);
        setBackground(new Color(0, 128, 255));
      } else {
        setBackground(DefaultBackground);
        setForeground(new Color(0, 128, 255));
      }
      return (this);
    }
  }


  /**
   * @param e
   */
  public void resetColumnWidths() {
    //      double r = com.chelseasystems.cr.swing.CMSApplet.r;
    //      TableColumnModel tblColModel = tblList.getColumnModel();
    //
    //      TableColumn colITem = tblColModel.getColumn(ITEM_CODE);
    //      colITem.setPreferredWidth((int)(115*r));
    //
    //      TableColumn colSellReturn = tblColModel.getColumn(SELL_RETURN);
    //      colSellReturn.setPreferredWidth((int)(35*r));
    //
    //      TableColumn colSelect = tblColModel.getColumn(SELECT);
    //      colSelect.setPreferredWidth((int)(65*r));
    //
    //
    //      TableColumn colSell = tblColModel.getColumn(SELL);
    //      colSell.setPreferredWidth((int)(25*r));
    //
    //      TableColumn colReturn = tblColModel.getColumn(RETURN);
    //      colReturn.setPreferredWidth((int)(25*r));
    //
    //      TableColumn colQty = tblColModel.getColumn(QUANTITY);
    //      colQty.setPreferredWidth((int)(35*r));
    //
    //      TableColumn colUnitPrice = tblColModel.getColumn(UNIT_PRICE);
    //      colUnitPrice.setPreferredWidth((int)(105*r));
    //
    //      TableColumn colMkdn = tblColModel.getColumn(MARKDOWN);
    //      colMkdn.setPreferredWidth((int)(115*r));
    //
    //      TableColumn colAmtDue = tblColModel.getColumn(AMOUNT_DUE);
    //      colAmtDue.setPreferredWidth((int)(100*r));
    //
    //
    //      TableColumn colItemDesc = tblColModel.getColumn(ITEM_DESCRIPTION);
    //      colItemDesc.setPreferredWidth(tblList.getWidth() -
    //                                    (colITem.getPreferredWidth() +
    //                                     colSellReturn.getPreferredWidth() +
    //                                     colSelect.getPreferredWidth() +
    //                                     colSell.getPreferredWidth() +
    //                                     colReturn.getPreferredWidth() +
    //                                     colQty.getPreferredWidth() +
    //                                     colUnitPrice.getPreferredWidth() +
    //                                     colMkdn.getPreferredWidth() +
    //                                     colAmtDue.getPreferredWidth()
    //                                     )
    //
    //                                    );
    modelList.setRowsShown(tblList.getHeight() / tblList.getRowHeight());
  }
}

