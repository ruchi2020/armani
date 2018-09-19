/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.xml;

import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.UnsupportedCurrencyTypeException;
import com.chelseasystems.cr.xml.XMLStore;
import com.chelseasystems.cr.xml.XMLUtil;
import com.chelseasystems.cs.pricing.*;
import com.chelseasystems.cs.xml.tags.PromotionXMLTags;
import java.io.PrintStream;
import java.util.StringTokenizer;
import org.w3c.dom.*;


/**
 * put your documentation comment here
 */
public class PromotionXML extends XMLStore implements PromotionXMLTags {

  /**
   * put your documentation comment here
   */
  public PromotionXML() {
  }

  /**
   * put your documentation comment here
   * @return
   */
  protected String getThisTagName() {
    return "PROMOTION";
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
    if (obj instanceof MultiunitPromotion)
      addMultiunitPromotion(doc, parent, (MultiunitPromotion)obj);
    else if (obj instanceof ItemThresholdPromotion)
      addItemThresholdPromotion(doc, parent, (ItemThresholdPromotion)obj);
    else if (obj instanceof BuyXGetYPromotion)
      addBuyXGetYPromotion(doc, parent, (BuyXGetYPromotion)obj);
    else if (obj instanceof PackagePromotion)
      addPackagePromotion(doc, parent, (PackagePromotion)obj);
    else if (obj instanceof MultipriceBreakPromotion) {
      addMultipriceBreakPromotion(doc, parent, (MultipriceBreakPromotion)obj);
    } else {
      //System.out.println("Unknown promotion object type: " );
      throw new ClassCastException("Unknown promotion object type: " + obj.getClass().getName());
    }
  }

  /**
   * put your documentation comment here
   * @param doc
   * @param parent
   * @param promotion
   * @exception Exception
   */
  private void addMultiunitPromotion(Document doc, Element parent, MultiunitPromotion promotion)
      throws Exception {
    Element element = doc.createElement("PROMOTION");
    element.setAttribute("type", "multiunit");
    XMLUtil.addItem(doc, element, this.promotionIdTag, promotion.getId());
    XMLUtil.addItem(doc, element, promotionRuleDrvIdTag, promotion.getRuleDrvId());
    XMLUtil.addItem(doc, element, this.descriptionTag, promotion.getDescription());
    XMLUtil.addItem(doc, element, this.beginTimeTag, promotion.getBeginTime());
    XMLUtil.addItem(doc, element, this.endTimeTag, promotion.getEndTime());
    XMLUtil.addItem(doc, element, this.methodOfReductionTag, promotion.doGetMethodOfReduction());
    if (promotion.doGetMethodOfReduction().equals("PERCENTAGE_OFF"))
      XMLUtil.addItem(doc, element, this.reductionPercentTag, promotion.getReductionPercent());
    else
      XMLUtil.addItem(doc, element, this.reductionAmountTag, promotion.getReductionAmount());
    XMLUtil.addItem(doc, element, this.quantityBreakTag, promotion.getQuantityBreak());
    parent.appendChild(element);
  }

  /**
   * put your documentation comment here
   * @param doc
   * @param parent
   * @param promotion
   * @exception Exception
   */
  private void addItemThresholdPromotion(Document doc, Element parent
      , ItemThresholdPromotion promotion)
      throws Exception {
    Element element = doc.createElement("PROMOTION");
    element.setAttribute("type", "itemThreshold");
    XMLUtil.addItem(doc, element, this.promotionIdTag, promotion.getId());
    XMLUtil.addItem(doc, element, promotionRuleDrvIdTag, promotion.getRuleDrvId());
    XMLUtil.addItem(doc, element, this.descriptionTag, promotion.getDescription());
    XMLUtil.addItem(doc, element, this.beginTimeTag, promotion.getBeginTime());
    XMLUtil.addItem(doc, element, this.endTimeTag, promotion.getEndTime());
    XMLUtil.addItem(doc, element, this.methodOfReductionTag, promotion.doGetMethodOfReduction());
    if (promotion.doGetMethodOfReduction().equals("PERCENTAGE_OFF"))
      XMLUtil.addItem(doc, element, this.reductionPercentTag, promotion.getReductionPercent());
    else
      XMLUtil.addItem(doc, element, this.reductionAmountTag, promotion.getReductionAmount());
    XMLUtil.addItem(doc, element, this.triggerQuantityTag, promotion.getTriggerQuantity());
    XMLUtil.addItem(doc, element, this.triggerAmountTag, promotion.getTriggerAmount());
    parent.appendChild(element);
  }

