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
 | 2    | 06-24-2005 | Megha     | N/A       |Tax Free                                            |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
//import java.rmi.*;
//import java.util.*;
import  com.chelseasystems.cr.node.CMSComponent;
import  java.rmi.RemoteException;
import  java.util.Date;
import  java.util.Properties;
import  com.chelseasystems.cr.logging.LoggingServices;
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
public class TaxFreeRMIServerImpl extends CMSComponent
    implements TaxFreeRMIServer {

  /**
   * put your documentation comment here
   * @param   Properties props
   */
  public TaxFreeRMIServerImpl (Properties props) throws RemoteException
  {
    super(props);
    setImpl();
    init();
  }

  /**
   * Sets the current implementation
   */
  private void setImpl () {
    System.out.println("****setimpl");
    Object obj = getConfigManager().getObject("SERVER_IMPL");
    System.out.println("******* Server obj = " + obj);
    if (null == obj) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "setImpl()", "Could not instantiate SERVER_IMPL.", "Make sure taxfree.cfg contains SERVER_IMPL", LoggingServices.MAJOR);
    }
    TaxFreeServices.setCurrent((TaxFreeServices)obj);
  }

  /**
   * put your documentation comment here
   */
  private void init () {
    System.out.println("Binding to RMIRegistry...");
    String theName = getConfigManager().getString("REMOTE_NAME");
    if (null != theName) {
      bind(theName, this);
    }
    else {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "init()", "Could not find name to bind to in registry.", "Make sure loyalty.cfg contains a RMIREGISTRY entry.", LoggingServices.MAJOR);
    }
  }

  /**
   * Receives callback when the config file changes
   * @param aKey an array of keys that have changed
   */
  protected void configEvent (String[] aKey) {}

  /**
   * ping
   *
   * @return boolean
   * @throws RemoteException
   *
   * Used by the DowntimeManager to determine when this object is available.
   * Just because this process is up doesn't mean that the clients can come up.
   * Make sure that the database is available.
   * @return boolean <code>true</code> indicates that this class is available.
   */
  public boolean ping () throws RemoteException {
    return  true;
  }

  /**
   *
   * @throws RemoteException
   * @param customerId String
   * @return Loyalty[]
   */
  public TaxFree[] findAllTaxFree () throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw  new ConnectException("Service is not available");
      incConnection();
      return  TaxFreeServices.getCurrent().findAllTaxFree();
    } catch (Exception e) {
      e.printStackTrace();
      throw  new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findAllTaxFree", start);
      decConnection();
    }
  }

  /**
   *
   * @throws RemoteException
   * @param customerId String
   * @return Loyalty[]
   */
  public TaxFree[] findTaxFreeForStore (String storeID) throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw  new ConnectException("Service is not available");
      incConnection();
      return  TaxFreeServices.getCurrent().findTaxFreeForStore(storeID);
    } catch (Exception e) {
      e.printStackTrace();
      throw  new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findTaxFreeForStore", start);
      decConnection();
    }
  }
}



