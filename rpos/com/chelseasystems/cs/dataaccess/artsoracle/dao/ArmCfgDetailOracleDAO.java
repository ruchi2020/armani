/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  com.chelseasystems.cs.config.*;
import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  java.sql.*;
import  java.util.*;


/**
 * <p>Title: ArmCfgDetailOracleDAO </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class ArmCfgDetailOracleDAO extends BaseOracleDAO
    implements ArmCfgDetailDAO {

  /**
   * put your documentation comment here
   * @param sCountry
   * @param sLanguage
   * @return 
   * @exception SQLException
   */
  public ArmConfig selectByCountryAndLanguage (String sCountry, String sLanguage) throws SQLException {
    String sSelectSQL = ArmCfgDetailOracleBean.selectSql + where(ArmCfgDetailOracleBean.COL_ED_CO, ArmCfgDetailOracleBean.COL_ED_LA) + " ORDER BY " + ArmCfgDetailOracleBean.COL_TY_CFG + " , " + ArmCfgDetailOracleBean.COL_PRIORITY + "," + ArmCfgDetailOracleBean.COL_DE_CFG;
    List params = new ArrayList();
    params.add(sCountry);
    params.add(sLanguage);
    ArmConfigDetail[] configDetails = fromBeansToObjects(query(new ArmCfgDetailOracleBean(), sSelectSQL, params));
    ArmConfig armConfig = new ArmConfig();
    if (configDetails == null || configDetails.length == 0)
      return  null;
    String sTmp;
    sTmp = configDetails[0].getConfigType();
    List listConfigDetails = new ArrayList();
    for (int iCtr = 0; iCtr < configDetails.length; iCtr++) {
      if (!configDetails[iCtr].getConfigType().equals(sTmp)) {
        armConfig.doAddConfigDetail(sTmp, listConfigDetails);
        sTmp = configDetails[iCtr].getConfigType();
        listConfigDetails = new ArrayList();
      }
      listConfigDetails.add(configDetails[iCtr]);
    }
    armConfig.doAddConfigDetail(sTmp, listConfigDetails);
    return  armConfig;
  }

  /**
   * put your documentation comment here
   * @return 
   */
  protected BaseOracleBean getDatabeanInstance () {
    return  new ArmCfgDetailOracleBean();
  }

  /**
   * put your documentation comment here
   * @param beans
   * @return 
   */
  private ArmConfigDetail[] fromBeansToObjects (BaseOracleBean[] beans) {
    ArmConfigDetail[] array = new ArmConfigDetail[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = fromBeanToObject(beans[i]);
    return  array;
  }

  /**
   * put your documentation comment here
   * @param baseBean
   * @return 
   */
  private ArmConfigDetail fromBeanToObject (BaseOracleBean baseBean) {
    ArmCfgDetailOracleBean bean = (ArmCfgDetailOracleBean)baseBean;
    ArmConfigDetail object = new ArmConfigDetail();
    object.doSetConfigType(bean.getTyCfg());
    object.doSetCode(bean.getCdCfg());
    object.doSetDescription(bean.getDeCfg());
    //Ruchi: 1800 - sort discounts by Code/Priority - Japan
    object.doSetPriority(bean.getPriority());
    return  object;
  }
}



