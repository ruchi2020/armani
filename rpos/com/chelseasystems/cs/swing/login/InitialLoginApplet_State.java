/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 4    | 04-29-2005 | Khyati    | N/A       |1.StoreClose out                              |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 3    | 04-29-2005 | Deepak    | N/A       | Modification to set the appropriate screen nm|
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 02-01-2005 | Anand     | N/A       |1.New main menu functionality added or Armani |
 --------------------------------------------------------------------------------------------
 */


package com.chelseasystems.cs.swing.login;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.appmgr.state.*;
import java.util.Date;


/**
 * Comments generated by AppBuilder. Do not modify.
 * 0. com.chelseasystems.cs.swing.menu.PosMenuApplet
 * 1. com.chelseasystems.cs.swing.timecard.TimecardApplet
 * 2. com.chelseasystems.cs.swing.menu.InitialMgmtMenuApplet
 * 3. com.chelseasystems.cs.swing.menu.InitialCustMenuApplet
 * 4. com.chelseasystems.cs.swing.menu.InitialInventoryMenuApplet
 * 5. com.chelseasystems.cs.swing.login.ConsultantSalesApplet
 * 6. com.chelseasystems.cs.swing.pos.InitialSaleApplet
 * 7. com.chelseasystems.cs.swing.transaction.ViewTxnApplet
 * 8. com.chelseasystems.cs.swing.pos.EvenExchangeApplet
 * 9. com.chelseasystems.cs.swing.layaway.LayawayPaymentApplet
 * 10. com.chelseasystems.cs.swing.session.EndofDayApplet
 * 11. com.chelseasystems.cs.swing.returns.InitialReturnApplet
 * 12. com.chelseasystems.cs.swing.pos.ParkApplet
 * 13. com.chelseasystems.cs.swing.pos.IdentifyConsultantApplet
 * 14. com.chelseasystems.cs.swing.session.EndOfSession
 * 15. com.chelseasystems.cs.swing.readings.SalesByPeriodApplet
 * 16. com.chelseasystems.cs.swing.menu.InitialMerchandiseMenuApplet
 * 20. com.chelseasystems.cs.swing.item.ItemLookupListApplet
 */
