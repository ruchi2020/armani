/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.dataaccess.artsoracle.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.currency.UnsupportedCurrencyTypeException;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.ArtsConstants;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.BaseOracleDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.EodOracleDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.RkEodOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.RkSosOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.TrLtmTndOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.TrTrnOracleBean;
import com.ga.cs.pos.datatranssferobject.ConsolidatedOverShortReportDataTransferObject;


/**
 * put your documentation comment here
 */
public class ConsolidatedOverShortReportOracleDAO extends BaseOracleDAO {

  /**
   * put your documentation comment here
   * @param storeId
   * @param registerIds
   * @param beginDate
   * @param endDate
   * @return
   * @exception SQLException
   */
  public HashMap getStartFund(String storeId, String[] registerIds, Date beginDate, Date endDate)
      throws SQLException {
    String sql = "select " + RkSosOracleBean.COL_DRAWER_FUND + " from "
        + TrTrnOracleBean.TABLE_NAME + " ," + RkSosOracleBean.TABLE_NAME + " where "
        + TrTrnOracleBean.COL_AI_TRN + " = " + RkSosOracleBean.COL_AI_TRN + " and "
        + TrTrnOracleBean.COL_TY_TRN + " = '" + ArtsConstants.TRANSACTION_TYPE_SOS + "'" + " and "
        + TrTrnOracleBean.COL_ID_STR_RT + " = ? " + " and " + TrTrnOracleBean.COL_ID_RPSTY_TND
        + " = ? " + " and " + TrTrnOracleBean.COL_TS_TRN_CRT + " >= ? " + " and "
        + TrTrnOracleBean.COL_TS_TRN_CRT + " <= ? ";
    HashMap map = new HashMap();
    for (int i = 0; i < registerIds.length; i++) {
      List params = new ArrayList();
      params.add(storeId);
      params.add(registerIds[i]);
      params.add(beginDate);
      params.add(endDate);
      String[] drawerFundStrings = this.queryForIds(sql, params);
      if (drawerFundStrings != null && drawerFundStrings.length > 0) {
        try {
          ArmCurrency sum = fromStringToCurrency(drawerFundStrings[0]);
          if (drawerFundStrings.length > 1) {
            for (int j = 1; j < drawerFundStrings.length; j++) {
              sum = sum.add(fromStringToCurrency(drawerFundStrings[j]));
            }
          }
          map.put(registerIds[i], sum);
        } catch (UnsupportedCurrencyTypeException ucte) {
          //should never happen
        } catch (CurrencyException ce) {
          //should never happen
        }
      }
    }
    return map;
  }

  public static final String RK_EOD_SELECT = "select MGR_SAYS_CASH_LIST, MGR_SAYS_CHEK_LIST, MGR_SAYS_BCRD, MGR_SAYS_AMEX, MGR_SAYS_DISC, MGR_SAYS_TR_CHKS, MGR_SAYS_MNY_ODR, REG_DRWR_FUND, RK_EOD.AI_TRN, MGR_SAYS_MALLCERTIFICATE from RK_EOD ";

  /**
   * put your documentation comment here
   * @param storeId
   * @param registerIds
   * @param beginDate
   * @param endDate
   * @return
   * @exception SQLException
   */
  public HashMap getEODCounts(String storeId, String[] registerIds, Date beginDate, Date endDate)
      throws SQLException {
    String sql = RK_EOD_SELECT + " ," + TrTrnOracleBean.TABLE_NAME + " where "
        + TrTrnOracleBean.COL_AI_TRN + " = " + RkEodOracleBean.COL_AI_TRN + " and "
        + TrTrnOracleBean.COL_TY_TRN + " = '" + ArtsConstants.TRANSACTION_TYPE_EOD + "'" + " and "
        + TrTrnOracleBean.COL_ID_STR_RT + " = ? " + " and " + TrTrnOracleBean.COL_ID_RPSTY_TND
        + " = ? " + " and " + TrTrnOracleBean.COL_TS_TRN_CRT + " >= ? " + " and "
        + TrTrnOracleBean.COL_TS_TRN_CRT + " <= ? ";
    HashMap map = new HashMap();
    for (int i = 0; i < registerIds.length; i++) {
      List params = new ArrayList();
      params.add(storeId);
      params.add(registerIds[i]);
      params.add(beginDate);
      params.add(endDate);
      BaseOracleBean[] beans = this.query(new RkEodOracleBean(), sql, params);
      if (beans != null && beans.length > 0) {
        ConsolidatedOverShortReportDataTransferObject object = fromBeanToObject(registerIds[i]
            , beans);
        map.put(registerIds[i], object);
      }
    }
    return map;
  }

