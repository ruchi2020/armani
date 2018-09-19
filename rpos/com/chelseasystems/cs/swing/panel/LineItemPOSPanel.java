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
 | 3    | 04-13-2005 | Manpreet  | N/A       |1.PreSale Open Specification                  |
 ---------------------------------------------------------------------------------------------
 | 2    | 02-02-2005 | Manpreet  | N/A       | Modified  to add Associate ID column         |
 |      |            |           |           |   and Reset column widths acoord. to Armani  |
 +------+------------+-----------+-----------+----------------------------------------------+
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
import com.chelseasystems.cs.swing.model.LineItemPOSModel;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.swing.MultiLineCellRenderer;
import com.chelseasystems.cr.discount.Discount;
import com.chelseasystems.cs.discount.ArmLineDiscount;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.util.ResourceManager;
import javax.swing.table.TableCellRenderer;


/**
 */
public class LineItemPOSPanel extends JPanel implements MarkdownModifier, PageNumberGetter {
  LineItemPOSModel model = new LineItemPOSModel();
  JCMSTable tblList = new JCMSTable(model, JCMSTable.SELECT_ROW);
  TextRenderer renderer = new TextRenderer();
  ArmCurrency undoBufferPrice = null;
  ArmCurrency undoBufferMarkdown = null;
  ArmCurrency undoBufferMarkdownPercent = null;
  private ResourceBundle res = null;
  private IApplicationManager theAppMgr;
  private final int ITEM_CODE = 0;
  private final int ITEM_DESCRIPTION = 1;
  private final int ASSOCIATE_ID = 2;
  private final int QUANTITY = 3;
  private final int UNIT_PRICE = 4;
  private final int MARKDOWN = 5;
  private final int AMOUNT_DUE = 6;
  private Color clrReservations;

  /**
   * Default Constructor
   */
  public LineItemPOSPanel() {
    clrReservations = new Color(128, 0, 0);
    try {
      jbInit();
      res = ResourceManager.getResourceBundle();
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
      this.theAppMgr = theAppMgr;
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
    this.add(tblList.getTableHeader(), BorderLayout.NORTH);
    this.add(tblList, BorderLayout.CENTER);
    model.setRowsShown(13); // arbitrarily set until resize occurs
    TableColumnModel modelColumn = tblList.getColumnModel();
    for (int i = 0; i < model.getColumnCount(); i++) {
      modelColumn.getColumn(i).setCellRenderer(renderer);
    }
    modelColumn.getColumn(ITEM_DESCRIPTION).setCellRenderer(new MultiLineRenderer());
    this.addComponentListener(new java.awt.event.ComponentAdapter() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void componentResized(ComponentEvent e) {
        resetColumnWidths();
      }
    });
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
  }

  /**
   * put your documentation comment here
   * @param reason
   * @exception BusinessRuleException
   */
  public void setManualMarkdownReason(String reason)
      throws BusinessRuleException {
    POSLineItem lineItem = getSelectedLineItem();
    lineItem.setManualMarkdownReason(reason);
  }

  /**
   * @param qty
   * @return
   */
  public void modifySelectedQuantity(int qty)
      throws BusinessRuleException {
    POSLineItem line = getSelectedLineItem();
    if (line != null) {
      if (line instanceof CMSSaleLineItem) {
        Discount priceLineDiscount = ((CMSSaleLineItem)line).getPriceDiscount();
        if (priceLineDiscount != null) {
          theAppMgr.showErrorDlg(res.getString(
              "Now-Price Discount has been removed due to modification of Quantity"));
          ((CMSSaleLineItem)line).removeDiscount(priceLineDiscount);
          line.getTransaction().getCompositeTransaction().removeDiscount(priceLineDiscount);
          ((CMSSaleLineItem)line).removeAddPriceDiscount();
        }
//TD
        Discount[] discounts = ((CMSSaleLineItem)line).getDiscountsArray();
        for (int index=0; index < discounts.length; index++) {
          Discount discount = discounts[index];
          if (discount instanceof ArmLineDiscount) {
            theAppMgr.showErrorDlg(res.getString("By Amount Discount has been removed due to modification of Quantity"));
            line.getTransaction().getCompositeTransaction().removeDiscount(discount);
          }
        }
      }
      if (qty == 0)
        deleteSelectedLineItem();
      else
        line.setQuantity(new Integer(qty));
    }
    tblList.repaint();
  }

