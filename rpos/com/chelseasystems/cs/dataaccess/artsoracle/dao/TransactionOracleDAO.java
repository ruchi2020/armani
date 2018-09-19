/*
 * @copyright (c) 1998-2002 Retek Inc
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 7    | 05-05-2005 | Khyati    | N/A       | set trn_seq_Num                                    |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 6      04-28-2005    Megha         N/A        Added new method
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 5    | 04-27-2005 | Rajesh    | N/A       | Inventory adjustment impl                          |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 04-27-2005 | Pankaja   | N/A       |Setting the Register ID                             |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 04-13-2005 | Rajesh    | N/A       |Specs Consignment impl                              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 04-12-2005 | Rajesh    | N/A       |Specs Presale impl                                  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */


package com.chelseasystems.cs.dataaccess.artsoracle.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cr.eod.TransactionEOD;
import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.POSLineItemDetail;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.pos.ReturnLineItemDetail;
import com.chelseasystems.cr.pos.SaleLineItemDetail;
import com.chelseasystems.cr.pos.Transaction;
import com.chelseasystems.cr.pos.VoidTransaction;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.transaction.CommonTransaction;
import com.chelseasystems.cr.transaction.IVoidTransaction;
import com.chelseasystems.cs.dataaccess.LotTransactionDAO;
import com.chelseasystems.cs.dataaccess.PaymentSummaryDAO;
import com.chelseasystems.cs.dataaccess.SalesSummaryDAO;
import com.chelseasystems.cs.dataaccess.TransactionDAO;
import com.chelseasystems.cs.dataaccess.TxnTypeSummaryDAO;
import com.chelseasystems.cs.dataaccess.ArmItmSohDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.RkRtnPosLnItmOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.TrTrnOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmPrsPosLnItmDtlOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmCsgPosLnItmDtlOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmRsvPosLnItmDtlOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmAsisTxnDataOracleBean;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.item.ItemStock;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cs.dataaccess.LoyaltyDAO;
import com.chelseasystems.cs.loyalty.LoyaltyHistory;
import com.chelseasystems.cs.dataaccess.RedeemableIssueDAO;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cs.loyalty.RewardCard;
import com.chelseasystems.cs.paidout.CMSPaidOutTransaction;
import com.chelseasystems.cs.collection.CMSCollectionTransaction;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.PaPrtyOracleBean;
import com.chelseasystems.cr.collection.CollectionTransaction;
import com.chelseasystems.cs.eod.CMSTransactionEOD;


/**
 *
 *  Transaction Data Access Object.<br>
 *  This object encapsulates all database access for Transaction.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *    <tr><td>CreateDate</td><td>TR_TRN</td><td>TS_TRN_CRT</td></tr>
 *    <tr><td>HandWrittenTicketNumber</td><td>TR_TRN</td><td>DE_HND_TCK</td></tr>
 *    <tr><td>Id</td><td>TR_TRN</td><td>AI_TRN</td></tr>
 *    <tr><td>IsNotVoidableReason</td><td>TR_TRN</td><td>TRANSACTION_IS_NOT_VOID_REASON</td></tr>
 *    <tr><td>PostDate</td><td>TR_TRN</td><td>TS_TRN_PST</td></tr>
 *    <tr><td>ProcessDate</td><td>TR_TRN</td><td>TS_TRN_PRC</td></tr>
 *    <tr><td>Store</td><td>TR_TRN</td><td>ID_STR_RT</td></tr>
 *    <tr><td>SubmitDate</td><td>TR_TRN</td><td>TS_TRN_SBM</td></tr>
 *    <tr><td>TheOperator</td><td>TR_TRN</td><td>ID_OPR</td></tr>
 *    <tr><td>TrainingFlag</td><td>TR_TRN</td><td>FL_TRG_TRN</td></tr>
 *  </table>
 *
 *  @see Transaction
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.TrTrnOracleBean
 *
 */
public class TransactionOracleDAO extends BaseOracleDAO implements TransactionDAO {
  private static PaymentTransactionOracleDAO paymentTransactionDAO = new
      PaymentTransactionOracleDAO();
  private static StoreOracleDAO storeDAO = new StoreOracleDAO();
  private static EmployeeOracleDAO employeeDAO = new EmployeeOracleDAO();
  private static VoidTransactionOracleDAO voidTransactionDAO = new VoidTransactionOracleDAO();
  private static EodOracleDAO eodDAO = new EodOracleDAO();
  private static LotTransactionDAO lotTransactionDAO = new LotTransactionOracleDAO();
  private static PaymentSummaryDAO paymentSummaryDAO = new PaymentSummaryOracleDAO();
  private static SalesSummaryDAO salesSummaryDAO = new SalesSummaryOracleDAO();
  private static TxnTypeSummaryDAO txnTypeSummaryDAO = new TxnTypeSummaryOracleDAO();
  private static ReturnedPosLineItemOracleDAO returnedPosLineItemDAO = new
      ReturnedPosLineItemOracleDAO();
  private static ArmPrsPosLnItmDtlOracleDAO armPrsPosLnItmDtlDAO = new ArmPrsPosLnItmDtlOracleDAO();
  private static ArmCsgPosLnItmDtlOracleDAO armCsgPosLnItmDtlDAO = new ArmCsgPosLnItmDtlOracleDAO();
  private static ArmRsvPosLnItmDtlOracleDAO armRsvPosLnItmDtlDAO = new ArmRsvPosLnItmDtlOracleDAO();
  private static ArmItmSohDAO armItmSohDAO = new ArmItmSohOracleDAO();
  private static LoyaltyDAO loyaltyDAO = new LoyaltyOracleDAO();
  private static RedeemableIssueDAO redeemableIssueDAO = new RedeemableIssueOracleDAO();
  private static String updateSqlForVoid = "update " + TrTrnOracleBean.TABLE_NAME + " set "
      + TrTrnOracleBean.COL_ID_VOID + " = ? " + where(TrTrnOracleBean.COL_AI_TRN);
  static String txnDelim = new ConfigMgr("txnnumber.cfg").getString("DELIM");
  //Vivek Mishra : Added to Original PRESALE Open transaction while PRESALE CLOSE return
  Transaction transaction=null;
  //Ends

