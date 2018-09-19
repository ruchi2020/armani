/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.customer;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.swing.bean.*;
import java.awt.event.*;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import javax.swing.*;
import java.awt.*;
import com.chelseasystems.cs.loyalty.*;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.customer.CMSCustomerHelper;
import com.chelseasystems.cs.customer.CreditHistory;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.swing.panel.CreditTenderHistoryPanel;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyType;
import com.chelseasystems.cs.fiscaldocument.*;


/**
 * <p>Title:ItemLookupListApplet </p>
 * <p>Description: Searches items on provided search criteria</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillnetInc</p>
 * @author Megha
 * @version 1.0
 */
public class CreditTenderHistoryApplet extends CMSApplet {
  private CreditTenderHistoryPanel pnlCreditHistory;
  private JPanel pnlHeader;
  private JCMSLabel lblID;
  private JCMSLabel lblFamilyName;
  private JCMSLabel lblCurrentBal;
  private JCMSTextField txtID;
  private JCMSTextField txtFamilyName;
  private JCMSTextField txtCurrentBal;
  private CreditHistory creditHistory[];
  IRepositoryManager theMgr;
  double dResolution = com.chelseasystems.cr.swing.CMSApplet.r;
  Loyalty loyaltyCard;
  private CMSCustomer cmsCust = new CMSCustomer();
  private static final String CONFIGURATION_FILE = "pos.cfg";
  private static final ConfigMgr configMgr = new ConfigMgr(CONFIGURATION_FILE);
  private String custCreditHeaderPanelString = null;
  private FiscalInterface fiscalInterface = null;

  /**
   * put your documentation comment here
   */
  public void stop() {
    // reset();
  }

