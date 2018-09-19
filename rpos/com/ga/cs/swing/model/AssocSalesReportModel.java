/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.model;

import java.util.ResourceBundle;
import java.util.Vector;
import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.ga.cs.swing.report.assocreport.AssociateReportApplet;


/**
 * TableModel for Associate Report
 *
 */
public class AssocSalesReportModel extends BaseReportModel {
  public static final String[] ASSOC_REPORT_COLNAMES = AssociateReportApplet.
      ASSOC_REPORT_COL_HEADINGS;

  /**
   */
  public AssocSalesReportModel() {
    ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    this.setColumnIdentifiers(new String[] {res.getString("Associate"), res.getString("Asso/ciate#")
        , res.getString("Total/Transactions"), res.getString("Net/Sales Units")
        , res.getString("Sales/Amount"), res.getString("Average/Units")
        , res.getString("Average/TranSale")
    });
  }

  /**
   * @param colhdgs
   */
  public AssocSalesReportModel(String[] colhdgs) {
    super(colhdgs);
  }

  /**
   */
  public AssocSalesReportModel(ResourceBundle res) {
    this.setColumnIdentifiers(new String[] {res.getString("Associate"), res.getString("Associate#")
        , res.getString("Total Transactions"), res.getString("Net Sales Units")
        , res.getString("Sales Amount"), res.getString("Average Units")
        , res.getString("Average TranSale")
    });
  }

  /**
   * @return
   */
  public int getColumnCount() {
    return 7;
  }
}

