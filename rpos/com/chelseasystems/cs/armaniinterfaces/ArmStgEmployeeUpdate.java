/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.armaniinterfaces;

/* History:-
 +--------+------------+-----------+------------------+------------------------------+
 | Ver#   |    Date    |   By      |        Defect #  |              Description     |
 +--------+------------+-----------+------------------+------------------------------+
 | 14     | 05-12-2005 | Rajesh    | N/A              | Set job code                 |
 +--------+------------+-----------+------------------+------------------------------+
 | 4      | 03-03-2005 | Khyati    | SKipping on error| Added new Method for logging |
 |        |            |           |                  | errors logEmployeeStagingTabl|                    |
 +--------+------------+-----------+------------------+------------------------------+
 | 3      | 03-02-2005 | Manpreet  | N/A              |Modified for TransactionType=0
 |
 +--------+------------+-----------+------------------+------------------------------+
 | 2      | 03-02-2005 | Manpreet  | Not Storing      | Commented adding Employee as |
 |        |            |           | DateOf Birth     | customer, added Logging.     |
 +--------+------------+-----------+------------------+------------------------------+
 | 1      | 01-28-2005 | Manpreet  | N/A              | Original Version per spec    |
 +--------+------------+-----------+------------------+------------------------------+
 */
import com.chelseasystems.cs.armaniinterfaces.ArmStgEmployeeData;
import com.chelseasystems.cs.dataaccess.EmployeeDAO;
import com.chelseasystems.cs.dataaccess.CustomerDAO;
import com.chelseasystems.cs.dataaccess.ArmStgEmployeeDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.EmployeeOracleDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.CustomerOracleDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.ArmStgEmployeeOracleDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.ArtsConstants;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cr.employee.*;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cr.user.UserAccessRole;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmProcessLogOracleBean;
import java.sql.SQLException;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.telephone.Telephone;
import com.chelseasystems.cr.telephone.TelephoneType;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Title: ArmStgEmployee.java</p>
 *
 * <p>Description: Transforms Armani Staging Employee values to RPOS</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: SkillnetInc </p>
 *
 * @author Manpreet S Bawa
 * @version 1.0
 *
 */
public class ArmStgEmployeeUpdate implements ArtsConstants {
  /**
   * Upload to base tables Failed
   */
  private final String UPLOAD_FAILED = "1";
  /**
   * Upload to base tables successful
   */
  private final String UPLOAD_SUCCESSFUL = "0";
  /**
   * Armani Staging Employee DAO reference.
   */
  private final String ARM_STG_EMP_DAO = "ARMSTGEMPLOYEE_DAO";
  /**
   * Employee DAO
   */
  private final String EMP_DAO = "EMPLOYEE_DAO";
  /**
   * Configuration Manager
   */
  private ConfigMgr jdbcCfg;
  /**
   * ArmaniStagingEmployeeDAO object
   */
  private ArmStgEmployeeDAO armStgEmpDAO;
  /**
   * EmployeeDAO object
   */
  private EmployeeDAO empDAO;
  /**
   * CustomerDAO object
   */
  private CustomerDAO customerDAO;
  /**
   * ArmaniStagingEmployeeData object array
   */
  private ArmStgEmployeeData[] arrayArmStgEmpData;
  /**
   * UpdateLog Key
   */
  private static String sUpdateLogKey = "ARMS_STG_EMPLOYEE_UPDATE";
  /**
   * Employee Status = 0
   */
  private final Long EMP_ACTIVE = new Long(0);
  /**
   * Employee Status  =1
   */
  private final Long EMP_TERMINATED = new Long(1);
  /**
   * Employee Status =2
   */
  private final Long EMP_ON_CALL = new Long(2);
  /**
   * Transaction Type=0
   */
  private final Long INSERT_UPDATE_DATA = new Long(0);
  /**
   * Transaction Type = 1
   */
  private final Long INSERT_DATA = new Long(1);
  /**
   * Transaction Type=2
   */
  private final Long UPDATE_DATA = new Long(2);
  /**
   * Maps RoleIds to Roles from Staging Tables
   */
  private final Hashtable ROLE_MAPPINGS;
  /**
   * Maps ROleLabels to Roles from Staging Tables.
   */
  private final Hashtable ROLE_LABELS;
  private final Hashtable ROLE_JOB_CODE_MAPPINGS;
  /**
   * Logging Services
   */
  private static LoggingFileServices loggingFileServices;
  /**
   * Logging file name and location
   */
  private static final String LOG_FILE = "Employee/ArmStgEmployeeUpld.log";
  ArmStgEmployeeData armStgEmpData = null;
  private final String UNDECLARED = "0000";

