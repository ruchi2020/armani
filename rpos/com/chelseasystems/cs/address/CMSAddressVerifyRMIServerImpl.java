/*
 * @copyright (c) 2005 Retek. All Rights Reserved.
 */

package  com.chelseasystems.cs.address;

import  com.chelseasystems.cr.logging.*;
import  com.chelseasystems.cr.node.*;
import  java.rmi.*;
import  java.util.*;

/*
History:
+------+------------+-----------+-----------+----------------------------------------------------+
| Ver# | Date       | By        | Defect #  | Description                                        |
+------+------------+-----------+-----------+----------------------------------------------------+ 
| 1    | 04-04-2006 |David Fung | PCR 67    | QAS                                                |
+------+------------+-----------+-----------+----------------------------------------------------+
*/

/**
* <p>Title:CMSAddressVerifyRMIServerImpl.java </p>
*
* <p>Description: CMSAddressVerifyRMIServerImpl </p>
*
* <p>Copyright: Copyright (c) 2005</p>
*
* <p>Company: Skillnet inc.</p>
*
* @author David Fung
* @version 1.0
*/

/**
 * This is the server side of the RMI connection used for fetching/submitting 
 * information.  This class delgates all method calls to the object referenced
 * by the return value from CMSAddressVerifyServices.getCurrent().
 */
public class CMSAddressVerifyRMIServerImpl extends CMSComponent implements ICMSAddressVerifyRMIServer{

   public CMSAddressVerifyRMIServerImpl(Properties props) throws RemoteException {
      super(props);
      setImpl();
      init();
   }

   /**
    * Sets the current implementation
    */
   private void setImpl() {
      Object obj = getConfigManager().getObject("SERVER_IMPL");
      if (null == obj) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "setImpl()",
            "Could not instantiate SERVER_IMPL.",
            "Make sure addressverify.cfg contains SERVER_IMPL",
            LoggingServices.MAJOR);
      }
      CMSAddressVerifyServices.setCurrent((CMSAddressVerifyServices) obj);
   }

   private void init() {
      System.out.println("Binding to RMIRegistry...");
      String theName = getConfigManager().getString("REMOTE_NAME");
      if (null != theName) {
         bind(theName, this);
      } else {
        LoggingServices.getCurrent().logMsg(getClass().getName(), "init()",
           "Could not find name to bind to in registry.",
           "Make sure addressverify.cfg contains a REMOTE_NAME entry.",
           LoggingServices.MAJOR);
      }
   }

   /**
    * Receives callback when the config file changes
    * @param aKey an array of keys that have changed
    */
   protected void configEvent(String[] aKey) {
   }

   /**
    * Used by the DowntimeManager to determine when this object is available.
    * Just because this process is up doesn't mean that the clients can come up.
    * Make sure that the database is available.
    * @return boolean <code>true</code> indicates that this class is available.
    */
   public boolean ping() throws RemoteException {
      return true;
   }

   /**
    * To verify address. 
    * @param request request
    */
   public Response search (Request request) throws RemoteException {
      long start = getStartTime();
      try {
         if (!isAvailable())
            throw  new ConnectException("Service is not available");
         incConnection();
         return (Response) CMSAddressVerifyServices.getCurrent().search(request);
      } catch (Exception e) {
         throw new RemoteException(e.getMessage(), e);
      } finally {
         addPerformance("search(Request)", start);
         decConnection();
      }
   }

}