  /**
   */
  public LineItemPOSModel getModel() {
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
      com.chelseasystems.cr.item.Item item = line.getItem();
      try {
        this.undoBufferPrice = line.getItemRetailPrice().subtract(line.getManualMarkdownAmount());
      } catch (CurrencyException ex) {
        // TODO!?!
      }
      line.adjustMarkdownAmount(newPrice);
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
   *
   * @param newPrice Currency
   * @throws BusinessRuleException
   */
  public void modifySelectedUnitPrice(ArmCurrency newPrice)
      throws BusinessRuleException {
    POSLineItem line = getSelectedLineItem();
//price override not possible for returnlineitem in US
    if ("US".equalsIgnoreCase(Version.CURRENT_REGION)){
    	if((line instanceof ReturnLineItem) == false){
    		line.setManualUnitPrice(newPrice);
    	}
    }else{
    	line.setManualUnitPrice(newPrice);
    }
    //commented this code to modify only selected item's unit price
    //        if(line != null)
    //        {
    //            com.chelseasystems.cr.item.Item item = line.getItem();
    //
    //            this.undoBufferPrice = line.getManualUnitPrice();
    //            line.adjustUnitPrice(newPrice);
    //
    //            this.repaint();
    //        }
    this.repaint();
  }

  /**
   *
   * @param newPrice Currency
   * @throws BusinessRuleException
   */
  public void modifySelectedPriceOverride(ArmCurrency newPrice)
      throws BusinessRuleException {
    POSLineItem line = getSelectedLineItem();
    line.setItemPriceOverride(newPrice);
    //commented this code to modify only selected item's unit price
    //        if(line != null)
    //        {
    //            com.chelseasystems.cr.item.Item item = line.getItem();
    //
    //            this.undoBufferPrice = line.getManualUnitPrice();
    //            line.adjustUnitPrice(newPrice);
    //
    //            this.repaint();
    //        }
    this.repaint();
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
    POSLineItem line = getSelectedLineItem();
    if (line != null) {
      this.undoBufferMarkdown = line.getManualMarkdownAmount();
      line.setManualMarkdownAmount(markdownAmount);
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
        this.modifySelectedMarkdown(this.undoBufferMarkdown);
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
    POSLineItem line = getSelectedLineItem();
    if (line != null) {
      // Note: cant' get the current percent because the line item doesn't
      // store it. So, we use the markdown amount.
      this.undoBufferMarkdownPercent = line.getManualMarkdownAmount();
      line.setManualMarkdownPercent(new Double(amount * .01));
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
        this.modifySelectedMarkdown(this.undoBufferMarkdownPercent);
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
    int rowCount = tblList.getRowCount();
    if (rowCount < 1) {
      // why do we do this?  the table is showing zero rows.
      model.prevPage();
      rowCount = tblList.getRowCount();
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
    model.addLineItem(line);
    selectLastRow();
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
    model.removeRowInModel(line);
    model.fireTableDataChanged();
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
      this.setFocusable(false);
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
//      this.transferFocus();
      if (isSelected) {
        setForeground(Color.white);
        // determine if line item is return
        LineItemPOSModel itemModel = (LineItemPOSModel)table.getModel();
        if (itemModel.isSale(row)) {
          setBackground(new Color(0, 0, 128));
          this.setSelectionColor(new Color(0, 0, 128));
        } else if (itemModel.isLayaway(row))
          setBackground(new Color(0, 132, 121));
        else if (itemModel.isReservationOpen(row))
          setBackground(clrReservations);
        // IF its PResale Open Row - MSB
        else if (itemModel.isPreSaleOpen(row))
          setBackground(new Color(0, 128, 255));
        else if (itemModel.isConsignmentIn(row))
          setBackground(new Color(0, 140, 255));
        else
          setBackground(Color.red);
      } else {
        setBackground(DefaultBackground);
        // determine if line item is return
        LineItemPOSModel itemModel = (LineItemPOSModel)table.getModel();
        if (itemModel.isSale(row))
          setForeground(DefaultForeground);
        else if (itemModel.isLayaway(row))
          setForeground(new Color(0, 132, 121));
        // IF its PResale Open Row - MSB
        else if (itemModel.isReservationOpen(row))
          setForeground(clrReservations);
        else if (itemModel.isPreSaleOpen(row))
          setForeground(new Color(0, 128, 255));
        else if (itemModel.isConsignmentIn(row))
          setForeground(new Color(0, 140, 255));
        else
          setForeground(Color.red);
      }
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
      // determine if line item is return
      switch (col) {
        case ITEM_CODE:
        case ITEM_DESCRIPTION:
        case ASSOCIATE_ID:
          setHorizontalAlignment(JLabel.LEFT);
          break;
        case QUANTITY:
          setHorizontalAlignment(JLabel.CENTER);
          break;
        case UNIT_PRICE:
        case MARKDOWN:
        case AMOUNT_DUE:
          setHorizontalAlignment(JLabel.RIGHT);
          break;
      }
      if (isSelected) {
        setForeground(Color.white);
        // determine if line item is return
        LineItemPOSModel itemModel = (LineItemPOSModel)table.getModel();
        if (itemModel.isSale(row))
          setBackground(new Color(0, 0, 128));
        else if (itemModel.isLayaway(row))
          setBackground(new Color(0, 132, 121));
        else if (itemModel.isReservationOpen(row))
          setBackground(clrReservations);
        // IF its PResale Open Row - MSB
        else if (itemModel.isPreSaleOpen(row))
          setBackground(new Color(0, 128, 255));
        else if (itemModel.isConsignmentIn(row))
          setBackground(new Color(0, 140, 255));
        else
          setBackground(Color.red);
      } else {
        setBackground(DefaultBackground);
        // determine if line item is return
        LineItemPOSModel itemModel = (LineItemPOSModel)table.getModel();
        if (itemModel.isSale(row))
          setForeground(DefaultForeground);
        else if (itemModel.isLayaway(row))
          setForeground(new Color(0, 132, 121));
        // IF its PResale Open Row - MSB
        else if (itemModel.isPreSaleOpen(row))
          setForeground(new Color(0, 128, 255));
        else if (itemModel.isConsignmentIn(row))
          setForeground(new Color(0, 140, 255));
        else if (itemModel.isReservationOpen(row))
          setForeground(clrReservations);
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
    TableColumn ItemCol = tblList.getColumnModel().getColumn(ITEM_CODE);
    ItemCol.setPreferredWidth((int)(125 * r));
    TableColumn AsscIDCol = tblList.getColumnModel().getColumn(ASSOCIATE_ID);
    if("JP".equalsIgnoreCase(Version.CURRENT_REGION)){
      AsscIDCol.setPreferredWidth((int)(145 * r));
    } else{
    AsscIDCol.setPreferredWidth((int)(105 * r));
    }
    TableColumn QtyCol = tblList.getColumnModel().getColumn(QUANTITY);
    QtyCol.setPreferredWidth((int)(25 * r));
    TableColumn UnitPriceCol = tblList.getColumnModel().getColumn(UNIT_PRICE);
    UnitPriceCol.setPreferredWidth((int)(105 * r));
    TableColumn MarkdownCol = tblList.getColumnModel().getColumn(MARKDOWN);
    MarkdownCol.setPreferredWidth((int)(115 * r));
    TableColumn AmtDueCol = tblList.getColumnModel().getColumn(AMOUNT_DUE);
    AmtDueCol.setPreferredWidth((int)(100 * r));
    TableColumn DescriptionCol = tblList.getColumnModel().getColumn(ITEM_DESCRIPTION);
    DescriptionCol.setPreferredWidth(tblList.getWidth()
        - (ItemCol.getPreferredWidth() + AsscIDCol.getPreferredWidth() + +QtyCol.getPreferredWidth()
        + UnitPriceCol.getPreferredWidth() + AmtDueCol.getPreferredWidth()
        + MarkdownCol.getPreferredWidth()));
    model.setRowsShown(tblList.getHeight() / tblList.getRowHeight());
  }  
  
  //Added for Dolci Candy 
  public void modifyDescription(String key){
	  try{
	   POSLineItem line = getSelectedLineItem();
	   if(line!=null){
		   String itemDesc = new String(line.getItem().getDescription());
		   itemDesc = itemDesc+" - "+key;		   
		   line.getItem().setDescription(itemDesc);		
	   }
	  }catch(Exception e){
		  e.printStackTrace();
	  }
  }
  //Ended for Dolci Candy

}

