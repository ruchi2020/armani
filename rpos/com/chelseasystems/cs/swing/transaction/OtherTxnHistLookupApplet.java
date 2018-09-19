/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.transaction;

import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cs.swing.panel.OtherTxnHistLookupPanel;
import java.awt.BorderLayout;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.util.TransactionUtil;
import com.chelseasystems.cs.pos.TransactionSearchString;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cs.pos.CMSTransactionPOSHelper;
import com.chelseasystems.cs.pos.CMSTransactionHeader;
import com.chelseasystems.cs.customer.CMSCustomer;
import javax.swing.SwingUtilities;
import java.text.SimpleDateFormat;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.ga.fs.fsbridge.ARMFSBridge;
import com.chelseasystems.cs.employee.CMSEmployee;


/**
 * <p>Title:OtherTxnHistLookupApplet </p>
 * <p>Description: Looksup TransactionHistory based on given params</p>
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
 | 2    | 09-12-2005 | Manpreet  | 1003      | set singleEditArea() message                       |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 05-09-2005 | Manpreet  | N/A       | POS_104665_TS_TranactionHistory_Rev0               |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class OtherTxnHistLookupApplet extends CMSApplet {
  private OtherTxnHistLookupPanel pnlSearch;
  private SimpleDateFormat dateFormat;

  /**
   * put your documentation comment here
   */
  public void stop() {}

  /**
   * put your documentation comment here
   */
  public void init() {
    try {
      this.setLayout(new BorderLayout());
      pnlSearch = new OtherTxnHistLookupPanel();
      this.add(pnlSearch, BorderLayout.CENTER);
      pnlSearch.setAppMgr(theAppMgr);
      pnlSearch.populateStoreIdsAndTypes();
      dateFormat = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   */
  public void start() {
    reset();
    theAppMgr.showMenu(MenuConst.SEARCH_PREV, theOpr);
    theAppMgr.setSingleEditArea(res.getString("Enter search criteria and press 'Search'"));
  }

  /**
   * put your documentation comment here
   */
  private void reset() {
    SwingUtilities.invokeLater(new Runnable() {

      /**
       * put your documentation comment here
       */
      public void run() {
        pnlSearch.reset();
        if (theAppMgr.getStateObject("TXN_CUSTOMER") != null) {
          CMSCustomer cmsCustomer = (CMSCustomer)theAppMgr.getStateObject("TXN_CUSTOMER");
          pnlSearch.setCustomerID(cmsCustomer.getId());
          pnlSearch.setCustomerName(cmsCustomer.getFirstName());
          pnlSearch.setFamilyName(cmsCustomer.getLastName());
          pnlSearch.setSelectedStore(theStore.getId());
        }
      }
    });
  }

  /**
   * put your documentation comment here
   * @param anEvent
   */
  public void appButtonEvent(CMSActionEvent anEvent) {
    String sCommand = anEvent.getActionCommand();
    if (sCommand.equals("SEARCH") && pnlSearch.completeAttributes()) {
      TransactionSearchString tString = new TransactionSearchString();
      tString.setCustomerId(pnlSearch.getCustomerID());
      tString.setCustomerFirstName(pnlSearch.getCustomerName());
      tString.setCustomerLastName(pnlSearch.getFamilyName());
      tString.setRegisterId(pnlSearch.getRegisterID());
      tString.setStoreId(pnlSearch.getSelectedStore());
      tString.setCompanyCode(pnlSearch.getCompanyCode());
      tString.setStoreBrand(pnlSearch.getSelectedStoreType());
      tString.setAssociate(pnlSearch.getAssociate());
      tString.setCashier(pnlSearch.getCashier());
      tString.setTransactionType(pnlSearch.getSelectedTxnType());
      tString.setCurrencyCode(pnlSearch.getCurrencyCode());
      tString.setTransactionStartAmount(pnlSearch.getAmountStartRange());
      tString.setTransactionEndAmount(pnlSearch.getAmountEndRange());
      tString.setSku(pnlSearch.getSku());
      tString.setModel(pnlSearch.getModel());
      tString.setStyle(pnlSearch.getStyle());
      tString.setSupplier(pnlSearch.getSupplier());
      tString.setFabric(pnlSearch.getFabric());
      tString.setColor(pnlSearch.getColor());
      tString.setYear(pnlSearch.getYear());
      tString.setSeason(pnlSearch.getSeason());
      tString.setStartDate(pnlSearch.getStartDate());
      tString.setEndDate(pnlSearch.getEndDate());
      try {
        CMSTransactionHeader[] headers = CMSTransactionPOSHelper.findBySearchCriteria(theAppMgr
            , tString);
        if (headers == null || headers.length < 1) {
          theAppMgr.showErrorDlg(res.getString("Cannot find any matching transations."));
          return;
        }
        String dateString = "";
        dateString = res.getString("Between ") + dateFormat.format(headers[headers.length - 1].getSubmitDate());
        dateString += res.getString(" and ") + dateFormat.format(headers[0].getSubmitDate());
        theAppMgr.addStateObject("ARM_TXN_HEADERS", headers);
        theAppMgr.addStateObject("DATE_STRING", dateString);
        System.out.println("tString       :"+tString);
        theAppMgr.addStateObject("TITLE_STRING", TransactionUtil.getTransactionSearchString(tString));
        theAppMgr.goBack();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    if (sCommand.equals("PREV")) {
      //theAppMgr.goBack();
    }
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
    return ("Transaction History");
  }

  /**
   * MP: Home pressed at customer display exits transaction with no message
   * @return
   */
  public boolean isHomeAllowed() {
		CMSCompositePOSTransaction cmsTxn = (CMSCompositePOSTransaction) theAppMgr
				.getStateObject("TXN_POS");
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

