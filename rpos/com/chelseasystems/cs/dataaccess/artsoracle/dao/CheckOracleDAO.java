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
import java.util.Date;
import  java.util.List;
import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cr.payment.BankCheck;
import  com.chelseasystems.cr.payment.BusinessCheck;
import  com.chelseasystems.cr.payment.Check;
import  com.chelseasystems.cr.payment.EmployeeCheck;
import  com.chelseasystems.cr.payment.MoneyOrder;
import  com.chelseasystems.cr.payment.TravellersCheck;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.TrLtmChkTndOracleBean;
import  com.chelseasystems.cs.payment.CMSBankCheck;


/**
 *
 *  Check Data Access Object.<br>
 *  This object encapsulates all database access for Check.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *    <tr><td>AccountNumber</td><td>TR_LTM_CHK_TND</td><td>ID_ACNT_CHK</td></tr>
 *    <tr><td>ActionDate</td><td>TR_LTM_CHK_TND</td><td>ACTION_DATE</td></tr>
 *    <tr><td>ApprovalDate</td><td>TR_LTM_CHK_TND</td><td>APPROVAL_DATE</td></tr>
 *    <tr><td>AuthRequired</td><td>TR_LTM_CHK_TND</td><td>AUTH_REQUIRED</td></tr>
 *    <tr><td>BankNumber</td><td>TR_LTM_CHK_TND</td><td>ID_BK_CHK</td></tr>
 *    <tr><td>CheckMICRdata</td><td>TR_LTM_CHK_TND</td><td>MICR_DATA</td></tr>
 *    <tr><td>CheckNumber</td><td>TR_LTM_CHK_TND</td><td>AI_CHK</td></tr>
 *    <tr><td>CheckNumber</td><td>TR_LTM_CHK_TND</td><td>AI_CHK</td></tr>
 *    <tr><td>DriversLicenseNumber</td><td>TR_LTM_CHK_TND</td><td>DRIVERS_LICENSE_NUMBER</td></tr>
 *    <tr><td>Employee</td><td>TR_LTM_CHK_TND</td><td>EMPLOYEE_ID</td></tr>
 *    <tr><td>IsCheckScannedIn</td><td>TR_LTM_CHK_TND</td><td>IS_CHECK_SCANNED_IN</td></tr>
 *    <tr><td>IsIDScannedIn</td><td>TR_LTM_CHK_TND</td><td>IS_ID_SCANNED_IN</td></tr>
 *    <tr><td>ManualOverride</td><td>TR_LTM_CHK_TND</td><td>RESP_AUTHORIZATION_CODE</td></tr>
 *    <tr><td>MessageIdentifier</td><td>TR_LTM_CHK_TND</td><td>MESSAGE_IDENTIFIER</td></tr>
 *    <tr><td>MessageType</td><td>TR_LTM_CHK_TND</td><td>MESSAGE_TYPE</td></tr>
 *    <tr><td>RespAuthorizationCode</td><td>TR_LTM_CHK_TND</td><td>RESP_AUTHORIZATION_CODE</td></tr>
 *    <tr><td>RespAuthorizationResponseCode</td><td>TR_LTM_CHK_TND</td><td>RESP_AUTH_RESPONSE_CODE</td></tr>
 *    <tr><td>RespHostActionCode</td><td>TR_LTM_CHK_TND</td><td>RESP_HOST_ACTION_CODE</td></tr>
 *    <tr><td>RespId</td><td>TR_LTM_CHK_TND</td><td>RESP_ID</td></tr>
 *    <tr><td>RespStatusCode</td><td>TR_LTM_CHK_TND</td><td>RESP_STATUS_CODE</td></tr>
 *    <tr><td>StateIdCode</td><td>TR_LTM_CHK_TND</td><td>STATE_ID_CODE</td></tr>
 *    <tr><td>TenderType</td><td>TR_LTM_CHK_TND</td><td>TENDER_TYPE</td></tr>
 *    <tr><td>TransitNumber</td><td>TR_LTM_CHK_TND</td><td>TRANSIT_NUMBER</td></tr>
 *
 *  </table>
 *
 *  @see Check
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.TrLtmChkTndOracleBean
 *
 */
public class CheckOracleDAO extends BaseOracleDAO {
  private static EmployeeOracleDAO employeeDAO = new EmployeeOracleDAO();
  private static String selectSql = TrLtmChkTndOracleBean.selectSql;
  private static String insertSql = TrLtmChkTndOracleBean.insertSql;
  private static String updateSql = TrLtmChkTndOracleBean.updateSql + where(TrLtmChkTndOracleBean.COL_AI_TRN, TrLtmChkTndOracleBean.COL_AI_LN_ITM);
  private static String deleteSql = TrLtmChkTndOracleBean.deleteSql + where(TrLtmChkTndOracleBean.COL_AI_TRN);

  /**
   * put your documentation comment here
   * @param transactionId
   * @param sequenceNumber
   * @param paymentTypeId
   * @param object
   * @return 
   * @exception SQLException
   */
  ParametricStatement getInsertSQL (String transactionId, int sequenceNumber, String paymentTypeId, Check object) throws SQLException {
    return  new ParametricStatement(insertSql, fromObjectToBean(transactionId, sequenceNumber, paymentTypeId, object).toList());
  }

