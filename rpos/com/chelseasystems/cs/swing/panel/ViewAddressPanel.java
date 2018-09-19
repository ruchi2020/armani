/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import javax.swing.*;
import com.chelseasystems.cs.swing.model.CustomerAddressModel;
import com.chelseasystems.cs.address.Address;
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
import com.chelseasystems.cr.config.ConfigMgr;


/**
 * <p>Title: ViewAddressPanel.java</p>
 *
 * <p>Description: View list of customer Addresses </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company:  Skillnet Inc. </p>
 *
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 04-12-2005 | Khyati  | N/A         | Send Sale                                          |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 03-14-2005 | Manpreet  | N/A       | POS_104665_FS_CustomerManagement_Rev2              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class ViewAddressPanel extends JPanel implements PageNumberGetter {
  private CustomerAddressModel modelAddress;
  private JCMSTable tblAddress;
  private TextRenderer renderer;
  private List listCustomerAddresses;
  private boolean bInitialLoad;
  private AddressPanel addressPanelMainScreen;
  private final int VIEW_EMPTY = -1010010100;
  private int iIndexPrimaryAddress = VIEW_EMPTY;
  //private boolean bInternalUpdate = true;
  private CMSShippingRequest shippingRequest = null;
  private CMSCustomer customer = null;
  private boolean persistRequired = false;
  private boolean updateAllStgTbl=false;

  /**
   * put your documentation comment here
   * @param   AddressPanel addressPanel
   */
  public ViewAddressPanel(AddressPanel addressPanel) {
    bInitialLoad = true;
    modelAddress = new CustomerAddressModel();
    // Reference to main screen address Panel
    // We need this to synch. address from table to main screen.
    this.addressPanelMainScreen = addressPanel;
    // Disable Setting up primary address from main screen
    tblAddress = new JCMSTable(modelAddress, JCMSTable.SELECT_ROW);
    renderer = new TextRenderer();
    this.setLayout(new BorderLayout());
    this.add(tblAddress.getTableHeader(), BorderLayout.NORTH);
    this.add(tblAddress, BorderLayout.CENTER);
    modelAddress.setRowsShown(13); // arbitrarily set until resize occurs
    TableColumnModel modelColumn = tblAddress.getColumnModel();
    for (int i = 0; i < modelAddress.getColumnCount(); i++) {
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
   * ks: Added this to enable this panel to be displayed from send sale screen
   */
  public ViewAddressPanel() {
    bInitialLoad = true;
    modelAddress = new CustomerAddressModel();
    addressPanelMainScreen = null;
    // Reference to main screen address Panel
    // Disable Setting up primary address from main screen
    //    addressPanelMainScreen.setPrimaryEnabled(false);
    tblAddress = new JCMSTable(modelAddress, JCMSTable.SELECT_ROW);
    renderer = new TextRenderer();
    this.setLayout(new BorderLayout());
    this.add(tblAddress.getTableHeader(), BorderLayout.NORTH);
    this.add(tblAddress, BorderLayout.CENTER);
    modelAddress.setRowsShown(13); // arbitrarily set until resize occurs
    TableColumnModel modelColumn = tblAddress.getColumnModel();
    for (int i = 0; i < modelAddress.getColumnCount(); i++) {
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
    ListSelectionModel model = tblAddress.getSelectionModel();
    model.setSelectionInterval(row, row);
  }

  /**
   * put your documentation comment here
   */
  public void resetColumnWidths() {
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    int iWidth = 0;
    TableColumn TypeCol = tblAddress.getColumnModel().getColumn(CustomerAddressModel.TYPE);
    TypeCol.setPreferredWidth((int)(55 * r));
    TableColumn CountryCol = tblAddress.getColumnModel().getColumn(CustomerAddressModel.COUNTRY);
    CountryCol.setPreferredWidth((int)(55 * r));
    TableColumn PostCodeCol = tblAddress.getColumnModel().getColumn(CustomerAddressModel.
        POSTAL_CODE);
    PostCodeCol.setPreferredWidth((int)(75 * r));
    iWidth = tblAddress.getWidth()
        - (TypeCol.getPreferredWidth() + CountryCol.getPreferredWidth()
        + PostCodeCol.getPreferredWidth());
    iWidth = iWidth / 3;
    TableColumn Address1Col = tblAddress.getColumnModel().getColumn(CustomerAddressModel.ADDRESS1);
    TableColumn Address2Col = tblAddress.getColumnModel().getColumn(CustomerAddressModel.ADDRESS2);
    TableColumn StateCol = tblAddress.getColumnModel().getColumn(CustomerAddressModel.CITY_STATE);
    Address1Col.setPreferredWidth(iWidth);
    Address2Col.setPreferredWidth(iWidth);
    StateCol.setPreferredWidth(iWidth);
    modelAddress.setRowsShown(tblAddress.getHeight() / tblAddress.getRowHeight());
    tblAddress.getTableHeader().setPreferredSize(new Dimension((int)r, 50));
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    if (theAppMgr != null) {
      tblAddress.setAppMgr(theAppMgr);
      renderer.setFont(theAppMgr.getTheme().getTextFieldFont());
    }
  }

  // public void updatePrimaryAddress(Address addressFromMainScreen)
  // {
  //   if(iIndexPrimaryAddress == VIEW_EMPTY)
  //     {
  //       if(addressFromMainScreen.getAddressLine1()!=null && addressFromMainScreen.getAddressLine1().length()>1)
  //       {
  //         addAddress(addressFromMainScreen);
  //         iIndexPrimaryAddress =0;
  //       }
  //       return;
  //     }
  //   if(iIndexPrimaryAddress<0 || addressFromMainScreen==null ) return;
  //   bInternalUpdate = false;
  //  // setAddressAt(iIndexPrimaryAddress, addressFromMainScreen);
  //   bInternalUpdate = true;
  // }
  public void setCustomer(CMSCustomer cmsCustomer) {
    modelAddress.clear();
    this.customer = cmsCustomer;
    // Eur- Update all customer address into Staging table
    ConfigMgr custConfigMgr = new ConfigMgr("customer.cfg");
    String strupdateAllStgTbl=custConfigMgr.getString("UPDATE_ALL_CUST_STG_TABLE");
    if(strupdateAllStgTbl!=null && strupdateAllStgTbl.equalsIgnoreCase("true")){
      updateAllStgTbl=true;
    }

    this.listCustomerAddresses = cmsCustomer.getAddresses();
    bInitialLoad = true;
    for (int iCtr = 0; iCtr < listCustomerAddresses.size(); iCtr++) {
      Address add = (Address)listCustomerAddresses.get(iCtr);
      if(updateAllStgTbl){
        add.setIsModified(true);
      }
      addAddress(add);
    }
    bInitialLoad = false;
  }

  /**
   * put your documentation comment here
   * @param shippingRequest
   */
  public void setShippingRequest(CMSShippingRequest shippingRequest) {
    this.shippingRequest = shippingRequest;
  }

  /**
   * put your documentation comment here
   * @param address
   */
  public void addShippingAddress(Address address) {
    this.shippingRequest.setAddress(address);
    modelAddress.addAddress(address);
    selectLastRow();
  }

  /**
   * put your documentation comment here
   * @param address
   */
  public void addAddress(Address address) {
    //    if(modelAddress.getRowCount() < 1)
    //    {
    //      addressPanelMainScreen.setPrimaryEnabled(false);
    //      addressPanelMainScreen.enableAddressType(false);
    //    }
    if (address == null)
      return;
    if (address.isRemove())
      return;
    if (address.isUseAsPrimary() || modelAddress.getRowCount() < 1) {
      address.setUseAsPrimary(true);
      iIndexPrimaryAddress = modelAddress.getRowCount();
      if (addressPanelMainScreen != null) {
        addressPanelMainScreen.setAddress(address);
        addressPanelMainScreen.setPrimary(true);
      }
    }
    modelAddress.addAddress(address);
    if (!bInitialLoad) {
      listCustomerAddresses.add(address);
      // resetPrimaryAddressInList();
      if (address.getAddressType().toUpperCase().indexOf("PRIMARY") != -1
          && modelAddress.getRowCount() > 1) {
        address.setUseAsPrimary(true);
        iIndexPrimaryAddress = modelAddress.getRowCount() - 1;
        if (addressPanelMainScreen != null) {
          addressPanelMainScreen.setAddress(address);
          addressPanelMainScreen.setPrimary(true);
        }
      }
      resetPrimaryAddressInView();
    }
    selectLastRow();
  }

  /**
   * put your documentation comment here
   */
  private void resetPrimaryAddressInList() {
    int iCtr = 0;
    for (iCtr = 0; iCtr < listCustomerAddresses.size(); iCtr++) {
      if (iCtr == iIndexPrimaryAddress)
        continue;
      ((Address)listCustomerAddresses.get(iCtr)).setUseAsPrimary(false);
    }
  }

  /**
   * put your documentation comment here
   */
  private void resetPrimaryAddressInView() {
    int iCtr = 0;
    Vector vecRows = modelAddress.getAllRows();
    for (iCtr = 0; iCtr < vecRows.size(); iCtr++) {
      if (iCtr == iIndexPrimaryAddress)
        ((Address)vecRows.elementAt(iCtr)).setUseAsPrimary(true);
      else
         ((Address)vecRows.elementAt(iCtr)).setUseAsPrimary(false);
    }
  }

  /**
   * put your documentation comment here
   * @exception BusinessRuleException
   */
  public void deleteSelectedAddress()
      throws BusinessRuleException {
    int row = tblAddress.getSelectedRow();
    Address address = getAddressAt(row);
    deleteCustomerAddress(address);
  }

  /**
   * put your documentation comment here
   * @param address
   * @exception BusinessRuleException
   */
  public void deleteCustomerAddress(Address address)
      throws BusinessRuleException {
    modelAddress.removeRowInModel(address);
    modelAddress.fireTableDataChanged();
    //Check if its existing customer
    // Set Modified True for Address
    if (address.getAddressId() != null && address.getAddressId().length() > 0) {
      address.setIsRemove(true);
    } else {
      listCustomerAddresses.remove(address);
    }
    this.selectLastRow();
    //   if(address.isUseAsPrimary() && tblAddress.getRowCount() >0)
    //   {
    //     iIndexPrimaryAddress = 0;
    //     address = getAddressAt(iIndexPrimaryAddress);
    //     address.setUseAsPrimary(true);
    //     addressPanelMainScreen.setAddress(address);
    //     addressPanelMainScreen.setPrimary(true);
    //     resetPrimaryAddressInView();
    //   }
    if (tblAddress.getRowCount() < 1) {
      addressPanelMainScreen.reset();
      iIndexPrimaryAddress = VIEW_EMPTY;
    }
  }

  /**
   * put your documentation comment here
   */
  public void pageUp() {
    modelAddress.prevPage();
    selectFirstRow();
  }

  /**
   * put your documentation comment here
   */
  public void pageDown() {
    modelAddress.nextPage();
    selectLastRow();
  }

  /**
   * put your documentation comment here
   * @param row
   * @return
   */
  public Address getAddressAt(int row) {
    return (modelAddress.getAddressAt(row));
  }

  /**
   * put your documentation comment here
   * @param address
   * @param iRow
   */
  public void setAddress(Address address, int iRow) {
    modelAddress.fireTableDataChanged();
    if (address.getAddressId() != null && address.getAddressId().length() > 0) {
      address.setIsModified(true);
    }
    selectRow(iRow);
    if (address.isUseAsPrimary()) {
      iIndexPrimaryAddress = iRow;
      if (addressPanelMainScreen != null) {
        addressPanelMainScreen.setAddress(address);
        addressPanelMainScreen.setPrimary(true);
      }
      //resetPrimaryAddressInList();
      resetPrimaryAddressInView();
    }
  }

  /**
   * put your documentation comment here
   * @param sAddressType
   * @return
   */
  public int addressTypeExists(String sAddressType) {
    Vector vecRows = modelAddress.getAllRows();
    if (vecRows == null)
      return -1;
    for (int iCtr = 0; iCtr < vecRows.size(); iCtr++) {
      Address address = (Address)vecRows.elementAt(iCtr);
      if (address.getAddressType().equalsIgnoreCase(sAddressType))
        return iCtr;
    }
    return -1;
  }

  /**
   * put your documentation comment here
   * @param sAddressType
   * @return
   */
  public Address getAddressByType(String sAddressType) {
    Vector vecRows = modelAddress.getAllRows();
    if (vecRows == null)
      return null;
    for (int iCtr = 0; iCtr < vecRows.size(); iCtr++) {
      Address address = (Address)vecRows.elementAt(iCtr);
      if (address.getAddressType().equalsIgnoreCase(sAddressType))
        return address;
    }
    return null;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getSelectedRowIndex() {
    return tblAddress.getSelectedRow();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Address getSelectedAddress() {
    if (getSelectedRowIndex() == -1)
      return null;
    return getAddressAt(getSelectedRowIndex());
  }

  /**
   * put your documentation comment here
   */
  public void selectLastRow() {
    int rowCount = tblAddress.getRowCount();
    if (rowCount < 1) {
      modelAddress.prevPage();
    }
    if (rowCount > 0) {
      tblAddress.setRowSelectionInterval(rowCount - 1, rowCount - 1);
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
    modelAddress.clear();
  }

  /**
   * put your documentation comment here
   */
  public void update() {
    modelAddress.fireTableDataChanged();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public CustomerAddressModel getAddressModel() {
    return modelAddress;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public JCMSTable getTable() {
    return (this.tblAddress);
  }

  /**
   * @return
   */
  public int getCurrentPageNumber() {
    return (modelAddress.getCurrentPageNumber());
  }

  /**
   * @return
   */
  public int getTotalPages() {
    return (modelAddress.getPageCount());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getNumberAddresses() {
    return modelAddress.getRowCount();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean isPrimaryAddressPresent() {
    int iCtr = 0;
    Vector vecRows = modelAddress.getAllRows();
    for (iCtr = 0; iCtr < vecRows.size(); iCtr++)
      if (((Address)vecRows.get(iCtr)).isUseAsPrimary())
        return true;
    return false;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Address getPrimaryAddress() {
    int iCtr = 0;
    Vector vecRows = modelAddress.getAllRows();
    for (iCtr = 0; iCtr < vecRows.size(); iCtr++) {
      Address add = (Address)vecRows.get(iCtr);
      if (add.isUseAsPrimary())
        return add;
    }
    return null;
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
        case CustomerAddressModel.TYPE:
        case CustomerAddressModel.COUNTRY:
        case CustomerAddressModel.ADDRESS1:
        case CustomerAddressModel.ADDRESS2:
        case CustomerAddressModel.CITY_STATE:
        case CustomerAddressModel.POSTAL_CODE:
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

