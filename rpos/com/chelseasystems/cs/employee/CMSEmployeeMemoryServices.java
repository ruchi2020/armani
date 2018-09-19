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

import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.employee.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.user.*;
import java.io.*;
import java.util.*;


/**
 *
 * <p>Title: CMSEmployeeMemoryServices</p>
 *
 * <p>Description: A simple implementation of EmployeeServices that manages a
 * non-persistent memory-based database of Employee information.</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSEmployeeMemoryServices extends CMSEmployeeServices {
  private ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
  private CMSEmployee[] allEmployees = null;
  private JobCode[] allJobCodes = null;
  private UserAuthInfo[] allEmployeeAuthInfo = null;
  private String delim = "|";

  /**
   * Reads a file with the given filename and places the lines, each of which
   * contains delimited data for a single customer, into a hashtable for
   * later retrieval.
   **/
  /**
   * Default Constructor
   */
  public CMSEmployeeMemoryServices() {
    loadAllEmployeesFromFile();
    loadAllJobCodesFromFile();
    loadAllEmployeeAuthInfoFromFile();
  }

  /**
   * This method is used to search all employees of a store
   * @param store Store
   * @return Employee[]
   * @throws Exception
   */
  public Employee[] findByStore(Store store)
      throws java.lang.Exception {
    if (allEmployees == null || allEmployees.length == 0)
      return null;
    String storeId = store.getId();
    Vector employeeVector = new Vector();
    for (int i = 0; i < allEmployees.length; i++) {
      if (allEmployees[i].getHomeStoreId().equalsIgnoreCase(storeId))
        employeeVector.add(allEmployees[i]);
    }
    employeeVector.trimToSize();
    int size = employeeVector.size();
    if (size > 0) {
      CMSEmployee[] employees = new CMSEmployee[size];
      for (int i = 0; i < size; i++)
        employees[i] = (CMSEmployee)employeeVector.elementAt(i);
      return employees;
    } else {
      return null;
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
    if (allJobCodes == null || allJobCodes.length == 0)
      return null;
    JobCode jobCode = null;
    for (int i = 0; i < allJobCodes.length; i++)
      if (allJobCodes[i].getId().equalsIgnoreCase(jobCodeId)) {
        jobCode = allJobCodes[i];
        break;
      }
    return jobCode;
  }

  /**
   * This method is used to search all job codes
   * @return JobCode[]
   * @throws Exception
   */
  public JobCode[] findJobCodes()
      throws java.lang.Exception {
    return allJobCodes;
  }

  /**
   * This method is used to search an employee on the basis of employee id
   * @param employeeId String
   * @return Employee
   * @throws Exception
   */
  public Employee findById(String employeeId)
      throws java.lang.Exception {
    if (allEmployees == null || allEmployees.length == 0)
      return null;
    Employee employee = null;
    for (int i = 0; i < allEmployees.length; i++)
      if (allEmployees[i].getId().equalsIgnoreCase(employeeId)) {
        employee = allEmployees[i];
        break;
      }
    return employee;
  }

  /**
   * This method is used to search an employee on the basis of external HRMS Id
   * @param externalId String
   * @return CMSEmployee
   * @throws Exception
   */
  public CMSEmployee findByExternalId(String externalId)
      throws java.lang.Exception {
    if (allEmployees == null || allEmployees.length == 0)
      return null;
    CMSEmployee employee = null;
    for (int i = 0; i < allEmployees.length; i++)
      if (allEmployees[i].getId().equalsIgnoreCase(externalId)) {
        employee = allEmployees[i];
        break;
      }
    return employee;
  }

  /**
   * This method is used to search an employee on the basis of ssn
   * @param ssn String
   * @return Employee
   * @throws Exception
   */
  public Employee findBySSN(String ssn)
      throws java.lang.Exception {
    if (allEmployees == null || allEmployees.length == 0)
      return null;
    Employee employee = null;
    for (int i = 0; i < allEmployees.length; i++)
      if (allEmployees[i].getSSN().equalsIgnoreCase(ssn)) {
        employee = allEmployees[i];
        break;
      }
    return employee;
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
    if (allEmployeeAuthInfo == null || allEmployeeAuthInfo.length == 0)
      return null;
    UserAuthInfo employeeAuthInfo = null;
    for (int i = 0; i < allEmployeeAuthInfo.length; i++)
      if (allEmployeeAuthInfo[i].getUserId().equalsIgnoreCase(employeeId)) {
        employeeAuthInfo = allEmployeeAuthInfo[i];
        break;
      }
    return employeeAuthInfo;
  }

  /**
   * This method is used to search auth info of an employee for a store
   * @param store Store
   * @return UserAuthInfo[]
   * @throws Exception
   */
  public UserAuthInfo[] findAuthInfo(Store store)
      throws java.lang.Exception {
    return null;
  }

  /**
   * This method is used to search all commissioned employees of a store
   * @param store Store
   * @return Employee[]
   * @throws Exception
   */
  public Employee[] findCommissionedByStore(Store store)
      throws java.lang.Exception {
    //Not sure what does this method do!!
    return findByStore(store);
  }

  /**
   * This method is used to search an employees on the basis of short name
   * @param shortName String
   * @return Employee
   * @throws Exception
   */
  public Employee findByShortName(String shortName)
      throws java.lang.Exception {
    if (allEmployees == null || allEmployees.length == 0)
      return null;
    Employee employee = null;
    for (int i = 0; i < allEmployees.length; i++)
      if (allEmployees[i].getShortName().equalsIgnoreCase(shortName)) {
        employee = allEmployees[i];
        break;
      }
    return employee;
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
   * @param txnEmpAuthInfo TransactionEmployeeAuthInfo
   * @return boolean
   * @throws Exception
   */
  public boolean submitAuthInfo(TransactionEmployeeAuthInfo txnEmpAuthInfo)
      throws java.lang.Exception {
    return false;
  }

  /**
   * This method is used to load employees from the local file
   */
  private void loadAllEmployeesFromFile() {
    String fileName = FileMgr.getLocalFile("tmp", "CMS_EMPLOYEES");
    if (fileName == null || fileName.equals("")) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "CMSEmployeeMemoryServices()"
          , "Missing data file name.", "Make sure the data file is there."
          , LoggingServices.CRITICAL);
      System.exit(1);
    }
    Vector employeeVector = new Vector();
    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(res.getString("MM/dd/yy"));
    try {
      BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
      String data = br.readLine();
      while (data != null) {
        StringTokenizer st = new StringTokenizer(data, delim);
        String id = st.nextToken();
        CMSEmployee employee = new CMSEmployee(id);
        employee.doSetFirstName(st.nextToken());
        employee.doSetMiddleName(st.nextToken());
        employee.doSetLastName(st.nextToken());
        employee.doSetAddress(st.nextToken());
        employee.doSetCity(st.nextToken());
        employee.doSetState(st.nextToken());
        employee.doSetZipCode(st.nextToken());
        //employee.doSetPhone(st.nextToken());
        String hireDate = st.nextToken();
        employee.doSetHireDate(hireDate.trim().equals("") ? null : formatter.parse(hireDate));
        employee.doSetHomeStoreId(st.nextToken());
        employee.doSetJobCodeId(st.nextToken());
        String termDate = st.nextToken();
        employee.doSetTerminationDate(termDate.trim().equals("") ? null : formatter.parse(termDate));
        employee.doSetShortName(st.nextToken());
        employee.doSetNickName(st.nextToken());
        String x1 = st.nextToken();
        String x2 = st.nextToken(); //employee.doSet??(st.nextToken());
        employee.doSetVacationLeaveBalance(new Double(st.nextToken()));
        employee.doSetSickLeaveBalance(new Double(st.nextToken()));
        employeeVector.add(employee);
        data = br.readLine();
      }
    } catch (FileNotFoundException e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "CMSEmployeeMemoryServices()"
          , "Missing data file.", "Make sure that the data file is there."
          , LoggingServices.CRITICAL);
      System.exit(1);
    } catch (IOException e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "CMSEmployeeMemoryServices()"
          , "Cannot process the employee data file."
          , "Verify the integrity of the employee data file.", LoggingServices.CRITICAL);
      System.exit(1);
    } catch (java.text.ParseException e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "CMSEmployeeMemoryServices()"
          , "Cannot process the employee data file."
          , "Verify that the dates are in the format mm/dd/yyyy.", LoggingServices.CRITICAL);
      System.exit(1);
    }
    employeeVector.trimToSize();
    int size = employeeVector.size();
    if (size > 0) {
      allEmployees = new CMSEmployee[size];
      for (int i = 0; i < size; i++)
        allEmployees[i] = (CMSEmployee)employeeVector.elementAt(i);
    }
  }

  /**
   * This method is used load job codes from local file
   */
  private void loadAllJobCodesFromFile() {
    String fileName = FileMgr.getLocalFile("tmp", "CMS_JOBCODES");
    if (fileName == null || fileName.equals("")) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "CMSEmployeeMemoryServices()"
          , "Missing data file name.", "Make sure the job code data file is there."
          , LoggingServices.CRITICAL);
      System.exit(1);
    }
    Vector jobCodeVector = new Vector();
    try {
      BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
      String data = br.readLine();
      while (data != null) {
        StringTokenizer st = new StringTokenizer(data, delim);
        String id = st.nextToken();
        String desc = st.nextToken();
        JobCode jobCode = new JobCode(id, desc);
        jobCodeVector.add(jobCode);
        data = br.readLine();
      }
    } catch (FileNotFoundException e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "CMSEmployeeMemoryServices()"
          , "Missing job code data file.", "Make sure that the data file is there."
          , LoggingServices.CRITICAL);
      System.exit(1);
    } catch (IOException e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "CMSEmployeeMemoryServices()"
          , "Cannot process the job code data file."
          , "Verify the integrity of the employee/job code data file.", LoggingServices.CRITICAL);
      System.exit(1);
    }
    jobCodeVector.trimToSize();
    int size = jobCodeVector.size();
    if (size > 0) {
      allJobCodes = new JobCode[size];
      for (int i = 0; i < size; i++)
        allJobCodes[i] = (JobCode)jobCodeVector.elementAt(i);
    }
  }

  /**
   * This method is used to load employee auth information from the local file
   */
  private void loadAllEmployeeAuthInfoFromFile() {
    String fileName = FileMgr.getLocalFile("tmp", "CMS_EMPAUTHINFO");
    if (fileName == null || fileName.equals("")) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "CMSEmployeeMemoryServices()"
          , "Missing data file name.", "Make sure the emp auth info file is there."
          , LoggingServices.CRITICAL);
      System.exit(1);
    }
    Vector employeeAuthInfoVector = new Vector();
    try {
      BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
      String data = br.readLine();
      while (data != null) {
        StringTokenizer st = new StringTokenizer(data, delim);
        String id = st.nextToken();
        String info1 = st.nextToken();
        String info2 = st.nextToken();
        UserAuthInfo employeeAuthInfo = new UserAuthInfo();
        employeeAuthInfo.doSetUserId(id);
        employeeAuthInfo.doSetPrimary(info1.getBytes());
        employeeAuthInfo.doSetSecondary(info2.getBytes());
        employeeAuthInfoVector.add(employeeAuthInfo);
        data = br.readLine();
      }
    } catch (FileNotFoundException e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "CMSEmployeeMemoryServices()"
          , "Missing emp auth info data file.", "Make sure that the data file is there."
          , LoggingServices.CRITICAL);
      System.exit(1);
    } catch (IOException e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "CMSEmployeeMemoryServices()"
          , "Cannot process the emp auth data file."
          , "Verify the integrity of the employee/job code data file.", LoggingServices.CRITICAL);
      System.exit(1);
    }
    employeeAuthInfoVector.trimToSize();
    int size = employeeAuthInfoVector.size();
    if (size > 0) {
      allEmployeeAuthInfo = new UserAuthInfo[size];
      for (int i = 0; i < size; i++)
        allEmployeeAuthInfo[i] = (UserAuthInfo)employeeAuthInfoVector.elementAt(i);
    }
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
		if (allEmployees == null || allEmployees.length == 0)
		      return null;
		    CMSEmployee employee = null;
		    ArrayList temp = new ArrayList();
		    for (int i = 0; i < allEmployees.length; i++) {
		    	System.out.println("%"+((CMSEmployee) allEmployees[i]).getLastName());
			      if (allEmployees[i].getLastName().equalsIgnoreCase(lastName)) {
			        temp.add(allEmployees[i]);
			       
			      }
		    }
		    return (CMSEmployee[])temp.toArray(new CMSEmployee[0]);
	}
}

