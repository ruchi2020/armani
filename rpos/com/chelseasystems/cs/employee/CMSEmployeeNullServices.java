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
 | 3    | 02-21-2005 | Vikram    | 104665    | Added submitAndGetRemoteEmployee method      |
 --------------------------------------------------------------------------------------------
 */


package com.chelseasystems.cs.employee;

import com.chelseasystems.cr.employee.*;
import com.chelseasystems.cs.employee.*;
import com.chelseasystems.cr.store.*;
import java.util.*;
import com.chelseasystems.cr.user.*;


/**
 *
 * <p>Title: CMSEmployeeNullServices</p>
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
public class CMSEmployeeNullServices extends CMSEmployeeServices {

  /**
   * Default Constructor
   */
  public CMSEmployeeNullServices() {
  }

  /**
   * This method is used to search an employee on the basis of employee Id
   * @param employeeId String
   * @return Employee
   * @throws Exception
   */
  public Employee findById(String employeeId)
      throws java.lang.Exception {
    return null;
  }

  /**
   * This method is used to search an employee on the basis of external HRMS Id
   * @param externalId String
   * @return CMSEmployee
   * @throws Exception
   */
  public CMSEmployee findByExternalId(String externalId)
      throws java.lang.Exception {
    return null;
  }

  /**
   * This method is used to search an employee on the basis of ssn
   * @param ssn String
   * @return Employee
   * @throws Exception
   */
  public Employee findBySSN(String ssn)
      throws java.lang.Exception {
    return null;
  }

  /**
   * This method is used to search an employee on the basis of short name
   * @param shortName String
   * @return Employee
   * @throws Exception
   */
  public Employee findByShortName(String shortName)
      throws java.lang.Exception {
    return null;
  }

  /**
   * This method is used to search all employees of a store
   * @param store Store
   * @return Employee[]
   * @throws Exception
   */
  public Employee[] findByStore(Store store)
      throws java.lang.Exception {
    return new CMSEmployee[0];
  }

  /**
   * This method is used to search all commissioned employees of a store
   * @param store Store
   * @return Employee[]
   * @throws Exception
   */
  public Employee[] findCommissionedByStore(Store store)
      throws java.lang.Exception {
    return new CMSEmployee[0];
  }

  /**
   * This method is used to find a job code for employee on the basis of job
   * code id
   * @param jobCodeId String
   * @return JobCode
   * @throws Exception
   */
  public JobCode findJobCode(String jobCodeId)
      throws java.lang.Exception {
    return null;
  }

  /**
   * This method is used to find all job codes for employee
   * @return JobCode[]
   * @throws Exception
   */
  public JobCode[] findJobCodes()
      throws java.lang.Exception {
    return new JobCode[0];
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
    return null;
  }

  /**
   * This method is used to search auth info of an employee for a store
   * @param store Store
   * @return UserAuthInfo[]
   * @throws Exception
   */
  public UserAuthInfo[] findAuthInfo(Store store)
      throws java.lang.Exception {
    return new UserAuthInfo[0];
  }

  /**
   * This method is used to submit a new or updated employee
   * @param employee Employee
   * @return boolean
   * @throws Exception
   */
  public boolean submit(Employee employee)
      throws Exception {
    return false;
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
   * @param parm1 TransactionEmployeeAuthInfo
   * @return boolean
   * @throws Exception
   */
  public boolean submitAuthInfo(TransactionEmployeeAuthInfo parm1)
      throws java.lang.Exception {
    return false;
  }

  /**
   * This method is used to submit Employee to database and return it
   * @param employee Employee
   * @return Employee
   * @throws Exception
   * @author Vikram Mundhra
   * Created Date: 04/20/2005
   */
  public Employee submitAndGetRemoteEmployee(Employee employee)
      throws Exception {
    return null;
  }

public CMSEmployee[] findByLastName(String lastName) throws Exception {
	// TODO Auto-generated method stub
	return null;
}
}

