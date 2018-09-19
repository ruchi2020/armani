/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.eod;

import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.business.BusinessObject;


/**
 * <p>Title: </p>
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
public class CMSEODNonDepositTotal extends BusinessObject {
  private String typeCode = null;
  private int mediaCount = 0;
  private ArmCurrency eodTotal = null;
  private ArmCurrency reportedEODTotal = null;
  private String groupCode = null;

  /**
   * put your documentation comment here
   */
  public CMSEODNonDepositTotal() {
    eodTotal = new ArmCurrency(0.0d);
    reportedEODTotal = new ArmCurrency(0.0d);
    typeCode = null;
    mediaCount = 0;
  }

  /**
   * put your documentation comment here
   * @param   String typecode
   * @param   ArmCurrency eodtotal
   * @param   int mediacount
   * @param   String groupcode
   */
  public CMSEODNonDepositTotal(String typecode, ArmCurrency eodtotal, ArmCurrency reportedEODTotal, int mediacount, String groupcode) {
    this.typeCode = typecode;
    this.eodTotal = eodtotal;
    this.reportedEODTotal = reportedEODTotal;
    this.mediaCount = mediacount;
    this.groupCode = groupcode;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getTypeCode() {
    return this.typeCode;
  }

  /**
   * put your documentation comment here
   * @param typecode
   */
  public void setTypeCode(String typecode) {
    doSetTypeCode(typecode);
  }

  /**
   * put your documentation comment here
   * @param typecode
   */
  public void doSetTypeCode(String typecode) {
    this.typeCode = typecode;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getGroupCode() {
    return this.groupCode;
  }

  /**
   * put your documentation comment here
   * @param groupcode
   */
  public void setGroupCode(String groupcode) {
    doSetGroupCode(groupcode);
  }

  /**
   * put your documentation comment here
   * @param groupcode
   */
  public void doSetGroupCode(String groupcode) {
    this.groupCode = groupcode;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getEODTotal() {
    return this.eodTotal;
  }

  /**
   * put your documentation comment here
   * @param eodtotal
   */
  public void setEODTotal(ArmCurrency eodtotal) {
    doSetEODTotal(eodtotal);
  }

  /**
   * put your documentation comment here
   * @param eodtotal
   */
  public void doSetEODTotal(ArmCurrency eodtotal) {
    this.eodTotal = eodtotal;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getReportedEODTotal() {
    return this.reportedEODTotal;
  }

  /**
   * put your documentation comment here
   * @param eodtotal
   */
  public void setReportedEODTotal(ArmCurrency eodtotal) {
    doSetReportedEODTotal(eodtotal);
  }

  /**
   * put your documentation comment here
   * @param eodtotal
   */
  public void doSetReportedEODTotal(ArmCurrency eodtotal) {
    this.reportedEODTotal = eodtotal;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getMediaCount() {
    return this.mediaCount;
  }

  /**
   * put your documentation comment here
   * @param count
   */
  public void setMediaCount(int count) {
    doSetMediaCount(count);
  }

  /**
   * put your documentation comment here
   * @param count
   */
  public void doSetMediaCount(int count) {
    this.mediaCount = count;
  }
}

