/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.xml;

import com.chelseasystems.cr.pricing.ThresholdPromotion;
import com.chelseasystems.cr.xml.XMLStore;
import com.chelseasystems.cr.xml.XMLUtil;
import com.chelseasystems.cs.pricing.CMSThresholdPromotion;
import com.chelseasystems.cs.xml.tags.ThresholdPromotionXMLTags;
import org.w3c.dom.*;


/**
 * put your documentation comment here
 */
public class ThresholdPromotionXML extends XMLStore implements ThresholdPromotionXMLTags {

  /**
   * put your documentation comment here
   */
  public ThresholdPromotionXML() {
  }

  /**
   * put your documentation comment here
   * @return
   */
  protected String getThisTagName() {
    return "THRESHOLD_PROMOTION";
  }

  /**
   * put your documentation comment here
   * @param doc
   * @param parent
   * @param obj
   * @exception Exception
   */
  protected void addObject(Document doc, Element parent, Object obj)
      throws Exception {
    Element element = doc.createElement("THRESHOLD_PROMOTION");
    CMSThresholdPromotion thresholdPromotion = (CMSThresholdPromotion)obj;
    XMLUtil.addItem(doc, element, "id", thresholdPromotion.getId());
    XMLUtil.addItem(doc, element, "thresholdAmount", thresholdPromotion.getThresholdAmount());
    XMLUtil.addItem(doc, element, "startDate", thresholdPromotion.getStartDate());
    XMLUtil.addItem(doc, element, "endDate", thresholdPromotion.getEndDate());
    XMLUtil.addItem(doc, element, "percentOffFlag", thresholdPromotion.isPercentOff());
    if (thresholdPromotion.isPercentOff())
      XMLUtil.addItem(doc, element, "percentOff", thresholdPromotion.getPercentOff());
    else
      XMLUtil.addItem(doc, element, "amountOff", thresholdPromotion.getAmountOff());
    parent.appendChild(element);
    XMLUtil.addItem(doc, element, "idStrRt", thresholdPromotion.getIdStrRt());
  }

  /**
   * put your documentation comment here
   * @param element
   * @return
   * @exception Exception
   */
  protected Object getObject(Element element)
      throws Exception {
    CMSThresholdPromotion thresholdPromotion = new CMSThresholdPromotion();
    thresholdPromotion.doSetId(XMLUtil.getValueAsString(element, "id"));
    thresholdPromotion.doSetThresholdAmount(XMLUtil.getValueAsCurrency(element, "thresholdAmount"));
    thresholdPromotion.doSetStartDate(XMLUtil.getValueAsDate(element, "startDate"));
    thresholdPromotion.doSetEndDate(XMLUtil.getValueAsDate(element, "endDate"));
    boolean isPercentOff = XMLUtil.getValueAs_boolean(element, "percentOffFlag");
    thresholdPromotion.doSetPercentOffFlag(isPercentOff);
    if (isPercentOff)
      thresholdPromotion.doSetPercentOff(XMLUtil.getValueAsDouble(element, "percentOff"));
    else
      thresholdPromotion.doSetAmountOff(XMLUtil.getValueAsCurrency(element, "amountOff"));
    thresholdPromotion.doSetIdStrRt(XMLUtil.getValueAsString(element, "idStrRt"));
    return thresholdPromotion;
  }
}

