/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//
/*
 History:
 +------+------------+-----------+-----------+-----------------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                               |
 +------+------------+-----------+-----------+-----------------------------------------------------------+
 | 28   | 11-08-2005 | Manpreet  | N/A       | Created selectByBarcode(String sBarcode)                  |
 +------+------------+-----------+-----------+-----------------------------------------------------------+
 | 27   | 09-06-2005 | Manpreet  | N/A       | getUpdateSQL(Customer obj) modified to update AddressType |
 +------+------------+-----------+-----------+-----------------------------------------------------------+
 | 26   | 08-03-2005 | Jin       | 815,816   | Join PA_CT table during customer search by name to        |
 |      |            |           |           |  strengthen data integrity and thus resilient to bad data;|
 |      |            |           |           | Throws TooManySearchResultsException when 100+ custs found|
 +------+------------+-----------+-----------+-----------------------------------------------------------+
 | 25   | 07-22-2005 | Vikram    | 286       | Changes to support shipping to foreign countries.         |
 +------+------------+-----------+-----------+-----------------------------------------------------------+
 | 24   | 07-15-2005 | Vikram    |  328      |Corrected persistance of Age-Group in staging table        |
 +------+------------+-----------+-----------+-----------------------------------------------------------+
 | 23   | 07-13-2005 | Vikram    |  468      |Changed the order for deleting address & telephone         |
 +------+------------+-----------+-----------+-----------------------------------------------------------+
 | 22   | 06-22-2005 | Vikram    |  262      |LO_ADS_NSTD.MU_NM (municipality) now used as Address Format|
 +------+------------+-----------+-----------+-----------------------------------------------------------+
 | 21   | 06-13-2005 | Vikram    |  167      | LASTNAME+(POST CODE or CITY) combination is not working   |
 |      |            |           |           | properly                                                  |
 +------+------------+-----------+-----------+-----------------------------------------------------------+
 | 20   | 06-02-2005 |    Megha  |  96       | When entering customers with country other than USA       |
 |      |            |           |           |  i.e Canada or Japan Cust ID is not assigned              |
 |      |            |           |           |  and resultes in broken Txn.                              |
 |      |            |           |           |  Modified : getInsertSql()                                |
 +------+------------+-----------+-----------+-------------- --------------------------------------------+
 | 19   | 06-03-2005 | Vikram    | 95        | Customer last name with apostaphe throws register off-line|
 +------+------------+-----------+-----------+-----------------------------------------------------------+
 | 18   | 05-25-2005 | Megha     | N/A       | Methods for customer messages                             |
 +------+------------+-----------+-----------+-----------------------------------------------------------+
 | 17   | 05-19-2005 | Vikram    | N/A       | Removed Customer affiliation table query for loyalty      |
 +------+------------+-----------+-----------+-----------------------------------------------------------+
 | 16   | 04-27-2005 | Manpreet  | N/A       | Implemented selectByRewardCard                            |
 +------+------------+-----------+-----------+-----------------------------------------------------------+
 | 15   | 04-27-2005 | Rajesh    | N/A       | changes in selectByPhone method                           |
 +------+------------+-----------+-----------+-----------------------------------------------------------+
 | 14   | 04-16-2005 | Rajesh    | N/A       | changes in selectById method                              |
 +------+------------+-----------+-----------+-----------------------------------------------------------+
 | 13   | 04-15-2005 | Rajesh    | N/A       | changes to email and other minorbug fixes                 |
 +------+------------+-----------+-----------+-----------------------------------------------------------+
 | 12   | 04-12-2005 | Rajesh    | N/A       | Uncomment previously commented code for customer outbound |
 +------+------------+-----------+-----------+-----------------------------------------------------------+
 */


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

//import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import  java.sql.SQLException;
import  java.text.ParseException;
import  java.util.ArrayList;
import  java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import  java.util.List;
import  java.util.Date;
import  java.util.Calendar;
import java.util.Map;
import  java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import  java.text.SimpleDateFormat;

import  com.chelseasystems.cr.customer.Customer;
import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cr.telephone.Telephone;
import  com.chelseasystems.cr.telephone.TelephoneType;
import  com.chelseasystems.cs.address.Address;
import com.chelseasystems.cs.address.qas.QASHelper;
import com.chelseasystems.cs.customer.CMSCustomerAlertRule;
import com.chelseasystems.cs.customer.CMSCustomerDate;
import com.chelseasystems.cs.customer.CreditHistory;
import  com.chelseasystems.cs.customer.CustomerSearchString;
import  com.chelseasystems.cs.customer.CMSCustomer;
import  com.chelseasystems.cs.customer.CustomerComment;
import  com.chelseasystems.cs.customer.CustomerSalesAssociate;
import  com.chelseasystems.cs.customer.CustomerSaleSummary;
import  com.chelseasystems.cs.dataaccess.CustomerDAO;
import  com.chelseasystems.cs.dataaccess.ArmCustSaleSummaryDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmCustCreditHistOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmEmpAlertRuleOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.CoCtafOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.LoAdsNstdOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.LoAdsOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.LoAdsPrtyOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.LoEmlAdsOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.PaCtOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.PaKyCtOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.PaPrsOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.PaPrtyOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.PaRoPrtyOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmAdsPhOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmCtCommentsOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmCtAssocOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmCtDtlOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmStgCustOutOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.DoCfGfOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmCustomerMessagesOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmCustDepositHistOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.VArmCustRuleDataOracleBean;
import  com.chelseasystems.cs.customer.CMSCustomerMessage;
import  com.chelseasystems.cs.customer.DepositHistory;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmCustDepositHistOracleBean;
import  com.chelseasystems.cs.collection.CMSMiscCollection;
import  com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import  com.chelseasystems.cr.pos.PaymentTransaction;
import  com.chelseasystems.cs.paidout.CMSMiscPaidOut;
import  com.chelseasystems.cr.collection.CollectionTransaction;
import  com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import  com.chelseasystems.cs.util.CustomerUtil;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmCtCrdbCrdDtlOracleBean;
//import  com.armani.crm.crmCustomer;
//import  com.armani.crm.exception.crmCustomerException;
import  com.chelseasystems.cs.util.EncryptionUtils;
import  com.chelseasystems.cs.util.CaseSensitiveString;
import  com.chelseasystems.cs.customer.CustomerCreditCard;
import  com.chelseasystems.cs.customer.TooManySearchResultsException;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.customer.CMSVIPMembershipDetail;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmCustDiscountsOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmVipDiscountDetailOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmStgCustCrmOracleBean;


/**
 *
 *  Customer Data Access Object.<br>
 *  This object encapsulates all database access for Customer.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *    <tr><td>Address</td><td>LO_ADS_NSTD</td><td>A1_ADS</td></tr>
 *    <tr><td>Apartment</td><td>LO_ADS_NSTD</td><td>A2_ADS</td></tr>
 *    <tr><td>BirthDate</td><td>PA_PRS</td><td>DC_PRS_BRT</td></tr>
 *    <tr><td>City</td><td>LO_ADS_NSTD</td><td>NM_UN</td></tr>
 *    <tr><td>Comment</td><td>PA_CT</td><td>DE_COMMENTS</td></tr>
 *    <tr><td>Country</td><td>LO_ADS_NSTD</td><td>CO_NM</td></tr>
 *    <tr><td>FirstName</td><td>PA_PRS</td><td>FN_PRS</td></tr>
 *    <tr><td>LastName</td><td>PA_PRS</td><td>LN_PRS</td></tr>
 *    <tr><td>PreferredISOCountry</td><td>PA_PRTY</td><td>ED_CO</td></tr>
 *    <tr><td>PreferredISOLanguage</td><td>PA_PRTY</td><td>ED_LA</td></tr>
 *    <tr><td>ReceiveMail</td><td>PA_CT</td><td>FL_RC_ML</td></tr>
 *    <tr><td>State</td><td>LO_ADS_NSTD</td><td>TE_NM</td></tr>
 *    <tr><td>ZipCode</td><td>LO_ADS_NSTD</td><td>PC_NM</td></tr>
 *  </table>
 *
 *  @see com.chelseasystems.cr.customer.Customer
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.((LoEmlAdsOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.(LoAdsPrtyOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.LoAdsNstdOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.LoAdsOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.LoAdsPrtyOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.LoEmlAdsOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.PaCtOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.PaCtafOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.PaGpCtOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.PaKyCtOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.PaPrsOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.PaPrtyOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.PaRoPrtyOracleBean
 *
 */
