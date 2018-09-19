/**
 * Copyright 1999-2001, Chelsea Market Systems
 History:
 +------+------------+-----------+-----------+-------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                     |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 2    | 04-28-2005 |Megha      |           |  Added new method for finding if user is
 |      |            |           |           |  Cashier or Manager                             |
 |      |            |           |           |                                                 |
 |      |            |           |           |                                                 |
 -----------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.register;

import java.util.Date;

import com.chelseasystems.cr.register.Register;
import com.chelseasystems.cr.store.Store;


/**
 *
 * <p>Title: CMSRegister</p>
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
public class CMSRegister extends Register implements com.chelseasystems.cr.rules.IRuleEngine {
  static final long serialVersionUID = 5457992836128242540L;
  static final char REGISTER_TYPE_CASHIER = 'C';
  static final char REGISTER_TYPE_MANAGER = 'M';
  /**
   * New Fields Added
   */
  private String RegisterType;
  private String RegisterDesc;
  private String OperatorID;
  private String RoundType;

	 //Vishal Yevale : Dec 2017 : POS_TS_NF25 Implementation PCR (FRANCE)
  private boolean isMonthEnd = false;
  private boolean isYearEnd = false;
  private double netAmountOfMonth;
  private double netAmountOfYear;
  private double netAmountOfDay;
  private boolean isCheckedEopAtSos = false;
  private boolean isEopPersisted = false;
  private Date currentBusinessDate;
  
  		  	

public Date getCurrentBusinessDate() {
	return currentBusinessDate;
}
public void setCurrentBusinessDate(Date currentBusinessDate) {
	this.currentBusinessDate = currentBusinessDate;
}
public boolean isCheckedEopAtSos() {
	return isCheckedEopAtSos;
}
public void setCheckedEopAtSos(boolean isCheckedEopAtSos) {
	this.isCheckedEopAtSos = isCheckedEopAtSos;
}
public boolean isEopPersisted() {
	return isEopPersisted;
}
public void setEopPersisted(boolean isEopPersisted) {
	this.isEopPersisted = isEopPersisted;
}
	public boolean isMonthEnd() {
  		return isMonthEnd;
  	}
  	public void setMonthEnd(boolean isMonthEnd) {
  		this.isMonthEnd = isMonthEnd;
  	}
  	public boolean isYearEnd() {
  		return isYearEnd;
  	}
  	public void setYearEnd(boolean isYearEnd) {
  		this.isYearEnd = isYearEnd;
  	}
  	public double getNetAmountOfMonth() {
  		return netAmountOfMonth;
  	}
  	public void setNetAmountOfMonth(Double netAmountOfMonth) {
  		this.netAmountOfMonth = netAmountOfMonth;
  	}
  	public double getNetAmountOfYear() {
  		return netAmountOfYear;
  	}
  	public void setNetAmountOfYear(Double netAmountOfYear) {
  		this.netAmountOfYear = netAmountOfYear;
  	}
  	public double getNetAmountOfDay() {
  		return netAmountOfDay;
  	}
  	public void setNetAmountOfDay(Double netAmountOfDay) {
  		this.netAmountOfDay = netAmountOfDay;
  	}
	 //Vishal Yevale : Dec 2017 : POS_TS_NF25 Implementation PCR (FRANCE)
      
      

  /**
   * Get rounding type
   * @return String
   */
  public String getRoundType() {
    return RoundType;
  }

  /**
   * Set rounding type
   * @param roundType String
   */
  public void doSetRoundType(String roundType) {
    RoundType = roundType;
  }

  /**
   * Constructor
   * @param id String
   * @param storeId String
   */
  public CMSRegister(String id, String storeId) {
    super(id, storeId);
  }

  /**
   * Returns operator id
   * @return String
   */
  public String getOperatorID() {
    return OperatorID;
  }

  /**
   * set operator id
   * @param opID String
   */
  public void doSetOperatorID(String opID) {
    OperatorID = opID;
  }

  /**
   * Return register type
   * @return String
   */
  public String getRegisterType() {
    return RegisterType;
  }

  /**
   * Set register type
   * @param RegType String
   */
  public void doSetRegisterType(String RegType) {
    RegisterType = RegType;
  }

  /**
   * Return register description
   * @return String
   */
  public String getRegisterDesc() {
    return RegisterDesc;
  }

  /**
   * Set register description
   * @param RegDesc String
   */
  public void doSetRegisterDesc(String RegDesc) {
    RegisterDesc = RegDesc;
  }

  /**
   * Added the method to find if the manager/cashier
   */
  public boolean isRegisterTypeCashier(String RegType) {
    if (RegType.equals("REGISTER_TYPE_CASHIER")) {
      return true;
    } else {
      return false;
    }
  }

  /**
   *
   * @param RegType String
   * @return boolean
   */
  public boolean isRegisterTypeManager(String RegType) {
    if (RegType.equals("REGISTER_TYPE_MANAGER")) {
      return true;
    } else {
      return false;
    }
  }
}

