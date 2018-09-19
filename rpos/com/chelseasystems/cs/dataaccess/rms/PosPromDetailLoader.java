/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 03-05-2005 | Manpreet  | N/A       | Added Logging capability for Audit columns   |
 --------------------------------------------------------------------------------------------
 | 1   | 02-26-2005 | Anand     | N/A       | Created for bulk load of promotions           |
 --------------------------------------------------------------------------------------------
 */


package  com.chelseasystems.cs.dataaccess.rms;

import  com.chelseasystems.cr.config.ConfigMgr;
import  com.chelseasystems.cs.dataaccess.artsoracle.dao.ArmStgPrmOracleDAO;
import  com.chelseasystems.cs.dataaccess.ArmStgPrmDAO;
import  com.chelseasystems.cs.dataaccess.ThresholdPromotionDAO;
import  com.chelseasystems.cs.rms.CMSPosPromDetail;
import  com.chelseasystems.cs.rms.CMSPosPromDetailToPromotion;
import  com.chelseasystems.cs.dataaccess.PromotionDAO;
import  java.sql.SQLException;
import  com.chelseasystems.cr.logging.LoggingFileServices;
import  com.chelseasystems.cr.logging.LoggingServices;
import  com.chelseasystems.cr.pricing.IPromotion;
import  com.chelseasystems.cs.pricing.ItemThresholdPromotion;
import  com.chelseasystems.cs.pricing.CMSThresholdPromotion;
import  com.chelseasystems.cs.pricing.CMSPromotion;
import  com.chelseasystems.cr.currency.ArmCurrency;
import  com.chelseasystems.cs.store.CMSStore;
import  java.util.List;
import  java.util.Date;
import  java.text.SimpleDateFormat;
import  java.util.ArrayList;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmProcessLogOracleBean;
import  com.chelseasystems.cr.database.ParametricStatement;
import  java.text.ParseException;


/**
 */
public class PosPromDetailLoader {
  private static final String className = "com.chelseasystems.cs.dataaccess.rms.PosPromDetailLoader";
  /**
   * Upload to base tables Failed
   */
  private final String UPLOAD_FAILED = "1";
  /**
   * Upload to base tables successful
   */
  private final String UPLOAD_SUCCESSFUL = "0";
  private static LoggingFileServices loggingFileServices;
  private static final String LOG_FILE = "/PosPromDetail.log";
  private static String sLogKey = "ARM_STG_PRM";
  ArmStgPrmDAO vPosPromDetailDAO = null;
  PromotionDAO promotionDAO = null;

  /**
   *
   * @param args String[]
   * args[0] Date
   * args[1] Interface Key
   * args[2] FileName
   */
  public static void main (String[] args) {
    try {
      //Set Logging file services.
      loggingFileServices = (LoggingFileServices)LoggingServices.getCurrent();
      loggingFileServices.setLogFile(LOG_FILE);
      if (args.length < 1)
        throw  new Exception("Start Date(yyyyMMddhh:mm:ss),Date Needed");
      else if (args.length < 2)
        throw  new Exception("Process File Name, File Name needed");
      else if (args.length < 3)
        throw  new Exception("InterFace Key needed");
      //code modified to accomodate the hours logging in ARM_PROCESS_LOG table
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhh:mm:ss");
      Date dStartDate = new Date();
      dStartDate = sdf.parse(args[0]);
      PosPromDetailLoader loader = new PosPromDetailLoader();
      boolean exitCode = loader.importData(dStartDate, args[2], args[1]);
      if (exitCode)
        System.exit(0);         //Success
      else
        System.exit(1);         //Failed
    } catch (Exception e) {
      e.printStackTrace();
      //    	if(e instanceof ParseException){
      //    	((ParseException)e).getErrorOffset();
      //     }
      if (loggingFileServices != null) {
        loggingFileServices.logMsg("ArmStgPrm", sLogKey, "Exception", "See Exception", LoggingServices.MAJOR, e);
        loggingFileServices.recordMsg();
        System.exit(1);         //Failed
      }
      System.exit(1);           //Failed
    }
  }

  /**
   */
  public PosPromDetailLoader () {
    ConfigMgr configMgr = new ConfigMgr("jdbc.cfg");
    vPosPromDetailDAO = (ArmStgPrmDAO)configMgr.getObject("ARMSTGPRM_DAO");
    promotionDAO = (PromotionDAO)configMgr.getObject("PROMOTION_DAO");
  }

