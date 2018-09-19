/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.model;

import java.util.ResourceBundle;
import com.ga.cs.swing.report.itemreport.GaItemReportApplet;


/**
 */
public class GaItemReportModel extends BaseReportModel {
  public static final String[] ITEM_REPORT_COLNAMES = GaItemReportApplet.ITEM_REPORT_COL_HEADINGS;

  /**
   */
  public GaItemReportModel() {
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    this.setColumnIdentifiers(new String[] {res.getString("Item"), res.getString("ItemDescription")
        , res.getString("Department"), res.getString("DeptDescription"), res.getString("Employee")
        , res.getString("NetSales"), res.getString("NetUnits")
    });
  }

  /**
   * @param
   */
  /**
   * @return
   */
  /*
   public int getColumnCount () {
   return  7;
   }
   */
  public int getColumnCount() {
    return ITEM_REPORT_COLNAMES.length;
  }

  /**
   * @param row
   * @param column
   * @return
   */
  public GaItemReportModel(String[] colhdgs) {
    super(colhdgs);
  }

  /**
   * put your documentation comment here
   * @param   ResourceBundle res
   */
  public GaItemReportModel(ResourceBundle res) {
    this.setColumnIdentifiers(new String[] {res.getString("Item"), res.getString("ItemDescription")
        , res.getString("Department"), res.getString("DeptDescription"), res.getString("Employee")
        , res.getString("NetSales"), res.getString("NetUnits")
    });
  }
  /**
   * @param row
   * @param column
   * @return
   */
}

