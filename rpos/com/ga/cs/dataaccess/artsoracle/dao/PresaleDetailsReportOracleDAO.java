/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 05/03/2006 | Sandhya   | N/A       | Presale Details Report                             |
 -------------------------------------------------------------------------------------------------- 
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.ga.cs.dataaccess.artsoracle.dao;

import java.sql.SQLException;
import java.util.*;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.BaseOracleDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import com.ga.cs.dataaccess.artsoracle.databean.PresaleDetailsReportBean;
import com.ga.cs.pos.datatranssferobject.PresaleDetailsReport;

/**
 * put your documentation comment here
 */
public class PresaleDetailsReportOracleDAO extends BaseOracleDAO {
  private static final String SQL = "SELECT \n" + "  PRS.ID_PRTY_PRS        CUSTOMER_ID, \n"
      + "  UPPER(PRS.FN_PRS)      FIRST_NAME, \n" + "  UPPER(PRS.LN_PRS)      LAST_NAME, \n"
      + "  (EMP_PRS.FN_PRS || ' ' || EMP_PRS.LN_PRS)   ASSOCIATE_NAME, \n"
      + "  EMP.ARM_EXTERNAL_ID    ASSOCIATE_ID, \n" + "  TRN.AI_TRN             TRANSACTION_ID, \n"
      + "  TRN.TY_GUI_TRN         TRAN_GUI_TYPE, \n" + "  LTM.AI_LN_ITM          LINE_NUMBER, \n"
      + "  LTM.ID_ITM             ITEM_ID, \n" + "  ITM.NM_ITM             ITEM_DESC, \n"
      + "  DECODE(LTM.POS_LN_ITM_TY_ID, 1, 'SOLD', 2, 'RETURNED', 4, 'OPEN', ' ') ITEM_STATUS, \n"
      + "  TRN.TS_TRN_PRC         DATE_ISSUED, \n" + "  LTM.QUANTITY           ORIG_QTY, \n"
      + "  LTM.ITM_SEL_PRICE      ORIG_AMT, \n" + "  null                   DATE_SOLDRETURN, \n"
      + "  null                   CURR_QTY, \n" + "  null                   CURR_AMT \n"
      + "FROM  \n" + "  PA_EM EMP, \n" + "  PA_PRS EMP_PRS, \n" + "  PA_PRS PRS, \n" + "  PA_CT CT, \n" + "  TR_TRN TRN, \n"
      + "  TR_RTL RTL, \n" + "  TR_LTM_RTL_TRN LTM, \n" + "  AS_ITM ITM \n" + "WHERE  \n"
      + "  TRN.ID_VOID IS NULL            AND \n" + "  TRN.ID_STR_RT   = ?            AND \n"
      + "  TRN.TS_TRN_PRC >= ?            AND \n" + "  TRN.TS_TRN_PRC < ? + 1         AND \n"
      + "  TRN.AI_TRN      = RTL.AI_TRN   AND \n" + "  TRN.AI_TRN      = LTM.AI_TRN   AND \n"
      + "  LTM.ID_ITM      = ITM.ID_ITM   AND \n" + "  RTL.CONSULTANT_ID = EMP.ID_EM   AND \n"
      + "  RTL.CONSULTANT_ID = EMP_PRS.ID_PRTY_PRS   AND \n"
      + "  RTL.ID_CT       = CT.ID_PRTY   AND \n" + "  PRS.ID_PRTY_PRS = CT.ID_PRTY   AND \n"
      + "  TRN.AI_TRN IN (SELECT AI_TRN FROM ARM_POS_PRS) AND \n" + "  NOT EXISTS ( \n"
      + "    SELECT 1 \n" + "	FROM ARM_PRS_POS_LN_ITM_DTL PRSDTL \n"
      + "	 WHERE PRSDTL.ORG_AI_TRN = LTM.AI_TRN AND \n"
      + "	 PRSDTL.ORIG_AI_LN_ITM = LTM.AI_LN_ITM AND \n"
      + "	 NVL(PRSDTL.FL_VD_LN_ITM,0) NOT IN ('1') \n" + "	) \n" + "UNION ALL \n" + "SELECT \n"+ "  PRS.ID_PRTY_PRS        CUSTOMER_ID, \n" + "  UPPER(PRS.FN_PRS)      FIRST_NAME, \n"
      + "  UPPER(PRS.LN_PRS)      LAST_NAME, \n"
      + "  (EMP_PRS.FN_PRS || ' ' || EMP_PRS.LN_PRS)    ASSOCIATE_NAME, \n"
      + "  EMP.ARM_EXTERNAL_ID    ASSOCIATE_ID, \n" + "  TRN.AI_TRN             TRANSACTION_ID, \n"
      + "  TRN.TY_GUI_TRN         TRAN_GUI_TYPE, \n" + "  LTM.AI_LN_ITM          LINE_NUMBER, \n"
      + "  LTM.ID_ITM             ITEM_ID, \n" + "  ITM.NM_ITM             ITEM_DESC, \n"
      + "  DECODE(LTM.POS_LN_ITM_TY_ID, 1, 'SOLD', 2, 'RETURNED', 4, 'OPEN', ' ') ITEM_STATUS, \n"
      + "  TRN_ORIG.TS_TRN_PRC    DATE_ISSUED, \n" + "  LTM_ORIG.QUANTITY           ORIG_QTY, \n"
      + "  LTM_ORIG.ITM_RETAIL_PRICE   ORIG_AMT, \n"
      + "  TRN.TS_TRN_PRC         DATE_SOLDRETURN, \n" + "  LTM.QUANTITY           CURR_QTY, \n"
      + "  LTM.ITM_SEL_PRICE      CURR_AMT \n" + "FROM  \n" + "  PA_EM EMP, \n" + "  PA_PRS EMP_PRS, \n"
      + "  PA_PRS PRS, \n" + "  PA_CT CT, \n" + "  TR_TRN TRN, \n" + "  TR_RTL RTL, \n"
      + "  TR_LTM_RTL_TRN LTM, \n" + "  AS_ITM ITM, \n" + "  TR_TRN TRN_ORIG, \n"
      + "  TR_LTM_RTL_TRN LTM_ORIG, \n" + "  ARM_PRS_POS_LN_ITM_DTL PRSDTL  \n" + "WHERE  \n"
      + "  TRN.ID_VOID IS NULL            AND \n" + "  TRN.ID_STR_RT   = ?            AND \n"
      + "  TRN.TS_TRN_PRC >= ?            AND \n" + "  TRN.TS_TRN_PRC < ? + 1         AND \n"
      + "  TRN.AI_TRN      = RTL.AI_TRN   AND \n" + "  TRN.AI_TRN      = LTM.AI_TRN   AND \n"
      + "  LTM.ID_ITM      = ITM.ID_ITM   AND \n" + "  RTL.ID_CT       = CT.ID_PRTY   AND \n"
      + "  PRS.ID_PRTY_PRS = CT.ID_PRTY   AND \n" + "  RTL.CONSULTANT_ID = EMP.ID_EM   AND \n"
      + "  RTL.CONSULTANT_ID = EMP_PRS.ID_PRTY_PRS   AND \n"
      + "  LTM.AI_TRN         = PRSDTL.AI_TRN   AND \n"
      + "  LTM.AI_LN_ITM      = PRSDTL.AI_LN_ITM  AND \n"
      + "  LTM_ORIG.AI_TRN    = PRSDTL.ORG_AI_TRN     AND \n"
      + "  LTM_ORIG.AI_LN_ITM = PRSDTL.ORIG_AI_LN_ITM  AND \n"
      + "  TRN_ORIG.AI_TRN    = LTM_ORIG.AI_TRN \n"
      + "ORDER BY LAST_NAME, FIRST_NAME, TRANSACTION_ID, LINE_NUMBER";
  public static String DEPT = "DEPT";
  public static String DEPT_DESC = "DEPT_DESC";

