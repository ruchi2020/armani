/** Copyright 2001,chelseamarketsystem
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

import java.util.HashMap;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cs.register.*;
import com.chelseasystems.cs.store.*;


/**
 *
 * <p>Title: CMSRegisterHelper</p>
 *
 * <p>Description: Static convenience methods to manipulate Client Services.</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSRegisterHelper {

  /**
   * This method used to get the next register
   * @param theAppMgr IRepositoryManager
   * @param store CMSStore
   * @return CMSRegister
   * @throws Exception
   */
  public static CMSRegister getNextRegister(IRepositoryManager theAppMgr, CMSStore store)
      throws Exception {
    CMSRegisterClientServices cs = (CMSRegisterClientServices)theAppMgr.getGlobalObject(
        "REGISTER_SRVC");
    return cs.getNextRegister(store);
  }

  /**
   * This method is used to update a Register
   * @param theAppMgr IRepositoryManager
   * @param register CMSRegister
   * @throws Exception
   */
  public static void updateRegister(IRepositoryManager theAppMgr, CMSRegister register)
      throws Exception {
    CMSRegisterClientServices cs = (CMSRegisterClientServices)theAppMgr.getGlobalObject(
        "REGISTER_SRVC");
    cs.updateRegister(register);
  }

  /**
   * This method is used to select by a RegisterID & StoreID
   * @param theAppMgr IRepositoryManager
   * @param register String
   * @param store String
   * @return CMSRegister
   * @throws Exception
   */
  public static CMSRegister selectByStoreAndRegID(IRepositoryManager theAppMgr, String register
      , String store)
      throws Exception {
    CMSRegister reg = null;
    CMSRegisterClientServices cs = (CMSRegisterClientServices)theAppMgr.getGlobalObject(
        "REGISTER_SRVC");
    reg = cs.selectByStoreAndRegID(register, store);
    return reg;
  }

  /**
   * This method is used to get all   Registers by  StoreID
   * @param theAppMgr IRepositoryManager
   * @param store String
   * @return CMSRegister[]
   * @throws Exception
   */
  public static CMSRegister[] selectByStoreID(IRepositoryManager theAppMgr, String store)
      throws Exception {
    CMSRegister reg[] = null;
    CMSRegisterClientServices cs = (CMSRegisterClientServices)theAppMgr.getGlobalObject(
        "REGISTER_SRVC");
    reg = cs.selectByStoreID(store);
    return reg;
  }
  /**
   * Vishal Yevale : Dec 2017 : POS_TS_NF25 Implementation PCR (FRANCE)
   * @param theAppMgr
   * @param txnObject (CMSTransactionSOS OR CMSTransactionEOD)
   * @return MAP (Calculated  netAmount of Month and Year (if end of year)
   * @throws Exception
   */
  public static HashMap submitAndReturnNetAmount(IApplicationManager theAppMgr, Object txnObject) throws Exception {
	  CMSRegisterClientServices cs = (CMSRegisterClientServices)theAppMgr.getGlobalObject("REGISTER_SRVC");
		return cs.submitAndReturnNetAmount(txnObject);
		
	}
}

