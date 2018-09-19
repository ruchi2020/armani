/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import javax.swing.*;
import com.chelseasystems.cs.swing.model.CustomerCreditCardModel;
import com.chelseasystems.cs.customer.CustomerCreditCard;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import java.awt.Font;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import java.awt.Color;
import java.awt.Component;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import javax.swing.table.TableColumn;
import java.awt.Dimension;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cs.pos.CMSShippingRequest;
import java.util.*;


/**
 * <p>Title: ViewCreditCardPanel.java</p>
 *
 * <p>Description: View list of customer CreditCard </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company:  Skillnet Inc. </p>
 *
 * @author Sumit Krishnan
 * @version 1.0
 */
public class ViewCreditCardPanel extends JPanel implements PageNumberGetter {
  private CustomerCreditCardModel modelCustomerCreditCard;
  private JCMSTable tblCreditCard;
  private TextRenderer renderer;
  private List listCustomerCreditCard;
  private boolean bInitialLoad;
  private final int VIEW_EMPTY = -1010010100;
  private int iIndexPrimaryCreditCard = VIEW_EMPTY;
  private CMSShippingRequest shippingRequest = null;
  private CMSCustomer customer = null;
  private boolean persistRequired = false;

  /**
   * ks: Added this to enable this panel to be displayed from send sale screen
   */
  public ViewCreditCardPanel() {
    bInitialLoad = true;
    modelCustomerCreditCard = new CustomerCreditCardModel();
    tblCreditCard = new JCMSTable(modelCustomerCreditCard, JCMSTable.SELECT_ROW);
    renderer = new TextRenderer();
    this.setLayout(new BorderLayout());
    this.add(tblCreditCard.getTableHeader(), BorderLayout.NORTH);
    this.add(tblCreditCard, BorderLayout.CENTER);
    modelCustomerCreditCard.setRowsShown(13); // arbitrarily set until resize occurs
    TableColumnModel modelColumn = tblCreditCard.getColumnModel();
    for (int i = 0; i < modelCustomerCreditCard.getColumnCount(); i++) {
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
   * @param row
   */
  public void selectRow(int row) {
    ListSelectionModel model = tblCreditCard.getSelectionModel();
    model.setSelectionInterval(row, row);
  }

  /**
   * put your documentation comment here
   */
  public void resetColumnWidths() {
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    int iWidth = 0;
    TableColumn CardType = tblCreditCard.getColumnModel().getColumn(CustomerCreditCardModel.
        CARD_TYPE);
    CardType.setPreferredWidth((int)(55 * r));
    TableColumn CardNumber = tblCreditCard.getColumnModel().getColumn(CustomerCreditCardModel.
        CARD_NUMBER);
    CardNumber.setPreferredWidth((int)(55 * r));
    TableColumn ExpirationDate = tblCreditCard.getColumnModel().getColumn(CustomerCreditCardModel.
        EXPIRATION_DATE);
    ExpirationDate.setPreferredWidth((int)(55 * r));
    TableColumn BillingZipCode = tblCreditCard.getColumnModel().getColumn(CustomerCreditCardModel.
        BILLING_ZIPCODE);
    BillingZipCode.setPreferredWidth((int)(75 * r));
    iWidth = tblCreditCard.getWidth()
        - (CardNumber.getPreferredWidth() + ExpirationDate.getPreferredWidth()
        + BillingZipCode.getPreferredWidth());
    iWidth = iWidth / 3;
    modelCustomerCreditCard.setRowsShown(tblCreditCard.getHeight() / tblCreditCard.getRowHeight());
    tblCreditCard.getTableHeader().setPreferredSize(new Dimension((int)r, 50));
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    if (theAppMgr != null) {
      tblCreditCard.setAppMgr(theAppMgr);
      renderer.setFont(theAppMgr.getTheme().getTextFieldFont());
    }
  }

  /**
   * put your documentation comment here
   * @param creditCard
   */
  public void addCreditCard(CustomerCreditCard creditCard) {
    if (creditCard.isRemove())
      return;
    if (creditCard.isUseAsPrimary() || modelCustomerCreditCard.getRowCount() < 1) {
      creditCard.setUseAsPrimary(true);
      iIndexPrimaryCreditCard = modelCustomerCreditCard.getRowCount();
    }
    modelCustomerCreditCard.addCreditCard(creditCard);
    if (!bInitialLoad) {
      listCustomerCreditCard.add(creditCard);
      if (creditCard.getCreditCardType().toUpperCase().indexOf("PRIMARY") != -1
          && modelCustomerCreditCard.getRowCount() > 1) {
        creditCard.setUseAsPrimary(true);
        iIndexPrimaryCreditCard = modelCustomerCreditCard.getRowCount() - 1;
      }
      resetPrimaryCreditCardInView();
    }
    selectLastRow();
  }

  /**
   * put your documentation comment here
   */
  private void resetPrimaryCreditCardInView() {
    int iCtr = 0;
    Vector vecRows = modelCustomerCreditCard.getAllRows();
    for (iCtr = 0; iCtr < vecRows.size(); iCtr++) {
      if (iCtr == iIndexPrimaryCreditCard)
        ((CustomerCreditCard)vecRows.elementAt(iCtr)).setUseAsPrimary(true);
      else
         ((CustomerCreditCard)vecRows.elementAt(iCtr)).setUseAsPrimary(false);
    }
  }

  // Modify Credit Card
  public CustomerCreditCard modifySelectedCreditCard()
      throws BusinessRuleException {
    int row = tblCreditCard.getSelectedRow();
    CustomerCreditCard creditCard = getCreditCardAt(row);
    creditCard.setIsModified(true);
    modelCustomerCreditCard.removeRowInModel(creditCard);
    return creditCard;
  }

  // Remove Credit Card
  public void deleteSelectedCreditCard()
      throws BusinessRuleException {
    int row = tblCreditCard.getSelectedRow();
    CustomerCreditCard creditCard = getCreditCardAt(row);
    creditCard.setIsRemove(true);
    modelCustomerCreditCard.addVRemoveCC(creditCard);
    deleteCreditCard(creditCard);
    this.selectLastRow();
  }

  /**
   * put your documentation comment here
   * @param creditCard
   * @exception BusinessRuleException
   */
  public void deleteCreditCard(CustomerCreditCard creditCard)
      throws BusinessRuleException {
    modelCustomerCreditCard.removeRowInModel(creditCard);
    modelCustomerCreditCard.fireTableDataChanged();
    //Check if its existing Credit Card
    // Set Modified True for CustomerCreditCard
    if (creditCard.getCreditCardNumber() != null && creditCard.getCreditCardNumber().length() > 0) {
      creditCard.setIsRemove(true);
    } else {
      // Add into List
    }
    this.selectLastRow();
    if (tblCreditCard.getRowCount() < 1) {
      //      addressPanelMainScreen.reset();
      iIndexPrimaryCreditCard = VIEW_EMPTY;
    }
  }

  /**
   * put your documentation comment here
   */
  public void pageUp() {
    modelCustomerCreditCard.prevPage();
    selectFirstRow();
  }

  /**
   * put your documentation comment here
   */
  public void pageDown() {
    modelCustomerCreditCard.nextPage();
    selectLastRow();
  }

  /**
   * put your documentation comment here
   * @param row
   * @return
   */
  public CustomerCreditCard getCreditCardAt(int row) {
    return (modelCustomerCreditCard.getCreditCardAt(row));
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getSelectedRowIndex() {
    return tblCreditCard.getSelectedRow();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public CustomerCreditCard getSelectedCreditCard() {
    if (getSelectedRowIndex() == -1)
      return null;
    return getCreditCardAt(getSelectedRowIndex());
  }

  /**
   * put your documentation comment here
   */
  public void selectLastRow() {
    int rowCount = tblCreditCard.getRowCount();
    if (rowCount < 1) {
      modelCustomerCreditCard.prevPage();
    }
    if (rowCount > 0) {
      tblCreditCard.setRowSelectionInterval(rowCount - 1, rowCount - 1);
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
    modelCustomerCreditCard.clear();
  }

  /**
   * put your documentation comment here
   */
  public void update() {
    modelCustomerCreditCard.fireTableDataChanged();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public CustomerCreditCardModel getCreditCardModel() {
    return modelCustomerCreditCard;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public JCMSTable getTable() {
    return (this.tblCreditCard);
  }

  /**
   * @return
   */
  public int getCurrentPageNumber() {
    return (modelCustomerCreditCard.getCurrentPageNumber());
  }

  /**
   * @return
   */
  public int getTotalPages() {
    return (modelCustomerCreditCard.getPageCount());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getNumberCreditCard() {
    return modelCustomerCreditCard.getRowCount();
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
        setText(value.toString().toUpperCase());
      else
        setText("");
      switch (col) {
        case CustomerCreditCardModel.CARD_NUMBER:
        case CustomerCreditCardModel.EXPIRATION_DATE:
        case CustomerCreditCardModel.BILLING_ZIPCODE:
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
      return (this);
    }
  }


  /**
   * put your documentation comment here
   * @return
   */
  public boolean isshippingRequest() {
    return (this.shippingRequest != null);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean isPersistRequired() {
    return persistRequired;
  }

  /**
   * put your documentation comment here
   * @param persistRequired
   */
  public void setPersistRequired(boolean persistRequired) {
    this.persistRequired = persistRequired;
  }
}

