/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.dataaccess;

import  java.sql.SQLException;
import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cs.fiscaldocument.FiscalDocument;


public interface ArmFiscalDocumentDAO extends BaseDAO
{

  /**
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (FiscalDocument object) throws SQLException;



  /**
   * @param sqls
   * @exception SQLException
   */
  public void execute (ParametricStatement[] sqls) throws SQLException;
}



