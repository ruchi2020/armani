/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 05/03/2006 | Sandhya   | N/A       | Presale Details Report                             |
 -------------------------------------------------------------------------------------------------- 
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.ga.cs.swing.model;

import java.util.ResourceBundle;
import com.chelseasystems.cr.util.ResourceManager;

/**
 * put your documentation comment here
 */
public class PresaleDetailsReportModel extends BaseReportModel {
  public static final String[] COLUMN_NAMES;
  public static final ResourceBundle RESOURCE;
  static {
    RESOURCE = ResourceManager.getResourceBundle();
    COLUMN_NAMES = new String[] {RESOURCE.getString("Assoc#"), RESOURCE.getString("Item")
        , RESOURCE.getString("Date Issued"), RESOURCE.getString("Qty Orig")
        , RESOURCE.getString("Amt Orig"), RESOURCE.getString("Item Status")
        , RESOURCE.getString("Date SoldReturn"), RESOURCE.getString("Qty Remain")
        , RESOURCE.getString("Amt Remain"), RESOURCE.getString("TranId")
    };
  }

  /**
   * put your documentation comment here
   */
  public PresaleDetailsReportModel() {
    initializeColumnIdentifers(RESOURCE);
  }

  /**
   * put your documentation comment here
   * @param res
   */
  private void initializeColumnIdentifers(ResourceBundle res) {
    this.setColumnIdentifiers(COLUMN_NAMES);
  }

  /**
   * put your documentation comment here
   * @param   String[] colhdgs
   */
  public PresaleDetailsReportModel(String[] colhdgs) {
    super(colhdgs);
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
   * @param   ResourceBundle res
   */
  public PresaleDetailsReportModel(ResourceBundle res) {
    initializeColumnIdentifers(res);
  }
}