  /**
   *
   * @param startDate Date
   * @param sProcessFileName String
   * @param sInterfaceKey String
   * @throws Exception
   * @return boolean
   */
  public boolean importData (Date startDate, String sProcessFileName, String sInterfaceKey) throws Exception {
    int i = 0;
    CMSPosPromDetail[] posPromDetails = null;
    boolean bUpdateFailed = false;
    //PosPromDetailErrorDAO detailErrorDAO = (PosPromDetailErrorDAO) configMgr.getObject("POS_PROM_DETAIL_ERROR_DAO");
    int iRecordNum = 0;
    CMSPosPromDetailToPromotion cmsPosPromDetToProm = new CMSPosPromDetailToPromotion();
    try {
      posPromDetails = vPosPromDetailDAO.selectByStgStatus();
      for (i = 0; i < posPromDetails.length; i++) {
        CMSPosPromDetail posPromDet = posPromDetails[i];
        if (posPromDet != null) {
          try {
            IPromotion currPromo = null;
            posPromDet.doSetPromTranType("1001");
            IPromotion iPromo = (CMSPromotion)cmsPosPromDetToProm.map(posPromDet);
            promotionDAO.insert(iPromo);
            posPromDet.doSetStgErrorMessage("");
            posPromDet.doSetStgProcessDate(new Date());
            posPromDet.doSetStgStatus("0");
            logPromotionStagingTable(posPromDet);
            iRecordNum++;
          } catch (Exception ex) {
            ex.printStackTrace();
            bUpdateFailed = true;
            loggingFileServices.logMsg(this.getClass().getName(), sLogKey, "Exception", "See Exception", LoggingServices.MAJOR, ex);
            loggingFileServices.recordMsg();
            posPromDet.doSetStgErrorMessage(ex.getMessage());
            posPromDet.doSetStgProcessDate(new Date());
            posPromDet.doSetStgStatus("1");
            logPromotionStagingTable(posPromDet);
          }
        }
      }         //end for
      loggingFileServices.logMsg(className, null, "Total Records Processed : " + posPromDetails.length, null, LoggingServices.INFO);
    } catch (Exception ex) {
      ex.printStackTrace();
      bUpdateFailed = true;
      loggingFileServices.logMsg(className, null, "Exception in main : ", null, LoggingServices.MAJOR);
    }
    loggingFileServices.recordMsg();
    //Update the ARM_PROCESS_LOG Table
    try {
      List params = new ArrayList();
      params.add(sInterfaceKey);
      params.add(startDate);
      params.add(new Date());
      if (bUpdateFailed)
        params.add(UPLOAD_FAILED);
      else
        params.add(UPLOAD_SUCCESSFUL);
      params.add(new Integer(iRecordNum));
      params.add(sProcessFileName);
      ArmProcessLogOracleBean armProcessLogOracleBean = new ArmProcessLogOracleBean();
      ((ArmStgPrmOracleDAO)vPosPromDetailDAO).execute(new ParametricStatement(armProcessLogOracleBean.getInsertSql(), params));
    } catch (Exception exc) {
      loggingFileServices.logMsg(this.getClass().getName(), sLogKey, "Exception", "See Exception", LoggingServices.MAJOR, exc);
      loggingFileServices.recordMsg();
    }
    return  bUpdateFailed ? false : true;
  }

  /**
   *
   * @param errMessage String
   */
  private void logPromotionStagingTable (CMSPosPromDetail posPromDetail) {
    // Code to Add error in database
    try {
      vPosPromDetailDAO.update(posPromDetail);
    } catch (Exception ex) {
      loggingFileServices.logMsg(this.getClass().getName(), sLogKey, "Exception", "See Exception", LoggingServices.MAJOR, ex);
      loggingFileServices.recordMsg();
      ex.printStackTrace();
    }
  }

  /**
   *
   * @param startDate Date
   * @param sInterfaceKey String
   * @param isSuccessful boolean
   * @param iRecordNum int
   * @param sProcessFileName String
   */
  private void updateProcessLog (Date startDate, String sInterfaceKey, boolean isSuccessful, int iRecordNum, String sProcessFileName) {
    try {
      List params = new ArrayList();
      params.add(sInterfaceKey);
      params.add(startDate);
      params.add(new Date());
      if (isSuccessful)
        params.add(UPLOAD_SUCCESSFUL);
      else
        params.add(UPLOAD_FAILED);
      params.add(new Integer(iRecordNum));
      params.add(sProcessFileName);
      ArmProcessLogOracleBean armProcessLogOracleBean = new ArmProcessLogOracleBean();
      ((ArmStgPrmOracleDAO)vPosPromDetailDAO).execute(new ParametricStatement(armProcessLogOracleBean.getInsertSql(), params));
    } catch (Exception exc) {
      loggingFileServices.logMsg(this.getClass().getName(), sLogKey, "Exception", "See Exception", LoggingServices.MAJOR, exc);
      loggingFileServices.recordMsg();
    }
  }
}