  /**
   * put your documentation comment here
   */
  public void init() {
    try {
      //Fiscal interface
      ConfigMgr configMgr = new ConfigMgr("fiscal_document.cfg");
      String sClassName = configMgr.getString("FISCAL_DOCUMENT_PRINTER");
      if (sClassName != null) {
        Class cls = Class.forName(sClassName);
        fiscalInterface = (FiscalInterface)cls.newInstance();
      }
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * put your documentation comment here
   */
  public void start() {
    this.reset();
    Integer rowSelected = null;
//    theAppMgr.showMenu(MenuConst.PREV_ONLY, theOpr);
    theAppMgr.showMenu(MenuConst.CREDIT_HISTORY, theOpr);
    cmsCust = (CMSCustomer)theAppMgr.getStateObject("CUSTOMER_LOOKUP");
    //theAppMgr.removeStateObject("CUSTOMER_LOOKUP");
    if (cmsCust == null) {
      CMSCompositePOSTransaction theTxn = (CMSCompositePOSTransaction)theAppMgr.getStateObject(
          "TXN_POS");
      if (theTxn != null) {
        cmsCust = (CMSCustomer)theTxn.getCustomer();
      }
    }
    if (cmsCust != null) {
      //PCR1326 DepositHistory fix for Armani Japan
      custCreditHeaderPanelString = configMgr.getString("CUSTOMER.CUST_DEPOSIT_HIST_HEADER_PANEL");
      if (custCreditHeaderPanelString == null) {
        txtID.setText(cmsCust.getId());
      } else {
        txtID.setText(cmsCust.getLastName() + " " + cmsCust.getFirstName());
      }
      //txtID.setText(cmsCust.getId());
      txtFamilyName.setText(cmsCust.getLastName());
      if (cmsCust.getCustomerBalance() != null) {
        txtCurrentBal.setText(cmsCust.getCustomerBalance().formattedStringValue());
      }
    }
    // If the applet is changed, get the deposit details from the state obj.
    if (theAppMgr.getStateObject("CREDIT_DETAILS") != null) {
      creditHistory = (CreditHistory[])theAppMgr.getStateObject("CREDIT_LIST");
      rowSelected = ((Integer)theAppMgr.getStateObject("TXN_HEADER_ROW"));
      pnlCreditHistory.clear();
      theAppMgr.removeStateObject("CREDIT_LIST");
      theAppMgr.removeStateObject("TXN_HEADER_ROW");
      if (rowSelected == null)
        rowSelected = new Integer(0);
      loadCreditHistory(creditHistory, null, rowSelected);
    } else {
      try {
        if (cmsCust != null) {
          String thisStoreId = ((CMSStore)theStore).getId();
          System.out.println("@@ Store Id " + thisStoreId);

          creditHistory = CMSCustomerHelper.getCreditTenderHistory(theAppMgr, cmsCust.getId()
              , thisStoreId);

//        	if (cmsCust.getCustomerBalance() != null) {
          ArmCurrency totalBalance = new ArmCurrency(0.0);
          for (int i = 0; i < creditHistory.length; i++) {
            totalBalance = totalBalance.add(creditHistory[i].getamount());
          }
          System.out.println("set to " + totalBalance.formattedStringValue());
          txtCurrentBal.setText(totalBalance.formattedStringValue());
          /*this.repaint();*/
//        }

        System.out.println("@@ total histories : " + creditHistory.length);

        for (int i = 0; i < creditHistory.length; i++) {
          creditHistory[i].setCustomer(cmsCust);
        }
      }
      pnlCreditHistory.setItems(creditHistory);
      if (creditHistory != null) {
        SwingUtilities.invokeLater(new Runnable() {

          /**
           */
          public void run() {
            pnlCreditHistory.update();
          }
        });
        //          }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Setting the panel fields
   */
  enter();

  this.addComponentListener(new java.awt.event.ComponentAdapter() {

    /**
     * put your documentation comment here
     * @param e
     */
    public void componentResized(ComponentEvent e) {
      //resize();
    }
  });
}


/**
 * put your documentation comment here
 */
private void reset() {
  pnlCreditHistory.clear();
  txtID.setText("");
  txtFamilyName.setText("");
  txtCurrentBal.setText("");
}

/**
 * For setting the default edit area
 */
private void enter() {
  theAppMgr.setSingleEditArea(res.getString("Select a transaction to view details"));
}

/**
 * put your documentation comment here
 */
private void doPrevious() {
  reset();
  theAppMgr.goBack();
}

/**
 * For Handling Button Events
 * @param anEvent CMSActionEvent
 */
public void appButtonEvent(CMSActionEvent anEvent) {
  String sAction = anEvent.getActionCommand();
  if (sAction.equals("PRINT_CREDIT_HISTORY")) {
    fiscalInterface.printCustomerStatement(creditHistory);
    return;
  }
}

/**
 * put your documentation comment here
 * @param creditHistory[]
 * @return
 */
private int loadCreditHistory(CreditHistory[] creditHistory, Integer sortColumn
    , Integer selectedHistoryRow) {
  pnlCreditHistory.clear();
  pnlCreditHistory.getAddressModel().setItems(creditHistory);

  if (sortColumn != null || selectedHistoryRow == null) {
    int sortColumnIntValue = Integer.MIN_VALUE;
    if (sortColumn != null)
      sortColumnIntValue = sortColumn.intValue();
    pnlCreditHistory.getAddressModel().sortItems(sortColumnIntValue, null);
  }
  pnlCreditHistory.selectRow(selectedHistoryRow.intValue());

  return creditHistory.length;
}

/**
 * put your documentation comment here
 * @return
 */
public String getVersion() {
  return ("$Revision: 1.1 $");
}

/**
 * put your documentation comment here
 * @return
 */
public String getScreenName() {
  return "Credit History";
}

/**
 * Initializes the parameters
 * @throws Exception
 */
private void jbInit()
    throws Exception {

  pnlCreditHistory = new CreditTenderHistoryPanel(theAppMgr);
  pnlHeader = new JPanel();
  lblID = new JCMSLabel();
  lblFamilyName = new JCMSLabel();
  lblCurrentBal = new JCMSLabel();
  txtID = new JCMSTextField();
  txtFamilyName = new JCMSTextField();
  txtCurrentBal = new JCMSTextField();
  this.getContentPane().setLayout(new BorderLayout());
  this.getContentPane().add(pnlCreditHistory, BorderLayout.SOUTH);
  this.setLayout(new BorderLayout());
  pnlHeader.setLayout(new GridBagLayout());
  lblID.setLabelFor(txtID);
  //PCR1326 DepositHistory fix for Armani Japan
  custCreditHeaderPanelString = configMgr.getString("CUSTOMER.CUST_DEPOSIT_HIST_HEADER_PANEL");
  if (custCreditHeaderPanelString == null) {
    lblID.setText("     ID ");
  } else {
    lblID.setText("     Name ");
  }
  //lblID.setText("     ID ");
  txtID.setText("");
  txtID.setEnabled(false);
  lblFamilyName.setLabelFor(txtFamilyName);
  lblFamilyName.setText("FamilyName");
  txtFamilyName.setText("");
  txtFamilyName.setEnabled(false);
  lblCurrentBal.setLabelFor(txtCurrentBal);
  lblCurrentBal.setText("Current Bal.");
  txtCurrentBal.setText("");
  txtCurrentBal.setEnabled(false);
  pnlHeader.setPreferredSize(new Dimension((int)(800 * dResolution), (int)(135 * dResolution)));
  pnlCreditHistory.setPreferredSize(new Dimension((int)(400 * dResolution)
      , (int)(465 * dResolution)));
  this.add(pnlHeader, BorderLayout.NORTH);
  this.add(pnlCreditHistory, BorderLayout.SOUTH);
  //  this.setBackground(theAppMgr.getBackgroundColor());
  pnlHeader.setBackground(theAppMgr.getBackgroundColor());
  pnlCreditHistory.setAppMgr(theAppMgr);
  lblID.setAppMgr(theAppMgr);
  lblID.setFont(theAppMgr.getTheme().getLabelFont());
  txtID.setAppMgr(theAppMgr);
  txtID.setFont(theAppMgr.getTheme().getTextFieldFont());
  lblFamilyName.setAppMgr(theAppMgr);
  lblFamilyName.setFont(theAppMgr.getTheme().getLabelFont());
  txtFamilyName.setAppMgr(theAppMgr);
  txtFamilyName.setFont(theAppMgr.getTheme().getTextFieldFont());
  lblCurrentBal.setAppMgr(theAppMgr);
  lblCurrentBal.setFont(theAppMgr.getTheme().getLabelFont());
  txtCurrentBal.setAppMgr(theAppMgr);
  txtCurrentBal.setFont(theAppMgr.getTheme().getTextFieldFont());
  this.getContentPane().add(pnlHeader, java.awt.BorderLayout.NORTH);
  pnlHeader.add(txtID
      , new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.SOUTHEAST
      , GridBagConstraints.NONE, new Insets( -9, 0, 0, 0), 310, 4));
  pnlHeader.add(lblCurrentBal
      , new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST
      , GridBagConstraints.NONE, new Insets(1, 35, 3, 3), 27, 0));
  pnlHeader.add(txtFamilyName
      , new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
      , GridBagConstraints.NONE, new Insets(0, 30, 4, 105), 310, 4));
  pnlHeader.add(lblID
      , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST
      , GridBagConstraints.NONE, new Insets(0, 1, 0, 0), 6, 4));
  pnlHeader.add(lblFamilyName
      , new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
      , GridBagConstraints.NONE, new Insets(1, 60, 3, 20), 8, 0));
  pnlHeader.add(txtCurrentBal
      , new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
      , GridBagConstraints.NONE, new Insets(2, 30, 2, 14), 169, 4));
  pnlCreditHistory.addMouseListener(new MouseAdapter() {

    /**
     * put your documentation comment here
     * @param e
     */
    public void mouseClicked(MouseEvent e) {
      if (e.getClickCount() != 2) {
        return;
      }
      if (!pnlCreditHistory.getSelectedCreditHistory().getamount().getCurrencyType().equals(((
          CMSStore)theStore).getCurrencyType())) {
        theAppMgr.showErrorDlg(res.getString(
            "Transaction can't be loaded, Store doesn't support Transaction's currency type."));
        return;
      }
      try {
        theAppMgr.addStateObject("CREDIT_LIST", pnlCreditHistory.getCreditHistHeaders());
        theAppMgr.addStateObject("TXN_HEADER_ROW"
            , new Integer(pnlCreditHistory.getSelectedRow()));
        theAppMgr.addStateObject("CREDIT_DETAILS", "CREDIT_DETAILS");
        theAppMgr.fireButtonEvent("CREDIT_DETAILS");
      } catch (Exception e1) {
        theAppMgr.showExceptionDlg(e1);
      }
      pnlCreditHistory.clickEvent(e);
    }
  });
}

