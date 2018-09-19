/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04-17-2005 | Pankaja   | N/A       |Specs Presale/Consignment impl                      |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */

package com.chelseasystems.cs.dataaccess;

import com.chelseasystems.cr.database.*;
import java.sql.SQLException;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.pos.*;

public interface PosLineItemDetailDAO extends BaseDAO {

	/**
	 * @param returnTransactionId
	 * @param returnLineItemSeqNum
	 * @param returnLineItemDetailSeqNum
	 * @return
	 * @exception SQLException
	 */
	public ParametricStatement[] getUndoReturn(String returnTransactionId, int returnLineItemSeqNum, int returnLineItemDetailSeqNum) throws SQLException;

	/**
	 * @param saleDetail
	 * @param returnDetail
	 * @return
	 * @exception SQLException
	 */
	public ParametricStatement[] getUpdateReturningSaleLineItemDetailSQL(SaleLineItemDetail saleDetail, ReturnLineItemDetail returnDetail) throws SQLException;

	/**
	 * @param saleDetail
	 * @param returnDetail
	 * @exception SQLException
	 */
	public void updateReturningSaleLineItemDetail(SaleLineItemDetail saleDetail, ReturnLineItemDetail returnDetail) throws SQLException;

	/**
	 * @param preSaleDetail
	 * @param lineItemDetail
	 * @exception SQLException
	 */
	public void updateProcessedPreSaleLineItemDetail(CMSPresaleLineItemDetail preSaleDetail, POSLineItemDetail lineItemDetail) throws SQLException;

	/**
	 * @param preSaleDetail
	 * @param lineItemDetail
	 * @return
	 * @exception SQLException
	 */
	public ParametricStatement[] getUpdateProcessedPreSaleLineItemDetailSQL(CMSPresaleLineItemDetail preSaleDetail, POSLineItemDetail lineItemDetail) throws SQLException;

	/**
	 * @param csgnDetail
	 * @param lineItemDetail
	 * @exception SQLException
	 */
	public void updateProcessedConsignmentLineItemDetail(CMSConsignmentLineItemDetail csgnDetail, POSLineItemDetail lineItemDetail) throws SQLException;

	/**
	 * @param csgnDetail
	 * @param lineItemDetail
	 * @return
	 * @exception SQLException
	 */
	public ParametricStatement[] getUpdateProcessedConsignmentLineItemDetailSQL(CMSConsignmentLineItemDetail csgnDetail, POSLineItemDetail lineItemDetail) throws SQLException;

	/**
	 * @param posLnItmDtl
	 * @param preSaleDetail
	 * @return
	 * @exception SQLException
	 */
	public ParametricStatement[] getUndoPresale(POSLineItemDetail posLnItmDtl, CMSPresaleLineItemDetail preSaleDetail) throws SQLException;

	/**
	 * @param posLnItmDtl
	 * @param csgnDetail
	 * @return
	 * @exception SQLException
	 */
	public ParametricStatement[] getUndoConsignment(POSLineItemDetail posLnItmDtl, CMSConsignmentLineItemDetail csgnDetail) throws SQLException;

	/**
	 * @param posLnItmDtl
	 * @param rsvDetail
	 * @return
	 * @exception SQLException
	 */
	public ParametricStatement[] getUndoReservation(POSLineItemDetail posLnItmDtl, CMSReservationLineItemDetail rsvDetail) throws SQLException;

	/**
	 * @param resSaleDetail
	 * @param lineItemDetail
	 * @exception SQLException
	 */
	public void updateProcessedReservationLineItemDetail(CMSReservationLineItemDetail resSaleDetail, POSLineItemDetail lineItemDetail) throws SQLException;

	/**
	 * @param resSaleDetail
	 * @param lineItemDetail
	 * @return
	 * @exception SQLException
	 */
	public ParametricStatement[] getUpdateProcessedReservationLineItemDetailSQL(CMSReservationLineItemDetail resSaleDetail, POSLineItemDetail lineItemDetail) throws SQLException;
}
