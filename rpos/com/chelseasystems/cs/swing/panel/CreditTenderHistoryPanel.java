/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

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
import javax.swing.table.JTableHeader;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionEvent;
import java.awt.Event;
import com.chelseasystems.cr.swing.MultiLineCellRenderer;
import com.chelseasystems.cs.pos.CMSTransactionPOSHelper;
import com.chelseasystems.cr.pos.PaymentTransaction;
import java.util.ResourceBundle;

import com.chelseasystems.cs.swing.model.CreditTenderHistoryModel;
import com.chelseasystems.cs.customer.CreditHistory;
import java.util.Vector;
import java.util.Enumeration;


/**
 * <p>Title:CreditTenderHistoryPanel </p>
 * <p>Description: View List of Credit Tender</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillnetInc</p>
 * @author Deo
 * @version 1.0
 */
public class CreditTenderHistoryPanel extends JPanel {
  private CreditTenderHistoryModel modelCreditTenderHistory;
  private JCMSTable tblItem;
  private TextRenderer renderer;
  private IApplicationManager theAppMgr;
  public static ResourceBundle res = null;

  /**
   * put your documentation comment here
   * @param   IApplicationManager theAppMgr
   */
  public CreditTenderHistoryPanel(IApplicationManager theAppMgr) {
    this.theAppMgr = theAppMgr;
    try {
      res = ResourceManager.getResourceBundle();
      modelCreditTenderHistory = new CreditTenderHistoryModel(theAppMgr);
      tblItem = new JCMSTable(modelCreditTenderHistory, JCMSTable.SELECT_ROW);
      renderer = new TextRenderer();
      WrapTextRenderer wrapRenderer = new WrapTextRenderer();
      this.setLayout(new BorderLayout());
      this.add(tblItem.getTableHeader(), BorderLayout.NORTH);
      this.add(tblItem, BorderLayout.CENTER);
      tblItem.setAlignmentX(CENTER_ALIGNMENT);
      tblItem.setAlignmentY(CENTER_ALIGNMENT);
      modelCreditTenderHistory.setRowsShown(13); // arbitrarily set until resize occurs
      TableColumnModel modelColumn = tblItem.getColumnModel();
      for (int i = 0; i < modelCreditTenderHistory.getColumnCount(); i++) {
        switch (i) {
          case CreditTenderHistoryModel.DATE:
          case CreditTenderHistoryModel.STORE_ID:
          case CreditTenderHistoryModel.TXN_ID:
          case CreditTenderHistoryModel.TXN_TYPE:
          case CreditTenderHistoryModel.ASSOC:
          case CreditTenderHistoryModel.AMOUNT:
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
      tblItem.getTableHeader().addMouseListener(new MouseAdapter() {

        /**
         * put your documentation comment here
         * @param e
         */
        public void mouseClicked(MouseEvent e) {
          JTableHeader h = (JTableHeader)e.getSource();
          TableColumnModel columnModel = h.getColumnModel();
          int viewColumn = columnModel.getColumnIndexAtX(e.getX());
          int column = columnModel.getColumn(viewColumn).getModelIndex();
          (new SortItemsAction(column)).actionPerformed(new ActionEvent(this
              , ActionEvent.ACTION_PERFORMED, "SortItems"));
        }
      });
      this.requestFocus();
      InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
      String hotKey = null;
      char hotKeyChar = ' ';
      KeyStroke key = null;
      hotKey = modelCreditTenderHistory.getHotKeyArray()[modelCreditTenderHistory.DATE];
      hotKeyChar = (hotKey != null && hotKey.length() > 0) ? hotKey.charAt(0) : 'D';
      key = KeyStroke.getKeyStroke(Character.toUpperCase(hotKeyChar), Event.ALT_MASK);
      inputMap.put(key, "SortByDate");
      getActionMap().put("SortByDate", new SortItemsAction(modelCreditTenderHistory.DATE));
      hotKey = modelCreditTenderHistory.getHotKeyArray()[modelCreditTenderHistory.STORE_ID];
      hotKeyChar = (hotKey != null && hotKey.length() > 0) ? hotKey.charAt(0) : 'S';
      key = KeyStroke.getKeyStroke(Character.toUpperCase(hotKeyChar), Event.ALT_MASK);
      inputMap.put(key, "SortByStoreId");
      getActionMap().put("SortByStoreId", new SortItemsAction(modelCreditTenderHistory.STORE_ID));
      hotKey = modelCreditTenderHistory.getHotKeyArray()[modelCreditTenderHistory.TXN_ID];
      hotKeyChar = (hotKey != null && hotKey.length() > 0) ? hotKey.charAt(0) : 'x';
      key = KeyStroke.getKeyStroke(Character.toUpperCase(hotKeyChar), Event.ALT_MASK);
      inputMap.put(key, "SortByTxnId");
      getActionMap().put("SortByTxnId", new SortItemsAction(modelCreditTenderHistory.TXN_ID));
      hotKey = modelCreditTenderHistory.getHotKeyArray()[modelCreditTenderHistory.TXN_TYPE];
      hotKeyChar = (hotKey != null && hotKey.length() > 0) ? hotKey.charAt(0) : 'y';
      key = KeyStroke.getKeyStroke(Character.toUpperCase(hotKeyChar), Event.ALT_MASK);
      inputMap.put(key, "SortByTxnType");
      getActionMap().put("SortByTxnType", new SortItemsAction(modelCreditTenderHistory.TXN_TYPE));
      hotKey = modelCreditTenderHistory.getHotKeyArray()[modelCreditTenderHistory.ASSOC];
      hotKeyChar = (hotKey != null && hotKey.length() > 0) ? hotKey.charAt(0) : 'A';
      key = KeyStroke.getKeyStroke(Character.toUpperCase(hotKeyChar), Event.ALT_MASK);
      inputMap.put(key, "SortByAssoc");
      getActionMap().put("SortByAssoc", new SortItemsAction(modelCreditTenderHistory.ASSOC));
      hotKey = modelCreditTenderHistory.getHotKeyArray()[modelCreditTenderHistory.AMOUNT];
      hotKeyChar = (hotKey != null && hotKey.length() > 0) ? hotKey.charAt(0) : 'm';
      key = KeyStroke.getKeyStroke(Character.toUpperCase(hotKeyChar), Event.ALT_MASK);
      inputMap.put(key, "SortByAmount");
      getActionMap().put("SortByAmount", new SortItemsAction(modelCreditTenderHistory.AMOUNT));
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
    modelCreditTenderHistory.pageContainingRow(row);
    selectRowIfInCurrentPage(row);
  }

  /**
   * put your documentation comment here
   * @param absoluteRow
   */
  public void selectRowIfInCurrentPage(int absoluteRow) {
    if (absoluteRow < 0)return;
    if (absoluteRow >= modelCreditTenderHistory.getAllRows().size())return;
    int rowInPage = modelCreditTenderHistory.getCurrentPage().indexOf(
        modelCreditTenderHistory.getAllRows().
        elementAt(absoluteRow));
    modelCreditTenderHistory.setLastSelectedRow(absoluteRow);
    if (rowInPage < 0)
      return;
    ListSelectionModel model = tblItem.getSelectionModel();
    model.setSelectionInterval(rowInPage, rowInPage);
  }

  /**
   * put your documentation comment here
   * @param creditHistory[]
   */
  public void setItems(CreditHistory creditHistory[]) {
    modelCreditTenderHistory.setItems(creditHistory);
    selectFirstRow();
    /*(new SortItemsAction(modelCreditTenderHistory.DATE)).actionPerformed(new ActionEvent(this
        , ActionEvent.ACTION_PERFORMED, "SortItems"));*/
  }

  /**
   * put your documentation comment here
   * @param creditHistory[]
   * @param row
   */
  public void setItemsReturn(CreditHistory creditHistory[], int row) {
    modelCreditTenderHistory.setItems(creditHistory);
    this.selectRow(row);
  }

  /**
   * Adds Item to the panel
   * @param loyaltyHistory LoyaltyHistory
   */
  public void addItem(CreditHistory creditHistory) {
    modelCreditTenderHistory.addItem(creditHistory);
  }

  /**
   * put your documentation comment here
   * @exception BusinessRuleException
   */
  public void deleteSelectedItem()
      throws BusinessRuleException {
    int row = tblItem.getSelectedRow();
    CreditHistory creditHis = getItemAt(row);
    deleteItem(creditHis);
  }

  /**
   * Delete Item from the panel
   * @param loyaltyHistory LoyaltyHistory
   * @throws BusinessRuleException
   */
  public void deleteItem(CreditHistory creditHistory)
      throws BusinessRuleException {
    modelCreditTenderHistory.removeRowInModel(creditHistory);
    modelCreditTenderHistory.fireTableDataChanged();
    this.selectLastRow();
  }

  /**
   * put your documentation comment here
   */
  public void pageUp() {
    modelCreditTenderHistory.prevPage();
    selectFirstRow();
  }

  /**
   * put your documentation comment here
   */
  public void pageDown() {
    modelCreditTenderHistory.nextPage();
    selectLastRow();
  }

  /**
   * put your documentation comment here
   * @param row
   * @return
   */
  public CreditHistory getItemAt(int row) {
    return (modelCreditTenderHistory.getItemAt(row));
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
    return tblItem.getSelectedRow();
  }

  /**
   * Gets the Selected Item from the panel
   * @return LoyaltyHistory
   */
  public CreditHistory getSelectedItem() {
    if (getSelectedRowIndex() == -1)
      return null;
    return getItemAt(getSelectedRowIndex());
  }

  /**
   * put your documentation comment here
   */
  public void selectLastRow() {
    int rowCount = tblItem.getRowCount();
    if (rowCount < 1) {
      modelCreditTenderHistory.prevPage();
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
    modelCreditTenderHistory.clear();
  }

  /**
   * put your documentation comment here
   */
  public void update() {
    modelCreditTenderHistory.fireTableDataChanged();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public CreditTenderHistoryModel getAddressModel() {
    return modelCreditTenderHistory;
  }

  /**
   * put your documentation comment here
   */
  public void resetColumnWidths() {
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    int iWidth = 0;
    TableColumn DateCol = tblItem.getColumnModel().getColumn(CreditTenderHistoryModel.DATE);
    DateCol.setPreferredWidth((int)(105 * r));
    TableColumn StoreIDCol = tblItem.getColumnModel().getColumn(CreditTenderHistoryModel.STORE_ID);
    StoreIDCol.setPreferredWidth((int)(65 * r));
    TableColumn TxnIDCol = tblItem.getColumnModel().getColumn(CreditTenderHistoryModel.TXN_ID);
    TxnIDCol.setPreferredWidth((int)(55 * r));
    TableColumn TxnTypeCol = tblItem.getColumnModel().getColumn(CreditTenderHistoryModel.TXN_TYPE);
    TxnTypeCol.setPreferredWidth((int)(55 * r));
    TableColumn CreditsCol = tblItem.getColumnModel().getColumn(CreditTenderHistoryModel.ASSOC);
    CreditsCol.setPreferredWidth((int)(50 * r));
    TableColumn WithdrawlsCol = tblItem.getColumnModel().getColumn(CreditTenderHistoryModel.AMOUNT);
    WithdrawlsCol.setPreferredWidth((int)(55 * r));
    iWidth = tblItem.getWidth()
        - (DateCol.getPreferredWidth() + StoreIDCol.getPreferredWidth()
        + TxnIDCol.getPreferredWidth() + TxnTypeCol.getPreferredWidth()
        + CreditsCol.getPreferredWidth() + WithdrawlsCol.getPreferredWidth());
    this.modelCreditTenderHistory.setRowsShown(tblItem.getHeight() / tblItem.getRowHeight());
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
    return (modelCreditTenderHistory.getCurrentPageNumber());
  }

  /**
   * @return int
   */
  public int getTotalPages() {
    return (modelCreditTenderHistory.getPageCount());
  }

  /**
   * put your documentation comment here
   */
  public void nextPage() {
    modelCreditTenderHistory.nextPage();
  }

  /**
   */
  public void prevPage() {
    modelCreditTenderHistory.prevPage();
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
        case CreditTenderHistoryModel.DATE:
        case CreditTenderHistoryModel.STORE_ID:
        case CreditTenderHistoryModel.TXN_ID:
        case CreditTenderHistoryModel.TXN_TYPE:
        case CreditTenderHistoryModel.ASSOC:
        case CreditTenderHistoryModel.AMOUNT:
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
  public void clickEvent(MouseEvent me) {
    CreditHistory creditHist = modelCreditTenderHistory.getItemAt(tblItem.getSelectedRow());
    if (creditHist != null)
      try {
        PaymentTransaction theTxn = (PaymentTransaction)CMSTransactionPOSHelper.findById(theAppMgr
            , creditHist.getTransactionId());
        if (theTxn != null) {
          theAppMgr.addStateObject("THE_TXN", theTxn);
          theAppMgr.fireButtonEvent("OK");
        } else {
          theAppMgr.showErrorDlg(res.getString("Cannot find transaction."));
        }
      } catch (Exception ex) {
        theAppMgr.showExceptionDlg(ex);
      }
  }

  /**
   * put your documentation comment here
   * @return
   */
  public CreditHistory getSelectedCreditHistory() {
    int row = tblItem.getSelectedRow();
    return (row > -1) ? modelCreditTenderHistory.getCreditHistory(row) : null;
  }

  private class SortItemsAction extends AbstractAction {
    private int columnType = -1;

    /**
     * put your documentation comment here
     */
    public SortItemsAction() {
    }

    /**
     * put your documentation comment here
     * @param     int columnType
     */
    public SortItemsAction(int columnType) {
      this.columnType = columnType;
    }

    /**
     * put your documentation comment here
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
      int selectedItemRowNew = modelCreditTenderHistory.sortItems(columnType, getSelectedItem());
      if (selectedItemRowNew > -1)
        selectRow(selectedItemRowNew);
    }
  }


  /**
   * put your documentation comment here
   * @return
   */
  public CreditHistory[] getCreditHistHeaders() {
    Vector allRows = modelCreditTenderHistory.getAllRows();
    CreditHistory[] allCredits = new CreditHistory[allRows.size()];
    Enumeration e = allRows.elements();
    int i = 0;
    while (e.hasMoreElements())
    	allCredits[i++] = (CreditHistory)e.nextElement();
    return allCredits;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getSelectedRow() {
    if (tblItem.getSelectedRow() < 0)
      return tblItem.getSelectedRow();
    int page = modelCreditTenderHistory.getCurrentPageNumber();
    int row = (page * modelCreditTenderHistory.getRowsShown()) + tblItem.getSelectedRow();
    return row;
  }
}

