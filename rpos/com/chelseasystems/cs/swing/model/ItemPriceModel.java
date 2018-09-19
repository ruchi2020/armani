/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.model;

import java.util.*;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.util.ResourceManager;
import com.chelseasystems.cs.swing.item.ItemDetailApplet;
import com.chelseasystems.cs.util.LineItemPOSUtil;
import com.chelseasystems.cs.pos.*;
import java.text.NumberFormat;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.store.CMSStore;


/**
 *
 * <p>Title: ItemPriceModel</p>
 *
 * <p>Description:Model for Item pricing information </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company:SkillNet Inc. </p>
 *
 *
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 4    | 07-12-2005 | Vikram    | 552       | Tax % should show upto 3 decimal digits      |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 3    | 06-17-2005 | Vikram    | N/A       | % not correctly displayed for quantity > 1   |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 05-01-2005 | Anand     | N/A       | Changes related to displaying reduction %    |
 --------------------------------------------------------------------------------------------
 | 1    | 02-11-2005 | Manpreet  | N/A       | Model for Item pricing information           |
 +------+------------+-----------+-----------+----------------------------------------------+
 */
public class ItemPriceModel extends ScrollableTableModel {
  private int ROW_RETAIL = 0;
  private int ROW_PROMO_MKDN = 1;
  private int ROW_DISCOUNT = 2;
  private int ROW_TOT_REDUCTION = 3;
  private int ROW_SELLING_PRICE = 4;
  private int ROW_TAX = 5;
  private final int PRICE_DESCRIPTION = 0;
  private final int REDUCTION_PERCENTAGE = 1;
  private final int REDUCTION_AMOUNT = 2;
  private boolean vatEnabled=false;

  /**
   *  Stores column Names.
   */
  private final String COLUMN_NAMES[] = {"Price Description", "Reduction%", "Reduction Amount"
  };
  private POSLineItem posLineItem = null;
  /**
   * Stores data
   */
  private String[][] arrayData = null;
  private ResourceBundle resource = null;
  /**
   *  Stores Labels for the rows shown in table.
   */
  private String ROW_LABELS[] = {"Retail Price", "Promotional Markdown", "Discounts"
      , "Total Price Reduction", "Selling Price", "Tax", "Taxable", "VAT Rate", "VAT Code"
  };

  /**
   * put your documentation comment here
   */
  public ItemPriceModel() {
    int iCtr = 0;
    this.resource = null;
    resource = ResourceManager.getResourceBundle();
    String sIdentiFiers[] = new String[COLUMN_NAMES.length];
    // Get row labels
    for (iCtr = 0; iCtr < ROW_LABELS.length; iCtr++) {
      ROW_LABELS[iCtr] = resource.getString(ROW_LABELS[iCtr]);
    }
    this.setColumnIdentifiers(sIdentiFiers);
    // Get Column names
    for (iCtr = 0; iCtr < COLUMN_NAMES.length; iCtr++) {
      sIdentiFiers[iCtr] = resource.getString(COLUMN_NAMES[iCtr]);
    }
    CMSStore store = (CMSStore)CMSApplet.theAppMgr.getGlobalObject("STORE");
    vatEnabled = store.isVATEnabled();
  }

  /**
   * put your documentation comment here
   * @param posLineItem
   */
  public void setPriceModel(POSLineItem posLineItem) {
    this.arrayData = null;
    this.clear();
    //Check for calling applet.
    // Rows that arent supposed to be shown will be = -1
    if (ItemDetailApplet.bItemLookUpApplet) {
      ROW_PROMO_MKDN = -1;
      ROW_DISCOUNT = -1;
      ROW_TOT_REDUCTION = -1;
      ROW_SELLING_PRICE = -1;
      ROW_TAX = -1;
    }
    this.posLineItem = null;
    this.posLineItem = posLineItem;
    buildData(this.posLineItem);
    fireTableDataChanged();
  }

  /**
   *
   * @param iRow Row Number
   * @param iColumn Column Number
   * @return Object
   */
  public Object getValueAt(int iRow, int iColumn) {
    if (arrayData == null || arrayData.length < 1 || iRow > arrayData.length)
      return null;
    return arrayData[iRow][iColumn];
  }

