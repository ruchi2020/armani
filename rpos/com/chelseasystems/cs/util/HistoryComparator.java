/*
 * Creato il 23-nov-06
 * 
 * @author dott. Monti Alessandro
 */
package com.chelseasystems.cs.util;

import java.util.Comparator;

import com.chelseasystems.cs.customer.CreditHistory;
import com.chelseasystems.cs.customer.DepositHistory;

/**
 * Class that can sort deposit history records
 * 
 * @author dott. Monti Alessandro
 */
public class HistoryComparator implements Comparator {

	/**
	 * 
	 */
	public HistoryComparator() {
		super();
	}

	/* (non Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		int result=0;
		
		try {
			result = compare ((DepositHistory) o1, (DepositHistory) o2);
		} catch (ClassCastException e) {
			//it is normal, one or both must fail 			
			//e.printStackTrace();
		}
			
		try {
			result = compare ((CreditHistory) o1, (CreditHistory) o2);
			
		} catch (ClassCastException e) {
			//it is normal, one or both must fail 			
			//e.printStackTrace();
		}
		
		return result;
	}

	/**
	 * natural order for Deposit History:
	 * -customer
	 * -shop
	 * -date
	 * -transaction ID
	 * 
	 * @param h1
	 * @param h2
	 * @return
	 */
	public int compare(DepositHistory h1, DepositHistory h2) {
		int result=0;
		if (!h2.getCustomer().equals(h1.getCustomer())){
			result = h2.getCustomer().compareTo(h1.getCustomer());
		}else if (!h2.getStoreName().equals(h1.getStoreName())){
			result = h2.getStoreName().compareTo(h1.getStoreName());
		}else if (!h2.getTransactionDate().equals(h1.getTransactionDate())){
			result = h2.getTransactionDate().compareTo(h1.getTransactionDate());
		}else{
			result = h2.getTransactionId().compareTo(h1.getTransactionId());
		}
		
		return result;
	}

	/**
	 * natural order for Credit History:
	 * -customer
	 * -shop
	 * -date
	 * -transaction ID
	 * 
	 * @param h1
	 * @param h2
	 * @return
	 */
	public int compare(CreditHistory h1, CreditHistory h2) {
		int result=0;
		if (!h2.getCustomer().equals(h1.getCustomer())){
			result = h2.getCustomer().compareTo(h1.getCustomer());
		}else if (!h2.getStoreName().equals(h1.getStoreName())){
			result = h2.getStoreName().compareTo(h1.getStoreName());
		}else if (!h2.getTransactionDate().equals(h1.getTransactionDate())){
			result = h2.getTransactionDate().compareTo(h1.getTransactionDate());
		}else{
			result = h2.getTransactionId().compareTo(h1.getTransactionId());
		}
		
		return result;
	}

}
