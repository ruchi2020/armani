/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.pos.POSLineItem;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class CMSVoidLineItemDetail extends CMSReturnLineItemDetail {
  public static final Long POS_LINE_ITEM_TYPE_VOID = new Long(7);

  /**
   * put your documentation comment here
   * @param   POSLineItem lineItem
   */
  public CMSVoidLineItemDetail(POSLineItem lineItem) {
    super(lineItem);
  }
}

