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
import  java.util.Enumeration;
import  java.util.Hashtable;
import  java.util.List;
import  com.chelseasystems.cr.currency.ArmCurrency;
import  com.chelseasystems.cr.currency.CurrencyException;
import  com.chelseasystems.cr.currency.CurrencyType;
import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cr.pos.CompositePOSTransaction;
import  com.chelseasystems.cr.pos.POSLineItem;
import  com.chelseasystems.cr.pos.POSLineItemDetail;
import  com.chelseasystems.cr.pos.PaymentTransaction;
import  com.chelseasystems.cr.pos.Reduction;
import  com.chelseasystems.cr.pos.VoidTransaction;
import  com.chelseasystems.cr.transaction.ITransaction;
import  com.chelseasystems.cr.txnposter.TxnTypeSummary;
import  com.chelseasystems.cs.dataaccess.TxnTypeSummaryDAO;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.RkTxnTySumOracleBean;


/**
 *
 *  TxnTypeSummary Data Access Object.<br>
 *  This object encapsulates all database access for TxnTypeSummary.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *    <tr><td>Date</td><td>RK_TXN_TYPE_SUMMARY</td><td>DATE</td></tr>
 *    <tr><td>EmployeeId</td><td>RK_TXN_TYPE_SUMMARY</td><td>ID_EM</td></tr>
 *    <tr><td>Id</td><td>RK_TXN_TYPE_SUMMARY</td><td>ID</td></tr>
 *    <tr><td>ReductionTotal</td><td>RK_TXN_TYPE_SUMMARY</td><td>REDUCTION_TOT</td></tr>
 *    <tr><td>RegionalTaxTotal</td><td>RK_TXN_TYPE_SUMMARY</td><td>REG_TAX_TOTAL</td></tr>
 *    <tr><td>RegisterId</td><td>RK_TXN_TYPE_SUMMARY</td><td>REGISTER_ID</td></tr>
 *    <tr><td>StoreId</td><td>RK_TXN_TYPE_SUMMARY</td><td>ID_STR_RT</td></tr>
 *    <tr><td>TaxTotal</td><td>RK_TXN_TYPE_SUMMARY</td><td>TAX_TOTAL</td></tr>
 *    <tr><td>Total</td><td>RK_TXN_TYPE_SUMMARY</td><td>TOTAL</td></tr>
 *    <tr><td>TransactionCount</td><td>RK_TXN_TYPE_SUMMARY</td><td>COUNT</td></tr>
 *    <tr><td>TxnType</td><td>RK_TXN_TYPE_SUMMARY</td><td>TXN_TYPE</td></tr>
 *    <tr><td>VoidedTxnType</td><td>RK_TXN_TYPE_SUMMARY</td><td>VOIDED_TYPE</td></tr>
 *  </table>
 *
 *  @see TxnTypeSummary
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.RkTxnTySumOracleBean
 *
 */
