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
import  java.util.List;

import com.chelseasystems.cr.config.ConfigMgr;
import  com.chelseasystems.cr.currency.ArmCurrency;
import  com.chelseasystems.cr.currency.CurrencyException;
import  com.chelseasystems.cr.pos.AdHocQueryConstraints;
import  com.chelseasystems.cr.pos.TransactionHeader;
import  com.chelseasystems.cs.dataaccess.TransactionHeaderDAO;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.RkShipReqOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.RkTxnHeaderOracleBean;
import  com.chelseasystems.cs.pos.CMSTransactionHeader;
import  com.chelseasystems.cs.pos.TransactionSearchString;
import  com.chelseasystems.cr.payment.PaymentMgr;
import  java.util.Arrays;


/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 04-12-2005 | Rajesh    | N/A       |Specs Presale impl                                  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 05-10-2005 | Manpreet  | N/A       |TranscationHistory specs                            |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
/**
 *
 *  TransactionHeader Data Access Object.<br>
 *  This object encapsulates all database access for TransactionHeader.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *    <tr><td>ConsultantFirstName</td><td>RK_TRANSACTION_HEADER</td><td>FN_PRS</td></tr>
 *    <tr><td>ConsultantId</td><td>RK_TRANSACTION_HEADER</td><td>CONSULTANT_ID</td></tr>
 *    <tr><td>ConsultantLastName</td><td>RK_TRANSACTION_HEADER</td><td>LN_PRS</td></tr>
 *    <tr><td>CustomerId</td><td>RK_TRANSACTION_HEADER</td><td>PAYMENT_TRANSACTION_CUST_ID</td></tr>
 *    <tr><td>MarkdownAmount</td><td>RK_TRANSACTION_HEADER</td><td>REDUCTION_AMOUNT</td></tr>
 *    <tr><td>ProcessDate</td><td>RK_TRANSACTION_HEADER</td><td>TS_TRN_PRC</td></tr>
 *    <tr><td>RetailAmount</td><td>RK_TRANSACTION_HEADER</td><td>NET_AMOUNT</td></tr>
 *    <tr><td>StoreId</td><td>RK_TRANSACTION_HEADER</td><td>ID_STR_RT</td></tr>
 *    <tr><td>SubmitDate</td><td>RK_TRANSACTION_HEADER</td><td>TS_TRN_SBM</td></tr>
 *    <tr><td>TheOperatorId</td><td>RK_TRANSACTION_HEADER</td><td>ID_OPR</td></tr>
 *    <tr><td>TotalAmountDue</td><td>RK_TRANSACTION_HEADER</td><td>PAYMENT_TRANSACTION_TOTAL_AMT</td></tr>
 *    <tr><td>TransactionType</td><td>RK_TRANSACTION_HEADER</td><td>TY_GUI_TRN</td></tr>
 *  </table>
 *
 *  @see TransactionHeader
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.RkTxnHeaderOracleBean
 *
 */
