/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//


package  com.chelseasystems.cs.dataaccess;

import  java.sql.SQLException;
import  com.chelseasystems.cs.armaniinterfaces.ArmStgEmployeeData;


public interface ArmStgEmployeeDAO extends BaseDAO
{

  /**
   * @return
   */
  public ArmStgEmployeeData[] getNewArmStgEmpData ();
}



