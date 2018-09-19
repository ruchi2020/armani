/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.model;

import java.util.Vector;
import javax.swing.event.TableModelEvent;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.pos.*;
import java.text.NumberFormat;
import com.chelseasystems.cr.config.ConfigMgr;


/**
 */
public class DetailLineItemModel extends ScrollableTableModel {
  java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
  // fields that make up lineItemPOS
  public final static int ID = 0;
  public final static int DESC = 1;
  public final static int QTY = 2;
  public final static int SALESTAX = 3;
  public final static int CONSULTANT = 4;
  private boolean vatEnabled=false;

  /**
   */
  public DetailLineItemModel() {
    ConfigMgr storeConfig = new ConfigMgr("store.cfg");
    String strVatEnabled = storeConfig.getString("VAT_ENABLED");
    if (strVatEnabled != null && strVatEnabled.equalsIgnoreCase("TRUE")){
      vatEnabled=true;
    }
    if (vatEnabled){
      this.setColumnIdentifiers(new String[] {res.getString("Item Code")
          , res.getString("Item Description"), res.getString("Qty"), res.getString("VAT by Item")
          , res.getString("Additional Consultant")
    });
    }else{
      this.setColumnIdentifiers(new String[] {res.getString("Item Code")
          , res.getString("Item Description"), res.getString("Qty"), res.getString("Tax by Item")
          , res.getString("Additional Consultant")
    });
    }
  }

  /**
   * @param item
   */
  public void addLineItem(POSLineItemDetail item) {
    addRow(item);
    // if (getTotalRowCount() > getRowsShown()) {                // scroll down with rows
    //    nextPage();
    // }
  }

  /**
   * @param row
   * @return
   */
  public boolean isSale(int row) {
    POSLineItemDetail line = (POSLineItemDetail)getRowInPage(row);
    return line.getLineItem() instanceof SaleLineItem;
  }

  /**
   * @param row
   * @return
   */
  public boolean isLayaway(int row) {
    POSLineItemDetail line = (POSLineItemDetail)getRowInPage(row);
    return line.getLineItem() instanceof LayawayLineItem;
  }

  /**
   * @param row
   * @return
   */
  public POSLineItemDetail getLineItem(int row) {
    return (POSLineItemDetail)this.getRowInPage(row);
  }

  /**
   * @return
   */
  public int getColumnCount() {
    return 5;
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
    Vector vTemp = this.getCurrentPage();
    POSLineItemDetail line = (POSLineItemDetail)vTemp.elementAt(row);
    switch (column) {
      case ID:
        if (((CMSItem)(line.getLineItem().getItem())).getBarCode() != null)
          return " " + ((CMSItem)(line.getLineItem().getItem())).getBarCode();
        else
          return " ";
      case DESC:
        return line.getLineItem().getItemDescription();
      case CONSULTANT:
        if (line.getLineItem().getAdditionalConsultant() != null) {
          return line.getLineItem().getAdditionalConsultant().getFirstName() + " "
              + line.getLineItem().getAdditionalConsultant().getLastName();
        } else {
          return " ";
        }
      case QTY:
        return "1";
      case SALESTAX:
        if (vatEnabled){
          return getVatDisplay (line);
        }else {
          boolean useTwoLineDisplay = line.getLineItem().getTransaction().getCompositeTransaction().
              getStore().usesRegionalTaxCalculations();
          return (useTwoLineDisplay) ? getTwoLineTaxDisplay(line) : getTaxDisplay(line);
        }
      default:
        return " ";
    }
  }

  /**
   */
  private Object getTaxDisplay(POSLineItemDetail line) {
    double dTaxRate = 0.0d;
    NumberFormat nf = NumberFormat.getPercentInstance();
    nf.setMaximumFractionDigits(3);
    dTaxRate = (line.getTaxAmount().doubleValue() / line.getNetAmount().doubleValue());
    dTaxRate = (double)Math.round(dTaxRate * 100000) / 1000;
    if (line.isManualTaxAmount()) {
      if (line instanceof CMSSaleLineItemDetail && ((CMSSaleLineItemDetail)line).isManualTaxPercent()) {
        dTaxRate = ((CMSSaleLineItemDetail)line).getManualTaxRate();
        dTaxRate = ((double)Math.round(dTaxRate * 100000) / 100000);
      }
      return res.getString("Manual") + ": " + line.getManualTaxAmount().formattedStringValue()
          + " (" + dTaxRate + "%)";
    } else if (line.getLineItem().getTransaction().getCompositeTransaction().isTaxExempt()) {
      return res.getString("Exempt");
    } else {
      if (line.getTaxAmount() != null) {
        if (line.getLineItem().isItemTaxable()) {
          if (line instanceof CMSSaleLineItemDetail) {
            dTaxRate = ((CMSSaleLineItemDetail)line).getTaxRate();
            dTaxRate = ((double)Math.round(dTaxRate * 100000) * 100 / 100000);
          }
          return line.getTaxAmount().formattedStringValue() + " (" + dTaxRate + "%)";
        } else {
          return res.getString("Non-Taxable");
        }
      }
    }
    return res.getString("Not Calculated");
  }

  /**
   */
  private Object getRegionalTaxDisplay(POSLineItemDetail line) {
    if (line.getManualRegionalTaxAmount() != null) {
      return res.getString("Manual") + ": "
          + line.getManualRegionalTaxAmount().formattedStringValue();
    }
    if (line.getLineItem().getTransaction().getCompositeTransaction().isRegionalTaxExempt()) {
      return res.getString("Exempt");
    } else {
      if (line.getRegionalTaxAmount() != null) {
        if (line.getLineItem().isItemRegionalTaxable()) {
          return line.getRegionalTaxAmount().formattedStringValue();
        } else {
          return res.getString("Non-Taxable");
        }
      }
    }
    return res.getString("Not Calculated");
  }

  /**
   */
  private Object getTwoLineTaxDisplay(POSLineItemDetail line) {
    StringBuffer buff = new StringBuffer();
    buff.append("(");
    buff.append(res.getString(line.getLineItem().getTransaction().getCompositeTransaction().
        getStore().getTaxLabel()));
    buff.append(") ");
    buff.append(getTaxDisplay(line));
    buff.append("\n");
    buff.append("(");
    buff.append(res.getString(line.getLineItem().getTransaction().getCompositeTransaction().
        getStore().getRegionalTaxLabel()));
    buff.append(") ");
    buff.append(getRegionalTaxDisplay(line));
    return buff.toString();
  }
  /**
   */
  private Object getVatDisplay(POSLineItemDetail line) {
    double dVatRate = 0.0d;
    NumberFormat nf = NumberFormat.getPercentInstance();
    nf.setMaximumFractionDigits(3);
    return line.getVatAmount().formattedStringValue();
  }

}

