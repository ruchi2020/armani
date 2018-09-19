/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.fiscaldocument;

/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 06-24-2005 | Megha     | N/A       | TaxFreeRMIClient                                   |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
import  com.chelseasystems.cr.node.IRemoteServerClient;
import  com.chelseasystems.cr.config.ConfigMgr;
import  java.rmi.RMISecurityManager;
import  com.chelseasystems.cr.appmgr.DowntimeException;
import  com.chelseasystems.cr.logging.LoggingServices;
import  com.chelseasystems.cr.config.NetworkMgr;
import  com.igray.naming.NamingService;
import  com.chelseasystems.cr.node.ICMSComponent;
import  java.rmi.ConnectException;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class TaxFreeRMIClient extends TaxFreeServices
    implements IRemoteServerClient {

  /** The configuration manager */
  private ConfigMgr config = null;

  /** The reference to the remote implementation of the service. */
  private TaxFreeRMIServer taxFreeServer = null;

  /** The maximum number of times to try to establish a connection to the RMIServerImpl */
  private int maxTries = 1;

  /**
   * Set the configuration manager and make sure that the system has a
   * security manager set.
   */
  public TaxFreeRMIClient () throws DowntimeException
  {
    config = new ConfigMgr("taxfree.cfg");
    if (System.getSecurityManager() == null)
      System.setSecurityManager(new RMISecurityManager());
    init();
  }

  /**
   * Get the remote interface from the registry.
   */
  private void init () throws DowntimeException {
    try {
      this.lookup();
      System.out.println("TaxFreeRMIClient Lookup: Complete");
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "init()", "Cannot establish connection to RMI server.", "Make sure that the server is registered on the remote server" + " and that the name of the remote server and remote service are" + " correct in the taxfree.cfg file.", LoggingServices.MAJOR,
          e);
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
    System.out.println("***** connect = " + connect);
    taxFreeServer = (TaxFreeRMIServer)NamingService.lookup(connect);
    System.out.println("***** taxFreeServer =" + taxFreeServer);
  }

  /**
   * @see com.chelseasystems.cr.node.ICMSComponent
   * @return  <true> is component is available
   */
  public boolean isRemoteServerAvailable () {
    try {
      return  ((ICMSComponent)this.taxFreeServer).isAvailable();
    } catch (Exception ex) {
      return  false;
    }
  }

  /**
   *
   * @throws Exception
   * @param
   * @return TaxFree[]
   */
  public TaxFree[] findAllTaxFree () throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (taxFreeServer == null)
        init();
      try {
        return  taxFreeServer.findAllTaxFree();
      } catch (ConnectException ce) {
        taxFreeServer = null;
      } catch (Exception ex) {
        throw  new DowntimeException(ex.getMessage());
      }
    }
    throw  new DowntimeException("Unable to establish connection to TaxFreeServices");
  }

  /**
   *
   * @throws Exception
   * @param
   * @return TaxFree[]
   */
  public TaxFree[] findTaxFreeForStore (String storeID) throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (taxFreeServer == null)
        init();
      try {
        return  taxFreeServer.findTaxFreeForStore(storeID);
      } catch (ConnectException ce) {
        taxFreeServer = null;
      } catch (Exception ex) {
        throw  new DowntimeException(ex.getMessage());
      }
    }
    throw  new DowntimeException("Unable to establish connection to TaxFreeServices");
  }
}