  /**
   *  Builds the required data
   *  and stores it into arrayData.
   */
  private void buildData(POSLineItem posItem) {
    int iRowCtr = 0;
    int iDiscCtr = 0;
    double dRetailPrice = 0.00;
    ROW_TOT_REDUCTION = 3;
    ROW_SELLING_PRICE = 4;
    ROW_TAX = 5;
    ArmCurrency curDiscounts[] = new ArmCurrency[0];
    ArmCurrency curPromoMkdn = null;
    String sRetailPrice, sTotalReduction, sSellingPrice, sTax,sVat = null;
    LineItemPOSUtil utilLineItem = new LineItemPOSUtil(posItem);
    sRetailPrice = posItem.getItemRetailPrice().formattedStringValue();
    dRetailPrice = posItem.getItemRetailPrice().doubleValue();
    //sTotalReduction= utilLineItem.getTotalReduction().formattedStringValue();
    sSellingPrice = posItem.getExtendedNetAmount().divide(posItem.getQuantity().intValue()).
        formattedStringValue();
    double dTotalReduction = 0.0d;
    double dTaxRate = 0.0d;
    try {
      dTotalReduction = posItem.getItemRetailPrice().subtract(posItem.getExtendedNetAmount().divide(
          posItem.getQuantity().intValue())).doubleValue();
      sTotalReduction = posItem.getItemRetailPrice().subtract(posItem.getExtendedNetAmount().divide(
          posItem.getQuantity().intValue())).formattedStringValue();
    } catch (Exception e) {
      sTotalReduction = "0.0";
    }
    curPromoMkdn = utilLineItem.getPromotionalMarkdown();
    curDiscounts = utilLineItem.getAvailableDiscounts();
    POSLineItemDetail posLineItemDetail = posItem.getLineItemDetailsArray()[0];
    dTaxRate = (posItem.getExtendedTaxAmount().getDoubleValue()
        / posItem.getExtendedNetAmount().getDoubleValue());
    dTaxRate = (double)Math.round(dTaxRate * 100000) / 1000;
    if (posItem.getTransaction().getCompositeTransaction().isTaxExempt()) {
      sTax = resource.getString("Exempt");
    }
    // Another condition applied, to prevent view txn thru inquiry option to come here.
    else {
      if (posLineItem instanceof CMSSaleLineItem && !posLineItemDetail.isManualTaxAmount()
          && (posLineItemDetail instanceof CMSSaleLineItemDetail
          && ((CMSSaleLineItemDetail)posLineItemDetail).isTaxRateDefined())) {
        dTaxRate = ((CMSSaleLineItemDetail)posLineItemDetail).getTaxRate();
        dTaxRate = ((double)Math.round(dTaxRate * 100000) * 100 / 100000);
      }
      sTax = posItem.getExtendedTaxAmount().divide(posItem.getQuantity().intValue()).
          //Issue # 709
          //formattedStringValue() + " (" + dTaxRate + "%)";
          formattedStringValue();
    }
    if (vatEnabled){
      sVat = posItem.getExtendedVatAmount().divide(posItem.getQuantity().intValue()).formattedStringValue();
      System.out.println("I m here:: sVat is "+sVat);
    }
    // Will store all the data
    // Length should be equal to no. of rows + no. of discounts present on line item.
    arrayData = new String[ROW_LABELS.length + curDiscounts.length - 1][COLUMN_NAMES.length];
    // Start storing data into array.
    for (iRowCtr = 0; iRowCtr < arrayData.length; iRowCtr++) {
      // Row label is constant.
      if (ROW_LABELS.length > iRowCtr)
        arrayData[iRowCtr][PRICE_DESCRIPTION] = ROW_LABELS[iRowCtr];
      if (iRowCtr == ROW_RETAIL) {
        arrayData[iRowCtr][REDUCTION_PERCENTAGE] = "";
        arrayData[iRowCtr][REDUCTION_AMOUNT] = sRetailPrice;
      }
      if (iRowCtr == ROW_PROMO_MKDN && curPromoMkdn != null) {
        arrayData[iRowCtr][REDUCTION_PERCENTAGE] = getPercentage(dRetailPrice
            , curPromoMkdn.doubleValue());
        arrayData[iRowCtr][REDUCTION_AMOUNT] = curPromoMkdn.formattedStringValue();
      }
      if (iRowCtr == ROW_DISCOUNT) {
        for (iDiscCtr = 0; iDiscCtr < curDiscounts.length; iDiscCtr++) {
          arrayData[iRowCtr][PRICE_DESCRIPTION] = utilLineItem.getDiscountReason(iDiscCtr);
          arrayData[iRowCtr][REDUCTION_PERCENTAGE] = utilLineItem.getDiscountPercent(iDiscCtr); //getPercentage(dRetailPrice,curDiscounts[iDiscCtr].doubleValue());
          arrayData[iRowCtr][REDUCTION_AMOUNT] = curDiscounts[iDiscCtr].formattedStringValue();
          iRowCtr++;
          //System.out.println("befre iRowCtr " + iRowCtr + " ROW_SELLING_PRICE " + ROW_SELLING_PRICE + " ROW_TAX " + ROW_TAX);
          //if (iRowCtr > ROW_TOT_REDUCTION)
          ROW_TOT_REDUCTION++;
          //  if (iRowCtr > ROW_SELLING_PRICE)
          ROW_SELLING_PRICE++;
          //  if (iRowCtr > ROW_TAX)
          ROW_TAX++;
          //System.out.println("iRowCtr " + iRowCtr + " ROW_SELLING_PRICE " + ROW_SELLING_PRICE + " ROW_TAX " + ROW_TAX);
        }
      }
      //System.out.println("ourtside iRowCtr " + iRowCtr + " ROW_SELLING_PRICE " + ROW_SELLING_PRICE + " ROW_TAX " + ROW_TAX);
      if (iRowCtr == ROW_TOT_REDUCTION) {
        //  if (curPromoMkdn != null && curDiscounts.length > 0)
        {
          //           System.out.println("getting totreduciont: "+ sTotalReduction);
          arrayData[iRowCtr][PRICE_DESCRIPTION] = ROW_LABELS[3];
          arrayData[iRowCtr][REDUCTION_PERCENTAGE] = getPercentage(dRetailPrice, dTotalReduction);
          arrayData[iRowCtr][REDUCTION_AMOUNT] = sTotalReduction;
        }
      }
      if (iRowCtr == ROW_SELLING_PRICE) {
        arrayData[iRowCtr][PRICE_DESCRIPTION] = ROW_LABELS[4];
        arrayData[iRowCtr][REDUCTION_PERCENTAGE] = "";
        arrayData[iRowCtr][REDUCTION_AMOUNT] = sSellingPrice;
        if (ROW_SELLING_PRICE == ROW_TAX)
          ROW_TAX++;
      }
      if (iRowCtr == ROW_TAX && !ItemDetailApplet.bItemLookUpApplet) {
        if (vatEnabled){
          arrayData[iRowCtr][PRICE_DESCRIPTION] = "Vat";
          arrayData[iRowCtr][REDUCTION_PERCENTAGE] = "";
          arrayData[iRowCtr][REDUCTION_AMOUNT] = sVat;
        }else {
          arrayData[iRowCtr][PRICE_DESCRIPTION] = ROW_LABELS[5];
          arrayData[iRowCtr][REDUCTION_PERCENTAGE] = "";
          arrayData[iRowCtr][REDUCTION_AMOUNT] = sTax;
        }
      }
    } // -- End Row Loop
    removeNullRows(); // Remove the rows that dont have value for Reduction_Amount. fe
  }

