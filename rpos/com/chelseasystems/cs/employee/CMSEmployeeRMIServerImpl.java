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
 --------------------------------------------------------------------------------------------
 */


package com.chelseasystems.cs.employee;

import com.chelseasystems.cr.appmgr.DowntimeException;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.node.*;
import com.igray.naming.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import com.chelseasystems.cr.employee.*;
import java.lang.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.user.*;


/**
 *
 * <p>Title: CMSEmployeeRMIServerImpl </p>
 *
 * <p>Description: This is the server side of the RMI connection used for
 * fetching/submitting information .  This class delgates all method calls to the
 * object referenced by the return value from CMSEmployeeServices.getLocal().</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSEmployeeRMIServerImpl extends CMSComponent implements ICMSEmployeeRMIServer {

  /**
   * Constructor
   * @param props Properties
   * @throws RemoteException
   */
  public CMSEmployeeRMIServerImpl(Properties props)
      throws RemoteException {
    super(props);
    setImpl();
    init();
  }

  /**
   * This method is used to set the server side implementation
   */
  private void setImpl() {
    Object obj = getConfigManager().getObject("SERVER_IMPL");
    if (null == obj) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "setImpl()"
          , "Could not instantiate SERVER_IMPL.", "Make sure employee.cfg contains SERVER_IMPL"
          , LoggingServices.MAJOR);
    }
    CMSEmployeeServices.setLocal((CMSEmployeeServices)obj);
  }

  /**
   * This method is used to bind employee object to RMI registery
   */
  private void init() {
    System.out.println("Binding to RMIRegistry...");
    String theName = getConfigManager().getString("REMOTE_NAME");
    if (null != theName) {
      bind(theName, this);
    } else {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "init()"
          , "Could not find name to bind to in registry."
          , "Make sure employee.cfg contains a RMIREGISTRY entry.", LoggingServices.MAJOR);
    }
  }

  /**
   * This method receives callback when the config file changes
   * @param aKey an array of keys that have changed
   */
  protected void configEvent(String[] aKey) {}

  /**
   * This method is used by the DowntimeManager to determine when this object is
   * available.
   * @return boolean <code>true</code> indicates that this class is available.
   * @throws RemoteException
   */
  public boolean ping()
      throws RemoteException {
    return true;
  }

  /**
   * This method is used to search an employee on the basis of employee id
   * @param employeeId String
   * @return CMSEmployee
   * @throws RemoteException
   */
  public CMSEmployee findById(String employeeId)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSEmployee)CMSEmployeeServices.getLocal().findById(employeeId);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findById", start);
      decConnection();
    }
  }

  /**
   * This method is used to search an employee on the basis of external HRMS Id
   * @param externalId String
   * @return CMSEmployee
   * @throws RemoteException
   */
  public CMSEmployee findByExternalId(String externalId)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSEmployee)((CMSEmployeeServices)CMSEmployeeServices.getLocal()).findByExternalId(
          externalId);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByExternalId", start);
      decConnection();
    }
  }

  /**
   * This method is used to search an employee on the basis of short name
   * @param shortName String
   * @return CMSEmployee
   * @throws RemoteException
   */
  public CMSEmployee findByShortName(String shortName)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSEmployee)CMSEmployeeServices.getLocal().findByShortName(shortName);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByShortName", start);
      decConnection();
    }
  }

  /**
   * This method is used to search an employee on the basis of short name
   * @param shortName String
   * @return CMSEmployee
   * @throws RemoteException
   */
  public CMSEmployee[] findByLastName(String shortName)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSEmployee[])((CMSEmployeeServices)CMSEmployeeServices.getLocal()).findByLastName(shortName);
    } catch (Exception e) {
    	e.printStackTrace();
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByShortName", start);
      decConnection();
    }
  }
  
  
  /**
   * This method is used to search an employee on the basis of ssn
   * @param ssn String
   * @return CMSEmployee
   * @throws RemoteException
   */
  public CMSEmployee findBySSN(String ssn)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSEmployee)CMSEmployeeServices.getLocal().findBySSN(ssn);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findBySSN", start);
      decConnection();
    }
  }

  /**
   * This method is used to search all employee for a store
   * @param store Store
   * @return CMSEmployee[]
   * @throws RemoteException
   */
  public CMSEmployee[] findByStore(Store store)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSEmployee[])CMSEmployeeServices.getLocal().findByStore(store);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByStore", start);
      decConnection();
    }
  }

  /**
   * This method is used to search all commissioned employee for a store
   * @param store Store
   * @return CMSEmployee[]
   * @throws RemoteException
   */
  public CMSEmployee[] findCommissionedByStore(Store store)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSEmployee[])CMSEmployeeServices.getLocal().findCommissionedByStore(store);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findCommissionedByStore", start);
      decConnection();
    }
  }

  /**
   * This method is used to search all job codes for an employee
   * @return JobCode[]
   * @throws RemoteException
   */
  public JobCode[] findJobCodes()
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (JobCode[])CMSEmployeeServices.getLocal().findJobCodes();
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findJobCodes", start);
      decConnection();
    }
  }

  /**
   * This method is used to search job code for an employee on the basis of
   * job code id
   * @param jobCodeId String
   * @return JobCode
   * @throws RemoteException
   */
  public JobCode findJobCode(String jobCodeId)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (JobCode)CMSEmployeeServices.getLocal().findJobCode(jobCodeId);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findJobCode", start);
      decConnection();
    }
  }

  /**
   * This method is used to search auth information of the employee on the
   * basis of employee id and store
   * @param employeeId String
   * @param store Store
   * @return UserAuthInfo
   * @throws RemoteException
   */
  public UserAuthInfo findAuthInfo(String employeeId, Store store)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (UserAuthInfo)CMSEmployeeServices.getLocal().findAuthInfo(employeeId, store);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findAuthInfo", start);
      decConnection();
    }
  }

  /**
   * This method is used to search auth information for a store
   * @param store Store
   * @return UserAuthInfo[]
   * @throws RemoteException
   */
  public UserAuthInfo[] findAuthInfo(Store store)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (UserAuthInfo[])CMSEmployeeServices.getLocal().findAuthInfo(store);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findAuthInfo(Store)", start);
      decConnection();
    }
  }

  /**
   * This method is used to submit a new or updated employee
   * @param employee Employee
   * @return boolean
   * @throws RemoteException
   */
  public boolean submit(Employee employee)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return CMSEmployeeServices.getLocal().submit(employee);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("submit", start);
      decConnection();
    }
  }

  /**
   * This method is used to presists employee auth info to data store
   * @param txEmployee TransactionEmployeeAuthInfo
   * @return boolean
   * @throws RemoteException
   */
  public boolean submitAuthInfo(TransactionEmployeeAuthInfo txEmployee)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (boolean)CMSEmployeeServices.getLocal().submitAuthInfo(txEmployee);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("submitAuthInfo", start);
      decConnection();
    }
  }

  /**
   * This method is used to Submit Employee to database and return it
   * @param employee Employee
   * @return Employee
   * @author Vikram Mundhra
   * Created By: Vikram Mundhra
   * Created Date: 04/20/2005
   */
  public Employee submitAndGetRemoteEmployee(Employee employee)
      throws Exception {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return ((CMSEmployeeServices)CMSEmployeeServices.getLocal()).submitAndGetRemoteEmployee(
          employee);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("submit", start);
      decConnection();
    }
  }
}