  /**
   * put your documentation comment here
   * @param storeId
   * @param begin
   * @param end
   * @return
   * @exception SQLException
   */
  public PresaleDetailsReport[] getPresaleDetails(String storeId, Date begin, Date end)
      throws SQLException {
    List params = new ArrayList();
    params.add(storeId);
    params.add(begin);
    params.add(end);
    params.add(storeId);
    params.add(begin);
    params.add(end);
    PresaleDetailsReportBean beans[] = (PresaleDetailsReportBean[])query(new
    		PresaleDetailsReportBean(), SQL, params);
    return fromBeansToObjects(beans);
  }

  /**
   * put your documentation comment here
   * @param beans
   * @return
   */
  private PresaleDetailsReport[] fromBeansToObjects(BaseOracleBean[] beans) {
	PresaleDetailsReport[] array = new PresaleDetailsReport[beans.length];
    for (int i = 0; i < array.length; i++) {
      array[i] = fromBeanToObject(beans[i]);
    }
    return array;
  }

  /**
   * put your documentation comment here
   * @param bean
   * @return
   */
  private PresaleDetailsReport fromBeanToObject(BaseOracleBean bean) {
	PresaleDetailsReport report = new PresaleDetailsReport();
    report.bean = (PresaleDetailsReportBean)bean;
    return report;
  }
}

