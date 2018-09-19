/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 02-14-2005 | Anand     | N/A       | Modified to add External ID                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 3    | 02-19-2005 | Vikram    | 104665    |  Removed Employee ID defaulting to SSN       |
 |      |            |           |           | Added submitAndGetRemoteEmployee method      |
 --------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.employee;

import com.chelseasystems.cr.xml.XMLUtil;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.employee.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cs.xml.*;
import java.io.*;
import java.util.*;
import com.chelseasystems.cr.user.*;


/**
 *
 * <p>Title: CMSEmployeeFileServices </p>
 *
 * <p>Description: A simple implementation of EmployeeServices that manages a
 * non-persistent memory-based database of Employee information</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSEmployeeFileServices extends CMSEmployeeServices {
  static private Vector allEmployees = null;
  static private Vector allJobCodes = null;
  static private Vector allEmployeeAuthInfo = null;
  static private String employeeFileName = FileMgr.getLocalFile("xml", "employees.xml");
  static private String jobCodeFileName = FileMgr.getLocalFile("xml", "jobcodes.xml");
  static private String empAuthInfoFileName = FileMgr.getLocalFile("xml", "authinfo.xml");
  static private int numberOfUsing = 0;

  /**
   * Default Constructor
   */
  public CMSEmployeeFileServices() {
    if (allEmployees == null)
      loadAllEmployeesFromFile();
    if (allJobCodes == null)
      loadAllJobCodesFromFile();
    if (allEmployeeAuthInfo == null)
      loadAllEmployeeAuthInfoFromFile();
  }

  /**
   * This method is used to find all employees of a store
   * @param store Store
   * @return Employee[]
   * @throws Exception
   */
  public Employee[] findByStore(Store store)
      throws java.lang.Exception {
    if (numberOfUsing < 2) {
      loadAllEmployeesFromFile();
      numberOfUsing++;
    }
    if (allEmployees == null || allEmployees.size() == 0)
      return null;
    String storeId = store.getId();
    Vector employeeVector = new Vector();
    for (int i = 0; i < allEmployees.size(); i++) {
      CMSEmployee emp = (CMSEmployee)allEmployees.get(i);
      if (emp.getHomeStoreId().equalsIgnoreCase(storeId))
        employeeVector.add(emp.clone());
    }
    if (employeeVector == null || employeeVector.size() == 0)
      return null;
    else
      return (CMSEmployee[])employeeVector.toArray(new CMSEmployee[0]);
  }

  /**
   * This method is used to search job code on the basis of job code id
   * @param jobCodeId String
   * @return JobCode
   * @throws Exception
   */
  public JobCode findJobCode(String jobCodeId)
      throws java.lang.Exception {
    if (allJobCodes == null || allJobCodes.size() == 0)
      return null;
    JobCode jobCode = null;
    for (int i = 0; i < allJobCodes.size(); i++)
      if (((JobCode)allJobCodes.get(i)).getId().equalsIgnoreCase(jobCodeId)) {
        jobCode = (JobCode)allJobCodes.get(i);
        break;
      }
    return jobCode;
  }

  /**
   * This method is used to get all job codes
   * @return JobCode[]
   * @throws Exception
   */
  public JobCode[] findJobCodes()
      throws java.lang.Exception {
    int size = allJobCodes.size();
    JobCode[] jobCodes = new JobCode[size];
    for (int i = 0; i < size; i++)
      jobCodes[i] = (JobCode)allJobCodes.get(i);
    return jobCodes;
  }

  /**
   * This method is used to find employee on the basis of employee id
   * @param employeeId String
   * @return Employee
   * @throws Exception
   */
  public Employee findById(String employeeId)
      throws java.lang.Exception {
    if (numberOfUsing < 2) {
      loadAllEmployeesFromFile();
      numberOfUsing++;
    }
    if (allEmployees == null || allEmployees.size() == 0)
      return null;
    Employee employee = null;
    for (int i = 0; i < allEmployees.size(); i++)
      if (((CMSEmployee)allEmployees.get(i)).getId().equalsIgnoreCase(employeeId)) {
        employee = (CMSEmployee)allEmployees.get(i);
        break;
      }
    if (employee == null)
      return null;
    else
      return (Employee)employee.clone();
  }

  /**
   * This method is used to find employee on the basis of External HRMS Id
   * @param externalId String
   * @return CMSEmployee
   * @throws Exception
   */
  public CMSEmployee findByExternalId(String externalId)
      throws java.lang.Exception {
    if (numberOfUsing < 2) {
      loadAllEmployeesFromFile();
      numberOfUsing++;
    }
    if (allEmployees == null || allEmployees.size() == 0)
      return null;
    Employee employee = null;
    for (int i = 0; i < allEmployees.size(); i++)
      if (((CMSEmployee)allEmployees.get(i)).getExternalID().equalsIgnoreCase(externalId)) {
        employee = (CMSEmployee)allEmployees.get(i);
        break;
      }
    if (employee == null)
      return null;
    else
      return (CMSEmployee)employee.clone();
  }

  /**
   * This method is used to find employee on the basis of ssn
   * @param ssn String
   * @return Employee
   * @throws Exception
   */
  public Employee findBySSN(String ssn)
      throws java.lang.Exception {
    if (numberOfUsing < 2) {
      loadAllEmployeesFromFile();
      numberOfUsing++;
    }
    if (allEmployees == null || allEmployees.size() == 0)
      return null;
    Employee employee = null;
    for (int i = 0; i < allEmployees.size(); i++)
      if (((CMSEmployee)allEmployees.get(i)).getSSN().equalsIgnoreCase(ssn)) {
        employee = (CMSEmployee)allEmployees.get(i);
        break;
      }
    if (employee == null)
      return null;
    else
      return (Employee)employee.clone();
  }

  /**
   * This method is used to find auth of an employee on the basis of employee
   * id and store id
   * @param employeeId String
   * @param store Store
   * @return UserAuthInfo
   * @throws Exception
   */
  public UserAuthInfo findAuthInfo(String employeeId, Store store)
      throws java.lang.Exception {
    if (allEmployeeAuthInfo == null || allEmployeeAuthInfo.size() == 0)
      return null;
    UserAuthInfo employeeAuthInfo = null;
    for (int i = 0; i < allEmployeeAuthInfo.size(); i++)
      if (((UserAuthInfo)allEmployeeAuthInfo.get(i)).getUserId().equalsIgnoreCase(employeeId)) {
        employeeAuthInfo = (UserAuthInfo)allEmployeeAuthInfo.get(i);
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
    if (allEmployeeAuthInfo == null || allEmployeeAuthInfo.size() == 0)
      return null;
    else
      return (UserAuthInfo[])allEmployeeAuthInfo.toArray(new UserAuthInfo[0]);
  }

  /**
   * This method is used to find all commissioned employees of a store
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
   * This method is used to find an employee on the basis of short name
   * @param shortName String
   * @return Employee
   * @throws Exception
   */
  public Employee findByShortName(String shortName)
      throws java.lang.Exception {
    if (numberOfUsing < 2) {
      loadAllEmployeesFromFile();
      numberOfUsing++;
    }
    if (allEmployees == null || allEmployees.size() == 0)
      return null;
    Employee employee = null;
    for (int i = 0; i < allEmployees.size(); i++)
      if (((CMSEmployee)allEmployees.get(i)).getShortName().equalsIgnoreCase(shortName)) {
        employee = (CMSEmployee)allEmployees.get(i);
        break;
      }
    if (employee == null)
      return null;
    else
      return (Employee)employee.clone();
  }

  /**
   * This method is used to submit a new or updated employee
   * @param employee Employee
   * @return boolean
   * @throws Exception
   */
  public boolean submit(Employee employee)
      throws Exception {
    if (allEmployees == null || allEmployees.size() == 0)
      loadAllEmployeesFromFile();
          /*Base Code: Not required as per ArmaniPOS_104665_TS_usermanagement_Rev0
         if (employee.isNew()){employee.doSetId(employee.getSSN());}
    */

   for (int i = 0; i < allEmployees.size(); i++)
     if (((CMSEmployee)allEmployees.get(i)).getId().equals(employee.getId())) {
       allEmployees.remove(i);
       break;
     }
   employee.setExisting();
    allEmployees.add(employee);
    String xml = new EmployeeXML().toXML(allEmployees);
    new EmployeeXML().writToFile(employeeFileName, allEmployees);
    return true;
  }

  /**
   *
   * @param employee Employee
   * @return boolean
   * @throws Exception
   */
  public boolean submitLocalEmployee(Employee employee)
      throws Exception {
    return submit(employee);
  }

  /**
   * This method is used to persists auth information of employee to data store
   * @param txnEmpAuthInfo TransactionEmployeeAuthInfo
   * @return boolean
   * @throws Exception
   */
  public boolean submitAuthInfo(TransactionEmployeeAuthInfo txnEmpAuthInfo)
      throws java.lang.Exception {
    UserAuthInfo employeeAuthInfo = txnEmpAuthInfo.getEmployeeAuthInfo();
    boolean isNew = true;
    if (allEmployeeAuthInfo == null) {
      allEmployeeAuthInfo = new Vector();
    } else {
      for (int i = 0; i < allEmployeeAuthInfo.size(); i++) {
        if (((UserAuthInfo)allEmployeeAuthInfo.get(i)).getUserId().equals(employeeAuthInfo.
            getUserId())) {
          allEmployeeAuthInfo.remove(i);
          allEmployeeAuthInfo.add(i, employeeAuthInfo);
          isNew = false;
          break;
        }
      }
    }
    if (isNew)
      allEmployeeAuthInfo.add(employeeAuthInfo);
    new EmpAuthInfoXML().writToFile(empAuthInfoFileName, allEmployeeAuthInfo);
    return true;
  }

  /**
   * This method is used to load all employee from the local file
   */
  protected void loadAllEmployeesFromFile() {
    long begin = new java.util.Date().getTime();
    if (employeeFileName == null || employeeFileName.equals("")) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadAllEmployeesFromFile()"
          , "Missing data file name.", "Make sure the data file is there."
          , LoggingServices.CRITICAL);
      System.exit(1);
    }
    try {
      allEmployees = (new EmployeeXML()).toObjects(employeeFileName);
    } catch (org.xml.sax.SAXException saxException) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadAllEmployeesFromFile()"
          , "Cannot parse the employee data file."
          , "Verify the integrity of the employee data file", LoggingServices.CRITICAL
          , saxException);
      //         System.exit(1);
    } catch (IOException e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadAllEmployeesFromFile()"
          , "Cannot process the employee data file."
          , "Verify the integrity of the employee data file", LoggingServices.CRITICAL, e);
      //         System.exit(1);
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadAllEmployeesFromFile()"
          , "Cannot process the employee data file (2).", "Unknow exception: " + e
          , LoggingServices.CRITICAL, e);
      //         System.exit(1);
    }
    long end = new java.util.Date().getTime();
    System.out.println("Number of employees loaded: " + allEmployees.size() + " (" + (end - begin)
        + "ms)");
  }

  /**
   * This method is used to load all job codes from the local file
   */
  protected void loadAllJobCodesFromFile() {
    long begin = new java.util.Date().getTime();
    if (jobCodeFileName == null || jobCodeFileName.equals("")) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadAllJobCodesFromFile()"
          , "Missing data file name.", "Make sure the data file is there."
          , LoggingServices.CRITICAL);
      System.exit(1);
    }
    try {
      allJobCodes = (new JobCodeXML()).toObjects(jobCodeFileName);
    } catch (org.xml.sax.SAXException saxException) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadAllJobCodesFromFile()"
          , "Cannot parse the job code data file."
          , "Verify the integrity of the job code data file", LoggingServices.CRITICAL);
      System.exit(1);
    } catch (IOException e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadAllJobCodesFromFile()"
          , "Cannot process the job code data file."
          , "Verify the integrity of the job code data file", LoggingServices.CRITICAL);
      System.exit(1);
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadAllJobCodesFromFile()"
          , "Cannot process the job code data file.", "Unknow exception: " + e
          , LoggingServices.CRITICAL);
      System.exit(1);
    }
    long end = new java.util.Date().getTime();
    System.out.println("Number of job codes loaded: " + allJobCodes.size() + " (" + (end - begin)
        + "ms)");
  }

  /**
   * This method is used to load employee auth information from local file
   */
  protected void loadAllEmployeeAuthInfoFromFile() {
    long begin = new java.util.Date().getTime();
    if (empAuthInfoFileName == null || empAuthInfoFileName.equals("")) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadAllEmployeeAuthInfoFromFile()"
          , "Missing data file name.", "Make sure the data file is there."
          , LoggingServices.CRITICAL);
      System.exit(1);
    }
    try {
      allEmployeeAuthInfo = (new EmpAuthInfoXML()).toObjects(empAuthInfoFileName);
    } catch (org.xml.sax.SAXException saxException) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadAllEmployeeAuthInfoFromFile()"
          , "Cannot parse the auth info data file."
          , "Verify the integrity of the auth info data file", LoggingServices.CRITICAL);
      System.exit(1);
    } catch (IOException e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadAllEmployeeAuthInfoFromFile()"
          , "Cannot process the auth info data file."
          , "Verify the integrity of the auth info data file", LoggingServices.CRITICAL);
      System.exit(1);
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadAllEmployeeAuthInfoFromFile()"
          , "Cannot process the auth info data file.", "Unknow exception: " + e
          , LoggingServices.CRITICAL);
      System.exit(1);
    }
    long end = new java.util.Date().getTime();
    System.out.println("Number of auto info loaded: " + allEmployeeAuthInfo.size() + " ("
        + (end - begin) + "ms)");
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
		if (numberOfUsing < 2) {
			loadAllEmployeesFromFile();
			numberOfUsing++;
		}
		ArrayList temp = new ArrayList();
		if (allEmployees == null || allEmployees.size() == 0)
			return null;
		for (int i = 0; i < allEmployees.size(); i++){
			System.out.println("$"+((CMSEmployee) allEmployees.get(i)).getLastName());
			if (((CMSEmployee) allEmployees.get(i)).getLastName()
					.equalsIgnoreCase(lastName)) {
				temp.add(((CMSEmployee) allEmployees.get(i)).clone());
			}
		}
		return (CMSEmployee[]) temp.toArray(new CMSEmployee[0]);
	}
}

