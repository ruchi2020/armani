/**
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.transaction;

import com.chelseasystems.cr.appmgr.mask.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.discount.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cr.persist.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.swing.event.*;
import com.chelseasystems.cr.telephone.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cs.customer.*;
import com.chelseasystems.cs.employee.*;
import com.chelseasystems.cs.item.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.store.*;
import com.chelseasystems.cs.swing.dlg.*;
import com.chelseasystems.cs.swing.mask.*;
import com.chelseasystems.cs.swing.menu.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.tree.*;


/**
 * Construct a predicate to generate a list of txns <p>
 * @version 1.0
 */
public class TxnAdHocQueryApplet extends CMSApplet {
  ScrollablePane scrollPane = new ScrollablePane();
  JPanel mainPanel = new JPanel();
  JCMSLabel title = new JCMSLabel();
  JTree contents = new JTree();
  TitledBorder titledBorder1;
  Border border1;
  private GenericChooserRow[] availDiscountTypes;
  private GenericChooserRow[] availTransactionTypes;
  private GenericChooserRow[] availPaymentTypes;
  private GenericChooserRow[] availEmployees;
  private GenericChooserRow[] options;
  private GenericChooserRow[] shippingOptions;
  private AdHocQueryConstraints queryConstraints;
  private Date beginDate;
  private ArmCurrency leastAmount;
  private String beginTime;
  private Persist persist;
  DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
  private boolean bCustLookupDone;

  /**
   */
  public TxnAdHocQueryApplet() {
  }