public class TransactionHeaderOracleDAO extends BaseOracleDAO
    implements TransactionHeaderDAO {
  private static PosLineItemOracleDAO posLineItemDAO = new PosLineItemOracleDAO();
  private static String selectSql = RkTxnHeaderOracleBean.selectSql;
  private static String TRANSACTION_ID = RkTxnHeaderOracleBean.COL_AI_TRN;
  private static String TRANSACTION_STORE_ID = RkTxnHeaderOracleBean.COL_ID_STR_RT;
  private static String TRANSACTION_PROCESS_DATE = RkTxnHeaderOracleBean.COL_TS_TRN_PRC;
  private static String COMPOSITE_CONSULTANT_ID = RkTxnHeaderOracleBean.COL_CONSULTANT_ID;
  private static String COMPOSITE_DISCOUNT_TYPES = RkTxnHeaderOracleBean.COL_DISCOUNT_TYPES;
  private static String COMPOSITE_NET_AMOUNT = RkTxnHeaderOracleBean.COL_NET_AMOUNT;
  private static String PAYMENT_TRANSACTION_PAY_TYPES = RkTxnHeaderOracleBean.COL_PAY_TYPES;
  private static String TRANSACTION_THE_OPERATOR_ID = RkTxnHeaderOracleBean.COL_ID_OPR;
  private static String TRANSACTION_TYPES = RkTxnHeaderOracleBean.COL_TY_GUI_TRN;
  private static String COMPOSITE_ITEMS_IDS = RkTxnHeaderOracleBean.COL_ITEMS_IDS;
  private static String COMPOSITE_REGISTER_ID = RkTxnHeaderOracleBean.COL_REGISTER_ID;
  private static String PAYMENT_TRANSACTION_CUST_ID = RkTxnHeaderOracleBean.COL_CUST_ID;
  private static String TRANSACTION_SUBMIT_DATE = RkTxnHeaderOracleBean.COL_TS_TRN_SBM;
  int maxNumberOfRecords = 0;
  
   
  public int getMaxRecords(){
  ConfigMgr cfg = new ConfigMgr("pos.cfg");
  int maxRecords = cfg.getInt("MAX_RECORDS_TO_RETRIEVE");
  if(maxRecords == 0){
		  maxRecords = 100;  
	}	  
   return maxRecords;	  
  }
  
  /**
   * Queries on base of SearchString
   * @param sQueryString String
   * @throws SQLException
   * @return TransactionHeader[]
   */
  public TransactionHeader[] selectBySearchCriteria (TransactionSearchString txnSearchString) throws SQLException {
    List params = new ArrayList();
    maxNumberOfRecords = getMaxRecords();
    String sQueryString = txnSearchString.buildQuery(params);
    sQueryString += "AND ROWNUM <="+maxNumberOfRecords+" ORDER BY " + RkTxnHeaderOracleBean.COL_TS_TRN_SBM + " DESC" ;
    return  fromBeansToObjects(query(new RkTxnHeaderOracleBean(), sQueryString, params));
  }

  /**
   * Select on CustomerId and Payment Type
   * @param sCustomerId String
   * @param sPaymentType String
   * @throws SQLException
   * @return TransactionHeader[]
   */
  public TransactionHeader[] selectByCustomerIdAndPaymentType (String sCustomerId, String sPaymentType) throws SQLException {
	  maxNumberOfRecords = getMaxRecords();
	  if (!sPaymentType.equals("CREDIT_CARD")) {
      String whereSql = "where " + PAYMENT_TRANSACTION_CUST_ID + " = ? and " + PAYMENT_TRANSACTION_PAY_TYPES + " like ? "+ "AND ROWNUM <="+maxNumberOfRecords+ " ORDER BY " + TRANSACTION_SUBMIT_DATE + " DESC";
      List params = new ArrayList();
      params.add(sCustomerId);
      params.add("%*" + sPaymentType + "*%");
      return  fromBeansToObjects(query(new RkTxnHeaderOracleBean(), whereSql, params));
    } 
    else {
      String[] creditPaymentTypes = PaymentMgr.getReportCreditPayments();       //   .getCreditPaymentDesc();
      Arrays.sort(creditPaymentTypes);
      if (creditPaymentTypes == null || creditPaymentTypes.length == 0)
        return  new TransactionHeader[0];
      String whereSql = "where " + PAYMENT_TRANSACTION_CUST_ID + " = ? and (";
      for (int i = 0; i < creditPaymentTypes.length - 1; i++)
        whereSql += (PAYMENT_TRANSACTION_PAY_TYPES + " like ? or ");
      whereSql += (PAYMENT_TRANSACTION_PAY_TYPES + " like ?)") + "AND ROWNUM <="+maxNumberOfRecords+" ORDER BY " + TRANSACTION_SUBMIT_DATE + " DESC ";
      List params = new ArrayList();
      params.add(sCustomerId);
      for (int i = 0; i < creditPaymentTypes.length; i++)
        params.add("%" + creditPaymentTypes[i] + "%");
      return  fromBeansToObjects(query(new RkTxnHeaderOracleBean(), whereSql, params));
    }
  }

  /**
   * Select by CustomerId and Dates
   * @param sCustomerId String
   * @param dtStart StartDate
   * @param dtEnd EndDate
   * @throws SQLException
   * @return TransactionHeader[]
   */
  public TransactionHeader[] selectByCustomerIdAndDates (String sCustomerId, Date dtStart, Date dtEnd) throws SQLException {
	maxNumberOfRecords = getMaxRecords();
	String whereSql = "where " + PAYMENT_TRANSACTION_CUST_ID + " = ? and " + TRANSACTION_SUBMIT_DATE + " >= ? and " + TRANSACTION_SUBMIT_DATE + " < ?" + "AND ROWNUM <="+maxNumberOfRecords+" ORDER BY " + TRANSACTION_SUBMIT_DATE + " DESC";
    List params = new ArrayList();
    params.add(sCustomerId);
    params.add(dtStart);
    params.add(dtEnd);
    return  fromBeansToObjects(query(new RkTxnHeaderOracleBean(), whereSql, params));
  }

  /**
   * Select by CustomerId and SHipping is requested
   * @param sCustomerId String
   * @param sPaymentType String
   * @throws SQLException
   * @return TransactionHeader[]
   */
  public TransactionHeader[] selectByCustomerIdAndShippingRequested (String sCustomerId) throws SQLException {
	maxNumberOfRecords = getMaxRecords();
	String sql = replaceSubstring(RkTxnHeaderOracleBean.selectSql, "AI_TRN", RkShipReqOracleBean.COL_AI_TRN);
    String whereSql = ", " + RkShipReqOracleBean.TABLE_NAME + " where " + TRANSACTION_ID + " = " + RkShipReqOracleBean.COL_AI_TRN + " and " + RkShipReqOracleBean.COL_SEQ_NUM + " = 0 " + " and " + PAYMENT_TRANSACTION_CUST_ID + " = ? " + "AND ROWNUM <="+maxNumberOfRecords+" ORDER BY " + TRANSACTION_SUBMIT_DATE + " DESC";
    List params = new ArrayList();
    params.add(sCustomerId);
    return  fromBeansToObjects(query(new RkTxnHeaderOracleBean(), sql + whereSql, params));
  }

  /**
   *
   * @param storeId
   * @param begin
   * @param end
   * @return
   * @exception SQLException
   */
  public TransactionHeader[] selectByStoreIdAndDates (String storeId, Date begin, Date end) throws SQLException {
	maxNumberOfRecords = getMaxRecords();
	String whereSql = "where " + TRANSACTION_STORE_ID + " = ? and " + TRANSACTION_SUBMIT_DATE + " >= ? and " + TRANSACTION_SUBMIT_DATE + " < ?" + "AND ROWNUM <="+maxNumberOfRecords+ " ORDER BY " + TRANSACTION_SUBMIT_DATE + " DESC";
    List params = new ArrayList();
    params.add(storeId);
    params.add(begin);
    params.add(end);
    return  fromBeansToObjects(query(new RkTxnHeaderOracleBean(), whereSql, params));
  }

  /**
   *
   * @param storeId
   * @param begin
   * @param end
   * @param consultantId
   * @return
   * @exception SQLException
   */
  public TransactionHeader[] selectByStoreIdAndDatesAndConsultantId (String storeId, Date begin, Date end, String consultantId) throws SQLException {
	StringBuffer whereSql = new StringBuffer();
	maxNumberOfRecords = getMaxRecords();
	whereSql.append("where " + TRANSACTION_STORE_ID + " = ? and " + TRANSACTION_SUBMIT_DATE + " >= ? and " + TRANSACTION_SUBMIT_DATE + " < ? "+"AND ROWNUM <="+maxNumberOfRecords); 
    whereSql.append("and " + TRANSACTION_ID + " in "); 
    whereSql.append("(select TR_LTM_RTL_TRN.AI_TRN from TR_LTM_RTL_TRN ");
    whereSql.append("inner join PA_EM on TR_LTM_RTL_TRN.ADD_CONSULTANT_ID = PA_EM.ID_EM AND PA_EM.ARM_EXTERNAL_ID = ?) "); 
    whereSql.append("order by " + TRANSACTION_SUBMIT_DATE + " desc");
    List params = new ArrayList();
    params.add(storeId);
    params.add(begin);
    params.add(end);
    params.add(consultantId);
    return  fromBeansToObjects(query(new RkTxnHeaderOracleBean(), whereSql.toString(), params));
  }

  /**
   *
   * @param storeId
   * @param begin
   * @param end
   * @param discountType
   * @return
   * @exception SQLException
   */
  public TransactionHeader[] selectByStoreIdAndDatesAndDiscountType (String storeId, Date begin, Date end, String discountType) throws SQLException {
	maxNumberOfRecords = getMaxRecords();
	String whereSql = "where " + TRANSACTION_STORE_ID + " = ? and " + TRANSACTION_SUBMIT_DATE + " >= ? and " + TRANSACTION_SUBMIT_DATE + " < ? and lower(" + COMPOSITE_DISCOUNT_TYPES + ") like ?" +"AND ROWNUM <= "+maxNumberOfRecords+ " ORDER BY " + TRANSACTION_SUBMIT_DATE + " DESC";
    List params = new ArrayList();
    params.add(storeId);
    params.add(begin);
    params.add(end);
    params.add(("%" + discountType + "%").toLowerCase());
    return  fromBeansToObjects(query(new RkTxnHeaderOracleBean(), whereSql, params));
  }

  /**
   *
   * @param storeId
   * @param begin
   * @param end
   * @param amount
   * @return
   * @exception SQLException
   */
  public TransactionHeader[] selectByStoreIdAndDatesAndAmount (String storeId, Date begin, Date end, ArmCurrency amount) throws SQLException {
	maxNumberOfRecords = getMaxRecords();
	String whereSql = "where " + TRANSACTION_STORE_ID + " = ? and " + TRANSACTION_SUBMIT_DATE + " >= ? and " + TRANSACTION_SUBMIT_DATE + " < ? and " + COMPOSITE_NET_AMOUNT + " = ?" + "AND ROWNUM <="+maxNumberOfRecords+" ORDER BY " + TRANSACTION_SUBMIT_DATE + " DESC";
    List params = new ArrayList();
    params.add(storeId);
    params.add(begin);
    params.add(end);
    params.add(amount.toDelimitedString());
    return  fromBeansToObjects(query(new RkTxnHeaderOracleBean(), whereSql, params));
  }

  /**
   *
   * @param storeId
   * @param begin
   * @param end
   * @param operatorId
   * @return
   * @exception SQLException
   */
  public TransactionHeader[] selectByStoreIdAndDatesAndOperatorId (String storeId, Date begin, Date end, String operatorId) throws SQLException {
	maxNumberOfRecords = getMaxRecords();
	String whereSql = "where " + TRANSACTION_STORE_ID + " = ? and " + TRANSACTION_SUBMIT_DATE + " >= ? and " + TRANSACTION_SUBMIT_DATE + " < ? and " + TRANSACTION_THE_OPERATOR_ID + " = ?" + "AND ROWNUM <="+maxNumberOfRecords+" ORDER BY " + TRANSACTION_SUBMIT_DATE + " DESC";
    List params = new ArrayList();
    params.add(storeId);
    params.add(begin);
    params.add(end);
    params.add(operatorId);
    return  fromBeansToObjects(query(new RkTxnHeaderOracleBean(), whereSql, params));
  }

  /**
   *
   * @param storeId
   * @param begin
   * @param end
   * @param paymentType
   * @return
   * @exception SQLException
   */
  public TransactionHeader[] selectByStoreIdAndDatesAndPaymentType (String storeId, Date begin, Date end, String paymentType) throws SQLException {
	  maxNumberOfRecords = getMaxRecords();
    String whereSql = "where " + TRANSACTION_STORE_ID + " = ? and " + TRANSACTION_SUBMIT_DATE + " >= ? and " + TRANSACTION_SUBMIT_DATE + " < ? and " + PAYMENT_TRANSACTION_PAY_TYPES + " like ?" +" AND ROWNUM <= "+maxNumberOfRecords+ " ORDER BY " + TRANSACTION_SUBMIT_DATE + " DESC";
    List params = new ArrayList();
    params.add(storeId);
    params.add(begin);
    params.add(end);
    params.add("%*" + paymentType + "*%");
    return  fromBeansToObjects(query(new RkTxnHeaderOracleBean(), whereSql, params));
  }

  /**
   *
   * @param storeId
   * @param begin
   * @param end
   * @param creditPaymentTypes
   * @return
   * @exception SQLException
   */
  public TransactionHeader[] selectByStoreIdAndDatesAndCreditPaymentTypes (String storeId, Date begin, Date end, String[] creditPaymentTypes) throws SQLException {
	  maxNumberOfRecords = getMaxRecords();  
    if (creditPaymentTypes == null || creditPaymentTypes.length == 0)
      return  new TransactionHeader[0];
    String whereSql = "where " + TRANSACTION_STORE_ID + " = ? and " + TRANSACTION_SUBMIT_DATE + " >= ? and " + TRANSACTION_SUBMIT_DATE + " < ? and (";
    for (int i = 0; i < creditPaymentTypes.length - 1; i++)
      whereSql += (PAYMENT_TRANSACTION_PAY_TYPES + " like ? or ");
    whereSql += (PAYMENT_TRANSACTION_PAY_TYPES + " like ?)") + " AND ROWNUM <= "+maxNumberOfRecords+" ORDER BY " + TRANSACTION_SUBMIT_DATE + " DESC";
    List params = new ArrayList();
    params.add(storeId);
    params.add(begin);
    params.add(end);
    for (int i = 0; i < creditPaymentTypes.length; i++)
      params.add("%" + creditPaymentTypes[i] + "%");
    return  fromBeansToObjects(query(new RkTxnHeaderOracleBean(), whereSql, params));
  }

  /**
   *
   * @param storeId
   * @param begin
   * @param end
   * @param transactionType
   * @return
   * @exception SQLException
   */
  public TransactionHeader[] selectByStoreIdAndDatesAndTransactionType (String storeId, Date begin, Date end, String transactionType) throws SQLException {
	maxNumberOfRecords = getMaxRecords();  
    String whereSql = "where " + TRANSACTION_STORE_ID + " = ? and " + TRANSACTION_SUBMIT_DATE + " >= ? and " + TRANSACTION_SUBMIT_DATE + " < ? and " + TRANSACTION_TYPES + " like ?"+ " AND ROWNUM <= "+maxNumberOfRecords + " ORDER BY " + TRANSACTION_SUBMIT_DATE + " DESC";
    List params = new ArrayList();
    params.add(storeId);
    params.add(begin);
    params.add(end);
    params.add("%" + transactionType + "%");
    return  fromBeansToObjects(query(new RkTxnHeaderOracleBean(), whereSql, params));
  }

  /**
   *
   * @param customerId
   * @return
   * @exception SQLException
   */
  public TransactionHeader[] selectByCustomerId (String customerId) throws SQLException {
	maxNumberOfRecords = getMaxRecords();  
    String whereSql = where(RkTxnHeaderOracleBean.COL_CUST_ID) + " AND ROWNUM <= "+maxNumberOfRecords+ " ORDER BY " + TRANSACTION_SUBMIT_DATE + " DESC";
    return  fromBeansToObjects(query(new RkTxnHeaderOracleBean(), whereSql, customerId));
  }

  /**
   *
   * @param storeId
   * @param begin
   * @param end
   * @return
   * @exception SQLException
   */
  public TransactionHeader[] selectByStoreIdAndDatesAndShippingRequested (String storeId, Date begin, Date end) throws SQLException {
	maxNumberOfRecords = getMaxRecords();  
    String sql = replaceSubstring(RkTxnHeaderOracleBean.selectSql, "AI_TRN", RkShipReqOracleBean.COL_AI_TRN);
    String whereSql = ", " + RkShipReqOracleBean.TABLE_NAME + " where " + TRANSACTION_ID + " = " + RkShipReqOracleBean.COL_AI_TRN + " and " + RkShipReqOracleBean.COL_SEQ_NUM + " = 0 " + " and " + TRANSACTION_STORE_ID + " = ? " + " and " + TRANSACTION_SUBMIT_DATE + " >= ? " + " and " + TRANSACTION_SUBMIT_DATE
        + " <= ?" + " AND ROWNUM <= "+maxNumberOfRecords+ " ORDER BY " + TRANSACTION_SUBMIT_DATE + " DESC";
    List params = new ArrayList();
    params.add(storeId);
    params.add(begin);
    params.add(end);
    return  fromBeansToObjects(query(new RkTxnHeaderOracleBean(), sql + whereSql, params));
  }

  /**
   *
   * @param constraints
   * @return
   * @exception SQLException
   */
  public TransactionHeader[] selectByAdHocQueryConstraints (AdHocQueryConstraints constraints) throws SQLException {
    List whereSqls = new ArrayList();
    List params = new ArrayList();
    //test for date
    if (constraints.getTxnBeginDate() != null && constraints.getTxnEndDate() != null) {
      whereSqls.add(TRANSACTION_SUBMIT_DATE + " >= ? and " + TRANSACTION_SUBMIT_DATE + " < ?");
      //remove any manipulation of dates on server,
      //params.add(DateUtil.getBeginingOfDay(constraints.getTxnBeginDate()));
      //params.add(DateUtil.getEndOfDay(constraints.getTxnEndDate()));
      params.add(constraints.getTxnBeginDate());
      params.add(constraints.getTxnEndDate());
    }
    //test for payment type
    if (constraints.getPaymentTypes() != null && constraints.getPaymentTypes().length > 0) {
      String sql = PAYMENT_TRANSACTION_PAY_TYPES + " like ?";
      whereSqls.add(buildWhereSqlsWithOr(sql, constraints.getPaymentTypes().length));
      for (int i = 0; i < constraints.getPaymentTypes().length; i++)
        params.add("%" + constraints.getPaymentTypes()[i] + "%");
    }
    //test for Store Ids
    if (constraints.getStores() != null && constraints.getStores().length > 0) {
      String sql = TRANSACTION_STORE_ID + " = ?";
      whereSqls.add(buildWhereSqlsWithOr(sql, constraints.getStores().length));
      for (int i = 0; i < constraints.getStores().length; i++)
        params.add(constraints.getStores()[i]);
    }
    //test for operator ids
    if (constraints.getOperators() != null && constraints.getOperators().length > 0) {
      String sql = TRANSACTION_THE_OPERATOR_ID + " = ?";
      whereSqls.add(buildWhereSqlsWithOr(sql, constraints.getOperators().length));
      for (int i = 0; i < constraints.getOperators().length; i++)
        params.add(constraints.getOperators()[i]);
    }
    //test for consultant ids
    if (constraints.getConsultants() != null && constraints.getConsultants().length > 0) {
      String sql = COMPOSITE_CONSULTANT_ID + " = ?";
      whereSqls.add(buildWhereSqlsWithOr(sql, constraints.getConsultants().length));
      for (int i = 0; i < constraints.getConsultants().length; i++)
        params.add(constraints.getConsultants()[i]);
    }
    //test for transaciton type
    if (constraints.getTxnTypes() != null && constraints.getTxnTypes().length > 0) {
      String sql = TRANSACTION_TYPES + " like ?";
      whereSqls.add(buildWhereSqlsWithOr(sql, constraints.getTxnTypes().length));
      for (int i = 0; i < constraints.getTxnTypes().length; i++)
        params.add("%" + constraints.getTxnTypes()[i] + "%");
    }
    //test for Discounts type, it must be a CompositePOSTransaction
    if (constraints.getDiscounts() != null && constraints.getDiscounts().length > 0) {
      String sql = "lower(" + COMPOSITE_DISCOUNT_TYPES + ") like ?";
      whereSqls.add(buildWhereSqlsWithOr(sql, constraints.getDiscounts().length));
      for (int i = 0; i < constraints.getDiscounts().length; i++)
        params.add(("%" + constraints.getDiscounts()[i] + "%").toLowerCase());
    }
    //test for item ids, it must be a CompositePOSTransaction
    if (constraints.getItems() != null && constraints.getItems().length > 0) {
      String sql = "lower(" + COMPOSITE_ITEMS_IDS + ") like ?";
      whereSqls.add(buildWhereSqlsWithOr(sql, constraints.getItems().length));
      for (int i = 0; i < constraints.getItems().length; i++)
        params.add(("%" + constraints.getItems()[i] + "%").toLowerCase());
    }
    //test for registerId, it is found in transaction number: 1115*100*1139
    if (constraints.getRegisters() != null && constraints.getRegisters().length > 0) {
      String sql = COMPOSITE_REGISTER_ID + " = ?";
      whereSqls.add(buildWhereSqlsWithOr(sql, constraints.getRegisters().length));
      for (int i = 0; i < constraints.getRegisters().length; i++)
        params.add(constraints.getRegisters()[i]);
    }
    //test for customerId, and the transaction has to be CompositePOSTransaction
    if (constraints.getCustomers() != null && constraints.getCustomers().length > 0) {
      String sql = PAYMENT_TRANSACTION_CUST_ID + " = ?";
      whereSqls.add(buildWhereSqlsWithOr(sql, constraints.getCustomers().length));
      for (int i = 0; i < constraints.getCustomers().length; i++)
        params.add(constraints.getCustomers()[i]);
    }
    if (whereSqls == null || whereSqls.size() == 0)
      throw  new SQLException("AdHocQueryConstraints does not contains a valid condition. No search was done.");
    String whereSqlString = "where ";
    if (whereSqls.size() == 1) {
      whereSqlString += (String)whereSqls.get(0);
    } 
    else {
      for (int i = 0; i < whereSqls.size() - 1; i++)
        whereSqlString += ((String)whereSqls.get(i) + " and ");
      whereSqlString += (String)whereSqls.get(whereSqls.size() - 1);
    }
    TransactionHeader[] transactionHeaders = fromBeansToObjects(query(new RkTxnHeaderOracleBean(), whereSqlString, params));
    if (transactionHeaders == null || transactionHeaders.length == 0)
      return  transactionHeaders;
    //test for amount
    if (constraints.getAtLeastAmount() != null && constraints.getNotMoreThanAmount() != null) {
      List list = new ArrayList();
      for (int i = 0; i < transactionHeaders.length; i++) {
        try {
          if (transactionHeaders[i].getTotalAmountDue().greaterThanOrEqualTo(constraints.getAtLeastAmount()) && transactionHeaders[i].getTotalAmountDue().lessThanOrEqualTo(constraints.getNotMoreThanAmount()))
            list.add(transactionHeaders[i]);
        } catch (CurrencyException ce) {
        //currency type does not match, it is ok. Keep going.
        }
      }
      transactionHeaders = (CMSTransactionHeader[])list.toArray(new CMSTransactionHeader[0]);
    }
    //test for shipping or without
    if (constraints.getSearchForHavingShippingRequests() != null) {
      List list = new ArrayList();
      for (int i = 0; i < transactionHeaders.length; i++) {
        Boolean haveSippingRequests = posLineItemDAO.doesTransactionHaveSippingRequests(transactionHeaders[i].getId());
        if (haveSippingRequests != null && haveSippingRequests.equals(constraints.getSearchForHavingShippingRequests()))
          list.add(transactionHeaders[i]);
      }
      transactionHeaders = (CMSTransactionHeader[])list.toArray(new CMSTransactionHeader[0]);
    }
    return  transactionHeaders;
  }

  /**
   *
   * @param whereSql
   * @param number
   * @return
   */
  private String buildWhereSqlsWithOr (String whereSql, int number) {
    if (number == 0)
      return  null;
    if (number == 1)
      return  whereSql;
    String sql = "(";
    for (int i = 0; i < number - 1; i++)
      sql += (whereSql + " or ");
    sql += (whereSql + ")");
    return  sql;
  }

  /**
   *
   * @return
   */
  protected BaseOracleBean getDatabeanInstance () {
    return  new RkTxnHeaderOracleBean();
  }

  /**
   *
   * @param beans
   * @return
   * @exception SQLException
   */
  private TransactionHeader[] fromBeansToObjects (BaseOracleBean[] beans) throws SQLException {
    CMSTransactionHeader[] array = new CMSTransactionHeader[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = (CMSTransactionHeader)fromBeanToObject(beans[i]);
    return  array;
  }

  /**
   *
   * @param baseBean
   * @return
   * @exception SQLException
   */
  private TransactionHeader fromBeanToObject (BaseOracleBean baseBean) throws SQLException {
    RkTxnHeaderOracleBean bean = (RkTxnHeaderOracleBean)baseBean;
    //TransactionHeader object = new CMSTransactionHeader(bean.getAiTrn());
    CMSTransactionHeader object = new CMSTransactionHeader(bean.getAiTrn());
    object.doSetStoreId(bean.getIdStrRt());
    object.doSetRegisterId(bean.getRegisterId());
    //PCR1326 Transaction History fix for Armani Japan
    object.doSetStoreName(bean.getStoreDesc());
    object.doSetFiscalReceiptNum(bean.getFiscalReceiptNum());
    bean.getTyStrRt();
    bean.getCurrencyCd();
    object.doSetTheOperatorId(bean.getIdOpr());
    object.doSetProcessDate(bean.getTsTrnPrc());
    object.doSetSubmitDate(bean.getTsTrnSbm());
    object.doSetTransactionType(bean.getTyGuiTrn());
    object.doSetPayTxnType(bean.getPayTypes());
    object.doSetIsShipping(bean.getIsShipping());
    if (bean.getCustId() != null)
      object.doSetCustomerId(bean.getCustId());
    if (bean.getConsultantId() != null)
      object.doSetConsultantId(bean.getConsultantId());
    if (bean.getFnPrs() != null)
      object.doSetConsultantFirstName(bean.getFnPrs());
    if (bean.getLnPrs() != null)
      object.doSetConsultantLastName(bean.getLnPrs());
    if (bean.getReductionAmount() != null)
      object.doSetMarkdownAmount(bean.getReductionAmount());
    if (bean.getNetAmount() != null)
      object.doSetRetailAmount(bean.getNetAmount());
    if (bean.getTotalAmt() != null)
      object.doSetTotalAmountDue(bean.getTotalAmt());
    object.doSetExpirationDate(bean.getExpDt());
    object.doSetRefId(bean.getRefId());
    return  object;
  }

  /**
   * put your documentation comment here
   * @param string
   * @param oldSubstring
   * @param newSubstring
   * @return 
   */
  private static String replaceSubstring (String string, String oldSubstring, String newSubstring) {
    int index = string.indexOf(oldSubstring);
    if (index == -1)
      return  string;
    String left = string.substring(0, index);
    String right = string.substring(index + oldSubstring.length());
    return  (left + newSubstring + right);
  }

  /**
   * put your documentation comment here
   * @param s
   */
  public static void main (String[] s) {
    String newString = replaceSubstring(selectSql, "AI_TRN", RkTxnHeaderOracleBean.COL_AI_TRN);
    System.out.println("" + selectSql);
    System.out.println("" + newString);
  }
}



