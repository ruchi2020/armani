/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.txnposter;

import com.chelseasystems.cr.txnposter.SalesSummary;
import com.chelseasystems.cr.currency.ArmCurrency;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class CMSSalesSummary extends SalesSummary {
  private String itemDesc;
  private String vpn;
  private String deptId;
  private String deptDesc;
  private String classId;
  private String classDesc;
  private String type;
  private ArmCurrency markdownAmt;
  private String miscItemId;
  public static final String SALE_TYPE = "S";
  public static final String RETURN_TYPE = "R";

  /**
   * put your documentation comment here
   */
  public CMSSalesSummary() {
  }

  /**
   * put your documentation comment here
   * @param desc
   */
  public void doSetItemDesc(String desc) {
    this.itemDesc = desc;
  }

  /**
   * put your documentation comment here
   * @param desc
   */
  public void setItemDesc(String desc) {
    doSetItemDesc(desc);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getItemDesc() {
    return this.itemDesc;
  }

  /**
   * put your documentation comment here
   * @param vpn
   */
  public void doSetVPN(String vpn) {
    this.vpn = vpn;
  }

  /**
   * put your documentation comment here
   * @param vpn
   */
  public void setVPN(String vpn) {
    doSetVPN(vpn);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getVPN() {
    return this.vpn;
  }

  /**
   * put your documentation comment here
   * @param deptID
   */
  public void setDeptID(String deptID) {
    doSetDeptID(deptID);
  }

  /**
   * put your documentation comment here
   * @param deptID
   */
  public void doSetDeptID(String deptID) {
    this.deptId = deptID;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getDeptID() {
    return this.deptId;
  }

  /**
   * put your documentation comment here
   * @param desc
   */
  public void setDeptDesc(String desc) {
    doSetDeptDesc(desc);
  }

  /**
   * put your documentation comment here
   * @param desc
   */
  public void doSetDeptDesc(String desc) {
    this.deptDesc = desc;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getDeptDesc() {
    return this.deptDesc;
  }

  /**
   * put your documentation comment here
   * @param classID
   */
  public void setClassID(String classID) {
    doSetClassID(classID);
  }

  /**
   * put your documentation comment here
   * @param classID
   */
  public void doSetClassID(String classID) {
    this.classId = classID;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getClassID() {
    return this.classId;
  }

  /**
   * put your documentation comment here
   * @param desc
   */
  public void setClassDesc(String desc) {
    doSetClassDesc(desc);
  }

  /**
   * put your documentation comment here
   * @param desc
   */
  public void doSetClassDesc(String desc) {
    this.classDesc = desc;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getClassDesc() {
    return this.classDesc;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getMarkdownAmt() {
    return this.markdownAmt;
  }

  /**
   * put your documentation comment here
   * @param markdownAmt
   */
  public void setMarkdownAmt(ArmCurrency markdownAmt) {
    doSetMarkdownAmt(markdownAmt);
  }

  /**
   * put your documentation comment here
   * @param markdownAmt
   */
  public void doSetMarkdownAmt(ArmCurrency markdownAmt) {
    this.markdownAmt = markdownAmt;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getType() {
    return this.type;
  }

  /**
   * put your documentation comment here
   * @param type
   */
  public void setType(String type) {
    doSetType(type);
  }

  /**
   * put your documentation comment here
   * @param type
   */
  public void doSetType(String type) {
    this.type = type;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getMiscItemId() {
    return this.miscItemId;
  }

  /**
   * put your documentation comment here
   * @param miscItemId
   */
  public void setMiscItemId(String miscItemId) {
    doSetMiscItemId(miscItemId);
  }

  /**
   * put your documentation comment here
   * @param miscItemId
   */
  public void doSetMiscItemId(String miscItemId) {
    this.miscItemId = miscItemId;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean isReturn() {
    return (this.type == RETURN_TYPE);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean isSale() {
    return (this.type == SALE_TYPE);
  }
}

