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

import java.rmi.*;
import com.igray.naming.*;
import com.chelseasystems.cr.employee.*;
import java.lang.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.user.*;


/**
 *
 * <p>Title: ICMSEmployeeRMIServer</p>
 *
 * <p>Description: This interface defines the customer services that are
 * available remotely via RMI </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: Skill net</p>
 *
 * @author
 * @version 1.0
 */
public interface ICMSEmployeeRMIServer extends Remote, IPing {

  /**
   * This method used to search an employee on the basis of employee id
   * @param employeeId String
   * @return CMSEmployee
   * @throws RemoteException
   */
  public CMSEmployee findById(String employeeId)
      throws RemoteException;


  /**
   * This method is used to search an employee on the basis of external HRMS Id
   * @param externalId String
   * @return CMSEmployee
   * @throws RemoteException
   */
  public CMSEmployee findByExternalId(String externalId)
      throws RemoteException;


  /**
   * This method is used to search an employee on the basis short name
   * @param shortName String
   * @return CMSEmployee
   * @throws RemoteException
   */
  public CMSEmployee findByShortName(String shortName)
      throws RemoteException;


  /**
   * This method is used to search an employees on the basis last name
   * @param shortName String
   * @return CMSEmployee
   * @throws RemoteException
   */
  public CMSEmployee[] findByLastName(String lastName)
      throws RemoteException;
  

  /**
   * This method is used to search an employee on the basis of ssn
   * @param ssn String
   * @return CMSEmployee
   * @throws RemoteException
   */
  public CMSEmployee findBySSN(String ssn)
      throws RemoteException;


  /**
   * This method is used to search all employees of a store
   * @param store Store
   * @return CMSEmployee[]
   * @throws RemoteException
   */
  public CMSEmployee[] findByStore(Store store)
      throws RemoteException;


  /**
   * This method is used to search all commissioned employees of a store
   * @param store Store
   * @return CMSEmployee[]
   * @throws RemoteException
   */
  public CMSEmployee[] findCommissionedByStore(Store store)
      throws RemoteException;


  /**
   * This method is used to find all jobs code
   * @return JobCode[]
   * @throws RemoteException
   */
  public JobCode[] findJobCodes()
      throws RemoteException;


  /**
   * This method is used to search job code on the basis of job code id
   * @param jobCodeId String
   * @return JobCode
   * @throws RemoteException
   */
  public JobCode findJobCode(String jobCodeId)
      throws RemoteException;


  /**
   * This method is used to find auth of an employee on the basis of employee
   * id and store
   * @param employeeId employeeId
   * @param store store
   */
  public UserAuthInfo findAuthInfo(String employeeId, Store store)
      throws RemoteException;


  /**
   * This method is used to search auth info of an employee for a store
   * @param store Store
   * @return UserAuthInfo[]
   * @throws RemoteException
   */
  public UserAuthInfo[] findAuthInfo(Store store)
      throws RemoteException;


  /**
   * This method is used to submit a new or updated employee
   * @param employee Employee
   * @return boolean
   * @throws RemoteException
   */
  public boolean submit(Employee employee)
      throws RemoteException;


  /**
   * This method is used to persists auth information of employee to data store
   * @param txEmployee TransactionEmployeeAuthInfo
   * @return boolean
   * @throws RemoteException
   */
  public boolean submitAuthInfo(TransactionEmployeeAuthInfo txEmployee)
      throws RemoteException;


  /**
   * This method is used to submit Employee to database and return it
   * @param employee Employee
   * @return Employee
   * @author Vikram Mundhra
   * Created Date: 04/20/2005
   *
   */
  public Employee submitAndGetRemoteEmployee(Employee employee)
      throws Exception;
}

