/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 05/03/2006 | Sandhya   | N/A       | Reservation Details Report                         |
 -------------------------------------------------------------------------------------------------- 
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.ga.cs.dataaccess.artsoracle.databean;

import java.io.Serializable;
import java.sql.*;
import java.util.*;
import java.util.Date;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;

/**
 * put your documentation comment here
 */
public class ReservationDetailsReportBean extends BaseOracleBean implements Serializable {
	public String customerId;
	public String customerFirstName;
	public String customerLastName;
	public String associateId;
	public String associateName;
	public String transactionId;
	public int lineNumber;
	public String itemId;
	public String itemDesc;
	public String itemStatus;
	public Date dateIssued;
	public int origQty;
	public ArmCurrency origAmt;
	public Date dateSoldReturn;
	public int remainingQty;
	public ArmCurrency remainingAmt;
	private static String COL_CUSTOMER_ID = "CUSTOMER_ID";
	private static String COL_FIRST_NAME = "FIRST_NAME";
	private static String COL_LAST_NAME = "LAST_NAME";
	private static String COL_ASSOCIATE_ID = "ASSOCIATE_ID";
	private static String COL_ASSOCIATE_NAME = "ASSOCIATE_NAME";
	private static String COL_TRANSACTION_ID = "TRANSACTION_ID";
	private static String COL_LINE_NUMBER = "LINE_NUMBER";
	private static String COL_ITEM_ID = "ITEM_ID";
	private static String COL_ITEM_DESC = "ITEM_DESC";
	private static String COL_ITEM_STATUS = "ITEM_STATUS";
	private static String COL_DATE_ISSUED = "DATE_ISSUED";
	private static String COL_ORIG_QTY = "ORIG_QTY";
	private static String COL_ITEM_RETAIL = "ORIG_AMT";
	private static String COL_DATE_SOLDRETURN = "DATE_SOLDRETURN";
	private static String COL_CURR_QTY = "CURR_QTY";
	private static String COL_CURR_AMT = "CURR_AMT";

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer("customerId=" + customerId + "\n");
		buf.append("customerFirstName=" + customerFirstName + "\n");
		buf.append("customerLastName=" + customerLastName + "\n");
		buf.append("associateId=" + associateId + "\n");
		buf.append("associateName=" + associateName + "\n");
		buf.append("transactionId=" + transactionId + "\n");
		buf.append("lineNumber=" + lineNumber + "\n");
		buf.append("itemId=" + itemId + "\n");
		buf.append("itemDesc=" + itemDesc + "\n");
		buf.append("itemStatus=" + itemStatus + "\n");
		buf.append("dateIssued=" + dateIssued + "\n");
		buf.append("origQty=" + origQty + "\n");
		buf.append("origAmt=" + origAmt + "\n");
		buf.append("dateSoldReturn=" + dateSoldReturn + "\n");
		buf.append("remainingQty=" + remainingQty + "\n");
		buf.append("remainingAmt=" + remainingAmt + "\n");
		return buf.toString();
	}

	/**
	 * put your documentation comment here
	 * @param rs
	 * @return
	 * @exception SQLException
	 */
	public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
		ArrayList list = new ArrayList();
		while(rs.next()) {
			ReservationDetailsReportBean bean = new ReservationDetailsReportBean();
			bean.customerId = getStringFromResultSet(rs, COL_CUSTOMER_ID);
			bean.customerFirstName = getStringFromResultSet(rs, COL_FIRST_NAME);
			bean.customerLastName = getStringFromResultSet(rs, COL_LAST_NAME);
			bean.associateId = getStringFromResultSet(rs, COL_ASSOCIATE_ID);
			bean.associateName = getStringFromResultSet(rs, COL_ASSOCIATE_NAME);
			bean.transactionId = getStringFromResultSet(rs, COL_TRANSACTION_ID);
			bean.lineNumber = rs.getInt(COL_LINE_NUMBER);
			bean.itemId = getStringFromResultSet(rs, COL_ITEM_ID);
			bean.itemDesc = getStringFromResultSet(rs, COL_ITEM_DESC);
			bean.itemStatus = getStringFromResultSet(rs, COL_ITEM_STATUS);
			bean.dateIssued = getDateFromResultSet(rs, COL_DATE_ISSUED);
			bean.origQty = rs.getInt(COL_ORIG_QTY);
			bean.origAmt = getCurrencyFromResultSet(rs, COL_ITEM_RETAIL);
			bean.dateSoldReturn = getDateFromResultSet(rs, COL_DATE_SOLDRETURN);
			bean.remainingQty = getRemainingQty(bean, rs);
			bean.remainingAmt = getRemainingAmt(bean, rs);
			list.add(bean);
		}
		return (ReservationDetailsReportBean[]) list.toArray(new ReservationDetailsReportBean[0]);
	}

	/**
	 * put your documentation comment here
	 * @param bean
	 * @param rs
	 * @return
	 * @exception SQLException
	 */
	private int getRemainingQty(ReservationDetailsReportBean bean, ResultSet rs) throws SQLException {
		return bean.origQty - rs.getInt(COL_CURR_QTY);
	}

	/**
	 * put your documentation comment here
	 * @param bean
	 * @param rs
	 * @return
	 * @exception SQLException
	 */
	private ArmCurrency getRemainingAmt(ReservationDetailsReportBean bean, ResultSet rs) throws SQLException {
		try {
		ArmCurrency curAmt = getCurrencyFromResultSet(rs, COL_CURR_AMT);
			return (curAmt != null) ? bean.origAmt.subtract(curAmt) : bean.origAmt;
		} catch (CurrencyException e) {
			throw new SQLException("CurrencyException thrown");
		}
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public List toList() {
		return null;
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getSelectSql() {
		return null;
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getInsertSql() {
		return null;
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getUpdateSql() {
		return null;
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getDeleteSql() {
		return null;
	}
}
