/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 02-27-2005 | Anand     | N/A       | Modified to add methods to facilitate deletion|
 |      |            |           |           | and updation of of records in DB              |
 --------------------------------------------------------------------------------------------
 */


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

/////put your import statemants here.
import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  com.chelseasystems.cs.rms.CMSPosPromDetail;
import  java.util.Date;
import  java.sql.*;
import  java.util.*;
import  com.chelseasystems.cr.database.*;


/**
 * put your documentation comment here
 */
public class ArmStgPrmOracleDAO extends BaseOracleDAO
    implements ArmStgPrmDAO {
  private static String selectSql = ArmStgPrmOracleBean.selectSql;
  private static String insertSql = ArmStgPrmOracleBean.insertSql;
  private static String deleteSql = ArmStgPrmOracleBean.deleteSql;

  /**
   * put your documentation comment here
   * @param object
   * @exception SQLException
   */
  public void insert (CMSPosPromDetail object) throws SQLException {
    execute(getInsertSQL(object));
  }

  /**
   * put your documentation comment here
   * @param object
   * @exception SQLException
   */
  public void delete (CMSPosPromDetail object) throws SQLException {
    execute(getDeleteSQL(object.getPromotionNum()));
  }

  /**
   * put your documentation comment here
   * @param object
   * @exception SQLException
   */
  public void update (CMSPosPromDetail object) throws SQLException {
    Calendar now = Calendar.getInstance();
    Date processDate = now.getTime();
    //SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    //String startDate = formatter.format(nowDate);
    execute(getUpdateSQL(object));
  }

  /**
   * put your documentation comment here
   * @param object
   * @return 
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (CMSPosPromDetail object) throws SQLException {
    List statements = new ArrayList();
    ArmStgPrmOracleBean armStgPrmBean = this.toArmStgPrmBean(object);
    statements.add(new ParametricStatement(armStgPrmBean.getInsertSql(), armStgPrmBean.toList()));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /*public ParametricStatement getDeleteSQL(String stgStatus) throws SQLException {
   List params = new ArrayList();
   //params.add(stgStatus);
   return new ParametricStatement(deleteSql,params);
   }*/
  public ParametricStatement[] getDeleteSQL (String promotionNum) throws SQLException           //------------> Deletion has been done based on the promotion Number.
  {
    List statements = new ArrayList();
    List params = new ArrayList();
    params.add(promotionNum);
    String whereSql = "where PROMOTION_NUM = ?";
    statements.add(new ParametricStatement(deleteSql + " " + whereSql, params));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param posPromDetail
   * @return 
   * @exception SQLException                  //------------> Deletion has been done based on the promotion Number.
   */
  public ParametricStatement[] getUpdateSQL (CMSPosPromDetail posPromDetail) throws SQLException {
    List statements = new ArrayList();
    List params = new ArrayList();
    params.add(posPromDetail.getStgStatus());
    params.add(posPromDetail.getStgErrorMessage());
    params.add(posPromDetail.getStgEventId());
    //String updateSql1 = " and set stg_status = '1' ";
    statements.add(new ParametricStatement(ArmStgPrmOracleBean.updateSql, params));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @return 
   * @exception SQLException
   */
  public CMSPosPromDetail[] selectByStgStatus () throws SQLException {
    //this method needs to be customized
    String sWhereSQL = "where NVL(" + ArmStgPrmOracleBean.COL_STG_STATUS + ", 0) IN (0, 2)";
    return  fromBeansToObjects(query(new ArmStgPrmOracleBean(), sWhereSQL, null));
  }

  //
  //Non public methods begin here!
  //
  private ArmStgPrmOracleBean toArmStgPrmBean (CMSPosPromDetail object) {
    ArmStgPrmOracleBean bean = new ArmStgPrmOracleBean();
    bean.setEndDate(object.getEndTime());
    bean.setStoreId(object.getStoreId());
    bean.setPromotionNum(object.getPromotionNum());
    bean.setApplyTo(object.getApplyTo());
    bean.setDiscountAmount(object.getDiscountAmt());
    bean.setDiscountPercent(object.getDiscountPercent());
    bean.setDiscountType(object.getDiscountType());
    bean.setThresholdAmt(object.getThresholdAmt());
    bean.setIsDiscount(new Boolean(object.getIsDiscount()));
    bean.setApplicableItm(object.getApplicableItm());
    //    bean.setIdRuPrdv(object.getId());
    bean.setStgEventId(new Long(object.getStgEventId()));
    bean.setStgStatus(new Long(object.getStgStatus()));
    bean.setStgErrorMessage(object.getStgErrorMessage());
    bean.setStgLoadDate(object.getStgLoadDate());
    bean.setStgProcessDate(object.getStgProcessDate());
    bean.setPromotionName(object.getPromotionName());
    return  bean;
  }

  /**
   * put your documentation comment here
   * @param beans
   * @return 
   */
  private CMSPosPromDetail[] fromBeansToObjects (BaseOracleBean[] beans) {
    CMSPosPromDetail[] array = new CMSPosPromDetail[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = fromBeanToObject(beans[i]);
    return  array;
  }

  /**
   * put your documentation comment here
   * @return 
   */
  protected BaseOracleBean getDatabeanInstance () {
    return  new ArmStgPrmOracleBean();
  }

  /*private CMSPosPromDetail[] fromBeansToObjects(BaseOracleBean[] beans) {
   CMSPosPromDetail[] array = new CMSPosPromDetail[beans.length];
   for (int i = 0; i < array.length; i++) array[i] = fromBeanToObject(beans[i]);
   return array;
   }*/
  /////this method needs to customized
  private CMSPosPromDetail fromBeanToObject (BaseOracleBean baseBean) {
    ArmStgPrmOracleBean bean = (ArmStgPrmOracleBean)baseBean;
    CMSPosPromDetail object = new CMSPosPromDetail();
    object.doSetStartTime(bean.getStartDate());
    object.doSetEndTime(bean.getEndDate());
    object.doSetStoreId(bean.getStoreId());
    object.doSetPromotionNum(bean.getPromotionNum());
    object.doSetApplyTo(bean.getApplyTo().toString());
    if (bean.getDiscountAmount() != null)
      object.doSetDiscountAmt(bean.getDiscountAmount().doubleValue()); 
    else 
      object.doSetDiscountAmt(new Double("0.00").doubleValue());
    if (bean.getDiscountPercent() != null)
      object.doSetDiscountPercent(bean.getDiscountPercent().doubleValue()); 
    else 
      object.doSetDiscountPercent(new Double("0.00").doubleValue());
    object.doSetDiscountType(bean.getDiscountType());
    if (bean.getThresholdAmt() != null)
      object.doSetThresholdAmt(bean.getThresholdAmt().doubleValue()); 
    else 
      object.doSetThresholdAmt(new Double("0.00").doubleValue());
    if (bean.getIsDiscount() != null)
      object.doSetIsDiscount(bean.getIsDiscount().booleanValue());
    object.doSetApplicableItm(bean.getApplicableItm());
    if (bean.getStgEventId() != null)
      // object.doSetId(bean.getIdRuPrdv());
      object.doSetStgEventId(bean.getStgEventId().toString()); 
    else 
      object.doSetStgEventId("");
    if (bean.getStgStatus() != null)
      object.doSetStgStatus(bean.getStgStatus().toString()); 
    else 
      object.doSetStgStatus(null);
    if (bean.getStgErrorMessage() != null)
      object.doSetStgErrorMessage(bean.getStgErrorMessage()); 
    else 
      object.doSetStgErrorMessage("");
    if (bean.getStgLoadDate() != null)
      object.doSetStgLoadDate(bean.getStgLoadDate()); 
    else 
      object.doSetStgLoadDate(null);
    if (bean.getStgProcessDate() != null)
      object.doSetStgProcessDate(bean.getStgProcessDate()); 
    else 
      object.doSetStgProcessDate(null);
    object.doSetPromotionName(bean.getPromotionName());
    return  object;
  }

  /////these method needs to customized
  private ArmStgPrmOracleBean fromObjectToBean (CMSPosPromDetail object) {
    ArmStgPrmOracleBean bean = new ArmStgPrmOracleBean();
    bean.setEndDate(object.getEndTime());
    bean.setStoreId(object.getStoreId());
    bean.setPromotionNum(object.getPromotionNum());
    bean.setApplyTo(object.getApplyTo());
    bean.setDiscountAmount(object.getDiscountAmt());
    bean.setDiscountPercent(object.getDiscountPercent());
    bean.setDiscountType(object.getDiscountType());
    bean.setThresholdAmt(object.getThresholdAmt());
    // bean.setIdRuPrdv(object.getIdRuPrdv());
    bean.setIsDiscount(new Boolean(object.getIsDiscount()));
    bean.setApplicableItm(object.getApplicableItm());
    bean.setStgEventId(new Long(object.getStgEventId()));
    bean.setStgStatus(new Long(object.getStgStatus()));
    bean.setStgErrorMessage(object.getStgErrorMessage());
    bean.setStgLoadDate(object.getStgLoadDate());
    bean.setStgProcessDate(object.getStgProcessDate());
    return  bean;
  }
}



