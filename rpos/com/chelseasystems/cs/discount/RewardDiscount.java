/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.discount;

import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.rules.RuleEngine;
import com.chelseasystems.cs.loyalty.RewardCard;


/**
 *
 * <p>Title: RewardDiscount</p>
 *
 * <p>Description: This class stores the information about the reward discount</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class RewardDiscount extends CMSDiscount {
  private static final long serialVersionUID = -3052687363903040688L;
  private RewardCard rewardCard;

  /**
   * Default Constructor
   */
  public RewardDiscount() {
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

  /**
   * This method is used to get reward discount card
   * @return RewardCard
   */
  public RewardCard getRewardCard() {
    return this.rewardCard;
  }

  /**
   * This method is used to set reward discount id
   * @param rewardDiscountId String
   */
  public void doSetRewardCard(RewardCard rewardCard) {
    this.rewardCard = rewardCard;
  }

  /**
   * This method is used to set reward discount id
   * @param rewardDiscountId String
   */
  public void setRewardCard(RewardCard rewardCard) {
    doSetRewardCard(rewardCard);
  }
}

