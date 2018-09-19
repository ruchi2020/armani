/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.customer;

import com.armani.business.rules.ARMCustomerBR;
import com.armani.utils.ArmConfig;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.rules.Rule;
import com.chelseasystems.cr.rules.RulesInfo;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.payment.Credit;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSReservationLineItem;
import com.chelseasystems.cs.util.Version;

/**
 * <p>Title: DummyOrDefaultCustomerNotAllowed</p>
 *
 * <p>Description: This business rule does not allow dummy or default customer</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company:SkillNet Inc. </p>
 *
 * @author Sandhya Ajit
 * @version 1.0
 */
/* History:-
 +--------+------------+-----------+------------------+-------------------------------------------------+
 | Ver#   |    Date    |   By      |        Defect #  |              Description                        |
 +--------+------------+-----------+------------------+-------------------------------------------------+
 | 1      | 11-13-2006 | Sandhya   |        NA        | Dummy or default customer not allowed           |                             |
 +--------+------------+-----------+------------------+-------------------------------------------------+
 */
public class DummyOrDefaultCustomerNotAllowed extends Rule {

  public static final long serialVersionUID = 0;
  //Vivek Mishra : Merged updated code from source provided by Sergio 18-MAY-16
  private boolean skipdummy = false;
  //Ends here
  
  /**
   * Default Constructor
   */
  public DummyOrDefaultCustomerNotAllowed() {}

  /**
   * Execute the rule.
   *
   * @param object theParent - instance of Transaction
   * @return RulesInfo
   */
  public RulesInfo execute(Object theParent, Object[] args) {
	return execute(theParent, args[0]);
  }

  /**
   * Execute the Rule
   * @param Transaction txn
   * @return RulesInfo
   */
  private RulesInfo execute(Object parent, Object currentObj) {
	try {
		CMSCustomer customer;
		String message = "";
		boolean bcheckCustomer = false;
		//Vivek Mishra : Merged updated code from source provided by Sergio 18-MAY-16
		if (ArmConfig.hasValue("SKIPDUMMY")) {
            skipdummy = Boolean.valueOf(ArmConfig.getValue("SKIPDUMMY")).booleanValue();
        }
		//Ends here
		if (parent instanceof CMSCompositePOSTransaction) {
			CMSCompositePOSTransaction compTxn = (CMSCompositePOSTransaction)parent;
			//Check for Reservation line items, Deposit line items and
			//Credit transaction
			POSLineItem[] lineItems = compTxn.getLineItemsArray();
			for(int i = 0; lineItems != null && i < lineItems.length; i++) {
				
				//Added by Vivek Mishra for PCR : Dummy Customer Association
				if ("JP".equalsIgnoreCase(Version.CURRENT_REGION))
				{
					if ((lineItems[i] instanceof CMSReservationLineItem && (((CMSItem)lineItems[i].getItem()).isDeposit())) || 
							(((CMSItem)lineItems[i].getItem()).isDeposit()) || 
							(currentObj instanceof Credit)) {
			    		bcheckCustomer = true;
			    		break;
			    	}
				}//end
				else if (lineItems[i] instanceof CMSReservationLineItem || 
						(((CMSItem)lineItems[i].getItem()).isDeposit()) || 
						(currentObj instanceof Credit)) {
		    		bcheckCustomer = true;
		    		break;
		    	}
		    }
		    if (bcheckCustomer) {
		    	if (currentObj instanceof CMSCustomer) {
		    		customer = (CMSCustomer)currentObj;
		    	} else {
		    		customer = (CMSCustomer)compTxn.getCustomer();
		    	}
		    	if (customer != null) {
		    		if (ARMCustomerBR.isDummy(customer.getId()) || ARMCustomerBR.isDefault(customer.getId())) {
		    			//Vivek Mishra : Merged updated code from source provided by Sergio 18-MAY-16
		    			 if ( ! skipdummy )
						 {//Ends here
		    				 message = "Dummy or Default customer not allowed for this transaction. " + 
		    				"Please select a real customer.";
		    				 return new RulesInfo(message);
						 }
					}
		    	}
			}
		}
	} catch (Exception ex) {
		ex.printStackTrace();
		LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
				, "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
	}
    return new RulesInfo();
  }  
 
  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    return ("Dummy or Default customer not allowed for Reservation, Deposit and Credit transactions.");
  }

  /**
   * Return the name of the rule.
   * @return name of the rule.
   */
  public String getName() {
    return "DummyOrDefaultCustomerNotAllowed";
  }
}

