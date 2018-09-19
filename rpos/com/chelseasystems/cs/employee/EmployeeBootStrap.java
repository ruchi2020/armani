//
// Copyright 1999-2001, Chelsea Market Systems
//
package com.chelseasystems.cs.employee;

import java.awt.*;
import java.util.*;
import java.io.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.employee.*;
import com.chelseasystems.cr.appmgr.bootstrap.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cs.xml.*;
import com.chelseasystems.cr.logging.*;

public class EmployeeBootStrap implements IBootStrap {

  private BootStrapManager bootMgr;
  private IBrowserManager theMgr;

  public EmployeeBootStrap() {
  }

  public String getName() {
     return "EmployeeBootStrap";
  }

   /**
    * returns the long description of the bootstrap
    * @return String the long description of the bootstrap
    */
  public String getDesc() {
     return "This bootstrap determines if the employee file needs to be downloaded.";
  }

  public BootStrapInfo start(IBrowserManager theMgr, Window parentFrame, BootStrapManager bootMgr) {
    try {
      this.bootMgr = bootMgr;
      this.theMgr = theMgr;
      // check to make sure that employee.xml exists and its not in the backup
      File fileEmployee = new File(FileMgr.getLocalFile("xml", "employees.xml"));
      if (!fileEmployee.exists()) {
        File fileBackup = new File(FileMgr.getLocalFile("xml", "employees.bkup"));
        if (fileBackup.exists()) {
         fileBackup.renameTo(fileEmployee);
        } else { // no files, so delete date from last employee download
          File fileDate = new File(FileMgr.getLocalFile("repository", "EMPLOYEE_DOWNLOAD_DATE"));
          fileDate.delete();
        }
      }

      if (!theMgr.isOnLine()) return new BootStrapInfo(this.getClass().getName());

      bootMgr.setBootStrapStatus("Checking employee download date...");
      Date date = (Date) theMgr.getGlobalObject("EMPLOYEE_DOWNLOAD_DATE");
//    PCR from Japan issue # 1913 Download timing to register
//    Code commented     
//      if (date == null || DateUtil.isDateAtLeastHoursOld(date, 12)){
//      end
//      ADDED new check for the same day
      if (date == null || checkDownloadDate(date)){
    	 // downloadFile();
      }

    } catch (Exception ex) {
      System.out.println("Exception EmployeeBootStrap.start()->" + ex);
      ex.printStackTrace();
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "start", "Exception", "See Exception", LoggingServices.MAJOR, ex);
    }

    return new BootStrapInfo(this.getClass().getName());
  }

  private void downloadFile() throws Exception {
    try {
      File backup = new File(FileMgr.getLocalFile("xml", "employees.bkup"));
      backup.delete();
      File employeeFile = new File(FileMgr.getLocalFile("xml", "employees.xml"));
      employeeFile.renameTo(backup);
      employeeFile.delete();

      Store store = (Store) theMgr.getGlobalObject("STORE");

      ConfigMgr config = new ConfigMgr("employee.cfg");
      CMSEmployeeServices employeeDownloadServices = (CMSEmployeeServices) config.getObject("CLIENT_DOWNLOAD_IMPL");
      bootMgr.setBootStrapStatus("Downloading employee file...");

      Employee[] employees = employeeDownloadServices.findByStore(store);
      new EmployeeXML().writToFile(employeeFile.getAbsolutePath(), employees);
      theMgr.addGlobalObject("EMPLOYEE_DOWNLOAD_DATE", new java.util.Date(), true);
      CMSEmployeeFileServices employeeFileServices = (CMSEmployeeFileServices) config.getObject("CLIENT_LOCAL_IMPL");
      System.out.println("LOADING ALL EMPLOYEES FROM EMPLOYEE BOOTSTRAP INTO FILE SERVICES...");
      employeeFileServices.loadAllEmployeesFromFile();
    }
    catch (Exception ex) {
      try {
        File employeeFile = new File(FileMgr.getLocalFile("xml", "employees.xml"));
        employeeFile.delete();
        File backup = new File(FileMgr.getLocalFile("xml", "employees.bkup"));
        backup.renameTo(employeeFile);
      } catch (Exception ex1) {}
      System.out.println("Exception downloadFile()->" + ex);
      ex.printStackTrace();
    }
    finally {
      if (theMgr instanceof IApplicationManager)
         ((IApplicationManager)theMgr).closeStatusDlg();
    }
  }
  
//# 1913 Download timing to register
  private boolean checkDownloadDate(Date d){
	  Date businessDate = (Date)theMgr.getGlobalObject("PROCESS_DATE");
	  if("JP".equalsIgnoreCase(Version.CURRENT_REGION)){
		    return (!DateUtil.isSameDay(d,businessDate));
	  }
	  else{
		  return (DateUtil.isDate24HourOld(d));
	  }
  }

}
