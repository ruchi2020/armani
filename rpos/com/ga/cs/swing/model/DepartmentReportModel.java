/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.model;

import javax.swing.table.*;
import java.util.*;
import com.ga.cs.pos.*;
import com.ga.cs.swing.report.depreport.DepartmentReportApplet;
import com.ga.cs.dataaccess.artsoracle.dao.*;
import com.chelseasystems.cr.swing.ScrollablePane;
import com.chelseasystems.cr.swing.ScrollableTableModel;


/**
 */
public class DepartmentReportModel extends BaseReportModel {
  public static final String[] DEPT_REPORT_COLNAMES = DepartmentReportApplet.
      DEPT_REPORT_COL_HEADINGS;

  /**
   */
  public DepartmentReportModel() {
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    this.setColumnIdentifiers(new String[] {res.getString("Dept#"), res.getString("Department")
        , res.getString("GrSal$"), res.getString("GrMkd$"), res.getString("Sales$")
        , res.getString("Rtn#"), res.getString("GrRtn"), res.getString("RtnMkd$")
        , res.getString("Rtn$"), res.getString("NetQty"), res.getString("NetSal$")
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
   return  11;
   }
   */
  public int getColumnCount() {
    return DEPT_REPORT_COLNAMES.length;
  }

  /**
   * @param row
   * @param column
   * @return
   */
  public DepartmentReportModel(String[] colhdgs) {
    super(colhdgs);
  }

  /**
   * put your documentation comment here
   * @param   ResourceBundle res
   */
  public DepartmentReportModel(ResourceBundle res) {
    this.setColumnIdentifiers(new String[] {res.getString("Dept#"), res.getString("Department")
        , res.getString("GrSal$"), res.getString("GrMkd$"), res.getString("Sales$")
        , res.getString("Rtn#"), res.getString("GrRtn"), res.getString("RtnMkd$")
        , res.getString("Rtn$"), res.getString("NetQty"), res.getString("NetSal$")
    });
  }
  /**
   * @param row
   * @param column
   * @return
   */
}

