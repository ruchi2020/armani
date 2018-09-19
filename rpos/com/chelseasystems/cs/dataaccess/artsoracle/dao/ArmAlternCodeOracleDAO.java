/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 08-24-2005 | Manpreet  | N/A       | Reads AlterationDetail from DB for Alterations.xml |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
import  com.chelseasystems.cs.pos.*;
import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cr.currency.ArmCurrency;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  java.sql.*;


/**
 *
 * <p>Title: ArmAlternCodeOracleDAO </p>
 * <p>Description: Reads AlterationDetail from DB for Alterations.xml</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class ArmAlternCodeOracleDAO extends BaseOracleDAO
    implements ArmAlternCodeDAO {
  /**
   * Select SQL
   */
  private static String selectSql = ArmAlternCodeOracleBean.selectSql;

  /**
   * Select AlterationDetail for a Group
   * @param sGroupId String
   * @throws SQLException
   * @return AlterationDetail[]
   */
  public AlterationDetail[] selectByGroupId (String sGroupId) throws SQLException {
    String whereSql = where(ArmAlternCodeOracleBean.COL_GROUP_ID);
    return  fromBeansToObjects(query(new ArmAlternCodeOracleBean(), selectSql + whereSql, sGroupId));
  }

  /**
   * Get dataBeanInstance
   * @return BaseOracleBean
   */
  protected BaseOracleBean getDatabeanInstance () {
    return  new ArmAlternCodeOracleBean();
  }

  /**
   * Beans to
   * @param beans BaseOracleBean[]
   * @return AlterationDetail[]
   */
  private AlterationDetail[] fromBeansToObjects (BaseOracleBean[] beans) {
    AlterationDetail[] array = new AlterationDetail[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = fromBeanToObject(beans[i]);
    return  array;
  }

  /**
   * put your documentation comment here
   * @param baseBean
   * @return 
   */
  private AlterationDetail fromBeanToObject (BaseOracleBean baseBean) {
    ArmAlternCodeOracleBean bean = (ArmAlternCodeOracleBean)baseBean;
    AlterationDetail object = new AlterationDetail();
    object.doSetAlterationCode(bean.getAlternCode());
    object.doSetDescription(bean.getAlternDesc());
    object.doSetEstimatedPrice(bean.getAlternPrice());
    object.doSetEstimatedTime(bean.getAlternTime());
    return  object;
  }
}



