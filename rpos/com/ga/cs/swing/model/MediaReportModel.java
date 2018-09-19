/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.model;

import java.util.ResourceBundle;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.ga.cs.swing.report.mediareport.MediaReportApplet;


/**
 * TableModel for Media Report
 * @author fbulah
 *
 */
public class MediaReportModel extends BaseReportModel {
  public static final String[] MEDIA_REPORT_COLNAMES = MediaReportApplet.MEDIA_REPORT_COL_HEADINGS;

  /**
   */
  public MediaReportModel() {
    ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    this.setColumnIdentifiers(new String[] {res.getString("Media Type")
        , res.getString("Media Count"), res.getString("Gross Amount")
        , res.getString("Credit/Return"), res.getString("Net Amount")
    });
  }

  /**
   * @param colhdgs
   */
  public MediaReportModel(String[] colhdgs) {
    super(colhdgs);
  }

  /**
   */
  public MediaReportModel(ResourceBundle res) {
    this.setColumnIdentifiers(new String[] {res.getString("Media Type")
        , res.getString("Media Count"), res.getString("Gross Amount")
        , res.getString("Credit/Return"), res.getString("Net Amount")
    });
  }

  //	/* (non-Javadoc)
   //	 * @see javax.swing.table.TableModel#getColumnClass(int)
   //	 */
  //	public Class getColumnClass(int columnIndex) {
  //		return getValueAt(0, columnIndex).getClass();
  //	}
  /**
   * @return
   */
  public int getColumnCount() {
    return MEDIA_REPORT_COLNAMES.length;
  }
}

