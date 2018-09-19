/*
 History:
 +------+------------+-----------+-----------+-------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                     |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 2    | 02-14-2005 | Anand     | N/A       | Modified to add additinal tags to promotions.xml|
 ----------------------------------------------------------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.xml.tags;


public interface PromotionXMLTags {
  public static final String promotionTag = "PROMOTION";
  public static final String promotionTypeAttribute = "type";
  public static final String promotionTypeMultiunit = "multiunit";
  public static final String promotionTypeItemThreshold = "itemThreshold";
  public static final String promotionTypeBuyXGetY = "buyXGetY";
  public static final String promotionTypePackage = "package";
  public static final String promotionTypeMultipriceBreak = "multipriceBreak";
  public static final String promotionIdTag = "id";
  public static final String promotionRuleDrvIdTag = "ruleDrvId";
  public static final String descriptionTag = "description";
  public static final String beginTimeTag = "beginTime";
  public static final String endTimeTag = "endTime";
  public static final String methodOfReductionTag = "methodOfReduction";
  public static final String reductionAmountTag = "reductionAmount";
  public static final String reductionPercentTag = "reductionPercent";
  public static final String quantityBreakTag = "quantityBreak";
  public static final String triggerQuantityTag = "triggerQuantity";
  public static final String triggerAmountTag = "triggerAmount";
  public static final String numberOfComponentsTag = "numberOfComponents";
  public static final String componentReductionPercentsTag = "componentReductionPercents";
  public static final String componentReductionAmountsTag = "componentReductionAmounts";
  public static final String priceBreaksTag = "priceBreaks";
  public static final String idStrRtTag = "idStrRt";
  public static final String thresholdAmtTag = "thresholdAmt";
  public static final String discountTypeTag = "discountType";
}

