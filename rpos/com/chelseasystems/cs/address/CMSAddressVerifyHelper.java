/*
 * @copyright (c) 2005 Retek. All Rights Reserved.
 */

package com.chelseasystems.cs.address;

import  com.chelseasystems.cr.appmgr.IRepositoryManager;

/*
History:
+------+------------+-----------+-----------+----------------------------------------------------+
| Ver# | Date       | By        | Defect #  | Description                                        |
+------+------------+-----------+-----------+----------------------------------------------------+ 
| 1    | 04-04-2006 |David Fung | PCR 67    | QAS                                                |
+------+------------+-----------+-----------+----------------------------------------------------+
*/

/**
* <p>Title:CMSAddressVerifyHelper.java </p>
*
* <p>Description: CMSAddressVerifyHelper </p>
*
* <p>Copyright: Copyright (c) 2005</p>
*
* <p>Company: Skillnet inc.</p>
*
* @author David Fung
* @version 1.0
*/

/**
 * Static convenience methods to manipulate Client Services.
 */
public class CMSAddressVerifyHelper {

   /**
    * To verify address. 
    * @param request request
    */
   public static Response search (IRepositoryManager theMgr, Request request) throws Exception {
	   System.out.println("Calling CMSAddressVerifyClientServices.search(request)...");
      CMSAddressVerifyClientServices cs = (CMSAddressVerifyClientServices) theMgr.getGlobalObject("ADDRESSVERIFY_SRVC");
      return  cs.search(request);
   }

}
