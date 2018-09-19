/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.customer;

import com.chelseasystems.cr.rules.*;


/**
 * <p>
 * Title: EmailAddressIsValid
 * </p>
 * <p>
 * Description: This business rule checks email address format, it should be
 * some characters, then an @ symbol, then more characters. If invalid format,
 * throw the 'Invalid Email Address' business rule.
 * </p>
 */
public class EmailAddressIsValid extends Rule {

  /**
   * put your documentation comment here
   */
  public EmailAddressIsValid() {
  }

  /**
   * put your documentation comment here
   * @param theParent
   * @param args[]
   * @return
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return (execute((String)args[0]));
  }

  /**
   * put your documentation comment here
   * @param email
   * @return
   */
  private RulesInfo execute(String email) {
    if (email != null && email.length() > 0) {
      int atIndex = email.indexOf('@');
      if ((atIndex == -1) || (atIndex == 0) || (atIndex == email.length() - 1))
        return (new RulesInfo("Invalid Email Address."));
    }
    return (new RulesInfo());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getName() {
    return "EmailAddressIsValid";
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getDesc() {
    return "Email address must be valid.";
  }
}

