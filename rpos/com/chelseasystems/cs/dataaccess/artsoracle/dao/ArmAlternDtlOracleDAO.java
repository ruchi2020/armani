/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  com.chelseasystems.cs.pos.AlterationDetail;
import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  java.util.*;
import  java.sql.*;
import  com.chelseasystems.cr.currency.ArmCurrency;


/**
 * <p>Title: ArmAlternDtlOracleDAO</p>
 * <p>Description:  DAO for AlterationDetail </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04-19-2005 | Manpreet  | N/A       | POS_104665_TS_Alterations_Rev2                     |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 04-21-2005 | Manpreet  | N/A       | Added Description Attribute                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 04-27-2005 | Manpreet  | N/A       | Modified EstimatedTime and ActualTime to Date type |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class ArmAlternDtlOracleDAO extends BaseOracleDAO
    implements ArmAlternDtlDAO {

  /**
   * Insert AlterationDetail object
   * @param object AlterationDetail
   * @throws SQLException
   */
  public void insert (AlterationDetail object) throws SQLException {
    execute(getInsertSQL(object));
  }

  /**
   * Persist AlterationDetail object
   * @param object AlterationDetail
   * @throws SQLException
   * @return ParametricStatement[]
   */
  public ParametricStatement[] getInsertSQL (AlterationDetail object) throws SQLException {
    List statements = new ArrayList();
    ArmAlternDtlOracleBean bean = this.fromObjectToBean(object);
    statements.add(new ParametricStatement(bean.insertSql, bean.toList()));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * Return Databean Instance
   * @return BaseOracleBean
   */
  protected BaseOracleBean getDatabeanInstance () {
    return  new ArmAlternDtlOracleBean();
  }

  /**
   * Get by TransactionID and SequenceNumbers
   * @param sTransactionID TransactionID
   * @param lLineItemID LineItemID
   * @param lDetailSeqNum DetailsSequenceNumber
   * @param lAlteratonSeqNum AlterationSequenceNumber
   * @throws SQLException
   * @return AlterationDetail[]
   */
  public AlterationDetail[] getByTransactionIdAndSequenceNumbers (String sTransactionID, Long lLineItemID, Long lDetailSeqNum, Long lAlteratonSeqNum) throws SQLException {
    String sQueryString = ArmAlternDtlOracleBean.selectSql;
    String arrayColumns[] =  {
      ArmAlternDtlOracleBean.COL_AI_TRN, ArmAlternDtlOracleBean.COL_AI_LN_ITM, ArmAlternDtlOracleBean.COL_DET_SEQ_NUM, ArmAlternDtlOracleBean.COL_ALTERN_SEQ_NUM
    };
    sQueryString += BaseOracleDAO.where(arrayColumns);
    ArrayList params = new ArrayList();
    params.add(sTransactionID);
    params.add(lLineItemID);
    params.add(lDetailSeqNum);
    params.add(lAlteratonSeqNum);
    return  fromBeansToObjects(query(new ArmAlternDtlOracleBean(), sQueryString, params));
  }

  /**
   * Convert Beans To Objects
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
   * Convert Bean to Object
   * @param baseBean BaseOracleBean
   * @return AlterationDetail
   */
  private AlterationDetail fromBeanToObject (BaseOracleBean baseBean) {
    ArmAlternDtlOracleBean bean = (ArmAlternDtlOracleBean)baseBean;
    AlterationDetail object = new AlterationDetail();
    object.doSetTransactionID(bean.getAiTrn());
    object.doSetLineItemID(bean.getAiLnItm());
    object.doSetDetailSeqNum(bean.getDetSeqNum());
    object.doSetAlterationSeqNum(bean.getAlternSeqNum());
    object.doSetAlterationCode(bean.getCdAltern());
    object.doSetEstimatedPrice(bean.getEstimatedPrice());
    object.doSetEstimatedTime(bean.getEstimatedTime());
    object.doSetActualPrice(bean.getActualPrice());
    object.doSetActualTime(bean.getActualTime());
    object.doSetDescription(bean.getDescription());
    return  object;
  }

  /**
   * Convert Object to Bean
   * @param object AlterationDetail
   * @return ArmAlternDtlOracleBean
   */
  private ArmAlternDtlOracleBean fromObjectToBean (AlterationDetail object) {
    ArmAlternDtlOracleBean bean = new ArmAlternDtlOracleBean();
    bean.setAiTrn(object.getTransactionID());
    bean.setAiLnItm(object.getLineItemID());
    bean.setDetSeqNum(object.getDetailSeqNum());
    bean.setAlternSeqNum(object.getAlterationSeqNum());
    bean.setCdAltern(object.getAlterationCode());
    bean.setEstimatedPrice(object.getEstimatedPrice());
    bean.setEstimatedTime(object.getEstimatedTime());
    bean.setActualPrice(object.getActualPrice());
    bean.setActualTime(object.getActualTime());
    bean.setDescription(object.getDescription());
    return  bean;
  }
}



