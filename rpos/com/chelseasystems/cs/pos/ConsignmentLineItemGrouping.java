/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.rules.IRuleEngine;
import java.io.Serializable;


/**
 *
 * <p>Title: ConsignmentLineItemGrouping</p>
 *
 * <p>Description: This class is use to instantiate consignment line item group</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author
 * @version 1.0
 */
public class ConsignmentLineItemGrouping extends POSLineItemGrouping implements IRuleEngine
    , Serializable {

  /**
   * Constructor
   * @param aLineItem POSLineItem
   */
  public ConsignmentLineItemGrouping(POSLineItem aLineItem) {
    super(aLineItem);
  }
}

