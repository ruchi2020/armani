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
 * This class is an object representation of the Arts database table RK_SALES_SUMMARY<BR>
 * Followings are the column of the table: <BR>
 *     ID(PK) -- VARCHAR2(50)<BR>
 *     SALES_DATE -- DATE(7)<BR>
 *     NET_AMOUNT -- VARCHAR2(75)<BR>
 *     REGISTER_ID -- VARCHAR2(50)<BR>
 *     TOTAL_QUANTITY -- NUMBER(22)<BR>
 *     TAX_AMOUNT -- VARCHAR2(75)<BR>
 *     REG_TAX_AMOUNT -- VARCHAR2(75)<BR>
 *     ID_ITM -- VARCHAR2(128)<BR>
 *     ID_EM -- VARCHAR2(128)<BR>
 *     ID_STR_RT -- VARCHAR2(128)<BR>
 *     MARKDOWN_AMT -- VARCHAR2(75)<BR>
 *     TYPE -- CHAR(1)<BR>
 *     ITEM_DEPT_ID -- VARCHAR2(20)<BR>
 *     ITEM_CLSS_ID -- VARCHAR2(20)<BR>
 *     MISC_ITEM_ID -- VARCHAR2(20)<BR>
 *
 */
public class RkSalesSummaryOracleBean extends BaseOracleBean {

  public RkSalesSummaryOracleBean() {}

  //bug#29615 US is experiancing transaction Posting is extremely slow.  /*+ index(RK_SALES_SUMMARY,IDX_D_SUMMARY) */ this is forced hint to add index because SQL explain plan showed POS db was not using the index. to improve the performace.
  public static String selectSql = "select ID, SALES_DATE, NET_AMOUNT, REGISTER_ID, TOTAL_QUANTITY, TAX_AMOUNT, REG_TAX_AMOUNT, ID_ITM, ID_EM, ID_STR_RT, MARKDOWN_AMT, TYPE, ITEM_DEPT_ID, ITEM_CLSS_ID, MISC_ITEM_ID from RK_SALES_SUMMARY ";
  public static String insertSql = "insert into RK_SALES_SUMMARY (ID, SALES_DATE, NET_AMOUNT, REGISTER_ID, TOTAL_QUANTITY, TAX_AMOUNT, REG_TAX_AMOUNT, ID_ITM, ID_EM, ID_STR_RT, MARKDOWN_AMT, TYPE, ITEM_DEPT_ID, ITEM_CLSS_ID, MISC_ITEM_ID) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update RK_SALES_SUMMARY set ID = ?, SALES_DATE = ?, NET_AMOUNT = ?, REGISTER_ID = ?, TOTAL_QUANTITY = ?, TAX_AMOUNT = ?, REG_TAX_AMOUNT = ?, ID_ITM = ?, ID_EM = ?, ID_STR_RT = ?, MARKDOWN_AMT = ?, TYPE = ?, ITEM_DEPT_ID = ?, ITEM_CLSS_ID = ?, MISC_ITEM_ID = ? ";
  public static String deleteSql = "delete from RK_SALES_SUMMARY ";

  public static String TABLE_NAME = "RK_SALES_SUMMARY";
  public static String COL_ID = "RK_SALES_SUMMARY.ID";
  public static String COL_SALES_DATE = "RK_SALES_SUMMARY.SALES_DATE";
  public static String COL_NET_AMOUNT = "RK_SALES_SUMMARY.NET_AMOUNT";
  public static String COL_REGISTER_ID = "RK_SALES_SUMMARY.REGISTER_ID";
  public static String COL_TOTAL_QUANTITY = "RK_SALES_SUMMARY.TOTAL_QUANTITY";
  public static String COL_TAX_AMOUNT = "RK_SALES_SUMMARY.TAX_AMOUNT";
  public static String COL_REG_TAX_AMOUNT = "RK_SALES_SUMMARY.REG_TAX_AMOUNT";
  public static String COL_ID_ITM = "RK_SALES_SUMMARY.ID_ITM";
  public static String COL_ID_EM = "RK_SALES_SUMMARY.ID_EM";
  public static String COL_ID_STR_RT = "RK_SALES_SUMMARY.ID_STR_RT";
  public static String COL_MARKDOWN_AMT = "RK_SALES_SUMMARY.MARKDOWN_AMT";
  public static String COL_TYPE = "RK_SALES_SUMMARY.TYPE";
  public static String COL_ITEM_DEPT_ID = "RK_SALES_SUMMARY.ITEM_DEPT_ID";
  public static String COL_ITEM_CLSS_ID = "RK_SALES_SUMMARY.ITEM_CLSS_ID";
  public static String COL_MISC_ITEM_ID = "RK_SALES_SUMMARY.MISC_ITEM_ID";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String id;
  private Date salesDate;
  private ArmCurrency netAmount;
  private String registerId;
  private Long totalQuantity;
  private ArmCurrency taxAmount;
  private ArmCurrency regTaxAmount;
  private String idItm;
  private String idEm;
  private String idStrRt;
  private ArmCurrency markdownAmt;
  private String type;
  private String itemDeptId;
  private String itemClssId;
  private String miscItemId;

