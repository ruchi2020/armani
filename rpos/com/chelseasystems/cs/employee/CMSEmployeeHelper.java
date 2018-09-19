/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
// Copyright 1999-2001, Chelsea Market Systems
//
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 02-14-2005 | Anand     | N/A       | Modified to add External ID                  |
 --------------------------------------------------------------------------------------------
 */


package com.chelseasystems.cs.employee;

import com.chelseasystems.cr.appmgr.IRepositoryManager;
import com.chelseasystems.cr.employee.*;
import java.lang.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.user.*;


/**
 *
 * <p>Title: CMSEmployeeHelper</p>
 *
 * <p>Description: This class have static convenience methods to manipulate
 * Client Services</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSEmployeeHelper {

  /**
   * This method is used to search an employee on the basis of employee Id
   * @param theAppMgr IRepositoryManager
   * @param employeeId String
   * @return CMSEmployee
   * @throws Exception
   */
  public static CMSEmployee findById(IRepositoryManager theAppMgr, String employeeId)
      throws Exception {
    CMSEmployeeClientServices cs = (CMSEmployeeClientServices)theAppMgr.getGlobalObject(
        "EMPLOYEE_SRVC");
    return cs.findById(employeeId);
  }

  /**
   * This method is used to search an employee on the basis of External HRMS Id
   * @param theAppMgr IRepositoryManager
   * @param externalId String
   * @return CMSEmployee
   * @throws Exception
   */
  public static CMSEmployee findByExternalId(IRepositoryManager theAppMgr, String externalId)
      throws Exception {
    CMSEmployeeClientServices cs = (CMSEmployeeClientServices)theAppMgr.getGlobalObject(
        "EMPLOYEE_SRVC");
    return cs.findByExternalId(externalId);
  }

  /**
   * This method is used to search an employee on the basis of  short name
   * @param theAppMgr IRepositoryManager
   * @param shortName String
   * @return CMSEmployee
   * @throws Exception
   */
  public static CMSEmployee findByShortName(IRepositoryManager theAppMgr, String shortName)
      throws Exception {
    CMSEmployeeClientServices cs = (CMSEmployeeClientServices)theAppMgr.getGlobalObject(
        "EMPLOYEE_SRVC");
    return cs.findByShortName(shortName);
  }

  /**
   * This method is used to search an employee on the basis of  last name
   * @param theAppMgr IRepositoryManager
   * @param lastName String
   * @return CMSEmployee
   * @throws Exception
   */
  public static CMSEmployee[] findByLastName(IRepositoryManager theAppMgr, String lastName)
      throws Exception {
    CMSEmployeeClientServices cs = (CMSEmployeeClientServices)theAppMgr.getGlobalObject(
        "EMPLOYEE_SRVC");
    System.out.println("CMSEmployeeHelper:findlastName: " + lastName);
    return cs.findByLastName(lastName);
  }
  

  /**
   * This method is used to search an employee on the basis of ssn
   * @param theAppMgr IRepositoryManager
   * @param ssn String
   * @return CMSEmployee
   * @throws Exception
   */
  public static CMSEmployee findBySSN(IRepositoryManager theAppMgr, String ssn)
      throws Exception {
    CMSEmployeeClientServices cs = (CMSEmployeeClientServices)theAppMgr.getGlobalObject(
        "EMPLOYEE_SRVC");
    return cs.findBySSN(ssn);
  }

  /**
   * This method is used to find all employees of a store
   * @param theAppMgr IRepositoryManager
   * @param store Store
   * @return CMSEmployee[]
   * @throws Exception
   */
  public static CMSEmployee[] findByStore(IRepositoryManager theAppMgr, Store store)
      throws Exception {
    CMSEmployeeClientServices cs = (CMSEmployeeClientServices)theAppMgr.getGlobalObject(
        "EMPLOYEE_SRVC");
    return cs.findByStore(store);
  }

  /**
   * This method is used to find all commissioned employee of a store
   * @param theAppMgr IRepositoryManager
   * @param store Store
   * @return CMSEmployee[]
   * @throws Exception
   */
  public static CMSEmployee[] findCommissionedByStore(IRepositoryManager theAppMgr, Store store)
      throws Exception {
    CMSEmployeeClientServices cs = (CMSEmployeeClientServices)theAppMgr.getGlobalObject(
        "EMPLOYEE_SRVC");
    return cs.findCommissionedByStore(store);
  }

  /**
   * This method is used to search all job codes
   * @param theAppMgr IRepositoryManager
   * @return JobCode[]
   * @throws Exception
   */
  public static JobCode[] findJobCodes(IRepositoryManager theAppMgr)
      throws Exception {
    CMSEmployeeClientServices cs = (CMSEmployeeClientServices)theAppMgr.getGlobalObject(
        "EMPLOYEE_SRVC");
    return cs.findJobCodes();
  }

  /**
   * This method is used to search a job code for an employee
   * @param theAppMgr IRepositoryManager
   * @param employeeId String
   * @return JobCode
   * @throws Exception
   */
  public static JobCode findJobCode(IRepositoryManager theAppMgr, String employeeId)
      throws Exception {
    CMSEmployeeClientServices cs = (CMSEmployeeClientServices)theAppMgr.getGlobalObject(
        "EMPLOYEE_SRVC");
    return cs.findJobCode(employeeId);
  }

  /**
   * This method is used to find auth of an employee on the basis of employee id
   * @param theAppMgr IRepositoryManager
   * @param employeeId String
   * @return UserAuthInfo
   * @throws Exception
   */
  public static UserAuthInfo findAuthInfo(IRepositoryManager theAppMgr, String employeeId)
      throws Exception {
    CMSEmployeeClientServices cs = (CMSEmployeeClientServices)theAppMgr.getGlobalObject(
        "EMPLOYEE_SRVC");
    Store store = (Store)theAppMgr.getGlobalObject("STORE");
    return cs.findAuthInfo(employeeId, store);
  }

  /**
   * This method is used to search auth info of an employee for a store
   * @param theAppMgr IRepositoryManager
   * @param store Store
   * @return UserAuthInfo[]
   * @throws Exception
   */
  public static UserAuthInfo[] findAuthInfo(IRepositoryManager theAppMgr, Store store)
      throws Exception {
    CMSEmployeeClientServices cs = (CMSEmployeeClientServices)theAppMgr.getGlobalObject(
        "EMPLOYEE_SRVC");
    return cs.findAuthInfo(store);
  }

  /**
   * This method is used to submit a new or updated employee
   * @param theAppMgr IRepositoryManager
   * @param employee Employee
   * @return boolean
   * @throws Exception
   */
  public static boolean submit(IRepositoryManager theAppMgr, Employee employee)
      throws Exception {
    CMSEmployeeClientServices cs = (CMSEmployeeClientServices)theAppMgr.getGlobalObject(
        "EMPLOYEE_SRVC");
    return cs.submit(employee);
  }

  /**
   * This method is used to persists auth information of employee to data store
   * @param theAppMgr IRepositoryManager
   * @param txEmployee TransactionEmployeeAuthInfo
   * @return boolean
   * @throws Exception
   */
  public static boolean submitAuthInfo(IRepositoryManager theAppMgr
      , TransactionEmployeeAuthInfo txEmployee)
      throws Exception {
    CMSEmployeeClientServices cs = (CMSEmployeeClientServices)theAppMgr.getGlobalObject(
        "EMPLOYEE_SRVC");
    return cs.submitAuthInfo(txEmployee);
  }
}

