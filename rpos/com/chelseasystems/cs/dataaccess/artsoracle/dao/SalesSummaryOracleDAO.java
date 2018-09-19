/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  java.sql.SQLException;
import  java.util.ArrayList;
import  java.util.Date;
import  java.util.Iterator;
import  java.util.List;
import  com.chelseasystems.cr.currency.ArmCurrency;
import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cr.pos.CompositePOSTransaction;
import  com.chelseasystems.cr.pos.POSLineItem;
import  com.chelseasystems.cr.pos.PaymentTransaction;
import  com.chelseasystems.cr.pos.VoidTransaction;
import  com.chelseasystems.cr.transaction.ITransaction;
import  com.chelseasystems.cr.txnposter.SalesSummary;
import  com.chelseasystems.cs.txnposter.CMSSalesSummary;
import  com.chelseasystems.cs.dataaccess.SalesSummaryDAO;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.RkSalesSummaryOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmSalesSummaryOracleBean;
import  com.chelseasystems.cs.pos.CMSReturnLineItemDetail;
import  com.chelseasystems.cr.pos.ReturnLineItem;
import  com.chelseasystems.cs.util.LineItemPOSUtil;
import  com.chelseasystems.cs.item.CMSItem;


/**
 *
 *  SalesSummary Data Access Object.<br>
 *  This object encapsulates all database access for SalesSummary.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *    <tr><td>Date</td><td>RK_SALES_SUMMARY</td><td>DATE</td></tr>
 *    <tr><td>EmployeeId</td><td>RK_SALES_SUMMARY</td><td>ID_EM</td></tr>
 *    <tr><td>Id</td><td>RK_SALES_SUMMARY</td><td>ID</td></tr>
 *    <tr><td>ItemId</td><td>RK_SALES_SUMMARY</td><td>ID_ITM</td></tr>
 *    <tr><td>RegisterId</td><td>RK_SALES_SUMMARY</td><td>REGISTER_ID</td></tr>
 *    <tr><td>StoreId</td><td>RK_SALES_SUMMARY</td><td>ID_STR_RT</td></tr>
 *    <tr><td>TotalAmount</td><td>RK_SALES_SUMMARY</td><td>NET_AMOUNT</td></tr>
 *    <tr><td>TotalQuantity</td><td>RK_SALES_SUMMARY</td><td>TOTAL_QUANTITY</td></tr>
 *  </table>
 *
 *  @see SalesSummary
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.RkSalesSummaryOracleBean
 *
 */
