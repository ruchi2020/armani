/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.dataaccess.artsoracle.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.BaseOracleDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.TrLtmTndOracleBean;
import com.ga.cs.pos.datatranssferobject.MediaReportDataTransferObject;


/**
 * put your documentation comment here
 */
public class MediaReportOracleDAO extends BaseOracleDAO {
  private final String TR_LTM_TND_SELECT = "select TR_LTM_TND.AI_TRN, TR_LTM_TND.AI_LN_ITM, TR_LTM_TND.TY_TND, TR_LTM_TND.ID_ACNT_NMB, TR_LTM_TND.ID_ACNT_TND, TR_LTM_TND.MO_ITM_LN_TND, TR_LTM_TND.LU_CLS_TND, TR_LTM_TND.JOURNAL_KEY, TR_LTM_TND.RES_MSG, TR_LTM_TND.MSG_NUM, TR_LTM_TND.MERCHANT_ID, TR_LTM_TND.CODE from TR_LTM_TND ";

  /**
   * put your documentation comment here
   * @param storeId
   * @param begin
   * @param end
   * @return
   * @exception SQLException
   */
  public MediaReportDataTransferObject[] getTotalSalesByStoreIdAndDates(String storeId, Date begin
      , Date end)
      throws SQLException {
    String sql = TR_LTM_TND_SELECT + ", TR_TRN where " + "TR_LTM_TND.AI_TRN = TR_TRN.AI_TRN and "
        + "TR_TRN.ID_STR_RT = ? and " + "TR_TRN.ID_VOID IS NULL and "
        + "TR_TRN.TS_TRN_PRC >= ? and " + "TR_TRN.TS_TRN_PRC <= ?";
    List params = new ArrayList();
    params.add(storeId);
    params.add(begin);
    params.add(end);
    return this.fromBeansToObjects(this.query(new TrLtmTndOracleBean(), sql, params));
  }

  /**
   * put your documentation comment here
   * @param beans
   * @return
   * @exception SQLException
   */
  private MediaReportDataTransferObject[] fromBeansToObjects(BaseOracleBean[] beans)
      throws SQLException {
    MediaReportDataTransferObject[] array = new MediaReportDataTransferObject[beans.length];
    for (int i = 0; i < array.length; i++) {
      array[i] = new MediaReportDataTransferObject();
      TrLtmTndOracleBean trLtmTndBean = (TrLtmTndOracleBean)beans[i];
      array[i].setTypeId(trLtmTndBean.getTyTnd());
     
      //Anjana: Added fix for Media Report going offlien when there is a Mobile terminal payment in the search query
      if(trLtmTndBean.getResMsg()!=null && trLtmTndBean.getResMsg().equalsIgnoreCase("Mobile") &&
    		  (trLtmTndBean.getLuClsTnd().equalsIgnoreCase("VISA") || trLtmTndBean.getLuClsTnd().equalsIgnoreCase("MASTER_CARD"))){
    	  array[i].setTypeId("BCRD"); 
    }
      
      array[i].setTenderClassCode(trLtmTndBean.getLuClsTnd());
      array[i].setMediaCount(1);
      ArmCurrency amt = trLtmTndBean.getMoItmLnTnd();
      if (amt.doubleValue() >= 0) {
        array[i].setGrossAmount(amt);
        array[i].setCreditReturn(new ArmCurrency(amt.getCurrencyType(), 0.0));
        array[i].setNetAmount(amt);
      } else {
        array[i].setGrossAmount(new ArmCurrency(amt.getCurrencyType(), 0.0));
        array[i].setCreditReturn(amt.absoluteValue());
        array[i].setNetAmount(amt);
      }
    }
    return array;
  }
}

