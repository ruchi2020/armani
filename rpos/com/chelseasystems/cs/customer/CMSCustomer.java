/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//

/* History:-
 +--------+------------+-----------+------------------+------------------------------+
 | Ver#   |    Date    |   By      |        Defect #  |              Description     |
 +--------+------------+-----------+------------------+------------------------------+
 | 37     | 06-02-2011 | Manpreet  | SAP-CRM PCR      | Added StoreID attribute      |
 +--------+------------+-----------+------------------+------------------------------+
 */

package com.chelseasystems.cs.customer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.customer.Customer;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cs.address.Address;


/**
*
* <p>Title: CMSCustomer</p>
*
* <p>Description: </p>
*
* <p>Copyright: Copyright (c) 2005</p>
*
* <p>Company: </p>
*
* @author not attributable
* @version 1.0
*/
public class CMSCustomer extends Customer implements com.chelseasystems.cr.rules.IRuleEngine {
  static final long serialVersionUID = 940707150178031335L;
  // LO_EML_ADS (Email)
  String secEmail;
  Map ytdQtyByProductCD = new HashMap();
  // Address
  List addressList;
  // Credit Card
  List creditCardList;
  // Comments
  List commentList, newCommentList;
  // Sales Associate
  List salesAssocList, newSalesAssocList;
  // PA_PRS (Person)
  String title, middleName, dbFirstName, dbLastName, suffix, gender;
  // PA_CT (Customer)
  String custStatus, custType, privacyCode, custBarCode;
  double vipDiscount;
  boolean canMail, canEmail, canCall, canSMS, canPrivacyMarketing, canPrivacyMaster;
  // Customer Comments
  CustomerComment custComment;
  // ARM_CT_DTL
  String vatNum, fiscalCode, idType, docNum, poi, payType, cmpId, intCmpCode;
  String accNum, age, refBy, prof, edu, partnerLN, partnerFN, pob, spEventType, childName, childNum
      , notes1, notes2, createOffline, supPymt, bank, ccNum1, ccType1, ccNum2, ccType2;
  Date issueDt, spEventDt;
  // flags for customer outbound data persistance
  boolean isModifiedForB, isModifiedForD;
  boolean isUpdateAllStgTbl=false;
  // New field added for customer message
  CMSCustomerMessage cmsCustMsg;
  ArmCurrency custBalance;
  ArmCurrency creditBalance;
  Map ytdNetAmountMap;
    SimpleDateFormat localDateFormat = null;
  Date dateOfBirth;
  boolean isDefaultCustomer;
  //Added by vivek sawant to stored Customer's created date.
  Date custIssueDate;
  String storeId=null;
  
  // vishal : for customer_id prefix in europe for FRANCHISING store
  Boolean Franchising_Store = false;
  Boolean Staging_cust_id =false;
  //end vishal 14 sept 2016
  
  /**
   * Default Constructor
   */
  public CMSCustomer() {
    super();
    addressList = new ArrayList();
    creditCardList = new ArrayList();
    commentList = new ArrayList();
    newCommentList = new ArrayList();
    salesAssocList = new ArrayList();
    newSalesAssocList = new ArrayList();
        localDateFormat = com.chelseasystems.cs.util.DateFormatUtil.
                          getLocalDateFormat();
        localDateFormat.setLenient(false);
    cmsCustMsg = new CMSCustomerMessage();
	custBalance = new ArmCurrency(0.0d);
    creditBalance = new ArmCurrency(0.0d);
    ytdNetAmountMap = new HashMap();
    isDefaultCustomer = false;
    //  Added by vivek sawant to stored Customer's created date.
     custIssueDate = new Date();
  }

  /**
   * Constructor
   * @param id String
   */
  public CMSCustomer(String id) {
    super(id);
    addressList = new ArrayList();
    creditCardList = new ArrayList();
    commentList = new ArrayList();
    newCommentList = new ArrayList();
    salesAssocList = new ArrayList();
    newSalesAssocList = new ArrayList();
    cmsCustMsg = new CMSCustomerMessage();
	localDateFormat = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
	localDateFormat.setLenient(false);
	custBalance = new ArmCurrency(0.0d);
    creditBalance = new ArmCurrency(0.0d);
    ytdNetAmountMap = new HashMap();
    isDefaultCustomer = false;
  }

   // vishal : for customer_id prefix in europe for FRANCHISING store

  public Boolean getFranchising_Store() {
	return Franchising_Store;
}

public void setFranchising_Store(Boolean franchising_Store) {
	Franchising_Store = franchising_Store;
}

public Boolean getStaging_cust_id() {
	return Staging_cust_id;
}

public void setStaging_cust_id(Boolean staging_cust_id) {
	Staging_cust_id = staging_cust_id;
}


// end vishal 14 sept 2016
  /**
   * This method is used to set the customer message
   * @param val String
   */
  public void setCustomerBalance(ArmCurrency custBal) {
    doSetCustomerBalance(custBal);
  }

  /**
   * This method is used to set the customer Balance
   * @param val String
   */
  public void doSetCustomerBalance(ArmCurrency custBal) {
    this.custBalance = custBal;
  }

  /**
   * This method is used to get  the customer balance
   * @return Currency
   */
  public ArmCurrency getCustomerBalance() {
    return this.custBalance;
  }

  // For customerBalance
  /**
   * This method is used to set the Credit Balance
   * @param val String
   */
  public void setCreditBalance(ArmCurrency custBal) {
    doSetCreditBalance(custBal);
  }

  /**
   * This method is used to set the Credit Balance
   * @param val String
   */
  public void doSetCreditBalance(ArmCurrency custBal) {
    this.creditBalance = custBal;
  }

  /**
   * This method is used to get  the Credit balance
   * @return Currency
   */
  public ArmCurrency getCreditBalance() {
    return this.creditBalance;
  }

  // For customer Message
  /**
   * This method is used to set the customer message
   * @param val String
   */
  public void setCustomerMessage(CMSCustomerMessage custMsg)
      throws BusinessRuleException {
    this.checkForNullParameter("setCustomerMessage", custMsg);
    this.executeRule("setCustomerMessage", custMsg);
    if (this.cmsCustMsg == null || !this.cmsCustMsg.equals(custMsg)) {
      this.doSetCustomerMessage(custMsg);
      this.setModified();
    }
  }

