/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.rules.IRuleEngine;
import java.io.Serializable;


/**
 * <p>Title: ReservationLineItemGrouping </p>
 * <p>Description:Used for grouping line items </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 07-11-2005 | Manpreet  | N/A       |  POS_104665_TS_Reservations_Rev4                   |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class ReservationLineItemGrouping extends POSLineItemGrouping implements IRuleEngine
    , Serializable {

  /**
   * Constructor
   * @param aLineItem POSLineItem
   */
  public ReservationLineItemGrouping(POSLineItem aLineItem) {
    super(aLineItem);
  }
}

