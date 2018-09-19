/*
 * @copyright (c) 2005 Retek. All Rights Reserved.
 */

package  com.chelseasystems.cs.address;

import  com.chelseasystems.cr.config.*;
import  com.chelseasystems.cr.logging.*;
import  com.chelseasystems.cr.appmgr.*;

/*
History:
+------+------------+-----------+-----------+----------------------------------------------------+
| Ver# | Date       | By        | Defect #  | Description                                        |
+------+------------+-----------+-----------+----------------------------------------------------+ 
| 1    | 04-04-2006 |David Fung | PCR 67    | QAS                                                |
+------+------------+-----------+-----------+----------------------------------------------------+
*/

/**
* <p>Title:CMSAddressVerifyClientServices.java </p>
*
* <p>Description: CMSAddressVerifyClientServices </p>
*
* <p>Copyright: Copyright (c) 2005</p>
*
* <p>Company: Skillnet inc.</p>
*
* @author David Fung
* @version 1.0
*/

/**
 * Client-side object for retrieving and submitting.
 */
public class CMSAddressVerifyClientServices extends ClientServices {

   /** Configuration manager **/
   //private ConfigMgr config = null;

   /**
    * Set the current implementation
    */
   public CMSAddressVerifyClientServices() {
      // Set up the configuration manager.
      config = new ConfigMgr("addressverify.cfg");
   }

   /**
    * initialize primary implementation
    */
   public void init(boolean online) throws Exception {
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
    String className = config.getString("CLIENT_IMPL");
    Class serviceClass = Class.forName(className);
    return  serviceClass;
  }

   public void onLineMode() {
      LoggingServices.getCurrent().logMsg("On-Line Mode for CMSAddressVerifyClientServices");
      CMSAddressVerifyServices serviceImpl = (CMSAddressVerifyServices) config.getObject("CLIENT_IMPL");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSAddressVerifyClientServices", "onLineMode()",
             "Cannot instantiate the class that provides the"
             + "implementation of CMSAddressVerifyServices in addressverify.cfg.", 
             "Make sure that addressverify.cfg contains an entry with "
             +"a key of CLIENT_IMPL and a value that is the name of a class "
             +"that provides a concrete implementation of CMSAddressVerifyServices.",
             LoggingServices.MAJOR);
         setOffLineMode();
         return;
      }
      CMSAddressVerifyServices.setCurrent(serviceImpl);
   }

   public void offLineMode() {
      LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSAddressVerifyClientServices");
      CMSAddressVerifyServices serviceImpl = (CMSAddressVerifyServices) config.getObject("CLIENT_DOWNTIME");
      if (null == serviceImpl) {
         LoggingServices.getCurrent().logMsg("CMSAddressVerifyClientServices", "offLineMode()",
             "Cannot instantiate the class that provides the"
             + " implementation of CMSAddressVerifyServices in addressverify.cfg.",
             "Make sure that addressverify.cfg contains an entry with "
             + "a key of CLIENT_DOWNTIME and a value"
             + " that is the name of a class that provides a concrete"
             + " implementation of CMSAddressVerifyServices.",
             LoggingServices.CRITICAL);
      }
      CMSAddressVerifyServices.setCurrent(serviceImpl);
   }

      public Object getCurrentService () {
         return  CMSAddressVerifyServices.getCurrent();
      }

   /**
    * To verify address. 
    * @param request request
    */
   public Response search (Request request) throws Exception {	   	  
      try {
         this.fireWorkInProgressEvent(true);
         System.out.println("Calling CMSAddressVerifyServices.search(request)...");
         return (Response) CMSAddressVerifyServices.getCurrent().search(request);
      } catch (DowntimeException ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(),"search(Request)",
            "Primary Implementation for CMSAddressVerifyServices failed, going Off-Line...",
            "See Exception", LoggingServices.MAJOR, ex);
         offLineMode();
         setOffLineMode();
         return (Response) CMSAddressVerifyServices.getCurrent().search(request);
      } finally {
         this.fireWorkInProgressEvent(false);
      }
   }

}