  private static final String TR_LTM_TND_SELECT = "select TR_LTM_TND.AI_TRN, TR_LTM_TND.AI_LN_ITM, TR_LTM_TND.TY_TND, TR_LTM_TND.ID_ACNT_NMB, TR_LTM_TND.ID_ACNT_TND, TR_LTM_TND.MO_ITM_LN_TND, TR_LTM_TND.LU_CLS_TND, TR_LTM_TND.JOURNAL_KEY, TR_LTM_TND.RES_MSG, TR_LTM_TND.MSG_NUM, TR_LTM_TND.MERCHANT_ID, TR_LTM_TND.CODE from TR_LTM_TND ";

  /**
   * put your documentation comment here
   * @param storeId
   * @param registerIds
   * @param beginDate
   * @param endDate
   * @return
   * @exception SQLException
   */
  public HashMap getSales(String storeId, String[] registerIds, Date beginDate, Date endDate)
      throws SQLException {
    String sql = TR_LTM_TND_SELECT + " ," + TrTrnOracleBean.TABLE_NAME + " where "
        + TrTrnOracleBean.COL_AI_TRN + " = " + TrLtmTndOracleBean.COL_AI_TRN + " and "
        + TrTrnOracleBean.COL_ID_VOID + " is null" + " and " + TrTrnOracleBean.COL_ID_STR_RT
        + " = ? " + " and " + TrTrnOracleBean.COL_ID_RPSTY_TND + " = ? " + " and "
        + TrTrnOracleBean.COL_TS_TRN_CRT + " >= ? " + " and " + TrTrnOracleBean.COL_TS_TRN_CRT
        + " <= ? ";
    HashMap map = new HashMap();
    for (int i = 0; i < registerIds.length; i++) {
      List params = new ArrayList();
      params.add(storeId);
      params.add(registerIds[i]);
      params.add(beginDate);
      params.add(endDate);
      BaseOracleBean[] beans = this.query(new TrLtmTndOracleBean(), sql, params);
      if (beans != null && beans.length > 0) {
        ConsolidatedOverShortReportDataTransferObject object = fromBeanToObject(registerIds[i]
            , beans);
        map.put(registerIds[i], object);
      }
    }
    return map;
  }

  /**
   * put your documentation comment here
   * @param registerId
   * @param beans
   * @return
   * @exception SQLException
   */
  private ConsolidatedOverShortReportDataTransferObject fromBeanToObject(String registerId
      , BaseOracleBean[] beans)
      throws SQLException {
    ConsolidatedOverShortReportDataTransferObject sum = new
        ConsolidatedOverShortReportDataTransferObject();
    for (int i = 0; i < beans.length; i++) {
      if (beans[i] instanceof RkEodOracleBean) {
        sum.add(this.fromBeanToObject(registerId, (RkEodOracleBean)beans[i]));
      } else if (beans[i] instanceof TrLtmTndOracleBean) {
        sum.add(this.fromBeanToObject(registerId, (TrLtmTndOracleBean)beans[i]));
      }
    }
    return sum;
  }

