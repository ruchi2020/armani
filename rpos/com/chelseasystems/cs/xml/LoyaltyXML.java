/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.xml;

import com.chelseasystems.cr.xml.XMLUtil;
import com.chelseasystems.cs.xml.tags.LoyaltyXMLTags;
import com.chelseasystems.cr.business.BusinessObject;
import org.w3c.dom.*;
import java.text.SimpleDateFormat;
import com.chelseasystems.cs.loyalty.LoyaltyRule;


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
public class LoyaltyXML extends XMLObject implements LoyaltyXMLTags {

  /**
   * Default Constructor
   */
  public LoyaltyXML() {
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
    LoyaltyRule loyaltyRule = (LoyaltyRule)obj;
    Element element = doc.createElement(LOYALTY_TAG);
    element.setAttribute(RULE_ID_TAG, loyaltyRule.getRuleID());
    XMLUtil.addItem(doc, element, START_DATE_TAG, loyaltyRule.getStartDate());
    XMLUtil.addItem(doc, element, END_DATE_TAG, loyaltyRule.getEndDate());
    XMLUtil.addItem(doc, element, STORE_ID_TAG, loyaltyRule.getStoreID());
    XMLUtil.addItem(doc, element, CUST_TYPE_TAG, loyaltyRule.getCustType());
    XMLUtil.addItem(doc, element, POINT_RATIO_TAG, loyaltyRule.getPointsRatio());
    XMLUtil.addItem(doc, element, ITEM_DEPART_TAG, loyaltyRule.getItemDepart());
    XMLUtil.addItem(doc, element, ITEM_CLASS_TAG, loyaltyRule.getItemClass());
    XMLUtil.addItem(doc, element, ITEM_SUBCLASS_TAG, loyaltyRule.getItemSubclass());
    XMLUtil.addItem(doc, element, STYLE_NUM_TAG, loyaltyRule.getStyleNumber());
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
    String id = element.getAttribute(RULE_ID_TAG);
    if (id == null || id.length() == 0)
      return null;
    LoyaltyRule loyaltyRule = new LoyaltyRule(id);
    loyaltyRule.doSetStartDate(XMLUtil.getValueAsDate(element, START_DATE_TAG));
    loyaltyRule.doSetEndDate(XMLUtil.getValueAsDate(element, END_DATE_TAG));
    loyaltyRule.doSetStoreID(XMLUtil.getValueAsString(element, STORE_ID_TAG));
    loyaltyRule.doSetCustType(XMLUtil.getValueAsString(element, CUST_TYPE_TAG));
    loyaltyRule.doSetPointsRatio(XMLUtil.getValueAs_double(element, POINT_RATIO_TAG));
    loyaltyRule.doSetItemDepart(XMLUtil.getValueAsString(element, ITEM_DEPART_TAG));
    loyaltyRule.doSetItemClass(XMLUtil.getValueAsString(element, ITEM_CLASS_TAG));
    loyaltyRule.doSetItemSubclass(XMLUtil.getValueAsString(element, ITEM_SUBCLASS_TAG));
    loyaltyRule.doSetStyleNumber(XMLUtil.getValueAsString(element, STYLE_NUM_TAG));
    return loyaltyRule;
  }
}

