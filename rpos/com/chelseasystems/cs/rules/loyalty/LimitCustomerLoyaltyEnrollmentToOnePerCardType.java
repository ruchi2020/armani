/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 08-16-2005 | Vikram    | 878       | This class checks the business rule to limit |
 |      |            |           |           | Customer Loyalty Enrollment to one per card  |
 |      |            |           |           | type                                         |
 +------+------------+-----------+-----------+----------------------------------------------+
 */


package com.chelseasystems.cs.rules.loyalty;

import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cs.loyalty.Loyalty;
import com.chelseasystems.cr.swing.CMSApplet;


/**
 * <p>Title: LimitCustomerLoyaltyEnrollmentToOnePerCardType</p>
 * <p>Description: This class checks the business rule to limit Customer Loyalty
 * Enrollment to one per card type</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Vikram
 * @version 1.0
 */
public class LimitCustomerLoyaltyEnrollmentToOnePerCardType extends Rule {

  /**
   */
  public LimitCustomerLoyaltyEnrollmentToOnePerCardType() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of Loyalty
   * @param args - args[0] => (Boolean) reIssue;  args[1] => (Loyalty[]) existingLoyalties
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((Loyalty)theParent, ((Boolean)args[0]).booleanValue(), (Loyalty[])args[1]);
  }

  /**
   * Execute the Rule
   * @param loyalty - the new Loyalty object
   * @param reIssue boolean
   * @param existingLoyalties Loyalty[]
   */
  private RulesInfo execute(Loyalty loyalty, boolean reIssue, Loyalty[] existingLoyalties) {
    try {
      if (!reIssue && existingLoyalties != null)
        for (int i = 0; i < existingLoyalties.length; i++) {
          if (loyalty.getStoreType().equals(existingLoyalties[i].getStoreType()))
            return new RulesInfo(CMSApplet.res.getString(
                "The customer is already enrolled for this card type"));
        }
    } catch (Exception ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
          , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
      ex.printStackTrace();
    }
    return new RulesInfo();
  }

  /**
   * Returns the name of the Rule.
   * @return the name of the rule
   */
  public String getName() {
    return CMSApplet.res.getString("Customer Loyalty enrollment is limited to one per card type");
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    return CMSApplet.res.getString(
        "Rule should ensure that Customer Loyalty enrollment is limited to one per card type");
  }
}

