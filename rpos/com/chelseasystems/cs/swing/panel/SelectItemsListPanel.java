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
import com.chelseasystems.cs.swing.model.SelectItemsListModel;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cs.pos.*;
import javax.swing.table.TableCellRenderer;
import com.chelseasystems.cs.swing.bean.ArmJCMSTable;


/**
 */
public class SelectItemsListPanel extends JPanel implements PageNumberGetter {
  SelectItemsListModel modelList = new SelectItemsListModel();
  JCMSTable tblList = new ArmJCMSTable(modelList, JCMSTable.SELECT_ROW);
  TextRenderer renderer = new TextRenderer();
  private final int SELECT = 0;
  private final int SELL_RETURN = 1;
  private final int ITEM_CODE = 2;
  private final int ITEM_DESCRIPTION = 3;
  private final int SELL = 4;
  private final int RETURN = 5;
  private final int QUANTITY = 6;
  private final int UNIT_PRICE = 7;
  private final int MARKDOWN = 8;
  private final int AMOUNT_DUE = 9;
  private boolean bSelectionAllowed = false;
  private boolean bSelectRows = false;

  /**
   * Default Constructor
   */
  public SelectItemsListPanel() {
    try {
      jbInit();
      tblList.addMouseListener(new MouseAdapter() {

        /**
         * put your documentation comment here
         * @param e
         */
        public void mouseClicked(MouseEvent e) {
          toggleRowSelection();
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   */
  public void setReservationCloseHeaders() {
    modelList.setReservationCloseHeaders();
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    if (theAppMgr != null) {
      tblList.setAppMgr(theAppMgr);
      renderer.setFont(theAppMgr.getTheme().getTextFieldFont());
      modelList.makeHTMLColumnHeaders(theAppMgr.getTheme().getHeaderFont());
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
    CheckBoxRenderer boxRenderer = new CheckBoxRenderer();
    modelColumn.getColumn(0).setCellRenderer(boxRenderer);
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
   * Set RowSelection ON/OFF
   * @param bSelectionOn True/False
   */
  public void setSelectionOn(boolean bSelectionOn) {
    tblList.setRowSelectionAllowed(bSelectionOn);
    bSelectionAllowed = bSelectionOn;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean isSelectionAllowed() {
    return bSelectionAllowed;
  }

  /**
   * put your documentation comment here
   */
  public void toggleListSelection() {
    modelList.toggleRowsSelection(bSelectRows);
    modelList.fireTableDataChanged();
    if (bSelectRows) {
      selectRow(0);
    }
    bSelectRows = !bSelectRows;
  }

  /**
   * put your documentation comment here
   */
  public void toggleRowSelection() {
    if (!bSelectionAllowed)
      return;
    int iRow = tblList.getSelectedRow();
    modelList.setRowSelected(iRow);
    modelList.fireTableDataChanged();
    if (modelList.getRowSelected(iRow))
      selectRow(iRow);
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
  public SelectItemsListModel getModel() {
    return (this.modelList);
  }

  /**
   */
  public JCMSTable getTable() {
    return (this.tblList);
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

  /**
   * @param line
   */
  public void addLineItem(POSLineItem line) {
    //if (line instanceof CMSPresaleLineItem || line instanceof CMSConsignmentLineItem || line instanceof CMSReservationLineItem) {
    if (line instanceof CMSPresaleLineItem || line instanceof CMSConsignmentLineItem) {    	
      bSelectRows = false;
    } else {
      bSelectRows = true;
    }
    modelList.addLineItem(line);
    selectLastRow();
  }

  /**
   * put your documentation comment here
   */
  public void sellSelectedLineItem() {
    /*      int iRow = tblList.getSelectedRow();
     modelList.sellLineItem(iRow);
     modelList.fireTableDataChanged();*/
    modelList.sellSelectedLineItems();
    selectRow(0);
  }

  /**
   * put your documentation comment here
   */
  public boolean returnSelectedLineItem() {
    /*int iRow = tblList.getSelectedRow();
     modelList.returnLineItem(iRow);
     modelList.fireTableDataChanged();*/
    boolean containsNonReturnableItems = modelList.returnSelectedLineItems();
    selectRow(0);
    return containsNonReturnableItems;
  }

  /**
   * put your documentation comment here
   */
  public void reserveSelectedLineItem() {
    modelList.reserveSelectedLineItem();
    selectRow(0);
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
  public POSLineItem getLineItem(int row) {
    return (modelList.getLineItem(row));
  }

  /**
   * @return
   */
  public POSLineItem getSelectedLineItem() {
    int row = tblList.getSelectedRow();
    if (row < 0)
      return (null);
    return (modelList.getLineItem(row));
  }

  /**
   */
  public void deleteSelectedLineItem()
      throws BusinessRuleException {
    int row = tblList.getSelectedRow();
    POSLineItem line = getLineItem(row);
    deleteLineItem(line);
  }

  /**
   */
  public void deleteLineItem(POSLineItem line)
      throws BusinessRuleException {
    line.delete();
    modelList.removeRowInModel(line);
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

  /**
   * put your documentation comment here
   * @return
   */
  public boolean isRowChecked() {
    return modelList.getRowSelected(tblList.getSelectedRow());
  }

  private class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {
    private Color DefaultBackground;
    private Color DefaultForeground;

    /**
     */
    public CheckBoxRenderer() {
      super();
      setHorizontalAlignment(SwingConstants.CENTER);
      setForeground(new Color(0, 0, 175));
      setBackground(Color.white);
      DefaultBackground = getBackground();
      DefaultForeground = getForeground();
    }

    /**
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param column
     * @return
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected
        , boolean hasFocus, int row, int column) {
      Boolean b = (Boolean)value;
      System.out.println("selected : " + value);
      if (b != null)
        setSelected(b.booleanValue());
      setBackground(DefaultBackground);
      boolean rowSelected = modelList.getRowSelected(row);
      boolean isAlreadyProcessed = modelList.getIsAlreadyProcessed(row);
      this.setEnabled(true);
      if (rowSelected)
        setForeground(Color.red);
      else
        setForeground(DefaultForeground);
      return this;
    }
  }


  private class TextRenderer extends JLabel implements TableCellRenderer {
    private Color DefaultBackground;
    private Color DefaultForeground;

    /**
     */
    public TextRenderer() {
      super();
      setFont(new Font("Helvetica", 1, 12));
      setForeground(new Color(0, 0, 175));
      setBackground(Color.white);
      DefaultBackground = getBackground();
      DefaultForeground = getForeground();
      setOpaque(true);
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
        case ITEM_CODE:
        case ITEM_DESCRIPTION:
        case QUANTITY:
          setHorizontalAlignment(JLabel.CENTER);
          break;
        case UNIT_PRICE:
        case MARKDOWN:
        case AMOUNT_DUE:
          setHorizontalAlignment(JLabel.RIGHT);
          break;
        default:
          setHorizontalAlignment(JLabel.CENTER);
          break;
      }
      if (isSelected) {
        setForeground(Color.white);
        SelectItemsListModel itemModel = (SelectItemsListModel)table.getModel();
        if (itemModel.isSale(row))
          setBackground(new Color(0, 0, 128));
        else if (itemModel.isLayaway(row))
          setBackground(new Color(0, 132, 121));
        else if (itemModel.isPreSaleOpen(row))
          setBackground(new Color(0, 128, 255));
        else
          setBackground(Color.red);
      } else {
        setBackground(DefaultBackground);
        SelectItemsListModel itemModel = (SelectItemsListModel)table.getModel();
        if (itemModel.isSale(row))
          setForeground(DefaultForeground);
        else if (itemModel.isLayaway(row))
          setForeground(new Color(0, 132, 121));
        else if (itemModel.isPreSaleOpen(row))
          setForeground(new Color(0, 128, 255));
        else
          setForeground(Color.red);
      }
      return (this);
    }
  }


  /**
   * @param e
   */
  public void resetColumnWidths() {
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    TableColumnModel tblColModel = tblList.getColumnModel();
    TableColumn colITem = tblColModel.getColumn(ITEM_CODE);
    colITem.setPreferredWidth((int)(125 * r));
    TableColumn colSellReturn = tblColModel.getColumn(SELL_RETURN);
    colSellReturn.setPreferredWidth((int)(35 * r));
    TableColumn colSelect = tblColModel.getColumn(SELECT);
    colSelect.setPreferredWidth((int)(65 * r));
    TableColumn colSell = tblColModel.getColumn(SELL);
    colSell.setPreferredWidth((int)(25 * r));
    TableColumn colReturn = tblColModel.getColumn(RETURN);
    colReturn.setPreferredWidth((int)(25 * r));
    TableColumn colQty = tblColModel.getColumn(QUANTITY);
    colQty.setPreferredWidth((int)(35 * r));
    TableColumn colUnitPrice = tblColModel.getColumn(UNIT_PRICE);
    colUnitPrice.setPreferredWidth((int)(105 * r));
    TableColumn colMkdn = tblColModel.getColumn(MARKDOWN);
    colMkdn.setPreferredWidth((int)(115 * r));
    TableColumn colAmtDue = tblColModel.getColumn(AMOUNT_DUE);
    colAmtDue.setPreferredWidth((int)(100 * r));
    TableColumn colItemDesc = tblColModel.getColumn(ITEM_DESCRIPTION);
    colItemDesc.setPreferredWidth(tblList.getWidth()
        - (colITem.getPreferredWidth() + colSellReturn.getPreferredWidth()
        + colSelect.getPreferredWidth() + colSell.getPreferredWidth() + colReturn.getPreferredWidth()
        + colQty.getPreferredWidth() + colUnitPrice.getPreferredWidth() + colMkdn.getPreferredWidth()
        + colAmtDue.getPreferredWidth()));
    modelList.setRowsShown(tblList.getHeight() / tblList.getRowHeight());
  }
}

