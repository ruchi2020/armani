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
 * <p>Title: PresaleLineItemGrouping</p>
 *
 * <p>Description: This class is use to instantiate pre sale line item group</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author
 * @version 1.0
 */
public class PresaleLineItemGrouping extends POSLineItemGrouping implements IRuleEngine
    , Serializable {

  /**
   * Constructor
   * @param aLineItem POSLineItem
   */
  public PresaleLineItemGrouping(POSLineItem aLineItem) {
    super(aLineItem);
  }
}

