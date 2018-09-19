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


/**
 *
 * <p>Title: CMSEmployeeServices</p>
 *
 * <p>Description: This is an abstract extension of EmployeeServices</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public abstract class CMSEmployeeServices extends EmployeeServices {

  /**
   * This method is used to search an employee on the basis of external HRMS Id
   * @param externalId String
   * @return CMSEmployee
   * @throws RemoteException
   */
  public abstract CMSEmployee findByExternalId(String externalId)
      throws Exception;

  /**
   * This method is used to Submit Employee to database and return it
   * @param employee Employee
   * @return Employee
   * @author Vikram Mundhra
   * Created By: Vikram Mundhra
   * Created Date: 04/20/2005
   */
  public abstract Employee submitAndGetRemoteEmployee(Employee employee)
      throws Exception;

  /**
   * This method is used to search an employee on the basis of lastName
   * @param lastName String
   * @return CMSEmployee
   * @throws RemoteException
   */
  public abstract CMSEmployee[] findByLastName(String lastName)
      throws Exception;
  
}
