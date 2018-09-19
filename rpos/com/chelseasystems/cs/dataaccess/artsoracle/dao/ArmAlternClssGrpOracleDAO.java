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
 | 1    | 08-24-2005 | Manpreet  | N/A       | Reads SubGroups    from DB for Alterations.xml     |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  java.sql.*;


/**
 *
 * <p>Title:ArmAlternClssGrpOracleDAO </p>
 * <p>Description:Reads SubGroups    from DB for Alterations.xml </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class ArmAlternClssGrpOracleDAO extends BaseOracleDAO
    implements ArmAlternClssGrpDAO {
  /**
   * SelectSQL
   */
  private static String selectSql = ArmAlternClssGrpOracleBean.selectSql;

  /**
   * Select SubGroups for a Group
   * @param groupId String
   * @throws SQLException
   * @return String[]
   */
  public String[] selectByGroupId (String groupId) throws SQLException {
    String whereSql = where(ArmAlternClssGrpOracleBean.COL_GROUP_ID);
    return  fromBeansToObjects(query(new ArmAlternClssGrpOracleBean(), selectSql + whereSql, groupId));
  }

  /**
   * Get Databean instance
   * @return BaseOracleBean
   */
  protected BaseOracleBean getDatabeanInstance () {
    return  new ArmAlternClssGrpOracleBean();
  }

  /**
   * Get objects.
   * @param beans BaseOracleBean[]
   * @return String[]
   */
  private String[] fromBeansToObjects (BaseOracleBean[] beans) {
    String[] array = new String[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = fromBeanToObject(beans[i]);
    return  array;
  }

  /**
   * Create SubGroup object.
   * @param baseBean BaseOracleBean
   * @return String
   */
  private String fromBeanToObject (BaseOracleBean baseBean) {
    ArmAlternClssGrpOracleBean bean = (ArmAlternClssGrpOracleBean)baseBean;
    String object = new String(bean.getSubGroupId());
    return  object;
  }
}



