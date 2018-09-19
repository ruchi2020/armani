/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cs.fiscaldocument.FiscalDocument;
import  com.chelseasystems.cs.fiscaldocument.FiscalDocumentNumber;
import  com.chelseasystems.cr.config.ConfigMgr;
import  java.util.List;
import  java.util.Vector;
import  java.util.ArrayList;
import  java.util.StringTokenizer;
import  java.sql.*;
import com.chelseasystems.cr.store.Store;


/**
 * <p>Title: ArmFiscalDocNoOracleDAO</p>
 * <p>Description: Persist Fiscal Document Number</p>
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
 | 1    | 06-06-2005 | Manpreet  | N/A       | POS_104665_TS_FiscalDocuments_Rev0                 |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class ArmFiscalDocNoOracleDAO extends BaseOracleDAO
    implements ArmFiscalDocNoDAO {
  /**
   * Select SQL
   */
  private static String sSelectSql = ArmFiscalDocNoOracleBean.selectSql;
  /**
   * Insert SQL
   */
  private static String sInsertSql = ArmFiscalDocNoOracleBean.insertSql;
  /**
   * Update SQL
   */
  private static String sUpdateSql = ArmFiscalDocNoOracleBean.updateSql;

  /**
   * GetData Bean Instance
   * @return BaseOracleBean
   */
  protected BaseOracleBean getDatabeanInstance () {
    return  new ArmFiscalDocNoOracleBean();
  }

  /**
   * Retrieve FiscalDocumentNumber by StoreId and RegisterId
   * @param sStoreId String
   * @param sRegisterId String
   * @throws SQLException
   * @return FiscalDocumentNumber
   */
  public FiscalDocumentNumber getByStoreAndRegister (String sStoreId, String sRegisterId) throws SQLException {
    try {
      List params = new ArrayList();
      String whereSql = where(ArmFiscalDocNoOracleBean.COL_ID_STR_RT, ArmFiscalDocNoOracleBean.COL_REGISTER_ID);
      params.add(sStoreId);
      params.add(sRegisterId);
      FiscalDocumentNumber[] document = fromBeansToObjects(query(new ArmFiscalDocNoOracleBean(), sSelectSql + whereSql, params));
      if (document != null && document.length > 0)
        return  document[0];
    } catch (Exception e) {
      System.out.println("getByStoreAndRegister() -- ArmFiscalDocNoOracleDAO");
      e.printStackTrace();
    }
    return  null;
  }

  /**
   * Get InsertSql for FiscalDocumentNumber
   * @param documentNumber FiscalDocumentNumber
   * @return ParametricStatement[]
   */
  public ParametricStatement[] getInsertSQL (FiscalDocumentNumber documentNumber) {
    ArrayList statements = new ArrayList();
    List params = fromObjectToBean(documentNumber).toList();
    statements.add(new ParametricStatement(sInsertSql, params));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * Convert Beans to Objects
   * @param beans BaseOracleBean[]
   * @return FiscalDocumentNumber[]
   */
  private FiscalDocumentNumber[] fromBeansToObjects (BaseOracleBean[] beans) {
    FiscalDocumentNumber[] array = new FiscalDocumentNumber[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = fromBeanToObject(beans[i]);
    return  array;
  }

  /**
   * Convert Bean to Object
   * @param baseBean BaseOracleBean
   * @return FiscalDocumentNumber
   */
  private FiscalDocumentNumber fromBeanToObject (BaseOracleBean baseBean) {
    ArmFiscalDocNoOracleBean bean = (ArmFiscalDocNoOracleBean)baseBean;
    FiscalDocumentNumber object = new FiscalDocumentNumber();
    if (bean.getLastCreditNote() != null){
      object.setNextCreditNoteNo(Long.parseLong(bean.getLastCreditNote()));
      System.out.println("For issue #1920 inside ArmFiscalNoOracleDAO getLastCreditNote  "+bean.getLastCreditNote());
    }
    if (bean.getLastDdtNo() != null){
      object.setNextDDTNo(Long.parseLong(bean.getLastDdtNo()));
      System.out.println("For issue #1920 inside ArmFiscalNoOracleDAO   getLastDdtNo"+bean.getLastDdtNo());
    }
    if (bean.getLastVatNo() != null){
      object.setNextVATNo(Long.parseLong(bean.getLastVatNo()));
      System.out.println("For issue #1920 inside ArmFiscalNoOracleDAO  getLastVatNo "+bean.getLastVatNo());
    }
    System.out.println("For issue #1920 inside ArmFiscalNoOracleDAO Register id  "+bean.getRegisterId());
    System.out.println("For issue #1920 inside ArmFiscalNoOracleDAO   Store id"+bean.getIdStrRt());
    object.setRegisterId(bean.getRegisterId());
    object.setStoreId(bean.getIdStrRt());
    return  object;
  }

  /**
   * Conver object to Bean
   * @param docNum FiscalDocumentNumber
   * @return ArmFiscalDocNoOracleBean
   */
  private ArmFiscalDocNoOracleBean fromObjectToBean (FiscalDocumentNumber docNum) {
    ArmFiscalDocNoOracleBean armFiscalDocNoOracleBean = new ArmFiscalDocNoOracleBean();
    armFiscalDocNoOracleBean.setIdStrRt(docNum.getStoreId());
    armFiscalDocNoOracleBean.setRegisterId(docNum.getRegisterId());
    armFiscalDocNoOracleBean.setLastCreditNote("" + docNum.getNextCreditNoteNo());
    armFiscalDocNoOracleBean.setLastDdtNo("" + docNum.getNextDDTNo());
    armFiscalDocNoOracleBean.setLastVatNo("" + docNum.getNextVATNo());
    return  armFiscalDocNoOracleBean;
  }

  /**
   * Get UpdateSql
   * @param documentNumber FiscalDocumentNumber
   * @return ParametricStatement[]
   */
  public ParametricStatement[] getUpdateSql (FiscalDocumentNumber documentNumber) {
    ArrayList statements = new ArrayList();
    List params = fromObjectToBean(documentNumber).toList();
    sUpdateSql = ArmFiscalDocNoOracleBean.updateSql + where(ArmFiscalDocNoOracleBean.COL_ID_STR_RT, ArmFiscalDocNoOracleBean.COL_REGISTER_ID);
    params.add(documentNumber.getRegisterId());
    params.add(documentNumber.getStoreId());
    statements.add(new ParametricStatement(sUpdateSql, params));
    System.out.println("For issue #1920   "+sUpdateSql);
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * Get UpdateSql
   * @param document FiscalDocument
   * @return ParametricStatement[]
   */
  public ParametricStatement[] getUpdateSql (FiscalDocument document) {
    ArrayList statements = new ArrayList();
    List params;
    String sUpdateSql = "UPDATE " + ArmFiscalDocNoOracleBean.TABLE_NAME;
    String sWhereSql = new String();
    sWhereSql = where(ArmFiscalDocNoOracleBean.COL_ID_STR_RT, ArmFiscalDocNoOracleBean.COL_REGISTER_ID);
    params = new ArrayList();
    params.add(document.getTxn().getStore().getId());
    params.add(document.getMasterRegister());
    params.add(document.getDocumentNumber());
    //Seperate counter for CreditNote if country is a CreditNoteCountry
    if (isCreditNoteCountry(document.getTxn().getStore())) {
      if (document.isDDTDocument()) {
        sUpdateSql += " SET " + ArmFiscalDocNoOracleBean.COL_LAST_DDT_NO + " =  ";
        sWhereSql += " and " +ArmFiscalDocNoOracleBean.COL_LAST_DDT_NO + " < to_number(?)";
        System.out.println("For issue #1920 isDDTDocument"+sUpdateSql);
      }
      else if (document.isVatInvoiceDocument() || document.isTaxFreeDocument()) {
        sUpdateSql += " SET " + ArmFiscalDocNoOracleBean.COL_LAST_VAT_NO + " =  ";
        sWhereSql += " and " +ArmFiscalDocNoOracleBean.COL_LAST_VAT_NO + " < to_number(?)";
        System.out.println("For issue #1920 isVatInvoiceDocument"+sUpdateSql);
      }
      else {
        sUpdateSql += " SET " + ArmFiscalDocNoOracleBean.COL_LAST_CREDIT_NOTE + " = ";
        sWhereSql += " and " +ArmFiscalDocNoOracleBean.COL_LAST_CREDIT_NOTE + " < to_number(?)";
        System.out.println("For issue #1920 "+sUpdateSql);
      }
    }
    else {
      if (document.isDDTDocument()) {
        sUpdateSql += " SET " + ArmFiscalDocNoOracleBean.COL_LAST_DDT_NO + " =  ";
        sWhereSql += " and " +ArmFiscalDocNoOracleBean.COL_LAST_DDT_NO + " < to_number(?)";
      }
      else if (document.isVatInvoiceDocument() || document.isTaxFreeDocument() || document.isCreditNoteDocument()) {
        sUpdateSql += " SET " + ArmFiscalDocNoOracleBean.COL_LAST_VAT_NO + " =  ";
        sWhereSql += " and " +ArmFiscalDocNoOracleBean.COL_LAST_VAT_NO + " < to_number(?)";
      }
    }
    sUpdateSql += " '" + document.getDocumentNumber() + "'";
    sUpdateSql += sWhereSql;

    statements.add(new ParametricStatement(sUpdateSql, params));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   */
  public ArmFiscalDocNoOracleDAO () {
  }

  private boolean isCreditNoteCountry(Store store) {
    boolean bIsACreditNoteCountry = false;
    try {
      ConfigMgr config = new ConfigMgr("fiscal_document.cfg");
      //Sergio
      //String sTmp = config.getString("FISCAL_ISO_COUNTRY_LIST");
          String sTmp = config.getString("FISCAL_STATE");
      //String sCountry = store.getPreferredISOCountry();
          String sState = store.getState();
      if (sTmp != null) {
        StringTokenizer sTokens = new StringTokenizer(sTmp, ",");
        while (sTokens.hasMoreTokens()) {
          sTmp = sTokens.nextToken();
          if (sTmp != null) {
            //if (sTmp.indexOf(sCountry) != -1 || sTmp.equalsIgnoreCase(sCountry)) {
                        if (sTmp.indexOf(sState) != -1 || sTmp.equalsIgnoreCase(sState)) {
              bIsACreditNoteCountry = true;
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return bIsACreditNoteCountry;
  }

}
