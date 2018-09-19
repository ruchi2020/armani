/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.util;

import java.util.Date;
import java.util.*;
import com.chelseasystems.cs.customer.CustomerSaleSummary;


/**
 * <p>Title:CustSaleSummaryUtil </p>
 *
 * <p>Description: Utility to get sale/return summary over period of time</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: SkillNet inc</p>
 *
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 03-26-2005 | Manpreet  | N/A       | POS_104665_FS_CustomerManagement_Rev2              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class CustSaleSummaryUtil {
	/**
	 * CustomerSaleSummary Array
	 */
	private CustomerSaleSummary[] arraySummary;
	/**
	 * ArrayList of CustomerSaleSummary for set period.
	 */
	private ArrayList listSummaries;

	/**
	 * Construct CustomerSaleSummary
	 * @param cusSaleSummary
	 *            CustomerSaleSummary[]
	 */
	public CustSaleSummaryUtil(CustomerSaleSummary[] cusSaleSummary) {
		arraySummary = cusSaleSummary;
		listSummaries = new ArrayList();
	}

	/**
	 * Set the summary of months
	 * 
	 * @param iMonths
	 *            int[]
	 */
	public void setSummaryMonths(int iMonths[]) {
		if (iMonths == null || iMonths.length < 1)
			return;
		listSummaries.clear();
		Arrays.sort(iMonths);
		for (int iCtr = 0; iCtr < iMonths.length; iCtr++) {
			listSummaries.add(makeSummaryForMonths(iMonths[iCtr]));
		}
	}

	/**
	 * @param iYears
	 *            int[]
	 */
	public void setSummaryYears(int iYears[]) {
		if (iYears == null || iYears.length < 1)
			return;
		listSummaries.clear();
		Arrays.sort(iYears);
		for (int iCtr = 0; iCtr < iYears.length; iCtr++) {
			listSummaries.add(makeSummaryForMonths(iYears[iCtr] * 12));
		}
	}

	/**
	 * This method is used to get the sale amount for month from customer sale summary
	 * 
	 * @param iIndex
	 *            int
	 * @return double
	 */
	public double getSaleAmountForMonths(int iIndex) {
		if (iIndex < 0 || iIndex > listSummaries.size())
			return 0;
		double dAmount = 0;
		ArrayList listSummaryForMonth = (ArrayList) listSummaries.get(iIndex);
		for (int iCtr = 0; iCtr < listSummaryForMonth.size(); iCtr++) {
			CustomerSaleSummary summary = (CustomerSaleSummary) listSummaryForMonth.get(iCtr);
			if (summary.getTxnType().equalsIgnoreCase(CustomerSaleSummary.TXN_TYPE_SALE)) {
				dAmount += summary.getTxnAmount().doubleValue();
			}
		}
		return Math.round(dAmount);
	}

	/**
	 * This method is used to get the return amount for month from customer sale summary
	 * 
	 * @param iIndex
	 *            int
	 * @return double
	 */
	public double getReturnAmountForMonths(int iIndex) {
		if (iIndex < 0 || iIndex > listSummaries.size())
			return 0;
		double dAmount = 0;
		ArrayList listSummaryForMonth = (ArrayList) listSummaries.get(iIndex);
		for (int iCtr = 0; iCtr < listSummaryForMonth.size(); iCtr++) {
			CustomerSaleSummary summary = (CustomerSaleSummary) listSummaryForMonth.get(iCtr);
			if (summary.getTxnType().equalsIgnoreCase(CustomerSaleSummary.TXN_TYPE_RETURN)) {
				dAmount += summary.getTxnAmount().getDoubleValue();
			}
		}
		return Math.round(dAmount);
	}

	/**
	 * This method is used to get the ReturnDollar amount for life to date 
     * from customer sale summary
	 * 
	 * @return ReturnDollarAmount double
	 */
	public double getReturnAmountForLife() {
		int iCtr = 0;
		double dAmount = 0;
		for (iCtr = 0; iCtr < arraySummary.length; iCtr++) {
			if (arraySummary[iCtr].getTxnType().equalsIgnoreCase(CustomerSaleSummary.TXN_TYPE_RETURN)) {
				dAmount += arraySummary[iCtr].getTxnAmount().getDoubleValue();
			}
		}
		return Math.round(dAmount);
	}

	/**
	 * This method is used to get the SaleDollar amount for life to date 
     * from customer sale summary
	 * @return SaleDollarAmount double
	 */
	public double getSaleAmountForLife() {
		int iCtr = 0;
		double dAmount = 0;
		for (iCtr = 0; iCtr < arraySummary.length; iCtr++) {
			if (arraySummary[iCtr].getTxnType().equalsIgnoreCase(CustomerSaleSummary.TXN_TYPE_SALE)) {
				dAmount += arraySummary[iCtr].getTxnAmount().getDoubleValue();
			}
		}
		return Math.round(dAmount);
	}

	/**
	 * This method is used to set the summary period
	 * @param iNumMonths
	 *            NumberOfMonths
	 * @return listMonthlySummary ArrayList
	 */
	private ArrayList makeSummaryForMonths(int iNumMonths) {
		int iDiff = 0;
		iNumMonths = iNumMonths * 30;
		ArrayList listMonthlySummary = new ArrayList();
		for (int iCtr = 0; iCtr < arraySummary.length; iCtr++) {
			iDiff = calculateDaysDifference(arraySummary[iCtr].getTxnDate());
			if (iDiff > iNumMonths)
				break;
			if (iDiff <= iNumMonths) {
				listMonthlySummary.add(arraySummary[iCtr]);
			}
		}
		return listMonthlySummary;
	}

	/**
	 * This method is used to calculates difference in days between current date and transaction date.
	 * 
	 * @param dtTransaction
	 *            TransactionDate
	 * @return Difference of days
	 */
	private int calculateDaysDifference(Date dtTransaction) {
		int iDifference = 0;
		GregorianCalendar gcTransaction, gcCurrent;
		gcCurrent = new GregorianCalendar();
		gcTransaction = new GregorianCalendar();
		gcTransaction.setTime(dtTransaction);
		gcCurrent.clear(Calendar.MILLISECOND);
		gcCurrent.clear(Calendar.SECOND);
		gcCurrent.clear(Calendar.MINUTE);
		gcCurrent.clear(Calendar.HOUR_OF_DAY);
		gcTransaction.clear(Calendar.MILLISECOND);
		gcTransaction.clear(Calendar.SECOND);
		gcTransaction.clear(Calendar.MINUTE);
		gcTransaction.clear(Calendar.HOUR_OF_DAY);
		while(gcTransaction.before(gcCurrent)) {
			gcTransaction.add(Calendar.DATE, 1);
			iDifference++;
		}
		return iDifference;
	}
}
