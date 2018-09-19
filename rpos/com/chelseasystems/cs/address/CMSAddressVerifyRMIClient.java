/*
 * @copyright (c) 2005 Retek. All Rights Reserved.
 */

package  com.chelseasystems.cs.address;

import  com.chelseasystems.cr.appmgr.*;
import  com.chelseasystems.cr.config.*;
import  com.chelseasystems.cr.logging.*;
import  com.chelseasystems.cr.node.IRemoteServerClient;
import  com.chelseasystems.cr.node.ICMSComponent;
import  com.igray.naming.*;
import  java.rmi.*;

/*
History:
+------+------------+-----------+-----------+----------------------------------------------------+
| Ver# | Date       | By        | Defect #  | Description                                        |
+------+------------+-----------+-----------+----------------------------------------------------+ 
| 1    | 04-04-2006 |David Fung | PCR 67    | QAS                                                |
+------+------------+-----------+-----------+----------------------------------------------------+
*/

/**
* <p>Title:CMSAddressVerifyRMIClient.java </p>
*
* <p>Description: CMSAddressVerifyRMIClient </p>
*
* <p>Copyright: Copyright (c) 2005</p>
*
* <p>Company: Skillnet inc.</p>
*
* @author David Fung
* @version 1.0
*/

/**
 * The client-side of an RMI connection for fetching/submitting.
 */
public class CMSAddressVerifyRMIClient extends CMSAddressVerifyServices implements IRemoteServerClient {

   /** The configuration manager */
   private ConfigMgr config = null;

   /** The reference to the remote implementation of the service. */
   private ICMSAddressVerifyRMIServer cmsaddressverifyServer = null;

   /** The maximum number of times to try to establish a connection to the RMIServerImpl */
   private int maxTries = 1;

   /**
    * Set the configuration manager and make sure that the system has a
    * security manager set.
    */
   public CMSAddressVerifyRMIClient() throws DowntimeException {
      config = new ConfigMgr("addressverify.cfg");
      if (System.getSecurityManager() == null)
          System.setSecurityManager(new RMISecurityManager());
      init();
   }

   /**
    * Get the remote interface from the registry.
    */
   private void init() throws DowntimeException {
      try {
         this.lookup();
         System.out.println("CMSAddressVerifyRMIClient Lookup: Complete");
      } catch (Exception e) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "init()",
            "Cannot establish connection to RMI server.",
            "Make sure that the server is registered on the remote server" +
            " and that the name of the remote server and remote service are" +
            " correct in the addressverify.cfg file.",
            LoggingServices.MAJOR, e);
         throw  new DowntimeException(e.getMessage());
      }
   }

   /**
    * Perform lookup of remote server.
    * @exception Exception
    */
   public void lookup () throws Exception {
      NetworkMgr mgr = new NetworkMgr("network.cfg");
      maxTries = mgr.getRetryAttempts();
      String connect = mgr.getRMIMasterNode() + config.getString("REMOTE_NAME") + mgr.getQuery();
      cmsaddressverifyServer = (ICMSAddressVerifyRMIServer) NamingService.lookup(connect);
   }

   /**
    * @see com.chelseasystems.cr.node.ICMSComponent
    * @return  <true> is component is available
    */
   public boolean isRemoteServerAvailable () {
      try {
         return  ((ICMSComponent)this.cmsaddressverifyServer).isAvailable();
      } catch (Exception ex) {
         return  false;
      }
   }

   /**
    * To verify address. 
    * @param request request
    */
   public Response search (Request request) throws Exception {
      for (int x = 0; x < maxTries; x++) {
         if (cmsaddressverifyServer == null)
            init();
         try {
            return cmsaddressverifyServer.search((Request) request);
         } catch (ConnectException ce) {
            cmsaddressverifyServer = null;
         } catch (Exception ex) {
            throw new DowntimeException(ex.getMessage());
         }
      }
      throw new DowntimeException("Unable to establish connection to CMSAddressVerifyServices");
   }

}
