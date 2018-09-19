//
// Copyright 2002, Retek Inc. All Rights Reserved.
//
package com.chelseasystems.cs.dataaccess.artsoracle.databean;

import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Types;
import com.chelseasystems.cr.currency.ArmCurrency;

/**
 *
 * This class is an object representation of the Arts database table ARM_ITM_SOH<BR>
 * Followings are the column of the table: <BR>
 *     ITEM_ID -- VARCHAR2(128)<BR>
 *     STORE_ID -- VARCHAR2(128)<BR>
 *     BALANCE_DATE -- DATE(7)<BR>
 *     QU_AVAILABLE -- NUMBER(4)<BR>
 *     QU_UNAVAILABLE -- NUMBER(4)<BR>
 *     FL_BALANCED -- CHAR(1)<BR>
 *     QU_SALE_TOTAL -- NUMBER(4)<BR>
 *     QU_RECEIPT -- NUMBER(4)<BR>
 *     QU_TRANSFER -- NUMBER(4)<BR>
 *     QU_STOCK_ADJ -- NUMBER(4)<BR>
 *     QU_ON_ORDER -- NUMBER(4)<BR>
 *     QU_STORE_AVAILABLE -- NUMBER(4)<BR>
 *     QU_STORE_UNAVAILABLE -- NUMBER(4)<BR>
 *
 */
public class ArmItmSohOracleBean extends BaseOracleBean {

  public ArmItmSohOracleBean() {}

  public static String selectSql = "select ITEM_ID, STORE_ID, BALANCE_DATE, NVL(QU_AVAILABLE,0) QU_AVAILABLE, NVL(QU_UNAVAILABLE,0) QU_UNAVAILABLE, FL_BALANCED, NVL(QU_SALE_TOTAL,0) QU_SALE_TOTAL, NVL(QU_RECEIPT,0) QU_RECEIPT, NVL(QU_TRANSFER,0) QU_TRANSFER, NVL(QU_STOCK_ADJ,0) QU_STOCK_ADJ, NVL(QU_ON_ORDER,0) QU_ON_ORDER, NVL(QU_STORE_AVAILABLE,0) QU_STORE_AVAILABLE, NVL(QU_STORE_UNAVAILABLE,0) QU_STORE_UNAVAILABLE from ARM_ITM_SOH ";
  public static String insertSql = "insert into ARM_ITM_SOH (ITEM_ID, STORE_ID, BALANCE_DATE, QU_AVAILABLE, QU_UNAVAILABLE, FL_BALANCED, QU_SALE_TOTAL, QU_RECEIPT, QU_TRANSFER, QU_STOCK_ADJ, QU_ON_ORDER, QU_STORE_AVAILABLE, QU_STORE_UNAVAILABLE) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_ITM_SOH set ITEM_ID = ?, STORE_ID = ?, BALANCE_DATE = ?, QU_AVAILABLE = ?, QU_UNAVAILABLE = ?, FL_BALANCED = ?, QU_SALE_TOTAL = ?, QU_RECEIPT = ?, QU_TRANSFER = ?, QU_STOCK_ADJ = ?, QU_ON_ORDER = ?, QU_STORE_AVAILABLE = ?, QU_STORE_UNAVAILABLE = ? ";
  public static String deleteSql = "delete from ARM_ITM_SOH ";