  /**
   *  Removes all the rows that are null.
   */
  private void removeNullRows() {
    int iLength = 0;
    int iCtr = 0;
    String sNewArray[][] = new String[arrayData.length][COLUMN_NAMES.length];
    // Loop through arrayData
    // look for rows with no data in REDUCTION_AMOUNT and REDUCTION_PERCENTAGE
    // Any such row wont be added into sNewArray.
    for (iCtr = 0; iCtr < arrayData.length; iCtr++) {
      if ((arrayData[iCtr][REDUCTION_AMOUNT] == null) && (arrayData[iCtr][REDUCTION_PERCENTAGE] == null))
        continue;
      else {
        sNewArray[iLength][PRICE_DESCRIPTION] = arrayData[iCtr][PRICE_DESCRIPTION];
        sNewArray[iLength][REDUCTION_PERCENTAGE] = arrayData[iCtr][REDUCTION_PERCENTAGE];
        sNewArray[iLength][REDUCTION_AMOUNT] = arrayData[iCtr][REDUCTION_AMOUNT];
        iLength++;
      }
    }
    arrayData = new String[iLength][COLUMN_NAMES.length];
    // Copy all valid rows into arrayData.
    // add data to model.
    for (iCtr = 0; iCtr < arrayData.length; iCtr++) {
      arrayData[iCtr][PRICE_DESCRIPTION] = sNewArray[iCtr][PRICE_DESCRIPTION];
      arrayData[iCtr][REDUCTION_PERCENTAGE] = sNewArray[iCtr][REDUCTION_PERCENTAGE];
      arrayData[iCtr][REDUCTION_AMOUNT] = sNewArray[iCtr][REDUCTION_AMOUNT];
      super.addRow(arrayData[iCtr]);
    }
  }

