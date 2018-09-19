/*
 History:
 +------+------------+-----------+--------------------+------------------------------------------------------+
 | Ver# | Date       | By        | Defect #           | Description                                          |
 +------+------------+-----------+--------------------+------------------------------------------------------+
 | 1    | 09/12/2006 | Sandhya   | Redeemable Buyback | Customer required for redeemable buyback transaction |
 ------------------------------------------------------------------------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.pos.RedeemableBuyBackTransaction;
import com.chelseasystems.cr.pos.TransactionPOSServices;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cs.customer.CMSCustomer;

/**
 * <p>Title: CMSRedeemableBuyBackTransaction</p>
 * <p>Description: This class is used to associate a customer with redeemable buyback txn </p>
 * @author Sandhya
 * @version 1.0
 */
public class CMSRedeemableBuyBackTransaction extends RedeemableBuyBackTransaction {
	static final long serialVersionUID = 6986630571992302569L;
	
	private CMSCustomer customer = null;
	
	/**
	 * Constructor
	 * @param store Store
	 */
    public CMSRedeemableBuyBackTransaction(Store store) {
        super(store);
    }
    
    /**
     * This method used to post the redeemable buyback transaction
     * @return boolean
     * @throws Exception
     */
    public boolean post()throws Exception   {
        return TransactionPOSServices.getCurrent().submit(this);
    }
    
    /**
     * This method used to set the customer value
     * @param customer CMSCustomer
     */
    public void setCustomer(CMSCustomer customer) {
    	this.customer = customer;
    }

    /**
     * This method used to set the customer value
     * @param customer CMSCustomer
     */
    public void doSetCustomer(CMSCustomer customer) {
    	setCustomer(customer);
    }

    /**
     * This method used to get the customer value
     * @return CMSCustomer
     */
    public CMSCustomer getCustomer() {
    	return customer;
    }
}