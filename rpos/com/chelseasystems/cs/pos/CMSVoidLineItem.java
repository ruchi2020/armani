/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.item.Item;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class CMSVoidLineItem extends CMSReturnLineItem {

  /**
   * put your documentation comment here
   * @param   POSTransaction aTransaction
   * @param   Item anItem
   * @param   int sequenceNumber
   */
  public CMSVoidLineItem(POSTransaction aTransaction, Item anItem, int sequenceNumber) {
    super(aTransaction, anItem, sequenceNumber);
  }

  /**
   * put your documentation comment here
   * @param   POSTransaction aTransaction
   * @param   Item anItem
   */
  public CMSVoidLineItem(POSTransaction aTransaction, Item anItem) {
    super(aTransaction, anItem);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public POSLineItemDetail createLineItemDetail() {
    return new CMSVoidLineItemDetail(this);
  }
}

