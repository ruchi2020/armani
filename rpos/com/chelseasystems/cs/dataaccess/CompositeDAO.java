package  com.chelseasystems.cs.dataaccess;

import  com.chelseasystems.cr.transaction.*;
import  com.chelseasystems.cr.database.*;
import  java.sql.*;


public interface CompositeDAO extends BaseDAO 
{

  /**
   * Added by Satin to search digital signature based on transaction Id.
   * @param txnId String
   * @throws SQLException
   * @return String
   */
  public String selectDigitalSignature (String txnId) throws SQLException;
}