  /**
   * This method is used to set the customer message
   * @param val String
   */
  public void doSetCustomerMessage(CMSCustomerMessage custMsg) {
    this.cmsCustMsg = custMsg;
  }

  /**
   * This method is used to get  the customer message
   * @return CMSCustomerMessage
   */
  public CMSCustomerMessage getCustomerMessage() {
    return this.cmsCustMsg;
  }

  /**
   * This method is used to set secondary mail of the customer
   * @param val String
   */
  public void setSecondaryEmail(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setSecondaryEmail", aString);
    String val = aString.trim();
    this.executeRule("setSecondaryEmail", val);
    if (this.secEmail == null || !this.secEmail.equals(val)) {
      this.doSetSecondaryEmail(val);
      this.setModified();
    }
  }

  /**
   * This method is used to set secondary mail of the customer
   * @param val String
   */
  public void doSetSecondaryEmail(String val) {
    this.secEmail = val;
  }

  /**
   * This method is used to get secondary mail of the customer
   * @return String
   */
  public String getSecondaryEmail() {
    return this.secEmail;
  }

  /**
   * This method is used to set the title for the customer
   * @param val String
   */
  public void doSetTitle(String val) {
    this.title = val;
  }

  /**
   * This method is used to set the title for the customer
   * @param val String
   */
  public void setTitle(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setTitle", aString);
    String val = aString.trim();
    this.executeRule("setTitle", val);
    if (this.title == null || !this.title.equals(val)) {
      this.doSetTitle(val);
      this.setModified();
    }
  }

  /**
   * This method is used to get the title for the customer
   * @return String
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * This method is used to set middle name for the customer
   * @param val String
   */
  public void setMiddleName(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setMiddleName", aString);
    String val = aString.trim();
    this.executeRule("setMiddleName", val);
    if (this.middleName == null || !this.middleName.equals(val)) {
      this.doSetMiddleName(val);
      this.setModified();
    }
  }

  /**
   * This method is used to set middle name for the customer
   * @param val String
   */
  public void doSetMiddleName(String val) {
    this.middleName = val;
  }

  /**
   * This method is used to get middle name for the customer
   * @return String
   */
  public String getMiddleName() {
    return this.middleName;
  }

  /**
   * This method is used to set the first name for the customer
   * @param val String
   */
  public void setDBFirstName(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setDBFirstName", aString);
    String val = aString.trim();
    this.executeRule("setDBFirstName", val);
    if (this.dbFirstName == null || !this.dbFirstName.equals(val)) {
      this.doSetDBFirstName(val);
      this.setModified();
    }
  }

  /**
   * This method is used to set the first name for the customer
   * @param val String
   */
  public void doSetDBFirstName(String val) {
    this.dbFirstName = val;
  }

  /**
   * This method is used to get the first name for the customer
   * @return String
   */
  public String getDBFirstName() {
    return this.dbFirstName;
  }

  /**
   * This method is used to set the last name of the customer
   * @param val String
   */
  public void setDBLastName(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setDBLastName", aString);
    String val = aString.trim();
    this.executeRule("setDBLastName", val);
    if (this.dbLastName == null || !this.dbLastName.equals(val)) {
      this.doSetDBLastName(val);
      this.setModified();
    }
  }

  /**
   * This method is used to set the last name of the customer
   * @param val String
   */
  public void doSetDBLastName(String val) {
    this.dbLastName = val;
  }

  /**
   * This method is used to get the last name of the customer
   * @return String
   */
  public String getDBLastName() {
    return this.dbLastName;
  }

  /**
   * This method is used to set suffix for the customer
   * @param val String
   */
  public void setSuffix(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setSuffix", aString);
    String val = aString.trim();
    this.executeRule("setSuffix", val);
    if (this.suffix == null || !this.suffix.equals(val)) {
      this.doSetSuffix(val);
      this.setModified();
    }
  }

  /**
   * This method is used to set suffix for the customer
   * @param val String
   */
  public void doSetSuffix(String val) {
    this.suffix = val;
  }

  /**
   * This method is used to get suffix for the customer
   * @return String
   */
  public String getSuffix() {
    return this.suffix;
  }

  /**
   * This method is used to set gender of the customer
   * @param val String
   */
  public void setGender(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setGender", aString);
    String val = aString.trim();
    this.executeRule("setGender", val);
    if (this.gender == null || !this.gender.equals(val)) {
      this.doSetGender(val);
      this.setModified();
    }
  }

  /**
   * This method is used to set gender of the customer
   * @param val String
   */
  public void doSetGender(String val) {
    this.gender = val;
  }

  /**
   * This method is used to get gender of the customer
   * @return String
   */
  public String getGender() {
    return this.gender;
  }

  /**
   * This method is used to set the customer status
   * @param val String
   */
  public void doSetCustomerStatus(String val) {
    this.custStatus = val;
  }

  /**
   * This method is used to set the customer status
   * @param val String
   */
  public void setCustomerStatus(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setCustomerStatus", aString);
    String val = aString.trim();
    this.executeRule("setCustomerStatus", val);
    if (this.custStatus == null || !this.custStatus.equals(val)) {
      this.doSetCustomerStatus(val);
      this.setModified();
    }
  }

  /**
   * This method is used to get the customer status
   * @return String
   */
  public String getCustomerStatus() {
    return this.custStatus;
  }

  /**
   * This method is used to set the customer type
   * @param val String
   */
  public void doSetCustomerType(String val) {
    this.custType = val;
  }

  /**
   * This method is used to set the customer type
   * @param val String
   */
  public void setCustomerType(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setCustomerType", aString);
    String val = aString.trim();
    this.executeRule("setCustomerType", val);
    if (this.custType == null || !this.custType.equals(val)) {
      this.doSetCustomerType(val);
      this.setModified();
    }
  }

