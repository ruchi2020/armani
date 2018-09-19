/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import javax.swing.*;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cs.swing.model.CustomerCommentModel;
import com.chelseasystems.cs.customer.CustomerComment;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import javax.swing.table.TableColumnModel;
import java.awt.event.ComponentEvent;
import java.awt.BorderLayout;
import javax.swing.table.TableColumn;
import java.awt.Font;
import java.awt.Color;
import java.awt.Component;
import javax.swing.table.TableCellRenderer;
import com.chelseasystems.cr.rules.BusinessRuleException;
import java.awt.Dimension;


/**
 * <p>Title: ViewCommentsPanel </p>
 *
 * <p>Description: To Add/View Comments</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: Skillnet Inc </p>
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
public class ViewCommentsPanel extends JPanel implements PageNumberGetter {
  private CustomerCommentModel modelComments;
  private JCMSTable tblComments;
  private TextRenderer renderer;

  /**
   * put your documentation comment here
   */
  public ViewCommentsPanel() {
    modelComments = new CustomerCommentModel();
    tblComments = new JCMSTable(modelComments, JCMSTable.SELECT_ROW);
    tblComments.setCellSelectionEnabled(false);
    tblComments.setColumnSelectionAllowed(false);
    tblComments.setRowSelectionAllowed(false);
    tblComments.setRequestFocusEnabled(false);
    renderer = new TextRenderer();
    this.setLayout(new BorderLayout());
    this.add(tblComments.getTableHeader(), BorderLayout.NORTH);
    this.add(tblComments, BorderLayout.CENTER);
    modelComments.setRowsShown(13); // arbitrarily set until resize occurs
    TableColumnModel modelColumn = tblComments.getColumnModel();
    for (int i = 0; i < modelComments.getColumnCount(); i++) {
      modelColumn.getColumn(i).setCellRenderer(renderer);
    }
    this.addComponentListener(new java.awt.event.ComponentAdapter() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void componentResized(ComponentEvent e) {
        resetColumnWidths();
      }
    });
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    if (theAppMgr != null) {
      tblComments.setAppMgr(theAppMgr);
      renderer.setFont(theAppMgr.getTheme().getTextFieldFont());
    }
  }

  /**
   * put your documentation comment here
   * @param comment
   */
  public void addCustomerComment(CustomerComment comment) {
    selectRow(modelComments.addCustomerComment(comment));
    // selectLastRow();
  }

  /**
   * put your documentation comment here
   * @exception BusinessRuleException
   */
  public void deleteSelectedComment()
      throws BusinessRuleException {
    int row = tblComments.getSelectedRow();
    CustomerComment comment = getCustomerCommentAt(row);
    deleteCustomerComment(comment);
  }

  /**
   * put your documentation comment here
   * @param comment
   * @exception BusinessRuleException
   */
  public void deleteCustomerComment(CustomerComment comment)
      throws BusinessRuleException {
    modelComments.removeRowInModel(comment);
    modelComments.fireTableDataChanged();
    this.selectLastRow();
  }

  /**
   * put your documentation comment here
   */
  public void pageUp() {
    modelComments.prevPage();
    selectFirstRow();
  }

  /**
   * put your documentation comment here
   */
  public void pageDown() {
    modelComments.nextPage();
    selectLastRow();
  }

  /**
   * put your documentation comment here
   * @param row
   * @return
   */
  public CustomerComment getCustomerCommentAt(int row) {
    return (modelComments.getCustomerCommentAt(row));
  }

  /**
   * put your documentation comment here
   */
  public void selectLastRow() {
    int rowCount = tblComments.getRowCount();
    if (rowCount < 1) {
      modelComments.prevPage();
    }
    if (rowCount > 0) {
      tblComments.setRowSelectionInterval(rowCount - 1, rowCount - 1);
    }
  }

  /**
   * put your documentation comment here
   */
  public void selectFirstRow() {
    selectRow(0);
  }

  /**
   * put your documentation comment here
   */
  public void clear() {
    modelComments.clear();
  }

  /**
   * put your documentation comment here
   */
  public void update() {
    modelComments.fireTableDataChanged();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public CustomerCommentModel getCommentModel() {
    return modelComments;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public JCMSTable getTable() {
    return (this.tblComments);
  }

  /**
   * @return
   */
  public int getCurrentPageNumber() {
    return (modelComments.getCurrentPageNumber());
  }

  /**
   * @return
   */
  public int getTotalPages() {
    return (modelComments.getPageCount());
  }

  /**
   * @param row
   */
  public void selectRow(int row) {
    ListSelectionModel model = tblComments.getSelectionModel();
    model.setSelectionInterval(row, row);
  }

  /**
   * put your documentation comment here
   */
  public void resetColumnWidths() {
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    TableColumn StoreIdCol = tblComments.getColumnModel().getColumn(CustomerCommentModel.STORE_ID);
    StoreIdCol.setPreferredWidth((int)(75 * r));
    TableColumn BrandIdCol = tblComments.getColumnModel().getColumn(CustomerCommentModel.BRAND_ID);
    BrandIdCol.setPreferredWidth((int)(115 * r));
    TableColumn AssocIdCol = tblComments.getColumnModel().getColumn(CustomerCommentModel.
        ASSOCIATE_ID);
    AssocIdCol.setPreferredWidth((int)(105 * r));
    TableColumn DateCommentCol = tblComments.getColumnModel().getColumn(CustomerCommentModel.
        DATE_COMMENTED);
    DateCommentCol.setPreferredWidth((int)(75 * r));
    TableColumn CommentCol = tblComments.getColumnModel().getColumn(CustomerCommentModel.COMMENT);
    CommentCol.setPreferredWidth(tblComments.getWidth()
        - (StoreIdCol.getPreferredWidth() + BrandIdCol.getPreferredWidth()
        + +AssocIdCol.getPreferredWidth() + DateCommentCol.getPreferredWidth()));
    modelComments.setRowsShown(tblComments.getHeight() / tblComments.getRowHeight());
    tblComments.getTableHeader().setPreferredSize(new Dimension((int)r, 50));
  }

  private class TextRenderer extends JLabel implements TableCellRenderer {
    private Color DefaultBackground;
    private Color DefaultForeground;

    /**
     */
    public TextRenderer() {
      super();
      setFont(new Font("Helvetica", 1, 12));
      setForeground(new Color(0, 0, 175));
      setBackground(Color.white);
      DefaultBackground = getBackground();
      DefaultForeground = getForeground();
      setOpaque(true);
    }

    /**
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected
        , boolean hasFocus, int row, int col) {
      if (value != null)
        setText(value.toString());
      else
        setText("");
      switch (col) {
        case CustomerCommentModel.STORE_ID:
        case CustomerCommentModel.BRAND_ID:
        case CustomerCommentModel.ASSOCIATE_ID:
        case CustomerCommentModel.DATE_COMMENTED:
          setHorizontalAlignment(JLabel.CENTER);
          break;
        case CustomerCommentModel.COMMENT:
          setHorizontalAlignment(JLabel.LEFT);
          break;
      }
      if (isSelected) {
        setForeground(Color.white);
        setBackground(new Color(0, 0, 128));
      } else {
        setBackground(DefaultBackground);
        setForeground(DefaultForeground);
      }
      return (this);
    }
  }
}

