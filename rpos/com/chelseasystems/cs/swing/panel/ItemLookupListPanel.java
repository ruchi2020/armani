/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 7    | 07/12/2005 | Vikram    | N/A       | Corrections to sorting when returning from Lookup. |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 5    | 05/17/2005 | Vikram    | N/A       | Updated for POS_104665_FS_ItemLookup_Rev1          |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04/27/2005 | Vikram    | N/A       | POS_104665_FS_ItemLookup_Rev1                      |
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import javax.swing.*;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import java.awt.Font;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import javax.swing.table.TableColumn;
import java.awt.Dimension;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cs.swing.model.ItemLookupListModel;
import com.chelseasystems.cs.item.CMSItem;
import javax.swing.table.JTableHeader;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.KeyEvent;
import java.awt.Event.*;
import java.awt.event.ActionEvent;
import java.awt.Event;
import com.chelseasystems.cr.swing.MultiLineCellRenderer;


/**
 * <p>Title:ItemLookupListPanel </p>
 * <p>Description: View List of Items</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillnetInc</p>
 * @author Vikram Mundhra
 * @version 1.0
 */
public class ItemLookupListPanel extends JPanel {
  private ItemLookupListModel modelItemLookupList;
  private JCMSTable tblItem;
  private TextRenderer renderer;

