/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.dataaccess.artsoracle.dao;

import com.chelseasystems.cs.fiscaldocument.*;
import com.chelseasystems.cs.dataaccess.*;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cr.customer.Customer;
import java.sql.*;
import java.util.*;
import com.chelseasystems.cr.pos.CompositePOSTransaction;


/**
 *
 * <p>Title:ArmFiscalDocumentOracleDAO </p>
 * <p>Description: FiscalDocument DAO</p>
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
public class ArmFiscalDocumentOracleDAO extends BaseOracleDAO implements ArmFiscalDocumentDAO {
  /**
   * Select SQL
   */
  private static String selectSql = ArmFiscalDocumentOracleBean.selectSql;
  /**
   * Insert SQL
   */
  private static String insertSql = ArmFiscalDocumentOracleBean.insertSql;

  /**
   * Insert a record
   * @param object FiscalDocument
   * @throws SQLException
   */
  public void insert(FiscalDocument object)
      throws SQLException {
    execute(getInsertSQL(object));
  }

  /**
   * Persist FiscalDocument object
   * @param object FiscalDocument
   * @throws SQLException
   * @return ParametricStatement[]
   */
  public ParametricStatement[] getInsertSQL(FiscalDocument object)
      throws SQLException {
    String fiscalDocumentComments = null;
    List statements = new ArrayList();
    CompositeOracleDAO compositeOracleDAO = new CompositeOracleDAO();
    ArmFiscalDocLnItmDtlOracleDAO fiscDocLnItmDtlDAO = new ArmFiscalDocLnItmDtlOracleDAO();
    ArmFiscalDocumentOracleBean bean = this.fromObjectToBean(object);
    statements.add(new ParametricStatement(bean.insertSql, bean.toList()));
    Customer customer = object.getTxn().getCustomer();
    if (customer != null) {
      CustomerOracleDAO customerDAO = new CustomerOracleDAO();
      if (customer.getId() == null || customer.getId().equals(""))
        statements.addAll(Arrays.asList(customerDAO.getInsertSQL(customer)));
      else
        statements.addAll(Arrays.asList(customerDAO.getUpdateSQL(customer)));
    }
    statements.add(compositeOracleDAO.getUpdateFiscalDocumentSQL(object.getTxn()));
    POSLineItem lineItems[] = object.getLineItemsArray();
    if (lineItems != null) {
      for (int iCtr = 0; iCtr < lineItems.length; iCtr++) {
        if (lineItems[iCtr] instanceof CMSNoSaleLineItem)
          fiscalDocumentComments = ((CMSNoSaleLineItem)lineItems[iCtr]).getFiscalDocComment(object.getDocumentType());
        else if (lineItems[iCtr] instanceof CMSNoReturnLineItem)
          fiscalDocumentComments = ((CMSNoReturnLineItem)lineItems[iCtr]).getFiscalDocComment(object.getDocumentType());
        else if (lineItems[iCtr] instanceof CMSSaleLineItem)
          fiscalDocumentComments = ((CMSSaleLineItem)lineItems[iCtr]).getFiscalDocComment(object.getDocumentType());
        else if (lineItems[iCtr] instanceof CMSPresaleLineItem)
          fiscalDocumentComments = ((CMSPresaleLineItem)lineItems[iCtr]).getFiscalDocComment(object.getDocumentType());
        else if (lineItems[iCtr] instanceof CMSReservationLineItem)
          fiscalDocumentComments = ((CMSReservationLineItem)lineItems[iCtr]).getFiscalDocComment(object.getDocumentType());
        else if (lineItems[iCtr] instanceof CMSConsignmentLineItem)
          fiscalDocumentComments = ((CMSConsignmentLineItem)lineItems[iCtr]).getFiscalDocComment(object.getDocumentType());
        else if (lineItems[iCtr] instanceof CMSReturnLineItem)
          fiscalDocumentComments = ((CMSReturnLineItem)lineItems[iCtr]).getFiscalDocComment(object.getDocumentType());
        statements.addAll(Arrays.asList(fiscDocLnItmDtlDAO.getInsertSQL(object, lineItems[iCtr], fiscalDocumentComments)));
      }
    }
    ArmFiscalDocNoOracleDAO armFiscalDocNoOracleDAO = new ArmFiscalDocNoOracleDAO();
    System.out.println("issue #1920 ArmFiscalDocumentOracleDAO   "+object.getMasterRegister());
    statements.addAll(Arrays.asList(armFiscalDocNoOracleDAO.getUpdateSql(object)));
    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * Select FiscalDocument by TXN number
   * @param aiTrn String
   * @throws SQLException
   * @return FiscalDocument[]
   */
  public FiscalDocument[] addFiscalDocumentIntoCompositePOSTransaction(CompositePOSTransaction
      transaction)
      throws SQLException {
    String whereSql = "where AI_TRN = ?";
    return fromBeansToObjects(query(new ArmFiscalDocumentOracleBean(), selectSql + whereSql
        , transaction.getId()), transaction);
  }


  /**
   * Select FiscalDocument by TXN number
   * @param aiTrn String
   * @throws SQLException
   * @return FiscalDocument[]
   */
  public FiscalDocument[] selectByAiTrn(String aiTrn, CompositePOSTransaction tranaction)
      throws SQLException {
    String whereSql = "where AI_TRN = ?";
    return fromBeansToObjects(query(new ArmFiscalDocumentOracleBean(), selectSql + whereSql, aiTrn)
        , tranaction);
  }

  /**
   * put your documentation comment here
   * @return
   */
  protected BaseOracleBean getDatabeanInstance() {
    return new ArmFiscalDocumentOracleBean();
  }

  /**
   * put your documentation comment here
   * @param beans
   * @return
   */
  private FiscalDocument[] fromBeansToObjects(BaseOracleBean[] beans
      , CompositePOSTransaction tranaction) {
    FiscalDocument[] array = new FiscalDocument[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = fromBeanToObject(beans[i], tranaction);
    return array;
  }

  /**
   * put your documentation comment here
   * @param baseBean
   * @return
   */
  private FiscalDocument fromBeanToObject(BaseOracleBean baseBean
      , CompositePOSTransaction tranaction) {
    ArmFiscalDocumentOracleBean bean = (ArmFiscalDocumentOracleBean)baseBean;
    FiscalDocument object = null;
    try {
      if (bean.getTyDoc().equals(FISCAL_DOC_TYPE_DDT))
      	object = new DDT();
      else if (bean.getTyDoc().equals(FISCAL_DOC_TYPE_TAXFREE))
      	object = new TaxFree();
      else if (bean.getTyDoc().equals(FISCAL_DOC_TYPE_VATINVOICE))
      	object = new VATInvoice();
      else if (bean.getTyDoc().equals(FISCAL_DOC_TYPE_CREDITNOTE))
      	object = new CreditNote();
      //TransactionOracleDAO transactionOracleDAO = new TransactionOracleDAO();
      else
      	object = new FiscalDocument((CMSCompositePOSTransaction)tranaction);
      object.setTxn((CMSCompositePOSTransaction)tranaction);
      ((CMSCompositePOSTransaction)tranaction).doAddFiscalDocument(object);
      //object.doSetTxn((CMSCompositePOSTransaction)transactionOracleDAO.selectById(bean.getAiTrn()));
      object.doSetDocumentNumber(bean.getDocNum());
      object.setIsDocNumAssigned();
      object.doSetDocumentType(bean.getTyDoc());
      object.doSetFiscalDate(bean.getFiscalDate());
      if (bean.getRefundAmount() != null)
        object.doSetRefundAmount(bean.getRefundAmount());
      object.doSetVATNumber(bean.getVatNum());
      object.doSetCompanyName(bean.getCompanyName());
      object.doSetCompanyName2(bean.getCompanyName2());
      object.doSetAddressLine1(bean.getAddressLine1());
      object.doSetAddressLine2(bean.getAddressLine2());
      object.doSetCity(bean.getCity());
      object.doSetCounty(bean.getCounty());
      object.doSetZipCode(bean.getZipCode());
      object.doSetCounty(bean.getState());
      object.doSetCountry(bean.getCountry());
      if (object instanceof DDT) {
        ((DDT)object).doSetDestination(bean.getDestination());
        ((DDT)object).doSetDestinationCode(bean.getCdDest());
        ((DDT)object).doSetSender(bean.getSender());
        ((DDT)object).doSetSenderCode(bean.getCdSender());
        ((DDT)object).doSetCarrierType(bean.getTyCarrier());
        ((DDT)object).doSetExpeditionCode(bean.getCdExpedition());
        ((DDT)object).doSetGoodsNumber(bean.getGoodsNum());
        ((DDT)object).doSetCarrierCode(bean.getCdCarrier());
        ((DDT)object).doSetCarrierDesc(bean.getDeCarrier());
        ((DDT)object).doSetPackageType(bean.getTyPackage());
        ((DDT)object).doSetNote(bean.getNote());
        ((DDT)object).doSetWeight(bean.getWeight().doubleValue());
      } else if (object instanceof CreditNote) {
        //      ( (CreditNote) object).doSetBank();
        //       ( (CreditNote) object).doSetFiscalCode();
        ((CreditNote)object).doSetSupplierPayType(bean.getTyPayment());
      } else if (object instanceof VATInvoice) {
        ((VATInvoice)object).doSetSupplierPayType(bean.getTyPayment());
        //      ( (VATInvoice) object).doSetBank();
        //      ( (VATInvoice) object).doSetFiscalCode(bean.getc);
      } else if (object instanceof TaxFree) {
        //      ( (TaxFree) object).doSetDetaxCode(bean.getd));
        //      ( (TaxFree) object).doSetIDType(bean.gett));
        //      ( (TaxFree) object).doSetIssueDate(bean.getd));
        ((TaxFree)object).doSetPaymentType(bean.getTyPayment());
        //      ( (TaxFree) object).doSetPlaceOfIssue(bean.getpl());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return object;
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   */
  private ArmFiscalDocumentOracleBean fromObjectToBean(FiscalDocument object) {
    ArmFiscalDocumentOracleBean bean = new ArmFiscalDocumentOracleBean();
    bean.setAiTrn(object.getTxn().getId());
    bean.setDocNum(object.getDocumentNumber());
    bean.setTyDoc(object.getDocumentType());
    bean.setFiscalDate(object.getFiscalDate());
    if (object.getRefundAmount() != null)
      bean.setRefundAmount(object.getRefundAmount());
    bean.setCompanyName(object.getCompanyName());
    bean.setCompanyName2(object.getCompanyName2());
    bean.setAddressLine1(object.getAddressLine1());
    bean.setAddressLine2(object.getAddressLine2());
    bean.setCity(object.getCity());
    bean.setCounty(object.getCounty());
    bean.setZipCode(object.getZipCode());
    bean.setState(object.getCounty());
    bean.setCountry(object.getCountry());
    if (object instanceof DDT) {
      bean.setDestination(((DDT)object).getDestination());
      bean.setCdDest(((DDT)object).getDestinationCode());
      bean.setSender(((DDT)object).getSender());
      bean.setCdSender(((DDT)object).getSenderCode());
      bean.setCdExpedition(((DDT)object).getExpeditionCode());
      bean.setGoodsNum(((DDT)object).getGoodsNumber());
      bean.setCdCarrier(((DDT)object).getCarrierCode());
      bean.setTyCarrier(((DDT)object).getCarrierType());
      bean.setDeCarrier(((DDT)object).getCarrierDesc());
      bean.setTyPackage(((DDT)object).getPackageType());
      bean.setVatNum(object.getVATNumber());
      bean.setTyPkg(((DDT)object).getPackageType());
      bean.setNote(((DDT)object).getNote());
      bean.setWeight(((DDT)object).getWeight());
    }
    if (object instanceof VATInvoice) {
      bean.setTyPayment(((VATInvoice)object).getSupplierPayType());
    }
    return bean;
  }
}


