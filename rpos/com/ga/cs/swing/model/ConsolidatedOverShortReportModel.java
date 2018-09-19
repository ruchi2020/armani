/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.model;

import java.util.ResourceBundle;
import com.ga.cs.swing.report.consolidatedovershort.ConsolidatedOverShortReportApplet;


/**
 * TableModel for GA Transaction Analysis Report
 * @author fbulah
 *
 */
public class ConsolidatedOverShortReportModel extends BaseReportModel {
  public static final String[] OVERSHORT_REPORT_COLNAMES = ConsolidatedOverShortReportApplet.
      COL_HEADINGS;

  /**
   *
   */
  public ConsolidatedOverShortReportModel() {
    ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    initializeColumnIdentifers(res);
  }

  /**
   * @param colhdgs
   */
  public ConsolidatedOverShortReportModel(String[] colhdgs) {
    super(colhdgs);
  }

  /**
   */
  public ConsolidatedOverShortReportModel(ResourceBundle res) {
    initializeColumnIdentifers(res);
  }

  /**
   * put your documentation comment here
   * @param res
   */
  protected void initializeColumnIdentifers(ResourceBundle res) {
    this.setColumnIdentifiers(new String[] {res.getString(ConsolidatedOverShortReportApplet.
        REGISTEER_ID), res.getString(ConsolidatedOverShortReportApplet.MEDIA_TYPE)
        , res.getString(ConsolidatedOverShortReportApplet.COUNTED)
        , res.getString(ConsolidatedOverShortReportApplet.EXPECTED)
        , res.getString(ConsolidatedOverShortReportApplet.OVERSHORT)
    });
  }

  /* (non-Javadoc)
   * @see javax.swing.table.TableModel#getColumnClass(int)
   */
  public Class getColumnClass(int columnIndex) {
    return getValueAt(0, columnIndex).getClass();
  }

  /**
   * @return
   */
  public int getColumnCount() {
    return OVERSHORT_REPORT_COLNAMES.length;
  }
}