  /**
   * This method is used to get the customer type
   * @return String
   */
  public String getCustomerType() {
    return this.custType;
  }

  /**
   * This method is used to set privacy code for the customer
   * @param val String
   */
  public void doSetPrivacyCode(String val) {
    this.privacyCode = val;
  }

  /**
   * This method is used to set privacy code for the customer
   * @param val String
   */
  public void setPrivacyCode(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setPrivacyCode", aString);
    String val = aString.trim();
    this.executeRule("setPrivacyCode", val);
    if (this.privacyCode == null || !this.privacyCode.equals(val)) {
      this.doSetPrivacyCode(val);
      this.setModified();
    }
  }

  /**
   * This method is used to set privacy code for the customer
   * @return String
   */
  public String getPrivacyCode() {
    return this.privacyCode;
  }

  /**
   *
   * @param val String
   */
  public void setCustomerBC(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setCustomerBC", aString);
    String val = aString.trim();
    this.executeRule("setCustomerBC", val);
    if (this.custBarCode == null || !this.custBarCode.equals(val)) {
      this.doSetCustomerBC(val);
      this.setModified();
    }
  }

  /**
   *
   * @param val String
   */
  public void doSetCustomerBC(String val) {
    this.custBarCode = val;
  }

  /**
   *
   * @return String
   */
  public String getCustomerBC() {
    return this.custBarCode;
  }

  /**
   * This method is used to set the vip discount for the customer
   * @param val double
   */
  public void doSetVIPDiscount(double val) {
    this.vipDiscount = val;
  }

  /**
   * This method is used to set the vip discount for the customer
   * @param val double
   */
  public void setVIPDiscount(double val)
      throws BusinessRuleException {
    this.executeRule("setVIPDiscount");
    if (this.vipDiscount != val) {
      this.doSetVIPDiscount(val);
      this.setModified();
    }
  }

  /**
   * This method is used to get the vip discount for the customer
   * @return double
   */
  public double getVIPDiscount() {
    return this.vipDiscount;
  }

  /**
   *
   * @param val boolean
   */
  public void doSetCanEmail(boolean val) {
    this.canEmail = val;
  }

  /**
   *
   * @param val boolean
   */
  public void setCanEmail(boolean val)
      throws BusinessRuleException {
    this.executeRule("setCanEmail");
    if (this.canEmail != val) {
      this.doSetCanEmail(val);
      this.setModified();
    }
  }

  /**
   *
   * @return boolean
   */
  public boolean isCanEmail() {
    return this.canEmail;
  }

  /**
   *
   * @param val boolean
   */
  public void doSetCanMail(boolean val) {
    this.canMail = val;
  }

  /**
   *
   * @param val boolean
   */
  public void setCanMail(boolean val)
      throws BusinessRuleException {
    this.executeRule("setCanMail");
    if (this.canMail != val) {
      this.doSetCanMail(val);
      this.setModified();
    }
  }

  /**
   *
   * @return boolean
   */
  public boolean isCanMail() {
    return this.canMail;
  }

  /**
   *
   * @param val boolean
   */
  public void doSetCanCall(boolean val) {
    this.canCall = val;
  }

  /**
   *
   * @param val boolean
   */
  public void setCanCall(boolean val)
      throws BusinessRuleException {
    this.executeRule("setCanCall");
    if (this.canCall != val) {
      this.doSetCanCall(val);
      this.setModified();
    }
  }

  /**
   *
   * @return boolean
   */
  public boolean isCanCall() {
    return this.canCall;
  }

  /**
   *
   * @param val boolean
   */
  public void doSetCanSMS(boolean val) {
    this.canSMS = val;
  }

  /**
   *
   * @param val boolean
   */
  public void setCanSMS(boolean val)
      throws BusinessRuleException {
    this.executeRule("setCanSMS");
    if (this.canSMS != val) {
      this.doSetCanSMS(val);
      this.setModified();
    }
  }

  /**
   *
   * @return boolean
   */
  public boolean isCanSMS() {
    return this.canSMS;
  }

  /**
   * This method is used to add address of the customer
   * @param val Address
   */
  public void addAddress(Address val) {
    addressList.add(val);
  }

  /**
   * This methoed iss used to save Customer's FirstName
   * @param aString String
   * @throws BusinessRuleException
   */
  public void setFirstName(String aString)
      throws BusinessRuleException
  {
      String aFirstName = aString.trim();
      if(!getFirstName().equals(aFirstName))
      {
        // First Name is optional in case of Japan
        // but at DB end FirstName cant be NULL
        // so explicitly make it blank - MSB(11/17/05)
        if(aFirstName==null  || aFirstName.length()<1) aFirstName = " ";
          doSetFirstName(aFirstName);
          setModified();
      }
      // If customer is New and Firstname is blank
      else if(aFirstName==null  || aFirstName.length()<1)
        {
          aFirstName = " ";
          doSetFirstName(aFirstName);
        }
  }

  /**
   * This method is used to verify if its a valid first
   * name.
   * @param aString String
   * @throws BusinessRuleException
   */
  public void verifyFirstName(String aString)
      throws BusinessRuleException
  {
      checkForNullParameter("setFirstName", aString);
      String aFirstName = aString.trim();
      executeRule("setFirstName", aFirstName);
  }


  /**
   * This method is used to get address of the customer
   * @return List
   */
  public List getAddresses() {
    return addressList;
  }

  /**
   * This method is used to add Credit Card of the customer
   * @param val Address
   */
  public void addCreditCard(CustomerCreditCard val) {
    creditCardList.add(val);
  }

  /**
   * This method is used to get Credit Card of the customer
   * @return List
   */
  public List getCreditCard() {
    return creditCardList;
  }

  /**
   * This method is used to add customer comments
   * @param val CustomerComment
   */
  public void addCustomerComment(CustomerComment val) {
    this.commentList.add(val);
  }

  /**
   * This method is used to get customer comments
   * @return List
   */
  public List getCustomerComments() {
    return this.commentList;
  }

  /**
   * This method is used to add new customer comments
   * @param val CustomerComment
   */
  public void addNewCustomerComment(CustomerComment val) {
    this.newCommentList.add(val);
  }