public class CustomerOracleDAO extends BaseOracleDAO
    implements CustomerDAO {

  public static String TOT_RECORD_TYPE = "TOT";	
  public static String PRO_RECORD_TYPE = "PRO";
  public static String DSC_LEVEL = "DSC";
  public static String ID_BRAND = "BRAND";
  ConfigMgr cfg = new ConfigMgr("customer.cfg");
  int maxRecords = cfg.getInt("MAX_RECORDS_TO_RETRIEVE");
  String throwException = cfg.getString("THROW_EXCEPTION");
  String employeeSearch = cfg.getString("EMPLOYEE_SEARCH");
  //SAP CRM Changes
  ConfigMgr addressConfig = new ConfigMgr("addressverify.cfg");
  String useQas = addressConfig.getString("USE_QAS");
  
  /**
   * put your documentation comment here
   * @param object
   * @exception SQLException
   */
  public void insert (Customer object) throws SQLException {
    this.execute(this.getInsertSQL(object));
  }

  /**
   * For inserting custDepositHistory
   * @param <any> depositHistory
   */
  public ParametricStatement getInsertDepositHistorySQL (DepositHistory depositHistory, PaymentTransaction object) throws SQLException {
    ArmCustDepositHistOracleBean bean = fromObjectToBean(depositHistory, object);
    return  new ParametricStatement(bean.getInsertSql(), bean.toList());
  }

  public ParametricStatement getUpdateDepositHistorySQLForDelete(DepositHistory depositHistory){
	ArmCustDepositHistOracleBean bean = new ArmCustDepositHistOracleBean();
	bean.setDcTransaction(depositHistory.getTransactionDate());
	bean.setIdStrRt(depositHistory.getStoreID());
	bean.setAiTrn(depositHistory.getTransactionId());
	bean.setTyTrn(depositHistory.getTransactionType());
	bean.setAssoc(depositHistory.getassoc());
	bean.setAmount(depositHistory.getamount());
	bean.setCustId(depositHistory.getCustomer().getId());
	bean.setFlDeleted(true);
	List params = new ArrayList();
  	params = bean.toList();
  	params.add(depositHistory.getTransactionId());
  	params.add(depositHistory.getCustomer().getId());
  	return new ParametricStatement(bean.getUpdateSql() + BaseOracleDAO.where(ArmCustDepositHistOracleBean.COL_AI_TRN, ArmCustDepositHistOracleBean.COL_CUST_ID), params);
  }
  
  /**
   * put your documentation comment here
   * @param depositHistory
   * @param object
   * @return
   */
  ArmCustDepositHistOracleBean fromObjectToBean (DepositHistory depositHistory, PaymentTransaction object) {
    ArmCustDepositHistOracleBean bean = new ArmCustDepositHistOracleBean();
    bean.setDcTransaction(depositHistory.getTransactionDate());
    bean.setIdStrRt(depositHistory.getStoreID());
    bean.setAiTrn(depositHistory.getTransactionId());
    bean.setTyTrn(depositHistory.getTransactionType());
    bean.setAssoc(depositHistory.getassoc());
    bean.setAmount(depositHistory.getamount());
    CMSCustomer cmsCust = new CMSCustomer();
    if (object instanceof CMSCompositePOSTransaction) {
      cmsCust = (CMSCustomer)((CMSCompositePOSTransaction)object).getCustomer();
    }
    else if (object instanceof CMSMiscCollection) {
      cmsCust = (CMSCustomer)((CMSMiscCollection)object).getCustomer();
    }
    else if (object instanceof CMSMiscPaidOut) {
      cmsCust = (CMSCustomer)((CMSMiscPaidOut)object).getCustomer();
    }
    if (depositHistory.getCustomer() != null && depositHistory.getCustomer().getId() != null && depositHistory.getCustomer().getId().trim().length() > 0) {
      bean.setCustId(depositHistory.getCustomer().getId());
    }
    else
      bean.setCustId(cmsCust.getId());
    return  bean;
  }

  /**
   *
   * @param loyalty Loyalty
   * @return ParametricStatement
   * @exception SQLException
   */
  public ParametricStatement getUpdateDepositSQL (CMSCustomer cmsCust, ArmCurrency currAmount) throws SQLException {
    String sql = "update " + PaCtOracleBean.TABLE_NAME + " set " + PaCtOracleBean.COL_CUST_BALANCE + " = ? " + BaseOracleDAO.where(PaCtOracleBean.COL_ID_CT);
    ArrayList list = new ArrayList();
    try {
      if (cmsCust.getCustomerBalance() == null || cmsCust.getCustomerBalance().doubleValue() == 0.0) {
        list.add(currAmount.stringValue());
      }
      else {
        list.add(cmsCust.getCustomerBalance().add(currAmount).stringValue());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    ArmCurrency c = cmsCust.getCustomerBalance();
    try {
      cmsCust.setCustomerBalance(c.add(currAmount));
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (cmsCust.getId() != null) {
      list.add(cmsCust.getId());
    }
    return  new ParametricStatement(sql, list);
  }

  /**
   * For Inserting
   * @param custMsg CMSCustomerMessage
   * @return ParametericStatement[]
   * @throws SQLException
   */
  public ParametricStatement[] getInsertSQLForMessages (CMSCustomerMessage custMsg, Customer cust) throws SQLException {
    CMSCustomerMessage cmsCustMsg = (CMSCustomerMessage)custMsg;
    custMsg.setCustomerId(cust.getId());
    custMsg.setCustomerType(((CMSCustomer)cust).getCustomerType());
    List statements = new ArrayList();
    ArmCustomerMessagesOracleBean armCustMsgBean = this.toArmCustomerMessagesOracleBean(cmsCustMsg);
    statements.add(new ParametricStatement(armCustMsgBean.getInsertSql(), armCustMsgBean.toList()));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * getUpdateSQLForMsg
   * @param cmsCustMsg CMSCustomerMessage
   * @return ParametricStatement
   * @throws SQLException
   */
  public ParametricStatement[] getUpdateSQLForCustomerMessage (CMSCustomerMessage cmsCustMsg) throws SQLException {
    List statements = new ArrayList();
    List params = null;
    try {
      params = this.toArmCustomerMessagesOracleBean(cmsCustMsg).toList();
      params.add(cmsCustMsg.getCustomerId());
      params.add(cmsCustMsg.getMessage());
      statements.add(new ParametricStatement(ArmCustomerMessagesOracleBean.updateSql + BaseOracleDAO.where(ArmCustomerMessagesOracleBean.COL_ID_CT, ArmCustomerMessagesOracleBean.COL_MESSAGE), params));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * From object to Bean (CMSCustomerMessage)
   * @param cmsCustMsg CMSCustomerMessage
   * @return ArmCustomerMessagesOracleBean
   */
  public ArmCustomerMessagesOracleBean toArmCustomerMessagesOracleBean (CMSCustomerMessage cmsCustMsg) {
    ArmCustomerMessagesOracleBean bean = new ArmCustomerMessagesOracleBean();
    bean.setIdCt(cmsCustMsg.getCustomerId());
    bean.setTyCt(cmsCustMsg.getCustomerType());
    bean.setMessage(cmsCustMsg.getMessage());
    bean.setTyMsg(cmsCustMsg.getMessageType());
    bean.setResponse(cmsCustMsg.getResponse());
    bean.setStatus("P");
    return  bean;
  }

  /**
   * put your documentation comment here
   * @param cust
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (Customer cust) throws SQLException {
    CMSCustomer customer = (CMSCustomer)cust;
    List statements = new ArrayList();
    try {
    	
    if (customer.getId().length() > 0) {}
    else {
//    // set Next Id for this party/customer
//    if (cfg.getString("DERIVE_CUST_ID_FROM_IMPL") == null
//        || cfg.getString("DERIVE_CUST_ID_FROM_IMPL").equals("")
//        || cfg.getString("DERIVE_CUST_ID_FROM_IMPL").equalsIgnoreCase("FALSE")) {
//      customer.doSetId(this.getNextCustomerId());
//    }     
	    String customerID = this.getNextCustomerId();
	    
	    // vishal : for customer_id prefix in europe for FRANCHISING store
		//vishal yevale : fixed null pointer exception issue 30 sept 2016
	    if(customer.getFranchising_Store()!=null &&customer.getFranchising_Store()){
	    	customerID = "F"+customerID;
	    }
	    else{
	    	if(cfg.getString("POS_CUSTOMER_ID_PREFIX")!=null){
	    	customerID = cfg.getString("POS_CUSTOMER_ID_PREFIX")+ customerID;
	    }
	    }
	    // vishal : for customer_id prefix in europe for FRANCHISING store

	    customer.doSetId(customerID);
    }
    /**
     * -------------- BEGIN COMMENTED FOR SAP-CRM -------
     * 
     * 
     *     
      else {
      // Call Europe interface for getting the customer id
      try {
        String codice;
        //crmCustomer ccliente = new crmCustomer();
        if (customer.isDefaultCustomer()) {
        	Address address = (Address)customer.getAddresses().get(0);
            //codice = ccliente.getDefaultCode(customer.getGender(), customer.getAge(), address.getCity(), address.getCountry());
        } else {
        	//codice = ccliente.getFirstAvailableCode();
        }
        //System.out.println("AS400 calling for next Customer ID = " + codice);
        //customer.doSetId(codice); //Sergio
      } catch (crmCustomerException e) {
        // TODO Blocco catch generato automaticamente
        customer.doSetId("C" + this.getNextCustomerId());
        e.printStackTrace();
      	}
      }
    *-------------- END COMMENTED FOR SAP-CRM -------
    */
    
    PaPrtyOracleBean paPrtyBean = this.toPaPrtyBean(customer);
    PaPrsOracleBean paPrsBean = this.toPaPrsBean(customer);
    PaRoPrtyOracleBean paRoPrtyBean = this.toPaRoPrtyBean(customer);
    PaCtOracleBean paCtBean = this.toPaCtBean(customer);
    ArmCtDtlOracleBean armCtDtlBean = this.toArmCtDtlBean(customer);
    statements.add(new ParametricStatement(paPrtyBean.getInsertSql(), paPrtyBean.toList()));
    statements.add(new ParametricStatement(paPrsBean.getInsertSql(), paPrsBean.toList()));
    statements.add(new ParametricStatement(paRoPrtyBean.getInsertSql(), paRoPrtyBean.toList()));
    statements.add(new ParametricStatement(paCtBean.getInsertSql(), paCtBean.toList()));
    statements.add(new ParametricStatement(armCtDtlBean.getInsertSql(), armCtDtlBean.toList()));
    // Email
    LoEmlAdsOracleBean loEmlAdsBean = this.toLoEmlAdsBean(customer, 1);
    if (loEmlAdsBean != null)
      statements.add(new ParametricStatement(loEmlAdsBean.getInsertSql(), loEmlAdsBean.toList()));
    loEmlAdsBean = this.toLoEmlAdsBean(customer, 2);
    if (loEmlAdsBean != null)
      statements.add(new ParametricStatement(loEmlAdsBean.getInsertSql(), loEmlAdsBean.toList()));
    // *** Customer stg outbound sql
    statements.addAll(getInsertSQLForOutboundB(customer));
    statements.addAll(getInsertSQLForOutboundD(customer));
    // Multiple sales associates
    List salesAssocList = customer.getSalesAssociates();
    CustomerSalesAssociate salesAssociate = null;
    for (int i = 0; i < salesAssocList.size(); i++) {
      salesAssociate = (CustomerSalesAssociate)salesAssocList.get(i);
      salesAssociate.setCustSalesAssocId(this.getNextChelseaId());
      ArmCtAssocOracleBean armCtAssocBean = this.toArmCtAssocBean(salesAssociate, customer.getId());
      statements.add(new ParametricStatement(armCtAssocBean.getInsertSql(), armCtAssocBean.toList()));
      // *** Customer stg outbound sql
      statements.addAll(getInsertSQLForOutboundCA(salesAssociate, customer));
    }
    // Multiple comments
    List commentList = customer.getCustomerComments();
    CustomerComment custCom = null;
    for (int i = 0; i < commentList.size(); i++) {
      custCom = (CustomerComment)commentList.get(i);
      custCom.setCustomerCommentId(this.getNextChelseaId());
      ArmCtCommentsOracleBean armCtCommentsBean = this.toArmCtCommentsBean(custCom, customer.getId());
      statements.add(new ParametricStatement(armCtCommentsBean.getInsertSql(), armCtCommentsBean.toList()));
      // *** Customer stg outbound sql
      statements.addAll(getInsertSQLForOutboundCC(custCom, customer));
    }
    // Multiple Credit Cards
    List creditCardList = customer.getCreditCard();
    CustomerCreditCard creditCard = null;
    for (int i = 0; i < creditCardList.size(); i++) {
      creditCard = (CustomerCreditCard)creditCardList.get(i);
      // Encrypt the Credit Card Number
      EncryptionUtils encryptionUtils = new EncryptionUtils();
      //Commenting the code because of testing issues
      String ccNum = creditCard.getCreditCardNumber();
      //System.out.println("ccNum>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ccNum);
      String encryptCCNum = encryptionUtils.encrypt(ccNum);
      //System.out.println("EncryptedCreditCardNumber   :"+encryptCCNum);
      //commenting for PCI compliance
    //  creditCard.setCreditCardNumber(encryptCCNum);
      //String encryptedString = creditCard.getEncryptedCCData();
      //creditCard.setEncryptedCCData(encryptedString);
      //end commenting
      ArmCtCrdbCrdDtlOracleBean armCtCrdbCrdDtlBean = this.toArmCtCrdbCrdDtlBean(creditCard, customer.getId());
     // System.out.println("token number in DB>>>>>>>"+armCtCrdbCrdDtlBean.getCardToken());
      if(creditCard.getCardToken()!=null){
      armCtCrdbCrdDtlBean.setCrdToken(creditCard.getCardToken());
      }
       List list = toArmCtCrdbCrdDtlBeanList(armCtCrdbCrdDtlBean.toList());
      statements.add(new ParametricStatement(armCtCrdbCrdDtlBean.getInsertSql(), list));
      // *** Customer stg outbound sql
      //statements.addAll(getInsertSQLForOutboundCCard(creditCard,customer.getId()));
    }
    // Multiple addresses
    List addressList = customer.getAddresses();
    Address addr = null;
    Telephone tel = null;
    for (int i = 0; i < addressList.size(); i++) {
      addr = (Address)addressList.get(i);
      LoAdsOracleBean loAdsBean = this.toLoAdsBean(customer);
      LoAdsPrtyOracleBean loAdsPrtyBean = this.toLoAdsPrtyBean(customer, loAdsBean.getIdAds(), addr);
      LoAdsNstdOracleBean loAdsNstdBean = this.toLoAdsNstdBean(loAdsBean.getIdAds(), addr);
      statements.add(new ParametricStatement(loAdsBean.getInsertSql(), loAdsBean.toList()));
      statements.add(new ParametricStatement(loAdsNstdBean.getInsertSql(), loAdsNstdBean.toList()));
      statements.add(new ParametricStatement(loAdsPrtyBean.getInsertSql(), loAdsPrtyBean.toList()));
      // Telephone details
      /*
       Bug #96
       When entering customers with country other than USA  i.e Canada or
       Japan  cust ID   not assigned  ,results in broken txn
       */
      ArmAdsPhOracleBean armAdsPhBean = null;
      tel = addr.getPrimaryPhone();
      // MP: Modified the code to check if the phone number specified is null or an
      // empty string. If yes, do not insert the info in the database.
      if (tel != null) {
        if (!(tel.getTelephoneNumber() == null || tel.getTelephoneNumber().trim().length() <= 0)) {
          armAdsPhBean = this.toArmAdsPhBean(tel, loAdsBean.getIdAds(), 1);
          statements.add(new ParametricStatement(armAdsPhBean.getInsertSql(), armAdsPhBean.toList()));
        }
      }
      tel = addr.getSecondaryPhone();
      if (tel != null) {
        if (!(tel.getTelephoneNumber() == null || tel.getTelephoneNumber().trim().length() <= 0)) {
          armAdsPhBean = this.toArmAdsPhBean(tel, loAdsBean.getIdAds(), 2);
          statements.add(new ParametricStatement(armAdsPhBean.getInsertSql(), armAdsPhBean.toList()));
        }
      }
      tel = addr.getTernaryPhone();
      if (tel != null) {
        if (!(tel.getTelephoneNumber() == null || tel.getTelephoneNumber().trim().length() <= 0)) {
          armAdsPhBean = this.toArmAdsPhBean(tel, loAdsBean.getIdAds(), 3);
          statements.add(new ParametricStatement(armAdsPhBean.getInsertSql(), armAdsPhBean.toList()));
        }
      }
      // *** Customer stg outbound sql
      statements.addAll(getInsertSQLForOutboundA(addr, customer.getId()));
    }
    if (customer.isLoyaltyMember()) {
      PaKyCtOracleBean paKyCtOracleBean = toPaKyCtBean(customer);
      CoCtafOracleBean coCtafOracleBean = toCoCtaf(customer);
      statements.add(new ParametricStatement(paKyCtOracleBean.getInsertSql(), paKyCtOracleBean.toList()));
      statements.add(new ParametricStatement(coCtafOracleBean.getInsertSql(), coCtafOracleBean.toList()));
        }
    }
    catch(Exception e) {
        e.printStackTrace();
    }
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param customer
   * @return
   * @exception SQLException
   */
  public List getInsertSQLForOutboundB (CMSCustomer customer) throws SQLException {
    List statements = new ArrayList();
    ArmStgCustOutOracleBean bean = new ArmStgCustOutOracleBean();
    bean.setRecordType(ArtsConstants.CUST_RECORD_TYPE_B);
    bean.setCustomerId(customer.getId());
    bean.setDataPopulationDt(new Date());
    bean.setCustBarcode(customer.getCustomerBC());
    bean.setTitle(customer.getTitle());
    bean.setFirstNm(customer.getFirstName());
    bean.setLastNm(customer.getLastName());
    bean.setMiddleNm(customer.getMiddleName());
    bean.setSuffix(customer.getSuffix());
    bean.setDbLastNm(customer.getDBLastName());
    bean.setDbFirstNm(customer.getDBFirstName());
    bean.setGender(customer.getGender());
    bean.setCustType(customer.getCustomerType());
    if (customer.isLoyaltyMember())
      bean.setLoyaltyProgram("1");
    else
      bean.setLoyaltyProgram("0");
    bean.setVipPercent(customer.getVIPDiscount());
    bean.setPrivacy(customer.getPrivacyCode());
    if (customer.isCanMail())
      bean.setNoMail("0");
    else
      bean.setNoMail("1");
    if (customer.isCanEmail())
      bean.setNoEmail("0");
    else
      bean.setNoEmail("1");
    if (customer.isCanCall())
      bean.setNoCall("0");
    else
      bean.setNoCall("1");
    if (customer.isCanSMS())
      bean.setNoSms("0");
    else
      bean.setNoSms("1");
    bean.setEmail1(customer.getEMail());
    bean.setEmail2(customer.getSecondaryEmail());
    //Added for Privacy Mgmt Marketing and Master
    if(customer.isPrivacyMarketing()){
    	bean.setFlMarketing("1");
    }else{
    	bean.setFlMarketing("0");
    }
    if(customer.isPrivacyMaster()){
    	bean.setFlMaster("1");
    }else{
    	bean.setFlMaster("0");
    }
    bean.setCustIssueDate(customer.getCustIssueDate());
    statements.add(new ParametricStatement(bean.getInsertSql(), bean.toList()));
    return  statements;
  }

  /**
   * put your documentation comment here
   * @param customer
   * @return
   * @exception SQLException
   */
  public List getInsertSQLForOutboundD (CMSCustomer customer) throws SQLException {
    List statements = new ArrayList();
    ArmStgCustOutOracleBean bean = new ArmStgCustOutOracleBean();
    bean.setRecordType(ArtsConstants.CUST_RECORD_TYPE_D);
    bean.setCustomerId(customer.getId());
    bean.setDataPopulationDt(new Date());
    bean.setVatNum(customer.getVatNumber());
    bean.setFiscalCd(customer.getFiscalCode());
    bean.setIdType(customer.getIdType());
    bean.setDocNum(customer.getDocNumber());
    bean.setPlaceOfIssue(customer.getPlaceOfIssue());
    bean.setIssueDt(customer.getIssueDate());
    bean.setPayType(customer.getPymtType());
    bean.setCompanyId(customer.getCompanyId());
    bean.setInterCmyCd(customer.getInterCompanyCode());
    bean.setCustStatus(customer.getCustomerStatus());
    bean.setAccNum(customer.getAccountNum());
    Date dtBirthDate = customer.getDateOfBirth();
    bean.setRealBirthday(dtBirthDate);
    try {
      if (customer.getAge() != null && customer.getAge().trim().length() > 0) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dtBirthDate);
        String birthDay = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        String birthMonth = String.valueOf(cal.get(Calendar.MONTH)+1);
        bean.setBirthDay(birthDay);
        bean.setBirthMonth(birthMonth);
      }
    } catch (Exception e) {
      bean.setBirthDay("");
      bean.setBirthMonth("");
      bean.setRealBirthday(null);
    }
    try {
      bean.setAge("" + CustomerUtil.getAgeRangeIndex(dtBirthDate));
    } catch (Exception ex) {
      //ex.printStackTrace();
      if (customer.getAge() != null && customer.getAge().trim().length() > 0)
        bean.setAge(customer.getAge());
      else
      bean.setAge("" + CustomerUtil.AGE_RANGE_NOT_DEFINED_INDEX);
    }
    bean.setReferredBy(customer.getRefBy());
    bean.setProfession(customer.getProfession());
    bean.setEducation(customer.getEducation());
    bean.setNotes1(customer.getNotes1());
    bean.setNotes2(customer.getNotes2());
    bean.setPntrFmlyNm(customer.getPtLastName());
    bean.setPntrNm(customer.getPtFirstName());
    bean.setBirthPlace(customer.getPlaceOfBirth());
    bean.setSplEvtType(customer.getSpEventType());
    bean.setSplEvtDt(customer.getSpecialEventDate());
    bean.setChildrenNames(customer.getChildName());
    try {
      bean.setNumOfChildren(Integer.parseInt(customer.getNumOfChildren()));
    } catch (Exception e) {
      bean.setNumOfChildren(0);
    }
    bean.setCreatedoffline(customer.getCreateOffline());
    bean.setSupplierPayment(customer.getSupplierPymt());
    bean.setBank(customer.getBank());
    bean.setCrdtCrdNum1(customer.getCreditCardNum1());
    bean.setCrdtCrdTyp1(customer.getCreditCardType1());
    bean.setCrdtCrdNum2(customer.getCreditCardNum2());
    bean.setCrdtCrdTyp2(customer.getCreditCardType2());
    statements.add(new ParametricStatement(bean.getInsertSql(), bean.toList()));
    return  statements;
  }

  /**
   * put your documentation comment here
   * @param addr
   * @param custId
   * @return
   * @exception SQLException
   */
  public List getInsertSQLForOutboundA (Address addr, String custId) throws SQLException {
    List statements = new ArrayList();
    Telephone tel;
    ArmStgCustOutOracleBean bean = new ArmStgCustOutOracleBean();
    bean.setRecordType(ArtsConstants.CUST_RECORD_TYPE_A);
    bean.setCustomerId(custId);
    bean.setDataPopulationDt(new Date());
    bean.setAddressLine1(addr.getAddressLine1());
    bean.setAddressLine2(addr.getAddressLine2());
    bean.setUnitName(addr.getDirectional());
    bean.setCity(addr.getCity());
    bean.setState(addr.getState());
    if (addr.getZipCodeExtension() != null && addr.getZipCodeExtension().length() > 0) {
      bean.setPostCode(addr.getZipCode() + "-" + addr.getZipCodeExtension());
    }
    else {
      bean.setPostCode(addr.getZipCode());
    }
    bean.setCountry(addr.getCountry());
    bean.setAddressType(addr.getAddressType());
    if (addr.isUseAsPrimary())
      bean.setUseAsPrimaryAddr("1");
    else
      bean.setUseAsPrimaryAddr("0");
    tel = addr.getPrimaryPhone();
    if (tel != null) {
      bean.setPhoneType1(tel.getTelephoneType().getType());
      bean.setPhoneNumber1(tel.getTelephoneNumber());
    }
    tel = addr.getSecondaryPhone();
    if (tel != null) {
      bean.setPhoneType2(tel.getTelephoneType().getType());
      bean.setPhoneNumber2(tel.getTelephoneNumber());
    }
    tel = addr.getTernaryPhone();
    if (tel != null) {
      bean.setPhoneType3(tel.getTelephoneType().getType());
      bean.setPhoneNumber3(tel.getTelephoneNumber());
    }
    statements.add(new ParametricStatement(bean.getInsertSql(), bean.toList()));
    return  statements;
  }

  /**
   * put your documentation comment here
   * @param ca
   * @param customer
   * @return
   * @exception SQLException
   */
  public List getInsertSQLForOutboundCA (CustomerSalesAssociate ca, CMSCustomer customer) throws SQLException {
    List statements = new ArrayList();
    ArmStgCustOutOracleBean bean = new ArmStgCustOutOracleBean();
    bean.setRecordType(ArtsConstants.CUST_RECORD_TYPE_CA);
    bean.setCustomerId(customer.getId());
    bean.setDataPopulationDt(new Date());
    bean.setStoreId(ca.getStoreId());
    bean.setCompanyCd(customer.getCompanyId());
    bean.setAssociateId(ca.getAssocId());
    statements.add(new ParametricStatement(bean.getInsertSql(), bean.toList()));
    return  statements;
  }

  /**
   * put your documentation comment here
   * @param cc
   * @param customer
   * @return
   * @exception SQLException
   */
  public List getInsertSQLForOutboundCC (CustomerComment cc, CMSCustomer customer) throws SQLException {
    List statements = new ArrayList();
    ArmStgCustOutOracleBean bean = new ArmStgCustOutOracleBean();
    bean.setRecordType(ArtsConstants.CUST_RECORD_TYPE_CC);
    bean.setCustomerId(customer.getId());
    bean.setDataPopulationDt(new Date());
    bean.setStoreId(cc.getStoreId());
    bean.setCompanyCd(customer.getCompanyId());
    bean.setAssociateId(cc.getAssociateId());
    bean.setBrand(cc.getBrandId());
    bean.setCreateDate(cc.getDateCommented());
    bean.setComments(cc.getComment());
    statements.add(new ParametricStatement(bean.getInsertSql(), bean.toList()));
    return  statements;
  }

  /**
   * put your documentation comment here
   * @param object
   * @exception SQLException
   */
  public void update (Customer object) throws SQLException {
    this.execute(this.getUpdateSQL(object));
  }

  /**
   * put your documentation comment here
   * @param obj
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getUpdateSQL (Customer obj) throws SQLException {
    CMSCustomer object = (CMSCustomer)obj;
    List statements = new ArrayList();
    String addressId = null;
    List params = null;
    try {
      params = this.toPaPrsBean(object).toList();
      params.add(object.getId());
      statements.add(new ParametricStatement(PaPrsOracleBean.updateSql + BaseOracleDAO.where(PaPrsOracleBean.COL_ID_PRTY_PRS), params));
      params = this.toPaCtBean(object).toList();
      params.add(object.getId());
      statements.add(new ParametricStatement(PaCtOracleBean.updateSql + BaseOracleDAO.where(PaCtOracleBean.COL_ID_CT), params));
      params = this.toArmCtDtlBean(object).toList();
      params.add(object.getId());
      statements.add(new ParametricStatement(ArmCtDtlOracleBean.updateSql + BaseOracleDAO.where(ArmCtDtlOracleBean.COL_ID_CT), params));
      params = new ArrayList();
      params.add(object.getId());
      
          
      BaseOracleBean[] eBeans = this.query(new LoEmlAdsOracleBean(), BaseOracleDAO.where(LoEmlAdsOracleBean.COL_ID_PRTY), params);
      if (eBeans != null && eBeans.length > 0) {
        for (int i = 0; i < eBeans.length; i++) {
          statements.add(new ParametricStatement(LoEmlAdsOracleBean.deleteSql + BaseOracleDAO.where(LoEmlAdsOracleBean.COL_ID_PRTY), params));
        }
      }
      LoEmlAdsOracleBean loEmlAdsBean = this.toLoEmlAdsBean(object, 1);
      if (loEmlAdsBean != null)
        statements.add(new ParametricStatement(loEmlAdsBean.getInsertSql(), loEmlAdsBean.toList()));
      loEmlAdsBean = this.toLoEmlAdsBean(object, 2);
      if (loEmlAdsBean != null)
        statements.add(new ParametricStatement(loEmlAdsBean.getInsertSql(), loEmlAdsBean.toList()));
      statements.addAll(getInsertSQLForOutboundB(object));
      if (object.isUpdateAllStgTbl() || object.isModifiedForD())
        statements.addAll(getInsertSQLForOutboundD(object));
      // check if the sales associate is modified
      List assocList = object.getSalesAssociates();
      for (int i = 0; i < assocList.size(); i++) {
        CustomerSalesAssociate csa = (CustomerSalesAssociate)assocList.get(i);
        if (object.isUpdateAllStgTbl() || csa.isModified()) {
          params = this.toArmCtAssocBean(csa, object.getId()).toList();
          params.add(object.getId());
          params.add(csa.getStoreId());
          statements.add(new ParametricStatement(ArmCtAssocOracleBean.updateSql + BaseOracleDAO.where(ArmCtAssocOracleBean.COL_ID_CT, ArmCtAssocOracleBean.COL_ID_STR_RT), params));
          // *** Customer stg outbound sql
          statements.addAll(getInsertSQLForOutboundCA(csa, object));
        }
      }
      // deal with the newly added associates
      List newAssocList = object.getNewSalesAssociates();
      for (int i = 0; i < newAssocList.size(); i++) {
        CustomerSalesAssociate csa = (CustomerSalesAssociate)newAssocList.get(i);
        csa.setCustSalesAssocId(this.getNextChelseaId());
        ArmCtAssocOracleBean armCtAssocBean = this.toArmCtAssocBean(csa, object.getId());
        statements.add(new ParametricStatement(armCtAssocBean.getInsertSql(), armCtAssocBean.toList()));
        // *** Customer stg outbound sql
        statements.addAll(getInsertSQLForOutboundCA(csa, object));
      }
      List newCommentList = object.getNewCustomerComments();
      for (int i = 0; i < newCommentList.size(); i++) {
        CustomerComment cc = (CustomerComment)newCommentList.get(i);
        cc.setCustomerCommentId(this.getNextChelseaId());
        ArmCtCommentsOracleBean armCtCommentsBean = this.toArmCtCommentsBean(cc, object.getId());
        statements.add(new ParametricStatement(armCtCommentsBean.getInsertSql(), armCtCommentsBean.toList()));
        // *** Customer stg outbound sql
        statements.addAll(getInsertSQLForOutboundCC(cc, object));
      }
      // Europe- Update all the customer comments into Staging Table
      if (object.isUpdateAllStgTbl()) {
        List commentList = object.getCustomerComments();
        for (int i = 0; i < commentList.size(); i++) {
          CustomerComment cc = (CustomerComment)commentList.get(i);
          // *** Customer stg outbound sql
          statements.addAll(getInsertSQLForOutboundCC(cc, object));
        }
      }
      // Issue # 989
      // Insert or Remove or Delete Customer Credi Cards
      List creditCardList = object.getCreditCard();
      String custID=object.getId();
      // Delete the Credit Cards
      for (int i = 0; i < creditCardList.size(); i++) {
        CustomerCreditCard creditCard = (CustomerCreditCard)creditCardList.get(i);
        if (creditCard != null && creditCard.isRemove()) {
            String ccNum = creditCard.getCreditCardNumber();
            // Encrypt the Credit Card Number
            //System.out.println("Ruchi just before the encryption:"+ccNum);
            EncryptionUtils encryptionUtils = new EncryptionUtils();
            String encryptCCNum = encryptionUtils.encrypt(ccNum);
            creditCard.setCreditCardNumber(encryptCCNum);
            
        //sonali    String creditCardNumber = creditCard.getCreditCardNumber();
            //Commenting the code because of US testing issues
//          creditCard.setCreditCardNumber(ccNum);
           // creditCard.setEncryptedCCData(creditCard.getEncryptedCCData());
            //creditCard.setKey_id(creditCard.getKey_id());
            //end comments
            params = new ArrayList();
            //sonali
            params.add(creditCard.getCardToken());
            params.add(custID);
            params.add(creditCard.getStoreId());
            List listParam = toArmCtCrdbCrdDtlBeanList(params);
            //System.out.println("delte CUSTOMER CC >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ ccNum);
            //sonali:AJB modified for delete sql to add token num in parameters
            statements.add(new ParametricStatement(ArmCtCrdbCrdDtlOracleBean.deleteSql + BaseOracleDAO.where(ArmCtCrdbCrdDtlOracleBean.COL_ID_CARD_TOKEN,ArmCtCrdbCrdDtlOracleBean.COL_ID_CT,ArmCtCrdbCrdDtlOracleBean.COL_ID_STR_RT), listParam));
        }
      }
      // Add and Modify the Credit Cards
      for (int i = 0; i < creditCardList.size(); i++) {
        CustomerCreditCard creditCard = (CustomerCreditCard)creditCardList.get(i);
        // Encrypt the Credit Card Number
        EncryptionUtils encryptionUtils = new EncryptionUtils();
        String ccNum = creditCard.getCreditCardNumber();
        //System.out.println("Ruchi credit card number to encrypt   :"+ccNum);
       String encryptCCNum = encryptionUtils.encrypt(ccNum);
      // sonali  creditCard.setCreditCardNumber(encryptCCNum);
        //System.out.println("ADD CUSTOMER CC >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ encryptCCNum);
        //sonali
        String maskcreditCardNumber = creditCard.getMaskedCreditCardNum();
        //System.out.println("creditCardNumber    :"+creditCardNumber);
        //Commenting thE ISD code becase of US testing issues
       // String encryptedString = creditCard.getEncryptedCCData();
       // String key_id = creditCard.getKey_id();
        //ends
        if (maskcreditCardNumber != null) {
          params = new ArrayList();
          params.add(maskcreditCardNumber);
          //Commenting thE ISD code becase of US testing issues
          //params.add(encryptedString);
          //params.add(key_id);
          //ends
          if (creditCard.isNew()) {
            ArmCtCrdbCrdDtlOracleBean armCtCrdbCrdDtlBean = this.toArmCtCrdbCrdDtlBean(creditCard, object.getId());
            if(creditCard.getCardToken()!=null){
            armCtCrdbCrdDtlBean.setCrdToken(creditCard.getCardToken());
            }
            List list = toArmCtCrdbCrdDtlBeanList(armCtCrdbCrdDtlBean.toList());
            statements.add(new ParametricStatement(armCtCrdbCrdDtlBean.getInsertSql(), list));
            //statements.addAll(getInsertSQLForOutboundCCard(creditCard,object.getId()));
          }
          else if (!creditCard.isNew() && creditCard.isModified()) {
            //params = new ArrayList();
            List listParams = this.toArmCtCrdbCrdDtlBean(creditCard, object.getId()).toList();
            listParams.add(creditCard.getCardToken());
            listParams.add(custID);
            listParams.add(creditCard.getStoreId());
            //sonali
            
            List list = toArmCtCrdbCrdDtlBeanList(listParams);
            //params.add(creditCardNumber);
            //System.out.println("mod CUSTOMER CC >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ creditCardNumber);
            //sonali:AJB modified for update sql to add token num in parameters
            statements.add(new ParametricStatement(ArmCtCrdbCrdDtlOracleBean.updateSql + BaseOracleDAO.where(ArmCtCrdbCrdDtlOracleBean.COL_ID_CARD_TOKEN,ArmCtCrdbCrdDtlOracleBean.COL_ID_CT,ArmCtCrdbCrdDtlOracleBean.COL_ID_STR_RT), list));
          }
        }
      }
      // End Issue # 989
      List addressList = object.getAddresses();
      for (int i = 0; i < addressList.size(); i++) {
        Address addr = (Address)addressList.get(i);
        addressId = addr.getAddressId();
        if (addressId != null) {
          params = new ArrayList();
          params.add(addressId);
          if (addr.isRemove()) {
            statements.add(new ParametricStatement(ArmAdsPhOracleBean.deleteSql + BaseOracleDAO.where(ArmAdsPhOracleBean.COL_ID_ADS), params));
            statements.add(new ParametricStatement(LoAdsNstdOracleBean.deleteSql + BaseOracleDAO.where(LoAdsNstdOracleBean.COL_ID_ADS), params));
            statements.add(new ParametricStatement(LoAdsPrtyOracleBean.deleteSql + BaseOracleDAO.where(LoAdsPrtyOracleBean.COL_ID_ADS), params));
            statements.add(new ParametricStatement(LoAdsOracleBean.deleteSql + BaseOracleDAO.where(LoAdsOracleBean.COL_ID_ADS), params));
          }
          else {
            params = this.toLoAdsNstdBean(addressId, addr).toList();
            params.add(addressId);
            statements.add(new ParametricStatement(LoAdsNstdOracleBean.updateSql + BaseOracleDAO.where(LoAdsNstdOracleBean.COL_ID_ADS), params));
            // Manpreet --- (09/06/05)  Update AddressType for modified customer
            params = new ArrayList();
            params = this.toLoAdsPrtyBean(object, addressId, addr).toList();
            params.add(addressId);
            statements.add(new ParametricStatement(LoAdsPrtyOracleBean.updateSql + BaseOracleDAO.where(LoAdsPrtyOracleBean.COL_ID_ADS), params));
            Telephone tel = addr.getPrimaryPhone();
            if (tel != null) {
              // gotta check if this type of telephone exists
              params = new ArrayList();
              params.add(addressId);
              params.add(tel.getTelephoneType().getType());
              BaseOracleBean[] beans = this.query(new ArmAdsPhOracleBean(), BaseOracleDAO.where(ArmAdsPhOracleBean.COL_ID_ADS, ArmAdsPhOracleBean.COL_ID_PH_TYP), params);
              if (beans != null && beans.length > 0) {
                params = this.toArmAdsPhBean(tel, addressId, 1).toList();
                params.add(addressId);
                params.add(tel.getTelephoneType().getType());
                statements.add(new ParametricStatement(ArmAdsPhOracleBean.updateSql + BaseOracleDAO.where(ArmAdsPhOracleBean.COL_ID_ADS, ArmAdsPhOracleBean.COL_ID_PH_TYP), params));
              }
              else {
                // this is not an update but an insert
                ArmAdsPhOracleBean armAdsPhBean = this.toArmAdsPhBean(tel, addr.getAddressId(), 1);
                statements.add(new ParametricStatement(armAdsPhBean.getInsertSql(), armAdsPhBean.toList()));
              }
            }
            tel = addr.getSecondaryPhone();
            if (tel != null) {
              // gotta check if this type of telephone exists
              params = new ArrayList();
              params.add(addressId);
              params.add(tel.getTelephoneType().getType());
              BaseOracleBean[] beans = this.query(new ArmAdsPhOracleBean(), BaseOracleDAO.where(ArmAdsPhOracleBean.COL_ID_ADS, ArmAdsPhOracleBean.COL_ID_PH_TYP), params);
              if (beans != null && beans.length > 0) {
                params = this.toArmAdsPhBean(tel, addressId, 2).toList();
                params.add(addressId);
                params.add(tel.getTelephoneType().getType());
                statements.add(new ParametricStatement(ArmAdsPhOracleBean.updateSql + BaseOracleDAO.where(ArmAdsPhOracleBean.COL_ID_ADS, ArmAdsPhOracleBean.COL_ID_PH_TYP), params));
              }
              else {
                // this is not an update but an insert
                ArmAdsPhOracleBean armAdsPhBean = this.toArmAdsPhBean(tel, addressId, 2);
                statements.add(new ParametricStatement(armAdsPhBean.getInsertSql(), armAdsPhBean.toList()));
              }
            }
            tel = addr.getTernaryPhone();
            if (tel != null) {
              // gotta check if this type of telephone exists
              params = new ArrayList();
              params.add(addressId);
              params.add(tel.getTelephoneType().getType());
              BaseOracleBean[] beans = this.query(new ArmAdsPhOracleBean(), BaseOracleDAO.where(ArmAdsPhOracleBean.COL_ID_ADS, ArmAdsPhOracleBean.COL_ID_PH_TYP), params);
              if (beans != null && beans.length > 0) {
                params = this.toArmAdsPhBean(tel, addressId, 3).toList();
                params.add(addressId);
                params.add(tel.getTelephoneType().getType());
                statements.add(new ParametricStatement(ArmAdsPhOracleBean.updateSql + BaseOracleDAO.where(ArmAdsPhOracleBean.COL_ID_ADS, ArmAdsPhOracleBean.COL_ID_PH_TYP), params));
              }
              else {
                // this is not an update but an insert
                ArmAdsPhOracleBean armAdsPhBean = this.toArmAdsPhBean(tel, addressId, 3);
                statements.add(new ParametricStatement(armAdsPhBean.getInsertSql(), armAdsPhBean.toList()));
              }
            }
            if (object.isUpdateAllStgTbl() || addr.isModified()) {
              // *** Customer stg outbound sql
              statements.addAll(getInsertSQLForOutboundA(addr, object.getId()));
            }
          }
        }
        else {
          // this is an insert and not an update
          LoAdsOracleBean loAdsBean = this.toLoAdsBean(object);
          addressId = loAdsBean.getIdAds();
          LoAdsPrtyOracleBean loAdsPrtyBean = this.toLoAdsPrtyBean(object, addressId, addr);
          LoAdsNstdOracleBean loAdsNstdBean = this.toLoAdsNstdBean(addressId, addr);
          statements.add(new ParametricStatement(loAdsBean.getInsertSql(), loAdsBean.toList()));
          statements.add(new ParametricStatement(loAdsNstdBean.getInsertSql(), loAdsNstdBean.toList()));
          statements.add(new ParametricStatement(loAdsPrtyBean.getInsertSql(), loAdsPrtyBean.toList()));
          // Telephone details
          ArmAdsPhOracleBean armAdsPhBean = null;
          Telephone tel = null;
          tel = addr.getPrimaryPhone();
          if (tel != null) {
            armAdsPhBean = this.toArmAdsPhBean(tel, loAdsBean.getIdAds(), 1);
            statements.add(new ParametricStatement(armAdsPhBean.getInsertSql(), armAdsPhBean.toList()));
          }
          tel = addr.getSecondaryPhone();
          if (tel != null) {
            armAdsPhBean = this.toArmAdsPhBean(tel, loAdsBean.getIdAds(), 2);
            statements.add(new ParametricStatement(armAdsPhBean.getInsertSql(), armAdsPhBean.toList()));
          }
          tel = addr.getTernaryPhone();
          if (tel != null) {
            armAdsPhBean = this.toArmAdsPhBean(tel, loAdsBean.getIdAds(), 3);
            statements.add(new ParametricStatement(armAdsPhBean.getInsertSql(), armAdsPhBean.toList()));
          }
          // *** Customer stg outbound sql
          statements.addAll(getInsertSQLForOutboundA(addr, object.getId()));
        }
      }
      params = new ArrayList();
      params.add(object.getId());
      statements.add(new ParametricStatement(CoCtafOracleBean.deleteSql + where(CoCtafOracleBean.COL_ID_CT), params));
      statements.add(new ParametricStatement(PaKyCtOracleBean.deleteSql + where(PaKyCtOracleBean.COL_ID_CT), params));
      if (object.isLoyaltyMember()) {
        PaKyCtOracleBean paKyCtOracleBean = toPaKyCtBean(object);
        CoCtafOracleBean coCtafOracleBean = toCoCtaf(object);
        statements.add(new ParametricStatement(paKyCtOracleBean.getInsertSql(), paKyCtOracleBean.toList()));
        statements.add(new ParametricStatement(coCtafOracleBean.getInsertSql(), coCtafOracleBean.toList()));
      }
    } catch (Exception e) {
      System.out.println("========Exception in CustomerOracleDAO.getUpdateSQL=========");
      e.printStackTrace();
      throw  new SQLException();
    }
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }
  
  
  /**
   * Update ARM_STG_CUST_CRM table to set Status 1 for processed customers.
   * @param CMSCustomer   
   * @return
   */
  public ParametricStatement getUpdateSQLForCRMStagingCustomer(CMSCustomer customer) {
    String sql = "UPDATE " + ArmStgCustCrmOracleBean.TABLE_NAME + 
    			" SET " + ArmStgCustCrmOracleBean.COL_STATUS + " = ? ," 
    			+ ArmStgCustCrmOracleBean.COL_STG_PROCESS_DATE + " = ? "
    			+ where(ArmStgCustCrmOracleBean.COL_CUSTOMER_ID, ArmStgCustCrmOracleBean.COL_STORE_ID);
    ArrayList params = new ArrayList();
    params.add(""+ArmStgCustCrmOracleBean.STATUS_PROCESSED);
    params.add(new Date());
    params.add(customer.getId());
    params.add(customer.getStoreId());
    return  new ParametricStatement(sql, params);
  }  
  
  /**
   * put your documentation comment here
   * @param customerId
   * @param loyaltyMemberFlag
   * @return
   */
  public ParametricStatement getUpdateSQLForCustomerLoyaltyFlag (String customerId, boolean loyaltyMemberFlag) {
    String sql = "update " + PaCtOracleBean.TABLE_NAME + " set " + PaCtOracleBean.COL_FL_LOYALTY + " = ? " + where(PaCtOracleBean.COL_ID_CT);
    ArrayList list = new ArrayList();
    list.add((loyaltyMemberFlag) ? "1" : "0");
    list.add(customerId);
    return  new ParametricStatement(sql, list);
  }

  /**
   * put your documentation comment here
   * @param id
   * @return
   * @exception SQLException
   */
  public Customer selectById (String id) throws SQLException {
    List params = null;
    BaseOracleBean[] beans = null;
    params = new ArrayList();
    params.add(id);
    params.add(ArtsConstants.PARTY_ROLE_TYPE_CUSTOMER);
    String whereCauses[] =  {
      PaPrtyOracleBean.COL_ID_PRTY, PaPrtyOracleBean.COL_TY_PRTY
    };
    beans = this.query(new PaPrtyOracleBean(), BaseOracleDAO.where(whereCauses), params);
    if (beans == null || beans.length == 0) {
      return  null;
    }
    PaPrtyOracleBean paPrtyBean = (PaPrtyOracleBean)beans[0];
    CMSCustomer customer = new CMSCustomer(paPrtyBean.getIdPrty());
    customer.doSetPreferredISOCountry(paPrtyBean.getEdCo());
    customer.doSetPreferredISOLanguage(paPrtyBean.getEdLa());
    params = new ArrayList();
    params.add(id);
    beans = this.query(new PaPrsOracleBean(), BaseOracleDAO.where(PaPrsOracleBean.COL_ID_PRTY_PRS), params);
    if (beans != null && beans.length > 0) {
    	PaPrsOracleBean paPrsBean = (PaPrsOracleBean)beans[0];
    	customer.doSetTitle(paPrsBean.getNmPrsSln());
    	customer.doSetFirstName(paPrsBean.getFnPrs());
    	customer.doSetLastName(paPrsBean.getLnPrs());
    	customer.doSetMiddleName(paPrsBean.getMnPrs());
    	//    customer.doSetBirthDate(ArtsUtil.fromDateToString(paPrsBean.getDcPrsBrt()));
    	customer.doSetDateOfBirth(paPrsBean.getDcPrsBrt());
    	customer.doSetGender(paPrsBean.getTyGndPrs());
    	customer.doSetDBFirstName(paPrsBean.getDbFnPrs());
    	customer.doSetDBLastName(paPrsBean.getDbLnPrs());
    	customer.doSetSuffix(paPrsBean.getSuffix());
    }
    params = new ArrayList();
    params.add(id);
    beans = this.query(new LoEmlAdsOracleBean(), BaseOracleDAO.where(LoEmlAdsOracleBean.COL_ID_PRTY), params);
    if (beans != null && beans.length > 0) {
      for (int i = 0; i < beans.length; i++) {
        if (((LoEmlAdsOracleBean)beans[i]).getTyEmlAds() != null && ((LoEmlAdsOracleBean)beans[i]).getTyEmlAds().equals("EMAIL_1"))
          customer.doSetEMail(((LoEmlAdsOracleBean)beans[i]).getEmAds());
        if (((LoEmlAdsOracleBean)beans[i]).getTyEmlAds() != null && ((LoEmlAdsOracleBean)beans[i]).getTyEmlAds().equals("EMAIL_2"))
          customer.doSetSecondaryEmail(((LoEmlAdsOracleBean)beans[i]).getEmAds());
      }
    }
    params = new ArrayList();
    params.add(id);
    beans = this.query(new PaCtOracleBean(), BaseOracleDAO.where(PaCtOracleBean.COL_ID_CT), params);
    if (beans == null || beans.length == 0)
      return  null;
    PaCtOracleBean paCtBean = (PaCtOracleBean)beans[0];
    if (paCtBean.getScCt() != null && paCtBean.getScCt().equals("D"))
      return  null;
    customer.doSetCustomerStatus(paCtBean.getScCt());
    if (paCtBean.getFlRcMl() != null)
      customer.doSetReceiveMail(paCtBean.getFlRcMl().booleanValue());
    customer.doSetComment(paCtBean.getDeComments());
    customer.doSetCustomerType(paCtBean.getTyCt());
    if (paCtBean.getFlLoyalty() != null)
      customer.doSetLoyaltyMember(paCtBean.getFlLoyalty().booleanValue());
    if (paCtBean.getPeVipDisc() != null)
      customer.doSetVIPDiscount(paCtBean.getPeVipDisc().doubleValue());
    customer.doSetPrivacyCode(paCtBean.getCdPrivacy());
    customer.doSetCustomerBC(paCtBean.getCustBarcode());
    if (paCtBean.getFlMail() != null)
      customer.doSetCanMail(paCtBean.getFlMail().booleanValue());
    if (paCtBean.getFlEml() != null)
      customer.doSetCanEmail(paCtBean.getFlEml().booleanValue());
    if (paCtBean.getFlCall() != null)
      customer.doSetCanCall(paCtBean.getFlCall().booleanValue());
    if (paCtBean.getFlSms() != null)
      customer.doSetCanSMS(paCtBean.getFlSms().booleanValue());
    // For the Customer Balance.
    if (paCtBean.getCustBalance() != null) {
      customer.setCustomerBalance(paCtBean.getCustBalance());
      //Added for Privacy Mgmt Marketing and Master
      if (paCtBean.getFlMarketing() != null)
          customer.doSetPrivacyMarketing(paCtBean.getFlMarketing().booleanValue());
      if (paCtBean.getFlMaster()!= null)
          customer.doSetPrivacyMaster(paCtBean.getFlMaster().booleanValue());
    }
    //added for date Field by vivek
	customer.setCustIssueDate(paCtBean.getIssueDate());
    params = new ArrayList();
    params.add(id);
    beans = this.query(new ArmCtDtlOracleBean(), BaseOracleDAO.where(ArmCtDtlOracleBean.COL_ID_CT), params);
    if (beans != null && beans.length > 0) {
      ArmCtDtlOracleBean armCtDtlBean = (ArmCtDtlOracleBean)beans[0];
      customer.doSetVatNumber(armCtDtlBean.getVatNum());
      customer.doSetFiscalCode(armCtDtlBean.getFiscalCode());
      customer.doSetIdType(armCtDtlBean.getIdType());
      customer.doSetDocNumber(armCtDtlBean.getDocNum());
      customer.doSetPlaceOfIssue(armCtDtlBean.getPlaceOfIssue());
      customer.doSetIssueDate(armCtDtlBean.getIssueDt());
      customer.doSetPymtType(armCtDtlBean.getTyPay());
      customer.doSetCompanyId(armCtDtlBean.getIdCmy());
      customer.doSetInterCompanyCode(armCtDtlBean.getCdInterCmy());
      customer.doSetAccountNum(armCtDtlBean.getAcctNum());
      customer.doSetAge(armCtDtlBean.getAge());
      customer.doSetRefBy(armCtDtlBean.getReferredBy());
      customer.doSetProfession(armCtDtlBean.getProfession());
      customer.doSetEducation(armCtDtlBean.getEducation());
      customer.doSetNotes1(armCtDtlBean.getNotes1());
      customer.doSetNotes2(armCtDtlBean.getNotes2());
      customer.doSetPtFirstName(armCtDtlBean.getNmPtnr());
      customer.doSetPtLastName(armCtDtlBean.getNmPtnrFam());
      customer.doSetPlaceOfBirth(armCtDtlBean.getBirthPlace());
      customer.doSetSpecialEventDate(armCtDtlBean.getDcSplEvt());
      customer.doSetSpEventType(armCtDtlBean.getTySplEvt());
      customer.doSetChildName(armCtDtlBean.getNmChld());
      customer.doSetNumOfChildren(armCtDtlBean.getChldNum());
      customer.doSetCreateOffline(armCtDtlBean.getCreateOffline());
      customer.doSetSupplierPymt(armCtDtlBean.getSupplierPayment());
      customer.doSetBank(armCtDtlBean.getBank());
      customer.doSetCreditCardNum1(armCtDtlBean.getCrdtCrdNum1());
      customer.doSetCreditCardType1(armCtDtlBean.getCrdtCrdTyp1());
      customer.doSetCreditCardNum2(armCtDtlBean.getCrdtCrdNum2());
      customer.doSetCreditCardType2(armCtDtlBean.getCrdtCrdTyp2());
    }
    params = new ArrayList();
    params.add(id);
    beans = this.query(new ArmCtAssocOracleBean(), BaseOracleDAO.where(ArmCtAssocOracleBean.COL_ID_CT), params);
    if (beans != null && beans.length > 0) {
      for (int i = 0; i < beans.length; i++) {
        ArmCtAssocOracleBean armCtAssocBean = (ArmCtAssocOracleBean)beans[i];
        CustomerSalesAssociate csa = new CustomerSalesAssociate();
        csa.setCustSalesAssocId(armCtAssocBean.getIdCtAssociate());
        csa.setStoreId(armCtAssocBean.getIdStrRt());
        csa.setAssocId(armCtAssocBean.getIdAssociate());
        customer.addSalesAssociate(csa);
      }
    }
    params = new ArrayList();
    params.add(id);
    beans = this.query(new ArmCtCommentsOracleBean(), BaseOracleDAO.where(ArmCtCommentsOracleBean.COL_ID_CT), params);
    if (beans != null && beans.length > 0) {
      for (int i = 0; i < beans.length; i++) {
        ArmCtCommentsOracleBean armCtCommentsBean = (ArmCtCommentsOracleBean)beans[i];
        CustomerComment cc = new CustomerComment();
        cc.setCustomerCommentId(armCtCommentsBean.getIdCtComment());
        cc.setStoreId(armCtCommentsBean.getIdStrRt());
        cc.setAssociateId(armCtCommentsBean.getIdAssociate());
        cc.setBrandId(armCtCommentsBean.getIdBrn());
        cc.setDateCommented(armCtCommentsBean.getCreateDt());
        cc.setComment(armCtCommentsBean.getComments());
        customer.addCustomerComment(cc);
      }
    }
    // Issue # 989
    params = new ArrayList();
    params.add(id);
    beans = this.query(new ArmCtCrdbCrdDtlOracleBean(), BaseOracleDAO.where(ArmCtCrdbCrdDtlOracleBean.COL_ID_CT), params);
    if (beans != null && beans.length > 0) {
      for (int i = 0; i < beans.length; i++) {
        ArmCtCrdbCrdDtlOracleBean armCrdbCrdDtlBean = (ArmCtCrdbCrdDtlOracleBean)beans[i];
        CustomerCreditCard cc = new CustomerCreditCard();
        // Decrypt the Credit Card Number
        EncryptionUtils encryptionUtils = new EncryptionUtils();
        String ccNum = armCrdbCrdDtlBean.getIdAcntDbCrCrd();
        //Commented by sonali
      //  String decryptCCNum = encryptionUtils.decrypt(ccNum);
     //   cc.setCreditCardNumber(decryptCCNum);
        //modified by sonali for setting masked Card number instead of unmasked card number
        if(armCrdbCrdDtlBean.getMaskedCardNum()!=null)
        cc.setCreditCardNumber(armCrdbCrdDtlBean.getMaskedCardNum());
        //end
        //cc.setCreditCardNumber(armCrdbCrdDtlBean.getIdAcntDbCrCrd());
        cc.setCreditCardType(armCrdbCrdDtlBean.getTyCrd());
        cc.setBillingZipCode(armCrdbCrdDtlBean.getZipCode());
        cc.setExpDate(armCrdbCrdDtlBean.getExpirationDate());
        cc.setStoreId(armCrdbCrdDtlBean.getIdStrRt());
        //vivek added to get CC encrypted data
        //Commenting the code because of US testing issues
        //cc.setEncryptedCCData(armCrdbCrdDtlBean.getEncryptedData());
        //if(armCrdbCrdDtlBean.getKeyId()!=null)
        //cc.setKey_id(armCrdbCrdDtlBean.getKeyId());
        //ends
       if(armCrdbCrdDtlBean.getCrdToken()!=null)
        cc.setCardToken(armCrdbCrdDtlBean.getCrdToken());
       if(armCrdbCrdDtlBean.getMaskedCardNum()!=null)
        cc.setMaskedCreditCardNum(armCrdbCrdDtlBean.getMaskedCardNum());
        
        //Vivek Mishra : Added for fetching and unzipping the signature data.  
        //Vivek Mishra : Fix added for class cast issue from BLOB to ByteArray 
        //if(!(bean.getSignature().equals(null))){
        //Vivek Mishra : Changed condition for avoiding fatal exception while searching a transaction with null signature.
        if(armCrdbCrdDtlBean.getCustSignature()!=null){
        Blob blob = (Blob)armCrdbCrdDtlBean.getCustSignature();
        byte[] blobAsBytes = null;
        int blobLength;
    	try {
    		blobLength = (int) blob.length();
    		blobAsBytes = blob.getBytes(1, blobLength);
    	} catch (SQLException e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    	}  
    	//Ends
        //release the blob and free up memory. (since JDBC 4.0)
       
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(blobAsBytes);
        // A gzip expander
        GZIPInputStream gzipis = null;
    	try {
    		gzipis = new GZIPInputStream(byteArrayInputStream);
    	} catch (IOException e) {
    		
    		e.printStackTrace();
    	}
        
        byte[] unzip = new byte[20000];
        // Read the compressed data into the uncompressed byte array
        try {
    		gzipis.read(unzip, 0, 20000);
    	} catch (IOException e) {
    		
    		e.printStackTrace();
    	}
    	cc.setSignByteCode(unzip);
        }
        //Ends
          
        customer.addCreditCard(cc);
      }
    }
    // End Issue # 989
    params = new ArrayList();
    params.add(id);
    beans = this.query(new LoAdsPrtyOracleBean(), BaseOracleDAO.where(LoAdsPrtyOracleBean.COL_ID_PRTY), params);
    if (beans != null && beans.length > 0) {
      for (int i = 0; i < beans.length; i++) {
        LoAdsPrtyOracleBean loAdsPrtyBean = (LoAdsPrtyOracleBean)beans[i];
        String addressId = loAdsPrtyBean.getIdAds();
        params = new ArrayList();
        params.add(addressId);
        BaseOracleBean[] addrBeans = this.query(new LoAdsNstdOracleBean(), BaseOracleDAO.where(LoAdsNstdOracleBean.COL_ID_ADS), params);
        if (addrBeans != null && addrBeans.length > 0) {
        LoAdsNstdOracleBean loAdsNstdBean = (LoAdsNstdOracleBean)addrBeans[0];
        Address addr = new Address();
        addr.doSetAddressId(loAdsPrtyBean.getIdAds());
        addr.doSetAddressType(loAdsPrtyBean.getTyAds());
        addr.doSetAddressLine1(loAdsNstdBean.getA1Ads());
        addr.doSetAddressLine2(loAdsNstdBean.getA2Ads());
        addr.doSetCity(loAdsNstdBean.getNmUn());
        addr.doSetState(loAdsNstdBean.getTeNm());
        String zipCode = loAdsNstdBean.getPcNm();
        if (zipCode != null && zipCode.indexOf("-") == 5) {
          addr.doSetZipCode(zipCode.substring(0, 5));
          addr.doSetZipCodeExtension(zipCode.substring(6, zipCode.length()));
        } else {
          addr.doSetZipCode(zipCode);
        }
        addr.doSetCountry(loAdsNstdBean.getCoNm());
        addr.doSetDirectional(loAdsNstdBean.getMuNm());
        addr.doSetAddressFormat(loAdsNstdBean.getAdsFormat());
        if (loAdsNstdBean.getFlPrmyAds() != null)
          addr.doSetUseAsPrimary(loAdsNstdBean.getFlPrmyAds().booleanValue());
        params = new ArrayList();
        params.add(addressId);
        BaseOracleBean[] telBeans = this.query(new ArmAdsPhOracleBean(), BaseOracleDAO.where(ArmAdsPhOracleBean.COL_ID_ADS), params);
        if (telBeans != null && telBeans.length > 0) {
          for (int j = 0; j < telBeans.length; j++) {
            ArmAdsPhOracleBean armAdsPhBean = (ArmAdsPhOracleBean)telBeans[j];
            Telephone tel = null;
            if (j == 0) {
              tel = new Telephone(new TelephoneType(armAdsPhBean.getIdPhTyp()), armAdsPhBean.getCcPh(), armAdsPhBean.getTaPh(), armAdsPhBean.getTlPh(), armAdsPhBean.getPhExtn());
              tel = tel.newTelephoneNumber(armAdsPhBean.getTlPh());
              addr.doSetPrimaryPhone(tel);
            }
            if (j == 1) {
              tel = new Telephone(new TelephoneType(armAdsPhBean.getIdPhTyp()), armAdsPhBean.getCcPh(), armAdsPhBean.getTaPh(), armAdsPhBean.getTlPh(), armAdsPhBean.getPhExtn());
              tel =tel.newTelephoneNumber(armAdsPhBean.getTlPh());
              addr.doSetSecondaryPhone(tel);
            }
            if (j == 2) {
              tel = new Telephone(new TelephoneType(armAdsPhBean.getIdPhTyp()), armAdsPhBean.getCcPh(), armAdsPhBean.getTaPh(), armAdsPhBean.getTlPh(), armAdsPhBean.getPhExtn());
              tel = tel.newTelephoneNumber(armAdsPhBean.getTlPh());
              addr.doSetTernaryPhone(tel);
            }
          }
        }
        if (useQas != null && useQas.equalsIgnoreCase("true")) {
        	addr.doSetQasModified(true);
        	addr.doSetQasVerifyLevel("Verified");
        }
        customer.addAddress(addr);
      }
    }
    }
    return  customer;
  }

  /**
   * put your documentation comment here
   * @param ids
   * @return
   * @exception Exception
   */
  public Customer[] selectByIds (String[] ids) throws Exception {
    ids = ArtsUtil.removeDupString(ids);
    int totalRecords = 0;    
    if (maxRecords > 0) {
    	if (ids.length > maxRecords) {
    		totalRecords = maxRecords;
    		if (throwException != null && throwException.equalsIgnoreCase("true")) {
    			throw new TooManySearchResultsException("More than " + maxRecords +" query results");
    		}
    	}
    }
    totalRecords = ids.length;
    List result = new ArrayList();
    for (int i = 0; i < totalRecords; i++) {
      Customer customer = this.selectById(ids[i]);
      if (customer != null) {
        result.add(customer);
      }
    }
    return  (Customer[])result.toArray(new CMSCustomer[0]);
  }

  /**
   * To get Cutsomer Messages
   * @return CMSCustomerMessage[]
   * @throws SQLException
   */
  public CMSCustomerMessage[] getAllCustomerMessages () throws SQLException {
    BaseOracleBean[] beans = null;
    String sql = "select * from ARM_CUSTOMER_MESSAGES where STATUS = 'O' ";
    List result = new ArrayList();
    List params = new ArrayList();
    beans = this.query(new ArmCustomerMessagesOracleBean(), sql, params);
    if (beans == null || beans.length == 0) {
      return  new CMSCustomerMessage[0];
    }
    CMSCustomerMessage[] customerMsg = new CMSCustomerMessage[beans.length];
    for (int i = 0; i < beans.length; i++) {
      customerMsg[i] = fromBeanToObject((ArmCustomerMessagesOracleBean)beans[i]);
    }
    return  customerMsg;
  }

  /**
   * put your documentation comment here
   * @param armCustMsgBean
   * @return
   */
  public CMSCustomerMessage fromBeanToObject (ArmCustomerMessagesOracleBean armCustMsgBean) {
    CMSCustomerMessage customerMsg = new CMSCustomerMessage();
    customerMsg.doSetCustomerId(armCustMsgBean.getIdCt());
    customerMsg.doSetCustomerType(armCustMsgBean.getTyCt());
    customerMsg.doSetMessageType(armCustMsgBean.getTyMsg());
    customerMsg.doSetMessage(armCustMsgBean.getMessage());
    customerMsg.doSetResponse(armCustMsgBean.getResponse());
    customerMsg.doSetStatus(armCustMsgBean.getStatus());
    return  customerMsg;
  }

  /**
   * put your documentation comment here
   * @param lastName
   * @param zipCode
   * @return
   * @exception SQLException
   */
  public Customer[] selectByLastNameZipCode (String lastName, String zipCode) throws SQLException {
    /*String sql = "select PA_PRS.ID_PRTY_PRS from PA_PRS, LO_ADS_PRTY, LO_ADS_NSTD " +
     "where PA_PRS.ID_PRTY_PRS = LO_ADS_PRTY.ID_PRTY and LO_ADS_PRTY.ID_ADS = LO_ADS_NSTD.ID_ADS " +
     "and PA_PRS.LN_PRS = ? and LO_ADS_NSTD.PC_NM = ?";
     List params = new ArrayList();
     params.add(lastName);
     params.add(zipCode);
     return this.selectByIds(this.queryForIds(sql, params));
     */
    return  null;
  }

  /**
   * vishal : added for customer search in europe (franchising store requirement) 16 sept 2016
   */
  public Customer[] selectByLastNameFirstName (String lastName, String firstName,boolean isFranchisingStore) throws Exception {
	  String sql;
	  List params = new ArrayList();
	  if (firstName != null && firstName.trim().length() > 0) {
		  sql = "select ID_PRTY_PRS from PA_PRS, PA_PRTY, PA_CT where lower(LN_PRS) like ? and " 
			  + "lower(FN_PRS) like ? and " 
			  + "ID_PRTY_PRS = PA_PRTY.ID_PRTY and PA_PRTY.ID_PRTY = ID_CT and TY_PRTY = ?";
	      params.add(lastName.trim().toLowerCase() + "%");
	      params.add(firstName.trim().toLowerCase() + "%");
	    } else {
	  	//Fix for issue #1886 Search Customer Name
	      sql = "select ID_PRTY_PRS from PA_PRS, PA_PRTY, PA_CT where lower(LN_PRS) like ? and " 
	    	  + "ID_PRTY_PRS = PA_PRTY.ID_PRTY and PA_PRTY.ID_PRTY = ID_CT and TY_PRTY = ?";
	      
//	       sql = "select PA_PRS.ID_PRTY_PRS from (select ID_PRTY_PRS, REPLACE(LN_PRS, ' ','')AS col1,LN_PRS "
//	      	    +"from PA_PRS)PA_PRS,PA_PRTY ,PA_CT where LOWER(COL1) like ? AND PA_PRS.ID_PRTY_PRS = PA_PRTY.ID_PRTY "
//	      	    +"AND PA_PRTY.ID_PRTY = ID_CT AND PA_PRTY.TY_PRTY = ?";
	      	    params.add(lastName.trim().toLowerCase() + "%");	
	      	   
	  }
	  params.add(ArtsConstants.PARTY_ROLE_TYPE_CUSTOMER);	  
	  
	  if(isFranchisingStore){
	    	sql=sql+" and lower(ID_PRTY_PRS) like ?";
	    	params.add("f%");
	    }else{
	    	sql=sql+" and lower(ID_PRTY_PRS) NOT like ?";
	    	params.add("f%");	      	    }
	  if (employeeSearch != null && employeeSearch.equalsIgnoreCase("true")) {
		  sql += " and nvl(PA_CT.TY_CT,'0') != ?";
		  params.add(CustomerSearchString.CUSTOMER_TYPE_EMPLOYEE_CODE);
	  }	  	
	  return  this.selectByIds(this.queryForIds(sql, params));
  }
  //VM: Changed query to use ? instead of explicit concatenation of strings in order to handle strings with apostaphe (Bug #95)
  public Customer[] selectByLastNameFirstName (String lastName, String firstName) throws Exception {
	  String sql;
	  List params = new ArrayList();
	  if (firstName != null && firstName.trim().length() > 0) {
		  sql = "select ID_PRTY_PRS from PA_PRS, PA_PRTY, PA_CT where lower(LN_PRS) like ? and " 
			  + "lower(FN_PRS) like ? and " 
			  + "ID_PRTY_PRS = PA_PRTY.ID_PRTY and PA_PRTY.ID_PRTY = ID_CT and TY_PRTY = ?";
	      params.add(lastName.trim().toLowerCase() + "%");
	      params.add(firstName.trim().toLowerCase() + "%");
	    } else {
	  	//Fix for issue #1886 Search Customer Name
	      sql = "select ID_PRTY_PRS from PA_PRS, PA_PRTY, PA_CT where lower(LN_PRS) like ? and " 
	    	  + "ID_PRTY_PRS = PA_PRTY.ID_PRTY and PA_PRTY.ID_PRTY = ID_CT and TY_PRTY = ?";
	      
//	       sql = "select PA_PRS.ID_PRTY_PRS from (select ID_PRTY_PRS, REPLACE(LN_PRS, ' ','')AS col1,LN_PRS "
//	      	    +"from PA_PRS)PA_PRS,PA_PRTY ,PA_CT where LOWER(COL1) like ? AND PA_PRS.ID_PRTY_PRS = PA_PRTY.ID_PRTY "
//	      	    +"AND PA_PRTY.ID_PRTY = ID_CT AND PA_PRTY.TY_PRTY = ?";
	      	    params.add(lastName.trim().toLowerCase() + "%");	      
	  }
	  params.add(ArtsConstants.PARTY_ROLE_TYPE_CUSTOMER);	  
	  
	  if (employeeSearch != null && employeeSearch.equalsIgnoreCase("true")) {
		  sql += " and nvl(PA_CT.TY_CT,'0') != ?";
		  params.add(CustomerSearchString.CUSTOMER_TYPE_EMPLOYEE_CODE);
	  }	  	
	  return  this.selectByIds(this.queryForIds(sql, params));
  }
  
  /**
   * This query selects the new customers added to table ARM_STG_CUST_CRM.
   */
  public Customer[] selectNewCustomersForStore (String storeId) throws Exception {
	  String sql = null;
	  List params = new ArrayList();
	  if (storeId == null || storeId.trim().length() < 1) {
		  return null;
	  }
	  sql = "SELECT ID_PRTY_PRS FROM PA_PRS, PA_PRTY, PA_CT ,ARM_STG_CUST_CRM WHERE " 
		  + "ID_PRTY_PRS = PA_PRTY.ID_PRTY and PA_PRTY.ID_PRTY = ID_CT AND "+
		  ArmStgCustCrmOracleBean.COL_CUSTOMER_ID +"= ID_CT AND "+
		  ArmStgCustCrmOracleBean.COL_STORE_ID+" = ? AND "+ 
		  ArmStgCustCrmOracleBean.COL_STATUS+ " = ?";
      params.add(storeId.trim());
      params.add(ArmStgCustCrmOracleBean.STATUS_NOT_PROCESSED);
      
      Customer[] customers = this.selectByIds(this.queryForIds(sql, params));	  
	  //Set StoreID for customers.
	   if(customers !=null){
		   for(int iCtr=0; iCtr < customers.length; iCtr++){
			   ((CMSCustomer)customers[iCtr]).setStoreId(storeId);
		   }
	   }   
	   return  customers;
  }
  
  
  
  //This is temporary and dirty hack  for Armani Japan for customer search where no spaces given between firstname 
  // and lastname. This has to be redone: issue #1886 for Japan
  
  public Customer[] selectByLastNameFirstNameWithoutSpace(String lastName, String firstName) throws Exception {
	  String sql;
	  List params = new ArrayList();
	  if (firstName != null && firstName.trim().length() > 0) {
		  sql = "select ID_PRTY_PRS from PA_PRS, PA_PRTY, PA_CT where lower(LN_PRS) like ? and " 
			  + "lower(FN_PRS) like ? and " 
			  + "ID_PRTY_PRS = PA_PRTY.ID_PRTY and PA_PRTY.ID_PRTY = ID_CT and TY_PRTY = ?";
	      params.add(lastName.trim().toLowerCase() + "%");
	      params.add(firstName.trim().toLowerCase() + "%");
	    } else {
	  	//Fix for issue #1886 Search Customer Name
	      //sql = "select ID_PRTY_PRS from PA_PRS, PA_PRTY, PA_CT where lower(LN_PRS) like ? and " 
	    	//  + "ID_PRTY_PRS = PA_PRTY.ID_PRTY and PA_PRTY.ID_PRTY = ID_CT and TY_PRTY = ?";
	      
	       sql = "select PA_PRS.ID_PRTY_PRS from (select ID_PRTY_PRS, REPLACE(LN_PRS, ' ','')AS col1,LN_PRS "
	      	    +"from PA_PRS)PA_PRS,PA_PRTY ,PA_CT where LOWER(COL1) like ? AND PA_PRS.ID_PRTY_PRS = PA_PRTY.ID_PRTY "
	      	    +"AND PA_PRTY.ID_PRTY = ID_CT AND PA_PRTY.TY_PRTY = ?";
	      	    params.add(lastName.trim().toLowerCase() + "%");	      
	  }
	  params.add(ArtsConstants.PARTY_ROLE_TYPE_CUSTOMER);	  
	  
	  if (employeeSearch != null && employeeSearch.equalsIgnoreCase("true")) {
		  sql += " and nvl(PA_CT.TY_CT,'0') != ?";
		  params.add(CustomerSearchString.CUSTOMER_TYPE_EMPLOYEE_CODE);
	  }	
	 // System.out.println("Printing   selectByLastNameFirstNameWithoutSpace   :"+sql +"params     :"+params);
	  return  this.selectByIds(this.queryForIds(sql, params));
  }


  /**
   * Lookup customer using Barcode.
   * @param sBarcode String
   * @throws Exception
   * @return Customer[]
   */
  public Customer[] selectByBarcode(String sBarcode) throws Exception {
    String sSQL = null;
    List params = new ArrayList();
    sSQL = "SELECT " + PaCtOracleBean.COL_ID_PRTY +", "+ PaCtOracleBean.COL_TY_RO_PRTY;
    sSQL += ", " + PaCtOracleBean.COL_CUST_BARCODE;
    sSQL += " FROM " +PaCtOracleBean.TABLE_NAME;
    sSQL += " WHERE " + PaCtOracleBean.COL_TY_RO_PRTY + " = ?";
    params.add(ArtsConstants.PARTY_ROLE_TYPE_CUSTOMER);
    if (sBarcode != null && sBarcode.trim().length() > 0)
    {
      sSQL += " AND UPPER(" + PaCtOracleBean.COL_CUST_BARCODE + ") = ?";
      params.add(sBarcode.trim().toUpperCase());
    }
    return  this.selectByIds(this.queryForIds(sSQL, params));
  }


  /**
   * @deprecated use selectByTelephone(Telephone)
   */
  public Customer[] selectByPhone (String phoneString) throws SQLException {
    return  null;
  }

  /**
   * put your documentation comment here
   * @param telephone
   * @return
   * @exception Exception
   */
  public Customer[] selectByTelephone (Telephone telephone) throws Exception {
    List params = new ArrayList();
    String sql = "";
    sql = "select A.ID_PRTY from LO_ADS_PRTY A, ARM_ADS_PH B";
    if (employeeSearch != null && employeeSearch.equalsIgnoreCase("true")) {
    	sql += ", PA_CT C where "
                 + "B.TL_PH = ? and B.ID_ADS = A.ID_ADS and A.TY_RO_PRTY = ? "
                 + "and C.ID_PRTY = A.ID_PRTY and nvl(C.TY_CT,'0') != ? ";
    } else {
    	sql += " where replace(B.TL_PH,'-','') = ? and B.ID_ADS = A.ID_ADS and A.TY_RO_PRTY = ? ";
    }
    params.add(telephone.getTelephoneNumber());
    params.add(ArtsConstants.PARTY_ROLE_TYPE_CUSTOMER);
    if (employeeSearch != null && employeeSearch.equalsIgnoreCase("true")) {
    	params.add(CustomerSearchString.CUSTOMER_TYPE_EMPLOYEE_CODE);
    }
    if (telephone.getCountryCode() != null && telephone.getCountryCode().length() > 0) {
      sql = sql + "and B.CC_PH = ? ";
      params.add(telephone.getCountryCode());
    }
    if (telephone.getAreaCode() != null && telephone.getAreaCode().length() > 0) {
      sql = sql + "and B.TA_PH = ? ";
      params.add(telephone.getAreaCode());
    }
    return  this.selectByIds(this.queryForIds(sql, params));
  }

  /**
   * put your documentation comment here
   * @param rcId
   * @return
   * @exception Exception
   */
  public Customer[] selectByRewardCard (String rcId) throws Exception {
    List params = new ArrayList();
    String sSQL = "SELECT " + DoCfGfOracleBean.COL_ID_CT + " FROM " + DoCfGfOracleBean.TABLE_NAME + " WHERE " + DoCfGfOracleBean.COL_ID_NMB_SRZ_GF_CF + " = ? AND " + DoCfGfOracleBean.COL_TY_GF_CF + " = ?";
    params.add(rcId);
    params.add("REWARD_CARD");
    return  this.selectByIds(this.queryForIds(sSQL, params));
  }

  /**
   * put your documentation comment here
   * @param custId
   * @return
   */
  public DepositHistory[] getDepositHistory (String custId) {
    BaseOracleBean[] beans = null;
    //String sql = "Select * FROM " + ArmCustDepositHistOracleBean.TABLE_NAME + " WHERE " + ArmCustDepositHistOracleBean.COL_CUST_ID + " = ? AND " + ArmCustDepositHistOracleBean.COL_FL_DELETED + " NOT IN ?";
    //PCR1326 DepositHistory fix for Armani Japan
    String sql = ArmCustDepositHistOracleBean.depositHistorySql;
    List params = new ArrayList();
    params.add(custId);
    params.add(new Boolean(true));
    try {
      beans = this.query(new ArmCustDepositHistOracleBean(), sql, params);
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (beans == null || beans.length == 0) {
      return  new DepositHistory[0];
    }
    DepositHistory[] depositHistory = new DepositHistory[beans.length];
    for (int i = 0; i < beans.length; i++) {
      depositHistory[i] = fromBeanToObject((ArmCustDepositHistOracleBean)beans[i]);
    }
    return  depositHistory;
  }
  /**
   * put your documentation comment here
   * @param custId, storeId
   * @return
   */
  public DepositHistory[] getDepositHistoryForStore (String custId,String storeId) {
    BaseOracleBean[] beans = null;
    //String sql = "Select * FROM " + ArmCustDepositHistOracleBean.TABLE_NAME + " WHERE " + ArmCustDepositHistOracleBean.COL_CUST_ID + " = ? AND " + ArmCustDepositHistOracleBean.COL_FL_DELETED + " NOT IN ?";
    //PCR1326 DepositHistory fix for Armani Japan
    String sql = ArmCustDepositHistOracleBean.depositHistorySqlForStore;
    List params = new ArrayList();
    params.add(custId);
    params.add(storeId);
    params.add(new Boolean(true));
    try {
      beans = this.query(new ArmCustDepositHistOracleBean(), sql, params);
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (beans == null || beans.length == 0) {
      return  new DepositHistory[0];
    }
    DepositHistory[] depositHistory = new DepositHistory[beans.length];
    for (int i = 0; i < beans.length; i++) {
      depositHistory[i] = fromBeanToObject((ArmCustDepositHistOracleBean)beans[i]);
    }
    return  depositHistory;
  }
  /**
   * put your documentation comment here
   * @param custId, storeId
   * @return
   */
  public CreditHistory[] getCreditHistoryForStore (String custId,String storeId) {
    BaseOracleBean[] beans = null;
    String sql = ArmCustCreditHistOracleBean.creditHistorySqlForStore;
    List params = new ArrayList();
    params.add(custId);
    params.add(storeId);

    try {
      beans = this.query(new ArmCustCreditHistOracleBean(), sql, params);
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (beans == null || beans.length == 0) {
      return  new CreditHistory[0];
    }
    CreditHistory[] creditHistory = new CreditHistory[beans.length];
    for (int i = 0; i < beans.length; i++) {
      creditHistory[i] = fromBeanToObject((ArmCustCreditHistOracleBean)beans[i]);
    }
    return  creditHistory;
  }

  public DepositHistory[] getDepositHistory (String custId, String transactionId) {
    BaseOracleBean[] beans = null;
    //String sql = "Select * FROM " + ArmCustDepositHistOracleBean.TABLE_NAME + " WHERE " + ArmCustDepositHistOracleBean.COL_CUST_ID + " = ? and " + ArmCustDepositHistOracleBean.COL_AI_TRN + " = ?";
    String selectSQL = ArmCustDepositHistOracleBean.selectSql;
    List params = new ArrayList();
    params.add(custId);
    params.add(transactionId);
    try {
      beans = this.query(new ArmCustDepositHistOracleBean(), selectSQL +  BaseOracleDAO.where(ArmCustDepositHistOracleBean.COL_CUST_ID, ArmCustDepositHistOracleBean.COL_AI_TRN), params);
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (beans == null || beans.length == 0) {
      return  new DepositHistory[0];
    }
    DepositHistory[] depositHistory = new DepositHistory[beans.length];
    for (int i = 0; i < beans.length; i++) {
      depositHistory[i] = fromBeanToObject((ArmCustDepositHistOracleBean)beans[i]);
    }
    return  depositHistory;
  }

  /**
   * put your documentation comment here
   * @param bean
   * @return
   */
  public DepositHistory fromBeanToObject (ArmCustDepositHistOracleBean bean) {
    DepositHistory depositHist = new DepositHistory();
    depositHist.doSetamount(bean.getAmount());
    depositHist.doSetassoc(bean.getAssoc());
    try {
      depositHist.setCustomer((CMSCustomer)selectById(bean.getCustId()));
    } catch (SQLException e) {
      e.printStackTrace();
    }
    depositHist.doSetStoreID(bean.getIdStrRt());
    depositHist.doSetTransactionDate(bean.getDcTransaction());
    depositHist.doSetTransactionId(bean.getAiTrn());
    depositHist.doSetTransactionType(bean.getTyTrn());
    //PCR1326 DepositHistory fix for Armani Japan
	depositHist.doSetAssocFirstName(bean.getAssocFirstName());
	depositHist.doSetAssocLastName(bean.getAssocLastName());
	depositHist.doSetStoreName(bean.getDeStrRt());
    return  depositHist;
  }
  

  /**
   * put your documentation comment here
   * @param bean
   * @return
   */
  public CreditHistory fromBeanToObject (ArmCustCreditHistOracleBean bean) {
    CreditHistory creditHist = new CreditHistory();
    creditHist.doSetamount(bean.getAmount());
    creditHist.doSetassoc(bean.getAssoc());
    try {
    	creditHist.setCustomer((CMSCustomer)selectById(bean.getCustId()));
    } catch (SQLException e) {
      e.printStackTrace();
    }
    creditHist.doSetStoreID(bean.getIdStrRt());
    creditHist.doSetTransactionDate(bean.getDcTransaction());
    creditHist.doSetTransactionId(bean.getAiTrn());
    creditHist.doSetTransactionType(bean.getTyTrn());
    creditHist.doSetAssocFirstName(bean.getAssocFirstName());
    creditHist.doSetAssocLastName(bean.getAssocLastName());
    creditHist.doSetStoreName(bean.getDeStrRt());
    return  creditHist;
  }


  /**
   * put your documentation comment here
   * @param custSrchString
   * @return
   * @exception Exception
   */
  public Customer[] selectBySearchQuery (CustomerSearchString custSrchString) throws Exception {
    List params = new ArrayList();
    String sQuery = custSrchString.buildQuery(params);
    //System.out.println("Inside CustomerOracleDAO-------->"+sQuery);
    return  this.selectByIds(this.queryForIds(sQuery, params));
  }

  /**
   * vishal : added for customer search in europe (franchising store requirement) 16 sept 2016
   * @param searchStr
   * @return
   * @exception Exception
   */
  public Customer[] selectBySearchQuery (String searchStr,boolean isFranchisingStore) throws Exception {
  	StringTokenizer st = new StringTokenizer(searchStr, "|");
    String lName = "", fName = "", country = "", city = "", zip = "",region = "";

    String sql = "";
    List params = null;
    lName = st.nextToken();
    fName = st.nextToken();
    country = st.nextToken();
    city = st.nextToken();
    zip = st.nextToken();
    region = st.nextToken();
  
    
    
     if (city.trim().equals("") && zip.trim().equals("") && country.trim().equals("")) {
      return  this.selectByLastNameFirstName(lName, fName,isFranchisingStore);
    }
    params = new ArrayList();
    //VM: Changed query to use ? instead of explicit concatenation of strings in order to handle strings with apostaphe (Bug #95)
    sql = "select unique A.ID_PRTY from LO_ADS_PRTY A, LO_ADS_NSTD B, PA_PRS C, PA_CT D where " 
    	+ "lower(C.LN_PRS) like ? and B.ID_ADS = A.ID_ADS " 
    	+ "and C.ID_PRTY_PRS = A.ID_PRTY and A.TY_RO_PRTY = ? "
    	+ "and D.ID_PRTY = A.ID_PRTY ";
    params.add(lName.trim().toLowerCase()+ "%");
    params.add(ArtsConstants.PARTY_ROLE_TYPE_CUSTOMER);    
    if (employeeSearch != null && employeeSearch.equalsIgnoreCase("true")) {
    	sql += "and nvl(D.TY_CT,'0') != ? ";
    	params.add(CustomerSearchString.CUSTOMER_TYPE_EMPLOYEE_CODE);
    }
    if (fName.trim().length() > 0) {
      sql = sql + "and lower(C.FN_PRS) like ? ";
      params.add(fName.trim().toLowerCase() + "%");
    }
    if (city.trim().length() > 0) {
    	sql = sql + "and ( lower(B.NM_UN) like ? ";
    	params.add("%" + city.trim().toLowerCase() + "%");
    	sql = sql + "or lower(B.A1_ADS) like ? ";
    	params.add("%" + city.trim().toLowerCase() + "%");
    	sql = sql + "or lower(B.TE_NM) like ? )";
    	params.add("%" + city.trim().toLowerCase() + "%");
    }
    if (country.trim().length() > 0) {
      sql = sql + "and (B.CO_NM = ? or B.MU_NM = ?) ";
      params.add(country.trim());
      params.add(country.trim());               //deliberately added twice
    }
    if (zip.trim().length() > 0) {
      sql = sql + "and lower(B.PC_NM) like ? ";
      params.add(zip.trim().toLowerCase() + "%");
    }
    if(isFranchisingStore){
    	sql=sql+" and lower(A.ID_PRTY) like ?";
    	params.add("f%");
    }else{
    	sql=sql+" and lower(A.ID_PRTY) NOT like ?";
    	params.add("f%");
    }
     return  this.selectByIds(this.queryForIds(sql, params));
  }
  /**
   * put your documentation comment here
   * @param searchStr
   * @return
   * @exception Exception
   */
  public Customer[] selectBySearchQuery (String searchStr) throws Exception {
  	StringTokenizer st = new StringTokenizer(searchStr, "|");
    String lName = "", fName = "", country = "", city = "", zip = "",region = "";

    String sql = "";
    List params = null;
    lName = st.nextToken();
    fName = st.nextToken();
    country = st.nextToken();
    city = st.nextToken();
    zip = st.nextToken();
    region = st.nextToken();
  
    
    
     if (city.trim().equals("") && zip.trim().equals("") && country.trim().equals("")) {
    	if("JP".equalsIgnoreCase(region)){
    		return this.selectByLastNameFirstNameWithoutSpace(lName, fName);
    	}
      return  this.selectByLastNameFirstName(lName, fName);
    }
    params = new ArrayList();
    //VM: Changed query to use ? instead of explicit concatenation of strings in order to handle strings with apostaphe (Bug #95)
    sql = "select unique A.ID_PRTY from LO_ADS_PRTY A, LO_ADS_NSTD B, PA_PRS C, PA_CT D where " 
    	+ "lower(C.LN_PRS) like ? and B.ID_ADS = A.ID_ADS " 
    	+ "and C.ID_PRTY_PRS = A.ID_PRTY and A.TY_RO_PRTY = ? "
    	+ "and D.ID_PRTY = A.ID_PRTY ";
    params.add(lName.trim().toLowerCase()+ "%");
    params.add(ArtsConstants.PARTY_ROLE_TYPE_CUSTOMER);    
    if (employeeSearch != null && employeeSearch.equalsIgnoreCase("true")) {
    	sql += "and nvl(D.TY_CT,'0') != ? ";
    	params.add(CustomerSearchString.CUSTOMER_TYPE_EMPLOYEE_CODE);
    }
    if (fName.trim().length() > 0) {
      sql = sql + "and lower(C.FN_PRS) like ? ";
      params.add(fName.trim().toLowerCase() + "%");
    }
    if (city.trim().length() > 0) {
    	sql = sql + "and ( lower(B.NM_UN) like ? ";
    	params.add("%" + city.trim().toLowerCase() + "%");
    	sql = sql + "or lower(B.A1_ADS) like ? ";
    	params.add("%" + city.trim().toLowerCase() + "%");
    	sql = sql + "or lower(B.TE_NM) like ? )";
    	params.add("%" + city.trim().toLowerCase() + "%");
    }
    if (country.trim().length() > 0) {
      sql = sql + "and (B.CO_NM = ? or B.MU_NM = ?) ";
      params.add(country.trim());
      params.add(country.trim());               //deliberately added twice
    }
    if (zip.trim().length() > 0) {
      sql = sql + "and lower(B.PC_NM) like ? ";
      params.add(zip.trim().toLowerCase() + "%");
    }
     return  this.selectByIds(this.queryForIds(sql, params));
  }

  /**
   * put your documentation comment here
   * @param depositHistory
   * @return
   * @exception SQLException
   */
  public ParametricStatement getInsertDepositHistorySQL (DepositHistory depositHistory) throws SQLException {
    ArmCustDepositHistOracleBean bean = toArmCtDepositHistBean(depositHistory);
    return  new ParametricStatement(bean.getInsertSql(), bean.toList());
  }

  /**
   * put your documentation comment here
   * @return
   * @exception SQLException
   */
  private String getNextCustomerId () throws SQLException {
    String sql = "select ARM_SEQ_CUSTID.nextVal from dual";
    String[] ids = this.queryForIds(sql, null);
    if (ids == null || ids.length != 1)
      return  null;
    return  ids[0];
  }

  /**
   * put your documentation comment here
   * @param custId
   * @param storeId
   * @return
   * @exception SQLException
   */
  public CustomerSaleSummary[] getCustSaleSummary (String custId, String storeId) throws SQLException {
    ArmCustSaleSummaryDAO dao = new ArmCustSaleSummaryOracleDAO();
    return  dao.selectByCustNStoreId(custId, storeId);
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   */
  private PaPrtyOracleBean toPaPrtyBean (Customer object) {
    PaPrtyOracleBean bean = new PaPrtyOracleBean();
    bean.setIdPrty(object.getId());
    bean.setLuOrgLg(null);
    bean.setTyPrty(ArtsConstants.PARTY_ROLE_TYPE_CUSTOMER);
    bean.setEdCo(object.getPreferredISOCountry());
    bean.setEdLa(object.getPreferredISOLanguage());
    return  bean;
  }

  /**
   * put your documentation comment here
   * @param object
   * @param x
   * @return
   */
  LoEmlAdsOracleBean toLoEmlAdsBean (CMSCustomer object, int x) {
    LoEmlAdsOracleBean bean = null;
    if (x == 1) {
      if (object.getEMail() == null || object.getEMail().trim().length() < 1)
        return  bean;
      bean = new LoEmlAdsOracleBean();
      bean.setIdPrty(object.getId());
      bean.setEmAds(object.getEMail());
      bean.setTyEmlAds("EMAIL_1");
    }
    if (x == 2) {
      if (object.getSecondaryEmail() == null || object.getSecondaryEmail().trim().length() < 1)
        return  bean;
      bean = new LoEmlAdsOracleBean();
      bean.setIdPrty(object.getId());
      bean.setEmAds(object.getSecondaryEmail());
      bean.setTyEmlAds("EMAIL_2");
    }
    return  bean;
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  PaPrsOracleBean toPaPrsBean (CMSCustomer object) throws SQLException {
    PaPrsOracleBean bean = new PaPrsOracleBean();
    bean.setIdPrtyPrs(object.getId());
    bean.setNmPrsSln(object.getTitle());
    bean.setLnPrs(object.getLastName());
    bean.setFnPrs(object.getFirstName());
    bean.setMnPrs(object.getMiddleName());
    bean.setDbFnPrs(object.getDBFirstName());
    bean.setDbLnPrs(object.getDBLastName());
    bean.setDcPrsBrt(object.getDateOfBirth());
    bean.setSuffix(object.getSuffix());
    bean.setTyGndPrs(object.getGender());
    return  bean;
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   */
  PaRoPrtyOracleBean toPaRoPrtyBean (Customer object) {
    PaRoPrtyOracleBean bean = new PaRoPrtyOracleBean();
    bean.setIdPrty(object.getId());
    bean.setTyRoPrty(ArtsConstants.PARTY_ROLE_TYPE_CUSTOMER);
    bean.setDcRoPrtyEf(null);
    bean.setDcRoPrtyEp(null);
    return  bean;
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  LoAdsOracleBean toLoAdsBean (Customer object) throws SQLException {
    LoAdsOracleBean bean = new LoAdsOracleBean();
    bean.setIdAds(this.getNextChelseaId());
    return  bean;
  }

  /**
   * put your documentation comment here
   * @param addressId
   * @param addr
   * @return
   * @exception SQLException
   */
  LoAdsNstdOracleBean toLoAdsNstdBean (String addressId, Address addr) throws SQLException {
    LoAdsNstdOracleBean bean = new LoAdsNstdOracleBean();
    bean.setA1Ads(addr.getAddressLine1());
    bean.setA2Ads(addr.getAddressLine2());
    bean.setCoNm(addr.getCountry());
    bean.setIdAds(addressId);
    bean.setNmUn(addr.getCity());
    if (addr.getZipCodeExtension() != null && addr.getZipCodeExtension().length() > 0) {
        if (addr.getOldZipCode() == null) {
            bean.setPcNm(addr.getZipCode() + "-" + addr.getZipCodeExtension());
        } else if (addr.getOldZipCode().equals(addr.getZipCode())) {
            bean.setPcNm(addr.getZipCode() + "-" + addr.getZipCodeExtension());
        } else {
            bean.setPcNm(addr.getZipCode());
        }
    } else {
      bean.setPcNm(addr.getZipCode());
      addr.doSetZipCodeExtension(null);
    }
    bean.setTeNm(addr.getState());
    bean.setMuNm(addr.getDirectional());
    bean.setAdsFormat(addr.getAddressFormat());
    bean.setFlPrmyAds(addr.isUseAsPrimary());
    return  bean;
  }

  /**
   * put your documentation comment here
   * @param object
   * @param addressId
   * @param addr
   * @return
   */
  LoAdsPrtyOracleBean toLoAdsPrtyBean (CMSCustomer object, String addressId, Address addr) {
    LoAdsPrtyOracleBean bean = new LoAdsPrtyOracleBean();
    bean.setIdAds(addressId);
    bean.setIdPrty(object.getId());
    bean.setTyRoPrty(ArtsConstants.PARTY_ROLE_TYPE_CUSTOMER);
    bean.setDcEf(null);
    bean.setDcEp(null);
    bean.setScPrtyAds(null);
    bean.setTyAds(addr.getAddressType());
    return  bean;
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   */
  private PaCtOracleBean toPaCtBean (CMSCustomer object) {
    PaCtOracleBean bean = new PaCtOracleBean();
    bean.setIdCt(object.getId());
    bean.setIdPrty(object.getId());
    bean.setTyRoPrty(ArtsConstants.PARTY_ROLE_TYPE_CUSTOMER);
    bean.setTyCtLfc(null);
    bean.setMoCtIncmAnn(null);
    bean.setScMrtl(null);
    bean.setNmRlgAfln(null);
    bean.setNmHghEdcLv(null);
    bean.setFlRcMl(object.isReceivingMail());
    bean.setDeComments(object.getComment());
    bean.setScCt(object.getCustomerStatus());
    bean.setTyCt(object.getCustomerType());
    bean.setFlLoyalty(object.isLoyaltyMember());
    bean.setPeVipDisc(object.getVIPDiscount());
    bean.setCdPrivacy(object.getPrivacyCode());
    bean.setFlMail(object.isCanMail());
    bean.setFlCall(object.isCanCall());
    bean.setFlEml(object.isCanEmail());
    bean.setFlSms(object.isCanSMS());
    bean.setCustBarcode(object.getCustomerBC());
    if (object.getCustomerBalance() != null) {
      bean.setCustBalance(object.getCustomerBalance());
    }
     
    //Added for Privacy Mgmt Marketing and Master
    bean.setFlMarketing(object.isPrivacyMarketing());
    bean.setFlMaster(object.isPrivacyMaster());
    //Added by vivek sawant to stored Customer's created date.
    bean.setIssueDate(object.getCustIssueDate());
    
    return  bean;
  }

  /**
   * put your documentation comment here
   * @param object
   * @param addressId
   * @param i
   * @return
   */
  ArmAdsPhOracleBean toArmAdsPhBean (Telephone object, String addressId, int i) {
    ArmAdsPhOracleBean bean = new ArmAdsPhOracleBean();
    bean.setIdAds(addressId);
    if (i == 1)
      bean.setIdPhTyp(object.getTelephoneType().getType());
    if (i == 2)
      bean.setIdPhTyp(object.getTelephoneType().getType());
    if (i == 3)
      bean.setIdPhTyp(object.getTelephoneType().getType());
    bean.setCcPh(object.getCountryCode());
    bean.setTaPh(object.getAreaCode());
    bean.setTlPh(object.getTelephoneNumber());
    bean.setPhExtn(object.getExtension());
    return  bean;
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   */
  ArmCtDtlOracleBean toArmCtDtlBean (CMSCustomer object) {
    ArmCtDtlOracleBean bean = new ArmCtDtlOracleBean();
    bean.setIdCt(object.getId());
    bean.setFiscalCode(object.getFiscalCode());
    bean.setVatNum(object.getVatNumber());
    bean.setFiscalCode(object.getFiscalCode());
    bean.setIdType(object.getIdType());
    bean.setDocNum(object.getDocNumber());
    bean.setPlaceOfIssue(object.getPlaceOfIssue());
    bean.setIssueDt(object.getIssueDate());
    bean.setTyPay(object.getPymtType());
    bean.setIdCmy(object.getCompanyId());
    bean.setCdInterCmy(object.getInterCompanyCode());
    bean.setAcctNum(object.getAccountNum());
    bean.setAge(object.getAge());
    bean.setReferredBy(object.getRefBy());
    bean.setProfession(object.getProfession());
    bean.setEducation(object.getEducation());
    bean.setNotes1(object.getNotes1());
    bean.setNotes2(object.getNotes2());
    bean.setNmPtnr(object.getPtFirstName());
    bean.setNmPtnrFam(object.getPtLastName());
    bean.setBirthPlace(object.getPlaceOfBirth());
    bean.setDcSplEvt(object.getSpecialEventDate());
    bean.setTySplEvt(object.getSpEventType());
    bean.setNmChld(object.getChildName());
    bean.setChldNum(object.getNumOfChildren());
    bean.setCreateOffline(object.getCreateOffline());
    bean.setSupplierPayment(object.getSupplierPymt());
    bean.setBank(object.getBank());
    bean.setCrdtCrdNum1(object.getCreditCardNum1());
    bean.setCrdtCrdTyp1(object.getCreditCardType1());
    bean.setCrdtCrdNum2(object.getCreditCardNum2());
    bean.setCrdtCrdTyp2(object.getCreditCardType2());
    return  bean;
  }

  /**
   * put your documentation comment here
   * @param csa
   * @param custId
   * @return
   * @exception SQLException
   */
  ArmCtAssocOracleBean toArmCtAssocBean (CustomerSalesAssociate csa, String custId) throws SQLException {
    ArmCtAssocOracleBean bean = new ArmCtAssocOracleBean();
    bean.setIdCtAssociate(csa.getCustSalesAssocId());
    bean.setIdCt(custId);
    bean.setIdStrRt(csa.getStoreId());
    bean.setIdAssociate(csa.getAssocId());
    return  bean;
  }

  // Issue # 989
  ArmCtCrdbCrdDtlOracleBean toArmCtCrdbCrdDtlBean (CustomerCreditCard cc, String custId) throws SQLException {
    ArmCtCrdbCrdDtlOracleBean bean = new ArmCtCrdbCrdDtlOracleBean();
    bean.setIdCt(custId);
    bean.setIdStrRt(cc.getStoreId());
  //sonali bean.setIdAcntDbCrCrd(cc.getCreditCardNumber());
    bean.setExpirationDate(cc.getExpDate());
    bean.setZipCode(cc.getBillingZipCode());
    bean.setTyCrd(cc.getCreditCardType());
    //AJB:sonali for Card on file
    bean.setCrdToken(cc.getCardToken());
    bean.setMaskedCardNum(cc.getMaskedCreditCardNum());
    //vivek added for encrypted cc data
    //commenting the ISD code because of US testing issues
//    System.out.println("VALUE SETTING IN ArmCtCrdbCrdDtlOracleBean " + cc.getKey_id());
//    bean.setEncryptedData(cc.getEncryptedCCData());
//    bean.setkKeyId(cc.getKey_id());
//    System.out.println("value in Bean now " +  bean.getEncryptedData());
    //ends
    //Vivek Mishra : Added for setting the Signature response from AJB in Byte Code format
    if(cc.isSignatureValid()){   
        //byte[] sign = Base64.decodeBase64((object.getSignatureAscii()).getBytes());
    	byte[] sign = cc.getSignByteCode();
       //Vivek Mishra : Added logic for compressing the Byte Array before saving it to the BOLB as there is limitation of BLOB that it can't store byte code greater than 4k(4000 Bytes).
        try
        {
          ByteArrayOutputStream byteStream =
            new ByteArrayOutputStream(sign.length);
          try
          {
            GZIPOutputStream zipStream =
              new GZIPOutputStream(byteStream);
            try
            {
              zipStream.write(sign);
            }
            finally
            {
              zipStream.close();
            }
          }
          finally
          {
            byteStream.close();
          }

          byte[] compressedData = byteStream.toByteArray();
         //Setting the compressed byte array here.
          bean.setCustSignature(compressedData);
        
        }catch(Exception e)
        {
        	e.printStackTrace();
        }
    }
	//Ends
    return  bean;
  }

  /**
   * put your documentation comment here
   * @param list
   * @return
   */
  private List toArmCtCrdbCrdDtlBeanList (List list) {
    if (list == null)
      return  null;
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i) != null && list.get(i) instanceof String) {
        list.set(i, (new CaseSensitiveString(list.get(i))));
      }
    }
    return  list;
  }

  // End # 989
  ArmCtCommentsOracleBean toArmCtCommentsBean (CustomerComment cc, String custId) throws SQLException {
    ArmCtCommentsOracleBean bean = new ArmCtCommentsOracleBean();
    bean.setIdCtComment(cc.getCustomerCommentId());
    bean.setIdCt(custId);
    bean.setIdStrRt(cc.getStoreId());
    bean.setIdAssociate(cc.getAssociateId());
    bean.setIdBrn(cc.getBrandId());
    bean.setCreateDt(cc.getDateCommented());
    bean.setComments(cc.getComment());
    return  bean;
  }

  //KeyCustomer bean
  private PaKyCtOracleBean toPaKyCtBean (Customer object) {
    if (!object.isLoyaltyMember()) {
      return  null;
    }
    PaKyCtOracleBean paKyCtOracleBean = new PaKyCtOracleBean();
    paKyCtOracleBean.setIdCt(object.getId());
    return  paKyCtOracleBean;
  }

  //CustomerAffilication bean
  private CoCtafOracleBean toCoCtaf (Customer object) {
    if (!object.isLoyaltyMember()) {
      return  null;
    }
    CoCtafOracleBean coCtafOracleBean = new CoCtafOracleBean();
    coCtafOracleBean.setIdGpId(ArtsConstants.CUSTOMER_GROUP_LOYALTY);
    coCtafOracleBean.setIdCt(object.getId());
    coCtafOracleBean.setFlIdnCtafVrRq(true);
    return  coCtafOracleBean;
  }

  /**
   * put your documentation comment here
   * @param depositHist
   * @return
   * @exception SQLException
   */
  private ArmCustDepositHistOracleBean toArmCtDepositHistBean (DepositHistory depositHist) throws SQLException {
    ArmCustDepositHistOracleBean bean = new ArmCustDepositHistOracleBean();
    bean.setAiTrn(depositHist.getTransactionId());
    bean.setAmount(depositHist.getamount());
    bean.setAssoc(depositHist.getassoc());
    bean.setCustId(depositHist.getCustomer().getId());
    bean.setDcTransaction(depositHist.getTransactionDate());
    bean.setIdStrRt(depositHist.getStoreID());
    bean.setTyTrn(depositHist.getTransactionType());
    bean.setFlDeleted(false);
    return  bean;
  }
  /*
   // For standalone testing only!!!!
   public static void main(String[] s) {
   try {
   CustomerOracleDAO cdao = new CustomerOracleDAO();
   CMSCustomer cust = new CMSCustomer();
   cust.setTitle("Mr");
   cust.setFirstName("FN_email_check");
   cust.setLastName("LN_email_check");
   //CMSCustomer cust = (CMSCustomer)cdao.selectById("119869");
   cust.setEMail("longemail88888888888888888888@sss.com");
   cust.setSecondaryEmail(null);
   if (cust != null) {
   CustomerComment com = new CustomerComment();
   com.setStoreId("051010");
   com.setAssociateId("061001005");
   com.setComment("comment djfdjfadjslk");
   cust.addCustomerComment(com);
   }
   cdao.insert(cust);
   //cdao.update(cust);
   } catch (Exception e) {e.printStackTrace();}
   } */
  /**
   * For inserting custCredit
   * @param <any> creditHistory
   */
  public ParametricStatement getInsertCreditHistorySQL (CreditHistory creditHistory, PaymentTransaction object) throws SQLException {
    ArmCustCreditHistOracleBean bean = fromObjectToBean(creditHistory, object);
    return  new ParametricStatement(bean.getInsertSql(), bean.toList());
  }

  /**
   * put your documentation comment here
   * @param depositHistory
   * @param object
   * @return
   */
  ArmCustCreditHistOracleBean fromObjectToBean (CreditHistory creditHistory, PaymentTransaction object) {
    ArmCustCreditHistOracleBean bean = new ArmCustCreditHistOracleBean();
    bean.setDcTransaction(creditHistory.getTransactionDate());
    bean.setIdStrRt(creditHistory.getStoreID());
    bean.setAiTrn(creditHistory.getTransactionId());
    bean.setTyTrn(creditHistory.getTransactionType());
    bean.setAssoc(creditHistory.getassoc());
    bean.setAmount(creditHistory.getamount());
    CMSCustomer cmsCust = new CMSCustomer();
    if (object instanceof CMSCompositePOSTransaction) {
      cmsCust = (CMSCustomer)((CMSCompositePOSTransaction)object).getCustomer();
    }
    else if (object instanceof CMSMiscCollection) {
      cmsCust = (CMSCustomer)((CMSMiscCollection)object).getCustomer();
    }
    else if (object instanceof CMSMiscPaidOut) {
      cmsCust = (CMSCustomer)((CMSMiscPaidOut)object).getCustomer();
    }
    if (creditHistory.getCustomer() != null && creditHistory.getCustomer().getId() != null && creditHistory.getCustomer().getId().trim().length() > 0) {
      bean.setCustId(creditHistory.getCustomer().getId());
    }
    else
      bean.setCustId(cmsCust.getId());
    return  bean;
  }

  /**
   * put your documentation comment here
   * @param creditHistory
   * @return
   * @exception SQLException
   */
  public ParametricStatement getInsertCreditHistorySQL (CreditHistory creditHistory) throws SQLException {
    ArmCustCreditHistOracleBean bean = toArmCtCreditHistBean(creditHistory);
    return  new ParametricStatement(bean.getInsertSql(), bean.toList());
  }

  /**
   * put your documentation comment here
   * @param creditHist
   * @return
   * @exception SQLException
   */
  private ArmCustCreditHistOracleBean toArmCtCreditHistBean (CreditHistory creditHist) throws SQLException {
    ArmCustCreditHistOracleBean bean = new ArmCustCreditHistOracleBean();
    bean.setAiTrn(creditHist.getTransactionId());
    bean.setAmount(creditHist.getamount());
    bean.setAssoc(creditHist.getassoc());
    Customer customer = creditHist.getCustomer();
    if(customer==null){
    	bean.setCustId(null);
    }else {
    	bean.setCustId(customer.getId());
    }
    bean.setDcTransaction(creditHist.getTransactionDate());
    bean.setIdStrRt(creditHist.getStoreID());
    bean.setTyTrn(creditHist.getTransactionType());
    bean.setFlDeleted(false);
    return  bean;
  }
  
  /**
   * This method is used to get transaction data by employee rule 
   * @param customerId
   * @param countryCode
   * @return Map
   * @throws SQLException
   */
  public Map getTransactionDataByEmpRule(String customerId, String countryCode, String societyCode) 
  		throws SQLException {
	BaseOracleBean[] beans = null;
//	String selectSQL = VArmCustRuleDataOracleBean.selectSql;
	
	StringBuffer selectSQLBuf = new StringBuffer("")
		.append("SELECT ")
		.append(" (Arm_Util_Pkg.CONVERT_TO_CURRENCY(NET_AMOUNT, CURRENCY_TYPE,'0')) NET_AMOUNT,")
		.append(" QUANTITY,PRODUCT_CD,RECORD_TYPE,ID_CT,ED_CO,PRIORITY,CURRENCY_TYPE,SOCIETY_CODE")
		.append(" FROM ")
		.append(" ( ")
		.append(" SELECT ")
		.append(" SUM(Arm_Util_Pkg.CONVERT_TO_NUMBER(NET_AMOUNT, CURRENCY_TYPE)) NET_AMOUNT,")
		.append(" SUM(0) QUANTITY,PRODUCT_CD,RECORD_TYPE,ID_CT,ED_CO,PRIORITY,CURRENCY_TYPE,SOCIETY_CODE")
		.append(" FROM  V_ARM_CUST_RULE_DATA ")
		.append(" WHERE ")
		.append(" RECORD_TYPE = '").append(TOT_RECORD_TYPE).append("'")
		.append(" AND SOCIETY_CODE LIKE '")
		.append(societyCode==null?"":societyCode)
		.append("%'")
		.append(" AND ID_CT = '").append(customerId).append("'")
		.append(" GROUP BY RECORD_TYPE,PRIORITY,PRODUCT_CD,ED_CO,SOCIETY_CODE,ID_CT,CURRENCY_TYPE ")
		.append(" UNION ALL ")
		.append(" SELECT  ")
		.append(" SUM(0)NET_AMOUNT,SUM(QUANTITY),PRODUCT_CD,RECORD_TYPE,ID_CT,ED_CO,PRIORITY,CURRENCY_TYPE,SOCIETY_CODE ")
		.append(" FROM   V_ARM_CUST_RULE_DATA ")
		.append(" WHERE ")
		.append(" RECORD_TYPE = '").append(PRO_RECORD_TYPE).append("'")
		.append(" AND SOCIETY_CODE LIKE '")
		.append(societyCode==null?"":societyCode)
		.append("%'")
		.append(" AND ID_CT = '").append(customerId).append("'")
		.append(" GROUP BY PRODUCT_CD,PRIORITY,RECORD_TYPE,ID_CT,ED_CO,CURRENCY_TYPE,SOCIETY_CODE")
		.append(")"); 
	
	
	List params = new ArrayList();
	Map armCustRuleMap = new HashMap();
	params.add(customerId);
	params.add(countryCode);
	//System.out.println("Executing query: " + selectSQLBuf.toString());
	try {
/*		beans = this.query(new VArmCustRuleDataOracleBean(), selectSQL + 
					BaseOracleDAO.where(VArmCustRuleDataOracleBean.COL_ID_CT, 
					VArmCustRuleDataOracleBean.COL_ED_CO), params);
*/		
		beans = this.query(new VArmCustRuleDataOracleBean(),selectSQLBuf.toString(),null);
	} catch (Exception e) {
		e.printStackTrace();
	}	
	if (beans != null && beans.length > 0) {
		for (int i = 0; i < beans.length; i++) {
			fromBeanToObject(beans[i], armCustRuleMap);
		}
	}
	return armCustRuleMap;
  }
  
  /**
   * New Method : Vishal Yevale : to calculate total purchase amount with valid date 29th May 2017
   * : PCR for Employee budget threshold for multi level discount 29th May 2017 (EUROPE)
   * @param baseBean
   * @param map
   * @throws SQLException
   */
  private void fromBeanToObjectEUR (BaseOracleBean baseBean, Map map) 
  		throws SQLException {
	VArmCustRuleDataOracleBean bean = (VArmCustRuleDataOracleBean) baseBean;
		
	String recordType = bean.getRecordType(); 
	String productCode = bean.getProductCd();
	ArmCurrency netAmount = null;
	Integer newValue = new Integer(bean.getQuantity().intValue());
	Integer quantity;
	Map totalMap = null;
	Map productMap = null;
	int j = 0;
	
	if (recordType != null) {
	   	if (recordType.equals(TOT_RECORD_TYPE)) {
	   		if(map.size()==0){
	   		totalMap = (HashMap)map.get(TOT_RECORD_TYPE);
	   		if(totalMap == null) {
	   			totalMap = new HashMap();
		   		map.put(TOT_RECORD_TYPE, totalMap );
	   		}
	   		
	   		if(bean.getNetAmount() == null ||  "".equals(bean.getNetAmount())){
	   			netAmount = new ArmCurrency(0.0d);
	   		}else {
					netAmount = new ArmCurrency(bean.getNetAmount());				
	   		}
	   		totalMap.put(new Integer(bean.getPriority().intValue()),netAmount);
	   		}else{
	   			
	   			if(bean.getNetAmount() == null ||  "".equals(bean.getNetAmount())){
		   			netAmount = new ArmCurrency(0.0d);
		   		}else {
						netAmount = new ArmCurrency(bean.getNetAmount());				
		   		}
	   			totalMap = (HashMap)map.get(TOT_RECORD_TYPE);
	   			if(totalMap == null) {
		   			totalMap = new HashMap();
			   		map.put(TOT_RECORD_TYPE, totalMap );
		   		}
	   			ArmCurrency totalAmount = (ArmCurrency) totalMap.get(new Integer(bean.getPriority().intValue()));
	   			try {
	   				if(totalAmount==null){
	   					totalMap.put(new Integer(bean.getPriority().intValue()),netAmount);	
	   				}else{
					totalAmount = totalAmount.add(netAmount);
					totalMap.put(new Integer(bean.getPriority().intValue()),totalAmount);
	   				}
				} catch (CurrencyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	   		}
	   	}
	   	if (recordType.equals(PRO_RECORD_TYPE)) {
 		
	   		productMap = (HashMap)map.get(PRO_RECORD_TYPE);
	   		if (productMap == null) {
	   			productMap = new HashMap();
		   		map.put(PRO_RECORD_TYPE, productMap);
	   		}
	   		
	   		quantity = (Integer)productMap.get(productCode);
	   		if (quantity == null) {
	   			quantity = newValue;
	   		} else {
	   			quantity = new Integer(quantity.intValue() + newValue.intValue());
	   		}
	   		productMap.put(productCode, quantity);
	   	}
	}
  }
  
  /**
   * put your documentation comment here
   * @param baseBean
   * @param map
   * @throws SQLException
   */
  private void fromBeanToObject (BaseOracleBean baseBean, Map map) 
  		throws SQLException {
	VArmCustRuleDataOracleBean bean = (VArmCustRuleDataOracleBean) baseBean;
		
	String recordType = bean.getRecordType(); 
	String productCode = bean.getProductCd();
	ArmCurrency netAmount = null;
	Integer newValue = new Integer(bean.getQuantity().intValue());
	Integer quantity;
	Map totalMap = null;
	Map productMap = null;
	int j = 0;
	
	//System.out.println();
	//for(Object obj : baseBean.toList()){
	//	System.out.print(" | " + obj.toString());
	//}
	
	if (recordType != null) {
	   	if (recordType.equals(TOT_RECORD_TYPE)) {
//	 		System.out.println("----------------------------------");
//	  		System.out.println("netAmount: " + netAmount);
//	   		System.out.println("----------------------------------");
	   		totalMap = (HashMap)map.get(TOT_RECORD_TYPE);
	   		if(totalMap == null) {
	   			totalMap = new HashMap();
		   		map.put(TOT_RECORD_TYPE, totalMap );
	   		}
	   		
	   		if(bean.getNetAmount() == null ||  "".equals(bean.getNetAmount())){
	   			netAmount = new ArmCurrency(0.0d);
	   		}else {
					netAmount = new ArmCurrency(bean.getNetAmount());				
	   		}
	  		System.out.println("netAmount: " + netAmount + " | priority = " + bean.getPriority());
	   		totalMap.put(new Integer(bean.getPriority().intValue()),netAmount);
//	   		map.put(TOT_RECORD_TYPE, netAmount);
	   	}
	   	if (recordType.equals(PRO_RECORD_TYPE)) {
//	   		System.out.println("----------------------------------");
//	   		System.out.println("productCode: " + productCode);
//	   		System.out.println("value: " + newValue);
//	   		System.out.println("----------------------------------");
	   		
	   		productMap = (HashMap)map.get(PRO_RECORD_TYPE);
	   		if (productMap == null) {
	   			productMap = new HashMap();
		   		map.put(PRO_RECORD_TYPE, productMap);
	   		}
	   		
	   		quantity = (Integer)productMap.get(productCode);
	   		if (quantity == null) {
	   			quantity = newValue;
	   		} else {
	   			quantity = new Integer(quantity.intValue() + newValue.intValue());
	   		}
	   		productMap.put(productCode, quantity);
//	   		System.out.println("quantity by product code from rule table: " + productMap);
//	   		System.out.println("----------------------------------");
	   	}
	}
  }
  
  /**
   * This method gets customer alert rules
   * @return CMSCustomerAlertRule[]
   * @throws SQLException
   */
  public CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode) throws SQLException {
    BaseOracleBean[] beans = null;
    String selectSQL = ArmEmpAlertRuleOracleBean.selectSql +  where(ArmEmpAlertRuleOracleBean.COL_CD_CO)
    					+ " order by " + ArmEmpAlertRuleOracleBean.COL_PRIORITY;
    List params = new ArrayList();
    params.add(countryCode);
    try {
    	beans = this.query(new ArmEmpAlertRuleOracleBean(), selectSQL, params);
    } catch (Exception e) {
        e.printStackTrace();
    }
    if (beans == null || beans.length == 0) {
      return new CMSCustomerAlertRule[0];
    }
    CMSCustomerAlertRule[] customerAlertRule = new CMSCustomerAlertRule[beans.length];
    for (int i = 0; i < beans.length; i++) {
      customerAlertRule[i] = fromBeanToObject((ArmEmpAlertRuleOracleBean)beans[i]);
    }
    return  customerAlertRule;
  }
  
  /**
   * put your documentation comment here
   * @param armEmpAlertRuleBean
   * @return CMSCustomerAlertRule
   */
  public CMSCustomerAlertRule fromBeanToObject (ArmEmpAlertRuleOracleBean armEmpAlertRuleBean) {
	CMSCustomerAlertRule customerAlertRule = new CMSCustomerAlertRule();
	customerAlertRule.doSetCountryCode(armEmpAlertRuleBean.getCdCo());
	customerAlertRule.doSetRecordType(armEmpAlertRuleBean.getRecordType());
	customerAlertRule.doSetStartDate(armEmpAlertRuleBean.getStartDate());
	customerAlertRule.doSetEndDate(armEmpAlertRuleBean.getEndDate());
	customerAlertRule.doSetProductCode(armEmpAlertRuleBean.getProductCd());
	customerAlertRule.doSetValue(armEmpAlertRuleBean.getValue());
	customerAlertRule.doSetPriority(new Integer(armEmpAlertRuleBean.getPriority().intValue()));
    // The new Code Added by vivek Sawant on 24 Nov 08
	customerAlertRule.setIdBrand(armEmpAlertRuleBean.getIDBrand());
	customerAlertRule.setDsc_level(armEmpAlertRuleBean.getDsc_level());
	// Code Finished by Vivek Sawant.

    return customerAlertRule;
  }
  /**
	 * This method gets customer alert rules overloded method
	 * 
	 * @return CMSCustomerAlertRule[]
	 * @throws SQLException
	 * Auther:Vivek Sawant. 24 Nov 08
	 */
  public CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode, String brandID) throws SQLException {
		BaseOracleBean[] beans = null;
		String selectSQL = "select CD_CO, RECORD_TYPE, START_DATE, END_DATE, PRODUCT_CD, VALUE, PRIORITY,ID_BRAND, DSC_LEVEL from ARM_EMP_ALERT_RULE where ARM_EMP_ALERT_RULE.CD_CO  = ? AND ARM_EMP_ALERT_RULE.ID_BRAND = ? order by ARM_EMP_ALERT_RULE.PRIORITY";
		List params = new ArrayList();
		params.add(countryCode);
		params.add(brandID);
		try {			
			beans = this.query(new ArmEmpAlertRuleOracleBean(), selectSQL,params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (beans == null || beans.length == 0) {
			//added new select query for ALL brand.			
			try {
				List newparam = new ArrayList();
				newparam.add(countryCode);
				newparam.add("ALL");
				selectSQL = "select CD_CO, RECORD_TYPE, START_DATE, END_DATE, PRODUCT_CD, VALUE, PRIORITY,ID_BRAND, DSC_LEVEL from ARM_EMP_ALERT_RULE where ARM_EMP_ALERT_RULE.CD_CO  = ? AND ARM_EMP_ALERT_RULE.ID_BRAND = ?  order by ARM_EMP_ALERT_RULE.PRIORITY";
				beans = this.query(new ArmEmpAlertRuleOracleBean(), selectSQL, newparam);
				
			 } catch (Exception e) {
				e.printStackTrace();
			}
		}
		        CMSCustomerAlertRule[] customerAlertRule = new CMSCustomerAlertRule[beans.length];
				for (int i = 0; i < beans.length; i++) {
					customerAlertRule[i] = fromBeanToObject((ArmEmpAlertRuleOracleBean) beans[i]);				  
				}
				if(beans == null || beans.length == 0){					
					return null;
				 }
				else{					
					return customerAlertRule;
				}
				
			
		
	}
  
  /**
 	 * This method gets customer alert rules overloded method 
 	 * 
 	 * @return CMSCustomerAlertRule[]
 	 * @throws SQLException
 	 * @Auther:vishal
 	 */
   public CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode, String brandID, java.sql.Date businessDate) throws SQLException {
 		BaseOracleBean[] beans = null;
 		String selectSQL = "select CD_CO, RECORD_TYPE, START_DATE, END_DATE, PRODUCT_CD, VALUE, PRIORITY,ID_BRAND, DSC_LEVEL from ARM_EMP_ALERT_RULE where ARM_EMP_ALERT_RULE.CD_CO  = ? AND ARM_EMP_ALERT_RULE.ID_BRAND = ? AND ARM_EMP_ALERT_RULE.START_DATE <= TO_DATE('"+businessDate+"','YYYY-MM-DD') AND ARM_EMP_ALERT_RULE.END_DATE >= TO_DATE('"+businessDate+"','YYYY-MM-DD') order by ARM_EMP_ALERT_RULE.PRIORITY";
 		List params = new ArrayList();
 		params.add(countryCode);
 		params.add(brandID);
 		try {			
 			beans = this.query(new ArmEmpAlertRuleOracleBean(), selectSQL,params);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 		if (beans == null || beans.length == 0) {
 			//added new select query for ALL brand.			
 			try {
 				List newparam = new ArrayList();
 				newparam.add(countryCode);
 				newparam.add("ALL");
 				selectSQL = "select CD_CO, RECORD_TYPE, START_DATE, END_DATE, PRODUCT_CD, VALUE, PRIORITY,ID_BRAND, DSC_LEVEL from ARM_EMP_ALERT_RULE where ARM_EMP_ALERT_RULE.CD_CO  = ? AND ARM_EMP_ALERT_RULE.ID_BRAND = ? AND ARM_EMP_ALERT_RULE.START_DATE <= TO_DATE('"+businessDate+"','YYYY-MM-DD') AND ARM_EMP_ALERT_RULE.END_DATE >= TO_DATE('"+businessDate+"','YYYY-MM-DD') order by ARM_EMP_ALERT_RULE.PRIORITY";
 				beans = this.query(new ArmEmpAlertRuleOracleBean(), selectSQL, newparam);
 				
 			 } catch (Exception e) {
 				e.printStackTrace();
 			}
 		}
 		        CMSCustomerAlertRule[] customerAlertRule = new CMSCustomerAlertRule[beans.length];
 				for (int i = 0; i < beans.length; i++) {
 					customerAlertRule[i] = fromBeanToObject((ArmEmpAlertRuleOracleBean) beans[i]);				  
 				}
 				if(beans == null || beans.length == 0){					
 					return null;
 				 }
 				else{					
 					return customerAlertRule;
 				}
 				
 			
 		
 	}
	/**
	 *  new method created for rnull value passed on discount_level to get all Net amt
	 *  @author vivek.sawant
	 *  */
	private void fromBeanToObjects(BaseOracleBean[] baseBean, Map map)	throws SQLException {
		for (int i = 0; i < baseBean.length; i++) {
			Integer e= new Integer(i);
			VArmCustRuleDataOracleBean bean = (VArmCustRuleDataOracleBean) baseBean[i];
			String recordType = bean.getRecordType();
			String productCode = bean.getProductCd();
			ArmCurrency netAmount = null;
			Integer newValue = new Integer(bean.getQuantity().intValue());
			Integer quantity;
			// new Parameter defined for Employee Rule by Vivek Sawant for 24 Nov 08
			Map totalMap;
			Map productMap;
			Map dscLevelMap;
			Map id_BrandMap;
			String dsc_level = bean.getDsc_level();
			String id_brand = bean.getIDBrand();
			// End of code by Vivek
			int j = 0;
			if (recordType != null) {
				if (recordType.equals(TOT_RECORD_TYPE)) {
					totalMap = (HashMap) map.get(TOT_RECORD_TYPE);
					if (totalMap == null) {
						totalMap = new HashMap();
						map.put(TOT_RECORD_TYPE, totalMap);
					}
					if (bean.getNetAmount() == null	|| "".equals(bean.getNetAmount())) {
						netAmount = new ArmCurrency(0.0d);
					} else {
						netAmount = new ArmCurrency(bean.getNetAmount());

					}
					//System.out.println("netAmount: " + netAmount
						//	+ " | priority = " + bean.getPriority());
					
					totalMap.put(e, netAmount);

					// map.put(TOT_RECORD_TYPE, netAmount);
				}
				if (recordType.equals(PRO_RECORD_TYPE)) {
					productMap = (HashMap) map.get(PRO_RECORD_TYPE);
					if (productMap == null) {
						productMap = new HashMap();
						map.put(PRO_RECORD_TYPE, productMap);
					}

					quantity = (Integer) productMap.get(productCode);
					if (quantity == null) {
						quantity = newValue;
					} else {
						quantity = new Integer(quantity.intValue()
								+ newValue.intValue());
					}
					productMap.put(productCode, quantity);
				}
				if (dsc_level != null) {
					dscLevelMap = (HashMap) map.get(DSC_LEVEL);
					if (dscLevelMap == null) {
						dscLevelMap = new HashMap();
						// map.put(DSC_LEVEL, dscLevelMap);

					}
					dscLevelMap.put(e, dsc_level);
					map.put(DSC_LEVEL, dscLevelMap);
				}
				if (id_brand != null) {
					id_BrandMap = (HashMap) map.get(ID_BRAND);
					if (id_BrandMap == null) {
						id_BrandMap = new HashMap();
					}
					id_BrandMap.put(e, id_brand);
					map.put(ID_BRAND, id_BrandMap);
					}
			}
		}
	}


	/**
	 * This is overloded method is used to get transaction data by employee rule
	 * 
	 * @param customerId
	 * @param countryCode
	 * @return Map
	 * @throws SQLException
	 * Auther:Vivek Sawant 24 Nov 08
	 * method Updated : Vishal Yevale : PCR for Employee budget threshold for multi level discount 29th May 2017 (EUROPE)
	 * 
	 */
	public Map getTransactionDataByEmpRule(String customerId,String countryCode, String societyCode, CMSCustomerAlertRule rules[],String id_brand) throws SQLException {
		BaseOracleBean[] beans = null;

		Date startDate = rules[0].getStartDate();
		Date endDate = rules[0].getEndDate();
		String thresholdValue=rules[0].getValue();
		List params = new ArrayList();
		Map armCustRuleMap = new HashMap();
		societyCode = "%"+societyCode;
		params.add(customerId);
		params.add(id_brand);
		params.add(societyCode);
		String whereSQL = " where "+VArmCustRuleDataOracleBean.COL_ID_CT+"= ? and "+VArmCustRuleDataOracleBean.COL_ID_BRAND+"= ? and "+VArmCustRuleDataOracleBean.COL_SOCIETY_CODE+" like ?";
		try {
				beans = this.query(new VArmCustRuleDataOracleBean(), VArmCustRuleDataOracleBean.selectSql + whereSQL, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	if (beans != null && beans.length > 0) {
		List<VArmCustRuleDataOracleBean> finalBeans = new ArrayList<VArmCustRuleDataOracleBean>();
		List checkBeans = new ArrayList<VArmCustRuleDataOracleBean>();
		for(int i=0;i<beans.length;i++){
			VArmCustRuleDataOracleBean bean=(VArmCustRuleDataOracleBean) beans[i];
			Date beanStartDate =bean.getStartDate();
			Date beanEndDate = bean.getEndDate();
			if(beanEndDate.equals(endDate) && beanStartDate.equals(startDate) && !finalBeans.contains(bean)){
				finalBeans.add(bean);
			}
			if((startDate.before(beanStartDate) && endDate.after(beanStartDate)) || (startDate.before(beanEndDate) && endDate.after(beanEndDate))){
				if(!checkBeans.contains(bean) && !finalBeans.contains(bean) && thresholdValue.equalsIgnoreCase(bean.getValue())){
					checkBeans.add(bean);
					finalBeans.add(bean);
				}
			}
			if(i==beans.length-1 && checkBeans.size()!=0){
				VArmCustRuleDataOracleBean varmCustRuleDataOracleBean = (VArmCustRuleDataOracleBean) checkBeans.get(0);
				startDate = varmCustRuleDataOracleBean.getStartDate();
				endDate = varmCustRuleDataOracleBean.getEndDate();
				i=-1;
				checkBeans.remove(varmCustRuleDataOracleBean);
			}
		}
		for (VArmCustRuleDataOracleBean bean:finalBeans) {
			fromBeanToObjectEUR(bean, armCustRuleMap);
		}
		}		
		return armCustRuleMap;
	}

     /**
	 * @author vivek.sawant 
	 * @param membershipNo
	 * @return CMSVIPMembershipDetail
	 * this is onther method being used for customer to get the values
	 */
    
    public CMSVIPMembershipDetail selectByMembershipNumber (String membershipNo , String brand_ID) throws SQLException {
    	CMSVIPMembershipDetail vipMembershipDetail = new CMSVIPMembershipDetail();
	    String whereSql = where(ArmVipDiscountDetailOracleBean.COL_MEMBERSHIP_NUMBER);
	    CMSVIPMembershipDetail maps = passingToBean(query(new ArmVipDiscountDetailOracleBean(), whereSql, membershipNo));
	               if(maps.getExpiry_dt()!=null && maps!=null){
	            	   String query = "select RECORD_TYPE, MEMBER_NUM, BRAND, DISCOUNT FROM ARM_CUST_DISCOUNTS WHERE MEMBER_NUM = '"+membershipNo +"' AND BRAND = '"+ brand_ID+"'";
	            	   vipMembershipDetail =  passingToArmCustDiscountsBean(query(new ArmCustDiscountsOracleBean(), query, null));
	            	   maps.setDiscount_pct(vipMembershipDetail.getDiscount_pct());
	            	   maps.setBrand(vipMembershipDetail.getBrand());
	              }
	    return maps;
	   
      } 
    private CMSVIPMembershipDetail passingToBean (BaseOracleBean[] beans) throws SQLException {
    	CMSVIPMembershipDetail array = new CMSVIPMembershipDetail();
    	for(int i =0;i<beans.length;i++){
		      array = fromArmVipDiscountDetailOracleBeanToObject(beans[i]);
    	  }
			return array;
	 }
    
    private CMSVIPMembershipDetail passingToArmCustDiscountsBean(BaseOracleBean[] beans) throws SQLException {
    	CMSVIPMembershipDetail array = new CMSVIPMembershipDetail();
    	for(int i =0;i<beans.length;i++){
    		ArmCustDiscountsOracleBean bean = (ArmCustDiscountsOracleBean) beans[i];
    		 array = fromArmCustDiscountsOracleBeanToObject(beans[i]);
    	  }
     return array;
    }
    private CMSVIPMembershipDetail fromArmVipDiscountDetailOracleBeanToObject (BaseOracleBean beans) {
		ArmVipDiscountDetailOracleBean bean = (ArmVipDiscountDetailOracleBean) beans;
		CMSVIPMembershipDetail detail = new CMSVIPMembershipDetail();
		 if(bean!= null){
			detail.setCustomer_id(bean.getCust_id());
			detail.setMembership_id(bean.getMembership_no());
			detail.setExpiry_dt(bean.getExpiryDate());
			 }
		 return detail;
	 }
    private CMSVIPMembershipDetail fromArmCustDiscountsOracleBeanToObject (BaseOracleBean beans) {
    	ArmCustDiscountsOracleBean bean = (ArmCustDiscountsOracleBean) beans;
    	CMSVIPMembershipDetail detail = new CMSVIPMembershipDetail();
		 if(bean!= null){
			detail.setBrand(bean.getBrand());
			detail.setMembership_id(bean.getMembership_num());
			detail.setDiscount_pct(bean.getDiscount());
		 }
		 return detail;
	 }
    
    /**
     * This method return Membership Id depends on Customer ID 
     * @param Customer ID
     * @author vivek.sawant
     * @return String
     */
	public String getVIPMembershipID(String customerID) throws SQLException {
	    String whereSql = where(ArmVipDiscountDetailOracleBean.COL_CUSTOMER_ID);
		CMSVIPMembershipDetail vipMembershipDetail = passingToBean(query(new ArmVipDiscountDetailOracleBean(), whereSql, customerID));		   
		return vipMembershipDetail.getMembership_id();
		} 
	/**
     * This method return date of creation/modify of customer 
     * @param Customer ID
     * @author vivek.sawant
     * @return Date1
     */
	
	public Date getCustomerCreatationDate(String customerID){
		
		String query = "SELECT * FROM PA_CT WHERE ID_CT = '"+customerID+"'";
		BaseOracleBean[] beans = null;
		List param = new ArrayList();
		try {
			beans = this.query(new PaCtOracleBean(), query, null);
		} catch (Exception e) {
		e.printStackTrace();
		}
	    CMSCustomerDate customerDate = new CMSCustomerDate();
		Date date = null;
			customerDate = fromBeanToObject((PaCtOracleBean) beans[0]);		
			date = customerDate.getCreationDate();
		if(beans == null || beans.length == 0){					
			return new Date();
		 }else{
			 if(date!= null)
				return date;
			 else
				return new Date();
			 
		 }
		
		
    }

	/**
     * This method return CMSCustomerDate 
     * @param ArmStgCustOutOracleBean
     * @author vivek.sawant
     * @return CMSCustomerDate
     */
	
	public CMSCustomerDate fromBeanToObject (PaCtOracleBean bean) {
		  CMSCustomerDate customerDate = new CMSCustomerDate();
		  customerDate.setCreationDate(bean.getIssueDate());
	      return customerDate;
	}

	public String[] getCardToken(String customerId) throws Exception {
		
		// TODO Auto-generated method stub
		 String sql = "select CARD_TOKEN from  ARM_CT_CRDB_CRD_DTL where ID_CT = '"+customerId+"'";
		    String[] ids = this.queryForIds(sql, null);
		    /*if (ids == null || ids.length != 1)
		      return  null;*/
		    
		    return  ids;
	}

	
}
