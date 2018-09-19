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
 * This class is an object representation of the Arts database table RK_EMP_SALE<BR>
 * Followings are the column of the table: <BR>
 *     SALE_DATE -- DATE(7)<BR>
 *     AMOUNT -- VARCHAR2(75)<BR>
 *     TRANSACTION_NUMBER -- VARCHAR2(50)<BR>
 *     NET_AMOUNT -- VARCHAR2(75)<BR>
 *     ID_EM -- VARCHAR2(128)<BR>
 *     ID_STR_RT -- VARCHAR2(128)<BR>
 *     TOTAL_QTY -- NUMBER(22)<BR>
 *
 */
public class RkEmpSaleOracleBean extends BaseOracleBean {

    public RkEmpSaleOracleBean() {}

    public static String selectSql = "select SALE_DATE, AMOUNT, TRANSACTION_NUMBER, NET_AMOUNT, ID_EM, ID_STR_RT, TOTAL_QTY, NULL FN_PRS, NULL LN_PRS, 1 TXN_COUNT from RK_EMP_SALE ";
    public static String insertSql = "insert into RK_EMP_SALE (SALE_DATE, AMOUNT, TRANSACTION_NUMBER, NET_AMOUNT, ID_EM, ID_STR_RT, TOTAL_QTY) values (?, ?, ?, ?, ?, ?, ?)";
    public static String updateSql = "update RK_EMP_SALE set SALE_DATE = ?, AMOUNT = ?, TRANSACTION_NUMBER = ?, NET_AMOUNT = ?, ID_EM = ?, ID_STR_RT = ?, TOTAL_QTY = ? ";
    public static String deleteSql = "delete from RK_EMP_SALE ";

    public static String TABLE_NAME = "RK_EMP_SALE";
    public static String COL_SALE_DATE = "RK_EMP_SALE.SALE_DATE";
    public static String COL_AMOUNT = "RK_EMP_SALE.AMOUNT";
    public static String COL_TRANSACTION_NUMBER =
            "RK_EMP_SALE.TRANSACTION_NUMBER";
    public static String COL_NET_AMOUNT = "RK_EMP_SALE.NET_AMOUNT";
    public static String COL_ID_EM = "RK_EMP_SALE.ID_EM";
    public static String COL_ID_STR_RT = "RK_EMP_SALE.ID_STR_RT";
    public static String COL_TOTAL_QTY = "RK_EMP_SALE.TOTAL_QTY";

    public String getSelectSql() {
        return selectSql;
    }

    public String getInsertSql() {
        return insertSql;
    }

    public String getUpdateSql() {
        return updateSql;
    }

    public String getDeleteSql() {
        return deleteSql;
    }

    private Date saleDate;
    private ArmCurrency amount;
    private String transactionNumber;
    private ArmCurrency netAmount;
    private String idEm;
    private String idStrRt;
    private Long totalQty;
    private String firstName;
    private String lastName;
    private Long txnCount;

    public Date getSaleDate() {
        return this.saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public ArmCurrency getAmount() {
        return this.amount;
    }

    public void setAmount(ArmCurrency amount) {
        this.amount = amount;
    }

    public String getTransactionNumber() {
        return this.transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public ArmCurrency getNetAmount() {
        return this.netAmount;
    }

    public void setNetAmount(ArmCurrency netAmount) {
        this.netAmount = netAmount;
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

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getTotalQty() {
        return this.totalQty;
    }

    public void setTotalQty(Long totalQty) {
        this.totalQty = totalQty;
    }

    public void setTotalQty(long totalQty) {
        this.totalQty = new Long(totalQty);
    }

    public void setTotalQty(int totalQty) {
        this.totalQty = new Long((long) totalQty);
    }

    public Long getTxnCount() {
        return this.txnCount;
    }

    public void setTxnCount(Long txnCount) {
        this.txnCount = txnCount;
    }

    public void setTxnCount(long txnCount) {
        this.txnCount = new Long(txnCount);
    }

    public void setTxnCount(int txnCount) {
        this.txnCount = new Long((long) txnCount);
    }

    public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
        ArrayList list = new ArrayList();
        while (rs.next()) {
            RkEmpSaleOracleBean bean = new RkEmpSaleOracleBean();
            bean.saleDate = getDateFromResultSet(rs, "SALE_DATE");
            bean.amount = getCurrencyFromResultSet(rs, "AMOUNT");
            bean.transactionNumber = getStringFromResultSet(rs,"TRANSACTION_NUMBER");
            bean.netAmount = getCurrencyFromResultSet(rs, "NET_AMOUNT");
            bean.idEm = getStringFromResultSet(rs, "ID_EM");
            bean.idStrRt = getStringFromResultSet(rs, "ID_STR_RT");
            bean.totalQty = getLongFromResultSet(rs, "TOTAL_QTY");
            bean.firstName = getStringFromResultSet(rs, "FN_PRS");
            bean.lastName = getStringFromResultSet(rs, "LN_PRS");
            bean.txnCount = getLongFromResultSet(rs, "TXN_COUNT");
            list.add(bean);
        }
        return (RkEmpSaleOracleBean[]) list.toArray(new RkEmpSaleOracleBean[0]);
    }

    public List toList() {
        List list = new ArrayList();
        addToList(list, this.getSaleDate(), Types.TIMESTAMP);
        addToList(list, this.getAmount(), Types.VARCHAR);
        addToList(list, this.getTransactionNumber(), Types.VARCHAR);
        addToList(list, this.getNetAmount(), Types.VARCHAR);
        addToList(list, this.getIdEm(), Types.VARCHAR);
        addToList(list, this.getIdStrRt(), Types.VARCHAR);
        addToList(list, this.getTotalQty(), Types.DECIMAL);
        return list;
    }

}
