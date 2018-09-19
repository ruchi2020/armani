//
// Copyright 2002, Retek Inc. All Rights Reserved.
//
package com.chelseasystems.cs.dataaccess.artsoracle.databean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
 *
 */

public class ArmSalesSummaryOracleBean extends BaseOracleBean {

    public ArmSalesSummaryOracleBean() {}

    public static String selectSql = "select ID, SALES_DATE, NET_AMOUNT, REGISTER_ID, TOTAL_QUANTITY, TAX_AMOUNT, REG_TAX_AMOUNT, ID_ITM, ID_EM, ID_STR_RT, , MARKDOWN_AMT, TYPE, NULL ITEM_DESC, NULL DEPT_ID, NULL DEPT_NAME, NULL PRT_ITM, NULL CLASS_DESC from RK_SALES_SUMMARY ";

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
    private String itemDesc;
    private String deptID;
    private String deptDesc;
    private String vpn;
    private String classDesc;
    private ArmCurrency markdownAmt;
    private String type;

    public String getSelectSql() { return null; }
    public String getInsertSql() { return null; }
    public String getUpdateSql() { return null; }
    public String getDeleteSql() { return null; }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getSalesDate() {
        return this.salesDate;
    }

    public void setSalesDate(Date salesDate) {
        this.salesDate = salesDate;
    }

    public ArmCurrency getNetAmount() {
        return this.netAmount;
    }

    public void setNetAmount(ArmCurrency netAmount) {
        this.netAmount = netAmount;
    }

    public String getRegisterId() {
        return this.registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }

    public Long getTotalQuantity() {
        return this.totalQuantity;
    }

    public void setTotalQuantity(Long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public void setTotalQuantity(long totalQuantity) {
        this.totalQuantity = new Long(totalQuantity);
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = new Long((long) totalQuantity);
    }

    public ArmCurrency getTaxAmount() {
        return this.taxAmount;
    }

    public void setTaxAmount(ArmCurrency taxAmount) {
        this.taxAmount = taxAmount;
    }

    public ArmCurrency getRegTaxAmount() {
        return this.regTaxAmount;
    }

    public void setRegTaxAmount(ArmCurrency regTaxAmount) {
        this.regTaxAmount = regTaxAmount;
    }

    public String getIdItm() {
        return this.idItm;
    }

    public void setIdItm(String idItm) {
        this.idItm = idItm;
    }

    public String getIdEm() {
        return this.idEm;
    }

    public void setIdEm(String idEm) {
        this.idEm = idEm;
    }

    public String getIdStrRt() {
        return this.idStrRt;
    }

    public void setIdStrRt(String idStrRt) {
        this.idStrRt = idStrRt;
    }

    public void setItemDesc(String desc) {
        this.itemDesc = desc;
    }

    public String getItemDesc() {
        return this.itemDesc;
    }

    public void setDeptID(String deptID) {
        this.deptID = deptID;
    }

    public String getDeptID() {
        return this.deptID;
    }

    public void setDeptDesc(String desc) {
        this.deptDesc = desc;
    }

    public String getDeptDesc() {
        return this.deptDesc;
    }

    public void setVPN(String vpn) {
        this.vpn = vpn;
    }

    public String getVPN() {
        return this.vpn;
    }

    public void setClassDesc(String classDesc) {
        this.classDesc = classDesc;
    }

    public String getClassDesc() {
        return this.classDesc;
    }

    public ArmCurrency getMarkdownAmt() { return this.markdownAmt; }

    public void setMarkdownAmt(ArmCurrency markdownAmt) { this.markdownAmt = markdownAmt; }

    public String getType() { return this.type; }

    public void setType(String type) { this.type = type; }

    public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
        ArrayList list = new ArrayList();
        while (rs.next()) {
            ArmSalesSummaryOracleBean bean = new ArmSalesSummaryOracleBean();
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
            bean.deptID = getStringFromResultSet(rs, "DEPT_ID");
            bean.deptDesc = getStringFromResultSet(rs, "DEPT_NAME");
            bean.itemDesc = getStringFromResultSet(rs, "ITEM_DESC");
            bean.vpn = getStringFromResultSet(rs, "PRT_ITM");
            bean.classDesc = getStringFromResultSet(rs, "CLASS_DESC");
            bean.markdownAmt = getCurrencyFromResultSet(rs, "MARKDOWN_AMT");
            bean.type = getStringFromResultSet(rs, "TYPE");

            list.add(bean);
        }
        return (ArmSalesSummaryOracleBean[]) list.toArray(new
                ArmSalesSummaryOracleBean[0]);
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
        return list;
    }

}
