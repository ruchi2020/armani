/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.pos.datatranssferobject;


/**
 * put your documentation comment here
 */
public class ConsignmentSummaryItemData implements java.io.Serializable {
  static final long serialVersionUID = -2656441402125884274L;
  private String units = "";
  private String dollarAmount = "";

  /**
   * put your documentation comment here
   */
  public ConsignmentSummaryItemData() {
  }

  /**
   * put your documentation comment here
   * @param   String units
   * @param   String dollarAmount
   */
  public ConsignmentSummaryItemData(String units, String dollarAmount) {
    this.units = units;
    this.dollarAmount = dollarAmount;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean isEmply() {
    return (units == null || units.length() == 0)
        && (dollarAmount == null || dollarAmount.length() == 0);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getDollarAmount() {
    return dollarAmount;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getUnits() {
    return units;
  }
}

