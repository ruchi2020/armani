/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.xml.tags;


/**
 *
 * <p>Title:CustomerAlertRuleXMLTags </p>
 * <p>Description: Defines XML Tags for customer_alert_rule.xml </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Sandhya Ajit
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 11-06-2006 | Sandhya   | N/A       |   Customer Alert Rule                        |
 --------------------------------------------------------------------------------------------
 */
public interface CustomerAlertRuleXMLTags {
  public static final String CUSTOMER_ALERT_RULE_TAG = "CUSTOMER_ALERT_RULE";
  public static final String COUNTRY_TAG = "COUNTRY_CODE";
  public static final String RECORD_TYPE_TAG = "RECORD_TYPE";
  public static final String START_DATE_TAG = "START_DATE";
  public static final String END_DATE_TAG = "END_DATE";
  public static final String PRODUCT_CODE_TAG = "PRODUCT_CODE";
  public static final String VALUE_TAG = "VALUE";
  public static final String PRIORITY_TAG = "PRIORITY";
}

