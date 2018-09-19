/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.dataaccess;

import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmPosRsvOracleBean;
import  com.chelseasystems.cs.pos.ReservationTransaction;
import  java.sql.SQLException;


/*History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 07-21-2005 | Manpreet  | N/A       | Specs Reservation impl                       |
 +------+------------+-----------+-----------+----------------------------------------------+
 */
/**
 *
 * <p>Title: ArmPosRsvDAO</p>
 * <p>Description: Base DAO for persiisting Reservation Txn</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
public interface ArmPosRsvDAO extends BaseDAO
{

  /**
   * @param object
   * @exception SQLException
   */
  public void insert (ReservationTransaction object) throws SQLException;



  /**
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (ReservationTransaction object) throws SQLException;



  /**
   * @param txnId
   * @return
   * @exception SQLException
   */
  public ArmPosRsvOracleBean getReservationTransaction (String txnId) throws SQLException;


  public ReservationTransaction updateExpirationDt(ReservationTransaction object) throws SQLException;
}



