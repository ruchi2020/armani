/**
 * <p>Title: ItemDetailPanel.java </p>
 *
 * <p>Description: Panel used by ItemDetailApplet to render details</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: SkillNetInc </p>
 *
 * @author Bawa Manpreet Singh
 * @version 1.0
 * Date Created : 02/11/2005
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 02-11-2005 | Manpreet  | N/A       | Original Version                                   |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */


package com.chelseasystems.cs.swing.panel;

import javax.swing.*;
import com.chelseasystems.cs.swing.model.ItemPriceModel;
import com.chelseasystems.cs.swing.model.ItemDetailModel;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import java.awt.BorderLayout;
import javax.swing.table.*;
import java.awt.Dimension;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import java.awt.event.ComponentEvent;


/**
 * put your documentation comment here
 */
public class ItemDetailPanel extends JPanel implements PageNumberGetter {
  private ItemPriceModel ipModel;
  private ItemDetailModel idetModel;
  private POSLineItem lineItem;
  private JCMSTable tblPricing;
  private JCMSTable tblDetails;
  private JPanel jpanPricing;
  private JPanel jpanDetails;
  private IApplicationManager theAppMgr;

  /**
   * put your documentation comment here
   */
  public ItemDetailPanel() { //(POSLineItem lineItem)
    try {
      //this.lineItem = lineItem;
      jbInit();
      this.addComponentListener(new java.awt.event.ComponentAdapter() {

        /**
         * put your documentation comment here
         * @param e
         */
        public void componentResized(ComponentEvent e) {
          resizeRows();
        }
      });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   */
  public void clear() {
    ipModel.clear();
    // idetModel.clear();
  }

  /**
   * put your documentation comment here
   * @param posLineItem
   * @param theAppMgr
   */
  public void setPOSLineItem(POSLineItem posLineItem, IApplicationManager theAppMgr) {
    //    if(lineItem.equals(posLineItem)) return;
    //this.lineItem = null;
    this.theAppMgr = theAppMgr;
    this.lineItem = posLineItem;
    //    try {
    //      jbInit();
    //      this.addComponentListener(new java.awt.event.ComponentAdapter()
    //                                {
    //                                    public void componentResized (ComponentEvent e)
    //                                    {
    //                                      resizeRows();
    //                                    }
    //                                  });
    //
    //    } catch (Exception ex){
    //      ex.printStackTrace();
    //    }
    setAppMgr(theAppMgr);
    clear();
    ipModel.setPriceModel(this.lineItem);
    ipModel.fireTableDataChanged();
    idetModel.setPOSLineItem(lineItem);
    tblDetails.setModel(idetModel);
    resizeRows();
  }

  /**
   * put your documentation comment here
   */
  private void resizeRows() {
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    tblPricing.setSize(new Dimension(this.getSize().width
        , ipModel.getRowCount() * tblPricing.getRowHeight()));
    tblPricing.doLayout();
    tblDetails.setSize(new Dimension(this.getSize().width
        , this.getSize().height - tblPricing.getHeight()));
    idetModel.setRowsShown(tblDetails.getHeight() / tblDetails.getRowHeight());
    idetModel.fireTableDataChanged();
    tblDetails.doLayout();
    TableColumn colDesc = tblPricing.getColumnModel().getColumn(0);
    TableColumn colPercentage = tblPricing.getColumnModel().getColumn(1);
    colPercentage.setPreferredWidth((int)(135 * r));
    TableColumn colAmt = tblPricing.getColumnModel().getColumn(2);
    colAmt.setPreferredWidth((int)(145 * r));
    colDesc.setPreferredWidth(tblPricing.getWidth()
        - (colAmt.getPreferredWidth() + colPercentage.getPreferredWidth()));
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    tblPricing.setAppMgr(theAppMgr);
    tblPricing.setFont(theAppMgr.getTheme().getTableFont());
    tblDetails.setAppMgr(theAppMgr);
    tblDetails.setFont(theAppMgr.getTheme().getTableFont());
    this.setBackground(theAppMgr.getBackgroundColor());
  }

  /**
   * put your documentation comment here
   * @exception Exception
   */
  private void jbInit()
      throws Exception {
    ipModel = new ItemPriceModel();
    idetModel = new ItemDetailModel();
    tblPricing = new JCMSTable(ipModel, JCMSTable.SELECT_ROW);
    tblPricing.setRowHeight(15);
    tblPricing.setCellSelectionEnabled(false);
    tblPricing.setColumnSelectionAllowed(false);
    tblPricing.setRowSelectionAllowed(false);
    tblPricing.setRequestFocusEnabled(false);
    tblPricing.setSize(new Dimension(this.getSize().width, 200));
    jpanPricing = new JPanel(new BorderLayout());
    jpanPricing.add(tblPricing.getTableHeader(), BorderLayout.NORTH);
    jpanPricing.add(tblPricing, BorderLayout.CENTER);
    tblDetails = new JCMSTable(idetModel, JCMSTable.SELECT_ROW);
    tblDetails.setCellSelectionEnabled(false);
    tblDetails.setColumnSelectionAllowed(false);
    tblDetails.setRowSelectionAllowed(false);
    tblDetails.setRequestFocusEnabled(false);
    tblDetails.setRowHeight(15);
    ipModel.setRowsShown(13);
    idetModel.setRowsShown(13);
    jpanDetails = new JPanel(new BorderLayout());
    jpanDetails.add(tblDetails.getTableHeader(), BorderLayout.NORTH);
    jpanDetails.add(tblDetails, BorderLayout.CENTER);
    this.setLayout(new BorderLayout());
    this.add(jpanPricing, BorderLayout.NORTH);
    this.add(jpanDetails, BorderLayout.CENTER);
    this.doLayout();
  }

  /**
   * @return
   */
  public int getCurrentPageNumber() {
    return (idetModel.getCurrentPageNumber());
  }

  /**
   * @return
   */
  public int getTotalPages() {
    return (idetModel.getPageCount());
  }

  /**
   * put your documentation comment here
   */
  public void nextPage() {
    idetModel.nextPage();
  }

  /**
   */
  public void prevPage() {
    idetModel.prevPage();
  }
}

