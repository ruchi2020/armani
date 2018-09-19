/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cs.swing.model.LineItemPOSModelMultiSelect;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cs.swing.bean.ArmJCMSTable;


/**
 */
public class LineItemPOSPanelMultiSelect extends JPanel implements MarkdownModifier
    , PageNumberGetter {
  LineItemPOSModelMultiSelect model = new LineItemPOSModelMultiSelect();
  JCMSTable tblList = new ArmJCMSTable(model, JCMSTable.SELECT_ROW);
  TextRenderer renderer = new TextRenderer();
  CheckBoxRenderer boxRenderer = new CheckBoxRenderer();
  ArmCurrency undoBufferPrice = null;
  HashMap undoBufferMarkdown = new HashMap();
  HashMap undoBufferMarkdownPercent = new HashMap();
  IApplicationManager theAppMgr;
  java.util.ResourceBundle res;

  /**
   * Default Constructor
   */
  public LineItemPOSPanelMultiSelect() {
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
    //        tblList.addKeyListener(new java.awt.event.KeyAdapter()
    //            {
    //                public void keyPressed(KeyEvent e) {
    //                    System.out.println("key fired: ");
    //                   char key = e.getKeyChar();
    //                   System.out.println("key fired: " + key);
    //                   if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_SPACE || key == KeyEvent.VK_DELETE || key == KeyEvent.VK_BACK_SPACE)
    //                      clickEvent(null);
    //                }
    //            });
    tblList.registerKeyboardAction(tblList, JCMSTable.SELECT_CMD
        , KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), JTable.WHEN_FOCUSED);
    tblList.registerKeyboardAction(tblList, JCMSTable.SELECT_CMD
        , KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), JTable.WHEN_IN_FOCUSED_WINDOW);
  }

  /**
   * put your documentation comment here
   * @param e
   */
  public void clickEvent(MouseEvent e) {
    int row = tblList.getSelectedRow();
    if (row < 0)
      return;
    model.setRowSelected(row);
    model.fireTableDataChanged();
    selectRow(row);
    //theAppMgr.setEditAreaFocus();
  }

  /**
   * put your documentation comment here
   * @param lineItem
   */
  public void loadModel(POSLineItem[] lineItem) {
    model.clear();
    for (int i = 0; i < lineItem.length; i++) {
      model.addLineItem(lineItem[i]);
    }
    model.firstPage();
  }

  /**
   * put your documentation comment here
   * @param reason
   * @exception BusinessRuleException
   */
  public void setManualMarkdownReason(String reason)
      throws BusinessRuleException {
    POSLineItem[] lineItems = getSelectedLineItems();
    for (int i = 0; i < lineItems.length; i++)
      lineItems[i].setManualMarkdownReason(reason);
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
   * @param qty
   * @return
   */
  public void modifySelectedQuantity(int qty)
      throws BusinessRuleException {
    POSLineItem line = getSelectedLineItem();
    if (line != null) {
      if (qty == 0)
        deleteSelectedLineItem();
      else
        line.setQuantity(new Integer(qty));
    }
    tblList.repaint();
  }

  /**
   */
  public LineItemPOSModelMultiSelect getModel() {
    return (this.model);
  }

  /**
   */
  public JCMSTable getTable() {
    return (this.tblList);
  }

  /**
   * Get the selected line item and adjust the unit price. If the item is <code>
   * SpecificItem</code> then call setManualUnitPrice() directly.
   * @see <code>POSLineItem.adjustUnitPrice()</code>
   * @see <code>POSLineItem.setManualUnitPrice()</code>
   * @see <code>SpecificItem</code>
   * @param newPrice
   */
  public void modifySelectedPrice(ArmCurrency newPrice)
      throws BusinessRuleException {
    POSLineItem line = getSelectedLineItem();
    if (line != null) {
      // this is a hack for gift certs until objects are smarter
      com.chelseasystems.cr.item.Item item = line.getItem();
      if (item.requiresManualUnitPrice() || item.isRedeemable()) {
        this.undoBufferPrice = line.getManualUnitPrice();
        line.adjustUnitPrice(newPrice);
      } else {
        try {
          this.undoBufferPrice = line.getItemRetailPrice().subtract(line.getManualMarkdownAmount());
        } catch (CurrencyException ex) {
          // TODO!?!
        }
        line.adjustMarkdownAmount(newPrice);
      }
      this.repaint();
    }
  }

  /**
   * put your documentation comment here
   * @exception BusinessRuleException
   */
  public void undoModifySelectedPrice()
      throws BusinessRuleException {
    if (this.undoBufferPrice != null) {
      try {
        this.modifySelectedPrice(this.undoBufferPrice);
      } catch (BusinessRuleException ex) {
        // make sure we can only try this once.
        this.undoBufferPrice = null;
        throw ex;
      }
    }
  }

  /**
   * put your documentation comment here
   * @param newPrice
   * @exception BusinessRuleException
   */
  public void modifySelectedUnitPrice(ArmCurrency newPrice)
      throws BusinessRuleException {
    POSLineItem line = getSelectedLineItem();
    if (line != null) {
      // this is a hack for gift certs until objects are smarter
      com.chelseasystems.cr.item.Item item = line.getItem();
      this.undoBufferPrice = line.getManualUnitPrice();
      line.adjustUnitPrice(newPrice);
      this.repaint();
    }
  }

  /**
   * put your documentation comment here
   * @exception BusinessRuleException
   */
  public void undoModifySelectedUnitPrice()
      throws BusinessRuleException {
    if (this.undoBufferPrice != null) {
      try {
        this.modifySelectedUnitPrice(this.undoBufferPrice);
      } catch (BusinessRuleException ex) {
        // make sure we can only try this once.
        this.undoBufferPrice = null;
        throw ex;
      }
    }
  }

  /**
   * @param markdownAmount
   */
  public void modifySelectedMarkdown(ArmCurrency markdownAmount)
      throws BusinessRuleException {
    POSLineItem[] lines = getSelectedLineItems();
    undoBufferMarkdown = new HashMap();
    if (lines != null) {
      for (int i = 0; i < lines.length; i++) {
        this.undoBufferMarkdown.put(lines[i], lines[i].getManualMarkdownAmount());
        lines[i].setManualMarkdownAmount(markdownAmount);
      }
    }
    this.repaint();
  }

  /**
   * put your documentation comment here
   * @exception BusinessRuleException
   */
  public void undoModifySelectedMarkdown()
      throws BusinessRuleException {
    if (this.undoBufferMarkdown != null) {
      try {
        POSLineItem[] lines = getSelectedLineItems();
        if (lines != null) {
          for (int i = 0; i < lines.length; i++) {
            ArmCurrency markdownAmount = (ArmCurrency)this.undoBufferMarkdown.get(lines[i]);
            lines[i].setManualMarkdownAmount(markdownAmount);
          }
        }
      } catch (BusinessRuleException ex) {
        // make sure we can only try this once.
        this.undoBufferMarkdown = null;
        throw ex;
      }
    }
  }

  /**
   * @param amount
   */
  public void modifySelectedMarkdownPercent(int amount)
      throws BusinessRuleException {
    POSLineItem[] lines = getSelectedLineItems();
    undoBufferMarkdownPercent = new HashMap();
    if (lines != null) {
      for (int i = 0; i < lines.length; i++) {
        // Note: cant' get the current percent because the line item doesn't
        // store it. So, we use the markdown amount.
        this.undoBufferMarkdownPercent.put(lines[i], lines[i].getManualMarkdownAmount());
        lines[i].setManualMarkdownPercent(new Double(amount * .01));
      }
    }
    this.repaint();
  }

  /**
   * put your documentation comment here
   * @exception BusinessRuleException
   */
  public void undoModifySelectedMarkdownPercent()
      throws BusinessRuleException {
    if (this.undoBufferMarkdownPercent != null) {
      try {
        POSLineItem[] lines = getSelectedLineItems();
        if (lines != null) {
          for (int i = 0; i < lines.length; i++) {
            ArmCurrency markdownAmount = (ArmCurrency)this.undoBufferMarkdownPercent.get(lines[i]);
            lines[i].setManualMarkdownAmount(markdownAmount);
          }
        }
      } catch (BusinessRuleException ex) {
        // make sure we can only try this once.
        this.undoBufferMarkdownPercent = null;
        this.undoBufferMarkdown = null;
        throw ex;
      }
    }
  }

  /**
   */
  public void selectLastRow() {
    if (tblList.getRowCount() < 1) {
      model.prevPage();
    }
    ListSelectionModel model = tblList.getSelectionModel();
    model.setSelectionInterval(tblList.getRowCount() - 1, tblList.getRowCount() - 1);
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
    model.addLineItem(line);
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
    selectFirstRow();
  }

  /**
   */
  public void prevPage() {
    model.prevPage();
    selectLastRow();
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
  public int getTotalPages() {
    return (model.getPageCount());
  }

  /**
   * @param row
   * @return
   */
  public POSLineItem getLineItem(int row) {
    return (model.getLineItem(row));
  }

  /**
   * @return
   */
  public POSLineItem getSelectedLineItem() {
    int row = tblList.getSelectedRow();
    if (row < 0)
      return (null);
    return (model.getLineItem(row));
  }

  /**
   * put your documentation comment here
   * @return
   */
  public POSLineItem[] getSelectedLineItems() {
    return (model.getSelectedLineItems());
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
      return (false);
    else
      return (true);
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
      boolean rowSelected = LineItemPOSPanelMultiSelect.this.model.getRowSelected(row);
      setBackground(DefaultBackground);
      this.setEnabled(true);
      //if (rowSelected)
      //   setForeground(Color.red);
      //else
      //   setForeground(DefaultForeground);
      return (this);
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
        case 6:
          setHorizontalAlignment(JLabel.RIGHT);
          break;
      }
      LineItemPOSModelMultiSelect itemModel = LineItemPOSPanelMultiSelect.this.model;
      boolean rowSelected = itemModel.getRowSelected(row);
      if (isSelected) {
        setForeground(Color.white);
        // determine if line item is return
        if (itemModel.isSale(row))
          setBackground(new Color(0, 0, 128));
        else if (itemModel.isLayaway(row))
          setBackground(new Color(0, 132, 121));
        else
          setBackground(Color.red);
      } else {
        setBackground(DefaultBackground);
        // determine if line item is return
        if (itemModel.isSale(row))
          setForeground(DefaultForeground);
        else if (itemModel.isLayaway(row))
          setForeground(new Color(0, 132, 121));
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
    TableColumn SelectedCol = tblList.getColumnModel().getColumn(0);
    SelectedCol.setPreferredWidth((int)(60 * r));
    TableColumn ItemCol = tblList.getColumnModel().getColumn(1);
    ItemCol.setPreferredWidth((int)(125 * r));
    TableColumn QtyCol = tblList.getColumnModel().getColumn(3);
    QtyCol.setPreferredWidth((int)(100 * r));
    TableColumn UnitPriceCol = tblList.getColumnModel().getColumn(4);
    UnitPriceCol.setPreferredWidth((int)(100 * r));
    TableColumn MarkdownCol = tblList.getColumnModel().getColumn(5);
    MarkdownCol.setPreferredWidth((int)(100 * r));
    TableColumn AmtDueCol = tblList.getColumnModel().getColumn(6);
    AmtDueCol.setPreferredWidth((int)(100 * r));
    TableColumn DescriptionCol = tblList.getColumnModel().getColumn(2);
    DescriptionCol.setPreferredWidth(tblList.getWidth()
        - (ItemCol.getPreferredWidth() + QtyCol.getPreferredWidth()
        + UnitPriceCol.getPreferredWidth() + AmtDueCol.getPreferredWidth()
        + MarkdownCol.getPreferredWidth()));
    model.setRowsShown(tblList.getHeight() / tblList.getRowHeight());
  }
}

