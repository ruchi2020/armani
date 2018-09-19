/*
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//


package com.chelseasystems.cs.dataaccess.artsoracle.dao;

import java.util.Date;
import java.sql.*;
import java.util.*;
import com.chelseasystems.cs.dataaccess.*;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import com.chelseasystems.cs.fiscaldocument.*;
import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;


/**
 */
public class ArmStgFiscalDocOracleDAO extends BaseOracleDAO implements ArmStgFiscalDocDAO {
  private static String insertSql = ArmStgFiscalDocOracleBean.insertSql;

  /**
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getStgFiscalDocInsertSQL(FiscalDocument object)
      throws SQLException {
    List statements = new ArrayList();
    ArmStgFiscalDocOracleBean bean = new ArmStgFiscalDocOracleBean();
    CMSCompositePOSTransaction txn = object.getTxn();
    if (object instanceof VATInvoice) {
      VATInvoice vatInvoice = (VATInvoice)object;
      bean.setSuppPaymentType(vatInvoice.getSupplierPayType());
      bean.setFiscalCd(vatInvoice.getFiscalCode());
//      bean.setPreVatAmount(object.getCompositePreVATAmount().doubleValue());
//      bean.setPostVatAmount(object.getCompositeNetAmount().doubleValue());
      bean.setVatExemptCd(txn.getTaxExemptId());
      // Bank
    } else if (object instanceof TaxFree) {
      TaxFree taxFree = (TaxFree)object;
      bean.setVatNum(taxFree.getVATNumber());
      bean.setTaxFreeCd(taxFree.getDetaxCode());
      bean.setPaymentType(taxFree.getPaymentType());
      bean.setIdType(taxFree.getIDType());
      bean.setPlaceOfIssue(taxFree.getPlaceOfIssue());
      bean.setDateOfIssue(taxFree.getIssueDate());
      bean.setRefundAmount(object.getRefundAmount().doubleValue());
    } else if (object instanceof DDT) {
      DDT ddt = (DDT)object;
      bean.setDestinationCd(ddt.getDestinationCode());
      bean.setSender(ddt.getSender());
      bean.setSenderCd(ddt.getSenderCode());
      bean.setCarrierType(ddt.getCarrierType());
      bean.setExpeditionCd(ddt.getExpeditionCode());
      bean.setGoodsNum(ddt.getGoodsNumber());
      bean.setCarrierCd(ddt.getCarrierCode());
      bean.setCarrierDesc(ddt.getCarrierDesc());
      bean.setPackageType(ddt.getPackageType());
      bean.setWeight(ddt.getWeight());
      bean.setNote(ddt.getNote());
    } else if (object instanceof CreditNote) {
      CreditNote creditNote = (CreditNote)object;
      bean.setSuppPaymentType(creditNote.getSupplierPayType());
      bean.setFiscalCd(creditNote.getFiscalCode());
      // Bank
//      bean.setPreVatAmount(object.getCompositePreVATAmount().doubleValue());
//      bean.setPostVatAmount(object.getCompositeNetAmount().doubleValue());
      bean.setVatExemptCd(txn.getTaxExemptId());
    }
    bean.setTransactionId(txn.getId());
    bean.setPreVatAmount(object.getCompositePreVATAmount().doubleValue());
    bean.setPostVatAmount(object.getCompositeNetAmount().doubleValue());
    bean.setDocNum(object.getDocumentNumber());
    bean.setDocType(object.getDocumentType());
    bean.setFiscalDate(object.getFiscalDate());
    bean.setCompanyName(object.getCompanyName());
    bean.setCompanyName2(object.getCompanyName2());
    bean.setAddress(object.getAddressLine1());
    bean.setAddress2(object.getAddressLine2());
    bean.setCity(object.getCity());
    bean.setCounty(object.getCounty());
    bean.setZipCode(object.getZipCode());
    bean.setCountry(object.getCountry());
    statements.add(new ParametricStatement(ArmStgFiscalDocOracleBean.insertSql, bean.toList()));
    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }
}


