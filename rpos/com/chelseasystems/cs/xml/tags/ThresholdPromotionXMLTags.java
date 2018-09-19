/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 02-14-2005 | Anand     | N/A       | Modified to add Store ID                  |
 --------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.xml.tags;


public interface ThresholdPromotionXMLTags {
  public static final String thresholdPromotionTag = "THRESHOLD_PROMOTION";
  public static final String thresholdPromotionIdTag = "id";
  public static final String thresholdAmountTag = "thresholdAmount";
  public static final String thresholdStartDateTag = "startDate";
  public static final String thresholdEndDateTag = "endDate";
  public static final String thresholdAmountOffTag = "amountOff";
  public static final String thresholdPercentOffTag = "percentOff";
  public static final String thresholdPercentOffFlag = "percentOffFlag";
  public static final String thresholdIdStrRt = "idStrRt";
}

