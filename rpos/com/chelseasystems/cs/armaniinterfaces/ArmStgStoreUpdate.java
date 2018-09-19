/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.armaniinterfaces;

/*
 * History:-
 +--------+------------+-----------+------------------+------------------------------+
 | Ver#   |    Date    |   By      |        Defect #  |              Description     |
 +--------+------------+-----------+------------------+------------------------------+
 | 5      | 03-03-2005 | Khyati    | SKipping on error| Added new Method for logging |
 |        |            |           |                  | errors logStoreStagingTable  |                    |
 +--------+------------+-----------+------------------+------------------------------+
 | 4      | 03-02-2005 | Manpreet  | N/A              |Modified for TransactionType=0|
 +--------+------------+-----------+------------------+------------------------------+
 | 3      | 03-02-2005 | Manpreet  | N/A              | Added Logging capabilty      |
 |        |            |           |                  | LogFiles & Process_Log table |
 +--------+------------+-----------+------------------+------------------------------+
 | 2      | 02-16-2005 | Salil     | Alternate Tel    | Changed to accomodate alt.   |
 |        |            |           | for company      | telephone for company        |
 +--------+------------+-----------+------------------+------------------------------+
 | 1      | 01-20-2005 | Manpreet  | Original         | Original Version per spec    |
 +--------+------------+-----------+------------------+------------------------------+
 */
import com.chelseasystems.cs.dataaccess.artsoracle.dao.*;
import com.chelseasystems.cs.dataaccess.*;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cr.currency.CurrencyType;
import com.chelseasystems.cr.telephone.Telephone;
import com.chelseasystems.cr.telephone.TelephoneType;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmProcessLogOracleBean;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * <p>Title: </p>
 *
 * <p>Description: This class is used to Transform StagingTables into Base Tables. </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: SkillNetInc. </p>
 *
 * @author Bawa Manpreet Singh.
 * @version 1.0
 * Date Created : 01/20/2005
 *
 */
public class ArmStgStoreUpdate {
  /**
   * Upload to base tables Failed
   */
  private final String UPLOAD_FAILED = "1";
  /**
   * Upload to base tables successful
   */
  private final String UPLOAD_SUCCESSFUL = "0";
  /**
   * TransactionType=1
   */
  private final long INSERT_RECORD = 1;
  /**
   * TransactionType=2
   */
  private final long UPDATE_RECORD = 2;
  /**
   * TransactionType=0
   */
  private final long INSERT_UPDATE_RECORD = 0;
  /**
   * Delimits StoreID and CompanyCode
   */
  private final char DELIMITER = '*';
  /**
   * Reference to ArmaniStagingStoreDAO in config. file.
   */
  private final String ARM_STG_STORE_DAO = "ARMSTGSTORE_DAO";
  /**
   * Reference to StoreDAO in config. file
   */
  private final String STORE_DAO = "STORE_DAO";
  /**
   * Status of code while execution, used for TroubleShooting
   */
  private static String sLogKey = "ARMS_STG_STORE_UPDATE";
  /**
   *  Configuration Manager
   */
  private ConfigMgr jdbcCfg;
  /**
   * ArmaniStageOracleDAO
   */
  private ArmStgStoreOracleDAO armStgStoreDAO;
  ArmStgStoreData armStgStoreData;
  /**
   * StoreDAO
   */
  private StoreDAO storeDAO; // Store DAO
  /**
   * Armani Staging Store Data
   */
  private ArmStgStoreData[] arrayArmStgStoreData;
  /**
   * Logging file name and location
   */
  private static final String LOG_FILE = "Store/ArmStgStoreUpld.log";
  /**
   * Logging Services
   */
  private static LoggingFileServices loggingFileServices;

  /**
   * Constructor
   */
  public ArmStgStoreUpdate() {
    sLogKey = "Creating DAOS";
    jdbcCfg = new ConfigMgr("jdbc.cfg");
    // Get anchor to DAO
    armStgStoreDAO = (ArmStgStoreOracleDAO)jdbcCfg.getObject(ARM_STG_STORE_DAO);
    storeDAO = (StoreOracleDAO)jdbcCfg.getObject(STORE_DAO);
  }