  public static String TABLE_NAME = "ARM_ITM_SOH";
  public static String COL_ITEM_ID = "ARM_ITM_SOH.ITEM_ID";
  public static String COL_STORE_ID = "ARM_ITM_SOH.STORE_ID";
  public static String COL_BALANCE_DATE = "ARM_ITM_SOH.BALANCE_DATE";
  public static String COL_QU_AVAILABLE = "ARM_ITM_SOH.QU_AVAILABLE";
  public static String COL_QU_UNAVAILABLE = "ARM_ITM_SOH.QU_UNAVAILABLE";
  public static String COL_FL_BALANCED = "ARM_ITM_SOH.FL_BALANCED";
  public static String COL_QU_SALE_TOTAL = "ARM_ITM_SOH.QU_SALE_TOTAL";
  public static String COL_QU_RECEIPT = "ARM_ITM_SOH.QU_RECEIPT";
  public static String COL_QU_TRANSFER = "ARM_ITM_SOH.QU_TRANSFER";
  public static String COL_QU_STOCK_ADJ = "ARM_ITM_SOH.QU_STOCK_ADJ";
  public static String COL_QU_ON_ORDER = "ARM_ITM_SOH.QU_ON_ORDER";
  public static String COL_QU_STORE_AVAILABLE = "ARM_ITM_SOH.QU_STORE_AVAILABLE";
  public static String COL_QU_STORE_UNAVAILABLE = "ARM_ITM_SOH.QU_STORE_UNAVAILABLE";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String itemId;
  private String storeId;
  private Date balanceDate;
  private Long quAvailable;
  private Long quUnavailable;
  private Boolean flBalanced;
  private Long quSaleTotal;
  private Long quReceipt;
  private Long quTransfer;
  private Long quStockAdj;
  private Long quOnOrder;
  private Long quStoreAvailable;
  private Long quStoreUnavailable;

  public String getItemId() { return this.itemId; }
  public void setItemId(String itemId) { this.itemId = itemId; }

  public String getStoreId() { return this.storeId; }
  public void setStoreId(String storeId) { this.storeId = storeId; }

  public Date getBalanceDate() { return this.balanceDate; }
  public void setBalanceDate(Date balanceDate) { this.balanceDate = balanceDate; }

  public Long getQuAvailable() { return this.quAvailable; }
  public void setQuAvailable(Long quAvailable) { this.quAvailable = quAvailable; }
  public void setQuAvailable(long quAvailable) { this.quAvailable = new Long(quAvailable); }
  public void setQuAvailable(int quAvailable) { this.quAvailable = new Long((long)quAvailable); }

  public Long getQuUnavailable() { return this.quUnavailable; }
  public void setQuUnavailable(Long quUnavailable) { this.quUnavailable = quUnavailable; }
  public void setQuUnavailable(long quUnavailable) { this.quUnavailable = new Long(quUnavailable); }
  public void setQuUnavailable(int quUnavailable) { this.quUnavailable = new Long((long)quUnavailable); }

  public Boolean getFlBalanced() { return this.flBalanced; }
  public void setFlBalanced(Boolean flBalanced) { this.flBalanced = flBalanced; }
  public void setFlBalanced(boolean flBalanced) { this.flBalanced = new Boolean(flBalanced); }

  public Long getQuSaleTotal() { return this.quSaleTotal; }
  public void setQuSaleTotal(Long quSaleTotal) { this.quSaleTotal = quSaleTotal; }
  public void setQuSaleTotal(long quSaleTotal) { this.quSaleTotal = new Long(quSaleTotal); }
  public void setQuSaleTotal(int quSaleTotal) { this.quSaleTotal = new Long((long)quSaleTotal); }

  public Long getQuReceipt() { return this.quReceipt; }
  public void setQuReceipt(Long quReceipt) { this.quReceipt = quReceipt; }
  public void setQuReceipt(long quReceipt) { this.quReceipt = new Long(quReceipt); }
  public void setQuReceipt(int quReceipt) { this.quReceipt = new Long((long)quReceipt); }

  public Long getQuTransfer() { return this.quTransfer; }
  public void setQuTransfer(Long quTransfer) { this.quTransfer = quTransfer; }
  public void setQuTransfer(long quTransfer) { this.quTransfer = new Long(quTransfer); }
  public void setQuTransfer(int quTransfer) { this.quTransfer = new Long((long)quTransfer); }