  /**
   * put your documentation comment here
   * @param originalTransaction
   * @return
   * @exception SQLException
   */
  ParametricStatement getUpdateSQLForVoid(Transaction originalTransaction)
      throws SQLException {
    List params = new ArrayList();
    if (originalTransaction instanceof Transaction) {
      params.add(originalTransaction.getVoidTransaction().getId());
      params.add(originalTransaction.getId());
    }
    return new ParametricStatement(updateSqlForVoid, params);
  }

  /**
   * put your documentation comment here
   * @param object
   * @exception SQLException
   */
  public void insert(CommonTransaction object)
      throws SQLException {
    this.execute(getInsertSQL(object));
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL(CommonTransaction object)
      throws SQLException {
    ArrayList statements = new ArrayList();
    statements.add(new ParametricStatement(TrTrnOracleBean.insertSql
        , this.toTrTrnBean((Transaction)object).toList()));
    if (object instanceof PaymentTransaction) {
      statements.addAll(Arrays.asList(getPaymentTxnInsertSQL((PaymentTransaction)object)));
      if (object instanceof CMSCompositePOSTransaction) {
        CMSCompositePOSTransaction newRsvOpenTxn = ((CMSCompositePOSTransaction)object).
            getNewReservationOpenTxn();
        if (newRsvOpenTxn != null) {
          statements.add(new ParametricStatement(TrTrnOracleBean.insertSql
              , toTrTrnBean(newRsvOpenTxn).toList()));
          statements.addAll(Arrays.asList(getPaymentTxnInsertSQL(newRsvOpenTxn)));
        }
      }
      //statements.addAll(Arrays.asList(armItmSohDAO.createForPaymentTransaction((PaymentTransaction)
          //object)));
    } else if (object instanceof TransactionEOD) {
      statements.addAll(Arrays.asList(eodDAO.getInsertSQL((CMSTransactionEOD)object)));
    }
    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  private ParametricStatement[] getPaymentTxnInsertSQL(PaymentTransaction object)
      throws SQLException {
    ArrayList statements = new ArrayList();
    ArmAsisTxnDataOracleBean asisTxnDataOracleBean = toArmAsisTxnDataOracleBean(object);
    if (asisTxnDataOracleBean != null)
      statements.add(new ParametricStatement(ArmAsisTxnDataOracleBean.insertSql
          , asisTxnDataOracleBean.toList()));
    statements.addAll(Arrays.asList(paymentTransactionDAO.getInsertSQL(object)));
    //            if (object instanceof CMSCompositePOSTransaction && (((CMSCompositePOSTransaction) object).isNoSaleTxn()))
    //                return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
    statements.addAll(Arrays.asList(lotTransactionDAO.createForPaymentTransaction(object)));
    statements.addAll(Arrays.asList(paymentSummaryDAO.createForPaymentTransaction(object)));
    statements.addAll(Arrays.asList(salesSummaryDAO.createForPaymentTransaction(object)));
    statements.addAll(Arrays.asList(txnTypeSummaryDAO.createForPaymentTransaction(object)));
    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param id
   * @return
   * @exception SQLException
   */
  public Transaction selectById(String id)
      throws SQLException {
    Map transactionCache = new Hashtable();
    ////Vivek Mishra : Added to Original PRESALE Open transaction while PRESALE CLOSE return
    //Transaction transaction = this.privateSelectById(id, transactionCache);
    transaction = this.privateSelectById(id, transactionCache);
    //Ends
    if (transaction == null) {
      return null;
    }
    //For Void Transaction
    if (transaction instanceof VoidTransaction) {
      String originalTransactionId = voidTransactionDAO.getOriginalTransactionId(id);
      transaction = (Transaction)this.privateSelectById(originalTransactionId, transactionCache).
          getVoidTransaction();
    }
    //For Composite with ReturnLineItem
    if (transaction instanceof CompositePOSTransaction) {
      CompositePOSTransaction compositePOSTransaction = (CompositePOSTransaction)transaction;
      connectReturnLineItemDetail(compositePOSTransaction, transactionCache);
      connectSaleLineItemDetail(compositePOSTransaction, transactionCache);
      connectPresaleLineItemDetail(compositePOSTransaction, transactionCache);
      connectConsignmentLineItemDetail(compositePOSTransaction, transactionCache);
      connectReservationLineItemDetail(compositePOSTransaction, transactionCache);
    }
    return transaction;
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   */
  public static String getCommonTransactionType(CommonTransaction object) {
    if (object instanceof PaymentTransaction) {
      return ArtsConstants.TRANSACTION_TYPE_PAYMENT;
    }
    if (object instanceof TransactionEOD) {
      return ArtsConstants.TRANSACTION_TYPE_EOD;
    }
    return ArtsConstants.TRANSACTION_TYPE_UNKNOWN;
  }

  /**
   * put your documentation comment here
   * @param beans
   * @return
   * @exception SQLException
   */
  private Transaction[] fromBeansToObjects(BaseOracleBean[] beans)
      throws SQLException {
    Transaction[] array = new Transaction[beans.length];
    for (int i = 0; i < array.length; i++) {
      array[i] = fromBeanToObject(beans[i]);
    }
    return array;
  }

  /**
   * put your documentation comment here
   * @param bean
   * @return
   * @exception SQLException
   */
  private ASISTxnData fromASISTxnBeanToObject(ArmAsisTxnDataOracleBean bean)
      throws SQLException {
    ASISTxnData txn = new ASISTxnData();
    txn.doSetComments(bean.getComments());
    txn.doSetCompanyCode(bean.getCompanyCode());
    txn.doSetCustName(bean.getCustomerName());
    txn.doSetCustNo(bean.getCustomerNo());
    txn.doSetDocType(bean.getFiscalDocType());
    txn.doSetFiscalDocNo(bean.getFiscalDocNo());
    txn.doSetFiscalDocDate(bean.getFiscalDocDate());
    txn.doSetFiscalReceiptDate(bean.getFiscalReceiptDate());
    txn.doSetFiscalReceiptNo(bean.getFiscalReceiptNo());
    txn.doSetRegId(bean.getRegisterId());
    txn.doSetStoreId(bean.getStoreId());
    txn.doSetTxnDate(bean.getTxnDate());
    txn.doSetTxnNo(bean.getTxnNo());
    txn.doSetTxnAmount(bean.getTxnAmount());
    txn.doSetOrderNo(bean.getOrderNo());
    txn.doSetOrderDate(bean.getOrderDate());
    txn.doSetSupplierNo(bean.getSupplierNo());
    txn.doSetSupplierDate(bean.getSupplierDate());
    txn.doSetNotes(bean.getNotes());
    return txn;
  }

  /**
   * put your documentation comment here
   * @param baseBean
   * @return
   * @exception SQLException
   */
  private Transaction fromBeanToObject(BaseOracleBean baseBean)
      throws SQLException {
    TrTrnOracleBean bean = (TrTrnOracleBean)baseBean;
    Store store = storeDAO.selectById(bean.getIdStrRt());
    if (store == null || store.getCurrencyType() == null) {
      throw new SQLException("Transaction has <<null>> store or base currency type.");
    }
    Transaction object = this.getNewTransaction(bean, store);
    if (object == null) {
      return null;
    }
    object.doSetId(bean.getAiTrn());
    object.doSetStore(store);
    object.doSetTheOperator(employeeDAO.selectById(bean.getIdOpr()));
    if (bean.getFlTrgTrn() != null) {
      object.doSetTrainingFlag(bean.getFlTrgTrn().booleanValue());
    }
    object.doSetStore(storeDAO.selectById(bean.getIdStrRt()));
    object.doSetRegisterId(bean.getIdRpstyTnd());
    object.doSetPostDate(bean.getTsTrnPst());
    object.doSetSubmitDate(bean.getTsTrnSbm());
    object.doSetCreateDate(bean.getTsTrnCrt());
    object.doSetProcessDate(bean.getTsTrnPrc());
    object.doSetHandWrittenTicketNumber(bean.getDeHndTck());
    object.doSetIsNotVoidableReason(bean.getNotVoidReason());
    if (bean.getIdVoid() != null && bean.getIdVoid().length() > 0) {
      IVoidTransaction voidTransaction = (IVoidTransaction)this.privateSelectById(bean.getIdVoid(), null);
      object.doSetVoidTransaction(voidTransaction);
      ((VoidTransaction)voidTransaction).doSetOriginalTransaction((Transaction)object);
    }
    List params = new ArrayList();
    params.add(object.getId());
    ArmAsisTxnDataOracleBean[] asisTxnBean = (ArmAsisTxnDataOracleBean[])this.query(new
        ArmAsisTxnDataOracleBean(), where(ArmAsisTxnDataOracleBean.COL_AI_TRN), params);
    ASISTxnData asisTxnData = null;
    if (asisTxnBean != null && asisTxnBean.length > 0) {
      asisTxnData = fromASISTxnBeanToObject(asisTxnBean[0]);
    }
    if (object instanceof CMSCompositePOSTransaction) {
      ((CMSCompositePOSTransaction)object).setFiscalReceiptDate(bean.getFiscalReceiptDate());
      ((CMSCompositePOSTransaction)object).setFiscalReceiptNumber(bean.getFiscalReceiptNumber());
      if (asisTxnData != null)
        ((CMSCompositePOSTransaction)object).doSetASISTxnData(asisTxnData);
    } else if (object instanceof CMSCollectionTransaction) {
      ((CMSCollectionTransaction)object).setFiscalReceiptDate(bean.getFiscalReceiptDate());
      ((CMSCollectionTransaction)object).setFiscalReceiptNumber(bean.getFiscalReceiptNumber());
      if (asisTxnData != null)
        ((CMSCollectionTransaction)object).doSetASISTxnData(asisTxnData);
    } else if (object instanceof CMSPaidOutTransaction) {
      ((CMSPaidOutTransaction)object).setFiscalReceiptDate(bean.getFiscalReceiptDate());
      ((CMSPaidOutTransaction)object).setFiscalReceiptNumber(bean.getFiscalReceiptNumber());
      if (asisTxnData != null)
        ((CMSPaidOutTransaction)object).doSetASISTxnData(asisTxnData);
    }
    /*
     -- this is done in selectById(String)
     if (object instanceof VoidTransaction) {
     String originalTransactionId = voidTransactionDAO.getOriginalTransactionId(object.getId());
     object = this.selectById(originalTransactionId).getVoidTransaction();
     }
     */
    return object;
  }

  /**
   * put your documentation comment here
   * @param id
   * @param transactionCache
   * @return
   * @exception SQLException
   */
  private Transaction privateSelectById(String id, Map transactionCache)
      throws SQLException {
    if (transactionCache == null) {
      transactionCache = new Hashtable();
    }
    if (transactionCache.containsKey(id)) {
      return (Transaction)transactionCache.get(id);
    }
    String whereSql = where(TrTrnOracleBean.COL_AI_TRN);
    Transaction[] transactions = fromBeansToObjects(query(new TrTrnOracleBean(), whereSql, id));
    if (transactions == null || transactions.length == 0 || transactions[0] == null) {
      return null;
    }
    transactionCache.put(transactions[0].getId(), transactions[0]);
    return transactions[0];
  }

  /**
   * put your documentation comment here
   * @param trTrnBean
   * @param store
   * @return
   * @exception SQLException
   */
  private Transaction getNewTransaction(TrTrnOracleBean trTrnBean, Store store)
      throws SQLException {
    if (store == null) {
      store = storeDAO.selectById(trTrnBean.getIdStrRt());
    }
    String type = trTrnBean.getTyTrn();
    if (type.equals(ArtsConstants.TRANSACTION_TYPE_PAYMENT)) {
      Transaction txn = paymentTransactionDAO.getById(trTrnBean.getAiTrn(), store);
      if (txn instanceof CMSNoSaleTransaction && trTrnBean.getTyGuiTrn().trim().equals("REWD")) {
        //((CMSNoSaleTransaction)txn).setTransactionType(trTrnBean.getTyGuiTrn());
        txn = this.getRewardTransaction((CMSNoSaleTransaction)txn, trTrnBean);
      }
      return txn;
    }
    if (type.equals(TRANSACTION_TYPE_EOD)) {
      return eodDAO.getById(trTrnBean.getAiTrn(), store, trTrnBean.getIdRpstyTnd());
    }
    return null;
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   */
  private TrTrnOracleBean toTrTrnBean(Transaction object) {
    TrTrnOracleBean bean = new TrTrnOracleBean();
    bean.setAiTrn(object.getId());
    bean.setIdOpr(object.getTheOperator().getId());
    bean.setTyTrn(getCommonTransactionType(object));
    bean.setTsTmSrt(null);
    bean.setTsTrnBgn(null);
    bean.setTsTrnEnd(null);
    bean.setFlTrgTrn(object.isTrainingFlagOn());
    bean.setFlKyOfl(null);
    bean.setIdStrRt(object.getStore().getId());
    if (object instanceof TransactionEOD) {
      bean.setIdRpstyTnd(((TransactionEOD)object).getRegister().getId());
    } else {
      bean.setIdRpstyTnd(object.getRegisterId());
    }
    bean.setTsTrnPst(object.getPostDate());
    bean.setTsTrnSbm(object.getSubmitDate());
    bean.setTsTrnCrt(object.getCreateDate());
    bean.setTsTrnPrc(object.getProcessDate());
    bean.setTyGuiTrn(object.getTransactionType());
    bean.setNotVoidReason(object.getIsNotVoidableReason());
    if (object.getVoidTransaction() != null) {
      bean.setIdVoid(object.getVoidTransaction().getId());
    }
    bean.setDeHndTck(object.getHandWrittenTicketNumber());
    //ks: get the seq Number from the transactionId
    String trxnId = object.getId();
    int storeAndRegLength = object.getStore().getId().trim().length();
    storeAndRegLength = storeAndRegLength + object.getRegisterId().trim().length();
    if (txnDelim != null && trxnId.indexOf(txnDelim) != -1) {
      storeAndRegLength = storeAndRegLength + (2 * txnDelim.length());
    }
    String seqNum = object.getId().substring(storeAndRegLength);
    bean.setTrnSeqNum(new Long(seqNum));
    if (object instanceof CMSCompositePOSTransaction) {
      CMSCompositePOSTransaction cmsCompositeTxn = (CMSCompositePOSTransaction)object;
      bean.setFiscalReceiptNumber(cmsCompositeTxn.getFiscalReceiptNumber());
      bean.setFiscalReceiptDate(cmsCompositeTxn.getFiscalReceiptDate());
    } else if (object instanceof CMSPaidOutTransaction) {
      CMSPaidOutTransaction cmsPaidOutTxn = (CMSPaidOutTransaction)object;
      bean.setFiscalReceiptNumber(cmsPaidOutTxn.getFiscalReceiptNumber());
      bean.setFiscalReceiptDate(cmsPaidOutTxn.getFiscalReceiptDate());
    } else if (object instanceof CMSCollectionTransaction) {
      CMSCollectionTransaction cmsCollectionTxn = (CMSCollectionTransaction)object;
      bean.setFiscalReceiptNumber(cmsCollectionTxn.getFiscalReceiptNumber());
      bean.setFiscalReceiptDate(cmsCollectionTxn.getFiscalReceiptDate());
    }
    return bean;
  }

  /**
   * put your documentation comment here
   * @param compositePOSTransaction
   * @param transactionCache
   * @exception SQLException
   */
  private void connectSaleLineItemDetail(CompositePOSTransaction compositePOSTransaction
      , Map transactionCache)
      throws SQLException {
    POSLineItem[] saleLineItems = compositePOSTransaction.getSaleLineItemsArray();
    if (saleLineItems != null && saleLineItems.length > 0) {
      for (int i = 0; i < saleLineItems.length; i++) {
        POSLineItemDetail[] lineItemDetails = saleLineItems[i].getLineItemDetailsArray();
        int saleLineItemSeqNum = saleLineItems[i].getSequenceNumber();
        for (int j = 0; j < lineItemDetails.length; j++) {
          int saleLineItemDetailSeqNum = lineItemDetails[j].getSequenceNumber();
          CMSSaleLineItemDetail cmsSaleLnItmDtl = (CMSSaleLineItemDetail)lineItemDetails[j];
          if (cmsSaleLnItmDtl.getTypeCode() != null
              && (cmsSaleLnItmDtl.getTypeCode().longValue()
              == CMSSaleLineItemDetail.POS_LINE_ITEM_TYPE_RETURN.longValue())) { //if (((SaleLineItemDetail)lineItemDetails[j]).isReturned()) {
            RkRtnPosLnItmOracleBean bean = returnedPosLineItemDAO.getBySaleLineItemDetail(
                compositePOSTransaction.getId(), saleLineItemSeqNum, saleLineItemDetailSeqNum);
            String returnTransactionId = bean.getRtnAiTrn();
            int returnLineItemSeqNum = bean.getRtnAiLnItm().intValue();
            int returnLineItemDetailSeqNum = bean.getSequenceNumber().intValue();
            CompositePOSTransaction returnTransaction = (CompositePOSTransaction)this.
                privateSelectById(returnTransactionId, transactionCache);
            ReturnLineItemDetail returnLineItemDetail = (ReturnLineItemDetail)TransactionOracleDAO.
                getPOSLineItemDetail(returnTransaction, returnLineItemSeqNum
                , returnLineItemDetailSeqNum);
            returnLineItemDetail.connectSaleLineItemDetail((SaleLineItemDetail)lineItemDetails[j]);
          } else if (cmsSaleLnItmDtl.getTypeCode() != null
              && (cmsSaleLnItmDtl.getTypeCode().longValue()
              == CMSSaleLineItemDetail.POS_LINE_ITEM_TYPE_PRESALE.longValue())) {
            ArmPrsPosLnItmDtlOracleBean bean = armPrsPosLnItmDtlDAO.getPresaleLineItemDetail(
                compositePOSTransaction.getId(), saleLineItemSeqNum, saleLineItemDetailSeqNum);
            if (bean != null) {
              String prsTxnId = bean.getOrgAiTrn();
              int prsLnItmSeqNum = bean.getOrigAiLnItm().intValue();
              int prsLnItmDtlSeqNum = bean.getOrigSeqNum().intValue();
              CompositePOSTransaction prsTxn = (CompositePOSTransaction)this.privateSelectById(
                  prsTxnId, transactionCache);
              //Vivek Mishra : Added to Original PRESALE Open transaction while PRESALE CLOSE return
              ((CMSCompositePOSTransaction)transaction).setOrgPRSOTxn(prsTxn);
              //Ends
              CMSPresaleLineItemDetail prsLnItmDtl = (CMSPresaleLineItemDetail)TransactionOracleDAO.
                  getPOSLineItemDetail(prsTxn, prsLnItmSeqNum, prsLnItmDtlSeqNum);
              prsLnItmDtl.connectSaleLineItemDetail(cmsSaleLnItmDtl);
            }
          } else if (cmsSaleLnItmDtl.getTypeCode() != null
              && (cmsSaleLnItmDtl.getTypeCode().longValue()
              == CMSSaleLineItemDetail.POS_LINE_ITEM_TYPE_CONSIGNMENT.longValue())) {
            ArmCsgPosLnItmDtlOracleBean bean = armCsgPosLnItmDtlDAO.getConsignmentLineItemDetail(
                compositePOSTransaction.getId(), saleLineItemSeqNum, saleLineItemDetailSeqNum);
            if (bean != null) {
              String csgTxnId = bean.getOrgAiTrn();
              int csgLnItmSeqNum = bean.getOrigAiLnItm().intValue();
              int csgLnItmDtlSeqNum = bean.getOrigSeqNum().intValue();
              CompositePOSTransaction csgTxn = (CompositePOSTransaction)this.privateSelectById(
                  csgTxnId, transactionCache);
              CMSConsignmentLineItemDetail csgLnItmDtl = (CMSConsignmentLineItemDetail)
                  TransactionOracleDAO.getPOSLineItemDetail(csgTxn, csgLnItmSeqNum
                  , csgLnItmDtlSeqNum);
              csgLnItmDtl.connectSaleLineItemDetail(cmsSaleLnItmDtl);
            }
          } else if (cmsSaleLnItmDtl.getTypeCode() != null
              && (cmsSaleLnItmDtl.getTypeCode().longValue()
              == CMSSaleLineItemDetail.POS_LINE_ITEM_TYPE_RESERVATION.longValue())) {
            ArmRsvPosLnItmDtlOracleBean bean = armRsvPosLnItmDtlDAO.getReservationLineItemDetail(
                compositePOSTransaction.getId(), saleLineItemSeqNum, saleLineItemDetailSeqNum);
            if (bean != null) {
              String rsvTxnId = bean.getOrgAiTrn();
              int rsvLnItmSeqNum = bean.getOrigAiLnItm().intValue();
              int rsvLnItmDtlSeqNum = bean.getOrigSeqNum().intValue();
              CMSCompositePOSTransaction rsvTxn = null;
              CMSReservationLineItemDetail rsvLnItmDtl = null;
              if (rsvTxnId.equals(compositePOSTransaction.getId())) {
                rsvTxn = (CMSCompositePOSTransaction)compositePOSTransaction;
                CMSReservationLineItem rsvLnItm = null;
                rsvLnItm = new CMSReservationLineItem(rsvTxn.getNoReservationOpenTransaction()
                    , cmsSaleLnItmDtl.getLineItem().getItem(), cmsSaleLnItmDtl.getSequenceNumber());
                try {
                  rsvLnItm.setQuantity(new Integer(1));
                } catch (Exception e) {
                }
                rsvLnItmDtl = (CMSReservationLineItemDetail)rsvLnItm.getLineItemDetailsArray()[0];
              } else {
                rsvTxn = (CMSCompositePOSTransaction)this.privateSelectById(rsvTxnId
                    , transactionCache);
                rsvLnItmDtl = (CMSReservationLineItemDetail)TransactionOracleDAO.
                    getPOSLineItemDetail(rsvTxn, rsvLnItmSeqNum, rsvLnItmDtlSeqNum);
              }
              rsvLnItmDtl.connectSaleLineItemDetail(cmsSaleLnItmDtl);
            }
          }
        }
      }
    }
  }

  /**
   * put your documentation comment here
   * @param compositePOSTransaction
   * @param transactionCache
   * @exception SQLException
   */
  private void connectPresaleLineItemDetail(CompositePOSTransaction compositePOSTransaction
      , Map transactionCache)
      throws SQLException {
    POSLineItem[] presaleLineItems = ((CMSCompositePOSTransaction)compositePOSTransaction).
        getPresaleLineItemsArray();
    if (presaleLineItems != null && presaleLineItems.length > 0) {
      for (int i = 0; i < presaleLineItems.length; i++) {
        POSLineItemDetail[] lineItemDetails = presaleLineItems[i].getLineItemDetailsArray();
        int presaleLineItemSeqNum = presaleLineItems[i].getSequenceNumber();
        for (int j = 0; j < lineItemDetails.length; j++) {
          int presaleLineItemDetailSeqNum = lineItemDetails[j].getSequenceNumber();
          CMSPresaleLineItemDetail cmsPresaleLnItmDtl = (CMSPresaleLineItemDetail)lineItemDetails[j];
          if (cmsPresaleLnItmDtl.getProcessed()) {
            ArmPrsPosLnItmDtlOracleBean bean = armPrsPosLnItmDtlDAO.getProcessedToLineItemDetail(
                compositePOSTransaction.getId(), presaleLineItemSeqNum, presaleLineItemDetailSeqNum, false);
            if (bean != null) {
              String txnId = bean.getAiTrn();
              int lnItmSeqNum = bean.getAiLnItm().intValue();
              int lnItmDtlSeqNum = bean.getSequenceNum().intValue();
              CompositePOSTransaction prsTxn = (CompositePOSTransaction)this.privateSelectById(
                  txnId, transactionCache);
              POSLineItemDetail posLnItmDtl = TransactionOracleDAO.getPOSLineItemDetail(prsTxn
                  , lnItmSeqNum, lnItmDtlSeqNum);
              if (posLnItmDtl instanceof CMSReturnLineItemDetail) {
                cmsPresaleLnItmDtl.connectReturnLineItemDetail((CMSReturnLineItemDetail)posLnItmDtl);
              } else if (posLnItmDtl instanceof CMSSaleLineItemDetail) {
                cmsPresaleLnItmDtl.connectSaleLineItemDetail((CMSSaleLineItemDetail)posLnItmDtl);
              }
            }
          }
        }
      }
    }
  }

  /**
   * put your documentation comment here
   * @param compositePOSTransaction
   * @param transactionCache
   * @exception SQLException
   */
  private void connectConsignmentLineItemDetail(CompositePOSTransaction compositePOSTransaction
      , Map transactionCache)
      throws SQLException {
    POSLineItem[] csgLineItems = ((CMSCompositePOSTransaction)compositePOSTransaction).
        getConsignmentLineItemsArray();
    if (csgLineItems != null && csgLineItems.length > 0) {
      for (int i = 0; i < csgLineItems.length; i++) {
        POSLineItemDetail[] lineItemDetails = csgLineItems[i].getLineItemDetailsArray();
        int csgLineItemSeqNum = csgLineItems[i].getSequenceNumber();
        for (int j = 0; j < lineItemDetails.length; j++) {
          int csgLineItemDetailSeqNum = lineItemDetails[j].getSequenceNumber();
          CMSConsignmentLineItemDetail cmsCsgLnItmDtl = (CMSConsignmentLineItemDetail)
              lineItemDetails[j];
          if (cmsCsgLnItmDtl.getProcessed()) {
            ArmCsgPosLnItmDtlOracleBean bean = armCsgPosLnItmDtlDAO.getProcessedToLineItemDetail(
                compositePOSTransaction.getId(), csgLineItemSeqNum, csgLineItemDetailSeqNum, false);
            if (bean != null) {
              String txnId = bean.getAiTrn();
              int lnItmSeqNum = bean.getAiLnItm().intValue();
              int lnItmDtlSeqNum = bean.getSequenceNum().intValue();
              CompositePOSTransaction csgTxn = (CompositePOSTransaction)this.privateSelectById(
                  txnId, transactionCache);
              POSLineItemDetail posLnItmDtl = TransactionOracleDAO.getPOSLineItemDetail(csgTxn
                  , lnItmSeqNum, lnItmDtlSeqNum);
              if (posLnItmDtl instanceof CMSReturnLineItemDetail) {
                cmsCsgLnItmDtl.connectReturnLineItemDetail((CMSReturnLineItemDetail)posLnItmDtl);
              } else if (posLnItmDtl instanceof CMSSaleLineItemDetail) {
                cmsCsgLnItmDtl.connectSaleLineItemDetail((CMSSaleLineItemDetail)posLnItmDtl);
              }
            }
          }
        }
      }
    }
  }

  /**
   * put your documentation comment here
   * @param compositePOSTransaction
   * @param transactionCache
   * @exception SQLException
   */
  private void connectReturnLineItemDetail(CompositePOSTransaction compositePOSTransaction
      , Map transactionCache)
      throws SQLException {
    POSLineItem[] returnLineItems = compositePOSTransaction.getReturnLineItemsArray();
    if (returnLineItems != null && returnLineItems.length > 0) {
      for (int i = 0; i < returnLineItems.length; i++) {
        POSLineItemDetail[] lineItemDetails = returnLineItems[i].getLineItemDetailsArray();
        int returnLineItemSeqNum = returnLineItems[i].getSequenceNumber();
        for (int j = 0; j < lineItemDetails.length; j++) {
          int returnLineItemDetailSeqNum = lineItemDetails[j].getSequenceNumber();
          CMSReturnLineItemDetail cmsRtnLnItmDtl = (CMSReturnLineItemDetail)lineItemDetails[j];
          if (cmsRtnLnItmDtl.getTypeCode() != null
              && (cmsRtnLnItmDtl.getTypeCode().equals(CMSReturnLineItemDetail.
              POS_LINE_ITEM_TYPE_PRESALE))) {
            ArmPrsPosLnItmDtlOracleBean bean = armPrsPosLnItmDtlDAO.getPresaleLineItemDetail(
                compositePOSTransaction.getId(), returnLineItemSeqNum, returnLineItemDetailSeqNum);
            if (bean != null) {
              String prsTxnId = bean.getOrgAiTrn();
              int prsLnItmSeqNum = bean.getOrigAiLnItm().intValue();
              int prsLnItmDtlSeqNum = bean.getOrigSeqNum().intValue();
              CompositePOSTransaction prsTxn = (CompositePOSTransaction)this.privateSelectById(
                  prsTxnId, transactionCache);
              CMSPresaleLineItemDetail prsLnItmDtl = (CMSPresaleLineItemDetail)TransactionOracleDAO.
                  getPOSLineItemDetail(prsTxn, prsLnItmSeqNum, prsLnItmDtlSeqNum);
              prsLnItmDtl.connectReturnLineItemDetail(cmsRtnLnItmDtl);
            }
          } else if (cmsRtnLnItmDtl.getTypeCode() != null
              && (cmsRtnLnItmDtl.getTypeCode().equals(CMSReturnLineItemDetail.
              POS_LINE_ITEM_TYPE_CONSIGNMENT))) {
            ArmCsgPosLnItmDtlOracleBean bean = armCsgPosLnItmDtlDAO.getConsignmentLineItemDetail(
                compositePOSTransaction.getId(), returnLineItemSeqNum, returnLineItemDetailSeqNum);
            if (bean != null) {
              String csgTxnId = bean.getOrgAiTrn();
              int csgLnItmSeqNum = bean.getOrigAiLnItm().intValue();
              int csgLnItmDtlSeqNum = bean.getOrigSeqNum().intValue();
              CompositePOSTransaction prsTxn = (CompositePOSTransaction)this.privateSelectById(
                  csgTxnId, transactionCache);
              CMSConsignmentLineItemDetail csgLnItmDtl = (CMSConsignmentLineItemDetail)
                  TransactionOracleDAO.getPOSLineItemDetail(prsTxn, csgLnItmSeqNum
                  , csgLnItmDtlSeqNum);
              csgLnItmDtl.connectReturnLineItemDetail(cmsRtnLnItmDtl);
            }
          } else if (cmsRtnLnItmDtl.getTypeCode() != null
              && (cmsRtnLnItmDtl.getTypeCode().equals(CMSSaleLineItemDetail.
              POS_LINE_ITEM_TYPE_RESERVATION))) {
            ArmRsvPosLnItmDtlOracleBean bean = armRsvPosLnItmDtlDAO.getReservationLineItemDetail(
                compositePOSTransaction.getId(), returnLineItemSeqNum, returnLineItemDetailSeqNum);
            if (bean != null) {
              String rsvTxnId = bean.getOrgAiTrn();
              int rsvLnItmSeqNum = bean.getOrigAiLnItm().intValue();
              int rsvLnItmDtlSeqNum = bean.getOrigSeqNum().intValue();
              CMSCompositePOSTransaction rsvTxn = null;
              CMSReservationLineItemDetail rsvLnItmDtl = null;
              if (rsvTxnId.equals(compositePOSTransaction.getId())) {
                rsvTxn = (CMSCompositePOSTransaction)compositePOSTransaction;
                CMSReservationLineItem rsvLnItm = null;
                rsvLnItm = new CMSReservationLineItem(rsvTxn.getNoReservationOpenTransaction()
                    , cmsRtnLnItmDtl.getLineItem().getItem(), rsvLnItmSeqNum);
                try {
                  rsvLnItm.setQuantity(new Integer(1));
                } catch (Exception e) {
                }
                rsvLnItmDtl = (CMSReservationLineItemDetail)rsvLnItm.getLineItemDetailsArray()[0];
              } else {
                rsvTxn = (CMSCompositePOSTransaction)this.privateSelectById(rsvTxnId
                    , transactionCache);
                rsvLnItmDtl = (CMSReservationLineItemDetail)
                    TransactionOracleDAO.getPOSLineItemDetail(rsvTxn, rsvLnItmSeqNum
                    , rsvLnItmDtlSeqNum);
              }
              rsvLnItmDtl.connectReturnLineItemDetail(cmsRtnLnItmDtl);
            }
          } else {
            RkRtnPosLnItmOracleBean bean = returnedPosLineItemDAO.getByReturnLineItemDetail(
                compositePOSTransaction.getId(), returnLineItemSeqNum, returnLineItemDetailSeqNum);
            if (bean != null) {
              String saleTransactionId = bean.getSaleAiTrn();
              int saleLineItemSeqNum = bean.getSaleAiLnItm().intValue();
              int saleLineItemDetailSeqNum = bean.getSaleDtlSeqNum().intValue();
              CompositePOSTransaction saleTransaction = (CompositePOSTransaction)this.
                  privateSelectById(saleTransactionId, transactionCache);
              SaleLineItemDetail saleLineItemDetail = (SaleLineItemDetail)TransactionOracleDAO.
                  getPOSLineItemDetail(saleTransaction, saleLineItemSeqNum
                  , saleLineItemDetailSeqNum);
              ((ReturnLineItemDetail)lineItemDetails[j]).connectSaleLineItemDetail(
                  saleLineItemDetail);
            }
          } //end else non prs/csg
        }
      }
    }
  }

  /**
   * put your documentation comment here
   * @param compositePOSTransaction
   * @param transactionCache
   * @exception SQLException
   */
  private void connectReservationLineItemDetail(CompositePOSTransaction compositePOSTransaction
      , Map transactionCache)
      throws SQLException {
    POSLineItem[] rsvLineItems = ((CMSCompositePOSTransaction)compositePOSTransaction).
        getReservationLineItemsArray();
    if (rsvLineItems != null && rsvLineItems.length > 0) {
      for (int i = 0; i < rsvLineItems.length; i++) {
        POSLineItemDetail[] lineItemDetails = rsvLineItems[i].getLineItemDetailsArray();
        int rsvLineItemSeqNum = rsvLineItems[i].getSequenceNumber();
        for (int j = 0; j < lineItemDetails.length; j++) {
          int rsvLineItemDetailSeqNum = lineItemDetails[j].getSequenceNumber();
          CMSReservationLineItemDetail cmsRsvLnItmDtl = (CMSReservationLineItemDetail)
              lineItemDetails[j];
          if (cmsRsvLnItmDtl.getProcessed()) {
            ArmRsvPosLnItmDtlOracleBean bean = armRsvPosLnItmDtlDAO.getProcessedToLineItemDetail(
                compositePOSTransaction.getId(), rsvLineItemSeqNum, rsvLineItemDetailSeqNum);
            if (bean != null) {
              String txnId = bean.getAiTrn();
              int lnItmSeqNum = bean.getAiLnItm().intValue();
              int lnItmDtlSeqNum = bean.getSequenceNum().intValue();
              CompositePOSTransaction rsvTxn = null;
              if (txnId.equals(compositePOSTransaction.getId()))
                rsvTxn = compositePOSTransaction;
              else
                rsvTxn = (CompositePOSTransaction)this.privateSelectById(txnId, transactionCache);
              POSLineItemDetail posLnItmDtl = TransactionOracleDAO.getPOSLineItemDetail(rsvTxn
                  , lnItmSeqNum, lnItmDtlSeqNum);
              if (posLnItmDtl instanceof CMSReturnLineItemDetail) {
                cmsRsvLnItmDtl.connectReturnLineItemDetail((CMSReturnLineItemDetail)posLnItmDtl);
              } else if (posLnItmDtl instanceof CMSSaleLineItemDetail) {
                cmsRsvLnItmDtl.connectSaleLineItemDetail((CMSSaleLineItemDetail)posLnItmDtl);
              }
            }
          }
        }
      }
    }
  }

  //util method to get a line item detail from a CompositePOSTransaction
  static private POSLineItemDetail getPOSLineItemDetail(CompositePOSTransaction
      compositePOSTransaction, int lineItemSequenceNumber, int lineItemDetailSequenceNumber) {
    POSLineItem[] posLineItems = compositePOSTransaction.getLineItemsArray();
    for (int i = 0; i < posLineItems.length; i++) {
      if (posLineItems[i].getSequenceNumber() == lineItemSequenceNumber) {
        POSLineItemDetail[] posLineItemDetails = posLineItems[i].getLineItemDetailsArray();
        for (int j = 0; j < posLineItemDetails.length; j++) {
          if (posLineItemDetails[j].getSequenceNumber() == lineItemDetailSequenceNumber) {
            return posLineItemDetails[j];
          }
        }
      }
    }
    return null;
  }

  /**
   * put your documentation comment here
   * @param storeId
   * @param registerId
   * @return
   * @exception SQLException
   */
  public String[] selectMaxTxnId(String storeId, String registerId)
      throws SQLException {
    //     String sql = "select max("+ TrTrnOracleBean.COL_AI_TRN
    //         + ") AS COL_AI_TRN from " + TrTrnOracleBean.TABLE_NAME
    //         + " where " + TrTrnOracleBean.COL_ID_RPSTY_TND + " = ?  and "
    //         +  TrTrnOracleBean.COL_ID_STR_RT + " = ?";
    //ks: will get the maxTxnId from TRN_SEQ_NUM
    String sql = "select max(" + TrTrnOracleBean.COL_TRN_SEQ_NUM + ") AS COL_TRN_SEQ_NUM from "
        + TrTrnOracleBean.TABLE_NAME + " where " + TrTrnOracleBean.COL_ID_RPSTY_TND + " = ?  and "
        + TrTrnOracleBean.COL_ID_STR_RT + " = ?";
    List params = new ArrayList();
    params.add(0, registerId);
    params.add(1, storeId);
    String[] ids = queryForIds(sql, params);
    if (ids == null || ids.length == 0) {
      return null;
    } else {
      return ids;
    }
  }

  /**
   * put your documentation comment here
   * @param theTxn
   * @param trTrnBean
   * @return
   * @exception SQLException
   */
  private RewardTransaction getRewardTransaction(CMSNoSaleTransaction theTxn
      , TrTrnOracleBean trTrnBean)
      throws SQLException {
    RewardTransaction rewardTxn = new RewardTransaction(theTxn.getStore());
    LoyaltyHistory hist = loyaltyDAO.selectHistoryByTransactionId(theTxn.getId())[0];
    rewardTxn.setPoints((long)hist.getPointEarned());
    String rewardId = null;
    if (hist.getReasonCode() != null && hist.getReasonCode().trim().length() > 0) {
      int startIndex = hist.getReasonCode().lastIndexOf("[") + 1;
      int endIndex = hist.getReasonCode().lastIndexOf("]");
      if (startIndex >= 0 && endIndex >= 0 && endIndex > startIndex)
        rewardId = hist.getReasonCode().substring(startIndex, endIndex).trim();
      else { //to take care of existing data
        startIndex = hist.getReasonCode().lastIndexOf(":") + 1;
        if (startIndex >= 0)
          rewardId = hist.getReasonCode().substring(startIndex).trim();
      }
      if (rewardId != null && rewardId.length() > 0) {
        Redeemable redeemable = redeemableIssueDAO.selectRedeemableById(rewardId);
        if (redeemable != null && redeemable instanceof RewardCard) {
          rewardTxn.setRewardCard((RewardCard)redeemable);
        }
      }
    }
    return rewardTxn;
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   */
  private ArmAsisTxnDataOracleBean toArmAsisTxnDataOracleBean(Transaction object) {
    ArmAsisTxnDataOracleBean bean = new ArmAsisTxnDataOracleBean();
    ASISTxnData asisTxnData = null;
    if (object instanceof CMSCompositePOSTransaction) {
      CMSCompositePOSTransaction cmsCompositeTxn = (CMSCompositePOSTransaction)object;
      asisTxnData = cmsCompositeTxn.getASISTxnData();
      bean.setAiTrn(cmsCompositeTxn.getId());
    } else if (object instanceof CMSPaidOutTransaction) {
      CMSPaidOutTransaction cmsPaidOutTxn = (CMSPaidOutTransaction)object;
      asisTxnData = cmsPaidOutTxn.getASISTxnData();
      bean.setAiTrn(cmsPaidOutTxn.getId());
    } else if (object instanceof CMSCollectionTransaction) {
      CMSCollectionTransaction cmsCollectionTxn = (CMSCollectionTransaction)object;
      asisTxnData = cmsCollectionTxn.getASISTxnData();
      bean.setAiTrn(cmsCollectionTxn.getId());
    }
    if (asisTxnData != null) {
      bean.setCompanyCode(asisTxnData.getCompanyCode());
      bean.setStoreId(asisTxnData.getStoreId());
      bean.setRegisterId(asisTxnData.getRegId());
      bean.setTxnNo(asisTxnData.getTxnNo());
      bean.setTxnDate(asisTxnData.getTxnDate());
      bean.setFiscalReceiptNo(asisTxnData.getFiscalReceiptNo());
      bean.setFiscalReceiptDate(asisTxnData.getFiscalReceiptDate());
      bean.setFiscalDocNo(asisTxnData.getFiscalDocNo());
      bean.setFiscalDocDate(asisTxnData.getFiscalDocDate());
      bean.setFiscalDocType(asisTxnData.getDocType());
      bean.setCustomerNo(asisTxnData.getCustNo());
      bean.setCustomerName(asisTxnData.getCustName());
      bean.setComments(asisTxnData.getComments());
      bean.setTxnAmount(asisTxnData.getTxnAmount());
      bean.setOrderNo(asisTxnData.getOrderNo());
      bean.setOrderDate(asisTxnData.getOrderDate());
      bean.setSupplierNo(asisTxnData.getSupplierNo());
      bean.setSupplierDate(asisTxnData.getSupplierDate());
      bean.setNotes(asisTxnData.getNotes());
      return bean;
    }
    return null;
  }
}

