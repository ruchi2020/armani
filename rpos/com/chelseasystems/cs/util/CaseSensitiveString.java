/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 09/13/2005 | Sumit Krishnan    | 989       |                  |
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.util;


/**
 * put your documentation comment here
 */
public class CaseSensitiveString {
  String caseSensitiveString;

  /**
   * put your documentation comment here
   * @param   Object str
   */
  public CaseSensitiveString(Object str) {
    caseSensitiveString = (String)str;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String toString() {
    return this.caseSensitiveString;
  }
}