  /**
   * put your documentation comment here
   * @param   IApplicationManager theAppMgr
   */
  public ItemLookupListPanel(IApplicationManager theAppMgr) {
    try {
      modelItemLookupList = new ItemLookupListModel(theAppMgr);
      tblItem = new JCMSTable(modelItemLookupList, JCMSTable.SELECT_ROW);
      renderer = new TextRenderer();
      renderer.setFont(theAppMgr.getTheme().getTextFieldFont());
      WrapTextRenderer wrapRenderer = new WrapTextRenderer();
      this.setLayout(new BorderLayout());
      this.add(tblItem.getTableHeader(), BorderLayout.NORTH);
      this.add(tblItem, BorderLayout.CENTER);
      tblItem.setAlignmentX(CENTER_ALIGNMENT);
      tblItem.setAlignmentY(CENTER_ALIGNMENT);
      modelItemLookupList.setRowsShown(13); // arbitrarily set until resize occurs
      TableColumnModel modelColumn = tblItem.getColumnModel();
      for (int i = 0; i < modelItemLookupList.getColumnCount(); i++) {
        switch (i) {
          case ItemLookupListModel.SKU:
          case ItemLookupListModel.STYLE:
          case ItemLookupListModel.BRAND:
          case ItemLookupListModel.MODEL:
          case ItemLookupListModel.FABRIC:
          case ItemLookupListModel.COLOR:
          case ItemLookupListModel.SUPPLIER:
          case ItemLookupListModel.YEAR:
          case ItemLookupListModel.SEASON:
          case ItemLookupListModel.SIZE:
          case ItemLookupListModel.DESCRIPTION:
          case ItemLookupListModel.PRICE:
            modelColumn.getColumn(i).setCellRenderer(renderer);
        }
      }
      this.addComponentListener(new java.awt.event.ComponentAdapter() {

        /**
         * put your documentation comment here
         * @param e
         */
        public void componentResized(ComponentEvent e) {
          resetColumnWidths();
        }
      }); tblItem.getTableHeader().addMouseListener(new MouseAdapter() {

        /**
         * put your documentation comment here
         * @param e
         */
        public void mouseClicked(MouseEvent e) {
          JTableHeader h = (JTableHeader)e.getSource();
          TableColumnModel columnModel = h.getColumnModel();
          int viewColumn = columnModel.getColumnIndexAtX(e.getX());
          int column = columnModel.getColumn(viewColumn).getModelIndex();
          (new SortItemsAction(column)).actionPerformed(new ActionEvent(this
              , ActionEvent.ACTION_PERFORMED, "SortItems"));
        }
      });
      this.requestFocus();
      InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
      String hotKey = null;
      char hotKeyChar = ' ';
      KeyStroke key = null;
      hotKey = modelItemLookupList.getHotKeyArray()[ItemLookupListModel.SKU];
      hotKeyChar = (hotKey != null && hotKey.length() > 0) ? hotKey.charAt(0) : 'S';
      key = KeyStroke.getKeyStroke(Character.toUpperCase(hotKeyChar), Event.ALT_MASK);
      inputMap.put(key, "SortBySKU");
      getActionMap().put("SortBySKU", new SortItemsAction(modelItemLookupList.SKU));
      hotKey = modelItemLookupList.getHotKeyArray()[ItemLookupListModel.COLOR];
      hotKeyChar = (hotKey != null && hotKey.length() > 0) ? hotKey.charAt(0) : 'C';
      key = KeyStroke.getKeyStroke(Character.toUpperCase(hotKeyChar), Event.ALT_MASK);
      inputMap.put(key, "SortByColor");
      getActionMap().put("SortByColor", new SortItemsAction(modelItemLookupList.COLOR));
      hotKey = modelItemLookupList.getHotKeyArray()[ItemLookupListModel.SIZE];
      hotKeyChar = (hotKey != null && hotKey.length() > 0) ? hotKey.charAt(0) : 'z';
      key = KeyStroke.getKeyStroke(Character.toUpperCase(hotKeyChar), Event.ALT_MASK);
      inputMap.put(key, "SortBySize");
      getActionMap().put("SortBySize", new SortItemsAction(modelItemLookupList.SIZE));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  public JCMSTable getTable() {
    return tblItem;
  }

  /**
   * put your documentation comment here
   * @param row
   */
  public void selectRow(int row) {
    //    ListSelectionModel model = tblItem.getSelectionModel();
    //    model.setSelectionInterval(row, row);
    if (row < 0)
      return;
    modelItemLookupList.pageContainingRow(row);
    selectRowIfInCurrentPage(row);
  }

  /**
   * put your documentation comment here
   * @param absoluteRow
   */
  public void selectRowIfInCurrentPage(int absoluteRow) {
    if (absoluteRow < 0)
      return;
    int rowInPage = modelItemLookupList.getCurrentPage().indexOf(modelItemLookupList.getAllRows().
        elementAt(absoluteRow));
    modelItemLookupList.setLastSelectedItemRow(absoluteRow);
    if (rowInPage < 0)
      return;
    ListSelectionModel model = tblItem.getSelectionModel();
    model.setSelectionInterval(rowInPage, rowInPage);
  }

  /**
   * put your documentation comment here
   * @param cmsItems
   * @param doSort
   */
  public void setItems(CMSItem[] cmsItems, boolean doSort) {
    modelItemLookupList.setItems(cmsItems);
    selectFirstRow();
    if (doSort)
      (new SortItemsAction(ItemLookupListModel.SKU)).actionPerformed(new ActionEvent(this
          , ActionEvent.ACTION_PERFORMED, "SortItems"));
  }

  /**
   * put your documentation comment here
   * @param cmsItem
   */
  public void addItem(CMSItem cmsItem) {
    modelItemLookupList.addItem(cmsItem);
  }

  /**
   * put your documentation comment here
   * @exception BusinessRuleException
   */
  public void deleteSelectedItem()
      throws BusinessRuleException {
    int row = tblItem.getSelectedRow();
    CMSItem cmsItem = getItemAt(row);
    deleteItem(cmsItem);
  }

  /**
   * put your documentation comment here
   * @param cmsItem
   * @exception BusinessRuleException
   */
  public void deleteItem(CMSItem cmsItem)
      throws BusinessRuleException {
    modelItemLookupList.removeRowInModel(cmsItem);
    modelItemLookupList.fireTableDataChanged();
    this.selectLastRow();
  }

  /**
   * put your documentation comment here
   */
  public void pageUp() {
    modelItemLookupList.prevPage();
    //selectFirstRow();
  }

  /**
   * put your documentation comment here
   */
  public void pageDown() {
    modelItemLookupList.nextPage();
    //selectLastRow();
  }

  /**
   * put your documentation comment here
   * @param row
   * @return
   */
  public CMSItem getItemAt(int row) {
    return (modelItemLookupList.getItemAt(row));
  }

  /**
   * put your documentation comment here
   * @param l
   */
  public void addMouseListener(MouseListener l) {
    tblItem.addMouseListener(l);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getSelectedRowIndex() {
    return tblItem.getSelectedRow();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public CMSItem getSelectedItem() {
    if (getSelectedRowIndex() == -1)
      return null;
    return getItemAt(getSelectedRowIndex());
  }

  /**
   * put your documentation comment here
   */
  public void selectLastRow() {
    int rowCount = tblItem.getRowCount();
    if (rowCount < 1) {
      modelItemLookupList.prevPage();
    }
    if (rowCount > 0) {
      tblItem.setRowSelectionInterval(rowCount - 1, rowCount - 1);
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
  public void clear() {
    modelItemLookupList.clear();
  }

  /**
   * put your documentation comment here
   */
  public void update() {
    modelItemLookupList.fireTableDataChanged();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ItemLookupListModel getAddressModel() {
    return modelItemLookupList;
  }

  /**
   * put your documentation comment here
   */
  public void resetColumnWidths() {
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    int iWidth = 0;
    TableColumn skuCol = tblItem.getColumnModel().getColumn(ItemLookupListModel.SKU);
    skuCol.setPreferredWidth((int)(105 * r));
    TableColumn styleCol = tblItem.getColumnModel().getColumn(ItemLookupListModel.STYLE);
    styleCol.setPreferredWidth((int)(65 * r));
    TableColumn brandCol = tblItem.getColumnModel().getColumn(ItemLookupListModel.BRAND);
    brandCol.setPreferredWidth((int)(55 * r));
    TableColumn modelCol = tblItem.getColumnModel().getColumn(ItemLookupListModel.MODEL);
    modelCol.setPreferredWidth((int)(55 * r));
    TableColumn fabricCol = tblItem.getColumnModel().getColumn(ItemLookupListModel.FABRIC);
    fabricCol.setPreferredWidth((int)(50 * r));
    TableColumn colorCol = tblItem.getColumnModel().getColumn(ItemLookupListModel.COLOR);
    colorCol.setPreferredWidth((int)(55 * r));
    TableColumn supplierCol = tblItem.getColumnModel().getColumn(ItemLookupListModel.SUPPLIER);
    supplierCol.setPreferredWidth((int)(55 * r));
    TableColumn yearCol = tblItem.getColumnModel().getColumn(ItemLookupListModel.YEAR);
    yearCol.setPreferredWidth((int)(45 * r));
    TableColumn seasonCol = tblItem.getColumnModel().getColumn(ItemLookupListModel.SEASON);
    seasonCol.setPreferredWidth((int)(70 * r));
    TableColumn sizeCol = tblItem.getColumnModel().getColumn(ItemLookupListModel.SIZE);
    sizeCol.setPreferredWidth((int)(55 * r));
    TableColumn priceCol = tblItem.getColumnModel().getColumn(ItemLookupListModel.PRICE);
    priceCol.setPreferredWidth((int)(110 * r));
    iWidth = tblItem.getWidth()
        - (skuCol.getPreferredWidth() + styleCol.getPreferredWidth() + brandCol.getPreferredWidth()
        + modelCol.getPreferredWidth() + fabricCol.getPreferredWidth() + colorCol.getPreferredWidth()
        + supplierCol.getPreferredWidth() + yearCol.getPreferredWidth()
        + seasonCol.getPreferredWidth() + sizeCol.getPreferredWidth() + priceCol.getPreferredWidth());
    TableColumn descriptionCol = tblItem.getColumnModel().getColumn(ItemLookupListModel.DESCRIPTION);
    descriptionCol.setPreferredWidth(iWidth);
    this.modelItemLookupList.setRowsShown(tblItem.getHeight() / tblItem.getRowHeight());
    tblItem.getTableHeader().setPreferredSize(new Dimension((int)r, 30));
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    if (theAppMgr != null) {
      tblItem.setAppMgr(theAppMgr);
      renderer.setFont(theAppMgr.getTheme().getTextFieldFont());
    }
  }

  /**
   * @return int
   */
  public int getCurrentPageNumber() {
    return (modelItemLookupList.getCurrentPageNumber());
  }

  /**
   * @return int
   */
  public int getTotalPages() {
    return (modelItemLookupList.getPageCount());
  }

  /**
   * put your documentation comment here
   */
  public void nextPage() {
    modelItemLookupList.nextPage();
  }

  /**
   */
  public void prevPage() {
    modelItemLookupList.prevPage();
  }

  private class TextRenderer extends JLabel implements TableCellRenderer {
    private Color DefaultBackground;
    private Color DefaultForeground;

    /**
     * put your documentation comment here
     */
    public TextRenderer() {
      super();
      //setFont(new Font("Helvetica", 1, 12));
      //theAppMgr.getTheme().getTextFieldFont();
      setForeground(new Color(0, 0, 175));
      setBackground(Color.white);
      DefaultBackground = getBackground();
      DefaultForeground = getForeground();
      setOpaque(true);
    }

    /**
     * put your documentation comment here
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param col
     * @return
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected
        , boolean hasFocus, int row, int col) {
      String textToSet = "";
      if (value != null)
        textToSet = value.toString();
      switch (col) {
        case ItemLookupListModel.SKU:
        case ItemLookupListModel.STYLE:
        case ItemLookupListModel.BRAND:
        case ItemLookupListModel.MODEL:
        case ItemLookupListModel.FABRIC:
        case ItemLookupListModel.COLOR:
        case ItemLookupListModel.SUPPLIER:
        case ItemLookupListModel.YEAR:
        case ItemLookupListModel.SEASON:
        case ItemLookupListModel.SIZE:
        case ItemLookupListModel.DESCRIPTION:
        case ItemLookupListModel.PRICE:
          setHorizontalAlignment(JLabel.CENTER);
          break;
      }
      if (isSelected) {
        setForeground(Color.white);
        setBackground(new Color(0, 0, 128));
      } else {
        setBackground(DefaultBackground);
        setForeground(DefaultForeground);
      }
      setText(textToSet);
      return (this);
    }
  }


  private class WrapTextRenderer extends MultiLineCellRenderer {
    private Color DefaultBackground;
    private Color DefaultForeground;

    /**
     * put your documentation comment here
     */
    public WrapTextRenderer() {
      super();
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
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param col
     * @return
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected
        , boolean hasFocus, int row, int col) {
      super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
      setWrapStyleWord(true);
      String textToSet = "";
      if (value != null)
        textToSet = value.toString();
      setAlignmentX(CENTER_ALIGNMENT);
      setAlignmentY(CENTER_ALIGNMENT);
      if (isSelected) {
        setForeground(Color.white);
        setBackground(new Color(0, 0, 128));
      } else {
        setBackground(DefaultBackground);
        setForeground(DefaultForeground);
      }
      setText(textToSet);
      return (this);
    }
  }


  private class SortItemsAction extends AbstractAction {
    private int columnType = -1;

    /**
     * put your documentation comment here
     */
    public SortItemsAction() {
    }

    /**
     * put your documentation comment here
     * @param     int columnType
     */
    public SortItemsAction(int columnType) {
      this.columnType = columnType;
    }

    /**
     * put your documentation comment here
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
      int selectedItemRowNew = modelItemLookupList.sortItems(columnType
          , ((getSelectedItem() != null) ? getSelectedItem().getId() : null));
      if (selectedItemRowNew > -1)
        selectRow(selectedItemRowNew);
    }
  }
}

