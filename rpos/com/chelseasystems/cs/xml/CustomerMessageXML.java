/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.xml;

import com.chelseasystems.cr.xml.XMLUtil;
import com.chelseasystems.cs.xml.tags.CustomerMessageXMLTags;
import com.chelseasystems.cr.business.BusinessObject;
import org.w3c.dom.*;
import java.text.SimpleDateFormat;
import com.chelseasystems.cs.customer.CMSCustomerMessage;


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
 | 1    | 05-03-2005 | Megha     | N/A       | Build the class                              |
 --------------------------------------------------------------------------------------------
 */
public class CustomerMessageXML extends XMLObject implements CustomerMessageXMLTags {

  /**
   * Default Constructor
   */
  public CustomerMessageXML() {
  }

  /**
   * Base TagName
   * @return String
   */
  protected String getThisTagName() {
    return "LOYALTY";
  }

  /**
   * Add an object to Document
   * @param doc Document
   * @param parent Element
   * @param obj Object
   * @throws Exception
   *
   *
   */
  protected void addBusinessObject(Document doc, Element parent, BusinessObject obj)
      throws Exception {
    CMSCustomerMessage custMsg = (CMSCustomerMessage)obj;
    Element element = doc.createElement(CUSTOMER_MSG_TAG);
    XMLUtil.addItem(doc, element, CUSTOMER_EXTERNAL_ID_TAG, custMsg.getCustomerId());
    XMLUtil.addItem(doc, element, CUSTOMER_TYPE_TAG, custMsg.getCustomerType());
    XMLUtil.addItem(doc, element, MSG_TYPE_TAG, custMsg.getMessageType());
    XMLUtil.addItem(doc, element, MSG_TAG, custMsg.getMessage());
    parent.appendChild(element);
  }

  /**
   * put your documentation comment here
   * @param element
   * @return
   * @exception Exception
   */
  protected BusinessObject getObject(Element element)
      throws Exception {
    CMSCustomerMessage custMsg = new CMSCustomerMessage();
    custMsg.doSetCustomerId(XMLUtil.getValueAsString(element, CUSTOMER_EXTERNAL_ID_TAG));
    custMsg.doSetCustomerType(XMLUtil.getValueAsString(element, CUSTOMER_TYPE_TAG));
    custMsg.doSetMessageType(XMLUtil.getValueAsString(element, MSG_TYPE_TAG));
    custMsg.doSetMessage(XMLUtil.getValueAsString(element, MSG_TAG));
    return custMsg;
  }
}