/**
 * put your documentation comment here
 */
public void resize() {
  double r = com.chelseasystems.cr.swing.CMSApplet.r;
  lblID.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
  txtID.setPreferredSize(new Dimension((int)(75 * r), (int)(30 * r)));
  lblFamilyName.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
  txtFamilyName.setPreferredSize(new Dimension((int)(75 * r), (int)(30 * r)));
  lblCurrentBal.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
  txtCurrentBal.setPreferredSize(new Dimension((int)(75 * r), (int)(30 * r)));
}

/**
 * put your documentation comment here
 * @param e
 */
public void pageDown(MouseEvent e) {
  int selectedRow = pnlCreditHistory.getAddressModel().getAllRows().indexOf(pnlCreditHistory.
      getSelectedCreditHistory());
  if (selectedRow < 0)
    selectedRow = pnlCreditHistory.getAddressModel().getLastSelectedRow();
  pnlCreditHistory.nextPage();
  theAppMgr.showPageNumber(e, pnlCreditHistory.getCurrentPageNumber() + 1
      , pnlCreditHistory.getTotalPages());
  pnlCreditHistory.selectRowIfInCurrentPage(selectedRow);
}

/**
 * put your documentation comment here
 * @param e
 */
public void pageUp(MouseEvent e) {
  int selectedRow = pnlCreditHistory.getAddressModel().getAllRows().indexOf(pnlCreditHistory.
      getSelectedCreditHistory());
  if (selectedRow < 0)
    selectedRow = pnlCreditHistory.getAddressModel().getLastSelectedRow();
  pnlCreditHistory.prevPage();
  theAppMgr.showPageNumber(e, pnlCreditHistory.getCurrentPageNumber() + 1
      , pnlCreditHistory.getTotalPages());
  pnlCreditHistory.selectRowIfInCurrentPage(selectedRow);
}

/**
 * put your documentation comment here
 * @param sMessage
 * @param sCommand
 * @param iMask
 */
public void setEditArea(String sMessage, String sCommand, int iMask) {
  if (sCommand != null) {
    theAppMgr.setSingleEditArea(sMessage, sCommand, iMask);
  } else {
    theAppMgr.setSingleEditArea(sMessage);
  }
}
}
