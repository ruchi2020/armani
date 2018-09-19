/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 05/02/2005 | Khyati    | N/A       | Added a payment type check for Redeemables         |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04/19/2005 | Anand     | N/A       | Customizations as per Specifications for House     |
 |      |            |           |           | Account related customizations                     |
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  java.sql.SQLException;
import  com.chelseasystems.cr.collection.CollectionTransaction;
import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cr.store.Store;
import  com.chelseasystems.cs.collection.CMSMiscCollection;
import  com.chelseasystems.cs.dataaccess.CollectionDAO;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.RkCollectionOracleBean;
import  com.chelseasystems.cs.payment.HouseAccount;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.RkPayTrnOracleBean;
import  com.chelseasystems.cs.customer.CMSCustomer;


/**
 *
 *  CollectionTransaction Data Access Object.<br>
 *  This object encapsulates all database access for CollectionTransaction.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *    <tr><td>Amount</td><td>RK_COLLECTION</td><td>AMOUNT</td></tr>
 *    <tr><td>Comment</td><td>RK_COLLECTION</td><td>COMMENT</td></tr>
 *    <tr><td>Id</td><td>RK_COLLECTION</td><td>AI_TRN</td></tr>
 *  </table>
 *
 *  @see CollectionTransaction
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.RkCollectionOracleBean
 *
 */
public class CollectionOracleDAO extends BaseOracleDAO
    implements CollectionDAO {
  private static TransactionOracleDAO transactionDAO = new TransactionOracleDAO();
  private static CustomerOracleDAO customerDAO = new CustomerOracleDAO();
  private static String selectSql = RkCollectionOracleBean.selectSql;
  private static String insertSql = RkCollectionOracleBean.insertSql;
  private static String updateSql = RkCollectionOracleBean.updateSql + where(RkCollectionOracleBean.COL_AI_TRN);
  private static String deleteSql = RkCollectionOracleBean.deleteSql + where(RkCollectionOracleBean.COL_AI_TRN);

  /**
   * put your documentation comment here
   * @param object
   * @return 
   * @exception SQLException
   */
  ParametricStatement getInsertSQL (CollectionTransaction object) throws SQLException {
    return  new ParametricStatement(insertSql, fromObjectToBean(object).toList());
  }

  /**
   * put your documentation comment here
   * @param transactionId
   * @param store
   * @return 
   * @exception SQLException
   */
  CollectionTransaction getById (String transactionId, Store store) throws SQLException {
    String whereSql = where(RkCollectionOracleBean.COL_AI_TRN);
    CollectionTransaction[] collectionTransactions = fromBeansToObjects(query(new RkCollectionOracleBean(), whereSql, transactionId), store);
    if (collectionTransactions == null || collectionTransactions.length == 0)
      return  null;
    return  collectionTransactions[0];
  }

  /**
   * put your documentation comment here
   * @param paymentTransactionBean
   * @param store
   * @return 
   * @exception SQLException
   */
  CollectionTransaction getByRkPayTrnBean (RkPayTrnOracleBean paymentTransactionBean, Store store) throws SQLException {
    CMSMiscCollection collectionTxn = (CMSMiscCollection)getById(paymentTransactionBean.getAiTrn(), store);
    if (paymentTransactionBean.getCustId() != null && paymentTransactionBean.getCustId().trim().length() > 0)
      collectionTxn.setCustomer((CMSCustomer)customerDAO.selectById(paymentTransactionBean.getCustId()));
    return  collectionTxn;
  }

  /**
   * put your documentation comment here
   * @param beans
   * @param store
   * @return 
   * @exception SQLException
   */
  private CollectionTransaction[] fromBeansToObjects (BaseOracleBean[] beans, Store store) throws SQLException {
    CollectionTransaction[] array = new CMSMiscCollection[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = fromBeanToObject(beans[i], store);
    return  array;
  }

  /**
   * put your documentation comment here
   * @param baseBean
   * @param store
   * @return 
   * @exception SQLException
   */
  private CollectionTransaction fromBeanToObject (BaseOracleBean baseBean, Store store) throws SQLException {
    RkCollectionOracleBean bean = (RkCollectionOracleBean)baseBean;
    CMSMiscCollection object = new CMSMiscCollection(bean.getType(), store);
    object.doSetId(bean.getAiTrn());
    object.doSetAmount(bean.getAmount());
    object.doSetComment(bean.getComments());
    //ks: Add Redeembale only if its house account type payment
    if (bean.getType().equals("HOUSE_ACCOUNT_PAYMENT")) {
      HouseAccount redeemable = new HouseAccount();
      redeemable.doSetControlNum(bean.getRedeemableControlId());
      object.doSetRedeemable(redeemable);
    }
    return  object;
  }

  /**
   * put your documentation comment here
   * @param object
   * @return 
   */
  private RkCollectionOracleBean fromObjectToBean (CollectionTransaction object) {
    RkCollectionOracleBean bean = new RkCollectionOracleBean();
    bean.setAiTrn(object.getId());
    bean.setType(object.getType());
    bean.setAmount(object.getAmount());
    bean.setComments(object.getComment());
    if (((CMSMiscCollection)object).getRedeemable() instanceof HouseAccount)
      bean.setRedeemableControlId(((HouseAccount)((CMSMiscCollection)object).getRedeemable()).getControlNum());
    return  bean;
  }
}