  /**
   * put your documentation comment here
   * @param doc
   * @param parent
   * @param promotion
   * @exception Exception
   */
  private void addBuyXGetYPromotion(Document doc, Element parent, BuyXGetYPromotion promotion)
      throws Exception {
    Element element = doc.createElement("PROMOTION");
    element.setAttribute("type", "buyXGetY");
    XMLUtil.addItem(doc, element, this.promotionIdTag, promotion.getId());
    XMLUtil.addItem(doc, element, promotionRuleDrvIdTag, promotion.getRuleDrvId());
    XMLUtil.addItem(doc, element, this.descriptionTag, promotion.getDescription());
    XMLUtil.addItem(doc, element, this.beginTimeTag, promotion.getBeginTime());
    XMLUtil.addItem(doc, element, this.endTimeTag, promotion.getEndTime());
    XMLUtil.addItem(doc, element, this.methodOfReductionTag, promotion.doGetMethodOfReduction());
    if (promotion.doGetMethodOfReduction().equals("PERCENTAGE_OFF"))
      XMLUtil.addItem(doc, element, this.reductionPercentTag, promotion.getReductionPercent());
    else
      XMLUtil.addItem(doc, element, this.reductionAmountTag, promotion.getReductionAmount());
    parent.appendChild(element);
  }

  /**
   * put your documentation comment here
   * @param doc
   * @param parent
   * @param promotion
   * @exception Exception
   */
  private void addPackagePromotion(Document doc, Element parent, PackagePromotion promotion)
      throws Exception {
    Element element = doc.createElement("PROMOTION");
    element.setAttribute("type", "package");
    XMLUtil.addItem(doc, element, this.promotionIdTag, promotion.getId());
    XMLUtil.addItem(doc, element, promotionRuleDrvIdTag, promotion.getRuleDrvId());
    XMLUtil.addItem(doc, element, this.descriptionTag, promotion.getDescription());
    XMLUtil.addItem(doc, element, this.beginTimeTag, promotion.getBeginTime());
    XMLUtil.addItem(doc, element, this.endTimeTag, promotion.getEndTime());
    String methodOfReduction = promotion.doGetMethodOfReduction();
    XMLUtil.addItem(doc, element, this.methodOfReductionTag, methodOfReduction);
    if (methodOfReduction.equals("FIXED_UNIT_PRICE"))
      XMLUtil.addItem(doc, element, this.componentReductionAmountsTag
          , convertToString(promotion.getComponentReductionAmounts()));
    else if (methodOfReduction.equals("FIXED_TOTAL_PRICE")) {
      XMLUtil.addItem(doc, element, this.numberOfComponentsTag, promotion.getNumberOfComponents());
      XMLUtil.addItem(doc, element, this.reductionAmountTag, promotion.getReductionAmount());
    } else if (methodOfReduction.equals("UNIT_PRICE_OFF"))
      XMLUtil.addItem(doc, element, this.componentReductionAmountsTag
          , convertToString(promotion.getComponentReductionAmounts()));
    else if (methodOfReduction.equals("TOTAL_PRICE_OFF")) {
      XMLUtil.addItem(doc, element, this.numberOfComponentsTag, promotion.getNumberOfComponents());
      XMLUtil.addItem(doc, element, this.reductionAmountTag, promotion.getReductionAmount());
    } else if (methodOfReduction.equals("PERCENTAGE_OFF"))
      XMLUtil.addItem(doc, element, this.componentReductionPercentsTag
          , convertToString(promotion.getComponentReductionPercents()));
    parent.appendChild(element);
  }