public class InitialLoginApplet_State {

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int CANCEL(IApplicationManager theAppMgr) throws StateException {
		//
		// put state logic here
		//
		throw new StateException("State change not implemented->CANCEL");
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int CLOCK_IN_OUT(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int CLOCK_IN_OUT_HIDDEN(IApplicationManager theAppMgr) throws StateException {
		return 1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int CUST_MENU(IApplicationManager theAppMgr) throws StateException {
		return 3;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int END_DAY(IApplicationManager theAppMgr) throws StateException {
		return 10;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int END_SESSION(IApplicationManager theAppMgr) throws StateException {
		return 14;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int MANAGEMENT(IApplicationManager theAppMgr) throws StateException {
		return 2;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int OK(IApplicationManager theAppMgr) throws StateException {
		//
		// put state logic here
		//
		throw new StateException("State change not implemented->OK");
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int POS_MENU(IApplicationManager theAppMgr) throws StateException {
		return 0;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int PREV(IApplicationManager theAppMgr) throws StateException {
		// MSB - This state object is never used.
		// theAppMgr.addStateObject("FROM_PREV", "FROM_PREV");
		if (theAppMgr.getGlobalObject("EOD_COMPLETE") != null)
			((InitialLoginApplet) theAppMgr.getAppletManager().getCurrentCMSApplet()).initEODCompleteButtons();
		else
			((InitialLoginApplet) theAppMgr.getAppletManager().getCurrentCMSApplet()).initButton();
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int RE_ENTRY(IApplicationManager theAppMgr) throws StateException {
		//
		// put state logic here
		//
		throw new StateException("State change not implemented->RE_ENTRY");
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int INVENTORY_MENU(IApplicationManager theAppMgr) throws StateException {
		return 4;
	}

	/**
	 * Button does not really exist. Method will be invoked when Eu clicks ScrollerPanel
	 */
	public int CON_SALES(IApplicationManager theAppMgr) throws StateException {
		return 5;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int EVEN_EXCHANGE(IApplicationManager theAppMgr) throws StateException {
		return 8;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int LAY_PAYMENT(IApplicationManager theAppMgr) throws StateException {
		return 9;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int MERCH_RETURN(IApplicationManager theAppMgr) throws StateException {
		return 11;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int RECALL_TXN(IApplicationManager theAppMgr) throws StateException {
		return 12;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int SALE(IApplicationManager theAppMgr) throws StateException {
		return 13;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int ITEM_LOOKUP(IApplicationManager theAppMgr) throws StateException {
		if (theAppMgr.getGlobalObject("EOD_COMPLETE") != null)
			theAppMgr.addStateObject("INQUIRIES_AFTER_EOD", "TRUE");
		return 20;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int CUST_LOOKUP(IApplicationManager theAppMgr) throws StateException {
		theAppMgr.addStateObject("ARM_DIRECTED_FROM", "LOGIN_APPLET");
		theAppMgr.addStateObject("ARM_DIRECT_TO", "LOGIN_APPLET");
		return 17;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int VIEW_TXN(IApplicationManager theAppMgr) throws StateException {
		if (theAppMgr.getGlobalObject("EOD_COMPLETE") != null)
			theAppMgr.addStateObject("INQUIRIES_AFTER_EOD", "TRUE");
		return 7;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int SALES_PER_PERIOD(IApplicationManager theAppMgr) throws StateException {
		return 15;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int MERCH_MENU(IApplicationManager theAppMgr) throws StateException {
		return 16;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int MAIN_LOG_OFF(IApplicationManager theAppMgr) throws StateException {
		//
		// put state logic here
		//
		throw new StateException("State change not implemented->MAIN_LOG_OFF");
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int SHUTDOWN(IApplicationManager theAppMgr) throws StateException {
		//
		// put state logic here
		//
		throw new StateException("State change not implemented->SHUTDOWN");
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int STORE_CLOSEOUT(IApplicationManager theAppMgr) throws StateException {
		theAppMgr.addStateObject("END_OF_STORE_DAY", "END_OF_STORE_DAY");
		return 10;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int CONSULTANT_LOOKUP(IApplicationManager theAppMgr) throws StateException {
		return 13;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int REDEEMABLE_INQUIRY(IApplicationManager theAppMgr) throws StateException {
		if (theAppMgr.getGlobalObject("EOD_COMPLETE") != null)
			theAppMgr.addStateObject("INQUIRIES_AFTER_EOD", "TRUE");
		return 18;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int INQUIRIES(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	/*
	 * public int INQUIRIES (IApplicationManager theAppMgr) throws StateException { return -1; }
	 */
	public int STORE_LOCATOR(IApplicationManager theAppMgr) throws StateException {
		if (theAppMgr.getGlobalObject("EOD_COMPLETE") != null)
			theAppMgr.addStateObject("INQUIRIES_AFTER_EOD", "TRUE");
		return 19;
	}

	/**
	 * @param theAppMgr
	 *            IApplicationManager
	 * @throws StateException
	 * @return int
	 */
	public int CONTINUE_PROCESSING(IApplicationManager theAppMgr) throws StateException {
		theAppMgr.addGlobalObject("ARM_LAST_UNPROCESSED_EOD_DATE", new Date());
		return 5;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int TRANS_MGMT_LOGIN(IApplicationManager theAppMgr) throws StateException {
		return 21;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int MANAGEMENT_LOGIN(IApplicationManager theAppMgr) throws StateException {
		return 2;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param theAppMgr
	 * @return
	 * @throws StateException
	 */
	public int NO_UNIT_PRICE_ITEMS_LIST(IApplicationManager theAppMgr) throws StateException {
		return 22;
	}
}