  /**
   * This method is used to get new customer comments
   * @return List
   */
  public List getNewCustomerComments() {
    return this.newCommentList;
  }

  /**
   * This method is used to add sales associate with customer
   * @param val CustomerSalesAssociate
   */
  public void addSalesAssociate(CustomerSalesAssociate val) {
    salesAssocList.add(val);
  }

  /**
   * This method is used to get sales associate with customer
   * @return List
   */
  public List getSalesAssociates() {
    return this.salesAssocList;
  }

  /**
   * This method is used to add new sales associate with customer
   * @param val CustomerSalesAssociate
   */
  public void addNewSalesAssociate(CustomerSalesAssociate val) {
    this.newSalesAssocList.add(val);
  }

  /**
   * This method is used to get new sales associate with customer
   * @return List
   */
  public List getNewSalesAssociates() {
    return this.newSalesAssocList;
  }

  /**
   * put your documentation comment here
   * @param newAssocs
   */
  public void setNewSalesAssociates(List newAssocs) {
    this.newSalesAssocList = newAssocs;
  }

  /**
   * This method is used to set the vat number
   * @param val String
   */
  public void doSetVatNumber(String val) {
    this.vatNum = val;
  }

  /**
   * This method is used to set the vat number
   * @param val String
   */
  public void setVatNumber(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setVatNumber", aString);
    String val = aString.trim();
    this.executeRule("setVatNumber", val);
    if (this.vatNum == null || !this.vatNum.equals(val)) {
      this.doSetVatNumber(val);
      this.setModified();
    }
  }

  /**
   * This method is used to get the vat number
   * @return String
   */
  public String getVatNumber() {
    return this.vatNum;
  }

  /**
   * This method is used to set the fiscal code
   * @param val String
   */
  public void doSetFiscalCode(String val) {
    this.fiscalCode = val;
  }

  /**
   * This method is used to set the fiscal code
   * @param val String
   */
  public void setFiscalCode(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setFiscalCode", aString);
    String val = aString.trim();
    this.executeRule("setFiscalCode", val);
    if (this.fiscalCode == null || !this.fiscalCode.equals(val)) {
      this.doSetFiscalCode(val);
      this.setModified();
    }
  }

  /**
   * This method is used to get the fiscal code
   * @return String
   */
  public String getFiscalCode() {
    return this.fiscalCode;
  }

  /**
   *
   * @param val String
   */
  public void doSetIdType(String val) {
    this.idType = val;
  }

  /**
   *
   * @param val String
   */
  public void setIdType(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setIdType", aString);
    String val = aString.trim();
    this.executeRule("setIdType", val);
    if (this.idType == null || !this.idType.equals(val)) {
      this.doSetIdType(val);
      this.setModified();
    }
  }

  /**
   *
   * @return String
   */
  public String getIdType() {
    return this.idType;
  }

  /**
   * This method is used to set the document number for the customer
   * @param val String
   */
  public void doSetDocNumber(String val) {
    this.docNum = val;
  }

  /**
   * This method is used to set the document number for the customer
   * @param val String
   */
  public void setDocNumber(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setDocNumber", aString);
    String val = aString.trim();
    this.executeRule("setDocNumber", val);
    if (this.docNum == null || !this.docNum.equals(val)) {
      this.doSetDocNumber(val);
      this.setModified();
    }
  }

  /**
   * This method is used to get the document number of the customer
   * @return String
   */
  public String getDocNumber() {
    return this.docNum;
  }

  /**
   *
   * @param val String
   */
  public void doSetPlaceOfIssue(String val) {
    this.poi = val;
  }

  /**
   *
   * @param val String
   */
  public void setPlaceOfIssue(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setPlaceOfIssue", aString);
    String val = aString.trim();
    this.executeRule("setPlaceOfIssue", val);
    if (this.poi == null || !this.poi.equals(val)) {
      this.doSetPlaceOfIssue(val);
      this.setModified();
    }
  }

  /**
   *
   * @return String
   */
  public String getPlaceOfIssue() {
    return this.poi;
  }

  /**
   * Changing the doSetIssueDate & setIssuedate
   * @param val Date
   */
  //  public void doSetIssueDate(Date val) {
  //    setIssueDate(val);
  //  }
  //
  //  public void setIssueDate(Date val) {
  //     this.issueDt = val;
  //  }
  /**
   * This method is used to set the issue date
   * @param val Date
   */
  public void doSetIssueDate(Date val) {
    this.issueDt = val;
  }

  public void setIssueDate(Date dtIssue) throws BusinessRuleException
  {
//    checkForNullParameter("setIssueDate", dtIssue);
    if(this.issueDt == null || !issueDt.equals(dtIssue))
    {
      doSetIssueDate(dtIssue);
      setModified();
    }
  }

  public void setDateOfBirth(Date aDate) throws BusinessRuleException {
    if (this.dateOfBirth == null || !dateOfBirth.equals(aDate)) {
      doSetDateOfBirth(aDate);
      setModified();
    }
  }

  public void doSetDateOfBirth(Date aDate) {
    this.dateOfBirth = aDate;
  }

  public Date getDateOfBirth() {
    return this.dateOfBirth;
  }
  /**
   * This method is used to set the issue date
   * @param val String
   * @throws BusinessRuleException
   */
//  public void setIssueDate(String val)
//      throws BusinessRuleException {
//    checkForNullParameter("setIssueDate", val.toString());
//    String aIssueDate = val.toString();
//    executeRule("setIssueDate", aIssueDate);
//    if (issueDt == null|| !issueDt.equals(aIssueDate)) {
//      // For converting the String into Date
//        ResourceBundle res = ResourceManager.getResourceBundle();
//        SimpleDateFormat df = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
//        Date myDate = null;
//        try {
//          if(val!=null && val.length()>0)
//            myDate = df.parse(val);
//        } catch (ParseException e) {
//          System.out.println("Issue Date:: Invalid Date Parser Exception ");
//          e.printStackTrace();
//        }
//        doSetIssueDate(myDate);
//        setModified();
//
//    }
//  }