  /**
   * put your documentation comment here
   * @param transactionId
   * @param sequenceNumber
   * @param paymentTypeId
   * @param object
   * @return 
   * @exception SQLException
   */
  ParametricStatement getUpdateSQL (String transactionId, int sequenceNumber, String paymentTypeId, Check object) throws SQLException {
    List list = fromObjectToBean(transactionId, sequenceNumber, paymentTypeId, object).toList();
    list.add(transactionId);
    list.add(new Integer(sequenceNumber));
    return  new ParametricStatement(updateSql, list);
  }

  /**
   * put your documentation comment here
   * @param transactionId
   * @return 
   * @exception SQLException
   */
  ParametricStatement getDeleteSQL (String transactionId) throws SQLException {
    ArrayList list = new ArrayList();
    list.add(transactionId);
    return  new ParametricStatement(deleteSql, list);
  }

  /**
   * put your documentation comment here
   * @param transactionId
   * @param sequenceNumber
   * @param txnPaymentName
   * @return 
   * @exception SQLException
   */
  Check getByTransactionIdAndSequenceNumber (String transactionId, int sequenceNumber, String txnPaymentName) throws SQLException {
    String whereSql = where(TrLtmChkTndOracleBean.COL_AI_TRN, TrLtmChkTndOracleBean.COL_AI_LN_ITM);
    ArrayList list = new ArrayList();
    list.add(transactionId);
    list.add(new Integer(sequenceNumber));
    BaseOracleBean[] beans = query(new TrLtmChkTndOracleBean(), whereSql, list);
    if (beans == null || beans.length == 0)
      return  null;
    return  fromBeanToObject(beans[0], txnPaymentName);
  }

  /**
   * put your documentation comment here
   * @return 
   */
  protected BaseOracleBean getDatabeanInstance () {
    return  new TrLtmChkTndOracleBean();
  }

  /**
   * put your documentation comment here
   * @param baseBean
   * @param txnPaymentName
   * @return 
   * @exception SQLException
   */
  private Check fromBeanToObject (BaseOracleBean baseBean, String txnPaymentName) throws SQLException {
    TrLtmChkTndOracleBean bean = (TrLtmChkTndOracleBean)baseBean;
    String type = bean.getLuClsTnd();
    if (type.equals(PAYMENT_TYPE_CHECK))
      return  new Check(txnPaymentName); 
    else if (type.equals(PAYMENT_TYPE_MONEY_ORDER))
      return  new MoneyOrder(txnPaymentName); 
    else if (type.equals(PAYMENT_TYPE_TRAVELLERS_CHECK))
      return  new TravellersCheck(txnPaymentName); 
    else if (type.equals(PAYMENT_TYPE_EMPLOYEE_CHECK)) {
      EmployeeCheck check = new EmployeeCheck(txnPaymentName);
      check.setCheckNumber(bean.getAiChk());
      check.setEmployee(employeeDAO.selectById(bean.getEmployeeId()));
      return  check;
    } 
    else {
      BankCheck object = null;
      //      if (type.equals(PAYMENT_TYPE_BANK_CHECK))
      object = new CMSBankCheck(txnPaymentName);
      //      else if (type.equals(PAYMENT_TYPE_BUSINESS_CHECK))
      //        object = new BusinessCheck(txnPaymentName);
      object.setMessageIdentifier(bean.getMsgIdentifier());
      object.setMessageType(bean.getMsgType());
      object.setTenderType(bean.getTenderType());
      object.setCheckMICRdata(bean.getMicrData());
      object.setAccountNumber(bean.getIdAcntChk());
      object.setCheckNumber(bean.getAiChk());
      object.setBankNumber(bean.getIdBkChk());
      object.setTransitNumber(bean.getTransitNumber());
      object.setIsCheckScannedIn(bean.getCheckScannedIn().booleanValue());
      object.setActionDate(bean.getActionDate());
      object.setStateIdCode(bean.getStateIdCode());
      object.setDriversLicenseNumber(bean.getDrvrLicenseNum());
      object.setIsIDScannedIn(bean.getIsIdScannedIn().booleanValue());
      //object.setRespId(bean.getRespId());
      object.setApprovalDate(bean.getApprovalDate());
      object.setRespHostActionCode(bean.getRspHostActCode());
      object.setRespStatusCode(bean.getRspStatCode());
      object.setRespAuthorizationCode(bean.getRspAuthCode());
      object.setRespAuthorizationResponseCode(bean.getRspAuthRespCode());
      object.setAuthRequired(bean.getAuthRequired().booleanValue());
      //Vivek Mishra : Added for getting unique AJB sequence.
      //Vivek Mishra : Changed to respId from ajbSequence
      ((CMSBankCheck)object).setAjbSequence(bean.getRespId());
      //Ends
      //Vivek Mishra : Added to capture check entry mode.
      ((CMSBankCheck)object).setLuNmbChkSwpKy(bean.getLuNmbChkSwpKy());
      if (bean.getManualOverride().booleanValue())
        object.setManualOverride(bean.getRspAuthCode());
      return  object;
    }
  }