  /**
   */
  public void init() {
    try {
      jbInit();
      renderer.setHorizontalAlignment(SwingConstants.CENTER);
      loadDiscountTypes();
      loadTransactionTypes();
      loadPaymentTypes();
      loadOperators();
      loadShippingOptions();
      loadOptions();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * @exception Exception
   */
  private void jbInit()
      throws Exception {
    titledBorder1 = new TitledBorder("");
    border1 = BorderFactory.createMatteBorder(4, 4, 4, 4, theAppMgr.getBackgroundColor());
    mainPanel.setBorder(border1);
    this.getContentPane().add(mainPanel);
    title.setAppMgr(theAppMgr);
    title.setText(res.getString("Build Ad Hoc Transaction Query"));
    title.setHorizontalAlignment(SwingConstants.CENTER);
    title.setFont(theAppMgr.getTheme().getTitleFont());
    mainPanel.setLayout(new BorderLayout());
    mainPanel.add(title, BorderLayout.NORTH);
    mainPanel.add(scrollPane, BorderLayout.CENTER);
    mainPanel.setBackground(theAppMgr.getBackgroundColor());
  }

  /**
   */
  public void stop() {
    persist = null;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getScreenName() {
    return (res.getString("Txn Ad Hoc Query"));
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getVersion() {
    return ("$Revision: 1.10.2.3 $");
  }

  /**
   * Start the applet.
   */
  public void start() {
    renewTree();
    CMSCustomer customer = (CMSCustomer)theAppMgr.getStateObject("TXN_CUSTOMER");
    if (customer == null) {
      queryConstraints = (AdHocQueryConstraints)theAppMgr.getStateObject("ADHOC_QUERY");
      if (!bCustLookupDone && queryConstraints == null) {
        theAppMgr.showMenu(MenuConst.QUERY_MANAGEMENT
            , (CMSEmployee)theAppMgr.getStateObject("OPERATOR"));
      } else {
        theAppMgr.showMenu(MenuConst.ADHOC_QUERY_TXN
            , (CMSEmployee)theAppMgr.getStateObject("OPERATOR"));
      }
    } else {
      if (bCustLookupDone) {
        if(queryConstraints==null){
          queryConstraints=new AdHocQueryConstraints();
        }
        queryConstraints.addCustomer(new Constraint(customer.getId()
            , customer.getFirstName() + " " + customer.getLastName() + " "
            + (customer.getTelephone() == null ? "" : customer.getTelephone().getFormattedNumber())
            + ""));
        if (customer != null)
          customer = null;
        bCustLookupDone = false;
      }
      theAppMgr.showMenu(MenuConst.ADHOC_QUERY_TXN
          , (CMSEmployee)theAppMgr.getStateObject("OPERATOR"));
    }
    scrollPane.setViewportView(contents);
    theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
    title.setBackground(theAppMgr.getBackgroundColor());
    reDisplayQuery();
    selectOptions();
  }

  /**
   * callback when <b>Page Down</b> is pressed
   */
  public void pageDown(MouseEvent me) {
    scrollPane.nextPage();
  }

  /**
   * callback when <b>Page Up</b> is pressed
   */
  public void pageUp(MouseEvent me) {
    scrollPane.prevPage();
  }

  /**
   * put your documentation comment here
   */
  private void selectExisting() {
    new Thread() {

      /**
       * put your documentation comment here
       */
      public void run() {
        //showPersistChooserDialog(true);
    	showPersistChooserDialog(false);
        if (persist != null) {
          queryConstraints = (AdHocQueryConstraints)persist.getSerializable();
          theAppMgr.showMenu(MenuConst.ADHOC_QUERY_TXN
              , (CMSEmployee)theAppMgr.getStateObject("OPERATOR"));
          reDisplayQuery();
          selectOptions();
        }
      }
    }.start();
  }

  /**
   * put your documentation comment here
   */
  private void removeExisting() {
    new Thread() {

      /**
       * put your documentation comment here
       */
      public void run() {
        showPersistChooserDialog(false);
        if (persist != null) {
          queryConstraints = (AdHocQueryConstraints)persist.getSerializable();
          theAppMgr.showMenu(MenuConst.ADHOC_QUERY_TXN
              , (CMSEmployee)theAppMgr.getStateObject("OPERATOR"));
          reDisplayQuery();
          selectOptions();
          if (theAppMgr.showOptionDlg(res.getString("Delete Query?")
              , res.getString("Are you sure you want to delete this query?"))) {
            PersistFileServices persistServices = new PersistFileServices(res.getString("QUERIES"));
            persistServices.remove(persist.getFileName());
          }
          queryConstraints = null;
          theAppMgr.showMenu(MenuConst.QUERY_MANAGEMENT
              , (CMSEmployee)theAppMgr.getStateObject("OPERATOR"));
          reDisplayQuery();
          selectOptions();
        }
      }
    }.start();
  }

  /**
   * @param anEvent
   */
  public void appButtonEvent(CMSActionEvent anEvent) {
    selectOptions();
    String sAction = anEvent.getActionCommand();
    if (sAction.equals("CREATE_NEW")) {
      persist = null;
      theAppMgr.showMenu(MenuConst.ADHOC_QUERY_TXN
          , (CMSEmployee)theAppMgr.getStateObject("OPERATOR"));
      queryConstraints = new AdHocQueryConstraints();
      theAppMgr.addStateObject("ARM_ADHOC_CONSTRAINT_MENU","ARM_ADHOC_MENU");
      reDisplayQuery();
      selectOptions();
    } else if (sAction.equals("SELECT_EXISTING")) {
      selectExisting();
    } else if (sAction.equals("REMOVE_EXISTING")) {
      removeExisting();
    } else if (sAction.equals("PREV")) {

      if(theAppMgr.getStateObject("ARM_ADHOC_CONSTRAINT_MENU")!=null)
      {
        theAppMgr.removeStateObject("ARM_ADHOC_CONSTRAINT_MENU");
        theAppMgr.showMenu(MenuConst.QUERY_MANAGEMENT
                           , (CMSEmployee) theAppMgr.getStateObject("OPERATOR"));
        queryConstraints = null;
        reDisplayQuery();
        selectOptions();
        anEvent.consume();
      }
    } else if (sAction.equals("ADD_CONSTRAINT")) {
      GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame()
          , theAppMgr, options, new String[] {res.getString("Constraint Types.")
      });
      dlg.getTable().getColumnModel().getColumn(0).setCellRenderer(renderer);
      dlg.setVisible(true);
      if (dlg.isOK()) {
        handleSelection((String)dlg.getSelectedRow().getRowKeyData());
      }
    } else if (sAction.equals("REMOVE_CONSTRAINTS") || sAction.equals("EDIT_CONSTRAINT")) {
      if (contents.getLastSelectedPathComponent() == null) {
        return;
      }
      String handle = "";
      AHQTreeData data = (AHQTreeData)((DefaultMutableTreeNode)contents.
          getLastSelectedPathComponent()).getUserObject();
      if (data.getObject() instanceof String) {
        if (data.getType() == AHQTreeData.STORE) {
          queryConstraints.removeStore((String)data.getObject());
          handle = "STORE";
        } else if (data.getType() == AHQTreeData.REGISTER) {
          queryConstraints.removeRegister((String)data.getObject());
          handle = "REGISTER";
        }
      } else if (data.getObject() instanceof Boolean) {
        if (data.getType() == AHQTreeData.WITH_SHIPPING_REQUESTS) {
          queryConstraints.setSearchForShippingRequests(null);
          handle = "SHIPPING_REQUESTS";
        } else if (data.getType() == AHQTreeData.WITHOUT_SHIPPING_REQUESTS) {
          queryConstraints.setSearchForWithoutShippingRequests(null);
          handle = "SHIPPING_REQUESTS";
        }
      } else if (data.getObject() instanceof Date[]) {
        if (data.getType() == AHQTreeData.TRANSACTION_DATE) {
          queryConstraints.removeTxnDateRange();
          handle = "DATE";
        } else if (data.getType() == AHQTreeData.TRANSACTION_TIME) {
          queryConstraints.removeTxnTimeRange();
          handle = "TIME_OF_DAY";
        }
      } else if (data.getObject() == null) {
        if (data.getType() == AHQTreeData.DYNAMIC_TRANSACTION_DATE) {
          handle = "DATE";
        }
      } else if (data.getObject() instanceof ArmCurrency[]) {
        if (data.getType() == AHQTreeData.TRANSACTION_AMOUNT) {
          queryConstraints.removeAmountRange();
          handle = "AMOUNT";
        }
      } else if (data.getObject() instanceof Constraint) {
        String display = ((Constraint)data.getObject()).key;
        switch (data.getType()) {
          case (AHQTreeData.CUSTOMER):
            queryConstraints.removeCustomer(display);
            theAppMgr.removeStateObject("TXN_CUSTOMER");
            handle = "CUSTOMER";
            break;
          case (AHQTreeData.DISCOUNT):
            queryConstraints.removeDiscount(display);
            handle = "DISCOUNT";
            break;
          case (AHQTreeData.ITEM):
            queryConstraints.removeItem(display);
            handle = "ITEM";
            break;
          case (AHQTreeData.OPERATOR):
            queryConstraints.removeOperator(display);
            handle = "OPERATOR_SEARCH";
            break;
          case (AHQTreeData.CONSULTANT):
            queryConstraints.removeConsultant(display);
            handle = "CONSULTANT_SEARCH";
            break;
          case (AHQTreeData.PAYMENT_TYPE):
            queryConstraints.removePaymentType(display);
            handle = "PMT_TYPE";
            break;
          case (AHQTreeData.TRANSACTION_TYPE):
            queryConstraints.removeTxnType(display);
            handle = "TXN_TYPE";
            break;
        }
      }
      if (sAction.equals("EDIT_CONSTRAINT")) {
        handleSelection(handle);
      } else {
        reDisplayQuery();
      }
    } else if (sAction.equals("SAVE")) {
      if (queryConstraints.getTxnBeginDate() == null) {
        theAppMgr.showErrorDlg(res.getString("You must include a date range in your query."));
        anEvent.consume();
        return;
      }
      if (persist != null
          && theAppMgr.showOptionDlg(res.getString("Overwrite Query?")
          , res.getString("Do you want to overwrite the existing query?"))) {
        editAreaEvent("PERSIST_COMMENT", persist.getComment());
      } else {
        persist = null;
        theAppMgr.setSingleEditArea(res.getString("Enter query name."), "PERSIST_COMMENT");
      }
    } else if (sAction.equals("CLEAR_ALL")) {
      queryConstraints = new AdHocQueryConstraints();
      if (theAppMgr.getStateObject("TXN_CUSTOMER") != null) {
        theAppMgr.removeStateObject("TXN_CUSTOMER");
      }
      if (persist != null) {
        persist.setSerializable(queryConstraints);
      }
      reDisplayQuery();
    } else if (sAction.equals("SUBMIT")) {
      if (queryConstraints.getTxnBeginDate() == null) {
        theAppMgr.showErrorDlg(res.getString("You must include a date range in your query."));
        anEvent.consume();
        return;
      }
      CMSTransactionHeader[] transactionHeaders;
      try {
        transactionHeaders = CMSTransactionPOSHelper.findByAdHocQueryConstraints(theAppMgr
            , queryConstraints);
      } catch (Exception e) {
        theAppMgr.showExceptionDlg(e);
        return;
      }
      if (transactionHeaders == null || transactionHeaders.length == 0) {
        theAppMgr.showErrorDlg(res.getString("Cannot find any matching transations."));
        anEvent.consume();
      } else {
        theAppMgr.addStateObject("ADHOC_QUERY", queryConstraints);
        theAppMgr.addStateObject("TXN_HEADER", transactionHeaders);
        String dateString = null;
        if (queryConstraints.isDynamicDate()) {
          dateString = res.getString("Today");
        } else {
          SimpleDateFormat formater = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
          dateString = formater.format(queryConstraints.getTxnBeginDate()) + " "
              + res.getString("to") + " " + formater.format(queryConstraints.getTxnEndDate());
        }
        theAppMgr.addStateObject("DATE_STRING", dateString);
        theAppMgr.addStateObject("TITLE_STRING", res.getString("All Transactions"));
      }
    }
  }

  /**
   * @param selection
   */
  private void handleSelection(String selection) {
    if (selection.equals("PMT_TYPE")) {
      GenericChooserRow[] remainingPaymentTypes = getRemainingChoices(availPaymentTypes
          , queryConstraints.getPaymentTypes());
      GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame()
          , theAppMgr, remainingPaymentTypes, new String[] {res.getString("Payment Types")
      });
      dlg.getTable().getColumnModel().getColumn(0).setCellRenderer(renderer);
      dlg.setVisible(true);
      if (dlg.isOK()) {
        GenericChooserRow row = dlg.getSelectedRow();
        queryConstraints.addPaymentType(new Constraint(row.getRowKeyData().toString()
            , row.getDisplayRow()[0].toString()));
        reDisplayQuery();
      }
    } else if (selection.equals("TXN_TYPE")) {
      GenericChooserRow[] remainingPaymentTypes = getRemainingChoices(availTransactionTypes
          , queryConstraints.getTxnTypes());
      GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame()
          , theAppMgr, remainingPaymentTypes, new String[] {res.getString("Transaction Types")
      });
      dlg.getTable().getColumnModel().getColumn(0).setCellRenderer(renderer);
      dlg.setVisible(true);
      if (dlg.isOK()) {
        GenericChooserRow row = dlg.getSelectedRow();
        queryConstraints.addTxnType(new Constraint(row.getRowKeyData().toString()
            , row.getDisplayRow()[0].toString()));
        reDisplayQuery();
      }
    } else if (selection.equals("DISCOUNT")) {
      GenericChooserRow[] remainingDiscountTypes = getRemainingChoices(availDiscountTypes
          , queryConstraints.getDiscounts());
      GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame()
          , theAppMgr, remainingDiscountTypes, new String[] {res.getString("Discount Types")
      });
      dlg.getTable().getColumnModel().getColumn(0).setCellRenderer(renderer);
      dlg.setVisible(true);
      if (dlg.isOK()) {
        GenericChooserRow row = dlg.getSelectedRow();
        queryConstraints.addDiscount(new Constraint(row.getRowKeyData().toString()
            , row.getDisplayRow()[0].toString()));
        reDisplayQuery();
      }
    } else if (selection.equals("SHIPPING_REQUESTS")) {
      GenericChooserRow[] remainingShippingOptions = getRemainingShippingOptions();
      GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame()
          , theAppMgr, remainingShippingOptions, new String[] {res.getString("Shipping Options")
      });
      dlg.getTable().getColumnModel().getColumn(0).setCellRenderer(renderer);
      dlg.setVisible(true);
      if (dlg.isOK()) {
        String key = dlg.getSelectedRow().getRowKeyData().toString();
        if (key.equals("WITH_SHIPPING_REQUESTS")) {
          queryConstraints.setSearchForShippingRequests(new Boolean(true));
        } else if (key.equals("WITHOUT_SHIPPING_REQUESTS")) {
          queryConstraints.setSearchForWithoutShippingRequests(new Boolean(true));
        }
        reDisplayQuery();
      }
    } else if (selection.equals("DATE")) {
      theAppMgr.setSingleEditArea(res.getString(
          "Enter start date. (MM/DD/YYYY)  or  \"C\" for Calendar."), "BEGIN_DATE", new Date()
          , CMSMaskConstants.CALENDAR_MASK);
    } else if (selection.equals("OPERATOR_SHORT_NAME")) {
      theAppMgr.setSingleEditArea(res.getString("Enter operator user name."), "SHORT_NAME");
    } else if (selection.equals("OPERATOR_SEARCH")) {
      GenericChooserRow[] remainingEmployees = getRemainingChoices(availEmployees
          , queryConstraints.getOperators());
      GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame()
          , theAppMgr, remainingEmployees, new String[] {res.getString("User Name")
          , res.getString("First Name"), res.getString("Last Name")
      });
      dlg.setVisible(true);
      if (dlg.isOK()) {
        Object[] employeeDisplay = dlg.getSelectedRow().getDisplayRow();
        queryConstraints.addOperator(new Constraint((String)dlg.getSelectedRow().getRowKeyData()
            , employeeDisplay[1] + " " + employeeDisplay[2]));
        reDisplayQuery();
      }
    } else if (selection.equals("CONSULTANT_SEARCH")) {
      GenericChooserRow[] remainingConsultants = getRemainingChoices(availEmployees
          , queryConstraints.getConsultants());
      GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame()
          , theAppMgr, remainingConsultants, new String[] {res.getString("User Name")
          , res.getString("First Name"), res.getString("Last Name")
      });
      dlg.setVisible(true);
      if (dlg.isOK()) {
        Object[] consultantDisplay = dlg.getSelectedRow().getDisplayRow();
        queryConstraints.addConsultant(new Constraint((String)dlg.getSelectedRow().getRowKeyData()
            , consultantDisplay[1] + " " + consultantDisplay[2]));
        reDisplayQuery();
      }
    } else if (selection.equals("AMOUNT")) {
      theAppMgr.setSingleEditArea(res.getString(
          "Enter the LEAST amount the transactions should be for."), "LEAST_AMOUNT"
          , theAppMgr.CURRENCY_MASK);
    } else if (selection.equals("ITEM")) {
      theAppMgr.setSingleEditArea(res.getString("Enter or scan item code."), "ITEM"
          , theAppMgr.ITEM_MASK);
    } else if (selection.equals("TIME_OF_DAY")) {
      theAppMgr.setSingleEditArea(res.getString(
          "Enter the EARLIEST time that the transactions should have occured."), "BEGIN_TIME"
          , theAppMgr.TIME_OF_DAY_MASK);
    } else if (selection.equals("REGISTER")) {
      theAppMgr.setSingleEditArea(res.getString(
          "Enter the ID of the register that created the transactions."), "REGISTER_ID");
    } else if (selection.equals("STORE")) {
      theAppMgr.setSingleEditArea(res.getString(
          "Enter the ID of the store where the transaction was made."), "STORE_ID");
    } else if (selection.equals("CUSTOMER")) {
      //theAppMgr.setSingleEditArea(res.getString("Enter the phone number of the customer that the transactions are for."),
      //"CUSTOMER_PHONE", theAppMgr.PHONE_MASK);
      theAppMgr.addStateObject("ARM_DIRECT_TO", "ADHOC_QUERY_APPLET");
      theAppMgr.addStateObject("ARM_DIRECTED_FROM", "ADHOC_QUERY_APPLET");
      bCustLookupDone = true;
      theAppMgr.fireButtonEvent("CUST_LOOKUP");
    }
  }

  /**
   * @param command
   * @param value
   */
  public void editAreaEvent(String command, String value) {
    editAreaEvent(command, (Object)value);
  }

  /**
   * @param command
   * @param value
   */
  public void editAreaEvent(String command, ArmCurrency value) {
    editAreaEvent(command, (Object)value);
  }

  /**
   * @param command
   * @param value
   */
  public void editAreaEvent(String command, Date value) {
    editAreaEvent(command, (Object)value);
  }

  /**
   * put your documentation comment here
   * @param command
   * @param value
   */
  public void editAreaEvent(String command, QuantifiedInput value) {
    editAreaEvent(command, (Object)value);
  }

  /**
   * @param command
   * @param value
   */
  public void editAreaEvent(String command, Object value) {
    //System.out.println("edit area event: " + command);
    try {
      if (command.equals("PERSIST_COMMENT")) {
        PersistFileServices PersistServices = new PersistFileServices(res.getString("QUERIES"));
        if (persist == null) {
          persist = new Persist();
        }
        persist.setSerializable(queryConstraints);
        persist.setComment(value.toString());
        try {
          PersistServices.save(persist);
        } catch (Exception e) {
          theAppMgr.showExceptionDlg(e);
        }
        selectOptions();
        theAppMgr.showMenu(MenuConst.ADHOC_QUERY_TXN
            , (CMSEmployee)theAppMgr.getStateObject("OPERATOR"));
      } else if (command.equals("BEGIN_DATE")) {
        beginDate = (Date)value;
        theAppMgr.setSingleEditArea(res.getString(
            "Enter ending date. (MM/DD/YYYY)  or  \"C\" for Calendar."), "END_DATE", new Date()
            , CMSMaskConstants.CALENDAR_MASK);
      } else if (command.equals("END_DATE")) {
        selectOptions();
        queryConstraints.setTxnDateRange(beginDate, (Date)value);
        reDisplayQuery();
      } else if (command.equals("SHORT_NAME")) {
        selectOptions();
        CMSEmployee emp = CMSEmployeeHelper.findByShortName(theAppMgr, (String)value);
        if (emp == null)
          theAppMgr.showErrorDlg(res.getString("Cannot find employee."));
        else {
          queryConstraints.addOperator(new Constraint(emp.getExternalID()
              , emp.getFirstName() + " " + emp.getLastName() + " (" + emp.getShortName() + ")"));
          reDisplayQuery();
        }
      } else if (command.equals("LEAST_AMOUNT")) {
        leastAmount = (ArmCurrency)value;
        theAppMgr.setSingleEditArea(res.getString(
            "Enter the MOST amount that the transactions should be for."), "MOST_AMOUNT"
            , theAppMgr.CURRENCY_MASK);
      } else if (command.equals("MOST_AMOUNT")) {
        selectOptions();
        queryConstraints.setAmountRange(leastAmount, (ArmCurrency)value);
        reDisplayQuery();
      } else if (command.equals("ITEM")) {
        //             The following three lines will need to be added if findByBarCode
        //              String sItemValue = ((ItemCodeMask.QuantifiedItemId)value).getItemId();
        //              ItemEntryParser itmParser = new ItemEntryParser();
        //             String retailBarcode = itmParser.getRetailBarcode(this, theAppMgr,sItemValue, new Integer(sItemValue.length()).toString());
        CMSItem item = CMSItemHelper.findByBarCode(theAppMgr
            , ((ItemCodeMask.QuantifiedItemId)value).getItemId(), theStore.getId());
        if (item != null) {
          queryConstraints.addItem(new Constraint(item.getId()
              , item.getDescription() + " (" + item.getId() + ")"));
          reDisplayQuery();
        }
        selectOptions();
      } else if (command.equals("BEGIN_TIME")) {
        beginTime = (String)value;
        theAppMgr.setSingleEditArea(res.getString(
            "Enter the LATEST time that the transactions should have occured."), "END_TIME"
            , theAppMgr.TIME_OF_DAY_MASK);
      } else if (command.equals("END_TIME")) {
        selectOptions();
        Calendar beginCal = Calendar.getInstance();
        beginCal.setTime(new Date());
        beginCal.set(Calendar.HOUR_OF_DAY
            , Integer.parseInt(beginTime.substring(0, beginTime.indexOf(":"))));
        beginCal.set(Calendar.MINUTE
            , Integer.parseInt(beginTime.substring(beginTime.indexOf(":") + 1
            , beginTime.indexOf(":") + 3)));
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(new Date());
        endCal.set(Calendar.HOUR_OF_DAY
            , Integer.parseInt(((String)value).substring(0, ((String)value).indexOf(":"))));
        endCal.set(Calendar.MINUTE
            , Integer.parseInt(((String)value).substring(((String)value).indexOf(":") + 1
            , ((String)value).indexOf(":") + 3)));
        queryConstraints.setTxnTimeRange(beginCal.getTime(), endCal.getTime());
        reDisplayQuery();
      } else if (command.equals("REGISTER_ID")) {
        selectOptions();
        queryConstraints.addRegister((String)value);
        reDisplayQuery();
      } else if (command.equals("STORE_ID")) {
        selectOptions();
        queryConstraints.addStore((String)value);
        reDisplayQuery();
      } else if (command.equals("CUSTOMER_PHONE")) {
        CMSCustomer[] customers = (CMSCustomer[])CMSCustomerHelper.findByTelephone(theAppMgr
            , (Telephone)value);
        if (customers == null || customers.length == 0) {
          theAppMgr.setSingleEditArea(res.getString("Enter customer's phone number.")
              , "CUSTOMER_PHONE", theAppMgr.PHONE_MASK);
          theAppMgr.showErrorDlg(res.getString("Cannot find customer."));
          return;
        }
        selectOptions();
        for (int i = 0; i < customers.length; i++) {
          queryConstraints.addCustomer(new Constraint(customers[i].getId()
              , customers[i].getFirstName() + " " + customers[i].getLastName() + " ("
              + (customers[i].getTelephone() == null ? ""
              : customers[i].getTelephone().getFormattedNumber()) + ")"));
        }
        reDisplayQuery();
      }
    } catch (BusinessRuleException r) {
      theAppMgr.showErrorDlg(res.getString(r.getMessage()));
    } catch (TooManySearchResultsException e) {
      theAppMgr.showErrorDlg(res.getString("More than 100 query results"));
    } catch (Exception e) {
      theAppMgr.showExceptionDlg(e);
    }
  }

  /**
   */
  private void selectOptions() {
    theAppMgr.setSingleEditArea(res.getString("Select option."));
  }

  /**
   * @param remoteAlso
   * @return
   */
  private void showPersistChooserDialog(final boolean remoteAlso) {
    try {
      theAppMgr.setWorkInProgress(true);
      Persist[] persists = getPersistedQueries(remoteAlso);
      GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame()
          , theAppMgr, loadPersists(persists), new String[] {res.getString("Query Name")
      });
      dlg.getTable().getColumnModel().getColumn(0).setCellRenderer(renderer);
      theAppMgr.setWorkInProgress(false);
      dlg.setVisible(true);
      if (dlg.isOK()) {
        persist = (Persist)dlg.getSelectedRow().getRowKeyData();
      } else {
        persist = null;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      theAppMgr.setWorkInProgress(false);
    }
  }

  /**
   * @param persists
   * @return
   */
  private GenericChooserRow[] loadPersists(Persist[] persists) {
    GenericChooserRow[] rtnVal = new GenericChooserRow[persists.length];
    for (int idx = 0; idx < persists.length; idx++) {
      rtnVal[idx] = new GenericChooserRow(new String[] {persists[idx].getComment()
      }, persists[idx]);
    }
    return (rtnVal);
  }

  /**
   * @return
   */
  private void loadOptions() {
    GenericChooserRow[] rtnVal = null;
    HashMap map = new HashMap();
    int idx = 0;
    map.put("AMOUNT", res.getString("Amount"));
    map.put("CONSULTANT_SEARCH", res.getString("Associate Pick List"));
    map.put("CUSTOMER", res.getString("Customer"));
    map.put("DATE", res.getString("Date"));
    map.put("DISCOUNT", res.getString("Discount"));
    map.put("ITEM", res.getString("Item"));
    map.put("OPERATOR_SEARCH", res.getString("Cashier Pick List"));
    map.put("OPERATOR_SHORT_NAME", res.getString("Cashier Short Name"));
    map.put("PMT_TYPE", res.getString("Payment Type"));
    map.put("REGISTER", res.getString("HDR_Register"));
    map.put("SHIPPING_REQUESTS", res.getString("Shipping Requests"));
    map.put("STORE", res.getString("HDR_Store"));
    map.put("TIME_OF_DAY", res.getString("Time"));
    map.put("TXN_TYPE", res.getString("Transaction Type"));
    Vector keys = new Vector(map.keySet());
    Collections.sort(keys);
    rtnVal = new GenericChooserRow[keys.size()];
    for (Iterator it = keys.iterator(); it.hasNext(); ) {
      String key = (String)it.next();
      String value = (String)map.get(key);
      rtnVal[idx++] = new GenericChooserRow(new String[] {value
      }, key);
    }
    options = rtnVal;
  }

  /**
   * put your documentation comment here
   */
  private void loadShippingOptions() {
    shippingOptions = new GenericChooserRow[2];
    shippingOptions[0] = new GenericChooserRow(new String[] {res.getString(
        "Transactions that do have Shipping Requests")
    }, "WITH_SHIPPING_REQUESTS");
    shippingOptions[1] = new GenericChooserRow(new String[] {res.getString(
        "Transactions that do NOT have Shipping Requests")
    }, "WITHOUT_SHIPPING_REQUESTS");
  }

  /**
   */
  private void loadDiscountTypes() {
    String[] discountTypes = DiscountMgr.getDiscountTypes();
    availDiscountTypes = new GenericChooserRow[discountTypes.length];
    for (int i = 0; i < discountTypes.length; i++) {
      availDiscountTypes[i] = new GenericChooserRow(new String[] {DiscountMgr.getLabel(
          discountTypes[i])
      }, DiscountMgr.getLabel(discountTypes[i]));
    }
  }

  /**
   * @exception Exception
   */
  private void loadTransactionTypes()
      throws Exception {
    StringTokenizer st;
    INIFile file;
    try {
      file = new INIFile(FileMgr.getLocalFile("config", "pos.cfg"), true);
    } catch (Exception e) {
      theAppMgr.showExceptionDlg(e);
      return;
    }
    String transTypes = file.getValue("TRANS_TYPES");
    st = new StringTokenizer(transTypes, ",");
    availTransactionTypes = new GenericChooserRow[st.countTokens()];
    int i = 0;
    while (st.hasMoreElements()) {
      String key = (String)st.nextElement();
      String transTypeDesc = res.getString(file.getValue(key, "DESC_KEY", ""));
      String transTypeCode = file.getValue(key, "CODE", "");
      availTransactionTypes[i++] = new GenericChooserRow(new String[] {transTypeDesc
      }, transTypeCode);
    }
  }

  /**
   */
  private void loadPaymentTypes() {
    System.out.println("loading payment types");
    String[] payments = PaymentMgr.getReportPayments();
    availPaymentTypes = new GenericChooserRow[payments.length];
    for (int i = 0; i < payments.length; i++) {
      String searchPredicate = payments[i];
      String searchLabel = PaymentMgr.getReportPaymentDesc(payments[i]);
      availPaymentTypes[i] = new GenericChooserRow(new String[] {searchLabel
      }, searchPredicate);
    }
  }

  /**
   */
  private void loadOperators() {
    CMSEmployee[] emps = null;
    try {
      emps = CMSEmployeeHelper.findByStore(theAppMgr, (CMSStore)theStore);
    } catch (Exception e) {
      theAppMgr.showExceptionDlg(e);
    }
    //if(theStore==null)
    //System.out.println("txnAdHocQuery says: store is null");
    if (emps == null) {
      //System.out.println("txnAdHocQuery says: no employees found for store, null returned by service?");
      availEmployees = new GenericChooserRow[0];
    } else
      availEmployees = new GenericChooserRow[emps.length];
    for (int i = 0; i < availEmployees.length; i++) {
      availEmployees[i] = new GenericChooserRow(new String[] {emps[i].getShortName()
          , emps[i].getFirstName(), emps[i].getLastName()
      }, emps[i].getExternalID());
    }
  }

  /**
   * @param availChoices
   * @param usedKeys
   * @return
   */
  private GenericChooserRow[] getRemainingChoices(GenericChooserRow[] availChoices
      , String[] usedKeys) {
    if (usedKeys == null)
      return (availChoices);
    Vector vUsedKey = new Vector(Arrays.asList(usedKeys));
    Vector vRemainingChoices = new Vector();
    for (int i = 0; i < availChoices.length; i++) {
      if (!(vUsedKey.contains(availChoices[i].getRowKeyData().toString())))
        vRemainingChoices.add(availChoices[i]);
    }
    return (GenericChooserRow[])vRemainingChoices.toArray(new GenericChooserRow[vRemainingChoices.
        size()]);
  }

  /**
   * put your documentation comment here
   * @return
   */
  private GenericChooserRow[] getRemainingShippingOptions() {
    Vector keys = new Vector();
    if (queryConstraints.getSearchForShippingRequests() == null) {
      keys.add(shippingOptions[0]);
    }
    if (queryConstraints.getSearchForWithoutShippingRequests() == null) {
      keys.add(shippingOptions[1]);
    }
    return (GenericChooserRow[])keys.toArray(new GenericChooserRow[keys.size()]);
  }

  /**
   */
  private void reDisplayQuery() {
    ((AHQTreeModel)contents.getModel()).constructTree(queryConstraints);
    for (int idx = 0; idx < contents.getRowCount(); idx++) {
      contents.expandRow(idx);
    }
    contents.requestDefaultFocus();
  }

  /**
   */
  private void renewTree() {
    contents = new JTree(new AHQTreeModel());
    DefaultTreeSelectionModel model = new DefaultTreeSelectionModel();
    model.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    contents.setSelectionModel(model);
    contents.setCellRenderer(new AHQRenderer());
    contents.addTreeWillExpandListener(new AHQTreeWillExpandListener());
    contents.setRowHeight(30);
  }

  /**
   * @param remoteAlso
   * @return
   */
  private Persist[] getPersistedQueries(boolean remoteAlso) {
    ArrayList queries = new ArrayList();
    try {
      // get local
      if (!remoteAlso || !theAppMgr.isOnLine()) {
        PersistFileServices persistServices = new PersistFileServices(res.getString("QUERIES"));
        queries.addAll(Arrays.asList(persistServices.load(AdHocQueryConstraints.class)));
      } else if (theAppMgr.isOnLine() && remoteAlso) {
        // get remotes
        IPersistRmiServer persistServer = null;
        Remote[] rmts = theAppMgr.getPeerStubs("persist");
        if (rmts != null) {
          for (int i = 0; i < rmts.length; i++) {
            try {
              persistServer = (IPersistRmiServer)rmts[i];
              persistServer.setDirectory(res.getString("QUERIES"));
              queries.addAll(Arrays.asList(persistServer.load(AdHocQueryConstraints.class)));
            } catch (Exception e) {}
          }
        }
      }
    } catch (Exception ex) {
      theAppMgr.showExceptionDlg(ex);
    }
    return ((Persist[])queries.toArray(new Persist[0]));
  }

  private class AHQTreeModel extends DefaultTreeModel {

    /**
     */
    public AHQTreeModel() {
      super(new DefaultMutableTreeNode("", true), true);
    }

    /**
     * @param constraints
     */
    public void constructTree(AdHocQueryConstraints constraints) {
      if (constraints != null
          && ((constraints.getAtLeastAmount() != null && constraints.getNotMoreThanAmount() != null)
          || ((constraints.getTxnBeginDate() != null && constraints.getTxnEndDate() != null)
          || constraints.isDynamicDate())
          || (constraints.getTxnBeginTime() != null && constraints.getTxnEndTime() != null)
          || constraints.getCustomerConstraints() != null || constraints.getDiscountConstraints() != null
          || constraints.getItemConstraints() != null || constraints.getOperatorConstraints() != null
          || constraints.getConsultantConstraints() != null
          || constraints.getPaymentTypeConstraints() != null || constraints.getRegisters() != null
          || constraints.getStores() != null || constraints.getTxnTypeConstraints() != null
          || constraints.getSearchForShippingRequests() != null
          || constraints.getSearchForWithoutShippingRequests() != null)) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new AHQTreeData(AHQTreeData.PREFIX, null
            , res.getString("Select all Transactions WHERE")));
        setRoot(root);
        if (constraints.getAtLeastAmount() != null && constraints.getNotMoreThanAmount() != null) {
          insertNodeInto(new DefaultMutableTreeNode(new AHQTreeData(AHQTreeData.TRANSACTION_AMOUNT
              , new ArmCurrency[] {constraints.getAtLeastAmount(), constraints.getNotMoreThanAmount()
          }, getPrefix(root))), root, root.getChildCount());
        }
        if (constraints.isDynamicDate()) {
          insertNodeInto(new DefaultMutableTreeNode(new AHQTreeData(AHQTreeData.
              DYNAMIC_TRANSACTION_DATE, null, getPrefix(root))), root, root.getChildCount());
        } else if (constraints.getTxnBeginDate() != null && constraints.getTxnEndDate() != null) {
          insertNodeInto(new DefaultMutableTreeNode(new AHQTreeData(AHQTreeData.TRANSACTION_DATE
              , new Date[] {constraints.getTxnBeginDate(), constraints.getTxnEndDate()
          }, getPrefix(root))), root, root.getChildCount());
        }
        if (constraints.getTxnBeginTime() != null && constraints.getTxnEndTime() != null) {
          insertNodeInto(new DefaultMutableTreeNode(new AHQTreeData(AHQTreeData.TRANSACTION_TIME
              , new Date[] {constraints.getTxnBeginTime(), constraints.getTxnEndTime()
          }, getPrefix(root))), root, root.getChildCount());
        }
        if (constraints.getSearchForShippingRequests() != null
            || constraints.getSearchForWithoutShippingRequests() != null) {
          if (constraints.getSearchForShippingRequests() != null
              && constraints.getSearchForWithoutShippingRequests() != null) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(new AHQTreeData(AHQTreeData.
                WITH_SHIPPING_REQUESTS, constraints.getSearchForShippingRequests(), getPrefix(root)));
            insertNodeInto(new DefaultMutableTreeNode(new AHQTreeData(AHQTreeData.
                WITHOUT_SHIPPING_REQUESTS, constraints.getSearchForWithoutShippingRequests()
                , res.getString("OR"))), node, node.getChildCount());
            insertNodeInto(node, root, root.getChildCount());
          } else {
            if (constraints.getSearchForShippingRequests() != null) {
              insertNodeInto(new DefaultMutableTreeNode(new AHQTreeData(AHQTreeData.
                  WITH_SHIPPING_REQUESTS, constraints.getSearchForShippingRequests()
                  , getPrefix(root))), root, root.getChildCount());
            }
            if (constraints.getSearchForWithoutShippingRequests() != null) {
              insertNodeInto(new DefaultMutableTreeNode(new AHQTreeData(AHQTreeData.
                  WITHOUT_SHIPPING_REQUESTS, constraints.getSearchForWithoutShippingRequests()
                  , getPrefix(root))), root, root.getChildCount());
            }
          }
        }
        handleConstraints(constraints.getCustomerConstraints(), AHQTreeData.CUSTOMER);
        handleConstraints(constraints.getDiscountConstraints(), AHQTreeData.DISCOUNT);
        handleConstraints(constraints.getItemConstraints(), AHQTreeData.ITEM);
        handleConstraints(constraints.getOperatorConstraints(), AHQTreeData.OPERATOR);
        handleConstraints(constraints.getConsultantConstraints(), AHQTreeData.CONSULTANT);
        handleConstraints(constraints.getPaymentTypeConstraints(), AHQTreeData.PAYMENT_TYPE);
        handleConstraints(constraints.getRegisters(), AHQTreeData.REGISTER);
        handleConstraints(constraints.getStores(), AHQTreeData.STORE);
        handleConstraints(constraints.getTxnTypeConstraints(), AHQTreeData.TRANSACTION_TYPE);
      } else {
        setRoot(new DefaultMutableTreeNode());
      }
    }

    /**
     * @param node
     * @return
     */
    private String getPrefix(DefaultMutableTreeNode node) {
      String prefix = "";
      if (node.getChildCount() > 0) {
        prefix = res.getString("AND");
      }
      return (prefix);
    }

    /**
     * @param constraints
     * @param type
     */
    private void handleConstraints(Object[] constraints, int type) {
      DefaultMutableTreeNode root = (DefaultMutableTreeNode)getRoot();
      if (constraints != null && constraints.length > 0) {
        DefaultMutableTreeNode child = new DefaultMutableTreeNode(new AHQTreeData(type
            , constraints[0], getPrefix(root)));
        insertNodeInto(child, root, root.getChildCount());
        for (int idx = 1; idx < constraints.length; idx++) {
          DefaultMutableTreeNode grandchild = new DefaultMutableTreeNode(new AHQTreeData(type
              , constraints[idx], res.getString("OR")));
          insertNodeInto(grandchild, child, child.getChildCount());
        }
      }
    }
  }


  private class AHQTreeWillExpandListener implements TreeWillExpandListener {

    /**
     * put your documentation comment here
     * @param event
     * @exception ExpandVetoException
     */
    public void treeWillExpand(TreeExpansionEvent event)
        throws ExpandVetoException {}

    /**
     * put your documentation comment here
     * @param event
     * @exception ExpandVetoException
     */
    public void treeWillCollapse(TreeExpansionEvent event)
        throws ExpandVetoException {
      throw new ExpandVetoException(event);
    }
  }


  private class AHQRenderer extends DefaultTreeCellRenderer {

    /**
     * put your documentation comment here
     * @param tree
     * @param value
     * @param selected
     * @param expanded
     * @param leaf
     * @param row
     * @param hasFocus
     * @return
     */
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected
        , boolean expanded, boolean leaf, int row, boolean hasFocus) {
      JLabel component = (JLabel)super.getTreeCellRendererComponent(tree, value, selected, expanded
          , leaf, row, hasFocus);
      Font font = theAppMgr.getTheme().getMessageFont();
      component.setFont(new Font(font.getName(), font.getStyle(), 18));
      component.setText(value.toString());
      component.setIcon(null);
      return (component);
    }
  }


  private class AHQTreeData {
    private static final int CUSTOMER = 0;
    private static final int DISCOUNT = 1;
    private static final int ITEM = 2;
    private static final int OPERATOR = 3;
    private static final int PAYMENT_TYPE = 4;
    private static final int REGISTER = 5;
    private static final int STORE = 6;
    private static final int TRANSACTION_AMOUNT = 7;
    private static final int TRANSACTION_DATE = 8;
    private static final int TRANSACTION_TIME = 9;
    private static final int TRANSACTION_TYPE = 10;
    private static final int CONSULTANT = 11;
    private static final int WITH_SHIPPING_REQUESTS = 12;
    private static final int WITHOUT_SHIPPING_REQUESTS = 13;
    private static final int DYNAMIC_TRANSACTION_DATE = 14;
    private static final int PREFIX = 99;
    private static final String OR = "OR";
    private int type;
    private Object object;
    private String prefix;

    /**
     * @param       int type
     * @param       Object object
     * @param       String prefix
     */
    public AHQTreeData(int type, Object object, String prefix) {
      setType(type);
      setObject(object);
      this.prefix = prefix;
    }

    /**
     * @return
     */
    public int getType() {
      return (type);
    }

    /**
     * @return
     */
    public Object getObject() {
      return (object);
    }

    /**
     * @param type
     */
    public void setType(int type) {
      if (type < CUSTOMER || type > PREFIX) {
        throw new IllegalArgumentException();
      }
      this.type = type;
    }

    /**
     * @param object
     */
    public void setObject(Object object) {
      switch (type) {
        case (STORE):
        case (REGISTER):
          if (!(object instanceof String)) {
            throw new IllegalArgumentException();
          }
          break;
        case (CUSTOMER):
        case (DISCOUNT):
        case (ITEM):
        case (OPERATOR):
        case (CONSULTANT):
        case (PAYMENT_TYPE):
        case (TRANSACTION_TYPE):
          if (!(object instanceof Constraint)) {
            throw new IllegalArgumentException();
          }
          break;
        case (TRANSACTION_AMOUNT):
          if (!(object instanceof ArmCurrency[])) {
            throw new IllegalArgumentException();
          }
          break;
        case (TRANSACTION_DATE):
        case (TRANSACTION_TIME):
          if (!(object instanceof Date[])) {
            throw new IllegalArgumentException();
          }
          break;
        case (WITH_SHIPPING_REQUESTS):
        case (WITHOUT_SHIPPING_REQUESTS):
          if (!(object instanceof Boolean)) {
            throw new IllegalArgumentException();
          }
          break;
        case (PREFIX):
          if (object != null) {
            throw new IllegalArgumentException();
          }
          break;
        case (DYNAMIC_TRANSACTION_DATE):
          if (object != null) {
            throw new IllegalArgumentException();
          }
          break;
      }
      this.object = object;
    }

    /**
     * @return
     */
    public String getPrefix() {
      StringBuffer rtnVal = new StringBuffer();
      rtnVal.append(prefix);
      if (type == PREFIX) {
        return (rtnVal.toString());
      }
      if (prefix != null && !prefix.equalsIgnoreCase("")) {
        rtnVal.append(" ");
      }
      switch (type) {
        case (STORE):
          rtnVal.append(res.getString("The Store ID is"));
          break;
        case (REGISTER):
          rtnVal.append(res.getString("The Register ID is"));
          break;
        case (CUSTOMER):
          rtnVal.append(res.getString("The Customer is"));
          break;
        case (DISCOUNT):
          rtnVal.append(res.getString("The Discount is"));
          break;
        case (ITEM):
          rtnVal.append(res.getString("Items sold include"));
          break;
        case (OPERATOR):
          rtnVal.append(res.getString("The Cashier Operator is"));
          break;
        case (CONSULTANT):
          rtnVal.append(res.getString("The Consultant is"));
          break;
        case (PAYMENT_TYPE):
          rtnVal.append(res.getString("The Payment type is"));
          break;
        case (TRANSACTION_AMOUNT):
          rtnVal.append(res.getString("The Amount is between"));
          break;
        case (DYNAMIC_TRANSACTION_DATE):
          rtnVal.append(res.getString("The Sale date is today"));
          break;
        case (TRANSACTION_DATE):
          rtnVal.append(res.getString("The Sale date is between"));
          break;
        case (TRANSACTION_TIME):
          rtnVal.append(res.getString("The Sale time is between"));
          break;
        case (TRANSACTION_TYPE):
          rtnVal.append(res.getString("The Transaction is"));
          break;
        case (WITH_SHIPPING_REQUESTS):
          rtnVal.append(res.getString("The Transaction does have Shipping Requests."));
          break;
        case (WITHOUT_SHIPPING_REQUESTS):
          rtnVal.append(res.getString("The Transaction does NOT have Shipping Requests."));
          break;
      }
      return (rtnVal.toString());
    }

    /**
     * @return
     */
    public String getValue() {
      StringBuffer rtnVal = new StringBuffer();
      if (type == PREFIX) {
        return (rtnVal.toString());
      }
      rtnVal.append(" ");
      switch (type) {
        case (STORE):
        case (REGISTER):
          rtnVal.append(((String)object));
          break;
        case (CUSTOMER):
        case (DISCOUNT):
        case (ITEM):
        case (OPERATOR):
        case (CONSULTANT):
        case (PAYMENT_TYPE):
        case (TRANSACTION_TYPE):
          rtnVal.append(((Constraint)object).display);
          break;
        case (TRANSACTION_AMOUNT):
          rtnVal.append(((ArmCurrency[])(object))[0].formattedStringValue());
          rtnVal.append(" ");
          rtnVal.append(res.getString("and"));
          rtnVal.append(" ");
          rtnVal.append(((ArmCurrency[])(object))[1].formattedStringValue());
          break;
        case (TRANSACTION_DATE):
          SimpleDateFormat date = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
          rtnVal.append(date.format(((Date[])(object))[0]));
          rtnVal.append(" ");
          rtnVal.append(res.getString("and"));
          rtnVal.append(" ");
          rtnVal.append(date.format(((Date[])(object))[1]));
          break;
        case (TRANSACTION_TIME):
          SimpleDateFormat time = new SimpleDateFormat(res.getString("hh:mm a"));
          rtnVal.append(time.format(((Date[])(object))[0]));
          rtnVal.append(" ");
          rtnVal.append(res.getString("and"));
          rtnVal.append(" ");
          rtnVal.append(time.format(((Date[])(object))[1]));
          break;
      }
      return (rtnVal.toString());
    }

    /**
     * @return
     */
    public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append(getPrefix());
      buffer.append(getValue());
      return (buffer.toString());
    }
  }
}