  /**
   * Default Constructor
   */
  public ArmStgEmployeeUpdate() {
    ROLE_MAPPINGS = new Hashtable();
    ROLE_MAPPINGS.put("M", new Long(1));
    ROLE_MAPPINGS.put("S", new Long(2));
    ROLE_MAPPINGS.put("C", new Long(8));
    ROLE_MAPPINGS.put("B", new Long(10));
    ROLE_MAPPINGS.put("T", new Long(16));
    ROLE_MAPPINGS.put("N", new Long(4)); // Non Store Associate
    ROLE_MAPPINGS.put("L", new Long(4)); // Non Store Associate
    ROLE_MAPPINGS.put("F", new Long(4)); // Non Store Associate
    ROLE_LABELS = new Hashtable();
    ROLE_LABELS.put("M", "MANAGER");
    ROLE_LABELS.put("S", "STORE_ASSOCIATE");
    ROLE_LABELS.put("C", "CASHIER");
    ROLE_LABELS.put("B", "BOTH");
    ROLE_LABELS.put("T", "TECHNICAL_SUPPORT");
    ROLE_LABELS.put("N", "NON_STORE_ASSOCIATE");
    ROLE_LABELS.put("L", "TAILOR");
    ROLE_LABELS.put("F", "FITTER");
    ROLE_JOB_CODE_MAPPINGS = new Hashtable();
    ROLE_JOB_CODE_MAPPINGS.put("M", new JobCode(JC_MANAGER_ID, JC_MANAGER_DE));
    ROLE_JOB_CODE_MAPPINGS.put("S", new JobCode(JC_STORE_ASSOCIATE_ID, JC_STORE_ASSOCIATE_DE));
    ROLE_JOB_CODE_MAPPINGS.put("C", new JobCode(JC_CASHIER_ID, JC_CASHIER_DE));
    ROLE_JOB_CODE_MAPPINGS.put("B", new JobCode(JC_MANAGER_ID, JC_MANAGER_DE));
    ROLE_JOB_CODE_MAPPINGS.put("T", new JobCode(JC_TECHNICAL_SUPPORT_ID, JC_TECHNICAL_SUPPORT_DE));
    ROLE_JOB_CODE_MAPPINGS.put("N"
        , new JobCode(JC_NON_STORE_ASSOCIATE_ID, JC_NON_STORE_ASSOCIATE_DE));
    ROLE_JOB_CODE_MAPPINGS.put("L", new JobCode(JC_TAILOR_ID, JC_TAILOR_DE));
    ROLE_JOB_CODE_MAPPINGS.put("F", new JobCode(JC_FITTER_ID, JC_FITTER_DE));
    jdbcCfg = new ConfigMgr("jdbc.cfg");
    armStgEmpDAO = (ArmStgEmployeeDAO)jdbcCfg.getObject(ARM_STG_EMP_DAO);
    empDAO = (EmployeeOracleDAO)jdbcCfg.getObject(EMP_DAO);
    customerDAO = (CustomerOracleDAO)jdbcCfg.getObject("CUSTOMER_DAO");
  }