  /**
   * This method is used to get the issue date
   * @return Date
   */
  public Date getIssueDate() {
    return this.issueDt;
  }

  /**
   * Changed doSetSpecialEventDate & setSpecialEventDate
   * @param val Date
   */
  //  public void doSetSpecialEventDate(Date val) {
  //    setSpecialEventDate(val);
  //  }
  /**
   * This method is used to set special event date of the customer
   * @param val Date
   */
  public void doSetSpecialEventDate(Date val) {
    this.spEventDt = val;
  }

  public void setSpecialEventDate(Date aDate) throws BusinessRuleException {
//    checkForNullParameter("setSpecialEventDate", aDate);
    if (this.spEventDt == null || !spEventDt.equals(aDate)) {
      doSetSpecialEventDate(aDate);
      setModified();
    }
  }

  //  public void setSpecialEventDate(Date val) {
  //    this.spEventDt = val;
  //  }
  /**
   * This method is used to set special event date of the customer
   * @param val String
   * @throws BusinessRuleException
   */
//  public void setSpecialEventDate(String val)
//      throws BusinessRuleException {
//    checkForNullParameter("setSpecialEventDate", val.toString());
//    String aBirthDate = val.toString();
//    executeRule("setSpecialEventDate", aBirthDate);
//    if (spEventDt == null || !spEventDt.equals(aBirthDate)) {
//      // For converting the String into Date
//      ResourceBundle res = ResourceManager.getResourceBundle();
//      SimpleDateFormat df = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
//      Date myDate = null;
//      try {
//        if(val!=null && val.length()>0)
//        myDate = df.parse(val);
//      } catch (ParseException e) {
//        System.out.println("Special Event Date::: Invalid Date Parser Exception ");
//        //e.printStackTrace();
//      }
//      doSetSpecialEventDate(myDate);
//      setModified();
//    }
//  }

  /**
   * This method is used to get special event date of the customer
   * @return Date
   */
  public Date getSpecialEventDate() {
    return this.spEventDt;
  }

  /**
   * This method is used to set payment type of the customer
   * @param val String
   */
  public void doSetPymtType(String val) {
    payType = val;
  }

  /**
   * This method is used to set payment type of the customer
   * @param val String
   */
  public void setPymtType(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setPymtType", aString);
    String val = aString.trim();
    this.executeRule("setPymtType", val);
    if (this.payType == null || !this.payType.equals(val)) {
      this.doSetPymtType(val);
      this.setModified();
    }
  }

  /**
   * This method is used to get payment type of the customer
   * @return String
   */
  public String getPymtType() {
    return this.payType;
  }

  /**
   * This method is used to set company id of the customer
   * @param val String
   */
  public void doSetCompanyId(String val) {
    this.cmpId = val;
  }

  /**
   * This method is used to set company id of the customer
   * @param val String
   */
  public void setCompanyId(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setCompanyId", aString);
    String val = aString.trim();
    this.executeRule("setCompanyId", val);
    if (this.cmpId == null || !this.cmpId.equals(val)) {
      this.doSetCompanyId(val);
      this.setModified();
    }
  }

  /**
   * This method is used to get company id of the customer
   * @return String
   */
  public String getCompanyId() {
    return this.cmpId;
  }

  /**
   * This method is used to set inter company code for customer
   * @param val String
   */
  public void doSetInterCompanyCode(String val) {
    this.intCmpCode = val;
  }

  /**
   * This method is used to set inter company code for customer
   * @param val String
   */
  public void setInterCompanyCode(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setInterCompanyCode", aString);
    String val = aString.trim();
    this.executeRule("setInterCompanyCode", val);
    if (this.intCmpCode == null || !this.intCmpCode.equals(val)) {
      this.doSetInterCompanyCode(val);
      this.setModified();
    }
  }

  /**
   * This method is used to get inter company code for customer
   * @return String
   */
  public String getInterCompanyCode() {
    return this.intCmpCode;
  }

  /**
   * This method is used to set customer account number
   * @param val String
   */
  public void doSetAccountNum(String val) {
    this.accNum = val;
  }

  /**
   * This method is used to set customer account number
   * @param val String
   */
  public void setAccountNum(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setAccountNum", aString);
    String val = aString.trim();
    this.executeRule("setAccountNum", val);
    if (this.accNum == null || !this.accNum.equals(val)) {
      this.doSetAccountNum(val);
      this.setModified();
    }
  }

  /**
   * This method is used to get customer account number
   * @return String
   */
  public String getAccountNum() {
    return this.accNum;
  }

  /**
   * This method is used to set age of customer
   * @param val String
   */
  public void doSetAge(String val) {
    this.age = val;
  }

  /**
   * This method is used to set age of customer
   * @param val String
   */
  public void setAge(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setAge", aString);
    String val = aString.trim();
    this.executeRule("setAge", val);
    if (this.age == null || !this.age.equals(val)) {
      this.doSetAge(val);
      this.setModified();
    }
  }

  /**
   * This method is used to get age of customer
   * @return String
   */
  public String getAge() {
    return this.age;
  }

  /**
   * This method is used to set customer reference by
   * @param val String
   */
  public void doSetRefBy(String val) {
    this.refBy = val;
  }

  /**
   * This method is used to set customer reference by
   * @param val String
   */
  public void setRefBy(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setRefBy", aString);
    String val = aString.trim();
    this.executeRule("setRefBy", val);
    if (this.refBy == null || !this.refBy.equals(val)) {
      this.doSetRefBy(val);
      this.setModified();
    }
  }

  /**
   * This method is used to get customer reference by
   * @return String
   */
  public String getRefBy() {
    return this.refBy;
  }

  /**
   * This method is used to set profession of the customer
   * @param val String
   */
  public void doSetProfession(String val) {
    this.prof = val;
  }

  /**
   * This method is used to set profession of the customer
   * @param val String
   */
  public void setProfession(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setProfession", aString);
    String val = aString.trim();
    this.executeRule("setProfession", val);
    if (this.prof == null || !this.prof.equals(val)) {
      this.doSetProfession(val);
      this.setModified();
    }
  }

