package com.chelseasystems.cs.dataaccess.artsoracle.databean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArmStgVirtualSaleBean extends BaseOracleBean {
	public ArmStgVirtualSaleBean() {
	}

	public static String selectSql = "select MOBILE_REGISTER_ID, STORE_ID, CUSTOMER_ID, BARCODE, EMPLOYEE_ID, ITEM_PRICE, TRANSACTION_TYPE, TRN_TIMESTAMP, TRN_COMMENT, ID_VIRTUAL_SALE, TRN_STATUS, ROW_NUMBER_ID from ARM_STG_VIRTUAL_SALE ";
	public static String insertSql = "insert into ARM_STG_VIRTUAL_SALE (MOBILE_REGISTER_ID, STORE_ID, CUSTOMER_ID, BARCODE, EMPLOYEE_ID, ITEM_PRICE, TRANSACTION_TYPE, TRN_TIMESTAMP, TRN_COMMENT, ID_VIRTUAL_SALE, TRN_STATUS, ROW_NUMBER_ID) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static String updateSql = "update ARM_STG_VIRTUAL_SALE set MOBILE_REGISTER_ID = ?, STORE_ID = ?, CUSTOMER_ID = ?, BARCODE = ?, EMPLOYEE_ID = ?, ITEM_PRICE = ?, TRANSACTION_TYPE = ?, TRN_TIMESTAMP = ?, TRN_COMMENT = ?, ID_VIRTUAL_SALE=?, TRN_STATUS=?, ROW_NUMBER_ID=? ";
	public static String deleteSql = "delete from ARM_STG_VIRTUAL_SALE ";

	public static String TABLE_NAME = "ARM_STG_VIRTUAL_SALE";
	public static String COL_MOBILE_REGISTER_ID = "ARM_STG_VIRTUAL_SALE.MOBILE_REGISTER_ID";
	public static String COL_STORE_ID = "ARM_STG_VIRTUAL_SALE.STORE_ID";
	public static String COL_CUSTOMER_ID = "ARM_STG_VIRTUAL_SALE.CUSTOMER_ID";
	public static String COL_BARCODE = "ARM_STG_VIRTUAL_SALE.BARCODE";
	public static String COL_EMPLOYEE_ID = "ARM_STG_VIRTUAL_SALE.EMPLOYEE_ID";
	public static String COL_ITEM_PRICE = "ARM_STG_VIRTUAL_SALE.ITEM_PRICE";
	public static String COL_TRANSACTION_TYPE = "ARM_STG_VIRTUAL_SALE.TRANSACTION_TYPE";
	public static String COL_TRN_TIMESTAMP = "ARM_STG_VIRTUAL_SALE.TRN_TIMESTAMP";
	public static String COL_TRN_COMMENT = "ARM_STG_VIRTUAL_SALE.TRN_COMMENT";
	public static String COL_ID_VIRTUAL_SALE = "ARM_STG_VIRTUAL_SALE.ID_VIRTUAL_SALE";
	public static String COL_TRN_STATUS = "ARM_STG_VIRTUAL_SALE.TRN_STATUS";
	public static String COL_ROW_NUMBER_ID = "ARM_STG_VIRTUAL_SALE.ROW_NUMBER_ID";

	@Override
	public String getSelectSql() {
		return selectSql;
	}

	@Override
	public String getInsertSql() {
		return insertSql;
	}

	@Override
	public String getUpdateSql() {
		return updateSql;
	}

	@Override
	public String getDeleteSql() {
		return deleteSql;
	}

	private String mobileRegisterId;
	private String storeId;
	private String customerId;
	private String barcode;
	private String employeeId;
	private Double itemPrice;
	private String transactionType;
	private Date trnTimestamp;
	private String trnComment;
	private String idVirtualSale;
	private String trnStatus;
	private String rowNumId;

	public String getMobileRegisterId() {
		return mobileRegisterId;
	}

	public void setMobileRegisterId(String mobileRegisterId) {
		this.mobileRegisterId = mobileRegisterId;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public Double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Date getTrnTimestamp() {
		return trnTimestamp;
	}

	public void setTrnTimestamp(Date trnTimestamp) {
		this.trnTimestamp = trnTimestamp;
	}

	public String getTrnComment() {
		return trnComment;
	}

	public void setTrnComment(String trnComment) {
		this.trnComment = trnComment;
	}

	public String getIdVirtualSale() {
		return idVirtualSale;
	}

	public void setIdVirtualSale(String idVirtualSale) {
		this.idVirtualSale = idVirtualSale;
	}

	public String getTrnStatus() {
		return trnStatus;
	}

	public void setTrnStatus(String trnStatus) {
		this.trnStatus = trnStatus;
	}

	public String getRowNumId() {
		return rowNumId;
	}

	public void setRowNumId(String rowNumId) {
		this.rowNumId = rowNumId;
	}

	@Override
	public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
		ArrayList<Object> list = new ArrayList<Object>();
		while (rs.next()) {
			ArmStgVirtualSaleBean bean = new ArmStgVirtualSaleBean();
			bean.mobileRegisterId = getStringFromResultSet(rs,
					"MOBILE_REGISTER_ID");
			bean.storeId = getStringFromResultSet(rs, "STORE_ID");
			bean.customerId = getStringFromResultSet(rs, "CUSTOMER_ID");
			bean.barcode = getStringFromResultSet(rs, "BARCODE");
			bean.employeeId = getStringFromResultSet(rs, "EMPLOYEE_ID");
			bean.itemPrice = getCurrencyFromResultSet(rs, "ITEM_PRICE")
					.doubleValue();
			bean.transactionType = getStringFromResultSet(rs,
					"TRANSACTION_TYPE");
			bean.trnTimestamp = getDateFromResultSet(rs, "TRN_TIMESTAMP");
			bean.trnComment = getStringFromResultSet(rs, "TRN_COMMENT");
			bean.idVirtualSale = getStringFromResultSet(rs, "ID_VIRTUAL_SALE");
			bean.trnStatus = getStringFromResultSet(rs, "TRN_STATUS");
			bean.rowNumId = getStringFromResultSet(rs, "ROW_NUMBER_ID");
			list.add(bean);
		}
		return (ArmStgVirtualSaleBean[]) list
				.toArray(new ArmStgVirtualSaleBean[0]);
	}

	@Override
	public List<Object> toList() {
		List<Object> list = new ArrayList<Object>();
		addToList(list, this.getMobileRegisterId(), Types.VARCHAR);
		addToList(list, this.getStoreId(), Types.VARCHAR);
		addToList(list, this.getCustomerId(), Types.VARCHAR);
		addToList(list, this.getBarcode(), Types.VARCHAR);
		addToList(list, this.getEmployeeId(), Types.VARCHAR);
		addToList(list, this.getItemPrice(), Types.VARCHAR);
		addToList(list, this.getTransactionType(), Types.VARCHAR);
		addToList(list, this.getTrnTimestamp(), Types.TIMESTAMP);
		addToList(list, this.getTrnComment(), Types.VARCHAR);
		addToList(list, this.getIdVirtualSale(), Types.VARCHAR);
		addToList(list, this.getTrnStatus(), Types.VARCHAR);
		addToList(list, this.getRowNumId(), Types.VARCHAR);
		return list;
	}

}
