/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  com.chelseasystems.cs.pos.*;
import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  java.sql.*;
import  java.util.*;
import  com.chelseasystems.cr.currency.ArmCurrency;


/**
 *
 * <p>Title: ArmAlternLnItmDtlOracleDAO</p>
 * <p>Description: DAO for AlterationLineItemDetail</p>
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
 | 2    | 04-27-2005 | Manpreet  | N/A       | Modified to accomodate Comments                    |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class ArmAlternLnItmDtlOracleDAO extends BaseOracleDAO
    implements ArmAlternLnItmDtlDAO {
  /**
   * AletrationDetailOracleDAO
   */
  private ArmAlternDtlOracleDAO armAlternDtlOracleDAO = new ArmAlternDtlOracleDAO();

  /**
   * Insert AlterationLineItemDetail
   * @param objLineItmDetail AlterationLineItemDetail
   * @throws SQLException
   */
  public void insert (AlterationLineItemDetail objLineItmDetail) throws SQLException {
    execute(getInsertSQL(objLineItmDetail));
  }

  /**
   * Insert AlterationLineItemDetail
   * @param object AlterationLineItemDetail
   * @throws SQLException
   * @return ParametricStatement[]
   */
  public ParametricStatement[] getInsertSQL (AlterationLineItemDetail object) throws SQLException {
    List statements = new ArrayList();
    ArmAlternLnItmDtlOracleBean bean = this.fromObjectToBean(object);
    statements.add(new ParametricStatement(bean.insertSql, bean.toList()));
    // Add AlterationDetails Inserts
    AlterationDetail[] alterationDetails = object.getAlterationDetailsArray();
    for (int iCtr = 0; iCtr < alterationDetails.length; iCtr++) {
      if (alterationDetails[iCtr] == null)
        continue;
      alterationDetails[iCtr].setTransactionID(object.getTransactionID());
      alterationDetails[iCtr].setLineItemID(object.getLineItemID());
      alterationDetails[iCtr].setDetailSeqNum(object.getDetailSeqNum());
      alterationDetails[iCtr].setAlteratonSeqNum(object.getAlterationSeqNum());
      statements.addAll(Arrays.asList(armAlternDtlOracleDAO.getInsertSQL(alterationDetails[iCtr])));
    }
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[statements.size()]);
  }

  /**
   * Get Instance of bean
   * @return BaseOracleBean
   */
  protected BaseOracleBean getDatabeanInstance () {
    return  new ArmAlternLnItmDtlOracleBean();
  }

  /**
   * Get AlterationLineItemDetails array
   * @param sTransactionID TransactionID
   * @param lLineItemID LineItemID
   * @param lDetailSeqNum DetailsSequenceNumber
   * @param lAlteratonSeqNum AlterationSequenceNumber
   * @throws SQLException
   * @return AlterationLineItemDetail[]
   */
  public AlterationLineItemDetail[] getByTransactionIdAndSequenceNumbers (String sTransactionID, Long lLineItemID, Long lDetailSeqNum) throws SQLException {
    String sQueryString = ArmAlternLnItmDtlOracleBean.selectSql;
    String arrayColumns[] =  {
      ArmAlternLnItmDtlOracleBean.COL_AI_TRN, ArmAlternLnItmDtlOracleBean.COL_AI_LN_ITM, ArmAlternLnItmDtlOracleBean.COL_DET_SEQ_NUM
    };
    sQueryString += BaseOracleDAO.where(arrayColumns);
    ArrayList params = new ArrayList();
    params.add(sTransactionID);
    params.add(lLineItemID);
    params.add(lDetailSeqNum);
    return  fromBeansToObjects(query(new ArmAlternLnItmDtlOracleBean(), sQueryString, params));
  }

  /**
   * Convert beans to Objects
   * @param beans BaseOracleBean[]
   * @return AlterationLineItemDetail[]
   */
  private AlterationLineItemDetail[] fromBeansToObjects (BaseOracleBean[] beans) throws SQLException {
    AlterationLineItemDetail[] array = new AlterationLineItemDetail[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = fromBeanToObject(beans[i]);
    return  array;
  }

  /**
   * Convert Bean to Object
   * @param baseBean BaseOracleBean
   * @return AlterationLineItemDetail
   */
  private AlterationLineItemDetail fromBeanToObject (BaseOracleBean baseBean) throws SQLException {
    ArmAlternLnItmDtlOracleBean bean = (ArmAlternLnItmDtlOracleBean)baseBean;
    AlterationLineItemDetail object = new AlterationLineItemDetail();
    object.doSetTransactionID(bean.getAiTrn());
    object.doSetLineItemID(bean.getAiLnItm());
    object.doSetDetailSeqNum(bean.getDetSeqNum());
    object.doSetAlterationSeqNum(bean.getSeqNum());
    object.doSetAlterationTicketID(bean.getIdAltern());
    object.doSetTryDate(bean.getTryDt());
    object.doSetPromiseDate(bean.getPromiseDt());
    object.doSetFitterID(bean.getFitterId());
    object.doSetFitterID(bean.getTailor());
    object.doSetTotalPrice(bean.getTotalPrice());
    object.doSetComments(bean.getComments());
    // Get AlterationDetails and add them.
    AlterationDetail altDetails[] = armAlternDtlOracleDAO.getByTransactionIdAndSequenceNumbers(bean.getAiTrn(), bean.getAiLnItm(), bean.getDetSeqNum(), bean.getSeqNum());
    if (altDetails == null)
      return  object;
    for (int iCtr = 0; iCtr < altDetails.length; iCtr++) {
      if (altDetails[iCtr] == null)
        continue;
      object.doAddAlterationDetail(altDetails[iCtr]);
    }
    return  object;
  }

  /**
   * Convert Object to Bean
   * @param object AlterationLineItemDetail
   * @return ArmAlternLnItmDtlOracleBean
   */
  private ArmAlternLnItmDtlOracleBean fromObjectToBean (AlterationLineItemDetail object) throws SQLException {
    ArmAlternLnItmDtlOracleBean bean = new ArmAlternLnItmDtlOracleBean();
    bean.setAiTrn(object.getTransactionID());
    bean.setAiLnItm(object.getLineItemID());
    bean.setDetSeqNum(object.getDetailSeqNum());
    bean.setSeqNum(object.getAlterationSeqNum());
    bean.setIdAltern(object.getAlterationTicketID());
    bean.setTryDt(object.getTryDate());
    bean.setPromiseDt(object.getPromiseDate());
    bean.setFitterId(object.getFitterID());
    bean.setTailor(object.getTailorID());
    bean.setTotalPrice(object.getTotalPrice());
    bean.setComments(object.getComments());
    return  bean;
  }
}



