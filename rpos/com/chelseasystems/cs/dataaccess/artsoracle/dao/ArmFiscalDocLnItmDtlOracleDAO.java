//
// Copyright 2002, Retek Inc. All Rights Reserved.
//
package com.chelseasystems.cs.dataaccess.artsoracle.dao;

/////
import com.chelseasystems.cs.dataaccess.*;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.fiscaldocument.FiscalDocument;
import java.util.Date;
import java.sql.*;
import java.util.*;
import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cr.pos.CompositePOSTransaction;

public class ArmFiscalDocLnItmDtlOracleDAO extends BaseOracleDAO {

  private static String selectSql = ArmFiscalDocLnItmDtlOracleBean.selectSql;
  private static String insertSql = ArmFiscalDocLnItmDtlOracleBean.insertSql;
  private static String updateSql = ArmFiscalDocLnItmDtlOracleBean.updateSql + "where AI_TRN = ?";
  private static String deleteSql = ArmFiscalDocLnItmDtlOracleBean.deleteSql + "where AI_TRN = ?";

  public ParametricStatement[] getInsertSQL(FiscalDocument document, POSLineItem object
      , String vatComments)
      throws SQLException {
    ArrayList statements = new ArrayList();
    statements.add(new ParametricStatement(insertSql
        , fromObjectToBean(document, object, vatComments).toList()));
    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  public POSLineItem addFiscalDocumentsToLineItem(POSLineItem lineItem)
      throws SQLException {
    ArrayList params = new ArrayList();
    String whereSQL = where(ArmFiscalDocLnItmDtlOracleBean.COL_AI_TRN
        , ArmFiscalDocLnItmDtlOracleBean.COL_AI_LN_ITM);
    params.add(lineItem.getTransaction().getCompositeTransaction().getId());
    params.add(lineItem.getSequenceNumber() + "");
    return fromBeansToObjects(query(new ArmFiscalDocLnItmDtlOracleBean(), whereSQL, params), lineItem);
  }

  //
  //Non public methods begin here!
  //

  protected BaseOracleBean getDatabeanInstance() {
    return new ArmFiscalDocLnItmDtlOracleBean();
  }


  /////these method needs to customized
  private ArmFiscalDocLnItmDtlOracleBean fromObjectToBean(FiscalDocument object
      , POSLineItem lineItem, String vatComments) {
    ArmFiscalDocLnItmDtlOracleBean bean = new ArmFiscalDocLnItmDtlOracleBean();
    bean.setAiTrn(object.getTxn().getId());
    bean.setAiLnItm(lineItem.getSequenceNumber());
    bean.setDocNum(object.getDocumentNumber());
    bean.setTyDoc(object.getDocumentType());
    bean.setVatComments(vatComments);
    return bean;
  }

  /**
   * put your documentation comment here
   * @param composite
   * @param beans
   * @exception SQLException
   */
  private POSLineItem fromBeansToObjects(BaseOracleBean[] beans, POSLineItem lineItem)
      throws SQLException {
    for (int i = 0; i < beans.length; i++)
      fromBeanToObject(beans[i], lineItem);
    return lineItem;
  }

  private POSLineItem fromBeanToObject(BaseOracleBean baseBean, POSLineItem object) {
    ArmFiscalDocLnItmDtlOracleBean bean = (ArmFiscalDocLnItmDtlOracleBean)baseBean;
    CMSCompositePOSTransaction composite = (CMSCompositePOSTransaction)object.getTransaction().
        getCompositeTransaction();
    FiscalDocument[] documents = ((CMSCompositePOSTransaction)composite).getFiscalDocumentArray();
    for (int index = 0; index < documents.length; index++) {
      FiscalDocument document = documents[index];
      String docNum = bean.getDocNum();
      String docType = bean.getTyDoc();
      if (document != null && document.getDocumentNumber().equals(docNum)
          && document.getDocumentType().equals(docType)) {
        if (object instanceof CMSNoSaleLineItem) {
          ((CMSNoSaleLineItem)object).addFiscalDocComment(docType, bean.getVatComments());
          ((CMSNoSaleLineItem)object).addFiscalDocument(document);
          document.doAddLineItem(object);
        } else if (object instanceof CMSNoReturnLineItem) {
          ((CMSNoReturnLineItem)object).addFiscalDocComment(docType, bean.getVatComments());
          ((CMSNoReturnLineItem)object).addFiscalDocument(document);
          document.doAddLineItem(object);
        } else if (object instanceof CMSSaleLineItem) {
          ((CMSSaleLineItem)object).addFiscalDocComment(docType, bean.getVatComments());
          ((CMSSaleLineItem)object).addFiscalDocument(document);
          document.doAddLineItem(object);
        } else if (object instanceof CMSReservationLineItem) {
          ((CMSReservationLineItem)object).addFiscalDocComment(docType, bean.getVatComments());
          ((CMSReservationLineItem)object).addFiscalDocument(document);
          document.doAddLineItem(object);
        } else if (object instanceof CMSConsignmentLineItem) {
          ((CMSConsignmentLineItem)object).addFiscalDocComment(docType, bean.getVatComments());
          ((CMSConsignmentLineItem)object).addFiscalDocument(document);
          document.doAddLineItem(object);
        } else if (object instanceof CMSPresaleLineItem) {
          ((CMSPresaleLineItem)object).addFiscalDocComment(docType, bean.getVatComments());
          ((CMSPresaleLineItem)object).addFiscalDocument(document);
          document.doAddLineItem(object);
        } else if (object instanceof CMSReturnLineItem) {
          ((CMSReturnLineItem)object).addFiscalDocComment(docType, bean.getVatComments());
          ((CMSReturnLineItem)object).addFiscalDocument(document);
          document.doAddLineItem(object);
        }
      }
    }
    return object;
  }
}
