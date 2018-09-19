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
 | 1    | 08-24-2005 | Manpreet  | N/A       | Reads AlterationItemGroup from DB for Alterations.xml |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
import  com.chelseasystems.cs.pos.*;
import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  java.sql.*;
import  java.util.*;


/**
 *
 * <p>Title: ArmAlternGrpOracleDAO</p>
 * <p>Description:Reads AlterationItemGroup from DB for Alterations.xml </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class ArmAlternGrpOracleDAO extends BaseOracleDAO
    implements ArmAlternGrpDAO {
  /**
   * Select SQL
   */
  private static String sSelectSQL = ArmAlternGrpOracleBean.selectSql;

  /**
   * Select AlterationItemGroup by Country and Language (Locale of Store)
   * @param sCountry String
   * @param sLanguage String
   * @throws SQLException
   * @return AlterationItemGroup[]
   */
  public AlterationItemGroup[] selectByCountryAndLanguage (String sCountry, String sLanguage) throws SQLException {
    try {
      String sWhereSQL = where(ArmAlternGrpOracleBean.COL_ED_CO, ArmAlternGrpOracleBean.COL_ED_LA);
      List params = new ArrayList();
      params.add(sCountry);
      params.add(sLanguage);
      return  fromBeansToObjects(query(new ArmAlternGrpOracleBean(), sSelectSQL + sWhereSQL, params));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return  null;
  }

  /**
   * Select all Groups
   * @throws SQLException
   * @return AlterationItemGroup[]
   */
  public AlterationItemGroup[] selectAllGroups () throws SQLException {
    try {
      return  fromBeansToObjects(query(new ArmAlternGrpOracleBean(), sSelectSQL, null));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return  null;
  }

  /**
   * Select AlterationItemGroup by Id
   * @param groupId String
   * @throws SQLException
   * @return AlterationItemGroup[]
   */
  public AlterationItemGroup[] selectByGroupId (String groupId) throws SQLException {
    try {
      String sWhereSQL = where(ArmAlternGrpOracleBean.COL_GROUP_ID);
      return  fromBeansToObjects(query(new ArmAlternGrpOracleBean(), sSelectSQL + sWhereSQL, groupId));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return  null;
  }

  /**
   * Get DatabeanInstance
   * @return BaseOracleBean
   */
  protected BaseOracleBean getDatabeanInstance () {
    return  new ArmAlternGrpOracleBean();
  }

  /**
   * Create Objects
   * @param beans BaseOracleBean[]
   * @throws Exception
   * @return AlterationItemGroup[]
   */
  private AlterationItemGroup[] fromBeansToObjects (BaseOracleBean[] beans) throws Exception {
    AlterationItemGroup[] array = new AlterationItemGroup[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = fromBeanToObject(beans[i]);
    return  array;
  }

  /**
   * Create AlterationItemGroup object.
   * @param baseBean BaseOracleBean
   * @throws Exception
   * @return AlterationItemGroup
   */
  private AlterationItemGroup fromBeanToObject (BaseOracleBean baseBean) throws Exception {
    int iCtr = 0;
    ArmAlternGrpOracleBean bean = (ArmAlternGrpOracleBean)baseBean;
    AlterationItemGroup object = new AlterationItemGroup();
    object.doSetGroupName(bean.getGroupName());
    ArmAlternClssGrpOracleDAO armAlternClssGrpOracleDAO = new ArmAlternClssGrpOracleDAO();
    String subGroups[] = armAlternClssGrpOracleDAO.selectByGroupId(bean.getGroupId());
    if (subGroups != null) {
      for (iCtr = 0; iCtr < subGroups.length; iCtr++) {
        object.doAddSubGroup(subGroups[iCtr]);
      }
    }
    ArmAlternCodeOracleDAO armAlternCodeOracleDAO = new ArmAlternCodeOracleDAO();
    AlterationDetail alterationDetails[] = armAlternCodeOracleDAO.selectByGroupId(bean.getGroupId());
    if (alterationDetails != null) {
      for (iCtr = 0; iCtr < alterationDetails.length; iCtr++) {
        object.doAddAlterationDetail(alterationDetails[iCtr]);
      }
    }
    return  object;
  }
}



