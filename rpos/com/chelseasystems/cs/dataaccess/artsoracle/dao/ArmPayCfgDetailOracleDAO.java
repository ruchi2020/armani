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
 * <p>Title: ArmPayCfgDetailOracleDAO </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Sumit Krishnan
 * @version 1.0
 */
public class ArmPayCfgDetailOracleDAO extends BaseOracleDAO
    implements ArmPayCfgDetailDAO {

  /**
   * put your documentation comment here
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception SQLException
   */
  public ArmPayConfigDetail[] selectByCountryAndLanguage(String sCountry, String sLanguage)
		throws SQLException {
		// ___Tim: BUG 1713: Adding new credit card info needs additional sorting.
		String sSelectSQL = ArmGrpClsTndOracleBean.selectSql + where(
			ArmGrpClsTndOracleBean.COL_ED_CO, ArmGrpClsTndOracleBean.COL_ED_LA) + " ORDER BY "
			+ ArmGrpClsTndOracleBean.COL_LU_CLS_TND + "," + ArmGrpClsTndOracleBean.COL_TND_CODE;
    List params = new ArrayList();
    params.add(sCountry);
    params.add(sLanguage);
		ArmPayConfigDetail[] configDetails = fromBeansToObjects(query(
			new ArmGrpClsTndOracleBean(), sSelectSQL, params));
    return  configDetails;
  }

  /**
   * Select CardPlans by Country and Language
   * @param sCountry String
   * @param sLanguage String
   * @return ArmPayPlanConfigDetail[]
   */
  public ArmPayPlanConfigDetail[] selectPlansByCountryAndLanguage(String sCountry, String sLanguage)
  {
    try
    {
      String sSelectSQL = VArmGrpPayPlanOracleBean.selectSql +
          where(VArmGrpPayPlanOracleBean.COL_ED_CO,
                VArmGrpPayPlanOracleBean.COL_ED_LA) + " ORDER BY " +
          VArmGrpPayPlanOracleBean.COL_TND_CODE + ", " +
          VArmGrpPayPlanOracleBean.COL_CRD_PLAN_CODE;
      List params = new ArrayList();
      params.add(sCountry);
      params.add(sLanguage);

      ArmPayPlanConfigDetail[] armPayPlans = toArmPayPlanConfigDetail(query(new
          VArmGrpPayPlanOracleBean(), sSelectSQL, params));
      return armPayPlans;
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }
  /**
   * put your documentation comment here
   * @return
   */
  protected BaseOracleBean getDatabeanInstance () {
    return  new ArmGrpClsTndOracleBean();
  }

  /**
   * put your documentation comment here
   * @param beans
   * @return
   */
  private ArmPayConfigDetail[] fromBeansToObjects (BaseOracleBean[] beans) {
    ArmPayConfigDetail[] array = new ArmPayConfigDetail[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = fromBeanToObject(beans[i]);
    return  array;
  }

  /**
   * put your documentation comment here
   * @param baseBean
   * @return
   */
  private ArmPayConfigDetail fromBeanToObject (BaseOracleBean baseBean) {
    ArmGrpClsTndOracleBean bean = (ArmGrpClsTndOracleBean)baseBean;
    ArmPayConfigDetail object = new ArmPayConfigDetail();
    object.setTenderType(bean.getLuClsTnd());
    object.setCode(bean.getTndCode());
    object.setDescription(bean.getDeClsTnd());
    return  object;
  }

  /**
   * Make ArmPayConfigDetail Objects from Beans.
   * @param beans BaseOracleBean[]
   * @return ArmPayPlanConfigDetail[]
   */
  private ArmPayPlanConfigDetail[] toArmPayPlanConfigDetail(BaseOracleBean[] beans)
  {
    ArmPayPlanConfigDetail armCardPlans[] = new ArmPayPlanConfigDetail[beans.length] ;
    for(int iCtr=0; iCtr<beans.length; iCtr++)
    {
      VArmGrpPayPlanOracleBean bean = (VArmGrpPayPlanOracleBean) beans[iCtr];
      armCardPlans[iCtr] = new ArmPayPlanConfigDetail();
      armCardPlans[iCtr].doSetTenderCode(bean.getTndCode());
      armCardPlans[iCtr].doSetCardPlanCode(bean.getCrdPlanCode());
      armCardPlans[iCtr].doSetCardPlanDescription(bean.getDeCrdPlan());
    }
    return armCardPlans;
  }

}