  /**
   * Imports data from staging table and puts into base tables.
   * @param startDate Date
   * @param sProcessFileName String
   * @param sInterfaceKey String
   * @return boolean
   */
  // Method signature changed -- Manpreet S Bawa (03/02/05)
  // Added attributes to be logged in ARM_PROCESS_LOG table.
  // public void importData()
  public boolean importData(Date startDate, String sProcessFileName, String sInterfaceKey) {
    int iCtr; // Counter Variable
    int iEmpCtr; // Counter Variable
    //    int iCustCtr;   // Counter Variable
    int iArmStgCtr;
    int iRecordNum = 0;
    boolean bInsertData = false;
    boolean bUpdateSuccessful = false;
    Long lTmp = null; // Temp. variable
    //        ArmStgEmployeeData armStgEmpData = null;  // Employee data
    CMSEmployee cmsEmployee = null; // Store
    //      CMSCustomer cmsCustomer=null;
    String sChelseaID = null;
    String sExternalID = null;
    ParametricStatement[] empParamStmts; // Store
    //      ParametricStatement[] custParamStmts;   // Customer
    ParametricStatement[] armEmpUpdateStmts; //
    ArrayList allParamStmts = null;
    try {
      lTmp = new Long("0");
      armStgEmpData = new ArmStgEmployeeData();
      arrayArmStgEmpData = armStgEmpDAO.getNewArmStgEmpData();
      if (arrayArmStgEmpData == null || arrayArmStgEmpData.length < 1) {
        bUpdateSuccessful = true;
        updateProcessLog(startDate, sInterfaceKey, bUpdateSuccessful, iRecordNum, sProcessFileName);
        return bUpdateSuccessful;
      }
      for (iCtr = 0; iCtr < arrayArmStgEmpData.length; iCtr++) {
        lTmp = arrayArmStgEmpData[iCtr].getTranType();
        // IF Transaction type is not INSERT_DATA or UPDATE_DATA then do nothing.
        if (!(lTmp.equals(INSERT_DATA) || lTmp.equals(UPDATE_DATA)
            || lTmp.equals(INSERT_UPDATE_DATA))) {
          continue;
        }
        armStgEmpData = arrayArmStgEmpData[iCtr];
        sExternalID = armStgEmpData.getEmployeeID();
        cmsEmployee = (CMSEmployee)empDAO.selectByExternalId(sExternalID);
        //Check if its Insert or  Update operation for Employee
        if (lTmp.equals(INSERT_UPDATE_DATA) && cmsEmployee == null) {
          lTmp = INSERT_DATA;
        } else if (lTmp.equals(INSERT_UPDATE_DATA) && cmsEmployee != null) {
          lTmp = UPDATE_DATA;
        }
        if (lTmp.equals(INSERT_DATA)) { // INSERT
          //cmsEmployee.setJobCode(new JobCode(UNDECLARED, "UNDECLARE"));
          //If Employee already exists.
          if (cmsEmployee != null) {
            String msg = "Error : Employee No. " + sExternalID + " already exists";
            logEmployeeStagingTable(msg);
            continue;
            //                    loggingFileServices.logMsg(this.getClass().getName() + "-- " + msg);
            //                    loggingFileServices.recordMsg();
          }
          // Employee Id
          bInsertData = true;
          sChelseaID = empDAO.getNextChelseaId();
          cmsEmployee = new CMSEmployee(sChelseaID);
          cmsEmployee.setNew();
          // cmsEmployee.setJobCode(new JobCode(UNDECLARED, "UNDECLARE"));
          //                    cmsEmployee.doSetPassword("PASS");
          setEmployee(cmsEmployee, armStgEmpData);
          // Commented by Manpreet S Bawa (03/02/05)
          // Don't create customer from employee
          //cmsCustomer= createCustomerFromEmployee(cmsEmployee);
          //custParamStmts = ((CustomerOracleDAO)customerDAO).getInsertSQL(cmsCustomer);
        } else if (lTmp.equals(UPDATE_DATA)) { // UPDATE
          // If Employee doesnt exist
          if (cmsEmployee == null) {
            String msg = "Error : Can't Update When Employee No. " + sExternalID + " doesnt Exist ";
            logEmployeeStagingTable(msg);
            continue;
          }
          setEmployee(cmsEmployee, armStgEmpData); /*
                                         Commented by Manpreet S Bawa (03/02/05)
                                         Don't create customer from employee
          cmsCustomer = getCustomer(cmsEmployee);  // Lookup customer record for this employee
                                         if(cmsCustomer == null)
                                         { // Create New customer
                                         cmsCustomer = createCustomerFromEmployee(cmsEmployee);
                    custParamStmts = ((CustomerOracleDAO)customerDAO).getInsertSQL(cmsCustomer);
                                         }
                                         else
                                         { // Update existing Customer
                                         cmsCustomer = updateCustomer(cmsEmployee, cmsCustomer);
                    custParamStmts = ((CustomerOracleDAO)customerDAO).getUpdateSQL(cmsCustomer);
                                         }*/

        }
        // ShortName & External ID
        //Khyati: changed setShortName to doSetShortName and doSetExternalID
        cmsEmployee.doSetShortName(sExternalID);
        cmsEmployee.doSetExternalID(sExternalID);
        if (bInsertData) {
          empParamStmts = ((EmployeeOracleDAO)empDAO).getInsertSQL(cmsEmployee);
        } else {
          empParamStmts = ((EmployeeOracleDAO)empDAO).getUpdateSQL(cmsEmployee);
        }
        armStgEmpData.setStgStatus(0);
        armStgEmpData.setStgErrorMessage("");
        armEmpUpdateStmts = ((ArmStgEmployeeOracleDAO)armStgEmpDAO).getUpdateSql(armStgEmpData);
        // Put all the data into one array
        allParamStmts = new ArrayList();
        // Employee
        for (iEmpCtr = 0; iEmpCtr < empParamStmts.length; iEmpCtr++) {
          allParamStmts.add(empParamStmts[iEmpCtr]);
        }
        /*
         // Commented by Manpreet S Bawa (03/02/05)
         // Don't create customer from employee
         // Customer
         for(iCustCtr=0; iCustCtr < custParamStmts.length; iCustCtr++)
         {
         allParamStmts.add(custParamStmts[iCustCtr]);
         }*/
        // Armani Staging Data
        for (iArmStgCtr = 0; iArmStgCtr < armEmpUpdateStmts.length; iArmStgCtr++) {
          allParamStmts.add(((ArmStgEmployeeOracleDAO)armStgEmpDAO).toUpperCase(armEmpUpdateStmts[
              iArmStgCtr]));
        }
        // Execute all the statements now.
        ((EmployeeOracleDAO)empDAO).executeCaseSensitive((ParametricStatement[])allParamStmts.
            toArray(new ParametricStatement[0]));
        iRecordNum++;
      } //End For Loop
      // Write to log file.
      loggingFileServices.logMsg(this.getClass().getName() + "-- Upload bUpdateSuccessfulful");
      loggingFileServices.recordMsg();
    } catch (BusinessRuleException bre) { // End Try
      bUpdateSuccessful = false;
      bre.printStackTrace();
      loggingFileServices.logMsg("Exception " + bre);
      loggingFileServices.recordMsg();
    } catch (Exception ex) {
      bUpdateSuccessful = false;
      loggingFileServices.logMsg(this.getClass().getName(), sUpdateLogKey, "Exception"
          , "See Exception", LoggingServices.MAJOR, ex);
      loggingFileServices.recordMsg();
      ex.printStackTrace();
      logEmployeeStagingTable(ex.getMessage());
    }
    // Incorporate logging in ARM_PROCESS_LOG table.
    // Manpreet S Bawa 03/02/05
    updateProcessLog(startDate, sInterfaceKey, bUpdateSuccessful, iRecordNum, sProcessFileName);
    return bUpdateSuccessful;
  }

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
      ((ArmStgEmployeeOracleDAO)armStgEmpDAO).execute(new ParametricStatement(
          armProcessLogOracleBean.getInsertSql(), params));
    } catch (Exception exc) {
      loggingFileServices.logMsg(this.getClass().getName(), sUpdateLogKey, "Exception"
          , "See Exception", LoggingServices.MAJOR, exc);
      loggingFileServices.recordMsg();
      exc.printStackTrace();
    }
  }

  /**
   *
   * @param errMessage String
   */
  private void logEmployeeStagingTable(String errMessage) {
    // Code to Add error in database
    armStgEmpData.setStgErrorMessage(errMessage);
    armStgEmpData.setStgStatus(new Long(1));
    ParametricStatement[] armEmployeeErrorUpdate = ((ArmStgEmployeeOracleDAO)armStgEmpDAO).
        getUpdateSql(armStgEmpData);
    try {
      ((ArmStgEmployeeOracleDAO)armStgEmpDAO).execute(armEmployeeErrorUpdate);
    } catch (Exception eex) {
      loggingFileServices.logMsg(this.getClass().getName(), sUpdateLogKey, "Exception"
          , "See Exception", LoggingServices.MAJOR, eex);
      loggingFileServices.recordMsg();
      eex.printStackTrace();
    }
  }

  /**
   * Updates Customer for the respective Employee
   * @param cmsEmployee Employee
   * @param cmsCustomer Customer
   * @return Updated Customer
   */
  private CMSCustomer updateCustomer(CMSEmployee cmsEmployee, CMSCustomer cmsCustomer) {
    Telephone ph = cmsEmployee.getTelephone().newTelephoneType(TelephoneType.CUSTOMER_HOME);
    try {
      cmsCustomer.setTelephone(ph);
      cmsCustomer.setFirstName(cmsEmployee.getFirstName().toUpperCase());
      cmsCustomer.setLastName(cmsEmployee.getLastName().toUpperCase());
      cmsCustomer.setAddress(cmsEmployee.getAddress());
      cmsCustomer.setCity(cmsEmployee.getCity());
      cmsCustomer.setState(cmsEmployee.getState());
      cmsCustomer.setZipCode(cmsEmployee.getZipCode());
//      int year = cmsEmployee.getBirthDate().getYear();
//      int month = cmsEmployee.getBirthDate().getMonth();
//      int date = cmsEmployee.getBirthDate().getDate();
      cmsCustomer.setDateOfBirth(cmsEmployee.getBirthDate());
//      cmsCustomer.setBirthDate(month + "/" + date + "/" + year);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return cmsCustomer;
  }

  /**
   * LookUp Customer for given Employee
   * @param cmsEmployee Employee
   * @return Customer
   */
  private CMSCustomer getCustomer(CMSEmployee cmsEmployee) {
    String sCustomerID = null;
    CMSCustomer cmsCust = null;
    try {
      cmsEmployee.getCustomerID();
      //System.out.println("cust: "+cmsEmployee.getCustomerID());
      if (cmsEmployee.getCustomerID() == null) {
        return null;
      }
      cmsCust = (CMSCustomer)customerDAO.selectById(sCustomerID);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return cmsCust;
  }

  /**
   * Sets Employee attributes.
   * @param cmsEmployee Employee
   * @param armStgEmpData Employee Data
   * @return Updated Employee
   * @throws Exception
   */
  private CMSEmployee setEmployee(CMSEmployee cmsEmployee, ArmStgEmployeeData armStgEmpData)
      throws Exception {
    String sTmp, sTmp2 = ""; // Temp. Variable
    Date dtTmp = null;
    Long lTmp; // Temp. variable
    //FirstName
    sTmp = armStgEmpData.getFirstName();
    if (validate(sTmp)) {
      cmsEmployee.setFirstName(sTmp.toUpperCase());
    }
    //Lastname
    sTmp = armStgEmpData.getLastName();
    if (validate(sTmp)) {
      cmsEmployee.setLastName(sTmp.toUpperCase());
    }
    //AddressLine1
    sTmp = armStgEmpData.getAddressLine1();
    if (validate(sTmp)) {
      cmsEmployee.setAddress(sTmp);
    }
    //AddressLine2
    sTmp = armStgEmpData.getAddressLine2();
    if (validate(sTmp)) {
      cmsEmployee.setAddressLine2(sTmp);
    }
    //City
    sTmp = armStgEmpData.getCity();
    if (validate(sTmp)) {
      cmsEmployee.setCity(sTmp);
    }
    //State
    sTmp = armStgEmpData.getState();
    if (validate(sTmp)) {
      cmsEmployee.setState(sTmp);
    }
    //ZipCode
    sTmp = armStgEmpData.getPostalCode();
    if (validate(sTmp)) {
      cmsEmployee.setZipCode(sTmp);
    }
    //Country
    sTmp = armStgEmpData.getCountry();
    if (validate(sTmp)) {
      cmsEmployee.setCountry(sTmp);
    }
    // Phone Number
    sTmp = armStgEmpData.getHomePhoneNumber();
    sTmp2 = armStgEmpData.getHomePhoneArea();
    if (validate(sTmp) && validate(sTmp2)) {
      sTmp = sTmp2 + "-" + sTmp;
      cmsEmployee.doSetTelephone(Telephone.getInstance(TelephoneType.EMPLOYEE, sTmp));
    }
    // Email
    sTmp = armStgEmpData.getEmail();
    if (validate(sTmp)) {
      cmsEmployee.setEmail(sTmp);
    }
    //Date of Birth
    dtTmp = armStgEmpData.getDateOfBirth();
    if (dtTmp != null) {
      //  String birthDate = MMDDYYYY.format(dtTmp);
      cmsEmployee.setBirthDate(dtTmp);
    }
    // HireDate
    dtTmp = armStgEmpData.getHireDate();
    if (dtTmp != null) {
      cmsEmployee.setHireDate(dtTmp);
    }
    // TerminationDate and Employee Status
    lTmp = armStgEmpData.getEmployeeStatus();
    if (lTmp.equals(EMP_ACTIVE)) {
      cmsEmployee.setEmploymentStatusActive();
    } else if (lTmp.equals(EMP_TERMINATED)) {
      cmsEmployee.setEmploymentStatusTerminated();
      cmsEmployee.setTerminationDate(armStgEmpData.getTermDate());
    } else if (lTmp.equals(EMP_ON_CALL)) {
      cmsEmployee.setEmploymentStatusOnCall();
    } else { // Inactive Employee
      cmsEmployee.setEmploymentStatusOnLeave();
    }
    // HomeStoreID
    cmsEmployee.setHomeStore(new CMSStore("" + armStgEmpData.getHomeStoreId()));
    // Assign User Role
    UserAccessRole usrRole = createUserRole(armStgEmpData);
    if (usrRole != null) {
      cmsEmployee.setAccessRole(usrRole);
    }
    // set job code
    String role = armStgEmpData.getRole();
    if (role != null) {
      role = role.trim().toUpperCase();
      if (ROLE_JOB_CODE_MAPPINGS.containsKey(role)) {
        cmsEmployee.setJobCode((JobCode)ROLE_JOB_CODE_MAPPINGS.get(role));
      }
    }
    return cmsEmployee;
  }

  /**
   * Creates Customer from present Employee
   * @param employee Employee
   * @return Customer
   */
  private CMSCustomer createCustomerFromEmployee(Employee employee) {
    CMSCustomer newCustomer = null;
    Telephone ph = employee.getTelephone().newTelephoneType(TelephoneType.CUSTOMER_HOME);
    try {
      newCustomer = new CMSCustomer(customerDAO.getNextChelseaId());
      newCustomer.setNew();
      newCustomer.setTelephone(ph);
      newCustomer.setFirstName(employee.getFirstName().toUpperCase());
      newCustomer.setLastName(employee.getLastName().toUpperCase());
      newCustomer.setAddress(employee.getAddress());
      newCustomer.setCity(employee.getCity());
      newCustomer.setState(employee.getState());
      newCustomer.setZipCode(employee.getZipCode());
//      int year = employee.getBirthDate().getYear();
//      int month = employee.getBirthDate().getMonth();
//      int date = employee.getBirthDate().getDate();
//      newCustomer.setBirthDate(month + "/" + date + "/" + year);
      newCustomer.setDateOfBirth(employee.getBirthDate());
      ((CMSEmployee)employee).setCustomerID(newCustomer.getId());
    } catch (BusinessRuleException bre) {
      bre.printStackTrace();
    } catch (SQLException sqe) {
      sqe.printStackTrace();
    }
    return newCustomer;
  }

  /**
   * Creates user role for employee based on
   * access level.
   * @param armStgEmpData Employee Data
   * @return UserAccessRole
   */
  private UserAccessRole createUserRole(ArmStgEmployeeData armStgEmpData) {
    String sRole;
    String sRoleLabel;
    Long lRoleID;
    sRole = armStgEmpData.getRole();
    if (validate(sRole)) {
      sRole = sRole.trim().toUpperCase();
      if (!ROLE_MAPPINGS.containsKey(sRole)) {
        return null; // If role not found in mapping
      }
      lRoleID = (Long)ROLE_MAPPINGS.get(sRole); // Find roleId
      sRoleLabel = (String)ROLE_LABELS.get(sRole); // Find roleLabel
      return new UserAccessRole(lRoleID.longValue(), sRoleLabel);
    }
    return null;
  }

  /**
   * Validates the object so that object
   * isnt null or blank.
   * @param sObject String to be validated.
   * @return boolean
   */
  private boolean validate(String sObject) {
    if (sObject == null || sObject.trim().length() < 1) {
      return false;
    }
    return true;
  }

  /**
   * Runs the application
   * @param args[0] Starttime
   * @param args[1] ProcessFileName
   * @param args[2] InterfaceKey
   */
  public static void main(String[] args) {
    // Manpreet S Bawa (03/02/05).
    // Get arguements for the logging.
    //Thu 03/03/2005 21:17:02.72
    try {
      loggingFileServices = (LoggingFileServices)LoggingServices.getCurrent();
      loggingFileServices.setLogFile(LOG_FILE);
      if (args.length < 1) {
        throw new Exception("Start Date(yyyyMMddhh:mm:ss), InterFace Key, Process File Name needed");
      } else if (args.length < 2) {
        throw new Exception("InterFace Key, Process File Name needed");
      } else if (args.length < 3) {
        throw new Exception("Process File Name  needed");
      }
      String sDate = args[0];
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhh:mm:ss");
      Date dStartDate = new Date();
      dStartDate = sdf.parse(sDate);
      System.out.println(dStartDate);
      ArmStgEmployeeUpdate armStgEmployeeUpdate = new ArmStgEmployeeUpdate();
      boolean updateStatus = armStgEmployeeUpdate.importData(dStartDate, args[2], args[1]);
      if (updateStatus) {
        System.exit(0); //Successful
      } else {
        System.exit(1); //Failed
      }
    } catch (Exception e) {
      if (loggingFileServices != null) {
        loggingFileServices.logMsg("ArmStgEmployeeUpdate", sUpdateLogKey, "Exception"
            , "See Exception", LoggingServices.MAJOR, e);
        loggingFileServices.recordMsg();
      }
      System.exit(1);
    }
  }
}
