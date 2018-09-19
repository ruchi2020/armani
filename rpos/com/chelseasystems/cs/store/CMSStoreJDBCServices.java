/*
 * @copyright (c) 1998-2002 Retek Inc
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.store;

import java.util.Date;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.employee.*;
import com.chelseasystems.cs.dataaccess.*;
import com.chelseasystems.cr.logging.*;



/**
 *
 * <p>Title: CMSStoreJDBCServices</p>
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
public class CMSStoreJDBCServices extends CMSStoreServices {
  private StoreDAO storeDAO;

  /**
   * Default Constructor
   */
  public CMSStoreJDBCServices() {
    ConfigMgr configMgr = new ConfigMgr("jdbc.cfg");
    storeDAO = (StoreDAO)configMgr.getObject("STORE_DAO");
  }

  /**
   * search a store by its id
   * @param storeId
   * @return
   * @exception Exception
   */
  public Store findById(String storeId)
      throws Exception {
    try {
      return storeDAO.selectById(storeId);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findById", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }
  
  /**Poonam S.
   * Added for Expiry_DATE issue on Nov 22,2016 
   * @param storeId
   * @return
   * @exception Exception
   */
  public Store findByStoreId(String storeId,java.sql.Date process_dt)
      throws Exception {
    try {
      return storeDAO.selectByStoreId(storeId,process_dt);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByStoreId", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }
  //ends here

  /**
   * search store for a city
   * @param city
   * @return
   * @exception Exception
   */
  public Store[] findByCity(String city)
      throws Exception {
    try {
      return storeDAO.selectByCity(city);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByCity", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * search store form a given state
   * @param state
   * @return
   * @exception Exception
   */
  public Store[] findByState(String state)
      throws Exception {
    try {
      return storeDAO.selectByState(state);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByState", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * search stores for the given state and city
   * @param city
   * @param state
   * @return
   * @exception Exception
   */
  public Store[] findByCityAndState(String city, String state)
      throws Exception {
    try {
      return storeDAO.selectByCityAndState(city, state);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByCityAndState"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   *
   * @return
   * @exception Exception
   */
  public Store[] findAll()
      throws Exception {
    try {
      return storeDAO.selectAll();
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findAll", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   *
   * @return CMSStore[]
   * @throws Exception
   */
  public CMSStore[] findAllStores()
      throws Exception {
    try {
      return storeDAO.selectAllStores();
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findAll", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * find stroes ids for the city
   * @param city
   * @return
   * @exception Exception
   */
  public String[] findIdsByCity(String city)
      throws Exception {
    try {
      return getStoreIds(storeDAO.selectByCity(city));
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findIdsByCity", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * find stores ids for the state
   * @param state
   * @return
   * @exception Exception
   */
  public String[] findIdsByState(String state)
      throws Exception {
    try {
      return getStoreIds(storeDAO.selectByState(state));
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findIdsByState", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * find store ids in the city and state.
   * @param city
   * @param state
   * @return
   * @exception Exception
   */
  public String[] findIdsByCityAndState(String city, String state)
      throws Exception {
    try {
      return getStoreIds(storeDAO.selectByCityAndState(city, state));
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findIdsByCityAndState"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   *
   * @return
   * @exception Exception
   */
  public String[] findAllIds()
      throws Exception {
    try {
      return getStoreIds(storeDAO.selectAll());
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findAllIds", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * store the store id of the stores
   * @param stores
   * @return
   */
  private String[] getStoreIds(Store[] stores) {
    String[] ids = new String[stores.length];
    for (int i = 0; i < stores.length; i++)
      ids[i] = stores[i].getId();
    return ids;
  }
}

