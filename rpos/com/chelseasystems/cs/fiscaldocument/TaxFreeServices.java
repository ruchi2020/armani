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
import  java.util.Date;
import  com.chelseasystems.cr.config.ConfigMgr;


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
public abstract class TaxFreeServices {
  private static TaxFreeServices current = null;

  /**
   *
   * @return LoyaltyServices
   */
  public static TaxFreeServices getCurrent () {
    if (null == current) {
      System.out.println("Automatically setting current implementation of TaxFreeServices.");
      ConfigMgr config = new ConfigMgr("taxfree.cfg");
      Object obj = config.getObject("TAX_FREE_SERVICES_IMPL");
      current = (TaxFreeServices)obj;
    }
    return  current;
  }

  /**
   *
   * @param aService LoyaltyServices
   */
  public static void setCurrent (TaxFreeServices aService) {
    System.out.println("Setting current implementation of TaxFreeServices.");
    current = aService;
  }

  /**
   * put your documentation comment here
   * @return
   * @exception Exception
   */
  public abstract TaxFree[] findAllTaxFree () throws Exception;

  /**
   * put your documentation comment here
   * @return
   * @exception Exception
   */
  public abstract TaxFree[] findTaxFreeForStore (String storeID) throws Exception;
}



