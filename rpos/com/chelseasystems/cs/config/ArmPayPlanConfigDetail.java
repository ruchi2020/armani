package com.chelseasystems.cs.config;

import com.chelseasystems.cr.business.BusinessObject;

/**
 * <p>Title:ArmPayPlanConfigDetail </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Skillnet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */

public class ArmPayPlanConfigDetail extends BusinessObject {

  private String sTenderCode;
  private String sCardPlanCode;
  private String sCardPlanDescription;

  public ArmPayPlanConfigDetail() {
  }

  public String getTenderCode() {
    return this.sTenderCode;
  }

  public void setTenderCode(String sTenderCode) {
    if (sTenderCode == null)return;
    doSetTenderCode(sTenderCode);
  }

  public void doSetTenderCode(String sTenderCode) {
    this.sTenderCode = sTenderCode;
  }

  public String getCardPlanCode() {
    return this.sCardPlanCode;
  }

  public void setCardPlanCode(String sCardPlanCode) {
    if (sCardPlanCode == null)return;
    doSetCardPlanCode(sCardPlanCode);
  }

  public void doSetCardPlanCode(String sCardPlanCode) {
    this.sCardPlanCode = sCardPlanCode;
  }

  public String getCardPlanDescription() {
    return this.sCardPlanDescription;
  }

  public void setCardPlanDescription(String sCardPlanDescription) {
    if (sCardPlanDescription == null)return;
    doSetCardPlanDescription(sCardPlanDescription);
  }

  public void doSetCardPlanDescription(String sCardPlanDescription) {
    this.sCardPlanDescription = sCardPlanDescription;
  }

}
