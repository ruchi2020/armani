/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.pos;

/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 3    | 06-15-2005 | Vikram    | 203       |Select All selects only first page and not all|
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 04-12-2005 | Khyati    | N/A       |1.Send Sale specification.                    |
 --------------------------------------------------------------------------------------------
 | 1    | 04-12-2005 | Base      | N/A       |                                              |
 --------------------------------------------------------------------------------------------
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cs.swing.model.ItemsToShipModel;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.item.MiscItemManager;
import com.chelseasystems.cs.swing.bean.ArmJCMSTable;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.util.LineItemPOSUtil;


/**
 */
public class ItemsToShipPanel extends JPanel {
  ItemsToShipModel model = new ItemsToShipModel();
  JCMSTable tblList = new ArmJCMSTable(model, JCMSTable.SELECT_ROW);
  TextRenderer renderer = new TextRenderer();
  CheckBoxRenderer boxRenderer = new CheckBoxRenderer();
  ArmCurrency undoBufferPrice = null;
  HashMap undoBufferMarkdown = new HashMap();
  HashMap undoBufferMarkdownPercent = new HashMap();
  IApplicationManager theAppMgr;
  java.util.ResourceBundle res;
  private boolean isInquireMode;

  /**
   * Default Constructor
   */
  public ItemsToShipPanel() {
    try {
      res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
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
    this.theAppMgr = theAppMgr;
  }

  /**
   * @exception Exception
   */
  private void jbInit()
      throws Exception {
    this.setLayout(new BorderLayout());
    this.add(tblList.getTableHeader(), BorderLayout.NORTH);
    this.add(tblList, BorderLayout.CENTER);
    model.setRowsShown(13); // arbitrarily set until resize occurs
    TableColumnModel modelColumn = tblList.getColumnModel();
    for (int i = 1; i < model.getColumnCount(); i++)
      modelColumn.getColumn(i).setCellRenderer(renderer);
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
    }); tblList.addMouseListener(new java.awt.event.MouseAdapter() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void mouseClicked(MouseEvent e) {
        clickEvent(e);
      }
    });
  }

  /**
   * put your documentation comment here
   * @param isInquireMode
   */
  public void setInquireMode(boolean isInquireMode) {
    this.isInquireMode = isInquireMode;
  }

  /**
   * put your documentation comment here
   * @param e
   */
  public void clickEvent(MouseEvent e) {
    if (isInquireMode)
      return;
    int row = tblList.getSelectedRow();
    if (row < 0)
      return;
    //if (getSelectedLineItem().getItem().canHaveManualUnitPrice()) {
    //   theAppMgr.showErrorDlg(res.getString("Manually priced items cannot be marked down"));
    //   return;
    //}
    model.setRowSelected(row);
    model.fireTableDataChanged();
    theAppMgr.setEditAreaFocus();
  }

  /**
   * put your documentation comment here
   * @param lineItem
   * @param shippingRequest
   */
  public void loadModel(SaleLineItem[] lineItem, ShippingRequest shippingRequest) {
    model.clear();
    for (int i = 0; i < lineItem.length; i++) {
      //         (MiscItemManager.getInstance().isMiscItem(item.getId()))
      if (!MiscItemManager.getInstance().isMiscItem(lineItem[i].getItem().getId()))
        model.addLineItem(lineItem[i], shippingRequest);
    }
  }

  /**
   * put your documentation comment here
   * @param lineItem
   * @param shippingRequest
   */
  public void loadModel(POSLineItem[] lineItem, ShippingRequest shippingRequest) {
    model.clear();
    for (int i = 0; i < lineItem.length; i++) {
      //         (MiscItemManager.getInstance().isMiscItem(item.getId()))
      if (!MiscItemManager.getInstance().isMiscItem(lineItem[i].getItem().getId())
          || (LineItemPOSUtil.isNotOnFileItem(lineItem[i].getItem().getId())))
        model.addLineItem(lineItem[i], shippingRequest);
    }
  }

  /**
   */
  public void clear() {
    model.clear();
  }

  /**
   */
  public void update() {
    model.fireTableDataChanged();
  }

  /**
   * @param row
   */
  public void selectRow(int row) {
    ListSelectionModel model = tblList.getSelectionModel();
    model.setSelectionInterval(row, row);
    //this.model.setRowSelected(row, true);
  }

  /**
   * Selects all the Rows on Shipping detail screen
   */
  public void selectAllRows(boolean selected) {
    int currentPageNumber = model.getCurrentPageNumber();
    int allPages = model.getPageCount();
    //VM: select all rows in all pages
    model.firstPage();
    for (int p = 0; p < allPages; model.nextPage(), p++) {
      int allRows = model.getRowCount();
      for (int i = 0; i < allRows; i++) {
        model.setRowSelected(i, selected);
      }
    }
    //VM: set current page
    model.firstPage();
    for (int p = 0; p < currentPageNumber; p++) {
      model.nextPage();
    }
    model.fireTableDataChanged();
    theAppMgr.setEditAreaFocus();
  }

  /**
   */
  public ItemsToShipModel getModel() {
    return this.model;
  }

  /**
   */
  public JCMSTable getTable() {
    return this.tblList;
  }

  /**
   * @param l mouse listener added to table
   */
  public void addMouseListener(MouseListener l) {
    tblList.addMouseListener(l);
  }

  /**
   */
  public void fireTableDataChanged() {
    model.fireTableDataChanged();
  }

  /**
   */
  public void nextPage() {
    model.nextPage();
    selectLastRow();
  }

  /**
   * put your documentation comment here
   */
  public void selectLastRow() {
    ListSelectionModel model = tblList.getSelectionModel();
    model.setSelectionInterval(tblList.getRowCount() - 1, tblList.getRowCount() - 1);
  }

  /**
   */
  public void selectFirstRow() {
    selectRow(0);
  }

  /**
   */
  public void prevPage() {
    model.prevPage();
    selectFirstRow();
  }

  /**
   * @return
   */
  public int getCurrentPageNumber() {
    return model.getCurrentPageNumber();
  }

  /**
   * @return
   */
  public int getTotalPages() {
    return model.getPageCount();
  }

  /**
   * @param row
   * @return
   */
  public POSLineItem getLineItem(int row) {
    return model.getLineItem(row);
  }

  /**
   * @return
   */
  public POSLineItem getSelectedLineItem() {
    int row = tblList.getSelectedRow();
    if (row < 0)
      return null;
    return model.getLineItem(row);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public POSLineItem[] getSelectedLineItems() {
    return model.getSelectedLineItems();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public POSLineItem[] getUnSelectedLineItems() {
    return model.getUnSelectedLineItems();
  }

  /**
   */
  public void deleteSelectedLineItem()
      throws BusinessRuleException {
    int row = tblList.getSelectedRow();
    POSLineItem line = getLineItem(row);
    line.delete();
    model.deleteLineItem(row);
  }

  /**
   */
  public void deleteLineItem(POSLineItem line)
      throws BusinessRuleException {
    line.delete();
    model.removeRowInModel(line);
    model.fireTableDataChanged();
  }

  /**
   * @return
   */
  public boolean isRowSelected() {
    int row = tblList.getSelectedRow();
    if (row < 0)
      return false;
    else
      return true;
  }

  /***********************************************************************/
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
      if (b != null)
        setSelected(b.booleanValue());
      boolean rowSelected = ItemsToShipPanel.this.model.getRowSelected(row);
      setBackground(DefaultBackground);
      this.setEnabled(true);
      //if (rowSelected)
      //   setForeground(Color.red);
      //else
      //   setForeground(DefaultForeground);
      return this;
    }
  }


  /***********************************************************************/
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
        case 0:
        case 1:
        case 2:
          setHorizontalAlignment(JLabel.LEFT);
          break;
        case 3:
          setHorizontalAlignment(JLabel.CENTER);
          break;
        case 4:
        case 5:
          setHorizontalAlignment(JLabel.RIGHT);
          break;
      }
      ItemsToShipModel itemModel = ItemsToShipPanel.this.model;
      boolean rowSelected = itemModel.getRowSelected(row);
      if (isSelected) {
        //if(rowSelected) {
        //   setForeground(Color.red);
        //}
        //else
        setForeground(Color.white);
        setBackground(new Color(0, 0, 128));
      } else {
        //if(rowSelected) {
        //   setForeground(Color.red);
        //}
        //else
        setForeground(DefaultForeground);
        setBackground(DefaultBackground);
      }
      return this;
    }
  }


  /**
   * @param e
   */
  public void resetColumnWidths() {
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    TableColumn SelectedCol = tblList.getColumnModel().getColumn(0);
    SelectedCol.setPreferredWidth((int)(60 * r));
    TableColumn ItemCol = tblList.getColumnModel().getColumn(1);
    ItemCol.setPreferredWidth((int)(125 * r));
    TableColumn QtyCol = tblList.getColumnModel().getColumn(3);
    QtyCol.setPreferredWidth((int)(100 * r));
    TableColumn UnitPriceCol = tblList.getColumnModel().getColumn(4);
    UnitPriceCol.setPreferredWidth((int)(100 * r));
    TableColumn AmtDueCol = tblList.getColumnModel().getColumn(5);
    AmtDueCol.setPreferredWidth((int)(100 * r));
    TableColumn DescriptionCol = tblList.getColumnModel().getColumn(2);
    DescriptionCol.setPreferredWidth(tblList.getWidth()
        - (ItemCol.getPreferredWidth() + QtyCol.getPreferredWidth()
        + UnitPriceCol.getPreferredWidth() + AmtDueCol.getPreferredWidth()));
    model.setRowsShown(tblList.getHeight() / tblList.getRowHeight());
  }
}

