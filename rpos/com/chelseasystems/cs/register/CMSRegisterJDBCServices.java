/*
 * @copyright (c) 1998-2002 Retek Inc
 History:
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

import java.util.*;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.register.*;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cs.dataaccess.*;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.ArmEopDtlOracleDAO;
import com.chelseasystems.cr.logging.*;


/**
 *
 * <p>Title: CMSRegisterJDBCServices</p>
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
public class CMSRegisterJDBCServices extends CMSRegisterServices {
  private RegisterDAO registerDAO;
  static private String counter101 = "101";

  /**
   * Default Constructor
   */
  public CMSRegisterJDBCServices() {
    ConfigMgr configMgr = new ConfigMgr("jdbc.cfg");
    registerDAO = (RegisterDAO)configMgr.getObject("REGISTER_DAO");
  }

  /**
   * This method used to get the next register
   * @param store
   * @return
   * @exception Exception
   */
  public Register getNextRegister(Store store)
      throws Exception {
    try {
      return registerDAO.getNextRegister(store.getId(), counter101);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getNextRegister", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * This method is used to update a Register
   * @param aRegister
   * @exception Exception
   */
  public void updateRegister(Register aRegister)
      throws Exception {
    try {
      registerDAO.update(aRegister);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "updateRegister", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * This method is used to select by a RegisterID & StoreID
   * @param aRegister
   * @exception Exception
   */
  public CMSRegister selectByStoreAndRegID(String aRegister, String aStore)
      throws Exception {
    CMSRegister reg = null;
    try {
      reg = registerDAO.selectByStoreAndRegID(aRegister, aStore);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "selectByStoreAndRegID"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
    return reg;
  }

  /**
   * This method is used to get all   Registers by  StoreID
   * @param aRegister
   * @exception Exception
   */
  public CMSRegister[] selectByStoreID(String aStore)
      throws Exception {
    CMSRegister reg[] = null;
    //CMSRegister regTemp[] = null;
    try {
      reg = registerDAO.selectByStoreId(aStore);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "selectByStoreID", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
    return reg;
  }
  
  /**
   * Vishal Yevale : Dec 2017 : POS_TS_NF25 Implementation PCR (FRANCE)
   * @param txnObject (CMSTransactionSOS OR CMSTransactionEOD)
   * @return MAP (Calculated  netAmount of Month and Year (if end of year)
   * @throws Exception
   */
  public HashMap submitAndReturnNetAmount(Object txnObject) throws Exception {
	    HashMap map = null;
	    try {
	    	ArmEopDtlDAO armEopDtlDAO = new ArmEopDtlOracleDAO();
	      map = armEopDtlDAO.submitAndReturnNetAmount(txnObject);
	    } catch (Exception exception) {
	      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "selectByStoreAndRegID"
	          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
	      throw exception;
	    }
	    return map;
	}
}

