/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 02-27-2005 | Anand     | N/A       | Modified to add accessor and mutator methods |
 |      |            |           |           | for accomodating Store Id                    |
 --------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pricing;

import com.chelseasystems.cr.business.BusinessObject;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.pos.CompositePOSTransaction;
import java.io.PrintStream;
import java.util.Date;
import com.chelseasystems.cr.pricing.ThresholdPromotion;


/**
 *
 * <p>Title: CMSThresholdPromotion</p>
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
public class CMSThresholdPromotion extends ThresholdPromotion {
  private String idStrRt;
  private String description;

  /**
   *
   * @return String
   */
  public String getIdStrRt() {
    return this.idStrRt;
  }

  /**
   *
   * @param idStrRt String
   */
  public void doSetIdStrRt(String idStrRt) {
    this.idStrRt = idStrRt;
  } //store Id

  /**
   * This method is used to get description of promotion
   * @return String
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * This method is used to set description of promotion
   * @param description String
   */
  public void doSetDescription(String description) {
    this.description = description;
  }
}

