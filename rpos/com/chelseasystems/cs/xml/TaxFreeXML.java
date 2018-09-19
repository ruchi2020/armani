/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.xml;

import com.chelseasystems.cr.xml.XMLUtil;
import com.chelseasystems.cs.xml.tags.TaxFreeXMLTags;
import com.chelseasystems.cr.business.BusinessObject;
import org.w3c.dom.*;
import com.chelseasystems.cs.fiscaldocument.*;
import com.chelseasystems.cr.currency.ArmCurrency;


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
public class TaxFreeXML extends XMLObject implements TaxFreeXMLTags {

  /**
   * Default Constructor
   */
  public TaxFreeXML() {
  }

  /**
   * Base TagName
   * @return String
   */
  protected String getThisTagName() {
    return "TAX_FREE";
  }

  /**
   * Add an object to Document
   * @param doc Document
   * @param parent Element
   * @param obj Object
   * @throws Exception
   *
   */
  protected void addBusinessObject(Document doc, Element parent, BusinessObject obj)
      throws Exception {
    TaxFree taxFree = (TaxFree)obj;
    Element element = doc.createElement(TAX_FREE_TAG);
    XMLUtil.addItem(doc, element, STORE_CODE_TAG, taxFree.getStoreCode());
    XMLUtil.addItem(doc, element, DETAX_CODE_TAG, taxFree.getDetaxCode());
    XMLUtil.addItem(doc, element, DESC_CODE_TAG, taxFree.getDescCode());
    XMLUtil.addItem(doc, element, RATIO_TAG, taxFree.getRatio());
    XMLUtil.addItem(doc, element, START_DATE_TAG, taxFree.getStartDate());
    XMLUtil.addItem(doc, element, NET_MIN_VALUE_TAG, taxFree.getNetMinVal());
    XMLUtil.addItem(doc, element, GROSS_MIN_VALUE_TAG, taxFree.getGrossMinValue());
    XMLUtil.addItem(doc, element, PAYMENT_CODE_TAG, taxFree.getPaymentCode());
    XMLUtil.addItem(doc, element, MIN_PRICE_TAG, taxFree.getMinPrice());
    XMLUtil.addItem(doc, element, MAX_PRICE_TAG, taxFree.getMaxPrice());
    XMLUtil.addItem(doc, element, MIN_AMOUNT_TAG, taxFree.getMinAmount());
    XMLUtil.addItem(doc, element, MAX_AMOUNT_TAG, taxFree.getMaxAmount());
    XMLUtil.addItem(doc, element, REFUND_AMOUNT_TAG, taxFree.getRefundAmount());
    XMLUtil.addItem(doc, element, REFUND_PERCENT_TAG, taxFree.getRefundPercentage());
    XMLUtil.addItem(doc, element, COMMISSION_TAG, taxFree.getCommission());
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
    TaxFree taxFree = new TaxFree();
    taxFree.doSetStoreCode(XMLUtil.getValueAsString(element, STORE_CODE_TAG));
    taxFree.doSetDetaxCode(XMLUtil.getValueAsString(element, DETAX_CODE_TAG));
    taxFree.doSetDescCode(XMLUtil.getValueAsString(element, DESC_CODE_TAG));
    taxFree.doSetRatio(XMLUtil.getValueAsDouble(element, RATIO_TAG));
    taxFree.doSetStartDate(XMLUtil.getValueAsDate(element, START_DATE_TAG));
    taxFree.doSetNetMinVal(XMLUtil.getValueAsCurrency(element, NET_MIN_VALUE_TAG));
    taxFree.doSetGrossMinValue(XMLUtil.getValueAsCurrency(element, GROSS_MIN_VALUE_TAG));
    taxFree.doSetPaymentCode(XMLUtil.getValueAsString(element, PAYMENT_CODE_TAG));
    taxFree.doSetMinPrice(XMLUtil.getValueAsCurrency(element, MIN_PRICE_TAG));
    taxFree.doSetMaxPrice(XMLUtil.getValueAsCurrency(element, MAX_PRICE_TAG));
    taxFree.doSetMinAmount(XMLUtil.getValueAsCurrency(element, MIN_AMOUNT_TAG));
    taxFree.doSetMaxAmount(XMLUtil.getValueAsCurrency(element, MAX_AMOUNT_TAG));
    taxFree.doSetRefundAmount(XMLUtil.getValueAsCurrency(element, REFUND_AMOUNT_TAG));
    taxFree.doSetRefundPercentage(XMLUtil.getValueAsString(element, REFUND_PERCENT_TAG));
    taxFree.doSetCommission(XMLUtil.getValueAsString(element, COMMISSION_TAG));
    return taxFree;
  }
}

