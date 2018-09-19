/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.model;

import java.util.ResourceBundle;
import com.ga.cs.swing.report.clasreport.ClassReportApplet;


/**
 */
public class ClassReportModel extends BaseReportModel {
  public static final String[] CLASS_REPORT_COLNAMES = ClassReportApplet.CLASS_REPORT_COL_HEADINGS;

  /**
   */
  public ClassReportModel() {
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    this.setColumnIdentifiers(new String[] {res.getString("Class"), res.getString("ClassDesc")
        , res.getString("Sale#"), res.getString("GrSal$"), res.getString("GrMkd$")
        , res.getString("Sales$"), res.getString("Rtn#"), res.getString("GrRtn")
        , res.getString("RtnMkd$"), res.getString("Rtn$"), res.getString("NetSal$")
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
    return CLASS_REPORT_COLNAMES.length;
  }

  /**
   * @param row
   * @param column
   * @return
   */
  public ClassReportModel(String[] colhdgs) {
    super(colhdgs);
  }

  /**
   * put your documentation comment here
   * @param   ResourceBundle res
   */
  public ClassReportModel(ResourceBundle res) {
    this.setColumnIdentifiers(new String[] {res.getString("Class"), res.getString("ClassDesc")
        , res.getString("Sale#"), res.getString("GrSal$"), res.getString("GrMkd$")
        , res.getString("Sales$"), res.getString("Rtn#"), res.getString("GrRtn")
        , res.getString("RtnMkd$"), res.getString("Rtn$"), res.getString("NetSal$")
    });
  }
  /**
   * @param row
   * @param column
   * @return
   */
}

