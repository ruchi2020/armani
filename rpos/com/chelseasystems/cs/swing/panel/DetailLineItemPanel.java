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
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cs.swing.model.DetailLineItemModel;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cs.pos.CMSSaleLineItemDetail;
import com.chelseasystems.cr.swing.ScrollProcessor;
import com.chelseasystems.cs.pos.CMSSaleLineItem;
import com.chelseasystems.cs.pos.CMSReturnLineItem;


/**
 */
public class DetailLineItemPanel extends JPanel implements PageNumberGetter, ScrollProcessor  {
  DetailLineItemModel model = new DetailLineItemModel();
  JCMSTable tblList = new JCMSTable(model, JCMSTable.SELECT_ROW);
  TextRenderer renderer = new TextRenderer();

  /**
   * Default Constructor
   */
  public DetailLineItemPanel() {
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
    this.add(tblList.getTableHeader(), BorderLayout.NORTH);
    this.add(tblList, BorderLayout.CENTER);
    model.setRowsShown(13); // arbitrarily set until resize occurs
    TableColumnModel modelColumn = tblList.getColumnModel();
    for (int i = 0; i < model.getColumnCount(); i++)
      modelColumn.getColumn(i).setCellRenderer(renderer);
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
   * @param qty
   * @return
   */
  public void modifySelectedLineConsultant(Employee emp)
      throws BusinessRuleException {
    POSLineItemDetail line = getSelectedLineItem();
    if (line != null) {
        if (line.getLineItem() instanceof CMSSaleLineItem)	
        	((CMSSaleLineItem)line.getLineItem()).setOldConsultant(((CMSSaleLineItem)line.getLineItem()).getAdditionalConsultant());
          else if (line.getLineItem() instanceof CMSReturnLineItem)
          	((CMSReturnLineItem)line.getLineItem()).setOldConsultant(((CMSReturnLineItem)line.getLineItem()).getAdditionalConsultant());    	  
      line.getLineItem().setAdditionalConsultant(emp);
    }
    tblList.repaint();
  }

  /**
   * @param qty
   * @return
   */
  public void removeSelectedLineConsultant()
      throws BusinessRuleException {
    POSLineItemDetail line = getSelectedLineItem();
    Employee emp = line.getLineItem().getAdditionalConsultant();
    if (emp != null) {
	    if (line.getLineItem() instanceof CMSSaleLineItem)	
	    	((CMSSaleLineItem)line.getLineItem()).setOldConsultant(((CMSSaleLineItem)line.getLineItem()).getAdditionalConsultant());
	      else if (line.getLineItem() instanceof CMSReturnLineItem)
	      	((CMSReturnLineItem)line.getLineItem()).setOldConsultant(((CMSReturnLineItem)line.getLineItem()).getAdditionalConsultant());    	  
      line.getLineItem().removeAdditionalConsultant(emp);
    }
    tblList.repaint();
  }

  /**
   * @param qty
   * @return
   */
  public void modifySelectedLineTax(ArmCurrency tax, boolean isRegional)
      throws BusinessRuleException {
    POSLineItemDetail line = getSelectedLineItem();
    if (line != null) {
      if (isRegional) {
        line.setManualRegionalTaxAmount(tax);
      } else {
        line.clearManualTaxAmount();
        line.setManualTaxAmount(tax);
      }
    }
    tblList.repaint();
  }

  /**
   * @param qty
   * @return
   */
  public void modifySelectedLineTax(ArmCurrency tax, double taxRate, boolean isRegional)
      throws BusinessRuleException {
    POSLineItemDetail line = getSelectedLineItem();
    if (line != null) {
      if (isRegional) {
        line.setManualRegionalTaxAmount(tax);
      } else {
        line.setManualTaxAmount(tax);
        if (line instanceof CMSSaleLineItemDetail)
          ((CMSSaleLineItemDetail)line).setManualTaxRate(taxRate);
      }
    }
    tblList.repaint();
  }

  /**
   * @param qty
   * @return
   */
  public void removeManualTax(boolean isRegional)
      throws BusinessRuleException {
    POSLineItemDetail line = getSelectedLineItem();
    if (line != null) {
      if (isRegional) {
        line.clearManualRegionalTaxAmount();
      } else {
        line.clearManualTaxAmount();
      }
    }
    tblList.repaint();
  }

  /**
   */
  public DetailLineItemModel getModel() {
    return this.model;
  }

  /**
   */
  public JCMSTable getTable() {
    return this.tblList;
  }

  /**
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
    return model.getCurrentPageNumber();
  }

  public int getPageCount() {
	    return model.getPageCount();
  }
  
  /**
   * @return
   */
  public int getTotalPages() {
    return model.getPageCount();
  }

  /**
   * @param line
   */
  public void addLineItem(POSLineItem line) {
    POSLineItemDetail[] lineDetails = line.getLineItemDetailsArray();
    for (int i = 0; i < lineDetails.length; i++) {
      //System.out.println("Adding one line item detail" + lineDetails[i].getLineItem().getItem().getId());
      model.addLineItem(lineDetails[i]);
    }
  }

  /**
   * @param row
   * @return
   */
  public POSLineItemDetail getLineItem(int row) {
    return model.getLineItem(row);
  }

  /**
   * @return
   */
  public POSLineItemDetail getSelectedLineItem() {
    int row = tblList.getSelectedRow();
    if (row < 0)
      return null;
    return model.getLineItem(row);
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
        case DetailLineItemModel.ID:
          setHorizontalAlignment(JLabel.LEFT);
          break;
        case DetailLineItemModel.DESC:
          setHorizontalAlignment(JLabel.LEFT);
          break;
        case DetailLineItemModel.QTY:
          setHorizontalAlignment(JLabel.CENTER);
          break;
        case DetailLineItemModel.SALESTAX:
          setHorizontalAlignment(JLabel.LEFT);

          // required for when tax displays on two lines.
          com.chelseasystems.cr.util.HTML.formatLabeltoHTML(this);
          break;
        case DetailLineItemModel.CONSULTANT:
          setHorizontalAlignment(JLabel.RIGHT);
          break;
      }
      if (isSelected) {
        setForeground(Color.white);
        // determine if line item is return
        DetailLineItemModel itemModel = (DetailLineItemModel)table.getModel();
        if (itemModel.isSale(row))
          setBackground(new Color(0, 0, 128));
        else if (itemModel.isLayaway(row))
          setBackground(new Color(0, 132, 121));
        else
          setBackground(Color.red);
      } else {
        setBackground(DefaultBackground);
        // determine if line item is return
        DetailLineItemModel itemModel = (DetailLineItemModel)table.getModel();
        if (itemModel.isSale(row))
          setForeground(DefaultForeground);
        else if (itemModel.isLayaway(row))
          setForeground(new Color(0, 132, 121));
        else
          setForeground(Color.red);
      }
      return this;
    }
  }


  /**
   * @param e
   */
  public void resetColumnWidths() {
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    TableColumn ItemCol = tblList.getColumnModel().getColumn(0);
    ItemCol.setPreferredWidth((int)(125 * r));
    TableColumn ConsCol = tblList.getColumnModel().getColumn(4);
    ConsCol.setPreferredWidth((int)(200 * r));
    TableColumn qtyCol = tblList.getColumnModel().getColumn(2);
    qtyCol.setPreferredWidth((int)(75 * r));
    TableColumn taxCol = tblList.getColumnModel().getColumn(3);
    taxCol.setPreferredWidth((int)(165 * r));
    TableColumn DescriptionCol = tblList.getColumnModel().getColumn(1);
    DescriptionCol.setPreferredWidth(tblList.getWidth()
        - (ItemCol.getPreferredWidth() + ConsCol.getPreferredWidth() + qtyCol.getPreferredWidth()
        + taxCol.getPreferredWidth()));
    model.setRowsShown(tblList.getHeight() / tblList.getRowHeight());
  }
}