  /**
   * put your documentation comment here
   * @param doc
   * @param parent
   * @param promotion
   * @exception Exception
   */
  private void addMultipriceBreakPromotion(Document doc, Element parent
      , MultipriceBreakPromotion promotion)
      throws Exception {
    Element element = doc.createElement("PROMOTION");
    element.setAttribute("type", "multipriceBreak");
    XMLUtil.addItem(doc, element, this.promotionIdTag, promotion.getId());
    XMLUtil.addItem(doc, element, promotionRuleDrvIdTag, promotion.getRuleDrvId());
    XMLUtil.addItem(doc, element, this.descriptionTag, promotion.getDescription());
    XMLUtil.addItem(doc, element, this.beginTimeTag, promotion.getBeginTime());
    XMLUtil.addItem(doc, element, this.endTimeTag, promotion.getEndTime());
    XMLUtil.addItem(doc, element, this.priceBreaksTag, convertToString(promotion.getPriceBreaks()));
    parent.appendChild(element);
  }

  /**
   * put your documentation comment here
   * @param element
   * @return
   * @exception Exception
   */
  protected Object getObject(Element element)
      throws Exception {
    String promotionType = element.getAttribute("type");
    if (promotionType.equals("multiunit"))
      return getMultiunitPromotion(element);
    if (promotionType.equals("itemThreshold"))
      return getItemThresholdPromotion(element);
    if (promotionType.equals("buyXGetY"))
      return getBuyXGetYPromotion(element);
    if (promotionType.equals("package"))
      return getPackagePromotion(element);
    if (promotionType.equals("multipriceBreak")) {
      return getMultipriceBreakPromotion(element);
    } else {
      System.out.println("Promotion type undefined:" + promotionType + ".");
      return null;
    }
  }

  /**
   * put your documentation comment here
   * @param element
   * @return
   * @exception Exception
   */
  private MultiunitPromotion getMultiunitPromotion(Element element)
      throws Exception {
    String id = XMLUtil.getValueAsString(element, this.promotionIdTag);
    MultiunitPromotion promotion = new MultiunitPromotion(id);
    promotion.doSetRuleDrvId(XMLUtil.getValueAsString(element, promotionRuleDrvIdTag));
    promotion.doSetDescription(XMLUtil.getValueAsString(element, this.descriptionTag));
    promotion.doSetBeginTime(XMLUtil.getValueAsCalendarOrNull(element, this.beginTimeTag));
    promotion.doSetEndTime(XMLUtil.getValueAsCalendarOrNull(element, this.endTimeTag));
    String method = XMLUtil.getValueAsString(element, this.methodOfReductionTag);
    if (method.equals("FIXED_TOTAL_PRICE"))
      promotion.doSetReductionByFixedTotalPrice(XMLUtil.getValueAsCurrency(element
          , this.reductionAmountTag));
    else if (method.equals("FIXED_UNIT_PRICE"))
      promotion.doSetReductionByFixedUnitPrice(XMLUtil.getValueAsCurrency(element
          , this.reductionAmountTag));
    else if (method.equals("PERCENTAGE_OFF"))
      promotion.doSetReductionByPercentageOff(XMLUtil.getValueAs_double(element
          , this.reductionPercentTag));
    else if (method.equals("TOTAL_PRICE_OFF"))
      promotion.doSetReductionByTotalPriceOff(XMLUtil.getValueAsCurrency(element
          , this.reductionAmountTag));
    else if (method.equals("UNIT_PRICE_OFF"))
      promotion.doSetReductionByUnitPriceOff(XMLUtil.getValueAsCurrency(element
          , this.reductionAmountTag));
    promotion.doSetQuantityBreak(XMLUtil.getValueAs_int(element, this.quantityBreakTag));
    if (promotion.getReductionAmount() == null)
      promotion.doSetReductionAmount(XMLUtil.getValueAsCurrency(element, this.reductionAmountTag));
    return promotion;
  }

