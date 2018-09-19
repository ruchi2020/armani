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
import  com.chelseasystems.cr.appmgr.ClientServices;
import  com.chelseasystems.cr.config.ConfigMgr;
import  com.chelseasystems.cr.logging.LoggingServices;
import  java.util.Date;
import  com.chelseasystems.cr.appmgr.DowntimeException;


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
public class TaxFreeClientServices extends ClientServices {

  /** Configuration manager **/
  //private ConfigMgr config = null;

  /**
   * Set the current implementation
   */
  public TaxFreeClientServices () {
    // Set up the configuration manager.
    config = new ConfigMgr("taxfree.cfg");
  }

  /**
   * initialize primary implementation
   */
  public void init (boolean online) throws Exception {
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
    String className = config.getString("CLIENT_DOWNLOAD_IMPL");
    Class serviceClass = Class.forName(className);
    return  serviceClass;
  }

  /**
   *
   */
  public void onLineMode () {
    LoggingServices.getCurrent().logMsg("On-Line Mode for TaxFreeClientServices");
    TaxFreeServices serviceImpl = (TaxFreeServices)config.getObject("CLIENT_IMPL");
    System.out.println("***** Service Name = " + serviceImpl);
    if (null == serviceImpl) {
      LoggingServices.getCurrent().logMsg("TaxFreeClientServices", "onLineMode()", "Cannot instantiate the class that provides the" + " implementation of TaxFreeServices in taxfree.cfg.", "Make sure that taxfree.cfg contains an entry with " + "a key of CLIENT_IMPL and a value that is the name of a class "
          + "that provides a concrete implementation of TaxFreeServices.", LoggingServices.MAJOR);
      setOffLineMode();
      return;
    }
    TaxFreeServices.setCurrent(serviceImpl);
  }

  /**
   *
   */
  public void offLineMode () {
    LoggingServices.getCurrent().logMsg("Off-Line Mode for TaxFreeClientServices");
    TaxFreeServices serviceImpl = (TaxFreeServices)config.getObject("CLIENT_DOWNTIME");
    if (null == serviceImpl) {
      LoggingServices.getCurrent().logMsg("TaxFreeClientServices", "offLineMode()", "Cannot instantiate the class that provides the" + " implementation of LoyaltyServices in taxfree.cfg.", "Make sure that taxfree.cfg contains an entry with " + "a key of CLIENT_DOWNTIME and a value" + " that is the name of a class that provides a concrete"
          + " implementation of TaxFreeServices.", LoggingServices.CRITICAL);
    }
    TaxFreeServices.setCurrent(serviceImpl);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Object getCurrentService () {
    return  TaxFreeServices.getCurrent();
  }

  /**
   *
   * @throws Exception
   * @param customerId String
   */
  public TaxFree[] findAllTaxFree () throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return  ((TaxFreeServices)TaxFreeServices.getCurrent()).findAllTaxFree();
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findAllTaxFree", "Primary Implementation for TaxFreeServices failed, going Off-Line...", "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return  ((TaxFreeServices)TaxFreeServices.getCurrent()).findAllTaxFree();
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   *
   * @throws Exception
   * @param storeID String
   */
  public TaxFree[] findTaxFreeForStore (String storeID) throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return  ((TaxFreeServices)TaxFreeServices.getCurrent()).findTaxFreeForStore(storeID);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findAllTaxFree", "Primary Implementation for TaxFreeServices failed, going Off-Line...", "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return  ((TaxFreeServices)TaxFreeServices.getCurrent()).findTaxFreeForStore(storeID);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }
}



