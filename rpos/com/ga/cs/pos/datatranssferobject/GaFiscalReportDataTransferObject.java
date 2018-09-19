/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.pos.datatranssferobject;

import java.util.Vector;
import java.util.Enumeration;


/**
 * put your documentation comment here
 */
public class GaFiscalReportDataTransferObject implements java.io.Serializable {
  static final long serialVersionUID = -8738978914706138525L;
  private String fiscalDay;
  private Vector tranDetails;

  /**
   * put your documentation comment here
   */
  public GaFiscalReportDataTransferObject() {
    tranDetails = new Vector();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getFiscalDay() {
    return fiscalDay;
  }

  /**
   * put your documentation comment here
   * @param fiscalDay
   */
  public void setFiscalDay(String fiscalDay) {
    this.fiscalDay = fiscalDay;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Enumeration getTranFiscalReports() {
    return tranDetails.elements();
  }

  /**
   * put your documentation comment here
   * @param detail
   */
  public void addTranDetails(GaTranFiscalReportDataTransferObject detail) {
    this.tranDetails.addElement(detail);
  }
}