  /**
   * put your documentation comment here
   * @param element
   * @return
   * @exception Exception
   */
  private ItemThresholdPromotion getItemThresholdPromotion(Element element)
      throws Exception {
    String id = XMLUtil.getValueAsString(element, this.promotionIdTag);
    ItemThresholdPromotion promotion = new ItemThresholdPromotion(id);
    promotion.doSetRuleDrvId(XMLUtil.getValueAsString(element, promotionRuleDrvIdTag));
    promotion.doSetDescription(XMLUtil.getValueAsString(element, this.descriptionTag));
    promotion.doSetBeginTime(XMLUtil.getValueAsCalendarOrNull(element, this.beginTimeTag));
    promotion.doSetEndTime(XMLUtil.getValueAsCalendarOrNull(element, this.endTimeTag));
    String method = XMLUtil.getValueAsString(element, this.methodOfReductionTag);
    if (method.equals("FIXED_UNIT_PRICE"))
      promotion.doSetReductionByFixedUnitPrice(XMLUtil.getValueAsCurrency(element
          , this.reductionAmountTag));
    else if (method.equals("PERCENTAGE_OFF"))
      promotion.doSetReductionByPercentageOff(XMLUtil.getValueAs_double(element
          , this.reductionPercentTag));
    else if (method.equals("UNIT_PRICE_OFF"))
      promotion.doSetReductionByUnitPriceOff(XMLUtil.getValueAsCurrency(element
          , this.reductionAmountTag));
    else
      System.out.println("Invalid reduction method " + method + " found in Promotion " + promotion);
    promotion.doSetTriggerAmount(XMLUtil.getValueAsCurrency(element, this.triggerAmountTag));
    promotion.doSetTriggerQuantity(XMLUtil.getValueAs_int(element, this.triggerQuantityTag));
    if (promotion.getReductionAmount() == null)
      promotion.doSetReductionAmount(XMLUtil.getValueAsCurrency(element, this.reductionAmountTag));
    return promotion;
  }

  /**
   * put your documentation comment here
   * @param element
   * @return
   * @exception Exception
   */
  private BuyXGetYPromotion getBuyXGetYPromotion(Element element)
      throws Exception {
    String id = XMLUtil.getValueAsString(element, this.promotionIdTag);
    BuyXGetYPromotion promotion = new BuyXGetYPromotion(id);
    promotion.doSetRuleDrvId(XMLUtil.getValueAsString(element, promotionRuleDrvIdTag));
    promotion.doSetDescription(XMLUtil.getValueAsString(element, this.descriptionTag));
    promotion.doSetBeginTime(XMLUtil.getValueAsCalendarOrNull(element, this.beginTimeTag));
    promotion.doSetEndTime(XMLUtil.getValueAsCalendarOrNull(element, this.endTimeTag));
    String method = XMLUtil.getValueAsString(element, this.methodOfReductionTag);
    if (method.equals("FIXED_UNIT_PRICE"))
      promotion.doSetReductionByFixedUnitPrice(XMLUtil.getValueAsCurrency(element
          , this.reductionAmountTag));
    else if (method.equals("PERCENTAGE_OFF"))
      promotion.doSetReductionByPercentageOff(XMLUtil.getValueAs_double(element
          , this.reductionPercentTag));
    else if (method.equals("UNIT_PRICE_OFF"))
      promotion.doSetReductionByUnitPriceOff(XMLUtil.getValueAsCurrency(element
          , this.reductionAmountTag));
    else
      System.out.println("Invalid reduction methid " + method + " found in Promotion " + promotion);
    if (promotion.getReductionAmount() == null)
      promotion.doSetReductionAmount(XMLUtil.getValueAsCurrency(element, this.reductionAmountTag));
    return promotion;
  }

  /**
   * put your documentation comment here
   * @param element
   * @return
   * @exception Exception
   */
  private PackagePromotion getPackagePromotion(Element element)
      throws Exception {
    String id = XMLUtil.getValueAsString(element, this.promotionIdTag);
    PackagePromotion promotion = new PackagePromotion(id);
    promotion.doSetRuleDrvId(XMLUtil.getValueAsString(element, promotionRuleDrvIdTag));
    promotion.doSetDescription(XMLUtil.getValueAsString(element, this.descriptionTag));
    promotion.doSetBeginTime(XMLUtil.getValueAsCalendarOrNull(element, this.beginTimeTag));
    promotion.doSetEndTime(XMLUtil.getValueAsCalendarOrNull(element, this.endTimeTag));
    String method = XMLUtil.getValueAsString(element, this.methodOfReductionTag);
    if (method.equals("FIXED_TOTAL_PRICE")) {
      int number = XMLUtil.getValueAs_int(element, this.numberOfComponentsTag);
      ArmCurrency amount = XMLUtil.getValueAsCurrency(element, this.reductionAmountTag);
      promotion.doSetReductionByFixedTotalPrice(amount, number);
    } else if (method.equals("FIXED_UNIT_PRICE")) {
      String s = XMLUtil.getValueAsString(element, this.componentReductionAmountsTag);
      promotion.doSetReductionByFixedUnitPrice(convertToCurrencyArray(s));
    } else if (method.equals("PERCENTAGE_OFF")) {
      String s = XMLUtil.getValueAsString(element, this.componentReductionPercentsTag);
      promotion.doSetReductionByPercentageOff(convertToDoubleArray(s));
    } else if (method.equals("TOTAL_PRICE_OFF")) {
      int number = XMLUtil.getValueAs_int(element, this.numberOfComponentsTag);
      ArmCurrency amount = XMLUtil.getValueAsCurrency(element, this.reductionAmountTag);
      promotion.doSetReductionByTotalPriceOff(amount, number);
    } else if (method.equals("UNIT_PRICE_OFF")) {
      String s = XMLUtil.getValueAsString(element, this.componentReductionAmountsTag);
      promotion.doSetReductionByUnitPriceOff(convertToCurrencyArray(s));
    }
    if (promotion.getReductionAmount() == null)
      if (promotion.getComponentReductionAmounts() != null
          && promotion.getComponentReductionAmounts().length > 0)
        promotion.doSetReductionAmount(new ArmCurrency(promotion.getComponentReductionAmounts()[0].
            getCurrencyType(), 0.0D));
      else
        promotion.doSetReductionAmount(XMLUtil.getValueAsCurrency(element, this.reductionAmountTag));
    return promotion;
  }

