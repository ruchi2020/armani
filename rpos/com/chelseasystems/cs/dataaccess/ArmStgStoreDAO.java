/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//


package  com.chelseasystems.cs.dataaccess;

import  com.chelseasystems.cs.dataaccess.BaseDAO;
import  java.sql.SQLException;
import  com.chelseasystems.cs.armaniinterfaces.ArmStgStoreData;


public interface ArmStgStoreDAO extends BaseDAO
{

  /**
   * @return
   */
  public ArmStgStoreData[] getNewArmStgStoreData ();
}



