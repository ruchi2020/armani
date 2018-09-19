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
 * This class is an object representation of the Arts database table V_ARM_ITEM_SALE<BR>
 * Followings are the column of the table: <BR>
 *     ID_STR_RT -- VARCHAR2(128)<BR>
 *     DIV -- VARCHAR2(7)<BR>
 *     DEPT_ID -- VARCHAR2(20)<BR>
 *     DEPT_NM -- VARCHAR2(40)<BR>
 *     CLASS_ID -- VARCHAR2(20)<BR>
 *     CLASS_NM -- VARCHAR2(50)<BR>
 *     ITEM_ID -- VARCHAR2(128)<BR>
 *     ITEM_NM -- VARCHAR2(40)<BR>
 *     BARCODE -- VARCHAR2(50)<BR>
 *     SALES_DATE -- DATE(7)<BR>
 *     SALE_MARKDOWN_AMT -- VARCHAR2(75)<BR>
 *     NET_SALE_AMT -- VARCHAR2(75)<BR>
 *     TOTAL_SALE_QTY -- NUMBER(22)<BR>
 *     RTN_MARKDOWN_AMT -- VARCHAR2(75)<BR>
 *     NET_RTN_AMT -- VARCHAR2(75)<BR>
 *     TOTAL_RTN_QTY -- NUMBER(22)<BR>
 *
 */
public class VArmItemSaleOracleBean extends BaseOracleBean {

  public VArmItemSaleOracleBean() {}

  public static String selectSql = "select ID_STR_RT, DIV, DEPT_ID, DEPT_NM, CLASS_ID, CLASS_NM, ITEM_ID, ITEM_NM, BARCODE, SALES_DATE, SALE_MARKDOWN_AMT, NET_SALE_AMT, TOTAL_SALE_QTY, RTN_MARKDOWN_AMT, NET_RTN_AMT, TOTAL_RTN_QTY from V_ARM_ITEM_SALE ";
  public static String insertSql = "insert into V_ARM_ITEM_SALE (ID_STR_RT, DIV, DEPT_ID, DEPT_NM, CLASS_ID, CLASS_NM, ITEM_ID, ITEM_NM, BARCODE, SALES_DATE, SALE_MARKDOWN_AMT, NET_SALE_AMT, TOTAL_SALE_QTY, RTN_MARKDOWN_AMT, NET_RTN_AMT, TOTAL_RTN_QTY) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update V_ARM_ITEM_SALE set ID_STR_RT = ?, DIV = ?, DEPT_ID = ?, DEPT_NM = ?, CLASS_ID = ?, CLASS_NM = ?, ITEM_ID = ?, ITEM_NM = ?, BARCODE = ?, SALES_DATE = ?, SALE_MARKDOWN_AMT = ?, NET_SALE_AMT = ?, TOTAL_SALE_QTY = ?, RTN_MARKDOWN_AMT = ?, NET_RTN_AMT = ?, TOTAL_RTN_QTY = ? ";
  public static String deleteSql = "delete from V_ARM_ITEM_SALE ";

  public static String TABLE_NAME = "V_ARM_ITEM_SALE";
  public static String COL_ID_STR_RT = "V_ARM_ITEM_SALE.ID_STR_RT";
  public static String COL_DIV = "V_ARM_ITEM_SALE.DIV";
  public static String COL_DEPT_ID = "V_ARM_ITEM_SALE.DEPT_ID";
  public static String COL_DEPT_NM = "V_ARM_ITEM_SALE.DEPT_NM";
  public static String COL_CLASS_ID = "V_ARM_ITEM_SALE.CLASS_ID";
  public static String COL_CLASS_NM = "V_ARM_ITEM_SALE.CLASS_NM";
  public static String COL_ITEM_ID = "V_ARM_ITEM_SALE.ITEM_ID";
  public static String COL_ITEM_NM = "V_ARM_ITEM_SALE.ITEM_NM";
  public static String COL_BARCODE = "V_ARM_ITEM_SALE.BARCODE";
  public static String COL_SALES_DATE = "V_ARM_ITEM_SALE.SALES_DATE";
  public static String COL_SALE_MARKDOWN_AMT = "V_ARM_ITEM_SALE.SALE_MARKDOWN_AMT";
  public static String COL_NET_SALE_AMT = "V_ARM_ITEM_SALE.NET_SALE_AMT";
  public static String COL_TOTAL_SALE_QTY = "V_ARM_ITEM_SALE.TOTAL_SALE_QTY";
  public static String COL_RTN_MARKDOWN_AMT = "V_ARM_ITEM_SALE.RTN_MARKDOWN_AMT";
  public static String COL_NET_RTN_AMT = "V_ARM_ITEM_SALE.NET_RTN_AMT";
  public static String COL_TOTAL_RTN_QTY = "V_ARM_ITEM_SALE.TOTAL_RTN_QTY";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idStrRt;
  private String div;
  private String deptId;
  private String deptNm;
  private String classId;
  private String classNm;
  private String itemId;
  private String itemNm;
  private String barcode;
  private Date salesDate;
  private ArmCurrency saleMarkdownAmt;
  private ArmCurrency netSaleAmt;
  private Long totalSaleQty;
  private ArmCurrency rtnMarkdownAmt;
  private ArmCurrency netRtnAmt;
  private Long totalRtnQty;

  public String getIdStrRt() { return this.idStrRt; }
  public void setIdStrRt(String idStrRt) { this.idStrRt = idStrRt; }

  public String getDiv() { return this.div; }
  public void setDiv(String div) { this.div = div; }

