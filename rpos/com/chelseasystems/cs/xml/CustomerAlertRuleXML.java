/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.xml;

import com.chelseasystems.cr.xml.XMLUtil;
import com.chelseasystems.cs.xml.tags.CustomerAlertRuleXMLTags;
import com.chelseasystems.cr.business.BusinessObject;
import org.w3c.dom.*;

import com.chelseasystems.cs.customer.CMSCustomerAlertRule;

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
 +------+------------+-----------+-----------+-------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                     |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 1    | 11-03-2006 | Sandhya   | N/A       | Rule to alert the employee about the sales limit|                              |
 ----------------------------------------------------------------------------------------------|
 */
public class CustomerAlertRuleXML extends XMLObject implements CustomerAlertRuleXMLTags {

  /**
   * Default Constructor
   */
  public CustomerAlertRuleXML() {
  }

  /**
   * Base TagName
   * @return String
   */
  protected String getThisTagName() {
    return "CUSTOMER_ALERT_RULE";
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
    CMSCustomerAlertRule custAlertRule = (CMSCustomerAlertRule)obj;
    Element element = doc.createElement(CUSTOMER_ALERT_RULE_TAG);
    XMLUtil.addItem(doc, element, COUNTRY_TAG, custAlertRule.getCountryCode());
    XMLUtil.addItem(doc, element, RECORD_TYPE_TAG, custAlertRule.getRecordType());
    XMLUtil.addItem(doc, element, START_DATE_TAG, custAlertRule.getStartDate());
    XMLUtil.addItem(doc, element, END_DATE_TAG, custAlertRule.getEndDate());
    XMLUtil.addItem(doc, element, PRODUCT_CODE_TAG, custAlertRule.getProductCode());
    XMLUtil.addItem(doc, element, VALUE_TAG, custAlertRule.getValue());
    XMLUtil.addItem(doc, element, PRIORITY_TAG, custAlertRule.getPriority());
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
	CMSCustomerAlertRule custAlertRule = new CMSCustomerAlertRule();
    custAlertRule.doSetCountryCode(XMLUtil.getValueAsString(element, COUNTRY_TAG));
    custAlertRule.doSetRecordType(XMLUtil.getValueAsString(element, RECORD_TYPE_TAG));
    custAlertRule.doSetStartDate(XMLUtil.getValueAsDate(element, START_DATE_TAG));
    custAlertRule.doSetEndDate(XMLUtil.getValueAsDate(element, END_DATE_TAG));
    custAlertRule.doSetProductCode(XMLUtil.getValueAsString(element, PRODUCT_CODE_TAG));
    custAlertRule.doSetValue(XMLUtil.getValueAsString(element, VALUE_TAG));
    custAlertRule.doSetPriority(XMLUtil.getValueAsInteger(element, PRIORITY_TAG));
    return custAlertRule;
  }
}

