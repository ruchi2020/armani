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
 | 12   | 07-15-2005 | Vikram    | 510       | Added label "Altered Item" to Returns also   |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 11   | 07-11-2005 | Vikram    | 436       | Add display label "SHIP" to Item description.|
 +------+------------+-----------+-----------+----------------------------------------------+
 | 10   | 07-08-2005 | Vikram    | 406       | Display Barcode instead of SKU               |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 9    | 06-16-2005 | Vikram    |   249     | Percentage is not correct in Mkdn/Disc column|
 |      |            |           |           | when qty is more than 1.                     |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 8    | 06-14-2005 | Vikram    |   164     | For price override with 0 price, mkdn/Disc   |
 |      |            |           |           | shows some junk special char in square shape |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 7    | 05-27-2005 | Samina    |    77     |  Item Percent markdown/Discount displayed    |
 |      |            |           |           |  should be rounded to 1 significant figures. |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 6    | 04-17-2005 | Pankaja   | N/A       |1.Consignment Open Specification              |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 5    | 04-14-2005 | Khyati    | N/A       |1.Consignment Open Specification              |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 4    | 04-13-2005 | Manpreet  | N/A       |1.PreSale Open Specification                  |
 --------------------------------------------------------------------------------------------
 | 3    | 02-17-2005 | Manpreet  | N/A       | Modified  to display Markdown amount  and    |
 |      |            |           |           |   -- markdown percentage                     |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 02-02-2005 | Manpreet  | N/A       | Modified  to add associate id column and     |
 |      |            |           |           |   -- display consulant for the current line  |
 +------+------------+-----------+-----------+----------------------------------------------+
 */


package com.chelseasystems.cs.swing.model;

import java.util.Vector;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.chelseasystems.cr.currency.*;
import java.text.NumberFormat;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.util.Version;


/**
 */
public class LineItemPOSModel extends ScrollableTableModel {
  // fields that make up lineItemPOS
  private final int ITEM_CODE = 0;
  private final int ITEM_DESCRIPTION = 1;
  private final int ASSOCIATE_ID = 2;
  private final int QUANTITY = 3;
  private final int UNIT_PRICE = 4;
  private final int MARKDOWN = 5;
  private final int AMOUNT_DUE = 6;
  java.util.ResourceBundle res;
  /**
   *
   */
  private final String COLUMN_NAMES[] = {"SKU", "Item Description", "Assoc.", "Q.", "Unit Price"
      , "Mkdn/Disc", "Amount Due"
  };

  /**
   */
  public LineItemPOSModel() {
    res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    /*
     --- Removed Hard coding of column names, instead use COLUMN_NAMES array.
     --- Manpreet S Bawa (02/01/05)
     this.setColumnIdentifiers( new String[] {
     res.getString("Item Code"), res.getString("Item Description"),
     res.getString("AssociateId"),
     res.getString("Quantity"),
     res.getString("Unit Price"), res.getString("Markdown"), res.getString("Amount Due")
     });
     ;*/
    String sIdentiFiers[] = new String[COLUMN_NAMES.length];
    for (int iCtr = 0; iCtr < COLUMN_NAMES.length; iCtr++) {
      sIdentiFiers[iCtr] = res.getString(COLUMN_NAMES[iCtr]);
    }
    this.setColumnIdentifiers(sIdentiFiers);
  }

  /**
   * @param item
   */
  public void addLineItem(POSLineItem item) {
    // item.
    //System.out.println(" Line Item POS MODEL ");
    // CMSSaleLineItem  cmsItem = ((CMSSaleLineItem)item);
    //System.out.println(cmsItem.getAdditionalConsultant());
    addRow(item);
    if (getTotalRowCount() > getRowsShown()) {
      lastPage();
    }
  }

  /**
   * @param row
   * @return
   */
  public boolean isSale(int row) {
    POSLineItem line = (POSLineItem)getRowInPage(row);
    return (line instanceof SaleLineItem);
  }

  /**
   * @param row
   * @return
   */
  public boolean isLayaway(int row) {
    POSLineItem line = (POSLineItem)getRowInPage(row);
    return (line instanceof LayawayLineItem);
  }

  // IF its PResale Open Row - MSB
  public boolean isPreSaleOpen(int row) {
    POSLineItem line = (POSLineItem)getRowInPage(row);
    return (line instanceof CMSPresaleLineItem);
  }

  /**
   * put your documentation comment here
   * @param row
   * @return
   */
  public boolean isReservationOpen(int row) {
    POSLineItem line = (POSLineItem)getRowInPage(row);
    return (line instanceof CMSReservationLineItem);
  }

  // IF its Consignment Open Row - MSB
  public boolean isConsignmentIn(int row) {
    POSLineItem line = (POSLineItem)getRowInPage(row);
    return (line instanceof CMSConsignmentLineItem);
  }

