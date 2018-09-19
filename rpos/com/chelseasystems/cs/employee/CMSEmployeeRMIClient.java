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

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.node.IRemoteServerClient;
import com.chelseasystems.cr.node.ICMSComponent;
import com.igray.naming.*;
import java.rmi.*;
import com.chelseasystems.cr.employee.*;
import java.lang.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.user.*;


/**
 *
 * <p>Title: CMSEmployeeRMIClient</p>
 *
 * <p>Description: This class deal with client-side of an RMI connection for
 * fetching/submitting employee object</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSEmployeeRMIClient extends CMSEmployeeServices implements IRemoteServerClient {

  /** The configuration manager */
  private ConfigMgr config = null;

  /** The reference to the remote implementation of the service. */
  private ICMSEmployeeRMIServer cmsemployeeServer = null;

  /** The maximum number of times to try to establish a connection to the RMIServerImpl */
  private int maxTries = 1;

  /**
   * This method set the configuration manager and make sure that the system
   * has a security manager set.
   */
  public CMSEmployeeRMIClient()
      throws DowntimeException {
    config = new ConfigMgr("employee.cfg");
    if (System.getSecurityManager() == null)
      System.setSecurityManager(new RMISecurityManager());
    init();
  }

  /**
   * This method is used to lookup the remote object from the RMI registry.
   */
  private void init()
      throws DowntimeException {
    try {
      this.lookup();
      System.out.println("CMSEmployeeRMIClient Lookup: Complete");
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "init()"
          , "Cannot establish connection to RMI server."
          , "Make sure that the server is registered on the remote server"
          + " and that the name of the remote server and remote service are"
          + " correct in the update.cfg file.", LoggingServices.MAJOR, e);
      throw new DowntimeException(e.getMessage());
    }
  }

  /**
   * This method is used to lookup the remote object from the RMI registry.
   * @exception Exception
   */
  public void lookup()
      throws Exception {
    NetworkMgr mgr = new NetworkMgr("network.cfg");
    maxTries = mgr.getRetryAttempts();
    String connect = mgr.getRMIMasterNode() + config.getString("REMOTE_NAME") + mgr.getQuery();
    cmsemployeeServer = (ICMSEmployeeRMIServer)NamingService.lookup(connect);
  }

  /**
   * This method is used to check whether remote server is available or not
   * @return boolean
   */
  public boolean isRemoteServerAvailable() {
    try {
      return ((ICMSComponent)this.cmsemployeeServer).isAvailable();
    } catch (Exception ex) {
      return false;
    }
  }

  /**
   * This method is used to search an employee by employee Id
   * @param employeeId employeeId
   */
  public Employee findById(String employeeId)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsemployeeServer == null)
        init();
      try {
        return cmsemployeeServer.findById((String)employeeId);
      } catch (ConnectException ce) {
        cmsemployeeServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSEmployeeServices");
  }

  /**
   * This method is used to search an employee by external HRMS Id
   * @param externalId String
   * @return CMSEmployee
   * @throws Exception
   */
  public CMSEmployee findByExternalId(String externalId)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsemployeeServer == null)
        init();
      try {
        return cmsemployeeServer.findByExternalId((String)externalId);
      } catch (ConnectException ce) {
        cmsemployeeServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSEmployeeServices");
  }

  /**
   * This method is used to search an employee by short name
   * @param shortName String
   * @return Employee
   * @throws Exception
   */
  public Employee findByShortName(String shortName)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsemployeeServer == null)
        init();
      try {
        return cmsemployeeServer.findByShortName((String)shortName);
      } catch (ConnectException ce) {
        cmsemployeeServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSEmployeeServices");
  }

	public CMSEmployee[] findByLastName(String lastName) throws Exception {
		for (int x = 0; x < maxTries; x++) {
			if (cmsemployeeServer == null)
				init();
			try {
				return cmsemployeeServer.findByLastName((String) lastName);
			} catch (ConnectException ce) {
				cmsemployeeServer = null;
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new DowntimeException(ex.getMessage());
			}
		}
		throw new DowntimeException(
				"Unable to establish connection to CMSEmployeeServices");
	}

  /**
   * This method is used to search employee on the basis of ssn
   * @param ssn String
   * @return Employee
   * @throws Exception
   */
  public Employee findBySSN(String ssn)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsemployeeServer == null)
        init();
      try {
        return cmsemployeeServer.findBySSN((String)ssn);
      } catch (ConnectException ce) {
        cmsemployeeServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSEmployeeServices");
  }

  /**
   * This method is used to search all employee for a store
   * @param store Store
   * @return Employee[]
   * @throws Exception
   */
  public Employee[] findByStore(Store store)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsemployeeServer == null)
        init();
      try {
        return cmsemployeeServer.findByStore((Store)store);
      } catch (ConnectException ce) {
        cmsemployeeServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSEmployeeServices");
  }

  /**
   * This method is used to search all commissioned employee for a store
   * @param store Store
   * @return Employee[]
   * @throws Exception
   */
  public Employee[] findCommissionedByStore(Store store)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsemployeeServer == null)
        init();
      try {
        return cmsemployeeServer.findCommissionedByStore((Store)store);
      } catch (ConnectException ce) {
        cmsemployeeServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSEmployeeServices");
  }

  /**
   * This method is used to search all job codes for an employee
   * @return JobCode[]
   * @throws Exception
   */
  public JobCode[] findJobCodes()
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsemployeeServer == null)
        init();
      try {
        return cmsemployeeServer.findJobCodes();
      } catch (ConnectException ce) {
        cmsemployeeServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSEmployeeServices");
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
    for (int x = 0; x < maxTries; x++) {
      if (cmsemployeeServer == null)
        init();
      try {
        return cmsemployeeServer.findJobCode(jobCodeId);
      } catch (ConnectException ce) {
        cmsemployeeServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSEmployeeServices");
  }

  /**
   * This method is used to search auth information of the employee on the
   * basis of employee id and store
   * @param employeeId String
   * @param store Store
   * @return UserAuthInfo
   * @throws Exception
   */
  public UserAuthInfo findAuthInfo(String employeeId, Store store)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsemployeeServer == null)
        init();
      try {
        return cmsemployeeServer.findAuthInfo((String)employeeId, store);
      } catch (ConnectException ce) {
        cmsemployeeServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSEmployeeServices");
  }

  /**
   * This method is used to search auth information for a store
   * @param store Store
   * @return UserAuthInfo[]
   * @throws Exception
   */
  public UserAuthInfo[] findAuthInfo(Store store)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsemployeeServer == null)
        init();
      try {
        return cmsemployeeServer.findAuthInfo((Store)store);
      } catch (ConnectException ce) {
        cmsemployeeServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSEmployeeServices");
  }

  /**
   * This method is used to submit a new or updated employee
   * @param employee Employee
   * @return boolean
   * @throws Exception
   */
  public boolean submit(Employee employee)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsemployeeServer == null)
        init();
      try {
        return cmsemployeeServer.submit((Employee)employee);
      } catch (ConnectException ce) {
        cmsemployeeServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSEmployeeServices");
  }

  /**
   * This method is used to submit a new or updated employee
   * @param employee Employee
   * @return boolean
   * @throws Exception
   */
  public boolean submitLocalEmployee(Employee employee)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsemployeeServer == null)
        init();
      try {
        return cmsemployeeServer.submit((Employee)employee);
      } catch (ConnectException ce) {
        cmsemployeeServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSEmployeeServices");
  }

  /**
   * This method is used to presists employee auth info to data store
   * @param txEmployee TransactionEmployeeAuthInfo
   * @return boolean
   * @throws Exception
   */
  public boolean submitAuthInfo(TransactionEmployeeAuthInfo txEmployee)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsemployeeServer == null)
        init();
      try {
        return cmsemployeeServer.submitAuthInfo((TransactionEmployeeAuthInfo)txEmployee);
      } catch (ConnectException ce) {
        cmsemployeeServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSEmployeeServices");
  }

  /**
   * This method is used to Submit Employee to database and return it
   * @param employee Employee
   * @return Employee
   * @author Vikram Mundhra
   * Created Date: 04/20/2005
   */
  public Employee submitAndGetRemoteEmployee(Employee employee)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsemployeeServer == null)
        init();
      try {
        return cmsemployeeServer.submitAndGetRemoteEmployee(employee);
      } catch (ConnectException ce) {
        cmsemployeeServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSEmployeeServices");
  }
}

