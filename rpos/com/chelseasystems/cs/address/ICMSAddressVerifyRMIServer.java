/*
 * @copyright (c) 2005 Retek. All Rights Reserved.
 */

package  com.chelseasystems.cs.address;

import  java.rmi.*;
import  com.igray.naming.*;

/*
History:
+------+------------+-----------+-----------+----------------------------------------------------+
| Ver# | Date       | By        | Defect #  | Description                                        |
+------+------------+-----------+-----------+----------------------------------------------------+ 
| 1    | 04-04-2006 |David Fung | PCR 67    | QAS                                                |
+------+------------+-----------+-----------+----------------------------------------------------+
*/

/**
* <p>Title:ICMSAddressVerifyRMIServer.java </p>
*
* <p>Description: ICMSAddressVerifyRMIServer </p>
*
* <p>Copyright: Copyright (c) 2005</p>
*
* <p>Company: Skillnet inc.</p>
*
* @author David Fung
* @version 1.0
*/

/**
 * Defines the customer services that are available remotely via RMI.
 */
public interface ICMSAddressVerifyRMIServer extends Remote, IPing {

   /**
    * To verify address. 
    * @param request request
    */
   public Response search (Request request) throws RemoteException;

}
