/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.config;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
import com.chelseasystems.cr.business.BusinessObject;


/**
 * put your documentation comment here
 */
public class ArmPayConfigDetail extends BusinessObject {
  private String sTenderType;
  private String sCode;
  private String sDesc;

  /**
   * put your documentation comment here
   */
  public ArmPayConfigDetail() {
    sTenderType = new String();
    sCode = new String();
    sDesc = new String();
  }

  /**
   * put your documentation comment here
   * @param sTenderType
   */
  public void setTenderType(String sTenderType) {
    this.sTenderType = sTenderType;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getTenderType() {
    return this.sTenderType;
  }

  /**
   * put your documentation comment here
   * @param sCode
   */
  public void setCode(String sCode) {
    this.sCode = sCode;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getCode() {
    return this.sCode;
  }

  /**
   * put your documentation comment here
   * @param sDesc
   */
  public void setDescription(String sDesc) {
    this.sDesc = sDesc;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getDescription() {
    return this.sDesc;
  }
}
