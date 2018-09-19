/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.swing.panel;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.swing.model.NoUnitPriceItemsListModel;


/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 10/17/2006 | Sandhya   | N/A       | Displays items with zero unit price                |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
/**
 */
public class NoUnitPriceItemsListPanel extends JPanel {
  private static final long serialVersionUID = 0;  
  NoUnitPriceItemsListModel model;
  JCMSTable tblItems;

  /**
   */
  public NoUnitPriceItemsListPanel() {
    try {
      model = new NoUnitPriceItemsListModel();
      tblItems = new JCMSTable(model, JCMSTable.SELECT_ROW);
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   * @param   String sColIdentifiers[]
   */
  public NoUnitPriceItemsListPanel(String sColIdentifiers[]) {
    try {
      model = new NoUnitPriceItemsListModel(sColIdentifiers);
      tblItems = new JCMSTable(model, JCMSTable.SELECT_ROW);
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * @exception Exception
   */
  private void jbInit()
      throws Exception {
    this.setLayout(new BorderLayout());
    tblItems.addComponentListener(new java.awt.event.ComponentAdapter() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void componentResized(ComponentEvent e) {
        resetColumnWidths();
      }
    });
    this.add(tblItems.getTableHeader(), BorderLayout.NORTH);
    this.add(tblItems, BorderLayout.CENTER);
    tblItems.setRequestFocusEnabled(false);
    tblItems.setCellSelectionEnabled(false);
    tblItems.setColumnSelectionAllowed(false);
    tblItems.setRowSelectionAllowed(true);
  }

  /**
   * @param items
   */
  public void setItems(CMSItem[] items) {
    clear();
    for (int x = 0; x < items.length; x++) {
    	addItem(items[x]);
    }
    sortItems();
  }

  /**
   * @param item
   */
  public void addItem(CMSItem item) {
    model.addItem(item);
  }
  
  /**
   */
  public void clear() {
    model.clear();
  }

  /**
   * @param theAppMgr
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
	  tblItems.getTableHeader().setBackground(theAppMgr.getBackgroundColor());
  }

  /**
   * @return
   */
  public JCMSTable getTable() {
    return tblItems;
  }

  /**
   * @param e
   */
  public void resetColumnWidths() {
    model.setColumnWidth(tblItems);
    model.setRowsShown(tblItems.getHeight() / tblItems.getRowHeight());
  }

  /**
   */
  public void nextPage() {
    model.nextPage();
  }

  /**
   */
  public void prevPage() {
    model.prevPage();
  }

  /**
   * @return
   */
  public int getTotalPages() {
    return model.getPageCount();
  }

  /**
   * @return
   */
  public int getCurrentPageNumber() {
    return model.getCurrentPageNumber();
  }

  /**
   * put your documentation comment here
   */
  public void sortItems() {
    model.sortItems();
  }
}

