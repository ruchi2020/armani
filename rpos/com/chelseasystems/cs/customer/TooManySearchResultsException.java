/**
 * Thrown by search queries when there are too many search results for a query.
 *
 * @author Jin Zhu
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.customer;


/**
 * put your documentation comment here
 */
public class TooManySearchResultsException extends Exception {

  /**
   * put your documentation comment here
   */
  public TooManySearchResultsException() {
    super();
  }

  /**
   * put your documentation comment here
   * @param   String s
   */
  public TooManySearchResultsException(String s) {
    super(s);
  }
}

