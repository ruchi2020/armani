//
// Copyright 2002, Retek Inc. All Rights Reserved.
//
package com.chelseasystems.cs.dataaccess.artsoracle.dao;

import com.chelseasystems.cs.config.ArmDiscountRule;
import com.chelseasystems.cs.dataaccess.*;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import java.sql.*;
import java.util.*;

public class ArmDscRuleOracleDAO extends BaseOracleDAO implements ArmDscRuleDAO {
 
  /**
   * put your documentation comment here
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception SQLException
   */
  public ArmDiscountRule[] selectDiscountRuleByCountryAndLanguage (String sCountry, String sLanguage) throws SQLException {
    String sSelectSQL = ArmDscRuleOracleBean.selectSql + where(ArmDscRuleOracleBean.COL_ED_CO, ArmDscRuleOracleBean.COL_ED_LA);
    List params = new ArrayList();
    params.add(sCountry);
    params.add(sLanguage);
    ArmDiscountRule[] discountRules = fromBeansToObjects(query(new ArmDscRuleOracleBean(), sSelectSQL, params));
    if (discountRules == null || discountRules.length == 0)
      return null;
    return discountRules;
  }
  
  /**
   * put your documentation comment here
   * @return
   */
  protected BaseOracleBean getDatabeanInstance() {
    return new ArmDscRuleOracleBean();
  }
  
  /**
   * put your documentation comment here
   * @param beans
   * @return
   */
  private ArmDiscountRule[] fromBeansToObjects (BaseOracleBean[] beans) {
	ArmDiscountRule[] array = new ArmDiscountRule[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = fromBeanToObject(beans[i]);
    return array;
  }
  
  /**
   * put your documentation comment here
   * @param baseBean
   * @return
   */
  private ArmDiscountRule fromBeanToObject(BaseOracleBean baseBean) {
    ArmDscRuleOracleBean bean = (ArmDscRuleOracleBean) baseBean;
    ArmDiscountRule object = new ArmDiscountRule();
    object.doSetCdDsc(bean.getCdDsc());
    object.doSetStartRange(bean.getStartRange());
    object.doSetEndRange(bean.getEndRange());
    object.doSetIsDscPercent(bean.getIsDscPercent());
    object.doSetPercent(bean.getPercent());
    object.doSetMoDsc(bean.getMoDsc());
    object.doSetCdNote(bean.getCdNote());
    object.doSetEdCo(bean.getEdCo());
    object.doSetEdLa(bean.getEdLa());
    return object;
  }
 
  /**
   * put your documentation comment here
   * @param object
   * @return
   */
  private ArmDscRuleOracleBean fromObjectToBean(ArmDiscountRule object) {
    ArmDscRuleOracleBean bean = new ArmDscRuleOracleBean();
    bean.setCdDsc(object.getCdDsc());
    bean.setStartRange(object.getStartRange());
    bean.setEndRange(object.getEndRange());
    bean.setIsDscPercent(object.getIsDscPercent());
    bean.setPercent(object.getPercent());
    bean.setMoDsc(object.getMoDsc());
    bean.setCdNote(object.getCdNote());
    bean.setEdCo(object.getEdCo());
    bean.setEdLa(object.getEdLa());
    return bean;
  }

}
