/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cs.pos.CMSSaleLineItemGrouping;


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
public class CMSNoSaleLineItemGrouping extends CMSSaleLineItemGrouping {

  /**
   * put your documentation comment here
   * @param   POSLineItem anLineItem
   */
  public CMSNoSaleLineItemGrouping(POSLineItem anLineItem) {
    super(anLineItem);
  }

  static final long serialVersionUID = 5604498972400682940L;
}