  /**
   * @param row
   */
  public void deleteLineItem(int row) {
    removeRowInPage(row);
    this.fireTableRowsDeleted(row, row);
  }

  /**
   * @param row
   * @return
   */
  public POSLineItem getLineItem(int row) {
    // this.
    return (POSLineItem)this.getRowInPage(row);
  }

  /**
   * @return
   */
  public int getColumnCount() {
    /*
     -- Removed returning hard coded value.
     -- Manpreet S Bawa (02/01/05)
     //  return(7);
     */
    return COLUMN_NAMES.length;
  }

  /**
   * put your documentation comment here
   */
  public void setCheck() {}

  /**
   * @param row
   * @param column
   * @return
   */
  public boolean isCellEditable(int row, int column) {
    return (false);
  }

  /**
   * @param row
   * @param column
   * @return
   */
  public Object getValueAt(int row, int column) {
    Vector vTemp = this.getCurrentPage();
    POSLineItem line = (POSLineItem)vTemp.elementAt(row);
    //        System.out.println((line instanceof CMSPresaleLineItem));
    switch (column) {
      case ITEM_CODE:
        return (getItemID(line));
      case ITEM_DESCRIPTION:

        //Manpreet S Bawa - 04/27/2005  -- See if item as alterations associated
        String value = "\n " + line.getItemDescription();
        if (line instanceof CMSSaleLineItem || line instanceof CMSPresaleLineItem
            || (line instanceof ReturnLineItem && ((ReturnLineItem)line).getSaleLineItem() != null)) {
          POSLineItem saleLineItem = null;
          if (line instanceof ReturnLineItem
              && ((ReturnLineItem)line).getSaleLineItem() instanceof CMSSaleLineItem)
            saleLineItem = (CMSSaleLineItem)((ReturnLineItem)line).getSaleLineItem();
          else if (line instanceof CMSPresaleLineItem) {
            saleLineItem = (CMSPresaleLineItem)line;
          } else {
            saleLineItem = (CMSSaleLineItem)line;
          }
          if (hasAlteration(saleLineItem)) {
            value += "\n " + res.getString("Altered Item");
            if (line instanceof CMSPresaleLineItem && ((CMSPresaleLineItem)line).hasShippingRequest())
              value += res.getString(", Ship");
            else if (line instanceof CMSSaleLineItem && ((CMSSaleLineItem)line).hasShippingRequest())
              value += res.getString(", Ship");
          } else if (line instanceof CMSPresaleLineItem
              && ((CMSPresaleLineItem)line).hasShippingRequest())
            value += res.getString(", Ship");
          else if (line instanceof CMSSaleLineItem && ((CMSSaleLineItem)line).hasShippingRequest())
            value += "\n " + res.getString("Ship");
        }
        return value;
      case ASSOCIATE_ID:

        // Manpreet S Bawa -- 02/03/05 -- Consultant for current line item
        if (line.getAdditionalConsultant() != null){
        	if("JP".equalsIgnoreCase(Version.CURRENT_REGION)){
            	return line.getAdditionalConsultant().getLastName() + " "+ line.getAdditionalConsultant().getFirstName();
        	}else{
          return line.getAdditionalConsultant().getShortName();
        	}
        }
        else
          return null;
      case QUANTITY:
        if (line instanceof ReturnLineItem) {
          return (line.getQuantity().intValue() * -1 + "");
        }
        return (line.getQuantity() + "");
      case UNIT_PRICE:
        if (line instanceof ReturnLineItem)
          return (line.getItemRetailPrice().formattedStringValue()); //original price it was sold at
        else
          return (line.getItemRetailPrice().formattedStringValue());
        //return  line.getItem().getRetailPrice().formattedStringValue();
      case MARKDOWN:

        //---Begin---
        // Manpreet S Bawa -- 02/04/05 -- Display MarkdownAmt/Percentage
        //---
        // Presale doesnt have no markdowns/discount to be displayed
        // MS -09/26/05
        // Display Zero markdown in locale specific format
        // instead of hardcoded '$'
        String sZeroAmount = new ArmCurrency(0.0d).formattedStringValue();
//        if (line instanceof CMSPresaleLineItem || line instanceof CMSReservationLineItem || line instanceof CMSNoSaleLineItem)
        if (line instanceof CMSReservationLineItem || line instanceof CMSNoSaleLineItem)
          //                return "$0.00 (0.00%) ";
          return sZeroAmount + " (0.00%) ";

        //ks: Consigngment doesnt have no markdowns/discount to be displayed
        if (line instanceof CMSConsignmentLineItem)
          return sZeroAmount + " (0.00%) ";
        if (line instanceof CMSReturnLineItem) {
          if (((CMSReturnLineItem)line).getPresaleLineItem() != null
              || ((CMSReturnLineItem)line).getConsignmentLineItem() != null
              || ((CMSReturnLineItem)line).getReservationLineItem() != null ) {
            return sZeroAmount + " (0.00%) ";
          }
        }
        double dReductionAmount, dSellingPrice, dNetDiscount;
        int iLineItemQuantity;
        String sMkdNPercent = new String();
        ArmCurrency totalMarkDown = new ArmCurrency(0.0);
        if (line instanceof CMSPresaleLineItem){
          dSellingPrice = line.getExtendedRetailAmount().doubleValue();
          dReductionAmount = ((CMSPresaleLineItem)line).getExtendedReductionAmount().doubleValue();
          iLineItemQuantity = line.getQuantity().intValue();
          dNetDiscount = (dReductionAmount / dSellingPrice) / iLineItemQuantity;
          try {
            totalMarkDown = ((CMSPresaleLineItem)line).getExtendedReductionAmount();
          }catch (Exception ex){
            System.out.println(ex.getMessage());
          }
        }
        else {
        dSellingPrice = line.getItemSellingPrice().doubleValue();
        dReductionAmount = line.getExtendedReductionAmount().doubleValue();
        iLineItemQuantity = line.getQuantity().intValue();
        dNetDiscount = (dReductionAmount / dSellingPrice) / iLineItemQuantity;
         try {
           totalMarkDown = line.getExtendedRetailAmount().subtract(line.
              getExtendedNetAmount());
         }catch (CurrencyException ex){
           System.out.println(ex.getMessage());
         }

        }

        // If dNetDiscount is not number or infinite
        if (Double.isNaN(dNetDiscount) || Double.isInfinite(dNetDiscount))
          dNetDiscount = 0.00;

        // Trim discount to 2 decimal points.
        // Integer digits cant be more than 2.
        sMkdNPercent = String.valueOf(dNetDiscount);

        // Check if length of integer digits is 2
        // Total length would be more than 3
        try {

//          ArmCurrency totalMarkDown = line.getExtendedRetailAmount().subtract(line.
//              getExtendedNetAmount());
          double d = 0.00;
          if (line.getItemRetailPrice().doubleValue() != 0.00)
            d = totalMarkDown.doubleValue() / line.getItemRetailPrice().doubleValue()
                / iLineItemQuantity;
          NumberFormat nf = NumberFormat.getPercentInstance();
          nf.setMaximumFractionDigits(1);
          sMkdNPercent = totalMarkDown.formattedStringValue() + " (" + nf.format(d) + ")"; ;
        } catch (Exception ex) {
          System.out.println(ex.getMessage());
        }
        return sMkdNPercent;
        //- ENd Modifications - Bawa-
        // --- Base Code ----
        //return(line.getExtendedReductionAmount().formattedStringValue());
        //if (line.isDiscountGreaterThanMarkdown())
        //   return line.getDiscountAmount().formattedStringValue();
        //else
        //   return line.getTotalMarkdownAmount().formattedStringValue();
      case AMOUNT_DUE:
        return (line.getExtendedNetAmount().formattedStringValue());
      default:
        return (" ");
    }
  }