  /**
   * Import Data from DAO and put into Base Tables.
   * @param startDate Date
   * @param sProcessFileName String
   * @param sInterfaceKey String
   * @throws Exception
   * @return boolean
   */
  // Method signature changed -- Manpreet S Bawa (03/02/05)
  // Added attributes to be logged in ARM_PROCESS_LOG table.
  // public void importData()
  public boolean importData(Date startDate, String sInterfaceKey, String sProcessFileName)
      throws Exception {
    int iCtr; // Counter Variable
    int iLength; // Holds length of array
    int iStoreCtr; // Counter Variable
    int iRMSCtr; // Counter Variable
    long lTmp; // Temp. variable
    int iRecordNum = 0;
    boolean bUpdateSuccessful = false;
    //        ArmStgStoreData armStgStoreData;  // Store data
    CMSStore cmsStore; // Store
    String sStoreID = null;
    ParametricStatement[] storeParametricStatements; // Store
    ParametricStatement[] allStatements; // All
    ParametricStatement[] rmsStoreParametricStatements; // RMSStore
    lTmp = 0;
    cmsStore = null;
    armStgStoreData = new ArmStgStoreData();
    storeParametricStatements = null;
    arrayArmStgStoreData = armStgStoreDAO.getNewArmStgStoreData();
    sLogKey = "Retrieving Staging Data";
    if (arrayArmStgStoreData == null || arrayArmStgStoreData.length < 1) {
      bUpdateSuccessful = true;
      updateProcessLog(startDate, sInterfaceKey, bUpdateSuccessful, iRecordNum, sProcessFileName);
      return bUpdateSuccessful;
    }
    for (iCtr = 0; iCtr < arrayArmStgStoreData.length; iCtr++) {
      try {
        lTmp = arrayArmStgStoreData[iCtr].getTransactionType().longValue();
        // IF Transaction type is not 0,1 or 2 then do nothing.
        if (lTmp != INSERT_RECORD && lTmp != UPDATE_RECORD && lTmp != INSERT_UPDATE_RECORD) {
          bUpdateSuccessful = true;
          updateProcessLog(startDate, sInterfaceKey, bUpdateSuccessful, iRecordNum
              , sProcessFileName);
          return bUpdateSuccessful;
        }
        armStgStoreData = arrayArmStgStoreData[iCtr];
        // Store ID = <CompanyCode> * <StoreID>
        sStoreID = armStgStoreData.getCompanyCode() == null ? armStgStoreData.getShopCode()
            : armStgStoreData.getCompanyCode() + armStgStoreData.getShopCode();
        cmsStore = (CMSStore)storeDAO.selectById(sStoreID);
        if (lTmp == INSERT_UPDATE_RECORD && cmsStore == null) {
          lTmp = INSERT_RECORD;
        } else if (lTmp == INSERT_UPDATE_RECORD && cmsStore != null) {
          lTmp = UPDATE_RECORD;
        }
        if (lTmp == INSERT_RECORD) { // INSERT
          sLogKey = "Transaction Type : Insert";
          if (cmsStore != null) {
            String errMsg = "Store " + sStoreID + " already exist";
            logStoreStagingTable(errMsg);
            continue;
          }
          cmsStore = new CMSStore(sStoreID);
        } else if (lTmp == UPDATE_RECORD) { // UPDATE
          sLogKey = "Transaction Type : Update";
          if (cmsStore == null) {
            String errMsg = "Store doesnt exist " + sStoreID;
            logStoreStagingTable(errMsg);
            continue;
          }
        }
        sLogKey = "Storing Data";
        // Add data to Store
        cmsStore.doSetAddress(armStgStoreData.getAddressLine1());
        cmsStore.doSetAddressLine2(armStgStoreData.getAddressLine2());
        cmsStore.doSetCity(armStgStoreData.getCity());
        cmsStore.doSetState(armStgStoreData.getState());
        //Company
        cmsStore.doSetCompanyId(sStoreID);
        cmsStore.doSetCompanyCode(armStgStoreData.getCompanyCode());
        cmsStore.doSetCompanyDescription(armStgStoreData.getCompanyDescription());
        cmsStore.doSetCompanyNameForFranking(armStgStoreData.getFrankingCompanyName());
        cmsStore.doSetCompanyAccountNumberForFranking(armStgStoreData.getFrankingCompanyAcNum());
        cmsStore.doSetCompAddressLine1(armStgStoreData.getCompAddressLine1());
        cmsStore.doSetCompAddressLine2(armStgStoreData.getCompAddressLine2());
        cmsStore.doSetCompCity(armStgStoreData.getCompCity());
        cmsStore.doSetCompState(armStgStoreData.getCompState());
        cmsStore.doSetCompPostalCode(armStgStoreData.getCompPostalCode());
        cmsStore.doSetCompCountry(armStgStoreData.getCompCountry());
        /**
         * Added Company phone # details
         */
        if (
            /*armStgStoreData.getCompOfficePhoneArea()!=null && */
            armStgStoreData.getCompOfficePhoneNumber() != null) {
          cmsStore.doSetAlternateTelephone(Telephone.getInstance(TelephoneType.STORE_ALT
              , armStgStoreData.getCompOfficePhoneArea().trim() + "-"
              + armStgStoreData.getCompOfficePhoneNumber().trim()));
        }
        cmsStore.doSetCountry(armStgStoreData.getCountry());
        cmsStore.doSetDefaultTaxRate(armStgStoreData.getTaxRate());
        cmsStore.doSetCurrencyType(CurrencyType.getCurrencyType(armStgStoreData.getCurrency()));
        cmsStore.doSetBrandID(armStgStoreData.getBrandID());
        cmsStore.doSetZipCode(armStgStoreData.getPostalCode());
        cmsStore.doSetFiscalCode(armStgStoreData.getFiscalCode());
        cmsStore.doSetShopDescription(armStgStoreData.getShopDescription());
        cmsStore.doSetPreferredISOCountry(armStgStoreData.getCountryCode());
        cmsStore.doSetPreferredISOLanguage(armStgStoreData.getLanguageCode());
        // GeoCode
        //cmsStore.doSetGeoCode(armStgStoreData.getShopCode());
		cmsStore.doSetGeoCode(sStoreID);

        if (armStgStoreData.getOfficePhoneNumber() != null
            /*&& armStgStoreData.getOfficePhoneArea()!=null*/
            ) {
		    //Sergio
		   cmsStore.doSetTelephone(
		   Telephone.getInstance(TelephoneType.STORE,"").newTelephoneNumber(armStgStoreData.getOfficePhoneArea().trim() + "-"
		   + armStgStoreData.getOfficePhoneNumber().trim())
		   );

           /*cmsStore.doSetTelephone(Telephone.getInstance(TelephoneType.STORE
              , armStgStoreData.getOfficePhoneArea().trim() + "-"
              + armStgStoreData.getOfficePhoneNumber().trim()));*/
        }
        cmsStore.doSetGroupIdentifier("ARM." + armStgStoreData.getCountry() + "."
            + armStgStoreData.getState() + "." + armStgStoreData.getCity());
        if (lTmp == INSERT_RECORD) { // Insert New Record
          sLogKey = "Insert Parameteric Stmts";
          storeParametricStatements = ((StoreOracleDAO)storeDAO).getInsertSQL(cmsStore);
        } else if (lTmp == UPDATE_RECORD) { // Update Existing Record
          sLogKey = "Update Parameteric Stmts";
          storeParametricStatements = ((StoreOracleDAO)storeDAO).getUpdateSQL(cmsStore);
        }
        armStgStoreData.setStgStatus(new Long(0));
        armStgStoreData.setStgErrorMessage("");
        rmsStoreParametricStatements = ((ArmStgStoreOracleDAO)armStgStoreDAO).getUpdateSql(
            armStgStoreData);
        iLength = storeParametricStatements.length + rmsStoreParametricStatements.length;
        // Create Parametric Stmts.
        allStatements = new ParametricStatement[iLength];
        for (iStoreCtr = 0; iStoreCtr < storeParametricStatements.length; iStoreCtr++) {
          allStatements[iStoreCtr] = storeParametricStatements[iStoreCtr];
        }
        for (iStoreCtr = storeParametricStatements.length, iRMSCtr = 0;
            iStoreCtr < allStatements.length; iStoreCtr++, iRMSCtr++) {
          allStatements[iStoreCtr] = rmsStoreParametricStatements[iRMSCtr];
        }
        sLogKey = "Executing Parameteric Stmts";
        // Execute all statments.
        ((StoreOracleDAO)storeDAO).executeCaseSensitive(allStatements);
        iRecordNum++;
        // Write to log file.
        loggingFileServices.logMsg(this.getClass().getName() + "-- Upload Successful");
        loggingFileServices.recordMsg();
      } catch (Exception ex) {
        bUpdateSuccessful = false;
        loggingFileServices.logMsg(this.getClass().getName(), sLogKey, "Exception", "See Exception"
            , LoggingServices.MAJOR, ex);
        loggingFileServices.recordMsg();
        logStoreStagingTable(ex.getMessage());
        // Code to Add error in table ArmStgStore
        //                armStgStoreData.setStgErrorMessage(ex.getMessage());
        //                armStgStoreData.setStgStatus(new Long(1));
        //                ParametricStatement[] rmsStoreErrorUpdate = ((ArmStgStoreOracleDAO)armStgStoreDAO).getUpdateSql(armStgStoreData);
        //                try
        //                {
        //                    ((ArmStgStoreOracleDAO)armStgStoreDAO).execute(rmsStoreErrorUpdate);
        //                }
        //                catch (Exception eex)
        //                {
        //                    bUpdateSuccessful = false;
        //                    loggingFileServices.logMsg(this.getClass().getName(), sLogKey, "Exception", "See Exception", LoggingServices.MAJOR, ex);
        //                    loggingFileServices.recordMsg();
        //                }
      }
    } // End For loop
    // Incorporate logging into ARM_PROCESS_LOG table.
    updateProcessLog(startDate, sInterfaceKey, bUpdateSuccessful, iRecordNum, sProcessFileName);
    return bUpdateSuccessful;
  } // End method

