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
 * *
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+------+------------+-----------+-----------+----------------------------------------------+
 | 1.0  | 07-20-2007 | Ruchi     | PCR       | Adding priority column 
 +------+------------+-----------+-----------+----------------------------------------------+
 */
import com.chelseasystems.cr.business.BusinessObject;


/**
 * put your documentation comment here
 */
public class ArmConfigDetail extends BusinessObject {
  private String sConfigType;
  private String sCode;
  private String sDesc;
  private String sPriority;

  /**
   * put your documentation comment here
   */
  public ArmConfigDetail() {
    sConfigType = new String();
    sCode = new String();
    sDesc = new String();
    sPriority = new String();
  }

  /**
   * put your documentation comment here
   * @param sConfigType
   */
  public void setConfigType(String sConfigType) {
    if (sConfigType == null) {
      return;
    }
    doSetConfigType(sConfigType);
  }

  /**
   * put your documentation comment here
   * @param sConfigType
   */
  public void doSetConfigType(String sConfigType) {
    this.sConfigType = sConfigType;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getConfigType() {
    return sConfigType;
  }

  /**
   * put your documentation comment here
   * @param sCode
   */
  public void setCode(String sCode) {
    if (sCode == null) {
      return;
    }
    doSetConfigType(sCode);
  }

  /**
   * put your documentation comment here
   * @param sCode
   */
  public void doSetCode(String sCode) {
    this.sCode = sCode;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getCode() {
    return sCode;
  }

  /**
   * put your documentation comment here
   * @param sDesc
   */
  public void setDescription(String sDesc) {
    if (sDesc == null) {
      return;
    }
    doSetConfigType(sDesc);
  }

  /**
   * put your documentation comment here
   * @param sDesc
   */
  public void doSetDescription(String sDesc) {
    this.sDesc = sDesc;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getDescription() {
    return sDesc;
  }
  
  public  String getPriority(){
	  	return sPriority;
  }
  
  public void setPriority(String sPriority) {
	if (sPriority == null) {
		return;
	}
	doSetConfigType(sPriority);
  }

  public void doSetPriority(String sPriority){
	  this.sPriority = sPriority;
  }
}
