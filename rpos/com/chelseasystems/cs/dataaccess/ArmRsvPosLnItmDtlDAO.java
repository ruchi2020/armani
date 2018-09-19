/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.dataaccess;

import  com.chelseasystems.cr.pos.POSLineItemDetail;
import  com.chelseasystems.cs.pos.CMSReservationLineItemDetail;
import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmRsvPosLnItmDtlOracleBean;
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
 * <p>Title:ArmPosRsvLnItmDtlDAO </p>
 * <p>Description:Used to Fetch/Persist Reservation line item detail </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
public interface ArmRsvPosLnItmDtlDAO extends BaseDAO {

	/**
	 * @param posLnItmDtl
	 * @param prsLnItmDtl
	 * @exception SQLException
	 */
	public void insert(POSLineItemDetail posLnItmDtl, CMSReservationLineItemDetail prsLnItmDtl) throws SQLException;

	/**
	 * @param posLnItmDtl
	 * @param rsvLnItmDtl
	 * @return
	 * @exception SQLException
	 */
	public ParametricStatement[] getInsertSQL(POSLineItemDetail posLnItmDtl, CMSReservationLineItemDetail rsvLnItmDtl) throws SQLException;

	/**
	 * @param transactionId
	 * @param lineItemSeqNum
	 * @param lineItemDetailSeqNum
	 * @return
	 * @exception SQLException
	 */
	public ArmRsvPosLnItmDtlOracleBean getReservationLineItemDetail(String transactionId, int lineItemSeqNum, int lineItemDetailSeqNum) throws SQLException;

	/**
	 * @param transactionId
	 * @param lineItemSeqNum
	 * @param lineItemDetailSeqNum
	 * @return
	 * @exception SQLException
	 */
	public ArmRsvPosLnItmDtlOracleBean getProcessedToLineItemDetail(String transactionId, int lineItemSeqNum, int lineItemDetailSeqNum) throws SQLException;

	/**
	 * put your documentation comment here
	 * 
	 * @param transactionId
	 * @param lineItemSeqNum
	 * @param lineItemDetailSeqNum
	 * @return
	 * @exception SQLException
	 */
	public ParametricStatement getUpdateVoidSQL(String transactionId, int lineItemSeqNum, int lineItemDetailSeqNum) throws SQLException;
}
