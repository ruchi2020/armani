/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.model;

import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyFormat;
import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.chelseasystems.cs.customer.CustomerSaleSummary;
import com.chelseasystems.cs.util.CustSaleSummaryUtil;
import com.chelseasystems.cs.util.HTMLColumnHeaderUtil;
import com.chelseasystems.cr.swing.CMSApplet;


/**
 * <p>Title:PurchaseHistoryModel.java </p>
 *
 * <p>Description: Model for Purchase History </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company:SkillNet Inc. </p>
 *
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class PurchaseHistoryModel extends ScrollableTableModel {
  private String COLUMN_NAMES[] = {"Txn Total", "1 Months \nto Date", "3 Months \nto Date"
      , "6 Months \nto Date", "Year \nto Date", "Life \nto Date($)"
  };
  private String ROW_LABELS[] = {"Sales ", "Returned ", "Net "
  };
  private String arrayData[][];
  private final int ROW_SALE = 0;
  private final int ROW_RETURN = 1;
  private final int ROW_NET = 2;
  private final int HISTORY_MONTHS[] = {1, 3, 6, 12
  };
  public static final int TXN_TOTAL = 0;
  public static final int MONTH_1 = 1;
  public static final int MONTH_3 = 2;
  public static final int MONTH_6 = 3;
  public static final int YEAR_TO_DATE = 4;
  public static final int LIFE_TO_DATE_DOLLAR = 5;
  public static final int LIFE_TO_DATE_PERCENT = 6;
  private CustSaleSummaryUtil util;

  /**
   * put your documentation comment here
   */
  public PurchaseHistoryModel() {
    int iCtr;
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    HTMLColumnHeaderUtil htmlUtil = new HTMLColumnHeaderUtil(CMSApplet.theAppMgr.getTheme().
        getTextFieldFont());
    String sIdentiFiers[] = new String[COLUMN_NAMES.length];
    for (iCtr = 0; iCtr < COLUMN_NAMES.length; iCtr++) {
      sIdentiFiers[iCtr] = htmlUtil.getHTMLHeaderFor(res.getString(COLUMN_NAMES[iCtr]));
    }
    this.setColumnIdentifiers(sIdentiFiers);
    for (iCtr = 0; iCtr < ROW_LABELS.length; iCtr++) {
      ROW_LABELS[iCtr] = res.getString(ROW_LABELS[iCtr]);
    }
    arrayData = new String[ROW_LABELS.length][COLUMN_NAMES.length];
  }

  /**
   * put your documentation comment here
   * @param arraySummary[]
   */
  public void setCustomerSaleSummary(CustomerSaleSummary arraySummary[]) {
    util = new CustSaleSummaryUtil(arraySummary);
    util.setSummaryMonths(HISTORY_MONTHS);
    buildData();
  }

  /**
   * put your documentation comment here
   * @param iRow
   * @param iColumn
   * @return
   */
  public Object getValueAt(int iRow, int iColumn) {
    if (arrayData == null || arrayData.length < 1 || iRow > arrayData.length)
      return null;
    return arrayData[iRow][iColumn];
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getColumnCount() {
    return COLUMN_NAMES.length;
  }

  /**
   * put your documentation comment here
   */
  private void buildData() {
    int iRowCtr;
    int iColCtr;
    int iMonthCtr = 0;
    if (util == null)
      return;
    for (iRowCtr = 0; iRowCtr < arrayData.length; iRowCtr++) {
      for (iColCtr = 0; iColCtr < COLUMN_NAMES.length; iColCtr++) {
        switch (iColCtr) {
          case TXN_TOTAL:
            arrayData[iRowCtr][iColCtr] = ROW_LABELS[iRowCtr];
            break;
          case LIFE_TO_DATE_DOLLAR:
            switch (iRowCtr) {
              case ROW_SALE:
                arrayData[iRowCtr][iColCtr] = "" + getDoubleValueAsString(util.getSaleAmountForLife());
                break;
              case ROW_RETURN:
                arrayData[iRowCtr][iColCtr] = "" + getDoubleValueAsString(util.getReturnAmountForLife());
                break;
              case ROW_NET:
                arrayData[iRowCtr][iColCtr] = ""
                    + getDoubleValueAsString((util.getSaleAmountForLife() - util.getReturnAmountForLife()));
                break;
            }
            break;
          case LIFE_TO_DATE_PERCENT:
            break;
          default:
            arrayData[iRowCtr][iColCtr] = getValueForMonth(iRowCtr, iMonthCtr++);
        }
      }
      iMonthCtr = 0;
      addRow(arrayData[iRowCtr]);
    }
  }
// Def 1531 : use CurrencyFormat.format to convert double to String
  private String getDoubleValueAsString(double value){
	  ArmCurrency curr = new ArmCurrency(value);
	  String str = CurrencyFormat.format(curr);
	  if(str.length()>1){
		  return str.substring(1);
	  }
	  return str;
  }

  /**
   * put your documentation comment here
   * @param iRow
   * @param iMonthCtr
   * @return
   */
  private String getValueForMonth(int iRow, int iMonthCtr) {
    switch (iRow) {
      case ROW_SALE:
        return "" + getDoubleValueAsString(util.getSaleAmountForMonths(iMonthCtr));
      case ROW_RETURN:
        return "" + getDoubleValueAsString(util.getReturnAmountForMonths(iMonthCtr));
      case ROW_NET:
        return ""
            + getDoubleValueAsString((util.getSaleAmountForMonths(iMonthCtr) - util.getReturnAmountForMonths(iMonthCtr)));
    }
    return null;
  }
}

