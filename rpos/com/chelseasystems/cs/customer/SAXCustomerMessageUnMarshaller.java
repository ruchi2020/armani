/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.customer;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;


/**
 * <p>Title: </p>
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
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 05-20-2005 | Megha    | N/A        | Customer Messages                            |
 --------------------------------------------------------------------------------------------
 */
public class SAXCustomerMessageUnMarshaller extends DefaultHandler {
  String customerId;
  String custType;
  String holder;
  StringBuffer strBuff = new StringBuffer();
  CMSCustomerMessage cmsCustMsg = new CMSCustomerMessage();
  CMSCustomerMessage tempCustMsg = new CMSCustomerMessage();
  boolean searchById = false;
  boolean noMoreSeachByCustType = false;
  boolean isMessageRequired = false;
  boolean isMessageTypeRequired = false;
  boolean isCustTypePresent;
  boolean isMsgSet;
  boolean isMsgTypeSet;
  boolean setMsg;
  boolean setMsgType;
  boolean searchByIdComplete;
  boolean isCustIdPresent;
  boolean isCustIdNull;
  boolean forCustId;

  /**
   * put your documentation comment here
   * @param   String custId
   * @param   String custType
   */
  public SAXCustomerMessageUnMarshaller(String custId, String custType) {
    this.customerId = custId;
    this.custType = custType;
  }

  /**
   * put your documentation comment here
   */
  public void startDocument() {
    isCustTypePresent = false;
    isCustIdPresent = false;
    isMsgSet = false;
    isMsgTypeSet = false;
    noMoreSeachByCustType = false;
    setMsg = false;
    setMsgType = false;
    searchByIdComplete = false;
    isCustIdNull = false;
  }

  /**
   * Method to get the CMSCustomerMessage
   * return CMSCustomerMessage
   */
  public CMSCustomerMessage getCustomerMessage() {
    if (searchByIdComplete) {
      return this.cmsCustMsg;
    } else if (noMoreSeachByCustType) {
      return this.tempCustMsg;
    } else {
      return null;
    }
  }

  /**
   * Find if SearchById or SearchByType
   *
   */
  public boolean getSearchById() {
    return this.searchByIdComplete;
  }

  // ----- callbacks: -----
  // -----
  public void startElement(String uri, String localName, String qName, Attributes attribs) {
    // If one msg is found corresponding to an Id
    if (searchByIdComplete) {
      endDocument();
    }
    if (qName.equals("CUST_EXT_ID")) {
      forCustId = true;
      isCustIdNull = true;
    }
    if (qName.equals("MSG")) {
      isMessageRequired = true;
    }
    if (qName.equals("MSG_TYPE")) {
      isMessageTypeRequired = true;
    }
  }

  // -----
  public void endElement(String uri, String localName, String qName) {
    if (qName.equals("MSG")) {
      isMessageRequired = false;
    }
    if (qName.equals("MSG_TYPE")) {
      isMessageTypeRequired = false;
    }
    if (qName.equals("CUST_TYPE")) {
      if (isMsgSet && isMsgTypeSet) {
        noMoreSeachByCustType = true;
      }
    }
    if (qName.equals("CUSTOMER_MSG")) {
      if (setMsg && setMsgType) {
        searchByIdComplete = true;
      }
    }
    if (qName.equals("CUST_EXT_ID")) {
      forCustId = false;
    }
  }

  // -----
  public void characters(char[] data, int start, int length) {
    String temp = new String();
    temp.copyValueOf(data, start, length);
    if (forCustId) {
      isCustIdNull = false;
    }
    if (temp.copyValueOf(data, start, length) != null
        && (temp.copyValueOf(data, start, length)).equals(customerId)) {
      isCustIdPresent = true;
    }
    // If search by Id present.
    if (isCustIdPresent && isMessageRequired && !searchByIdComplete) {
      cmsCustMsg.doSetMessage(temp.copyValueOf(data, start, length));
      setMsg = true;
    }
    if (isCustIdPresent && isMessageTypeRequired && !searchByIdComplete) {
      cmsCustMsg.doSetMessageType(temp.copyValueOf(data, start, length));
      setMsgType = true;
    }
    // Searching by Customer Type
    if ((temp.copyValueOf(data, start, length)).equals(custType)) {
      isCustTypePresent = true;
    }
    if (isCustTypePresent && isMessageRequired && !noMoreSeachByCustType && isCustIdNull) {
      tempCustMsg.doSetMessage(temp.copyValueOf(data, start, length));
      isMsgSet = true;
    }
    if (isCustTypePresent && isMessageTypeRequired && !noMoreSeachByCustType && isCustIdNull) {
      tempCustMsg.doSetMessageType(temp.copyValueOf(data, start, length));
      isMsgTypeSet = true;
    }
  }

  // -----
  public void endDocument() {
    if (isMsgSet && isMsgTypeSet) {
      noMoreSeachByCustType = true;
    }
    if (setMsg && setMsgType) {
      searchByIdComplete = true;
    }
    return;
  }
}

