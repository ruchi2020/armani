/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

/////
import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  java.util.Date;
import  java.sql.*;
import  java.util.*;
import  com.chelseasystems.cr.currency.ArmCurrency;
import  com.chelseasystems.cs.txnposter.ArmSalesSummary;
import  com.chelseasystems.cr.txnposter.SalesSummary;


/**
 * put your documentation comment here
 */
public class VArmItemSaleOracleDAO extends BaseOracleDAO
    implements VArmItemSaleDAO {
  private static String selectSql = VArmItemSaleOracleBean.selectSql;
  private static String selectByDeptSql = "SELECT V_ARM_ITEM_SALE.ID_STR_RT, DIV, DEPT_ID, DEPT_NM, NULL CLASS_ID, NULL CLASS_NM, NULL ITEM_ID, NULL ITEM_NM, NULL BARCODE, NULL SALES_DATE, " + "SUM(Arm_Util_Pkg.Convert_To_Number(NET_SALE_AMT, PA_STR_RTL.TY_CNY)) NET_SALE_AMT, SUM(Arm_Util_Pkg.Convert_To_Number(SALE_MARKDOWN_AMT,PA_STR_RTL.TY_CNY)) SALE_MARKDOWN_AMT,"
      + "SUM(TOTAL_SALE_QTY) TOTAL_SALE_QTY,  " + "SUM(Arm_Util_Pkg.Convert_To_Number(NET_RTN_AMT, PA_STR_RTL.TY_CNY)) NET_RTN_AMT, " + "SUM(Arm_Util_Pkg.Convert_To_Number(RTN_MARKDOWN_AMT, PA_STR_RTL.TY_CNY)) RTN_MARKDOWN_AMT," + "SUM(TOTAL_RTN_QTY) TOTAL_RTN_QTY " + "FROM V_ARM_ITEM_SALE,PA_STR_RTL WHERE V_ARM_ITEM_SALE.ID_STR_RT=PA_STR_RTL.ID_STR_RT AND V_ARM_ITEM_SALE.ID_STR_RT = ? AND SALES_DATE BETWEEN ? AND ? AND NVL(DEPT_ID, '-') != '999'"
      + "GROUP BY V_ARM_ITEM_SALE.ID_STR_RT, DIV, DEPT_ID, DEPT_NM";
  private static String selectByDeptClassSql = "SELECT V_ARM_ITEM_SALE.ID_STR_RT, DIV, DEPT_ID, DEPT_NM, CLASS_ID, CLASS_NM, NULL ITEM_ID, NULL ITEM_NM, NULL BARCODE, NULL SALES_DATE, " + "SUM(Arm_Util_Pkg.Convert_To_Number(NET_SALE_AMT, PA_STR_RTL.TY_CNY)) NET_SALE_AMT, SUM(Arm_Util_Pkg.Convert_To_Number(SALE_MARKDOWN_AMT,PA_STR_RTL.TY_CNY)) SALE_MARKDOWN_AMT,"
      + "SUM(TOTAL_SALE_QTY) TOTAL_SALE_QTY,  " + "SUM(Arm_Util_Pkg.Convert_To_Number(NET_RTN_AMT, PA_STR_RTL.TY_CNY)) NET_RTN_AMT, " + "SUM(Arm_Util_Pkg.Convert_To_Number(RTN_MARKDOWN_AMT, PA_STR_RTL.TY_CNY)) RTN_MARKDOWN_AMT," + "SUM(TOTAL_RTN_QTY) TOTAL_RTN_QTY " + "FROM V_ARM_ITEM_SALE,PA_STR_RTL WHERE V_ARM_ITEM_SALE.ID_STR_RT=PA_STR_RTL.ID_STR_RT AND V_ARM_ITEM_SALE.ID_STR_RT = ? AND SALES_DATE BETWEEN ? AND ?  AND NVL(DEPT_ID, '-') != '999'"
      + "GROUP BY V_ARM_ITEM_SALE.ID_STR_RT, DIV, DEPT_ID, DEPT_NM, CLASS_ID, CLASS_NM";
  private static String selectByItemSql = "SELECT V_ARM_ITEM_SALE.ID_STR_RT, DIV, DEPT_ID, DEPT_NM, CLASS_ID, CLASS_NM, ITEM_ID, ITEM_NM, BARCODE, NULL SALES_DATE, " + "SUM(Arm_Util_Pkg.Convert_To_Number(NET_SALE_AMT, PA_STR_RTL.TY_CNY)) NET_SALE_AMT, SUM(Arm_Util_Pkg.Convert_To_Number(SALE_MARKDOWN_AMT,PA_STR_RTL.TY_CNY)) SALE_MARKDOWN_AMT,"
      + "SUM(TOTAL_SALE_QTY) TOTAL_SALE_QTY,  " + "SUM(Arm_Util_Pkg.Convert_To_Number(NET_RTN_AMT, PA_STR_RTL.TY_CNY)) NET_RTN_AMT, " + "SUM(Arm_Util_Pkg.Convert_To_Number(RTN_MARKDOWN_AMT, PA_STR_RTL.TY_CNY)) RTN_MARKDOWN_AMT," + "SUM(TOTAL_RTN_QTY) TOTAL_RTN_QTY " + "FROM V_ARM_ITEM_SALE,PA_STR_RTL WHERE V_ARM_ITEM_SALE.ID_STR_RT=PA_STR_RTL.ID_STR_RT AND V_ARM_ITEM_SALE.ID_STR_RT = ? AND SALES_DATE BETWEEN ? AND ?  AND NVL(DEPT_ID, '-') != '999'"
      + "GROUP BY V_ARM_ITEM_SALE.ID_STR_RT, DIV, DEPT_ID, DEPT_NM, CLASS_ID, CLASS_NM, BARCODE, ITEM_ID, ITEM_NM";
  private static String insertSql = VArmItemSaleOracleBean.insertSql;

  /**
   * put your documentation comment here
   * @param idStrRt
   * @return
   * @exception SQLException
   */
  public ArmSalesSummary[] selectByIdStrRt (String idStrRt) throws SQLException {
    String whereSql = "where ID_STR_RT = ?";
    return  fromBeansToObjects(query(new VArmItemSaleOracleBean(), selectSql + whereSql, idStrRt));
  }

  /**
   * put your documentation comment here
   * @param from
   * @param to
   * @param storeId
   * @return
   * @exception SQLException
   */
  public ArmSalesSummary[] selectTotalByDept (Date from, Date to, String storeId) throws SQLException {
    ArrayList params = new ArrayList();
    params.add(storeId);
    params.add(from);
    params.add(to);
    return  fromBeansToObjects(query(new VArmItemSaleOracleBean(), selectByDeptSql, params));
  }

  /**
   * put your documentation comment here
   * @param from
   * @param to
   * @param storeId
   * @return
   * @exception SQLException
   */
  public ArmSalesSummary[] selectTotalByDeptClass (Date from, Date to, String storeId) throws SQLException {
    ArrayList params = new ArrayList();
    params.add(storeId);
    params.add(from);
    params.add(to);
    return  fromBeansToObjects(query(new VArmItemSaleOracleBean(), selectByDeptClassSql, params));
  }

  /**
   * put your documentation comment here
   * @param from
   * @param to
   * @param storeId
   * @return
   * @exception SQLException
   */
  public ArmSalesSummary[] selectTotalByItem (Date from, Date to, String storeId) throws SQLException {
    ArrayList params = new ArrayList();
    params.add(storeId);
    params.add(from);
    params.add(to);
    return  fromBeansToObjects(query(new VArmItemSaleOracleBean(), selectByItemSql, params));
  }

  //Non public methods begin here!
  //
  protected BaseOracleBean getDatabeanInstance () {
    return  new VArmItemSaleOracleBean();
  }

  /**
   * put your documentation comment here
   * @param beans
   * @return
   */
  private ArmSalesSummary[] fromBeansToObjects (BaseOracleBean[] beans) {
    ArmSalesSummary[] array = new ArmSalesSummary[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = fromBeanToObject(beans[i]);
    return  array;
  }

  /////this method needs to customized
  private ArmSalesSummary fromBeanToObject (BaseOracleBean baseBean) {
    VArmItemSaleOracleBean bean = (VArmItemSaleOracleBean)baseBean;
    ArmSalesSummary object = new ArmSalesSummary();
    object.doSetStoreId(bean.getIdStrRt());
    object.doSetGrpDiv(bean.getDiv());
    object.doSetDeptID(bean.getDeptId());
    object.doSetDeptDesc(bean.getDeptNm());
    object.doSetClassID(bean.getClassId());
    if (bean.getClassNm() != null)
      object.doSetClassDesc(bean.getClassNm());
    else
      object.doSetClassDesc("");
    object.doSetItemId(bean.getItemId());
    object.doSetItemDesc(bean.getItemNm());
    object.doSetDate(bean.getSalesDate());
    object.doSetNetSaleAmt(bean.getNetSaleAmt());
    object.doSetSaleMarkdownAmt(bean.getSaleMarkdownAmt());
    object.doSetSaleQty(bean.getTotalSaleQty().intValue());
    object.doSetNetReturnAmt(bean.getNetRtnAmt());
    object.doSetReturnMarkdownAmt(bean.getRtnMarkdownAmt());
    object.doSetReturnQty(bean.getTotalRtnQty().intValue());
    object.doSetBarCode(bean.getBarcode());
    return  object;
  }
}