public class SalesSummaryOracleDAO extends BaseOracleDAO
    implements SalesSummaryDAO {
  private static String selectSql = RkSalesSummaryOracleBean.selectSql;
  private static String insertSql = RkSalesSummaryOracleBean.insertSql;
  private static String updateSql = RkSalesSummaryOracleBean.updateSql + where(RkSalesSummaryOracleBean.COL_ID);
  private static String deleteSql = RkSalesSummaryOracleBean.deleteSql + where(RkSalesSummaryOracleBean.COL_ID);

  /**
   * put your documentation comment here
   * @param object
   * @exception SQLException
   */
  public void insert (SalesSummary object) throws SQLException {
    object.doSetId(this.getNextChelseaId());
    this.execute(new ParametricStatement[] {
      new ParametricStatement(insertSql, fromObjectToBean(object).toList())
    });
  }

  /**
   * put your documentation comment here
   * @param object
   * @exception SQLException
   */
  public void update (SalesSummary object) throws SQLException {
    List params = fromObjectToBean(object).toList();
    params.add(object.getId());
    this.execute(new ParametricStatement[] {
      new ParametricStatement(updateSql, params)
    });
  }

  /**
   * put your documentation comment here
   * @param date
   * @param storeId
   * @return
   * @exception SQLException
   */
  public SalesSummary[] selectByDateStoreId (Date date, String storeId) throws SQLException {
    String whereSql = where(RkSalesSummaryOracleBean.COL_SALES_DATE, RkSalesSummaryOracleBean.COL_ID_STR_RT);
    ArrayList params = new ArrayList();
    params.add(date);
    params.add(storeId);
    return  fromBeansToObjects(query(new RkSalesSummaryOracleBean(), whereSql, params));
  }

  /**
   * put your documentation comment here
   * @param from
   * @param to
   * @param storeId
   * @return
   * @exception SQLException
   */
  public SalesSummary[] selectByDateStoreId (Date from, Date to, String storeId) throws SQLException {
    String whereSql = "where ( " + RkSalesSummaryOracleBean.COL_SALES_DATE + " BETWEEN ? AND ? ) AND " + RkSalesSummaryOracleBean.COL_ID_STR_RT + " = ?";
    ArrayList params = new ArrayList();
    params.add(from);
    params.add(to);
    params.add(storeId);
    return  fromBeansToObjects(query(new RkSalesSummaryOracleBean(), whereSql, params));
  }

  /**
   * put your documentation comment here
   * @param from
   * @param to
   * @param storeId
   * @return
   * @exception SQLException
   */
  public SalesSummary[] selectTotalByDateStoreId (Date from, Date to, String storeId) throws SQLException {
    String whereSql = "where (SUMM.ID_STR_RT=STR.ID_STR_RT AND SUMM.ID_ITM = ITEM.ID_ITM AND ITEM.ID_DPT_POS = DEPT.ID_DPT_POS AND ITEM.ID_DPT_POS = CLASS.ID_DPT(+) AND ITEM.ID_CLSS = CLASS.ID_CLSS(+) AND ITEM.ID_ITM = ITEMSTORE.ID_ITM " + " AND SUMM.ID_STR_RT = ITEMSTORE.ID_STR_RT AND SALES_DATE" + " BETWEEN ? AND ? ) AND SUMM.ID_STR_RT = ?";
    String selectSql = "SELECT SUMM.ID_STR_RT ID_STR_RT, NVL(ITEMSTORE.NM_ITM,'NONE') ITEM_DESC, " + "ITEM.ID_DPT_POS DEPT_ID, DEPT.NM_DPT_POS DEPT_NAME, SUMM.ID_ITM ID_ITM, NVL(ITEM.ID_PRT_ITM,'NONE') PRT_ITM, CLASS.NM_CLSS CLASS_DESC, NULL ID, " + "NULL REGISTER_ID, NULL ID_EM, NULL SALES_DATE, NULL MARKDOWN_AMT, NULL TYPE, SUM(Arm_Util_Pkg.Convert_To_Number(NET_AMOUNT,STR.TY_CNY)) NET_AMOUNT, SUM(TOTAL_QUANTITY) TOTAL_QUANTITY, "
        + "SUM(Arm_Util_Pkg.Convert_To_Number(TAX_AMOUNT,STR.TY_CNY)) TAX_AMOUNT, SUM(Arm_Util_Pkg.Convert_To_Number(REG_TAX_AMOUNT,STR.TY_CNY)) REG_TAX_AMOUNT FROM " + "PA_STR_RTL STR,RK_SALES_SUMMARY SUMM, AS_ITM ITEM,  ARM_CLASS CLASS, AS_ITM_RTL_STR ITEMSTORE, ID_DPT_PS DEPT " + whereSql + " GROUP BY SUMM.ID_STR_RT, ITEMSTORE.NM_ITM, ITEM.ID_DPT_POS, DEPT.NM_DPT_POS, SUMM.ID_ITM, ITEM.ID_PRT_ITM, CLASS.NM_CLSS";
    ArrayList params = new ArrayList();
    params.add(from);
    params.add(to);
    params.add(storeId);
    return  fromBeansToObjects(query(new ArmSalesSummaryOracleBean(), selectSql, params));
  }

  /**
   * put your documentation comment here
   * @param paymentTransaction
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] createForPaymentTransaction (PaymentTransaction paymentTransaction) throws SQLException {
    Date date = paymentTransaction.getProcessDate();
    int multiplier = 1;
    CompositePOSTransaction compositePOSTransaction;
    if (paymentTransaction instanceof VoidTransaction) {
      VoidTransaction trans = (VoidTransaction)paymentTransaction;
      ITransaction originalTrans = trans.getOriginalTransaction();
      if (originalTrans instanceof CompositePOSTransaction) {
        multiplier = -1;
        compositePOSTransaction = (CompositePOSTransaction)originalTrans;
      }
      else {
        // do nothing if original was not a composite POS transaction
        // (we're ignoring layaways and nothing else has items and payments)
        return  new ParametricStatement[0];
      }
    }
    else if (paymentTransaction instanceof CompositePOSTransaction) {
      compositePOSTransaction = (CompositePOSTransaction)paymentTransaction;
    }
    else {
      // do nothing if transaction is not a composite POS transaction
      // (we're ignoring layaways and nothing else has items and payments)
      return  new ParametricStatement[0];
    }
    ArrayList parametricStatements = new ArrayList();
    SalesSummary[] currentSalesSummaries = this.selectByDateStoreId(date, compositePOSTransaction.getStore().getId());
    ArrayList newSalesSummaries = new ArrayList();
    ArrayList updatedSalesSummaries = new ArrayList();
    this.processSaleLineItems(compositePOSTransaction, multiplier, currentSalesSummaries, newSalesSummaries, updatedSalesSummaries);
    this.processReturnLineItems(compositePOSTransaction, multiplier, currentSalesSummaries, newSalesSummaries, updatedSalesSummaries);
    for (Iterator iter = updatedSalesSummaries.iterator(); iter.hasNext();) {
      SalesSummary salesSummary = (SalesSummary)iter.next();
      List list = fromObjectToBean(salesSummary).toList();
      list.add(salesSummary.getId());
      parametricStatements.add(new ParametricStatement(updateSql, list));
    }
    //    for (int index = 0 ; index < currentSalesSummaries.length ; index++) {
    //      List list = fromObjectToBean(currentSalesSummaries[index]).toList();
    //      list.add(currentSalesSummaries[index].getId());
    //      parametricStatements.add(new ParametricStatement(updateSql, list));
    //    }
    for (Iterator iter = newSalesSummaries.iterator(); iter.hasNext();) {
      SalesSummary salesSummary = (SalesSummary)iter.next();
      salesSummary.doSetId(this.getNextChelseaId());
      parametricStatements.add(new ParametricStatement(insertSql, fromObjectToBean(salesSummary).toList()));
    }
    return  (ParametricStatement[])parametricStatements.toArray(new ParametricStatement[parametricStatements.size()]);
  }

  /**
   * put your documentation comment here
   * @param compositePOSTransaction
   * @param multiplier
   * @param currentSalesSummaries
   * @param newSalesSummaries
   * @param updatedSalesSummaries
   */
  private void processSaleLineItems (CompositePOSTransaction compositePOSTransaction, int multiplier, SalesSummary[] currentSalesSummaries, ArrayList newSalesSummaries, ArrayList updatedSalesSummaries) {
    Date date = compositePOSTransaction.getProcessDate();
    String storeId = compositePOSTransaction.getStore().getId();
    String operatorId = compositePOSTransaction.getTheOperator().getId();
    String type = CMSSalesSummary.SALE_TYPE;
    //    String registerId = compositePOSTransaction.getId();
    //    int registerIdIndex = registerId.indexOf('*') + 1;
    //    registerId = registerId.substring(registerIdIndex, registerId.indexOf('*', registerIdIndex));
    String registerId = compositePOSTransaction.getRegisterId();
    POSLineItem[] lineItems = compositePOSTransaction.getSaleLineItemsArray();
    this.processLineItems(lineItems, multiplier, currentSalesSummaries, newSalesSummaries, updatedSalesSummaries, date, storeId, operatorId, registerId, type);
  }

  /**
   * put your documentation comment here
   * @param compositePOSTransaction
   * @param multiplier
   * @param currentSalesSummaries
   * @param newSalesSummaries
   * @param updatedSalesSummaries
   */
  private void processReturnLineItems (CompositePOSTransaction compositePOSTransaction, int multiplier, SalesSummary[] currentSalesSummaries, ArrayList newSalesSummaries, ArrayList updatedSalesSummaries) {
    Date date = compositePOSTransaction.getProcessDate();
    String storeId = compositePOSTransaction.getStore().getId();
    String operatorId = compositePOSTransaction.getTheOperator().getId();
    String type = CMSSalesSummary.RETURN_TYPE;
    //    String registerId = compositePOSTransaction.getId();
    //    int registerIdIndex = registerId.indexOf('*') + 1;
    //    registerId = registerId.substring(registerIdIndex, registerId.indexOf('*', registerIdIndex));
    String registerId = compositePOSTransaction.getRegisterId();
    POSLineItem[] lineItems = compositePOSTransaction.getReturnLineItemsArray();
    this.processLineItems(lineItems, multiplier*-1, currentSalesSummaries, newSalesSummaries, updatedSalesSummaries, date, storeId, operatorId, registerId, type);
  }

  /**
   * put your documentation comment here
   * @param lineItems
   * @param multiplier
   * @param currentSalesSummaries
   * @param newSalesSummaries
   * @param updatedSalesSummaries
   * @param date
   * @param storeId
   * @param operatorId
   * @param registerId
   * @param type
   */
  private void processLineItems (POSLineItem[] lineItems, int multiplier, SalesSummary[] currentSalesSummaries, ArrayList newSalesSummaries, ArrayList updatedSalesSummaries, Date date, String storeId, String operatorId, String registerId, String type) {
    for (int index = 0; index < lineItems.length; index++) {
      if (lineItems[index] instanceof ReturnLineItem) {
        CMSReturnLineItemDetail returnLnItmDtl = (CMSReturnLineItemDetail)lineItems[index].getLineItemDetailsArray()[0];
        if (returnLnItmDtl.getConsignmentLineItemDetail() != null || returnLnItmDtl.getPresaleLineItemDetail() != null)
          continue;
      }
      int lineItemQuantity = lineItems[index].getQuantity().intValue()*multiplier;
      ArmCurrency lineItemAmount = lineItems[index].getExtendedNetAmount().multiply(multiplier);
      ArmCurrency markdownAmount = lineItems[index].getExtendedReductionAmount().multiply(multiplier);
      String notOnFileItemID = LineItemPOSUtil.getNotOnFileItemId(lineItems[index]);
      String notOnFileDeptID = LineItemPOSUtil.getNotOnFileItemDept(lineItems[index]);
      String notOnFileClassID = LineItemPOSUtil.getNotOnFileItemClass(lineItems[index]);
      String deptId = (notOnFileDeptID != null) ? notOnFileDeptID : lineItems[index].getItem().getDepartment();
      String classId = (notOnFileClassID != null) ? notOnFileClassID : ((CMSItem)lineItems[index].getItem()).getClassId();
      String itemId = lineItems[index].getItem().getId();
      boolean found = false;
      for (int currentIndex = 0; currentIndex < currentSalesSummaries.length; currentIndex++) {
        CMSSalesSummary salesSummary = (CMSSalesSummary)currentSalesSummaries[currentIndex];
        if (salesSummary.getItemId().equals(itemId) && ((salesSummary.getDeptID() != null && salesSummary.getDeptID().equals(deptId)) || salesSummary.getDeptID() == null) && ((salesSummary.getClassID() != null && salesSummary.getClassID().equals(classId)) || salesSummary.getClassID() == null) && ((salesSummary.getMiscItemId()
            != null && notOnFileItemID != null && salesSummary.getMiscItemId().equals(notOnFileItemID)) || notOnFileItemID == null) && salesSummary.getRegisterId().equals(registerId) && salesSummary.getEmployeeId().equals(operatorId) && (salesSummary.getType() != null && salesSummary.getType().equals(type))) {
          found = true;
          CMSSalesSummary updatedSummary = null;
          for (int updatedSummaryIndex = 0; updatedSummaryIndex < updatedSalesSummaries.size(); updatedSummaryIndex++) {
            updatedSummary = (CMSSalesSummary)updatedSalesSummaries.get(updatedSummaryIndex);
            if (updatedSummary.getItemId().equals(itemId) && ((updatedSummary.getDeptID() != null && updatedSummary.getDeptID().equals(deptId)) || updatedSummary.getDeptID() == null) && ((updatedSummary.getClassID() != null && updatedSummary.getClassID().equals(classId)) || updatedSummary.getClassID()
                == null) && ((updatedSummary.getMiscItemId() != null && notOnFileItemID != null && updatedSummary.getMiscItemId().equals(notOnFileItemID)) || notOnFileItemID == null) && updatedSummary.getRegisterId().equals(registerId) && updatedSummary.getEmployeeId().equals(operatorId) && (updatedSummary.getType()
                != null && updatedSummary.getType().equals(type))) {
              break;
            }
            else {
              updatedSummary = null;
            }
          }
          if (updatedSummary == null) {
            updatedSummary = (CMSSalesSummary)salesSummary.clone();
          }
          ArmCurrency currentAmount = updatedSummary.getTotalAmount();
          ArmCurrency currMarkdownAmount = updatedSummary.getMarkdownAmt();
          int currentQuantity = updatedSummary.getTotalQuantity().intValue();
          try {
            updatedSummary.doSetTotalAmount(currentAmount.add(lineItemAmount));
            updatedSummary.doSetMarkdownAmt(currMarkdownAmount.add(markdownAmount));
          } catch (Exception ex) {
          // ignore currency exception?
          }
          updatedSummary.doSetTotalQuantity(new Long(currentQuantity + lineItemQuantity));
          // work-around for cloudscape date truncation problem.
          updatedSummary.doSetDate(date);
          updatedSalesSummaries.add(updatedSummary);
        }
      }
      if (!found) {
        for (Iterator newIter = newSalesSummaries.iterator(); newIter.hasNext();) {
          CMSSalesSummary salesSummary = (CMSSalesSummary)newIter.next();
          if (salesSummary.getItemId().equals(itemId) && ((salesSummary.getDeptID() != null && salesSummary.getDeptID().equals(deptId)) || salesSummary.getDeptID() == null) && ((salesSummary.getClassID() != null && salesSummary.getClassID().equals(classId)) || salesSummary.getClassID() == null) && (
              (salesSummary.getMiscItemId() != null && notOnFileItemID != null && salesSummary.getMiscItemId().equals(notOnFileItemID)) || notOnFileItemID == null) && salesSummary.getRegisterId().equals(registerId) && salesSummary.getEmployeeId().equals(operatorId) && (salesSummary.getType() != null
              && salesSummary.getType().equals(type))) {
            found = true;
            ArmCurrency currentAmount = salesSummary.getTotalAmount();
            ArmCurrency currMarkdownAmount = salesSummary.getMarkdownAmt();
            int currentQuantity = salesSummary.getTotalQuantity().intValue();
            try {
              salesSummary.doSetTotalAmount(currentAmount.add(lineItemAmount));
              salesSummary.doSetMarkdownAmt(currMarkdownAmount.add(markdownAmount));
            } catch (Exception ex) {
            // ignore currency exception?
            }
            salesSummary.doSetTotalQuantity(new Long(currentQuantity + lineItemQuantity));
            salesSummary.doSetDate(date);
          }
        }
      }
      if (!found) {
        CMSSalesSummary salesSummary = new CMSSalesSummary();
        salesSummary.doSetDate(date);
        salesSummary.doSetItemId(itemId);
        salesSummary.doSetStoreId(storeId);
        salesSummary.doSetEmployeeId(operatorId);
        salesSummary.doSetRegisterId(registerId);
        salesSummary.doSetTotalAmount(lineItemAmount);
        salesSummary.doSetTotalQuantity(new Long(lineItemQuantity));
        salesSummary.doSetMarkdownAmt(markdownAmount);
        salesSummary.doSetType(type);
        salesSummary.doSetClassID(classId);
        salesSummary.doSetDeptID(deptId);
        salesSummary.doSetMiscItemId(notOnFileItemID);
        newSalesSummaries.add(salesSummary);
      }
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  protected BaseOracleBean getDatabeanInstance () {
    return  new RkSalesSummaryOracleBean();
  }

  /**
   * put your documentation comment here
   * @param beans
   * @return
   * @exception SQLException
   */
  private SalesSummary[] fromBeansToObjects (BaseOracleBean[] beans) throws SQLException {
    SalesSummary[] array = new SalesSummary[beans.length];
    for (int i = 0; i < array.length; i++) {
      if (beans[i] instanceof RkSalesSummaryOracleBean)
        array[i] = fromBeanToObject((RkSalesSummaryOracleBean)beans[i]);
      else if (beans[i] instanceof ArmSalesSummaryOracleBean)
        array[i] = fromBeanToObject((ArmSalesSummaryOracleBean)beans[i]);
      else {}
    }
    return  array;
  }

  /**
   * put your documentation comment here
   * @param bean
   * @return
   * @exception SQLException
   */
  private SalesSummary fromBeanToObject (RkSalesSummaryOracleBean bean) throws SQLException {
    CMSSalesSummary object = new CMSSalesSummary();
    object.doSetId(bean.getId());
    object.doSetDate(bean.getSalesDate());
    object.doSetItemId(bean.getIdItm());
    object.doSetEmployeeId(bean.getIdEm());
    object.doSetStoreId(bean.getIdStrRt());
    object.doSetTotalAmount(bean.getNetAmount());
    object.doSetRegisterId(bean.getRegisterId());
    object.doSetTotalQuantity(bean.getTotalQuantity());
    object.doSetMarkdownAmt(bean.getMarkdownAmt());
    object.doSetType(bean.getType());
    object.doSetClassID(bean.getItemClssId());
    object.doSetDeptID(bean.getItemDeptId());
    object.doSetMiscItemId(bean.getMiscItemId());
    return  object;
  }

  /**
   * put your documentation comment here
   * @param bean
   * @return
   * @exception SQLException
   */
  private SalesSummary fromBeanToObject (ArmSalesSummaryOracleBean bean) throws SQLException {
    CMSSalesSummary object = new CMSSalesSummary();
    object.doSetId(bean.getId());
    object.doSetDate(bean.getSalesDate());
    object.doSetItemId(bean.getIdItm());
    object.doSetEmployeeId(bean.getIdEm());
    object.doSetStoreId(bean.getIdStrRt());
    object.doSetTotalAmount(bean.getNetAmount());
    object.doSetRegisterId(bean.getRegisterId());
    object.doSetTotalQuantity(bean.getTotalQuantity());
    object.doSetDeptDesc(bean.getDeptDesc());
    object.doSetDeptID(bean.getDeptID());
    object.doSetItemDesc(bean.getItemDesc());
    if (bean.getClassDesc() != null)
      object.doSetClassDesc(bean.getClassDesc());
    else
      object.doSetClassDesc("");
    if (bean.getVPN() != null)
      object.doSetVPN(bean.getVPN());
    else
      object.doSetVPN("");
    object.doSetMarkdownAmt(bean.getMarkdownAmt());
    object.doSetType(bean.getType());
    //        object.doSetClassID();
    return  object;
  }

  /**
   * put your documentation comment here
   * @param summ
   * @return
   */
  private RkSalesSummaryOracleBean fromObjectToBean (SalesSummary summ) {
    RkSalesSummaryOracleBean bean = new RkSalesSummaryOracleBean();
    CMSSalesSummary object = (CMSSalesSummary)summ;
    bean.setId(object.getId());
    bean.setSalesDate(object.getDate());
    bean.setIdItm(object.getItemId());
    bean.setIdEm(object.getEmployeeId());
    bean.setIdStrRt(object.getStoreId());
    bean.setNetAmount(object.getTotalAmount());
    bean.setRegisterId(object.getRegisterId());
    bean.setTotalQuantity(object.getTotalQuantity());
    bean.setMarkdownAmt(object.getMarkdownAmt());
    bean.setType(object.getType());
    bean.setItemDeptId(object.getDeptID());
    bean.setItemClssId(object.getClassID());
    bean.setMiscItemId(object.getMiscItemId());
    return  bean;
  }
}



