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
 | 3    | 02-20-2005 | Vikram    | 104665    | submitAndGetRemoteEmployee is used for remote|
 --------------------------------------------------------------------------------------------
 */


package com.chelseasystems.cs.employee;

import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.employee.*;
import com.chelseasystems.cs.store.*;
import java.lang.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.user.*;


/**
 *
 * <p>Title: CMSEmployeeClientServices</p>
 *
 * <p>Description: This is client side object for retrieving and submitting
 * employee information</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSEmployeeClientServices extends ClientServices {

  /** Configuration manager **/
//  private ConfigMgr config = null;

  /** Primary implementation */
  private CMSEmployeeServices localSvc = null;

  /** Secondary (optional) implementation */
  private CMSEmployeeServices remoteSvc = null;

  /**
   * Default Constructors which set the current implementation
   */
  public CMSEmployeeClientServices() {
    // Set up the configuration manager.
    config = new ConfigMgr("employee.cfg");
  }

  /**
   * This method initialize the primary implementation
   */
  public void init(boolean online)
      throws Exception {
    localSvc = (CMSEmployeeServices)config.getObject("CLIENT_LOCAL_IMPL");
    EmployeeServices.setLocal(localSvc);
    // Set up the proper implementation of the service.
    if (online)
      onLineMode();
    else
      offLineMode();
  }

  /**
   * Reads "CLIENT_IMPL" from config file. Returns the class that defines
   * what object is providing the service to objects using this client service
   * in "on-line" mode, i.e. connected to an app server.  If null, this 
   * clientservice is not considered when determining app online status.
   * @return a class of the online service.
   */
  protected Class getOnlineService () throws ClassNotFoundException {
    String className = config.getString("CLIENT_REMOTE_IMPL");
    Class serviceClass = Class.forName(className);
    return  serviceClass;
  }
  
  /**
   * This method is invoked when system is online, to get the client remote
   * implementation from the employee.cfg and set the same in CMSEmployeeServices
   */
  public void onLineMode() {
    LoggingServices.getCurrent().logMsg("On-Line Mode for CMSEmployeeClientServices");
    remoteSvc = (CMSEmployeeServices)config.getObject("CLIENT_REMOTE_IMPL");
    if (remoteSvc == null) {
      LoggingServices.getCurrent().logMsg("CMSEmployeeClientServices", "onLineMode()"
          , "Cannot instantiate the class that provides the"
          + "implementation of CMSEmployeeServices in employee.cfg."
          , "Make sure that employee.cfg contains an entry with "
          + "a key of CLIENT_IMPL and a value that is the name of a class "
          + "that provides a concrete implementation of CMSEmployeeServices."
          , LoggingServices.MAJOR);
      setOffLineMode();
      return;
    }
    CMSEmployeeServices.setRemote(remoteSvc);
  }

  /**
   * This method is invoked when system is offline, to get the client remote
   * downtime from the employee.cfg and set the same in CMSEmployeeServices
   */
  public void offLineMode() {
    LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSEmployeeClientServices");
    remoteSvc = (CMSEmployeeServices)config.getObject("CLIENT_REMOTE_DOWNTIME");
    if (remoteSvc == null) {
      LoggingServices.getCurrent().logMsg("CMSEmployeeClientServices", "offLineMode()"
          , "Cannot instantiate the class that provides the"
          + " implementation of CMSEmployeeServices in employee.cfg."
          , "Make sure that employee.cfg contains an entry with "
          + "a key of DOWNTIME_CLIENT_IMPL and a value"
          + " that is the name of a class that provides a concrete"
          + " implementation of CMSEmployeeServices.", LoggingServices.CRITICAL);
    }
    CMSEmployeeServices.setRemote(remoteSvc);
  }

  /**
   *
   * @return Object
   */
  public Object getCurrentService() {
    return (CMSEmployeeServices.getRemote());
  }

  /**
   * This method is used to search an employee on the basis of employee id
   * @param employeeId String
   * @return CMSEmployee
   * @throws Exception
   */
  public CMSEmployee findById(String employeeId)
      throws Exception {
    CMSEmployee result = null;
    try {
      this.fireWorkInProgressEvent(true);
      if (CMSEmployeeServices.getLocal() != null)
        result = (CMSEmployee)CMSEmployeeServices.getLocal().findById(employeeId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findById"
          , "Primary (Local) Implementation for CMSEmployeeServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
    if (result == null && remoteSvc != null)
      try {
        this.fireWorkInProgressEvent(true);
        result = (CMSEmployee)CMSEmployeeServices.getRemote().findById(employeeId);
        if (result != null)
          CMSEmployeeServices.getLocal().submit(result);
      } catch (DowntimeException ex) {
        LoggingServices.getCurrent().logMsg(getClass().getName(), "findById()"
            , "Primary (Remote) Implementation for CMSEmployeeServices failed, going Off-Line..."
            , "See Exception", LoggingServices.MAJOR, ex);
        offLineMode();
        setOffLineMode();
      }
    finally {
      this.fireWorkInProgressEvent(false);
    }
    return (result);
  }

  /**
   * This method is used to search an employee by External HRMS Id
   * @param externalId String
   * @return CMSEmployee
   * @throws Exception
   */
  public CMSEmployee findByExternalId(String externalId)
      throws Exception {
    CMSEmployee result = null;
    try {
      this.fireWorkInProgressEvent(true);
      if (CMSEmployeeServices.getLocal() != null)
        result = (CMSEmployee)((CMSEmployeeServices)CMSEmployeeServices.getLocal()).
            findByExternalId(externalId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByExternalId"
          , "Primary (Local) Implementation for CMSEmployeeServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
    if (result == null && remoteSvc != null)
      try {
        this.fireWorkInProgressEvent(true);
        result = (CMSEmployee)((CMSEmployeeServices)CMSEmployeeServices.getRemote()).
            findByExternalId(externalId);
        if (result != null)
          CMSEmployeeServices.getLocal().submit(result);
      } catch (DowntimeException ex) {
        LoggingServices.getCurrent().logMsg(getClass().getName(), "findByExternalId()"
            , "Primary (Remote) Implementation for CMSEmployeeServices failed, going Off-Line..."
            , "See Exception", LoggingServices.MAJOR, ex);
        offLineMode();
        setOffLineMode();
      }
    finally {
      this.fireWorkInProgressEvent(false);
    }
    return (result);
  }

  /**
   * This method is used to search an employee by short name
   * @param shortName String
   * @return CMSEmployee
   * @throws Exception
   */
  public CMSEmployee findByShortName(String shortName)
      throws Exception {
    CMSEmployee result = null;
    try {
      this.fireWorkInProgressEvent(true);
      result = (CMSEmployee)CMSEmployeeServices.getLocal().findByShortName(shortName);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByShortName"
          , "Primary (Local) Implementation for CMSEmployeeServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
    if (result == null && remoteSvc != null)
      try {
        this.fireWorkInProgressEvent(true);
        result = (CMSEmployee)CMSEmployeeServices.getRemote().findByShortName(shortName);
        if (result != null)
          CMSEmployeeServices.getLocal().submit(result);
      } catch (DowntimeException ex) {
        LoggingServices.getCurrent().logMsg(getClass().getName(), "findByShortName()"
            , "Primary (Remote) Implementation for CMSEmployeeServices failed, going Off-Line..."
            , "See Exception", LoggingServices.MAJOR, ex);
        offLineMode();
        setOffLineMode();
      }
    finally {
      this.fireWorkInProgressEvent(false);
    }
    return (result);
  }

  /**
   * This method is used to search an employee by last name
   * @param lastName String
   * @return CMSEmployee
   * @throws Exception
   */
  public CMSEmployee[] findByLastName(String lastName)
      throws Exception {
    CMSEmployee[] result = null;
    try {
      this.fireWorkInProgressEvent(true);
      result = (CMSEmployee[])((CMSEmployeeServices)(CMSEmployeeServices.getLocal())).findByLastName(lastName);
      System.out.println("CMSEmployeeClientServices:findByLastName:1:"+(result==null?"null":String.valueOf(result.length)));
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByLastName"
          , "Primary (Local) Implementation for CMSEmployeeServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
    if (((result == null)||result.length==0) && remoteSvc != null)
      try {
        this.fireWorkInProgressEvent(true);
        result = (CMSEmployee[])((CMSEmployeeServices)(CMSEmployeeServices.getRemote())).findByLastName(lastName);
        System.out.println("CMSEmployeeClientServices:findByLastName:2:"+(result==null?"null":String.valueOf(result.length)));
 /*       if (result != null)
          CMSEmployeeServices.getLocal().submit(result);*/
      } catch (DowntimeException ex) {
        ex.printStackTrace();
    	  LoggingServices.getCurrent().logMsg(getClass().getName(), "findByLastName()"
            , "Primary (Remote) Implementation for CMSEmployeeServices failed, going Off-Line..."
            , "See Exception", LoggingServices.MAJOR, ex);
        
        offLineMode();
        setOffLineMode();
      }
    finally {
      this.fireWorkInProgressEvent(false);
    }
    return (result);
  }
  
  
  /**
   * This method is used to search an employee on the basis of SSN no
   * @param ssn String
   * @return CMSEmployee
   * @throws Exception
   */
  public CMSEmployee findBySSN(String ssn)
      throws Exception {
    CMSEmployee result = null;
    try {
      this.fireWorkInProgressEvent(true);
      result = (CMSEmployee)CMSEmployeeServices.getLocal().findBySSN(ssn);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findBySSN"
          , "Primary (Local) Implementation for CMSEmployeeServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
    if (result == null && remoteSvc != null)
      try {
        this.fireWorkInProgressEvent(true);
        result = (CMSEmployee)CMSEmployeeServices.getRemote().findBySSN(ssn);
        if (result != null)
          CMSEmployeeServices.getLocal().submit(result);
      } catch (DowntimeException ex) {
        LoggingServices.getCurrent().logMsg(getClass().getName(), "findBySSN()"
            , "Primary (Remote) Implementation for CMSEmployeeServices failed, going Off-Line..."
            , "See Exception", LoggingServices.MAJOR, ex);
        offLineMode();
        setOffLineMode();
      }
    finally {
      this.fireWorkInProgressEvent(false);
    }
    return (result);
  }

  /**
   * This method is used to find an employee for a store
   * @param store Store
   * @return CMSEmployee[]
   * @throws Exception
   */
  public CMSEmployee[] findByStore(Store store)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSEmployee[])CMSEmployeeServices.getLocal().findByStore(store);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByStore"
          , "Primary Implementation for CMSEmployeeServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      return (new CMSEmployee[0]);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to find commissioned employee for a store
   * @param store Store
   * @return CMSEmployee[]
   * @throws Exception
   */
  public CMSEmployee[] findCommissionedByStore(Store store)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSEmployee[])CMSEmployeeServices.getLocal().findCommissionedByStore(store);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findCommissionedByStore"
          , "Primary Implementation for CMSEmployeeServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      return (new CMSEmployee[0]);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to search all job code
   * @return JobCode[]
   * @throws Exception
   */
  public JobCode[] findJobCodes()
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (JobCode[])CMSEmployeeServices.getRemote().findJobCodes();
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findJobCodes"
          , "Primary Implementation for CMSEmployeeServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (JobCode[])CMSEmployeeServices.getRemote().findJobCodes();
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to search job code for an employee on the basis of
   * job code id
   * @param jobCodeId String
   * @return JobCode
   * @throws Exception
   */
  public JobCode findJobCode(String jobCodeId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (JobCode)CMSEmployeeServices.getRemote().findJobCode(jobCodeId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findJobCode"
          , "Primary Implementation for CMSEmployeeServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (JobCode)CMSEmployeeServices.getRemote().findJobCode(jobCodeId);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to find auth of an employee
   * @param employeeId String
   * @param store Store
   * @return UserAuthInfo
   * @throws Exception
   */
  public UserAuthInfo findAuthInfo(String employeeId, Store store)
      throws Exception {
    UserAuthInfo result = null;
    try {
      this.fireWorkInProgressEvent(true);
      result = (UserAuthInfo)CMSEmployeeServices.getLocal().findAuthInfo(employeeId, store);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findAuthInfo"
          , "Primary (Local) Implementation for CMSEmployeeServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
    if (result == null && remoteSvc != null)
      try {
        this.fireWorkInProgressEvent(true);
        result = (UserAuthInfo)CMSEmployeeServices.getRemote().findAuthInfo(employeeId, store);
        if (result != null)
          CMSEmployeeServices.getLocal().submitAuthInfo(new TransactionEmployeeAuthInfo(result
              , store));
      } catch (DowntimeException ex) {
        LoggingServices.getCurrent().logMsg(getClass().getName(), "findById()"
            , "Primary (Remote) Implementation for CMSEmployeeServices failed, going Off-Line..."
            , "See Exception", LoggingServices.MAJOR, ex);
      }
    finally {
      this.fireWorkInProgressEvent(false);
    }
    return (result);
  }

  /**
   * This method is used to find Auth Info for a store
   * @param store Store
   * @return UserAuthInfo[]
   * @throws Exception
   */
  public UserAuthInfo[] findAuthInfo(Store store)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (UserAuthInfo[])CMSEmployeeServices.getLocal().findAuthInfo(store);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findAuthInfo"
          , "Primary Implementation for CMSEmployeeServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      return (new UserAuthInfo[0]);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method to submit a new or updated employee
   * @param employee Employee
   * @return boolean
   * @throws Exception
   */
  public boolean submit(Employee employee)
      throws Exception {
    boolean submitRemote = false, submitLocal = false;
    try {
      this.fireWorkInProgressEvent(true);
      //added by Vikram Mundhra
      Employee tempEmp = ((CMSEmployeeServices)CMSEmployeeServices.getRemote()).
          submitAndGetRemoteEmployee(employee);
      if (tempEmp != null && tempEmp.getId() != null) {
        employee.doSetId(tempEmp.getId());
        submitRemote = true;
      }
      /*Base Code: changes to above during ArmaniPOS_104665_TS_usermanagement_Rev0
             submitRemote = CMSEmployeeServices.getRemote().submit(employee);
      */

    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "submit"
          , "Primary (Romote) Implementation for CMSEmployeeServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
    } finally {
      this.fireWorkInProgressEvent(false);
    }
    if (submitRemote) {
      try {
        this.fireWorkInProgressEvent(true);
        submitLocal = CMSEmployeeServices.getLocal().submit(employee);
      } catch (DowntimeException ex) {
        LoggingServices.getCurrent().logMsg(getClass().getName(), "submit"
            , "Primary (Local) Implementation for CMSEmployeeServices failed, going Off-Line..."
            , "See Exception", LoggingServices.MAJOR, ex);
        offLineMode();
        setOffLineMode();
      } finally {
        this.fireWorkInProgressEvent(false);
      }
    }
    return (submitRemote && submitLocal);
  }

  /**
   * This method presists auth info to data store
   * @param txEmployee TransactionEmployeeAuthInfo
   * @return boolean
   * @throws Exception
   */
  public boolean submitAuthInfo(TransactionEmployeeAuthInfo txEmployee)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (boolean)CMSEmployeeServices.getLocal().submitAuthInfo(txEmployee);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "submitAuthInfo"
          , "Primary (Local) Implementation for CMSEmployeeServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
    } finally {
      this.fireWorkInProgressEvent(false);
    }
    return (true);
  }
}

