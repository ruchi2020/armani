/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.dataaccess.artsoracle.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.BaseOracleDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import com.ga.cs.dataaccess.artsoracle.databean.ConsignmentSummaryReportBean;
import com.ga.cs.pos.datatranssferobject.ConsignmentSummaryReportDataTransferObject;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.TrTrnOracleBean;


/**
 * put your documentation comment here
 */
public class ConsignmentSummaryReportOracleDAO extends BaseOracleDAO {

  /**
   * put your documentation comment here
   * @param storeId
   * @param begin
   * @param end
   * @return
   * @exception SQLException
   */
  public ConsignmentSummaryReportDataTransferObject[] getConsignmentSummaryByCustomer(String
      storeId, Date begin, Date end)
      throws SQLException {
    String sql = ConsignmentSummaryReportBean.selectSql;
    List params = new ArrayList();
    params.add(storeId);
    if(begin != null) {
      if(end == null)
        end = new Date();
      sql = sql + " and " + TrTrnOracleBean.COL_TS_TRN_CRT + " >= ? " + " and "
          + TrTrnOracleBean.COL_TS_TRN_CRT + " <= ? ";
      params.add(begin);
      params.add(end);
    }
    BaseOracleBean[] beans = this.query(new ConsignmentSummaryReportBean(), sql, params);
    return this.fromBeansToObjects(beans);
  }

  /**
   * put your documentation comment here
   * @param beans
   * @return
   */
  private ConsignmentSummaryReportDataTransferObject[] fromBeansToObjects(BaseOracleBean[] beans) {
    HashMap map = new HashMap();
    for (int i = 0; i < beans.length; i++) {
      ConsignmentSummaryReportBean bean = (ConsignmentSummaryReportBean)beans[i];
      String key = bean.getAiTrn();
      ConsignmentSummaryReportDataTransferObject value = (
          ConsignmentSummaryReportDataTransferObject)map.get(key);
      if (value == null) {
        map.put(key, this.fromBeanToObject(bean));
      } else {
        try {
          value.addQtyAndPrice(bean.getQuantity(), bean.getItmRetailPrice());
        } catch (CurrencyException ce) {}
      }
    }
    ConsignmentSummaryReportDataTransferObject[] array = (
        ConsignmentSummaryReportDataTransferObject[])map.values().toArray(new
        ConsignmentSummaryReportDataTransferObject[0]);
    Arrays.sort(array);
    return array;
  }

  /**
   * put your documentation comment here
   * @param bean
   * @return
   */
  private ConsignmentSummaryReportDataTransferObject fromBeanToObject(ConsignmentSummaryReportBean
      bean) {
    ConsignmentSummaryReportDataTransferObject object = new
        ConsignmentSummaryReportDataTransferObject();
    object.setTransactionId(bean.getAiTrn());
    object.setTime(bean.getTsTrnCrt());
    object.setConsignmentId(bean.getIdConsignment());
    object.setCustomerId(bean.getIdCt());
    object.setConsultantId(bean.getConsultantId());
    object.setPrice(bean.getItmRetailPrice());
    object.setQuantity(bean.getQuantity());
    object.setCustomerLastName(bean.getLnPrs());
    object.setCustomerFistName(bean.getFnPrs());
    return object;
  }
}

