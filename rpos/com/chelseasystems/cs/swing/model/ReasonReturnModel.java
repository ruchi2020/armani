/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.model;

import javax.swing.table.*;
import java.util.*;
import javax.swing.*;
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cs.swing.returns.*;


/**
 * put your documentation comment here
 */
public class ReasonReturnModel extends ScrollableTableModel {
  JTable tblReason;

  /**
   * put your documentation comment here
   * @param   ReturnReasonStruct[] returnReasons
   */
  public ReasonReturnModel(ReturnReasonStruct[] returnReasons) {
    super(new String[] {" ", com.chelseasystems.cr.util.ResourceManager.getResourceBundle().getString("Select Reason for Return")
    });
    if (returnReasons != null && returnReasons.length > 0) {
    	for (int i = 0; i < returnReasons.length; i++) {
      this.addRow(returnReasons[i]);
  }
  	}
  }

  /**
   * put your documentation comment here
   * @param tblReason
   */
  public void setTblReason(JTable tblReason) {
    this.tblReason = tblReason;
  }

  /**
   * put your documentation comment here
   * @param row
   * @return
   */
  public ReturnReasonStruct getReasonStructAt(int row) {
    Vector vPage = getCurrentPage();
    if (vPage.size() == 0)
      return null;
    if (row < 0)
      return null;
    return (ReturnReasonStruct)vPage.elementAt(row);
  }

  /**
   * put your documentation comment here
   * @param row
   * @param column
   * @return
   */
  public Object getValueAt(int row, int column) {
    Vector vPage = getCurrentPage();
    if (vPage.size() == 0)
      return null;
    if (row < 0)
      return null;
    if (column == 0)
      return new Boolean(((JRadioButton)tblReason.getCellRenderer(row, column)).isSelected());
    else {
      return com.chelseasystems.cr.util.ResourceManager.getResourceBundle().getString(vPage.
          elementAt(row).toString());
    }
  }
}

