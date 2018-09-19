/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.model;

import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cs.customer.CustomerComment;
import java.util.Vector;
import java.util.Arrays;
import java.text.SimpleDateFormat;
import com.chelseasystems.cs.util.HTMLColumnHeaderUtil;
import com.chelseasystems.cr.swing.CMSApplet;
import java.util.Comparator;
import java.util.ArrayList;
import com.chelseasystems.cs.store.CMSStore;
import java.util.List;


/**
 * <p>Title:CustomerCommentModel </p>
 *
 * <p>Description: Model for CustomerComments</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: SkillNet Inc</p>
 *
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 03-10-2005 | Manpreet  | N/A       | POS_104665_FS_CustomerManagement_Rev2              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class CustomerCommentModel extends ScrollableTableModel {
  private String COLUMN_NAMES[] = {"Store\nID", "Brand", "Assoc", "Date", "Comment"
  };
  public static final int STORE_ID = 0;
  public static final int BRAND_ID = 1;
  public static final int ASSOCIATE_ID = 2;
  public static final int DATE_COMMENTED = 3;
  public static final int COMMENT = 4;
  public boolean bSortComments = true;
  public SimpleDateFormat sDateFormat;

  /**
   * put your documentation comment here
   */
  public CustomerCommentModel() {
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    HTMLColumnHeaderUtil htmlUtil = new HTMLColumnHeaderUtil(CMSApplet.theAppMgr.getTheme().
        getTextFieldFont());
    String sIdentiFiers[] = new String[COLUMN_NAMES.length];
    for (int iCtr = 0; iCtr < COLUMN_NAMES.length; iCtr++) {
      sIdentiFiers[iCtr] = htmlUtil.getHTMLHeaderFor(res.getString(COLUMN_NAMES[iCtr]));
    }
    this.setColumnIdentifiers(sIdentiFiers);
    // Date format Issue # 810
    // CUSTOMER PROFILE - entering comments, the date posted is YYYYMMDD should be MMDDYYYY
    sDateFormat = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
  }

  /**
   * put your documentation comment here
   * @param comment
   * @return
   */
  public int addCustomerComment(CustomerComment comment) {
    if (comment == null)
      return -1;
    addRow(comment);
    if (bSortComments)
      sortComments();
    return vRows.indexOf(comment);
  }

  // Issue # 830
  /*
   private void sortComments()
   {
   int iCtr=0;
   Object obj[] = super.vRows.toArray();
   CustomerComment comments[] = new CustomerComment[obj.length];
   System.arraycopy(obj,0, comments,0, comments.length);
   Arrays.sort(comments);
   super.clear();
   for(iCtr=0; iCtr< comments.length; iCtr++)
   {
   addRow(comments[iCtr]);
   }
   }
   */
  public void setSorting(boolean bSort) {
    bSortComments = bSort;
  }

  /**
   * put your documentation comment here
   * @param row
   */
  public void deleteCustomerComment(int row) {
    removeRowInPage(row);
    this.fireTableRowsDeleted(row, row);
  }

  /**
   * put your documentation comment here
   * @param row
   * @return
   */
  public CustomerComment getCustomerCommentAt(int row) {
    return (CustomerComment)this.getRowInPage(row);
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
   * @param row
   * @param column
   * @return
   */
  public Object getValueAt(int row, int column) {
    Vector vTemp = this.getCurrentPage();
    CustomerComment comment = (CustomerComment)vTemp.elementAt(row);
    switch (column) {
      case STORE_ID:
        return comment.getStoreId();
      case BRAND_ID:
        return comment.getBrandId();
      case ASSOCIATE_ID:
        return comment.getAssociateId();
      case DATE_COMMENTED:
        try {
          return sDateFormat.format(comment.getDateCommented());
        } catch (Exception e) {
          return comment.getDateCommented();
        }
        case COMMENT:
          return comment.getComment();
    }
    return null;
  }

  /**
   * put your documentation comment here
   * @param RowsShown
   */
  public void setRowsShown(int RowsShown) {
    super.setRowsShown(RowsShown);
  }

  /**
   * put your documentation comment here
   */
  private void sortComments() {
    int iCtr = 0;
    Object obj[] = super.vRows.toArray();
    ArrayList currentStoreComments = new ArrayList();
    ArrayList otherStoreComments = new ArrayList();
    CMSStore cmsStore = (CMSStore)com.chelseasystems.cr.appmgr.AppManager.getInstance().
        getGlobalObject("STORE");
    try {
      if (obj.length > 0 && cmsStore != null) {
        String storeID = cmsStore.getId();
        for (iCtr = 0; iCtr < obj.length; iCtr++) {
          CustomerComment comment = (CustomerComment)obj[iCtr];
          if (comment.getStoreId().equalsIgnoreCase(storeID)) {
            currentStoreComments.add(comment);
          } else {
            otherStoreComments.add(comment);
          }
        }
        super.clear();
        if (currentStoreComments.size() > 0) {
          CustomerComment[] cStoreComments = (CustomerComment[])currentStoreComments.toArray(new
              CustomerComment[0]);
          Arrays.sort(cStoreComments, new Comparator() {

            /**
             * put your documentation comment here
             * @param o1
             * @param o2
             * @return
             */
            public int compare(Object o1, Object o2) {
              return ((CustomerComment)o1).getDateCommented().compareTo(((CustomerComment)o2).
                  getDateCommented()) * -1;
            }
          });
          for (iCtr = 0; iCtr < cStoreComments.length; iCtr++) {
            addRow(cStoreComments[iCtr]);
          }
        }
        if (otherStoreComments.size() > 0) {
          CustomerComment[] oStoreComments = (CustomerComment[])otherStoreComments.toArray(new
              CustomerComment[0]);
          Arrays.sort(oStoreComments, new Comparator() {

            /**
             * put your documentation comment here
             * @param o1
             * @param o2
             * @return
             */
            public int compare(Object o1, Object o2) {
              return ((CustomerComment)o1).getDateCommented().compareTo(((CustomerComment)o2).
                  getDateCommented()) * -1;
            }
          });
          for (iCtr = 0; iCtr < oStoreComments.length; iCtr++) {
            addRow(oStoreComments[iCtr]);
          }
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}

