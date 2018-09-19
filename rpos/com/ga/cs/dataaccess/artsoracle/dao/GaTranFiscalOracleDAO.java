/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.dataaccess.artsoracle.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Hashtable;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.BaseOracleDAO;
import com.ga.cs.dataaccess.artsoracle.databean.GaFiscalReportBean;
import com.ga.cs.dataaccess.artsoracle.databean.GaTranFiscalReportBean;
import com.ga.cs.pos.datatranssferobject.GaFiscalReportDataTransferObject;
import com.ga.cs.pos.datatranssferobject.GaTranFiscalReportDataTransferObject;


/**
 * GaReportTran DAO for GA customized reports
 * @author mdymarska
 */
public class GaTranFiscalOracleDAO extends BaseOracleDAO {
  public static String FISCAL_DAY = "FISCAL_DAY";
  public static String FISCAL_HALF = "FISCAL_HALF";
  public static String FISCAL_SEQ = "FISCAL_SEQ";
  public static String SALES = "SALES";
  public static String NET_SALES = "NET_SALES";
  public static String GROSS_SALES = "GROSS_SALES";
  public static String QTY = "QTY";

  /**
   * put your documentation comment here
   * @param storeId
   * @param begin
   * @param end
   * @return
   * @exception SQLException
   */
  public GaTranFiscalReportDataTransferObject[] getFiscalByStoreIdAndDates(String storeId
      , Date begin, Date end)
      throws SQLException {
    String sql = " SELECT  to_char(TN.TS_TRN_SBM,'DD/MM/YYYY') " + FISCAL_DAY
        + " , garpos_fiscal.fiscal_half(TN.TS_TRN_SBM) " + FISCAL_HALF + ","
        + " garpos_fiscal.fiscal_seq(TN.TS_TRN_SBM)" + FISCAL_SEQ + ","
        + " NVL(COUNT(DISTINCT TT.AI_TRN), 0) " + QTY + "," + " NVL(ROUND(SUM ( DECODE( TT.POS_LN_ITM_TY_ID-1,0, DTL.NET_AMT, 0)  - DECODE( TT.POS_LN_ITM_TY_ID-2,0, DTL.NET_AMT, 0)), 2), 0)"
        + NET_SALES + ", " + " NVL(ROUND(SUM (( DECODE( TT.POS_LN_ITM_TY_ID-1,0, (ARM_UTIL_PKG.CONVERT_TO_NUMBER(TT.ITM_RETAIL_PRICE,SUBSTR(TT.ITM_RETAIL_PRICE,0,3) ))*(TO_NUMBER(TT.QUANTITY)),0) ) -( DECODE( TT.POS_LN_ITM_TY_ID-2,0, (ARM_UTIL_PKG.convert_to_number(TT.ITM_RETAIL_PRICE,substr(TT.ITM_RETAIL_PRICE,0,3) ))*(TO_NUMBER(TT.QUANTITY)),0))), 2), 0)"
        + GROSS_SALES + " " + " FROM (SELECT SUM(ARM_UTIL_PKG.convert_to_number(RK_POS_LN_ITM_DTL.net_AMT, substr(RK_POS_LN_ITM_DTL.NET_AMT,0,3))) NET_AMT, AI_TRN, AI_LN_ITM FROM RK_POS_LN_ITM_DTL GROUP BY AI_TRN, AI_LN_ITM) DTL, TR_LTM_RTL_TRN TT,TR_TRN TN, AS_ITM A "
        + " WHERE TT.AI_TRN  =TN.AI_TRN " + " AND TT.AI_TRN = DTL.AI_TRN "
        + " AND TT.AI_LN_ITM = DTL.AI_LN_ITM " + " AND TT.ID_ITM= A.ID_ITM "
        + " AND nvl(A.ID_DPT_POS,'-') != '999' " + " AND TN.ID_VOID IS NULL "
        + "and TT.POS_LN_ITM_TY_ID in (1, 2) "
        + "AND NOT EXISTS ( SELECT 'x' from ARM_CSG_POS_LN_ITM_DTL CS, "
        + " TR_LTM_RTL_TRN TTS, TR_TRN TN " + "WHERE TTS.AI_TRN=TN.AI_TRN "
        + "and TTS.AI_TRN=CS.AI_TRN " + "AND TTS.POS_LN_ITM_TY_ID=2  " + "AND TN.ID_VOID IS NULL "
        + "AND CS.AI_TRN=TT.AI_TRN) " + " AND TN.ID_STR_RT        = ? "
        + " AND TN.TS_TRN_SBM BETWEEN ? AND ? " + " GROUP BY to_char(TN.TS_TRN_SBM,'DD/MM/YYYY'), garpos_fiscal.fiscal_half(TN.TS_TRN_SBM),garpos_fiscal.fiscal_seq(TN.TS_TRN_SBM) "
        + " ORDER BY to_char(TN.TS_TRN_SBM,'DD/MM/YYYY'), garpos_fiscal.fiscal_seq(TN.TS_TRN_SBM)";
    List params = new ArrayList();
    params.add(storeId);
    params.add(begin);
    params.add(end);
    GaTranFiscalReportBean beans[] = (GaTranFiscalReportBean[])query(new GaTranFiscalReportBean()
        , sql, params);
    return fromBeansToObjects(beans);
  }

  /**
   * put your documentation comment here
   * @param beans
   * @return
   * @exception SQLException
   */
  private GaTranFiscalReportDataTransferObject[] fromBeansToObjects(GaTranFiscalReportBean[] beans)
      throws SQLException {
    GaTranFiscalReportDataTransferObject[] array = new GaTranFiscalReportDataTransferObject[beans.
        length];
    for (int i = 0; i < array.length; i++) {
      GaTranFiscalReportDataTransferObject ast = new GaTranFiscalReportDataTransferObject(beans[i].
          getFiscalDay());
      ast.setFiscalHalf(beans[i].getFiscalHalf());
      ast.setFiscalSeq(beans[i].getFiscalSeq());
      ast.setQty(beans[i].getQty());
      ast.setSales(beans[i].getSales());
      ast.setNetSales(beans[i].getNetSales());
      ast.setGrossSales(beans[i].getGrossSales());
      array[i] = ast;
    }
    return array;
  }
}