  /**
   *
   * @param dActualPrice Net Amount
   * @param dReducedAmount Reduced Amount
   * @return Percentage of reduced amount in "%" format.
   */
  public String getPercentage(double dActualPrice, double dReducedAmount) {
    double dPercentage;
    String sPercent = "";
    //   dPercentage = (dActualPrice/dReducedAmount);
    dPercentage = (dReducedAmount / dActualPrice);
    sPercent = Math.round(dPercentage * 100) + " %";
    return sPercent;
  }

  // double d = totalMarkDown.doubleValue()/line.getItemRetailPrice().doubleValue();
  //                sMkdNPercent = totalMarkDown.formattedStringValue() +  " (" + d * 100 + "%) ";;
  /**
   *
   * @return Total No. Of columns
   */
  public int getColumnCount() {
    return COLUMN_NAMES.length;
  }

  /**
   *
   * @param iColumn Column Index
   * @return Column Name
   */
  public String getColumnName(int iColumn) {
    return COLUMN_NAMES[iColumn];
  }
  //  /**
   //   *
   //   * @param posLineItem Currently selected POSLineItem
   //   */
  //  public ItemPriceModel(POSLineItem posLineItem) {
  //
  //    int iCtr;
  //    resource = ResourceManager.getResourceBundle();
  //    String sIdentiFiers [] = new String[COLUMN_NAMES.length];
  //
  //    //Check for calling applet.
  //    // Rows that arent supposed to be shown will be = -1
  //    if(!ItemDetailApplet.bItemLookUpApplet) {
  //
  //      ROW_TAXABLE = -1;
  //    }
  //    else if(ItemDetailApplet.bItemLookUpApplet)
  //    {
  //      ROW_PROMO_MKDN = -1;
  //      ROW_DISCOUNT = -1;
  //      ROW_TOT_REDUCTION = -1;
  //      ROW_SELLING_PRICE = -1;
  //      ROW_TAX = -1;
  //    }
  //
  //    // Get Column names
  //    for(iCtr=0; iCtr < COLUMN_NAMES.length; iCtr++)
  //    {
  //        sIdentiFiers[iCtr] = resource.getString(COLUMN_NAMES[iCtr]);
  //    }
  //
  //    // Get row labels
  //    for(iCtr=0; iCtr  < ROW_LABELS.length; iCtr++)
  //    {
  //      ROW_LABELS[iCtr] = resource.getString(ROW_LABELS[iCtr]);
  //    }
  //    this.setColumnIdentifiers(sIdentiFiers);
  //    this.posLineItem = posLineItem;
  //
  //    buildData();
  //
  //  }
}

