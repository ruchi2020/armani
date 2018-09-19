/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.ga.cs.pos.datatranssferobject;

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.ga.cs.dataaccess.artsoracle.databean.ConsignmentDetailsReportBean;
import com.ga.cs.swing.report.ReportConstants;

/**
 * put your documentation comment here
 */
public class ConsignmentDetailsReport implements java.io.Serializable {
	static final long serialVersionUID = -6572138789941892281L;
	public ConsignmentDetailsReportBean bean;

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String toString() {
		return bean.toString();
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getAssociateId() {
		return this.bean.associateId;
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getAssociateName() {
		return this.bean.associateName;
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getItemStatus() {
		return this.bean.itemStatus;
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getTransactionId() {
		return this.bean.transactionId;
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String[] toStringArray() {
		String[] array = new String[10];
		// array[0] = bean.associateId;

		array[0] = bean.associateName;
		// padding to get the itemDesc in next line
		String itemStr = bean.itemId;
		while (itemStr.length() < 15) {
			itemStr = itemStr + " ";
		}
		array[1] = itemStr + " " + bean.itemDesc;
		array[2] = new SimpleDateFormat(ReportConstants.DATE_FORMAT_SLASH_MDY)
				.format(bean.dateIssued);
		array[3] = "" + bean.origQty;
		array[4] = bean.origAmt.formattedStringValue();
		array[5] = bean.itemStatus;
		array[6] = (bean.dateSoldReturn == null) ? "" : new SimpleDateFormat(
				ReportConstants.DATE_FORMAT_SLASH_MDY)
				.format(bean.dateSoldReturn);
		array[7] = "" + bean.remainingQty;
		array[8] = bean.remainingAmt.formattedStringValue();
		array[9] = bean.transactionId;
		return array;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param colZero
	 * @param totalOrigQty
	 * @param totalOrigAmt
	 * @param totalRemainQty
	 * @param totalRemainAmt
	 * @return
	 */
	public static String[] getTotalArray(String colZero, int totalOrigQty,
			ArmCurrency totalOrigAmt, int totalRemainQty,
			ArmCurrency totalRemainAmt) {
		String[] array = new String[10];
		array[0] = colZero;
		array[1] = "";
		array[2] = "";
		array[3] = "" + totalOrigQty;
		array[4] = totalOrigAmt.formattedStringValue();
		array[5] = "";
		array[6] = "";
		array[7] = "" + totalRemainQty;
		array[8] = totalRemainAmt.formattedStringValue();
		array[9] = "";
		return array;
	}

	/**
	 * 1923 put your documentation comment here
	 * @param colZero
	 * @param totalTrans
	 * @param totalOrigQty
	 * @param totReturnedItems
	 * @param soldItems
	 * @param sellThroughPercentage
	 * @return String[]
	 */
	public static String[] getTotalSellThroughArray(String colZero,
			String totTran, int totTrans, int totalOrigQty, String retItems,
			int totReturnedItems, String sldItm, int soldItems,
			String percentage, String sellThroughPercentage) {
		String[] array = new String[10];
		array[0] = colZero;
		array[1] = totTran;
		array[2] = "" + totTrans;
		array[3] = "" + totalOrigQty;
		array[4] = retItems;
		array[5] = "" + totReturnedItems;
		array[6] = sldItm;
		array[7] = "" + soldItems;
		array[8] = percentage;
		array[9] = "" + sellThroughPercentage;
		return array;
	}

	/**
	 * 1923 put your documentation comment here
	 * @param totSoldTran
	 * @param totSoldTrans
	 * @param sellThroughDollar
	 * @param sellThrough
	 * @param numberOfClients
	 * @param noOfCleints
	 * @param numberOfClientsPurchased
	 * @param noOfClientsPurchased
	 * @return String[]
	 */

	public static String[] getTotalSellThroughAdditionArray(
			String totSoldTran, String totSoldTrans, String numberOfClients,int noOfCleints,String numberOfClientsPurchased,int noOfClientsPurchased,String sellThroughDollar,String sellThrough ) {
		String[] array = new String[10];
		array[0]="";
		array[1] = totSoldTran;
		array[2] = "" + totSoldTrans;
		array[3] = numberOfClients;
		array[4] = ""+noOfCleints;
		array[5] = numberOfClientsPurchased;
		array[6] = ""+noOfClientsPurchased;
		array[7]= sellThroughDollar;
		array[8] = ""+sellThrough;
		array[9] = "";
		return array;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param all
	 * @param associateId
	 * @return
	 */
	public static ConsignmentDetailsReport[] filterByAssociate(
			ConsignmentDetailsReport[] all, String associateId) {
		if (all == null || all.length == 0) {
			return new ConsignmentDetailsReport[0];
		}
		if (associateId == null || associateId.length() == 0) {
			return all;
		}
		List list = new ArrayList();
		for (int i = 0; i < all.length; i++) {
			if (all[i].getAssociateId().equals(associateId.trim())) {
				list.add(all[i]);
			}
		}
		return (ConsignmentDetailsReport[]) list
				.toArray(new ConsignmentDetailsReport[0]);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param all
	 * @param associateId
	 * @return
	 */
	public static ConsignmentDetailsReport[] filterOpenByAssociate(
			ConsignmentDetailsReport[] all, String associateId) {
		if (all == null || all.length == 0) {
			return new ConsignmentDetailsReport[0];
		}
		List list = new ArrayList();
		for (int i = 0; i < all.length; i++) {
			if (all[i].getItemStatus().equals("OPEN")) {
				if (associateId == null || associateId.length() == 0
						|| all[i].getAssociateId().equals(associateId.trim())) {
					list.add(all[i]);
				}
			}
		}
		return (ConsignmentDetailsReport[]) list
				.toArray(new ConsignmentDetailsReport[0]);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param all
	 * @param associateId
	 * @return
	 */
	public static ConsignmentDetailsReport[] filterCloseByAssociate(
			ConsignmentDetailsReport[] all, String associateId) {
		if (all == null || all.length == 0) {
			return new ConsignmentDetailsReport[0];
		}
		List list = new ArrayList();
		for (int i = 0; i < all.length; i++) {
			if (!all[i].getItemStatus().equals("OPEN")) { // NOT OPEN is
															// CLOSED; ie
															// returned or sold
				if (associateId == null || associateId.length() == 0
						|| all[i].getAssociateId().equals(associateId.trim())) {
					list.add(all[i]);
				}
			}
		}
		return (ConsignmentDetailsReport[]) list
				.toArray(new ConsignmentDetailsReport[0]);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param all
	 * @param associateId
	 * @return
	 */
	public static ConsignmentDetailsReport[] filterBySellThrough(
			ConsignmentDetailsReport[] all, String associateId) {
		if (all == null || all.length == 0) {
			return new ConsignmentDetailsReport[0];
		}
		List list = new ArrayList();

		for (int i = 0; i < all.length; i++) {
			if ((all[i].getItemStatus().equals("OPEN"))
					|| !all[i].getItemStatus().equals("OPEN")) {
				if (associateId == null || associateId.length() == 0
						|| all[i].getAssociateId().equals(associateId.trim())) {
					list.add(all[i]);
				}
			}
		}
		return (ConsignmentDetailsReport[]) list
				.toArray(new ConsignmentDetailsReport[0]);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param all
	 * @param associateId
	 * @return
	 */

	public static ConsignmentDetailsReport[] filterBySellThroughByAssociate(
			ConsignmentDetailsReport[] all, String associateId) {
		if (all == null || all.length == 0) {
			return new ConsignmentDetailsReport[0];
		}
		if (associateId == null || associateId.length() == 0) {
			return all;
		}
		List list = new ArrayList();
		for (int i = 0; i < all.length; i++) {
			if (all[i].getAssociateId().equals(associateId.trim())) {
				list.add(all[i]);
			}
		}
		return (ConsignmentDetailsReport[]) list
				.toArray(new ConsignmentDetailsReport[0]);
	}
}
