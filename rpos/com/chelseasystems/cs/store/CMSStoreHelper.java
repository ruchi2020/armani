/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.store;

import java.util.Date;

import com.chelseasystems.cr.appmgr.IRepositoryManager;
import com.chelseasystems.cr.store.*;


/**
 *
 * <p>Title: CMSStoreHelper</p>
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
public class CMSStoreHelper {

  /**
   * search a store by its id
   * @param theAppMgr IRepositoryManager
   * @param storeId String
   * @return CMSStore
   * @throws Exception
   */
  public static CMSStore findById(IRepositoryManager theAppMgr, String storeId)
      throws Exception {
    CMSStoreClientServices cs = (CMSStoreClientServices)theAppMgr.getGlobalObject("STORE_SRVC");
    return cs.findById(storeId);
  }
  
  /**Poonam 
   * Added for Expiry_DATE issue on Nov 22,2016 
   * @param theAppMgr IRepositoryManager
   * @param storeId String
   * @return CMSStore
   * @throws Exception
   */
  public static CMSStore findByStoreId(IRepositoryManager theAppMgr, String storeId,java.sql.Date process_date)
      throws Exception {
    CMSStoreClientServices cs = (CMSStoreClientServices)theAppMgr.getGlobalObject("STORE_SRVC");
    return cs.findByStoreId(storeId,process_date);
  }
  //ends here

  /**
   * search store for a city
   * @param theAppMgr IRepositoryManager
   * @param city String
   * @return CMSStore[]
   * @throws Exception
   */
  public static CMSStore[] findByCity(IRepositoryManager theAppMgr, String city)
      throws Exception {
    CMSStoreClientServices cs = (CMSStoreClientServices)theAppMgr.getGlobalObject("STORE_SRVC");
    return cs.findByCity(city);
  }

  /**
   * search store form a given state
   * @param theAppMgr IRepositoryManager
   * @param state String
   * @return CMSStore[]
   * @throws Exception
   */
  public static CMSStore[] findByState(IRepositoryManager theAppMgr, String state)
      throws Exception {
    CMSStoreClientServices cs = (CMSStoreClientServices)theAppMgr.getGlobalObject("STORE_SRVC");
    return cs.findByState(state);
  }

  /**
   * search stores for the given state and city
   * @param theAppMgr IRepositoryManager
   * @param city String
   * @param state String
   * @return CMSStore[]
   * @throws Exception
   */
  public static CMSStore[] findByCityAndState(IRepositoryManager theAppMgr, String city
      , String state)
      throws Exception {
    CMSStoreClientServices cs = (CMSStoreClientServices)theAppMgr.getGlobalObject("STORE_SRVC");
    return cs.findByCityAndState(city, state);
  }

  /**
   *
   * @param theAppMgr IRepositoryManager
   * @return CMSStore[]
   * @throws Exception
   */
  public static CMSStore[] findAll(IRepositoryManager theAppMgr)
      throws Exception {
    CMSStoreClientServices cs = (CMSStoreClientServices)theAppMgr.getGlobalObject("STORE_SRVC");
    return cs.findAll();
  }

  /**
   *
   * @param theAppMgr IRepositoryManager
   * @return CMSStore[]
   * @throws Exception
   */
  public static CMSStore[] findAllStores(IRepositoryManager theAppMgr)
      throws Exception {
    CMSStoreClientServices cs = (CMSStoreClientServices)theAppMgr.getGlobalObject("STORE_SRVC");
    return cs.findAllStores();
  }

  /**
   * find stroes ids for the city
   * @param theAppMgr IRepositoryManager
   * @param city String
   * @return String[]
   * @throws Exception
   */
  public static String[] findIdsByCity(IRepositoryManager theAppMgr, String city)
      throws Exception {
    CMSStoreClientServices cs = (CMSStoreClientServices)theAppMgr.getGlobalObject("STORE_SRVC");
    return cs.findIdsByCity(city);
  }

  /**
   * find stores ids for the state
   * @param theAppMgr IRepositoryManager
   * @param state String
   * @return String[]
   * @throws Exception
   */
  public static String[] findIdsByState(IRepositoryManager theAppMgr, String state)
      throws Exception {
    CMSStoreClientServices cs = (CMSStoreClientServices)theAppMgr.getGlobalObject("STORE_SRVC");
    return cs.findIdsByState(state);
  }

  /**
   * find store ids in the city and state.
   * @param theAppMgr IRepositoryManager
   * @param city String
   * @param state String
   * @return String[]
   * @throws Exception
   */
  public static String[] findIdsByCityAndState(IRepositoryManager theAppMgr, String city
      , String state)
      throws Exception {
    CMSStoreClientServices cs = (CMSStoreClientServices)theAppMgr.getGlobalObject("STORE_SRVC");
    return cs.findIdsByCityAndState(city, state);
  }

  /**
   *
   * @param theAppMgr IRepositoryManager
   * @return String[]
   * @throws Exception
   */
  public static String[] findAllIds(IRepositoryManager theAppMgr)
      throws Exception {
    CMSStoreClientServices cs = (CMSStoreClientServices)theAppMgr.getGlobalObject("STORE_SRVC");
    return cs.findAllIds();
  }
}

