/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.loyalty;

import javax.swing.*;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import java.awt.Font;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import javax.swing.table.TableColumn;
import java.awt.Dimension;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.util.ResourceManager;
import java.awt.Event.*;
import java.awt.event.ActionEvent;
import java.awt.Event;
import com.chelseasystems.cr.swing.MultiLineCellRenderer;
import com.chelseasystems.cs.loyalty.LoyaltyHistory;
import com.chelseasystems.cs.loyalty.LoyaltyHistory;
import java.util.ResourceBundle;


/**
 * <p>Title:ItemLookupListPanel </p>
 * <p>Description: View List of Items</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillnetInc</p>
 * @author Megha
 * @version 1.0
 */
public class LoyaltyTransactionDetailPanel extends JPanel {
  private LoyaltyTransactionDetailModel modelLoyaltyTransactionDetail;
  private JCMSTable tblItem;
  private TextRenderer renderer;
  private IApplicationManager theAppMgr;
  public static ResourceBundle res = null;

  /**
   * put your documentation comment here
   * @param   IApplicationManager theAppMgr
   */
  public LoyaltyTransactionDetailPanel(IApplicationManager theAppMgr) {
    this.theAppMgr = theAppMgr;
    try {
      res = ResourceManager.getResourceBundle();
      modelLoyaltyTransactionDetail = new LoyaltyTransactionDetailModel(theAppMgr);
      tblItem = new JCMSTable(modelLoyaltyTransactionDetail, JCMSTable.SELECT_ROW);
      renderer = new TextRenderer();
      WrapTextRenderer wrapRenderer = new WrapTextRenderer();
      this.setLayout(new BorderLayout());
      this.add(tblItem.getTableHeader(), BorderLayout.NORTH);
      this.add(tblItem, BorderLayout.CENTER);
      tblItem.setAlignmentX(CENTER_ALIGNMENT);
      tblItem.setAlignmentY(CENTER_ALIGNMENT);
      modelLoyaltyTransactionDetail.setRowsShown(13); // arbitrarily set until resize occurs
      TableColumnModel modelColumn = tblItem.getColumnModel();
      for (int i = 0; i < modelLoyaltyTransactionDetail.getColumnCount(); i++) {
        switch (i) {
          case LoyaltyTransactionDetailModel.DATE:
          case LoyaltyTransactionDetailModel.STORE_ID:
          case LoyaltyTransactionDetailModel.TXN_ID:
          case LoyaltyTransactionDetailModel.TXN_TYPE:
          case LoyaltyTransactionDetailModel.DEPOSITS:
          case LoyaltyTransactionDetailModel.WITHDRAWLS:
            modelColumn.getColumn(i).setCellRenderer(renderer);
        }
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
      //      tblItem.getTableHeader().addMouseListener(new MouseAdapter() {
      //          public void mouseClicked(MouseEvent e) {
      //              JTableHeader h = (JTableHeader) e.getSource();
      //              TableColumnModel columnModel = h.getColumnModel();
      //              int viewColumn = columnModel.getColumnIndexAtX(e.getX());
      //              int column = columnModel.getColumn(viewColumn).getModelIndex();
      //               clickEvent(e);
      //          }
      //      });
      this.requestFocus();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  public JCMSTable getTable() {
    return tblItem;
  }

  /**
   * put your documentation comment here
   * @param row
   */
  public void selectRow(int row) {
    //    ListSelectionModel model = tblItem.getSelectionModel();
    //    model.setSelectionInterval(row, row);
    if (row < 0)
      return;
    modelLoyaltyTransactionDetail.pageContainingRow(row);
    selectRowIfInCurrentPage(row);
  }

  /**
   * put your documentation comment here
   * @param absoluteRow
   */
  public void selectRowIfInCurrentPage(int absoluteRow) {
    if (absoluteRow < 0 || absoluteRow >= modelLoyaltyTransactionDetail.getAllRows().size())
      return;
    int rowInPage = modelLoyaltyTransactionDetail.getCurrentPage().indexOf(
        modelLoyaltyTransactionDetail.getAllRows().elementAt(absoluteRow));
    modelLoyaltyTransactionDetail.setLastSelectedTxnRow(absoluteRow);
    if (rowInPage < 0)
      return;
    ListSelectionModel model = tblItem.getSelectionModel();
    model.setSelectionInterval(rowInPage, rowInPage);
  }

  /**
   * put your documentation comment here
   * @param loyaltyHistory[]
   */
  public void setItems(LoyaltyHistory loyaltyHistory[]) {
    clear();
    modelLoyaltyTransactionDetail.setItems(loyaltyHistory);
    selectFirstRow();
  }

  /**
   * Adds Item to the panel
   * @param loyaltyHistory LoyaltyHistory
   */
  public void addItem(LoyaltyHistory loyaltyHistory) {
    modelLoyaltyTransactionDetail.addItem(loyaltyHistory);
  }

  /**
   * put your documentation comment here
   * @exception BusinessRuleException
   */
  public void deleteSelectedItem()
      throws BusinessRuleException {
    int row = tblItem.getSelectedRow();
    LoyaltyHistory loyalty = getItemAt(row);
    deleteItem(loyalty);
  }

  /**
   * Delete Item from the panel
   * @param loyaltyHistory LoyaltyHistory
   * @throws BusinessRuleException
   */
  public void deleteItem(LoyaltyHistory loyaltyHistory)
      throws BusinessRuleException {
    modelLoyaltyTransactionDetail.removeRowInModel(loyaltyHistory);
    modelLoyaltyTransactionDetail.fireTableDataChanged();
    this.selectLastRow();
  }

  /**
   * put your documentation comment here
   */
  public void pageUp() {
    modelLoyaltyTransactionDetail.prevPage();
    selectFirstRow();
  }

  /**
   * put your documentation comment here
   */
  public void pageDown() {
    modelLoyaltyTransactionDetail.nextPage();
    selectLastRow();
  }

  /**
   * put your documentation comment here
   * @param row
   * @return
   */
  public LoyaltyHistory getItemAt(int row) {
    return (modelLoyaltyTransactionDetail.getItemAt(row));
  }

  /**
   * put your documentation comment here
   * @param l
   */
  public void addMouseListener(MouseListener l) {
    tblItem.addMouseListener(l);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getSelectedRowIndex() {
    //return tblItem.getSelectedRow();
    if (tblItem.getSelectedRow() < 0)
      return tblItem.getSelectedRow();
    int page = modelLoyaltyTransactionDetail.getCurrentPageNumber();
    int row = (page * modelLoyaltyTransactionDetail.getRowsShown()) + tblItem.getSelectedRow();
    return row;
  }

  /**
   * Gets the Selected Item from the panel
   * @return LoyaltyHistory
   */
  public LoyaltyHistory getSelectedItem() {
    if (tblItem.getSelectedRow() == -1)
      return null;
    return getItemAt(tblItem.getSelectedRow());
  }

  /**
   * put your documentation comment here
   */
  public void selectLastRow() {
    int rowCount = tblItem.getRowCount();
    if (rowCount < 1) {
      modelLoyaltyTransactionDetail.prevPage();
    }
    if (rowCount > 0) {
      tblItem.setRowSelectionInterval(rowCount - 1, rowCount - 1);
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
    modelLoyaltyTransactionDetail.clear();
    tblItem.removeAll();
  }

  /**
   * put your documentation comment here
   */
  public void update() {
    modelLoyaltyTransactionDetail.fireTableDataChanged();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public LoyaltyTransactionDetailModel getAddressModel() {
    return modelLoyaltyTransactionDetail;
  }

  /**
   * put your documentation comment here
   */
  public void resetColumnWidths() {
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    int iWidth = 0;
    TableColumn DateCol = tblItem.getColumnModel().getColumn(LoyaltyTransactionDetailModel.DATE);
    DateCol.setPreferredWidth((int)(105 * r));
    TableColumn StoreIDCol = tblItem.getColumnModel().getColumn(LoyaltyTransactionDetailModel.
        STORE_ID);
    StoreIDCol.setPreferredWidth((int)(65 * r));
    TableColumn TxnIDCol = tblItem.getColumnModel().getColumn(LoyaltyTransactionDetailModel.TXN_ID);
    TxnIDCol.setPreferredWidth((int)(55 * r));
    TableColumn TxnTypeCol = tblItem.getColumnModel().getColumn(LoyaltyTransactionDetailModel.
        TXN_TYPE);
    TxnTypeCol.setPreferredWidth((int)(55 * r));
    TableColumn DepositsCol = tblItem.getColumnModel().getColumn(LoyaltyTransactionDetailModel.
        DEPOSITS);
    DepositsCol.setPreferredWidth((int)(50 * r));
    TableColumn WithdrawlsCol = tblItem.getColumnModel().getColumn(LoyaltyTransactionDetailModel.
        WITHDRAWLS);
    WithdrawlsCol.setPreferredWidth((int)(55 * r));
    iWidth = tblItem.getWidth()
        - (DateCol.getPreferredWidth() + StoreIDCol.getPreferredWidth()
        + TxnIDCol.getPreferredWidth() + TxnTypeCol.getPreferredWidth()
        + DepositsCol.getPreferredWidth() + WithdrawlsCol.getPreferredWidth());
    this.modelLoyaltyTransactionDetail.setRowsShown(tblItem.getHeight() / tblItem.getRowHeight());
    tblItem.getTableHeader().setPreferredSize(new Dimension((int)r, 30));
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    if (theAppMgr != null) {
      tblItem.setAppMgr(theAppMgr);
      renderer.setFont(theAppMgr.getTheme().getTextFieldFont());
    }
  }

  /**
   * @return int
   */
  public int getCurrentPageNumber() {
    return (modelLoyaltyTransactionDetail.getCurrentPageNumber());
  }

  /**
   * @return int
   */
  public int getTotalPages() {
    return (modelLoyaltyTransactionDetail.getPageCount());
  }

  /**
   * put your documentation comment here
   */
  public void nextPage() {
    modelLoyaltyTransactionDetail.nextPage();
  }

  /**
   */
  public void prevPage() {
    modelLoyaltyTransactionDetail.prevPage();
  }

  private class TextRenderer extends JLabel implements TableCellRenderer {
    private Color DefaultBackground;
    private Color DefaultForeground;

    /**
     * put your documentation comment here
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
     * put your documentation comment here
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param col
     * @return
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected
        , boolean hasFocus, int row, int col) {
      String textToSet = "";
      if (value != null)
        textToSet = value.toString();
      switch (col) {
        case LoyaltyTransactionDetailModel.DATE:
        case LoyaltyTransactionDetailModel.STORE_ID:
        case LoyaltyTransactionDetailModel.TXN_ID:
        case LoyaltyTransactionDetailModel.TXN_TYPE:
        case LoyaltyTransactionDetailModel.DEPOSITS:
        case LoyaltyTransactionDetailModel.WITHDRAWLS:
          setHorizontalAlignment(JLabel.CENTER);
          break;
      }
      if (isSelected) {
        setForeground(Color.white);
        setBackground(new Color(0, 0, 128));
      } else {
        setBackground(DefaultBackground);
        setForeground(DefaultForeground);
      }
      setText(textToSet);
      return (this);
    }
  }


  private class WrapTextRenderer extends MultiLineCellRenderer {
    private Color DefaultBackground;
    private Color DefaultForeground;

    /**
     * put your documentation comment here
     */
    public WrapTextRenderer() {
      super();
      setFont(new Font("Helvetica", 1, 12));
      setForeground(new Color(0, 0, 175));
      setBackground(Color.white);
      DefaultBackground = getBackground();
      DefaultForeground = getForeground();
      setOpaque(true);
    }

    /**
     * put your documentation comment here
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param col
     * @return
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected
        , boolean hasFocus, int row, int col) {
      super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
      setWrapStyleWord(true);
      String textToSet = "";
      if (value != null)
        textToSet = value.toString();
      setAlignmentX(CENTER_ALIGNMENT);
      setAlignmentY(CENTER_ALIGNMENT);
      if (isSelected) {
        setForeground(Color.white);
        setBackground(new Color(0, 0, 128));
      } else {
        setBackground(DefaultBackground);
        setForeground(DefaultForeground);
      }
      setText(textToSet);
      return (this);
    }
  }


  /**
   * Handles click event
   * @param me MouseEvent
   */
  //  public void clickEvent (MouseEvent me)
  //     {
  //
  //
  //         LoyaltyHistory loyaltyHist = modelLoyaltyTransactionDetail.getItemAt(tblItem.getSelectedRow());
  //
  //         if(loyaltyHist != null)
  //             try
  //             { System.out.println("**** txn Id = " +loyaltyHist.getTransactionId());
  //                 PaymentTransaction theTxn = (PaymentTransaction)CMSTransactionPOSHelper.findById(theAppMgr, loyaltyHist.getTransactionId());
  //                 if(theTxn != null)
  //                 {
  //                     theAppMgr.addStateObject("THE_TXN", theTxn);
  //                     theAppMgr.fireButtonEvent("OK");
  //                 }
  //                 else
  //                 {
  //                     theAppMgr.showErrorDlg(res.getString("Cannot find transaction."));
  //                 }
  //             }
  //             catch(Exception ex)
  //             {
  //                 theAppMgr.showExceptionDlg(ex);
  //             }
  //     }
  /**
   *
   * <p>Title: SortTxnAction</p>
   * <p>Description: Traps keyboard, mouse event to perform sort on columns</p>
   * <p>Copyright: Copyright (c) 2005</p>
   * <p>Company: SkillNet Inc</p>
   * @author Vikram Mundhra
   * @version 1.0
   */
  private class SortTxnAction extends AbstractAction {
    private int iColumnIndex = -1;

    /**
     * put your documentation comment here
     * @param     int iColumnIndex
     */
    public SortTxnAction(int iColumnIndex) {
      this.iColumnIndex = iColumnIndex;
    }

    /**
     * put your documentation comment here
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
      int selectedItemRowNew = modelLoyaltyTransactionDetail.sortByColumnType(iColumnIndex
          , getSelectedItem().getTransactionId());
      if (selectedItemRowNew > -1)
        selectRow(selectedItemRowNew);
    }
  }
}