  /**
   * This method is used to get profession of the customer
   * @return String
   */
  public String getProfession() {
    return this.prof;
  }

  /**
   * This method is used to set education of the customer
   * @param val String
   */
  public void doSetEducation(String val) {
    this.edu = val;
  }

  /**
   * This method is used to set education of the customer
   * @param val String
   */
  public void setEducation(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setEducation", aString);
    String val = aString.trim();
    this.executeRule("setEducation", val);
    if (this.edu == null || !this.edu.equals(val)) {
      this.doSetEducation(val);
      this.setModified();
    }
  }

  /**
   * This method is used to get education of the customer
   * @return String
   */
  public String getEducation() {
    return this.edu;
  }

  /**
   *
   * @param val String
   */
  public void doSetNotes1(String val) {
    this.notes1 = val;
  }

  /**
   *
   * @param val String
   */
  public void setNotes1(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setNotes1", aString);
    String val = aString.trim();
    this.executeRule("setNotes1", val);
    if (this.notes1 == null || !this.notes1.equals(val)) {
      this.doSetNotes1(val);
      this.setModified();
    }
  }

  /**
   *
   * @return String
   */
  public String getNotes1() {
    return this.notes1;
  }

  /**
   *
   * @param val String
   */
  public void doSetNotes2(String val) {
    this.notes2 = val;
  }

  /**
   *
   * @param val String
   */
  public void setNotes2(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setNotes2", aString);
    String val = aString.trim();
    this.executeRule("setNotes2", val);
    if (this.notes2 == null || !this.notes2.equals(val)) {
      this.doSetNotes2(val);
      this.setModified();
    }
  }

  /**
   *
   * @return String
   */
  public String getNotes2() {
    return this.notes2;
  }

  /**
   *
   * @param val String
   */
  public void doSetPtFirstName(String val) {
    this.partnerFN = val;
  }

  /**
   *
   * @param val String
   */
  public void setPtFirstName(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setPtFirstName", aString);
    String val = aString.trim();
    this.executeRule("setPtFirstName", val);
    if (this.partnerFN == null || !this.partnerFN.equals(val)) {
      this.doSetPtFirstName(val);
      this.setModified();
    }
  }

  /**
   *
   * @return String
   */
  public String getPtFirstName() {
    return this.partnerFN;
  }

  /**
   *
   * @param val String
   */
  public void doSetPtLastName(String val) {
    this.partnerLN = val;
  }

  /**
   *
   * @param val String
   */
  public void setPtLastName(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setPtLastName", aString);
    String val = aString.trim();
    this.executeRule("setPtLastName", val);
    if (this.partnerLN == null || !this.partnerLN.equals(val)) {
      this.doSetPtLastName(val);
      this.setModified();
    }
  }

  /**
   *
   * @return String
   */
  public String getPtLastName() {
    return this.partnerLN;
  }

  /**
   *
   * @param val String
   */
  public void doSetPlaceOfBirth(String val) {
    this.pob = val;
  }

  /**
   *
   * @param val String
   */
  public void setPlaceOfBirth(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setPlaceOfBirth", aString);
    String val = aString.trim();
    this.executeRule("setPlaceOfBirth", val);
    if (this.pob == null || !this.pob.equals(val)) {
      this.doSetPlaceOfBirth(val);
      this.setModified();
    }
  }

  /**
   *
   * @return String
   */
  public String getPlaceOfBirth() {
    return this.pob;
  }

  /**
   *
   * @param val String
   */
  public void doSetSpEventType(String val) {
    this.spEventType = val;
  }

  /**
   *
   * @param val String
   */
  public void setSpEventType(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setSpEventType", aString);
    String val = aString.trim();
    this.executeRule("setSpEventType", val);
    if (this.spEventType == null || !this.spEventType.equals(val)) {
      this.doSetSpEventType(val);
      this.setModified();
    }
  }

  /**
   *
   * @return String
   */
  public String getSpEventType() {
    return this.spEventType;
  }

  /**
   *
   * @param val String
   */
  public void doSetChildName(String val) {
    this.childName = val;
  }

  /**
   *
   * @param val String
   */
  public void setChildName(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setChildName", aString);
    String val = aString.trim();
    this.executeRule("setChildName", val);
    if (this.childName == null || !this.childName.equals(val)) {
      this.doSetChildName(val);
      this.setModified();
    }
  }

  /**
   *
   * @return String
   */
  public String getChildName() {
    return this.childName;
  }

  /**
   *
   * @param val String
   */
  public void doSetNumOfChildren(String val) {
    this.childNum = val;
  }

  /**
   *
   * @param val String
   */
  public void setNumOfChildren(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setNumOfChildren", aString);
    String val = aString.trim();
    this.executeRule("setNumOfChildren", val);
    if (this.childNum == null || !this.childNum.equals(val)) {
      this.doSetNumOfChildren(val);
      this.setModified();
    }
  }

  /**
   *
   * @return String
   */
  public String getNumOfChildren() {
    return this.childNum;
  }

  /**
   *
   * @param val String
   */
  public void doSetCreateOffline(String val) {
    this.createOffline = val;
  }

  /**
   *
   * @param val String
   */
  public void setCreateOffline(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setCreateOffline", aString);
    String val = aString.trim();
    this.executeRule("setCreateOffline", val);
    if (this.createOffline == null || !this.createOffline.equals(val)) {
      this.doSetCreateOffline(val);
      this.setModified();
    }
  }

  /**
   *
   * @return String
   */
  public String getCreateOffline() {
    return this.createOffline;
  }

  /**
   *
   * @param val String
   */
  public void doSetSupplierPymt(String val) {
    this.supPymt = val;
  }

  /**
   *
   * @param val String
   */
  public void setSupplierPymt(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setSupplierPymt", aString);
    String val = aString.trim();
    this.executeRule("setSupplierPymt", val);
    if (this.supPymt == null || !this.supPymt.equals(val)) {
      this.doSetSupplierPymt(val);
      this.setModified();
    }
  }

  /**
   *
   * @return String
   */
  public String getSupplierPymt() {
    return this.supPymt;
  }