  public Long getQuStockAdj() { return this.quStockAdj; }
  public void setQuStockAdj(Long quStockAdj) { this.quStockAdj = quStockAdj; }
  public void setQuStockAdj(long quStockAdj) { this.quStockAdj = new Long(quStockAdj); }
  public void setQuStockAdj(int quStockAdj) { this.quStockAdj = new Long((long)quStockAdj); }

  public Long getQuOnOrder() { return this.quOnOrder; }
  public void setQuOnOrder(Long quOnOrder) { this.quOnOrder = quOnOrder; }
  public void setQuOnOrder(long quOnOrder) { this.quOnOrder = new Long(quOnOrder); }
  public void setQuOnOrder(int quOnOrder) { this.quOnOrder = new Long((long)quOnOrder); }

  public Long getQuStoreAvailable() { return this.quStoreAvailable; }
  public void setQuStoreAvailable(Long quStoreAvailable) { this.quStoreAvailable = quStoreAvailable; }
  public void setQuStoreAvailable(long quStoreAvailable) { this.quStoreAvailable = new Long(quStoreAvailable); }
  public void setQuStoreAvailable(int quStoreAvailable) { this.quStoreAvailable = new Long((long)quStoreAvailable); }

  public Long getQuStoreUnavailable() { return this.quStoreUnavailable; }
  public void setQuStoreUnavailable(Long quStoreUnavailable) { this.quStoreUnavailable = quStoreUnavailable; }
  public void setQuStoreUnavailable(long quStoreUnavailable) { this.quStoreUnavailable = new Long(quStoreUnavailable); }
  public void setQuStoreUnavailable(int quStoreUnavailable) { this.quStoreUnavailable = new Long((long)quStoreUnavailable); }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmItmSohOracleBean bean = new ArmItmSohOracleBean();
      bean.itemId = getStringFromResultSet(rs, "ITEM_ID");
      bean.storeId = getStringFromResultSet(rs, "STORE_ID");
      bean.balanceDate = getDateFromResultSet(rs, "BALANCE_DATE");
      bean.quAvailable = getLongFromResultSet(rs, "QU_AVAILABLE");
      bean.quUnavailable = getLongFromResultSet(rs, "QU_UNAVAILABLE");
      bean.flBalanced = getBooleanFromResultSet(rs, "FL_BALANCED");
      bean.quSaleTotal = getLongFromResultSet(rs, "QU_SALE_TOTAL");
      bean.quReceipt = getLongFromResultSet(rs, "QU_RECEIPT");
      bean.quTransfer = getLongFromResultSet(rs, "QU_TRANSFER");
      bean.quStockAdj = getLongFromResultSet(rs, "QU_STOCK_ADJ");
      bean.quOnOrder = getLongFromResultSet(rs, "QU_ON_ORDER");
      bean.quStoreAvailable = getLongFromResultSet(rs, "QU_STORE_AVAILABLE");
      bean.quStoreUnavailable = getLongFromResultSet(rs, "QU_STORE_UNAVAILABLE");
      list.add(bean);
    }
    return (ArmItmSohOracleBean[]) list.toArray(new ArmItmSohOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getItemId(), Types.VARCHAR);
    addToList(list, this.getStoreId(), Types.VARCHAR);
    addToList(list, this.getBalanceDate(), Types.TIMESTAMP);
    addToList(list, this.getQuAvailable(), Types.DECIMAL);
    addToList(list, this.getQuUnavailable(), Types.DECIMAL);
    addToList(list, this.getFlBalanced(), Types.DECIMAL);
    addToList(list, this.getQuSaleTotal(), Types.DECIMAL);
    addToList(list, this.getQuReceipt(), Types.DECIMAL);
    addToList(list, this.getQuTransfer(), Types.DECIMAL);
    addToList(list, this.getQuStockAdj(), Types.DECIMAL);
    addToList(list, this.getQuOnOrder(), Types.DECIMAL);
    addToList(list, this.getQuStoreAvailable(), Types.DECIMAL);
    addToList(list, this.getQuStoreUnavailable(), Types.DECIMAL);
    return list;
  }

}
