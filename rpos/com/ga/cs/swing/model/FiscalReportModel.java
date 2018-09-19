/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.model;

import javax.swing.table.*;
import java.util.*;
import com.ga.cs.pos.*;
import com.ga.cs.swing.report.fiscalreport.GaFiscalReportApplet;
import com.ga.cs.dataaccess.artsoracle.dao.*;
import com.chelseasystems.cr.swing.ScrollablePane;
import com.chelseasystems.cr.swing.ScrollableTableModel;


/**
 */
public class FiscalReportModel extends BaseReportModel {
  public static final String[] FISCAL_REPORT_COLNAMES = GaFiscalReportApplet.
      FISCAL_REPORT_COL_HEADINGS;

  /**
   */
  public FiscalReportModel() {
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    this.setColumnIdentifiers(new String[] {res.getString("Intervals"), res.getString("TranCount")
        , res.getString("Gross Sales"), res.getString("Net Sales ")
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
   return  4;
   }
   */
  public int getColumnCount() {
    return FISCAL_REPORT_COLNAMES.length;
  }

  /**
   * @param row
   * @param column
   * @return
   */
  public FiscalReportModel(String[] colhdgs) {
    super(colhdgs);
  }

  /**
   * put your documentation comment here
   * @param   ResourceBundle res
   */
  public FiscalReportModel(ResourceBundle res) {
    this.setColumnIdentifiers(new String[] {res.getString("Intervals"), res.getString("TranCount")
        , res.getString("Gross Sales"), res.getString("Net Sales ")
    });
  }
  /**
   * @param row
   * @param column
   * @return
   */
}