  public String getDeptId() { return this.deptId; }
  public void setDeptId(String deptId) { this.deptId = deptId; }

  public String getDeptNm() { return this.deptNm; }
  public void setDeptNm(String deptNm) { this.deptNm = deptNm; }

  public String getClassId() { return this.classId; }
  public void setClassId(String classId) { this.classId = classId; }

  public String getClassNm() { return this.classNm; }
  public void setClassNm(String classNm) { this.classNm = classNm; }

  public String getItemId() { return this.itemId; }
  public void setItemId(String itemId) { this.itemId = itemId; }

  public String getItemNm() { return this.itemNm; }
  public void setItemNm(String itemNm) { this.itemNm = itemNm; }

  public String getBarcode() { return this.barcode; }
  public void setBarcode(String barcode) { this.barcode = barcode; }

  public Date getSalesDate() { return this.salesDate; }
  public void setSalesDate(Date salesDate) { this.salesDate = salesDate; }

  public ArmCurrency getSaleMarkdownAmt() { return this.saleMarkdownAmt; }
  public void setSaleMarkdownAmt(ArmCurrency saleMarkdownAmt) { this.saleMarkdownAmt = saleMarkdownAmt; }

  public ArmCurrency getNetSaleAmt() { return this.netSaleAmt; }
  public void setNetSaleAmt(ArmCurrency netSaleAmt) { this.netSaleAmt = netSaleAmt; }

  public Long getTotalSaleQty() { return this.totalSaleQty; }
  public void setTotalSaleQty(Long totalSaleQty) { this.totalSaleQty = totalSaleQty; }
  public void setTotalSaleQty(long totalSaleQty) { this.totalSaleQty = new Long(totalSaleQty); }
  public void setTotalSaleQty(int totalSaleQty) { this.totalSaleQty = new Long((long)totalSaleQty); }

  public ArmCurrency getRtnMarkdownAmt() { return this.rtnMarkdownAmt; }
  public void setRtnMarkdownAmt(ArmCurrency rtnMarkdownAmt) { this.rtnMarkdownAmt = rtnMarkdownAmt; }

  public ArmCurrency getNetRtnAmt() { return this.netRtnAmt; }
  public void setNetRtnAmt(ArmCurrency netRtnAmt) { this.netRtnAmt = netRtnAmt; }

  public Long getTotalRtnQty() { return this.totalRtnQty; }
  public void setTotalRtnQty(Long totalRtnQty) { this.totalRtnQty = totalRtnQty; }
  public void setTotalRtnQty(long totalRtnQty) { this.totalRtnQty = new Long(totalRtnQty); }
  public void setTotalRtnQty(int totalRtnQty) { this.totalRtnQty = new Long((long)totalRtnQty); }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      VArmItemSaleOracleBean bean = new VArmItemSaleOracleBean();
      bean.idStrRt = getStringFromResultSet(rs, "ID_STR_RT");
      bean.div = getStringFromResultSet(rs, "DIV");
      bean.deptId = getStringFromResultSet(rs, "DEPT_ID");
      bean.deptNm = getStringFromResultSet(rs, "DEPT_NM");
      bean.classId = getStringFromResultSet(rs, "CLASS_ID");
      bean.classNm = getStringFromResultSet(rs, "CLASS_NM");
      bean.itemId = getStringFromResultSet(rs, "ITEM_ID");
      bean.itemNm = getStringFromResultSet(rs, "ITEM_NM");
      bean.barcode = getStringFromResultSet(rs, "BARCODE");
      bean.salesDate = getDateFromResultSet(rs, "SALES_DATE");
      bean.saleMarkdownAmt = getCurrencyFromResultSet(rs, "SALE_MARKDOWN_AMT");
      bean.netSaleAmt = getCurrencyFromResultSet(rs, "NET_SALE_AMT");
      bean.totalSaleQty = getLongFromResultSet(rs, "TOTAL_SALE_QTY");
      bean.rtnMarkdownAmt = getCurrencyFromResultSet(rs, "RTN_MARKDOWN_AMT");
      bean.netRtnAmt = getCurrencyFromResultSet(rs, "NET_RTN_AMT");
      bean.totalRtnQty = getLongFromResultSet(rs, "TOTAL_RTN_QTY");
      list.add(bean);
    }
    return (VArmItemSaleOracleBean[]) list.toArray(new VArmItemSaleOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdStrRt(), Types.VARCHAR);
    addToList(list, this.getDiv(), Types.VARCHAR);
    addToList(list, this.getDeptId(), Types.VARCHAR);
    addToList(list, this.getDeptNm(), Types.VARCHAR);
    addToList(list, this.getClassId(), Types.VARCHAR);
    addToList(list, this.getClassNm(), Types.VARCHAR);
    addToList(list, this.getItemId(), Types.VARCHAR);
    addToList(list, this.getItemNm(), Types.VARCHAR);
    addToList(list, this.getBarcode(), Types.VARCHAR);
    addToList(list, this.getSalesDate(), Types.TIMESTAMP);
    addToList(list, this.getSaleMarkdownAmt(), Types.VARCHAR);
    addToList(list, this.getNetSaleAmt(), Types.VARCHAR);
    addToList(list, this.getTotalSaleQty(), Types.DECIMAL);
    addToList(list, this.getRtnMarkdownAmt(), Types.VARCHAR);
    addToList(list, this.getNetRtnAmt(), Types.VARCHAR);
    addToList(list, this.getTotalRtnQty(), Types.DECIMAL);
    return list;
  }

}