  /**
   * @param line
   * @return
   */
  private String getItemID(POSLineItem line) {
    if (((CMSItem)line.getItem()).getBarCode() != null)
      return " " + ((CMSItem)line.getItem()).getBarCode();
    else
      return " ";
  }

  /**
   * put your documentation comment here
   * @param RowsShown
   */
  public void setRowsShown(int RowsShown) {
    //System.out.println("setting rows shown: " + RowsShown);
    super.setRowsShown(RowsShown);
  }

  /**
   * Check if the sale line item has alteration
   * Used to display "Altered" label.
   * @param cmsSaleLineItem CMSSaleLineItem
   * @return boolean
   */
  private boolean hasAlteration(POSLineItem lineItem) {
    POSLineItemDetail details[] = lineItem.getLineItemDetailsArray();
    if (details == null)
      return false;
    for (int iCtr = 0; iCtr < details.length; iCtr++) {
      AlterationLineItemDetail altDets[] = null;
      if (details[iCtr] instanceof CMSPresaleLineItemDetail) {
        altDets = ((CMSPresaleLineItemDetail)details[iCtr]).getAlterationLineItemDetailArray();
      } else if (details[iCtr] instanceof CMSSaleLineItemDetail) {
        altDets = ((CMSSaleLineItemDetail)details[iCtr]).getAlterationLineItemDetailArray();
      }
      //        CMSSaleLineItemDetail det  = (CMSSaleLineItemDetail) details[iCtr];
      //        if(det.getAlterationLineItemDetailArray() != null
      //           &&
      //           det.getAlterationLineItemDetailArray().length>0
      //           ) return true;
      if (altDets != null && altDets.length > 0)
        return true;
      else
        continue;
    }
    return false;
  }
}

