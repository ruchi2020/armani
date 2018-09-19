/**Copyright 2001,chelseamarketsystem
 *
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

import java.rmi.*;
import java.util.HashMap;

import com.igray.naming.*;
import com.chelseasystems.cr.register.*;

import java.lang.*;

import com.chelseasystems.cr.store.*;
import com.chelseasystems.cs.store.*;


/**
 *Defines the customer services that are available remotely via RMI.
 **/
/**
 *
 * <p>Title: ICMSRegisterRMIServer</p>
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
public interface ICMSRegisterRMIServer extends Remote, IPing {

  /**
   * This method is used to get the next register
   *@parm store store
   **/
  public CMSRegister getNextRegister(CMSStore store)
      throws RemoteException;


  /**
   * This method is used to update a register
   * @param register register
   */
  public void updateRegister(CMSRegister register)
      throws RemoteException;


  /**
   * This method is used to select by a RegisterID & StoreID
   * @param register String
   * @param store String
   * @return CMSRegister
   * @throws RemoteException
   */
  public CMSRegister selectByStoreAndRegID(String register, String store)
      throws RemoteException;


  /**
   * This method is used to get all   Registers by  StoreID
   * @param store String
   * @return CMSRegister[]
   * @throws RemoteException
   */
  public CMSRegister[] selectByStoreID(String store)
      throws RemoteException;
  
  
  /**
   * Vishal Yevale : Dec 2017 : POS_TS_NF25 Implementation PCR (FRANCE)
   * @param txnObject (CMSTransactionSOS OR CMSTransactionEOD)
   * @return MAP (Calculated  netAmount of Month and Year (if end of year)
   * @throws Exception
   */
  public  HashMap submitAndReturnNetAmount(Object txnObject) throws RemoteException;

}