  /**
   *
   * @param startDate Date
   * @param sInterfaceKey String
   * @param isSuccessful boolean
   * @param iRecordNum int
   * @param sProcessFileName String
   */
  private void updateProcessLog(Date startDate, String sInterfaceKey, boolean isSuccessful
      , int iRecordNum, String sProcessFileName) {
    try {
      List params = new ArrayList();
      params.add(sInterfaceKey);
      params.add(startDate);
      params.add(new Date());
      if (isSuccessful) {
        params.add(UPLOAD_SUCCESSFUL);
      } else {
        params.add(UPLOAD_FAILED);
      }
      params.add(new Integer(iRecordNum));
      params.add(sProcessFileName);
      ArmProcessLogOracleBean armProcessLogOracleBean = new ArmProcessLogOracleBean();
      ((ArmStgStoreOracleDAO)armStgStoreDAO).execute(new ParametricStatement(
          armProcessLogOracleBean.getInsertSql(), params));
    } catch (Exception exc) {
      loggingFileServices.logMsg(this.getClass().getName(), sLogKey, "Exception", "See Exception"
          , LoggingServices.MAJOR, exc);
      loggingFileServices.recordMsg();
    }
  }

  /**
   *
   * @param errMessage String
   */
  private void logStoreStagingTable(String errMessage) {
    // Code to Add error in database
    if (errMessage == null)
      errMessage = "";
    armStgStoreData.setStgErrorMessage(errMessage);
    armStgStoreData.setStgStatus(new Long(1));
    ParametricStatement[] rmsStoreErrorUpdate = ((ArmStgStoreOracleDAO)armStgStoreDAO).getUpdateSql(
        armStgStoreData);
    try {
      ((ArmStgStoreOracleDAO)armStgStoreDAO).execute(rmsStoreErrorUpdate);
    } catch (Exception ex) {
      loggingFileServices.logMsg(this.getClass().getName(), sLogKey, "Exception", "See Exception"
          , LoggingServices.MAJOR, ex);
      loggingFileServices.recordMsg();
      ex.printStackTrace();
    }
  }