public class TxnTypeSummaryOracleDAO extends BaseOracleDAO
    implements TxnTypeSummaryDAO {
  private static String selectSql = RkTxnTySumOracleBean.selectSql;
  private static String insertSql = RkTxnTySumOracleBean.insertSql;
  private static String updateSql = RkTxnTySumOracleBean.updateSql + where(RkTxnTySumOracleBean.COL_ID);
  private static String deleteSql = RkTxnTySumOracleBean.deleteSql + where(RkTxnTySumOracleBean.COL_ID);
  public static final String TOTAL = "Total";

  /**
   * put your documentation comment here
   * @param object
   * @exception SQLException
   */
  public void insert (TxnTypeSummary object) throws SQLException {
    this.execute(new ParametricStatement[] {
      new ParametricStatement(insertSql, fromObjectToBean(object).toList())
    });
  }

  /**
   * put your documentation comment here
   * @param object
   * @exception SQLException
   */
  public void update (TxnTypeSummary object) throws SQLException {
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
  public TxnTypeSummary[] selectByDateStoreId (Date date, String storeId) throws SQLException {
    String whereSql = where(RkTxnTySumOracleBean.COL_SUMMARY_DATE, RkTxnTySumOracleBean.COL_ID_STR_RT);
    ArrayList params = new ArrayList();
    params.add(date);
    params.add(storeId);
    return  fromBeansToObjects(query(new RkTxnTySumOracleBean(), whereSql, params));
  }

  /**
   * put your documentation comment here
   * @param date
   * @param storeId
   * @param employeeId
   * @return 
   * @exception SQLException
   */
  public TxnTypeSummary[] selectByDateStoreIdEmployeeId (Date date, String storeId, String employeeId) throws SQLException {
    String whereSql = where(RkTxnTySumOracleBean.COL_SUMMARY_DATE, RkTxnTySumOracleBean.COL_ID_STR_RT, RkTxnTySumOracleBean.COL_ID_EM);
    ArrayList params = new ArrayList();
    params.add(date);
    params.add(storeId);
    params.add(employeeId);
    return  fromBeansToObjects(query(new RkTxnTySumOracleBean(), whereSql, params));
  }

  /**
   * put your documentation comment here
   * @param paymentTransaction
   * @return 
   * @exception SQLException
   */
  public ParametricStatement[] createForPaymentTransaction (PaymentTransaction paymentTransaction) throws SQLException {
    CurrencyType baseType = paymentTransaction.getBaseCurrencyType();
    ArrayList parametricStatements = new ArrayList();
    Date date = paymentTransaction.getProcessDate();
    String storeId = paymentTransaction.getStore().getId();
    String employeeId = paymentTransaction.getTheOperator().getId();
    //    String registerId = paymentTransaction.getId();
    //    int registerIdIndex = registerId.indexOf('*') + 1;
    //    registerId = registerId.substring(registerIdIndex, registerId.indexOf('*', registerIdIndex));
    String registerId = paymentTransaction.getRegisterId();
    String txnType = paymentTransaction.getTransactionType();
    String voidedType = "";
    ArmCurrency txnAmount;
    ArmCurrency taxAmount;
    ArmCurrency regionalTaxAmount;
    Hashtable reductionAmounts;
    if (paymentTransaction instanceof VoidTransaction) {
      ITransaction originalTxn = ((VoidTransaction)paymentTransaction).getOriginalTransaction();
      if (!(originalTxn instanceof PaymentTransaction)) {
        return  new ParametricStatement[0];
      }
      PaymentTransaction originalPaymentTransaction = (PaymentTransaction)originalTxn;
      voidedType = originalPaymentTransaction.getTransactionType();
      txnAmount = originalPaymentTransaction.getTotalPaymentAmount();
      taxAmount = this.getTax(originalPaymentTransaction);
      regionalTaxAmount = this.getRegionalTax(originalPaymentTransaction);
      reductionAmounts = this.getReductions(originalPaymentTransaction, false);
    } 
    else {
      txnAmount = paymentTransaction.getTotalPaymentAmount();
      taxAmount = this.getTax(paymentTransaction);
      regionalTaxAmount = this.getRegionalTax(paymentTransaction);
      reductionAmounts = this.getReductions(paymentTransaction, false);
    }
    ArmCurrency totalReduction = (ArmCurrency)reductionAmounts.get(TxnTypeSummaryOracleDAO.TOTAL);
    reductionAmounts.remove(TxnTypeSummaryOracleDAO.TOTAL);
    TxnTypeSummary[] currentTxnTypeSummaries = this.selectByDateStoreIdEmployeeId(date, storeId, employeeId);
    boolean found = false;
    for (int index = 0; index < currentTxnTypeSummaries.length; index++) {
      if (txnType.equals(currentTxnTypeSummaries[index].getTxnType()) && registerId.equals(currentTxnTypeSummaries[index].getRegisterId()) && voidedType.equals(currentTxnTypeSummaries[index].getVoidedTxnType())) {
        found = true;
        ArmCurrency currentTotal = currentTxnTypeSummaries[index].getTotal();
        if (currentTotal == null) {
          currentTotal = new ArmCurrency(baseType, 0.0d);
        }
        ArmCurrency currentTaxTotal = currentTxnTypeSummaries[index].getTaxTotal();
        if (currentTaxTotal == null) {
          currentTaxTotal = new ArmCurrency(baseType, 0.0d);
        }
        ArmCurrency currentRegionalTaxTotal = currentTxnTypeSummaries[index].getRegionalTaxTotal();
        if (currentRegionalTaxTotal == null) {
          currentRegionalTaxTotal = new ArmCurrency(baseType, 0.0d);
        }
        ArmCurrency currentReductionTotal = currentTxnTypeSummaries[index].getReductionTotal();
        if (currentReductionTotal == null) {
          currentReductionTotal = new ArmCurrency(baseType, 0.0d);
        }
        Hashtable currentReductions = currentTxnTypeSummaries[index].getReductions();
        if (currentReductions.get(TxnTypeSummary.DEALS) == null) {
          currentReductions.put(TxnTypeSummary.DEALS, new ArmCurrency(baseType, 0.0));
        }
        if (currentReductions.get(TxnTypeSummary.MARKDOWNS) == null) {
          currentReductions.put(TxnTypeSummary.MARKDOWNS, new ArmCurrency(baseType, 0.0d));
        }
        if (currentReductions.get(TxnTypeSummary.DISCOUNTS) == null) {
          currentReductions.put(TxnTypeSummary.DISCOUNTS, new ArmCurrency(baseType, 0.0d));
        }
        try {
          currentTxnTypeSummaries[index].doSetTransactionCount(currentTxnTypeSummaries[index].getTransactionCount() + 1);
          currentTxnTypeSummaries[index].doSetTotal(currentTotal.add(txnAmount));
          currentTxnTypeSummaries[index].doSetTaxTotal(currentTaxTotal.add(taxAmount));
          currentTxnTypeSummaries[index].doSetRegionalTaxTotal(currentRegionalTaxTotal.add(regionalTaxAmount));
          currentTxnTypeSummaries[index].doSetReductionTotal(currentReductionTotal.add(totalReduction));
          ArmCurrency deals = ((ArmCurrency)currentReductions.get(TxnTypeSummary.DEALS)).add(((ArmCurrency)reductionAmounts.get(TxnTypeSummary.DEALS)));
          ArmCurrency markdowns = ((ArmCurrency)currentReductions.get(TxnTypeSummary.MARKDOWNS)).add(((ArmCurrency)reductionAmounts.get(TxnTypeSummary.MARKDOWNS)));
          ArmCurrency discounts = ((ArmCurrency)currentReductions.get(TxnTypeSummary.DISCOUNTS)).add(((ArmCurrency)reductionAmounts.get(TxnTypeSummary.DISCOUNTS)));
          currentReductions.put(TxnTypeSummary.DEALS, deals);
          currentReductions.put(TxnTypeSummary.MARKDOWNS, markdowns);
          currentReductions.put(TxnTypeSummary.DISCOUNTS, discounts);
          currentTxnTypeSummaries[index].doSetReductions(currentReductions);
          // This has to be done to prevent a rounding problem going
          // to/from the cloudscape database. ~mda
          currentTxnTypeSummaries[index].doSetDate(date);
        } catch (Exception ex) {
        // ignore currency exception?
        }
        List list = fromObjectToBean(currentTxnTypeSummaries[index]).toList();
        list.add(currentTxnTypeSummaries[index].getId());
        parametricStatements.add(new ParametricStatement(updateSql, list));
        break;
      }
    }
    if (!found) {
      TxnTypeSummary txnTypeSummary = new TxnTypeSummary();
      txnTypeSummary.doSetDate(date);
      txnTypeSummary.doSetEmployeeId(employeeId);
      txnTypeSummary.doSetStoreId(storeId);
      txnTypeSummary.doSetRegisterId(registerId);
      txnTypeSummary.doSetTransactionCount(1L);
      txnTypeSummary.doSetTotal(txnAmount);
      txnTypeSummary.doSetTaxTotal(taxAmount);
      txnTypeSummary.doSetRegionalTaxTotal(regionalTaxAmount);
      txnTypeSummary.doSetReductionTotal(totalReduction);
      txnTypeSummary.doSetReductions(reductionAmounts);
      txnTypeSummary.doSetTxnType(txnType);
      txnTypeSummary.doSetVoidedTxnType(voidedType);
      txnTypeSummary.doSetId(this.getNextChelseaId());
      parametricStatements.add(new ParametricStatement(insertSql, fromObjectToBean(txnTypeSummary).toList()));
    }
    return  (ParametricStatement[])parametricStatements.toArray(new ParametricStatement[parametricStatements.size()]);
  }

  /**
   * put your documentation comment here
   * @return 
   */
  protected BaseOracleBean getDatabeanInstance () {
    return  new RkTxnTySumOracleBean();
  }

  /**
   * put your documentation comment here
   * @param beans
   * @return 
   * @exception SQLException
   */
  private TxnTypeSummary[] fromBeansToObjects (BaseOracleBean[] beans) throws SQLException {
    TxnTypeSummary[] array = new TxnTypeSummary[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = fromBeanToObject(beans[i]);
    return  array;
  }

  /**
   * put your documentation comment here
   * @param baseBean
   * @return 
   * @exception SQLException
   */
  private TxnTypeSummary fromBeanToObject (BaseOracleBean baseBean) throws SQLException {
    RkTxnTySumOracleBean bean = (RkTxnTySumOracleBean)baseBean;
    TxnTypeSummary object = new TxnTypeSummary();
    object.doSetId(bean.getId());
    object.doSetDate(bean.getSummaryDate());
    object.doSetStoreId(bean.getIdStrRt());
    object.doSetEmployeeId(bean.getIdEm());
    object.doSetRegisterId(bean.getRegisterId());
    object.doSetTransactionCount(bean.getTxnCount().longValue());
    object.doSetTotal(bean.getTotal());
    object.doSetTxnType(bean.getTxnType());
    object.doSetVoidedTxnType(bean.getVoidedType());
    object.doSetTaxTotal(bean.getTaxTotal());
    object.doSetRegionalTaxTotal(bean.getRegTaxTotal());
    object.doSetReductionTotal(bean.getReductionTot());
    Hashtable reductions = new Hashtable(10);
    reductions.put(TxnTypeSummary.DEALS, bean.getDealTotal());
    reductions.put(TxnTypeSummary.MARKDOWNS, bean.getMarkdownTot());
    reductions.put(TxnTypeSummary.DISCOUNTS, bean.getDiscountTot());
    object.doSetReductions(reductions);
    return  object;
  }

  /**
   * put your documentation comment here
   * @param object
   * @return 
   */
  private RkTxnTySumOracleBean fromObjectToBean (TxnTypeSummary object) {
    RkTxnTySumOracleBean bean = new RkTxnTySumOracleBean();
    bean.setId(object.getId());
    bean.setSummaryDate(object.getDate());
    bean.setIdStrRt(object.getStoreId());
    bean.setIdEm(object.getEmployeeId());
    bean.setRegisterId(object.getRegisterId());
    bean.setTxnCount(object.getTransactionCount());
    bean.setTotal(object.getTotal());
    bean.setTxnType(object.getTxnType());
    bean.setVoidedType(object.getVoidedTxnType());
    bean.setTaxTotal(object.getTaxTotal());
    bean.setRegTaxTotal(object.getRegionalTaxTotal());
    bean.setReductionTot(object.getReductionTotal());
    Hashtable reductions = object.getReductions();
    ArmCurrency deals = (ArmCurrency)reductions.get(TxnTypeSummary.DEALS);
    bean.setDealTotal(deals);
    ArmCurrency markdowns = (ArmCurrency)reductions.get(TxnTypeSummary.MARKDOWNS);
    bean.setMarkdownTot(markdowns);
    ArmCurrency discounts = (ArmCurrency)reductions.get(TxnTypeSummary.DISCOUNTS);
    bean.setDiscountTot(discounts);
    return  bean;
  }

  // This method is used rather than just compositeTransaction.getCompositeTaxAmount
  // because of the current need to avoid adding in layaway totals.
  private ArmCurrency getTax (PaymentTransaction transaction) {
    ArmCurrency retVal = null;
    if (transaction instanceof CompositePOSTransaction) {
      CompositePOSTransaction compositeTransaction = (CompositePOSTransaction)transaction;
      try {
        retVal = compositeTransaction.getSaleTaxAmount().subtract(compositeTransaction.getReturnTaxAmount());
      } catch (CurrencyException ignore) {
      // ignore currency exception. Should never be a problem.
      }
    }
    if (retVal == null) {
      retVal = new ArmCurrency(transaction.getBaseCurrencyType(), 0.0d);
    }
    return  retVal;
  }

  // This method is used rather than just compositeTransaction.getCompositeRegionalTaxAmount
  // because of the current need to avoid adding in layaway totals.
  private ArmCurrency getRegionalTax (PaymentTransaction transaction) {
    ArmCurrency retVal = null;
    if (transaction instanceof CompositePOSTransaction) {
      CompositePOSTransaction compositeTransaction = (CompositePOSTransaction)transaction;
      try {
        retVal = compositeTransaction.getSaleRegionalTaxAmount().subtract(compositeTransaction.getReturnRegionalTaxAmount());
      } catch (CurrencyException ignore) {
      // ignore currency exception. Should never be a problem.
      }
    }
    if (retVal == null) {
      retVal = new ArmCurrency(transaction.getBaseCurrencyType(), 0.0d);
    }
    return  retVal;
  }

  /**
   * Return the reduction totals. Currently, index 0 is total overall, index 1
   * is total discounts, index 2 is total non-deal markdowns, and 3 is total deal markdowns.
   */
  private Hashtable getReductions (PaymentTransaction transaction, boolean negate) {
    Hashtable retVal = new Hashtable(10);
    CurrencyType baseCurrencyType = ((PaymentTransaction)transaction).getBaseCurrencyType();
    if (transaction instanceof CompositePOSTransaction) {
      CompositePOSTransaction compositeTransaction = (CompositePOSTransaction)transaction;
      try {
        Enumeration saleEnum = compositeTransaction.getSaleLineItems();
        ArmCurrency[] saleLineItemReductions = getLineItemReductions(saleEnum, baseCurrencyType);
        Enumeration returnEnum = compositeTransaction.getReturnLineItems();
        ArmCurrency[] returnLineItemReductions = getLineItemReductions(returnEnum, baseCurrencyType);
        ArmCurrency total = saleLineItemReductions[0].subtract(returnLineItemReductions[0]);
        ArmCurrency deals = saleLineItemReductions[1].subtract(returnLineItemReductions[1]);
        ArmCurrency markdowns = saleLineItemReductions[2].subtract(returnLineItemReductions[2]);
        ArmCurrency discounts = saleLineItemReductions[3].subtract(returnLineItemReductions[3]);
        if (negate) {
          total = total.multiply(-1);
          deals = deals.multiply(-1);
          markdowns = markdowns.multiply(-1);
          discounts = discounts.multiply(-1);
        }
        retVal.put(TxnTypeSummaryOracleDAO.TOTAL, total);
        retVal.put(TxnTypeSummary.DEALS, deals);
        retVal.put(TxnTypeSummary.MARKDOWNS, markdowns);
        retVal.put(TxnTypeSummary.DISCOUNTS, discounts);
      } catch (CurrencyException ignore) {
      // ignore currency exception. *Should* never be a problem for a single store.
      }
    } 
    else {
      retVal.put(TxnTypeSummaryOracleDAO.TOTAL, new ArmCurrency(baseCurrencyType, 0.0d));
      retVal.put(TxnTypeSummary.DEALS, new ArmCurrency(baseCurrencyType, 0.0d));
      retVal.put(TxnTypeSummary.MARKDOWNS, new ArmCurrency(baseCurrencyType, 0.0d));
      retVal.put(TxnTypeSummary.DISCOUNTS, new ArmCurrency(baseCurrencyType, 0.0d));
    }
    return  retVal;
  }

  /**
   * put your documentation comment here
   * @param enum
   * @param baseCurrencyType
   * @return 
   * @exception CurrencyException
   */
  private ArmCurrency[] getLineItemReductions (Enumeration enm, CurrencyType baseCurrencyType) throws CurrencyException {
    ArmCurrency[] retVal = new ArmCurrency[] {
      new ArmCurrency(baseCurrencyType, 0.0d), new ArmCurrency(baseCurrencyType, 0.0d), new ArmCurrency(baseCurrencyType, 0.0d), new ArmCurrency(baseCurrencyType, 0.0d)
    };
    while (enm.hasMoreElements()) {
      POSLineItem lineItem = (POSLineItem)enm.nextElement();
      Enumeration detailEnum = lineItem.getLineItemDetails();
      while (detailEnum.hasMoreElements()) {
        POSLineItemDetail lineItemDetail = (POSLineItemDetail)detailEnum.nextElement();
        Enumeration reductionsEnum = lineItemDetail.getReductions();
        while (reductionsEnum.hasMoreElements()) {
          Reduction reduction = (Reduction)reductionsEnum.nextElement();
          String reason = reduction.getReason();
          ArmCurrency reductionAmount = reduction.getAmount();
          retVal[0] = retVal[0].add(reductionAmount);
          if (reason.endsWith("Deal Markdown")) {
            retVal[1] = retVal[1].add(reductionAmount);
          } 
          else if (reason.endsWith("Markdown")) {
            retVal[2] = retVal[2].add(reductionAmount);
          } 
          else if (reason.endsWith("Discount")) {
            retVal[3] = retVal[3].add(reductionAmount);
          }
        }
      }
    }
    return  retVal;
  }
}



