/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 04-08-2005 | Anand     | N/A       |1.launching external applications CRM and     |
 |      |            |           |           | E-mail(NEW FILE)                             |
 +------+------------+-----------+-----------+----------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.customer;

import java.util.*;
import java.io.*;
import com.chelseasystems.cr.config.*;


/**
 *
 * <p>Title: LaunchExternalApp</p>
 *
 * <p>Description: This class is used to launch external applications like
 * CRM and E-mail</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class LaunchExternalApp {
  private ConfigMgr config;
  private String strExtApp;
  private String sScript;
  private String scriptToLaunch;

  /**
   * The rules that launch external applications, CRM and E-MAIL.
   * @param paramList String[]
   */
  public void launchApp(String paramList[]) {
    config = new ConfigMgr("customer.cfg");
    try {
      if (paramList[0].equalsIgnoreCase("LAUNCH_EMAIL")) {
        scriptToLaunch = config.getString("EMAIL_SCRIPT");
      } else {
        if (paramList[0].equalsIgnoreCase("LAUNCH_CRM")) {
          scriptToLaunch = config.getString("CRM_SCRIPT");
        }
      }
      String[] cmd = null;
      //if no arguments are passed, then the array will consist of only cmd.exe,/c and scriptToLaunch and will be independent of paramlist
      // else below code makes provision to take in all the parameters passed to it
      if (paramList.length > 1) {
        cmd = new String[paramList.length + 2];
      } else {
        cmd = new String[3];
      }
      cmd[0] = "cmd.exe";
      cmd[1] = "/C";
      cmd[2] = scriptToLaunch;
      //the following code has been added to pass parameters dynamically.
      if (paramList.length > 1) {
        //paramlist.length -1 because the first parameter passed is the sAction and we need to discount that from our p'meter list
        for (int i = 0; i < paramList.length - 1; i++) {
          //the fourth element of the cmd array will be the second element(or the first parameter) of the paramList array
          cmd[i
              + 3] = paramList[i + 1]; /*paramList[i+1] because the firstElement will be LAUNCH_EMAIL or LAUNCH_CRM...The actual
                     parameters start from the 2nd position in the array.*/

        }
      }
      Runtime rt = Runtime.getRuntime();
      Process proc = rt.exec(cmd);
      int exitVal = proc.waitFor();
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }
}

