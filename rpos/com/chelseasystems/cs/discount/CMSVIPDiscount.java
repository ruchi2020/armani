/**
 * This class is created for VIP membership discount by Vivek Sawant
 * @author vivek.sawant
 *  
**/
package com.chelseasystems.cs.discount;

import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.rules.RuleEngine;
/**
 * This method is used to check whether transaction is valid for discount
 * @param txnPos CompositePOSTransaction
 * @throws BusinessRuleException
 */
public class CMSVIPDiscount extends CMSDiscount  {

	private double discountPercent ; 
	
	
	public void isValid(CompositePOSTransaction txnPos)
    throws BusinessRuleException {
 	  RuleEngine.execute(getClass().getName(), "isValid", txnPos, null);
}
	/**@author vivek.sawant
	   * This method is used to get promotion discount
	   *  percent for employee
	   * @return Double
	   */
	public double getDiscountPercent() {
		return this.discountPercent;
	}
	/**@author vivek.sawant
	   * This method is used to set promotion discount
	   *  percent for employee
	   * @return void
	   */
	public void setDiscountPercent(double discountPercent) {
		this.discountPercent = discountPercent;
	}


}
