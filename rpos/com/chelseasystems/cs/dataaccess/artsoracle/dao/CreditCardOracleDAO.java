/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import  java.sql.SQLException;
import  java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import  java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cr.payment.Amex;
import  com.chelseasystems.cr.payment.CreditCard;
import  com.chelseasystems.cr.payment.DebitCard;
import  com.chelseasystems.cr.payment.DigitalSignature;
import  com.chelseasystems.cr.payment.Discover;
import  com.chelseasystems.cr.payment.MasterCard;
import  com.chelseasystems.cr.payment.Visa;
import  com.chelseasystems.cs.payment.JCBCreditCard;
import  com.chelseasystems.cs.payment.Diners;
import com.chelseasystems.cs.ajbauthorization.AJBCreditDebitFormatter;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.TrLtmCrdbCrdTnOracleBean;
import com.chelseasystems.cs.database.CMSParametricStatement;


/**
 *
 *  CreditCard Data Access Object.<br>
 *  This object encapsulates all database access for CreditCard.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *    <tr><td>AccountNumber</td><td>TR_LTM_CRDB_CRD_TN</td><td>ID_ACNT_DB_CR_CRD</td></tr>
 *    <tr><td>ApprovalDate</td><td>TR_LTM_CRDB_CRD_TN</td><td>APPROVAL_DATE</td></tr>
 *    <tr><td>AuthRequired</td><td>TR_LTM_CRDB_CRD_TN</td><td>AUTH_REQUIRED</td></tr>
 *    <tr><td>CidNumber</td><td>TR_LTM_CRDB_CRD_TN</td><td>AMEX_CID_NUMBER</td></tr>
 *    <tr><td>CreditCardCompanyName</td><td>TR_LTM_CRDB_CRD_TN</td><td>COMPANY_NAME</td></tr>
 *    <tr><td>CreditCardHolderName</td><td>TR_LTM_CRDB_CRD_TN</td><td>HOLDER_NAME</td></tr>
 *    <tr><td>DigitalSignature</td><td>TR_LTM_CRDB_CRD_TN</td><td>CH_IM_TND_AZN_CT</td></tr>
 *    <tr><td>ExpirationDate</td><td>TR_LTM_CRDB_CRD_TN</td><td>EXPIRATION_DATE</td></tr>
 *    <tr><td>ManualOverride</td><td>TR_LTM_CRDB_CRD_TN</td><td>RESP_AUTHORIZATION</td></tr>
 *    <tr><td>MessageIdentifier</td><td>TR_LTM_CRDB_CRD_TN</td><td>MESSAGE_IDENTIFIER</td></tr>
 *    <tr><td>MessageType</td><td>TR_LTM_CRDB_CRD_TN</td><td>MESSAGE_TYPE</td></tr>
 *    <tr><td>RespAddressVerification</td><td>TR_LTM_CRDB_CRD_TN</td><td>RESP_ADDRESS_VERIF</td></tr>
 *    <tr><td>RespAuthorizationCode</td><td>TR_LTM_CRDB_CRD_TN</td><td>RESP_AUTHORIZATION</td></tr>
 *    <tr><td>RespAuthorizationResponseCode</td><td>TR_LTM_CRDB_CRD_TN</td><td>RESP_AUTH_RESPONSE</td></tr>
 *    <tr><td>RespAuthorizationSource</td><td>TR_LTM_CRDB_CRD_TN</td><td>RESP_AUTH_SOURCE</td></tr>
 *    <tr><td>RespHostActionCode</td><td>TR_LTM_CRDB_CRD_TN</td><td>RESP_HOST_ACTION</td></tr>
 *    <tr><td>RespId</td><td>TR_LTM_CRDB_CRD_TN</td><td>RESP_ID</td></tr>
 *    <tr><td>RespPOSEntryMode</td><td>TR_LTM_CRDB_CRD_TN</td><td>RESP_POS_ENTRY_MOD</td></tr>
 *    <tr><td>RespPaymentServiceIndicator</td><td>TR_LTM_CRDB_CRD_TN</td><td>RESP_PAY_SERVICE</td></tr>
 *    <tr><td>RespStatusCode</td><td>TR_LTM_CRDB_CRD_TN</td><td>RESP_STATUS_CODE</td></tr>
 *    <tr><td>RespTransactionIdentifier</td><td>TR_LTM_CRDB_CRD_TN</td><td>RESP_TRANS_IDFR</td></tr>
 *    <tr><td>RespValidationCode</td><td>TR_LTM_CRDB_CRD_TN</td><td>RESP_VALID_CODE</td></tr>
 *    <tr><td>TenderType</td><td>TR_LTM_CRDB_CRD_TN</td><td>TENDER_TYPE</td></tr>
 *    <tr><td>cardIdentifier</td><td>TR_LTM_CRDB_CRD_TN</td>CARD_IDENTIFIER</td></tr>
 *  </table>
 *
 *  @see CreditCard
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.TrLtmCrdbCrdTnOracleBean
 *
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    |            |           |           | Initial Version                                    |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 06-03-2005 | Megha     | 92        | Changed PAYMENT_TYPE_diners to PAYMENT_TYPE_DINERS |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class CreditCardOracleDAO extends BaseOracleDAO {
  private static final int MAX_DIGITAL_SIGNATURE_LENGTH = 400;                  //number of points
  private static String selectSql = TrLtmCrdbCrdTnOracleBean.selectSql;
  private static String insertSql = TrLtmCrdbCrdTnOracleBean.insertSql;
  private static String updateSql = TrLtmCrdbCrdTnOracleBean.updateSql + where(TrLtmCrdbCrdTnOracleBean.COL_AI_TRN, TrLtmCrdbCrdTnOracleBean.COL_AI_LN_ITM);
  private static String deleteSql = TrLtmCrdbCrdTnOracleBean.deleteSql + where(TrLtmCrdbCrdTnOracleBean.COL_AI_TRN);

  /**
   * put your documentation comment here
   * @param transactionId
   * @param sequenceNumber
   * @param paymentTypeId
   * @param object
   * @return
   * @exception SQLException
   */
  ParametricStatement getInsertSQL (String transactionId, int sequenceNumber, String paymentTypeId, CreditCard object) throws SQLException {
  	//Dated 12/3/07 as per mail and talk with Lorenzo Europe was getting decrypt error because of capital encrypted credit card number
    //return  new ParametricStatement(insertSql, fromObjectToBean(transactionId, sequenceNumber, paymentTypeId, object).toList());
  	return  new CMSParametricStatement(insertSql, fromObjectToBean(transactionId, sequenceNumber, paymentTypeId, object).toList(), true);
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
  ParametricStatement getUpdateSQL (String transactionId, int sequenceNumber, String paymentTypeId, CreditCard object) throws SQLException {
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
  CreditCard getByTransactionIdAndSequenceNumber (String transactionId, int sequenceNumber, String txnPaymentName) throws SQLException {
    String whereSql = where(TrLtmCrdbCrdTnOracleBean.COL_AI_TRN, TrLtmCrdbCrdTnOracleBean.COL_AI_LN_ITM);
    ArrayList list = new ArrayList();
    list.add(transactionId);
    list.add(new Integer(sequenceNumber));
    BaseOracleBean[] beans = query(new TrLtmCrdbCrdTnOracleBean(), whereSql, list);
    if (beans == null || beans.length == 0)
      return  null;
    return  fromBeanToObject(beans[0], txnPaymentName);
  }

  /**
   * put your documentation comment here
   * @return
   */
  protected BaseOracleBean getDatabeanInstance () {
    return  new TrLtmCrdbCrdTnOracleBean();
  }

  /**
   * put your documentation comment here
   * @param baseBean
   * @param txnPaymentName
   * @return
   */
  private CreditCard fromBeanToObject (BaseOracleBean baseBean, String txnPaymentName) {
    TrLtmCrdbCrdTnOracleBean bean = (TrLtmCrdbCrdTnOracleBean)baseBean;
    //Vishal Yeavle : to consider Maestro Card as Master Card : fix 1 feb 2017
    if(bean.getLuClsTnd()!=null && bean.getLuClsTnd().equalsIgnoreCase("MAESTRO")){
    	bean.setLuClsTnd("MASTER_CARD");
    } //end Vishal Yevale : 1 feb 2017
    String type = bean.getLuClsTnd();
    CreditCard object = null;
    if (type.equals(PAYMENT_TYPE_AMEX))
      object = new Amex(txnPaymentName);
    else if (type.equals(PAYMENT_TYPE_DISCOVER))
      object = new Discover(txnPaymentName);
    else if (type.equals(PAYMENT_TYPE_MASTER_CARD))
      object = new MasterCard(txnPaymentName);
    else if (type.equals(PAYMENT_TYPE_VISA))
      object = new Visa(txnPaymentName);
    else if (type.equals(PAYMENT_TYPE_DEBIT_CARD))
      object = new DebitCard(txnPaymentName);
    else if (type.equals(PAYMENT_TYPE_JCB))
      object = new JCBCreditCard(txnPaymentName);
    // Changed PAYMENT_TYPE_diners to PAYMENT_TYPE_DINERS
    else if (type.equals(PAYMENT_TYPE_DINERS))
      object = new Diners(txnPaymentName);
    else if (type.equals(PAYMENT_TYPE_CREDIT_CARD))
      object = new CreditCard(txnPaymentName);
    object.setMessageIdentifier(bean.getMessageIdentifier());
    object.setMessageType(bean.getMessageType());
    object.setTenderType(bean.getTenderType());
    //Commenting the below as we are saving the masked card numbe rina different column and the encrupted one is not needed
    //object.setAccountNumber(bean.getIdAcntDbCrCrd());
    object.setExpirationDate(bean.getExpirationDate());
    object.setTrackNumber(bean.getTrackNumber());
    object.setTrackData(bean.getTrackData());
    object.setRespId(bean.getRespId());
    object.setApprovalDate(bean.getApprovalDate());
    object.setRespHostActionCode(bean.getRespHostAction());
    object.setRespStatusCode(bean.getRespStatusCode());
    object.setRespAuthorizationCode(bean.getRespAuthorization());
    object.setRespAuthorizationResponseCode(bean.getRespAuthResponse());
    object.setRespPOSEntryMode(bean.getRespPosEntryMod());
    object.setRespAuthorizationSource(bean.getRespAuthSource());
    object.setRespAddressVerification(bean.getRespAddressVerif());
    object.setRespPaymentServiceIndicator(bean.getRespPayService());
    object.setRespTransactionIdentifier(bean.getRespTransIdfr());
    object.setRespValidationCode(bean.getRespValidCode());
    object.setCreditCardHolderName(bean.getHolderName());
    object.setCreditCardCompanyName(bean.getCompanyName());
  	// VISHAL YEVALE - 7 SEPT 2016 : ADDED TO PRINT AND SAVE EMV TAG
    object.setAID(bean.getEmvTagAid());
    object.setTVR(bean.getEmvTagTvr());
    object.setTSI(bean.getEmvTagTsi());
    object.setIAD(bean.getEmvTagIad());
    object.setCVM(bean.getEmvTagCvm());
    object.setARC(bean.getEmvTagArc());
  	// end VISHAL YEVALE - 7 SEPT 2016 : ADDED TO PRINT AND SAVE EMV TAG
    if(bean.getPlanCode()!=null)
    object.setCardPlanCode(bean.getPlanCode());
    if (bean.getAuthRequired() != null)
      object.setAuthRequired(bean.getAuthRequired().booleanValue());
    // Added a new method for CardIdentifier
    object.setCardIdentifier(bean.getCardIdentifier());
    //Anjana added for card entry mode
    object.setLuNmbCrdSwpKy(bean.getLuNmbCrdSwpKy());
    if (bean.getManualOverride().booleanValue())
      object.setManualOverride(bean.getRespAuthorization());    /*
     if (object instanceof Amex)
     {
     Amex amex = (Amex)object;
     amex.setCidNumber(bean.getAmexCidNumber());
     }
     */
  //Vivek Mishre : Added comment to make sure that ChImTndAznCt is used for Digital Signature.
    object.setDigitalSignature(new DigitalSignature(bean.getChImTndAznCt()));
    
    //Vivek Mishra : Added for fetching and unzipping the signature data.  
    //Vivek Mishra : Fix added for class cast issue from BLOB to ByteArray 
    //if(!(bean.getSignature().equals(null))){
    //Vivek Mishra : Changed condition for avoiding fatal exception while searching a transaction with null signature.
    if(bean.getChImgTndAznCt()!=null){
    Blob blob = (Blob)bean.getChImgTndAznCt();
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
	object.setSignByteCode(unzip);
    }
    //Ends
    
    //Vivek Mishra : Added for getting token no.
    if(bean.getAccntCardToken()!=null){
    object.setTokenNo(bean.getAccntCardToken());
    }
    //Ends
    
    //Vivek Mishra : Added for getting unique AJB sequence.
    if(bean.getRespId()!=null){
    object.setAjbSequence(bean.getRespId());
    }
    //Ends
    //Vivek Mishra : Added to fix no masked card number on duplicate receipt issue.
    String mskCrdNum = bean.getMaskCardNum();
    if(mskCrdNum!=null){
    object.setMaskCardNum("*********"+mskCrdNum.substring(mskCrdNum.length()-4, mskCrdNum.length()));
    object.setAccountNumber(mskCrdNum);
    }
    //Ends
    return  object;
  }

  /**
   * put your documentation comment here
   * @param transactionId
   * @param sequenceNumber
   * @param paymentTypeId
   * @param object
   * @return
   */
  private TrLtmCrdbCrdTnOracleBean fromObjectToBean (String transactionId, int sequenceNumber, String paymentTypeId, CreditCard object){
    TrLtmCrdbCrdTnOracleBean bean = new TrLtmCrdbCrdTnOracleBean();
    bean.setAiTrn(transactionId);
    bean.setAiLnItm(sequenceNumber);
    bean.setMessageIdentifier(object.getMessageIdentifier());
    //Vivek Mishra : Added to save LU_CLS_TND as CREDIT_CARD for Japan region 16-OCT-2016
    if(transactionId!=null && transactionId.contains("*"))
    {
    	String tempTransactionId = transactionId;
    	tempTransactionId = tempTransactionId.replace("*", ",");
    	String[] txnIdBkd = tempTransactionId.split(",");
    	if(object.getTransactionPaymentName()!=null && object.getTransactionPaymentName().equals(PAYMENT_TYPE_CREDIT_CARD) && txnIdBkd[0].length()>5)
        bean.setLuClsTnd(PAYMENT_TYPE_CREDIT_CARD);	
    	else
    	    bean.setLuClsTnd(paymentTypeId);
    }
    else{
    bean.setLuClsTnd(paymentTypeId);
    }
    //Ends here 16-OCT-2016
    bean.setMessageType(object.getMessageType());
    //ruchi for canada
    if((((CreditCard)object).getAccountNumber().equals("4444333322221111"))&&(paymentTypeId.equalsIgnoreCase("DINERS"))){
    	bean.setTenderType("DINERS");
    }else
    bean.setTenderType(object.getTenderType());
    //    bean.setIdAcntDbCrCrd(putXXXXIntoCreditCardNumber(object.getAccountNumber()));
    //Commenting the below code as we doen want to save account number encrypted 
   // bean.setIdAcntDbCrCrd(object.getAccountNumber());
    bean.setExpirationDate(object.getExpirationDate());
    bean.setTrackNumber(object.getTrackNumber());
    bean.setTrackData(object.getTrackData());
    bean.setRespId(object.getRespId());
    bean.setApprovalDate(object.getApprovalDate());
    bean.setRespHostAction(object.getRespHostActionCode());
    bean.setRespStatusCode(object.getRespStatusCode());
    bean.setRespStatusDesc(object.getRespStatusCodeDesc());
    bean.setRespAuthorization(object.getRespAuthorizationCode());
    bean.setRespAuthResponse(object.getRespAuthorizationResponseCode());
    bean.setRespPosEntryMod(object.getRespPOSEntryMode());
    bean.setRespAuthSource(object.getRespAuthorizationSource());
    bean.setRespAddressVerif(object.getRespAddressVerification());
    bean.setRespPayService(object.getRespPaymentServiceIndicator());
    bean.setRespTransIdfr(object.getRespTransactionIdentifier());
    bean.setRespValidCode(object.getRespValidationCode());
    bean.setHolderName(object.getCreditCardHolderName());
    bean.setCompanyName(object.getCreditCardCompanyName());
    bean.setAuthRequired(object.isAuthRequired());
    bean.setManualOverride(object.getManualOverride());
    // New bean set method for Card Identifier.
    bean.setCardIdentifier(object.getCardIdentifier());
    /*Vivek Mishra : Adding newly created fields for AJB*/
    bean.setMaskCardNum(object.getMaskCardNum());
    //Ends
    //Anjana added for capturing card payment mode like SWIPED/DIPPED?MANUAL
    bean.setLuNmbCrdSwpKy(object.getLuNmbCrdSwpKy());
    //ends
    //Vivek Mishra : Added for setting the Signature response from AJB in Byte Code format
	// VISHAL YEVALE - 7 SEPT 2016 : ADDED TO PRINT AND SAVE EMV TAG
    bean.setEmvTagAid(object.getAID());
    bean.setEmvTagArc(object.getARC());
    bean.setEmvTagCvm(object.getCVM());
    bean.setEmvTagIad(object.getIAD());
    bean.setEmvTagTsi(object.getTSI());
    bean.setEmvTagTvr(object.getTVR());
	//end  VISHAL YEVALE - 7 SEPT 2016 : ADDED TO PRINT AND SAVE EMV TAG
    if(object.isSignatureValid()){   
        //byte[] sign = Base64.decodeBase64((object.getSignatureAscii()).getBytes());
    	byte[] sign = object.getSignByteCode();
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
          bean.setChImgTndAznCt(compressedData);
        
        }catch(Exception e)
        {
        	e.printStackTrace();
        }
    }
	//Ends
        
    //Vivek Mishra : Added for setting token number
    
     bean.setAccntCardToken(object.getTokenNo());
     
    //Ends
     
   //Vivek Mishra : Added for setting unique AJB Sequence
     
     bean.setRespId(object.getAjbSequence());
     
     //Anjana added below to save RESP_ID when POS is offline with AJB
     if(object.getAjbSequence()==null){
    	 String journalKey = null; 
    	 long lastJournalKey = 0;
    	  String str = transactionId;
    	  String[] temp;
    	 
    	  /* delimiter */
    	  String delimiter = "\\*";
    	  /* given string will be split by the argument delimiter provided. */
    	  temp = str.split(delimiter);
    	  if(temp.length>1){
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
    	  }
    	    bean.setRespId(journalKey);
     }
     
    //Ends
     
    /*
     if (object instanceof Amex)
     {
     Amex amex = (Amex)object;
     bean.setAmexCidNumber(amex.getCidNumber());
     }
     */
    //Vivek Mishre : Added comment to make sure that ChImTndAznCt is used for Digital Signature
    if(object.getCardPlanCode()!=null)
      bean.setPlanCode(object.getCardPlanCode());
    bean.setChImTndAznCt(object.getDigitalSignature().toString(MAX_DIGITAL_SIGNATURE_LENGTH));
    return  bean;
  }
  //Anjana added below to save RESP_ID when POS is offline with AJB
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
  //Ends
  /*
   private String putXXXXIntoCreditCardNumber(String creditCardNumber)
   {
   if (creditCardNumber == null || creditCardNumber.length() < 6)
   return creditCardNumber;
   int length = creditCardNumber.length();
   String xxxx = "";
   for (int i = 0 ; i < length - 3 ; i++)
   xxxx += "X";
   return xxxx + creditCardNumber.substring(length - 4);
   }
   */
}



