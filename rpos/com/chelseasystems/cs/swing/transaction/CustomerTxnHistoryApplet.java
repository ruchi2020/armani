/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.transaction;

import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.swing.panel.TxnHistPanel;
import com.chelseasystems.cs.swing.panel.TxnListHeaderPanel;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.employee.CMSEmployee;
import java.awt.event.*;
import java.awt.*;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyType;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.dlg.*;
import javax.swing.table.DefaultTableCellRenderer;
import com.chelseasystems.cr.payment.PaymentMgr;
import com.chelseasystems.cr.payment.Payment;
import java.text.SimpleDateFormat;
import com.chelseasystems.cs.util.CustomerUtil;
import com.chelseasystems.cs.util.TransactionUtil;
import com.chelseasystems.cr.config.ConfigMgr;
import com.ga.fs.fsbridge.ARMFSBridge;

/**
 * <p>Title:CustomerTxnHistoryApplet </p>
 * <p>Description: Lists Customer's transaction history</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1   | 05-05-2005 |Manpreet   | N/A       | POS_104665_TS_TransactionHistory_Rev0               |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class CustomerTxnHistoryApplet
    extends CMSApplet {
  /**
   * TransactionHistory list panel
   */
  private TxnHistPanel pnlTxnHistory;
  private TxnListHeaderPanel pnlHeader;
  private GenericChooserRow[] availPaymentTypes;
  private DefaultTableCellRenderer renderer;
  private CMSTransactionHeader[] transactionHeaders;
  private CMSCustomer cmsCustomer;
  private SimpleDateFormat dateFormat = com.chelseasystems.cs.util.
      DateFormatUtil.getLocalDateFormat();
  
  private static final String CONFIGURATION_FILE = "pos.cfg";
  private static final ConfigMgr configMgr = new ConfigMgr(CONFIGURATION_FILE);
  private String txnListHeaderPanelString = null;
  private String txnHistPanelClassName = null;

  /**
   * put your documentation comment here
   */
  public void init() {
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   *  Check if customer is not null in else if block
	 *  and control wasn't passed back by OtherTxnHistoryLookupApplet.
   */
  public void start() {
    initMainBtns();
    pnlTxnHistory.clear();
    pnlHeader.reset();
    if (theAppMgr.getStateObject("CUSTOMER_LOOKUP") != null) {
      				cmsCustomer = (CMSCustomer) theAppMgr.getStateObject("CUSTOMER_LOOKUP");
      			  //PCR1326 TransactionHistory fix for Armani Japan
      			  txnListHeaderPanelString = configMgr.getString("TRANSACTION.TXN_LIST_HEADER_PANEL");
      			  if (txnListHeaderPanelString == null) {
      			  			 pnlHeader.setCustomerName(CustomerUtil.getCustomerNameString(cmsCustomer,false));	
      			  } else {
      			  			 pnlHeader.setCustomerName(cmsCustomer.getLastName() + " " + cmsCustomer.getFirstName());
      			  	}
    }
    if (theAppMgr.getStateObject("ARM_TXN_HEADERS") != null) {
    	        transactionHeaders = (CMSTransactionHeader[]) theAppMgr.getStateObject(
               "ARM_TXN_HEADERS");
              pnlHeader.setSearchCriteria( (String) theAppMgr.getStateObject(
               "TITLE_STRING"));
              pnlHeader.setDateRange( (String) theAppMgr.getStateObject("DATE_STRING"));
              populateTotalForCustomer(transactionHeaders);
              theAppMgr.removeStateObject("ARM_TXN_HEADERS");
              theAppMgr.removeStateObject("TITLE_STRING");
              theAppMgr.removeStateObject("DATE_STRING");
              loadBySearchedHeaders();
    }else if (cmsCustomer != null &&
             theAppMgr.getStateObject("ARM_CUST_OTHER_TXNS_LOOKUP") == null) {
    	       loadByCustomerId(cmsCustomer.getId());
     }
    theAppMgr.removeStateObject("ARM_CUST_OTHER_TXNS_LOOKUP");
  }
	
    

  /**
   * put your documentation comment here
   * @param anEvent
   */
  public void appButtonEvent(CMSActionEvent anEvent) {
    String sCommand = anEvent.getActionCommand();
    if (sCommand.equals("TENDER_TYPE")) {
      if (availPaymentTypes == null || availPaymentTypes.length < 1)
        populatePaymentTypes();
      GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr.
          getParentFrame()
          , theAppMgr, availPaymentTypes,
          new String[] {res.getString("Tender Types")
      });
      if (dlg != null) {
        dlg.getTable().getColumnModel().getColumn(0).setCellRenderer(renderer);
        dlg.setVisible(true);
      }
      if (dlg.isOK()) {
        GenericChooserRow row = dlg.getSelectedRow();
        loadByPaymentType( (String) row.getRowKeyData(),
                          (String) dlg.getTable().
                          getModel().getValueAt(dlg.getTable().getSelectedRow(),
                                                0));
//        pnlHeader.setSearchCriteria(res.getString("Tender Type") + ": "
        //          + (String)dlg.getTable().getModel().getValueAt(dlg.getTable().getSelectedRow(), 0));
        //     pnlHeader.setDateRange("All");
      }
    }
    else if (sCommand.equals("SHIPPING")) {
      loadShippingTxns();
    }
    else if (sCommand.equals("FISCAL_SEARCH")) {
      FiscalTxnLookupDlg fiscalDlg = new FiscalTxnLookupDlg(theAppMgr.
          getParentFrame(), theAppMgr);
      fiscalDlg.setVisible(true);
      TransactionSearchString txnSrchStr = fiscalDlg.getTransactionSearchString();
      txnSrchStr.setCustomerId(cmsCustomer.getId());
      if (txnSrchStr.isSearchRequired()) {
        try {
//          txnSrchStr.setStoreId(theStore.getId());
          CMSTransactionHeader[] transactionHeaders = CMSTransactionPOSHelper.
              findBySearchCriteria(
              theAppMgr, txnSrchStr);
          populateTotalForCustomer(transactionHeaders);

          if (transactionHeaders == null || transactionHeaders.length < 1) {
            theAppMgr.showErrorDlg(res.getString(
                "No matching transactions found."));
            return;
          }
          pnlTxnHistory.clear();
          pnlTxnHistory.setTxnHeaders(transactionHeaders);
          String searchTitle = TransactionUtil.getTransactionSearchString(txnSrchStr);
          String dateString = res.getString("Between ")
              +
              dateFormat.format(transactionHeaders[transactionHeaders.length -
                                1].getSubmitDate());
          dateString += res.getString(" and ") +
              dateFormat.format(transactionHeaders[0].getSubmitDate());
          pnlHeader.setSearchCriteria(searchTitle);
          pnlHeader.setDateRange(dateString);
          applySort();
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    else if (sCommand.equals("OTHER")) {
      theAppMgr.addStateObject("ARM_CUST_OTHER_TXNS_LOOKUP", "TRUE");
      if (pnlTxnHistory.getCMSTxnHeaders() != null)
        saveCurrentListToState();
    }
    else if (sCommand.equals("PREV")) {
      theAppMgr.removeStateObject("TXN_CUSTOMER");
    }
  }

  /**
   * put your documentation comment here
   */
  private void populatePaymentTypes() {
    //String[] payments = CMSPaymentMgr.getAllPaymentKeys();
    Payment[] payments = PaymentMgr.getAllPayments();
    availPaymentTypes = new GenericChooserRow[payments.length + 1];
    int j = 0;
    for (int i = 0; i < payments.length; i++) {
      if (!payments[i].getTransactionPaymentName().equals(
          "STORE_VALUE_CREDIT_MEMO")) {
        String searchPredicate = payments[i].getTransactionPaymentName();
        String searchLabel = payments[i].getGUIPaymentName();
        availPaymentTypes[i +
            j] = new GenericChooserRow(new String[] {searchLabel
        }
            , searchPredicate);
      }
      else {
        try {
          Payment storeValue = PaymentMgr.getPayment("STORE_VALUE_CARD");
          String searchLabel = res.getString(storeValue.getGUIPaymentName());
          String searchPredicate = storeValue.getTransactionPaymentName();
          availPaymentTypes[i +
              j] = new GenericChooserRow(new String[] {searchLabel
          }
              , searchPredicate);
          j = 1;
          Payment creditMemo = PaymentMgr.getPayment("CREDIT_MEMO");
          String searchLabel1 = res.getString(creditMemo.getGUIPaymentName());
          String searchPredicate1 = creditMemo.getTransactionPaymentName();
          availPaymentTypes[i +
              j] = new GenericChooserRow(new String[] {searchLabel1
          }
              , searchPredicate1);
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }
  
  /*
   * This method populates total for customer
   * This method is written for PCR 1817 for Armani Europe
   */
  private void populateTotalForCustomer (CMSTransactionHeader transactionHeaders[]) {
	  double totalAmount = 0;
	  if (transactionHeaders != null && transactionHeaders.length > 0) {
		  for (int iCtr = 0; iCtr < transactionHeaders.length; iCtr++) {
			  totalAmount += transactionHeaders[iCtr].getTotalAmountDue().doubleValue();
		  }
	  }
	  pnlHeader.setTotal(new ArmCurrency(totalAmount).formattedStringValue());
  }

  /**
   * put your documentation comment here
   */
  private void loadShippingTxns() {
    try {
      transactionHeaders = CMSTransactionPOSHelper.
          findByCustomerIdAndShippingRequested(theAppMgr
                                               , cmsCustomer.getId());
      pnlTxnHistory.clear();
      pnlHeader.setSearchCriteria(res.getString("Merchandise was shipped"));
      pnlHeader.setDateRange(res.getString("All"));
      populateTotalForCustomer(transactionHeaders);
      if (transactionHeaders.length == 0) {
        theAppMgr.showErrorDlg(res.getString("No matching transactions found."));
        return;
      }
      pnlTxnHistory.setTxnHeaders(transactionHeaders);
      applySort();
    }
    catch (Exception e) {}
  }

  /**
   * put your documentation comment here
   * @param sPaymentType
   */
  private void loadByPaymentType(String sPaymentCode, String sPaymentLabel) {
    try {
      transactionHeaders = CMSTransactionPOSHelper.
          findByCustomerIdAndPaymentType(theAppMgr
                                         , cmsCustomer.getId(), sPaymentCode);
      pnlTxnHistory.clear();
      pnlHeader.setSearchCriteria(res.getString("Tender Type") + ": "
                                  + sPaymentLabel);
      pnlHeader.setDateRange(res.getString("All"));
      populateTotalForCustomer(transactionHeaders);

      if (transactionHeaders.length == 0) {
        theAppMgr.showErrorDlg(res.getString("No matching transactions found."));
        return;
      }
      pnlTxnHistory.setTxnHeaders(transactionHeaders);
      applySort();

    }
    catch (Exception e) {}
  }

  /**
   * put your documentation comment here
   */
  private void loadBySearchedHeaders() {
  	pnlTxnHistory.clear();
    pnlTxnHistory.setTxnHeaders(transactionHeaders);
    applySort();
  }
  
  /**
   * added selectFirstRow() method in else block for Issue #1846
   */

  private void applySort() {
    	String selectedTranHeaderID = ( (String) theAppMgr.getStateObject(
        		"TXN_HIST_SELECTEDTXNID"));
    	Integer sortColumn = ( (Integer) theAppMgr.getStateObject(
        		"TXN_HIST_SORTCOLUMN"));
    	theAppMgr.removeStateObject("TXN_HIST_SELECTEDTXNID");
    	int selectedItemRowNew = -1;
    	if (sortColumn != null) {
    						selectedItemRowNew = pnlTxnHistory.getAddressModel().sortByColumnType(
    						sortColumn.intValue()
    						, selectedTranHeaderID);
    	}else {
    				selectedItemRowNew = pnlTxnHistory.getAddressModel().doInitialSort(
    						selectedTranHeaderID);
    	}
    	if (selectedItemRowNew > -1) {
      pnlTxnHistory.selectRow(selectedItemRowNew);
    	}else {
    		pnlTxnHistory.selectFirstRow();
  		}
  }

  /**
   * put your documentation comment here
   * @param sCustomerId
   * @return
   */
  private boolean loadByCustomerId(String sCustomerId) {
    try {
    	 transactionHeaders = CMSTransactionPOSHelper.findByCustomerIdHeader(
          theAppMgr, sCustomerId);
      String sCustomerNameString = CustomerUtil.getCustomerNameString(cmsCustomer, false);
      populateTotalForCustomer(transactionHeaders);
      if (transactionHeaders == null || transactionHeaders.length<=0)
      {
        theAppMgr.showErrorDlgLater(res.getString("No transactions found for : "));
        return false;
      }
      for (int iCtr = 0; iCtr < transactionHeaders.length; iCtr++) {
        pnlTxnHistory.addTxnHeader(transactionHeaders[iCtr]);
      }
      //PCR1326 TransactionHistory fix for Armani Japan
	  txnListHeaderPanelString = configMgr.getString("TRANSACTION.TXN_LIST_HEADER_PANEL");
	  if (txnListHeaderPanelString == null) {
		  pnlHeader.setSearchCriteria(res.getString("Customer = ") + sCustomerNameString);
      } else {
    	  pnlHeader.setSearchCriteria(res.getString("Customer = ") +
    			  cmsCustomer.getLastName() + " " + cmsCustomer.getFirstName());
      }
      pnlHeader.setDateRange(res.getString("All"));
      applySort();
    }
    catch (Exception e) {}
    return true;
  }

  /**
   * put your documentation comment here
   */
  private void initMainBtns() {
    theAppMgr.showMenu(MenuConst.CUST_TXN_HIST, theOpr);
    selectTransaction();
  }

  /**
   * put your documentation comment here
   */
  private void selectTransaction() {
    theAppMgr.setSingleEditArea(res.getString(
        "Select a transaction to view details"));
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getVersion() {
    return ("$Revision: 1.3 $");
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getScreenName() {
    return (res.getString("Transaction History"));
  }

  /**
   * put your documentation comment here
   * @exception Exception
   */
  private void jbInit() throws Exception {
  	 this.setLayout(new BorderLayout());
    //PCR1326 TransactionHistory fix for Armani Japan
    txnHistPanelClassName = configMgr.getString("TRANSACTION.TXN_HIST_PANEL");
    if (txnHistPanelClassName == null) {
		pnlTxnHistory = new TxnHistPanel();
		} else {
		pnlTxnHistory = (TxnHistPanel)Class.forName(txnHistPanelClassName).newInstance();
		}
    pnlHeader = new TxnListHeaderPanel();
    renderer = new DefaultTableCellRenderer();
    pnlHeader.setPreferredSize(new Dimension(833, 95));
    this.getContentPane().add(pnlHeader, BorderLayout.NORTH);
    this.getContentPane().add(pnlTxnHistory, BorderLayout.CENTER);
    setAppMgr();
    pnlTxnHistory.addMouseListener(new MouseAdapter() {

      /**
       * put your documentation comment here
       * @param me
       */
      public void mouseClicked(MouseEvent me) {
        if (me.getClickCount() != 2)
          return;
        if (pnlTxnHistory.getSelectedTransactionHeader() == null)return;
        try {

          CurrencyType txnCurrencyType =pnlTxnHistory.getSelectedTransactionHeader().getTotalAmountDue().getCurrencyType();
          CurrencyType storeCurrencyType = ((CMSStore)theStore).getCurrencyType();
          //MSB 01/31/06
          //Check if transaction's currency type
          //matches current store's currency type.
          if(! txnCurrencyType.equals(storeCurrencyType))
          {
            theAppMgr.showErrorDlg(res.getString("Transaction can't be loaded, Store doesn't support Transaction's currency type."));
            return;
          }

          PaymentTransaction theTxn = (PaymentTransaction)
              CMSTransactionPOSHelper.findById(
              theAppMgr, pnlTxnHistory.getSelectedTransactionHeader().getId());
          if (theTxn != null)
            theAppMgr.addStateObject("THE_TXN", theTxn);
          if (pnlTxnHistory.getCMSTxnHeaders() != null) {
            theAppMgr.addStateObject("TXN_HEADER_LIST",pnlTxnHistory.getCMSTxnHeaders());
          }
          theAppMgr.addStateObject("TXN_HEADER_ROW",
                                   new Integer(pnlTxnHistory.getSelectedRow()));
          theAppMgr.addStateObject("TITLE_STRING", pnlHeader.getSearchCriteria());
          theAppMgr.addStateObject("DATE_STRING", pnlHeader.getDateRange());
          saveSortColumnToState();
        }
        catch (Exception e) {
          theAppMgr.showExceptionDlg(e);
        }
        theAppMgr.fireButtonEvent("TXN_DETAILS");
      }
    });
  }

  /**
   * This method is used to store the current list of txns to the state objects.
   */
  private void saveCurrentListToState() {
    theAppMgr.addStateObject("ARM_TXN_HEADERS", pnlTxnHistory.getCMSTxnHeaders());
    if (pnlTxnHistory.getSelectedTransactionHeader() != null)
      theAppMgr.addStateObject("TXN_HIST_SELECTEDTXNID",
                               pnlTxnHistory.getSelectedTransactionHeader().getId());
    theAppMgr.addStateObject("TITLE_STRING", pnlHeader.getSearchCriteria());
    theAppMgr.addStateObject("DATE_STRING", pnlHeader.getDateRange());
    saveSortColumnToState();
  }

  /**
   * This method is used to store the SORTED_COLUMN into State object.
   */
  private void saveSortColumnToState() {
	//- MSB 01/30/2006
	// Whenever applet is reloaded SORT_COLUMN gets reset in model.
	// For instance - OtherTxnHistory search was used
	// when control comes back to this applet
	// Model gets reset and hence currentSort column too,
	// so anytime there on, when control goes to another applet
	// SORT_COLUMN = Integer.MAX_VALUE and when control comes back
	// again to applet, sorting results can be different.
	// To make sure SORT_COLUMN is never lost
	// it's kept in state object till control goes back to CustomerManagementApplet.

    Integer sortColumnLast = ( (Integer) theAppMgr.getStateObject(
        "TXN_HIST_SORTCOLUMN"));
    int sortColumnNow = Math.abs(pnlTxnHistory.getAddressModel().
                                 getCurrentSortColumnAndType());

	// If TXN_HIST_SORTCOLUMN exists in state object
	// and current sort column in model = Integer.MAX_VALUE
	// means sorting was performed on the list and SORT_COLUMN
	// was lost due to model being reset.
    if (sortColumnLast != null && sortColumnNow == Integer.MAX_VALUE) {
      theAppMgr.addStateObject("TXN_HIST_SORTCOLUMN", sortColumnLast);
    }
    else {
      theAppMgr.addStateObject("TXN_HIST_SORTCOLUMN",
                               new Integer(pnlTxnHistory.getAddressModel().
                                           getCurrentSortColumnAndType()));
    }
  }

  /**
   * put your documentation comment here
   * @param e
   */
  public void pageDown(MouseEvent e) {
    pnlTxnHistory.pageDown();
    theAppMgr.showPageNumber(e, (pnlTxnHistory.getCurrentPageNumber() + 1)
                             , pnlTxnHistory.getTotalPages());
  }

  /**
   * put your documentation comment here
   * @param e
   */
  public void pageUp(MouseEvent e) {
    pnlTxnHistory.pageUp();
    theAppMgr.showPageNumber(e, (pnlTxnHistory.getCurrentPageNumber() + 1)
                             , pnlTxnHistory.getTotalPages());
  }

  /**
   * put your documentation comment here
   */
  private void setAppMgr() {
    pnlTxnHistory.setAppMgr(theAppMgr);
    pnlHeader.setAppMgr(theAppMgr);
  }

  /**
   * put your documentation comment here
   */
  public void stop() {}

  /**
   * MP: Home pressed at customer display exits transaction with no message
   * @return
   */
  public boolean isHomeAllowed() {
    CMSCompositePOSTransaction cmsTxn = (CMSCompositePOSTransaction) theAppMgr.
        getStateObject(
        "TXN_POS");
    if (cmsTxn == null) {
      return (true);
    }
/*
		 * Added by Yves Agbessi (05-Dec-2017) Handles the posting of the Sign
		 * Off event for Fiscal Solutions Service START--
		 */
		boolean goToHomeView = (theAppMgr
				.showOptionDlg(
						res.getString("Cancel Transaction"),

						res.getString("Are you sure you want to cancel this transaction?")));
		if (goToHomeView) {

			ARMFSBridge.postARMSignOffTransaction((CMSEmployee) theOpr);
		}

		return goToHomeView;
		/*
		 * --END
		 */
  }
}