  /**
   * put your documentation comment here
   * @param element
   * @return
   * @exception Exception
   */
  private MultipriceBreakPromotion getMultipriceBreakPromotion(Element element)
      throws Exception {
    String id = XMLUtil.getValueAsString(element, this.promotionIdTag);
    MultipriceBreakPromotion promotion = new MultipriceBreakPromotion(id);
    promotion.doSetRuleDrvId(XMLUtil.getValueAsString(element, promotionRuleDrvIdTag));
    promotion.doSetDescription(XMLUtil.getValueAsString(element, this.descriptionTag));
    promotion.doSetBeginTime(XMLUtil.getValueAsCalendarOrNull(element, this.beginTimeTag));
    promotion.doSetEndTime(XMLUtil.getValueAsCalendarOrNull(element, this.endTimeTag));
    promotion.doSetPriceBreaks(convertToCurrencyArray(XMLUtil.getValueAsString(element
        , this.priceBreaksTag)));
    if (promotion.getReductionAmount() == null)
      promotion.doSetReductionAmount(new ArmCurrency(promotion.getPriceBreaks()[0].getCurrencyType()
          , 0.0D));
    return promotion;
  }

  /**
   * put your documentation comment here
   * @param amounts[]
   * @return
   */
  private static String convertToString(ArmCurrency amounts[]) {
    if (amounts == null || amounts.length == 0)
      return "";
    StringBuffer sb = new StringBuffer(amounts[0].toDelimitedString());
    for (int i = 1; i < amounts.length; i++)
      sb.append("," + amounts[i].toDelimitedString());
    return sb.toString();
  }

  /**
   * put your documentation comment here
   * @param doubles[]
   * @return
   */
  private static String convertToString(double doubles[]) {
    if (doubles == null || doubles.length == 0)
      return "";
    StringBuffer sb = new StringBuffer("" + doubles[0]);
    for (int i = 1; i < doubles.length; i++)
      sb.append("," + doubles[i]);
    return sb.toString();
  }

  /**
   * put your documentation comment here
   * @param string
   * @return
   * @exception UnsupportedCurrencyTypeException
   */
  private static ArmCurrency[] convertToCurrencyArray(String string)
      throws UnsupportedCurrencyTypeException {
    if (string == null || string.length() == 0)
      return new ArmCurrency[0];
    StringTokenizer st = new StringTokenizer(string, ",");
    ArmCurrency array[] = new ArmCurrency[st.countTokens()];
    for (int i = 0; i < array.length; i++)
      array[i] = ArmCurrency.valueOf(st.nextToken());
    return array;
  }

  /**
   * put your documentation comment here
   * @param string
   * @return
   */
  private static double[] convertToDoubleArray(String string) {
    if (string == null || string.length() == 0)
      return new double[0];
    StringTokenizer st = new StringTokenizer(string, ",");
    double array[] = new double[st.countTokens()];
    for (int i = 0; i < array.length; i++)
      array[i] = (new Double(st.nextToken())).doubleValue();
    return array;
  }
}

