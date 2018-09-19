/**
 * Description:ArmCurrencyRateOracleDAO
 * Created By:Khyati Shah
 * Created Date:1/24/2005
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/**
 * History (tab separated):-
 * Vers	Date		By	Spec		                Description
 * 1	1/24/05	        KS	POS_IS_CurrencyRate_Rev1	ArmCurrencyRateOracleDAO
 *
 */


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  com.chelseasystems.cs.currency.CurrencyRate;
import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  java.util.Date;
import  java.sql.SQLException;
import  java.util.*;


/**
 * put your documentation comment here
 */
public class ArmCurrencyRateOracleDAO extends BaseOracleDAO
    implements ArmCurrencyRateDAO {
  private static String selectSql = ArmCurrencyRateOracleBean.selectSql;
  private static String insertSql = ArmCurrencyRateOracleBean.insertSql;
  private static String updateSql = ArmCurrencyRateOracleBean.updateSql + "where ID_CNY_FROM = ?";
  private static String deleteSql = ArmCurrencyRateOracleBean.deleteSql + "where ID_CNY_FROM = ?";

  /**
   *
   * @param idCnyFrom String
   * @throws SQLException
   * @return CurrencyRate[]
   */
  public CurrencyRate[] selectByCurrencyFrom (String idCnyFrom) throws SQLException {
    String whereSql = "where ID_CNY_FROM = ?";
    return  fromBeansToObjects(query(new ArmCurrencyRateOracleBean(), selectSql + whereSql, idCnyFrom));
  }

  /**
   *
   * @param idCnyTo String
   * @throws SQLException
   * @return CurrencyRate[]
   */
  public CurrencyRate[] selectByCurrencyTo (String idCnyTo) throws SQLException {
    String whereSql = "where ID_CNY_TO = ?";
    return  fromBeansToObjects(query(new ArmCurrencyRateOracleBean(), selectSql + whereSql, idCnyTo));
  }

  /**
   *
   * @param moRtToBuy Long
   * @throws SQLException
   * @return CurrencyRate[]
   */
  public CurrencyRate[] selectByCurrencyRate (Long moRtToBuy) throws SQLException {
    String whereSql = "where MO_RT_TO_BUY = ?";
    return  fromBeansToObjects(query(new ArmCurrencyRateOracleBean(), selectSql + whereSql, moRtToBuy));
  }

  /**
   *
   * @param updateDt Date
   * @throws SQLException
   * @return CurrencyRate[]
   */
  public CurrencyRate[] selectByUpdateDt (Date updateDt) throws SQLException {
    String whereSql = "where UPDATE_DT = ?";
    return  fromBeansToObjects(query(new ArmCurrencyRateOracleBean(), selectSql + whereSql, updateDt));
  }

  /**
   *
   * @throws SQLException
   * @return CurrencyRate[]
   */
  public CurrencyRate[] selectAll () throws SQLException {
    String sql = "select * from " + ArmCurrencyRateOracleBean.TABLE_NAME;
    List params = new ArrayList();
    return  fromBeansToObjects(query(new ArmCurrencyRateOracleBean(), sql, params));
  }

  /**
   * put your documentation comment here
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception SQLException
   */
  public CurrencyRate[] selectAll (String sCountry, String sLanguage) throws SQLException {
    String sSelectSQL = ArmCurrencyRateOracleBean.selectSql + where(ArmCurrencyRateOracleBean.COL_ED_CO, ArmCurrencyRateOracleBean.COL_ED_LA);
    List params = new ArrayList();
    params.add(sCountry);
    params.add(sLanguage);
    return  fromBeansToObjects(query(new ArmCurrencyRateOracleBean(), sSelectSQL, params));
  }

  //
  //Non public methods begin here!
  //
  /**
   *
   * @return BaseOracleBean
   */
  protected BaseOracleBean getDatabeanInstance () {
    return  new ArmCurrencyRateOracleBean();
  }

  /**
   *
   * @param beans BaseOracleBean[]
   * @return CurrencyRate[]
   */
  private CurrencyRate[] fromBeansToObjects (BaseOracleBean[] beans) {
    CurrencyRate[] array = new CurrencyRate[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = fromBeanToObject(beans[i]);
    return  array;
  }

  /**
   *
   * @param baseBean BaseOracleBean
   * @return CurrencyRate
   */
  private CurrencyRate fromBeanToObject (BaseOracleBean baseBean) {
    ArmCurrencyRateOracleBean bean = (ArmCurrencyRateOracleBean)baseBean;
    CurrencyRate object = new CurrencyRate();
    object.doSetFromCurrency(bean.getIdCnyFrom());
    object.doSetToCurrency(bean.getIdCnyTo());
    object.doSetConversionRate(bean.getMoRtToBuy());
    object.doSetUpdateDate(bean.getUpdateDt());
    object.setTenderCode(bean.getCdFin());
    return  object;
  }

  /**
   *
   * @param object CurrencyRate
   * @return ArmCurrencyRateOracleBean
   */
  private ArmCurrencyRateOracleBean fromObjectToBean (CurrencyRate object) {
    ArmCurrencyRateOracleBean bean = new ArmCurrencyRateOracleBean();
    bean.setIdCnyFrom(object.getFromCurrency());
    bean.setIdCnyTo(object.getToCurrency());
    bean.setMoRtToBuy(object.getConversionRate());
    bean.setUpdateDt(object.getUpdateDate());
    bean.setCdFin(object.getTenderCode());
    return  bean;
  }
}



