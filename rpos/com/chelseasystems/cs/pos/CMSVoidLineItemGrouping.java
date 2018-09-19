/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.pos.POSLineItem;


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
public class CMSVoidLineItemGrouping extends CMSReturnLineItemGrouping {
  static final long serialVersionUID = 6428907380906420549L;

  /**
   * put your documentation comment here
   * @param   POSLineItem lineItem
   */
  public CMSVoidLineItemGrouping(POSLineItem lineItem) {
    super(lineItem);
  }
}

