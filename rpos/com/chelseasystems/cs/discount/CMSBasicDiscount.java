/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.discount;

import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.rules.RuleEngine;


/**
 *
 * <p>Title: CMSBasicDiscount</p>
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
public class CMSBasicDiscount extends CMSDiscount {

  /**
   * Default Constructor
   */
  public CMSBasicDiscount() {
  }

  /**
   * This method is used to check whether transaction is valid for discount
   * @param txnPos CompositePOSTransaction
   * @throws BusinessRuleException
   */
  public void isValid(CompositePOSTransaction txnPos)
      throws BusinessRuleException {
    RuleEngine.execute(getClass().getName(), "isValid", txnPos, null);
  }

  private static final long serialVersionUID = 0xd6aa8b68cc66d9b8L;
}
