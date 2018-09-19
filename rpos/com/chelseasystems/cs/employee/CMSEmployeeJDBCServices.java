/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 02-14-2005 | Anand     | N/A       | Modified to add External ID                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 3    | 02-21-2005 | Vikram    | 104665    | New Employee ID is now set using sequence no.|
 |      |            |           |           | Added submitAndGetRemoteEmployee method      |
 --------------------------------------------------------------------------------------------
 */


package com.chelseasystems.cs.employee;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.employee.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cs.dataaccess.*;
import com.chelseasystems.cr.logging.*;
import java.util.*;
import com.chelseasystems.cr.user.*;


/**
 *
 * <p>Title: CMSEmployeeJDBCServices</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSEmployeeJDBCServices extends CMSEmployeeServices {
  private EmployeeDAO employeeDAO;
  private JobCodeDAO jobCodeDAO;
  private EmployeeAuthInfoDAO employeeAuthInfoDAO;

  /**
   * Default Constructor
   */
  public CMSEmployeeJDBCServices() {
    ConfigMgr configMgr = new ConfigMgr("jdbc.cfg");
    employeeDAO = (EmployeeDAO)configMgr.getObject("EMPLOYEE_DAO");
    jobCodeDAO = (JobCodeDAO)configMgr.getObject("JOBCODE_DAO");
    employeeAuthInfoDAO = (EmployeeAuthInfoDAO)configMgr.getObject("EMPLOYEEAUTHINFO_DAO");
  }

  /**
   * This method is used to search employee on the basis of employee id
   * @param employeeId String
   * @return Employee
   * @throws Exception
   */
  public Employee findById(String employeeId)
      throws java.lang.Exception {
    try {
      return employeeDAO.selectById(employeeId);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findById", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * This method is used to search employee on the basis of external HRMS Id
   * @param externalId String
   * @return CMSEmployee
   * @throws Exception
   */
  public CMSEmployee findByExternalId(String externalId)
      throws java.lang.Exception {
    try {
      return employeeDAO.selectByExternalId(externalId);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByExternalId"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * This method is used to search employee on the basis of ssn
   * @param ssn String
   * @return Employee
   * @throws Exception
   */
  public Employee findBySSN(String ssn)
      throws java.lang.Exception {
    try {
      Employee[] employees = employeeDAO.selectBySSN(ssn);
      if (employees == null || employees.length == 0)
        return null;
      return employees[0];
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findBySSN", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * This method is used to search employee on the basis of short name
   * @param shortName String
   * @return Employee
   * @throws Exception
   */
  public Employee findByShortName(String shortName)
      throws java.lang.Exception {
    try {
      Employee[] employees = employeeDAO.selectByShortName(shortName);
      if (employees == null || employees.length == 0)
        return null;
      return employees[0];
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByShortName", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * This method is used to search employee on the basis of last name
   * @param lastName String
   * @return Employee
   * @throws Exception
   */
  public CMSEmployee[] findByLastName(String lastName)
      throws java.lang.Exception {
    try {
    System.out.println("CMSEmployeeJDBCServices findByLastName "+ lastName);	
      Employee[] employees = employeeDAO.selectByLastName(lastName);
      
      if (employees == null || employees.length == 0){
    	  System.out.println("findByLastName: null or empty employee " + lastName+":"+employees);
    	  return null;
      }
      return (CMSEmployee[])employees;
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByLastName", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }
  

  /**
   * This method is used to search all employees of a store
   * @param store Store
   * @return Employee[]
   * @throws Exception
   */
  public Employee[] findByStore(Store store)
      throws java.lang.Exception {
    try {
      return employeeDAO.selectByHomeStoreId(store.getId());
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByStore", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * This method is used to search all commissioned employees of a store
   * @param store Store
   * @return Employee[]
   * @throws Exception
   */
  public Employee[] findCommissionedByStore(Store store)
      throws java.lang.Exception {
    try {
      return this.findByStore(store);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findCommissionedByStore"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * This method is used to search job code by job code id
   * @param jobCodeId String
   * @return JobCode
   * @throws Exception
   */
  public JobCode findJobCode(String jobCodeId)
      throws java.lang.Exception {
    try {
      return jobCodeDAO.selectById(jobCodeId);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findJobCode", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * This method is used to search all job codes
   * @return JobCode[]
   * @throws Exception
   */
  public JobCode[] findJobCodes()
      throws java.lang.Exception {
    try {
      return jobCodeDAO.selectAll();
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findJobCodes", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * This method is used to find auth of an employee on the basis of employee
   * id and store
   * @param employeeId String
   * @param store Store
   * @return UserAuthInfo
   * @throws Exception
   */
  public UserAuthInfo findAuthInfo(String employeeId, Store store)
      throws java.lang.Exception {
    try {
      return employeeAuthInfoDAO.selectByEmployeeId(employeeId);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findAuthInfo", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * This method is used to search auth info of an employee for a store
   * @param store Store
   * @return UserAuthInfo[]
   * @throws Exception
   */
  public UserAuthInfo[] findAuthInfo(Store store)
      throws java.lang.Exception {
    try {
      return employeeAuthInfoDAO.selectByStoreId(store.getId());
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByStore", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * This method is used to submit a new or updated employee
   * @param employee Employee
   * @return boolean
   * @throws Exception
   */
  public boolean submit(Employee employee)
      throws Exception {
    try {
      if (employee.isNew()) {
        /* Base Code: Not required as per ArmaniPOS_104665_TS_usermanagement_Rev0
         employee.doSetId(employee.getSSN());
         */
        employeeDAO.insert(employee);
      } else if (employee.isModified()) {
        employeeDAO.update(employee);
      }
      return true;
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "submit", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   *
   * @param employee Employee
   * @return boolean
   * @throws Exception
   */
  public boolean submitLocalEmployee(Employee employee)
      throws Exception {
    return false;
  }

  /**
   * This method is used to persists auth information of employee to data store
   * @param transactionEmployeeAuthInfo TransactionEmployeeAuthInfo
   * @return boolean
   * @throws Exception
   */
  public boolean submitAuthInfo(TransactionEmployeeAuthInfo transactionEmployeeAuthInfo)
      throws java.lang.Exception {
    UserAuthInfo employeeAuthInfo = transactionEmployeeAuthInfo.getEmployeeAuthInfo();
    String employeeId = employeeAuthInfo.getUserId();
    if (employeeAuthInfoDAO.selectByEmployeeId(employeeId) == null)
      employeeAuthInfoDAO.insert(employeeAuthInfo);
    else
      employeeAuthInfoDAO.update(employeeAuthInfo);
    return true;
  }

  /**
   * This method is used to Submit Employee to database and return it
   * @param employee Employee
   * @return Employee
   * @throws Exception
   * @author Vikram Mundhra
   * Created Date: 04/20/2005
   */
  public Employee submitAndGetRemoteEmployee(Employee employee)
      throws Exception {
    try {
      if (employee.isNew()) {
        employee.doSetId(employeeDAO.getNextChelseaId());
      }
      this.submit(employee);
      return employee;
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "submitAndGetRemoteEmployee"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }
}