  /**
   * Runs the application
   * @param args[0] StartDate
   * @param args[1] ProcessFileName
   * @param args[2] InterfaceKey
   */
  public static void main(String[] args) {
    try {
      //Set Logging file services.
      loggingFileServices = (LoggingFileServices)LoggingServices.getCurrent();
      loggingFileServices.setLogFile(LOG_FILE);
      // Manpreet S Bawa (03/02/05).
      // Get arguements for the logging.
      if (args.length < 1) {
        throw new Exception("Start Date(yyyyMMddhh:mm:ss), InterFace Key, Process File Name needed");
      } else if (args.length < 2) {
        throw new Exception("InterFace Key, Process File Name needed");
      } else if (args.length < 3) {
        throw new Exception("Process File needed");
      }
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhh:mm:ss");
      Date dStartDate = new Date();
      try {
        dStartDate = sdf.parse(args[0]);
      } catch (Exception pe) {
        if (loggingFileServices != null) {
          loggingFileServices.logMsg("ArmStgStoreUpdate", sLogKey, "Exception", "See Exception"
              , LoggingServices.MAJOR, pe);
          loggingFileServices.recordMsg();
        }
        System.exit(1);
      }
      ArmStgStoreUpdate armstgstore = new ArmStgStoreUpdate();
      //armstgstore.importData();
      boolean isSuccess = armstgstore.importData(dStartDate, args[1], args[2]);
      if (isSuccess) {
        System.exit(0);
      } else {
        System.exit(1);
      }
    } catch (Exception e) {
      if (loggingFileServices != null) {
        loggingFileServices.logMsg("ArmStgStoreUpdate", sLogKey, "Exception", "See Exception"
            , LoggingServices.MAJOR, e);
        loggingFileServices.recordMsg();
        System.exit(1);
      }
      System.exit(1);
    }
  }
}
