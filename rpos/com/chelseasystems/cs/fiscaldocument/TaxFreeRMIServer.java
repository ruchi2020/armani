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
 | 2    | 06-24-2005 | Megha     | N/A       | Tax Free                                           |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
import  java.rmi.Remote;
import  com.igray.naming.IPing;
import  java.rmi.RemoteException;
import  java.util.Date;


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
public interface TaxFreeRMIServer extends Remote, IPing
{

  /**
   * put your documentation comment here
   * @return
   * @exception RemoteException
   */
  public abstract TaxFree[] findAllTaxFree () throws RemoteException;

  /**
   * put your documentation comment here
   * @return
   * @exception RemoteException
   */
  public abstract TaxFree[] findTaxFreeForStore (String storeID) throws RemoteException;
}