  /**
   * put your documentation comment here
   * @param registerId
   * @param bean
   * @return
   * @exception SQLException
   */
  private ConsolidatedOverShortReportDataTransferObject fromBeanToObject(String registerId
      , RkEodOracleBean bean)
      throws SQLException {
    ConsolidatedOverShortReportDataTransferObject object = new
        ConsolidatedOverShortReportDataTransferObject();
    object.setRegisterId(registerId);
    EodOracleDAO eodDAO = new EodOracleDAO();
    ArmCurrency[] cashes = eodDAO.fromStringToCurrencies(bean.getMgrSaysCashList());
    for (int i = 0; i < cashes.length; i++) {
	//Anjana:06/23/2017 to support CAD currency for canada store
      if (cashes[i].getCurrencyType().getCode().equals("USD") || cashes[i].getCurrencyType().getCode().equals("CAD")) {
        object.setTotalDollars(cashes[i]);
      } else if (cashes[i].getCurrencyType().getCode().equals("JPY")) {
        object.setTotalYen(cashes[i]);
      }
    }
    ArmCurrency[] checks = eodDAO.fromStringToCurrencies(bean.getMgrSaysChekList());
    for (int i = 0; i < checks.length; i++) {
		//Anjana:06/23/2017 to support CAD currency for canada store
      if (checks[i].getCurrencyType().getCode().equals("USD") || checks[i].getCurrencyType().getCode().equals("CAD")) {
        object.setTendeerTypeValue(ArtsConstants.PAYMENT_TYPE_CHECK, checks[i]);
      }
    }
    object.setTendeerTypeValue(ArtsConstants.PAYMENT_TYPE_MALLCERT, bean.getMgrSaysMallcertificate());
    object.setTotalTravChecks(bean.getMgrSaysTrChks());
    object.setTendeerTypeValue("ENDING_DRAWER_FUND", bean.getRegDrwrFund());
    return object;
  }

  /**
   * put your documentation comment here
   * @param registerId
   * @param bean
   * @return
   * @exception SQLException
   */
  private ConsolidatedOverShortReportDataTransferObject fromBeanToObject(String registerId
      , TrLtmTndOracleBean bean)
      throws SQLException {
    ConsolidatedOverShortReportDataTransferObject object = new
        ConsolidatedOverShortReportDataTransferObject();
    object.setRegisterId(registerId);
    String type = bean.getTyTnd();
    ArmCurrency amount = bean.getMoItmLnTnd();
    if (type.equals(ArtsConstants.PAYMENT_TYPE_CASH)) {
		//Anjana:06/23/2017 to support CAD currency for canada store
      if (amount.getCurrencyType().getCode().equals("USD") || amount.getCurrencyType().getCode().equals("CAD")) {
        object.setTotalDollars(amount);
      } else if (amount.getCurrencyType().getCode().equals("JPY")) {
        object.setTotalYen(amount);
      }
    } else if (type.equals(ArtsConstants.PAYMENT_TYPE_CHECK)
        || type.equals(ArtsConstants.PAYMENT_TYPE_EMPLOYEE_CHECK)
        || type.equals(ArtsConstants.PAYMENT_TYPE_BANK_CHECK)
        || type.equals(ArtsConstants.PAYMENT_TYPE_CAD_BANK_CHECK)
        || type.equals(ArtsConstants.PAYMENT_TYPE_BUSINESS_CHECK)) {
			//Anjana:06/23/2017 to support CAD currency for canada store
      if (amount.getCurrencyType().getCode().equals("USD") || (amount.getCurrencyType().getCode().equals("CAD"))) {
        object.setTendeerTypeValue(ArtsConstants.PAYMENT_TYPE_CHECK, amount);
      }
    } else if (type.equals(ArtsConstants.PAYMENT_TYPE_TRAVELLERS_CHECK)
        || type.equals("TRAVERLS_CHECK")) {
      object.setTotalTravChecks(amount);
    } else if (type.equals(ArtsConstants.PAYMENT_TYPE_GIFT_CERT)){
      object.setTendeerTypeValue(ArtsConstants.PAYMENT_TYPE_MALLCERT, amount);
    }
    return object;
  }
}