  /**
   *
   * @param val String
   */
  public void doSetBank(String val) {
    this.bank = val;
  }

  /**
   *
   * @param val String
   */
  public void setBank(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setBank", aString);
    String val = aString.trim();
    this.executeRule("setBank", val);
    if (this.bank == null || !this.bank.equals(val)) {
      this.doSetBank(val);
      this.setModified();
    }
  }

  /**
   *
   * @return String
   */
  public String getBank() {
    return this.bank;
  }

  /**
   *
   * @param val String
   */
  public void doSetCreditCardNum1(String val) {
    this.ccNum1 = val;
  }

  /**
   *
   * @param val String
   */
  public void setCreditCardNum1(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setCreditCardNum1", aString);
    String val = aString.trim();
    this.executeRule("setCreditCardNum1", val);
    if (this.ccNum1 == null || !this.ccNum1.equals(val)) {
      this.doSetCreditCardNum1(val);
      this.setModified();
    }
  }

  /**
   *
   * @return String
   */
  public String getCreditCardNum1() {
    return this.ccNum1;
  }

  /**
   *
   * @param val String
   */
  public void doSetCreditCardType1(String val) {
    this.ccType1 = val;
  }

  /**
   *
   * @param val String
   */
  public void setCreditCardType1(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setCreditCardType1", aString);
    String val = aString.trim();
    this.executeRule("setCreditCardType1", val);
    if (this.ccType1 == null || !this.ccType1.equals(val)) {
      this.doSetCreditCardType1(val);
      this.setModified();
    }
  }

  /**
   *
   * @return String
   */
  public String getCreditCardType1() {
    return this.ccType1;
  }

  /**
   *
   * @param val String
   */
  public void doSetCreditCardNum2(String val) {
    this.ccNum2 = val;
  }

  /**
   *
   * @param val String
   */
  public void setCreditCardNum2(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setCreditCardNum2", aString);
    String val = aString.trim();
    this.executeRule("setCreditCardNum2", val);
    if (this.ccNum2 == null || !this.ccNum2.equals(val)) {
      this.doSetCreditCardNum2(val);
      this.setModified();
    }
  }

  /**
   *
   * @return String
   */
  public String getCreditCardNum2() {
    return this.ccNum2;
  }

  /**
   *
   * @param val String
   */
  public void doSetCreditCardType2(String val) {
    this.ccType2 = val;
  }

  /**
   *
   * @param val String
   */
  public void setCreditCardType2(String aString)
      throws BusinessRuleException {
    this.checkForNullParameter("setCreditCardType2", aString);
    String val = aString.trim();
    this.executeRule("setCreditCardType2", val);
    if (this.ccType2 == null || !this.ccType2.equals(val)) {
      this.doSetCreditCardType2(val);
      this.setModified();
    }
  }

  /**
   *
   * @return String
   */
  public String getCreditCardType2() {
    return this.ccType2;
  }

  /**
   *
   * @param val boolean
   */
  public void setIsModifiedForB(boolean val)
      throws BusinessRuleException {
    this.executeRule("setIsModifiedForB");
    if (this.isModifiedForB != val) {
      this.isModifiedForB = val;
      this.setModified();
    }
  }

  /**
   *
   * @return boolean
   */
  public boolean isModifiedForB() {
    return this.isModifiedForB;
  }

  /**
   *
   * @param val boolean
   */
  public void setIsModifiedForD(boolean val)
      throws BusinessRuleException {
    this.executeRule("setIsModifiedForD");
    if (this.isModifiedForD != val) {
      this.isModifiedForD = val;
      this.setModified();
    }
  }

  /**
   *
   * @return boolean
   */
  public boolean isModifiedForD() {
    return this.isModifiedForD;
  }

  /**
   * This method is used to get accociate for a given store
   * @param sStoreID String
   * @return String
   */
  public String getAssociateForStore(String sStoreID) {
    if (sStoreID == null || sStoreID.length() < 1) {
      return null;
    }
    int iCtr = 0;
    for (iCtr = 0; iCtr < salesAssocList.size(); iCtr++) {
      CustomerSalesAssociate defaultAssc = (CustomerSalesAssociate)salesAssocList.get(iCtr);
      if (defaultAssc.getStoreId().equals(sStoreID)) {
        return defaultAssc.getAssocId();
      }
    }
    return null;
  }

  /**
   * This method is used to set accociate for a given store
   * @param sStoreID String
   * @param sAssociateID String
   */
  public void setAssociateForStore(String sStoreID, String sAssociateID) {
    if (sStoreID == null || sStoreID.length() < 1 || sAssociateID == null
        || sAssociateID.length() < 1) {
      return;
    }
    int iCtr = 0;
    CustomerSalesAssociate defaultAssc = null;
    for (iCtr = 0; iCtr < salesAssocList.size(); iCtr++) {
      defaultAssc = (CustomerSalesAssociate)salesAssocList.get(iCtr);
      if (defaultAssc.getStoreId().equals(sStoreID)) {
        defaultAssc.setIsModified(true);
        defaultAssc.setAssocId(sAssociateID);
        return;
      }
    }
    for (iCtr = 0; iCtr < newSalesAssocList.size(); iCtr++) {
      CustomerSalesAssociate defaultAssc1 = (CustomerSalesAssociate)newSalesAssocList.get(iCtr);
      if (defaultAssc1.getStoreId().equals(sStoreID)) {
        // Associate for store already exists in list .. have to replace
        defaultAssc1.setAssocId(sAssociateID);
        return;
      }
    }
    // Have to add to create a new associate
    CustomerSalesAssociate defaultAssc2 = new CustomerSalesAssociate();
    defaultAssc2.setStoreId(sStoreID);
    defaultAssc2.setAssocId(sAssociateID);
    newSalesAssocList.add(defaultAssc2);
  }

  /**
   * This method is used to get primary address of the customer
   * @return Address
   */
  public Address getPrimaryAddress() {
    for (int i = 0; i < addressList.size(); i++) {
      Address address = (Address)addressList.get(i);
      if (address.isUseAsPrimary()) {
        return address;
      }
    }
    return null;
  }

