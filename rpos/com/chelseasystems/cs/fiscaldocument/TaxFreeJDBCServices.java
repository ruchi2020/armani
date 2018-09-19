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
 | 5    | 05-18-2005 | Vikram    | N/A       | updated reissueLoyalty                             |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 05-18-2005 | Vikram    | N/A       | updated to update loyaltyFlag in Customer table    |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 05-10-2005 | Vikram    | N/A       |POS_104665_TS_LoyaltyManagement_Rev0                |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
import  java.util.*;
import  com.chelseasystems.cr.config.ConfigMgr;
import  com.chelseasystems.cs.dataaccess.LoyaltyDAO;
import  com.chelseasystems.cr.logging.LoggingServices;
import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cs.dataaccess.ArmTaxFreeDAO;


//import com.chelseasystems.cs.dataaccess.artsoracle.dao.RedeemableIssueOracleDAO;
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
public class TaxFreeJDBCServices extends TaxFreeServices {
  private ArmTaxFreeDAO taxFreeDAO;

  /**
   * put your documentation comment here
   */
  public TaxFreeJDBCServices () {
    ConfigMgr configMgr = new ConfigMgr("jdbc.cfg");
    taxFreeDAO = (ArmTaxFreeDAO)configMgr.getObject("ARMTAXFREE_DAO");
  }

  /**
   *
   * @throws Exception
   * @param customerId String
   * @return Loyalty[]
   */
  public TaxFree[] findAllTaxFree () throws Exception {
    try {
      return  taxFreeDAO.selectAllTaxFree();
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "selectAllTaxFree", "Exception", "See Exception", LoggingServices.MAJOR, exception);
      throw  exception;
    }
  }

  /**
   * put your documentation comment here
   * @return
   * @exception Exception
   */
  public TaxFree[] findTaxFreeForStore (String storeID) throws Exception {
    try {
      return  taxFreeDAO.selectTaxFreeForStore(storeID);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "selectTaxFreeForStore", "Exception", "See Exception", LoggingServices.MAJOR, exception);
      throw  exception;
    }
  }
}



