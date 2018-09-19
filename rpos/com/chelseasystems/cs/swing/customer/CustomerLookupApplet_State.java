/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 6    | 10-25-2005 | Manpreet  | N/A       |Prev /Skip button for SelectItemsApplet       |
 |      |            |           |           |to indicate Customer wasnt selected           |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 5    | 07-05-2005 | Vikram    | 298       |In Required Customer Tender Screen - Previous |
 |      |            |           |           |button did not work properly.                 |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 4    | 06-08-2005 | Vikram    | 123       |Item Return Screen flow issue                 |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 3    | 04-12-2005 | Khyati    | N/A       |1.SendSale Specification                      |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 04-11-2005 | Khyati    | N/A       |1.Return Specification                        |
 ---------------------------------------------------------------------------------------------
 | 1    | 04-09-2005 | Manpreet  | N/A       | Customer Manangement                         |
 +------+------------+-----------+-----------+----------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.customer;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.appmgr.state.*;
import com.chelseasystems.cs.util.Version;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class CustomerLookupApplet_State {

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int NEW_CUST(IApplicationManager theAppMgr) throws StateException {
		return 1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int SKIP(IApplicationManager theAppMgr) throws StateException {
		if (theAppMgr.getStateObject("FROM_ASIS_CUST_LOOKUP") != null) {
			theAppMgr.removeStateObject("FROM_ASIS_CUST_LOOKUP");
			return 14;
		}
		// ks: Modified to support look up for Return transaction
		String toWhere = (String) theAppMgr.getStateObject("ARM_DIRECT_TO");
		String fromWhere = (String) theAppMgr.getStateObject("ARM_DIRECTED_FROM");
		theAppMgr.removeStateObject("CUST_MGMT_MODE");
		theAppMgr.removeStateObject("ARM_DIRECTED_FROM");
		if (toWhere == null)
			toWhere = fromWhere;
		if (toWhere != null) {
			theAppMgr.removeStateObject("ARM_DIRECT_TO");
			if (toWhere.equals("PAYMENT_APPLET")) {
				return 0;
			} else if (toWhere.equals("CUST_MGMT")) {
				return 1;
			} else if (toWhere.equals("RETURN_TO_SALE_APPLET") || toWhere.equals("SALE_APPLET")) {
				return 2;
			} else if (toWhere.equals("RETURN_APPLET")) {
				return 3;
			} else if (toWhere.equals("SHIPPING_HEADER")) {
				return 4;
			} else if (toWhere.equals("SELECT_ITEMS_APPLET")) {
				// Return to SelectItemsApplet without selecting Customer
				// Stops loading of TxnHistory if Customer is already present
				// on transaction - MSB (10/25/2005)
				theAppMgr.addStateObject("ARM_CUST_LOOKUP_PREV_PRESSED", "TRUE");
				return 5;
			} else if (toWhere.equals("RETURN_TXN_HIST_APPLET")) {
				return 8;
			} else if (toWhere.equals("VIEW_TXN_APPLET")) {
				return 9;
			} else if (toWhere.equals("ADHOC_QUERY_APPLET")) {
				return 10;
			} else if (toWhere.equals("PRINT_FISCAL")) {
				return 13;
			}
		}
		// else if(fromWhere!=null)
		// {
		theAppMgr.fireButtonEvent("PREV");
		// }
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int CUST_DEFAULT(IApplicationManager theAppMgr) throws StateException {
		if ("EUR".equalsIgnoreCase(Version.CURRENT_REGION)) {
		return 17;
		}
		return 15;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int SEARCH(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int CUST_PHONE(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int CUST_NO(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int REWARD_CARD(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int ADVANCED_SEARCH(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int PREV(IApplicationManager theAppMgr) throws StateException {
		theAppMgr.removeStateObject("CUST_MGMT_MODE");
		theAppMgr.removeStateObject("CUSTOMER_REQUIRED");
		String fromWhere = (String) theAppMgr.getStateObject("ARM_DIRECTED_FROM");
		if (theAppMgr.getStateObject("ARM_CUST_REQUIRED") != null && (theAppMgr.getStateObject("ARM_CUST_REQUIRED").toString().trim().equalsIgnoreCase("TRUE"))){
			theAppMgr.removeStateObject("CUST_NEEDED_PAYMENT");
			theAppMgr.removeStateObject("PAYMENT");
		}
		if(theAppMgr.getStateObject("ARM_CUST_REQUIRED") != null)
		theAppMgr.removeStateObject("ARM_CUST_REQUIRED");
		// Remove the tax exempt state object if previous is clicked so that it does get re-directed back to customer
		// applet
		if (theAppMgr.getStateObject("TAX_EXEMPT") != null)
			theAppMgr.removeStateObject("TAX_EXEMPT");
		if (fromWhere != null) {
			theAppMgr.removeStateObject("ARM_DIRECTED_FROM");
			if (fromWhere.equals("PAYMENT_APPLET")) {
				return 0;
			} else if (fromWhere.equals("CUST_MGMT")) {
				return 1;
			} else if (fromWhere.equals("RETURN_TO_SALE_APPLET") || fromWhere.equals("SALE_APPLET") || fromWhere.equals("TXN_DETAIL_APPLET")) {
				return 2;
			} else if (fromWhere.equals("RETURN_APPLET")) {
				return 3;
			} else if (fromWhere.equals("SHIPPING_HEADER")) {
				return 4;
			} else if (fromWhere.equals("SELECT_ITEMS_APPLET")) {
				// Return to SelectItemsApplet without selecting Customer
				// Stops loading of TxnHistory if Customer is already present
				// on transaction - MSB (10/25/2005)
				theAppMgr.addStateObject("ARM_CUST_LOOKUP_PREV_PRESSED", "TRUE");
				return 5;
			} else if (fromWhere.equals("COLLECTIONS_APPLET")) {
				return 6;
			} else if (fromWhere.equals("LOGIN_APPLET")) {
				return 7;
			} else if (fromWhere.equals("RETURN_TXN_HIST_APPLET")) {
				return 8;
			} else if (fromWhere.equals("VIEW_TXN_APPLET")) {
				return 9;
			} else if (fromWhere.equals("ADHOC_QUERY_APPLET")) {
				return 10;
			} else if (fromWhere.equals("INITIAL_RETURN_APPLET")) {
				return 11;
			} else if (fromWhere.equals("ORIGINAL_CONSULTANT_APPLET")) {
				return 12;
			} else if (fromWhere.equals("PRINT_FISCAL")) {
				theAppMgr.removeStateObject("ARM_DIRECTED_FROM");
				return 13;
			} else if (fromWhere.equals("ASIS_TXN_APPLET")) {
				theAppMgr.removeStateObject("ARM_DIRECTED_FROM");
				return 14;
			} else if (fromWhere.equals("PAID_OUT")) {
				theAppMgr.removeStateObject("ARM_DIRECTED_FROM");
				return 15;
			} else if (fromWhere.equals("REDEEMABLE_BUYBACK_APPLET")) {
				return 13;
			}
		} else
			theAppMgr.goBack();
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int OK(IApplicationManager theAppMgr) throws StateException {
		// ks: Modified to support lookup from Return Txn and to Shipping header screen
		String toWhere = (String) theAppMgr.getStateObject("ARM_DIRECT_TO");
		theAppMgr.removeStateObject("ARM_CUST_REQUIRED");
		theAppMgr.removeStateObject("CUST_MGMT_MODE");
		if (theAppMgr.getStateObject("UPDATE_TXN_CUSTOMER") != null) {
			theAppMgr.removeStateObject("ARM_DIRECTED_FROM");
			if ("EUR".equalsIgnoreCase(Version.CURRENT_REGION)) {
				return 16;
			}
			//Added by Vivek Mishra for Japan's PCR : Delayed Customer Association
			else if ("JP".equalsIgnoreCase(Version.CURRENT_REGION)) {
				return 17;
			}//End
			else{
				return 14;
			}			
			
			
		}
		if (theAppMgr.getStateObject("FROM_ASIS_CUST_LOOKUP") != null) {
			theAppMgr.removeStateObject("FROM_ASIS_CUST_LOOKUP");
			return 14;
		} else if (theAppMgr.getStateObject("FROM_PAID_OUT_CUST_LOOKUP") != null) {
			theAppMgr.removeStateObject("FROM_PAID_OUT_CUST_LOOKUP");
			return 15;
		}
		if (toWhere != null) {
			theAppMgr.removeStateObject("ARM_DIRECT_TO");
			if (toWhere.equals("PAYMENT_APPLET")) {
				theAppMgr.removeStateObject("ARM_DIRECTED_FROM");
				return 0;
			} else if (toWhere.equals("CUST_MGMT")) {
				theAppMgr.removeStateObject("ARM_DIRECTED_FROM");
				return 1;
			} else if (toWhere.equals("RETURN_TO_SALE_APPLET") || toWhere.equals("SALE_APPLET")) {
				theAppMgr.removeStateObject("ARM_DIRECTED_FROM");
				return 2;
			} else if (toWhere.equals("RETURN_APPLET")) {
				theAppMgr.removeStateObject("ARM_DIRECTED_FROM");
				return 3;
			} else if (toWhere.equals("SHIPPING_HEADER_APPLET")) {
				theAppMgr.removeStateObject("ARM_DIRECTED_FROM");
				return 4;
			} else if (toWhere.equals("SELECT_ITEMS_APPLET")) {
				theAppMgr.removeStateObject("ARM_DIRECTED_FROM");
				return 5;
			} else if (toWhere.equals("COLLECTIONS_APPLET")) {
				theAppMgr.removeStateObject("ARM_DIRECTED_FROM");
				return 6;
			} else if (toWhere.equals("LOGIN_APPLET")) {
				theAppMgr.removeStateObject("ARM_DIRECTED_FROM");
				return 7;
			} else if (toWhere.equals("RETURN_TXN_HIST_APPLET")) {
				theAppMgr.removeStateObject("ARM_DIRECTED_FROM");
				return 8;
			} else if (toWhere.equals("VIEW_TXN_APPLET")) {
				theAppMgr.removeStateObject("ARM_DIRECTED_FROM");
				return 9;
			} else if (toWhere.equals("ADHOC_QUERY_APPLET")) {
				theAppMgr.removeStateObject("ARM_DIRECTED_FROM");
				return 10;
			} else if (toWhere.equals("INITIAL_RETURN_APPLET")) {
				theAppMgr.removeStateObject("ARM_DIRECTED_FROM");
				return 11;
			} else if (toWhere.equals("ORIGINAL_CONSULTANT_APPLET")) {
				theAppMgr.removeStateObject("ARM_DIRECTED_FROM");
				return 12;
			} else if (toWhere.equals("PRINT_FISCAL")) {
				theAppMgr.removeStateObject("ARM_DIRECTED_FROM");
				return 13;
			} else if (toWhere.equals("ASIS_TXN_APPLET")) {
				theAppMgr.removeStateObject("ARM_DIRECTED_FROM");
				return 14;
			} else if (toWhere.equals("PAID_OUT")) {
				theAppMgr.removeStateObject("ARM_DIRECTED_FROM");
				return 15;
			} else if (theAppMgr.getStateObject("ARM_DIRECTED_FROM") != null) {
				theAppMgr.fireButtonEvent("PREV");
			}
		}
		if (theAppMgr.getStateObject("CUSTOMER_REQUIRED") != null) {
			return 2;
		}
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int LOAD_MGMT(IApplicationManager theAppMgr) throws StateException {
		return 1;
	}

	public int REGISTER_VIP(IApplicationManager theAppMgr) throws StateException {
		return 16;
	}

	public int DUMMY_CUST(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}
	/**
	 * @author Neeti Mistry.
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	
	public int REFRESH(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}
	
	public int PREVIOUS(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}
	
}