  /**
   * put your documentation comment here
   * @param anObject
   * @return
   */
  public boolean equals(Object anObject) {
    if (anObject instanceof CMSCustomer
        && this.getId().trim().equals(((CMSCustomer)anObject).getId().trim())) {
      return true;
    }
    //else
    return false;
  }

  public boolean isVIPCustomer() {
    ConfigMgr config = new ConfigMgr("customer.cfg");
    String sVIPTypes = config.getString("VIP_TYPES");
    if (sVIPTypes == null || sVIPTypes.length() < 1)return false;
    if(getCustomerType() == null || getCustomerType().length()<1) return false;
    if (sVIPTypes.indexOf(",") == -1)
      return getCustomerType().toUpperCase().equals(sVIPTypes.toUpperCase());
    StringTokenizer sTokens = new StringTokenizer(sVIPTypes, ",");
    if (sTokens == null)return false;
    while (sTokens.hasMoreElements()) {
      String sTmp = sTokens.nextToken();
      if (sTmp == null || sTmp.length() < 1)continue;
      if (getCustomerType().toUpperCase().equals(sTmp.toUpperCase()))return true;
    }
    return false;
  }

  /**
   *
   * @param sStoreId String
   * @return boolean
   */
  public boolean testIfAddressViewable(String sStoreId) {
    if (!isVIPCustomer())return true;

    if ( (salesAssocList == null || salesAssocList.size() < 1) &&
        (newSalesAssocList == null || newSalesAssocList.size() < 1))return false;
    for (int iCtr = 0; iCtr < salesAssocList.size(); iCtr++) {
      CustomerSalesAssociate assoc = (CustomerSalesAssociate) salesAssocList.
          get(iCtr);
      if (assoc.getStoreId().equals(sStoreId))return true;
    }
    for (int iCtr = 0; iCtr < newSalesAssocList.size(); iCtr++) {
      CustomerSalesAssociate assoc = (CustomerSalesAssociate) newSalesAssocList.
          get(iCtr);
      if (assoc.getStoreId().equals(sStoreId))return true;
    }
    return false;
  }
    public SimpleDateFormat getLocalDateFormat() {
        return this.localDateFormat;
    }

    public String getBirthDate() {
        if (this.localDateFormat != null)
            return super.getBirthDate();
        else {
            String strBirthDate = super.getBirthDate();
            SimpleDateFormat defaultSDF = new SimpleDateFormat("MM/dd/yy");
            try {
                Date dtBirthDate = defaultSDF.parse(strBirthDate);
                strBirthDate = com.chelseasystems.cs.util.DateFormatUtil.
                               getLocalDateFormat().format(dtBirthDate);
                this.localDateFormat = com.chelseasystems.cs.util.DateFormatUtil.
                               getLocalDateFormat();
                return strBirthDate;
            } catch (Exception e) {
                return null;
            }
        }
    }

    /**
     *
     * @param val boolean
     */
    public void setIsUpdateAllStgTbl(boolean val){
      this.isUpdateAllStgTbl = val;
    }

    /**
     *
     * @return boolean
     */
    public boolean isUpdateAllStgTbl() {
      return this.isUpdateAllStgTbl;
    }
  public void setIsDefaultCustomer(boolean val) {
    this.isDefaultCustomer = val;
  }

  public boolean isDefaultCustomer() {
    return this.isDefaultCustomer;
  }
  
  /**
   * This method gets ytdNetAmountMap from the customer object
   * @return
   */
  public ArmCurrency getYtdNetAmount(Integer priority) {
	  if(ytdNetAmountMap.containsKey(priority) )
		  return (ArmCurrency)ytdNetAmountMap.get(priority);
	  
	  return new ArmCurrency(0.0d);
  }
  
  /**
   * This method sets ytdNetAmountMap in the customer object
   * @param ytdNetAmountMap
   */
  public void setYtdNetAmount(Map ytdNetAmountMap) {
	this.ytdNetAmountMap = ytdNetAmountMap;
  }

  /**
   * This method gets ytdQtyByProductCD from the customer object
   * @return
   */
  public Map getYtdQtyByProductCD() {
	return ytdQtyByProductCD;
  }
  
  /**
   * This method sets ytdQtyByProductCD in the customer object
   * @param ytdQtyByProductCD
   */
  public void setYtdQtyByProductCD(Map ytdQtyByProductCD) {
	this.ytdQtyByProductCD = ytdQtyByProductCD;
  }
  
  //Added for Privacy Mgmt Marketing and Master  
  public void doSetPrivacyMarketing(boolean val) {
	    this.canPrivacyMarketing = val;
	  }

	  /**
	   *
	   * @param val boolean
	   */
  public void setPrivacyMarketing(boolean val)
  {
	  if (this.canPrivacyMarketing != val) {
      this.doSetPrivacyMarketing(val);
      this.setModified();
    }
  }

	  /**
	   *
	   * @return boolean
	   */
  public boolean isPrivacyMarketing() {	
    return this.canPrivacyMarketing;
  }
  
  public void doSetPrivacyMaster(boolean val) {
	    this.canPrivacyMaster = val;
	  }

	  /**
	   *
	   * @param val boolean
	   */
  public void setPrivacyMaster(boolean val)
  {			
    if (this.canPrivacyMaster != val) {
      this.doSetPrivacyMaster(val);
      this.setModified();
    }
  }

  /**
   *
   * @return boolean
   */
  public boolean isPrivacyMaster() {			 
    return this.canPrivacyMaster;
  }
  //Added by vivek sawant to stored Customer's created date.
  /**
   * @param val Date
   */
  public Date getCustIssueDate() {
	return this.custIssueDate;
  }
  /**
   * @param val Date
   */
	
  public void setCustIssueDate(Date custIssueDate) {
	this.custIssueDate = custIssueDate;
  }

  /**
   * Set StoreID for the customer.
   * @return StoreID
   */
	public String getStoreId() {
		return storeId;
	}
	
	/**
	 * Get Customer StoreID
	 * @param storeId
	 */
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
  
}

