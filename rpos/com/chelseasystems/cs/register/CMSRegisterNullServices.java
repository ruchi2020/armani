/**
 *  History:
 +------+------------+-----------+-----------+-------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                     |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 2    | 04-28-2005 |Megha      |           |  Added new method selectByStoreAndRegID
 |      |            |           |           |   selectByStoreID                               |
 |      |            |           |           |                                                 |
 |      |            |           |           |                                                 |
 -----------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.register;

import java.rmi.ConnectException;
import java.util.*;

import com.chelseasystems.cr.appmgr.DowntimeException;
import com.chelseasystems.cr.register.*;
import com.chelseasystems.cr.store.Store;


/**
 *
 * <p>Title: CMSRegisterNullServices</p>
 *
 * <p>Description: Null Register Services.</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSRegisterNullServices extends CMSRegisterServices {
  static int counter = 100;

  /**
   * In off-line mode you cannot get the next register number
   * @param store Store
   * @return Register
   * @throws Exception
   */
  public Register getNextRegister(Store store)
      throws Exception {
    String id = String.valueOf(counter++);
    return new CMSRegister(id, store.getId());
  }

  /**
   * This method is used to update a Register
   * @param aRegister Register
   * @throws Exception
   */
  public void updateRegister(Register aRegister)
      throws Exception {}

  /**
   * This method is used to select by a RegisterID & StoreID
   * @param aRegister String
   * @param aStore String
   * @return CMSRegister
   * @throws Exception
   */
  public CMSRegister selectByStoreAndRegID(String aRegister, String aStore)
      throws Exception {
    return new CMSRegister(aRegister, aStore);
  }

  /**
   * This method is used to get all   Registers by  StoreID
   * @param aStore String
   * @return CMSRegister[]
   * @throws Exception
   */
  public CMSRegister[] selectByStoreID(String aStore)
      throws Exception {
    return null;
  }
  
  /**
   * Vishal Yevale : Dec 2017 : POS_TS_NF25 Implementation PCR (FRANCE)
   * @param txnObject (CMSTransactionSOS OR CMSTransactionEOD)
   * @return MAP (Calculated  netAmount of Month and Year (if end of year)
   * @throws Exception
   */
  public HashMap submitAndReturnNetAmount(Object txnObject) throws Exception {
		return null;
  }

}