  public String getId() { return this.id; }
  public void setId(String id) { this.id = id; }

  public Date getSalesDate() { return this.salesDate; }
  public void setSalesDate(Date salesDate) { this.salesDate = salesDate; }

  public ArmCurrency getNetAmount() { return this.netAmount; }
  public void setNetAmount(ArmCurrency netAmount) { this.netAmount = netAmount; }

  public String getRegisterId() { return this.registerId; }
  public void setRegisterId(String registerId) { this.registerId = registerId; }

  public Long getTotalQuantity() { return this.totalQuantity; }
  public void setTotalQuantity(Long totalQuantity) { this.totalQuantity = totalQuantity; }
  public void setTotalQuantity(long totalQuantity) { this.totalQuantity = new Long(totalQuantity); }
  public void setTotalQuantity(int totalQuantity) { this.totalQuantity = new Long((long)totalQuantity); }

  public ArmCurrency getTaxAmount() { return this.taxAmount; }
  public void setTaxAmount(ArmCurrency taxAmount) { this.taxAmount = taxAmount; }

  public ArmCurrency getRegTaxAmount() { return this.regTaxAmount; }
  public void setRegTaxAmount(ArmCurrency regTaxAmount) { this.regTaxAmount = regTaxAmount; }

  public String getIdItm() { return this.idItm; }
  public void setIdItm(String idItm) { this.idItm = idItm; }

  public String getIdEm() { return this.idEm; }
  public void setIdEm(String idEm) { this.idEm = idEm; }

  public String getIdStrRt() { return this.idStrRt; }
  public void setIdStrRt(String idStrRt) { this.idStrRt = idStrRt; }

  public ArmCurrency getMarkdownAmt() { return this.markdownAmt; }
  public void setMarkdownAmt(ArmCurrency markdownAmt) { this.markdownAmt = markdownAmt; }

  public String getType() { return this.type; }
  public void setType(String type) { this.type = type; }

  public String getItemDeptId() { return this.itemDeptId; }
  public void setItemDeptId(String itemDeptId) { this.itemDeptId = itemDeptId; }

  public String getItemClssId() { return this.itemClssId; }
  public void setItemClssId(String itemClssId) { this.itemClssId = itemClssId; }

  public String getMiscItemId() { return this.miscItemId; }
  public void setMiscItemId(String miscItemId) { this.miscItemId = miscItemId; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      RkSalesSummaryOracleBean bean = new RkSalesSummaryOracleBean();
      bean.id = getStringFromResultSet(rs, "ID");
      bean.salesDate = getDateFromResultSet(rs, "SALES_DATE");
      bean.netAmount = getCurrencyFromResultSet(rs, "NET_AMOUNT");
      bean.registerId = getStringFromResultSet(rs, "REGISTER_ID");
      bean.totalQuantity = getLongFromResultSet(rs, "TOTAL_QUANTITY");
      bean.taxAmount = getCurrencyFromResultSet(rs, "TAX_AMOUNT");
      bean.regTaxAmount = getCurrencyFromResultSet(rs, "REG_TAX_AMOUNT");
      bean.idItm = getStringFromResultSet(rs, "ID_ITM");
      bean.idEm = getStringFromResultSet(rs, "ID_EM");
      bean.idStrRt = getStringFromResultSet(rs, "ID_STR_RT");
      bean.markdownAmt = getCurrencyFromResultSet(rs, "MARKDOWN_AMT");
      bean.type = getStringFromResultSet(rs, "TYPE");
      bean.itemDeptId = getStringFromResultSet(rs, "ITEM_DEPT_ID");
      bean.itemClssId = getStringFromResultSet(rs, "ITEM_CLSS_ID");
      bean.miscItemId = getStringFromResultSet(rs, "MISC_ITEM_ID");
      list.add(bean);
    }
    return (RkSalesSummaryOracleBean[]) list.toArray(new RkSalesSummaryOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getId(), Types.VARCHAR);
    addToList(list, this.getSalesDate(), Types.TIMESTAMP);
    addToList(list, this.getNetAmount(), Types.VARCHAR);
    addToList(list, this.getRegisterId(), Types.VARCHAR);
    addToList(list, this.getTotalQuantity(), Types.DECIMAL);
    addToList(list, this.getTaxAmount(), Types.VARCHAR);
    addToList(list, this.getRegTaxAmount(), Types.VARCHAR);
    addToList(list, this.getIdItm(), Types.VARCHAR);
    addToList(list, this.getIdEm(), Types.VARCHAR);
    addToList(list, this.getIdStrRt(), Types.VARCHAR);
    addToList(list, this.getMarkdownAmt(), Types.VARCHAR);
    addToList(list, this.getType(), Types.VARCHAR);
    addToList(list, this.getItemDeptId(), Types.VARCHAR);
    addToList(list, this.getItemClssId(), Types.VARCHAR);
    addToList(list, this.getMiscItemId(), Types.VARCHAR);
    return list;
  }

}