  /**
   * put your documentation comment here
   * @param transactionId
   * @param sequenceNumber
   * @param paymentTypeId
   * @param object
   * @return 
   */
  private TrLtmChkTndOracleBean fromObjectToBean (String transactionId, int sequenceNumber, String paymentTypeId, Check object) {
    TrLtmChkTndOracleBean bean = new TrLtmChkTndOracleBean();
    bean.setAiTrn(transactionId);
    bean.setAiLnItm(sequenceNumber);
    bean.setLuClsTnd(paymentTypeId);
    if (object instanceof EmployeeCheck) {
      EmployeeCheck employeeCheck = (EmployeeCheck)object;
      bean.setAiChk(employeeCheck.getCheckNumber());
      bean.setEmployeeId(employeeCheck.getEmployee().getId());
    } 
    else if (object instanceof BankCheck) {
      BankCheck bankCheck = (BankCheck)object;
      bean.setMsgIdentifier(bankCheck.getMessageIdentifier());
      bean.setMsgType(bankCheck.getMessageType());
      bean.setTenderType(bankCheck.getTenderType());
      bean.setMicrData(bankCheck.getCheckMICRdata());
      bean.setIdAcntChk(bankCheck.getAccountNumber());
      bean.setAiChk(bankCheck.getCheckNumber());
      bean.setIdBkChk(bankCheck.getBankNumber());
      bean.setTransitNumber(bankCheck.getTransitNumber());
      bean.setCheckScannedIn(bankCheck.getIsCheckScannedIn());
      bean.setActionDate(bankCheck.getActionDate());
      bean.setStateIdCode(bankCheck.getStateIdCode());
      bean.setDrvrLicenseNum(bankCheck.getDriversLicenseNumber());
      bean.setIsIdScannedIn(bankCheck.getIsIDScannedIn());
      bean.setRespId(bankCheck.getRespId());
      bean.setApprovalDate(bankCheck.getApprovalDate());
      bean.setRspHostActCode(bankCheck.getRespHostActionCode());
      bean.setRspStatCode(bankCheck.getRespStatusCode());
      bean.setRspStatCodeDesc(bankCheck.getRespStatusCodeDesc());
      bean.setRspAuthCode(bankCheck.getRespAuthorizationCode());
      bean.setRspAuthRespCode(bankCheck.getRespAuthorizationResponseCode());
      bean.setAuthRequired(bankCheck.isAuthRequired());
      bean.setManualOverride(bankCheck.getManualOverride());
      //Vivek Mishra : Added for setting unique AJB Sequence
      
      bean.setRespId(((CMSBankCheck) object).getAjbSequence());
      
      //Anjana added below to save RESP_ID when POS is offline with AJB
      if(((CMSBankCheck) object).getAjbSequence()==null){
     	 String journalKey = null; 
     	 long lastJournalKey = 0;
     	  String str = transactionId;
     	  String[] temp;
     	 
     	  /* delimiter */
     	  String delimiter = "\\*";
     	  /* given string will be split by the argument delimiter provided. */
     	  temp = str.split(delimiter);
     	  /* print substrings */
     	  for(int i =0; i < temp.length ; i++)
     	    System.out.println(temp[i]);
     	 String storeNum = temp[0];
     	 String registerId = temp[1];
     
     	    String timeStampStr = "" + new Date().getTime();
     	    //Store ID of 4 digits
     	    journalKey = getRightJustifiedNumber(storeNum.trim(), 4);
     	    //Register ID of 3 digits
     	    journalKey += getRightJustifiedNumber(registerId.trim(), 3);
     	    //TimeStamp of 9 least significant digits with max precision=10 seconds
     	    journalKey += getRightJustifiedNumber(timeStampStr.substring(0, timeStampStr.length() - 4), 9);
     	    if (Long.parseLong(journalKey) <= lastJournalKey)
     	      journalKey = "" + (lastJournalKey + 1);
     	    lastJournalKey = Long.parseLong(journalKey);
     	    bean.setRespId(journalKey);
      }
      
     //Ends
    //Vivek Mishra : Added to capture check entry mode.
      bean.setLuNmbChkSwpKy(((CMSBankCheck) object).getLuNmbChkSwpKy());
    }
    return  bean;
  }
  
  private static String getRightJustifiedNumber(String orgString, int leng) {
	    String inStr = orgString.trim();
	    int diff = leng - inStr.length();
	    String result = null;
	    if (diff > 0) {
	      String temp = createZeroString(diff);
	      result = temp.concat(inStr);
	    } else if (diff < 0) {
	      //System.err.println("ERROR -- input too large.  Input: >" + inStr + "<   Max. length: " + leng);
	      result = inStr.substring(0 - diff, inStr.length());
	    } else {
	      result = new String(inStr);
	    }
	    return result;
	  }
private static String createZeroString(int size) {
	    char array[] = new char[size];
	    for (int i = 0; i < size; i++)
	      array[i] = '0';
	    String str = new String(array);
	    array = null;
	    return str;
  }
}



